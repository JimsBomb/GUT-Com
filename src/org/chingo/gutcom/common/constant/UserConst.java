package org.chingo.gutcom.common.constant;

public class UserConst
{
	/**
	 * 用户状态：正常（已审核）
	 */
	public static final byte STATUS_NORMAL = 1;
	/**
	 * 用户状态：禁止（未审核）
	 */
	public static final byte STATUS_FORBIT = 0;
	/**
	 * 标准头像默认地址
	 */
	public static final String AVATAR_URL = "";
	/**
	 * 大头像默认地址
	 */
	public static final String BIG_AVATAR_URL = "";
	/**
	 * 血型：0-其它
	 */
	public static final byte BLOODTYPE_OTHER = 0;
	/**
	 * 血型：1-A
	 */
	public static final byte BLOODTYPE_A = 1;
	/**
	 * 血型：2-B
	 */
	public static final byte BLOODTYPE_B = 2;
	/**
	 * 血型：3-AB
	 */
	public static final byte BLOODTYPE_AB = 3;
	/**
	 * 血型：4-O
	 */
	public static final byte BLOODTYPE_O = 4;
	/**
	 * 未读信息类型：新粉丝
	 */
	public static final String CNT_NEWFOLLOWER = "follower";
	/**
	 * 未读信息类型：新消息
	 */
	public static final String CNT_NEWMSG = "msg";
	/**
	 * 未读信息类型：新@提到
	 */
	public static final String CNT_NEWAT = "at";
	/**
	 * 未读信息类型：新评论
	 */
	public static final String CNT_NEWCOMMENT = "cmt";
	/**
	 * 搜索用户依据：0-按昵称
	 */
	public static final byte SEARCH_TYPE_NICKNAME = 0;
	/**
	 * 搜索用户依据：1-按专业
	 */
	public static final byte SEARCH_TYPE_MAJOR = 1;
	/**
	 * 关注标识：1-已关注
	 */
	public static final byte IS_FOLLOWED_YES = 1;
	/**
	 * 关注标识：0-未关注
	 */
	public static final byte IS_FOLLOWED_NO = 0;
	/**
	 * 系统（管理员）ID
	 */
	public static final String SYSTEM_ID = "0";
}
