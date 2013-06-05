package org.chingo.gutcom.action.api.weibo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.chingo.gutcom.action.base.api.weibo.WbBaseAction;
import org.chingo.gutcom.bean.UserInfoBean;
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
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.domain.WeiboContent;

public class WbAction extends WbBaseAction
{
	private String wid; // 微博ID
	private String uid; // 用户ID
	private String nickname; // 用户昵称
	private String title; // 话题标题
	private String status; // 微博内容
	private byte visibility = WeiboConst.VISIBILITY_ALL; // 微博可见性
	private String pic; // 微博图片
	private String sid; // 转发/评论源微博ID
	private int count = 20; // 单页显示数量
	private String nextrow; // 下页首行rowKey
	private byte trim_user = 0; // 简化用户Bean标记，0-返回完整字段，1-仅返回用户ID和昵称字段
	private byte trim_source = 0; // 简化源微博标记，0-返回完整字段，1-仅返回源ID、源内容和简化后的用户Bean
	// 存放JSON格式响应数据
	private Map<String, Object> jsonRst = new HashMap<String, Object>();

	public String getWid()
	{
		return this.wid;
	}
	
	public void setWid(String wid)
	{
		this.wid = WebUtil.decode(wid);
	}
	
	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = WebUtil.decode(uid);
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = WebUtil.decode(nickname);
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public void setTitle(String title)
	{
		this.title = WebUtil.decode(title);
	}
	
	public String getStatus()
	{
		return this.status;
	}
	
	public void setStatus(String status)
	{
		this.status = WebUtil.decode(status);
	}
	
	public byte getVisibility()
	{
		return this.visibility;
	}
	
	public void setVisibility(byte visibility)
	{
		this.visibility = visibility;
	}
	
	public String getPic()
	{
		return this.pic;
	}
	
	public void setPic(String pic)
	{
		this.pic = pic;
	}
	
	public String getSid()
	{
		return this.sid;
	}
	
	public void setSid(String sid)
	{
		this.sid = sid;
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
		return this.nextrow;
	}
	
