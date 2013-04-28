package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.WeiboReport;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class WeiboReportDaoImpl extends HDaoSupport implements BaseDao<WeiboReport>
{

	@Override
	public Serializable save(WeiboReport instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(WeiboReport instance)
	{
		getSession().update(instance);
		
	}

	@Override
	public void delete(WeiboReport instance)
	{
		getSession().delete(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public WeiboReport get(Serializable id)
	{
		return (WeiboReport) getSession().get(WeiboReport.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WeiboReport> list()
	{
		return getSession().createQuery("from WeiboReport wr").list();
	}

}
