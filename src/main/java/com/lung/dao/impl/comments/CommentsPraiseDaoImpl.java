package com.lung.dao.impl.comments;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.lung.dao.comments.ICommentsPraiseDao;
import com.lung.model.comments.CommentsPraise;
import com.lung.mongo.dao.GeneralICommentReportDaoImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StoryCommentsPraiseDao 
 * @创建时间:  2017年12月22日 下午2:00:18
 * @修改时间:  2017年12月22日 下午2:00:18
 * @类说明:
 */
@Slf4j
@Repository
public class CommentsPraiseDaoImpl extends GeneralICommentReportDaoImpl<CommentsPraise> implements ICommentsPraiseDao {

	@Override
	protected Class<CommentsPraise> getEntityClass() {
		return CommentsPraise.class;
	}
	@Override
	public CommentsPraise findByConditions(String comments_id, String player_id) {
		log.info("根据条件查询某人的评论点赞信息开始");
		Query query = new Query();
		query.addCriteria(Criteria.where("comments_id").is(comments_id));
		query.addCriteria(Criteria.where("player_id").is(player_id));
		List<CommentsPraise> commentsPraiseLst = this.getMongoTemplate().find(query, CommentsPraise.class, "comments_praise");
		log.info("根据条件查询某人的评论点赞信息结束");
		if (commentsPraiseLst == null || commentsPraiseLst.size() == 0) {
			return null;
		}
		return commentsPraiseLst.get(0);
	}

	@Override
	public CommentsPraise insertOrUpdateCommentsPraise(
			CommentsPraise commentsPraise) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("comments_id").is(commentsPraise.getComments_id()));
			query.addCriteria(Criteria.where("player_id").is(commentsPraise.getPlayer_id()));
			if (StringUtils.isBlank(commentsPraise.get_id())) {
				this.getMongoTemplate().insert(commentsPraise, "comments_praise");
			} else {
				Update update = new Update();
				update.set("praise_status", commentsPraise.getPraise_status());
				this.getMongoTemplate().updateFirst(query, update, CommentsPraise.class, "comments_praise");
			}
			commentsPraise = this.getMongoTemplate().findOne(query, CommentsPraise.class, "comments_praise");
			return commentsPraise;
		} catch (Exception e) {
			log.error("==================点赞或取消赞操作失败,失败消息:"+e.getMessage());
		}
		return null;
	}


}
