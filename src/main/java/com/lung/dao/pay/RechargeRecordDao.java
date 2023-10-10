package com.lung.dao.pay;

import java.util.List;

import com.lung.model.RechargeRecord;
import com.lung.mongo.dao.GeneralDao;

public interface RechargeRecordDao extends GeneralDao<RechargeRecord>{

	 void updateBill(String transaction_id, String ordeNo,String remark);
	 
	 List<RechargeRecord> findAppleUnAccountingOrder();
	 
	 List<RechargeRecord> findGoogleUnAccountingOrder();
	 
	 boolean findRepeatOrderByOriginalOrderId(String originalOrderId, int orderType);
}
