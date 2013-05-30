package org.chingo.gutcom.common.constant;

/**
 * 日志常量类
 * @author Chingo.Org
 *
 */
public class SyslogConst
{
	/**
	 * 日志类型：0-后台登录
	 */
	public static final byte TYPE_LOGIN_BACK = 0;
	/**
	 * 日志类型：1-前台登录
	 */
	public static final byte TYPE_LOGIN_FRONT = 1;
	/**
	 * 日志类型：2-后台操作
	 */
	public static final byte TYPE_OP_ADMIN = 2;
	/**
	 * 日志类型：3-前台操作
	 */
	public static final byte TYPE_OP_FRONT = 3;
	/**
	 * 日志类型：4-微博日志
	 */
	public static final byte TYPE_WEIBO = 4;
	
	/**
	 * 日志描述：登录后台。
	 */
	public static final String DETAIL_ADMIN_LOGIN = "登录后台。";
	/**
	 * 日志描述：登录后台失败。
	 */
	public static final String DETAIL_ADMIN_LOGIN_FAILED = "登录后台失败。";
	/**
	 * 日志描述：更改后台登录密码。
	 */
	public static final String DETAIL_ADMIN_PW_UPDATE = "更改后台登录密码。";
	/**
	 * 日志描述：更改后台登录密码。
	 */
	public static final String DETAIL_ADMIN_PW_UPDATE_FAILED = "更改后台登录密码失败。";
	/**
	 * 日志描述：添加过滤关键词。
	 */
	public static final String DETAIL_ADMIN_WORD_ADD = "添加过滤关键词。";
	/**
	 * 日志描述：导入过滤关键词。
	 */
	public static final String DETAIL_ADMIN_WORD_IMPORT = "导入过滤关键词。";
	/**
	 * 日志描述：删除过滤关键词。
	 */
	public static final String DETAIL_ADMIN_WORD_DEL = "删除过滤关键词。";
	/**
	 * 日志描述：发送通知消息。
	 */
	public static final String DETAIL_ADMIN_MSG_SEND = "发送通知消息。";
	/**
	 * 日志描述：更改系统设置。
	 */
	public static final String DETAIL_ADMIN_CONF_UPDATE = "更改系统设置。";
	/**
	 * 日志描述：删除系统日志。
	 */
	public static final String DETAIL_ADMIN_LOG_DEL = "删除系统日志。";
	/**
	 * 日志描述：删除微博话题。
	 */
	public static final String DETAIL_ADMIN_TOPIC_DEL = "删除微博话题。";
	/**
	 * 日志描述：更改微博话题状态。
	 */
	public static final String DETAIL_ADMIN_TOPIC_STATUS_UPDATE = "更改微博话题状态。";
	/**
	 * 日志描述：新增用户。
	 */
	public static final String DETAIL_ADMIN_USER_ADD = "新增用户。";
	/**
	 * 日志描述：删除用户。
	 */
	public static final String DETAIL_ADMIN_USER_DEL = "删除用户。";
	/**
	 * 日志描述：审核用户。
	 */
	public static final String DETAIL_ADMIN_USER_AUDIT = "审核用户。";
	/**
	 * 日志描述：更改用户状态。
	 */
	public static final String DETAIL_ADMIN_USER_STATUS_UPDATE = "更改用户状态。";
	/**
	 * 日志描述：更改用户状态失败。
	 */
	public static final String DETAIL_ADMIN_USER_STATUS_UPDATE_FAILED = "更改用户状态失败（可能是指定用户不存在）。";
	/**
	 * 日志描述：更改用户密码。
	 */
	public static final String DETAIL_ADMIN_USER_PWD_UPDATE = "更改用户密码。";
	/**
	 * 日志描述：更改用户密码失败。
	 */
	public static final String DETAIL_ADMIN_USER_PWD_UPDATE_FAILED = "更改用户密码失败（可能是指定用户不存在）。";
	/**
	 * 日志描述：删除微博。
	 */
	public static final String DETAIL_ADMIN_WEIBO_DEL = "删除微博。";
	/**
	 * 日志描述：审核微博。
	 */
	public static final String DETAIL_ADMIN_WEIBO_AUDIT = "审核微博。";
	/**
	 * 日志描述：删除微博举报。
	 */
	public static final String DETAIL_ADMIN_WEIBO_REPORT_DEL = "删除微博举报。";
	/**
	 * 日志描述：更改微博举报处理状态。
	 */
	public static final String DETAIL_ADMIN_WEIBO_REPORT_STATUS_UPDATE = "更改微博举报处理状态。";
	/**
	 * 日志描述：处理微博举报。
	 */
	public static final String DETAIL_ADMIN_WEIBO_REPORT_DEAL = "处理微博举报。";
	
	/**
	 * 日志描述：用户登录。
	 */
	public static final String DETAIL_USER_LOGIN = "用户登录。";
	/**
	 * 日志描述：用户登录失败。
	 */
	public static final String DETAIL_USER_LOGIN_FAILED = "用户登录失败。";
	/**
	 * 日志描述：更新访问令牌。
	 */
	public static final String DETAIL_USER_TOKEN_UPDATE = "更新访问令牌。";
	/**
	 * 日志描述：更新访问令牌失败。
	 */
	public static final String DETAIL_USER_TOKEN_UPDATE_FAILED = "更新访问令牌失败。";
	/**
	 * 日志描述：新用户（%s）注册。
	 */
	public static final String DETAIL_USER_REG = "新用户（%s）注册。";
	/**
	 * 日志描述：新用户注册失败。
	 */
	public static final String DETAIL_USER_REG_FAILED = "新用户注册失败。";
	/**
	 * 日志描述：绑定（更新）学号。
	 */
	public static final String DETAIL_USER_STUDENTNUM_UPDATE = "绑定（更新）学号。";
	/**
	 * 日志描述：学号绑定失败。
	 */
	public static final String DETAIL_USER_STUDENTNUM_UPDATE_FAILED = "绑定（更新）学号失败，学号已被绑定。";
	/**
	 * 日志描述：更新用户信息。
	 */
	public static final String DETAIL_USER_INFO_UPDATE = "更新用户信息。";
	/**
	 * 日志描述：更新用户信息失败。
	 */
	public static final String DETAIL_USER_INFO_UPDATE_FAILED  = "更新用户信息失败。";
}
