package org.chingo.gutcom.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * API错误代码Util类
 * @author Chingo.Org
 *
 */
public class ErrorCodeUtil
{
	private static final String REQUEST = "request"; // 请求地址
	private static final String ERROR_CODE = "error_code"; // 错误代码
	private static final String ERROR = "error"; // 错误信息
	
	/* 系统级错误代码常量列表 */
	/**
	 * 代码：10001-系统错误
	 */
	public static final String CODE_10001 = "10001";
	/**
	 * 代码：10002-服务暂停
	 */
	public static final String CODE_10002 = "10002";
	/**
	 * 代码：10003-远程服务错误
	 */
	public static final String CODE_10003 = "10003";
	/**
	 * 代码：10004-IP限制不能请求该资源
	 */
	public static final String CODE_10004 = "10004";
	/**
	 * 代码：10005-access_token无效（过期）
	 */
	public static final String CODE_10005 = "10005";
	/**
	 * 代码：10006-缺少access_token参数
	 */
	public static final String CODE_10006 = "10006";
	/**
	 * 代码：10007-不支持的MediaType (%s)
	 */
	public static final String CODE_10007 = "10007";
	/**
	 * 代码：10008-参数错误，请参考API文档
	 */
	public static final String CODE_10008 = "10008";
	/**
	 * 代码：10009-任务过多，系统繁忙
	 */
	public static final String CODE_10009 = "10009";
	/**
	 * 代码：10010-任务超时
	 */
	public static final String CODE_10010 = "10010";
	/**
	 * 代码：10011-RPC错误
	 */
	public static final String CODE_10011 = "10011";
	/**
	 * 代码：10012-非法请求
	 */
	public static final String CODE_10012 = "10012";
	/**
	 * 代码：10013-缺失必选参数 (%s)，请参考API文档
	 */
	public static final String CODE_10013 = "10013";
	/**
	 * 代码：10014-参数值非法，需为 (%s)，实际为 (%s)，请参考API文档
	 */
	public static final String CODE_10014 = "10014";
	/**
	 * 代码：10015-
	 */
	public static final String CODE_10015 = "10015";
	/**
	 * 代码：10016-请求长度超过限制
	 */
	public static final String CODE_10016 = "10016";
	/**
	 * 代码：10017-接口不存在
	 */
	public static final String CODE_10017 = "10017";
	/**
	 * 代码：10018-请求的HTTP METHOD不支持，请检查是否选择了正确的POST/GET方式
	 */
	public static final String CODE_10018 = "10018";
	
	/* 服务级错误代码常量列表 */
	/**
	 * 代码：20001-用户未登录
	 */
	public static final String CODE_20001 = "20001";
	/**
	 * 代码：20002-指定对象（用户/微博/消息/收藏等）不存在
	 */
	public static final String CODE_20002 = "20002";
	/**
	 * 代码：20003-（昵称/内容等）含有非法内容
	 */
	public static final String CODE_20003 = "20003";
	/**
	 * 代码：20004-用户未审核/被禁止，无权操作
	 */
	public static final String CODE_20004 = "20004";
	/**
	 * 代码：20101-注册/绑定失败，昵称/邮箱/学号已存在
	 */
	public static final String CODE_20101 = "20101";
	/**
	 * 代码：20102-登录失败，昵称/邮箱/学号或密码错误
	 */
	public static final String CODE_20102 = "20102";
	/**
	 * 代码：20103-当前用户已登录
	 */
	public static final String CODE_20103 = "20103";
	
	/* 系统级错误信息 */
	/**
	 * 信息：10001-系统错误
	 */
	private static final String INFO_10001 = "System error.";
	/**
	 * 信息：10002-服务暂停
	 */
	private static final String INFO_10002 = "Service unavailable.";
	/**
	 * 信息：10003-远程服务错误
	 */
	private static final String INFO_10003 = "Remote service error.";
	/**
	 * 信息：10004-IP限制不能请求该资源
	 */
	private static final String INFO_10004 = "IP limit.";
	/**
	 * 信息：10005-access_token无效（过期）
	 */
	private static final String INFO_10005 = "Access_token invalid.";
	/**
	 * 信息：10006-缺少 (access_token) 参数
	 */
	private static final String INFO_10006 = "Paramter (access_token) is missing.";
	/**
	 * 信息：10007-不支持的MediaType (%s)
	 */
	private static final String INFO_10007 = "Unsupport mediatype (%s).";
	/**
	 * 信息：10008-参数错误，请参考API文档
	 */
	private static final String INFO_10008 = "Param error, see doc for more info.";
	/**
	 * 信息：10009-任务过多，系统繁忙
	 */
	private static final String INFO_10009 = "Too many pending tasks, system is busy.";
	/**
	 * 信息：10010-任务超时
	 */
	private static final String INFO_10010 = "Job expired.";
	/**
	 * 信息：10011-RPC错误
	 */
	private static final String INFO_10011 = "RPC error.";
	/**
	 * 信息：10012-非法请求
	 */
	private static final String INFO_10012 = "Illegal request.";
	/**
	 * 信息：10013-缺失必选参数 (%s)，请参考API文档
	 */
	private static final String INFO_10013 = "Miss required parameter (%s) , see doc for more info.";
	/**
	 * 信息：10014-参数值非法，需为 (%s)，实际为 (%s)，请参考API文档
	 */
	private static final String INFO_10014 = "Parameter (%s)'s value invalid, expect (%s) , but get (%s) , see doc for more info.";
	/**
	 * 信息：10015-
	 */
	private static final String INFO_10015 = "";
	/**
	 * 信息：10016-请求长度超过限制
	 */
	private static final String INFO_10016 = "Request body length over limit.";
	/**
	 * 信息：10017-接口不存在
	 */
	private static final String INFO_10017 = "Request api not found.";
	/**
	 * 信息：10018-请求的HTTP METHOD不支持，请检查是否选择了正确的POST/GET方式
	 */
	private static final String INFO_10018 = "HTTP method is not suported for this request.";
	
