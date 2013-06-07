package org.chingo.gutcom.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.action.base.WeiboReportBaseAction;
import org.chingo.gutcom.common.constant.ErrorMsg;
import org.chingo.gutcom.common.constant.ResultMsg;
import org.chingo.gutcom.common.constant.SysconfConst;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.WeiboReport;
import org.chingo.gutcom.exception.GcException;

public class WeiboReportAction extends WeiboReportBaseAction
{
	private List<WeiboReport> lstReport; // 微博举报对象列表
	private WeiboReport report; // 微博举报对象
	private int searchMode = 0;
	private byte status = -1; // 微博举报状态
	private byte newStatus; // 微博举报新状态
	
	private int pageCount = 1; // 当前页数
	private String prevP; // 上一页起始行的rowKey
	private String nextP; // 下一页起始行的rowKey
	
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
	public byte getNewStatus()
	{
		return newStatus;
	}
	public void setNewStatus(byte newStatus)
	{
		this.newStatus = newStatus;
	}
	/**
	 * 微博举报管理Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String mgr() throws Exception
	{// 存放请求参数
		Map<String, Object> values = new HashMap<String, Object>();
		String p = null;
		if(searchMode == 1) // 搜索模式时
		{
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
		// 分页查询
		List<Object> rst = weiboMgr.findWeiboReportByPage(values, p, sizePerPage);
		if(rst != null)
		{
			lstReport = (List<WeiboReport>) rst.get(0); // 获取结果集
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
		if(lstReport!=null && lstReport.size()>0) // 当内容列表非空时
		{
			pageKey.add(lstReport.get(0).getRid()); // 将当前首行rowKey添加到历史列表中
		}
		session.put(SystemConst.SESSION_PAGE, pageKey); // 替换session中的旧分页历史

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
		List<String> ids = new ArrayList<String>(); // 微博举报ID
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
		log.setNickname(WebUtil.getUser(session).getNickname());
		log.setType(SyslogConst.TYPE_OP_ADMIN);
		log.setDetail(SyslogConst.DETAIL_ADMIN_WEIBO_REPORT_DEL);
		log.setDateline(new Date().getTime());
		weiboMgr.delWeiboReport(ids, log); // 执行删除
		
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
		String id;
		try
		{
			id = ((String[]) parameters.get("id"))[0];
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		report = weiboMgr.getWeiboReport(id); // 执行查询
		
		return "show";
	}
	
	/**
	 * 更新举报状态Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String updateStatus() throws Exception
	{
		Object[] params; // 请求参数
		List<String> ids = new ArrayList<String>(); // 微博举报ID
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
		log.setNickname(WebUtil.getUser(session).getNickname());
		log.setType(SyslogConst.TYPE_OP_ADMIN);
		log.setDetail(SyslogConst.DETAIL_ADMIN_WEIBO_REPORT_STATUS_UPDATE);
		log.setDateline(new Date().getTime());
		// 更新微博举报处理状态
		weiboMgr.updateWeiboReportStatus(ids, newStatus, log);
		
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
		String id;
		try
		{
			id = ((String[]) parameters.get("id"))[0];
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		/* 生成日志对象 */
		CommonSyslog log = new CommonSyslog();
		log.setIp(WebUtil.getRemoteAddr(request));
		log.setUserid(WebUtil.getUser(session).getUid());
		log.setNickname(WebUtil.getUser(session).getNickname());
		log.setType(SyslogConst.TYPE_OP_ADMIN);
		log.setDetail(SyslogConst.DETAIL_ADMIN_WEIBO_REPORT_DEAL);
		log.setDateline(new Date().getTime());
		// 删除被举报微博并更新举报处理状态
		weiboMgr.updateWeiboReportDeal(id, log);
		
		this.resultMsg = ResultMsg.WEIBO_REPORT_DEAL;
		this.backTo = "wbReportshow.do?id=" + id; // 返回微博举报详情页面
		
		return SUCCESS;
	}
}
