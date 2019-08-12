package com.xmbl.enumeration;

/**
 * 举报处理结果类型
 */
public enum EnumReportResultTypeCode {
	// 0 未处理 1 举报证据充足 2 举报证据不足
	REPORT_RESULT_TYPE1(0,"0","0","未处理"),
	// 图片
	REPORT_RESULT_TYPE2(2,"2","1","举报证据充足"),
	REPORT_RESULT_TYPE3(3,"3","2","举报证据不足"),
	;
	private EnumReportResultTypeCode(int id, String numcode, String wordcode, String desc) {
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
