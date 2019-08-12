package com.xmbl.service.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xmbl.dao.impl.report.ReportTypeDaoImpl;
import com.xmbl.dao.report.IReportTypeDao;
import com.xmbl.model.report.ReportType;

@Service
@Transactional
public class ReportTypeService extends ReportTypeDaoImpl{
	@Autowired
	private IReportTypeDao ireportTypeDao;
	
	public Long countAll(String startDate, String endDate, String status) {
		Long count  = ireportTypeDao.countAll(startDate, endDate, status);
		return count;
	}

	public List<ReportType> findPageBy(String startDate, String endDate,
			String status, String sortType, String sort, int page, int size) {
		List<ReportType> reportLst = ireportTypeDao.findPageBy(startDate, endDate,
				status, sortType, sort, page, size);
		return reportLst;
	}

	public boolean updateById(String id, String reportResultType) {
		boolean flag = ireportTypeDao.updateById(id,reportResultType);
		return flag;
	}

	public List<ReportType> find(String type,int pageNo,int pageSize) {
		return ireportTypeDao.find(type,pageNo,pageSize);
	}

	public Integer findCountByQuery(Query countQuery) {
		return ireportTypeDao.findCountByQuery(countQuery);
	}

	public void addType(int sort, int status, String describe, String remark, String type_icon) {
		ireportTypeDao.addType(sort,status,describe,remark,type_icon);
	}

	public void deleteByIDS(String iDS) {
		ireportTypeDao.deleteByIDS(iDS);
	}

	public void updateType(String id, int sort, int status, String describe, String remark, String type_icon) {
		ireportTypeDao.updateType(id,sort,status,describe,remark,type_icon);
	}

	public ReportType findByType(String report_type) {
		return ireportTypeDao.findByType(report_type);
	}

	public List<ReportType> findAll() {
		return ireportTypeDao.findAll();
	}

	public ReportType findBySort(int sort) {
		return ireportTypeDao.findBySort(sort);
	}

	public ReportType findByIcon(String type_icon) {
		return ireportTypeDao.findByIcon(type_icon);
	}

}
