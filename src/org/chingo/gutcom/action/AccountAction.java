package org.chingo.gutcom.action;

import org.chingo.gutcom.action.base.AccountBaseAction;
import org.chingo.gutcom.common.constant.ResultMsg;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.util.SecurityUtil;
import org.chingo.gutcom.domain.CommonUser;

public class AccountAction extends AccountBaseAction
{
	private String name; // 昵称
	private String pwd; // 密码
	private String newPwd; // 新密码
	
	private String resultMsg; // 操作结果信息
	private String backTo; // 返回页面
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}
	public void setNewPwd(String newPwd)
	{
		this.newPwd = newPwd;
	}
	public String getResulMsg()
	{
		return this.resultMsg;
	}
	public String getBackTo()
	{
		return this.backTo;
	}
	
	/**
	 * 登录验证Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String login() throws Exception
	{
		CommonUser user = accMgr.getUser(name, SecurityUtil.md5(pwd));
		if(user==null || user.getUid()!=0) // 验证不通过
		{
			return LOGIN;
		}
		else // 登录成功
		{
			session.put(SystemConst.SESSION_USER, user); // 往session中存放用户信息
			return SUCCESS;
		}
	}
	
	/**
	 * 退出登录Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String logout() throws Exception
	{
		session.remove(SystemConst.SESSION_USER); // 移除session中的用户信息
		this.resultMsg = ResultMsg.ACCOUNT_LOGOUT;
		this.backTo = "/login.jsp";
		return SUCCESS;
	}
	
	/**
	 * 更新密码Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String changePwd() throws Exception
	{
		if(pwd!=null && !pwd.isEmpty()
				&& newPwd!=null && !newPwd.isEmpty()) // 参数非空时
		{
			if(!pwd.equals(newPwd)) // 新旧密码不相同时
			{
				accMgr.updatePwd(pwd, newPwd); // 更新密码
			}
		}
		this.resultMsg = ResultMsg.ACCOUNT_CHANGE_PWD;
		this.backTo = "changePwd.do";
		return SUCCESS;
	}
}
