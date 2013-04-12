package org.chingo.gutcom.entity;

// Generated Apr 12, 2013 10:41:18 AM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

/**
 * ShareContent generated by hbm2java
 */
public class ShareContent implements java.io.Serializable
{

	private Long sid;
	private ShareCategory shareCategory;
	private CommonUser commonUser;
	private String introduction;
	private byte status;
	private int dateline;
	private Set<ShareUrl> shareUrls = new HashSet<ShareUrl>(0);
	private ShareStatus shareStatus;
	private Set<ShareFav> shareFavs = new HashSet<ShareFav>(0);
	private Set<ShareReport> shareReports = new HashSet<ShareReport>(0);
	private Set<ShareComment> shareComments = new HashSet<ShareComment>(0);

	public ShareContent()
	{
	}

	public ShareContent(ShareCategory shareCategory, CommonUser commonUser,
			String introduction, byte status, int dateline)
	{
		this.shareCategory = shareCategory;
		this.commonUser = commonUser;
		this.introduction = introduction;
		this.status = status;
		this.dateline = dateline;
	}

	public ShareContent(ShareCategory shareCategory, CommonUser commonUser,
			String introduction, byte status, int dateline,
			Set<ShareUrl> shareUrls, ShareStatus shareStatus,
			Set<ShareFav> shareFavs, Set<ShareReport> shareReports,
			Set<ShareComment> shareComments)
	{
		this.shareCategory = shareCategory;
		this.commonUser = commonUser;
		this.introduction = introduction;
		this.status = status;
		this.dateline = dateline;
		this.shareUrls = shareUrls;
		this.shareStatus = shareStatus;
		this.shareFavs = shareFavs;
		this.shareReports = shareReports;
		this.shareComments = shareComments;
	}

	public Long getSid()
	{
		return this.sid;
	}

	public void setSid(Long sid)
	{
		this.sid = sid;
	}

	public ShareCategory getShareCategory()
	{
		return this.shareCategory;
	}

	public void setShareCategory(ShareCategory shareCategory)
	{
		this.shareCategory = shareCategory;
	}

	public CommonUser getCommonUser()
	{
		return this.commonUser;
	}

	public void setCommonUser(CommonUser commonUser)
	{
		this.commonUser = commonUser;
	}

	public String getIntroduction()
	{
		return this.introduction;
	}

	public void setIntroduction(String introduction)
	{
		this.introduction = introduction;
	}

	public byte getStatus()
	{
		return this.status;
	}

	public void setStatus(byte status)
	{
		this.status = status;
	}

	public int getDateline()
	{
		return this.dateline;
	}

	public void setDateline(int dateline)
	{
		this.dateline = dateline;
	}

	public Set<ShareUrl> getShareUrls()
	{
		return this.shareUrls;
	}

	public void setShareUrls(Set<ShareUrl> shareUrls)
	{
		this.shareUrls = shareUrls;
	}

	public ShareStatus getShareStatus()
	{
		return this.shareStatus;
	}

	public void setShareStatus(ShareStatus shareStatus)
	{
		this.shareStatus = shareStatus;
	}

	public Set<ShareFav> getShareFavs()
	{
		return this.shareFavs;
	}

	public void setShareFavs(Set<ShareFav> shareFavs)
	{
		this.shareFavs = shareFavs;
	}

	public Set<ShareReport> getShareReports()
	{
		return this.shareReports;
	}

	public void setShareReports(Set<ShareReport> shareReports)
	{
		this.shareReports = shareReports;
	}

	public Set<ShareComment> getShareComments()
	{
		return this.shareComments;
	}

	public void setShareComments(Set<ShareComment> shareComments)
	{
		this.shareComments = shareComments;
	}

}
