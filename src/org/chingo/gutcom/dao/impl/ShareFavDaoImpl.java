package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.ShareFav;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class ShareFavDaoImpl extends HDaoSupport implements BaseDao<ShareFav>
{

	@Override
	public Serializable save(ShareFav instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(ShareFav instance)
	{
		getSession().update(instance);
		
	}

	@Override
	public void delete(ShareFav instance)
	{
		getSession().delete(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public ShareFav get(Serializable id)
	{
		return (ShareFav) getSession().get(ShareFav.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShareFav> list()
	{
		return getSession().createQuery("from ShareFav sf").list();
	}

}
