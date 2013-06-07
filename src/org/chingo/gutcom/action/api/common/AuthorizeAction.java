package org.chingo.gutcom.action.api.common;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.chingo.gutcom.action.base.api.common.AuthorizeBaseAction;
import org.chingo.gutcom.bean.UserInfoBean;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonToken;
import org.chingo.gutcom.domain.CommonUser;

public class AuthorizeAction extends AuthorizeBaseAction
{
	private String uid; // 用户ID
	private String nickname; // 用户昵称
	private String email; // 邮箱地址
	private String studentnum; // 绑定学号
	private String password; // 密码
	// 存放JSON响应数据
	private Map<String, Object> jsonRst = new HashMap<String ,Object>();
	
	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

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

	/**
	 * 登录Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String login() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		if(WebUtil.getUser(session) == null)
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_LOGIN);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_LOGIN_FRONT);
			log.setUserid(SystemConst.USER_ID_NOT_LOGIN);
			List<Object> rst = null;
			// 验证用户登录
			rst = userMgr.checkLogin(nickname, email, studentnum, password, log);
			if(rst != null) // 登录成功时
			{
				UserInfoBean user = (UserInfoBean) rst.get(0); // 用户对象
				String accessToken = (String) rst.get(1); // 令牌
				long expiresIn = (long) rst.get(2); // 令牌有效时长（秒）
				jo.put("access_token", accessToken); // 令牌
				jo.put("expires_in", expiresIn); // 令牌时效
				jo.put("user", user); // 用户对象
				session.put(SystemConst.SESSION_USER, user); // 更新用户SESSION
				CommonToken token = new CommonToken();
				token.setAccessToken(accessToken);
				// 令牌过期时间戳（毫秒）
				token.setExpiredTime(user.getLastlogin() + expiresIn * 1000);
				token.setUserid(user.getUid());
				session.put(SystemConst.SESSION_TOKEN, token); // 更新令牌SESSION
				request.setAttribute("data", jo.toString());
			}
			else // 失败时，返回错误信息
			{
				jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20102,
						WebUtil.getRequestAddr(request), null);
				jo.put("root", jsonRst);
				request.setAttribute("data", jo.getJSONObject("root").toString());
			}
		}
		else
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20103, 
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
			request.setAttribute("data", jo.getJSONObject("root").toString());
		}
		return SUCCESS;
	}
	
	/**
	 * 更新令牌Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String update() throws Exception
	{
		jsonRst.clear();
		JSONObject jo = new JSONObject();
		CommonToken token = WebUtil.getToken(session); // 获取当前用户对象
		UserInfoBean user = WebUtil.getUser(session); // 获取当前用户的令牌对象
		// 检查参数和登录
		if(uid!=null && access_token!=null 
				&& user!=null && token!=null
				&& uid.equals(user.getUid())
				&& access_token.equals(token.getAccessToken()))
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_TOKEN_UPDATE);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_OP_FRONT);
			log.setUserid(WebUtil.getUser(session).getUid());
			log.setNickname(WebUtil.getUser(session).getNickname());
			// 更新令牌
			List<Object> rst = userMgr.updateToken(uid, access_token, log);
			if(rst != null) // 更新成功时
			{
				String accessToken = (String) rst.get(0); // 令牌
				int expiresIn = (int) rst.get(1); // 有效时长（秒）
				jo.put("access_token", accessToken); // 添加令牌
				jo.put("expires_in", expiresIn); // 添加有效时长
				token.setAccessToken(accessToken);
				// 设置过期时间戳（毫秒）
				token.setExpiredTime(log.getDateline() + expiresIn * 1000);
				session.put(SystemConst.SESSION_TOKEN, token); // 更新令牌SESSION
				request.setAttribute("data", jo.toString());
			}
			else // 系统错误
			{
				jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10001,
						WebUtil.getRequestAddr(request), null);
				jo.put("root", jsonRst);
				request.setAttribute("data", jo.getJSONObject("root").toString());
			}
		}
		else // 参数错误
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008, 
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
			request.setAttribute("data", jo.getJSONObject("root").toString());
		}
		return SUCCESS;
	}
}
