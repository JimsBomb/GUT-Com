package org.chingo.gutcom.service;

import java.util.List;
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
	
	/**
	 * 查询消息
	 * @param uid 要查询的用户ID
	 * @param type 消息类型
	 * @param pageSize 单页查询数量
	 * @param startRow 第一条记录的rowKey，无则null
	 * @return List中第一个元素为List<CommonMsgSend/CommonMsgRecv>，第二个为下一页首行的rowKey（无则null）。无数据则返回null
	 */
	public List<Object> listMsg(String uid, byte type, int pageSize, String startRow);
}
