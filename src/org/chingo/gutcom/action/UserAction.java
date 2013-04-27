package org.chingo.gutcom.action;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.action.base.UserBaseAction;
import org.chingo.gutcom.common.constant.ErrorMsg;
import org.chingo.gutcom.common.constant.ResultMsg;
import org.chingo.gutcom.common.constant.SysconfConst;
import org.chingo.gutcom.common.constant.UserConst;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.exception.GcException;

import com.opensymphony.xwork2.ActionContext;

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
	
	private long totalSize = 0; // 记录总数
	private int pageCount = 1; // 当前页码
	private int pageSize; // 总页数
	
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
	
	public long getTotalSize()
	{
		return totalSize;
	}

	public int getPageCount()
	{
		return pageCount;
	}

	public int getPageSize()
	{
		return pageSize;
	}
	
	/**
	 * 计算并设置总页数
	 */
	public void setPageSize()
	{
		int sizePerPage = Integer.parseInt(getConfigurations().get(SysconfConst.RECORDS_PER_PAGE));
		this.pageSize = (int) (this.totalSize / sizePerPage);
		if(this.totalSize % sizePerPage > 0)
		{
			this.pageSize++;
		}
	}

	public String getResultMsg()
	{
		return resultMsg;
	}

	public String getBackTo()
	{
		return backTo;
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
		if(searchMode == 1) // 搜索模式时
		{
			/* 用户昵称非空则获取 */
			if(nickname!=null && nickname.isEmpty()==false)
			{
				values.put("nickname", "%" + nickname + "%");
			}
			/* 学号非空则获取 */
			if(studentnum!=null && studentnum.isEmpty()==false)
			{
				values.put("studentnum", "%" + studentnum + "%");
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
		if (parameters.containsKey("p"))
		{
			String p = ((String[])parameters.get("p"))[0];
			try
			{
				pageCount = Integer.parseInt(p);
			}
			catch (Exception ex)
			{
				throw new GcException(ErrorMsg.INVALID_PARAM);
			}
		}
		int sizePerPage = Integer.parseInt(getConfigurations().get(SysconfConst.RECORDS_PER_PAGE));
		List rst = userMgr.findUserByPage(values, (pageCount-1)*sizePerPage, sizePerPage);
		lstUser = (List<CommonUser>) rst.get(0); // 获取结果集
		totalSize = (long) rst.get(1); // 获取结果总数
		if(totalSize == 0)
		{
			pageCount = 0;
		}
		setPageSize(); // 计算总页数

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
		Integer[] ids; // 用户ID
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
			ids = new Integer[params.length];
			for(int i=0; i<params.length; i++)
			{
				ids[i] = Integer.parseInt(params[i].toString());
			}
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		userMgr.delUser(ids, null); // 执行删除
		
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
		Integer id; // 用户ID
		byte pStatus; // 用户当前状态
		try
		{
			id = Integer.parseInt(((String[]) parameters.get("id"))[0]);
			pStatus = Byte.parseByte(((String[]) parameters.get("status"))[0]);
			pStatus = (byte) (pStatus ^ 1); // 将用户状态取反，即为新状态值
		}
		catch (Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		userMgr.updateStatus(id, pStatus, null); // 执行更新
		
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
		values.put("status", UserConst.STATUS_FORBIT); // 只查询未审核的用户
		/* 处理分页 */
		if (parameters.containsKey("p"))
		{
			String p = ((String[])parameters.get("p"))[0];
			try
			{
				pageCount = Integer.parseInt(p);
			}
			catch (Exception ex)
			{
				throw new GcException(ErrorMsg.INVALID_PARAM);
			}
		}
		int sizePerPage = Integer.parseInt(getConfigurations().get(SysconfConst.RECORDS_PER_PAGE));
		List rst = userMgr.findUserByPage(values, (pageCount-1)*sizePerPage, sizePerPage);
		lstUser = (List<CommonUser>) rst.get(0); // 获取结果集
		totalSize = (long) rst.get(1); // 获取结果总数
		if(totalSize == 0)
		{
			pageCount = 0;
		}
		setPageSize(); // 计算总页数

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
		Integer[] ids; // 用户ID
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
			ids = new Integer[params.length];
			for(int i=0; i<params.length; i++)
			{
				ids[i] = Integer.parseInt(params[i].toString());
			}
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		userMgr.updateStatus(ids, UserConst.STATUS_NORMAL, null); // 执行审核
		
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
			/* 学号为空时 */
			if(user.getStudentnum()==null || user.getStudentnum().isEmpty())
			{
				user.setStudentnum("");
			}
			userMgr.addUser(user, null); // 插入用户

			this.resultMsg = ResultMsg.USER_ADD;
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
		Integer id;
		try
		{
			String tmp = ((String[]) parameters.get("id"))[0];
			id = Integer.parseInt(tmp);
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
		Integer id; // 用户ID
		String pwd; // 新密码
		if(parameters.containsKey("id") && parameters.containsKey("pwd"))
		{
			try
			{
				String tmp = ((String[]) parameters.get("id"))[0];
				id = Integer.parseInt(tmp);

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
			userMgr.updatePassword(id, pwd, null); // 执行更新

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
