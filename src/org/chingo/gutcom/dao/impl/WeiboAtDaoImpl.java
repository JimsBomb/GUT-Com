package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.WeiboAt;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class WeiboAtDaoImpl extends HDaoSupport implements BaseDao<WeiboAt>
{

	@Override
	public Serializable save(WeiboAt instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(WeiboAt instance)
	{
		getSession().update(instance);
		
	}

	@Override
	public void delete(WeiboAt instance)
	{
		getSession().delete(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public WeiboAt get(Serializable id)
	{
		return (WeiboAt) getSession().get(WeiboAt.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WeiboAt> list()
	{
		return getSession().createQuery("from WeiboAt wa").list();
	}

}
