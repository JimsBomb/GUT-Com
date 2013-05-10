package org.chingo.gutcom.interceptor;

import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.domain.CommonUser;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 后台权限检查拦截器类
 * @author Chingo.Org
 *
 */
public class AdminAuthorityInterceptor extends AbstractInterceptor
{

	@Override
	public String intercept(ActionInvocation invocation) throws Exception
	{
		ActionContext ac = ActionContext.getContext();
		Object obj = ac.getSession().get(SystemConst.SESSION_USER); // 获取session中的用户信息
		if(obj != null) // 非空时
		{
			CommonUser user = (CommonUser) obj;
			if(user.getUid() == 0) // 为管理员时
			{
				return invocation.invoke(); // 继续执行
			}
		}
		
		return Action.LOGIN; // 提示登录
	}

}
