package com.lung.dao.impl;

import org.springframework.stereotype.Repository;

import com.lung.dao.ServiceRepesitoryDao;
import com.lung.model.ServiceRepository;
import com.lung.mongo.dao.GeneralDaoImpl;

@Repository
public class ServiceRepesitoryDaoImpl extends GeneralDaoImpl<ServiceRepository> implements ServiceRepesitoryDao{

	@Override
	protected Class<ServiceRepository> getEntityClass() {
		return ServiceRepository.class;
	}

}
