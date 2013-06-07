package org.chingo.gutcom.common.constant;

/**
 * 操作结果信息常量类
 * @author Chingo.Org
 *
 */
public class ResultMsg
{
	/* 账号相关操作信息 */
	public static final String ACCOUNT_LOGIN = "登录成功，稍后将转到后台首页。";
	public static final String ACCOUNT_LOGOUT = "已成功退出登录。";
	public static final String ACCOUNT_CHANGE_PWD = "已成功更改密码。";
	public static final String ACCOUNT_CHANGE_PWD_FAILED = "更改密码失败，原密码不正确。";
	
	/* 过滤关键词管理操作信息 */
	public static final String FILTER_WORD_ADD = "关键词已成功添加。";
	public static final String FILTER_WORD_DEL = "关键词已成功删除。";
	public static final String FILTER_WORD_IMPORT = "关键词已成功导入。";
	
	/* 日志管理操作信息 */
	public static final String LOG_DEL = "日志已成功删除。";
	
	/* 用户管理操作信息*/
	public static final String USER_DEL = "用户已成功删除。";
	public static final String USER_STATUS_UPDATE = "用户状态已成功更新。";
	public static final String USER_AUDIT = "用户审核已成功。";
	public static final String USER_ADD ="用户已成功添加。";
	public static final String USER_ADD_FAILED = "用户添加失败，昵称或邮箱已被使用。";
	public static final String USER_PWD_UPDATE = "用户密码已成功更新。";
	
	/* 微博管理操作信息 */
	public static final String WEIBO_DEL = "微博已成功删除。";
	public static final String WEIBO_AUDIT = "微博审核已成功。";
	public static final String TOPIC_DEL = "话题已成功删除。";
	public static final String TOPIC_STATUS_UPDATE = "话题状态已成功更新。";
	public static final String WEIBO_REPORT_DEL = "微博举报信息已成功删除。";
	public static final String WEIBO_REPORT_STATUS_UPDATE = "微博举报状态已成功更新。";
	public static final String WEIBO_REPORT_DEAL = "微博举报处理已成功。";
	
	/* 数据管理操作信息 */
	public static final String DB_BACKUP = "数据库已成功备份到backup目录下，文件名为 ";
	public static final String DB_RESTORE = "数据已成功恢复。";
	public static final String DB_DEL = "数据备份文件已成功删除。";
	public static final String DB_OP_FAILED = "数据库操作失败。请确定已将MySQL相关目录添加到系统常量中。";
}
