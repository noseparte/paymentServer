package com.xmbl.util.platformSendYxServerUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  PlatformSendYouXiServerUtil 
 * @创建时间:  2018年3月9日 下午6:24:00
 * @修改时间:  2018年3月9日 下午6:24:00
 * @类说明: 平台向游戏服务器发送通信请求工具类
 */
public class PlatformSendYouXiServerUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(PlatformSendYouXiServerUtil.class);
	
	/**
	 * 平台向游戏服务器发送请求
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String send(String url,String jsonObjStr) {
		try {
			LOGGER.info("平台向游戏服务器发送请求开始,请求url为:{},请求参数json字符串为:{}",url,jsonObjStr);
			HttpClient httpClient =  new HttpClient();
			httpClient.getParams().setSoTimeout(30);
			httpClient.getParams().setConnectionManagerTimeout(30);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(30);
			PostMethod postMethod = new PostMethod(url);
//			PostMethod postMethod = new PostMethod("http://"+GameServerConstant.SEND_GOODS_IP+":"+GameServerConstant.SEND_GOODS_PORT+RECHARGE);
//			PostMethod postMethod = new PostMethod("http://"+gameServer.getRpcIp()+":"+gameServer.getRpcPort()+RECHARGE+params);
			HttpMethodParams param = postMethod.getParams();
			postMethod.setRequestBody(jsonObjStr);
			param.setContentCharset("UTF-8");
			int statuscode = httpClient.executeMethod(postMethod);
			LOGGER.info("平台向游戏服务器返回响应码为:{}", statuscode);
			String result = postMethod.getResponseBodyAsString();
			LOGGER.info("平台向游戏服务器发送请求结束,返回结果json字符串对象为:{}",result);
			Assert.isTrue(StringUtils.isNotBlank(result),"游戏服务器响应信息返回结果出错");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("平台向游戏服务器发送请求报错啦，错误信息为:",e.getMessage());
			return null;
		}
		
	}
}
