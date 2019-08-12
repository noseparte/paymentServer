package com.xmbl.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xmbl.dao.UserDao;
import com.xmbl.dao.impl.UserDaoImpl;

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
