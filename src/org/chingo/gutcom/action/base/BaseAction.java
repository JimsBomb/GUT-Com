package org.chingo.gutcom.action.base;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.CookiesAware;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.chingo.gutcom.common.constant.SystemConst;

import com.opensymphony.xwork2.ActionSupport;

/**
 * ������Ӧ���������Ȼ���Ϣ�ṩ��
 * @author Chingo.Org
 *
 */
public class BaseAction extends ActionSupport implements SessionAware, CookiesAware,
ParameterAware, ApplicationAware, ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2202552841172451771L;
	protected Map session;
	protected Map cookies;
	protected Map parameters;
	protected Map application;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	private Map<String, String> configurations;
	
	public Map<String, String> getConfigurations()
	{
		return (Map<String, String>) application.get(SystemConst.CONTEXT_CONF);
	}

	@Override
	public void setSession(Map<String, Object> session)
	{
		// TODO Auto-generated method stub
		this.session = session;
	}


	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		// TODO Auto-generated method stub
		this.request = request;
		
	}


	@Override
	public void setServletResponse(HttpServletResponse response)
	{
		// TODO Auto-generated method stub
		this.response = response;
	}


	@Override
	public void setCookiesMap(Map<String, String> cookies)
	{
		// TODO Auto-generated method stub
		this.cookies = cookies;
	}


	@Override
	public void setApplication(Map<String, Object> application)
	{
		// TODO Auto-generated method stub
		this.application = application;
	}


	@Override
	public void setParameters(Map<String, String[]> parameters)
	{
		// TODO Auto-generated method stub
		this.parameters = parameters;
	}

}