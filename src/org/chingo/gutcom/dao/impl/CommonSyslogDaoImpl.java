package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BasicDao;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class CommonSyslogDaoImpl extends HDaoSupport implements BasicDao<CommonSyslog>
{

	@Override
	public Serializable save(CommonSyslog instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(CommonSyslog instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(CommonSyslog instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public CommonSyslog get(Serializable id)
	{
		return (CommonSyslog) getSession().get(CommonSyslog.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommonSyslog> list()
	{
		return getSession().createQuery("from CommonSyslog log").list();
	}

}
