package com.xmbl.dao.report;

import java.util.List;

import com.xmbl.model.report.ReportCauseType;
import com.xmbl.mongo.dao.GeneralDao;

public interface ReportCauseTypeDao extends GeneralDao<ReportCauseType>{

	ReportCauseType findByType(String reportCauseType);
	
	List<ReportCauseType> findAll();
	
	
}
