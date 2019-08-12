package com.xmbl.enumeration;

/**
 * 举报类型枚举
 */
public enum EnumReportCode {
	// 故事集
	REPORT_STORYS(1,"1","report_storys","故事集举报"),
	// 图片
	REPORT_IMG(2,"2","report_img","图片举报"),
	// pk关卡
	REPORT_PK_GUANKA(3,"3","report_pk_guanka","pk关卡举报"),
	// 关卡集
	REPORT_GUANKA_JI(4,"4","report_guanka_ji","关卡集举报"),
	// pk关卡集
	REPORT_PK_GUANKA_JI(5,"5","report_pk_guanka_ji","pk关卡集举报"),
	// 主题
	REPORT_ZHUTI(6,"6","report_zhuti","主题举报"),
	// 玩家
	REPORT_WANJIA(7,"7","report_wanjia","玩家举报"),
	// 问题反馈
	REPORT_WENTIFANKUI(8,"8","report_wentifankui","问题反馈举报"),
	// 挑战关卡举报
	REPORT_FIGHTGUANQIA(9,"9","report_fightguanqia","挑战关卡举报")
	;
	private EnumReportCode(int id, String numcode, String wordcode, String desc) {
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
