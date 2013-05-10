package org.chingo.gutcom.service.impl;

import java.util.List;
import java.util.Map;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonMsgRecv;
import org.chingo.gutcom.domain.CommonMsgSend;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.service.MsgManager;

public class MsgManagerImpl implements MsgManager
{
	private BaseDao<CommonMsgRecv> msgRecvDao;
	private BaseDao<CommonMsgSend> msgSendDao;
	private BaseDao<CommonUser> userDao;
	
	public void setMsgRecvDao(BaseDao<CommonMsgRecv> msgRecvDao)
	{
		this.msgRecvDao = msgRecvDao;
	}

	public void setMsgSendDao(BaseDao<CommonMsgSend> msgSendDao)
	{
		this.msgSendDao = msgSendDao;
	}

	public void setUserDao(BaseDao<CommonUser> userDao)
	{
		this.userDao = userDao;
	}

	@Override
	public boolean addMsg(int offset, int size, CommonMsgRecv msg, Map<String, Object> logParams)
	{
		String hql = "select count(u) from CommonUser u";
		int userAmount = Integer.parseInt(userDao.query(hql, null).get(0).toString()); // 查询用户总数
		List<CommonUser> users = null;
		// 查询管理员信息
		CommonUser sendUsr = (CommonUser) userDao.query("from CommonUser u where u.uid=0", null).get(0);
		hql = "from CommonUser u where u.uid<>0"; // 排除管理员帐号
		/* 批量发送 */
		if(offset < userAmount) // 第一个用户的索引小于用户总数时
		{
			users = userDao.findByPage(hql, offset, size); // 批量查询用户
			/* 为每个用户发送消息 */
			for(CommonUser u : users)
			{
				CommonMsgRecv m = new CommonMsgRecv(u, sendUsr, msg.getContent()
						, msg.getDateline(), msg.getIsread());
				msgRecvDao.save(m);
			}
			offset += size;
		}
		
		if(offset < userAmount) // 未发送完毕
		{
			return false;
		}
		else // 发送完毕
		{
			return true;
		}
	}

}
