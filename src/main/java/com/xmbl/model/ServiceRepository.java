package com.xmbl.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.xmbl.mongo.pojo.GeneralBean;

import lombok.Data;

/**
 * 
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 * @Author Noseparte
 * @Compile 2018年9月18日 -- 下午8:48:50
 * @Version 1.0
 * @Description			平台的服务管理
 */
@Data
@Document(collection = "PT_server_repository")
public class ServiceRepository extends GeneralBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String 				serviceId;								//服务编号
	private String 				serviceName;							//服务名称	
	private String 				serviceType;							//服务类型
	private int 				status;									//服务状态
	
	public ServiceRepository() {
		super();
	}

	public ServiceRepository(String serviceId, String serviceName, String serviceType, int status) {
		super();
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.serviceType = serviceType;
		this.status = status;
	}
	

	
	
}
