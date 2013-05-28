package org.chingo.gutcom.service;

import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonUser;

public interface AccountManager
{
	/**
	 * 根据昵称和密码来查询用户信息
	 * @param name 昵称
	 * @param pwd 密码
	 * @param log 日志对象
	 * @return 用户信息对象
	 */
	public CommonUser getUser(String name, String pwd, CommonSyslog log);
	
	/**
	 * 更新后台登录密码
	 * @param oldPwd 旧密码
	 * @param newPwd 新密码
	 * @param log 日志对象
	 * @return 更改成功返回true，否则返回false
	 */
	public boolean updatePwd(String oldPwd, String newPwd, CommonSyslog log);
}
