package org.chingo.gutcom.bean;

import java.util.HashSet;
import java.util.Set;

import org.chingo.gutcom.domain.WeiboFollowGroup;

/**
 * 关注用户分组Bean类
 * @author Chingo.Org
 *
 */
public class UserGroupBean implements java.io.Serializable
{
	private String groupname; // 分组名
	// 存放分组用户Bean
	private Set<UserInfoBean> statuses = new HashSet<UserInfoBean>(0);
	
	public UserGroupBean()
	{
		
	}
	
	/**
	 * 根据WeiboFollowGroup对象填充字段
	 * @param group WeiboFollowGroup对象
	 */
	public UserGroupBean(WeiboFollowGroup group)
	{
		this.groupname = group.getGroupName();
	}
	
	public String getGroupname()
	{
		return groupname;
	}
	public void setGroupname(String groupname)
	{
		this.groupname = groupname;
	}
	public Set<UserInfoBean> getStatuses()
	{
		return statuses;
	}
	public void setStatuses(Set<UserInfoBean> statuses)
	{
		this.statuses = statuses;
	}
}
