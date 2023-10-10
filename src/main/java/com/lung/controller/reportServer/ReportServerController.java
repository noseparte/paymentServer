package com.lung.controller.reportServer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lung.controller.AbstractController;
import com.lung.dto.ResponseResult;
import com.lung.enumeration.EnumInfrastructureResCode;
import com.lung.enumeration.EnumReportCode;
import com.lung.enumeration.EnumResCode;
import com.lung.model.report.Report;
import com.lung.model.report.ReportCauseType;
import com.lung.model.report.ReportRecordVO;
import com.lung.model.report.ReportType;
import com.lung.service.report.ReportCauseTypeService;
import com.lung.service.report.ReportRecordService;
import com.lung.service.report.ReportService;
import com.lung.service.report.ReportTypeService;
import com.lung.web.api.bean.PageData;
import com.lung.web.api.bean.Route;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  ReportServerController 
 * @创建时间:  2017年12月26日 下午7:58:53
 * @修改时间:  2017年12月26日 下午7:58:53
 * @类说明: 举报服务器
 */
@Slf4j
@Controller
@RequestMapping(value=Route.PATH + Route.Report.PATH)
public class ReportServerController extends AbstractController {
	
	@Autowired
	private ReportService reportService;
	@Autowired
	private ReportTypeService reportTypeService;
	@Autowired
	private ReportCauseTypeService reportCauseTypeService;
	@Autowired
	private ReportRecordService reportRecordService;
	
