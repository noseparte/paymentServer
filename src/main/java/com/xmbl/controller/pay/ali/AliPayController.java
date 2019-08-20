package com.xmbl.controller.pay.ali;

import java.util.Date;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.xmbl.base.BaseController;
import com.xmbl.config.AlipayConfig;
import com.xmbl.constant.CommonConstant;
import com.xmbl.constant.GoodsInfoConstant;
import com.xmbl.dto.ResponseResult;
import com.xmbl.enumeration.EnumInfrastructureResCode;
import com.xmbl.model.ServiceRepository;
import com.xmbl.model.ThirdPayBean;
import com.xmbl.service.ServiceRepesitoryService;
import com.xmbl.service.pay.RechargeRecordService;
import com.xmbl.service.pay.ThirdPayService;
import com.xmbl.service.pay.TransferRecordService;
import com.xmbl.util.DateUtils;
import com.xmbl.util.OrderGeneratedUtils;
import com.xmbl.util.xiaomiUtil.XiaomiGoodsInfoUtil;
import com.xmbl.web.api.bean.Route;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 *
 * @Author Noseparte
 * @Compile 2018年7月2日 -- 下午4:07:36
 * @Version 1.0
 * @Description
 */
@Slf4j
@Controller
@RequestMapping(value = Route.PATH + Route.Payment.PATH)
public class AliPayController extends BaseController {

	@Autowired
	private ThirdPayService thirdPayService;
	@Autowired
	private RechargeRecordService rechargeRecordService;
	@Autowired
	private TransferRecordService transferRecordService;
	@Autowired
	private ServiceRepesitoryService ServiceRepesitoryService;

	/**
	 * 支付宝请求交易
	 * 
	 * @param jsonData
	 * @return
	 */
	@RequestMapping(value = Route.Payment.ALI_PAY, method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult ali_pay(HttpServletRequest request, String jsonData) {
		log.info("infoMsg:================ 支付宝统一下单请求交易开始 ==================== jsonData,{}",
				JSON.toJSONString(jsonData));
		String body = "";
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("serviceId").is("7"));
			query.addCriteria(Criteria.where("serviceName").is("recharge"));
			ServiceRepository service = ServiceRepesitoryService.findOneByQuery(query);
			if(service.getStatus() == 1) {
				return errorJson("充值功能维护中,请稍后再试.");
			}
			// single.ec 验证支付请求参数 不为空
			Assert.isTrue(jsonData != null, EnumInfrastructureResCode.PARAMETER_FORMAT_ERROR.code());
			// Step1. 解析客户端的请求的订单信息
			JSONObject orderObj = JSON.parseObject(jsonData);
			Long PlayerId = orderObj.getLong("PlayerId");
			String AccountId = orderObj.getString("AccountId");
			String userKey = orderObj.getString("userKey");
			String goodID = orderObj.getString("goodID");
			int goodNum = orderObj.getInteger("goodNum");
			if(String.valueOf(goodNum).equals("") || String.valueOf(goodNum) == null) {
				goodNum = 1;
			}

			// Step2. 通过产品id 获取商品信息
			log.info("通过产品id获取如下信息开始:产品id:" + goodID);
			Map<String, String> goodMap = XiaomiGoodsInfoUtil.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst,
					"id", goodID);
			log.info("通过产品id获取如下信息:" + goodMap.toString());
			Assert.isTrue(goodMap.size() > 0, EnumInfrastructureResCode.OBTAIN_PRODUCT_INFO_FAILURE.code());
			String goodName = goodMap.get("notice");
			String Amount = String.valueOf(Double.parseDouble(goodMap.get("cost"))*goodNum);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("PlayerId", PlayerId);
			jsonObj.put("AccountId", AccountId);
			jsonObj.put("userKey", userKey);
			jsonObj.put("goodID", goodID);
			jsonObj.put("goodNum", goodNum);
			String params = jsonObj.toJSONString();

