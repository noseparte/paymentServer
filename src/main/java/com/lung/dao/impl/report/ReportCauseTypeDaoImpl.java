package com.lung.dao.impl.report;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.lung.dao.report.ReportCauseTypeDao;
import com.lung.model.report.ReportCauseType;
import com.lung.mongo.dao.GeneralReportDaoImpl;

@Repository
public class ReportCauseTypeDaoImpl extends GeneralReportDaoImpl<ReportCauseType> implements ReportCauseTypeDao{
	
	@Override
	protected Class<ReportCauseType> getEntityClass() {
		return ReportCauseType.class;
	}

	/**
	 * 获取举报原因类型
	 */
	@Override
	public ReportCauseType findByType(String reportCauseType) {
		Query query = new Query();
		if(StringUtils.isNotBlank(reportCauseType) && !StringUtils.trim(reportCauseType).equals("")) {
			query.addCriteria(Criteria.where("sort").is(Integer.parseInt(reportCauseType)));
		}
		ReportCauseType reportCauseTypeVO = this.getMongoTemplate().findOne(query, ReportCauseType.class, "reportCauseType");
		return reportCauseTypeVO;
	}

	/**
	 * 获取所有举报原因类型
	 */
	@Override
	public List<ReportCauseType> findAll() {
		Query query = new Query();
		query.addCriteria(Criteria.where("status").is(0));
		List<ReportCauseType> causeTypeList = this.getMongoTemplate().find(query, ReportCauseType.class, "reportCauseType");
		return causeTypeList;
	}
	
	
	
}
