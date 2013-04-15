package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.CommonSmileCategory;

public interface CommonSmileCategoryDao
{
	public void save(CommonSmileCategory conf);
	public void update(CommonSmileCategory conf);
	public void delete(CommonSmileCategory conf);
	public void delete(Serializable id);
	public CommonSmileCategory get(Serializable id);
	public List<CommonSmileCategory> findAll();
}
