package org.chingo.gutcom.action;

import java.util.Date;

import org.chingo.gutcom.action.base.AccountBaseAction;
import org.chingo.gutcom.bean.UserInfoBean;
import org.chingo.gutcom.common.constant.ResultMsg;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;
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
	public String getResultMsg()
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
		/* 生成日志对象 */
		CommonSyslog log = new CommonSyslog();
		log.setIp(WebUtil.getRemoteAddr(request));
		log.setUserid(SystemConst.USER_ID_NOT_LOGIN);
		log.setType(SyslogConst.TYPE_LOGIN_BACK);
		log.setDetail(SyslogConst.DETAIL_ADMIN_LOGIN);
		log.setDateline(new Date().getTime());
		// 查询用户
		CommonUser user = accMgr.getUser(name, pwd, log);
		if(user==null || !user.getUid().equals("0"))
		{
			return LOGIN;
		}
		else // 登录成功
		{
			session.put(SystemConst.SESSION_USER, new UserInfoBean(user)); // 往session中存放用户信息
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
		session.clear(); // session中的用户信息
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
				/* 生成日志对象 */
				CommonSyslog log = new CommonSyslog();
				log.setIp(WebUtil.getRemoteAddr(request));
				log.setUserid(WebUtil.getUser(session).getUid());
				log.setType(SyslogConst.TYPE_OP_ADMIN);
				log.setDetail(SyslogConst.DETAIL_ADMIN_PW_UPDATE);
				log.setDateline(new Date().getTime());
				if(false == accMgr.updatePwd(pwd, newPwd, log)) // 更新密码失败时
				{
					this.resultMsg = ResultMsg.ACCOUNT_CHANGE_PWD_FAILED;
				}
				else // 更新成功时
				{
					this.resultMsg = ResultMsg.ACCOUNT_CHANGE_PWD;
				}
			}
			this.backTo = "changepwd.do";
			return SUCCESS;
		}
		return "changePwd"; // 转到密码更改页面
	}
}
