package org.chingo.gutcom.service.impl;

import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.chingo.gutcom.common.util.FormatUtil;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonMsgRecv;
import org.chingo.gutcom.domain.CommonMsgSend;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.service.MsgManager;

public class MsgManagerImpl implements MsgManager
{
	private BaseDao<CommonMsgRecv> msgRecvDao; // 已发消息DAO
	private BaseDao<CommonMsgSend> msgSendDao; // 已收消息DAO
	private BaseDao<CommonUser> userDao; // 用户DAO
	private BaseDao<CommonSyslog> logDao; // 日志DAO
	
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
	
	public void setLogDao(BaseDao<CommonSyslog> logDao)
	{
		this.logDao = logDao;
	}

	@Override
	public void sendNotice(CommonMsgRecv msg, CommonSyslog log)
	{
		FilterList fl = new FilterList();
		// 排除管理员账号
		fl.addFilter(new RowFilter(CompareOp.NOT_EQUAL, new BinaryComparator(Bytes.toBytes("0"))));
		List<Result> users = userDao.findByPage("common_user", null, null, 0);
		if(users != null) // 用户列表非空时
		{
			/* 逐个发送消息 */
			for(Result r : users)
			{
				msg.setMid(FormatUtil.createRowKey()); // 创建消息rowKey
				msg.setRecvuserId(Bytes.toString(r.getRow()));
				msgRecvDao.put(msg);
			}
		}
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		logDao.put(log); // 记录日志
	}

}
