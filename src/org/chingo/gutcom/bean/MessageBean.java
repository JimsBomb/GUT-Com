package org.chingo.gutcom.bean;

import org.chingo.gutcom.domain.CommonMsgRecv;
import org.chingo.gutcom.domain.CommonMsgSend;

public class MessageBean implements java.io.Serializable
{
	private String mid; // 消息ID
	private UserInfoBean user; // 用户Bean，已接收消息时为发送消息的用户，反之为收到消息的用户
	private String content; // 消息内容
	private long dateline; // 消息发送/接收时间戳
	private byte isread; // 已读标记
	
	public MessageBean()
	{
		
	}
	
	/**
	 * 根据CommonMsgSend对象填充字段
	 * @param msg CommonMsgSend对象
	 */
	public MessageBean(CommonMsgSend msg)
	{
		this.mid = msg.getMid();
		this.content = msg.getContent();
		this.dateline = msg.getDateline();
	}
	
	/**
	 * 根据CommonMsgRecv对象填充字段
	 * @param msg CommonMsgRecv对象
	 */
	public MessageBean(CommonMsgRecv msg)
	{
		this.mid = msg.getMid();
		this.content = msg.getContent();
		this.dateline = msg.getDateline();
		this.isread = msg.getIsread();
	}
	
	public String getMid()
	{
		return mid;
	}
	public void setMid(String mid)
	{
		this.mid = mid;
	}
	public UserInfoBean getUser()
	{
		return user;
	}
	public void setUser(UserInfoBean user)
	{
		this.user = user;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public long getDateline()
	{
		return dateline;
	}
	public void setDateline(long dateline)
	{
		this.dateline = dateline;
	}
	public byte getIsread()
	{
		return isread;
	}
	public void setIsread(byte isread)
	{
		this.isread = isread;
	}
	
}
