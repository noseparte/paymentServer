package com.xmbl.service.report;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xmbl.dao.report.IReportDao;
import com.xmbl.model.report.Report;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  ReportService 
 * @创建时间:  2017年12月27日 下午12:34:22
 * @修改时间:  2017年12月27日 下午12:34:22
 * @类说明: 举报服务
 */
@Service
public class ReportService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
	
	@Autowired
	private IReportDao ireportDao;
	
	public Report insert(Report report) {
		try {
			if (report==null) {
				return null;
			}
			if (StringUtils.isNotBlank(report.getId())) {
				LOGGER.info("举报已经创建过了");
				return report;
			}
			report = ireportDao.insertReport(report);
		} catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("新增举报出错了, 错误信息为:"+e.getMessage());
		}
		return report;
	}

	public Report findByConditions(String reportObjId, String reportType,
			String reportPlayerId, Integer status) {
		List<Report> reportLst = ireportDao.findByConditions(reportObjId, reportType, reportPlayerId, status);
		if (reportLst == null || reportLst.size() == 0) {
			return null;
		}
		return reportLst.get(0);
	}

	public int countByCauseType(String reportType, String reportObjId, int causeType) {
		return (int) ireportDao.countByCauseType(reportType,reportObjId,causeType);
	}
	
}
