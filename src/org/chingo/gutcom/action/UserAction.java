package org.chingo.gutcom.action;

import java.util.List;

import org.chingo.gutcom.action.base.UserBaseAction;
import org.chingo.gutcom.domain.CommonUser;

import com.opensymphony.xwork2.ActionContext;

public class UserAction extends UserBaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8261416438329867574L;
	
	private List<CommonUser> users;
	
	public List<CommonUser> getUsers()
	{
		return this.users;
	}
	
	public void setUsers(List<CommonUser> users)
	{
		this.users = users;
	}
	
	public String mgr() throws Exception
	{
		users = userMgr.findAllUser();
		return "mgr";
	}
	
	public String del() throws Exception
	{
		ActionContext ac = ActionContext.getContext();
		String[] cb = (String[]) ac.getParameters().get("checkbox");
		for(String s : cb)
		{
			System.out.println(s);
		}
		return "mgr";
	}
}
