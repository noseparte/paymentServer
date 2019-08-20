package com.xmbl.controller.reportServer;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmbl.controller.AbstractController;
import com.xmbl.dto.ResponseResult;
import com.xmbl.enumeration.EnumDeviceTypeCode;
import com.xmbl.enumeration.EnumFeedbackTypeCode;
import com.xmbl.enumeration.EnumInfrastructureResCode;
import com.xmbl.enumeration.EnumResCode;
import com.xmbl.model.problem.FeedbackProblem;
import com.xmbl.service.report.FeedbackProblemService;
import com.xmbl.web.api.bean.Route;

/**
 * 
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  FeedbackController 
 * @创建时间:  2018年5月11日 下午2:17:05
 * @修改时间:  2018年5月11日 下午2:17:05
 * @类说明:
 */
@Controller
@RequestMapping(value=Route.PATH + Route.Feedback.PATH)
public class FeedbackController extends AbstractController {
	
	private static Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);
	
	@Autowired
	private FeedbackProblemService feedbackProblemService;
	
	/**
	 * 问题反馈新增
	 * 
	 * @param player_id
	 * @param player_name
	 * @param player_img
	 * @param device_type
	 * @param feedback_type
	 * @param app_version
	 * @param feedback_content
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=Route.Feedback.ADD, method=RequestMethod.POST)
	public ResponseResult add(
			@RequestParam(value="player_id",required= false) String player_id,// 玩家id
			@RequestParam(value="player_name",required= false) String player_name,// 玩家姓名
			@RequestParam(value="player_img",required= false) String player_img,// 玩家头像
			@RequestParam(value="device_type",required= false) String device_type,// 设备类型 1 android  2 ios  3 pc
			@RequestParam(value="feedback_type",required= false) String feedback_type, // 反馈类型 1 游戏奔溃 2 无法战斗 3 数据异常 4 体验建议
			@RequestParam(value="app_version",required= false) String app_version,// app 版本
			@RequestParam(value="feedback_content",required= false) String feedback_content// 反馈内容
	) {
		try {
			LOGGER.info("问题反馈开始。。。");
			Assert.isTrue(StringUtils.isNotBlank(player_id), EnumInfrastructureResCode.FEEDBACK_PLATERID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(player_name), EnumInfrastructureResCode.FEEDBACK_PLATER_NAME_NOT_NULL.code());
			//Assert.isTrue(StringUtils.isNotBlank(player_img), "问题反馈玩家头像不能为空");
			Assert.isTrue(StringUtils.isNotBlank(device_type), EnumInfrastructureResCode.FEEDBACK_DEVICE_TYPE_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(feedback_type), EnumInfrastructureResCode.FEEDBACK_TYPE_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(app_version), EnumInfrastructureResCode.APP_VERSION_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(feedback_content), EnumInfrastructureResCode.FEEDBACK_CONTENT_NOT_NULL.code());
			// 验证设备类型
			boolean exitDeviceType = false;
			for (EnumDeviceTypeCode enumDeviceTypeCode: EnumDeviceTypeCode.values()) {
				if (enumDeviceTypeCode.getNumcode().equals(device_type)) {
					exitDeviceType = true;
					break;
				}
			}
			Assert.isTrue(exitDeviceType, EnumInfrastructureResCode.FEEDBACK_DEVICE_TYPE_NOT_EXISTS.code());
			// 问题反馈类型
			boolean exitFeedbackType = false;
			for (EnumFeedbackTypeCode enumfeedbacktypecode: EnumFeedbackTypeCode.values()) {
				if (enumfeedbacktypecode.getNumcode().equals(feedback_type)) {
					exitFeedbackType = true;
					break;
				}
			}
			Assert.isTrue(exitFeedbackType, EnumInfrastructureResCode.FEEDBACK_TYPE_NOT_EXISTS.code());
			FeedbackProblem feedbackProblem = new FeedbackProblem();
			feedbackProblem.setCreate_by(player_id);
			feedbackProblem.setUpdate_by(player_id);
			Date now = new Date();
			feedbackProblem.setCreate_date(now);
			feedbackProblem.setUpdate_date(now);
			feedbackProblem.setPlayer_id(player_id);
			feedbackProblem.setPlayer_name(player_name);
			feedbackProblem.setPlayer_img(player_img);
			feedbackProblem.setDevice_type(Integer.parseInt(device_type));
			feedbackProblem.setFeedback_type(Integer.parseInt(feedback_type));
			feedbackProblem.setApp_version(app_version);
			feedbackProblem.setFeedback_content(feedback_content);
			feedbackProblem = feedbackProblemService.add(feedbackProblem);
			Assert.isTrue(feedbackProblem != null, EnumInfrastructureResCode.FEEDBACK_FAILURE.code());
			LOGGER.info("问题反馈结束。。。");
			return successJson(feedbackProblem);
		} catch (Exception e) {
			LOGGER.error("问题反馈失败,失败原因:"+e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(),"问题反馈出错了,错误信息为:"+e.getMessage());
		}
	}
	
}
