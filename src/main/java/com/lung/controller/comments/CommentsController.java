package com.lung.controller.comments;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lung.constant.CommonConstant;
import com.lung.controller.AbstractController;
import com.lung.dto.Pagination;
import com.lung.dto.ResponseResult;
import com.lung.dto.comments.CommentsDto;
import com.lung.enumeration.EnumCommentCode;
import com.lung.enumeration.EnumGameServerResCode;
import com.lung.enumeration.EnumInfrastructureResCode;
import com.lung.enumeration.EnumResCode;
import com.lung.enumeration.EnumSortMethodCode;
import com.lung.enumeration.EnumSortTypeCode;
import com.lung.model.comments.Comments;
import com.lung.service.comments.CommentTypeService;
import com.lung.service.comments.CommentsReplysService;
import com.lung.service.comments.CommentsService;
import com.lung.util.DateUtils;
import com.lung.util.LetterUtils;
import com.lung.web.api.bean.Route;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名: StoryCommentsController
 * @创建时间: 2017年12月21日 下午6:01:45
 * @修改时间: 2017年12月21日 下午6:01:45
 * @类说明: 故事评论controller
 */
@Slf4j
@CrossOrigin(maxAge = 3600)
@Controller
@RequestMapping(value = Route.PATH + Route.Comment.PATH)
public class CommentsController extends AbstractController {

	@Autowired
	private CommentsService commentsService;
	@Autowired
	private CommentsReplysService commentsReplysService;

	@Autowired
	private CommentTypeService commentTypeService;

