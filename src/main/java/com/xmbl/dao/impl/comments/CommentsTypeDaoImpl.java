package com.xmbl.dao.impl.comments;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.xmbl.dao.comments.ICommentsTypeDao;
import com.xmbl.model.comments.CommentType;
import com.xmbl.mongo.dao.GeneralICommentReportDaoImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StoryCommentsDao 
 * @创建时间:  2017年12月21日 下午9:06:25
 * @修改时间:  2017年12月21日 下午9:06:25
 * @类说明:
 */
@Slf4j
@Repository
public class CommentsTypeDaoImpl extends GeneralICommentReportDaoImpl<CommentType> implements ICommentsTypeDao {
	
	@Override
	protected Class<CommentType> getEntityClass() {
		return CommentType.class;
	}

	@Override
	public Boolean hasCommentType(String comment_parent_type) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("numCode").is(comment_parent_type));
			Long count =  getMongoTemplate().count(query, getEntityClass());
			return count >0l;
		} catch (Exception e) {
			log.error("查询是否存在类型错误，错误信息:" + e.getMessage());
			return false;
		}
	}

	
	

}
