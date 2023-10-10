package com.lung.constant;

import com.lung.util.PropertyUtil;

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
