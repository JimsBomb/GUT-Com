package org.chingo.gutcom.service;

import java.util.List;
import java.util.Map;

import org.chingo.gutcom.bean.WeiboInfoBean;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.WeiboContent;

public interface WbCommManager
{
	/**
	 * 获取当前用户收到的评论列表
	 * @param uid 用户ID
	 * @param timestamp 上次更新获取时间戳
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 单页查询数量
	 * @return List中第一个对象为WeiboInfoBean列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> fetchRecvComment(String uid, long timestamp,
			String startRow, int pageSize);
	
	/**
	 * 获取当前用户发出的评论列表
	 * @param uid 用户ID
	 * @param timestamp 上次更新获取时间戳
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 单页查询数量
	 * @return List中第一个对象为WeiboInfoBean列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> fetchPostComment(String uid, long timestamp,
			String startRow, int pageSize);
	
	/**
	 * 获取指定微博的评论列表
	 * @param wid 微博ID
	 * @param timestamp 上次更新获取时间戳
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 单页查询数量
	 * @return List中第一个对象为WeiboInfoBean列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> fetchWeiboComment(String wid, long timestamp,
			String startRow, int pageSize);
	
	/**
	 * 发表一条评论
	 * @param comment 评论对象
	 * @param words 过滤词列表
	 * @param log 日志对象
	 * @return 评论WeiboInfoBean
	 */
	public WeiboInfoBean postComment(WeiboContent comment, Map<String, Byte> words,
			CommonSyslog log);
	
	/**
	 * 删除一条评论
	 * @param wid 要删除的评论ID
	 * @param log 日志对象
	 * @return true-删除成功，false-删除失败
	 */
	public boolean dropComment(String wid, CommonSyslog log);
}
