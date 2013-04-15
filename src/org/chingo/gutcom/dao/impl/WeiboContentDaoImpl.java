package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BasicDao;
import org.chingo.gutcom.domain.WeiboContent;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class WeiboContentDaoImpl extends HDaoSupport implements BasicDao<WeiboContent>
{

	@Override
	public Serializable save(WeiboContent instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(WeiboContent instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(WeiboContent instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public WeiboContent get(Serializable id)
	{
		return (WeiboContent) getSession().get(WeiboContent.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WeiboContent> list()
	{
		return getSession().createQuery("from WeiboContent wc").list();
	}

}
