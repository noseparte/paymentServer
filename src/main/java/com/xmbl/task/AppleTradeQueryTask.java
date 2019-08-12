package com.xmbl.task;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.xmbl.constant.GoodsInfoConstant;
import com.xmbl.enumeration.EnumInfrastructureResCode;
import com.xmbl.model.RechargeRecord;
import com.xmbl.service.pay.RechargeRecordService;
import com.xmbl.util.xiaomiUtil.XiaomiGoodsInfoUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 * @Author Noseparte
 * @Compile 2019年1月2日 -- 下午9:02:40
 * @Version 1.0
 * @Description		未到账订单轮询	
 */
@Slf4j
@Configuration
public class AppleTradeQueryTask {
	
	@Autowired
	private RechargeRecordService rechargeRecordService;	 
	
	@Scheduled(cron = "0 0/1 * * * ? ")
	private void tradeAppleQuery() {
		List<RechargeRecord> bills = rechargeRecordService.findAppleUnAccountingOrder();
		for(RechargeRecord record : bills) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("PlayerId", record.getPlayerId());
			jsonObj.put("AccountId", record.getAccountId());
			jsonObj.put("userKey", record.getUserKey());
			jsonObj.put("goodID", record.getGoodID());
			jsonObj.put("goodNum", 1);
			String params = jsonObj.toJSONString();
			// Step2. 通过产品id 获取商品信息
			log.info("通过产品id获取如下信息开始:产品id:" + record.getGoodID());
			Map<String, String> goodMap = XiaomiGoodsInfoUtil.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst,
					"id", record.getGoodID());
			log.info("通过产品id获取如下信息:" + goodMap.toString());
			Assert.isTrue(goodMap.size() > 0, EnumInfrastructureResCode.OBTAIN_PRODUCT_INFO_FAILURE.code());
			String Amount = String.valueOf(Double.parseDouble(goodMap.get("cost")));
			// 账变，修改状态，到账提醒
			rechargeRecordService.updatAppleBill(params, Amount, record.getOriginalOrderId(), record.getOrderNo());
		}
	}

	@Scheduled(cron = "0 0/1 * * * ? ")
	private void tradeGoogleQuery() {
		List<RechargeRecord> bills = rechargeRecordService.findGoogleUnAccountingOrder();
		for(RechargeRecord record : bills) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("PlayerId", record.getPlayerId());
			jsonObj.put("AccountId", record.getAccountId());
			jsonObj.put("userKey", record.getUserKey());
			jsonObj.put("goodID", record.getGoodID());
			jsonObj.put("goodNum", 1);
			String params = jsonObj.toJSONString();
			// Step2. 通过产品id 获取商品信息
			log.info("通过产品id获取如下信息开始:产品id:" + record.getGoodID());
			Map<String, String> goodMap = XiaomiGoodsInfoUtil.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst,
					"id", record.getGoodID());
			log.info("通过产品id获取如下信息:" + goodMap.toString());
			Assert.isTrue(goodMap.size() > 0, EnumInfrastructureResCode.OBTAIN_PRODUCT_INFO_FAILURE.code());
			String Amount = String.valueOf(Double.parseDouble(goodMap.get("cost")));
			// 账变，修改状态，到账提醒
			rechargeRecordService.updateGoogleBill(params, Amount, record.getOriginalOrderId(), record.getOrderNo());
		}
	}
	
}
