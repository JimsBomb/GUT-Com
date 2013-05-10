package org.chingo.gutcom.service;

import org.chingo.gutcom.domain.CommonUser;

public interface AccountManager
{
	/**
	 * 根据昵称和密码来查询用户信息
	 * @param name 昵称
	 * @param pwd 密码（已经过MD5加密）
	 * @return 用户信息对象
	 */
	public CommonUser getUser(String name, String pwd);
	
	/**
	 * 更新后台登录密码
	 * @param oldPwd 旧密码（已经过MD5加密）
	 * @param newPwd 新密码（已经过MD5加密）
	 */
	public void updatePwd(String oldPwd, String newPwd);
}
