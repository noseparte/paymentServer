package com.xmbl.enumeration;

public enum EnumResPraiseOrBelittleTypeCode {
	NOSTATUS(-1),// 状态不明
	NOPRAISE(1),// 取消赞
	PRAISE(2), // 赞
	NOBELITTLE(3), // 取消踩
	BELITTLE(4);// 踩

	private EnumResPraiseOrBelittleTypeCode(int status) {
		this.status = status;
	}

	private int status;

    public int value() {
		return status;
	}
}
