package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.WeiboTopic;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class WeiboTopicDaoImpl extends HDaoSupport implements BaseDao<WeiboTopic>
{

	@Override
	public Serializable save(WeiboTopic instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(WeiboTopic instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(WeiboTopic instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public WeiboTopic get(Serializable id)
	{
		return (WeiboTopic) getSession().get(WeiboTopic.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WeiboTopic> list()
	{
		return getSession().createQuery("from WeiboTopic wt").list();
	}

}
