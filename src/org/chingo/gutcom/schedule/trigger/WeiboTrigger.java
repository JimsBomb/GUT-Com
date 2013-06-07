package org.chingo.gutcom.schedule.trigger;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

/**
 * 微博调度任务Trigger类
 * @author Chingo.Org
 *
 */
public class WeiboTrigger extends CronTriggerFactoryBean
{
	public WeiboTrigger()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("m");
		int minute = Integer.parseInt(sdf.format(new Date())); // 获取当前时间的分(minute)的值
		setCronExpression("0 " + (minute+1) + "/5 * * * ?"); // 1分钟后起每5分钟触发一次
	}
}
