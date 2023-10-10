package com.lung.dao.impl.pay;

import org.springframework.stereotype.Repository;

import com.lung.dao.pay.ThirdPayDao;
import com.lung.model.ThirdPayBean;
import com.lung.mongo.dao.GeneralPaymentDaoImpl;

@Repository
public class ThirdPayDaoImpl extends GeneralPaymentDaoImpl<ThirdPayBean> implements ThirdPayDao{

	@Override
	protected Class<ThirdPayBean> getEntityClass() {
		return ThirdPayBean.class;
	}

}