	/**
	 * 查询某个模块下所有评论数
	 * 
	 * 
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = Route.Comment.COUNT2, method = RequestMethod.POST)
	public ResponseResult findCommentsCount2(//
			HttpServletRequest request,
			@RequestParam(value = "comment_parent_id", required = false) String comment_parent_id, //
			@RequestParam(value = "comment_parent_type", required = false) String comment_parent_type, //
			// 当前查询评论玩家
			@RequestParam(value = "player_id", required = false) String player_id) {
		log.info("查询评论数开始...");
		try {
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_id), EnumInfrastructureResCode.COMMENT_PARENT_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_type), EnumInfrastructureResCode.COMMENT_PARENT_TYPE_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(player_id), EnumInfrastructureResCode.PLAYER_ID_NOT_NULL.code());
			// ###
			// 1. 查询某模块id下评论总数
			String status = "0";// 0 正常状态
			Long allCount = commentsService.allCommentCountByConditions(comment_parent_id, comment_parent_type,
					player_id, status);
			log.info("==============当前count2数据:" + JSONObject.toJSONString(allCount));
			log.info("查询故事集评论数结束...");
			return successJson(allCount);
		} catch (Exception e) {
			log.error("=======查询故事集评论数失败," + e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
		}
	}

	/**
	 * 查询某个关卡集小关下所有评论数
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = Route.Comment.STAGE_NODE_COUNT, method = RequestMethod.POST)
	public ResponseResult findStageNodeCommentsCount(//
			HttpServletRequest request,
			@RequestParam(value = "comment_parent_id", required = false) String comment_parent_id, //
			@RequestParam(value = "comment_parent_type", required = false) String comment_parent_type, //
			@RequestParam(value = "nodeId", required = false) String nodeId, //
			@RequestParam(value = "stageId", required = false) String stageId, //
			// 当前查询评论玩家
			@RequestParam(value = "player_id", required = false) String player_id) {
		log.info("查询评论数开始...");
		try {
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_id), EnumInfrastructureResCode.COMMENT_PARENT_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_type), EnumInfrastructureResCode.COMMENT_PARENT_TYPE_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(nodeId), EnumInfrastructureResCode.COMMENT_NODEID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(stageId), EnumInfrastructureResCode.COMMENT_STAGEID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(player_id), EnumInfrastructureResCode.PLAYER_ID_NOT_NULL.code());
			// ###
			// 1. 查询某模块id下评论总数
			String status = "0";// 0 正常状态
			Long allCount = commentsService.allStageNodeCommentCountByConditions(comment_parent_id, comment_parent_type, nodeId, stageId,
					player_id, status);
			log.info("==============当前count2数据:" + JSONObject.toJSONString(allCount));
			log.info("查询故事集评论数结束...");
			return successJson(allCount);
		} catch (Exception e) {
			log.error("=======查询故事集评论数失败," + e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
		}
	}
	
	/**
	 * 查询某个模块下所有评论列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = Route.Comment.LIST2, method = RequestMethod.POST)
	public ResponseResult findCommentsLst2(//
			HttpServletRequest request,
			@RequestParam(value = "comment_parent_id", required = false) String comment_parent_id, //
			@RequestParam(value = "comment_parent_type", required = false) String comment_parent_type, //
			// 当前查询评论玩家
			@RequestParam(value = "player_id", required = false) String player_id, //
			// 排序类型: 创建时间creattime 点赞数 praisecnt 回复数 replyscnt
			@RequestParam(value = "type", defaultValue = "1") String type, //
			// 排序方式: desc 降序 asc 升序
			@RequestParam(value = "sort", defaultValue = "desc") String sort, //
			// 查询页码 默认 1
			@RequestParam(value = "page", defaultValue = "1") int pageNo, //
			// 每页显示条数
			@RequestParam(value = "size", defaultValue = "10") int pageSize//
	) {
		log.info(
				"查询评论列表开始 ============================== comment_parent_id,{},comment_parent_type,{},player_id,{},type,{},sort,{},page,{},size,{}",
				comment_parent_id, comment_parent_type, player_id, type, sort, pageNo, pageSize);
		List<Comments> commentsLst = null;
		try {
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_id), EnumInfrastructureResCode.COMMENT_PARENT_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_type), EnumInfrastructureResCode.COMMENT_PARENT_TYPE_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(player_id), EnumInfrastructureResCode.PLAYER_ID_NOT_NULL.code());
			Assert.isTrue(//
					EnumSortTypeCode.CREATE_DATE.getId().equals(type)//
							|| EnumSortTypeCode.PRAISE_CNT.getId().equals(type)//
							|| EnumSortTypeCode.REPLY_CNT.getId().equals(type)//
					, EnumInfrastructureResCode.COMMENT_TYPE_NOT_EXIST.code());
			sort = LetterUtils.toLowerCaseLetter(sort);
			Assert.isTrue(//
					EnumSortMethodCode.DESC.getType().equals(sort)//
							|| EnumSortMethodCode.ASC.getType().equals(sort)//
					, EnumInfrastructureResCode.COMMENT_SORT_INCORRENT.code());
			StringBuffer pageUrl = request.getRequestURL();
			// pageUrl.append(RequestUtils.getQueryString(request));

			// ###
			// 1. 查询某模块id下评论总数
			String status = "0";// 0 正常状态
			Long allCount = commentsService.allCommentCountByConditions(comment_parent_id, comment_parent_type,
					player_id, status);
			// Long myCount =
			// commentsService.myCountByConditions(comment_parent_id,comment_parent_type,player_id,status);
			Pagination page = new Pagination(pageNo, pageSize, allCount);
			// 查询list数据
			/*&& !comment_parent_type.equals(EnumCommentCode.COMMENT_FIGHTGUANQIA.getNumcode())*/
			if (type.equals(EnumSortTypeCode.PRAISE_CNT.getId()) ) {
				commentsLst = commentsService.findCommentLsts(comment_parent_id, comment_parent_type, player_id, status,
						type, sort, pageNo, CommonConstant.HOST_COMMENTS_NUM);
				if(comment_parent_type.equals(EnumCommentCode.STAGE_TREE_BATTLE.getNumcode())) {
					commentsLst = commentsService.findStageTreeLsts(comment_parent_id,
							player_id, status, EnumSortTypeCode.PRAISE_CNT.getId(), sort, 1, CommonConstant.HOST_COMMENTS_NUM);
				}
				if(comment_parent_type.equals(EnumCommentCode.STAGE_TREE_BATTLE_STAGE.getNumcode())) {
					commentsLst = commentsService.findCommentLsts(comment_parent_id, comment_parent_type,
							player_id, status, EnumSortTypeCode.PRAISE_CNT.getId(), sort, 1, CommonConstant.HOST_COMMENTS_NUM);
				}
			} else {
				commentsLst = commentsService.findCommentLsts(comment_parent_id, comment_parent_type, player_id, status,
						type, sort, pageNo, pageSize);
				if(comment_parent_type.equals(EnumCommentCode.COMMENT_FIGHTGUANQIA.getNumcode()) ||
						comment_parent_type.equals(EnumCommentCode.STAGE_TREE_BATTLE_STAGE.getNumcode())
						) {
					List<Comments> hotCommentsLst = commentsService.findCommentLsts(comment_parent_id, comment_parent_type,
							player_id, status, EnumSortTypeCode.PRAISE_CNT.getId(), sort, 1, CommonConstant.HOST_COMMENTS_NUM);
					commentsLst = commentsService.findCommentLst(comment_parent_id, comment_parent_type, player_id, status,
							type, sort, pageNo, pageSize);
					commentsLst.removeAll(hotCommentsLst);
				}
				if(comment_parent_type.equals(EnumCommentCode.STAGE_TREE_BATTLE.getNumcode())) {
					List<Comments> hotCommentsLst = commentsService.findStageTreeLsts(comment_parent_id,
							player_id, status, EnumSortTypeCode.PRAISE_CNT.getId(), sort, 1, CommonConstant.HOST_COMMENTS_NUM);
					commentsLst = commentsService.findStageTreeLst(comment_parent_id, player_id, status,
							type, sort, pageNo, pageSize);
					commentsLst.removeAll(hotCommentsLst);
				}
			}

