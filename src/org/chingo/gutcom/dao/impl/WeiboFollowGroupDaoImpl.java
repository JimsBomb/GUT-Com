package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BasicDao;
import org.chingo.gutcom.domain.WeiboFollowGroup;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class WeiboFollowGroupDaoImpl extends HDaoSupport implements BasicDao<WeiboFollowGroup>
{

	@Override
	public Serializable save(WeiboFollowGroup instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(WeiboFollowGroup instance)
	{
		getSession().save(instance);
		
	}

	@Override
	public void delete(WeiboFollowGroup instance)
	{
		getSession().update(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public WeiboFollowGroup get(Serializable id)
	{
		return (WeiboFollowGroup) getSession().get(WeiboFollowGroup.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WeiboFollowGroup> list()
	{
		return getSession().createQuery("from WeiboFollowGroup wfg").list();
	}

}
