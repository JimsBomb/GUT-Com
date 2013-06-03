package org.chingo.gutcom.action.base.api.common;

import org.chingo.gutcom.action.base.api.BaseApiAction;
import org.chingo.gutcom.service.UserManager;

public class NoticeBaseAction extends BaseApiAction
{
	protected UserManager userMgr;
	
	public void setUserMgr(UserManager userMgr)
	{
		this.userMgr = userMgr;
	}
}
