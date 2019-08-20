package com.xmbl.controller.comments;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmbl.controller.AbstractController;
import com.xmbl.dto.ResponseResult;
import com.xmbl.enumeration.EnumInfrastructureResCode;
import com.xmbl.enumeration.EnumResCode;
import com.xmbl.model.comments.Comments;
import com.xmbl.model.comments.CommentsPraise;
import com.xmbl.service.comments.CommentsPraiseService;
import com.xmbl.service.comments.CommentsService;
import com.xmbl.web.api.bean.Route;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StoryCommentsPraiseController 
 * @创建时间:  2017年12月21日 下午6:01:45
 * @修改时间:  2017年12月21日 下午6:01:45
 * @类说明: 故事评论点赞controller
 */
@CrossOrigin(maxAge=3600)
@Controller
@RequestMapping(value=Route.PATH + Route.Comment.PATH)
public class CommentsPraiseController extends AbstractController {

	private static Logger LOGGER = LoggerFactory.getLogger(CommentsPraiseController.class);
	
	@Autowired
	private CommentsPraiseService commentsPraiseService;
	
	@Autowired
	private CommentsService commentsService;
	
	/**
	 * 评论点赞和取消赞
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=Route.Comment.PRAISR + Route.Comment.SAVEORCANCEL, method=RequestMethod.POST)
	public ResponseResult saveOrCancel(
			@RequestParam(value="comments_id",required= false) String commentsId,
			@RequestParam(value="player_id",required= false) String playerId,
			@RequestParam(value="player_name",required= false) String playerName,
			@RequestParam(value="player_img",required= false) String playerImg
	) {
		LOGGER.info("评论点赞和取消赞开始。。。");
		try {
			Assert.isTrue(StringUtils.isNotBlank(commentsId), EnumInfrastructureResCode.STORY_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(playerId), EnumInfrastructureResCode.PLAYER_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(playerName), EnumInfrastructureResCode.PLAYER_NAME_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(playerImg), EnumInfrastructureResCode.PLAYER_IMAGE_NOT_NULL.code());
			// todo 验证玩家是否存在
			// todo 验证评论是否存在  storyCommentsId
			Comments comments = commentsService.findById(commentsId);
			Assert.isTrue(comments != null, EnumInfrastructureResCode.COMMENTS_THUMB_UP_DELETED.code());
			// 1.通过故事集评论id 和 玩家id 查询评论点赞表
			CommentsPraise commentsPraise = commentsPraiseService.findByCommentsIdAndPlayerId(commentsId,playerId);
			// 操作类型 false 取消赞 true 点赞
			boolean operationType = true;
			if (commentsPraise == null){
				commentsPraise = new CommentsPraise();
				commentsPraise.setCreate_date(new Date());
				commentsPraise.setCreate_id(playerId);
				commentsPraise.setUpdate_date(new Date());
				commentsPraise.setUpdate_id(playerId);
				commentsPraise.setPlayer_id(playerId);
				commentsPraise.setPlayer_name(playerName);
				commentsPraise.setPlayer_img(playerImg);
				commentsPraise.setComment_parent_id(comments.getComment_parent_id());
				commentsPraise.setComment_parent_type(comments.getComment_parent_type());
				commentsPraise.setComments_id(comments.get_id());
				commentsPraise.setPraise_status(operationType);
			} else {
				operationType = !commentsPraise.getPraise_status();
				commentsPraise.setPraise_status(operationType);
			}
			LOGGER.info("当前评论赞操作是:"+(operationType ? "点赞操作" : "取消赞操作"));
			commentsPraise = commentsPraiseService.addOrRemovePraise(commentsPraise);
			Assert.isTrue(commentsPraise != null , EnumInfrastructureResCode.COMMENTS_THUMB_UP_FAILED.code());
			// 更新评论点赞数
			Integer changeCount = operationType ? 1:-1;
			
			//  ==============================================================
			boolean flag = commentsService.updateCommentsPraiseCountById(commentsPraise.getComments_id(),changeCount);
			Assert.isTrue(flag,EnumInfrastructureResCode.UPDATE_THUMB_UP_NUMBER_FAILED.code());
			LOGGER.info("评论点赞和取消赞结束。。。");
			return successJson(commentsPraise);
		} catch (Exception e) {
			LOGGER.error("=============评论点赞和取消赞失败,失败原因:"+e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
		}
	}
	
}
