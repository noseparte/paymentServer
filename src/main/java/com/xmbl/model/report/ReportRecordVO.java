package com.xmbl.model.report;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xmbl.web.api.bean.PageData;

import lombok.Data;

/**
 * 
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 * @Author Noseparte
 * @Compile 2018年6月26日 -- 下午5:45:45
 * @Version 1.0
 * @Description		举报记录统计信息表
 */
@Data
@Document
public class ReportRecordVO {

	@Id
	private String id;
	private String report_type;  				//举报类型
	private String report_obj_id;  				//举报对象ID
    private String report_extend_info;			// 举报场景补充信息
	//** {举报原因类型 + 次数} 集合 
	private List<PageData> reportList;  				
	private int totalCount;          			//计数
	private Date createTime;
	private Date updateTime;
	private int status;							//举报处理状态
	private Boolean isDelete; // 是否删除(默认为：false)
	 
	public ReportRecordVO() {
		super();
	}

	public ReportRecordVO(String report_type, String report_obj_id, String report_extend_info, List<PageData> reportList, int totalCount, Date createTime,
			int status, Boolean isDelete) {
		super();
		this.report_type = report_type;
		this.report_obj_id = report_obj_id;
		this.report_extend_info = report_extend_info;
		this.reportList = reportList;
		this.totalCount = totalCount;
		this.createTime = createTime;
		this.status = status;
		this.isDelete = isDelete;
	}

	
	
	
	
	
}
