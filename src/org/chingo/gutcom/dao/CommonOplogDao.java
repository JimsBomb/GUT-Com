package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.CommonOplog;

public interface CommonOplogDao
{
	public void save(CommonOplog conf);
	public void update(CommonOplog conf);
	public void delete(CommonOplog conf);
	public void delete(Serializable id);
	public CommonOplog get(Serializable id);
	public List<CommonOplog> findAll();
}
