package com.lung.controller.comments;

import java.util.ArrayList;
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

import com.lung.controller.AbstractController;
import com.lung.dto.Pagination;
import com.lung.dto.ResponseResult;
import com.lung.enumeration.EnumCommentCode;
import com.lung.enumeration.EnumGameServerResCode;
import com.lung.enumeration.EnumInfrastructureResCode;
import com.lung.enumeration.EnumResCode;
import com.lung.enumeration.EnumSortMethodCode;
import com.lung.model.comments.Comments;
import com.lung.model.comments.CommentsReplys;
import com.lung.service.comments.CommentsReplysService;
import com.lung.service.comments.CommentsService;
import com.lung.util.DateUtils;
import com.lung.util.LetterUtils;
import com.lung.web.api.bean.PageData;
import com.lung.web.api.bean.Route;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: noseparte
 * @Email: 1402614629@qq.com
 * @类名: StoryCommentsController
 * @创建时间: 2017年12月21日 下午6:01:45
 * @修改时间: 2017年12月21日 下午6:01:45
 * @类说明: 评论回复controller
 */
@Slf4j
@CrossOrigin(maxAge = 3600)
@Controller
@RequestMapping(value = Route.PATH + Route.Comment.PATH)
public class CommentsReplysController extends AbstractController {

	@Autowired
	private CommentsReplysService commentsReplysService;

	@Autowired
	private CommentsService commentsService;

