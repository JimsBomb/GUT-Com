package org.chingo.gutcom.entity;

// Generated Apr 12, 2013 10:41:18 AM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

/**
 * WeiboContent generated by hbm2java
 */
public class WeiboContent implements java.io.Serializable
{

	private Long wid;
	private CommonUser commonUser;
	private String content;
	private int sourceid;
	private byte type;
	private byte format;
	private byte visibility;
	private byte status;
	private String thumbnailPic;
	private String middlePic;
	private String originalPic;
	private int dateline;
	private Set<WeiboTopicRelation> weiboTopicRelations = new HashSet<WeiboTopicRelation>(
			0);
	private Set<WeiboTopic> weiboTopics = new HashSet<WeiboTopic>(0);
	private Set<WeiboAt> weiboAts = new HashSet<WeiboAt>(0);
	private Set<WeiboFav> weiboFavs = new HashSet<WeiboFav>(0);
	private Set<WeiboReport> weiboReports = new HashSet<WeiboReport>(0);

	public WeiboContent()
	{
	}

	public WeiboContent(CommonUser commonUser, String content, int sourceid,
			byte type, byte format, byte visibility, byte status,
			String thumbnailPic, String middlePic, String originalPic,
			int dateline)
	{
		this.commonUser = commonUser;
		this.content = content;
		this.sourceid = sourceid;
		this.type = type;
		this.format = format;
		this.visibility = visibility;
		this.status = status;
		this.thumbnailPic = thumbnailPic;
		this.middlePic = middlePic;
		this.originalPic = originalPic;
		this.dateline = dateline;
	}

	public WeiboContent(CommonUser commonUser, String content, int sourceid,
			byte type, byte format, byte visibility, byte status,
			String thumbnailPic, String middlePic, String originalPic,
			int dateline, Set<WeiboTopicRelation> weiboTopicRelations,
			Set<WeiboTopic> weiboTopics, Set<WeiboAt> weiboAts,
			Set<WeiboFav> weiboFavs, Set<WeiboReport> weiboReports)
	{
		this.commonUser = commonUser;
		this.content = content;
		this.sourceid = sourceid;
		this.type = type;
		this.format = format;
		this.visibility = visibility;
		this.status = status;
		this.thumbnailPic = thumbnailPic;
		this.middlePic = middlePic;
		this.originalPic = originalPic;
		this.dateline = dateline;
		this.weiboTopicRelations = weiboTopicRelations;
		this.weiboTopics = weiboTopics;
		this.weiboAts = weiboAts;
		this.weiboFavs = weiboFavs;
		this.weiboReports = weiboReports;
	}

	public Long getWid()
	{
		return this.wid;
	}

	public void setWid(Long wid)
	{
		this.wid = wid;
	}

	public CommonUser getCommonUser()
	{
		return this.commonUser;
	}

	public void setCommonUser(CommonUser commonUser)
	{
		this.commonUser = commonUser;
	}

	public String getContent()
	{
		return this.content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public int getSourceid()
	{
		return this.sourceid;
	}

	public void setSourceid(int sourceid)
	{
		this.sourceid = sourceid;
	}

	public byte getType()
	{
		return this.type;
	}

	public void setType(byte type)
	{
		this.type = type;
	}

	public byte getFormat()
	{
		return this.format;
	}

	public void setFormat(byte format)
	{
		this.format = format;
	}

	public byte getVisibility()
	{
		return this.visibility;
	}

	public void setVisibility(byte visibility)
	{
		this.visibility = visibility;
	}

	public byte getStatus()
	{
		return this.status;
	}

	public void setStatus(byte status)
	{
		this.status = status;
	}

	public String getThumbnailPic()
	{
		return this.thumbnailPic;
	}

	public void setThumbnailPic(String thumbnailPic)
	{
		this.thumbnailPic = thumbnailPic;
	}

	public String getMiddlePic()
	{
		return this.middlePic;
	}

	public void setMiddlePic(String middlePic)
	{
		this.middlePic = middlePic;
	}

	public String getOriginalPic()
	{
		return this.originalPic;
	}

	public void setOriginalPic(String originalPic)
	{
		this.originalPic = originalPic;
	}

	public int getDateline()
	{
		return this.dateline;
	}

	public void setDateline(int dateline)
	{
		this.dateline = dateline;
	}

	public Set<WeiboTopicRelation> getWeiboTopicRelations()
	{
		return this.weiboTopicRelations;
	}

	public void setWeiboTopicRelations(
			Set<WeiboTopicRelation> weiboTopicRelations)
	{
		this.weiboTopicRelations = weiboTopicRelations;
	}

	public Set<WeiboTopic> getWeiboTopics()
	{
		return this.weiboTopics;
	}

	public void setWeiboTopics(Set<WeiboTopic> weiboTopics)
	{
		this.weiboTopics = weiboTopics;
	}

	public Set<WeiboAt> getWeiboAts()
	{
		return this.weiboAts;
	}

	public void setWeiboAts(Set<WeiboAt> weiboAts)
	{
		this.weiboAts = weiboAts;
	}

	public Set<WeiboFav> getWeiboFavs()
	{
		return this.weiboFavs;
	}

	public void setWeiboFavs(Set<WeiboFav> weiboFavs)
	{
		this.weiboFavs = weiboFavs;
	}

	public Set<WeiboReport> getWeiboReports()
	{
		return this.weiboReports;
	}

	public void setWeiboReports(Set<WeiboReport> weiboReports)
	{
		this.weiboReports = weiboReports;
	}

}
