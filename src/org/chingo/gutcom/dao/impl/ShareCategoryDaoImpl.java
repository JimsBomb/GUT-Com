package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.ShareCategory;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class ShareCategoryDaoImpl extends HDaoSupport implements BaseDao<ShareCategory>
{

	@Override
	public Serializable save(ShareCategory instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(ShareCategory instance)
	{
		getSession().update(instance);
		
	}

	@Override
	public void delete(ShareCategory instance)
	{
		getSession().delete(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public ShareCategory get(Serializable id)
	{
		return (ShareCategory) getSession().get(ShareCategory.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShareCategory> list()
	{
		return getSession().createQuery("from ShareCategory sc").list();
	}

}
