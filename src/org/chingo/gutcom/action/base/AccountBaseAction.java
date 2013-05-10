package org.chingo.gutcom.action.base;

import org.chingo.gutcom.service.AccountManager;

public class AccountBaseAction extends BaseAction
{
	protected AccountManager accMgr;
	
	public void setAccMgr(AccountManager accMgr)
	{
		this.accMgr = accMgr;
	}
}
