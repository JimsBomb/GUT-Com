package org.chingo.gutcom.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.action.base.WeiboBaseAction;
import org.chingo.gutcom.common.constant.ErrorMsg;
import org.chingo.gutcom.common.constant.ResultMsg;
import org.chingo.gutcom.common.constant.SysconfConst;
import org.chingo.gutcom.common.constant.WeiboConst;
import org.chingo.gutcom.common.util.FormatUtil;
import org.chingo.gutcom.domain.WeiboContent;
import org.chingo.gutcom.domain.WeiboTopic;
import org.chingo.gutcom.exception.GcException;

public class TopicAction extends WeiboBaseAction
{
	private List<WeiboTopic> lstTopic; // 存放话题列表
	private int searchMode = 0; // 搜索模式，0-否，1-是
	private byte status = -1; // 话题状态
	private String title; // 话题标题
	private String sort; // 排序
	private String startTime; // 起始时间时间
	private String endTime; // 截止时间
	
	private long totalSize = 0; // 记录总数
	private int pageCount = 1; // 当前页码
	private int pageSize; // 总页数
	
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

	public long getTotalSize()
	{
		return totalSize;
	}

	public int getPageCount()
	{
		return pageCount;
	}

	public int getPageSize()
	{
		return pageSize;
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

	public String getResultMsg()
	{
		return resultMsg;
	}

	public String getBackTo()
	{
		return backTo;
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
				values.put("title", "%" + title + "%");
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
		List rst = weiboMgr.findTopicByPage(values, (pageCount-1)*sizePerPage, sizePerPage);
		lstTopic = (List<WeiboTopic>) rst.get(0); // 获取结果集
		totalSize = (long) rst.get(1); // 获取结果总数
		if(totalSize == 0)
		{
			pageCount = 0;
		}
		setPageSize(); // 计算总页数

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
		Integer[] ids; // 话题ID
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
			ids = new Integer[params.length];
			for(int i=0; i<params.length; i++)
			{
				ids[i] = Integer.parseInt(params[i].toString());
			}
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		weiboMgr.delTopic(ids, null); // 执行删除
		
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
		Integer[] ids; // 微博ID
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
			ids = new Integer[params.length];
			for(int i=0; i<params.length; i++)
			{
				ids[i] = Integer.parseInt(params[i].toString());
			}
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		weiboMgr.updateTopicStatus(ids, null); // 执行更新
		
		this.resultMsg = ResultMsg.TOPIC_STATUS_UPDATE; // 设置操作结果信息
		this.backTo = "topicmgr.do"; // 设置返回页面
		
		return SUCCESS;
	}
}
