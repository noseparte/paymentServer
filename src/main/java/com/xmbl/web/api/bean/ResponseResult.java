package com.xmbl.web.api.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResponseResult {
	private int status;
	private String msg;
	private Object result;

}

