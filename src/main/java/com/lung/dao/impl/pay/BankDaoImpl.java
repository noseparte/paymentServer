package com.lung.dao.impl.pay;

import org.springframework.stereotype.Repository;

import com.lung.dao.pay.BankDao;
import com.lung.model.Bank;
import com.lung.mongo.dao.GeneralPaymentDaoImpl;

@Repository
public class BankDaoImpl extends GeneralPaymentDaoImpl<Bank> implements BankDao{

	@Override
	protected Class<Bank> getEntityClass() {
		return Bank.class;
	}

}
