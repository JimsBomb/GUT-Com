package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class CommonUserDaoImpl extends HDaoSupport implements BaseDao<CommonUser>
{

	@Override
	public Serializable save(CommonUser instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(CommonUser instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(CommonUser instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public CommonUser get(Serializable id)
	{
		return (CommonUser) getSession().get(CommonUser.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommonUser> list()
	{
		return getSession().createQuery("from CommonUser u").list();
	}

}
