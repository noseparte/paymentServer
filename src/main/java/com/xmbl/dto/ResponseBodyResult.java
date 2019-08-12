package com.xmbl.dto;

public class ResponseBodyResult<T> {
	private boolean isSuccess;
	private String successMsg;
	private String errorMsg;
	private T t;
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getSuccessMsg() {
		return successMsg;
	}
	public void setSuccessMsg(String successMsg) {
		this.isSuccess = Boolean.TRUE;
		this.successMsg = successMsg;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.isSuccess = Boolean.FALSE;
		this.errorMsg = errorMsg;
	}
	public T getT() {
		return t;
	}
	public void setT(T t) {
		this.t = t;
	}
	
	
}
