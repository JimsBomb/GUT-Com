package org.chingo.gutcom.action;

import java.util.Date;

import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.service.TestService;

import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TestService ts;
	
	public void setTs(TestService ts)
	{
		this.ts = ts;
	}
	
	public String addLog() throws Exception
	{
		CommonSyslog log = new CommonSyslog();
		log.setDetail("test log");
		log.setDateline(new Date().getTime());
		ts.addLog(log);
		return SUCCESS;
	}
	
	public String delUser() throws Exception
	{
		ts.deleteUser(new CommonUser());
		return SUCCESS;
	}
	
	public String addUsers() throws Exception
	{
		ts.addUsers();
		return SUCCESS;
	}
}
