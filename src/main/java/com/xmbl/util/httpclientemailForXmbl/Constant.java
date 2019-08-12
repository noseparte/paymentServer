package com.xmbl.util.httpclientemailForXmbl;

import com.xmbl.util.PropertyUtil;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  Constant 
 * @创建时间:  2018年1月5日 上午10:15:16
 * @修改时间:  2018年1月5日 上午10:15:16
 * @类说明:
 */
public class Constant {
	public static String ENV = PropertyUtil.getProperty("conf/env.properties", "env");
	public static String XMBL_EMAIL_URL = PropertyUtil.getProperty("conf/env.properties", "xmbl.email.url");
}
