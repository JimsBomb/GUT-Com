package org.chingo.gutcom.action.base.api.weibo;

import org.chingo.gutcom.action.base.api.BaseApiAction;
import org.chingo.gutcom.service.WeiboFavManager;

public class FavouriteBaseAction extends BaseApiAction
{
	protected WeiboFavManager wbFavMgr;
	
	public void setWbFavMgr(WeiboFavManager wbFavMgr)
	{
		this.wbFavMgr = wbFavMgr;
	}
}
