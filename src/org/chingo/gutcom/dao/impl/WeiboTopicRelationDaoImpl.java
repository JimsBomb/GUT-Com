package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BasicDao;
import org.chingo.gutcom.domain.WeiboTopicRelation;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class WeiboTopicRelationDaoImpl extends HDaoSupport implements BasicDao<WeiboTopicRelation>
{

	@Override
	public Serializable save(WeiboTopicRelation instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(WeiboTopicRelation instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(WeiboTopicRelation instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public WeiboTopicRelation get(Serializable id)
	{
		return (WeiboTopicRelation) getSession().get(WeiboTopicRelation.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WeiboTopicRelation> list()
	{
		return getSession().createQuery("from WeiboTopicRelation wtr").list();
	}

}
