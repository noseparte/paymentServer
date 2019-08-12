package com.xmbl.enumeration;

/**
 * 排序类型
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  EnumSortTypeCode 
 * @创建时间:  2017年12月21日 下午8:46:25
 * @修改时间:  2017年12月21日 下午8:46:25
 * @类说明:
 */
public enum EnumSortTypeCode {
	CREATE_DATE("1","create_date","创建时间类型"),
	PRAISE_CNT("2","praisecnt","点赞数类型"),
	REPLY_CNT("3","replycnt","回复数类型")
	;
	private EnumSortTypeCode(String id, String type, String desc) {
		this.id = id;
		this.type = type;
		this.desc = desc;
	}
	private String id;
	private String type;
	private String desc;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
