package org.chingo.gutcom.common.constant;

public class MsgConst
{
	/**
	 * 已读标记：1-已读
	 */
	public static final byte FLAG_READ = 1;
	/**
	 * 已读标记：0-未读
	 */
	public static final byte FLAG_NOT_READ = 0;
	/**
	 * 消息类型：已收到
	 */
	public static final byte TYPE_RECV = 0;
	/**
	 * 消息类新：已发送
	 */
	public static final byte TYPE_SEND = 1;
	
	/**
	 * 系统消息：微博存在非法内容被删除。
	 */
	public static final String MSG_SYS_WEIBO_ILLEGAL_DEALED = "由于您所发表的微博存在非法内容，已被管理员删除。微博内容如下：\n";
}
