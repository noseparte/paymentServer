package com.xmbl.dao.impl.pay;

import org.springframework.stereotype.Repository;

import com.xmbl.dao.pay.BankDao;
import com.xmbl.model.Bank;
import com.xmbl.mongo.dao.GeneralPaymentDaoImpl;

@Repository
public class BankDaoImpl extends GeneralPaymentDaoImpl<Bank> implements BankDao{

	@Override
	protected Class<Bank> getEntityClass() {
		return Bank.class;
	}

}
