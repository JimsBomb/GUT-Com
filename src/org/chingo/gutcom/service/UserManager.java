package org.chingo.gutcom.service;

import java.util.List;
import java.util.Map;

import org.chingo.gutcom.bean.UserInfoBean;
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
	 * @return List中第一个对象是UserInfoBean，第二个是访问令牌，第三个是令牌有效时长（秒）。若登录失败，则返回null
	 */
	public List<Object> checkLogin(String nickname, String email,
			String studentnum, String pwd, CommonSyslog log);
	
	/**
	 * 更新访问令牌
	 * @param uid 用户ID
	 * @param token 原令牌
	 * @param log 日志对象
	 * @return List中第一个对象是新令牌，第二个是有效时长（秒）。若更新失败，则返回null
	 */
	public List<Object> updateToken(String uid, String token, CommonSyslog log);
	
	/**
	 * 检查昵称/邮箱是否可用
	 * @param nickname 昵称
	 * @param email 邮箱
	 * @return List中第一个为昵称可用标记，第二个为邮箱可用标记，true-可用，false-不可用
	 */
	public List<Boolean> verifyId(String nickname, String email);
	
	/**
	 * 用户注册
	 * @param user 注册用户对象
	 * @param log 日志对象
	 * @return true-注册成功，false-注册失败（用户名/邮箱重复）
	 */
	public boolean signup(CommonUser user, CommonSyslog log);
	
	/**
	 * 绑定学号和相关信息
	 * @param user 要绑定信息的用户Bean
	 * @param studentnum 学号
	 * @param realname 真实姓名
	 * @param college 所在学院
	 * @param major 所读专业
	 * @param classname 所在班级
	 * @param log 日志对象
	 * @return 绑定成功返回用户Bean，否则（学号已被绑定）返回null
	 */
	public UserInfoBean updateStudentnum(UserInfoBean user, String studentnum, String realname,
			String college, String major, String classname, CommonSyslog log);
	
	/**
	 * 获取指定用户的信息，ID和昵称二选一，两个都存在时优先取ID
	 * @param currentUid 当前登录用户的ID
	 * @param uid 用户的ID，无则null
	 * @param nickname 昵称，无则null
	 * @return 用户信息Bean
	 */
	public UserInfoBean getUserInfo(String currentUid, String uid, String nickname);
	
	/**
	 * 用户更新信息
	 * @param uid 要更新信息的用户ID
	 * @param nickname 新昵称
	 * @param email 新邮箱
	 * @param gender 新性别
	 * @param birth 新生日
	 * @param bloodtype 新血型
	 * @param qq 新QQ
	 * @param selfintro 新自我简介
	 * @param log 日志对象
	 * @return 更新后的用户信息Bean
	 */
	public UserInfoBean updateUserInfo(String uid, String nickname, String email, byte gender,
			String birth, byte bloodtype, String qq, String selfintro, CommonSyslog log);
	
	/**
	 * 清零单项未读信息计数
	 * @param type 要清空的项目
	 * @param log 日志对象
	 * @return true-清零成功，false-清零失败
	 */
	public boolean resetNewCount(String type, CommonSyslog log);
	
	/**
	 * 根据关键字搜索用户
	 * @param keyword 关键字
	 * @param type 搜索类型
	 * @param pageSize 单页搜索数量
	 * @param startRow 查询起始行的rowKey
	 * @return List中第一个对象为UserInfoBean列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> searchUser(String keyword, byte type, byte trimUser,
			int pageSize, String startRow);
}
