package org.chingo.gutcom.bean;

import org.chingo.gutcom.domain.WeiboTopic;

/**
 * 微博话题Bean类
 * @author Chingo.Org
 *
 */
public class WeiboTopicBean implements java.io.Serializable
{
	private String title; // 标题
	private int count; // 下属微博总数
	private long dateline; // 发起时间戳
	private byte isblock; // 屏蔽标记，0-未屏蔽，1-已屏蔽
	private UserInfoBean sponsor; // 发起者Bean
	private long lastpost; // 最后发表微博的发表时间戳
	
	public WeiboTopicBean()
	{
		
	}
	
	/**
	 * 根据WeiboTopic对象填充字段
	 * @param topic
	 */
	public WeiboTopicBean(WeiboTopic topic)
	{
		this.title = topic.getTitle();
		this.count = topic.getCount();
		this.dateline = topic.getDateline();
		this.isblock = topic.getIsblock();
		this.lastpost = topic.getLastpost();
		if(topic.getSponsor() != null) // 发起者对象非空时，填充
		{
			sponsor = new UserInfoBean(topic.getSponsor());
		}
	}
	
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public int getCount()
	{
		return count;
	}
	public void setCount(int count)
	{
		this.count = count;
	}
	public long getDateline()
	{
		return dateline;
	}
	public void setDateline(long dateline)
	{
		this.dateline = dateline;
	}
	public byte getIsblock()
	{
		return isblock;
	}
	public void setIsblock(byte isblock)
	{
		this.isblock = isblock;
	}
	public UserInfoBean getSponsor()
	{
		return sponsor;
	}
	public void setSponsor(UserInfoBean sponsor)
	{
		this.sponsor = sponsor;
	}
	public long getLastpost()
	{
		return lastpost;
	}
	public void setLastpost(long lastpost)
	{
		this.lastpost = lastpost;
	}
}
