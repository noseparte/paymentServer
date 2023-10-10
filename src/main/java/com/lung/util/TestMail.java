package com.lung.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: noseparte
 * @Email: 1402614629@qq.com
 * @类名: Test
 * @创建时间: 2018年1月3日 下午2:59:23
 * @修改时间: 2018年1月3日 下午2:59:23
 * @类说明:
 */
@Deprecated
public class TestMail {
	
	private static Logger LOGGER = LoggerFactory.getLogger(TestMail.class);
	
	public static void sendMail() throws Exception {
		Properties props = new Properties(); // 可以加载一个配置文件
        // 开启debug调试  
		// props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证  
        props.setProperty("mail.smtp.auth", "true");  
        // 设置邮件服务器主机名  
        props.setProperty("mail.host", "smtp.163.com");  
        // 发送邮件协议名称  
        props.setProperty("mail.transport.protocol", "smtp");
        //设置端口  
        props.put("mail.smtp.port", 25);
		// 根据属性新建一个邮件会话
		Session session = Session.getInstance(props);
		
		// 创建邮件对象  
        Message msg = new MimeMessage(session);
        // 设置标题
        msg.setSubject("JavaMail测试");  
        // 发送 纯文本 邮件 
        // msg.setText("这是一封由JavaMail发送的邮件！");
     	// 发送HTML邮件，内容样式比较丰富
        msg.setContent("这是一封由JavaMail发送的邮件！", "text/html;charset=utf-8");
        
        // 设置发件人  
        msg.setFrom(new InternetAddress("18756977291@163.com"));  
        // 设置收件人,并设置其接收类型为TO
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress("1402614629@qq.com"));
        // 设置发信时间
        // msg.setSentDate(new Date());
     	// 存储邮件信息
     	msg.saveChanges();

        // 发送邮件
        // Transport transport = session.getTransport("smtp");
        Transport transport = session.getTransport();  
        // 连接邮件服务器  
        transport.connect("18756977291@163.com", "wjsbb2016");  
        // 发送邮件,其中第二个参数是所有已设好的收件人地址
        transport.sendMessage(msg, msg.getAllRecipients());  
        // 关闭连接  
        transport.close();
	}
	
	public static void sendMail(String fromMail, String user, String password, String toMail, String mailTitle, String mailContent) {
		LOGGER.info("发邮件开始");
		try {
			Properties props = new Properties(); // 可以加载一个配置文件
	        // 开启debug调试  
			// props.setProperty("mail.debug", "true");
	        // 发送服务器需要身份验证  
	        props.setProperty("mail.smtp.auth", "true");  
	        // 设置邮件服务器主机名  
	        props.setProperty("mail.smtp.host", "smtp.163.com");  
	        // 发送邮件协议名称  
	        props.setProperty("mail.transport.protocol", "smtp");
	        //设置端口  
	        props.put("mail.smtp.port", 25);
			// 根据属性新建一个邮件会话
			Session session = Session.getInstance(props);
			
			// 创建邮件对象  
	        Message msg = new MimeMessage(session);
	        // 设置标题
	        msg.setSubject(mailTitle);  
	        // 发送 纯文本 邮件 
	        // msg.setText("这是一封由JavaMail发送的邮件！");
	     	// 发送HTML邮件，内容样式比较丰富
	        msg.setContent(mailContent, "text/html;charset=utf-8");
	        
	        // 设置发件人  
	        msg.setFrom(new InternetAddress(fromMail));  
	        // 设置收件人,并设置其接收类型为TO
	        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
	        // 设置发信时间
	        // msg.setSentDate(new Date());
	     	// 存储邮件信息
	     	msg.saveChanges();

	        // 发送邮件
	        // Transport transport = session.getTransport("smtp");
	        Transport transport = session.getTransport();  
	        // 连接邮件服务器  
	        transport.connect(user, password);  
	        // 发送邮件,其中第二个参数是所有已设好的收件人地址
	        transport.sendMessage(msg, msg.getAllRecipients());  
	        // 关闭连接  
	        transport.close();
			LOGGER.info("发邮件结束");
		} catch (Exception e) {
			LOGGER.info("发邮件报错，错误信息:"+e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		sendMail("xmbladmin@163.com", "xmbladmin@163.com", "xmbl123456", "18756977291@163.com", "Java Mail 测试邮件",
				"<a href=\"www.baidu.com\">html 元素</a>:<b>邮件内容</b>");
	}
}