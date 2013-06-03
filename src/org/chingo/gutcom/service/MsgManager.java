package org.chingo.gutcom.service;

import java.util.List;
import java.util.Map;

import org.chingo.gutcom.bean.MessageBean;
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
	 * 查询消息列表
	 * @param uid 要查询的用户ID
	 * @param type 消息类型
	 * @param pageSize 单页查询数量
	 * @param startRow 第一条记录的rowKey，无则null
	 * @return List中第一个元素为List<MessageBean>，第二个为下一页首行的rowKey（无则null）。无数据则返回null
	 */
	public List<Object> listMsg(String uid, byte type, int pageSize, String startRow);
	
	/**
	 * 查询指定消息
	 * @param mid 消息ID
	 * @param type 消息类型
	 * @return 消息Bean，无则返回null
	 */
	public MessageBean showMsg(String mid, byte type);
	
	/**
	 * 发送消息，uid和nickname二选一，两者都有值时优先取uid
	 * @param uid 目标用户ID
	 * @param nickname 目标用户昵称
	 * @param content 消息内容
	 * @param log 日志对象
	 * @return 消息Bean
	 */
	public MessageBean sendMsg(String uid, String nickname, String content, CommonSyslog log);
	
	/**
	 * 批量删除消息
	 * @param rows 要删除的消息的ID列表
	 * @param type 消息类型
	 * @param log 日志对象
	 * @return List中第一个元素为余下消息List<MessageBean>，第二个为下一页首行的rowKey（无则null）。无数据则返回null
	 */
	public List<Object> dropMsgs(List<String> rows, byte type, CommonSyslog log);
}
