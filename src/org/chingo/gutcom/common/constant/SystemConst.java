package org.chingo.gutcom.common.constant;

/**
 * 系统常量类
 * @author Chingo.Org
 *
 */
public class SystemConst
{
	/**
	 * Session用户key值
	 */
	public static final String SESSION_USER = "user";
	/**
	 * Session分页记录key值
	 */
	public static final String SESSION_PAGE = "page";
	/**
	 * Context系统配置key值
	 */
	public static final String CONTEXT_CONF = "sysconf";
	
	/**
	 * 未登录时的用户ID
	 */
	public static final String USER_ID_NOT_LOGIN = "-1";
	/**
	 * 访问令牌有效时长（秒）：3600*24
	 */
	public static final int ACCESS_TOKEN_EFFECTIVE_TIME = 3600 * 24;
}
