package org.chingo.gutcom.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.action.base.SystemBaseAction;
import org.chingo.gutcom.domain.CommonSysconf;

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
	
	public String update() throws Exception
	{
		if(recordsPerPage.isEmpty())
		{
			recordsPerPage = "10";
		}
		if(logLifecycle.isEmpty())
		{
			logLifecycle = "0";
		}
		
		Map<String, String> mapConf = new HashMap<String, String>();
		mapConf.put("SERVER_STATUS", serverStatus);
		mapConf.put("USER_VERIFY", userVerify);
		mapConf.put("WEIBO_VERIFY", weiboVerify);
		mapConf.put("SHARE_VERIFY", shareVerify);
		mapConf.put("SHARE_COMMENT_VERIFY", shareCommentVerify);
		mapConf.put("RECORDS_PER_PAGE", recordsPerPage);
		mapConf.put("LOG_LIFECYCLE", logLifecycle);
		
		sysMgr.updateConf(mapConf);
		
		application.put("sysconf", mapConf);
		
		return "update";
	}
}
