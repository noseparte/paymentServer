package com.xmbl.model.report;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 * @Author Noseparte
 * @Compile 2018年4月24日 -- 下午6:49:48
 * @Version 1.0
 * @Description  	举报类别	
 */
@Data
@Document
public class ReportType {
	
	@Id
	private String					id;						//主键
	private int 					sort;					//序号
	private String 					describe;				//描述
	private String 					type_icon;				//类别标识
	private String 					remark;					//备注
	private int 					status;					//是否禁用
	private Date 					createTime;				//创建时间	
	private Date 					updateTime;				//修改时间	
	
	
	
}
