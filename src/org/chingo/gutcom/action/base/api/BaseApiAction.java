package org.chingo.gutcom.action.base.api;

import org.chingo.gutcom.action.base.BaseAction;

public class BaseApiAction extends BaseAction
{
	protected String access_token; // 访问令牌
	
	public String getAccess_token()
	{
		return this.access_token;
	}
	
	public void setAccess_token(String access_token)
	{
		this.access_token = access_token;
	}
}
