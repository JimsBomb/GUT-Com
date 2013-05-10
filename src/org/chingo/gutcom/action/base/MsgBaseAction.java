package org.chingo.gutcom.action.base;

import org.chingo.gutcom.service.MsgManager;

public class MsgBaseAction extends BaseAction
{
	protected MsgManager msgMgr;
	
	public void setMsgMgr(MsgManager msgMgr)
	{
		this.msgMgr = msgMgr;
	}
}