			// Step3. 准备步骤： 提供要参与签名和要加密的参数
			String outTradeNo = OrderGeneratedUtils.getOrderNo(); // 订单号
			// HttpCilent 支付宝封装的SDK,提供签名的加密等环节
			AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
					AlipayConfig.APP_PRIVATE_KEY, AlipayConfig.format, AlipayConfig.charset,
					AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.sign_type);
			AlipayTradeAppPayRequest payRequest = new AlipayTradeAppPayRequest();
			// 订单的数据 model
			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
			model.setBody(goodName + "*" + goodNum);
			model.setSubject("方块创造");
			model.setOutTradeNo(outTradeNo);
			model.setTimeoutExpress("60m");
			model.setTotalAmount(Amount);
			model.setProductCode("QUICK_MSECURITY_PAY");
			model.setPassbackParams(params);
			payRequest.setBizModel(model);
			payRequest.setNotifyUrl(AlipayConfig.NOTIFY_URL);
			log.info("infoMsg:================ 支付宝统一下单提交的签名 ==================== model,{}", JSON.toJSONString(model));

			// Step4. MD5加密方式，向支付宝服务器发送的请求参数
			AlipayTradeAppPayResponse response = alipayClient.sdkExecute(payRequest);
			log.info("infoMsg:================ 支付宝充值服务器返回的信息 ==================== response,{}",
					JSON.toJSONString(response));

			if (response.isSuccess()) {
				log.info("infoMsg:================ 支付宝预付单下单成功!,orderTime,{}",
						DateUtils.formatDate(new Date(), "YYYY:MM:dd HH:mm:ss"));
				body = response.getBody();
				log.info("返回客户端的body,{}=========================", body);
				// Step4. 生成支付宝充值订单
				boolean result = rechargeRecordService.generatedAliPayBills(params, Amount, outTradeNo);
				if (result) {
					log.info("infoMsg:================ 平台订单生成成功!,orderTime,{}",
							DateUtils.formatDate(new Date(), "YYYY:MM:dd HH:mm:ss"));
					return successJson(body);
				}
				log.info("infoMsg:================ 平台订单生成失败!,orderTime,{}",
						DateUtils.formatDate(new Date(), "YYYY:MM:dd HH:mm:ss"));
			} else {
				String subMsg = response.getSubMsg();
				return errorJson(subMsg);
			}
			log.info("infoMsg:================ 支付宝统一下单请求交易结束 ====================");
			return successJson(body);
		} catch (Exception e) {
			log.error("errorMsg:{================ 支付宝统一下单请求交易失败:" + e.getMessage() + "====================}");
			return errorJson(e.getMessage());
		}
	}

	/**
	 * 支付宝支付通知地址
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = Route.Payment.ALI_PAY_NOTIFY, method = RequestMethod.POST)
	@ResponseBody
	public String alipay_notify(HttpServletRequest request) {
		log.info("infoMsg: =========支付宝验证异步通知信息开始,notifyTime,{}",
				DateUtils.formatDate(new Date(), "YYYY:MM:dd HH:mm:ss"));
		String message = "";
		try {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
				params.put(name, valueStr);
			}
			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
            String trade_no = params.get("trade_no");
			// 商户订单号
			String out_trade_no = params.get("out_trade_no");
			// 订单金额
			String total_amount = params.get("total_amount");
			// 回传参数
			String passback_params = params.get("passback_params");
			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			// 计算得出通知验证结果
			// boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String
			// publicKey, String charset, String sign_type)
			boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY,
					AlipayConfig.charset, "RSA2");
			if (verify_result) {// 验证成功
				log.info("infoMsg:================ 支付宝订单验证签名成功 ====================");
				// 账变，修改状态，到账提醒
				boolean result = rechargeRecordService.updateAliPayBill(passback_params, total_amount, trade_no ,out_trade_no);
				if (result) {
					log.info("infoMsg:================ 支付宝订单更新成功 ====================");
					message = "success";
				} else {
					log.info("infoMsg:================ 支付宝订单更新失败 ====================");
					message = "failure";
				}
			}
			log.info("infoMsg:=============== 支付宝验证异步通知信息结束,message,{}",message);
			return message;
		} catch (Exception e) {
			log.error("errorMsg:{================ 微信异步通知发生异常,异常信息为 msg,{}: ", e.getMessage() + "====================}");
			return "failure";
		}

	}

	/**
	 * 支付宝单笔提现
	 * 
	 * @param //Id
	 *            //流水ID
	 * @param //PlayerId
	 *            //用户ID
	 * @param //AccountId
	 *            //账号ID
	 * @param //ServerId
	 *            //服务器ID
	 * @param //Amount
	 *            //提现金额
	 * @param //PayeeType
	 *            //提现类型 1：支付宝 2：微信
	 * @param //PayeeAccount
	 *            //支付宝账号
	 * @param //PayeeRealName
	 *            //真实姓名
	 * @param //PassWord
	 *            //用户密码
	 * @return http://www.ugcapp.com:8087/paymentServer/api/pay/ali_transfer
	 */
	@RequestMapping(value = Route.Payment.ALI_TRANSFER, method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String transfer(@RequestBody String transferInfo) {
		log.info("infoMsg:--- 用户提现申请开始。======================"
				+ DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss"));
		JSONObject transferResult = new JSONObject();
		String order_id = "";
		try {
			log.info("infoMsg:--- Game服的请求参数 transferInfo,{}======================", JSON.toJSONString(transferInfo));
			// 解析Game服的请求参数 transferInfo
			JSONObject gamePlayerWithDrawalObj = JSON.parseObject(transferInfo);
			Long Id = gamePlayerWithDrawalObj.getLong("Id"); // 流水ID
			Long PlayerId = gamePlayerWithDrawalObj.getLong("PlayerId"); // 用户ID
			String AccountId = gamePlayerWithDrawalObj.getString("AccountId"); // 账号ID
			int ServerId = gamePlayerWithDrawalObj.getInteger("ServerId"); // 服务器ID
			Float Amount = gamePlayerWithDrawalObj.getFloat("Amount"); // 提现金额
			int PayeeType = gamePlayerWithDrawalObj.getInteger("PayeeType"); // 提现类型 1：支付宝 2：微信
			String PayeeAccount = gamePlayerWithDrawalObj.getString("PayeeAccount"); // 支付宝账号
			String PayeeRealName = gamePlayerWithDrawalObj.getString("PayeeRealName"); // 真实姓名
			String PassWord = gamePlayerWithDrawalObj.getString("PassWord"); // 用户密码
			// 判断用户的支付密码是否正确
			// boolean validatePwd = appUserService.verifyPassword(AccountId,PassWord);
			// if(!validatePwd) {
			// // 发送服务器的参数
			// JSONObject Obj = new JSONObject();
			// Obj.put("Id", Id);
			// Obj.put("PlayerId", PlayerId);
			// Obj.put("AccountId", AccountId);
			// Obj.put("ServerId", ServerId);
			// Obj.put("Amount", Amount);
			// Obj.put("PayeeType", PayeeType);
			// Obj.put("PayeeAccount", PayeeAccount);
			// Obj.put("PayeeRealName", PayeeRealName);
			// Obj.put("PassWord", PassWord);
			// Obj.put("Result", 11001);
			// log.error("errorMsg:--- 提现用户输入的支付密码不匹配 Obj,{}======================" ,
			// JSON.toJSONString(Obj));
			// return JSON.toJSONString(Obj);
			// }
			// paymentServer端 保存提现流水
			transferResult.put("Id", Id);
			transferResult.put("PlayerId", PlayerId);
			transferResult.put("AccountId", AccountId);
			transferResult.put("ServerId", ServerId);
			transferResult.put("Amount", Amount);
			transferResult.put("PayeeType", PayeeType);
			transferResult.put("PayeeAccount", PayeeAccount);
			transferResult.put("PayeeRealName", PayeeRealName);
			transferResult.put("PassWord", PassWord);
			
			Query query = new Query();
			query.addCriteria(Criteria.where("serviceId").is("7"));
			query.addCriteria(Criteria.where("serviceName").is("transfer"));
			ServiceRepository service = ServiceRepesitoryService.findOneByQuery(query);
			if(service.getStatus() == 1) {
				transferResult.put("Result", 11004);
				return JSON.toJSONString(transferResult);
			}
			
			String remark = "方块创造于" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss") + ",申请提现" + Amount + "元"
					+ "流水号为：" + Id;
			boolean result = transferRecordService.generatedBills(PlayerId, AccountId, Id, ServerId, Amount,
					CommonConstant.ALIPAY_WAYTD, PayeeAccount, PayeeRealName, CommonConstant.Unusual, remark);
			if (!result) {
				log.error("订单生成失败");
			}

			// ----------------请求参数------------------//
			ThirdPayBean payBean = thirdPayService.findByPayId(CommonConstant.ALIPAY_WAYTD);
			Assert.isTrue(payBean != null, EnumInfrastructureResCode.PLATFORM_INFO_NOT_NULL.code());
			// String APP_ID = payBean.getMer_no(); //支付宝商戶号
			// String mer_key = payBean.getMer_key(); //支付宝公钥|商户私钥

			// 实例化客户端
			AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
					AlipayConfig.APP_PRIVATE_KEY, AlipayConfig.format, AlipayConfig.charset,
					AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.sign_type);
			AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
			AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
			model.setOutBizNo(String.valueOf(Id));
			model.setPayeeType("ALIPAY_LOGONID");
			model.setPayeeAccount(PayeeAccount);
			model.setPayeeRealName(PayeeRealName);
			model.setAmount(String.valueOf(Amount));
			model.setPayerShowName("方块创造");
			model.setRemark(remark);
			request.setBizModel(model);
			log.info("=================此次提现申请的具体信息为：-------------" + JSON.toJSONString(model.toString()));
			AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
			/**
			 * 1. 如果商户重复请求转账，支付宝会幂等返回成功结果，商户必须对重复转账的业务做好幂等处理；如果不判断，存在潜在的风险，商户自行承担因此而产生的所有损失。
			 * 2. 如果调用alipay.fund.trans.toaccount.transfer掉单时，
			 * 或返回结果code=20000时，或返回结果code=40004，sub_code= SYSTEM_ERROR时，
			 * 请调用alipay.fund.trans.order.query发起查询，如果未查询到结果，请保持原请求不变再次请求alipay.fund.trans.toaccount.transfer接口。
			 * 3. 商户处理转账结果时，对于错误码的处理，只能使用sub_code作为后续处理的判断依据，不可使用sub_msg作为后续处理的判断依据。
			 */
			if (response.isSuccess()) {
				order_id = response.getOrderId();
				log.info("infoMsg:============== 支付宝单笔提现接口调用成功 ===============,order_id,{}", order_id);
				if (response.getCode().equals("10000")) {
					// paymentServer执行更改订单状态操作
					log.info("infoMsg:============== paymentServer执行更改订单状态操作. ===============");
					// 生成账单
					String transferRemark = "方块创造于" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss") + ",提现成功"
							+ Amount + "元" + "流水号为：" + Id;
					boolean updateState = transferRecordService.updateBill(String.valueOf(Id), transferRemark);
					if (updateState) {
						// 发送服务器的参数
						transferResult.put("Result", 1);
						log.info("infoMsg:--- 用户提现申请成功。 transferResult,{}======================",
								JSON.toJSONString(transferResult));
					}
				}
			} else if (response.getCode().equals("20000") || response.getCode().equals("40004")
					|| response.getSubCode().equals("SYSTEM_ERROR")) {
				AlipayClient alipayQueryClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
						AlipayConfig.APP_PRIVATE_KEY, AlipayConfig.format, AlipayConfig.charset,
						AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.sign_type);
				AlipayFundTransOrderQueryRequest requestQuery = new AlipayFundTransOrderQueryRequest();
				StringBuffer buffer = new StringBuffer();
				buffer.append("{");
				buffer.append("\"out_biz_no\":").append(Id).append(",");
				buffer.append("\"order_id\":").append(order_id);
				buffer.append("}");
				requestQuery.setBizContent(buffer.toString());
				AlipayFundTransOrderQueryResponse responseQuery = alipayQueryClient.execute(requestQuery);
				if (responseQuery.isSuccess()) {
					log.info("infoMsg:============== 支付宝单笔提现查询接口调用成功 ===============");
					// 发送服务器的参数
					if (responseQuery.getCode().equals("10000")) {
						// 生成账单
						String transferRemark = "方块创造于" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss")
								+ ",提现失败。失败原因为：" + responseQuery.getFailReason() + "提现金额为：" + Amount + "元" + "流水号为："
								+ Id;
						boolean updateState = transferRecordService.updateBill(String.valueOf(Id), transferRemark);
						if (updateState) {
							transferResult.put("Result", 11002);
							log.error("errorMsg:--- 用户提现申请失败。 transferResult,{}======================",
									JSON.toJSONString(transferResult));
						}
					}
				} else {
					log.error("errorMsg:--- 用户提现申请失败。 transferResult,{}======================",
							JSON.toJSONString(transferResult));
					transferResult.put("Result", 11002);
				}
			} else if (response.getSubCode().equals("PAYEE_USER_INFO_ERROR")) {
				// 生成账单
				String transferRemark = "方块创造于" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss")
						+ ",提现失败。失败原因为：" + "支付宝账号和姓名不匹配，请确认姓名是否正确" + "提现金额为：" + Amount + "元" + "流水号为：" + Id;
				boolean updateState = transferRecordService.updateBill(String.valueOf(Id), transferRemark);
				if (updateState) {
					transferResult.put("Result", 11002);
					log.error("errorMsg:--- 用户提现申请失败。 transferResult,{}======================",
							JSON.toJSONString(transferResult));
				}
			}
			log.info("infoMsg:--- 用户提现申请结果。 transferResult,{}======================",
					JSON.toJSONString(transferResult));
			return JSON.toJSONString(transferResult);
		} catch (Exception e) {
			log.error("errorMsg:{======== 用户申请提现发生异常:======================= " + e.getMessage() + "---}");
			transferResult.put("Result", 11003);
			return JSON.toJSONString(transferResult);
		}
	}

}
