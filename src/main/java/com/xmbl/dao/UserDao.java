package com.xmbl.dao;

import com.xmbl.model.AppUser;
import com.xmbl.mongo.dao.GeneralDao;

public interface UserDao extends GeneralDao<AppUser>{

	boolean verifyPassword(String AccountId, String passWord);
	
}
