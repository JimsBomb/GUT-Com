package org.chingo.gutcom.action.api.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.chingo.gutcom.action.base.api.common.MessageBaseAction;
import org.chingo.gutcom.bean.MessageBean;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.VerifyUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;

public class MessageAction extends MessageBaseAction
{
	private String mid; // 消息ID
	private byte type = -1; // 消息类型
	private String recvuser; // 接收消息用户ID
	private String recvusername; // 接收消息用户昵称
	private String content; // 消息内容
	private int count = 20; // 单页显示数量
	private String nextrow = null; // 下一页首条记录的rowKey
	// 存放JSON响应数据
	private Map<String, Object> jsonRst = new HashMap<String, Object>();
	
	public String getMid()
	{
		return this.mid;
	}
	public void setMid(String mid)
	{
		this.mid = mid;
	}
	public byte getType()
	{
		return type;
	}
	public void setType(byte type)
	{
		this.type = type;
	}
	public String getRecvuser()
	{
		return recvuser;
	}
	public void setRecvuser(String recvuser)
	{
		this.recvuser = recvuser;
	}
	public String getRecvusername()
	{
		return recvusername;
	}
	public void setRecvusername(String recvusername)
	{
		this.recvusername = WebUtil.decode(recvusername);
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = WebUtil.decode(content);
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
	public void setJsonRst(Map<String, Object> jsonRst)
	{
		this.jsonRst = jsonRst;
	}
	
	/**
	 * 获取消息Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String list() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if(type>=0 && type<=1
				&& count>0 && count<=100)
		{
			// 获取消息
			List<Object> rst = msgMgr.listMsg(WebUtil.getUser(session).getUid(),
					type, count, nextrow);
			if(rst != null) // 消息非空时
			{
				/* 设置响应数据 */
				List<MessageBean> msgBeans = (List<MessageBean>) rst.get(0);
				jo.put("statuses", msgBeans); // 消息Bean数组
				jo.put("page_num", msgBeans.size()); // 消息Bean数量
				if(rst.size() > 1) // 存在下一页时
				{
					jo.put("nextrow", rst.get(1)); // 下一页首条记录rowKey
				}
				else // 否则置null
				{
					jo.put("nextrow", null);
				}
			}
			else // 返回null
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
	 * 显示消息内容Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String show() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if(mid!=null && !mid.isEmpty() && type>=0 && type<=1)
		{
			// 获取消息
			MessageBean msg = msgMgr.showMsg(mid, type);
			if(msg != null) // 消息存在时
			{
				jo.put("root", msg);
			}
			else // 不存在时置null
			{
				jsonRst.put("root", ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20002,
						WebUtil.getRequestAddr(request), null));
				jo.put("root", jsonRst);
			}
		}
		else // 返回参数错误信息
		{
			jsonRst.put("root", ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null));
			jo.put("root", jsonRst);
		}
		request.setAttribute("data", jo.getJSONObject("root").toString());
		return SUCCESS;
	}
	
	/**
	 * 发送消息Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String send() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if((recvuser!=null && !recvuser.isEmpty())
				|| (recvusername!=null && VerifyUtil.checkNickname(recvusername))
				&& (content!=null && VerifyUtil.checkMsgContent(content)))
		{
			/* 设置日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_MSG_SEND);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_OP_FRONT);
			log.setUserid(WebUtil.getUser(session).getUid());
			// 发送消息
			MessageBean msg = msgMgr.sendMsg(recvuser, recvusername, content, log);
			if(msg != null) // 发送成功时
			{
				jo.put("root", msg); // 设置消息Bean为响应数据
			}
			else // 返回指定对象不存在错误信息
			{
				jo.put("root", ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20002,
						WebUtil.getRequestAddr(request), null));
			}
		}
		else // 返回参数错误信息
		{
			jo.put("root", ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10008,
					WebUtil.getRequestAddr(request), null));
		}
		request.setAttribute("data", jo.getJSONObject("root").toString());
		return SUCCESS;
	}
	
	/**
	 * 删除消息Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String drop() throws Exception
	{
		jsonRst.clear(); // 清空响应数据
		JSONObject jo = new JSONObject();
		// 检查参数
		if(mid!=null && !mid.isEmpty() && type>=0 && type<=1)
		{
			String[] tmp = mid.split(","); // 分隔消息ID
			List<String> rows = new ArrayList<String>();
			/* 填充消息ID列表 */
			for(String s : tmp)
			{
				rows.add(s);
			}
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_MSG_DROP);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_OP_FRONT);
			log.setUserid(WebUtil.getUser(session).getUid());
			// 删除消息
			List<Object> rst = msgMgr.dropMsgs(rows, type, log);
			if(rst != null) // 余下消息非空时
			{
				/* 设置响应数据 */
				List<MessageBean> msgs = (List<MessageBean>) rst.get(0);
				jo.put("statuses", msgs);
				jo.put("page_num", msgs.size());
				if(rst.size() > 1) // 有下一页时
				{
					jo.put("nextrow", rst.get(1));
				}
				else
				{
					jo.put("nextrow", null);
				}
			}
			else // 否则置空
			{
				jo.put("statuses", null);
				jo.put("page_num", 0);
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
}
