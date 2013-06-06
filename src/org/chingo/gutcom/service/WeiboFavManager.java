package org.chingo.gutcom.service;

import java.util.List;

import org.chingo.gutcom.bean.WeiboFavBean;
import org.chingo.gutcom.domain.CommonSyslog;

public interface WeiboFavManager
{
	/**
	 * 获取用户的收藏列表
	 * @param uid 用户ID
	 * @param timestamp 上次更新获取时间戳
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 单页查询数量
	 * @return List中第一个对象为WeiboFavBean列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> listFav(String uid, long timestamp, String startRow, int pageSize);
	
	/**
	 * 获取单条收藏信息
	 * @param uid 用户ID
	 * @param fid 收藏ID
	 * @return 微博收藏Bean，无则返回null
	 */
	public WeiboFavBean showFav(String uid, String fid);
	
	/**
	 * 收藏微博
	 * @param wid 收藏的微博的ID
	 * @param log 日志对象
	 * @return true-收藏成功，false-收藏失败（对象不存在）
	 */
	public boolean createFav(String wid, CommonSyslog log);
	
	/**
	 * 删除收藏
	 * @param fid 要删除的收藏的ID
	 * @param log 日志对象
	 * @return true-删除成功，false-删除失败（对象不存在）
	 */
	public boolean dropFav(String fid, CommonSyslog log);
}
