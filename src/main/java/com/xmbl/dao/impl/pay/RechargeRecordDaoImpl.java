package com.xmbl.dao.impl.pay;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.xmbl.constant.CommonConstant;
import com.xmbl.dao.pay.RechargeRecordDao;
import com.xmbl.model.RechargeRecord;
import com.xmbl.mongo.dao.GeneralPaymentDaoImpl;

@Repository
public class RechargeRecordDaoImpl extends GeneralPaymentDaoImpl<RechargeRecord> implements RechargeRecordDao{

	@Override
	protected Class<RechargeRecord> getEntityClass() {
		return RechargeRecord.class;
	}

	@Override
	public void updateBill(String transaction_id, String ordeNo, String remark) {
		Query query = new Query();
		if(StringUtils.isNotBlank(ordeNo) && !StringUtils.trim(ordeNo).equals("")) {
			query.addCriteria(Criteria.where("orderNo").is(ordeNo));
		}
		Update update = new Update();
		update.set("orderState", CommonConstant.Normal);
		update.set("orderAccountingTime", new Date());
		update.set("remark", remark);
		update.set("originalOrderId", transaction_id);
		this.getMongoTemplate().updateFirst(query, update, RechargeRecord.class, "rechargeRecord");
	}

	@Override
	public List<RechargeRecord> findAppleUnAccountingOrder() {
		Query query = new Query();
		query.addCriteria(Criteria.where("orderState").is(CommonConstant.Unusual));
		query.addCriteria(Criteria.where("orderType").is(CommonConstant.APPLE_WAYTD));
		return this.getMongoTemplate().find(query, RechargeRecord.class);
	}

	@Override
	public List<RechargeRecord> findGoogleUnAccountingOrder() {
		Query query = new Query();
		query.addCriteria(Criteria.where("orderState").is(CommonConstant.Unusual));
		query.addCriteria(Criteria.where("orderType").is(CommonConstant.GOOGLE_WAYTD));
		return this.getMongoTemplate().find(query, RechargeRecord.class);
	}

	@Override
	public boolean findRepeatOrderByOriginalOrderId(String originalOrderId, int orderType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("orderType").is(orderType));
		query.addCriteria(Criteria.where("originalOrderId").is(originalOrderId));
		List<RechargeRecord> recordList = this.getMongoTemplate().find(query, RechargeRecord.class);
		if(recordList == null || recordList.isEmpty()) {
			return true;
		}
		return false;
	}
	
}
