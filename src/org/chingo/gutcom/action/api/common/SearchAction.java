package org.chingo.gutcom.action.api.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.action.base.api.common.SearchBaseAction;
import org.chingo.gutcom.bean.UserInfoBean;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.WebUtil;

public class SearchAction extends SearchBaseAction
{
	private String keyword; // 关键词
	private byte type = 0; // 查询类型，0-昵称，1-专业
	private int count = 20; // 单页查询数量
	private String nextrow = null; // 下一页首行的rowKey
	private byte trim_user = 0; // 简化user字段标记，0-返回完整字段，1-只返回userID和nickname字段
	// 存放JSON响应数据
	private Map<String, Object> jsonRst = new HashMap<String, Object>();
	
	public String getKeyword()
	{
		return keyword;
	}
	public void setKeyword(String keyword)
	{
		this.keyword = WebUtil.decode(keyword);
	}
	public byte getType()
	{
		return type;
	}
	public void setType(byte type)
	{
		this.type = type;
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
	public Map<String, Object> getJsonRst()
	{
		return jsonRst;
	}
	public void setJsonRst(Map<String, Object> jsonRst)
	{
		this.jsonRst = jsonRst;
	}
	
	/**
	 * 搜索用户Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String user() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		// 检查参数
		if(keyword!=null && keyword.length()<=300
				&& type>=0 && type<=1
				&& count>0 && count<=50
				&& trim_user>=0 && trim_user<=1)
		{
			// 搜索用户
			List<Object> rst = userMgr.searchUser(keyword, type,
					trim_user, count, nextrow);
			if(rst != null) // 有结果时
			{
				/* 设置响应数据 */
				List<UserInfoBean> users = (List<UserInfoBean>) rst.get(0);
				jsonRst.put("statuses", users);
				jsonRst.put("page_num", users.size());
				if(rst.size() > 1) // 有下一页时
				{
					jsonRst.put("nextrow", rst.get(1));
				}
				else // 否则置null
				{
					jsonRst.put("nextrow", null);
				}
			}
			else // 无结果则返回null
			{
				jsonRst.put("statuses", null);
				jsonRst.put("page_num", null);
				jsonRst.put("nextrow", null);
			}
		}
		else // 返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008, 
					WebUtil.getRequestAddr(request), null);
		}
		return SUCCESS;
	}
}
