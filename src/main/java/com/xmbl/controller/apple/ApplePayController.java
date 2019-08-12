package com.xmbl.controller.apple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xmbl.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmbl.base.BaseController;
import com.xmbl.constant.GoodsInfoConstant;
import com.xmbl.dto.ResponseResult;
import com.xmbl.enumeration.EnumInfrastructureResCode;
import com.xmbl.service.pay.RechargeRecordService;
import com.xmbl.util.DateUtils;
import com.xmbl.util.OrderGeneratedUtils;
import com.xmbl.util.xiaomiUtil.XiaomiGoodsInfoUtil;
import com.xmbl.web.api.bean.Route;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 * 
 * @Author Noseparte
 * @Compile 2019年1月2日 -- 下午6:06:12
 * @Version 1.0
 * @Description 苹果IOS内购
 */
@Slf4j
@RestController
@RequestMapping(Route.PATH + Route.Payment.PATH)
public class ApplePayController extends BaseController{

	/**
	 * 提交此JSON对象作为HTTP POST请求的有效内容。
	 * 在测试环境中，https://sandbox.itunes.apple.com/verifyReceipt用作URL。
	 * 在生产中，https://buy.itunes.apple.com/verifyReceipt用作URL。
	 */
	private static final String APP_STORE_PRIVATE_RECEIPT = "https://sandbox.itunes.apple.com/verifyReceipt"; // 苹果的测试服
	private static final String APP_STORE_PUBLIC_RECEIPT = "https://buy.itunes.apple.com/verifyReceipt"; // 苹果的生产服

	@Autowired
	private RechargeRecordService rechargeRecordService;

