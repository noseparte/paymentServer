package com.lung.controller.comments;

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
import com.lung.dto.ResponseResult;
import com.lung.enumeration.EnumCommentCode;
import com.lung.enumeration.EnumInfrastructureResCode;
import com.lung.enumeration.EnumResCode;
import com.lung.enumeration.EnumResPraiseOrBelittleCode;
import com.lung.enumeration.EnumResPraiseOrBelittleTypeCode;
import com.lung.model.comments.PraiseAndBelittleParent;
import com.lung.model.comments.PraiseAndBelittleParentDetail;
import com.lung.service.comments.PraiseAndBelittleParentDetailService;
import com.lung.service.comments.PraiseAndBelittleParentService;
import com.lung.web.api.bean.Route;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author: noseparte
 * @Email: 1402614629@qq.com
 * @类名:  PraiseAndBelittleParentDetailController 
 * @创建时间:  2017年12月20日 下午9:30:38
 * @修改时间:  2017年12月20日 下午9:30:38
 * @类说明: 点赞或踩评论父对象详情表
 * <br></br>
 * 一个人只能点赞或踩一次某一个评论父对象
 */
@Slf4j
@CrossOrigin(maxAge=3600)
@Controller
@RequestMapping(value=Route.PATH + Route.Comment.PATH)
public class PraiseAndBelittleParentDetailController extends AbstractController{
	
	@Autowired
	private PraiseAndBelittleParentService praiseAndBelittleParentService;
	
	@Autowired 
	private PraiseAndBelittleParentDetailService praiseAndBelittleParentDetailService;
	
