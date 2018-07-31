package com.xmbl.util.msgUtil;

import org.apache.commons.lang3.StringUtils;

/**
 * 消息拼接工具类
 * @author sbb
 *
 */
public class MsgUtil {

	/**
	 * 消息生成工具
	 * @param errorMsgs 消息数据组
	 * @return
	 */
	public static String generateMsg(String ...errorMsgs) {
		StringBuffer sb = new StringBuffer();
		for(String errorMsg : errorMsgs){
			sb.append(errorMsg);
		}
		return sb.toString();
	}
	
	/**
	 * 生成系统打印日志消息体
	 * @param errorMsgs 日志消息组
	 * @return
	 */
	public static String generateLogMsg(String ... errorMsgs) {
		String msgStr = generateMsg(errorMsgs);
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtils.repeat("==", 25));
		sb.append(msgStr);
		sb.append(StringUtils.repeat("==", 25));
		return sb.toString();
	}
	
	public static void main(String[] args){
//		String errorMsg = generateMsg();
//		System.out.println(errorMsg);
//		System.out.println(generateLogMsg("1","woshidjdfsjsf","2","dshshhdf"));
	}
}
