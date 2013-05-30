package org.chingo.gutcom.action.base.api;

import org.chingo.gutcom.service.UserManager;

public class AccountBaseAction extends BaseApiAction
{
	protected UserManager userMgr;
	
	public void setUserMgr(UserManager userMgr)
	{
		this.userMgr = userMgr;
	}
}
