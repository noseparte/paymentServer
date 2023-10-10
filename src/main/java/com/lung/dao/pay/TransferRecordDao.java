package com.lung.dao.pay;

import com.lung.model.TransferRecord;
import com.lung.mongo.dao.GeneralDao;

public interface TransferRecordDao extends GeneralDao<TransferRecord>{

	boolean generatedBills(TransferRecord record);	
	
	boolean updateBill(String orderNo, String transferRemark);
}
