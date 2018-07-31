package com.xmbl.enumeration;

public enum EnumResCode {
	SUCCESSFUL(0), SERVER_SUCCESS(2),SERVER_ERROR(-1);

	private EnumResCode(int status) {
		this.status = status;
	}

	private int status;

    public int value() {
		return status;
	}
}
