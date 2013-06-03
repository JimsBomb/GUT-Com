package org.chingo.gutcom.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.bean.UserInfoBean;
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
	private Map<String, Object> jsonRst = new HashMap<String, Object>();
	private String ids;
	private Object[] ido;
	
	public void setIds(String ids)
	{
		this.ids = ids;
	}
	
	public void setIdo(Object[] ido)
	{
		this.ido = ido;
	}
	
	public void setTs(TestService ts)
	{
		this.ts = ts;
	}
	
	public Map<String, Object> getJsonRst()
	{
		return this.jsonRst;
	}
	
	public String test() throws Exception
	{
		Map<String, Object> m = new HashMap<String, Object>();
		UserInfoBean userInfo = new UserInfoBean();
		userInfo.setLatest(userInfo.new Latest());
		Object tt = null;
		m.put("a", 1);
		m.put("b", 2);
		m.put("c", tt);
		List<Object> lst = new ArrayList<Object>();
		lst.add("lst1");
		lst.add("lst2");
		lst.add("lst3");
		m.put("lst", lst);
		jsonRst.put("m", m);
		jsonRst.put("aa", 11);
		jsonRst.put("bb", 22);
		return SUCCESS;
	}
	
	public String addLog() throws Exception
	{
		CommonSyslog log = new CommonSyslog();
		log.setDetail("test log");
		log.setDateline(new Date().getTime());
		log.setIp("127.0.0.1");
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
