package com.lung.util.email;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.lung.util.XmblLoggerUtil;

/**
 * 
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名: MailSender
 * @创建时间: 2018年1月4日 下午1:17:37
 * @修改时间: 2018年1月4日 下午1:17:37
 * @类说明: 实现邮件的发送
 */
public class MailSender {
	
	private MimeMessage msg;
	
	/**
	 * 得到邮件的环境对象
	 * @param ms
	 */
	public void getMimeMessage(EmailMessage ms) {
		String to = ms.getTo();
		EnumEmailInfoCode enumEmailInfoCode = EmailUtils.getMailServerEnumEmailInfo(to);
		ms.setSender(enumEmailInfoCode.getMail_sender());
		ms.setUsername(enumEmailInfoCode.getMail_username());
		ms.setPassword(enumEmailInfoCode.getMail_password());
		Properties props = new Properties();// 设置一些参数：发送时需要设置
		props.setProperty("mail.transport.protocol", ms.getProtocol());
		props.setProperty("mail.host", ms.getHostByEmail());
		props.setProperty("mail.smtp.auth", ms.getAuth());// 请求认证
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.setProperty("mail.smtp.port", "465");
		final String username = ms.getUsername();
		final String password = ms.getPassword();
		// 代表环境的对象
		Session session = Session.getInstance(props, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		msg = new MimeMessage(session);// 得到了代表邮件的对象
	}
	
	/**
	 * 得到文本部分
	 * @param content
	 * @param charset
	 * @return
	 * @throws MessagingException
	 */
	public MimeBodyPart getTextPart(String content, String charset)
			throws MessagingException {
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent(content, "text/html;charset=" + charset);
		return textPart;
	}
	
	/**
	 * 得到图片部分
	 * @param map
	 * @return
	 * @throws MessagingException
	 */
	public List<MimeBodyPart> getImageParts(Map<String, String> map)
			throws MessagingException {
		List<MimeBodyPart> imageParts = new ArrayList<MimeBodyPart>();
		for (Entry<String, String> e : map.entrySet()) {
			MimeBodyPart imagePart = new MimeBodyPart();
			DataHandler dh = new DataHandler(new FileDataSource(e.getValue())); // jaf会自动探测文件的MIME类型
			imagePart.setDataHandler(dh);
			imagePart.setContentID(e.getKey());
			imageParts.add(imagePart);
		}
		return imageParts;
	}

	/**
	 * 得到附件部分
	 * @param sources
	 * @return
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public List<MimeBodyPart> getAttachmentParts(List<String> sources)
			throws MessagingException, UnsupportedEncodingException {
		List<MimeBodyPart> attachmentParts = new ArrayList<MimeBodyPart>();
		for (String source : sources) {
			MimeBodyPart attachmentPart = new MimeBodyPart();
			DataHandler dh = new DataHandler(new FileDataSource(source));// jaf会自动探测文件的MIME类型
			String name = dh.getName();
			attachmentPart.setDataHandler(dh);
			attachmentPart.setFileName(MimeUtility.encodeText(name
					.substring(name.lastIndexOf("$^%") + 3)));
			attachmentParts.add(attachmentPart);
		}
		return attachmentParts;
	}
	
	/**
	 * 发送邮件,可发送多个图片和附件
	 * @param ms
	 */
	public boolean sendEmail(EmailMessage ms) {
		boolean flag = true;
		try {
			// 得到环境对象
			getMimeMessage(ms);
			String sender = ms.getSender();
			Assert.isTrue(StringUtils.isNotBlank(sender), "发送人不能为空");
			msg.setFrom(new InternetAddress(sender));
			// 中英文逗号或竖线进行切割
			Assert.isTrue(StringUtils.isNotBlank(ms.getTo()), "接收人不能为空");
			String to = ms.getTo().trim();
			List<String> receiverLst = Arrays.asList(to.split("[,|，]"));
			// 收件人InternetAddress数组
			InternetAddress[] receiverAddressArray = new InternetAddress[receiverLst.size()];
			for (int i = 0,len=receiverLst.size(); i < len; i++) {
				receiverAddressArray[i] = new InternetAddress(
						receiverLst.get(i).trim());
			}
			// 设置收件人
			msg.setRecipients(MimeMessage.RecipientType.TO,	receiverAddressArray);
			
			// 抄送
			if (StringUtils.isNotBlank(ms.getCc())) {
				List<String> ccLst = Arrays.asList(ms.getCc().trim().split("[,|，]"));
				InternetAddress[] ccAddressArray = new InternetAddress[ccLst.size()];
				for (int i = 0,len=ccLst.size(); i < len; i++) {
					ccAddressArray[i] = new InternetAddress(ccLst.get(i).trim());
				}
				msg.setRecipients(MimeMessage.RecipientType.CC, ccAddressArray);
			}
			
			// 暗送
			if (StringUtils.isNotBlank(ms.getBcc())) {
				List<String> bccLst = Arrays.asList(ms.getBcc().trim().split("[,|，]"));
				InternetAddress[] bccAddressArray = new InternetAddress[bccLst.size()];
				for (int i = 0,len=bccLst.size(); i < len; i++) {
					bccAddressArray[i] = new InternetAddress(bccLst.get(i).trim());
				}
				msg.setRecipients(MimeMessage.RecipientType.BCC,
						bccAddressArray);
			}

			// 是否要求回执
			if (ms.isReplySign()) {
				msg.setHeader("Disposition-Notification-To", "1");
				msg.addHeader("Disposition-Notification-To", "1");
			}

			// 主题
			msg.setSubject(ms.getSubject());

			MimeMultipart mmp = new MimeMultipart();
			Map<String, String> map = ms.getMapByEmail();
			// 得到邮件内容
			if (map == null) {
				mmp.addBodyPart(getTextPart(ms.getContent(), ms.getCharset()));
			}
			// 得到所有的图片
			if (map != null) {
				mmp.addBodyPart(getTextPart(ms.getContent(), ms.getCharset()));
				List<MimeBodyPart> imageParts = getImageParts(map);
				for (MimeBodyPart imagePart : imageParts) {
					mmp.addBodyPart(imagePart);
				}
			}
			// 得到所有的附件
			List<String> attachPathLst = ms.getAttacPathsByEmail();
			if (attachPathLst != null) {
				List<MimeBodyPart> attchParts = getAttachmentParts(attachPathLst);
				for (MimeBodyPart attchPart : attchParts) {
					mmp.addBodyPart(attchPart);
				}
			}
			mmp.setSubType("mixed");
			msg.setContent(mmp);
			//	msg.setSentDate(ms.getSendDate());
			msg.saveChanges();// 保存修改
			// 发送邮件
			Transport.send(msg);
		} catch (Exception e) {
			XmblLoggerUtil.error("发送邮件报错,错误信息为:",e.getMessage());
			flag = false;
		}
		return flag;
	}

}
