package org.chingo.gutcom.servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.chingo.gutcom.dao.impl.CommonSysconfDaoImpl;
import org.chingo.gutcom.domain.CommonSysconf;
import org.chingo.gutcom.service.SystemManager;
import org.chingo.gutcom.service.impl.SystemManagerImpl;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Servlet implementation class InitServlet
 */
@WebServlet("/InitServlet")
public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SystemManager sysMgr;
	
	public void setSysMgr(SystemManager sysMgr)
	{
		this.sysMgr = sysMgr;
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitServlet()
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException
	{
		ServletContext servletContext = config.getServletContext(); 
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext); 
		AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory(); 
		autowireCapableBeanFactory.configureBean(this, "initServlet");
		List<CommonSysconf> conf = sysMgr.findAllConf();
		Map<String, String> mapConf = new HashMap<String, String>();
		for(CommonSysconf c : conf)
		{
			mapConf.put(c.getConfname(), c.getConfvalue());
		}
		servletContext.setAttribute("sysconf", mapConf);
	}

}
