package com.xmbl.dao.impl.pay;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.xmbl.constant.CommonConstant;
import com.xmbl.dao.pay.TransferRecordDao;
import com.xmbl.model.TransferRecord;
import com.xmbl.mongo.dao.GeneralPaymentDaoImpl;

@Repository
public class TransferRecordDaoImpl extends GeneralPaymentDaoImpl<TransferRecord> implements TransferRecordDao{

	@Override
	protected Class<TransferRecord> getEntityClass() {
		return TransferRecord.class;
	}

	@Override
	public boolean generatedBills(TransferRecord record) {
		if(null != record) {
			this.getMongoTemplate().save(record,"transferRecord");
			return true;
		}
		return false;
	}

	@Override
	public boolean updateBill(String orderNo, String transferRemark) {
		if(StringUtils.isNotBlank(orderNo) && !StringUtils.trim(orderNo).equals("")) {
			Query query = new Query();
			query.addCriteria(Criteria.where("orderNo").is(Long.parseLong(orderNo)));
			Update update = new Update();
			update.set("orderState", CommonConstant.Normal);
			update.set("tansferTime", new Date());
			update.set("remark", transferRemark);
			this.getMongoTemplate().updateFirst(query, update, TransferRecord.class,"transferRecord");
			return true;
		}
		return false;
	}

}
