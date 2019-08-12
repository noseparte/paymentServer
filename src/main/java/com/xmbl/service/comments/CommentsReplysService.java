package com.xmbl.service.comments;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.xmbl.dao.IGameServersDao;
import com.xmbl.dao.comments.ICommentsReplysDao;
import com.xmbl.dto.comments.CommentsReplysDto;
import com.xmbl.enumeration.EnumInfrastructureResCode;
import com.xmbl.model.GameServers;
import com.xmbl.model.comments.CommentsReplys;
import com.xmbl.util.platformSendYxServerUtil.PlatformSendYouXiServerUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StoryCommentsReplysService 
 * @创建时间:  2017年12月22日 下午5:54:53
 * @修改时间:  2017年12月22日 下午5:54:53
 * @类说明:
 */
@Slf4j
@Service
public class CommentsReplysService{

	@Autowired
	private ICommentsReplysDao iCommentsReplysDao;
	@Autowired
	private IGameServersDao gameServersDao;
	
	public Long allCommentReplysCountByStatus(String comments_id, String status) {
		Long count = iCommentsReplysDao.allCommentCountByStatus(comments_id,status);
		return count;
	}

	public List<CommentsReplys> findCommentReplysLst(//
			String comments_id,String author_id,
			String status, String sort, int pageNo, int pageSize) {
		List<CommentsReplys> commentsLst = iCommentsReplysDao.findCommentReplysLst(comments_id, author_id, status,sort,pageNo,pageSize);
		return commentsLst;
	}
	
	public List<CommentsReplys> findCommentReplysLsts(//
			String comments_id,String author_id,
			String status, String sort, int pageNo, int pageSize) {
		List<CommentsReplys> commentsLst = iCommentsReplysDao.findCommentReplysLsts(comments_id, author_id, status,sort,pageNo,pageSize);
		return commentsLst;
	}

	public List<CommentsReplysDto> copyProperties(
			List<CommentsReplys> commentsReplysLst) {
		List<CommentsReplysDto> commentsReplysDtoLst = new ArrayList<CommentsReplysDto>();
		CommentsReplysDto commentsReplysDto = null;
		for (CommentsReplys commentsReplys : commentsReplysLst) {
			commentsReplysDto = new CommentsReplysDto();
			BeanUtils.copyProperties(commentsReplys, commentsReplysDto);
			commentsReplysDtoLst.add(commentsReplysDto);
		}
		return commentsReplysDtoLst;
	}

	public CommentsReplys create(CommentsReplys commentsReplys) {
		return iCommentsReplysDao.insertComments(commentsReplys);
	}

