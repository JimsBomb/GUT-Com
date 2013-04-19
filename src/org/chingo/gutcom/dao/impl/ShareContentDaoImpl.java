package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.ShareContent;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class ShareContentDaoImpl extends HDaoSupport implements BaseDao<ShareContent>
{

	@Override
	public Serializable save(ShareContent instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(ShareContent instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(ShareContent instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public ShareContent get(Serializable id)
	{
		return (ShareContent) getSession().get(ShareContent.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShareContent> list()
	{
		return getSession().createQuery("from ShareContent sc").list();
	}

}
