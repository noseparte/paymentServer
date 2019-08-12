package com.xmbl.enumeration;

/**
 * 举报原因类型枚举
 */
public enum EnumReportCauseCode {
	REPORT_CAUSE1(1,"1","report_cause1","有导致不适宜的画面或文字"),
	REPORT_CAUSE2(2,"2","report_cause2","有侵权嫌疑"),
	REPORT_CAUSE3(3,"3","report_cause3","诱导、诈骗信息"),
	REPORT_CAUSE4(4,"4","report_cause4","广告或垃圾信息"),
	REPORT_CAUSE5(5,"5","report_cause5","暴力、色情、政治等违反法律法规的内容"),
	REPORT_CAUSE6(6,"6","report_cause6","问题反馈")
	;
	private EnumReportCauseCode(int id, String numcode, String wordcode, String desc) {
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
