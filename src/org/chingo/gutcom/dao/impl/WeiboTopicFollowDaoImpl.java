package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BasicDao;
import org.chingo.gutcom.domain.WeiboTopicFollow;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class WeiboTopicFollowDaoImpl extends HDaoSupport implements BasicDao<WeiboTopicFollow>
{

	@Override
	public Serializable save(WeiboTopicFollow instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(WeiboTopicFollow instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(WeiboTopicFollow instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public WeiboTopicFollow get(Serializable id)
	{
		return (WeiboTopicFollow) getSession().get(WeiboTopicFollow.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WeiboTopicFollow> list()
	{
		return getSession().createQuery("from WeiboTopicFollow wtf").list();
	}

}
