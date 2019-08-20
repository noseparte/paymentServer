package com.xmbl.dao.report;

import java.util.List;

import com.xmbl.model.report.ReportRecordVO;
import com.xmbl.mongo.dao.GeneralDao;
import com.xmbl.web.api.bean.PageData;

public interface IReportRecordDao extends GeneralDao<ReportRecordVO>{

	ReportRecordVO findByCondition(String reportType, String reportObjId);
	
	void save(ReportRecordVO record);
	
	void update(String id,List<PageData> reportList, int count);
}
