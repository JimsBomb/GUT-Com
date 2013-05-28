package org.chingo.gutcom.service;

import java.util.Map;

import org.chingo.gutcom.domain.CommonMsgRecv;
import org.chingo.gutcom.domain.CommonSyslog;

public interface MsgManager
{	
	/**
	 * 向所有用户发送通知消息
	 * @param msg 要发送的消息对象
	 * @param log 日志对象
	 */
	public void sendNotice(CommonMsgRecv msg, CommonSyslog log);
}
