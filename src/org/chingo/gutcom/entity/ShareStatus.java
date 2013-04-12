package org.chingo.gutcom.entity;

// Generated Apr 12, 2013 9:55:45 AM by Hibernate Tools 4.0.0

/**
 * ShareStatus generated by hbm2java
 */
public class ShareStatus implements java.io.Serializable
{

	private Long sid;
	private ShareContent shareContent;
	private int viewcnt;

	public ShareStatus()
	{
	}

	public ShareStatus(ShareContent shareContent, int viewcnt)
	{
		this.shareContent = shareContent;
		this.viewcnt = viewcnt;
	}

	public Long getSid()
	{
		return this.sid;
	}

	public void setSid(Long sid)
	{
		this.sid = sid;
	}

	public ShareContent getShareContent()
	{
		return this.shareContent;
	}

	public void setShareContent(ShareContent shareContent)
	{
		this.shareContent = shareContent;
	}

	public int getViewcnt()
	{
		return this.viewcnt;
	}

	public void setViewcnt(int viewcnt)
	{
		this.viewcnt = viewcnt;
	}

}
