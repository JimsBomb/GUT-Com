package org.chingo.gutcom.action.base;

import org.chingo.gutcom.action.base.api.BaseApiAction;
import org.chingo.gutcom.service.AccountManager;

public class AccountBaseAction extends BaseApiAction
{
	protected AccountManager accMgr;
	
	public void setAccMgr(AccountManager accMgr)
	{
		this.accMgr = accMgr;
	}
}
