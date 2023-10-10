package com.lung.config;

import com.lung.util.PropertyUtil;

/**
 * @author noseparte
 * @implSpec Wxpay微信支付相关配置
 * @since 2023/10/10 - 10:49
 * @version 1.0
 */
public class WxpayConfig {

	// 应用网关
	public final static String gatewayUrl = "";
	// 应用网关
	public final static String Transfer_Gateway_Url = "";
	// 商户ID
	public final static String MCH_ID = "";
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
	public final static String SSLCERT_PATH = "";	//windows
		
		
}
