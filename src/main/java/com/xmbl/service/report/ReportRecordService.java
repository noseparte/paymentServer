package com.xmbl.service.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xmbl.dao.report.IReportRecordDao;
import com.xmbl.model.report.ReportRecordVO;
import com.xmbl.web.api.bean.PageData;

@Service
@Transactional
public class ReportRecordService {

	@Autowired
	private IReportRecordDao reportRecordDao;

	public ReportRecordVO findByCondition(String reportType, String reportObjId) {
		return reportRecordDao.findByCondition(reportType,reportObjId);
	}

	public void save(ReportRecordVO record) {
		reportRecordDao.save(record);
	}

	public void update(String id, List<PageData> reportList, int totalCount) {
		reportRecordDao.update(id,reportList,totalCount);
	}
	
	
}
