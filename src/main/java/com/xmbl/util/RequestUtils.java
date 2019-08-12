package com.xmbl.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  RequestUtils 
 * @创建时间:  2017年12月22日 上午10:50:21
 * @修改时间:  2017年12月22日 上午10:50:21
 * @类说明:
 */
public class RequestUtils {
	
	private static Logger LOGGER = LoggerFactory.getLogger(RequestUtils.class);
	
	public static String getQueryString(HttpServletRequest request) {
		StringBuffer queryString = new StringBuffer("?");
		Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String paraName=(String)enu.nextElement();
			LOGGER.info(paraName+": "+request.getParameter(paraName));
			queryString.append(paraName).append("=").append(request.getParameter(paraName)).append("&");
		}
		return queryString.substring(0, queryString.length()-1).toString();
	}
}
