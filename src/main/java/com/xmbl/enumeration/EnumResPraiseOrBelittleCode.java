package com.xmbl.enumeration;

public enum EnumResPraiseOrBelittleCode {
	//	0 非赞非踩 1 赞 2 踩 
	NOPRAISEBELITTLE(0), PRAISE(1), BELITTLE(2);

	private EnumResPraiseOrBelittleCode(int status) {
		this.status = status;
	}

	private int status;

    public int value() {
		return status;
	}
}
