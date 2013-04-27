package org.chingo.gutcom.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FormatUtil
{
	/**
	 * SimpleDateFormat对象，默认pattern为yyyy-MM-dd
	 */
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 通过日期字符串获取时间戳
	 * @param date 日期字符串，格式必须为yyyy-MM-dd
	 * @return 指定日期的时间戳
	 */
	public static long getTimeStampByDate(String date) throws Exception
	{
		try
		{
			return sdf.parse(date).getTime();
		} catch (ParseException e)
		{
			throw e;
		}
	}
}
