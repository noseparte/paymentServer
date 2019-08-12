package com.xmbl.dao.impl.pay;

import com.xmbl.constant.CommonConstant;
import com.xmbl.dao.pay.GoogleTranspondRecordDao;
import com.xmbl.model.GoogleTranspondRecord;
import com.xmbl.mongo.dao.GeneralPaymentDaoImpl;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


/**
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 *
 * @Author Noseparte
 * @Compile --
 * @Version 1.0
 * @Description
 */
@Repository
public class GoogleTranspondRecordDaoImpl extends GeneralPaymentDaoImpl<GoogleTranspondRecord> implements GoogleTranspondRecordDao {
    @Override
    protected Class<GoogleTranspondRecord> getEntityClass() {
        return GoogleTranspondRecord.class;
    }

    @Override
    public void saveRecord(GoogleTranspondRecord record) {
        this.getMongoTemplate().save(record);
    }

    @Override
    public GoogleTranspondRecord findLasted() {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(CommonConstant.Normal));
        query.limit(1);
        return this.getMongoTemplate().findOne(query,GoogleTranspondRecord.class,"pt_google_transpond_record");
    }
}
