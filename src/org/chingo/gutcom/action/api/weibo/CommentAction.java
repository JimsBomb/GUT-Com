package org.chingo.gutcom.action.api.weibo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.chingo.gutcom.action.base.api.weibo.CommentBaseAction;
import org.chingo.gutcom.bean.WeiboInfoBean;
import org.chingo.gutcom.common.constant.SysconfConst;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.constant.WeiboConst;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.FormatUtil;
import org.chingo.gutcom.common.util.VerifyUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.WeiboContent;

public class CommentAction extends CommentBaseAction
{
	private String wid; // 评论ID
	private String status; // 评论内容
	
	private int count = 20; // 单页显示数量
	private String nextrow = null; // 下一页首条记录的rowKey
	private byte trim_user = 0; // 简化用户Bean标记，0-返回完整字段，1-仅返回用户ID和昵称字段
	private byte trim_source = 0; // 简化源微博标记，0-返回完整字段，1-仅返回源ID、源内容和简化后的用户Bean
	
	private Map<String, Object> jsonRst = new HashMap<String, Object>();
	
	public String getWid()
	{
		return this.wid;
	}
	
	public void setWid(String wid)
	{
		this.wid = WebUtil.decode(wid);
	}
	
	public String getStatus()
	{
		return this.status;
	}
	
	public void setStatus(String status)
	{
		this.status = WebUtil.decode(status);
	}
	
	public int getCount()
	{
		return count;
	}
	public void setCount(int count)
	{
		this.count = count;
	}
	public String getNextrow()
	{
		return nextrow;
	}
	public void setNextrow(String nextrow)
	{
		this.nextrow = nextrow;
	}
	public byte getTrim_user()
	{
		return trim_user;
	}
	public void setTrim_user(byte trim_user)
	{
		this.trim_user = trim_user;
	}
	public byte getTrim_source()
	{
		return trim_source;
	}
	public void setTrim_source(byte trim_source)
	{
		this.trim_source = trim_source;
	}
	public Map<String, Object> getJsonRst()
	{
		return jsonRst;
	}
	
