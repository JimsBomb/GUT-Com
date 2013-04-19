package org.chingo.gutcom.service;

import java.util.List;
import java.util.Map;

import org.chingo.gutcom.domain.CommonSysconf;
import org.chingo.gutcom.domain.CommonSyslog;

public interface SystemManager
{
	/********** 系统设置管理 **********/
	/**
	 * 新增设置项
	 * @param conf 要添加的设置对象
	 */
	public void addConf(CommonSysconf conf);
	
	/**
	 * 更新设置项
	 * @param conf 要更新的设置对象
	 */
	public void updateConf(CommonSysconf conf);
	
	/**
	 * 删除设置项
	 * @param conf 要删除的设置对象的ID
	 */
	public void delConf(java.io.Serializable id);
	
	/**
	 * 获取所有设置项
	 * @return 设置项集合
	 */
	public List<CommonSysconf> findAllConf();
	
	
	/********** 系统日志管理 **********/
	/**
	 * 新增系统日志项
	 * @param log 要添加的系统日志对象
	 */
	public void addSyslog(CommonSyslog log);
	
	/**
	 * 批量删除系统日志项
	 * @param ids 要删除的系统日志项的ID数组
	 */
	public void delSyslog(java.io.Serializable[] ids);
	
	/**
	 * 获取所有系统日志项
	 * @return 系统日志项集合
	 */
	public List<CommonSyslog> findAllSyslog();
	
	/**
	 * 查询日志记录总数
	 * @return 日志记录总数
	 */
	public long getSyslogTotalSize();
	
	/**
	 * 分页获取系统日志项
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示的记录数
	 * @return 查询页的记录集
	 */
	public List<CommonSyslog> findSyslogByPage(int offset, int pageSize);
	
	/**
	 * 按条件分页获取系统日志项
	 * @param values 条件参数值
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示的记录数
	 * @return List中的第一个对象为查询页的记录集，第二个对象为结果数
	 */
	public List findSyslogByPage(Map<String, Object> values, int offset, int pageSize);
	
}
