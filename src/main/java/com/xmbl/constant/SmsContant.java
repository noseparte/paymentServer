package com.xmbl.constant;

import com.xmbl.util.PropertyUtil;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  SmsContant 
 * @创建时间:  2018年1月2日 下午5:06:13
 * @修改时间:  2018年1月2日 下午5:06:13
 * @类说明: 短信通知模板常量类
 */
public class SmsContant {
	// Rest URL
	public static String ENV = PropertyUtil.getProperty("conf/env.properties", "env");
	public static String REST_URL = PropertyUtil.getProperty("conf/env.properties", "sms.rest_url");
	public static String REST_URL_IP = PropertyUtil.getProperty("conf/env.properties", "sms.rest_url_ip");
	public static String REST_URL_PORT = PropertyUtil.getProperty("conf/env.properties", "sms.rest_url_port");
	//	ACCOUNT_SID 
	public static String ACCOUNT_SID = PropertyUtil.getProperty("conf/env.properties", "sms.account_sid");
	//	AUTH TOKEN  
	public static String AUTH_TOKEN = PropertyUtil.getProperty("conf/env.properties", "sms.auth_token");
	public static String SMS_CLIENT_URL = PropertyUtil.getProperty("conf/env.properties", "sms.client.url");
	public static String SMS_CLIENT_APPID = PropertyUtil.getProperty("conf/env.properties", "sms.client.appId");
	public static String SMS_CLIENT_TEMPLATEID = PropertyUtil.getProperty("conf/env.properties", "sms.client.templateId");
}
