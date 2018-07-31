package com.xmbl.model;

import java.util.Date;

import com.xmbl.mongo.pojo.GeneralBean;

import lombok.Data;

/**
 * Copyright © 2017 noseparte(Libra) © Like the wind, like rain
 * @Author Noseparte
 * @Compile 2017年11月3日 -- 下午3:17:40
 * @Version 1.0
 * @Description	提现记录
 */
@Data
public class TransferRecord extends GeneralBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long playerId; 					//用户ID	
	private String accountId; 				//账号ID	
	private Long orderNo;					//订单编号,流水号
	private int ServerId;					//服务器ID
	private Float Amount;					//提现金额
	private int payeeType;					//提现方式   1：支付宝  2：微信
	private String payeeAccount;			//支付宝账号
	private String payeeRealName;			//真实姓名
	private Date tansferTime;				//到账时间
	private int orderState;					//订单状态
	private String remark;					//备注
	
	public TransferRecord() {
		super();
	}

	public TransferRecord(Long playerId, String accountId, Long orderNo, int serverId, Float amount, int payeeType,
			String payeeAccount, String payeeRealName, int orderState, String remark) {
		super();
		this.playerId = playerId;
		this.accountId = accountId;
		this.orderNo = orderNo;
		ServerId = serverId;
		Amount = amount;
		this.payeeType = payeeType;
		this.payeeAccount = payeeAccount;
		this.payeeRealName = payeeRealName;
		this.orderState = orderState;
		this.remark = remark;
	}

	
	
	
	
}
