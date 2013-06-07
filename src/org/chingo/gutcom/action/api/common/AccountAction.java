package org.chingo.gutcom.action.api.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.chingo.gutcom.action.base.api.common.AccountBaseAction;
import org.chingo.gutcom.bean.UserInfoBean;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonToken;

public class AccountAction extends AccountBaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -82935563654106735L;
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
		this.studentno = WebUtil.decode(studentno);
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
	
	/**
	 * 获取当前登录用户ID Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String fetchUid() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 返回用户ID
		jo.put("uid", WebUtil.getUser(session).getUid());
		request.setAttribute("data", jo.toString());
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
		JSONObject jo = new JSONObject();
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
			log.setNickname(WebUtil.getUser(session).getNickname());
			// 绑定
			UserInfoBean user = userMgr.updateStudentnum(WebUtil.getUser(session), 
					studentno, realname, college, major, classname, log);
			if(user != null) // 绑定成功时
			{
				session.put(SystemConst.SESSION_USER, user); // 更新SESSION中的用户信息
				jo.put("result", true); // 设置响应数据
				request.setAttribute("data", jo.toString());
			}
			else // 否则返回错误信息
			{
				jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20101, 
						WebUtil.getRequestAddr(request), null);
				jo.put("root", jsonRst);
				request.setAttribute("data", jo.getJSONObject("root").toString());
			}
		}
		else // 否则返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10013, 
					WebUtil.getRequestAddr(request), new String[]{
					"studentno/realname/college/major/classname"});
			jo.put("root", jsonRst);
			request.setAttribute("data", jo.getJSONObject("root").toString());
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
		session.clear(); // 清空SESSION
		JSONObject jo = new JSONObject();
		jo.put("result", true); // 设置响应数据
		request.setAttribute("data", jo.toString());
		return SUCCESS;
	}
}
