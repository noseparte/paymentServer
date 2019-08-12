package com.xmbl.enumeration;

/**
 * 举报类型枚举
 */
public enum EnumDeviceTypeCode {
	ANDROID(1,"1","android","安卓"),
	IOS(2,"2","ios","苹果"),
	PC(3,"3","pc","电脑")
	;
	private EnumDeviceTypeCode(int id, String numcode, String wordcode, String desc) {
		this.id = id;
		this.numcode = numcode;
		this.wordcode = wordcode;
		this.desc = desc;
	}
	private int id;
	private String numcode;
	private String wordcode;
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
	public String getWordcode() {
		return wordcode;
	}
	public void setWordcode(String wordcode) {
		this.wordcode = wordcode;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
