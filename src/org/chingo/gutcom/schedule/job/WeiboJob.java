package org.chingo.gutcom.schedule.job;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.ApplicationAware;
import org.chingo.gutcom.bean.WeiboInfoBean;
import org.chingo.gutcom.common.WeiboCache;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.service.WeiboManager;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 微博调度任务类
 * @author Chingo.Org
 *
 */
public class WeiboJob
{
	private boolean isRunning = false; // 运行状态标识
	
	private WeiboManager weiboMgr;
	
	public void setWeiboMgr(WeiboManager weiboMgr)
	{
		this.weiboMgr = weiboMgr;
	}
	
	/**
	 * 获取最新公共微博
	 */
	private void updatePublicWeibo()
	{
		System.out.println("update public weibo");
		if(isRunning == false) // 不正在运行时
		{
			isRunning = true; // 设置为正在运行
			byte trim = 0; // 简化字段标识
			// 获取列表
			List<WeiboInfoBean> weibos = weiboMgr.fetchPublicWeibo(100, trim, trim);
			WeiboCache.weibos = weibos; // 存储到内存中
			isRunning = false; // 设置为停止运行
		}
	}
}
