package org.chingo.gutcom.service.impl;

import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.util.FormatUtil;
import org.chingo.gutcom.common.util.SecurityUtil;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.service.AccountManager;

public class AccountManagerImpl implements AccountManager
{
	private BaseDao<CommonUser> userDao; // 用户DAO
	private BaseDao<CommonSyslog> logDao; // 日志DAO
	
	public void setUserDao(BaseDao<CommonUser> userDao)
	{
		this.userDao = userDao;
	}
	
	public void setLogDao(BaseDao<CommonSyslog> logDao)
	{
		this.logDao = logDao;
	}
	
	@Override
	public CommonUser getUser(String name, String pwd, CommonSyslog log)
	{
		FilterList fl = new FilterList();
		// 昵称过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("nickname"), CompareOp.EQUAL,
				Bytes.toBytes(name)));
		// 密码过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("password"), CompareOp.EQUAL,
				Bytes.toBytes(SecurityUtil.md5(pwd))));
		// 管理员过滤器
		fl.addFilter(new RowFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("0"))));
		// 记录条数过滤器
		fl.addFilter(new PageFilter(1L));
		// 查询用户
		List<Result> result = userDao.findByPage("common_user", fl, null, 0);
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		if(result != null) // 用户存在时
		{
			Result rst = result.get(0);
			/* 解析并设置用户字段 */
			CommonUser u = new CommonUser();
			u.fillByResult(rst); // 填充字段
			u.setLastlogin(log.getDateline()); // 设置最后登录时间戳
			u.setLastip(log.getIp()); // 设置最后登录IP
			
			userDao.put(u); // 更新最后登录信息
			logDao.put(log); // 追加日志信息
			return u;
		}
		
		log.setDetail(SyslogConst.DETAIL_ADMIN_LOGIN_FAILED); // 设置登录失败描述
		logDao.put(log); // 追加日志信息

		return null;
	}

	@Override
	public boolean updatePwd(String oldPwd, String newPwd, CommonSyslog log)
	{
		FilterList fl = new FilterList();
		// 密码过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("password"), CompareOp.EQUAL,
				Bytes.toBytes(SecurityUtil.md5(oldPwd))));
		// 管理员过滤器
		fl.addFilter(new RowFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("0"))));
		// 记录条数过滤器
		fl.addFilter(new PageFilter(1L));
		// 查询用户
		List<Result> result = userDao.findByPage("common_user", fl, null, 0);
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		if(result != null) // 存在用户时
		{
			Result rst = result.get(0);
			/* 解析并设置用户字段 */
			CommonUser u = new CommonUser();
			u.fillByResult(rst); // 填充用户字段
			u.setPassword(SecurityUtil.md5(newPwd)); // 设置新密码
			
			userDao.put(u); // 更新密码
			logDao.put(log); // 追加日志信息
			
			return true;
		}
		
		log.setDetail(SyslogConst.DETAIL_ADMIN_PW_UPDATE_FAILED); // 设置更改密码失败信息
		logDao.put(log); // 追加日志信息
		
		return false;
	}
}
