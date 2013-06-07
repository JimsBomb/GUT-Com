package org.chingo.gutcom.service;

import java.util.List;
import java.util.Map;

import org.chingo.gutcom.bean.WeiboInfoBean;
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
	
	/**
	 * 检索最新公共微博
	 * @param pageSize 单页查询数量
	 * @param trimUser 简化用户字段标记
	 * @param trimSource 简化源微博字段标记
	 * @return 微博Bean列表，无则返回null
	 */
	public List<WeiboInfoBean> fetchPublicWeibo(int pageSize, byte trimUser, byte trimSource);
	
	/**
	 * 获取当前用户及其关注用户的最新微博列表
	 * @param uid 用户ID
	 * @param type 0-查询当前用户及其关注用户的微博，1-只查询关注用户的微博
	 * @param timestamp 上次更新获取时间戳
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 单页查询数量
	 * @return List中第一个对象为WeiboInfoBean列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> fetchListWeibo(String uid, int type, long timestamp, String startRow, int pageSize);
	
	/**
	 * 获取当前用户的微博列表
	 * @param uid 用户ID
	 * @param timestamp 上次更新获取时间戳
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 单页查询数量
	 * @return List中第一个对象为WeiboInfoBean列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> fetchMyWeibo(String uid, long timestamp, String startRow, int pageSize);
	
	/**
	 * 获取指定用户的微博列表，用户ID和昵称二选一，优先取用户ID的值
	 * @param uid 用户ID
	 * @param nickname 用户昵称
	 * @param timestamp 上次更新获取时间戳
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 单页查询数量
	 * @return List中第一个对象为WeiboInfoBean列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> fetchOnesWeibo(String uid, String nickname,
			long timestamp, String startRow, int pageSize);
	
	/**
	 * 获取指定话题的最新微博列表
	 * @param title 话题标题
	 * @param timestamp 上次更新获取时间戳
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 单页查询数量
	 * @return List中第一个对象为WeiboInfoBean列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> fetchTopicWeibo(String title, long timestamp,
			String startRow, int pageSize);
	
	/**
	 * 获取指定ID的微博信息
	 * @param wid 微博ID
	 * @return 微博Bean
	 */
	public WeiboInfoBean fetchSingleWeibo(String wid);
	
	/**
	 * 获取提及@当前用户的微博列表
	 * @param uid 用户ID
	 * @param timestamp 上次更新获取时间戳
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 单页查询数量
	 * @return List中第一个对象为WeiboInfoBean列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> fetchAtWeibo(String uid, long timestamp,
			String startRow, int pageSize);
	
	/**
	 * 发表一条新微博
	 * @param weibo 微博对象
	 * @param words 过滤关键词列表
	 * @param log 日志对象
	 * @return 微博Bean，发表失败则返回null
	 */
	public WeiboInfoBean postWeibo(WeiboContent weibo, Map<String, Byte> words, CommonSyslog log);
	
	/**
	 * 根据微博ID删除指定微博
	 * @param wid 要删除的微博ID
	 * @param log 日志对象
	 * @return true-删除成功，false-删除失败
	 */
	public boolean dropWeibo(String wid, CommonSyslog log);
	
	/**
	 * 举报微博
	 * @param wid 举报的微博的ID
	 * @param reason 举报理由
	 * @param log 日志对象
	 * @return true-举报成功，false-举报失败（对象不存在）
	 */
	public boolean reportWeibo(String wid, String reason, CommonSyslog log);
	
	/**
	 * 按条件分页查询微博
	 * @param uid 发起搜索的用户的ID
	 * @param values 条件集
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 每页显示记录数
	 * @return List中第一个对象为WeiboInfoBean结果列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> searchWeibo(String uid, Map<String, Object> values, String startRow, int pageSize);
	
	/**
	 * 按条件分页查询话题
	 * @param uid 发起搜索的用户的ID
	 * @param values 条件集
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 每页显示记录数
	 * @return List中第一个对象为WeiboTopicBean结果列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> searchTopic(String uid, Map<String, Object> values, String startRow, int pageSize);
}
