package org.chingo.gutcom.action.api.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.chingo.gutcom.action.base.api.AccountBaseAction;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonToken;
import org.chingo.gutcom.domain.CommonUser;

public class AccountAction extends AccountBaseAction
{
	private String studentno; // 学号
	private String realname; // 真实姓名
	private String college; //所在学院
	private String major; // 所读专业
	private String classname; // 班级名字
	
	// 存放JSON响应数据
	private Map<String, Object> jsonRst = new HashMap<String, Object>();
	
	public String getStudentno()
	{
		return studentno;
	}

	public void setStudentno(String studentno)
	{
		this.studentno = studentno;
	}

	public String getRealname()
	{
		return realname;
	}

	public void setRealname(String realname)
	{
		this.realname = WebUtil.decode(realname);
	}

	public String getCollege()
	{
		return college;
	}

	public void setCollege(String college)
	{
		this.college = WebUtil.decode(college);
	}

	public String getMajor()
	{
		return major;
	}

	public void setMajor(String major)
	{
		this.major = WebUtil.decode(major);
	}

	public String getClassname()
	{
		return classname;
	}

	public void setClassname(String classname)
	{
		this.classname = WebUtil.decode(classname);
	}

	public Map<String, Object> getJsonRst()
	{
		return this.jsonRst;
	}
	
	public void setJsonRst(Map<String, Object> jsonRst)
	{
		this.jsonRst = jsonRst;
	}
	
	/**
	 * 获取当前登录用户ID Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String fetchUid() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		if(WebUtil.getUser(session) == null) // 用户未登录时，返回未登录错误数据
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20001, 
					WebUtil.getRequestAddr(request), null);
		}
		else
		{
			// 获取SESSION中的令牌对象
			CommonToken token = WebUtil.getToken(session);
			// 检查参数和令牌
			if(access_token!=null && token!=null 
					&& access_token.equals(token.getAccessToken()))
			{
				// 返回用户ID
				jsonRst.put("uid", token.getUserid());
			}
			else // 参数错误时，返回参数错误数据
			{
				if(access_token == null) // 无access_token参数
				{
					jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10006, 
							WebUtil.getRequestAddr(request), null);
				}
				else // 令牌无效/过期
				{
					jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10005, 
							WebUtil.getRequestAddr(request), null);
				}
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * 绑定（更新）学号及相关信息Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String updateStunum() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		// 检查参数
		if(studentno!=null && !studentno.isEmpty()&& realname!=null 
				&& college!=null && major!=null && classname!=null)
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_STUDENTNUM_UPDATE);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_OP_FRONT);
			log.setUserid(WebUtil.getUser(session).getUid());
			// 绑定
			CommonUser user = userMgr.updateStudentnum(WebUtil.getUser(session), 
					studentno, realname, college, major, classname, log);
			if(user != null) // 绑定成功时
			{
				session.put(SystemConst.SESSION_USER, user); // 更新SESSION中的用户信息
				jsonRst.put("result", true); // 设置响应数据
			}
			else // 否则返回错误信息
			{
				jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20101, 
						WebUtil.getRequestAddr(request), null);
			}
		}
		else // 否则返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10013, 
					WebUtil.getRequestAddr(request), new String[]{
					"studentno/realname/college/major/classname"});
		}
		return SUCCESS;
	}
	
	/**
	 * 用户注销Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String logout() throws Exception
	{
		jsonRst.clear(); //清空响应数据
		session.remove(SystemConst.SESSION_USER); // 移除SESSION中用户信息
		session.remove(SystemConst.SESSION_TOKEN); // 移除SESSION中令牌信息
		jsonRst.put("result", true); // 设置响应数据
		return SUCCESS;
	}
}
