package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonSysconf;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class CommonSysconfDaoImpl extends HDaoSupport implements BaseDao<CommonSysconf>
{

	@Override
	public Serializable save(CommonSysconf instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(CommonSysconf instance)
	{
		getSession().saveOrUpdate(instance);
		
	}

	@Override
	public void delete(CommonSysconf instance)
	{
		getSession().delete(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public CommonSysconf get(Serializable id)
	{
		return (CommonSysconf) getSession().get(CommonSysconf.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommonSysconf> list()
	{
		return getSession().createQuery("from CommonSysconf conf").list();
	}

}
