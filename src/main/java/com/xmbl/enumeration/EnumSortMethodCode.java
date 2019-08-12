package com.xmbl.enumeration;

/**
 * 排序方式
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  EnumSortMethodCode 
 * @创建时间:  2017年12月21日 下午8:46:38
 * @修改时间:  2017年12月21日 下午8:46:38
 * @类说明:
 */
public enum EnumSortMethodCode {
	DESC("1","desc","降序"),
	ASC("2","asc","升序")
	;
	private EnumSortMethodCode(String id, String type, String desc) {
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
