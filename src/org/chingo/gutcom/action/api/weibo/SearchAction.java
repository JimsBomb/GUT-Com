package org.chingo.gutcom.action.api.weibo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.chingo.gutcom.action.base.api.weibo.SearchBaseAction;
import org.chingo.gutcom.bean.WeiboInfoBean;
import org.chingo.gutcom.bean.WeiboTopicBean;
import org.chingo.gutcom.common.constant.WeiboConst;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.VerifyUtil;
import org.chingo.gutcom.common.util.WebUtil;

public class SearchAction extends SearchBaseAction
{
	private String keyword; // 搜索关键词
	
	private int count = 20; // 单页显示数量
	private String nextrow = null; // 下一页首条记录的rowKey
	private byte trim_user = 0; // 简化用户Bean标记，0-返回完整字段，1-仅返回用户ID和昵称字段
	private byte trim_source = 0; // 简化源微博标记，0-返回完整字段，1-仅返回源ID、源内容和简化后的用户Bean
	
	private Map<String, Object> jsonRst = new HashMap<String, Object>();
	
	public String getKeyword()
	{
		return keyword;
	}
	public void setKeyword(String keyword)
	{
		this.keyword = WebUtil.decode(keyword);
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
	 * 根据关键字搜索微博Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String weibo() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		// 检查参数
		if(keyword!=null && VerifyUtil.checkSearchKeyword(keyword)
				&& count>0 && count<=100
				&& trim_user>=0 && trim_user<=1
				&& trim_source>=0 && trim_source<=1)
		{
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("content", keyword); // 查询条件为内容
			// 获取搜索结果
			List<Object> rst = weiboMgr.searchWeibo(WebUtil.getUser(session).getUid(),
					values, nextrow, count);
			if(rst != null) // 结果非空时
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
			else // 返回空
			{
				jo.put("statuses", null);
				jo.put("page_num", null);
				jo.put("nextrow", null);
			}
			request.setAttribute("data", jo.toString());
		}
		else // 返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20002,
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
			request.setAttribute("data", jo.getJSONObject("root").toString());
		}
		
		return SUCCESS;
	}
	
	/**
	 * 根据关键字搜索话题Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String topic() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		// 检查参数
		if(keyword!=null && VerifyUtil.checkSearchKeyword(keyword)
				&& count>0 && count<=100)
		{
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("title", keyword); // 查询条件为内容
			values.put("status", WeiboConst.TOPIC_NOT_BLOCK); // 只查询未被屏蔽的话题
			// 获取搜索结果
			List<Object> rst = weiboMgr.searchWeibo(WebUtil.getUser(session).getUid(), 
					values, nextrow, count);
			if(rst != null) // 结果非空时
			{
				List<WeiboTopicBean> topics = (List<WeiboTopicBean>) rst.get(0);
				jo.put("statuses", topics);
				jo.put("page_num", topics.size());
				if(rst.size() > 1) // 有下一页时
				{
					jo.put("nextrow", rst.get(1));
				}
				else
				{
					jo.put("nextrow", null);
				}
			}
			else // 返回空
			{
				jo.put("statuses", null);
				jo.put("page_num", null);
				jo.put("nextrow", null);
			}
			request.setAttribute("data", jo.toString());
		}
		else // 返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20002,
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
			request.setAttribute("data", jo.getJSONObject("root").toString());
		}
		
		return SUCCESS;
	}
}
