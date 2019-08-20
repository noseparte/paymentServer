package com.xmbl.util;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  TestMail2 
 * @创建时间:  2018年1月3日 下午7:39:53
 * @修改时间:  2018年1月3日 下午7:39:53
 * @类说明:
 */
@Deprecated
public class TestMail2 {
	
	private static Logger LOGGER = LoggerFactory.getLogger(TestMail2.class);
    private static Properties props = System.getProperties();
    static {
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
    }
    
    /**
     * 发送邮件文本
     * @param fromMail
     * @param user
     * @param password
     * @param toMail
     * @param mailTitle
     * @param mailContent
     * @return
     */
    public static boolean sendMailText(String fromMail, String user, String password, String toMail, String mailTitle, String mailText) {
    	try {
    		LOGGER.info("发送邮件文本开始...");
    		// 根据属性新建一个邮件会话
			Session session = Session.getInstance(props);
			// 创建邮件对象  
	        Message msg = new MimeMessage(session);
	        // 设置标题
	        msg.setSubject(mailTitle);
	        // 发送 纯文本 邮件 
	        msg.setText(mailText);
	        // 设置发件人  
	        msg.setFrom(new InternetAddress(fromMail));  
	        // 设置收件人,并设置其接收类型为TO
	        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
	        // 设置发信时间
	        //	        msg.setSentDate(new Date());
	     	// 存储邮件信息
	     	msg.saveChanges();
	        // 发送邮件
	        Transport transport = session.getTransport();  
	        // 连接邮件服务器  
	        transport.connect(user, password);  
	        // 发送邮件,其中第二个参数是所有已设好的收件人地址
	        transport.sendMessage(msg, msg.getAllRecipients());  
	        // 关闭连接  
	        transport.close();
    		LOGGER.info("发送邮件文本结束...");
    		return true;
    	} catch (Exception e) {
    		LOGGER.error("发送邮件文本报错,错误信息为:" + e.getMessage());
    		return false;
    	}
    }
    
    public static boolean sendMailHtmlContent(String fromMail, String user, String password, String toMail, String mailTitle, String mailContent) {
    	try {
    		LOGGER.info("发邮件htmlContent开始");
    		// 根据属性新建一个邮件会话
			Session session = Session.getInstance(props);
			// 创建邮件对象  
	        Message msg = new MimeMessage(session);
	        // 设置标题
	        msg.setSubject(mailTitle);
	        // 发送 html  邮件 
	        msg.setContent(mailContent, "text/html;charset=utf-8");
	        // 设置发件人  
	        msg.setFrom(new InternetAddress(fromMail));  
	        // 设置收件人,并设置其接收类型为TO
	        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
	        // 设置发信时间
	        //	        msg.setSentDate(new Date());
	     	// 存储邮件信息
	     	msg.saveChanges();
	        // 发送邮件
	        Transport transport = session.getTransport();  
	        // 连接邮件服务器  
	        transport.connect(user, password);  
	        // 发送邮件,其中第二个参数是所有已设好的收件人地址
	        transport.sendMessage(msg, msg.getAllRecipients());  
	        // 关闭连接  
	        transport.close();
    		LOGGER.info("发邮件htmlContent结束");
    		return true;
    	} catch (Exception e) {
    		LOGGER.info("发邮件htmlContent报错,错误信息:" + e.getMessage());
    		return false;
    	}
    }
    
    /**
     * 发送邮件图片
     * 
     * @param fromMail
     * @param user
     * @param password
     * @param toMail
     * @param mailTitle
     * @param mailContent
     * @param imgMapLst
     * @return
     */
    public static boolean sendMailHtmlContentBySrcImg(String fromMail, String user, String password, String toMail, String mailTitle, String mailContent,List<Map<String,String>> imgMapLst) {
    	try {
    		LOGGER.info("发邮件htmlContentBySrcImg开始");
    		// 根据属性新建一个邮件会话
			Session session = Session.getInstance(props);
			// 创建邮件对象  
	        Message msg = new MimeMessage(session);
	        // 设置标题
	        msg.setSubject(mailTitle);
	        // 准备邮件正文数据
	        MimeBodyPart text = new MimeBodyPart();
	        text.setContent(mailContent, "text/html;charset=UTF-8");
	        for (Map<String,String> imgIdMap : imgMapLst) {
	        	// 准备图片数据
		        MimeBodyPart image = new MimeBodyPart();
		        DataHandler dh = new DataHandler(new FileDataSource(imgIdMap.get("img")));
		        image.setDataHandler(dh);
		        image.setContentID(imgIdMap.get("id"));
		        // 描述数据关系
		        MimeMultipart mm = new MimeMultipart();
		        mm.addBodyPart(text);
		        mm.addBodyPart(image);
		        mm.setSubType("related");
		        msg.setContent(mm);
	        }
	        
	        // 设置发件人  
	        msg.setFrom(new InternetAddress(fromMail));  
	        // 设置收件人,并设置其接收类型为TO
	        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
	        // 设置发信时间
	        // msg.setSentDate(new Date());
	     	// 存储邮件信息
	     	msg.saveChanges();
	     	msg.writeTo(new FileOutputStream("E:\\ImageMail.eml"));
	        // 发送邮件
	        // Transport transport = session.getTransport("smtp");
	        Transport transport = session.getTransport();  
	        // 连接邮件服务器  
	        transport.connect(user, password);  
	        // 发送邮件,其中第二个参数是所有已设好的收件人地址
	        transport.sendMessage(msg, msg.getAllRecipients());  
	        // 关闭连接  
	        transport.close();
    		LOGGER.info("发邮件htmlContentBySrcImg结束");
    		return true;
    	} catch (Exception e) {
    		LOGGER.info("发邮件htmlContentBySrcImg报错,错误信息:" + e.getMessage());
    		return false;
    	}
    }
    
