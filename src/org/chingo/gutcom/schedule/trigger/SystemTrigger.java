package org.chingo.gutcom.schedule.trigger;

import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

/**
 * 系统调度任务Trigger类
 * @author Chingo.Org
 *
 */
public class SystemTrigger extends CronTriggerFactoryBean
{
	public SystemTrigger()
	{
		setCronExpression("0 0 3 * * ?"); // 即刻起每天03:00触发一次
	}
}
