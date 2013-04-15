package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.CommonSyslog;

public interface CommonSyslogDao
{
	public void save(CommonSyslog conf);
	public void update(CommonSyslog conf);
	public void delete(CommonSyslog conf);
	public void delete(Serializable id);
	public CommonSyslog get(Serializable id);
	public List<CommonSyslog> findAll();
}
