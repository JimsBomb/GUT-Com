package org.chingo.gutcom.action.base;

import org.chingo.gutcom.service.SystemManager;

public class SystemBaseAction extends BaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6798814193905375820L;
	protected SystemManager sysMgr;
	
	public void setSysMgr(SystemManager sysMgr)
	{
		this. sysMgr = sysMgr;
	}
}
