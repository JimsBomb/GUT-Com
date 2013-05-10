package org.chingo.gutcom.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.service.AccountManager;

public class AccountManagerImpl implements AccountManager
{
	private BaseDao<CommonUser> userDao;
	
	public void setUserDao(BaseDao<CommonUser> userDao)
	{
		this.userDao = userDao;
	}
	
	@Override
	public CommonUser getUser(String name, String pwd)
	{
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("nickname", name); // 昵称
		values.put("password", pwd); // 密码
		String hql = "from CommonUser u where u.nickname=:nickname and u.password=:password"; // 查询语句
		List u = userDao.query(hql, values); // 查询
		return u.size()==0 ? null : (CommonUser)u.get(0);
	}

	@Override
	public void updatePwd(String oldPwd, String newPwd)
	{
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("password", oldPwd); // 密码
		String hql = "from CommonUser u where u.uid=0 and u.password=:password"; // 查询语句
		List u = userDao.query(hql, values); // 查询
		if(u.size() > 0) // 存在该用户时
		{
			((CommonUser)u.get(0)).setPassword(newPwd); // 更新密码
		}
	}
}
