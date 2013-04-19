package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.ShareStatus;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class ShareStatusDaoImpl extends HDaoSupport implements BaseDao<ShareStatus>
{

	@Override
	public Serializable save(ShareStatus instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(ShareStatus instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(ShareStatus instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public ShareStatus get(Serializable id)
	{
		return (ShareStatus) getSession().get(ShareStatus.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShareStatus> list()
	{
		return getSession().createQuery("from ShareStatus ss").list();
	}

}
