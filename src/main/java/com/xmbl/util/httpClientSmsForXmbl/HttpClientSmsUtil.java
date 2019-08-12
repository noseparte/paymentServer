package com.xmbl.util.httpClientSmsForXmbl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.xmbl.constant.SmsContant;
import com.xmbl.util.XmblLoggerUtil;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  HttpClientSmsUtil 
 * @创建时间:  2018年1月3日 上午10:14:50
 * @修改时间:  2018年1月3日 上午10:14:50
 * @类说明:
 */
public class HttpClientSmsUtil {

	/**
	 * 客户端请求服务端短信发送平台
	 * @param smsUrl  客户端发送到短信服务端地址
	 * @param appId    客户端调用第三方设置的应用id
	 * @param templateId 客户端调用第三方设置的模板id
	 * @param mobiles 客户端调用第三方设置的手机号码  支持一条信息群发功能 。多个手机号以逗号分隔，如"12385968596,15756235623"
	 * @param params 客户端调用第三方设置的模板中的参数 多个参数以逗号分隔 如 "模板内容1,模板内容2"
	 * @return
	 */
	private static String sendSms(String smsUrl,String appId,String templateId,String mobiles,String params) {
    	Map<String,String> map = new HashMap<String,String>();
    	JSONObject jsonObj = new JSONObject();
    	jsonObj.put("appId", appId);
    	jsonObj.put("templateId",templateId);
//    	jsonObj.put("mobiles", "17611478870");
    	jsonObj.put("mobiles", mobiles);
    	jsonObj.put("params", params);
    	String jsonStr = JSONObject.toJSONString(jsonObj);
    	map.put("jsonData", jsonStr);
        String body = HttpClientUtil.send(smsUrl, map);
        return body;
	}
	
	public static String sendSmsByPublic(String smsUrl,String appId,String templateId,String mobiles,String params) {
		String body = "";
		try {
			body = sendSms(smsUrl,appId,templateId,mobiles,params);
		} catch (Exception e) {
			XmblLoggerUtil.error("报错了: ",e.getMessage());
		}
		return body;
	}
	
	public static void main(String[] args) {
		String appId = SmsContant.SMS_CLIENT_APPID;
		String templateId = SmsContant.SMS_CLIENT_TEMPLATEID;
		String smsUrl = SmsContant.SMS_CLIENT_URL;
//		String smsUrl = "http://120.92.3.242:8081/templateSMSServer/sms/send";
		String body = sendSmsByPublic(smsUrl,appId,templateId,"18756977299","1245634,5");
		XmblLoggerUtil.info("响应结果: ",body);
	}
}