	/**
	 * 举报新增
	 * 
	 * @param reportPlayerId
	 * @param reportPlayerName
	 * @param reportPlayerImg
	 * @param reportType
	 * @param reportObjId
	 * @param report_extend_info
	 * @param reportCauseType
	 * @param reportContent
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=Route.Report.ADD, method=RequestMethod.POST)
	public ResponseResult addReport(
			@RequestParam(value="report_player_id",required= false) String reportPlayerId,// 举报玩家id
			@RequestParam(value="report_player_name",required= false) String reportPlayerName,// 举报玩家姓名
			@RequestParam(value="report_player_img",required= false) String reportPlayerImg,// 举报玩家头像
			@RequestParam(value="report_type",required= false) String reportType,// 举报类型  1-8 
			@RequestParam(value="report_obj_id",required= false) String reportObjId,// 举报对象
			@RequestParam(value="report_extend_info",required= false) String report_extend_info,// 举报场景拓展信息
			@RequestParam(value="report_cause_type",required= false) String reportCauseType,// 举报原因 1-6
			@RequestParam(value="report_content",required= false) String reportContent// 举报描述
	) {
		try {
			if (EnumReportCode.REPORT_WENTIFANKUI.getNumcode().equals(reportType)) {
				log.info("问题反馈开始。。。");
				Assert.isTrue(StringUtils.isNotBlank(reportPlayerId), EnumInfrastructureResCode.FEEDBACK_PLATERID_NOT_NULL.code());
				Assert.isTrue(StringUtils.isNotBlank(reportPlayerName), EnumInfrastructureResCode.FEEDBACK_PLATER_NAME_NOT_NULL.code());
				//Assert.isTrue(StringUtils.isNotBlank(reportPlayerImg), "举报玩家头像不能为空");
				Assert.isTrue(StringUtils.isNotBlank(reportType), EnumInfrastructureResCode.FEEDBACK_TYPE_NOT_NULL.code());
				Assert.isTrue(StringUtils.isNotBlank(reportObjId), EnumInfrastructureResCode.FEEDBACK_OBJECT_NOT_NULL.code());
				Assert.isTrue(StringUtils.isNotBlank(reportCauseType), EnumInfrastructureResCode.FEEDBACK_REASON_TYPE_NOT_NULL.code());
				ReportType type = reportTypeService.findBySort(Integer.parseInt(reportType));
				Assert.isTrue(type != null, EnumInfrastructureResCode.REPORT_TYPE_NOT_NULL.code());
				ReportCauseType causeTypeVO = reportCauseTypeService.findByType(reportCauseType);
//				EnumReportCauseCode enumReportCauseCode = ReportUtil.getReportCauseCodeInfo(reportCauseType);
				Assert.isTrue(causeTypeVO != null, EnumInfrastructureResCode.REPORT_TYPE_NOT_EXISTS.code());
				// 查询当前用户举报该对象信息
				//				Integer status = 0;
				//				Report reportInfo = reportService.findByConditions(reportObjId,reportType,reportPlayerId,status);
				//				Assert.isTrue(reportInfo == null, "用户已举报过该内容");
				// 举报对象构建
				Report report = new Report(null, reportPlayerId,reportPlayerName,reportPlayerImg,reportType,reportObjId,report_extend_info,reportCauseType,reportContent);
				report = reportService.insert(report);
				Assert.isTrue(report!=null, EnumInfrastructureResCode.REPORT_STORAGE_FAILURE.code());
				log.info("问题反馈结束。。。");
				return successJson(report);
			} else {
				log.info("举报开始。。。");
				Assert.isTrue(StringUtils.isNotBlank(reportPlayerId), EnumInfrastructureResCode.REPORT_PLAYERID_NOT_NULL.code());
				Assert.isTrue(StringUtils.isNotBlank(reportPlayerName), EnumInfrastructureResCode.FEEDBACK_PLATER_NAME_NOT_NULL.code());
				//Assert.isTrue(StringUtils.isNotBlank(reportPlayerImg), "举报玩家头像不能为空");
				Assert.isTrue(StringUtils.isNotBlank(reportType), EnumInfrastructureResCode.REPORT_TYPE_NOT_NULL.code());
				Assert.isTrue(StringUtils.isNotBlank(reportObjId), EnumInfrastructureResCode.REPORT_OBJECT_NOT_NULL.code());
				Assert.isTrue(StringUtils.isNotBlank(reportCauseType), EnumInfrastructureResCode.REPORT_TYPE_NOT_NULL.code());
				ReportType type = reportTypeService.findBySort(Integer.parseInt(reportType));
				Assert.isTrue(type != null, EnumInfrastructureResCode.REPORT_TYPE_NOT_EXISTS.code());
				ReportCauseType causeTypeVO = reportCauseTypeService.findByType(reportCauseType);
				Assert.isTrue(causeTypeVO != null, EnumInfrastructureResCode.REPORT_REASON_TYPE_NOT_EXISTS.code());
				// 查询当前用户举报该对象信息
				Integer status = 0;
				Report reportInfo = reportService.findByConditions(reportObjId,reportType,reportPlayerId,status);
				Assert.isTrue(reportInfo == null, EnumInfrastructureResCode.CONTENT_HAVE_REPORTED.code());
				// 举报对象构建
				Report report = new Report(null, reportPlayerId,reportPlayerName,reportPlayerImg,reportType,reportObjId,report_extend_info,reportCauseType,reportContent);
				report = reportService.insert(report);
				Assert.isTrue(report!=null, EnumInfrastructureResCode.REPORT_STORAGE_FAILURE.code());
				// 新增举报信息统计记录
				ReportRecordVO record = reportRecordService.findByCondition(reportType,reportObjId);
				
				List<PageData> reportList = new ArrayList<PageData>();
				PageData pd = null;
				int totalCount = 0;		//统计当前关卡被举报的总次数
				//拼装 reportList {举报原因类型 + 次数} 集合 
				List<ReportCauseType> causeTypeList = reportCauseTypeService.findAll(); 
				for(ReportCauseType causeType : causeTypeList) {
					pd = new PageData();
					int causeTypeSort = causeType.getSort();
					int causeCount = reportService.countByCauseType(reportType,reportObjId,causeTypeSort);
					totalCount += causeCount; 
					pd.put("report_cause_type", causeTypeSort);
					pd.put("causeCount", causeCount);
					reportList.add(pd);
				}
				if(record != null) {
					reportRecordService.update(record.getId(),reportList,totalCount);
				}else {
					record = new ReportRecordVO(reportType,reportObjId,report_extend_info,reportList,totalCount,new Date(),1,Boolean.FALSE);
					reportRecordService.save(record);
				}
				log.info("举报结束。。。");
				return successJson(report);
			}
		} catch (Exception e) {
			if (EnumReportCode.REPORT_WENTIFANKUI.getNumcode().equals(reportType)) {
				log.error("问题反馈失败,失败原因:"+e.getMessage());
				return errorJson(EnumResCode.SERVER_ERROR.value(),"问题反馈出错了,错误信息为:"+e.getMessage());
			} else {
				log.error("举报失败,失败原因:"+e.getMessage());
				return errorJson(EnumResCode.SERVER_ERROR.value(),"举报出错了,错误信息为:"+e.getMessage());
			}
			
		}
	}
	
}
