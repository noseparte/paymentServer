package com.lung.dao.report;

import java.util.List;

import com.lung.model.report.ReportCauseType;
import com.lung.mongo.dao.GeneralDao;

public interface ReportCauseTypeDao extends GeneralDao<ReportCauseType>{

	ReportCauseType findByType(String reportCauseType);
	
	List<ReportCauseType> findAll();
	
	
}
