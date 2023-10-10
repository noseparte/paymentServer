package com.lung.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
