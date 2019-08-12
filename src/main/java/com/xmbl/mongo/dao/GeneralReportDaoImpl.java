package com.xmbl.mongo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.xmbl.mongo.util.Pagination;


@Repository("generalDao")
public abstract  class GeneralReportDaoImpl<T> implements GeneralDao<T> {

	@Autowired
	private MongoTemplate mongoTemplateReport;

	public MongoTemplate getMongoTemplate() {
		return mongoTemplateReport;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplateReport) {
		this.mongoTemplateReport = mongoTemplateReport;
	}

	public List<T> find(Query query) {
		return mongoTemplateReport.find(query, this.getEntityClass());
	}
	
	public Pagination<T> findPaginationByQuery(Query query, int pageNo,
			int pageSize) {
		if (query == null) {
			query = new Query();
		}
		long totalCount = this.mongoTemplateReport
				.count(query, this.getEntityClass());
		Pagination<T> page = new Pagination<T>(pageNo, pageSize, totalCount);
		query.skip(page.getFirstResult());// skip相当于从那条记录.
		query.limit(pageSize);// 从skip,取多少条记录
		List<T> datas = this.find(query);
		page.setDatas(datas);
		return page;
	}
	
	public void insert(T t) {
		this.mongoTemplateReport.insert(t);
	}

	public void save(T t) {
		this.mongoTemplateReport.save(t);
	}

	public void remove(T t) {
		this.mongoTemplateReport.remove(t);

	}

	public void updateFirst(Query query, Update update) {
		this.mongoTemplateReport.updateFirst(query, update, this.getEntityClass());
	}

	public T findOneById(String id) {
		return this.mongoTemplateReport.findById(id, this.getEntityClass());
	}

	public T findAndModify(Query query, Update update) {
		return this.mongoTemplateReport.findAndModify(query, update,
				this.getEntityClass());
	}

	public T findAndRemove(Query query) {
		return this.mongoTemplateReport.findAndRemove(query, this.getEntityClass());
	}

	public T findByIdAndCollectionName(String id, String collectionName) {
		return this.mongoTemplateReport.findById(id, this.getEntityClass(),
				collectionName);
	}

	public T findOneByQuery(Query query) {
		// TODO Auto-generated method stub
		return this.mongoTemplateReport.findOne(query, this.getEntityClass());
	}

	public void updateAllByQuery(Query query, Update update) {
		this.mongoTemplateReport.updateMulti(query, update, this.getEntityClass());

	}
	
	public Integer findCountByQuery(Query query) {
		Long totalCount = this.mongoTemplateReport
				.count(query, this.getEntityClass());
		return Integer.parseInt(String.valueOf(totalCount));
	}
	protected abstract Class<T> getEntityClass();
	
	

}
