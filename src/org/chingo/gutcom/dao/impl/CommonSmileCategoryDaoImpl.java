package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BasicDao;
import org.chingo.gutcom.domain.CommonSmileCategory;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class CommonSmileCategoryDaoImpl extends HDaoSupport implements BasicDao<CommonSmileCategory>
{

	@Override
	public Serializable save(CommonSmileCategory instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(CommonSmileCategory instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(CommonSmileCategory instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public CommonSmileCategory get(Serializable id)
	{
		return (CommonSmileCategory) getSession().get(CommonSmileCategory.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommonSmileCategory> list()
	{
		return getSession().createQuery("from CommonSmileCategory sc").list();
	}

}
