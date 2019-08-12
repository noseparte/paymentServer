package com.xmbl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  XmblLogger 
 * @创建时间:  2018年3月21日 下午8:39:36
 * @修改时间:  2018年3月21日 下午8:39:36
 * @类说明: 小米菠萝日志工具类
 */
public class XmblLoggerUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(XmblLoggerUtil.class);
	
	/**
	 * 打印info日志信息
	 * @param infos
	 */
	public static String info(String ... infos) {
		int length = infos.length;
		String info = "";
		if (length !=0) {
			if (length == 1) {
				info = infos[0];
				LOGGER.info(info);
				return info;
			} else {
				StringBuilder sbBuilder = new StringBuilder();
				for (int i = 0; i<length; i++) {
					sbBuilder.append(infos[i]);
				}
				info = sbBuilder.toString();
				LOGGER.info(info);
				return info;
			}
		}
		return info;
	}
	
	/**
	 * 打印错误信息
	 * @param errors
	 */
	public static String error(String ...errors){
		int length = errors.length;
		StringBuilder sb = new StringBuilder();
		sb.append("*************************************************************");
		if (length !=0) {
			if (length == 1) {
				sb.append(errors[0]);
				sb.append("*************************************************************");
				LOGGER.error(sb.toString());
				return sb.toString();
			} else {
				for (int i = 0; i<length; i++) {
					sb.append(errors[i]);
				}
				sb.append("*************************************************************");
				LOGGER.error(sb.toString());
				return sb.toString();
			}
		}
		sb.append("*************************************************************");
		return sb.toString();
	}
}
