package org.chingo.gutcom.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.chingo.gutcom.bean.MessageBean;
import org.chingo.gutcom.bean.UserInfoBean;
import org.chingo.gutcom.common.constant.MsgConst;
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

	@Override
	public List<Object> listMsg(String uid, byte type, int pageSize,
			String startRow)
	{
		FilterList fl = new FilterList();
		fl.addFilter(new PageFilter(pageSize + 1)); // 单页查询数量过滤器
		List<Result> results = null;
		if(type == MsgConst.TYPE_RECV) // 查询接收消息时
		{
			// 接收用户ID过滤器
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("recvuserid"), CompareOp.EQUAL,
					Bytes.toBytes(uid)));
			// 查询接收消息
			results = msgRecvDao.findByPage("common_msg_recv", fl, startRow, pageSize + 1);
			if(results != null) // 消息存在时
			{
				List<Object> rst = new ArrayList<Object>();
				List<MessageBean> msgBeans = new ArrayList<MessageBean>();
				int rstSize = results.size();
				if(rstSize > pageSize) // 结果数量大于需要的数量，即存在下一页时
				{
					rstSize = pageSize; // 循环判断上限设置为单页数量
				}
				Result result;
				/* 解析结果并添加到结果列表中 */
				for(int i=0; i<rstSize; i++)
				{
					result = results.get(i);
					CommonMsgRecv msg = new CommonMsgRecv();
					msg.fillByResult(result); // 填充消息字段
					MessageBean msgBean = new MessageBean(msg);
					// 查询发送用户
					Result userRst = userDao.get(msg.getSenduserId(), null, null);
					if(userRst != null) // 用户存在时
					{
						CommonUser user = new CommonUser();
						user.fillByResult(userRst); // 填充用户字段
						msgBean.setUser(new UserInfoBean(user)); // 设置发送用户Bean
					}
					msgBeans.add(msgBean); // 添加消息Bean
				}
				rst.add(msgBeans); // 添加消息Bean列表到结果列表
				if(results.size() > pageSize) // 存在更多记录时
				{
					// 添加下一页起始行的rowKey到结果列表中
					rst.add(Bytes.toString(results.get(pageSize).getRow()));
				}
				return rst; // 返回结果列表
			}
		}
		else if(type == MsgConst.TYPE_SEND) // 查询发送消息时
		{
			// 发送用户ID过滤器
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("senduserid"), CompareOp.EQUAL,
					Bytes.toBytes(uid)));
			// 查询发送消息
			results = msgRecvDao.findByPage("common_msg_send", fl, startRow, pageSize+1);
			if(results != null) // 消息存在时
			{
				List<Object> rst = new ArrayList<Object>();
				List<MessageBean> msgBeans = new ArrayList<MessageBean>();
				int rstSize = results.size();
				if(rstSize > pageSize) // 结果数量大于需要的数量，即存在下一页时
				{
					rstSize = pageSize; // 循环判断上限设置为单页数量
				}
				Result result;
				/* 解析结果并添加到结果列表中 */
				for(int i=0; i<rstSize; i++)
				{
					result = results.get(i);
					CommonMsgSend msg = new CommonMsgSend();
					msg.fillByResult(result); // 填充消息字段
					MessageBean msgBean = new MessageBean(msg);
					// 查询接收用户
					Result userRst = userDao.get(msg.getRecvuserId(), null, null);
					if(userRst != null) // 用户存在时
					{
						CommonUser user = new CommonUser();
						user.fillByResult(userRst); // 填充用户字段
						msgBean.setUser(new UserInfoBean(user)); // 设置接收用户Bean
					}
					msgBeans.add(msgBean); // 添加消息Bean
				}
				rst.add(msgBeans); // 添加消息Bean列表到结果列表
				if(results.size() > pageSize) // 存在更多记录时
				{
					// 添加下一页起始行的rowKey到结果列表中
					rst.add(Bytes.toString(results.get(pageSize).getRow()));
				}
				return rst; // 返回结果列表
			}
		}
		return null;
	}

}
