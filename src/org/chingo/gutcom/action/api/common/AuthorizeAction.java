package org.chingo.gutcom.action.api.common;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.action.base.api.AuthorizeBaseAction;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;

public class AuthorizeAction extends AuthorizeBaseAction
{
	private String nickname; // 用户昵称
	private String email; // 邮箱地址
	private String studentnum; // 绑定学号
	private String password; // 密码
	// 存放JSON响应数据
	private Map<String, Object> jsonRst = new HashMap<String ,Object>();
	
	public String getNickname()
	{
		return nickname;
	}


	public void setNickname(String nickname)
	{
		this.nickname = WebUtil.decode(nickname);
	}


	public String getEmail()
	{
		return email;
	}


	public void setEmail(String email)
	{
		this.email = WebUtil.decode(email);
	}


	public String getStudentnum()
	{
		return studentnum;
	}


	public void setStudentnum(String studentnum)
	{
		this.studentnum = studentnum;
	}


	public String getPassword()
	{
		return password;
	}


	public void setPassword(String password)
	{
		this.password = WebUtil.decode(password);
	}

	public Map<String, Object> getJsonRst()
	{
		return this.jsonRst;
	}

	public String login() throws Exception
	{
		List<Object> rst = null;
		/* 创建日志对象 */
		CommonSyslog log = new CommonSyslog();
		log.setDateline(new Date().getTime());
		log.setDetail(SyslogConst.DETAIL_USER_LOGIN);
		log.setIp(WebUtil.getRemoteAddr(request));
		log.setType(SyslogConst.TYPE_LOGIN_FRONT);
		log.setUserid(SystemConst.USER_ID_NOT_LOGIN);
		// 验证用户登录
		rst = userMgr.checkLogin(nickname, email, studentnum, password, log);
		jsonRst.clear(); // 清空响应数据
		if(rst != null) // 登录成功时
		{
			jsonRst.put("access_token", rst.get(1)); // 令牌
			jsonRst.put("expires_in", rst.get(2)); // 令牌时效
			jsonRst.put("user", rst.get(0)); // 用户对象
			session.put(SystemConst.SESSION_USER, rst.get(0)); // 更新SESSION
		}
		else // 失败时，返回错误信息
		{
			jsonRst.put("request", request.getRequestURI());
			jsonRst.put("error_code", "20001");
			jsonRst.put("error", "Login failed.");
		}
		return SUCCESS;
	}
}