	/**
	 * 通知作者 (关卡)
	 * @param comment_id
	 * @param comments_replys_pid
	 * @param comment_parent_id
	 * @param comment_parent_type
	 * @param playerId
	 * @param auther_id
	 * @param content
	 * @return
	 */
	public String sendGuanKaMessage(String comment_id, String comments_replys_pid, String comment_parent_id,
			String comment_parent_type, String playerId, String auther_id, String content) {
		String result = "";
		try {
			Long serverId = Long.parseLong(playerId.substring(0, playerId.length() - 14));
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
			Obj.put("PlayID", playerId);
			Obj.put("AuthID", auther_id);
			Obj.put("Content", content);
			Obj.put("ReplysID", "-1");
			String ObjInfo = Obj.toString();
			log.info("向World服发送的奖励信息为：CommentID,{},CommentObjID,{},CommentType,{},PlayID,{},AuthID,{},Content,{},ReplysID,{}",
					comment_id, comment_parent_id, comment_parent_type, playerId, auther_id, content, comments_replys_pid);
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

	/**
	 * 通知作者 (关卡集)
	 * @param comment_id
	 * @param comments_replys_pid
	 * @param comment_parent_id
	 * @param comment_parent_type
	 * @param playerId
	 * @param auther_id
	 * @param content
	 * @return
	 */
	public String sendGuanKaJiMessage(String comment_id, String comments_replys_pid, String comment_parent_id,
			String comment_parent_type, String playerId, String auther_id, String content) {
		String result = "";
		try {
			Long serverId = Long.parseLong(playerId.substring(0, playerId.length() - 14));
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
			Obj.put("PlayID", playerId);
			Obj.put("AuthID", auther_id);
			Obj.put("Content", content);
			Obj.put("ReplysID", "-1");
			String ObjInfo = Obj.toString();
			log.info("向World服发送的奖励信息为：CommentID,{},CommentObjID,{},CommentType,{},PlayID,{},AuthID,{},Content,{},ReplysID,{}",
					comment_id, comment_parent_id, comment_parent_type, playerId, auther_id, content, comments_replys_pid);
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
	
	/**
	 * 通知玩家 (关卡)
	 * @param comment_id
	 * @param replysPlayerId
	 * @param comment_parent_id
	 * @param comment_parent_type
	 * @param playerId
	 * @param auther_id
	 * @param content
	 * @return
	 */
	public String sendGuanKaReplysMessage(String comment_id, String replysPlayerId, String comment_parent_id,
			String comment_parent_type, String playerId, String auther_id, String content) {
		String result = "";
		try {
			Long serverId = Long.parseLong(playerId.substring(0, playerId.length() - 14));
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
			rpcUrl.append("/ReceiveReplys");//
			// 0:挑战大师 | 1：闯关大师
			log.info("rpcUrl信息:{}", rpcUrl.toString());
			// 发送服务器的参数
			JSONObject Obj = new JSONObject();
			Obj.put("CommentID", comment_id);
			Obj.put("LCommentObjID", Long.parseLong(comment_parent_id));
			Obj.put("CommentType", comment_parent_type);
			Obj.put("PlayID", playerId);
			Obj.put("AuthID", auther_id);
			Obj.put("Content", content);
			Obj.put("ReplysID", replysPlayerId);
			String ObjInfo = Obj.toString();
			log.info("向World服发送的奖励信息为：CommentID,{},CommentObjID,{},CommentType,{},PlayID,{},AuthID,{},Content,{},ReplysID,{}",
					comment_id, comment_parent_id, comment_parent_type, playerId, auther_id, content, replysPlayerId);
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
	
	/**
	 * 通知玩家 (关卡集)
	 * @param comment_id
	 * @param replysPlayerId
	 * @param comment_parent_id
	 * @param comment_parent_type
	 * @param playerId
	 * @param auther_id
	 * @param content
	 * @return
	 */
	public String sendGuanKaJiReplysMessage(String comment_id, String replysPlayerId, String comment_parent_id,
			String comment_parent_type, String playerId, String auther_id, String content) {
		String result = "";
		try {
			Long serverId = Long.parseLong(playerId.substring(0, playerId.length() - 14));
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
			rpcUrl.append("/ReceiveReplys");//
			// 0:挑战大师 | 1：闯关大师
			log.info("rpcUrl信息:{}", rpcUrl.toString());
			// 发送服务器的参数
			JSONObject Obj = new JSONObject();
			Obj.put("CommentID", comment_id);
			Obj.put("SCommentObjID", comment_parent_id);
			Obj.put("CommentType", comment_parent_type);
			Obj.put("PlayID", playerId);
			Obj.put("AuthID", auther_id);
			Obj.put("Content", content);
			Obj.put("ReplysID", replysPlayerId);
			String ObjInfo = Obj.toString();
			log.info("向World服发送的奖励信息为：CommentID,{},CommentObjID,{},CommentType,{},PlayID,{},AuthID,{},Content,{},ReplysID,{}",
					comment_id, comment_parent_id, comment_parent_type, playerId, auther_id, content, replysPlayerId);
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

	public CommentsReplys findOneByCommentsIdAndReplysPid(String comments_id, String comments_replys_pid) {
		return iCommentsReplysDao.findOneByCommentsIdAndReplysPid(comments_id,comments_replys_pid);
	}

	public List<CommentsReplys> findByAuthReplys(String comments_id, String authId, String status, String sort, int pageNo,
			int pageSize) {
		List<CommentsReplys> commentsLst = iCommentsReplysDao.findByAuthReplys(comments_id,authId, status,sort,pageNo,pageSize);
		return commentsLst;
	}

	public CommentsReplys findOneByCommentsId(String comments_replys_pid) {
		return iCommentsReplysDao.findOneByCommentsId(comments_replys_pid);
	}

	public void removeLstByCommentId(String commentId) {
		iCommentsReplysDao.removeLstByCommentId(commentId);
	}

	public void removeOneByCommentIdAndReplysId(String commentId, String replysId) {
		iCommentsReplysDao.removeOneByCommentIdAndReplysId(commentId,replysId);
	}

	public CommentsReplys findOneByCommentsIdAndReplysId(String commentId, String replysId) {
		return iCommentsReplysDao.findOneByCommentsIdAndReplysId(commentId,replysId);
	}
	
	
}
