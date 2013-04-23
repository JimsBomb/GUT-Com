package org.chingo.gutcom.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.action.base.SystemBaseAction;
import org.chingo.gutcom.common.constant.ErrorMsg;
import org.chingo.gutcom.common.constant.ResultMsg;
import org.chingo.gutcom.common.constant.SysconfConst;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.exception.GcException;

import com.opensymphony.xwork2.ActionContext;

/**
 * 系统日志Action
 * @author Chingo.Org
 *
 */
public class SyslogAction extends SystemBaseAction
{
	private List<CommonSyslog> lstLog; // 存放系统日志列表
	private long totalSize = 0; // 记录总数
	private int pageCount = 1; // 当前页码
	private int pageSize; // 总页数
	private int searchMode = 0; // 搜索模式：0-否，1-是
	private String username; // 用户昵称
	private String startTime; // 起始时间
	private String endTime; // 截止时间
	private byte type = -1; // 日志类型
	
	private String resultMsg; // 操作结果信息
	private String backTo; // 返回页面
	
	public List<CommonSyslog> getLstLog()
	{
		return this.lstLog;
	}
	
	public long getTotalSize()
	{
		return this.totalSize;
	}
	
	public int getPageCount()
	{
		return this.pageCount;
	}
	
	public int getPageSize()
	{
		return this.pageSize;
	}
	
	/**
	 * 计算并设置总页数
	 */
	public void setPageSize()
	{
		int sizePerPage = Integer.parseInt(getConfigurations().get(SysconfConst.RECORDS_PER_PAGE));
		this.pageSize = (int) (this.totalSize / sizePerPage);
		if(this.totalSize % sizePerPage > 0)
		{
			this.pageSize++;
		}
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

	public String getResultMsg()
	{
		return resultMsg;
	}

	public String getBackTo()
	{
		return backTo;
	}

	/**
	 * 管理Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String mgr() throws Exception
	{
		// 存放请求参数
		Map<String, Object> values = new HashMap<String, Object>();
		if(searchMode == 1) // 搜索模式时
		{
			/* 用户昵称非空则获取 */
			if(username!=null && username.isEmpty()==false)
			{
				values.put("username", "%" + username + "%");
			}
			/* 起始时间非空则获取 */
			if(startTime!=null && startTime.isEmpty()==false)
			{
				long from;
				try
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					from = sdf.parse(startTime).getTime(); // 转换为时间戳
				}
				catch(Exception ex)
				{
					throw new GcException(ErrorMsg.INVALID_PARAM);
				}
				values.put("startTime", from);
			}
			/* 截止时间非空则获取 */
			if(endTime!=null && endTime.isEmpty()==false)
			{
				long to;
				try
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					// 时间+1天，转换为时间戳
					to = sdf.parse(endTime).getTime() + 24 * 3600 * 1000;
				}
				catch(Exception ex)
				{
					throw new GcException(ErrorMsg.INVALID_PARAM);
				}
				values.put("endTime", to);
			}
			// 类型值>=0（即有选择）时，存放参数
			if(type >= 0)
			{
				values.put("type", type);
			}
		}
		/* 处理分页 */
		if (parameters.containsKey("p"))
		{
			String p = ((String[])parameters.get("p"))[0];
			try
			{
				pageCount = Integer.parseInt(p);
			}
			catch (Exception ex)
			{
				throw new GcException(ErrorMsg.INVALID_PARAM);
			}
		}
		int sizePerPage = Integer.parseInt(getConfigurations().get(SysconfConst.RECORDS_PER_PAGE));
		List rst = sysMgr.findSyslogByPage(values, (pageCount-1)*sizePerPage, sizePerPage);
		lstLog = (List<CommonSyslog>) rst.get(0); // 获取结果集
		totalSize = (long) rst.get(1); // 获取结果总数
		if(totalSize == 0)
		{
			pageCount = 0;
		}
		setPageSize(); // 计算总页数

		return "mgr";
	}
	
	/**
	 * 删除日志Method
	 * @return
	 * @throws Exception
	 */
	public String del() throws Exception
	{
		Object[] params; // 请求参数
		Long[] ids; // 日志ID
		try
		{
			if(parameters.containsKey("id")) // 删除单条日志时
			{
				params = (Object[]) parameters.get("id");
			}
			else if(parameters.containsKey("checkbox")) // 勾选复选框批量删除时
			{
				params = (Object[]) parameters.get("checkbox");
			}
			else
			{
				throw new Exception();
			}
			/* 填充ID列表 */
			ids = new Long[params.length];
			for(int i=0; i<params.length; i++)
			{
				ids[i] = Long.parseLong(params[i].toString());
			}
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		sysMgr.delSyslog(ids); // 执行删除
		
		this.resultMsg = ResultMsg.LOG_DEL; // 设置操作结果信息
		this.backTo = "syslogmgr.do"; // 设置返回页面
		
		return SUCCESS;
	}
}
