package com.xmbl.config;

import com.xmbl.util.PropertyUtil;

/**
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 * @Author Noseparte
 * @Compile 2018年7月18日 -- 下午6:22:44
 * @Version 1.0
 * @Description			Wxpay微信支付相关配置
 */
public class WxpayConfig {

	// 应用网关
	public final static String gatewayUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	// 应用网关
	public final static String Transfer_Gateway_Url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
	// 商户ID
	public final static String MCH_ID = "1509358601";
	// 应用ID
	public final static String APP_ID = "";
	// API秘钥
	public final static String APP_KEY = "";
	// 应用秘钥
	public final static String App_Secret = "";
	// 异步通知地址
	public static String NOTIFY_URL = PropertyUtil.getProperty("conf/env.properties", "wxpay.notifyurl");
	
	public final static String return_url = "RSA2";
	// 签名类型
	public final static String sign_type = "RSA2";
	// 编码格式
	public final static String charset = "utf-8";
	// 仅支持JSON 
	public final static String format = "JSON";
	// SSLCERT_PATH
//	public final static String SSLCERT_PATH = "/D:/JavaWorkSpace/paymentServer/target/classes/apiclient_cert.p12";	//windows
	public final static String SSLCERT_PATH = "/data/xmbl/apiclient_cert.p12";	//windows
		
		
}
