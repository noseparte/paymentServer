package com.xmbl.util.email;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名: EmailMessage
 * @创建时间: 2018年1月4日 下午12:47:29
 * @修改时间: 2018年1月4日 下午12:47:29
 * @类说明: 电子邮件消息
 */
@Data
public class EmailMessage implements Serializable {
	private static final long serialVersionUID = 6732172357029717599L;
	// 使用协议
	private String protocol = "smtp";
	// 邮箱服务器
	private String host;
	// 是否请求认证
	private String auth = "true";
	// 发件人
	private String sender;
	// 用户名
	private String username;
	// 密码
	private String password;
	// 收件人
	private String to;
	// 抄送
	private String cc;
	// 暗送
	private String bcc;
	// 主题
	private String subject;
	// 邮件的文本内容
	private String content;
	// 发送时间
	private Date sendDate;
	// 要求回执
	private boolean replySign = Boolean.FALSE;
	// 是否包含附件
	private boolean containAttach;
	// 邮件图片的路劲 （多个，使用"|"拼接）
	private String imagePath;
	// 邮件附件的路径 （多个，使用"|"拼接）
	private String attachPath;
	// 发送邮件处理图片所需的内容
	private Map<String, String> map = new HashMap<String, String>();
	// 发送邮件附件
	private List<String> attacPathLst = new ArrayList<String>();
	// 字符编码
	private String charset = "UTF-8";

	// getter and setter
	public String getHostByEmail() {
		return EmailUtils.getMailServerHost(this.getSender());
	}
	
	public Map<String, String> getMapByEmail() {
		// 如果图片路径为null,直接返回null
		if (StringUtils.isBlank(imagePath)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(content);
		map = new HashMap<String,String>();
		// 处理邮件中图片和附件的路径
		for (String realImgPath : imagePath.split("\\|")) {
			String cid = genPK();
			map.put(cid, realImgPath);
			sb.append("<br/><img src='cid:").append(cid).append("'").append("/>");
		}
		// 设置文本
		setContent(sb.toString());
		return map;
	}
	
	public List<String> getAttacPathsByEmail() {
		// 如果附件路径为null,直接返回null
		if (StringUtils.isBlank(attachPath)) {
			return null;
		}
		attacPathLst = new ArrayList<String>();
		for (String realAttachPath : attachPath.split("\\|")) {
			attacPathLst.add(realAttachPath);
		}
		return attacPathLst;
	}
	
	public static String genPK() {
		return new BigInteger(165, new Random()).toString(36).toUpperCase();
	}

}
