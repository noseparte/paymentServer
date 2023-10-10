package com.lung.dao.impl.comments;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.lung.dao.comments.IPraiseAndBelittleParentDao;
import com.lung.model.comments.PraiseAndBelittleParent;
import com.lung.model.comments.PraiseAndBelittleParentDetail;
import com.lung.mongo.dao.GeneralICommentReportDaoImpl;

/**
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 * @Author Noseparte
 * @Compile 2018年8月21日 -- 下午6:01:47
 * @Version 1.0
 * @Description
 */
@Repository
public class PraiseAndBelittleParentDaoImpl extends GeneralICommentReportDaoImpl<PraiseAndBelittleParent> implements IPraiseAndBelittleParentDao {

	@Override
	protected Class<PraiseAndBelittleParent> getEntityClass() {
		return PraiseAndBelittleParent.class;
	}
	
	@Override
	public List<PraiseAndBelittleParent> findByConditions(
			String comment_parent_id, String comment_parent_type) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		if (StringUtils.isNotBlank(comment_parent_id)){
			criteria = criteria.and("comment_parent_id").is(comment_parent_id);
		}
		if (StringUtils.isNotBlank(comment_parent_type)){
			criteria = criteria.and("comment_parent_type").is(comment_parent_type);
		}
		query.addCriteria(criteria);
		return getMongoTemplate().find(query, getEntityClass(),"praise_or_belittle_comment_parent");
	}

	@Override
	public PraiseAndBelittleParent create(
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail) {
		PraiseAndBelittleParent praiseAndBelittleParent = new PraiseAndBelittleParent();
		// praiseAndBelittleParent.set_id(UUIDUtil.generateUUID());
		praiseAndBelittleParent.setCreate_id(praiseAndBelittleParentDetail.getPlayer_id());
		praiseAndBelittleParent.setCreate_date(new Date());
		praiseAndBelittleParent.setUpdate_id(praiseAndBelittleParentDetail.getPlayer_id());
		praiseAndBelittleParent.setUpdate_date(new Date());
		praiseAndBelittleParent.setComment_parent_id(praiseAndBelittleParentDetail.getComment_parent_id());
		praiseAndBelittleParent.setComment_parent_name(praiseAndBelittleParentDetail.getComment_parent_name());
		praiseAndBelittleParent.setComment_parent_type(praiseAndBelittleParentDetail.getComment_parent_type());
		 getMongoTemplate().insert(praiseAndBelittleParent, "praise_or_belittle_comment_parent");
		 Query query = new Query();
		 Criteria criteria = new Criteria();
		 criteria = criteria.and("_id").is(praiseAndBelittleParent.get_id());
		 query.addCriteria(criteria);
		 praiseAndBelittleParent = getMongoTemplate().findOne(query, PraiseAndBelittleParent.class, "praise_or_belittle_comment_parent");
		 return praiseAndBelittleParent;
	}

	@Override
	public PraiseAndBelittleParent updateByPraiseNumAndBelittleNum(
			String commentParentId, //
			String commentParentType,//
			Integer praiseCount,//
			Integer belittleCount//
	) {
		Query query = new Query();
		query.addCriteria(Criteria.where("comment_parent_id").is(commentParentId));
		query.addCriteria(Criteria.where("comment_parent_type").is(commentParentType));
		Update update = new Update();
        update.inc("praise_num", praiseCount);
        update.inc("belittle_num", belittleCount);
        getMongoTemplate().updateFirst(query, update, "praise_or_belittle_comment_parent");
        
	     query = new Query();
	     query.addCriteria(Criteria.where("comment_parent_id").is(commentParentId));
		 query.addCriteria(Criteria.where("comment_parent_type").is(commentParentType));
		 PraiseAndBelittleParent praiseAndBelittleParent = getMongoTemplate().findOne(query, PraiseAndBelittleParent.class, "praise_or_belittle_comment_parent");
		 return praiseAndBelittleParent;
	}

	@Override
	public List<PraiseAndBelittleParent> findByPIdAndPType(
			String commentParentId, String commentParentType) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		if (StringUtils.isNotBlank(commentParentId)){
			criteria = criteria.and("comment_parent_id").is(commentParentId);
		}
		if (StringUtils.isNotBlank(commentParentType)){
			criteria = criteria.and("comment_parent_type").is(commentParentType);
		}
		query.addCriteria(criteria);
		return getMongoTemplate().find(query, getEntityClass(),"praise_or_belittle_comment_parent");
	}


}
