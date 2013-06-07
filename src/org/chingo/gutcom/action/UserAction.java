package org.chingo.gutcom.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.action.base.UserBaseAction;
import org.chingo.gutcom.common.constant.ErrorMsg;
import org.chingo.gutcom.common.constant.ResultMsg;
import org.chingo.gutcom.common.constant.SysconfConst;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.constant.UserConst;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.exception.GcException;

/**
 * 用户Action
 * @author Chingo.Org
 *
 */
public class UserAction extends UserBaseAction
{	
	private List<CommonUser> lstUser; // 存放用户列表
	private int searchMode = 0; // 搜索模式，0-否，1-是
	private byte status = -1; // 用户状态
	private String nickname; // 昵称
	private String studentnum; //学号
	private String createTime; // 注册时间
	private CommonUser user; // 用户对象
	
	private int pageCount = 1; // 当前页数
	private String prevP; // 上一页起始行的rowKey
	private String nextP; // 下一页起始行的rowKey
	
	private String resultMsg; // 操作结果信息
	private String backTo; // 返回页面
	
	public int getSearchMode()
	{
		return searchMode;
	}

	public void setSearchMode(int searchMode)
	{
		this.searchMode = searchMode;
	}

	public byte getStatus()
	{
		return status;
	}

	public void setStatus(byte status)
	{
		this.status = status;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getStudentnum()
	{
		return studentnum;
	}

	public void setStudentnum(String studentnum)
	{
		this.studentnum = studentnum;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public CommonUser getUser()
	{
		return user;
	}

	public void setUser(CommonUser user)
	{
		this.user = user;
	}

	public List<CommonUser> getLstUser()
	{
		return this.lstUser;
	}
	
	public void setLstUser(List<CommonUser> lstUser)
	{
		this.lstUser = lstUser;
	}

	public int getPageCount()
	{
		return pageCount;
	}

	public void setPageCount(int pageCount)
	{
		this.pageCount = pageCount;
	}
	
	public String getResultMsg()
	{
		return resultMsg;
	}

	public String getBackTo()
	{
		return backTo;
	}

	public String getPrevP()
	{
		return prevP;
	}

	public void setPrevP(String prevP)
	{
		this.prevP = prevP;
	}

	public String getNextP()
	{
		return nextP;
	}

	public void setNextP(String nextP)
	{
		this.nextP = nextP;
	}

	/**
	 * 用户管理Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String mgr() throws Exception
	{
		// 存放请求参数
		Map<String, Object> values = new HashMap<String, Object>();
		String p = null;
		if(searchMode == 1) // 搜索模式时
		{
			/* 用户昵称非空则获取 */
			if(nickname!=null && nickname.isEmpty()==false)
			{
				values.put("nickname", nickname);
			}
			/* 学号非空则获取 */
			if(studentnum!=null && studentnum.isEmpty()==false)
			{
				values.put("studentnum", studentnum);
			}
			/* 注册时间非空则获取 */
			if(createTime!=null && createTime.isEmpty()==false)
			{
				long regdate;
				try
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					regdate = sdf.parse(createTime).getTime(); // 转换为时间戳
				}
				catch(Exception ex)
				{
					throw new GcException(ErrorMsg.INVALID_PARAM);
				}
				values.put("regdate", regdate);
			}
			// 用户状态值>=0（即有选择）时，存放参数
			if(status >= 0)
			{
				values.put("status", status);
			}
		}
		/* 处理分页 */
		if (parameters.containsKey("p")) // 分页时获取下一页起始行
		{
			p = ((String[])parameters.get("p"))[0];
		}
		int sizePerPage = Integer.parseInt(getConfigurations().get(SysconfConst.RECORDS_PER_PAGE));
		List<Object> rst = userMgr.findUserByPage(values, p, sizePerPage);
		if(rst != null)
		{
			lstUser = (List<CommonUser>) rst.get(0); // 获取结果集
			if(rst.size() > 1) // 有下一页时
			{
				nextP = (String) rst.get(1); // 获取并设置下一页起始行的rowKey
			}
		}
		/* SESSION分页历史处理 */
		List<String> pageKey; // 存储分页历史的首行rowKey
		if(session.containsKey(SystemConst.SESSION_PAGE)) // session中有分页历史时获取
		{
			pageKey = (List<String>) session.get(SystemConst.SESSION_PAGE);
		}
		else // 否则新建
		{
			pageKey = new ArrayList<String>();
		}
		if(pageCount > 1) // 当前不是第一页时
		{
			prevP = pageKey.get(pageCount-2); // 设置上一页首行rowKey
		}
		else // 否则清空历史
		{
			pageKey.clear();
		}
		if(lstUser!=null && lstUser.size()>0) // 当内容列表非空时
		{
			pageKey.add(lstUser.get(0).getUid()); // 将当前首行rowKey添加到历史列表中
		}
		session.put(SystemConst.SESSION_PAGE, pageKey); // 替换session中的旧分页历史

