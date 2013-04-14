package org.chingo.gutcom.service;

import org.chingo.gutcom.entity.CommonSyslog;
import org.chingo.gutcom.entity.CommonUser;

public interface TestService
{
	public void addLog(CommonSyslog log);
	public CommonSyslog getLog(java.io.Serializable id);
	public CommonUser getOneUser();
	public void deleteUser(CommonUser u);
	public void addUsers();
}
