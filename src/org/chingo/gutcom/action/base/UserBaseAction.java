package org.chingo.gutcom.action.base;

import org.chingo.gutcom.service.UserManager;

public class UserBaseAction extends BaseAction
{
	protected UserManager userMgr;
	
	public void setUserMgr(UserManager userMgr)
	{
		this.userMgr = userMgr;
	}
}
