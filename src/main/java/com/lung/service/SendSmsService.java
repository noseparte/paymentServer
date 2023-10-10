package com.lung.service;

import com.lung.dto.ResponseResult;

/**
 * @author: noseparte
 * @Email: 1402614629@qq.com
 * @类名:  SendSmsService 
 * @创建时间:  2018年1月2日 下午8:32:56
 * @修改时间:  2018年1月2日 下午8:32:56
 * @类说明:
 */
public interface SendSmsService {

	ResponseResult sendSms(String jsonData);

}
