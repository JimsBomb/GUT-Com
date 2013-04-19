package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonUserStatus;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class CommonUserStatusDaoImpl extends HDaoSupport implements BaseDao<CommonUserStatus>
{

	@Override
	public Serializable save(CommonUserStatus instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(CommonUserStatus instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(CommonUserStatus instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public CommonUserStatus get(Serializable id)
	{
		return (CommonUserStatus) getSession().get(CommonUserStatus.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommonUserStatus> list()
	{
		return getSession().createQuery("from CommonUserStatus us").list();
	}

}
