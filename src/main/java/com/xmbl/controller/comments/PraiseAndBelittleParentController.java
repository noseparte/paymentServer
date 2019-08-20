package com.xmbl.controller.comments;

import java.util.HashMap;
import java.util.Map;

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
import com.xmbl.model.comments.PraiseAndBelittleParent;
import com.xmbl.service.comments.PraiseAndBelittleParentService;
import com.xmbl.web.api.bean.Route;

/**
 * 
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  PraiseAndBelittleParentController 
 * @创建时间:  2017年12月20日 下午9:29:07
 * @修改时间:  2017年12月20日 下午9:29:07
 * @类说明: 记录某个评论父对象的点赞和踩的数量
 */
@CrossOrigin(maxAge=3600)
@Controller
@RequestMapping(value=Route.PATH + Route.Comment.PATH)
public class PraiseAndBelittleParentController extends AbstractController{
	
	private static Logger LOGGER = LoggerFactory.getLogger(PraiseAndBelittleParentController.class);
	
	@Autowired
	private PraiseAndBelittleParentService praiseAndBelittleParentService;
	
	/**
	 * 
	 * 
	 * @param commentParentId
	 * @param commentParentType
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=Route.Comment.PRAISRORBELITTLE + Route.Comment.SEARCHCOUNT,method = RequestMethod.POST)
	public ResponseResult searchCount(//
			@RequestParam(value="comment_parent_id", required= false) String commentParentId, // 评论父对象id
			@RequestParam(value="comment_parent_type",required=false) String commentParentType // 评论父对象类型
	) {
		try {
			Assert.isTrue(StringUtils.isNotBlank(commentParentId), EnumInfrastructureResCode.COMMENT_PARENT_ID_NOT_NULL.code());
			LOGGER.info("comment_parent_id："+commentParentId +"查询评论父对象赞踩数量开始...");
			PraiseAndBelittleParent praiseAndBelittleParent = praiseAndBelittleParentService.findPraiseAndBelittleCommentParentByCommentParentId(commentParentId,commentParentType);
			
			Map<String,String> mapResult = new HashMap<String,String>();
			
			if (praiseAndBelittleParent == null) {
				mapResult.put("praise_num", "0");
				mapResult.put("belittle_num", "0");
			} else {
				mapResult.put("praise_num", String.valueOf(praiseAndBelittleParent.getPraise_num()));
				mapResult.put("belittle_num", String.valueOf(praiseAndBelittleParent.getBelittle_num()));
			}
			LOGGER.info("comment_parent_id："+commentParentId +"查询评论父对象赞踩数量结束...");
			return successJson(mapResult);
		} catch (Exception e) {
			LOGGER.error("==================comment_parent_id："+commentParentId +"查询评论父对象赞踩数量失败...");
			LOGGER.error("评论父对象赞踩数量查询信息错误，错误信息:"+e.getMessage());
			return errorJson(EnumResCode.SERVER_ERROR.value(), e.getMessage()); 
		}
	}
}
