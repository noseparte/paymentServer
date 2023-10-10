package com.lung.dao;

import com.lung.model.AppUser;
import com.lung.mongo.dao.GeneralDao;

public interface UserDao extends GeneralDao<AppUser>{

	boolean verifyPassword(String AccountId, String passWord);
	
}
