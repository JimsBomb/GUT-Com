package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.ShareStatus;

public interface ShareStatusDao
{
	public void save(ShareStatus conf);
	public void update(ShareStatus conf);
	public void delete(ShareStatus conf);
	public void delete(Serializable id);
	public ShareStatus get(Serializable id);
	public List<ShareStatus> findAll();
}
