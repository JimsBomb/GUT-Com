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
import org.chingo.gutcom.common.constant.SyslogConst;
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
			results = msgSendDao.findByPage("common_msg_send", fl, startRow, pageSize+1);
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

	@Override
	public MessageBean showMsg(String mid, byte type)
	{
		Result result = null;
		if(type == MsgConst.TYPE_RECV) // 查询接收消息时
		{
			// 查询消息
			result = msgRecvDao.get(mid, null, null);
			if(result != null) // 消息非空时
			{
				CommonMsgRecv msg = new CommonMsgRecv();
				msg.fillByResult(result); // 填充消息字段
				MessageBean msgBean = new MessageBean(msg); // 创建消息Bean并填充字段
				// 查询发送用户
				Result userRst = userDao.get(msg.getSenduserId(), null, null);
				if(userRst != null) // 用户存在时
				{
					CommonUser user = new CommonUser();
					user.fillByResult(userRst); // 填充用户字段
					msgBean.setUser(new UserInfoBean(user)); // 设置发送用户Bean
				}
				return msgBean; // 返回消息Bean
			}
		}
		else if(type == MsgConst.TYPE_SEND) // 查询发送消息时
		{
			// 查询消息
			result = msgSendDao.get(mid, null, null);
			if(result != null) // 消息存在时
			{
				CommonMsgSend msg = new CommonMsgSend();
				msg.fillByResult(result); // 填充消息字段
				MessageBean msgBean = new MessageBean(msg); // 创建消息Bean并填充字段
				// 查询接收用户
				Result userRst = userDao.get(msg.getRecvuserId(), null, null);
				if(userRst != null) // 用户存在时
				{
					CommonUser user = new CommonUser();
					user.fillByResult(userRst); // 填充用户字段
					msgBean.setUser(new UserInfoBean(user)); // 设置接收用户Bean
				}
				return msgBean; // 返回消息Bean
			}
		}
		
		return null;
	}

	@Override
	public MessageBean sendMsg(String uid, String nickname, String content,
			CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		FilterList fl = new FilterList();
		fl.addFilter(new PageFilter(1L)); // 只查询一条记录
		if(uid != null) // 用户ID非空时
		{
			// 用户ID过滤器
			fl.addFilter(new RowFilter(CompareOp.EQUAL, 
					new BinaryComparator(Bytes.toBytes(uid))));
		}
		else if(nickname != null) // 否则昵称非空时
		{
			// 昵称过滤器
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("nickname"), CompareOp.EQUAL,
					Bytes.toBytes(nickname)));
		}
		// 查询接收用户
		List<Result> results = userDao.findByPage("common_user", fl, null, 0);
		if(results != null) // 用户存在时
		{
			Result result = results.get(0);
			CommonUser user = new CommonUser();
			user.fillByResult(result); // 填充用户字段
			/* 创建接收消息对象 */
			CommonMsgRecv msgRecv = new CommonMsgRecv();
			msgRecv.setMid(FormatUtil.createRowKey());
			msgRecv.setContent(content);
			msgRecv.setDateline(log.getDateline());
			msgRecv.setIsread(MsgConst.FLAG_NOT_READ);
			msgRecv.setRecvuserId(user.getUid());
			msgRecv.setSenduserId(log.getUserid());
			msgRecvDao.put(msgRecv); // 追加数据
			/* 创建发送消息对象 */
			CommonMsgSend msgSend = new CommonMsgSend();
			msgSend.setMid(FormatUtil.createRowKey());
			msgSend.setContent(content);
			msgSend.setDateline(log.getDateline());
			msgSend.setRecvuserId(user.getUid());
			msgSend.setSenduserId(log.getUserid());
			msgSendDao.put(msgSend);
			user.setNewmsg(user.getNewmsg() + 1);
			userDao.put(user); // 追加数据
			// 格式化并设置日志描述
			log.setDetail(String.format(SyslogConst.DETAIL_USER_MSG_SEND, user.getNickname()));
			logDao.put(log); // 记录日志
			// 创建消息Bean并填充字段
			MessageBean msgBean = new MessageBean(msgSend);
			msgBean.setUser(new UserInfoBean(user)); // 设置接收消息用户Bean
			return msgBean; // 返回消息Bean
		}
		log.setDetail(String.format(SyslogConst.DETAIL_USER_MSG_SEND_FAILED,
				uid==null?nickname:"ID:"+uid)); // 格式化并设置失败描述
		logDao.put(log); // 记录日志
		return null;
	}

	@Override
	public List<Object> dropMsgs(List<String> rows, byte type, CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		String[] tmp = (String[]) rows.toArray(); // 用于遍历
		int len = tmp.length; // 记录数组长度
		if(type == MsgConst.TYPE_RECV) // 删除接收消息时
		{
			/* 遍历检查消息是否属于请求用户 */
			for(int i=0; i<len; i++)
			{
				FilterList fl = new FilterList();
				// 消息ID过滤器
				fl.addFilter(new RowFilter(CompareOp.EQUAL, 
						new BinaryComparator(Bytes.toBytes(tmp[i]))));
				// 接收用户ID过滤器
				fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
						Bytes.toBytes("recvuserid"), CompareOp.EQUAL,
						Bytes.toBytes(log.getLid())));
				// 消息不属于请求用户时
				if(msgRecvDao.findByPage("common_msg_recv", fl, null, 0) == null)
				{
					rows.remove(i); // 移除消息ID
				}
			}
			msgRecvDao.delete(rows); // 执行删除
		}
		else if(type == MsgConst.TYPE_SEND) // 删除发送消息时
		{
			/* 遍历检查消息是否属于请求用户 */
			for(int i=0; i<len; i++)
			{
				FilterList fl = new FilterList();
				// 消息ID过滤器
				fl.addFilter(new RowFilter(CompareOp.EQUAL, 
						new BinaryComparator(Bytes.toBytes(tmp[i]))));
				// 发送用户ID过滤器
				fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
						Bytes.toBytes("senduserid"), CompareOp.EQUAL,
						Bytes.toBytes(log.getLid())));
				// 消息不属于请求用户时
				if(msgRecvDao.findByPage("common_msg_send", fl, null, 0) == null)
				{
					rows.remove(i); // 移除消息ID
				}
			}
			msgSendDao.delete(rows); // 执行删除
		}
		logDao.put(log); // 记录日志
		return listMsg(log.getUserid(), type, 20, null); // 返回余下消息List
	}

}
