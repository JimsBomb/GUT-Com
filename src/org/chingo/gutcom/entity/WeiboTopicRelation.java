package org.chingo.gutcom.entity;

// Generated Apr 12, 2013 9:55:45 AM by Hibernate Tools 4.0.0

/**
 * WeiboTopicRelation generated by hbm2java
 */
public class WeiboTopicRelation implements java.io.Serializable
{

	private Long id;
	private WeiboTopic weiboTopic;
	private WeiboContent weiboContent;

	public WeiboTopicRelation()
	{
	}

	public WeiboTopicRelation(WeiboTopic weiboTopic, WeiboContent weiboContent)
	{
		this.weiboTopic = weiboTopic;
		this.weiboContent = weiboContent;
	}

	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public WeiboTopic getWeiboTopic()
	{
		return this.weiboTopic;
	}

	public void setWeiboTopic(WeiboTopic weiboTopic)
	{
		this.weiboTopic = weiboTopic;
	}

	public WeiboContent getWeiboContent()
	{
		return this.weiboContent;
	}

	public void setWeiboContent(WeiboContent weiboContent)
	{
		this.weiboContent = weiboContent;
	}

}
