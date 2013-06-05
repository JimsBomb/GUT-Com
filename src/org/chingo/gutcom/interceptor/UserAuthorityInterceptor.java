package org.chingo.gutcom.interceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

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
		JSONObject jo = new JSONObject(); // 存放JSON响应信息
		if(WebUtil.getUser(session) != null) // 用户已登录即非空时
		{
			CommonToken token = WebUtil.getToken(session); // 令牌信息
			long currentTime = new Date().getTime();
			// 令牌非空且未过期时
			if(token != null && token.getExpiredTime()<currentTime)
			{
				// 获取请求参数acess_token
				String accessToken = request.getParameter("access_token");
				// 令牌请求参数非空且与当前登录用户的令牌相同时
				if(accessToken!=null && accessToken.equals(token.getAccessToken()))
				{
					invocation.invoke(); // 继续执行
				}
				else // 返回缺少令牌参数或令牌无效错误信息
				{
					jsonRst = ErrorCodeUtil.createErrorJsonRst(accessToken==null?
							ErrorCodeUtil.CODE_10006:ErrorCodeUtil.CODE_10005,
							WebUtil.getRequestAddr(request), null);
					jo.put("root", jsonRst);
				}
			}
			else // 返回令牌无效（过期）错误信息
			{
				jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10005,
						WebUtil.getRequestAddr(request), null);
				jo.put("root", jsonRst);
			}
		}
		else // 返回用户未登录错误信息
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20001,
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
		}
		request.setAttribute("data", jo.getJSONObject("root").toString());
		return Action.ERROR;
	}

}
