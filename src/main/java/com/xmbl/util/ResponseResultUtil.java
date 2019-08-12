package com.xmbl.util;

import com.xmbl.dto.ResponseResult;
import com.xmbl.enumeration.EnumResCode;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  ResponseResultUtil 
 * @创建时间:  2018年1月2日 下午7:42:05
 * @修改时间:  2018年1月2日 下午7:42:05
 * @类说明: 响应返回结果对象工具类
 */
public class ResponseResultUtil {
	
	/**
	 * 正确时返回消息体
	 * @param data
	 * @return
	 */
	public static ResponseResult successJson(Object data) {
		ResponseResult result = new ResponseResult();
		result.setStatus(EnumResCode.SUCCESSFUL.value());
		result.setMsg("ok");
		result.setResult(data);
		return result;
	}
	
	/**
	 * 报错时返回消息体
	 * @param message
	 * @return
	 */
	public static ResponseResult errorJson(String message) {
		ResponseResult result = new ResponseResult();
		result.setStatus(EnumResCode.SERVER_ERROR.value());
		result.setMsg(message);
		return result;
	}
}
