package org.chingo.gutcom.schedule.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.struts2.interceptor.ApplicationAware;
import org.chingo.gutcom.common.constant.SysconfConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonSyslog;

/**
 * 系统调度任务类
 * @author Chingo.Org
 *
 */
public class SystemJob implements ApplicationAware
{
	private boolean isRunning = false; // 运行状态标识
	
	private BaseDao<CommonSyslog> logDao;
	
	private Map<String, Object> application;
	
	public void setLogDao(BaseDao<CommonSyslog> logDao)
	{
		this.logDao = logDao;
	}
	
	@Override
	public void setApplication(Map<String, Object> application)
	{
		this.application = application;
	}
	
	/**
	 * 删除超过存放期限的日志
	 */
	private void clearOldLogs()
	{
		if(isRunning == false) // 不正在运行时
		{
			isRunning = true; // 设置为正在运行
			// 系统设置存在时
			if(application.containsKey(SystemConst.CONTEXT_CONF))
			{
				Map<String, String> conf = WebUtil.getConfigurations(application);
				// 设置非空且包含日志存放天数时
				if(conf!=null && conf.containsKey(SysconfConst.LOG_LIFECYCLE))
				{
					String life = conf.get(SysconfConst.LOG_LIFECYCLE);
					if(life!=null && !life.equals("0")) // 日志存放天数非空且不是永久保存时
					{
						// 转换保存时间为毫秒
						long time = Integer.parseInt(life) * 24L * 3600 * 1000;
						time = new Date().getTime() - time; // 减去当前时间戳
						FilterList fl = new FilterList();
						// 过期日志过滤器
						fl.addFilter(new RowFilter(CompareOp.GREATER_OR_EQUAL,
								new BinaryComparator(Bytes.toBytes(String.valueOf(Long.MAX_VALUE-time)))));
						// 查询日志
						List<Result> results = logDao.findByPage("common_syslog", fl, null, 0);
						if(results != null) // 日志非空时
						{
							// 存放日志rowKey
							List<String> rows = new ArrayList<String>();
							for(Result rst : results)
							{
								rows.add(Bytes.toString(rst.getRow()));
							}
							logDao.delete(rows); // 删除旧日志
						}
					}
				}
			}
			isRunning = false; // 设置为停止运行
		}
	}

	
}
