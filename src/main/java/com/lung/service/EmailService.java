package com.lung.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: noseparte
 * @Email: 1402614629@qq.com
 * @类名:  EmailService 
 * @创建时间:  2018年1月4日 下午8:44:38
 * @修改时间:  2018年1月4日 下午8:44:38
 * @类说明:
 */
public interface EmailService {

	boolean send(JSONObject jsonObj);

}
