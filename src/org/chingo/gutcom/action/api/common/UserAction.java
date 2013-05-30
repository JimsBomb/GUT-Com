package org.chingo.gutcom.action.api.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.chingo.gutcom.action.base.api.UserBaseAction;
import org.chingo.gutcom.bean.UserInfoBean;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.VerifyUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;

public class UserAction extends UserBaseAction
{
	private String uid; // 用户ID
	private String nickname; // 昵称
	private String email; // 邮箱
	private byte gender = -1; //性别
	private String birth; // 生日yyyy-mm-dd
	private byte bloodtype = -1; // 血型，1-A，2-B，3-AB，4-O，0-其它
	private String qq; // QQ号
	private String selfintro; // 自我简介
	private String avatar; // 头像
	// 存放JSON响应数据
	private Map<String, Object> jsonRst = new HashMap<String, Object>();

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

	public byte getGender()
	{
		return gender;
	}

	public void setGender(byte gender)
	{
		this.gender = gender;
	}

	public String getBirth()
	{
		return birth;
	}

	public void setBirth(String birth)
	{
		this.birth = WebUtil.decode(birth);
	}

	public byte getBloodtype()
	{
		return bloodtype;
	}

	public void setBloodtype(byte bloodtype)
	{
		this.bloodtype = bloodtype;
	}

	public String getQq()
	{
		return qq;
	}

	public void setQq(String qq)
	{
		this.qq = qq;
	}

	public String getSelfintro()
	{
		return selfintro;
	}

	public void setSelfintro(String selfintro)
	{
		this.selfintro = WebUtil.decode(selfintro);
	}
	
	public String getAvatar()
	{
		return this.avatar;
	}
	
	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}

	public Map<String, Object> getJsonRst()
	{
		return jsonRst;
	}

	public void setJsonRst(Map<String, Object> jsonRst)
	{
		this.jsonRst = jsonRst;
	}
	
	/**
	 * 获取用户信息Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String show() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		// 检查参数
		if((uid!=null && !uid.isEmpty()) 
				|| (nickname!=null && !nickname.isEmpty()))
		{
			// 获取用户信息
			UserInfoBean userInfo = userMgr.getUserInfo(WebUtil.getUser(session).getUid(),
					uid, nickname);
			if(userInfo != null) // 用户存在时
			{
				jsonRst.put("root", userInfo); // 响应数据
			}
		}
		else // 否则返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null);
		}
		return SUCCESS;
	}
	
	/**
	 * 获取用户各种计数Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String counts() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		// 检查参数
		if((uid!=null && !uid.isEmpty()) 
				|| (nickname!=null && !nickname.isEmpty()))
		{
			// 获取用户信息
			UserInfoBean userInfo = userMgr.getUserInfo(WebUtil.getUser(session).getUid(),
					uid, nickname);
			if(userInfo != null) // 用户存在时
			{
				/* 响应数据 */
				jsonRst.put("uid", userInfo.getUid());
				jsonRst.put("weibocnt", userInfo.getWeibocnt());
				jsonRst.put("follower", userInfo.getFollower());
				jsonRst.put("following", userInfo.getFollowing());
			}
		}
		else // 否则返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null);
		}
		return SUCCESS;
	}
	
	/**
	 * 更新用户信息Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String update() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		// 检查参数
		if((nickname!=null && VerifyUtil.checkNickname(nickname))
				|| (email!=null && VerifyUtil.checkEmail(email))
				|| (gender>=0 && gender<=2)
				|| (birth!=null && VerifyUtil.checkBirth(birth))
				|| (bloodtype>=0 && bloodtype<=4)
				|| (qq!=null && VerifyUtil.checkQq(qq))
				|| (selfintro!=null && VerifyUtil.checkSelfIntro(selfintro)))
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_INFO_UPDATE);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_OP_FRONT);
			log.setUserid(WebUtil.getUser(session).getUid());
			// 更新
			UserInfoBean userInfo = userMgr.updateUserInfo(WebUtil.getUser(session).getUid(),
					nickname, email, gender, birth, bloodtype, qq, selfintro, log);
			if(userInfo != null) // 更新成功时
			{
				jsonRst.put("root", userInfo); // 响应数据
			}
			else // 失败时，返回错误信息
			{
				jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10001, 
						WebUtil.getRequestAddr(request), null);
			}
		}
		else // 返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008, 
					WebUtil.getRequestAddr(request), null);
		}
		return SUCCESS;
	}
	
	/**
	 * 上传头像Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String upload() throws Exception
	{
		
		return SUCCESS;
	}
}
