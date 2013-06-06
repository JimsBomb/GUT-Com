package org.chingo.gutcom.action.base.api.weibo;

import org.chingo.gutcom.action.base.api.BaseApiAction;
import org.chingo.gutcom.service.WbCommManager;
import org.chingo.gutcom.service.WeiboManager;

public class CommentBaseAction extends BaseApiAction
{
protected WbCommManager wbCommMgr;
	
	public void setWbCommMgr(WbCommManager wbCommMgr)
	{
		this.wbCommMgr = wbCommMgr;
	}
}
