package org.chingo.gutcom.action.base.api;

import org.chingo.gutcom.service.UserManager;

public class UserBaseAction extends BaseApiAction
{
	protected UserManager userMgr;
	
	public void setUserMgr(UserManager userMgr)
	{
		this.userMgr = userMgr;
	}
}
