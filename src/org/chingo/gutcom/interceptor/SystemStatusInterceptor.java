package org.chingo.gutcom.interceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.chingo.gutcom.common.constant.SysconfConst;
import org.chingo.gutcom.common.util.ErrorCodeUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSysconf;
import org.chingo.gutcom.domain.CommonToken;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SystemStatusInterceptor extends AbstractInterceptor
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
		Map<String, String> confs = WebUtil.getConfigurations(ac.getApplication());
		// Application中包含设置Map且设置Map包含系统状态项时
		if(confs!=null && confs.containsKey(SysconfConst.SERVER_STATUS))
		{
			// 系统设置中的系统状态
			String status = confs.get(SysconfConst.SERVER_STATUS);
			if(status.equals(SysconfConst.SERVER_STATUS_CLOSE)) // 系统服务状态为关闭时
			{
				JSONObject jo = new JSONObject();
				// 返回服务暂停错误信息
				jsonRst = ErrorCodeUtil.createErrorJsonRst(ErrorCodeUtil.CODE_10002,
						WebUtil.getRequestAddr(request), null);
				jo.put("root", jsonRst);
				request.setAttribute("data", jo.getJSONObject("root").toString());
				return Action.ERROR;
			}
		}
		invocation.invoke(); // 继续执行
		return null;
	}
}