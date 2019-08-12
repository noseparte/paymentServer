package com.xmbl.model;

import java.util.Date;

import com.xmbl.mongo.pojo.GeneralBean;

import lombok.Data;

/**
 * Copyright © 2017 noseparte(Libra) © Like the wind, like rain
 * 
 * @Author Noseparte
 * @Compile 2017年10月2日 -- 下午11:34:27
 * @Version 1.0
 * @Description 充值记录
 */
@Data
public class RechargeRecord extends GeneralBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long playerId; // 用户ID
	private String accountId; // 账号ID
	private String userKey; // 登录名
	private String originalOrderId; // 服务商订单号
	private String orderNo; // 订单编号,流水号
	private int orderType; // 订单类型 1.支付宝 | 2.微信  | 3.Apple Pay | 4.Google Play
	private String goodID; // 道具ID
	private int goodNum; // 道具ID
	private String goodName; // 道具名称
	private String Amount; // 充值金额
	private Date orderTime; // 下单时间
	private Date orderAccountingTime; // 到账时间
	private int orderState; // 订单状态 0:已到账 | 1:未到账
	private String remark; // 备注


	public RechargeRecord(Long playerId, String accountId, String userKey, String originalOrderId, String orderNo, int orderType, String goodID,int goodNum,
			String goodName, String amount, Date orderTime, int orderState, String remark) {
		super();
		this.playerId = playerId;
		this.accountId = accountId;
		this.userKey = userKey;
		this.originalOrderId = originalOrderId;
		this.orderNo = orderNo;
		this.orderType = orderType;
		this.goodID = goodID;
		this.goodNum = goodNum;
		this.goodName = goodName;
		this.Amount = amount;
		this.orderTime = orderTime;
		this.orderState = orderState;
		this.remark = remark;
	}

	public RechargeRecord(Long playerId, String accountId, String userKey, String outTradeNo, String goodID, String goodName, String amount, Date date, int unusual, String remark) {
	}
}