	/**
	 * 验证IOS内购订单
	 * 
	 * @param /*productIdentifier*
	 *            产品标识符
	 * @param /*state*
	 *            交易状态 Purchased 购买成功 | Restored 恢复购买 | Failed 失败 | Deferred
	 *            等待确认，儿童模式需要询问家长同意
	 * @param /*receipt*
	 *            二次验证的重要依据,验签
	 * @param /*transactionIdentifier*
	 *            交易标识符
	 * @return
	 */
	@PostMapping(value = Route.Payment.VERIFY_APPLE_ORDER)
	public ResponseResult verify_order(HttpServletRequest request, String jsonData) {
		log.info("infoMsg:--- 验证IOS内购订单开始 ==================== jsonData,{}", JSON.toJSONString(jsonData));
		int status = 0;
		try {
			// single.ec 验证支付请求参数 不为空
			Assert.isTrue(jsonData != null, EnumInfrastructureResCode.PARAMETER_FORMAT_ERROR.code());
			// Step1. 解析客户端的请求的订单信息
			JSONObject orderObj = JSON.parseObject(jsonData);
			Long PlayerId = orderObj.getLong("PlayerId");
			String AccountId = orderObj.getString("AccountId");
			String userkey = orderObj.getString("userkey");
			String receipt = orderObj.getString("receipt");
			
			String sendPost = sendPost(APP_STORE_PUBLIC_RECEIPT, "{\"receipt-data\":\"" + receipt + "\"}");
			log.info("苹果内购订单返回值,reciveMsg,{}", sendPost.toString());

			JSONObject object = JSON.parseObject(sendPost);
			status = (int) object.get("status");
			if (status == 0) {
				String response = object.getString("receipt");
				JSONObject obj = JSON.parseObject(response);
				String product_id = (String) obj.getString("product_id");
				String originalOrderId = (String) obj.getString("original_transaction_id");
				// 检测是否有重复订单
				if(!rechargeRecordService.findRepeatOrderByOriginalOrderId(originalOrderId,CommonConstant.APPLE_WAYTD)){
					return errorJson(EnumInfrastructureResCode.ORDER_HAS_BEEN_SHIPPED.value(),EnumInfrastructureResCode.ORDER_HAS_BEEN_SHIPPED.code());
				}
				Assert.isTrue(product_id.substring(0, 18).equals("com.boluogame.fkcz"), EnumInfrastructureResCode.APPLE_PAY_INFO_NOT_NULL.code());
				if (product_id.substring(0, 18).equals("com.boluogame.fkcz")) {
					String goodID = product_id.substring(20);
					// Step2. 通过产品id 获取商品信息
					log.info("通过产品id获取如下信息开始:产品id:" + goodID);
					Map<String, String> goodMap = XiaomiGoodsInfoUtil
							.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst, "id", goodID);
					log.info("通过产品id获取如下信息:" + goodMap.toString());
					Assert.isTrue(goodMap.size() > 0, EnumInfrastructureResCode.OBTAIN_PRODUCT_INFO_FAILURE.code());
					String Amount = String.valueOf(Double.parseDouble(goodMap.get("cost")));
					// Step3. 准备步骤： 提供要参与签名和要加密的参数
					String outTradeNo = OrderGeneratedUtils.getOrderNo(); // 订单号

					JSONObject jsonObj = new JSONObject();
					jsonObj.put("PlayerId", PlayerId);
					jsonObj.put("AccountId", AccountId);
					jsonObj.put("userKey", userkey);
					jsonObj.put("goodID", goodID);
					jsonObj.put("goodNum", 1);
					String params = jsonObj.toJSONString();
					// 充值记录
					// Step4. 生成支付宝充值订单
					rechargeRecordService.generatedAppleBills(params, Amount, originalOrderId , outTradeNo);
					// 账变，修改状态，到账提醒
					boolean result = rechargeRecordService.updatAppleBill(params, Amount, originalOrderId,outTradeNo);
					if (result) {
						log.info("infoMsg:================ 平台订单生成成功!,orderTime,{}",
								DateUtils.formatDate(new Date(), "YYYY:MM:dd HH:mm:ss"));
						return successJson(status,null);
					} else {
						log.info("infoMsg:================ 平台订单生成失败!,orderTime,{}",
								DateUtils.formatDate(new Date(), "YYYY:MM:dd HH:mm:ss"));
						return errorJson(status,"");
					}
				}
			}else if(status == 21007){
				String sendPrivatePost = sendPost(APP_STORE_PRIVATE_RECEIPT, "{\"receipt-data\":\"" + receipt + "\"}");
				log.info("苹果内购沙箱环境订单返回值,reciveMsg,{}", sendPrivatePost.toString());
				JSONObject objectPrivate = JSON.parseObject(sendPrivatePost);
				int privateStatus = (int) objectPrivate.get("status");
				if (privateStatus == 0) {
					String response = objectPrivate.getString("receipt");
					Assert.isTrue(response != null, EnumInfrastructureResCode.APPLE_PAY_RESPONSE_NOT_NULL.code());
					JSONObject obj = JSON.parseObject(response);
					String product_id = (String) obj.getString("product_id");
					String privateOriginalOrderId = (String) obj.getString("original_transaction_id");
					// 检测是否有重复订单
					if(!rechargeRecordService.findRepeatOrderByOriginalOrderId(privateOriginalOrderId,CommonConstant.APPLE_WAYTD)){
						return errorJson(EnumInfrastructureResCode.ORDER_HAS_BEEN_SHIPPED.value(),EnumInfrastructureResCode.ORDER_HAS_BEEN_SHIPPED.code());
					}
					Assert.isTrue(product_id.substring(0, 18).equals("com.boluogame.fkcz"), EnumInfrastructureResCode.APPLE_PAY_INFO_NOT_NULL.code());
					if (product_id.substring(0, 18).equals("com.boluogame.fkcz")) {
						String goodID = product_id.substring(20);
						// Step2. 通过产品id 获取商品信息
						log.info("通过产品id获取如下信息开始:产品id:" + goodID);
						Map<String, String> goodMap = XiaomiGoodsInfoUtil
								.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst, "id", goodID);
						log.info("通过产品id获取如下信息:" + goodMap.toString());
						Assert.isTrue(goodMap.size() > 0, EnumInfrastructureResCode.OBTAIN_PRODUCT_INFO_FAILURE.code());
						String Amount = String.valueOf(Double.parseDouble(goodMap.get("cost")));
						// Step3. 准备步骤： 提供要参与签名和要加密的参数
						String outTradeNo = OrderGeneratedUtils.getOrderNo(); // 订单号
						
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("PlayerId", PlayerId);
						jsonObj.put("AccountId", AccountId);
						jsonObj.put("userKey", userkey);
						jsonObj.put("goodID", goodID);
						jsonObj.put("goodNum", 1);
						String params = jsonObj.toJSONString();
						// 充值记录
						// Step4. 生成支付宝充值订单
						rechargeRecordService.generatedAppleBills(params, Amount, privateOriginalOrderId, outTradeNo);
						// 账变，修改状态，到账提醒
//						boolean result = rechargeRecordService.updatAppleBill(params, Amount, outTradeNo);
//						if (result) {
							log.info("infoMsg:================ 平台订单生成成功!,orderTime,{}",
									DateUtils.formatDate(new Date(), "YYYY:MM:dd HH:mm:ss"));
							return successJson(status,null);
//						} else {
//							log.info("infoMsg:================ 平台订单生成失败!,orderTime,{}",
//									DateUtils.formatDate(new Date(), "YYYY:MM:dd HH:mm:ss"));
//							return successJson(String.valueOf(status));
//						}
					}
				}
			}else {
				return errorJson(status,"");
			}
			log.info("infoMsg:--- 验证IOS内购订单结束");
			return successJson(status,null);
		} catch (Exception e) {
			log.error("errorMsg:{--- 验证IOS内购订单结束:" + e.getMessage() + "---}");
			return errorJson(status,e.getMessage());
		}
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		StringBuilder sb = new StringBuilder();
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			sb = new StringBuilder();
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return sb.toString();
	}



}