	/**
	 * 获取当前用户所收到的评论列表Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String fetchUserRecv() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		// 检查参数
		if(count>0 && count<=100
				&& trim_user>=0 && trim_user<=1
				&& trim_source>=0 && trim_source<=1)
		{
			// 存放上次查询时间戳
			long timestamp = WebUtil.getTimestamp(session, "commFetchUserRecv"); 
			
			// 获取评论
			List<Object> rst = wbCommMgr.fetchRecvComment(WebUtil.getUser(session).getUid(),
					timestamp, nextrow, count);
			if(rst != null) // 评论非空时，设置响应数据
			{
				List<WeiboInfoBean> comms = (List<WeiboInfoBean>) rst.get(0);
				jo.put("statuses", comms);
				jo.put("page_num", comms.size());
				if(rst.size() > 1) // 有下一页时
				{
					jo.put("nextrow", rst.get(1));
				}
				else // 否则设为null
				{
					jo.put("nextrow", null);
				}
			}
			else // 返回null
			{
				jo.put("statuses", null);
				jo.put("page_num", null);
				jo.put("nextrow", null);
			}
			request.setAttribute("data", jo.toString());
		}
		else // 返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
			request.setAttribute("data", jo.getJSONObject("root").toString());
		}
		return SUCCESS;
	}
	
	/**
	 * 获取当前用户所发表的评论列表Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String fetchUserPost() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		// 检查参数
		if(count>0 && count<=100
				&& trim_user>=0 && trim_user<=1
				&& trim_source>=0 && trim_source<=1)
		{
			// 存放上次查询时间戳
			long timestamp = WebUtil.getTimestamp(session, "commFetchUserPost");
			
			// 获取评论
			List<Object> rst = wbCommMgr.fetchPostComment(WebUtil.getUser(session).getUid(),
					timestamp, nextrow, count);
			if(rst != null) // 评论非空时，设置响应数据
			{
				List<WeiboInfoBean> comms = (List<WeiboInfoBean>) rst.get(0);
				jo.put("statuses", comms);
				jo.put("page_num", comms.size());
				if(rst.size() > 1) // 有下一页时
				{
					jo.put("nextrow", rst.get(1));
				}
				else // 否则设为null
				{
					jo.put("nextrow", null);
				}
			}
			else // 返回null
			{
				jo.put("statuses", null);
				jo.put("page_num", null);
				jo.put("nextrow", null);
			}
			request.setAttribute("data", jo.toString());
		}
		else // 返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
			request.setAttribute("data", jo.getJSONObject("root").toString());
		}
		return SUCCESS;
	}
	
	/**
	 * 获取指定微博所收到的评论列表Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String fetchWeibo() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		// 检查参数
		if(wid!=null && !wid.isEmpty()
				&& trim_user>=0 && trim_user<=1
				&& trim_source>=0 && trim_source<=1)
		{
			// 存放上次查询时间戳
			long timestamp = WebUtil.getTimestamp(session, "commFetchWeibo");
			
			// 获取评论
			List<Object> rst = wbCommMgr.fetchWeiboComment(wid,
					timestamp, nextrow, count);
			if(rst != null) // 评论非空时，设置响应数据
			{
				List<WeiboInfoBean> comms = (List<WeiboInfoBean>) rst.get(0);
				jo.put("statuses", comms);
				jo.put("page_num", comms.size());
				if(rst.size() > 1) // 有下一页时
				{
					jo.put("nextrow", rst.get(1));
				}
				else // 否则设为null
				{
					jo.put("nextrow", null);
				}
			}
			else // 返回null
			{
				jo.put("statuses", null);
				jo.put("page_num", null);
				jo.put("nextrow", null);
			}
			request.setAttribute("data", jo.toString());
		}
		else // 返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
			request.setAttribute("data", jo.getJSONObject("root").toString());
		}
		return SUCCESS;
	}
	
	/**
	 * 发表一条评论Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String post() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		if(wid!=null && !wid.isEmpty()
				&& status!=null && VerifyUtil.checkWeiboContent(status))
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_WEIBO_COMM);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_WEIBO);
			log.setUserid(WebUtil.getUser(session).getUid());
			log.setNickname(WebUtil.getUser(session).getNickname());
			/* 创建微博对象 */
			WeiboContent weibo = new WeiboContent();
			weibo.setWid(FormatUtil.createRowKey());
			weibo.setAuthorid(log.getUserid());
			weibo.setContent(status);
			weibo.setDateline(log.getDateline());
			weibo.setVisibility(WeiboConst.VISIBILITY_ALL);
			weibo.setFormat(WeiboConst.FORMAT_TEXT);
			/* 根据系统设置设置微博是否需要审核 */
			if(application.containsKey(SystemConst.CONTEXT_CONF))
			{
				Map<String, String> confs = (Map<String, String>) application
						.get(SystemConst.CONTEXT_CONF);
				if(confs.containsKey(SysconfConst.WEIBO_VERIFY))
				{
					weibo.setStatus(Byte.parseByte(String.valueOf(
							Byte.parseByte(confs.get(SysconfConst.WEIBO_VERIFY)) ^ 1)));
				}
			}
			/* 获取过滤词列表 */
			Map<String, Byte> words = null;
			if(application.containsKey(SystemConst.CONTEXT_FILTER_WORD))
			{
				words = (Map<String, Byte>) application
						.get(SystemConst.CONTEXT_FILTER_WORD);
			}
			// 发表评论
			WeiboInfoBean comm = wbCommMgr.postComment(weibo, words, log);
			if(comm != null) // 发表成功时
			{
				jo.put("root", comm);
			}
			else // 发表失败时
			{
				jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20003,
						WebUtil.getRequestAddr(request), null);
				jo.put("root", jsonRst);
			}
		}
		else // 返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
		}
		request.setAttribute("data", jo.getJSONObject("root").toString());
		return SUCCESS;
	}
	
	/**
	 * 根据评论ID删除指定评论Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String drop() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		if(wid!=null && !wid.isEmpty()) // 检查参数
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_WEIBO_COMM_DROP);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_WEIBO);
			log.setUserid(WebUtil.getUser(session).getUid());
			log.setNickname(WebUtil.getUser(session).getNickname());
			if(wbCommMgr.dropComment(wid, log) == true) // 删除评论成功时
			{
				jo.put("result", true);
				request.setAttribute("data", jo.toString());
			}
			else // 删除失败时
			{
				jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20002,
						WebUtil.getRequestAddr(request), null);
				jo.put("root", jsonRst);
				request.setAttribute("data", jo.getJSONObject("root").toString());
			}
		}
		else // 返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
			request.setAttribute("data", jo.getJSONObject("root").toString());
		}
		return SUCCESS;
	}
}
