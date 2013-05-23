package org.chingo.gutcom.domain;

import java.io.Serializable;

public class WeiboFollowGroupOwn implements Serializable
{
	private String id; // rowKey，分组从属ID
	private String userId; // 用户ID
	private String groupName; // 用户拥有分组的名称
	
	public WeiboFollowGroupOwn()
	{
		
	}
	
	public WeiboFollowGroupOwn(String id, String userId, String groupName)
	{
		this.id = id;
		this.userId = userId;
		this.groupName = groupName;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}
	
	
}
