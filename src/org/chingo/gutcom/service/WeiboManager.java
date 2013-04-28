package org.chingo.gutcom.service;

import java.util.List;
import java.util.Map;

import org.chingo.gutcom.domain.WeiboContent;
import org.chingo.gutcom.domain.WeiboReport;

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
	 * @param ids 要删除的微博的ID列表(Long)
	 * @param logParams 日志参数
	 */
	public void delWeibo(java.io.Serializable[] ids, Map<String, Object> logParams);
	
	/**
	 * 更新微博状态
	 * @param ids 要更新的微博的ID列表(Long)
	 * @param status 更新的状态
	 * @param logParams 日志参数
	 */
	public void updateWeiboStatus(java.io.Serializable[] ids, byte status, Map<String, Object> logParams);
	
	/**
	 * 查询指定微博
	 * @param id 要查询的微博的ID(Long)
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
	 * @param ids 要删除的话题的ID列表(Long)
	 * @param logParams 日志参数
	 */
	public void delTopic(java.io.Serializable[] ids, Map<String, Object> logParams);
	
	/**
	 * 更新话题状态
	 * @param ids 要更新的话题的ID列表(Long)
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
	
	/**
	 * 批量删除微博举报
	 * @param ids 要删除的微博举报的ID列表(Long)
	 * @param logParams 日志参数
	 */
	public void delWeiboReport(java.io.Serializable[] ids, Map<String, Object> logParams);
	
	/**
	 * 查询指定微博举报信息
	 * @param id 要查询的微博举报的ID(Long)
	 * @return 查询结果
	 */
	public WeiboReport getWeiboReport(java.io.Serializable id);
	
	/**
	 * 更新微博举报状态
	 * @param ids 要更新的微博举报的ID列表(Long)
	 * @param status 更新的状态
	 * @param logParams 日志参数
	 */
	public void updateWeiboReportStatus(java.io.Serializable[] ids, Map<String, Object> logParams);
	
	/**
	 * 删除微博举报的微博，并将微博举报标记为已处理
	 * @param id 要处理的微博举报的ID
	 * @param logParams 日志参数
	 */
	public void updateWeiboReportDeal(java.io.Serializable id, Map<String, Object> logParams);
	
	/**
	 * 按条件分页查询微博举报
	 * @param values 查询条件集
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示记录数
	 * @return 返回List的第一个值为查询结果集List，第二值为查询结果总数
	 */
	public List findWeiboReportByPage(Map<String, Object> values, int offset, int pageSize);
}
