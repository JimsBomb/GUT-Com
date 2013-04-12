package org.chingo.gutcom.entity;

// Generated Apr 12, 2013 9:55:45 AM by Hibernate Tools 4.0.0

/**
 * CommonMsgSend generated by hbm2java
 */
public class CommonMsgSend implements java.io.Serializable
{

	private Long mid;
	private CommonUser commonUserByRecvuser;
	private CommonUser commonUserBySenduser;
	private String content;
	private int dateline;

	public CommonMsgSend()
	{
	}

	public CommonMsgSend(CommonUser commonUserByRecvuser,
			CommonUser commonUserBySenduser, String content, int dateline)
	{
		this.commonUserByRecvuser = commonUserByRecvuser;
		this.commonUserBySenduser = commonUserBySenduser;
		this.content = content;
		this.dateline = dateline;
	}

	public Long getMid()
	{
		return this.mid;
	}

	public void setMid(Long mid)
	{
		this.mid = mid;
	}

	public CommonUser getCommonUserByRecvuser()
	{
		return this.commonUserByRecvuser;
	}

	public void setCommonUserByRecvuser(CommonUser commonUserByRecvuser)
	{
		this.commonUserByRecvuser = commonUserByRecvuser;
	}

	public CommonUser getCommonUserBySenduser()
	{
		return this.commonUserBySenduser;
	}

	public void setCommonUserBySenduser(CommonUser commonUserBySenduser)
	{
		this.commonUserBySenduser = commonUserBySenduser;
	}

	public String getContent()
	{
		return this.content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public int getDateline()
	{
		return this.dateline;
	}

	public void setDateline(int dateline)
	{
		this.dateline = dateline;
	}

}
