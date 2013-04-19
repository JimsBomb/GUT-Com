package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonFilterWord;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class CommonFilterWordDaoImpl extends HDaoSupport implements BaseDao<CommonFilterWord>
{

	@Override
	public Serializable save(CommonFilterWord instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(CommonFilterWord instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(CommonFilterWord instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public CommonFilterWord get(Serializable id)
	{
		return (CommonFilterWord) getSession().get(CommonFilterWord.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommonFilterWord> list()
	{
		return getSession().createQuery("from CommonFilterWord word").list();
	}

}
