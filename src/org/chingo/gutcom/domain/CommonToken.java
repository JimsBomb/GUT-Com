package org.chingo.gutcom.domain;

public class CommonToken implements java.io.Serializable
{
	private Long tid;
	private CommonUser commonUser;
	private String accessToken;
	private long expiredTime;
	
	public CommonToken()
	{
		
	}
	
	public CommonToken(CommonUser commonUser, String accessToken, long expiredTime)
	{
		this.commonUser = commonUser;
		this.accessToken = accessToken;
		this.expiredTime = expiredTime;
	}

	public Long getTid()
	{
		return tid;
	}

	public void setTid(Long tid)
	{
		this.tid = tid;
	}

	public CommonUser getCommonUser()
	{
		return commonUser;
	}

	public void setCommonUser(CommonUser commonUser)
	{
		this.commonUser = commonUser;
	}

	public String getAccessToken()
	{
		return accessToken;
	}

	public void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;
	}

	public long getExpiredTime()
	{
		return expiredTime;
	}

	public void setExpiredTime(long expiredTime)
	{
		this.expiredTime = expiredTime;
	}
	
}
