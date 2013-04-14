package org.chingo.gutcom.service.impl;

import java.io.Serializable;

import org.chingo.gutcom.dao.TestDao;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.service.TestService;

public class TestServiceImpl implements TestService
{
	private TestDao td;
	
	public void setTd(TestDao td)
	{
		this.td = td;
	}

	@Override
	public void addLog(CommonSyslog log)
	{
		// TODO Auto-generated method stub
		log.setCommonUser((CommonUser) td.getOne(CommonUser.class));
		td.save(log);
	}

	@Override
	public CommonSyslog getLog(Serializable id)
	{
		// TODO Auto-generated method stub
		return (CommonSyslog) td.get(CommonSyslog.class, id);
	}

	@Override
	public void deleteUser(CommonUser u)
	{
		// TODO Auto-generated method stub
		u = (CommonUser) td.getOne(CommonUser.class);
		td.delete(u);
	}

	@Override
	public CommonUser getOneUser()
	{
		// TODO Auto-generated method stub
		return (CommonUser) td.getOne(CommonUser.class);
	}

	@Override
	public void addUsers()
	{
		// TODO Auto-generated method stub
		CommonUser u = new CommonUser();
		u.setNickname("tnt");
		u.setEmail("tnt@tnt.com");
		u.setPassword("098F6BCD4621D373CADE4E832627B4F6");
		u.setStudentnum("");
		td.save(u);
		CommonUser uu = new CommonUser();
		u.setNickname("tnt");
		u.setEmail("tnt@tnt.com");
		u.setPassword("098F6BCD4621D373CADE4E832627B4F6");
		u.setStudentnum("");
		td.save(uu);
	}

}
