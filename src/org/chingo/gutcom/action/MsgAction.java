package org.chingo.gutcom.action;

import java.util.Date;

import org.chingo.gutcom.action.base.MsgBaseAction;
import org.chingo.gutcom.common.constant.MsgConst;
import org.chingo.gutcom.domain.CommonMsgRecv;

public class MsgAction extends MsgBaseAction
{
	private String content; // 消息内容
	private int offset; // 第一个用户的索引
	private int size; // 每批次发送用户数
	
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public int getOffset()
	{
		return offset;
	}
	public void setOffset(int offset)
	{
		this.offset = offset;
	}
	public int getSize()
	{
		return size;
	}
	public void setSize(int size)
	{
		this.size = size;
	}
	
	/**
	 * 发布公告页面Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String mgr() throws Exception
	{
		return "mgr";
	}
	
	/**
	 * 批量发送消息Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String send() throws Exception
	{
		/* 封装消息 */
		CommonMsgRecv msg = new CommonMsgRecv();
		msg.setContent(content); // 内容
		msg.setDateline(new Date().getTime()); // 发送时间戳
		msg.setIsread(MsgConst.MSG_NOT_READ); // 未读标记
		if(msgMgr.addMsg(offset, size, msg, null) == false) // 未发送完毕
		{
			offset += size;
		}
		else // 发送完毕
		{
			offset = -1;
		}
		
		return "send";
	}
}
