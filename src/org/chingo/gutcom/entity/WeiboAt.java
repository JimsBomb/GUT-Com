package org.chingo.gutcom.entity;

// Generated Apr 11, 2013 4:11:18 PM by Hibernate Tools 4.0.0

/**
 * WeiboAt generated by hbm2java
 */
public class WeiboAt implements java.io.Serializable
{

	private Long id;
	private WeiboContent weiboContent;
	private CommonUser commonUser;

	public WeiboAt()
	{
	}

	public WeiboAt(WeiboContent weiboContent, CommonUser commonUser)
	{
		this.weiboContent = weiboContent;
		this.commonUser = commonUser;
	}

	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public WeiboContent getWeiboContent()
	{
		return this.weiboContent;
	}

	public void setWeiboContent(WeiboContent weiboContent)
	{
		this.weiboContent = weiboContent;
	}

	public CommonUser getCommonUser()
	{
		return this.commonUser;
	}

	public void setCommonUser(CommonUser commonUser)
	{
		this.commonUser = commonUser;
	}

}
