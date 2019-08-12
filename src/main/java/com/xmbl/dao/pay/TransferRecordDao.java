package com.xmbl.dao.pay;

import com.xmbl.model.TransferRecord;
import com.xmbl.mongo.dao.GeneralDao;

public interface TransferRecordDao extends GeneralDao<TransferRecord>{

	boolean generatedBills(TransferRecord record);	
	
	boolean updateBill(String orderNo, String transferRemark);
}
