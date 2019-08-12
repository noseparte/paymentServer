package com.xmbl.service.impl;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.xmbl.dto.ResponseResult;
import com.xmbl.service.SendSmsService;
import com.xmbl.util.ResponseResultUtil;
import com.xmbl.util.SmsUtil;
import com.xmbl.util.XmblLoggerUtil;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  SendSmsServiceImpl 
 * @创建时间:  2018年1月2日 下午8:33:21
 * @修改时间:  2018年1月2日 下午8:33:21
 * @类说明:
 */
@Service
public class SendSmsServiceImpl implements SendSmsService {

	@Override
	public ResponseResult sendSms(String jsonData) {
		try {
			JSONObject jsonDataObj = (JSONObject) JSONObject.parse(jsonData);
			String appId = (String) jsonDataObj.get("appId");
			String mobiles = (String) jsonDataObj.get("mobiles");
			String templateId = (String) jsonDataObj.get("templateId");
			String params = (String) jsonDataObj.get("params");
			String [] paramArr = params.split(",");
			ResponseResult responseResult = SmsUtil.sendSmsParamByPro(appId, mobiles, templateId, paramArr);
			return responseResult;
		} catch (Exception e) {
			XmblLoggerUtil.error("报错了,错误信息为:",e.getMessage());
			return ResponseResultUtil.errorJson(e.getMessage());
		}
		
	}

}
