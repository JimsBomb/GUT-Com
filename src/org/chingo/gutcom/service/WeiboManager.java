package org.chingo.gutcom.service;

import java.util.List;
import java.util.Map;

import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.WeiboContent;
import org.chingo.gutcom.domain.WeiboReport;

public interface WeiboManager
{
	/**
	 * 新增/更新微博
	 * @param weibo 要插入/更新的微博对象
	 * @param log 日志对象
	 */
	public WeiboContent putWeibo(WeiboContent weibo, CommonSyslog log);
	
	/**
	 * 删除微博
	 * @param rowKeys 要删除的微博的ID列表
	 * @param log 日志对象
	 */
	public void delWeibo(List<String> rowKeys, CommonSyslog log);
	
	/**
	 * 更新微博状态
	 * @param rowKeys 要更新的微博的ID列表
	 * @param status 更新的状态
	 * @param log 日志对象
	 */
	public void updateWeiboStatus(List<String> rowKeys, byte status, CommonSyslog log);
	
	/**
	 * 查询指定微博
	 * @param rowKey 要查询的微博的ID
	 * @return 微博对象
	 */
	public WeiboContent getWeibo(String rowKey);
	
	/**
	 * 按条件分页查询微博
	 * @param values 条件集
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 每页显示记录数
	 * @return List中第一个对象为结果列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> findWeiboByPage(Map<String, Object> values, String startRow, int pageSize);
	
	/**
	 * 删除话题
	 * @param rowKeys 要删除的话题的ID列表
	 * @param log 日志对象
	 */
	public void delTopic(List<String> rowKeys, CommonSyslog log);
	
	/**
	 * 更新话题状态
	 * @param rowKeys 要更新的话题的ID列表
	 * @param status 更新的状态
	 * @param log 日志对象
	 */
	public void updateTopicStatus(List<String> rowKeys, byte status, CommonSyslog log);
	
	/**
	 * 按条件分页查询话题
	 * @param values 条件集
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 每页显示记录数
	 * @return List中第一个对象为结果列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> findTopicByPage(Map<String, Object> values, String startRow, int pageSize);
	
	/**
	 * 批量删除微博举报
	 * @param rowKeys 要删除的微博举报的ID列表
	 * @param log 日志对象
	 */
	public void delWeiboReport(List<String> rowKeys, CommonSyslog log);
	
	/**
	 * 查询指定微博举报信息
	 * @param rowKey 要查询的微博举报的ID
	 * @return 查询结果
	 */
	public WeiboReport getWeiboReport(String rowKey);
	
	/**
	 * 更新微博举报状态
	 * @param rowKeys 要更新的微博举报的ID列表
	 * @param status 更新的状态
	 * @param log 日志对象
	 */
	public void updateWeiboReportStatus(List<String> rowKeys, byte status, CommonSyslog log);
	
	/**
	 * 删除微博举报的微博，并将微博举报标记为已处理
	 * @param rowKey 要处理的微博举报的ID
	 * @param log 日志对象
	 */
	public void updateWeiboReportDeal(String rowKey, CommonSyslog log);
	
	/**
	 * 按条件分页查询微博举报
	 * @param values 查询条件集
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 每页显示记录数
	 * @return List中第一个对象为结果列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> findWeiboReportByPage(Map<String, Object> values, String startRow, int pageSize);
}
