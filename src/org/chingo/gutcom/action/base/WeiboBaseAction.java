package org.chingo.gutcom.action.base;

import org.chingo.gutcom.service.WeiboManager;

public class WeiboBaseAction extends BaseAction
{
	protected WeiboManager weiboMgr;
	
	public void setWeiboMgr(WeiboManager weiboMgr)
	{
		this.weiboMgr = weiboMgr;
	}
}
