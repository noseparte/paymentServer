package com.lung.dao.impl.comments;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.lung.dao.comments.ICommentsReplysDao;
import com.lung.model.comments.CommentsReplys;
import com.lung.mongo.dao.GeneralICommentReportDaoImpl;
import com.lung.util.LetterUtils;

/**
 * @author: noseparte
 * @Email: 1402614629@qq.com
 * @类名: StoryCommentsReplysDao
 * @创建时间: 2017年12月22日 下午5:58:06
 * @修改时间: 2017年12月22日 下午5:58:06
 * @类说明:
 */
@Repository
public class CommentsReplysDaoImpl extends GeneralICommentReportDaoImpl<CommentsReplys> implements ICommentsReplysDao {

	@Override
	protected Class<CommentsReplys> getEntityClass() {
		return CommentsReplys.class;
	}

	@Override
	public Long allCommentCountByStatus(String comments_id, String status) {
		Query query = new Query();
		query.addCriteria(Criteria.where("status").is(Integer.parseInt(status)));
		query.addCriteria(Criteria.where("comments_id").is(comments_id));
		Long count = this.getMongoTemplate().count(query, "comments_replys");
		return count;
	}

	@Override
	public List<CommentsReplys> findCommentReplysLst(String comments_id, String author_id, String status, String sort,
			int pageNo, int pageSize) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and("status").is(Integer.parseInt(status));
		criteria.and("comments_id").is(comments_id);
		query.addCriteria(criteria);
		String typeName = "create_date";
		query.with(
				new Sort("asc".equals(LetterUtils.toLowerCaseLetter(sort)) ? Direction.ASC : Direction.DESC, typeName));
		pageNo = pageNo <= 0 ? pageNo : pageNo - 1;
		query.skip(pageNo * pageSize);
		query.limit(pageSize);
		return getMongoTemplate().find(query, CommentsReplys.class, "comments_replys");
	}

	@Override
	public List<CommentsReplys> findCommentReplysLsts(String comments_id, String author_id, String status, String sort,
			int pageNo, int pageSize) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and("status").is(Integer.parseInt(status));
		criteria.and("comments_id").is(comments_id);
		criteria.and("player_id").ne(author_id);
		query.addCriteria(criteria);
		String typeName = "create_date";
		query.with(
				new Sort("asc".equals(LetterUtils.toLowerCaseLetter(sort)) ? Direction.ASC : Direction.DESC, typeName));
		pageNo = pageNo <= 0 ? pageNo : pageNo - 1;
		query.skip(pageNo * pageSize);
		query.limit(pageSize);
		return getMongoTemplate().find(query, CommentsReplys.class, "comments_replys");
	}

	@Override
	public CommentsReplys insertComments(CommentsReplys commentsReplys) {
		getMongoTemplate().insert(commentsReplys, "comments_replys");
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria = criteria.and("_id").is(commentsReplys.get_id());
		query.addCriteria(criteria);
		commentsReplys = getMongoTemplate().findOne(query, CommentsReplys.class, "comments_replys");
		return commentsReplys;
	}

	@Override
	public CommentsReplys findOneByCommentsIdAndReplysPid(String comments_id, String replys_id) {
		Query query = new Query();
		if (StringUtils.isNotBlank(comments_id) && !StringUtils.trim(comments_id).equals("")) {
			query.addCriteria(Criteria.where("comments_id").is(comments_id));
		}
		if (StringUtils.isNotBlank(replys_id) && !StringUtils.trim(replys_id).equals("")) {
			query.addCriteria(Criteria.where("comments_replys_pid").is(replys_id));
		}
		return this.getMongoTemplate().findOne(query, CommentsReplys.class, "comments_replys");
	}

	@Override
	public List<CommentsReplys> findByAuthReplys(String comments_id, String authorId, String status, String sort,
			int pageNo, int pageSize) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and("status").is(Integer.parseInt(status));
		criteria.and("comments_id").is(comments_id);
		criteria.and("player_id").is(authorId);
		query.addCriteria(criteria);
		String typeName = "create_date";
		query.with(
				new Sort("asc".equals(LetterUtils.toLowerCaseLetter(sort)) ? Direction.ASC : Direction.DESC, typeName));
		pageNo = pageNo <= 0 ? pageNo : pageNo - 1;
		query.skip(pageNo * pageSize);
		query.limit(pageSize);
		return getMongoTemplate().find(query, CommentsReplys.class, "comments_replys");
	}

	@Override
	public CommentsReplys findOneByCommentsId(String comments_replys_pid) {
		Query query = new Query();
		if (StringUtils.isNotBlank(comments_replys_pid) && !StringUtils.trim(comments_replys_pid).equals("")) {
			query.addCriteria(Criteria.where("_id").is(comments_replys_pid));
		}
		return this.getMongoTemplate().findOne(query, CommentsReplys.class, "comments_replys");
	}

	@Override
	public void removeLstByCommentId(String commentId) {
		Query query = new Query();
		if (StringUtils.isNotBlank(commentId) && !StringUtils.trim(commentId).equals("")) {
			query.addCriteria(Criteria.where("comments_id").is(commentId));
		}
		this.getMongoTemplate().findAllAndRemove(query, CommentsReplys.class, "comments_replys");
	}
	
	@Override
	public CommentsReplys findOneByCommentsIdAndReplysId(String commentId, String replysId) {
		Query query = new Query();
		if (StringUtils.isNotBlank(commentId) && !StringUtils.trim(commentId).equals("")) {
			query.addCriteria(Criteria.where("comments_id").is(commentId));
		}
		if (StringUtils.isNotBlank(replysId) && !StringUtils.trim(replysId).equals("")) {
			query.addCriteria(Criteria.where("_id").is(replysId));
		}
		return this.getMongoTemplate().findOne(query, CommentsReplys.class, "comments_replys");
	}

	@Override
	public void removeOneByCommentIdAndReplysId(String commentId, String replysId) {
		Query query = new Query();
		if (StringUtils.isNotBlank(commentId) && !StringUtils.trim(commentId).equals("")) {
			query.addCriteria(Criteria.where("comments_id").is(commentId));
		}
		if (StringUtils.isNotBlank(replysId) && !StringUtils.trim(replysId).equals("")) {
			query.addCriteria(Criteria.where("_id").is(replysId));
		}
		this.getMongoTemplate().findAllAndRemove(query, CommentsReplys.class, "comments_replys");
	}

}
