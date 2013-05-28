package org.chingo.gutcom.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 * 网络Util类
 * @author Chingo.Org
 *
 */
public class WebUtil
{
	private static final String ENCODE_CHAR = "UTF-8"; // 编码方式
	
	/**
	 * 获取客户端真实地址
	 * @param request 请求对象
	 * @return 真实地址
	 */
	public static final String getRemoteAddr(HttpServletRequest request)
	{
		String ip = request.getHeader("x-forwarded-for");
		if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getRemoteAddr();
		}
		return ip.split(",")[0];
	}
	
	/**
	 * 以UTF-8编码decode内容
	 * @param content 要decode的内容
	 * @return decode后的内容
	 */
	public static final String decode(String content)
	{
		String result = null;
		try
		{
			result = URLDecoder.decode(content, ENCODE_CHAR);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 以UTF-8编码encode内容
	 * @param content 要encode的内容
	 * @return encode后的内容
	 */
	public static final String encode(String content)
	{
		String result = null;
		try
		{
			result = URLEncoder.encode(content, ENCODE_CHAR);
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
