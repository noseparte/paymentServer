package com.xmbl.service.comments;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.xmbl.dao.IGameServersDao;
import com.xmbl.dao.comments.ICommentsDao;
import com.xmbl.dao.comments.ICommentsPraiseDao;
import com.xmbl.dao.comments.ICommentsReplysDao;
import com.xmbl.dto.comments.CommentsDto;
import com.xmbl.dto.comments.CommentsReplysDto;
import com.xmbl.enumeration.EnumInfrastructureResCode;
import com.xmbl.model.GameServers;
import com.xmbl.model.comments.Comments;
import com.xmbl.model.comments.CommentsPraise;
import com.xmbl.model.comments.CommentsReplys;
import com.xmbl.util.platformSendYxServerUtil.PlatformSendYouXiServerUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名: StoryCommentsService
 * @创建时间: 2017年12月21日 下午8:58:19
 * @修改时间: 2017年12月21日 下午8:58:19
 * @类说明:
 */
@Slf4j
@Service
public class CommentsService{

	@Autowired
	private ICommentsDao iCommentsDao;
	@Autowired
	private ICommentsPraiseDao iCommentsPraiseDao;
	@Autowired
	private IGameServersDao gameServersDao;

	@Autowired
	private ICommentsReplysDao iCommentsReplysDao;

	public Long allCommentCountByConditions(//
			String comment_parent_id, //
			String comment_parent_type, //
			String player_id, String status//
	) {
		Long count = iCommentsDao.allCommentCountByConditions(comment_parent_id, comment_parent_type, player_id,
				status);
		return count;
	}

	public List<CommentsDto> copyProperties(//
			List<Comments> commentsLst, //
			String player_id//
	) {
		CommentsDto commentsDto = null;
		List<CommentsDto> commentsDtoList = new ArrayList<CommentsDto>();
		CommentsPraise commentsPraise = null;
		for (Comments comments : commentsLst) {
			commentsDto = new CommentsDto();
			BeanUtils.copyProperties(comments, commentsDto);
			commentsPraise = iCommentsPraiseDao.findByConditions(comments.get_id(), player_id);
			if (commentsPraise == null) {
				commentsDto.setCurrent_player_ispraise(false);
			} else {
				commentsDto.setCurrent_player_ispraise(commentsPraise.getPraise_status());
			}
			commentsDtoList.add(commentsDto);
		}
		return commentsDtoList;
	}

	public List<CommentsDto> copyProperties(//
			List<Comments> commentsLst, //
			String comment_parent_id, //
			String comment_parent_type, //
			String player_id, //
			String status) {
		CommentsDto commentsDto = null;
		List<CommentsDto> commentsDtoList = new ArrayList<CommentsDto>();
		CommentsPraise commentsPraise = null;
		for (Comments comments : commentsLst) {
			commentsDto = new CommentsDto();
			BeanUtils.copyProperties(comments, commentsDto);
			commentsPraise = iCommentsPraiseDao.findByConditions(comments.get_id(), player_id);
			if (commentsPraise == null) {
				commentsDto.setCurrent_player_ispraise(false);
			} else {
				commentsDto.setCurrent_player_ispraise(commentsPraise.getPraise_status());
			}
			//
			List<CommentsReplysDto> commentsReplysDtoLst = new ArrayList<CommentsReplysDto>();
			List<CommentsReplys> commentsReplysLst = iCommentsReplysDao.findCommentReplysLst(comments.get_id(), comments.getAuther_id(),status,
					"asc", 1, 2);
			CommentsReplysDto commentsReplysDto = null;
			for (CommentsReplys commentsReplys : commentsReplysLst) {
				commentsReplysDto = new CommentsReplysDto();
				BeanUtils.copyProperties(commentsReplys, commentsReplysDto);
				commentsReplysDtoLst.add(commentsReplysDto);
			}
			List<CommentsReplys> thirdCommentsReplysLst = iCommentsReplysDao.findCommentReplysLst(comments.get_id(), comments.getAuther_id(),status,
					"asc", 2, 2);
			if(thirdCommentsReplysLst != null && thirdCommentsReplysLst.size() > 0) {
				String third_player_name = thirdCommentsReplysLst.get(0).getPlayer_name();
				commentsDto.setThird_player_name(third_player_name);
			}
			commentsDto.setCommentsReplysDtoLst(commentsReplysDtoLst);
			commentsDtoList.add(commentsDto);
		}
		return commentsDtoList;
	}

	public Boolean isExistPlayerComment(String comment_parent_id, //
			String comment_parent_type, //
			String player_id, //
			String status//
	) {
		Boolean flag = iCommentsDao.isExistPlayerComment(comment_parent_id, comment_parent_type, player_id, status);
		return flag;
	}