	public static void sendMail(String fromMail, String user, String password, String toMail, String mailTitle, String mailContent) {
		LOGGER.info("发邮件开始");
		try {
			// 根据属性新建一个邮件会话
			Session session = Session.getInstance(props);
			// 创建邮件对象  
	        Message msg = new MimeMessage(session);
	        // 设置标题
	        msg.setSubject(mailTitle);
	        // 发送 纯文本 邮件 
	        // msg.setText("这是一封由JavaMail发送的邮件！");
	     	// 发送HTML邮件，内容样式比较丰富
	        // msg.setContent(mailContent, "text/html;charset=utf-8");
	        // 准备邮件数据
	        // 准备邮件正文数据
	        MimeBodyPart text = new MimeBodyPart();
	        String uuidStr = String.valueOf(((int)Math.random() * 100000));
	        text.setContent("这是一封邮件正文带图片<img src='cid:"+uuidStr+"'>的邮件", "text/html;charset=UTF-8");
	        // 准备图片数据
	        MimeBodyPart image = new MimeBodyPart();
	        DataHandler dh = new DataHandler(new FileDataSource("C:\\Users\\gg\\Desktop\\fkwg.jpg"));
	        image.setDataHandler(dh);
	        image.setContentID(uuidStr);
	        // 描述数据关系
	        MimeMultipart mm = new MimeMultipart();
	        mm.addBodyPart(text);
	        mm.addBodyPart(image);
	        mm.setSubType("related");
	        msg.setContent(mm);
	        // 附件
//	        List<String> files = new ArrayList<String>();
//	        files.add("C:\\Users\\gg\\Desktop\\fkwg.jpg");
//	        if (files != null) {  
//	            for (String filename : files) {  
//	            	if (filename != null && filename.length() > 0) {  
//	                    FileDataSource fileds = new FileDataSource(filename);  
//	                    msg.setDataHandler(new DataHandler(fileds));
//	                    msg.setFileName(MimeUtility.encodeText(fileds.getName(), "utf-8", null)); // 解决附件名称乱码  
//	                }
//	            }  
//	        }
	        
	        // 设置发件人  
	        msg.setFrom(new InternetAddress(fromMail));  
	        // 设置收件人,并设置其接收类型为TO
	        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
	        // 设置发信时间
	        // msg.setSentDate(new Date());
	     	// 存储邮件信息
	     	msg.saveChanges();
	     	msg.writeTo(new FileOutputStream("E:\\ImageMail.eml"));
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
		//		sendMail("xmbladmin@163.com", "xmbladmin@163.com", "xmbl123456", "18756977291@163.com", "Java Mail 测试邮件",
		//				"<a href=\"www.baidu.com\">html 元素</a>:<b>邮件内容</b>");
		
		//		boolean flag = sendMailText("18756977291@163.com", "18756977291@163.com", "wjsbb2016", "3253291877@qq.com", "我是标题", "我是文本");
		//		System.out.println(flag);
		
		// html content 
		//		boolean flag = sendMailHtmlContent("18756977291@163.com", "18756977291@163.com", "wjsbb2016", "1402614629@qq.com", "我是标题", "<a href='https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=249009111,1094131950&fm=173&s=C2D35A9E4F6055094A5821DB0300C0F2&w=218&h=146&img.JPEG'>我是文本123</a><br/><img src='https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=249009111,1094131950&fm=173&s=C2D35A9E4F6055094A5821DB0300C0F2&w=218&h=146&img.JPEG'>");
		//		System.out.println(flag);
		
		List<Map<String,String>> mapLst = new ArrayList<Map<String,String>>();
		Map<String,String> map = new HashMap<String,String>();
		map.put("id", "11");
		map.put("img", "C:\\Users\\gg\\Desktop\\fkwg.jpg");
		mapLst.add(map);
		map = new HashMap<String,String>();
		map.put("id", "22");
		map.put("img", "C:\\Users\\gg\\Desktop\\fkwg.jpg");
		mapLst.add(map);
		boolean flag = sendMailHtmlContentBySrcImg("18756977291@163.com", "18756977291@163.com", "wjsbb2016", "1402614629@qq.com", "我是标题", "111，<img src='cid:11' />我是文本<img src='cid:22' />", mapLst);
		System.out.print(flag);
	}
}