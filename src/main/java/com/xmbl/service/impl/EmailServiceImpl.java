package com.xmbl.service.impl;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.xmbl.service.EmailService;
import com.xmbl.util.email.EmailUtils;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  EmailServiceImpl 
 * @创建时间:  2018年1月4日 下午8:45:01
 * @修改时间:  2018年1月4日 下午8:45:01
 * @类说明:
 */
@Service
public class EmailServiceImpl implements EmailService {

	@Override
	public boolean send(JSONObject jsonObj) {
		return EmailUtils.sendMailForSystem(jsonObj);
	}

}