	/**
	 * 玩家对某个评论父对象点赞
	 * @param commentParentId
	 * @param commentParentType
	 * @param playerId
	 * @param playerName
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=Route.Comment.PRAISRORBELITTLE,method = RequestMethod.POST)
	public ResponseResult praiseOrBelittle(//
			@RequestParam(value="comment_parent_id", required= false) String commentParentId,// 评论父对象id
			@RequestParam(value="comment_parent_type", required= false) String commentParentType,// 评论父对象类型
			@RequestParam(value="player_id", required= false) String playerId,// 点赞玩家id
			@RequestParam(value="player_name", required= false) String playerName,// 点赞玩家name
			@RequestParam(value="player_img", required= false) String playerImg,// 点赞玩家name
			@RequestParam(value="type", required=false) String type // 操作类型 1 赞 2踩
	) {
		ResponseResult result =  null;
		try {
			Assert.isTrue(StringUtils.isNotBlank(commentParentId), EnumInfrastructureResCode.COMMENT_PARENT_ID_NOT_NULL.code());
			//Assert.isTrue(StringUtils.isNotBlank(commentParentName), "评论父对象name不能为空或空字符串");
			Assert.isTrue(StringUtils.isNotBlank(commentParentType), EnumInfrastructureResCode.COMMENT_PARENT_TYPE_NOT_NULL.code());
			int commentParentTypeInt = Integer.parseInt(commentParentType.trim());
			Assert.isTrue(commentParentTypeInt>=1 && commentParentTypeInt <=8, EnumInfrastructureResCode.COMMENT_TYPE_NOT_EXIST.code());
			Assert.isTrue(StringUtils.isNotBlank(playerId), EnumInfrastructureResCode.THUMB_UP_PLAYER_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(playerName), EnumInfrastructureResCode.THUMB_UP_PLAYER_NAME_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(playerImg), EnumInfrastructureResCode.THUMB_UP_PLAYER_IMAGE_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(type), EnumInfrastructureResCode.THUMB_UP_TYPE_NOT_NULL.code());
			Assert.isTrue(String.valueOf(EnumResPraiseOrBelittleCode.PRAISE.value()).equals(type) //
					|| String.valueOf(EnumResPraiseOrBelittleCode.BELITTLE.value()).equals(type), //
					EnumInfrastructureResCode.THUMB_UP_TYPE_INCORRENT.code());
			log.info("评论父对象点赞或踩或取消赞或取消踩参数验证通过...");
			
			// 1.查询故事集数量表是否创建，若未创建则创建
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail = new PraiseAndBelittleParentDetail();
			praiseAndBelittleParentDetail.setComment_parent_id(commentParentId);
			String commentParentName = "";
			for (EnumCommentCode enumCommentCode : EnumCommentCode.values()) {
				if (enumCommentCode.getNumcode().equals(commentParentType)) {
					commentParentName = enumCommentCode.getWordcode();
					break;
				}
			}
			praiseAndBelittleParentDetail.setComment_parent_name(commentParentName);
			praiseAndBelittleParentDetail.setComment_parent_type(commentParentType);
			praiseAndBelittleParentDetail.setPlayer_id(playerId);
			praiseAndBelittleParentDetail.setPlayer_name(playerName);
			praiseAndBelittleParentDetail.setPlayer_img(playerImg);
			PraiseAndBelittleParent praiseAndBelittleParent = praiseAndBelittleParentService.findOneByConditions(praiseAndBelittleParentDetail);
			Assert.isTrue(praiseAndBelittleParent != null, EnumInfrastructureResCode.THUMB_UP_OBJECT_NOT_NULL.code());
			praiseAndBelittleParentDetail.setComment_parent_praise_and_belittle_id(praiseAndBelittleParent.get_id());
			log.info("赞踩评论父对象不为空,验证通过");
			
			// 2.根据评论父对象id 和评论父对象类型 和 玩家id,查询玩家点赞记录,若没有则创建一条记录
			praiseAndBelittleParentDetail = praiseAndBelittleParentDetailService.findOneByConditions(praiseAndBelittleParentDetail);
			
			// 3.根据类型type 点1赞  2踩 0取消赞或取消踩
			String is_praise_or_belittle_type = praiseAndBelittleParentDetail.getIs_praise_or_belittle();
//			Assert.isTrue(type.equals(is_praise_or_belittle_type), "点赞和踩类型传参有误!");
			result = praiseAndBelittleParentDetailService.updateByConditions(commentParentId,commentParentType,playerId,is_praise_or_belittle_type,type);
			Assert.isTrue("ok".equals(result.getMsg()), EnumInfrastructureResCode.THUMB_UP_FAILED.code());
			log.info("操作成功!");
			return result;
		} catch (Exception e) {
			log.error("===============操作失败,失败信息为:"+e.getMessage());
			if (result == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage());
			}
			return result;
		}
	}
	
	/**
	 * 查询玩家对某个评论父对象的点赞状态
	 * 
	 * @param commentParentId
	 * @param commentParentType
	 * @param playerId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=Route.Comment.PRAISRORBELITTLESTATUS,method = RequestMethod.POST)
	public ResponseResult isPraiseOrBelittleStoryDetail(//
			@RequestParam(value="comment_parent_id", required= false) String commentParentId,// 评论父对象id
			@RequestParam(value="comment_parent_type", required= false) String commentParentType,// 评论父对象类型
			@RequestParam(value="player_id", required= false) String playerId// 玩家id
	){ //
		log.info("玩家某个评论父对象点赞踩状态查询开始。。。");
		int status = EnumResPraiseOrBelittleTypeCode.NOSTATUS.value();
		try {
			Assert.isTrue(StringUtils.isNotBlank(commentParentId), EnumInfrastructureResCode.COMMENT_PARENT_ID_NOT_NULL.code());
			Assert.isTrue(StringUtils.isNotBlank(playerId), EnumInfrastructureResCode.PLAYER_ID_NOT_NULL.code());
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail = praiseAndBelittleParentDetailService.findOneByConditions(commentParentId,commentParentType,playerId);
			log.info("玩家某个故事集点赞踩状态查询成功。。。");
			if (praiseAndBelittleParentDetail==null) {
				status = EnumResPraiseOrBelittleCode.NOPRAISEBELITTLE.value();
			} else {
				status = Integer.parseInt(praiseAndBelittleParentDetail.getIs_praise_or_belittle());
			}
			return successJson(status);
		} catch (Exception e) {
			log.error("======================"+e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(),e.getMessage());
		}
	}
}
