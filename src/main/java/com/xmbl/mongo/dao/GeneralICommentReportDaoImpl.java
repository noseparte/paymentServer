package com.xmbl.mongo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.xmbl.mongo.util.Pagination;


@Repository("generalDao")
public abstract  class GeneralICommentReportDaoImpl<T> implements GeneralDao<T> {

	@Autowired
	private MongoTemplate mongoTemplateComment;

	public MongoTemplate getMongoTemplate() {
		return mongoTemplateComment;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplateComment) {
		this.mongoTemplateComment = mongoTemplateComment;
	}

	public List<T> find(Query query) {
		return mongoTemplateComment.find(query, this.getEntityClass());
	}
	
	public Pagination<T> findPaginationByQuery(Query query, int pageNo,
			int pageSize) {
		if (query == null) {
			query = new Query();
		}
		long totalCount = this.mongoTemplateComment
				.count(query, this.getEntityClass());
		Pagination<T> page = new Pagination<T>(pageNo, pageSize, totalCount);
		query.skip(page.getFirstResult());// skip相当于从那条记录.
		query.limit(pageSize);// 从skip,取多少条记录
		List<T> datas = this.find(query);
		page.setDatas(datas);
		return page;
	}
	
	public void insert(T t) {
		this.mongoTemplateComment.insert(t);
	}

	public void save(T t) {
		this.mongoTemplateComment.save(t);
	}

	public void remove(T t) {
		this.mongoTemplateComment.remove(t);

	}

	public void updateFirst(Query query, Update update) {
		this.mongoTemplateComment.updateFirst(query, update, this.getEntityClass());
	}

	public T findOneById(String id) {

		return this.mongoTemplateComment.findById(id, this.getEntityClass());
	}

	public T findAndModify(Query query, Update update) {
		return this.mongoTemplateComment.findAndModify(query, update,
				this.getEntityClass());
	}

	public T findAndRemove(Query query) {
		return this.mongoTemplateComment.findAndRemove(query, this.getEntityClass());
	}

	public T findByIdAndCollectionName(String id, String collectionName) {
		return this.mongoTemplateComment.findById(id, this.getEntityClass(),
				collectionName);
	}

	public T findOneByQuery(Query query) {
		// TODO Auto-generated method stub
		return this.mongoTemplateComment.findOne(query, this.getEntityClass());
	}

	public void updateAllByQuery(Query query, Update update) {
		this.mongoTemplateComment.updateMulti(query, update, this.getEntityClass());

	}

	public Integer findCountByQuery(Query query) {
		Long totalCount = this.mongoTemplateComment
				.count(query, this.getEntityClass());
		return Integer.parseInt(String.valueOf(totalCount));
	}
	protected abstract Class<T> getEntityClass();
	
	

}
