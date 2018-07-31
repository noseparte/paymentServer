package com.xmbl.constant;

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
	// 商户ID
	public final static String MCH_ID = "";
	// 应用ID
	public final static String APP_ID = "";
	// API秘钥
	public final static String APP_KEY = "";
	// 应用秘钥
	public final static String App_Secret = "";
	// 异步通知地址
	public final static String Notify_Url = "http://120.92.210.53:8087/paymentServer/api/pay/wx_pay_notify";
	
	public final static String return_url = "RSA2";
	// 签名类型
	public final static String sign_type = "RSA2";
	// 编码格式
	public final static String charset = "utf-8";
	// 仅支持JSON 
	public final static String format = "JSON";
		
		
}
