package com.lung.service.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lung.dao.report.ReportCauseTypeDao;
import com.lung.model.report.ReportCauseType;

@Service
@Transactional
public class ReportCauseTypeService {

	@Autowired
	private ReportCauseTypeDao reportCauseTypeDao;

	public ReportCauseType findByType(String reportCauseType) {
		return reportCauseTypeDao.findByType(reportCauseType);
	}

	public List<ReportCauseType> findAll() {
		return reportCauseTypeDao.findAll();
	}
	
		
	
}
