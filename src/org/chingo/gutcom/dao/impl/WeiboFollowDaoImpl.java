package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BasicDao;
import org.chingo.gutcom.domain.WeiboFollow;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class WeiboFollowDaoImpl extends HDaoSupport implements BasicDao<WeiboFollow>
{

	@Override
	public Serializable save(WeiboFollow instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(WeiboFollow instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(WeiboFollow instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public WeiboFollow get(Serializable id)
	{
		return (WeiboFollow) getSession().get(WeiboFollow.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WeiboFollow> list()
	{
		return getSession().createQuery("from WeiboFollow wf").list();
	}

}
