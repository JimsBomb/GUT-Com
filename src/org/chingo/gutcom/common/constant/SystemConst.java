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
	 * Session令牌key值
	 */
	public static final String SESSION_TOKEN = "token";
	/**
	 * Session分页记录key值
	 */
	public static final String SESSION_PAGE = "page";
	/**
	 * Session最后获取时间戳key值
	 */
	public static final String SESSION_LAST_FETCH_TIME = "lastFetchTime";
	/**
	 * Context系统配置key值
	 */
	public static final String CONTEXT_CONF = "sysconf";
	/**
	 * Context过滤关键词key值
	 */
	public static final String CONTEXT_FILTER_WORD = "filterword";
	
	/**
	 * 未登录时的用户ID
	 */
	public static final String USER_ID_NOT_LOGIN = "-1";
	/**
	 * 访问令牌有效时长（秒）：3600*24
	 */
	public static final int ACCESS_TOKEN_EFFECTIVE_TIME = 3600 * 24;
}
