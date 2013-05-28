package org.chingo.gutcom.domain;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class CommonToken implements java.io.Serializable
{
	private String userid; // rowKey，用户ID
	private String accessToken; // 令牌
	private CommonUser commonUser; // 用户对象
	private long expiredTime = 0L; // 令牌过期时间戳
	
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
	
	/**
	 * 根据Result填充字段
	 * @param rst 填充数据源Result
	 */
	public void fillByResult(Result rst)
	{
		this.setUserid(Bytes.toString(rst.getRow()));
		this.setAccessToken(Bytes.toString(rst.getValue(Bytes.toBytes("info"),
				Bytes.toBytes("access_token"))));
		this.setExpiredTime(Long.parseLong(Bytes.toString(rst.getValue(
				Bytes.toBytes("info"), Bytes.toBytes("expired_time")))));
	}
}
