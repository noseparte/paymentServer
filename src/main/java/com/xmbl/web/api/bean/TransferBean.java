package com.xmbl.web.api.bean;

import lombok.Data;

/**
 * 
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 * @Author Noseparte
 * @Compile 2018年8月1日 -- 下午5:28:48
 * @Version 1.0
 * @Description		转账信息DTO TransferBean
 */
@Data
public class TransferBean {

	private Long playerId; 					//用户ID	
	private String accountId; 				//账号ID	
	private Long orderNo;					//订单编号,流水号
	private int ServerId;					//服务器ID
	private Float Amount;					//提现金额
	private int payeeType;					//提现方式   1：支付宝  2：微信
	private String payeeAccount;			//支付宝账号
	private String payeeRealName;			//真实姓名
	private String PassWord;				//登录密码
	
	public TransferBean() {
		super();
	}
	
	public TransferBean(Long playerId, String accountId, Long orderNo, int serverId, Float amount, int payeeType,
			String payeeAccount, String payeeRealName, String passWord) {
		super();
		this.playerId = playerId;
		this.accountId = accountId;
		this.orderNo = orderNo;
		ServerId = serverId;
		Amount = amount;
		this.payeeType = payeeType;
		this.payeeAccount = payeeAccount;
		this.payeeRealName = payeeRealName;
		PassWord = passWord;
	}


}
