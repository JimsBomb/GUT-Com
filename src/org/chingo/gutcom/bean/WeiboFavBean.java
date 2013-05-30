package org.chingo.gutcom.bean;

import org.chingo.gutcom.domain.WeiboFav;

/**
 * 微博收藏Bean类
 * @author Chingo.Org
 *
 */
public class WeiboFavBean implements java.io.Serializable
{
	private String fid; // 收藏ID
	private WeiboInfoBean obj = null; // 收藏的微博Bean
	
	public WeiboFavBean()
	{
		
	}
	
	/**
	 * 根据WeiboFav对象填充字段
	 * @param fav WeiboFav对象
	 */
	public WeiboFavBean(WeiboFav fav)
	{
		this.fid = fav.getFid();
	}

	public String getFid()
	{
		return fid;
	}

	public void setFid(String fid)
	{
		this.fid = fid;
	}

	public WeiboInfoBean getObj()
	{
		return obj;
	}

	public void setObj(WeiboInfoBean obj)
	{
		this.obj = obj;
	}
}
