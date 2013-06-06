package org.chingo.gutcom.action.base.api.weibo;

import org.chingo.gutcom.action.base.api.BaseApiAction;
import org.chingo.gutcom.service.UserRelationManager;

public class RelationBaseAction extends BaseApiAction
{
	protected UserRelationManager urMgr;
	
	public void setUrMgr(UserRelationManager urMgr)
	{
		this.urMgr = urMgr;
	}
}
