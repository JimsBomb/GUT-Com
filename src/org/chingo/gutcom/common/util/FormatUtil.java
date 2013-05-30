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
	
	/**
	 * 根据年份计算生肖
	 * @param birthyear 年份
	 * @return 生肖
	 */
	public static String calcZodiac(int birthyear)
	{
		int year = birthyear - 1900;
		year %= 12;
		String rst = null;
		switch(year)
		{
		case 0:
			rst = "鼠";
			break;
		case 1:
			rst = "牛";
			break;
		case 2:
			rst = "虎";
			break;
		case 3:
			rst = "兔";
			break;
		case 4:
			rst = "龙";
			break;
		case 5:
			rst = "蛇";
			break;
		case 6:
			rst = "马";
			break;
		case 7:
			rst = "羊";
			break;
		case 8:
			rst = "猴";
			break;
		case 9:
			rst = "鸡";
			break;
		case 10:
			rst = "狗";
			break;
		case 11:
			rst = "猪";
			break;
		default:
			rst = "鼠";
			break;
		}
		return rst;
	}
	
	/**
	 * 根据月份和日期计算星座
	 * @param month 月份
	 * @param day 日期
	 * @return 星座
	 */
	public static String calcConstellation(int month, int day)
	{
		String rst = null;
		int date = month * 100 + day; // 将日期换算为xxxx的整型数值
		/* 计算星座 */
		if(date>=321 && date<=420)
		{
			rst = "白羊座";
		}
		else if(date>=421 && date<=521)
		{
			rst = "金牛座";
		}
		else if(date>=522 && date<=621)
		{
			rst = "双子座";
		}
		else if(date>=622 && date<=722)
		{
			rst = "巨蟹座";
		}
		else if(date>=723 && date<=822)
		{
			rst = "狮子座";
		}
		else if(date>=823 && date<=922)
		{
			rst = "处女座";
		}
		else if(date>=923 && date<=1023)
		{
			rst = "天秤座";
		}
		else if(date>=1024 && date<=1122)
		{
			rst = "天蝎座";
		}
		else if(date>=1123 && date<=1221)
		{
			rst = "射手座";
		}
		else if((date>=1222 && date<=1231) || (date>=101 && date<=119))
		{
			rst = "魔羯座";
		}
		else if(date>=120 && date<=218)
		{
			rst = "水瓶座";
		}
		else if(date>=219 && date<=320)
		{
			rst = "双鱼座";
		}
		return rst;	
	}
}
