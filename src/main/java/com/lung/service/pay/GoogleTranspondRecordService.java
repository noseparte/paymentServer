package com.lung.service.pay;

import com.lung.dao.pay.GoogleTranspondRecordDao;
import com.lung.model.GoogleTranspondRecord;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GoogleTranspondRecordService {

    @Autowired
    private GoogleTranspondRecordDao googleTranspondRecordDao;

    public void saveRecord(GoogleTranspondRecord record) {
        googleTranspondRecordDao.saveRecord(record);
    }

    public GoogleTranspondRecord findLasted() {
        return googleTranspondRecordDao.findLasted();
    }
}
