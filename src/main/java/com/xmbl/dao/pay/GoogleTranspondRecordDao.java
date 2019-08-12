package com.xmbl.dao.pay;

import com.xmbl.model.GoogleTranspondRecord;

public interface GoogleTranspondRecordDao {

    void saveRecord(GoogleTranspondRecord record);

    GoogleTranspondRecord findLasted();
}
