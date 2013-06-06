package org.chingo.gutcom.action.api.common;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.chingo.gutcom.action.base.api.common.RegisterBaseAction;
import org.chingo.gutcom.common.constant.SysconfConst;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.constant.UserConst;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.SecurityUtil;
import org.chingo.gutcom.common.util.VerifyUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonUser;

public class RegisterAction extends RegisterBaseAction
{
	private String nickname; // 昵称
	private String email; // 邮箱
	private String password; // 密码
	// 存放JSON响应数据
	private Map<String, Object> jsonRst = new HashMap<String, Object>();
	
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
	public String getPassword()
	{
		return this.password;
	}
	public void setPassword(String password)
	{
		this.password = WebUtil.decode(password);
	}
	
	/**
	 * 检查昵称/邮箱可用性Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String verifyId() throws Exception
	{
		jsonRst.clear();
		JSONObject jo = new JSONObject();
		if(nickname!=null || email!=null) // 有参数时
		{
			List<Boolean> rst = userMgr.verifyId(nickname, email);
			jo.put("nickname_is_legal", rst.get(0));
			jo.put("email_is_legal", rst.get(1));
			request.setAttribute("data", jo.toString());
		}
		else // 否则返回缺少参数的错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10013, 
					WebUtil.getRequestAddr(request), new String[]{"nickname/email"});
			jo.put("root", jsonRst);
			request.setAttribute("data", jo.getJSONObject("root").toString());
		}
		return SUCCESS;
	}
	
	/**
	 * 用户注册Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String signup() throws Exception
	{
//		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 参数非空且密码格式正确时
		if(nickname!=null && email!=null && password!=null 
				&& VerifyUtil.checkPwd(password))
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(String.format(SyslogConst.DETAIL_USER_REG, nickname));
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_OP_FRONT);
			log.setUserid(SystemConst.USER_ID_NOT_LOGIN);
			/* 构造用户对象 */
			CommonUser user = new CommonUser();
			user.setNickname(nickname);
			user.setEmail(email);
			user.setPassword(SecurityUtil.md5(password));
			user.setRegdate(log.getDateline());
			user.setRegip(log.getIp());
			Map<String, String> confs = WebUtil.getConfigurations(application);
			if(confs!=null && confs.containsKey(SysconfConst.USER_VERIFY))
			{
				if(confs.get(SysconfConst.USER_VERIFY).equals(SysconfConst.USER_VERIFY_ON))
				{
					user.setStatus(UserConst.STATUS_FORBIT);
				}
				else
				{
					user.setStatus(UserConst.STATUS_NORMAL);
				}
			}
			// 注册成功时
			if(userMgr.signup(user, log) == true)
			{
//				jsonRst.put("result", "true"); // 注册成功响应数据
				jo.put("result", true);
				request.setAttribute("data", jo.toString());
			}
			else // 否则返回注册失败错误数据
			{
				jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20101, 
						WebUtil.getRequestAddr(request), null);
				jo.put("root", jsonRst);
				request.setAttribute("data", jo.getJSONObject("root").toString());
			}
		}
		else // 否则返回参数错误数据
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008, 
					WebUtil.getRequestAddr(request), null);
			
			jo.put("root", jsonRst);
			request.setAttribute("data", jo.getJSONObject("root").toString());
		}
		return SUCCESS;
	}
}
