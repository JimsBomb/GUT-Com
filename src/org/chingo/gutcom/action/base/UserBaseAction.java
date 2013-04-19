package org.chingo.gutcom.action.base;

import org.chingo.gutcom.service.UserManager;

public class UserBaseAction extends BaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5855744978221649885L;
	protected UserManager userMgr;
	
	public void setUserMgr(UserManager userMgr)
	{
		this.userMgr = userMgr;
	}
}
