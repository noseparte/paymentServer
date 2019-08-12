package com.xmbl.enumeration;

/**
 * 举报处理结果状态
 */
public enum EnumReportStatusCode {
	// 未处理举报
	REPORT_STATUS_NO(0,"0","no","未处理举报"),
	// 已处理举报
	REPORT_STATUS_YES(1,"1","yes","已处理举报"),
	;
	private EnumReportStatusCode(int id, String numcode, String wordcode, String desc) {
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
