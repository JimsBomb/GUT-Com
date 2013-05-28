package org.chingo.gutcom.common.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	/**
	 * 计算并格式化文件大小
	 * @param length 文件长度（byte）
	 * @return 格式化后的文件大小字符串
	 */
	public static String calcFileSize(long length)
	{
		String size;
		double len = length;
		int divisor = 1024;
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		
		if(len >= divisor)
		{
			len /= divisor;
			if(len >= divisor)
			{
				size = nf.format(len) + " MB";
			}
			else
			{
				size = nf.format(len) + " KB";
			}
		}
		else
		{
			size = nf.format(len) + " B";
		}
		
		
		return size;
	}
	
	/**
	 * 创建rowKey，根据Long.MAX_VALUE-CURRENT_TIME生成，以实现HBase中新记录排在前面
	 * @return 生成的rowKey
	 */
	public static String createRowKey()
	{
		return String.valueOf(Long.MAX_VALUE - new Date().getTime());
	}
}
