package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.ShareReport;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class ShareReportDaoImpl extends HDaoSupport implements BaseDao<ShareReport>
{

	@Override
	public Serializable save(ShareReport instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(ShareReport instance)
	{
		getSession().update(instance);
		
	}

	@Override
	public void delete(ShareReport instance)
	{
		getSession().delete(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public ShareReport get(Serializable id)
	{
		return (ShareReport) getSession().get(ShareReport.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShareReport> list()
	{
		return getSession().createQuery("from ShareReport sr").list();
	}

}
