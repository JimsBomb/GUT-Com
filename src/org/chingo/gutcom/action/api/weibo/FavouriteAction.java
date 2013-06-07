package org.chingo.gutcom.action.api.weibo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.chingo.gutcom.action.base.api.weibo.FavouriteBaseAction;
import org.chingo.gutcom.bean.WeiboFavBean;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;

public class FavouriteAction extends FavouriteBaseAction
{
	private String fid; // 收藏ID
	private String wid; // 微博ID

	private int count = 20; // 单页显示数量
	private String nextrow = null; // 下一页首条记录的rowKey
	private byte trim_user = 0; // 简化用户Bean标记，0-返回完整字段，1-仅返回用户ID和昵称字段
	private byte trim_source = 0; // 简化源微博标记，0-返回完整字段，1-仅返回源ID、源内容和简化后的用户Bean
	
	private Map<String, Object> jsonRst = new HashMap<String, Object>();

	public void setFid(String fid)
	{
		this.fid = fid;
	}

	public void setWid(String wid)
	{
		this.wid = wid;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public void setNextrow(String nextrow)
	{
		this.nextrow = nextrow;
	}

	public void setTrim_user(byte trim_user)
	{
		this.trim_user = trim_user;
	}

	public void setTrim_source(byte trim_source)
	{
		this.trim_source = trim_source;
	}

	public void setJsonRst(Map<String, Object> jsonRst)
	{
		this.jsonRst = jsonRst;
	}
	
	/**
	 * 获取当前用户的收藏列表Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String list() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		// 检查参数
		if(count>0 && count<=100
				&& trim_user>=0 && trim_user<=1
				&& trim_source>=0 && trim_source<=1)
		{
			// 存放上次查询时间戳
			long timestamp = WebUtil.getTimestamp(session, "favList");
			// 获取收藏列表
			List<Object> rst = wbFavMgr.listFav(WebUtil.getUser(session).getUid(),
					timestamp, nextrow, count);
			if(rst != null) // 列表非空时
			{
				List<WeiboFavBean> favBeans = (List<WeiboFavBean>) rst.get(0);
				jo.put("favourites", favBeans);
				jo.put("page_num", favBeans.size());
				if(rst.size() > 1) // 有下一页时
				{
					jo.put("nextrow", rst.get(1));
				}
				else
				{
					jo.put("nextrow", null);
				}
			}
			else // 否则返回null
			{
				jo.put("favourites", null);
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
	 * 获取单条收藏信息Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String show() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		// 检查参数
		if(fid!=null && !fid.isEmpty()
				&& trim_user>=0 && trim_user<=1
				&& trim_source>=0 && trim_source<=1)
		{
			// 获取收藏
			WeiboFavBean favBean = wbFavMgr.showFav(WebUtil.getUser(session).getUid(), fid);
			if(favBean != null) // 收藏存在时
			{
				jo.put("root", favBean);
			}
			else // 不存在时返回错误信息
			{
				jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20002,
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
	 * 添加收藏Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String create() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		// 检查参数
		if(wid!=null && !wid.isEmpty())
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_WEIBO_FAV);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_WEIBO);
			log.setUserid(WebUtil.getUser(session).getUid());
			log.setNickname(WebUtil.getUser(session).getNickname());
			// 收藏成功时
			if(wbFavMgr.createFav(wid, log) == true)
			{
				jo.put("result", true);
				request.setAttribute("data", jo.toString());
			}
			else // 收藏失败时
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
	
	/**
	 * 删除收藏Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String drop() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		// 检查参数
		if(wid!=null && !wid.isEmpty())
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_WEIBO_FAV_DROP);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_WEIBO);
			log.setUserid(WebUtil.getUser(session).getUid());
			log.setNickname(WebUtil.getUser(session).getNickname());
			// 收藏成功时
			if(wbFavMgr.dropFav(wid, log) == true)
			{
				jo.put("result", true);
				request.setAttribute("data", jo.toString());
			}
			else // 收藏失败时
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
