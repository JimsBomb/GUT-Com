package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonMsgRecv;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class CommonMsgRecvDaoImpl extends HDaoSupport implements BaseDao<CommonMsgRecv>
{

	@Override
	public Serializable save(CommonMsgRecv instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(CommonMsgRecv instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(CommonMsgRecv instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public CommonMsgRecv get(Serializable id)
	{
		return (CommonMsgRecv) getSession().get(CommonMsgRecv.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommonMsgRecv> list()
	{
		return getSession().createQuery("from CommonMsgRecv msg").list();
	}

}
