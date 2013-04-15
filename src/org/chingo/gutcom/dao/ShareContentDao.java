package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.ShareContent;

public interface ShareContentDao
{
	public void save(ShareContent conf);
	public void update(ShareContent conf);
	public void delete(ShareContent conf);
	public void delete(Serializable id);
	public ShareContent get(Serializable id);
	public List<ShareContent> findAll();
}
