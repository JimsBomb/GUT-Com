package org.chingo.gutcom.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.action.base.SystemBaseAction;
import org.chingo.gutcom.common.constant.ErrorMsg;
import org.chingo.gutcom.common.constant.ResultMsg;
import org.chingo.gutcom.common.constant.SysconfConst;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.exception.GcException;

/**
 * 系统日志Action
 * @author Chingo.Org
 *
 */
public class SyslogAction extends SystemBaseAction
{
	private List<CommonSyslog> lstLog; // 存放系统日志列表
	private int pageCount = 1; // 当前页数
	private String prevP; // 上一页起始行的rowKey
	private String nextP; // 下一页起始行的rowKey
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
	
	public int getPageCount()
	{
		return this.pageCount;
	}
	
	public void setPageCount(int pageCount)
	{
		this.pageCount = pageCount;
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

	public String getPrevP()
	{
		return prevP;
	}

	public void setPrevP(String prevP)
	{
		this.prevP = prevP;
	}

	public String getNextP()
	{
		return nextP;
	}

	public void setNextP(String nextP)
	{
		this.nextP = nextP;
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
		String p = null;
		if(searchMode == 1) // 搜索模式时
		{
			/* 用户昵称非空则获取 */
			if(username!=null && username.isEmpty()==false)
			{
				values.put("username", username);
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
		if (parameters.containsKey("p")) // 分页时获取下一页起始行
		{
			p = ((String[])parameters.get("p"))[0];
		}
		int sizePerPage = Integer.parseInt(getConfigurations().get(SysconfConst.RECORDS_PER_PAGE));
		List<Object> rst = sysMgr.findSyslogByPage(values, p, sizePerPage);
		if(rst != null)
		{
			lstLog = (List<CommonSyslog>) rst.get(0); // 获取结果集
			if(rst.size() > 1) // 有下一页时
			{
				nextP = (String) rst.get(1); // 获取并设置下一页起始行的rowKey
			}
		}
		
		/* SESSION分页历史处理 */
		List<String> pageKey; // 存储分页历史的首行rowKey
		if(session.containsKey(SystemConst.SESSION_PAGE)) // session中有分页历史时获取
		{
			pageKey = (List<String>) session.get(SystemConst.SESSION_PAGE);
		}
		else // 否则新建
		{
			pageKey = new ArrayList<String>();
		}
		if(pageCount > 1) // 当前不是第一页时
		{
			prevP = pageKey.get(pageCount-2); // 设置上一页首行rowKey
		}
		else // 否则清空历史
		{
			pageKey.clear();
		}
		if(lstLog!=null && lstLog.size()>0) // 当内容列表非空时
		{
			pageKey.add(lstLog.get(0).getLid()); // 将当前首行rowKey添加到历史列表中
		}
		session.put(SystemConst.SESSION_PAGE, pageKey); // 替换session中的旧分页历史

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
		List<String> ids = new ArrayList<String>(); // 日志ID
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
			for(int i=0; i<params.length; i++)
			{
				ids.add(params[i].toString());
			}
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		/* 生成日志对象 */
		CommonSyslog log = new CommonSyslog();
		log.setIp(WebUtil.getRemoteAddr(request));
		log.setUserid(WebUtil.getUser(session).getUid());
		log.setType(SyslogConst.TYPE_OP_ADMIN);
		log.setDetail(SyslogConst.DETAIL_ADMIN_LOG_DEL);
		log.setDateline(new Date().getTime());
		sysMgr.delSyslog(ids, log); // 执行删除
		
		this.resultMsg = ResultMsg.LOG_DEL; // 设置操作结果信息
		this.backTo = "syslogmgr.do"; // 设置返回页面
		
		return SUCCESS;
	}
}