	public void setNextrow(String nextrow)
	{
		this.nextrow = WebUtil.decode(nextrow);
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

	public void setJsonRst(Map<String, Object> jsonRst)
	{
		this.jsonRst = jsonRst;
	}
	
	/**
	 * 获取最新公共微博Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String fetchPublic() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if(count>0 && count<=100 
				&& trim_user>=0 && trim_user<=1
				&& trim_source>=0 && trim_source<=1)
		{
			// 获取最新公共微博
			List<WeiboInfoBean> weibos = weiboMgr.fetchPublicWeibo(count, trim_user, trim_source);
			if(weibos != null) // 微博存在时
			{
				jo.put("statuses", weibos);
				jo.put("page_num", weibos.size());
			}
			else // 无微博时
			{
				jo.put("statuses", null);
				jo.put("page_num", null);
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
	 * 获取当前用户及其关注用户的最新微博列表Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String fetchList() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if(count>0 && count<=100 
				&& trim_user>=0 && trim_user<=1
				&& trim_source>=0 && trim_source<=1)
		{
			long timestamp; // 存放上次查询时间戳
			/* SESSION中存在上次查询时间戳时取出，否则新建 */
			if(session.containsKey(SystemConst.SESSION_LAST_FETCH_TIME))
			{
				Map<String, Long> lastTime = 
						(Map<String, Long>) session.get(SystemConst.SESSION_LAST_FETCH_TIME);
				if(lastTime.containsKey("wbFetchList"))
				{
					timestamp = lastTime.get("wbFetchList");
				}
				else
				{
					timestamp = new Date().getTime();
					lastTime.put("wbFetchList", timestamp);
				}
			}
			else
			{
				Map<String, Long> lastTime = new HashMap<String, Long>();
				timestamp = new Date().getTime();
				lastTime.put("wbFetchList", timestamp);
			}
			// 获取微博列表
			List<Object> rst = weiboMgr.fetchListWeibo(WebUtil.getUser(session).getUid(),
					timestamp, nextrow, count);
			if(rst != null) // 列表非空时，设置响应数据
			{
				List<WeiboInfoBean> weibos = (List<WeiboInfoBean>) rst.get(0);
				jo.put("statuses", weibos);
				jo.put("page_num", weibos.size());
				if(rst.size() > 1) // 有下一页时
				{
					jo.put("nextrow", rst.get(1));
				}
				else
				{
					jo.put("nextrow", null);
				}
			}
			else // 无数据时
			{
				jo.put("statuses", null);
				jo.put("page_num", 0);
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
	 * 获取当前用户所关注用户的最新微博列表Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String fetchFollow() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if(count>0 && count<=100 
				&& trim_user>=0 && trim_user<=1
				&& trim_source>=0 && trim_source<=1)
		{
			long timestamp; // 存放上次查询时间戳
			/* SESSION中存在上次查询时间戳时取出，否则新建 */
			if(session.containsKey(SystemConst.SESSION_LAST_FETCH_TIME))
			{
				Map<String, Long> lastTime = 
						(Map<String, Long>) session.get(SystemConst.SESSION_LAST_FETCH_TIME);
				if(lastTime.containsKey("wbFetchFollow"))
				{
					timestamp = lastTime.get("wbFetchFollow");
				}
				else
				{
					timestamp = new Date().getTime();
					lastTime.put("wbFetchFollow", timestamp);
				}
			}
			else
			{
				Map<String, Long> lastTime = new HashMap<String, Long>();
				timestamp = new Date().getTime();
				lastTime.put("wbFetchFollow", timestamp);
			}
			// 获取微博列表
			List<Object> rst = weiboMgr.fetchListWeibo(null,
					timestamp, nextrow, count);
			if(rst != null) // 列表非空时，设置响应数据
			{
				List<WeiboInfoBean> weibos = (List<WeiboInfoBean>) rst.get(0);
				jo.put("statuses", weibos);
				jo.put("page_num", weibos.size());
				if(rst.size() > 1) // 有下一页时
				{
					jo.put("nextrow", rst.get(1));
				}
				else
				{
					jo.put("nextrow", null);
				}
			}
			else // 无数据时
			{
				jo.put("statuses", null);
				jo.put("page_num", 0);
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
	 * 获取当前用户的微博列表Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String fetchMine() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if(count>0 && count<=100 
				&& trim_user>=0 && trim_user<=1
				&& trim_source>=0 && trim_source<=1)
		{
			long timestamp; // 存放上次查询时间戳
			/* SESSION中存在上次查询时间戳时取出，否则新建 */
			if(session.containsKey(SystemConst.SESSION_LAST_FETCH_TIME))
			{
				Map<String, Long> lastTime = 
						(Map<String, Long>) session.get(SystemConst.SESSION_LAST_FETCH_TIME);
				if(lastTime.containsKey("wbFetchMine"))
				{
					timestamp = lastTime.get("wbFetchMine");
				}
				else
				{
					timestamp = new Date().getTime();
					lastTime.put("wbFetchMine", timestamp);
				}
			}
			else
			{
				Map<String, Long> lastTime = new HashMap<String, Long>();
				timestamp = new Date().getTime();
				lastTime.put("wbFetchMine", timestamp);
			}
			// 获取微博列表
			List<Object> rst = weiboMgr.fetchMyWeibo(WebUtil.getUser(session).getUid(),
					timestamp, nextrow, count);
			if(rst != null) // 列表非空时，设置响应数据
			{
				List<WeiboInfoBean> weibos = (List<WeiboInfoBean>) rst.get(0);
				jo.put("statuses", weibos);
				jo.put("page_num", weibos.size());
				if(rst.size() > 1) // 有下一页时
				{
					jo.put("nextrow", rst.get(1));
				}
				else
				{
					jo.put("nextrow", null);
				}
			}
			else // 无数据时
			{
				jo.put("statuses", null);
				jo.put("page_num", 0);
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
	 * 获取指定用户的最新微博列表Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String fetchUser() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if((uid!=null&&!uid.isEmpty())
				||(nickname!=null&&!nickname.isEmpty()) 
				&& count>0 && count<=100 
				&& trim_user>=0 && trim_user<=1
				&& trim_source>=0 && trim_source<=1)
		{
			long timestamp; // 存放上次查询时间戳
			/* SESSION中存在上次查询时间戳时取出，否则新建 */
			if(session.containsKey(SystemConst.SESSION_LAST_FETCH_TIME))
			{
				Map<String, Long> lastTime = 
						(Map<String, Long>) session.get(SystemConst.SESSION_LAST_FETCH_TIME);
				if(lastTime.containsKey("wbFetchMine"))
				{
					timestamp = lastTime.get("wbFetchMine");
				}
				else
				{
					timestamp = new Date().getTime();
					lastTime.put("wbFetchMine", timestamp);
				}
			}
			else
			{
				Map<String, Long> lastTime = new HashMap<String, Long>();
				timestamp = new Date().getTime();
				lastTime.put("wbFetchMine", timestamp);
			}
			// 获取微博列表
			List<Object> rst = weiboMgr.fetchOnesWeibo(uid, nickname,
					timestamp, nextrow, count);
			if(rst != null) // 列表非空时，设置响应数据
			{
				List<WeiboInfoBean> weibos = (List<WeiboInfoBean>) rst.get(0);
				jo.put("statuses", weibos);
				jo.put("page_num", weibos.size());
				if(rst.size() > 1) // 有下一页时
				{
					jo.put("nextrow", rst.get(1));
				}
				else
				{
					jo.put("nextrow", null);
				}
			}
			else // 无数据时
			{
				jo.put("statuses", null);
				jo.put("page_num", 0);
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
	 * 获取指定话题的最新微博列表Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String fetchTopic() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if(title!=null && !title.isEmpty()
				&& count>0 && count<=100 
				&& trim_user>=0 && trim_user<=1
				&& trim_source>=0 && trim_source<=1)
		{
			long timestamp; // 存放上次查询时间戳
			/* SESSION中存在上次查询时间戳时取出，否则新建 */
			if(session.containsKey(SystemConst.SESSION_LAST_FETCH_TIME))
			{
				Map<String, Long> lastTime = 
						(Map<String, Long>) session.get(SystemConst.SESSION_LAST_FETCH_TIME);
				if(lastTime.containsKey("wbFetchTopic"))
				{
					timestamp = lastTime.get("wbFetchTopic");
				}
				else
				{
					timestamp = new Date().getTime();
					lastTime.put("wbFetchTopic", timestamp);
				}
			}
			else
			{
				Map<String, Long> lastTime = new HashMap<String, Long>();
				timestamp = new Date().getTime();
				lastTime.put("wbFetchTopic", timestamp);
			}
			// 获取微博列表
			List<Object> rst = weiboMgr.fetchTopicWeibo(title, timestamp,
					nextrow, count);
			jsonRst.put("title", title); // 设置话题标题
			if(rst != null) // 列表非空时，设置响应数据
			{
				List<WeiboInfoBean> weibos = (List<WeiboInfoBean>) rst.get(0);
				jo.put("statuses", weibos);
				jo.put("page_num", weibos.size());
				if(rst.size() > 1) // 有下一页时
				{
					jo.put("nextrow", rst.get(1));
				}
				else
				{
					jo.put("nextrow", null);
				}
			}
			else // 无数据时
			{
				jo.put("statuses", null);
				jo.put("page_num", 0);
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
	 * 获取指定微博ID的微博信息Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String fetchId() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if(wid!=null && !wid.isEmpty())
		{
			// 获取微博信息
			WeiboInfoBean weibo = weiboMgr.fetchSingleWeibo(wid);
			if(weibo != null) // 微博存在时
			{
				jo.put("root", weibo); // 设置响应数据
			}
			else // 否则返回对象不存在错误信息
			{
				jsonRst.put("root", ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20002,
						WebUtil.getRequestAddr(request), null));
				jo.put("root", jsonRst);
			}
		}
		else // 返回参数错误信息
		{
			jsonRst.put("root", ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null));
			jo.put("root", jsonRst);
		}
		request.setAttribute("data", jo.getJSONObject("root").toString());
		return SUCCESS;
	}
	
	/**
	 * 获取提及@当前用户的微博列表Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String fetchAt() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if(count>0 && count<=100 
				&& trim_user>=0 && trim_user<=1
				&& trim_source>=0 && trim_source<=1)
		{
			long timestamp;
			/* SESSION中存在上次查询时间戳时取出，否则新建 */
			if(session.containsKey(SystemConst.SESSION_LAST_FETCH_TIME))
			{
				Map<String, Long> lastTime = 
						(Map<String, Long>) session.get(SystemConst.SESSION_LAST_FETCH_TIME);
				if(lastTime.containsKey("wbFetchAt"))
				{
					timestamp = lastTime.get("wbFetchAt");
				}
				else
				{
					timestamp = new Date().getTime();
					lastTime.put("wbFetchAt", timestamp);
				}
			}
			else
			{
				Map<String, Long> lastTime = new HashMap<String, Long>();
				timestamp = new Date().getTime();
				lastTime.put("wbFetchAt", timestamp);
			}
			List<Object> rst = weiboMgr.fetchAtWeibo(uid, timestamp, nextrow, count);
			if(rst != null) // 列表非空时，设置响应数据
			{
				List<WeiboInfoBean> weibos = (List<WeiboInfoBean>) rst.get(0);
				jo.put("statuses", weibos);
				jo.put("page_num", weibos.size());
				if(rst.size() > 1) // 有下一页时
				{
					jo.put("nextrow", rst.get(1));
				}
				else
				{
					jo.put("nextrow", null);
				}
			}
			else // 无数据时
			{
				jo.put("statuses", null);
				jo.put("page_num", 0);
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
	 * 发表一条新微博Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String post() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if(status!=null && VerifyUtil.checkWeiboContent(status)
				&& (visibility==WeiboConst.VISIBILITY_ALL
				|| visibility==WeiboConst.VISIBILITY_FOLLOWING_ONLY))
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_WEIBO_POST);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_OP_FRONT);
			log.setUserid(WebUtil.getUser(session).getUid());
			/* 创建微博对象 */
			WeiboContent weibo = new WeiboContent();
			weibo.setWid(FormatUtil.createRowKey());
			weibo.setAuthorid(log.getUserid());
			weibo.setContent(status);
			weibo.setDateline(log.getDateline());
			weibo.setVisibility(visibility);
			weibo.setFormat(WeiboConst.FORMAT_TEXT);
			/* 根据系统设置设置微博是否需要审核 */
			if(application.containsKey(SystemConst.CONTEXT_CONF))
			{
				Map<String, String> confs = (Map<String, String>) application
						.get(SystemConst.CONTEXT_CONF);
				if(confs.containsKey(SysconfConst.WEIBO_VERIFY))
				{
					weibo.setStatus(Byte.parseByte(confs.get(SysconfConst.WEIBO_VERIFY)));
				}
			}
			/* 获取过滤词列表 */
			Map<String, Byte> words = null;
			if(application.containsKey(SystemConst.CONTEXT_FILTER_WORD))
			{
				words = (Map<String, Byte>) application
						.get(SystemConst.CONTEXT_FILTER_WORD);
			}
			// 插入新微博
			WeiboInfoBean weiboBean = weiboMgr.postWeibo(weibo, words, log);
			if(weiboBean != null) // 发表成功时
			{
				jo.put("root", weiboBean);
			}
			else // 失败时返回错误信息
			{
				jsonRst.put("root", ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20003,
						WebUtil.getRequestAddr(request), null));
				jo.put("root", jsonRst);
			}
		}
		else // 返回参数错误信息
		{
			jsonRst.put("root", ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null));
			jo.put("root", jsonRst);
		}
		request.setAttribute("data", jo.getJSONObject("root").toString());
		return SUCCESS;
	}
	
	/**
	 * 上传图片并发表一条新微博Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String upload() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if(status!=null && VerifyUtil.checkWeiboContent(status)
				&& pic!=null && !pic.isEmpty()
				&& (visibility==WeiboConst.VISIBILITY_ALL
				|| visibility==WeiboConst.VISIBILITY_FOLLOWING_ONLY))
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_WEIBO_POST);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_OP_FRONT);
			log.setUserid(WebUtil.getUser(session).getUid());
			/* 创建微博对象 */
			WeiboContent weibo = new WeiboContent();
			weibo.setWid(FormatUtil.createRowKey());
			weibo.setAuthorid(log.getUserid());
			weibo.setContent(status);
			weibo.setDateline(log.getDateline());
			weibo.setVisibility(visibility);
			weibo.setFormat(WeiboConst.FORMAT_TEXT);
			/* 根据系统设置设置微博是否需要审核 */
			if(application.containsKey(SystemConst.CONTEXT_CONF))
			{
				Map<String, String> confs = (Map<String, String>) application
						.get(SystemConst.CONTEXT_CONF);
				if(confs.containsKey(SysconfConst.WEIBO_VERIFY))
				{
					weibo.setStatus(Byte.parseByte(confs.get(SysconfConst.WEIBO_VERIFY)));
				}
			}
			
			//TODO:上传图片处理
			
			/* 获取过滤词列表 */
			Map<String, Byte> words = null;
			if(application.containsKey(SystemConst.CONTEXT_FILTER_WORD))
			{
				words = (Map<String, Byte>) application
						.get(SystemConst.CONTEXT_FILTER_WORD);
			}
			// 插入新微博
			WeiboInfoBean weiboBean = weiboMgr.postWeibo(weibo, words, log);
			if(weiboBean != null) // 发表成功时
			{
				/* 更新用户计数信息 */
				UserInfoBean user = WebUtil.getUser(session);
				user.setWeibocnt(user.getWeibocnt()+1);
				session.put(SystemConst.SESSION_USER, user);
				jo.put("root", weiboBean); // 响应数据
			}
			else // 失败时返回错误信息
			{
				jsonRst.put("root", ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20003,
						WebUtil.getRequestAddr(request), null));
				jo.put("root", jsonRst);
			}
		}
		else // 返回参数错误信息
		{
			jsonRst.put("root", ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null));
			jo.put("root", jsonRst);
		}

		request.setAttribute("data", jo.getJSONObject("root").toString());
		return SUCCESS;
	}
	
	/**
	 * 转发一条微博Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String repost() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if(status!=null && VerifyUtil.checkWeiboContent(status)
				&& (visibility==WeiboConst.VISIBILITY_ALL
				|| visibility==WeiboConst.VISIBILITY_FOLLOWING_ONLY))
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_WEIBO_POST);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_OP_FRONT);
			log.setUserid(WebUtil.getUser(session).getUid());
			/* 创建微博对象 */
			WeiboContent weibo = new WeiboContent();
			weibo.setWid(FormatUtil.createRowKey());
			weibo.setAuthorid(log.getUserid());
			weibo.setContent(status);
			weibo.setDateline(log.getDateline());
			weibo.setVisibility(visibility);
			weibo.setFormat(WeiboConst.FORMAT_TEXT);
			weibo.setType(WeiboConst.TYPE_REPOST); // 设置类型为转发微博
			weibo.setSourceid(sid); // 设置源微博ID
			/* 根据系统设置设置微博是否需要审核 */
			if(application.containsKey(SystemConst.CONTEXT_CONF))
			{
				Map<String, String> confs = (Map<String, String>) application
						.get(SystemConst.CONTEXT_CONF);
				if(confs.containsKey(SysconfConst.WEIBO_VERIFY))
				{
					weibo.setStatus(Byte.parseByte(confs.get(SysconfConst.WEIBO_VERIFY)));
				}
			}
			/* 获取过滤词列表 */
			Map<String, Byte> words = null;
			if(application.containsKey(SystemConst.CONTEXT_FILTER_WORD))
			{
				words = (Map<String, Byte>) application
						.get(SystemConst.CONTEXT_FILTER_WORD);
			}
			// 插入新微博
			WeiboInfoBean weiboBean = weiboMgr.postWeibo(weibo, words, log);
			if(weiboBean != null) // 发表成功时
			{
				/* 更新用户计数信息 */
				UserInfoBean user = WebUtil.getUser(session);
				user.setWeibocnt(user.getWeibocnt()+1);
				session.put(SystemConst.SESSION_USER, user);
				jo.put("root", weiboBean); // 响应数据
			}
			else // 失败时返回错误信息
			{
				jsonRst.put("root", ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20003,
						WebUtil.getRequestAddr(request), null));
				jo.put("root", jsonRst);
			}
		}
		else // 返回参数错误信息
		{
			jsonRst.put("root", ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null));
			jo.put("root", jsonRst);
		}
		request.setAttribute("data", jo.getJSONObject("root").toString());
		return SUCCESS;
	}
	
	/**
	 * 根据微博ID删除指定微博Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String drop() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if(wid!=null && !wid.isEmpty())
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_WEIBO_DROP);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_OP_FRONT);
			log.setUserid(WebUtil.getUser(session).getUid());
			if(weiboMgr.dropWeibo(wid, log) == true) // 删除成功时
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