	/* 服务级错误信息 */
	/**
	 * 信息：20001-用户未登录
	 */
	private static final String INFO_20001 = "Not login.";
	/**
	 * 信息：20002-指定对象（用户/微博/消息/收藏等）不存在
	 */
	private static final String INFO_20002 = "Object not exists.";
	/**
	 * 信息：20003-（昵称/内容等）含有非法内容
	 */
	private static final String INFO_20003 = "Contains illegal content.";
	/**
	 * 信息：20004-用户未审核/被禁止，无权操作
	 */
	private static final String INFO_20004 = "User forbitted.";
	/**
	 * 信息：20101-注册/绑定失败，昵称/邮箱/学号已存在
	 */
	private static final String INFO_20101 = "NickName/email/studentnum already exists.";
	/**
	 * 信息：20102-登录失败，用户名或密码错误
	 */
	private static final String INFO_20102 = "Login failed.";
	/**
	 * 信息：20103-当前用户已登录
	 */
	private static final String INFO_20103 = "Already logined.";
	
	/**
	 * 生成错误JSON信息
	 * @param errorCode 错误代码
	 * @param requestUrl 请求URL
	 * @param params 附加参数
	 * @return JSON格式的错误数据
	 */
	public static final Map<String, Object> createErrorJsonRst(String errorCode,
			String requestUrl, String[] params)
	{
		// 存放JSON
		Map<String, Object> rst = new HashMap<String, Object>();
		rst.put(REQUEST, requestUrl); // 请求URL
		rst.put(ERROR_CODE, errorCode); // 错误代码
		String errorInfo = ""; // 错误信息
		/* 根据错误代码来设置错误信息 */
		switch(errorCode)
		{
		case CODE_10001:
			errorInfo = INFO_10001;
			break;
		case CODE_10002:
			errorInfo = INFO_10002;
			break;
		case CODE_10003:
			errorInfo = INFO_10003;
			break;
		case CODE_10004:
			errorInfo = INFO_10004;
			break;
		case CODE_10005:
			errorInfo = INFO_10005;
			break;
		case CODE_10006:
			errorInfo = INFO_10006;
			break;
		case CODE_10007:
			errorInfo = String.format(INFO_10007, params[0]);
			break;
		case CODE_10008:
			errorInfo = INFO_10008;
			break;
		case CODE_10009:
			errorInfo = INFO_10009;
			break;
		case CODE_10010:
			errorInfo = INFO_10010;
			break;
		case CODE_10011:
			errorInfo = INFO_10011;
			break;
		case CODE_10012:
			errorInfo = INFO_10012;
			break;
		case CODE_10013:
			errorInfo = String.format(INFO_10013, params[0]);
			break;
		case CODE_10014:
			errorInfo = String.format(INFO_10014, params[0], params[1], params[0]);
			break;
		case CODE_10015:
			errorInfo = INFO_10015;
			break;
		case CODE_10016:
			errorInfo = INFO_10016;
			break;
		case CODE_10017:
			errorInfo = INFO_10017;
			break;
		case CODE_10018:
			errorInfo = INFO_10018;
			break;
		case CODE_20001:
			errorInfo = INFO_20001;
			break;
		case CODE_20002:
			errorInfo = INFO_20002;
			break;
		case CODE_20003:
			errorInfo = INFO_20003;
			break;
		case CODE_20004:
			errorInfo = INFO_20004;
			break;
		case CODE_20101:
			errorInfo = INFO_20101;
			break;
		case CODE_20102:
			errorInfo = INFO_20102;
			break;
		case CODE_20103:
			errorInfo = INFO_20103;
			break;
		}
		rst.put(ERROR, errorInfo); // 存放错误信息
		
		return rst; // 返回错误代码JSON数据
	}
}
