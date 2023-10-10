package com.lung.util.email;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.lung.util.DateUtils;
import com.lung.util.XmblLoggerUtil;
public class EmailUtils {
	
	public static String getMailServerHost(String emailAddress) {
		if (emailAddress == null || "".equals(emailAddress.trim())) {
			throw new RuntimeException("邮件账户错误！");
		}
		StringBuffer sb = new StringBuffer();
		sb.append("smtp.");
		emailAddress = emailAddress
				.substring(emailAddress.lastIndexOf("@") + 1);
		sb.append(emailAddress);
		return sb.toString().trim();
	}
	
	/**
	 * 解析发送给谁的信息，获取类型信息
	 * @param to
	 * @return
	 */
	public static EnumEmailInfoCode getMailServerEnumEmailInfo(String to){
		List<String> tolist = Arrays.asList(to.split(","));
		if (tolist!=null &&  tolist.size() > 0) {
			String toStr = tolist.get(0);
			String toTypeStr = toStr.substring(toStr.lastIndexOf("@") + 1,toStr.indexOf(".")).trim();
			for (EnumEmailInfoCode enumEmailInfoCode : EnumEmailInfoCode.values()) {
				if (toTypeStr.equals(enumEmailInfoCode.getType())) {
					return enumEmailInfoCode;
				}
			}
		}
		return EnumEmailInfoCode.y163;
	}
	// 验证邮箱地址
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			//String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			String check =  "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			XmblLoggerUtil.error("邮箱地址错误！");
			flag = false;
		}
		return flag;
	}

	@SuppressWarnings("unused")
	private static boolean sendMail(JSONObject jsonObj) {
		try {
			String sender = (String) jsonObj.get("sender");
			Assert.isTrue(StringUtils.isNoneBlank(sender), "发送人不能为空");
			String username = (String) jsonObj.get("username");
			Assert.isTrue(StringUtils.isNotBlank(username), "发送用户名不能为空");
			String password = (String) jsonObj.get("password");
			Assert.isTrue(StringUtils.isNotBlank(password), "发送用户授权码不能为空");
			String to = (String) jsonObj.get("to");
			Assert.isTrue(StringUtils.isNotBlank(to), "接收用户不能为空");
			String cc = (String) jsonObj.get("cc");
			String bcc = (String) jsonObj.get("bcc");
			String subject = (String) jsonObj.get("subject");
			Assert.isTrue(StringUtils.isNotBlank(subject), "邮件主题不能为空");
			String sendDateStr = (String) jsonObj.get("sendDate");
			if (StringUtils.isBlank(sendDateStr)) {
				sendDateStr = DateUtils.formatDate(new Date(), DateUtils.DEFAULT);
			}
			String replySignType = (String) jsonObj.get("replySignType");
			String imagePath = (String) jsonObj.get("imagePath");
			String attachPath = (String) jsonObj.get("attachPath");
			String content = (String) jsonObj.get("content");
			Assert.isTrue(StringUtils.isNotBlank(content), "邮件内容不能为空");
			
			// 验证通过
			EmailMessage ms = new EmailMessage();
			ms.setSender(sender);
			ms.setUsername(username);
			ms.setPassword(password);
			ms.setTo(to);
			ms.setCc(cc);
			ms.setBcc(bcc);
			ms.setSubject(subject);
			Date sendDate = DateUtils.parseDate(sendDateStr, DateUtils.DEFAULT);
			ms.setSendDate(sendDate);
			ms.setReplySign(replySignType!=null && "1".equals(replySignType));
			ms.setContainAttach(false);
			ms.setImagePath(imagePath);
			ms.setAttachPath(attachPath);
			ms.setContent(content);
			MailSender mailSender = new MailSender();
			mailSender.sendEmail(ms);
			return true;
		} catch (Exception e) {
			XmblLoggerUtil.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * 发送系统邮件,发送失败返回false
	 * @param jsonObj
	 * @return
	 */
	public static boolean sendMailForSystem(JSONObject jsonObj) {
		try {
			String to = (String) jsonObj.get("to");
			Assert.isTrue(StringUtils.isNotBlank(to), "接收用户不能为空");
			String cc = (String) jsonObj.get("cc");
			String bcc = (String) jsonObj.get("bcc");
			String subject = (String) jsonObj.get("subject");
			Assert.isTrue(StringUtils.isNotBlank(subject), "邮件主题不能为空");
			String content = (String) jsonObj.get("content");
			Assert.isTrue(StringUtils.isNotBlank(content), "邮件内容不能为空");
			String sendDateStr = (String) jsonObj.get("sendDateStr");
			if (StringUtils.isBlank(sendDateStr)) {
				sendDateStr = DateUtils.formatDate(new Date(), DateUtils.DEFAULT);
			}
			String replySignType = (String) jsonObj.get("replySignType");
			String imagePath = (String) jsonObj.get("imagePath");
			String attachPath = (String) jsonObj.get("attachPath");
			// 验证通过
			EmailMessage ms = new EmailMessage();
			ms.setSender(MailConstant.MAIL_SENDER_DEFAULT);
			ms.setUsername(MailConstant.MAIL_USERNAME_DEFAULT);
			ms.setPassword(MailConstant.MAIL_PASSWORD_DEFAULT);
			ms.setTo(to);
			ms.setCc(cc);
			ms.setBcc(bcc);
			ms.setSubject(subject);
			Date sendDate = DateUtils.parseDate(sendDateStr, DateUtils.DEFAULT);
			ms.setSendDate(sendDate);
			ms.setReplySign(replySignType!=null && "1".equals(replySignType));
			ms.setContainAttach(false);
			ms.setImagePath(imagePath);
			ms.setAttachPath(attachPath);
			ms.setContent(content);
			MailSender mailSender = new MailSender();
			boolean flag = mailSender.sendEmail(ms);
			return flag;
		} catch (Exception e) {
			XmblLoggerUtil.error(e.getMessage());
			return false;
		}
	}
	
	public static void main(String[] args) {
		MailSender mailSender = new MailSender();
		EmailMessage ms = new EmailMessage();
//		ms.setSender(MailConstant.MAIL_SENDER_DEFAULT);
//		ms.setUsername(MailConstant.MAIL_USERNAME_DEFAULT);
//		ms.setPassword(MailConstant.MAIL_PASSWORD_DEFAULT);
//		ms.setSender("2583428549@qq.com");
//		ms.setUsername("2583428549@qq.com");
//		ms.setPassword("azscoqxhwvflechb");
		ms.setTo("ugcapp@sina.com");
		ms.setCc("3253291877@qq.com");
		ms.setBcc("xmbladmin2@163.com");
		ms.setSubject("1111cdddd一份感恩，请笑纳");
//		ms.setSendDate(new Date());
		//	ms.setReplySign(false);
		ms.setContainAttach(false);
//		ms.setImagePath("C:\\Users\\gg\\Desktop\\fkwg.jpg|C:\\Users\\gg\\Desktop\\1111.png|C:\\Users\\gg\\Desktop\\fkwg.jpg|C:\\Users\\gg\\Desktop\\fkwg.jpg");
//		ms.setAttachPath("C:\\Users\\gg\\Desktop\\commentServer.doc|C:\\Users\\gg\\Desktop\\FTPClientUtil.txt");
		ms.setContent("dsjnfkjdsfsndjnffjdjd");
		mailSender.sendEmail(ms);
	}
}