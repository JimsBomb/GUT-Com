package org.chingo.gutcom.interceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonToken;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class UserAuthorityInterceptor extends AbstractInterceptor
{
	// 存放JSON响应数据
	private Map<String, Object> jsonRst = new HashMap<String, Object>();
	
	public Map<String, Object> getJsonRst()
	{
		return jsonRst;
	}

	public void setJsonRst(Map<String, Object> jsonRst)
	{
		this.jsonRst = jsonRst;
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception
	{
		ActionContext ac = invocation.getInvocationContext();
		HttpServletRequest request = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);
		Map<String, Object> session = ac.getSession();
		if(WebUtil.getUser(session) != null) // 用户已登录即非空时
		{
			CommonToken token = WebUtil.getToken(session); // 令牌信息
			long currentTime = new Date().getTime();
			// 令牌非空且未过期时
			if(token != null && token.getExpiredTime()<currentTime)
			{
				invocation.invoke(); // 继续执行
			}
			else // 返回令牌无效（过期）错误信息
			{
				jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10005,
						WebUtil.getRequestAddr(request), null);
			}
		}
		else // 返回用户未登录错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20001,
					WebUtil.getRequestAddr(request), null);
		}
		return Action.ERROR;
	}

}
