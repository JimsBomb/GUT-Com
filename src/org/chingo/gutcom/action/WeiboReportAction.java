package org.chingo.gutcom.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.action.base.WeiboReportBaseAction;
import org.chingo.gutcom.common.constant.ErrorMsg;
import org.chingo.gutcom.common.constant.ResultMsg;
import org.chingo.gutcom.common.constant.SysconfConst;
import org.chingo.gutcom.common.constant.WeiboConst;
import org.chingo.gutcom.domain.WeiboContent;
import org.chingo.gutcom.domain.WeiboReport;
import org.chingo.gutcom.exception.GcException;

public class WeiboReportAction extends WeiboReportBaseAction
{
	private List<WeiboReport> lstReport; // 微博举报对象列表
	private WeiboReport report; // 微博举报对象
	private int searchMode = 0;
	private byte status = -1; // 微博举报状态
	
	private long totalSize = 0; // 记录总数
	private int pageCount = 1; // 当前页码
	private int pageSize; // 总页数
	
	private String resultMsg; // 操作结果信息
	private String backTo; // 返回页面
	
	public List<WeiboReport> getLstReport()
	{
		return lstReport;
	}
	public void setLstReport(List<WeiboReport> lstReport)
	{
		this.lstReport = lstReport;
	}
	public WeiboReport getReport()
	{
		return report;
	}
	public void setReport(WeiboReport report)
	{
		this.report = report;
	}
	public int getSearchMode()
	{
		return this.searchMode;
	}
	public void setSearchMode(int searchMode)
	{
		this.searchMode = searchMode;
	}
	public byte getStatus()
	{
		return this.status;
	}
	public void setStatus(byte status)
	{
		this.status = status;
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
	 * 微博举报管理Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String mgr() throws Exception
	{// 存放请求参数
		Map<String, Object> values = new HashMap<String, Object>();
		if(searchMode == 1) // 搜索模式时
		{
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
		List rst = weiboMgr.findWeiboReportByPage(values, (pageCount-1)*sizePerPage, sizePerPage);
		lstReport = (List<WeiboReport>) rst.get(0); // 获取结果集
		totalSize = (long) rst.get(1); // 获取结果总数
		if(totalSize == 0)
		{
			pageCount = 0;
		}
		setPageSize(); // 计算总页数

		return "mgr";
	}
	
	/**
	 * 删除Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String del() throws Exception
	{
		Object[] params; // 请求参数
		Integer[] ids; // 微博举报ID
		try
		{
			if(parameters.containsKey("id")) // 删除单条微博举报时
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
		weiboMgr.delWeiboReport(ids, null); // 执行删除
		
		this.resultMsg = ResultMsg.WEIBO_REPORT_DEL; // 设置操作结果信息
		this.backTo = "wbReportmgr.do"; // 设置返回页面
		
		return SUCCESS;
	}
	
	/**
	 * 查询微博举报信息Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String show() throws Exception
	{
		Integer id;
		try
		{
			String tmp = ((String[]) parameters.get("id"))[0];
			id = Integer.parseInt(tmp);
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		report = weiboMgr.getWeiboReport(id); // 执行查询
		
		return "show";
	}
	
	/**
	 * 更新状态Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String updateStatus() throws Exception
	{
		Object[] params; // 请求参数
		Integer[] ids; // 微博举报ID
		try
		{
			if(parameters.containsKey("id")) // 更新单条微博举报时
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
		weiboMgr.updateWeiboReportStatus(ids, null); // 执行更新
		
		this.resultMsg = ResultMsg.WEIBO_REPORT_STATUS_UPDATE; // 设置操作结果信息
		this.backTo = "wbReportmgr.do"; // 设置返回页面
		
		return SUCCESS;
	}
	
	/**
	 * 处理微博举报Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String deal() throws Exception
	{
		Integer id;
		try
		{
			String tmp = ((String[]) parameters.get("id"))[0];
			id = Integer.parseInt(tmp);
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		weiboMgr.updateWeiboReportDeal(id, null); // 执行查询
		
		this.resultMsg = ResultMsg.WEIBO_REPORT_DEAL;
		this.backTo = "wbReportshow.do?id=" + id; // 返回微博举报详情页面
		
		return SUCCESS;
	}
}