	@ResponseBody
	@RequestMapping(value = Route.Comment.REPLYS + Route.Comment.LIST, method = RequestMethod.POST)
	public ResponseResult findReplyLst(//
			HttpServletRequest request,
			// 某一条评论的id
			@RequestParam(value = "comments_id", required = false) String comments_id, //
			// 排序类型: 创建时间creattime 点赞数 praisecnt 回复数 replyscnt
			// @RequestParam(value="type",defaultValue="1") String type,//
			// 排序方式: desc 降序 asc 升序
			@RequestParam(value = "sort", defaultValue = "desc") String sort, //
			// 查询页码 默认 1
			@RequestParam(value = "page", defaultValue = "1") int pageNo, //
			// 每页显示条数
			@RequestParam(value = "size", defaultValue = "10") int pageSize//
	) {
		log.info("评论回复controller查询列表开始....,comments_id,{},sort,{},page,{},size,{}", comments_id, sort, pageNo,
				pageSize);
		List<PageData> pdList = new ArrayList<>();
		try {
			Assert.isTrue(StringUtils.isNotBlank(comments_id), EnumInfrastructureResCode.COMMENT_ID_NOT_NULL.code());
			sort = LetterUtils.toLowerCaseLetter(sort);
			Assert.isTrue(//
					EnumSortMethodCode.DESC.getType().equals(sort)//
							|| EnumSortMethodCode.ASC.getType().equals(sort)//
					, EnumInfrastructureResCode.COMMENT_SORT_INCORRENT.code());
			StringBuffer pageUrl = request.getRequestURL();
			// pageUrl.append(RequestUtils.getQueryString(request));
			// ###
			// 验证当前评论是否存在
			Comments comments = commentsService.findById(comments_id);
			Assert.isTrue(comments != null, EnumInfrastructureResCode.COMMENT_NOT_EXISTS.code());
			// ###
			// 1. 查询某个故事集评论回复总数
			// 0 正常状态
			String status = "0";
			Long allCount = commentsReplysService.allCommentReplysCountByStatus(comments_id, status);
			Pagination page = new Pagination(pageNo, pageSize, allCount);
			// 查询list数据
			// 作者回复的集合
			List<CommentsReplys> commentsAuthReplysLst = commentsReplysService.findByAuthReplys(comments_id,
					comments.getAuther_id(), status, sort, pageNo, pageSize);
			// 非作者回复的集合
			List<CommentsReplys> commentsReplysLst = commentsReplysService.findCommentReplysLsts(comments_id,
					comments.getAuther_id(), status, sort, pageNo, pageSize);
			if (commentsReplysLst != null && commentsReplysLst.size() > 0) {
				for (CommentsReplys replys : commentsReplysLst) {
					commentsAuthReplysLst.add(replys);
				}
			}
			PageData pd = null;
			for (CommentsReplys commentsReplys : commentsAuthReplysLst) {
				pd = new PageData();
				pd.put("_id", commentsReplys.get_id());
				pd.put("create_id", commentsReplys.getCreate_id());
				pd.put("create_date", commentsReplys.getCreate_date());
				pd.put("update_id", commentsReplys.getUpdate_id());
				pd.put("update_date", commentsReplys.getUpdate_date());
				pd.put("comment_parent_id", commentsReplys.getComment_parent_id());
				pd.put("comment_parent_type", commentsReplys.getComment_parent_type());
				pd.put("comments_id", commentsReplys.getComments_id());
				pd.put("comments_replys_pid", commentsReplys.getComments_replys_pid());
				pd.put("content", commentsReplys.getContent());
				pd.put("player_id", commentsReplys.getPlayer_id());
				pd.put("player_name", commentsReplys.getPlayer_name());
				pd.put("player_img", commentsReplys.getPlayer_img());
				pd.put("status", commentsReplys.getStatus());
//				CommentsReplys replys = commentsReplysService.findOneByCommentsIdAndReplysPid(comments_id,
//						commentsReplys.getComments_replys_pid());
				CommentsReplys replys = commentsReplysService.findOneByCommentsId(commentsReplys.getComments_replys_pid());
				if(replys != null) {
					pd.put("replyId", replys.getPlayer_id());
					pd.put("replyName", replys.getPlayer_name());
					pd.put("replyImg", replys.getPlayer_img());
				} else {
					pd.put("replyId", "");
					pd.put("replyName", "");
					pd.put("replyImg", "");
				}
				
				pdList.add(pd);
			}
			page.setDatas(pdList);
			page.setPageUrl(pageUrl.toString());
			log.info("评论回复controller查询列表结束....");
			return successJson(page);
		} catch (Exception e) {
			log.error("=============评论回复controller查询列表出错了,错误信息为:" + e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
		}
	}

	/**
	 * 添加评论的回复
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = Route.Comment.REPLYS + Route.Comment.ADD, method = RequestMethod.POST)
	public ResponseResult addReplys(//
			@RequestParam(value = "comments_id", required = false) String comments_id, //
			@RequestParam(value = "comments_replys_pid", defaultValue = "0") String comments_replys_pid, //
			@RequestParam(value = "player_id", required = false) String playerId, //
			@RequestParam(value = "player_name", required = false) String playerName, //
			@RequestParam(value = "player_img", required = false) String playerImg, //
			@RequestParam(value = "content", required = false) String content//
	) {
		try {
			log.info(
					"infoMsg: 评论回复开始 ===================,comments_id,{},comments_replys_pid,{}"
							+ ",playerId,{},playerName,{},playerImg,{},content,{}",
					comments_id, comments_replys_pid, playerId, playerName, playerImg, content);
			Assert.isTrue(StringUtils.isNotBlank(comments_id), EnumInfrastructureResCode.COMMENT_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(playerId), EnumInfrastructureResCode.PLAYER_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(playerName), EnumInfrastructureResCode.PLAYER_NAME_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(playerImg), EnumInfrastructureResCode.PLAYER_IMAGE_NOT_NULL.code());

			// 验证当前评论是否存在
			Comments comments = commentsService.findById(comments_id);
			Assert.isTrue(comments != null, EnumInfrastructureResCode.COMMENT_REPLIED_DELETED.code());
			// 评论回复验证是否存在
			// TODO
			Assert.isTrue(StringUtils.isNotBlank(content), EnumInfrastructureResCode.COMMENT_CONTENT_REPLY_NOT_NULL.code());

			// 添加评论回复
			CommentsReplys commentsReplys = new CommentsReplys();
			commentsReplys.setCreate_id(playerId);
			commentsReplys.setUpdate_id(playerId);
			commentsReplys.setCreate_date(new Date());
			commentsReplys.setUpdate_date(new Date());
			commentsReplys.setPlayer_id(playerId);
			commentsReplys.setPlayer_name(playerName);
			commentsReplys.setPlayer_img(playerImg);
			commentsReplys.setStatus(0);
			commentsReplys.setComments_id(comments_id);
			commentsReplys.setComments_replys_pid(comments_replys_pid);
			commentsReplys.setComment_parent_id(comments.getComment_parent_id());
			commentsReplys.setComment_parent_type(comments.getComment_parent_type());
			commentsReplys.setContent(content);
			commentsReplys = commentsReplysService.create(commentsReplys);
			Assert.isTrue(commentsReplys != null, EnumInfrastructureResCode.COMMENT_REPLIED_FAILED.code());
			
			// 更新回复数
			Integer changeCount = 1;
			boolean flag = commentsService.updateCommentsReplysCountById(commentsReplys.getComments_id(), changeCount);
			if (!flag) {
				log.error("更新评论回复数失败");
			}
			// 通知作者
			String message = "";
			if(comments.getComment_parent_type().equals(EnumCommentCode.COMMENT_FIGHTGUANQIA.getNumcode())) {
				message = commentsReplysService.sendGuanKaMessage(comments.get_id(), comments_replys_pid,
						comments.getComment_parent_id(), comments.getComment_parent_type(), playerId,
						comments.getAuther_id(), content);
			}else {
				message = commentsReplysService.sendGuanKaJiMessage(comments.get_id(), comments_replys_pid,
						comments.getComment_parent_id(), comments.getComment_parent_type(), playerId,
						comments.getAuther_id(), content);
			}
//			String message = commentsReplysService.sendMessage(comments.get_id(), comments_replys_pid,
//					comments.getComment_parent_id(), comments.getComment_parent_type(), playerId,
//					comments.getAuther_id(), content);
			if (message.equals(EnumGameServerResCode.SUCCESS.value())) {
				log.info("日期，data={},gameServer={}:成功", DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
			} else {
				log.info("日期，data={},gameServer={}:失败", DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
				return errorJson(10001, EnumInfrastructureResCode.GAMESERVER_ERROR.code());
			}
			CommentsReplys replys = commentsReplysService.findOneByCommentsIdAndReplysPid(comments_id,
					comments_replys_pid);
			Assert.notNull(replys, EnumInfrastructureResCode.REPLY_OBJECT_NOT_NULL.code());
			// 通知玩家
			String replysMessage = "";
			if(comments.getComment_parent_type().equals(EnumCommentCode.COMMENT_FIGHTGUANQIA.getNumcode())) {
				replysMessage = commentsReplysService.sendGuanKaReplysMessage(comments.get_id(), comments.getPlayer_id(),
						comments.getComment_parent_id(), comments.getComment_parent_type(), playerId,
						comments.getAuther_id(), content);
			}else {
				replysMessage = commentsReplysService.sendGuanKaJiReplysMessage(comments.get_id(), comments.getPlayer_id(),
						comments.getComment_parent_id(), comments.getComment_parent_type(), playerId,
						comments.getAuther_id(), content);
			}
//			String replysMessage = commentsReplysService.sendReplysMessage(comments.get_id(), comments.getPlayer_id(),
//					comments.getComment_parent_id(), comments.getComment_parent_type(), playerId,
//					comments.getAuther_id(), content);
			if (replysMessage.equals(EnumGameServerResCode.SUCCESS.value())) {
				log.info("日期，data={},gameServer={}:成功", DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
			} else {
				log.info("日期，data={},gameServer={}:失败", DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
				return errorJson(10001, EnumInfrastructureResCode.GAMESERVER_ERROR.code());
			}
			log.info("评论回复结束....");
			return successJson(commentsReplys);
		} catch (Exception e) {
			log.error("=============评论回复失败,失败消息:" + e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
		}
	}

	/**
	 * 根据评论id 删除某条评论回复
	 * 
	 * @return	jsonData
	 */
	@ResponseBody
	@RequestMapping(value = Route.Comment.REPLYS + Route.Comment.DELETEREPLYS, method = RequestMethod.POST)
	public ResponseResult deleteReplys(//
			@RequestParam(value = "commentId", required = false) String commentId, //
			@RequestParam(value = "replysId", required = false) String replysId //
			) {
		try {
			log.info("infoMsg:============= 根据评论id 删除某条评论回复,commentId,{},replysId,{}",commentId,replysId);
			Assert.isTrue(StringUtils.isNotBlank(commentId), EnumInfrastructureResCode.COMMENT_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(replysId), EnumInfrastructureResCode.REPLY_ID_NOT_NULL.code());
			Comments comments = commentsService.findById(commentId);
			Assert.isTrue(comments != null, EnumInfrastructureResCode.COMMENT_NOT_EXISTS.code());
			CommentsReplys replys = commentsReplysService.findOneByCommentsIdAndReplysId(commentId, replysId);
			Assert.isTrue(replys != null, EnumInfrastructureResCode.COMMENT_REPLIES_NOT_EXIST.code());
			commentsReplysService.removeOneByCommentIdAndReplysId(commentId,replysId);
			commentsService.updateCommentsReplysCountById(commentId, -1);
			log.info("infoMsg:============= 根据评论id 删除某条评论回复结束");
			return successJson();
		} catch (Exception e) {
			log.error("errorMsg:=============删除某条评论回复失败,失败信息:" + e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
		}
	}
	
	
}
