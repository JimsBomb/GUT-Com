package org.chingo.gutcom.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.action.base.SystemBaseAction;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.exception.GcException;

import com.opensymphony.xwork2.ActionContext;

public class SyslogAction extends SystemBaseAction
{
	private List<CommonSyslog> lstLog;
	private long logTotalSize = 0;
	private int logPage = 1;
	private int searchMode = 0;
	private String username;
	private String startTime;
	private String endTime;
	private byte type = -1;
	
	public List<CommonSyslog> getLstLog()
	{
		return this.lstLog;
	}
	
	public long getLogTotalSize()
	{
		return this.logTotalSize;
	}
	
	public int getLogPage()
	{
		return this.logPage;
	}
	
	public int getSearchMode()
	{
		return this.searchMode;
	}
	
	public void setSearchMode(int searchMode)
	{
		this.searchMode = searchMode;
	}
	
	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public byte getType()
	{
		return type;
	}

	public void setType(byte type)
	{
		this.type = type;
	}

	public String mgr() throws Exception
	{
//		if(parameters.containsKey("searchMode"))
//		{
//			request.setAttribute("searchMode", ((String[])parameters.get("searchMode"))[0]);
//		}
//		else
//		{
//			request.setAttribute("searchMode", 0);
//		}
		Map<String, Object> values = new HashMap<String, Object>();
		if(searchMode == 1)
		{
//			if(parameters.containsKey("username"))
//			{
//				username = ((String[])parameters.get("username"))[0];
				if(username!=null && username.isEmpty()==false)
				{
					values.put("username", "%" + username + "%");
				}
//			}
//			if(parameters.containsKey("startTime"))
//			{
//				startTime = ((String[])parameters.get("startTime"))[0];
				if(startTime!=null && startTime.isEmpty()==false)
				{
					long from;
					try
					{
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						from = sdf.parse(startTime).getTime();
						
					}
					catch(Exception ex)
					{
						throw new GcException("日期格式不正确。");
					}
					values.put("startTime", from);
				}
//			}
//			if(parameters.containsKey("endTime"))
//			{
//				endTime = ((String[])parameters.get("endTime"))[0];
				if(endTime!=null && endTime.isEmpty()==false)
				{
					long to;
					try
					{
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						to = sdf.parse(endTime).getTime() + 24 * 3600 * 1000;
						
					}
					catch(Exception ex)
					{
						throw new GcException("日期格式不正确。");
					}
					values.put("endTime", to);
				}
//			}
//			if(parameters.containsKey("type"))
//			{
				if(type >= 0)
				{
					values.put("type", type);
				}
//			}
		}
		if (parameters.containsKey("p"))
		{
			String p = ((String[])parameters.get("p"))[0];
			try
			{
				logPage = Integer.parseInt(p);
			}
			catch (Exception ex)
			{
				throw new GcException("Invalid parameter : " + p);
			}
		}
		List rst = sysMgr.findSyslogByPage(values, (logPage-1)*20, 20);
		lstLog = (List<CommonSyslog>) rst.get(0);
		logTotalSize = (long) rst.get(1);
//		logTotalSize = sysMgr.getSyslogTotalSize();
		if(logTotalSize == 0)
		{
			logPage = 0;
		}
		return "mgr";
	}
	
	public String del() throws Exception
	{
		Object[] params;
		Long[] ids;
		try
		{
			if(parameters.containsKey("id"))
			{
				params = (Object[]) parameters.get("id");
			}
			else if(parameters.containsKey("checkbox"))
			{
				params = (Object[]) parameters.get("checkbox");
				
			}
			else
			{
				throw new Exception();
			}
			ids = new Long[params.length];
			for(int i=0; i<params.length; i++)
			{
				ids[i] = Long.parseLong(params[i].toString());
			}
		}
		catch(Exception ex)
		{
			throw new GcException("Invalid parameter");
		}
		sysMgr.delSyslog(ids);
		
		return "del";
	}
}
