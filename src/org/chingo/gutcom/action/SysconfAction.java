package org.chingo.gutcom.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.chingo.gutcom.action.base.SystemBaseAction;
import org.chingo.gutcom.common.CommonConst;
import org.chingo.gutcom.common.constant.SysconfConst;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonUser;

/**
 * 系统设置操作ACTION
 * @author Chingo.Org
 *
 */
public class SysconfAction extends SystemBaseAction
{
	private String serverStatus; // 服务器状态
	private String userVerify; // 用户审核开关
	private String weiboVerify; // 微博审核开关
	private String shareVerify; // 分享审核开关
	private String shareCommentVerify; // 分享评论审核开关
	private String recordsPerPage; // 后台内容列表单页显示记录数
	private String logLifecycle; //日志保留时间
	
	private String resultMsg; // 操作结果信息
	private String backTo; // 返回页面

	public String getServerStatus()
	{
		return serverStatus;
	}

	public void setServerStatus(String serverStatus)
	{
		this.serverStatus = serverStatus;
	}

	public String getUserVerify()
	{
		return userVerify;
	}

	public void setUserVerify(String userVerify)
	{
		this.userVerify = userVerify;
	}

	public String getWeiboVerify()
	{
		return weiboVerify;
	}

	public void setWeiboVerify(String weiboVerify)
	{
		this.weiboVerify = weiboVerify;
	}

	public String getShareVerify()
	{
		return shareVerify;
	}

	public void setShareVerify(String shareVerify)
	{
		this.shareVerify = shareVerify;
	}

	public String getShareCommentVerify()
	{
		return shareCommentVerify;
	}

	public void setShareCommentVerify(String shareCommentVerify)
	{
		this.shareCommentVerify = shareCommentVerify;
	}

	public String getRecordsPerPage()
	{
		return recordsPerPage;
	}

	public void setRecordsPerPage(String recordsPerPage)
	{
		this.recordsPerPage = recordsPerPage;
	}

	public String getLogLifecycle()
	{
		return logLifecycle;
	}

	public void setLogLifecycle(String logLifecycle)
	{
		this.logLifecycle = logLifecycle;
	}
	
	public String getResultMsg()
	{
		return resultMsg;
	}

	public String getBackTo()
	{
		return backTo;
	}

	public String mgr() throws Exception
	{
		return "mgr";
	}
	
	/**
	 * 更新系统设置
	 * @return Action Result
	 * @throws Exception
	 */
	public String update() throws Exception
	{
		if(recordsPerPage.isEmpty()) // 单页显示数留空则默认设置为10
		{
			recordsPerPage = "10";
		}
		if(logLifecycle.isEmpty()) // 日志保留时间留空则默认设置为0
		{
			logLifecycle = "0";
		}
		
		/* 将更新的设置值放到Map中 */
		Map<String, String> mapConf = new HashMap<String, String>();
		mapConf.put(SysconfConst.SERVER_STATUS, serverStatus);
		mapConf.put(SysconfConst.USER_VERIFY, userVerify);
		mapConf.put(SysconfConst.WEIBO_VERIFY, weiboVerify);
//		mapConf.put(SysconfConst.SHARE_VERIFY, shareVerify);
//		mapConf.put(SysconfConst.SHARE_COMMENT_VERIFY, shareCommentVerify);
		mapConf.put(SysconfConst.RECORDS_PER_PAGE, recordsPerPage);
		mapConf.put(SysconfConst.LOG_LIFECYCLE, logLifecycle);
		
		/* 生成日志对象 */
		CommonSyslog log = new CommonSyslog();
		log.setIp(WebUtil.getRemoteAddr(request));
		log.setUserid(WebUtil.getUser(session).getUid());
		log.setType(SyslogConst.TYPE_OP_ADMIN);
		log.setDetail(SyslogConst.DETAIL_ADMIN_CONF_UPDATE);
		log.setDateline(new Date().getTime());
		sysMgr.putConf(mapConf, log); // 提交更新
		
		application.put("sysconf", mapConf); // 更新application中的设置值
		
		this.resultMsg = "系统设置已成功更新。"; // 设置操作结果信息
		this.backTo = "sysconfmgr.do"; // 设置返回页面
		
		return SUCCESS;
	}
}
