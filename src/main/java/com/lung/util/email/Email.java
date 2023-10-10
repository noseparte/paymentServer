package com.lung.util.email;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author: noseparte
 * @Email: 1402614629@qq.com
 * @类名:  Email 
 * @创建时间:  2018年1月4日 下午12:43:18
 * @修改时间:  2018年1月4日 下午12:43:18
 * @类说明:  电子邮件类
 */
@Data
public class Email implements Serializable {
	private static final long serialVersionUID = 3802651631846355157L;
	
	private String id;
	// 发送人
	private String sender;
	// 用户名
	private String username;
	// 密码
	private String password;
	// 发给谁
	private String to;
	// 抄送人 多个
	private String cc;
	// 暗送人 多个
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
	// 是否已读
	private boolean isRead;
}
