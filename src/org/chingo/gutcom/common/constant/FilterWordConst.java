package org.chingo.gutcom.common.constant;

/**
 * 内容过滤常量类
 * @author Chingo.Org
 *
 */
public class FilterWordConst
{
	/**
	 * 过滤级别，0-屏蔽
	 */
	public static final byte LEVEL_SCREEN = 0;
	/**
	 * 过滤级别，1-审核
	 */
	public static final byte LEVEL_AUDIT = 1;
	/**
	 * 过滤级别，2-禁止
	 */
	public static final byte LEVEL_BAN = 2;
	/**
	 * 屏蔽替换字符串
	 */
	public static final String SCREEN_WORD = "*";
}
