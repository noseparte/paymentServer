package com.xmbl.dao.impl.pay;

import org.springframework.stereotype.Repository;

import com.xmbl.dao.pay.ThirdPayDao;
import com.xmbl.model.ThirdPayBean;
import com.xmbl.mongo.dao.GeneralPaymentDaoImpl;

@Repository
public class ThirdPayDaoImpl extends GeneralPaymentDaoImpl<ThirdPayBean> implements ThirdPayDao{

	@Override
	protected Class<ThirdPayBean> getEntityClass() {
		return ThirdPayBean.class;
	}

}