			List<CommentsDto> commentsDtoLst = commentsService.copyProperties(commentsLst, comment_parent_id,
					comment_parent_type, player_id, status);
			page.setDatas(commentsDtoLst);
			page.setPageUrl(pageUrl.toString());
			page.setTotalCount(allCount);
			log.info("==============当前list2数据:" + JSONObject.toJSONString(page));
			log.info("查询故事集评论列表结束...");
			return successJson(page);
		} catch (Exception e) {
			log.error("=======查询故事集评论列表失败," + e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
		}
	}
	
	/**
	 * 查询某个关卡集下小关的评论列表
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = Route.Comment.STAGE_NODE_COMMENTS_LIST, method = RequestMethod.POST)
	public ResponseResult findStageNodeCommentsList(//
			HttpServletRequest request,
			@RequestParam(value = "comment_parent_id", required = false) String comment_parent_id, //
			@RequestParam(value = "comment_parent_type", required = false) String comment_parent_type, //
			@RequestParam(value = "nodeId", required = false) String nodeId, //
			@RequestParam(value = "stageId", required = false) String stageId, //
			// 当前查询评论玩家
			@RequestParam(value = "player_id", required = false) String player_id, //
			// 排序类型: 创建时间creattime 点赞数 praisecnt 回复数 replyscnt
			@RequestParam(value = "type", defaultValue = "1") String type, //
			// 排序方式: desc 降序 asc 升序
			@RequestParam(value = "sort", defaultValue = "desc") String sort, //
			// 查询页码 默认 1
			@RequestParam(value = "page", defaultValue = "1") int pageNo, //
			// 每页显示条数
			@RequestParam(value = "size", defaultValue = "10") int pageSize//
			) {
		log.info(
				"查询评论列表开始 ============================== comment_parent_id,{},comment_parent_type,{},nodeId,{},stageId,{},player_id,{},type,{},sort,{},page,{},size,{}",
				comment_parent_id, comment_parent_type, nodeId, stageId, player_id, type, sort, pageNo, pageSize);
		List<Comments> commentsLst = null;
		try {
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_id), EnumInfrastructureResCode.COMMENT_PARENT_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_type), EnumInfrastructureResCode.COMMENT_PARENT_TYPE_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(player_id), EnumInfrastructureResCode.PLAYER_ID_NOT_NULL.code());
			Assert.isTrue(//
					EnumSortTypeCode.CREATE_DATE.getId().equals(type)//
					|| EnumSortTypeCode.PRAISE_CNT.getId().equals(type)//
					|| EnumSortTypeCode.REPLY_CNT.getId().equals(type)//
					, EnumInfrastructureResCode.COMMENT_TYPE_NOT_EXIST.code());
			sort = LetterUtils.toLowerCaseLetter(sort);
			Assert.isTrue(//
					EnumSortMethodCode.DESC.getType().equals(sort)//
					|| EnumSortMethodCode.ASC.getType().equals(sort)//
					, EnumInfrastructureResCode.COMMENT_SORT_INCORRENT.code());
			StringBuffer pageUrl = request.getRequestURL();
			// pageUrl.append(RequestUtils.getQueryString(request));
			
			// ###
			// 1. 查询某模块id下评论总数
			String status = "0";// 0 正常状态
			Long allCount = commentsService.allStageNodeCommentCountByConditions(comment_parent_id, comment_parent_type, nodeId, stageId,
					player_id, status);
			// Long myCount =
			// commentsService.myCountByConditions(comment_parent_id,comment_parent_type,player_id,status);
			Pagination page = new Pagination(pageNo, pageSize, allCount);
			// 查询list数据
			if (type.equals(EnumSortTypeCode.PRAISE_CNT.getId())) {
				commentsLst = commentsService.findStageNodeCommentLsts(comment_parent_id, comment_parent_type, nodeId, stageId, player_id, status,
						type, sort, pageNo, pageSize);
			} else {
				if(!comment_parent_type.equals(EnumCommentCode.STAGE_TREE_BATTLE_STAGE.getNumcode())) {
					commentsLst = commentsService.findStageNodeCommentLsts(comment_parent_id, comment_parent_type, nodeId, stageId, player_id, status,
							type, sort, pageNo, pageSize);
				}else {
					List<Comments> hotCommentsLst = commentsService.findStageNodeCommentLsts(comment_parent_id, comment_parent_type, nodeId, stageId,
							player_id, status, EnumSortTypeCode.PRAISE_CNT.getId(), sort, 1, CommonConstant.HOST_COMMENTS_NUM);
					commentsLst = commentsService.findStageNodeCommentLst(comment_parent_id, comment_parent_type, nodeId, stageId, player_id, status,
							type, sort, pageNo, pageSize);
					commentsLst.removeAll(hotCommentsLst);
				}
			}
			
			List<CommentsDto> commentsDtoLst = commentsService.copyProperties(commentsLst, comment_parent_id,
					comment_parent_type, player_id, status);
			page.setDatas(commentsDtoLst);
			page.setPageUrl(pageUrl.toString());
			// if (myCount >0) {
			// page.setTotalCount(allCount+myCount);
			// }
			page.setTotalCount(allCount);
			log.info("==============当前list2数据:" + JSONObject.toJSONString(page));
			log.info("查询故事集评论列表结束...");
			return successJson(page);
		} catch (Exception e) {
			log.error("=======查询故事集评论列表失败," + e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
		}
	}
	
	
	/**
	 * 查询某个关卡集下小关的所有评论列表 弹幕
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = Route.Comment.STAGE_NODE_ALL_COMMENTS, method = RequestMethod.POST)
	public ResponseResult findStageNodeAllCommentsList(//
			HttpServletRequest request,
			@RequestParam(value = "comment_parent_id", required = false) String comment_parent_id, //
			@RequestParam(value = "comment_parent_type", required = false) String comment_parent_type, //
			@RequestParam(value = "nodeId", required = false) String nodeId, //
			@RequestParam(value = "stageId", required = false) String stageId, //
			// 当前查询评论玩家
			@RequestParam(value = "player_id", required = false) String player_id, //
			// 排序类型: 创建时间creattime 点赞数 praisecnt 回复数 replyscnt
			@RequestParam(value = "type", defaultValue = "1") String type, //
			// 排序方式: desc 降序 asc 升序
			@RequestParam(value = "sort", defaultValue = "desc") String sort, //
			// 查询页码 默认 1
			@RequestParam(value = "page", defaultValue = "1") int pageNo, //
			// 每页显示条数
			@RequestParam(value = "size", defaultValue = "10") int pageSize//
			) {
		log.info(
				"查询评论列表开始 ============================== comment_parent_id,{},comment_parent_type,{},nodeId,{},stageId,{},player_id,{},type,{},sort,{},page,{},size,{}",
				comment_parent_id, comment_parent_type, nodeId, stageId ,player_id, type, sort, pageNo, pageSize);
		List<Comments> commentsLst = null;
		try {
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_id), EnumInfrastructureResCode.COMMENT_PARENT_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_type), EnumInfrastructureResCode.COMMENT_PARENT_TYPE_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(player_id), EnumInfrastructureResCode.PLAYER_ID_NOT_NULL.code());
			Assert.isTrue(//
					EnumSortTypeCode.CREATE_DATE.getId().equals(type)//
					|| EnumSortTypeCode.PRAISE_CNT.getId().equals(type)//
					|| EnumSortTypeCode.REPLY_CNT.getId().equals(type)//
					, EnumInfrastructureResCode.COMMENT_TYPE_NOT_EXIST.code());
			sort = LetterUtils.toLowerCaseLetter(sort);
			Assert.isTrue(//
					EnumSortMethodCode.DESC.getType().equals(sort)//
					|| EnumSortMethodCode.ASC.getType().equals(sort)//
					, EnumInfrastructureResCode.COMMENT_SORT_INCORRENT.code());
			StringBuffer pageUrl = request.getRequestURL();
			// pageUrl.append(RequestUtils.getQueryString(request));
			
			// ###
			// 1. 查询某模块id下评论总数
			String status = "0";// 0 正常状态
			Long allCount = commentsService.allStageNodeCommentCountByConditions(comment_parent_id, comment_parent_type, nodeId, stageId,
					player_id, status);
			// Long myCount =
			// commentsService.myCountByConditions(comment_parent_id,comment_parent_type,player_id,status);
			Pagination page = new Pagination(pageNo, pageSize, allCount);
			// 查询list数据
			if (type.equals(EnumSortTypeCode.PRAISE_CNT.getId())) {
				commentsLst = commentsService.findStageNodeCommentLsts(comment_parent_id, comment_parent_type, nodeId, stageId, player_id, status,
						type, sort, pageNo, pageSize);
			} else {
				if(!comment_parent_type.equals(EnumCommentCode.STAGE_TREE_BATTLE_STAGE.getNumcode())) {
					commentsLst = commentsService.findStageNodeCommentLsts(comment_parent_id, comment_parent_type, nodeId, stageId, player_id, status,
							type, sort, pageNo, pageSize);
				}else {
//					List<Comments> hotCommentsLst = commentsService.findStageNodeCommentLsts(comment_parent_id, comment_parent_type, nodeId, stageId,
//							player_id, status, EnumSortTypeCode.PRAISE_CNT.getId(), sort, 1, CommonConstant.HOST_COMMENTS_NUM);
					commentsLst = commentsService.findStageNodeCommentLsts(comment_parent_id, comment_parent_type, nodeId, stageId, player_id, status,
							type, sort, pageNo, pageSize);
//					commentsLst.removeAll(hotCommentsLst);
				}
			}
			
			List<CommentsDto> commentsDtoLst = commentsService.copyProperties(commentsLst, comment_parent_id,
					comment_parent_type, player_id, status);
			page.setDatas(commentsDtoLst);
			page.setPageUrl(pageUrl.toString());
			// if (myCount >0) {
			// page.setTotalCount(allCount+myCount);
			// }
			page.setTotalCount(allCount);
			log.info("==============当前list2数据:" + JSONObject.toJSONString(page));
			log.info("查询故事集评论列表结束...");
			return successJson(page);
		} catch (Exception e) {
			log.error("=======查询故事集评论列表失败," + e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
		}
	}
	
	/**
	 * 查询某个模块下当前玩家的评论
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = Route.Comment.MYCOMMENT, method = RequestMethod.POST)
	public ResponseResult mycomment(//
			HttpServletRequest request,
			@RequestParam(value = "comment_parent_id", required = false) String comment_parent_id, //
			@RequestParam(value = "comment_parent_type", required = false) String comment_parent_type, //
			// 当前查询评论玩家
			@RequestParam(value = "player_id", required = false) String player_id) {
		log.info("查询评论列表开始...");
		try {
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_id), EnumInfrastructureResCode.COMMENT_PARENT_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_type), EnumInfrastructureResCode.COMMENT_PARENT_TYPE_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(player_id), EnumInfrastructureResCode.PLAYER_ID_NOT_NULL.code());
			String status = "0";// 0 正常状态
			List<Comments> commentsLst = commentsService.findCommentLst(comment_parent_id, comment_parent_type,
					player_id, status);
			List<CommentsDto> commentsDtoLst = commentsService.copyProperties(commentsLst, comment_parent_id,
					comment_parent_type, player_id, status);
			log.info("==============当前mycomment数据:" + JSONObject.toJSONString(commentsDtoLst));
			log.info("查询故事集评论列表结束...");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("datas", commentsDtoLst);
			jsonObject.put("currentDate", new Date());
			return successJson(jsonObject);
		} catch (Exception e) {
			log.error("查询故事集评论列表失败," + e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
		}
	}

	/**
	 * 查询某个模块下当前玩家的评论
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = Route.Comment.MY_STAGE_NODE_COMMENT, method = RequestMethod.POST)
	public ResponseResult myStageNodeComment(//
			HttpServletRequest request,
			@RequestParam(value = "comment_parent_id", required = false) String comment_parent_id, //
			@RequestParam(value = "comment_parent_type", required = false) String comment_parent_type, //
			@RequestParam(value = "nodeId", required = false) String nodeId, //
			@RequestParam(value = "stageId", required = false) String stageId, //
			// 当前查询评论玩家
			@RequestParam(value = "player_id", required = false) String player_id) {
		log.info("查询评论列表开始...");
		try {
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_id), EnumInfrastructureResCode.COMMENT_PARENT_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_type), EnumInfrastructureResCode.COMMENT_PARENT_TYPE_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(nodeId), EnumInfrastructureResCode.COMMENT_NODEID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(stageId), EnumInfrastructureResCode.COMMENT_STAGEID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(player_id), EnumInfrastructureResCode.PLAYER_ID_NOT_NULL.code());
			String status = "0";// 0 正常状态
			List<Comments> commentsLst = commentsService.findStageNodeCommentLst(comment_parent_id, comment_parent_type, nodeId, stageId, player_id, status);
			List<CommentsDto> commentsDtoLst = commentsService.copyProperties(commentsLst, comment_parent_id,
					comment_parent_type, player_id, status);
			log.info("==============当前mycomment数据:" + JSONObject.toJSONString(commentsDtoLst));
			log.info("查询故事集评论列表结束...");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("datas", commentsDtoLst);
			jsonObject.put("currentDate", new Date());
			return successJson(jsonObject);
		} catch (Exception e) {
			log.error("查询故事集评论列表失败," + e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
		}
	}
	
	/**
	 * 添加评论
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = Route.Comment.ADD, method = RequestMethod.POST)
	public ResponseResult addComments(
			@RequestParam(value = "comment_parent_id", required = false) String comment_parent_id, // 评论父对象id
			@RequestParam(value = "comment_parent_type", required = false) String comment_parent_type, // 评论父对象类型
			@RequestParam(value = "player_id", required = false) String player_id, //
			@RequestParam(value = "player_name", required = false) String player_name, //
			@RequestParam(value = "player_img", required = false) String player_img, //
			@RequestParam(value = "auther_id", required = false) String auther_id, //
			@RequestParam(value = "content", required = false) String content//
	) {
		try {
			String message = "";
			log.info(
					"infoMsg: 添加评论开始===================,comment_parent_id,{},comment_parent_type,{}"
							+ ",player_id,{},player_name,{},player_img,{},auther_id,{},content,{}",
					comment_parent_id, comment_parent_type, player_id, player_name, player_img, auther_id, content);
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_id), EnumInfrastructureResCode.COMMENT_PARENT_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_type), EnumInfrastructureResCode.COMMENT_PARENT_TYPE_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(player_id), EnumInfrastructureResCode.PLAYER_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(player_name), EnumInfrastructureResCode.PLAYER_NAME_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(player_img), EnumInfrastructureResCode.PLAYER_IMAGE_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(auther_id), EnumInfrastructureResCode.AUTHOR_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(content), EnumInfrastructureResCode.COMMENT_CONTENT_NOT_NULL.code());
			// todo storyId 验证故事集是否存在
			// todo playerId 验证玩家是否存在
			// comment_parent_type 评论父对象类型查询是否存在
			Boolean hasCommentType = commentTypeService.hasCommentType(comment_parent_type);
			Assert.isTrue(hasCommentType, EnumInfrastructureResCode.COMMENT_TYPE_NOT_EXIST.code());
			// 1.查询玩家是否已经评论过该故事集 （一个故事集一个玩家 只有一条故事集评论）
			String status = "0";
			Boolean flag = commentsService.isExistPlayerComment(comment_parent_id, comment_parent_type, player_id,
					status);
			Assert.isTrue(!flag, EnumInfrastructureResCode.PLAYERS_HAVE_COMMENTED.code());
			// 故事评论
			Comments comments = new Comments();
			comments.setCreate_date(new Date());
			comments.setCreate_id(player_id);
			comments.setUpdate_date(new Date());
			comments.setUpdate_id(player_id);
			comments.setComment_parent_id(comment_parent_id);
			comments.setComment_parent_type(comment_parent_type);
			comments.setPlayer_id(player_id);
			comments.setPlayer_name(player_name);
			comments.setPlayer_img(player_img);
			comments.setAuther_id(auther_id);
			comments.setContent(content);
			comments.setStatus(0);
			comments.setPraisecnt(0);

			Comments commentInfo = commentsService.insertComments(comments);
			if(comment_parent_type.equals(EnumCommentCode.COMMENT_FIGHTGUANQIA.getNumcode())) {
				message = commentsService.sendGuanKaMessage(commentInfo.get_id(), comment_parent_id, comment_parent_type,
						player_id, auther_id, content);
			}else {
				message = commentsService.sendGuanKaJiMessage(commentInfo.get_id(), comment_parent_id, comment_parent_type,
						player_id, auther_id, content);
			}
			if (message.equals(EnumGameServerResCode.SUCCESS.value())) {
				log.info("日期，data={},gameServer={}:成功", DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
			} else {
				log.info("日期，data={},gameServer={}:失败", DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
				return errorJson(10001, EnumInfrastructureResCode.GAMESERVER_ERROR.code());
			}
			Assert.isTrue(commentInfo != null, EnumInfrastructureResCode.COMMENT_CREATION_FAILED.code());
			log.info("添加评论结束");
			return successJson(commentInfo);
		} catch (Exception e) {
			log.error("==============添加评论失败,错误信息:" + e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
		}
	}

	/**
	 * 添加关卡集评论
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = Route.Comment.ADD_STAGE_COMMENT, method = RequestMethod.POST)
	public ResponseResult addStageComments(
			@RequestParam(value = "comment_parent_id", required = false) String comment_parent_id, // 评论父对象id
			@RequestParam(value = "comment_parent_type", required = false) String comment_parent_type, // 评论父对象类型
			@RequestParam(value = "nodeId", required = false) String nodeId, // 节点ID
			@RequestParam(value = "stageId", required = false) String stageId, // 小关(关卡)ID
			@RequestParam(value = "pathType", required = false) String pathType, // 主线/支线
			@RequestParam(value = "nodeIndex", required = false) String nodeIndex, // 节点索引
			@RequestParam(value = "stageIndex", required = false) String stageIndex, // 关卡包中关卡的索引
			@RequestParam(value = "player_id", required = false) String player_id, //
			@RequestParam(value = "player_name", required = false) String player_name, //
			@RequestParam(value = "player_img", required = false) String player_img, //
			@RequestParam(value = "auther_id", required = false) String auther_id, //
			@RequestParam(value = "content", required = false) String content//
			) {
		try {
			String message = "";
			log.info(
					"infoMsg: 添加评论开始===================,comment_parent_id,{},comment_parent_type,{}"
							+ ",player_id,{},player_name,{},player_img,{},auther_id,{},content,{}",
							comment_parent_id, comment_parent_type, player_id, player_name, player_img, auther_id, content);
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_id), EnumInfrastructureResCode.COMMENT_PARENT_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(comment_parent_type), EnumInfrastructureResCode.COMMENT_PARENT_TYPE_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(nodeId), EnumInfrastructureResCode.COMMENT_NODEID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(stageId), EnumInfrastructureResCode.COMMENT_STAGEID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(pathType), EnumInfrastructureResCode.COMMENT_PATHTYPE_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(nodeIndex), EnumInfrastructureResCode.COMMENT_NODEINDEX_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(stageIndex), EnumInfrastructureResCode.COMMENT_STAGEINDEX_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(player_id), EnumInfrastructureResCode.PLAYER_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(player_name), EnumInfrastructureResCode.PLAYER_NAME_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(player_img), EnumInfrastructureResCode.PLAYER_IMAGE_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(auther_id), EnumInfrastructureResCode.AUTHOR_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(content), EnumInfrastructureResCode.COMMENT_CONTENT_NOT_NULL.code());
			// todo storyId 验证故事集是否存在
			// todo playerId 验证玩家是否存在
			// comment_parent_type 评论父对象类型查询是否存在
			Boolean hasCommentType = commentTypeService.hasCommentType(comment_parent_type);
			Assert.isTrue(hasCommentType, EnumInfrastructureResCode.COMMENT_TYPE_NOT_EXIST.code());
			// 1.查询玩家是否已经评论过该故事集 （一个故事集一个玩家 只有一条故事集评论）
			String status = "0";
			// TODO 
			Boolean flag = commentsService.isExistStagePlayerComment(comment_parent_id, comment_parent_type, nodeId ,stageId ,player_id,
					status);
			Assert.isTrue(!flag, EnumInfrastructureResCode.PLAYERS_HAVE_COMMENTED.code());
			// 故事评论
			Comments comments = new Comments();
			comments.setCreate_date(new Date());
			comments.setCreate_id(player_id);
			comments.setUpdate_date(new Date());
			comments.setUpdate_id(player_id);
			comments.setComment_parent_id(comment_parent_id);
			comments.setComment_parent_type(comment_parent_type);
			comments.setNodeId(nodeId);
			comments.setStageId(stageId);
			comments.setPathType(pathType);
			comments.setNodeIndex(nodeIndex);
			comments.setStageIndex(stageIndex);
			comments.setPlayer_id(player_id);
			comments.setPlayer_name(player_name);
			comments.setPlayer_img(player_img);
			comments.setAuther_id(auther_id);
			comments.setContent(content);
			comments.setStatus(0);
			comments.setPraisecnt(0);
			
			Comments commentInfo = commentsService.insertComments(comments);
			if(comment_parent_type.equals(EnumCommentCode.STAGE_TREE_BATTLE.getNumcode())) {
				message = commentsService.sendGuanKaMessage(commentInfo.get_id(), comment_parent_id, comment_parent_type,
						player_id, auther_id, content);
			}else {
				message = commentsService.sendGuanKaJiMessage(commentInfo.get_id(), comment_parent_id, comment_parent_type,
						player_id, auther_id, content);
			}
			if (message.equals(EnumGameServerResCode.SUCCESS.value())) {
				log.info("日期，data={},gameServer={}:成功", DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
			} else {
				log.info("日期，data={},gameServer={}:失败", DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
				return errorJson(10001, EnumInfrastructureResCode.GAMESERVER_ERROR.code());
			}
			Assert.isTrue(commentInfo != null, EnumInfrastructureResCode.COMMENT_CREATION_FAILED.code());
			log.info("添加评论结束");
			return successJson(commentInfo);
		} catch (Exception e) {
			log.error("==============添加评论失败,错误信息:" + e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
		}
	}

	/**
	 * 根据评论id 查询某一个评论详情
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = Route.Comment.FINDONE, method = RequestMethod.POST)
	public ResponseResult findOneComment(//
			@RequestParam(value = "id", required = false) String id //
	) {
		try {
			log.info(" 查询某一个评论详情开始");
			Assert.isTrue(StringUtils.isNotBlank(id), EnumInfrastructureResCode.COMMENT_ID_NOT_NULL.code());
			Comments comments = commentsService.findById(id);
			Assert.isTrue(comments != null, EnumInfrastructureResCode.COMMENT_NOT_EXISTS.code());
			log.info(" 查询某一个评论详情结束");
			return successJson(comments);
		} catch (Exception e) {
			log.error("==============查询某一个评论详情失败,失败信息:" + e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
		}
	}
	
	
	/**
	 * 根据评论id 删除某条评论
	 * 
	 * @return	jsonData
	 */
	@ResponseBody
	@RequestMapping(value = Route.Comment.DELETECOMMENT, method = RequestMethod.POST)
	public ResponseResult deleteComment(//
			@RequestParam(value = "commentId", required = false) String commentId //
			) {
		try {
			log.info("infoMsg:============= 根据评论id 删除某条评论,commentId,{}",commentId);
			Assert.isTrue(StringUtils.isNotBlank(commentId), EnumInfrastructureResCode.COMMENT_ID_NOT_NULL.code());
			Comments comments = commentsService.findById(commentId);
			Assert.isTrue(comments != null, EnumInfrastructureResCode.COMMENT_NOT_EXISTS.code());
			boolean isSuccess = commentsService.remove(comments);
			if(isSuccess) {
				// 删除评论下的回复
				commentsReplysService.removeLstByCommentId(commentId);
			}
			log.info("infoMsg:============= 根据评论id 删除某条评论结束");
			return successJson();
		} catch (Exception e) {
			log.error("errorMsg:=============删除某条评论失败,失败信息:" + e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
		}
	}
	
	


	

}