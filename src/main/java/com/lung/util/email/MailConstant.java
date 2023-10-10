package com.lung.util.email;

import com.lung.util.PropertyUtil;

/**
 * @author: noseparte
 * @Email: 1402614629@qq.com
 * @类名:  MailConstant 
 * @创建时间:  2018年1月4日 下午8:26:40
 * @修改时间:  2018年1月4日 下午8:26:40
 * @类说明:
 */
public class MailConstant {
//	public static String MAIL_SENDER_DEFAULT="xmbladmin@163.com";
//	public static String MAIL_USERNAME_DEFAULT="xmbladmin@163.com";
//	public static String MAIL_PASSWORD_DEFAULT="xmbl123456";
	public static String MAIL_SENDER_DEFAULT = PropertyUtil.getProperty("conf/env.properties", "mail.sender.default");
	public static String MAIL_USERNAME_DEFAULT = PropertyUtil.getProperty("conf/env.properties", "mail.username.default");
	public static String MAIL_PASSWORD_DEFAULT = PropertyUtil.getProperty("conf/env.properties", "mail.password.default");
	//public static String MAIL_BCC_DEFAULT = "xmbladmin2@163.com";
}
