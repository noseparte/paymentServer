package com.xmbl.dao;

import com.xmbl.model.RechargeRecord;
import com.xmbl.mongo.dao.GeneralDao;

public interface RechargeRecordDao extends GeneralDao<RechargeRecord>{

	 void updateBill(String ordeNo,String remark);
}
