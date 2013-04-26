package org.chingo.gutcom.service;

import java.util.List;
import java.util.Map;

import org.chingo.gutcom.domain.CommonUser;

public interface UserManager
{
	/**
	 * 添加新用户
	 * @param u 要添加的用户对象
	 * @param logParams 保存系统日志所需要的参数
	 */
	public void addUser(CommonUser u, Map<String, Object> logParams);
	
	/**
	 * 更新用户状态
	 * @param id 要更新状态的用户的ID（Integer）
	 * @param status 0-正常（已审核），1-禁止发表（未审核）
	 * @param logParams 保存系统日志所需要的参数
	 */
	public void updateStatus(java.io.Serializable id, byte status, Map<String, Object> logParams);
	
	/**
	 * 批量更新用户状态
	 * @param ids 要更新状态的用户的ID列表（Integer）
	 * @param status 0-正常（已审核），1-禁止发表（未审核）
	 * @param logParams 保存系统日志所需要的参数
	 */
	public void updateStatus(java.io.Serializable[] ids, byte status, Map<String, Object> logParams);
	
	/**
	 * 更新用户密码
	 * @param id 要更新密码的用户
	 * @param pwd 新密码（明文）
	 * @param logParams 保存系统日志所需要的参数
	 */
	public void updatePassword(java.io.Serializable id, String pwd, Map<String, Object> logParams);
	
	/**
	 * 删除单个用户
	 * @param id 要删除的用户的ID
	 * @param logParams 保存系统日志所需要的参数
	 */
	public void delUser(java.io.Serializable id, Map<String, Object> logParams);
	
	/**
	 * 批量删除用户
	 * @param ids 要删除的用户的ID列表（Integer）
	 * @param logParams 保存系统日志所需要的参数
	 */
	public void delUser(java.io.Serializable[] ids, Map<String, Object> logParams);
	
	/**
	 * 查询用户信息
	 * @param id 要查询的用户的ID
	 * @return 用户对象
	 */
	public CommonUser getUser(java.io.Serializable id);
	
	/**
	 * 按条件分页查询用户
	 * @param values 条件参数集
	 * @param offset 第一条记录的索引
	 * @param pageSize 单页显示记录数
	 * @return 结果List，该List第一个值为查询结果List，第二个值为查询结果总数
	 */
	public List findUserByPage(Map<String, Object> values, int offset, int pageSize);
}
