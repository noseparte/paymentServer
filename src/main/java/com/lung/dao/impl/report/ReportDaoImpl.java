package com.lung.dao.impl.report;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.lung.dao.report.IReportDao;
import com.lung.model.report.Report;
import com.lung.mongo.dao.GeneralReportDaoImpl;

/**
 * 
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 * 
 * @Author Noseparte
 * @Compile 2018年8月21日 -- 下午5:17:14
 * @Version 1.0
 * @Description
 */
@Repository
public class ReportDaoImpl extends GeneralReportDaoImpl<Report> implements IReportDao {

	@Override
	protected Class<Report> getEntityClass() {
		return Report.class;
	}

	@Override
	public Report insertReport(Report report) {
		this.getMongoTemplate().insert(report, "report");
		return report;
	}

	@Override
	public List<Report> findByConditions(String reportObjId, String reportType, String reportPlayerId, Integer status) {
		Query query = new Query();
		query.addCriteria(Criteria.where("report_obj_id").is(reportObjId));
		query.addCriteria(Criteria.where("report_type").is(reportType));
		query.addCriteria(Criteria.where("report_player_id").is(reportPlayerId));
		query.addCriteria(Criteria.where("status").is(status));
		List<Report> reportLst = this.getMongoTemplate().find(query, Report.class, "report");
		return reportLst;
	}

	/**
	 * 统计原因类型所占总数
	 */
	@Override
	public int countByCauseType(String reportType, String reportObjId, int causeType) {
		Query query = new Query();
		if (StringUtils.isNotBlank(reportType) && !StringUtils.trim(reportType).equals("")) {
			query.addCriteria(Criteria.where("report_type").is(reportType));
		}
		if (StringUtils.isNotBlank(reportObjId) && !StringUtils.trim(reportObjId).equals("")) {
			query.addCriteria(Criteria.where("report_obj_id").is(reportObjId));
		}
		query.addCriteria(Criteria.where("report_cause_type").is(String.valueOf(causeType)));
		return (int) this.getMongoTemplate().count(query, "report");
	}

}
