package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonSmile;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class CommonSmileDaoImpl extends HDaoSupport implements BaseDao<CommonSmile>
{

	@Override
	public Serializable save(CommonSmile instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(CommonSmile instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(CommonSmile instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public CommonSmile get(Serializable id)
	{
		return (CommonSmile) getSession().get(CommonSmile.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommonSmile> list()
	{
		return getSession().createQuery("from CommonSmile s").list();
	}

}
