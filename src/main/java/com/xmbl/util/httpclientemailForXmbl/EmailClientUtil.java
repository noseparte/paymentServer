package com.xmbl.util.httpclientemailForXmbl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.xmbl.util.XmblLoggerUtil;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  EmailClient 
 * @创建时间:  2018年1月4日 下午9:19:25
 * @修改时间:  2018年1月4日 下午9:19:25
 * @类说明:
 */
public class EmailClientUtil {

	/**
	 * 根据map消息发送邮件消息
	 * 
	 * @param map
	 * @return
	 */
	public static String sendEmailByMap(Map<String,String> map) {
		String body = "";
		try {
			JSONObject jsonObj = new JSONObject();
			for (String key:map.keySet()) {
				jsonObj.put(key, map.get(key));
			}
			body = sendEmailByJsonObj(jsonObj);
		} catch (Exception e) {
			XmblLoggerUtil.error("报错了: ",e.getMessage());
		}
		return body;
	}

	/**
	 * 根据jsonObj 发送邮件消息
	 * 
	 * @param jsonObj
	 * @return
	 */
	public static String sendEmailByJsonObj(JSONObject jsonObj) {
		String jsonStr = JSONObject.toJSONString(jsonObj);
		Map<String,String> map = new HashMap<String,String>();
    	map.put("jsonData", jsonStr);
        String body = HttpClientUtil.send(Constant.XMBL_EMAIL_URL, map);
        return body;
	}
	
	
	public static void main(String[] args) {
		// 测试方法
		// {"to":"1402614629@qq.com","cc":"18756977291@163.com","subject":"密码验证",
		//"content":"44444","sendDateStr":"2018-01-04 21:15:00","replySignType":"1",
		//"imagePath":"","attachPath":""}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("to", "1402614629@qq.com");
		jsonObj.put("cc", "18756977291@163.com");
		jsonObj.put("bcc", "xmbladmin1@163.com");
		jsonObj.put("subject", "密码验证11111");
		jsonObj.put("content", "内容部分，亲爱的你好");
		jsonObj.put("sendDateStr", "2018-01-05 13:35:00");
		// jsonObj.put("replySignType", "1");
		jsonObj.put("imagePath", "C:\\Users\\gg\\Desktop\\fkwg.jpg");
		jsonObj.put("attachPath", "C:\\Users\\gg\\Desktop\\fkwg.jpg");
		String body = sendEmailByJsonObj(jsonObj);
		System.out.println(body);
	}
}
