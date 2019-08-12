package com.xmbl.controller;

import com.xmbl.dto.ResponseResult;
import com.xmbl.enumeration.EnumResCode;

public abstract class AbstractController {


	protected ResponseResult successJson(Object data) {
		ResponseResult result = new ResponseResult();
		result.setStatus(EnumResCode.SUCCESSFUL.value());
		result.setMsg("ok");
		result.setResult(data);
		return result;
	}
	protected ResponseResult DatatoJson(Object data) {
		ResponseResult result = new ResponseResult();
		result.setResult(data);
		return result;
	}
	protected ResponseResult successSaveJson(Object data) {
		ResponseResult result = new ResponseResult();
		result.setStatus(EnumResCode.SERVER_SUCCESS.value());
		result.setMsg("ok");
		result.setResult(data);
		return result;
	}
	
	protected ResponseResult successSaveJson() {
		return successJson(null);
	}
	
	protected ResponseResult successJson() {
		return successJson(null);
	}

	protected ResponseResult errorJson(int status, String message) {
		ResponseResult result = new ResponseResult();
		result.setStatus(status);
		result.setMsg(message);
		return result;
	}
}
