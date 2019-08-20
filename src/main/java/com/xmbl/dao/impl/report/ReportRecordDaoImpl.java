package com.xmbl.dao.impl.report;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.xmbl.dao.report.IReportRecordDao;
import com.xmbl.model.report.ReportRecordVO;
import com.xmbl.mongo.dao.GeneralReportDaoImpl;
import com.xmbl.web.api.bean.PageData;

@Repository
public class ReportRecordDaoImpl extends GeneralReportDaoImpl<ReportRecordVO> implements IReportRecordDao {

	@Override
	protected Class<ReportRecordVO> getEntityClass() {
		return ReportRecordVO.class;
	}

	@Override
	public ReportRecordVO findByCondition(String reportType, String reportObjId) {
		Query query = new Query();
		if (StringUtils.isNotBlank(reportType) && !StringUtils.trim(reportType).equals("")) {
			query.addCriteria(Criteria.where("report_type").is(reportType));
		}
		if (StringUtils.isNotBlank(reportObjId) && !StringUtils.trim(reportObjId).equals("")) {
			query.addCriteria(Criteria.where("report_obj_id").is(reportObjId));
		}
		ReportRecordVO record = this.getMongoTemplate().findOne(query, ReportRecordVO.class, "reportRecordVO");
		return record;
	}

	@Override
	public void save(ReportRecordVO record) {
		this.getMongoTemplate().save(record);
	}

	@Override
	public void update(String id, List<PageData> reportList, int count) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("totalCount", count);
		update.set("reportList", reportList);
		update.set("updateTime", new Date());
		this.getMongoTemplate().updateFirst(query, update, ReportRecordVO.class, "reportRecordVO");
	}

}
