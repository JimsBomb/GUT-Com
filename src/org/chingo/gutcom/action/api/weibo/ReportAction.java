package org.chingo.gutcom.action.api.weibo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.chingo.gutcom.action.base.api.weibo.ReportBaseAction;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.VerifyUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;

public class ReportAction extends ReportBaseAction
{
	private String wid; // 微博ID
	private String reason; // 举报里有
	
	private Map<String, Object> jsonRst = new HashMap<String, Object>();

	public String getWid()
	{
		return wid;
	}

	public void setWid(String wid)
	{
		this.wid = wid;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = WebUtil.decode(reason);
	}

	public Map<String, Object> getJsonRst()
	{
		return jsonRst;
	}
	
	public String post() throws Exception
	{
		JSONObject jo = new JSONObject(); // 存放JSON响应数据
		// 检查参数
		if(wid!=null && !wid.isEmpty()
				&& reason!=null && VerifyUtil.checkReportReason(reason))
		{
			/* 创建日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setDateline(new Date().getTime());
			log.setDetail(SyslogConst.DETAIL_USER_WEIBO_REPORT);
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setType(SyslogConst.TYPE_OP_FRONT);
			log.setUserid(WebUtil.getUser(session).getUid());
			// 举报成功时
			if(weiboMgr.reportWeibo(wid, reason, log) == true)
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
}
