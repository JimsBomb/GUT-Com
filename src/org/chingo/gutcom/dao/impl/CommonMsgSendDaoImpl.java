package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonMsgSend;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class CommonMsgSendDaoImpl extends HDaoSupport implements BaseDao<CommonMsgSend>
{

	@Override
	public Serializable save(CommonMsgSend instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(CommonMsgSend instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(CommonMsgSend instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public CommonMsgSend get(Serializable id)
	{
		return (CommonMsgSend) getSession().get(CommonMsgSend.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommonMsgSend> list()
	{
		return getSession().createQuery("from CommonMsgSend msg").list();
	}

}
