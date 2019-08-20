package com.xmbl.dao.report;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import com.xmbl.model.report.ReportType;
import com.xmbl.mongo.dao.GeneralDao;

public interface IReportTypeDao extends GeneralDao<ReportType>{

	/**
	 * 通过条件查询总条数
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @return
	 */
	Long countAll(String startDate, String endDate, String status);

	/**
	 * 通过条件查询分页信息
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param sortType
	 * @param sort
	 * @param page
	 * @param size
	 * @return
	 */
	List<ReportType> findPageBy(String startDate, String endDate, String status,
			String sortType, String sort, int page, int size);

	/**
	 * 通过id和 举报结果类型 修改举报信息
	 * @param id
	 * @param reportResultType
	 * @return
	 */
	boolean updateById(String id, String reportResultType);
	
	List<ReportType> find(String type,int pageNo,int pageSize);
	
	Integer findCountByQuery(Query query);
	
	void addType(int sort, int status, String describe, String remark, String type_icon);
	
	void deleteByIDS(String iDS);
	
	void updateType(String id, int sort, int status, String describe, String remark, String type_icon);
	
	ReportType findByType(String report_type);
	
	List<ReportType> findAll();
	
	ReportType findBySort(int sort);
	
	ReportType findByIcon(String type_icon);
	
}
