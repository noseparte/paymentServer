package com.xmbl.util.email;

public enum EnumEmailInfoCode {
	qq(1,"1","qq","2583428549@qq.com","2583428549@qq.com","azscoqxhwvflechb","扣扣邮箱"),
	sina(2,"2","sina","ugcapp@sina.com","ugcapp@sina.com","MPgame2018","新浪邮箱"),
	y163(3,"3","163","xmbladmin@163.com","xmbladmin@163.com","xmbl123456","163邮箱")
	;
	private EnumEmailInfoCode(int id,String numcode,String type,String mail_sender,String mail_username,String mail_password,String desc) {
		this.id = id;
		this.numcode = numcode;
		this.type = type;
		this.mail_sender = mail_sender;
		this.mail_username = mail_sender;
		this.mail_password = mail_password;
		this.desc = desc;
	}
	
	private int id;
	private String numcode;
	private String type;
	private String mail_sender;
	private String mail_username;
	private String mail_password;
	private String desc;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumcode() {
		return numcode;
	}
	public void setNumcode(String numcode) {
		this.numcode = numcode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMail_sender() {
		return mail_sender;
	}
	public void setMail_sender(String mail_sender) {
		this.mail_sender = mail_sender;
	}
	public String getMail_username() {
		return mail_username;
	}
	public void setMail_username(String mail_username) {
		this.mail_username = mail_username;
	}
	public String getMail_password() {
		return mail_password;
	}
	public void setMail_password(String mail_password) {
		this.mail_password = mail_password;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
