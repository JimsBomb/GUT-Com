package org.chingo.gutcom.common.constant;

public class WeiboConst
{
	/**
	 * 微博状态：0-未审核
	 */
	public static final byte STATUS_NOT_AUDIT = 0;
	/**
	 * 微博状态：1-已审核
	 */
	public static final byte STATUS_AUDIT = 1;
	/**
	 * 微博可见性：0-公开
	 */
	public static final byte VISIBILITY_ALL = 0;
	/**
	 * 微博可见性：1-关注可见
	 */
	public static final byte VISIBILITY_FOLLOWING_ONLY = 1;
	/**
	 * 内容格式：0-文字
	 */
	public static final byte FORMAT_TEXT = 0;
	/**
	 * 内容格式：1-图片
	 */
	public static final byte FORMAT_IMG = 1;
	/**
	 * 内容格式：2-视频
	 */
	public static final byte FORMAT_VIDEO = 2;
	/**
	 * 内容格式：3-音乐
	 */
	public static final byte FORMAT_MUSIC = 3;
	/**
	 * 微博举报状态：0-未处理
	 */
	public static final byte REPORT_NOT_DEALED = 0;
	/**
	 * 微博举报状态：1-已处理
	 */
	public static final byte REPORT_DEALED = 1;
	/**
	 * 微博类型：0-普通微博
	 */
	public static final byte TYPE_NORMAL = 0;
	/**
	 * 微博类型：1-转发微博
	 */
	public static final byte TYPE_REPOST = 1;
	/**
	 * 微博类型：2-评论微博
	 */
	public static final byte TYPE_COMM = 2;
	
	/**
	 * 话题屏蔽状态：1-已屏蔽
	 */
	public static final byte TOPIC_BLOCK = 1;
	/**
	 * 话题屏蔽状态：0-未屏蔽
	 */
	public static final byte TOPIC_NOT_BLOCK = 0;
}