		return "mgr";
	}
	
	/**
	 * 删除用户Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String del() throws Exception
	{
		Object[] params; // 请求参数
		List<String> ids = new ArrayList<String>(); // 用户ID
		try
		{
			if(parameters.containsKey("id")) // 删除单个用户时
			{
				params = (Object[]) parameters.get("id");
			}
			else if(parameters.containsKey("checkbox")) // 勾选复选框批量删除时
			{
				params = (Object[]) parameters.get("checkbox");
			}
			else
			{
				throw new Exception();
			}
			/* 填充ID列表 */
			for(int i=0; i<params.length; i++)
			{
				ids.add(params[i].toString());
			}
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		/* 生成日志对象 */
		CommonSyslog log = new CommonSyslog();
		log.setIp(WebUtil.getRemoteAddr(request));
		log.setUserid(WebUtil.getUser(session).getUid());
		log.setNickname(WebUtil.getUser(session).getNickname());
		log.setType(SyslogConst.TYPE_OP_ADMIN);
		log.setDetail(SyslogConst.DETAIL_ADMIN_USER_DEL);
		log.setDateline(new Date().getTime());
		userMgr.delUser(ids, log); // 执行删除
		
		this.resultMsg = ResultMsg.USER_DEL; // 设置操作结果信息
		this.backTo = "usermgr.do"; // 设置返回页面
		
		return SUCCESS;
	}
	
	/**
	 * 更新单个用户的状态
	 * @return Action Result
	 * @throws Exception
	 */
	public String updateStatus() throws Exception
	{
		String id; // 用户ID
		byte pStatus; // 用户当前状态
		try
		{
			id = ((String[]) parameters.get("id"))[0];
			pStatus = Byte.parseByte(((String[]) parameters.get("status"))[0]);
			pStatus = (byte) (pStatus ^ 1); // 将用户状态取反，即为新状态值
		}
		catch (Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		/* 生成日志对象 */
		CommonSyslog log = new CommonSyslog();
		log.setIp(WebUtil.getRemoteAddr(request));
		log.setUserid(WebUtil.getUser(session).getUid());
		log.setNickname(WebUtil.getUser(session).getNickname());
		log.setType(SyslogConst.TYPE_OP_ADMIN);
		log.setDetail(SyslogConst.DETAIL_ADMIN_USER_STATUS_UPDATE);
		log.setDateline(new Date().getTime());
		userMgr.updateStatus(id, pStatus, log); // 执行更新
		
		this.resultMsg = ResultMsg.USER_STATUS_UPDATE;
		this.backTo = "usermgr.do";
		
		return SUCCESS;
	}
	
	/**
	 * 用户审核Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String auditmgr() throws Exception
	{
		// 存放请求参数
		Map<String, Object> values = new HashMap<String, Object>();
		String p = null;
		values.put("status", UserConst.STATUS_FORBIT); // 只查询未审核的用户
		/* 处理分页 */
		if (parameters.containsKey("p")) // 分页时获取下一页起始行
		{
			p = ((String[])parameters.get("p"))[0];
		}
		int sizePerPage = Integer.parseInt(getConfigurations().get(SysconfConst.RECORDS_PER_PAGE));
		List<Object> rst = userMgr.findUserByPage(values, p, sizePerPage);
		if(rst != null)
		{
			lstUser = (List<CommonUser>) rst.get(0); // 获取结果集
			if(rst.size() > 1) // 有下一页时
			{
				nextP = (String) rst.get(1); // 获取并设置下一页起始行的rowKey
			}
		}
		/* SESSION分页历史处理 */
		List<String> pageKey; // 存储分页历史的首行rowKey
		if(session.containsKey(SystemConst.SESSION_PAGE)) // session中有分页历史时获取
		{
			pageKey = (List<String>) session.get(SystemConst.SESSION_PAGE);
		}
		else // 否则新建
		{
			pageKey = new ArrayList<String>();
		}
		if(pageCount > 1) // 当前不是第一页时
		{
			prevP = pageKey.get(pageCount-2); // 设置上一页首行rowKey
		}
		else // 否则清空历史
		{
			pageKey.clear();
		}
		if(lstUser!=null && lstUser.size()>0) // 当内容列表非空时
		{
			pageKey.add(lstUser.get(0).getUid()); // 将当前首行rowKey添加到历史列表中
		}
		session.put(SystemConst.SESSION_PAGE, pageKey); // 替换session中的旧分页历史

		return "auditmgr";
	}
	
	/**
	 * 审核用户Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String audit() throws Exception
	{
		Object[] params; // 请求参数
		List<String> ids = new ArrayList<String>(); // 用户ID
		try
		{
			if(parameters.containsKey("id")) // 审核单个用户时
			{
				params = (Object[]) parameters.get("id");
			}
			else if(parameters.containsKey("checkbox")) // 勾选复选框批量审核时
			{
				params = (Object[]) parameters.get("checkbox");
			}
			else
			{
				throw new Exception();
			}
			/* 填充ID列表 */
			for(int i=0; i<params.length; i++)
			{
				ids.add(params[i].toString());
			}
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		/* 生成日志对象 */
		CommonSyslog log = new CommonSyslog();
		log.setIp(WebUtil.getRemoteAddr(request));
		log.setUserid(WebUtil.getUser(session).getUid());
		log.setNickname(WebUtil.getUser(session).getNickname());
		log.setType(SyslogConst.TYPE_OP_ADMIN);
		log.setDetail(SyslogConst.DETAIL_ADMIN_USER_AUDIT);
		log.setDateline(new Date().getTime());
		userMgr.updateStatus(ids, UserConst.STATUS_NORMAL, log); // 执行审核
		
		this.resultMsg = ResultMsg.USER_AUDIT; // 设置操作结果信息
		this.backTo = "userauditmgr.do"; // 设置返回页面
		
		return SUCCESS;
	}
	
	/**
	 * 新增用户Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String create() throws Exception
	{
		if(user != null) // 用户对象非空时
		{
			/* 参数为空时抛出异常 */
			if(user.getNickname()==null || user.getNickname().isEmpty() 
					|| user.getEmail()==null || user.getEmail().isEmpty() 
					|| user.getPassword()==null || user.getPassword().isEmpty())
			{
				throw new GcException(ErrorMsg.INVALID_PARAM);
			}
			// 检查昵称和邮箱是否可用
			List<Boolean> check = userMgr.verifyId(user.getNickname(), user.getEmail());
			if(check.get(0)==true && check.get(1)==true) // 昵称和邮箱可用时
			{
				/* 学号为空时 */
				if(user.getStudentnum()==null || user.getStudentnum().isEmpty())
				{
					user.setStudentnum("");
				}
				user.setRegdate(new Date().getTime());
				user.setRegip(WebUtil.getRemoteAddr(request));
				/* 生成日志对象 */
				CommonSyslog log = new CommonSyslog();
				log.setIp(WebUtil.getRemoteAddr(request));
				log.setUserid(WebUtil.getUser(session).getUid());
				log.setNickname(WebUtil.getUser(session).getNickname());
				log.setType(SyslogConst.TYPE_OP_ADMIN);
				log.setDetail(SyslogConst.DETAIL_ADMIN_USER_ADD);
				log.setDateline(new Date().getTime());
				userMgr.putUser(user, log); // 插入用户

				this.resultMsg = ResultMsg.USER_ADD;
			}
			this.resultMsg = ResultMsg.USER_ADD_FAILED;
			this.backTo = "usercreate.do";

			return SUCCESS;
		}
		else // 用户对象为空时，返回添加页面
		{
			return "create";
		}
	}
	
	/**
	 * 查看用户Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String show() throws Exception
	{
		String id;
		try
		{
			id = ((String[]) parameters.get("id"))[0];
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		user = userMgr.getUser(id); // 执行查询
		
		return "show";
	}
	
	/**
	 * 更改用户密码Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String changePwd() throws Exception
	{
		String id; // 用户ID
		String pwd; // 新密码
		if(parameters.containsKey("id") && parameters.containsKey("pwd"))
		{
			try
			{
				id = ((String[]) parameters.get("id"))[0];

				pwd = ((String[]) parameters.get("pwd"))[0];
				if(pwd==null || pwd.isEmpty())
				{
					throw new GcException();
				}
			}
			catch(Exception ex)
			{
				throw new GcException(ErrorMsg.INVALID_PARAM);
			}
			/* 生成日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setUserid(WebUtil.getUser(session).getUid());
			log.setNickname(WebUtil.getUser(session).getNickname());
			log.setType(SyslogConst.TYPE_OP_ADMIN);
			log.setDetail(SyslogConst.DETAIL_ADMIN_USER_PWD_UPDATE);
			log.setDateline(new Date().getTime());
			userMgr.updatePassword(id, pwd, log); // 执行更新

			this.resultMsg = ResultMsg.USER_PWD_UPDATE;
			this.backTo = "usermgr.do";

			return SUCCESS;
		}
		else // 只有ID参数时，返回更改密码页面
		{
			request.setAttribute("id", ((String[]) parameters.get("id"))[0]); // 传递ID
			return "pwd";
		}
	}
}
