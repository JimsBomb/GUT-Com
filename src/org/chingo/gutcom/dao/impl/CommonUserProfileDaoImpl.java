package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonUserProfile;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class CommonUserProfileDaoImpl extends HDaoSupport implements BaseDao<CommonUserProfile>
{

	@Override
	public Serializable save(CommonUserProfile instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(CommonUserProfile instance)
	{
		getSession().update(instance);
		
	}

	@Override
	public void delete(CommonUserProfile instance)
	{
		getSession().delete(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public CommonUserProfile get(Serializable id)
	{
		return (CommonUserProfile) getSession().get(CommonUserProfile.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommonUserProfile> list()
	{
		return getSession().createQuery("from CommonUserProfile up").list();
	}

}
