package com.xmbl.util;

import org.apache.commons.lang3.StringUtils;

import com.xmbl.enumeration.EnumReportCauseCode;
import com.xmbl.enumeration.EnumReportCode;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  ReportUtil 
 * @创建时间:  2017年12月27日 上午11:20:51
 * @修改时间:  2017年12月27日 上午11:20:51
 * @类说明: 举报工具类
 */
public class ReportUtil {

	/**
	 * 是否存在某报告码
	 * @param numcode
	 * @return
	 */
	public static boolean isExsitReportCode(String numcode) {
		if (StringUtils.isBlank(numcode)) {
			return false;
		}
		for (EnumReportCode enumReportCode : EnumReportCode.values()) {
			if (numcode.equals(enumReportCode.getNumcode())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取举报报告码信息
	 * @return
	 */
	public static EnumReportCode getReportCodeInfo(String numcode) {
		if (StringUtils.isBlank(numcode)) {
			return null;
		}
		for (EnumReportCode enumReportCode : EnumReportCode.values()) {
			if (numcode.equals(enumReportCode.getNumcode())) {
				return enumReportCode;
			}
		}
		return null;
	}
	/**
	 * 是否存在举报原因码
	 * @param numcode
	 * @return
	 */
	public static boolean isExsitReportCauseCode(String numcode) {
		if (StringUtils.isBlank(numcode)) {
			return false;
		}
		for (EnumReportCauseCode enumReportCauseCode : EnumReportCauseCode.values()) {
			if (numcode.equals(enumReportCauseCode.getNumcode())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取举报原因信息
	 * @return
	 */
	public static EnumReportCauseCode getReportCauseCodeInfo(String numcode) {
		if (StringUtils.isBlank(numcode)) {
			return null;
		}
		for (EnumReportCauseCode enumReportCauseCode : EnumReportCauseCode.values()) {
			if (numcode.equals(enumReportCauseCode.getNumcode())) {
				return enumReportCauseCode;
			}
		}
		return null;
	}
}
