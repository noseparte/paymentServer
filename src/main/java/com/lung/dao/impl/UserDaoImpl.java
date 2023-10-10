package com.lung.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.lung.dao.UserDao;
import com.lung.model.AppUser;
import com.lung.mongo.dao.GeneralLoginDaoImpl;
import com.lung.util.Md5PasswordEncoder;

@Repository
public class UserDaoImpl extends GeneralLoginDaoImpl<AppUser> implements UserDao{

	@Override
	protected Class<AppUser> getEntityClass() {
		return AppUser.class;
	}

	@Override
	public boolean verifyPassword(String AccountId, String passWord){
		try {
			Query query = new Query();
			if(StringUtils.isNotBlank(AccountId) && !StringUtils.trim(AccountId).equals("")) {
				query.addCriteria(Criteria.where("accountid").is(AccountId));
			}
			AppUser user = this.getMongoTemplate().findOne(query, AppUser.class, "app_user");
			if(user.getPassword().equals(Md5PasswordEncoder.encode(passWord))) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
