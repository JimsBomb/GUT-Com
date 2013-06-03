package org.chingo.gutcom.action.base.api.common;

import org.chingo.gutcom.action.base.api.BaseApiAction;
import org.chingo.gutcom.service.MsgManager;

public class MessageBaseAction extends BaseApiAction
{
	protected MsgManager msgMgr;
	
	public void setMsgMgr(MsgManager msgMgr)
	{
		this.msgMgr = msgMgr;
	}
}
