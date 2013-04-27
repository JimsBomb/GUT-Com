package org.chingo.gutcom.service;

import java.util.List;
import java.util.Map;

import org.chingo.gutcom.domain.WeiboContent;

public interface WeiboManager
{
	/**
	 * 新增微博
	 * @param weibo 要插入的微博对象
	 * @param logParams 日志参数
	 */
	public void addWeibo(WeiboContent weibo, Map<String, Object> logParams);
	
	/**
	 * 删除微博
	 * @param ids 要删除的微博的ID列表
	 * @param logParams 日志参数
	 */
	public void delWeibo(java.io.Serializable[] ids, Map<String, Object> logParams);
	
	/**
	 * 更新微博状态
	 * @param ids 要更新的微博的ID列表
	 * @param status 更新的状态
	 * @param logParams 日志参数
	 */
	public void updateWeiboStatus(java.io.Serializable[] ids, byte status, Map<String, Object> logParams);
	
	/**
	 * 查询指定微博
	 * @param id 要查询的微博的ID
	 * @return 微博对象
	 */
	public WeiboContent getWeibo(java.io.Serializable id);
	
	/**
	 * 按条件分页查询微博
	 * @param values 条件集
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示记录数
	 * @return List的第一个值为查询结果集List，第二个值为查询结果总数
	 */
	public List findWeiboByPage(Map<String, Object> values, int offset, int pageSize);
	
	/**
	 * 删除话题
	 * @param ids 要删除的话题的ID列表
	 * @param logParams 日志参数
	 */
	public void delTopic(java.io.Serializable[] ids, Map<String, Object> logParams);
	
	/**
	 * 更新话题状态
	 * @param ids 要更新的话题的ID列表
	 * @param status 更新的状态
	 * @param logParams 日志参数
	 */
	public void updateTopicStatus(java.io.Serializable[] ids, Map<String, Object> logParams);
	
	/**
	 * 按条件分页查询话题
	 * @param values 条件集
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示记录数
	 * @return List的第一个值为查询结果集List，第二个值为查询结果总数
	 */
	public List findTopicByPage(Map<String, Object> values, int offset, int pageSize);
}
