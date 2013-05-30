package org.chingo.gutcom.bean;

import java.util.HashSet;
import java.util.Set;

import org.chingo.gutcom.domain.WeiboContent;
import org.chingo.gutcom.domain.WeiboTopic;

/**
 * 微博Bean类
 * @author Chingo.Org
 *
 */
public class WeiboInfoBean implements java.io.Serializable
{
	private String wid; // 微博ID
	private UserInfoBean author = null; // 作者用户Bean
	private String content; // 微博内容
	private WeiboInfoBean source = null; // 源微博Bean，转发或评论时可用
	private byte type = 0; // 微博类型
	private byte format = 0; // 微博内容格式
	private byte visibility = 0; // 微博可见性
	private byte status = 0; // 微博状态
	private String thumbnailPic = ""; // 缩略图地址
	private String middlePic = ""; // 标准图地址
	private String originalPic = ""; // 原始图地址
	private long dateline; // 发表时间戳
	private byte fav = 0; // 收藏标记，0-未收藏，1-已收藏
	private Set<WeiboTopicBean> topics = new HashSet<WeiboTopicBean>(0); // 所属话题集合对象
	
	public WeiboInfoBean()
	{
		
	}

	/**
	 * 根据WeiboContent填充字段
	 * @param weibo WeiboContent对象
	 */
	public WeiboInfoBean(WeiboContent weibo)
	{
		this.wid = weibo.getWid();
		this.content = weibo.getContent();
		this.type = weibo.getType();
		this.format = weibo.getFormat();
		this.visibility = weibo.getVisibility();
		this.status = weibo.getStatus();
		this.thumbnailPic = weibo.getThumbnailPic();
		this.middlePic = weibo.getMiddlePic();
		this.originalPic = weibo.getOriginalPic();
		this.dateline = weibo.getDateline();
		if(weibo.getAuthor() != null) // 作者对象非空时填充
		{
			this.author = new UserInfoBean(weibo.getAuthor());
		}
		if(weibo.getSource() != null) // 源微博对象非空时填充
		{
			this.source = new WeiboInfoBean(weibo.getSource());
		}
		if(weibo.getTopics() != null) // 话题列表非空时填充
		{
			for(WeiboTopic topic : weibo.getTopics())
			{
				WeiboTopicBean topicBean = new WeiboTopicBean(topic);
				this.topics.add(topicBean);
			}
		}
	}
	
	public String getWid()
	{
		return wid;
	}
	public void setWid(String wid)
	{
		this.wid = wid;
	}
	public UserInfoBean getAuthor()
	{
		return author;
	}
	public void setAuthor(UserInfoBean author)
	{
		this.author = author;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public WeiboInfoBean getSource()
	{
		return source;
	}
	public void setSource(WeiboInfoBean source)
	{
		this.source = source;
	}
	public byte getType()
	{
		return type;
	}
	public void setType(byte type)
	{
		this.type = type;
	}
	public byte getFormat()
	{
		return format;
	}
	public void setFormat(byte format)
	{
		this.format = format;
	}
	public byte getVisibility()
	{
		return visibility;
	}
	public void setVisibility(byte visibility)
	{
		this.visibility = visibility;
	}
	public byte getStatus()
	{
		return status;
	}
	public void setStatus(byte status)
	{
		this.status = status;
	}
	public String getThumbnailPic()
	{
		return thumbnailPic;
	}
	public void setThumbnailPic(String thumbnailPic)
	{
		this.thumbnailPic = thumbnailPic;
	}
	public String getMiddlePic()
	{
		return middlePic;
	}
	public void setMiddlePic(String middlePic)
	{
		this.middlePic = middlePic;
	}
	public String getOriginalPic()
	{
		return originalPic;
	}
	public void setOriginalPic(String originalPic)
	{
		this.originalPic = originalPic;
	}
	public long getDateline()
	{
		return dateline;
	}
	public void setDateline(long dateline)
	{
		this.dateline = dateline;
	}
	public byte getFav()
	{
		return fav;
	}
	public void setFav(byte fav)
	{
		this.fav = fav;
	}
	public Set<WeiboTopicBean> getTopics()
	{
		return topics;
	}
	public void setTopics(Set<WeiboTopicBean> topics)
	{
		this.topics = topics;
	}
}
