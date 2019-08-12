package com.xmbl.enumeration;

public enum EnumGameServerResCode {
	FAIL					("0", "失败"),
	SUCCESS		            ("1", "成功"),
	PARAMETER_ERROR		    ("2", "参数错误"),
	ORDER_ACCOUNTED	    	("11010", "参数错误");

	private EnumGameServerResCode(String status, String description) {
		this.status = status;
		this.description = description;
	}
	
	private String status;
	private String description;

	public String value() {
		return status;
	}
	public String getDescription() {
		return description;
	}
}
