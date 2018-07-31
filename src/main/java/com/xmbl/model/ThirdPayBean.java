package com.xmbl.model;

import com.xmbl.mongo.pojo.GeneralBean;

import lombok.Data;

/**
 * Copyright © 2017 noseparte(Libra) © Like the wind, like rain
 * @Author Noseparte
 * @Compile 2017年10月2日 -- 下午11:11:58
 * @Version 1.0
 * @Description  第三方支付
 */
@Data
public class ThirdPayBean extends GeneralBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int bank_id;		//银行id
	private String third_name;	//第三方支付名称
	private String third_type;	//第三方支付类型
	private String mer_no;		//商户号
	private String mer_key;		//商户秘钥(公|私)
	private String return_url;	//异步通知地址

	public ThirdPayBean() {
		super();
	}
	
	public ThirdPayBean(int bank_id, String third_name, String third_type, String mer_no, String mer_key,
			String return_url) {
		super();
		this.bank_id = bank_id;
		this.third_name = third_name;
		this.third_type = third_type;
		this.mer_no = mer_no;
		this.mer_key = mer_key;
		this.return_url = return_url;
	}



	


	
	
	
}
