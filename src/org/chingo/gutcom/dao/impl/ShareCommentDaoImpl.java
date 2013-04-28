package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.ShareComment;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class ShareCommentDaoImpl extends HDaoSupport implements BaseDao<ShareComment>
{

	@Override
	public Serializable save(ShareComment instance)
	{
		return getSession().save(instance);
	}

	@Override
	public void update(ShareComment instance)
	{
		getSession().update(instance);
		
	}

	@Override
	public void delete(ShareComment instance)
	{
		getSession().delete(instance);
	}

	@Override
	public void delete(Serializable id)
	{
		getSession().delete(get(id));
	}

	@Override
	public ShareComment get(Serializable id)
	{
		return (ShareComment) getSession().get(ShareComment.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShareComment> list()
	{
		return getSession().createQuery("from ShareComment sc").list();
	}

}
