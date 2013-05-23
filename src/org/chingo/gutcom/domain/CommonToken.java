package org.chingo.gutcom.domain;

public class CommonToken implements java.io.Serializable
{
	private String userid; // rowKey，用户ID
	private String accessToken; // 令牌
	private CommonUser commonUser; // 用户对象
	private long expiredTime; // 令牌过期时间戳
	
	public CommonToken()
	{
		
	}
	
	public CommonToken(String userid, String accessToken, long expiredTime)
	{
		this.userid = userid;
		this.accessToken = accessToken;
		this.expiredTime = expiredTime;
	}

	public String getUserid()
	{
		return this.userid;
	}
	
	public void setUserid(String userid)
	{
		this.userid = userid;
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