	public Comments insertComments(Comments comments) {
		return iCommentsDao.insertComments(comments);
	}

	public Comments findById(String id) {
		Comments comments = iCommentsDao.findById(id);
		return comments;
	}

	public List<Comments> findCommentLst(//
			String comment_parent_id, //
			String comment_parent_type, //
			String player_id, //
			String status, //
			String type, //
			String sort, int pageNo, int pageSize) {
		List<Comments> commentsLst = iCommentsDao.findCommentLst(comment_parent_id, comment_parent_type, player_id,
				status, type, sort, pageNo, pageSize);
		return commentsLst;
	}

	public List<Comments> findCommentLst(//
			String comment_parent_id, //
			String comment_parent_type, //
			String player_id, //
			String status//
	) {
		List<Comments> commentsLst = iCommentsDao.findCommentLst(comment_parent_id, comment_parent_type, player_id,
				status);
		return commentsLst;
	}

	public boolean updateCommentsPraiseCountById(String id, Integer changeCount) {
		boolean flag = iCommentsDao.updatePraiseCountById(id, changeCount);
		return flag;
	}

	public boolean updateCommentsReplysCountById(String id, Integer changeCount) {
		boolean flag = iCommentsDao.updateReplysCountById(id, changeCount);
		return flag;
	}

	public Comments findByConditions(String comment_parent_id, String comment_parent_type, String player_id) {
		Comments comments = iCommentsDao.findByConditions(comment_parent_id, comment_parent_type, player_id);
		return comments;
	}

	public Long myCountByConditions(String comment_parent_id, String comment_parent_type, String player_id,
			String status) {
		Long count = iCommentsDao.myCountByConditions(comment_parent_id, comment_parent_type, player_id, status);
		return count;
	}

	public String sendGuanKaMessage(String comment_id,String comment_parent_id, String comment_parent_type, String player_id, String auther_id,
			String content) {
		String result = "";
		try {
			Long serverId = Long.parseLong(player_id.substring(0, player_id.length() - 14));
			log.info("当前用户所在的服务器为===========================：serverId,{}",serverId);
			GameServers gameServers = gameServersDao.getServerByServerIdAndTypeAndOperatorStatus(serverId,"World",1);
			Assert.isTrue(gameServers != null , EnumInfrastructureResCode.GAMESERVER_ERROR.code());
			log.info("=================游戏World服务器信息:{}", JSONObject.toJSONString(gameServers));
			// 发送的服务器地址
			StringBuilder rpcUrl = new StringBuilder();
			// ###
			// rpcUrl eg http://192.168.0.180:8223/SetChallenageRank
			// rpcUrl.append("http://192.168.0.180:9223");//
			rpcUrl.append("http://");//
			rpcUrl.append(gameServers.getRpcIp())//
					.append(":").append(gameServers.getRpcPort());//
			rpcUrl.append("/ReceiveComment");//
			// 0:挑战大师 | 1：闯关大师
			log.info("rpcUrl信息:{}", rpcUrl.toString());
			// 发送服务器的参数
			JSONObject Obj = new JSONObject();
			Obj.put("CommentID", comment_id);
			Obj.put("LCommentObjID", Long.parseLong(comment_parent_id));
			Obj.put("CommentType", comment_parent_type);
			Obj.put("PlayID", player_id);
			Obj.put("AuthID", auther_id);
			Obj.put("Content", content);
			Obj.put("ReplysID", -1L);
			String ObjInfo = Obj.toString();
			log.info("像Match服发送的奖励信息为：comment_parent_id,{},comment_parent_type,{},player_id,{},auther_id,{},content,{}",
					comment_parent_id, comment_parent_type, player_id, auther_id, content);
			String Result = PlatformSendYouXiServerUtil.send(rpcUrl.toString(), ObjInfo);
			if (StringUtils.isNotEmpty(Result)) {
				JSONObject resObj = JSONObject.parseObject(Result);
				result = resObj.getString("Result");
			}
		} catch (Exception e) {
			log.error("error：", e.getMessage());
		}
		return result;
	}
	
