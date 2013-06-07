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
import org.chingo.gutcom.common.constant.WeiboConst;
import org.chingo.gutcom.common.util.FormatUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.WeiboContent;
import org.chingo.gutcom.exception.GcException;

public class WeiboAction extends WeiboBaseAction
{
	private List<WeiboContent> lstWeibo; // 存放微博列表
	private int searchMode = 0; // 搜索模式，0-否，1-是
	private byte status = -1; // 微博状态
	private byte visibility = -1; // 微博可见性
	private String author; // 作者昵称
	private String content; // 微博内容
	private String topic; // 话题标题
	private String startTime; // 起始时间时间
	private String endTime; // 截止时间
	private WeiboContent weibo; // 微博对象
	
	private int pageCount = 1; // 当前页数
	private String prevP; // 上一页起始行的rowKey
	private String nextP; // 下一页起始行的rowKey
	
	private String resultMsg; // 操作结果信息
	private String backTo; // 返回页面
	
	public List<WeiboContent> getLstWeibo()
	{
		return lstWeibo;
	}
	public void setLstWeibo(List<WeiboContent> lstWeibo)
	{
		this.lstWeibo = lstWeibo;
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
	public byte getVisibility()
	{
		return visibility;
	}
	public void setVisibility(byte visibility)
	{
		this.visibility = visibility;
	}
	public String getAuthor()
	{
		return author;
	}
	public void setAuthor(String author)
	{
		this.author = author;
	}
	public String getContent()
	{
		return this.content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public String getTopic()
	{
		return this.topic;
	}
	public void setTopic(String topic)
	{
		this.topic = topic;
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
	public WeiboContent getWeibo()
	{
		return weibo;
	}
	public void setWeibo(WeiboContent weibo)
	{
		this.weibo = weibo;
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
	 * 微博管理列表Method
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
			/* 作者昵称非空则获取 */
			if(author!=null && author.isEmpty()==false)
			{
				values.put("author", author);
			}
			/* 内容非空则获取 */
			if(content!=null && content.isEmpty()==false)
			{
				values.put("content", content);
			}
			/* 话题非空则获取 */
			if(topic!=null && topic.isEmpty()==false)
			{
				values.put("topic", topic);
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
			if(visibility >= 0)
			{
				values.put("visibility", visibility);
			}
		}
		/* 处理分页 */
		if (parameters.containsKey("p")) // 分页时获取下一页起始行
		{
			p = ((String[])parameters.get("p"))[0];
		}
		int sizePerPage = Integer.parseInt(getConfigurations().get(SysconfConst.RECORDS_PER_PAGE));
		List<Object> rst = weiboMgr.findWeiboByPage(values, p, sizePerPage);
		if(rst != null)
		{
			lstWeibo = (List<WeiboContent>) rst.get(0); // 获取结果集
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
		if(lstWeibo!=null && lstWeibo.size()>0) // 当内容列表非空时
		{
			pageKey.add(lstWeibo.get(0).getWid()); // 将当前首行rowKey添加到历史列表中
		}
		session.put(SystemConst.SESSION_PAGE, pageKey); // 替换session中的旧分页历史

		return "mgr";
	}
	
	/**
	 * 删除微博Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String del() throws Exception
	{
		Object[] params; // 请求参数
		List<String> ids = new ArrayList<String>(); // 微博ID
		try
		{
			if(parameters.containsKey("id")) // 删除单条微博时
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
		log.setDetail(SyslogConst.DETAIL_ADMIN_WEIBO_DEL);
		log.setDateline(new Date().getTime());
		weiboMgr.delWeibo(ids, log); // 执行删除
		
		this.resultMsg = ResultMsg.WEIBO_DEL; // 设置操作结果信息
		this.backTo = "weibomgr.do"; // 设置返回页面
		
		return SUCCESS;
	}
	
	/**
	 * 微博审核列表Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String auditmgr() throws Exception
	{
		// 存放请求参数
		Map<String, Object> values = new HashMap<String, Object>();
		String p = null;
		values.put("status", WeiboConst.STATUS_NOT_AUDIT); // 只查询未审核的微博
		/* 处理分页 */
		if (parameters.containsKey("p")) // 分页时获取下一页起始行
		{
			p = ((String[])parameters.get("p"))[0];
		}
		int sizePerPage = Integer.parseInt(getConfigurations().get(SysconfConst.RECORDS_PER_PAGE));
		List<Object> rst = weiboMgr.findWeiboByPage(values, p, sizePerPage);
		if(rst != null)
		{
			lstWeibo = (List<WeiboContent>) rst.get(0); // 获取结果集
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
		if(lstWeibo!=null && lstWeibo.size()>0) // 当内容列表非空时
		{
			pageKey.add(lstWeibo.get(0).getWid()); // 将当前首行rowKey添加到历史列表中
		}
		session.put(SystemConst.SESSION_PAGE, pageKey); // 替换session中的旧分页历史

		return "auditmgr";
	}
	
	/**
	 * 审核微博Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String audit() throws Exception
	{
		Object[] params; // 请求参数
		List<String> ids = new ArrayList<String>(); // 微博ID
		try
		{
			if(parameters.containsKey("id")) // 审核单条微博时
			{
				params = (Object[]) parameters.get("id");
			}
			else if(parameters.containsKey("checkbox")) // 勾选复选框批量审核时
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
		log.setDetail(SyslogConst.DETAIL_ADMIN_WEIBO_AUDIT);
		log.setDateline(new Date().getTime());
		weiboMgr.updateWeiboStatus(ids, WeiboConst.STATUS_AUDIT, log); // 执行审核
		
		this.resultMsg = ResultMsg.WEIBO_AUDIT; // 设置操作结果信息
		this.backTo = "weiboauditmgr.do"; // 设置返回页面
		
		return SUCCESS;
	}
	
	/**
	 * 查看微博Method
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
		weibo = weiboMgr.getWeibo(id); // 执行查询
		
		return "show";
	}
}
