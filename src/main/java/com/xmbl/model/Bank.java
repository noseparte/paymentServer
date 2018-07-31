package com.xmbl.model;

import com.xmbl.mongo.pojo.GeneralBean;

import lombok.Data;

/**
 * Copyright © 2017 noseparte(Libra) © Like the wind, like rain
 * @Author Noseparte
 * @Compile 2017年10月2日 -- 下午11:22:59
 * @Version 1.0
 * @Description		银行pojo
 */
@Data
public class Bank extends GeneralBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int bank_id;		//银行id
	private String bank_name;	//银行名称
	private String bank_code;	//银行代码
	private String bank_url;	//银行网关
	
	public Bank() {
		super();
	}

	public Bank(int bank_id, String bank_name, String bank_code, String bank_url) {
		super();
		this.bank_id = bank_id;
		this.bank_name = bank_name;
		this.bank_code = bank_code;
		this.bank_url = bank_url;
	}
	
	
	
}
