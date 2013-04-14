package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.HDaoSupport;
import org.chingo.gutcom.dao.IBasicHibernateDao;
import org.chingo.gutcom.dao.TestDao;
import org.chingo.gutcom.entity.CommonSyslog;

public class TestDaoImpl extends HDaoSupport implements TestDao
{

	@Override
	public void save(Object o)
	{
		// TODO Auto-generated method stub
		getSession().save(o);
	}

	@Override
	public Object get(Class c, Serializable id)
	{
		// TODO Auto-generated method stub
		return getSession().get(c, id);
	}

	@Override
	public void delete(Object o)
	{
		// TODO Auto-generated method stub
		getSession().delete(o);
	}

	@Override
	public Object getOne(Class c)
	{
		// TODO Auto-generated method stub
		return getSession().createQuery("from " + c.getSimpleName() + " c").setFetchSize(1).list().get(0);
	}
}
