package com.lung.dao.report;

import java.util.List;

import com.lung.model.report.ReportRecordVO;
import com.lung.mongo.dao.GeneralDao;
import com.lung.web.api.bean.PageData;

public interface IReportRecordDao extends GeneralDao<ReportRecordVO>{

	ReportRecordVO findByCondition(String reportType, String reportObjId);
	
	void save(ReportRecordVO record);
	
	void update(String id,List<PageData> reportList, int count);
}
