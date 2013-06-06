package org.chingo.gutcom.action.base.api.weibo;

import org.chingo.gutcom.action.base.api.BaseApiAction;
import org.chingo.gutcom.service.WeiboManager;

public class ReportBaseAction extends BaseApiAction
{
	protected WeiboManager weiboMgr;
	
	public void setWeiboMgr(WeiboManager weiboMgr)
	{
		this.weiboMgr = weiboMgr;
	}
}
