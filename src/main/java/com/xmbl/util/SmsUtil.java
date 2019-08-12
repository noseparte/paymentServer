package com.xmbl.util;

import java.util.HashMap;
import java.util.Set;

import org.springframework.util.Assert;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.xmbl.constant.SmsContant;
import com.xmbl.dto.ResponseResult;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名: templateSMS
 * @创建时间: 2018年1月2日 下午4:53:37
 * @修改时间: 2018年1月2日 下午4:53:37
 * @类说明: 短信通知工具类
 */
@SuppressWarnings("unchecked")
public class SmsUtil {
	/**
	 * 沙箱环境运行
	 */
	@SuppressWarnings("unused")
	private static void sendSmsBySandbox() {
		try {
			CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
			restAPI.init("sandboxapp.cloopen.com", "8883");
			restAPI.setAccount("8aaf07085dcad420015dcff42dc401e1", "e8e62e9580504292bfab64568358c8ba");
			restAPI.setAppId("8aaf07085dcad420015dcff42f4f01e8");
			HashMap<String, Object> result = restAPI.sendTemplateSMS("18756977291", "1", new String[] {"1231", "5" });
			XmblLoggerUtil.info("SDKTestGetSubAccounts result=" ,result.toString());
			Assert.isTrue("000000".equals(result.get("statusCode")),//
					"错误码:" + result.get("statusCode") + ",错误信息:" + result.get("statusMsg"));
			HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for (String key : keySet) {
				Object object = data.get(key);
				XmblLoggerUtil.info(key," = ",object.toString());
			}
		} catch (Exception e) {
			XmblLoggerUtil.error("报错了，错误信息为:",e.getMessage());
		}
	}

	/**
	 * 开发 测试 生产环境发送短信
	 */
	@SuppressWarnings("unused")
	private static void sendSmsByPro() {
		try {
			HashMap<String, Object> result = null;
			// 初始化SDK
			CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
			// 沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");
			// 生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883");
			restAPI.init("app.cloopen.com", "8883");
			//restAPI.init("sandboxapp.cloopen.com", "8883");
			// 初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN
			// ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取
			// 参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN
			restAPI.setAccount("8aaf07085dcad420015dcff42dc401e1",
					"e8e62e9580504292bfab64568358c8ba");
			// 初始化应用ID
			// 测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID
			// 应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID
			restAPI.setAppId("8aaf07085dcad420015dcff42f4f01e8");
			// 调用发送模板短信的接口发送短信
			// 参数顺序说明：
			// 第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号
			// 第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1
			// 系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入"
			// 第三个参数是要替换的内容数组
			// 举例说明
			// 假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为
			// result = restAPI.sendTemplateSMS("13800000000","1" ,new String[]{"6532","5"});
			// 则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入
			result = restAPI.sendTemplateSMS("18756977291", "1", new String[] {"1231", "5" });
			XmblLoggerUtil.info("SDKTestGetSubAccounts result=",result.toString());
			Assert.isTrue("000000".equals(result.get("statusCode")),//
					"错误码:" + result.get("statusCode") + ",错误信息:" + result.get("statusMsg"));
			HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for (String key : keySet) {
				Object object = data.get(key);
				XmblLoggerUtil.info(key , " = " , object.toString());
			}
		} catch(Exception e) {
			XmblLoggerUtil.error("报错了，错误信息为:",e.getMessage());
		}
	}
	
	/**
	 * 开发 测试 生产环境发送短信 (带参)
	 * @param appId  应用appId
	 * @param mobiles 手机号码
	 * @param templateId 模板id
	 * @param strs 应用模板变量参数
	 */
	public static ResponseResult sendSmsParamByPro(String appId,String mobiles,String templateId,String ... strs) {
		try {
			HashMap<String, Object> result = null;
			// 初始化SDK
			CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
			// 沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");
			// 生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883");
			restAPI.init(SmsContant.REST_URL_IP, SmsContant.REST_URL_PORT);
			//restAPI.init("sandboxapp.cloopen.com", "8883");
			// 初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN
			// ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取
			// 参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN
			restAPI.setAccount(SmsContant.ACCOUNT_SID,
					SmsContant.AUTH_TOKEN);
			// 初始化应用ID
			// 测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID
			// 应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID
			restAPI.setAppId(appId);
			// 调用发送模板短信的接口发送短信
			// 参数顺序说明：
			// 第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号
			// 第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1
			// 系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入"
			// 第三个参数是要替换的内容数组
			// 举例说明
			// 假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为
			// result = restAPI.sendTemplateSMS("13800000000","1" ,new String[]{"6532","5"});
			// 则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入
			result = restAPI.sendTemplateSMS(mobiles, templateId, strs);
			XmblLoggerUtil.info("SDKTestGetSubAccounts result=", result.toString());
			Assert.isTrue("000000".equals(result.get("statusCode")),//
					"错误码:" + result.get("statusCode") + ",错误信息:" + result.get("statusMsg"));
			HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for (String key : keySet) {
				Object object = data.get(key);
				XmblLoggerUtil.info(key, " = ",object.toString());
			}
			return ResponseResultUtil.successJson(data);
		} catch(Exception e) {
			XmblLoggerUtil.error("报错了，错误信息为:",e.getMessage());
			return ResponseResultUtil.errorJson(e.getMessage());
		}
	}

	public static void main(String[] args) {
		// sendSmsBySandbox();
 		// sendSmsByPro();
		sendSmsParamByPro("8aaf07085dcad420015dcff42f4f01e8","18756977291","198516","1245634");
	}
}
