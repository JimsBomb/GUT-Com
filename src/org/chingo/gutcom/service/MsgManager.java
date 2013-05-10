package org.chingo.gutcom.service;

import java.util.Map;

import org.chingo.gutcom.domain.CommonMsgRecv;

public interface MsgManager
{
	/**
	 * 向所有用户发送消息
	 * @param offset 第一个用户的索引
	 * @param size 每批次发送用户数
	 * @param msg 要发送的消息对象
	 * @param logParams 日志参数
	 * @return 是否已全部发完，false-未发完，true-已发完
	 */
	public boolean addMsg(int offset, int size, CommonMsgRecv msg, Map<String, Object> logParams);
}
