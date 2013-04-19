package org.chingo.gutcom.service.impl;

import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.service.UserManager;

public class UserManagerImpl implements UserManager
{
	private BaseDao<CommonUser> userDao;
	
	public void setUserDao(BaseDao<CommonUser> userDao)
	{
		this.userDao = userDao;
	}

	@Override
	public List<CommonUser> findAllUser()
	{
		// TODO Auto-generated method stub
		return userDao.list();
	}

}
