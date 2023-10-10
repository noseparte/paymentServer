package com.lung.dao.report;

import java.util.List;

import com.lung.model.report.Report;
import com.lung.mongo.dao.GeneralDao;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  IReportDao 
 * @创建时间:  2017年12月27日 下午12:36:46
 * @修改时间:  2017年12月27日 下午12:36:46
 * @类说明:
 */
public interface IReportDao extends GeneralDao<Report>{

	Report insertReport(Report report);

	List<Report> findByConditions(String reportObjId, String reportType,
			String reportPlayerId, Integer status);

	int countByCauseType(String reportType, String reportObjId, int causeType);
	
}
