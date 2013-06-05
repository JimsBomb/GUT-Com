package org.chingo.gutcom.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.action.base.WeiboBaseAction;
import org.chingo.gutcom.common.constant.ErrorMsg;
import org.chingo.gutcom.common.constant.ResultMsg;
import org.chingo.gutcom.common.constant.SysconfConst;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.util.FormatUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.WeiboTopic;
import org.chingo.gutcom.exception.GcException;

public class TopicAction extends WeiboBaseAction
{
	private List<WeiboTopic> lstTopic; // 存放话题列表
	private int searchMode = 0; // 搜索模式，0-否，1-是
	private byte status = -1; // 话题状态
	private byte newStatus; // 新话题状态
	private String title; // 话题标题
	private String sort; // 排序
	private String startTime; // 起始时间时间
	private String endTime; // 截止时间
	
	private int pageCount = 1; // 当前页数
	private String prevP; // 上一页起始行的rowKey
	private String nextP; // 下一页起始行的rowKey
	
	private String resultMsg; // 操作结果信息
	private String backTo; // 返回页面
	
	public List<WeiboTopic> getLstTopic()
	{
		return lstTopic;
	}

	public void setLstTopic(List<WeiboTopic> lstTopic)
	{
		this.lstTopic = lstTopic;
	}

	public int getSearchMode()
	{
		return searchMode;
	}

	public void setSearchMode(int searchMode)
	{
		this.searchMode = searchMode;
	}

	public byte getStatus()
	{
		return status;
	}

	public void setStatus(byte status)
	{
		this.status = status;
	}
	
	public byte getNewStatus()
	{
		return this.newStatus;
	}
	
	public void setNewStatus(byte newStatus)
	{
		this.newStatus = newStatus;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getSort()
	{
		return sort;
	}

	public void setSort(String sort)
	{
		this.sort = sort;
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

	public int getPageCount()
	{
		return pageCount;
	}
	
	public void setPageCount(int pageCount)
	{
		this.pageCount = pageCount;
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
	 * 话题管理Method
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
			/* 排序非空则获取 */
			if(sort!=null && sort.isEmpty()==false)
			{
				values.put("sort", sort);
			}
			/* 话题非空则获取 */
			if(title!=null && title.isEmpty()==false)
			{
				values.put("title", title);
			}
			/* 起始时间非空则获取 */
			if(startTime!=null && startTime.isEmpty()==false)
			{
				long from;
				try
				{
					from = FormatUtil.getTimeStampByDate(startTime); // 转换为时间戳
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
					to = FormatUtil.getTimeStampByDate(endTime); // 转换为时间戳
				}
				catch(Exception ex)
				{
					throw new GcException(ErrorMsg.INVALID_PARAM);
				}
				values.put("endTime", to);
			}
			/* 状态值>=0（即有选择）时，存放参数 */
			if(status >= 0)
			{
				values.put("status", status);
			}
		}
		/* 处理分页 */
		if (parameters.containsKey("p")) // 分页时获取下一页起始行
		{
			p = ((String[])parameters.get("p"))[0];
		}
		int sizePerPage = Integer.parseInt(getConfigurations().get(SysconfConst.RECORDS_PER_PAGE));
		List<Object> rst = weiboMgr.findTopicByPage(values, p, sizePerPage);
		if(rst != null)
		{
			lstTopic = (List<WeiboTopic>) rst.get(0); // 获取结果集
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
		if(lstTopic!=null && lstTopic.size()>0) // 当内容列表非空时
		{
			pageKey.add(lstTopic.get(0).getTitle()); // 将当前首行rowKey添加到历史列表中
		}
		session.put(SystemConst.SESSION_PAGE, pageKey); // 替换session中的旧分页历史

		return "mgr";
	}
	
	/**
	 * 删除话题Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String del() throws Exception
	{
		Object[] params; // 请求参数
		List<String> ids = new ArrayList<String>(); // 话题ID
		try
		{
			if(parameters.containsKey("id")) // 删除单条话题时
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
		log.setDetail(SyslogConst.DETAIL_ADMIN_TOPIC_DEL);
		log.setDateline(new Date().getTime());
		weiboMgr.delTopic(ids, log); // 执行删除
		
		this.resultMsg = ResultMsg.TOPIC_DEL; // 设置操作结果信息
		this.backTo = "topicmgr.do"; // 设置返回页面
		
		return SUCCESS;
	}
	
	/**
	 * 更新话题状态Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String updateStatus() throws Exception
	{
		Object[] params; // 请求参数
		List<String> ids = new ArrayList<String>(); // 微博ID
		try
		{
			if(parameters.containsKey("id")) // 更新单条话题时
			{
				params = (Object[]) parameters.get("id");
			}
			else if(parameters.containsKey("checkbox")) // 勾选复选框批量更新时
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
		log.setDetail(SyslogConst.DETAIL_ADMIN_TOPIC_STATUS_UPDATE);
		log.setDateline(new Date().getTime());
		weiboMgr.updateTopicStatus(ids, newStatus, log); // 执行更新
		
		this.resultMsg = ResultMsg.TOPIC_STATUS_UPDATE; // 设置操作结果信息
		this.backTo = "topicmgr.do"; // 设置返回页面
		
		return SUCCESS;
	}
}
