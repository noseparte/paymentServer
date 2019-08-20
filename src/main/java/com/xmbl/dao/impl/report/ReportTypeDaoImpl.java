package com.xmbl.dao.impl.report;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.xmbl.dao.report.IReportTypeDao;
import com.xmbl.model.report.ReportType;
import com.xmbl.mongo.dao.GeneralReportDaoImpl;
import com.xmbl.util.DateUtils;

@Repository
public class ReportTypeDaoImpl extends GeneralReportDaoImpl<ReportType> implements IReportTypeDao {
	
	@Override
	protected Class<ReportType> getEntityClass() {
		return ReportType.class;
	}

	@Override
	public Long countAll(String startDate, String endDate, String status) {
		Query query = new Query();
		if (!"-1".equals(status)) {
			query.addCriteria(Criteria.where("status").is(Integer.parseInt(status)));
		}
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			Date sDate = DateUtils.parseDate(startDate, "yyyy-MM-dd HH:mm:ss");
			Date eDate = DateUtils.parseDate(endDate, "yyyy-MM-dd HH:mm:ss");
			query.addCriteria(Criteria.where("create_date").gte(sDate).lte(eDate));
		} else if (StringUtils.isNotBlank(startDate)){
			Date sDate = DateUtils.parseDate(startDate, "yyyy-MM-dd HH:mm:ss");
			query.addCriteria(Criteria.where("create_date").gte(sDate));
		} else if (StringUtils.isNotBlank(endDate)) {
			Date eDate = DateUtils.parseDate(endDate, "yyyy-MM-dd HH:mm:ss");
			query.addCriteria(Criteria.where("create_date").lte(eDate));
		}
		
		Long count = this.getMongoTemplate().count(query, "reportType");
		return count;
	}

	
	@Override
	public List<ReportType> findPageBy(String startDate, String endDate,
			String status, String sortType, String sort, int page, int size) {
		Query query = new Query();
		if (!"-1".equals(status)) {
			query.addCriteria(Criteria.where("status").is(Integer.parseInt(status)));
		}
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			Date sDate = DateUtils.parseDate(startDate, "yyyy-MM-dd HH:mm:ss");
			Date eDate = DateUtils.parseDate(endDate, "yyyy-MM-dd HH:mm:ss");
			query.addCriteria(Criteria.where("create_date").gte(sDate).lte(eDate));
		} else if (StringUtils.isNotBlank(startDate)){
			Date sDate = DateUtils.parseDate(startDate, "yyyy-MM-dd HH:mm:ss");
			query.addCriteria(Criteria.where("create_date").gte(sDate));
		} else if (StringUtils.isNotBlank(endDate)) {
			Date eDate = DateUtils.parseDate(endDate, "yyyy-MM-dd HH:mm:ss");
			query.addCriteria(Criteria.where("create_date").lte(eDate));
		}
		if (StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(sortType)) {
			if ("desc".equalsIgnoreCase(sort)) {
				query.with(new Sort(Sort.Direction.DESC, sortType));
			} else {
				query.with(new Sort(Sort.Direction.ASC, sortType));
			}
		}
		query.skip((page-1)*size);
		query.limit(size);
		List<ReportType> reportLst = this.getMongoTemplate().find(query, ReportType.class, "reportType");
		return reportLst;
	}


	@Override
	public boolean updateById(String id, String reportResultType) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(id));
			Update update = new Update();
			update.set("report_result_type", Integer.parseInt(reportResultType));
			update.set("status", 1);
			this.getMongoTemplate().updateFirst(query, update, "reportType");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public List<ReportType> find(String type,int pageNo,int pageSize) {
		Query query = new Query();
		if(!StringUtils.trim(type).equals("") && !StringUtils.isBlank(type)) {
			query.addCriteria(Criteria.where("type_icon").is(type));
		}
		// 分页
		query.skip((pageNo - 1) * pageSize).limit(pageSize);
		List<ReportType> reportLst = this.getMongoTemplate().find(query, ReportType.class, "reportType");
		return reportLst;
	}


	@Override
	public Integer findCountByQuery(Query query) {
		return (int) this.getMongoTemplate().count(query, "reportType");
	}


	@Override
	public void addType(int sort, int status, String describe, String remark, String type_icon) {
		ReportType type = new ReportType();
		type.setSort(sort);
		type.setDescribe(describe);
		type.setRemark(remark);
		type.setStatus(status);
		type.setType_icon(type_icon);
		type.setCreateTime(new Date());
		this.getMongoTemplate().save(type, "reportType");
	}


	@Override
	public void deleteByIDS(String iDS) {
		String[] ids = iDS.split(",");
		for(String id : ids) {
			ReportType type = findOneById(id);
			if(null != type) {
			    this.getMongoTemplate().remove(type, "reportType");
			}
		}
	}

	@Override
	public void updateType(String id, int sort, int status, String describe, String remark, String type_icon) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		Update update = new Update();
		update.set("sort", sort);
		update.set("status", status);
		update.set("describe", describe);
		update.set("remark", remark);
		update.set("type_icon", type_icon);
		update.set("updateTime", new Date());
		this.getMongoTemplate().updateFirst(query, update, "reportType");
	}


	@Override
	public ReportType findByType(String report_type) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sort").is(Integer.parseInt(report_type)));
		ReportType reportType = this.getMongoTemplate().findOne(query, ReportType.class, "reportType");
		return reportType;
	}


	@Override
	public List<ReportType> findAll() {
		Query query = new Query();
		query.addCriteria(Criteria.where("status").is(0));
		query.with(new Sort(new Order(Direction.DESC,"createTime")));
		List<ReportType> typeList = this.getMongoTemplate().find(query, ReportType.class, "reportType");
		return typeList;
	}


	@Override
	public ReportType findBySort(int sort) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sort").is(sort));
		ReportType type = this.getMongoTemplate().findOne(query, ReportType.class, "reportType");
		return type;
	}


	@Override
	public ReportType findByIcon(String type_icon) {
		Query query = new Query();
		query.addCriteria(Criteria.where("type_icon").is(type_icon));
		ReportType type = this.getMongoTemplate().findOne(query, ReportType.class, "reportType");
		return type;
	}

	
}
