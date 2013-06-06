package org.chingo.gutcom.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.chingo.gutcom.bean.UserInfoBean;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.domain.CommonToken;
import org.chingo.gutcom.domain.CommonUser;

/**
 * 网络Util类
 * @author Chingo.Org
 *
 */
public class WebUtil
{
	private static final String ENCODE_CHAR = "UTF-8"; // 编码方式
	
	/**
	 * 返回SESSION中当前用户Bean
	 * @param session Session映射Map
	 * @return 当前用户对象，无则返回null
	 */
	public static final UserInfoBean getUser(Map session)
	{
		if(session.containsKey(SystemConst.SESSION_USER))
		{
			return (UserInfoBean) session.get(SystemConst.SESSION_USER);
		}
		return null;
	}
	
	/**
	 * 返回SESSION中当前用户的令牌对象
	 * @param session Session映射Map
	 * @return 当前用户的令牌对象，无则返回null
	 */
	public static final CommonToken getToken(Map session)
	{
		if(session.containsKey(SystemConst.SESSION_TOKEN))
		{
			return (CommonToken) session.get(SystemConst.SESSION_TOKEN);
		}
		return null;
	}
	
	/**
	 * 获取session中指定key的最后查询时间戳
	 * @param session Session映射Map
	 * @param key 最后查询时间戳的项目key
	 * @return
	 */
	public static final long getTimestamp(Map session, String key)
	{
		long timestamp; // 存放上次查询时间戳
		/* SESSION中存在上次查询时间戳时取出，否则新建 */
		if(session.containsKey(SystemConst.SESSION_LAST_FETCH_TIME))
		{
			Map<String, Long> lastTime = 
					(Map<String, Long>) session.get(SystemConst.SESSION_LAST_FETCH_TIME);
			if(lastTime.containsKey(key))
			{
				timestamp = lastTime.get(key);
			}
			else
			{
				timestamp = new Date().getTime();
				lastTime.put(key, timestamp);
			}
		}
		else
		{
			Map<String, Long> lastTime = new HashMap<String, Long>();
			timestamp = new Date().getTime();
			lastTime.put(key, timestamp);
		}
		
		return timestamp;
	}
	
	/**
	 * 返回Application中的系统设置Map
	 * @param application Application映射Map
	 * @return 系统设置Map，无则返回null
	 */
	public static final Map<String, String> getConfigurations(Map application)
	{
		if(application.containsKey(SystemConst.CONTEXT_CONF))
		{
			return (Map<String, String>) application.get(SystemConst.CONTEXT_CONF);
		}
		return null;
	}
	
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
	 * 获取请求地址
	 * @param request 请求对象
	 * @return 请求绝对路径
	 */
	public static final String getRequestAddr(HttpServletRequest request)
	{
		return request.getRequestURI();
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
