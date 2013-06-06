package org.chingo.gutcom.action.api.weibo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.chingo.gutcom.action.base.api.weibo.RelationBaseAction;
import org.chingo.gutcom.bean.UserInfoBean;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.VerifyUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;

public class RelationAction extends RelationBaseAction
{
	private String uid; // 用户ID
	private String nickname; // 用户昵称
	private String remark; // 用户备注名
	
	private int count = 20; // 单页显示数量
	private String nextrow = null; // 下一页首条记录的rowKey
	private byte trim_user = 0; // 简化用户Bean标记，0-返回完整字段，1-仅返回用户ID和昵称字段
	
	private Map<String, Object> jsonRst = new HashMap<String, Object>();

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = WebUtil.decode(uid);
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = WebUtil.decode(nickname);
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = WebUtil.decode(remark);
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public String getNextrow()
	{
		return nextrow;
	}

	public void setNextrow(String nextrow)
	{
		this.nextrow = nextrow;
	}

	public Map<String, Object> getJsonRst()
	{
		return jsonRst;
	}

	public void setTrim_user(byte trim_user)
	{
		this.trim_user = trim_user;
	}
	
	/**
	 * 获取指定用户的关注列表Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String fetchFollowing() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		// 检查参数
		if((uid!=null && !uid.isEmpty())
				|| (nickname!=null && VerifyUtil.checkNickname(nickname))
				&& count>0 && count<=100
				&& trim_user>=0 && trim_user<=1)
		{
			// 存放上次查询时间戳
			long timestamp = WebUtil.getTimestamp(session, "relaFetchFolowing");
			
			// 获取关注列表
			List<Object> rst = urMgr.fetchFollowing(uid, nickname, timestamp,
					nextrow, count);
			if(rst != null) // 列表非空时
			{
				List<UserInfoBean> userBeans = (List<UserInfoBean>) rst.get(0);
				jo.put("statuses", userBeans); // 设置列表
				jo.put("page_num", userBeans.size()); // 设置列表大小
				if(rst.size() > 1) // 存在下一页时
				{
					jo.put("nextrow", rst.get(1));
				}
				else // 否则设为null
				{
					jo.put("nextrow", null);
				}
			}
			else // 否则设置为null
			{
				jo.put("statuses", null);
				jo.put("page_num", null);
				jo.put("nextrow", null);
			}
			
			request.setAttribute("data", jo.toString());
		}
		else // 返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
			request.setAttribute("data", jo.getJSONObject("root").toString());
		}
		
		return SUCCESS;
	}
	
	/**
	 * 获取指定用户的粉丝列表Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String fetchFollower() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		// 检查参数
		if((uid!=null && !uid.isEmpty())
				|| (nickname!=null && VerifyUtil.checkNickname(nickname))
				&& count>0 && count<=100
				&& trim_user>=0 && trim_user<=1)
		{
			// 存放上次查询时间戳
			long timestamp = WebUtil.getTimestamp(session, "relaFetchFolower");
			
			// 获取粉丝列表
			List<Object> rst = urMgr.fetchFollower(uid, nickname, timestamp,
					nextrow, count);
			if(rst != null) // 列表非空时
			{
				List<UserInfoBean> userBeans = (List<UserInfoBean>) rst.get(0);
				jo.put("statuses", userBeans); // 设置列表
				jo.put("page_num", userBeans.size()); // 设置列表大小
				if(rst.size() > 1) // 存在下一页时
				{
					jo.put("nextrow", rst.get(1));
				}
				else // 否则设为null
				{
					jo.put("nextrow", null);
				}
			}
			else // 否则设置为null
			{
				jo.put("statuses", null);
				jo.put("page_num", null);
				jo.put("nextrow", null);
			}
			
			request.setAttribute("data", jo.toString());
		}
		else // 返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
			request.setAttribute("data", jo.getJSONObject("root").toString());
		}
		
		return SUCCESS;
	}
	
	/**
	 * 关注指定用户Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String follow() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		// 检查参数
		if((uid!=null && !uid.isEmpty())
				|| (nickname!=null && VerifyUtil.checkNickname(nickname))
				&& (remark==null || VerifyUtil.checkRemark(remark)))
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_FOLLOW);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_OP_FRONT);
			log.setUserid(WebUtil.getUser(session).getUid());
			// 关注成功时
			if(urMgr.follow(uid, nickname, remark, log) == true)
			{
				jo.put("result", true);
				request.setAttribute("data", jo.toString());
			}
			else // 失败时返回错误信息
			{
				jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20002,
						WebUtil.getRequestAddr(request), null);
				jo.put("root", jsonRst);
				request.setAttribute("data", jo.getJSONObject("root").toString());
			}
		}
		else // 返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
			request.setAttribute("data", jo.getJSONObject("root").toString());
		}
		return SUCCESS;
	}
	
	/**
	 * 取消关注指定用户Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String drop() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		if((uid!=null && !uid.isEmpty())
				|| (nickname!=null && VerifyUtil.checkNickname(nickname)))
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_FOLLOW_DROP);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_OP_FRONT);
			log.setUserid(WebUtil.getUser(session).getUid());
			// 取消关注成功时
			if(urMgr.drop(uid, nickname, log) == true)
			{
				jo.put("result", true);
				request.setAttribute("data", jo.toString());
			}
			else // 失败时返回错误信息
			{
				jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20002,
						WebUtil.getRequestAddr(request), null);
				jo.put("root", jsonRst);
				request.setAttribute("data", jo.getJSONObject("root").toString());
			}
		}
		else // 返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
			request.setAttribute("data", jo.getJSONObject("root").toString());
		}
		return SUCCESS;
	}
	
	/**
	 * 更新指定关注用户的备注Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String remark() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		// 检查参数
		if((uid!=null && !uid.isEmpty())
				|| (nickname!=null && VerifyUtil.checkNickname(nickname))
				&& remark!=null && VerifyUtil.checkRemark(remark))
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_FOLLOW_REMARK);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_OP_FRONT);
			log.setUserid(WebUtil.getUser(session).getUid());
			// 更新备注名
			UserInfoBean userBean = urMgr.remark(uid, nickname, remark, log);
			if(userBean != null) // 更新成功时
			{
				jo.put("root", userBean); // 设置返回信息
			}
			else // 否则返回错误信息
			{
				jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20002,
						WebUtil.getRequestAddr(request), null);
				jo.put("root", jsonRst);
			}
		}
		else // 返回参数错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
		}
		request.setAttribute("data", jo.getJSONObject("root").toString());
				
		return SUCCESS;
	}
}
