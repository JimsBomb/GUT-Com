package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.ShareComment;

public interface ShareCommentDao
{
	public void save(ShareComment conf);
	public void update(ShareComment conf);
	public void delete(ShareComment conf);
	public void delete(Serializable id);
	public ShareComment get(Serializable id);
	public List<ShareComment> findAll();
}
