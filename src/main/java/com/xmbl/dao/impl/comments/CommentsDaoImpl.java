package com.xmbl.dao.impl.comments;

import java.util.List;


import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.xmbl.dao.comments.ICommentsDao;
import com.xmbl.enumeration.EnumCommentCode;
import com.xmbl.model.comments.Comments;
import com.xmbl.mongo.dao.GeneralICommentReportDaoImpl;
import com.xmbl.util.EnumUtils;
import com.xmbl.util.LetterUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 * 
 * @Author Noseparte
 * @Compile 2018年8月21日 -- 下午5:53:53
 * @Version 1.0
 * @Description
 */
@Slf4j
@Repository
public class CommentsDaoImpl extends GeneralICommentReportDaoImpl<Comments> implements ICommentsDao {

	@Override
	protected Class<Comments> getEntityClass() {
		return Comments.class;
	}

	@Override
	public Boolean isExistPlayerComment(String comment_parent_id, String comment_parent_type, String player_id,
			String status) {
		log.info("查询评论是否存在开始");
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria = criteria.and("status").is(Integer.parseInt(status));
		criteria = criteria.and("comment_parent_id").is(comment_parent_id);
		criteria = criteria.and("comment_parent_type").is(comment_parent_type);
		criteria = criteria.and("player_id").is(player_id);
		query.addCriteria(criteria);
		Long count = getMongoTemplate().count(query, "comments");
		log.info("查询评论是否存在结束");
		return count != null && count >= 1;
	}

	@Override
	public Comments insertComments(Comments comments) {
		log.info("添加评论是否存在开始");
		getMongoTemplate().insert(comments, "comments");
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria = criteria.and("_id").is(comments.get_id());
		query.addCriteria(criteria);
		comments = getMongoTemplate().findOne(query, Comments.class, "comments");
		log.info("添加评论是否存在结束");
		return comments;
	}

	@Override
	public Long allCommentCountByConditions(String comment_parent_id, String comment_parent_type, String player_id,
			String status) {
		log.info("根据条件查询某模块的评论数量:条件如下,comment_parent_id:{},comment_parent_type{},status:{}", //
				comment_parent_id, comment_parent_type, status);
		Query query = new Query();
		query.addCriteria(Criteria.where("status").is(Integer.parseInt(status)));
		// query.addCriteria(Criteria.where("player_id").ne(player_id));
		query.addCriteria(Criteria.where("comment_parent_id").is(comment_parent_id));
		query.addCriteria(Criteria.where("comment_parent_type").is(comment_parent_type));
		Long count = this.getMongoTemplate().count(query, "comments");
		log.info("根据条件查询某模块评论数量结束");
		return count;
	}

	@Override
	public List<Comments> findCommentLst(//
			String comment_parent_id, String comment_parent_type, String player_id, //
			String status, String type, String sort, int pageNo, int pageSize//
	) {
		Query query2 = new Query();
		Criteria criteria2 = new Criteria();
		criteria2.and("status").is(Integer.parseInt(status));
		criteria2.and("player_id").ne(player_id);
		criteria2.and("comment_parent_id").is(comment_parent_id);
		criteria2.and("comment_parent_type").is(comment_parent_type);
		query2.addCriteria(criteria2);
		String typeName = EnumUtils.getTypeNameByTypeId(type);
		query2.with(
				new Sort("asc".equals(LetterUtils.toLowerCaseLetter(sort)) ? Direction.ASC : Direction.DESC, typeName));
		query2.with(new Sort(new Order(Direction.ASC,"create_date")));
		pageNo = pageNo <= 0 ? pageNo : pageNo - 1;
		query2.skip(pageNo * pageSize);
		query2.limit(pageSize);
		List<Comments> commentsLst2 = getMongoTemplate().find(query2, Comments.class, "comments");
		return commentsLst2;
	}

