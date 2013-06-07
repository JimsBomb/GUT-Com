package org.chingo.gutcom.interceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.chingo.gutcom.common.constant.UserConst;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonToken;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class UserAuditCheckInterceptor extends AbstractInterceptor
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
		if(WebUtil.getUser(session).getStatus() == UserConst.STATUS_FORBIT) // 用户未审核/禁止发表时
		{
			jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_20004,
					WebUtil.getRequestAddr(request), null);
			jo.put("root", jsonRst);
		}
		else // 否则继续执行
		{
			invocation.invoke();
		}
		request.setAttribute("data", jo.getJSONObject("root").toString());
		return Action.ERROR;
	}

}
