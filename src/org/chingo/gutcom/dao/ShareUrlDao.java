package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.ShareUrl;

public interface ShareUrlDao
{
	public void save(ShareUrl conf);
	public void update(ShareUrl conf);
	public void delete(ShareUrl conf);
	public void delete(Serializable id);
	public ShareUrl get(Serializable id);
	public List<ShareUrl> findAll();
}
