package com.lung.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lung.dao.UserDao;
import com.lung.dao.impl.UserDaoImpl;

@Repository
public class AppUserService extends UserDaoImpl{

	@Autowired
	private UserDao userDao;
	
	public boolean verifyPassword(String AccountId, String passWord) {
		Boolean flag = userDao.verifyPassword(AccountId,passWord);
		if(flag) {
			return true;
		}
		return false;
	}

}
