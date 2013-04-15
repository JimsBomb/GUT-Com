package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BasicDao;
import org.chingo.gutcom.domain.WeiboFav;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class WeiboFavDaoImpl extends HDaoSupport implements BasicDao<WeiboFav>
{

	@Override
	public Serializable save(WeiboFav instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(WeiboFav instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(WeiboFav instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public WeiboFav get(Serializable id)
	{
		return (WeiboFav) getSession().get(WeiboFav.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WeiboFav> list()
	{
		return getSession().createQuery("from WeiboFav wf").list();
	}

}
