package org.chingo.gutcom.action.base;

import org.chingo.gutcom.service.WeiboManager;

public class WeiboReportBaseAction extends BaseAction
{
	protected WeiboManager weiboMgr;
	
	public void setWeiboMgr(WeiboManager weiboMgr)
	{
		this.weiboMgr = weiboMgr;
	}
}
