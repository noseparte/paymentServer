package com.lung.dao.impl.comments;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.lung.dao.comments.IPraiseAndBelittleParentDetailDao;
import com.lung.model.comments.PraiseAndBelittleParentDetail;
import com.lung.mongo.dao.GeneralICommentReportDaoImpl;

/**
 * 
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 * @Author Noseparte
 * @Compile 2018年8月21日 -- 下午6:01:40
 * @Version 1.0
 * @Description
 */
@Repository
public class PraiseAndBelittleParentDetailDaoImpl extends GeneralICommentReportDaoImpl<PraiseAndBelittleParentDetail>  implements IPraiseAndBelittleParentDetailDao {

	@Override
	protected Class<PraiseAndBelittleParentDetail> getEntityClass() {
		return PraiseAndBelittleParentDetail.class;
	}

	@Override
	public List<PraiseAndBelittleParentDetail> findByPIdAndPTypeAndPlayerId(
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail) {
		return null;
	}

	@Override
	public PraiseAndBelittleParentDetail create(
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail) {
		PraiseAndBelittleParentDetail praiseAndBelittleParentDetail1 = new PraiseAndBelittleParentDetail();
		// praiseAndBelittleParentDetail1.set_id(UUIDUtil.generateUUID());
		praiseAndBelittleParentDetail1.setCreate_id(praiseAndBelittleParentDetail.getPlayer_id());
		praiseAndBelittleParentDetail1.setCreate_date(new Date());
		praiseAndBelittleParentDetail1.setUpdate_id(praiseAndBelittleParentDetail.getPlayer_id());
		praiseAndBelittleParentDetail1.setUpdate_date(new Date());
		praiseAndBelittleParentDetail1.setComment_parent_id(praiseAndBelittleParentDetail.getComment_parent_id());
		praiseAndBelittleParentDetail1.setComment_parent_name(praiseAndBelittleParentDetail.getComment_parent_name());
		praiseAndBelittleParentDetail1.setComment_parent_praise_and_belittle_id(praiseAndBelittleParentDetail.getComment_parent_praise_and_belittle_id());
		praiseAndBelittleParentDetail1.setComment_parent_type(praiseAndBelittleParentDetail.getComment_parent_type());
		praiseAndBelittleParentDetail1.setPlayer_id(praiseAndBelittleParentDetail.getPlayer_id());
		praiseAndBelittleParentDetail1.setPlayer_name(praiseAndBelittleParentDetail.getPlayer_name());
		praiseAndBelittleParentDetail1.setPlayer_img(praiseAndBelittleParentDetail.getPlayer_img());
		praiseAndBelittleParentDetail1.setIs_praise_or_belittle("0");
		 getMongoTemplate().insert(praiseAndBelittleParentDetail1, "praise_or_belittle_comment_parent_detail");
		 Query query = new Query();
		 Criteria criteria = new Criteria();
		 criteria = criteria.and("_id").is(praiseAndBelittleParentDetail1.get_id());
		 query.addCriteria(criteria);
		 praiseAndBelittleParentDetail = getMongoTemplate().findOne(query, PraiseAndBelittleParentDetail.class, "praise_or_belittle_comment_parent_detail");
		 return praiseAndBelittleParentDetail;
	}

	@Override
	public List<PraiseAndBelittleParentDetail> findByConditions(//
			String commentParentId, //
			String commentParentType, //
			String playerId//
	) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		if (StringUtils.isNotBlank(commentParentId)){
			criteria = criteria.and("comment_parent_id").is(commentParentId);
		}
		if (StringUtils.isNotBlank(commentParentType)){
			criteria = criteria.and("comment_parent_type").is(commentParentType);
		}
		if (StringUtils.isNotBlank(playerId)){
			criteria = criteria.and("player_id").is(playerId);
		}
		query.addCriteria(criteria);
		return getMongoTemplate().find(query, getEntityClass(), "praise_or_belittle_comment_parent_detail");
	}

	@Override
	public List<PraiseAndBelittleParentDetail> findByConditions(
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		String comment_parent_praise_and_belittle_id = praiseAndBelittleParentDetail.getComment_parent_praise_and_belittle_id();
		if (StringUtils.isNotBlank(comment_parent_praise_and_belittle_id)){
			criteria = criteria.and("comment_parent_praise_and_belittle_id").is(comment_parent_praise_and_belittle_id);
		}
		String playerId = praiseAndBelittleParentDetail.getPlayer_id();
		if (StringUtils.isNotBlank(playerId)){
			criteria = criteria.and("player_id").is(playerId);
		}
		query.addCriteria(criteria);
		return getMongoTemplate().find(query, getEntityClass(), "praise_or_belittle_comment_parent_detail");
	}

	@Override
	public PraiseAndBelittleParentDetail updateByEntity(
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail) {
		Query query = new Query();
		query.addCriteria(Criteria.where("comment_parent_id").is(praiseAndBelittleParentDetail.getComment_parent_id()));  
		query.addCriteria(Criteria.where("comment_parent_type").is(praiseAndBelittleParentDetail.getComment_parent_type()));  
        query.addCriteria(Criteria.where("player_id").is(praiseAndBelittleParentDetail.getPlayer_id()));  
		Update update = new Update();
		update.set("is_praise_or_belittle", praiseAndBelittleParentDetail.getIs_praise_or_belittle());  
		getMongoTemplate().upsert(query, update, "praise_or_belittle_comment_parent_detail");
		
		List<PraiseAndBelittleParentDetail> praiseAndBelittleParentDetailLst = findByConditions(praiseAndBelittleParentDetail);
		if (praiseAndBelittleParentDetailLst.size() >=1){
			return praiseAndBelittleParentDetailLst.get(0);
		}
		return null;
	}

}
