package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BasicDao;
import org.chingo.gutcom.domain.CommonOplog;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class CommonOplogDaoImpl extends HDaoSupport implements BasicDao<CommonOplog>
{

	@Override
	public Serializable save(CommonOplog instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(CommonOplog instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(CommonOplog instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public CommonOplog get(Serializable id)
	{
		return (CommonOplog) getSession().get(CommonOplog.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommonOplog> list()
	{
		return getSession().createQuery("from CommonOplog log").list();
	}

}
