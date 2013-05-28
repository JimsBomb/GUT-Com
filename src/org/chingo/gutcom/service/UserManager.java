package org.chingo.gutcom.service;

import java.util.List;
import java.util.Map;

import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonUser;

public interface UserManager
{
	/**
	 * 添加/更新用户
	 * @param u 要添加/更新的用户对象
	 * @param log 日志对象
	 * @return 追加的用户对象
	 */
	public CommonUser putUser(CommonUser user, CommonSyslog log);
	
	/**
	 * 批量添加/更新用户
	 * @param users 要添加/更新的用户对象列表
	 * @param log 日志对象
	 * @return 追加的用户对象列表
	 */
	public List<CommonUser> putUser(List<CommonUser> users, CommonSyslog log);
	
	/**
	 * 更新用户状态
	 * @param rowKey 要更新状态的用户的ID
	 * @param status 0-正常（已审核），1-禁止发表（未审核）
	 * @param log 日志对象
	 */
	public void updateStatus(String rowKey, byte status, CommonSyslog log);
	
	/**
	 * 批量更新用户状态
	 * @param rowKeys 要更新状态的用户的ID列表
	 * @param status 0-正常（已审核），1-禁止发表（未审核）
	 * @param log 日志对象
	 */
	public void updateStatus(List<String> rowKeys, byte status, CommonSyslog log);
	
	/**
	 * 更新用户密码
	 * @param rowKey 要更新密码的用户的ID
	 * @param pwd 新密码（明文）
	 * @param log 日志对象
	 */
	public void updatePassword(String rowKey, String pwd, CommonSyslog log);
	
	/**
	 * 删除单个用户
	 * @param rowKey 要删除的用户的ID
	 * @param log 日志对象
	 */
	public void delUser(String rowKey, CommonSyslog log);
	
	/**
	 * 批量删除用户
	 * @param rowKeys 要删除的用户的ID列表
	 * @param log 日志对象
	 */
	public void delUser(List<String> rowKeys, CommonSyslog log);
	
	/**
	 * 查询用户信息
	 * @param rowKey 要查询的用户的ID
	 * @return 用户对象
	 */
	public CommonUser getUser(String rowKey);
	
	/**
	 * 按条件分页查询用户
	 * @param values 条件参数集
	 * @param startRow 查询起始记录的rowKey
	 * @param pageSize 单页显示记录数
	 * @return List中第一个对象为结果列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> findUserByPage(Map<String, Object> values, String startRow, int pageSize);
	
	/**
	 * 用户登录，nickname/email/studentnum只能三选一，其余两个设置为null，多个有值时以第一个为准
	 * @param nickname 昵称
	 * @param email 邮箱
	 * @param studentnum 学号
	 * @param pwd 密码
	 * @param log 日志对象
	 * @return List中第一个对象是用户信息对象，第二个是访问令牌，第三个是令牌有效时长（秒）。若登录失败，则返回null
	 */
	public List<Object> checkLogin(String nickname, String email,
			String studentnum, String pwd, CommonSyslog log);
}
