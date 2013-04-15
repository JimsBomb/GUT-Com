package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.ShareCategory;

public interface ShareCategoryDao
{
	public void save(ShareCategory conf);
	public void update(ShareCategory conf);
	public void delete(ShareCategory conf);
	public void delete(Serializable id);
	public ShareCategory get(Serializable id);
	public List<ShareCategory> findAll();
}
