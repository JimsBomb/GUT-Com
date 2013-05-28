package org.chingo.gutcom.service;

import java.util.List;
import java.util.Map;

import org.chingo.gutcom.domain.CommonFilterWord;
import org.chingo.gutcom.domain.CommonSysconf;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.exception.GcException;

public interface SystemManager
{
	/********** 系统配置 **********/
	/**
	 * 插入/更新系统配置项
	 * @param conf 要追加的系统配置对象
	 * @param log 日志对象
	 * @return 追加的系统配置对象
	 */
	public CommonSysconf putConf(CommonSysconf conf, CommonSyslog log);
	
	/**
	 * 批量插入/更新系统配置项
	 * @param confs 要追加的系统配置对象Map
	 * @param log 日志对象
	 * @return 追加的系统配置对象列表
	 */
	public List<CommonSysconf> putConf(Map<String, String> confs, CommonSyslog log);
	
	/**
	 * 删除系统配置项
	 * @param rowKey 要删除配置项的rowKey列表
	 * @param log 日志对象
	 */
	public void delConf(List<String> rowKeys, CommonSyslog log);
	
	/**
	 * 获取所有系统配置项
	 * @return 系统配置列表，无则返回null
	 */
	public List<CommonSysconf> findAllConf();
	
	
	/********** 系统日志 **********/
	/**
	 * 追加系统日志
	 * @param log 要追加的系统日志对象
	 */
	public void putSyslog(CommonSyslog log);
	
	/**
	 * 批量删除系统日志
	 * @param rowKeys 要删除的日志的rowKey列表
	 * @param log 日志对象
	 */
	public void delSyslog(List<String> rowKeys, CommonSyslog log);
	
	/**
	 * 获取所有系统日志
	 * @return 系统日志列表，无则返回null
	 */
	public List<CommonSyslog> findAllSyslog();
	
	/**
	 * 获取日志总数
	 * @return 日志总数
	 */
	public long getSyslogTotalSize();
	
	/**
	 * 分页查询日志
	 * @param startRow 查询的起始行rowKey
	 * @param pageSize 每页显示数量
	 * @return List中第一个对象为结果列表，第二个为下一页的起始行rowKey（如果有的话）。无结果则返回null
	 */
	public List<Object> findSyslogByPage(String startRow, int pageSize);
	
	/**
	 * 按条件分页查询日志
	 * @param values 查询条件Map
	 * @param startRow 查询的起始行rowKey
	 * @param pageSize 每页显示数量
	 * @return List中第一个对象为结果列表，第二个为下一页的起始行rowKey（如果有的话）。无结果则返回null
	 */
	public List<Object> findSyslogByPage(Map<String, Object> values, String startRow, int pageSize);
	
	/********** 过滤关键词 **********/
	/**
	 * 新增/更新关键词
	 * @param word 要追加的关键词对象
	 * @param log 日志对象
	 * @return 追加的关键词对象
	 */
	public CommonFilterWord putFilterWord(CommonFilterWord word, CommonSyslog log);
	
	/**
	 * 通过文件批量导入过滤关键词
	 * @param filePath 导入文件的绝对路径
	 * @param log 日志对象
	 */
	public void putFilterWord(String filePath, CommonSyslog log) throws GcException;
	
	/**
	 * 批量删除关键词
	 * @param rowKeys 要删除的关键词rowKey列表
	 * @param log 日志对象
	 */
	public void delFilterWord(List<String> rowKeys, CommonSyslog log);
	
	/**
	 * 获取所有关键词
	 * @return 关键词列表，无则返回null
	 */
	public List<CommonFilterWord> findAllFilterWord();
	
	/**
	 * 按条件分页查询关键词
	 * @param values 查询条件Map
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 每页显示数量
	 * @return List中第一个对象为结果列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> findFilterWordByPage(Map<String, Object> values, String startRow, int pageSize);
	
}
