package com.xmbl.dao.impl;

import org.springframework.stereotype.Repository;

import com.xmbl.dao.ServiceRepesitoryDao;
import com.xmbl.model.ServiceRepository;
import com.xmbl.mongo.dao.GeneralDaoImpl;

@Repository
public class ServiceRepesitoryDaoImpl extends GeneralDaoImpl<ServiceRepository> implements ServiceRepesitoryDao{

	@Override
	protected Class<ServiceRepository> getEntityClass() {
		return ServiceRepository.class;
	}

}
