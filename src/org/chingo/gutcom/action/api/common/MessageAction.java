package org.chingo.gutcom.action.api.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.action.base.api.MessageBaseAction;
import org.chingo.gutcom.bean.MessageBean;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.WebUtil;

public class MessageAction extends MessageBaseAction
{
	private byte type = -1; // 消息类型
	private int count = 20; // 单页显示数量
	private String nextrow = null; // 下一页首条记录的rowKey
	// 存放JSON响应数据
	private Map<String, Object> jsonRst = new HashMap<String, Object>();
	
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
	public Map<String, Object> getJsonRst()
	{
		return jsonRst;
	}
	public void setJsonRst(Map<String, Object> jsonRst)
	{
		this.jsonRst = jsonRst;
	}
	
	/**
	 * 获取消息Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String list() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		// 检查参数
		if(type>=0 && type<=1
				&& count>0 && count<=100)
		{
			// 获取消息
			List<Object> rst = msgMgr.listMsg(WebUtil.getUser(session).getUid(),
					type, count, nextrow);
			if(rst != null) // 消息非空时
			{
				/* 设置响应数据 */
				List<MessageBean> msgBeans = (List<MessageBean>) rst.get(0);
				jsonRst.put("statuses", msgBeans); // 消息Bean数组
				jsonRst.put("page_num", msgBeans.size()); // 消息Bean数量
				if(rst.size() > 1) // 存在下一页时
				{
					jsonRst.put("nextrow", rst.get(1)); // 下一页首条记录rowKey
				}
				else // 否则置null
				{
					jsonRst.put("nextrow", null);
				}
			}
			else // 返回null
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