	@Override
	public List<Comments> findCommentLst(String comment_parent_id, String comment_parent_type, String player_id,
			String status) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and("player_id").is(player_id);
		criteria.and("status").is(Integer.parseInt(status));
		criteria.and("comment_parent_id").is(comment_parent_id);
		criteria.and("comment_parent_type").is(comment_parent_type);
		query.addCriteria(criteria);
		List<Comments> commentsLst1 = getMongoTemplate().find(query, Comments.class, "comments");
		return commentsLst1;
	}

	@Override
	public Comments findById(String id) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria = criteria.and("_id").is(id);
		criteria = criteria.and("status").is(0);
		query.addCriteria(criteria);
		List<Comments> commentsLst = getMongoTemplate().find(query, Comments.class, "comments");
		if (commentsLst == null || commentsLst.size() == 0) {
			return null;
		}
		return commentsLst.get(0);
	}

	@Override
	public boolean updatePraiseCountById(String id, Integer changeCount) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(id));
			Update update = new Update();
			update.inc("praisecnt", changeCount);
			getMongoTemplate().updateFirst(query, update, "comments");
			log.info("更新点赞数成功!");
			return true;
		} catch (Exception e) {
			log.error("==================更新点赞数失败,失败信息:" + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean updateReplysCountById(String id, Integer changeCount) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(id));
			Update update = new Update();
			update.inc("replycnt", changeCount);
			getMongoTemplate().updateFirst(query, update, "comments");
			log.info("更新评论回复数成功!");
			return true;
		} catch (Exception e) {
			log.error("=====================更新评论回复数失败,失败信息:" + e.getMessage());
			return false;
		}
	}

	@Override
	public Comments findByConditions(String comment_parent_id, String comment_parent_type, String player_id) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria = criteria.and("comment_parent_id").is(comment_parent_id);
		criteria = criteria.and("comment_parent_type").is(comment_parent_type);
		criteria = criteria.and("status").is(0);
		query.addCriteria(criteria);
		List<Comments> commentsLst = getMongoTemplate().find(query, Comments.class, "comments");
		if (commentsLst == null || commentsLst.size() == 0) {
			return null;
		}
		return commentsLst.get(0);
	}

	@Override
	public Long myCountByConditions(String comment_parent_id, String comment_parent_type, String player_id,
			String status) {
		log.info("根据条件查询某模块的评论数量:条件如下,comment_parent_id:{},comment_parent_type{},status:{}", //
				comment_parent_id, comment_parent_type, status);
		Query query = new Query();
		query.addCriteria(Criteria.where("status").is(Integer.parseInt(status)));
		query.addCriteria(Criteria.where("player_id").is(player_id));
		query.addCriteria(Criteria.where("comment_parent_id").is(comment_parent_id));
		query.addCriteria(Criteria.where("comment_parent_type").is(comment_parent_type));
		Long count = this.getMongoTemplate().count(query, "comments");
		log.info("根据条件查询某模块评论数量结束");
		return count;
	}

	@Override
	public Comments findOneParentCommentOfCommentMaxPraise(String comment_parent_id, String comment_parent_type,
			String status) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("status").is(Integer.parseInt(status)));
			query.addCriteria(Criteria.where("comment_parent_id").is(comment_parent_id));
			query.addCriteria(Criteria.where("comment_parent_type").is(comment_parent_type));
			query.with(new Sort(Direction.DESC, "praisecnt"));
			query.skip(0);
			query.limit(1);
			List<Comments> commentsLst = getMongoTemplate().find(query, Comments.class, "comments");
			if (commentsLst != null && commentsLst.size() > 0) {
				return commentsLst.get(0);
			}
			return null;
		} catch (Exception e) {
			log.error("根据条件查询某个父模块下的所有评论中赞最多的一个评论出错啦，错误信息:", e.getMessage());
			return null;
		}
	}

	@Override
	public List<Comments> findCommentLsts(String comment_parent_id, String comment_parent_type, String player_id,
			String status, String type, String sort, int pageNo, int pageSize) {
		Query query2 = new Query();
		Criteria criteria2 = new Criteria();
		criteria2.and("status").is(Integer.parseInt(status));
		criteria2.and("comment_parent_id").is(comment_parent_id);
		criteria2.and("comment_parent_type").is(comment_parent_type);
		query2.addCriteria(criteria2);
		String typeName = EnumUtils.getTypeNameByTypeId(type);
		query2.with(
				new Sort("asc".equals(LetterUtils.toLowerCaseLetter(sort)) ? Direction.ASC : Direction.DESC, typeName));
		query2.with(new Sort(new Order(Direction.ASC,"create_date")));
		pageNo = pageNo <= 0 ? pageNo : pageNo - 1;
		query2.skip(pageNo * pageSize);
		query2.limit(pageSize);
		List<Comments> commentsLst2 = getMongoTemplate().find(query2, Comments.class, "comments");
		return commentsLst2;
	}

	@Override
	public Boolean isExistStagePlayerComment(String comment_parent_id, String comment_parent_type, String nodeId,
			String stageId, String player_id, String status) {
		log.info("查询评论是否存在开始");
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria = criteria.and("status").is(Integer.parseInt(status));
		criteria = criteria.and("comment_parent_id").is(comment_parent_id);
		criteria = criteria.and("comment_parent_type").is(comment_parent_type);
		criteria = criteria.and("nodeId").is(nodeId);
		criteria = criteria.and("stageId").is(stageId);
		criteria = criteria.and("player_id").is(player_id);
		query.addCriteria(criteria);
		Long count = getMongoTemplate().count(query, "comments");
		log.info("查询评论是否存在结束");
		return count != null && count >= 1;
	}

	@Override
	public Long allStageNodeCommentCountByConditions(String comment_parent_id, String comment_parent_type,
			String nodeId, String stageId, String player_id, String status) {
		log.info("根据条件查询某模块的评论数量:条件如下,comment_parent_id:{},comment_parent_type{},status:{}", //
				comment_parent_id, comment_parent_type, status);
		Query query = new Query();
		query.addCriteria(Criteria.where("status").is(Integer.parseInt(status)));
		// query.addCriteria(Criteria.where("player_id").ne(player_id));
		query.addCriteria(Criteria.where("comment_parent_id").is(comment_parent_id));
		query.addCriteria(Criteria.where("comment_parent_type").is(comment_parent_type));
		query.addCriteria(Criteria.where("nodeId").is(nodeId));
		query.addCriteria(Criteria.where("stageId").is(stageId));
		Long count = this.getMongoTemplate().count(query, "comments");
		log.info("根据条件查询某模块评论数量结束");
		return count;
	}

	@Override
	public List<Comments> findStageNodeCommentLsts(String comment_parent_id, String comment_parent_type, String nodeId,
			String stageId, String player_id, String status, String type, String sort, int pageNo, int pageSize) {
		Query query2 = new Query();
		Criteria criteria2 = new Criteria();
		criteria2.and("status").is(Integer.parseInt(status));
		criteria2.and("comment_parent_id").is(comment_parent_id);
		criteria2.and("comment_parent_type").is(comment_parent_type);
		criteria2.and("nodeId").is(nodeId);
		criteria2.and("stageId").is(stageId);
		query2.addCriteria(criteria2);
		String typeName = EnumUtils.getTypeNameByTypeId(type);
		query2.with(
				new Sort("asc".equals(LetterUtils.toLowerCaseLetter(sort)) ? Direction.ASC : Direction.DESC, typeName));
		query2.with(new Sort(new Order(Direction.ASC,"create_date")));
		pageNo = pageNo <= 0 ? pageNo : pageNo - 1;
		query2.skip(pageNo * pageSize);
		query2.limit(pageSize);
		List<Comments> commentsLst2 = getMongoTemplate().find(query2, Comments.class, "comments");
		return commentsLst2;
	}

	@Override
	public List<Comments> findStageNodeCommentLst(String comment_parent_id, String comment_parent_type, String nodeId,
			String stageId, String player_id, String status, String type, String sort, int pageNo, int pageSize) {
		Query query2 = new Query();
		Criteria criteria2 = new Criteria();
		criteria2.and("status").is(Integer.parseInt(status));
		criteria2.and("player_id").ne(player_id);
		criteria2.and("comment_parent_id").is(comment_parent_id);
		criteria2.and("comment_parent_type").is(comment_parent_type);
		criteria2.and("nodeId").is(nodeId);
		criteria2.and("stageId").is(stageId);
		query2.addCriteria(criteria2);
		String typeName = EnumUtils.getTypeNameByTypeId(type);
		query2.with(
				new Sort("asc".equals(LetterUtils.toLowerCaseLetter(sort)) ? Direction.ASC : Direction.DESC, typeName));
		query2.with(new Sort(new Order(Direction.ASC,"create_date")));
		pageNo = pageNo <= 0 ? pageNo : pageNo - 1;
		query2.skip(pageNo * pageSize);
		query2.limit(pageSize);
		List<Comments> commentsLst2 = getMongoTemplate().find(query2, Comments.class, "comments");
		return commentsLst2;
	}

	@Override
	public List<Comments> findStageNodeCommentLst(String comment_parent_id, String comment_parent_type, String nodeId,
			String stageId, String player_id, String status) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and("player_id").is(player_id);
		criteria.and("status").is(Integer.parseInt(status));
		criteria.and("comment_parent_id").is(comment_parent_id);
		criteria.and("comment_parent_type").is(comment_parent_type);
		criteria.and("nodeId").is(nodeId);
		criteria.and("stageId").is(stageId);
		query.addCriteria(criteria);
		List<Comments> commentsLst1 = getMongoTemplate().find(query, Comments.class, "comments");
		return commentsLst1;
	}

	@Override
	public List<Comments> findStageTreeLst(String comment_parent_id, String player_id,
			String status, String type, String sort, int pageNo, int pageSize) {
		Query query1 = new Query();		//总的
		Query query2 = new Query();		//小关
		Criteria criteria1 = new Criteria();	//总的条件	
		Criteria criteria2 = new Criteria();	//小关条件
		criteria1.and("status").is(Integer.parseInt(status));
		criteria1.and("comment_parent_id").is(comment_parent_id);
		criteria1.and("comment_parent_type").is(EnumCommentCode.STAGE_TREE_BATTLE.getNumcode());
		criteria1.and("player_id").ne(player_id);
		query1.addCriteria(criteria1);
		String typeName = EnumUtils.getTypeNameByTypeId(type);
		query1.with(
				new Sort("asc".equals(LetterUtils.toLowerCaseLetter(sort)) ? Direction.ASC : Direction.DESC, typeName));
		query1.with(new Sort(new Order(Direction.ASC,"create_date")));
		pageNo = pageNo <= 0 ? pageNo : pageNo - 1;
		query1.skip(pageNo * pageSize);
		query1.limit(pageSize);
		List<Comments> commentsLst1 = getMongoTemplate().find(query1, Comments.class, "comments");
		
		criteria2.and("status").is(Integer.parseInt(status));
		criteria2.and("comment_parent_id").is(comment_parent_id);
		criteria2.and("comment_parent_type").is(EnumCommentCode.STAGE_TREE_BATTLE_STAGE.getNumcode());
		query2.addCriteria(criteria2);
		query2.with(
				new Sort("asc".equals(LetterUtils.toLowerCaseLetter(sort)) ? Direction.ASC : Direction.DESC, typeName));
		query1.with(new Sort(new Order(Direction.ASC,"create_date")));
		pageNo = pageNo <= 0 ? pageNo : pageNo - 1;
		query2.skip(pageNo * pageSize);
		query2.limit(pageSize);
		query2.limit(pageSize);
		List<Comments> commentsLst2 = getMongoTemplate().find(query2, Comments.class, "comments");
		
		commentsLst1.addAll(commentsLst2);
		return commentsLst1;
	}

	@Override
	public List<Comments> findStageTreeLsts(String comment_parent_id, String player_id,
			String status, String type, String sort, int pageNo, int pageSize) {
		Query query1 = new Query();		//总的
		Criteria criteria1 = new Criteria();	//总的条件	
		criteria1.and("status").is(Integer.parseInt(status));
		criteria1.and("comment_parent_id").is(comment_parent_id);
		query1.addCriteria(criteria1);
		String typeName = EnumUtils.getTypeNameByTypeId(type);
		query1.with(
				new Sort("asc".equals(LetterUtils.toLowerCaseLetter(sort)) ? Direction.ASC : Direction.DESC, typeName));
		query1.with(new Sort(new Order(Direction.ASC,"create_date")));
		pageNo = pageNo <= 0 ? pageNo : pageNo - 1;
		query1.skip(pageNo * pageSize);
		query1.limit(pageSize);
		List<Comments> commentsLst1 = getMongoTemplate().find(query1, Comments.class, "comments");
		return commentsLst1;
	}

}
