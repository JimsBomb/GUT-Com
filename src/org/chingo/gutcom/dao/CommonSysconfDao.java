package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.CommonSysconf;

public interface CommonSysconfDao
{
	public void save(CommonSysconf conf);
	public void update(CommonSysconf conf);
	public void delete(CommonSysconf conf);
	public void delete(Serializable id);
	public CommonSysconf get(Serializable id);
	public List<CommonSysconf> findAll();
}
