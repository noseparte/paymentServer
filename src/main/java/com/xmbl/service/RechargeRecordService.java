package com.xmbl.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.xmbl.constant.CommonConstant;
import com.xmbl.constant.GoodsInfoConstant;
import com.xmbl.dao.RechargeRecordDao;
import com.xmbl.enumeration.EnumGameServerResCode;
import com.xmbl.model.GameServers;
import com.xmbl.model.RechargeRecord;
import com.xmbl.util.DateUtils;
import com.xmbl.util.WXPayUtil;
import com.xmbl.util.platformSendYxServerUtil.PlatformSendYouXiServerUtil;
import com.xmbl.util.xiaomiUtil.XiaomiGoodsInfoUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class RechargeRecordService {

	@Autowired
	private RechargeRecordDao rechargeRecordDao;
	@Autowired
	private GameServersService gameServersService;

	/**
	 * 生成微信充值预付单
	 *  
	 * @param attach
	 * @param amount
	 * @param out_trade_no
	 * @return
	 */
	public boolean generatedBills(String attach, String amount, String outTradeNo) {
		try {
			log.info("infoMsg:=================== 生成微信充值预付单,attach,{},amount,{},out_trade_no,{}",attach,amount,outTradeNo);
			if(StringUtils.isNotBlank(attach) && !StringUtils.trim(attach).equals("")) {
				//附加数据attach ps:用户ID|账号ID|userKey|goodID
				String[] orderObj = attach.split("\\|");
				Long PlayerId = Long.parseLong(orderObj[0]);
				String AccountId = String.valueOf(orderObj[1]);
				String userKey = String.valueOf(orderObj[2]);
				String goodID = String.valueOf(orderObj[3]);
				// 通过产品id获取如下信息
				log.info("通过产品id获取如下信息开始:产品id:" + goodID);
				Map<String, String> map = XiaomiGoodsInfoUtil
						.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst,
								"id", goodID);  
				log.info("通过产品id获取如下信息:"+map.toString());
				Assert.isTrue(map.size() > 0, "通过产品id不能获取产品信息");
				String goodName = map.get("notice");
				String remark = "用户：" + PlayerId + "于：" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss") 
							+ "微信充值预付： " + amount +"元" + "订单号：" + outTradeNo;
				RechargeRecord rechargeRecord = new RechargeRecord(PlayerId, AccountId, userKey, outTradeNo, goodID, goodName, amount, new Date(), CommonConstant.Unusual, remark);
				rechargeRecordDao.save(rechargeRecord);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 微信订单账变，修改状态，到账提醒
	 * 
	 * @param attach
	 * @param returnAmount
	 * @param order_no
	 * @return
	 */
	public boolean updateBillWeiXin(String attach ,Double returnAmount, String ordeNo) {
		log.info("infoMsg:=================== 微信充值结果,attach,{},returnAmount,{},out_trade_no,{}",attach,returnAmount,ordeNo);
		String result = "";
		try {
			if(StringUtils.isNotBlank(attach) && !StringUtils.trim(attach).equals("")) {
				//附加数据attach ps:用户ID|账号ID|userKey|goodID|goodName|goodNum
				String[] orderObj = attach.split("\\|");
				Long PlayerId = Long.parseLong(orderObj[0]);
				String AccountId = String.valueOf(orderObj[1]);
				String userKey = String.valueOf(orderObj[2]);
				String goodID = String.valueOf(orderObj[3]);
				// 通过产品id获取如下信息
				log.info("通过产品id获取如下信息开始:产品id:" + goodID);
				Map<String, String> map = XiaomiGoodsInfoUtil
						.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst,
								"id", goodID);  
				log.info("通过产品id获取如下信息:"+map.toString());
				Assert.isTrue(map.size() > 0, "通过产品id不能获取产品信息");
				String remark = "用户：" + PlayerId + "于：" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss") 
				+ "微信充值到账： " + returnAmount +"元" + "订单号：" + ordeNo;
				// 向游戏服务器发送消息  账变,发送奖励邮件
				List<GameServers> gameServersLst = gameServersService.getServerListByTypeAndOperatorStatus("Match",1);
				Assert.isTrue(gameServersLst!=null && gameServersLst.size() >0 , "match服务不可用，联系管理员添加match服务配置");
				log.info("=================游戏match服务器列表信息:{}",JSONObject.toJSONString(gameServersLst));
				log.info("=================游戏match服务器第一个服务器信息:{}",JSONObject.toJSONString(gameServersLst.get(0)));
				// 发送的服务器地址
				StringBuilder rpcUrl = new StringBuilder();
				rpcUrl.append("http://");//
				rpcUrl.append(gameServersLst.get(0).getRpcIp())//
					.append(":").append(gameServersLst.get(0).getRpcPort());//
				rpcUrl.append("/Recharge");
				log.info("rpcUrl信息:{}", rpcUrl.toString());
				// 发送服务器的参数
				JSONObject Obj = new JSONObject();	
				Obj.put("Ticket", ordeNo);//订单票据--订单id
				Obj.put("PlayerId", PlayerId);//--玩家id
				Obj.put("RechargeId", goodID);//--商品ID
				String ObjInfo = Obj.toString();
				log.info("像Match服发送的奖励信息为：Ticket,{},PlayerId,{}，RechargeId,{}",ordeNo,PlayerId,goodID);
				String Result = PlatformSendYouXiServerUtil.send(rpcUrl.toString(), ObjInfo);
				if (StringUtils.isNotEmpty(Result)) {
					JSONObject resObj = JSONObject.parseObject(Result);
					result = resObj.getString("Result");
				}
				if(result.equals(EnumGameServerResCode.SUCCESS.value())) {
					log.info("游戏服务器充值成功: 充值时间 "+DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") + ",响应结果 result" +result.toString());
					rechargeRecordDao.updateBill(ordeNo,remark);
					return true;
				}else{
					log.info("游戏服务器充值失败: 充值时间 "+DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss") + ",响应结果 result" +result.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String successfully() {
		try {
			Map<String, String> data = new HashMap<String, String>();
			data.put("return_code", "SUCCESS");
			data.put("return_msg", "OK");
			return WXPayUtil.mapToXml(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String errorfully() {
		try {
			Map<String, String> data = new HashMap<String, String>();
			data.put("return_code", "FAILURE");
			data.put("return_msg", "error");
			return WXPayUtil.mapToXml(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