	public String sendGuanKaJiMessage(String comment_id,String comment_parent_id, String comment_parent_type, String player_id, String auther_id,
			String content) {
		String result = "";
		try {
			Long serverId = Long.parseLong(player_id.substring(0, player_id.length() - 14));
			log.info("当前用户所在的服务器为===========================：serverId,{}",serverId);
			GameServers gameServers = gameServersDao.getServerByServerIdAndTypeAndOperatorStatus(serverId,"World",1);
			Assert.isTrue(gameServers != null , EnumInfrastructureResCode.GAMESERVER_ERROR.code());
			log.info("=================游戏World服务器信息:{}", JSONObject.toJSONString(gameServers));
			// 发送的服务器地址
			StringBuilder rpcUrl = new StringBuilder();
			// ###
			// rpcUrl eg http://192.168.0.180:8223/SetChallenageRank
			// rpcUrl.append("http://192.168.0.180:9223");//
			rpcUrl.append("http://");//
			rpcUrl.append(gameServers.getRpcIp())//
			.append(":").append(gameServers.getRpcPort());//
			rpcUrl.append("/ReceiveComment");//
			// 0:挑战大师 | 1：闯关大师
			log.info("rpcUrl信息:{}", rpcUrl.toString());
			// 发送服务器的参数
			JSONObject Obj = new JSONObject();
			Obj.put("CommentID", comment_id);
			Obj.put("SCommentObjID", comment_parent_id);
			Obj.put("CommentType", comment_parent_type);
			Obj.put("PlayID", player_id);
			Obj.put("AuthID", auther_id);
			Obj.put("Content", content);
			Obj.put("ReplysID", -1L);
			String ObjInfo = Obj.toString();
			log.info("像Match服发送的奖励信息为：comment_parent_id,{},comment_parent_type,{},player_id,{},auther_id,{},content,{}",
					comment_parent_id, comment_parent_type, player_id, auther_id, content);
			String Result = PlatformSendYouXiServerUtil.send(rpcUrl.toString(), ObjInfo);
			if (StringUtils.isNotEmpty(Result)) {
				JSONObject resObj = JSONObject.parseObject(Result);
				result = resObj.getString("Result");
			}
		} catch (Exception e) {
			log.error("error：", e.getMessage());
		}
		return result;
	}

	public List<Comments> findCommentLsts(String comment_parent_id, String comment_parent_type, String player_id,
			String status, String type, String sort, int pageNo, int pageSize) {
		List<Comments> commentsLst = iCommentsDao.findCommentLsts(comment_parent_id, comment_parent_type, player_id,
				status, type, sort, pageNo, pageSize);
		return commentsLst;
	}

	public boolean remove(Comments comments) {
		try {
			if(null != comments) {
				iCommentsDao.remove(comments);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Boolean isExistStagePlayerComment(String comment_parent_id, String comment_parent_type, String nodeId,
			String stageId, String player_id, String status) {
		Boolean flag = iCommentsDao.isExistStagePlayerComment(comment_parent_id, comment_parent_type, nodeId, stageId, player_id, status);
		return flag;
	}

	public Long allStageNodeCommentCountByConditions(String comment_parent_id, String comment_parent_type,
			String nodeId, String stageId, String player_id, String status) {
		Long count = iCommentsDao.allStageNodeCommentCountByConditions(comment_parent_id, comment_parent_type, nodeId, stageId, player_id,
				status);
		return count;
	}

	public List<Comments> findStageNodeCommentLsts(String comment_parent_id, String comment_parent_type, String nodeId,
			String stageId, String player_id, String status, String type, String sort, int pageNo, int pageSize) {
		List<Comments> commentsLst = iCommentsDao.findStageNodeCommentLsts(comment_parent_id, comment_parent_type, nodeId, stageId, player_id,
				status, type, sort, pageNo, pageSize);
		return commentsLst;
	}

	public List<Comments> findStageNodeCommentLst(String comment_parent_id, String comment_parent_type, String nodeId,
			String stageId, String player_id, String status, String type, String sort, int pageNo, int pageSize) {
		List<Comments> commentsLst = iCommentsDao.findStageNodeCommentLst(comment_parent_id, comment_parent_type, nodeId, stageId, player_id,
				status, type, sort, pageNo, pageSize);
		return commentsLst;
	}
	
	public List<Comments> findStageNodeCommentLst(String comment_parent_id, String comment_parent_type, String nodeId,
			String stageId, String player_id, String status) {
		List<Comments> commentsLst = iCommentsDao.findStageNodeCommentLst(comment_parent_id, comment_parent_type, nodeId, stageId, player_id,
				status);
		return commentsLst;
	}

	public List<Comments> findStageTreeLst(String comment_parent_id, String player_id,
			String status, String type, String sort, int pageNo, int pageSize) {
		List<Comments> commentsLst = iCommentsDao.findStageTreeLst(comment_parent_id, player_id,
				status, type, sort, pageNo, pageSize);
		return commentsLst;
	}

	public List<Comments> findStageTreeLsts(String comment_parent_id, String player_id,
			String status, String type, String sort, int pageNo, int pageSize) {
		List<Comments> commentsLst = iCommentsDao.findStageTreeLsts(comment_parent_id, player_id,
				status, type, sort, pageNo, pageSize);
		return commentsLst;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String s = "%2B";
		String s2 = java.net.URLDecoder.decode(s, "UTF-8");
		System.out.println(s2);
	}

}
