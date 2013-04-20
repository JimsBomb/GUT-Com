package org.chingo.gutcom.action;

import org.chingo.gutcom.action.base.SystemBaseAction;

public class SysconfAction extends SystemBaseAction
{
	private String serverStatus;
	private String userVerify;
	private String weiboVerify;
	private String shareVerify;
	private String shareCommentVerify;
	private String recordsPerPage;
	private String logLifecycle;

	public String getServerStatus()
	{
		return serverStatus;
	}

	public void setServerStatus(String serverStatus)
	{
		this.serverStatus = serverStatus;
	}

	public String getUserVerify()
	{
		return userVerify;
	}

	public void setUserVerify(String userVerify)
	{
		this.userVerify = userVerify;
	}

	public String getWeiboVerify()
	{
		return weiboVerify;
	}

	public void setWeiboVerify(String weiboVerify)
	{
		this.weiboVerify = weiboVerify;
	}

	public String getShareVerify()
	{
		return shareVerify;
	}

	public void setShareVerify(String shareVerify)
	{
		this.shareVerify = shareVerify;
	}

	public String getShareCommentVerify()
	{
		return shareCommentVerify;
	}

	public void setShareCommentVerify(String shareCommentVerify)
	{
		this.shareCommentVerify = shareCommentVerify;
	}

	public String getRecordsPerPage()
	{
		return recordsPerPage;
	}

	public void setRecordsPerPage(String recordsPerPage)
	{
		this.recordsPerPage = recordsPerPage;
	}

	public String getLogLifecycle()
	{
		return logLifecycle;
	}

	public void setLogLifecycle(String logLifecycle)
	{
		this.logLifecycle = logLifecycle;
	}
	
	public String mgr() throws Exception
	{
		return "mgr";
	}
}
