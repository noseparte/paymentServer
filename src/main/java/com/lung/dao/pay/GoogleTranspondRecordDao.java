package com.lung.dao.pay;

import com.lung.model.GoogleTranspondRecord;

public interface GoogleTranspondRecordDao {

    void saveRecord(GoogleTranspondRecord record);

    GoogleTranspondRecord findLasted();
}
