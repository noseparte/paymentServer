package com.xmbl.enumeration;

/**
 * 举报类型枚举
 */
public enum EnumFeedbackTypeCode {
	RUN_OUT(1,"1","run_out","游戏奔溃"),
	CANOT_FIGHT(2,"2","canot_fight","无法战斗"),
	DATA_ERRR(3,"3","data_err","数据异常"),
	RECOM_EXP(4,"4","recom_exp","体验建议")
	;
	private EnumFeedbackTypeCode(int id, String numcode, String wordcode, String desc) {
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
