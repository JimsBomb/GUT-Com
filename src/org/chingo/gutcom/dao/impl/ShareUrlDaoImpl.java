package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.ShareUrl;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class ShareUrlDaoImpl extends HDaoSupport implements BaseDao<ShareUrl>
{

	@Override
	public Serializable save(ShareUrl instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(ShareUrl instance)
	{
		getSession().update(instance);
		
	}

	@Override
	public void delete(ShareUrl instance)
	{
		getSession().delete(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public ShareUrl get(Serializable id)
	{
		return (ShareUrl) getSession().get(ShareUrl.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShareUrl> list()
	{
		return getSession().createQuery("from ShareUrl su").list();
	}

}
