package org.chingo.gutcom.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.chingo.gutcom.bean.UserInfoBean;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.constant.UserConst;
import org.chingo.gutcom.common.util.FormatUtil;
import org.chingo.gutcom.common.util.SecurityUtil;
import org.chingo.gutcom.common.util.VerifyUtil;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonToken;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.domain.WeiboContent;
import org.chingo.gutcom.domain.WeiboFollow;
import org.chingo.gutcom.service.UserManager;

public class UserManagerImpl implements UserManager
{
	private BaseDao<CommonUser> userDao; // 用户DAO
	private BaseDao<CommonSyslog> logDao; // 日志DAO
	private BaseDao<CommonToken> tokenDao; // 令牌DAO
	private BaseDao<WeiboFollow> wbFollowDao; // 微博关注DAO
	private BaseDao<WeiboContent> weiboDao; // 微博DAO
	
	public void setUserDao(BaseDao<CommonUser> userDao)
	{
		this.userDao = userDao;
	}
	
	public void setLogDao(BaseDao<CommonSyslog> logDao)
	{
		this.logDao = logDao;
	}
	
	public void setTokenDao(BaseDao<CommonToken> tokenDao)
	{
		this.tokenDao = tokenDao;
	}
	
	public void setWbFollowDao(BaseDao<WeiboFollow> wbFollowDao)
	{
		this.wbFollowDao = wbFollowDao;
	}
	
	public void setWeiboDao(BaseDao<WeiboContent> weiboDao)
	{
		this.weiboDao = weiboDao;
	}

	@Override
	public CommonUser putUser(CommonUser user, CommonSyslog log)
	{
		if(user.getUid()==null || user.getUid().isEmpty()) // 用户ID不存在时
		{
			user.setUid(FormatUtil.createRowKey()); // 创建用户rowKey
		}
		user.setPassword(SecurityUtil.md5(user.getPassword())); // 加密密码
		userDao.put(user); // 追加用户
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		logDao.put(log); // 记录日志
		
		return user;
	}
	
	@Override
	public List<CommonUser> putUser(List<CommonUser> users, CommonSyslog log)
	{
		for(CommonUser user : users)
		{
			if(user.getUid()==null || user.getUid().isEmpty()) // 用户ID不存在时
			{
				user.setUid(FormatUtil.createRowKey()); // 创建用户rowKey
			}
			user.setPassword(SecurityUtil.md5(user.getPassword())); // 加密密码
			userDao.put(user); // 追加用户
		}
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		logDao.put(log); // 记录日志
		
		return users;
	}
	
	@Override
	public void updateStatus(String rowKey, byte status, CommonSyslog log)
	{
		// 查询用户
		Result rst = userDao.get(rowKey, null, null);
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		if(rst != null) // 存在用户时
		{
			/* 解析并设置用户字段 */
			CommonUser u = new CommonUser();
			u.fillByResult(rst); // 填充字段
			u.setStatus(status); // 设置新状态
			
			userDao.put(u); // 更新状态
			logDao.put(log); // 记录日志
		}
		
		log.setDetail(SyslogConst.DETAIL_ADMIN_USER_STATUS_UPDATE_FAILED); // 设置更新失败描述
		logDao.put(log); // 记录日志
	}

	@Override
	public void updateStatus(List<String> rowKeys, byte status, CommonSyslog log)
	{
		for(String rowKey : rowKeys)
		{
			// 查询用户
			Result rst = userDao.get(rowKey, null, null);
			log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
			if(rst != null) // 存在用户时
			{
				/* 解析并设置用户字段 */
				CommonUser u = new CommonUser();
				u.fillByResult(rst); // 填充字段
				u.setStatus(status); // 设置新状态
				
				userDao.put(u); // 更新状态
				logDao.put(log); // 记录日志
			}
			
			log.setDetail(SyslogConst.DETAIL_ADMIN_USER_STATUS_UPDATE_FAILED); // 设置更新失败描述
			logDao.put(log); // 记录日志
		}
	}

	@Override
	public void updatePassword(String rowKey, String pwd, CommonSyslog log)
	{
		// 查询用户
		Result rst = userDao.get(rowKey, null, null);
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		if(rst != null) // 存在用户时
		{
			/* 解析并设置用户字段 */
			CommonUser u = new CommonUser();
			u.fillByResult(rst); // 填充字段
			u.setPassword(SecurityUtil.md5(pwd)); // 设置新密码

			userDao.put(u); // 更新用户
			logDao.put(log); // 记录日志
		}

		log.setDetail(SyslogConst.DETAIL_ADMIN_USER_PWD_UPDATE_FAILED); // 设置更新失败描述
		logDao.put(log); // 记录日志
	}

	@Override
	public void delUser(String rowKey, CommonSyslog log)
	{
		userDao.delete(rowKey); // 执行删除
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		logDao.put(log); // 记录日志
	}

	@Override
	public void delUser(List<String> rowKeys, CommonSyslog log)
	{
		userDao.delete(rowKeys); // 执行删除
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		logDao.put(log); // 记录日志
	}

	@Override
	public CommonUser getUser(String rowKey)
	{
		// 查询用户
		Result result = userDao.get(rowKey, null, null);
		if(result != null) // 用户存在时
		{
			CommonUser user = new CommonUser();
			user.fillByResult(result); // 填充字段
			return user; // 返回用户
		}
		return null;
	}

	@Override
	public List<Object> findUserByPage(Map<String, Object> values, String startRow,
			int pageSize)
	{
		FilterList fl = new FilterList();
		// 排除管理员账号
		fl.addFilter(new RowFilter(CompareOp.NOT_EQUAL, new BinaryComparator(Bytes.toBytes("0"))));
		fl.addFilter(new PageFilter(pageSize+1)); // 设置单页数量
		/* 填充查询条件 */
		if(values.containsKey("status")) // 匹配状态
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("status"),
					CompareOp.EQUAL, Bytes.toBytes(String.valueOf(values.get("status")))));
		}
		if(values.containsKey("nickname")) // 模糊匹配昵称
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("nickname"),
					CompareOp.EQUAL, new SubstringComparator(String.valueOf(values.get("nickname")))));
		}
		if(values.containsKey("studentnum")) // 模糊匹配学号
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("studentnum"),
					CompareOp.EQUAL, new SubstringComparator(String.valueOf(values.get("studentnum")))));
		}
		if(values.containsKey("regdate")) // 指定时间后注册
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("status"),
					CompareOp.GREATER_OR_EQUAL, Bytes.toBytes(String.valueOf(values.get("regdate")))));
		}
		
		// 分页查询
		List<Result> results = userDao.findByPage("common_user", fl, startRow, pageSize+1);
		if(results != null) // 结果非空时
		{
			List<Object> rst = new ArrayList<Object>();
			List<CommonUser> users = new ArrayList<CommonUser>();
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
				CommonUser user = new CommonUser();
				user.fillByResult(result); // 填充用户字段
				users.add(user);
			}
			rst.add(users); // 添加用户列表到返回结果列表中
			if(results.size() > pageSize) // 存在更多记录时
			{
				// 添加下一页起始行的rowKey到结果列表中
				rst.add(Bytes.toString(results.get(pageSize).getRow()));
			}
			return rst; // 返回结果列表
		}
		return null;
	}

	@Override
	public List<Object> checkLogin(String nickname, String email,
			String studentnum, String pwd, CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		/* 设置条件 */
		FilterList fl = new FilterList();
		if(nickname != null) // 昵称非空时，设置昵称过滤器
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("nickname"), CompareOp.EQUAL,
					Bytes.toBytes(nickname)));
		}
		else if(email != null) // 邮箱非空时，设置邮箱过滤器
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("email"), CompareOp.EQUAL,
					Bytes.toBytes(email)));
		}
		else if(studentnum != null) // 学号非空时，设置学号过滤器
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("studentnum"), CompareOp.EQUAL,
					Bytes.toBytes(studentnum)));
		}
		else // 三个参数都为空时
		{
			log.setDetail(SyslogConst.DETAIL_USER_LOGIN_FAILED); // 设置登录失败描述
			logDao.put(log); // 记录日志
			return null; // 返回null
		}
		// 排除管理员过滤器
		fl.addFilter(new RowFilter(CompareOp.NOT_EQUAL, 
				new BinaryComparator(Bytes.toBytes(UserConst.SYSTEM_ID))));
		// 密码过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("password"), CompareOp.EQUAL,
				Bytes.toBytes(SecurityUtil.md5(pwd))));
		// 查询用户
		List<Result> results = userDao.findByPage("common_user", fl, null, 0);
		if(results != null) // 用户存在时
		{
			List<Object> rst = new ArrayList<Object>();
			Result result = results.get(0);
			CommonUser user = new CommonUser();
			user.fillByResult(result); // 填充用户字段
			user.setLastip(log.getIp()); // 更新最后登录IP
			user.setLastlogin(new Date().getTime()); // 更新最后登录时间戳
			userDao.put(user); // 更新用户
			rst.add(new UserInfoBean(user)); // 添加用户信息Bean对象
			/* 令牌过滤器 */
			FilterList tokenFl = new FilterList();
			// 有效时长超过1小时的令牌过滤器
			tokenFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("expired_time"), CompareOp.GREATER_OR_EQUAL,
				Bytes.toBytes(String.valueOf(user.getLastlogin() + 3600 * 1000))));
			// 查询令牌
			List<Result> tokenRsts = tokenDao.findByPage("common_token", tokenFl, null, 0);
			CommonToken token = new CommonToken();
			if(tokenRsts != null) // 令牌存在时
			{
				token.fillByResult(tokenRsts.get(0)); // 填充令牌字段
			}
			else // 否则创建新令牌
			{
				/* 更新令牌表 */
				token.setUserid(user.getUid());
				token.setAccessToken(SecurityUtil.createAccessToken()); // 创建令牌
				// 设置过期时间戳为(用户登陆时间+令牌默认有效时长)，前者为毫秒，后者为秒
				token.setExpiredTime(user.getLastlogin() + 
						SystemConst.ACCESS_TOKEN_EFFECTIVE_TIME * 1000);
				tokenDao.put(token); // 插入令牌关联数据
			}
			
			rst.add(token.getAccessToken()); // 添加访问令牌
			rst.add((token.getExpiredTime() - user.getLastlogin()) / 1000); // 添加令牌有效时长，单位为秒
			logDao.put(log); // 记录日志
			return rst; // 返回结果列表
		}
		log.setDetail(SyslogConst.DETAIL_USER_LOGIN_FAILED); // 设置登录失败描述
		logDao.put(log); // 记录日志
		return null;
	}

	@Override
	public List<Object> updateToken(String uid, String token, CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志得rowKey
		FilterList fl = new FilterList();
		// 用户ID过滤器
		fl.addFilter(new RowFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(uid))));
		// 令牌过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), 
				Bytes.toBytes("access_token"), CompareOp.EQUAL,
				Bytes.toBytes(token)));
		// 查询令牌关联记录
		List<Result> results = tokenDao.findByPage("common_token", fl, null, 0);
		if(results != null) // 存在关联时
		{
			List<Object> rst = new ArrayList<Object>(); // 存放结果列表
			CommonToken tok = new CommonToken();
			tok.fillByResult(results.get(0)); // 填充令牌字段
			tok.setAccessToken(SecurityUtil.createAccessToken()); // 创建新令牌
			// 更新令牌过期时间戳
			tok.setExpiredTime(new Date().getTime() + 
					SystemConst.ACCESS_TOKEN_EFFECTIVE_TIME * 1000);
			tokenDao.put(tok); // 更新令牌
			rst.add(tok.getAccessToken()); // 添加令牌到结果列表
			rst.add(SystemConst.ACCESS_TOKEN_EFFECTIVE_TIME); // 添加有效时长
			
			logDao.put(log);// 记录日志
			return rst; // 返回结果列表
		}
		log.setDetail(SyslogConst.DETAIL_USER_TOKEN_UPDATE_FAILED); // 更新失败信息
		logDao.put(log); // 记录日志
		return null;
	}

	@Override
	public List<Boolean> verifyId(String nickname, String email)
	{
		// 存在结果，默认为false-不可用
		List<Boolean> rst = new ArrayList<Boolean>();
		rst.add(false);
		rst.add(false);
		// 昵称非空且匹配格式时
		if(nickname != null && VerifyUtil.checkNickname(nickname))
		{
			FilterList fl = new FilterList();
			// 昵称过滤器
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("nickname"), CompareOp.EQUAL,
					Bytes.toBytes(nickname)));
			// 查询用户
			List<Result> results = userDao.findByPage("common_user", fl, null, 0);
			if(results == null) // 用户不存在时
			{
				rst.set(0, true); // 昵称可用
			}
		}
		// 邮箱非空且匹配格式时
		if(email!=null && VerifyUtil.checkEmail(email))
		{
			FilterList fl = new FilterList();
			// 邮箱过滤器
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("email"), CompareOp.EQUAL,
					Bytes.toBytes(email)));
			// 查询用户
			List<Result> results = userDao.findByPage("common_user", fl, null, 0);
			if(results == null) // 用户不存在时
			{
				rst.set(1, true); // 邮箱可用
			}
		}
		
		return rst;
	}

	@Override
	public boolean signup(String nickname, String email,
			String password, CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		// 验证昵称/邮箱可用性
		List<Boolean> verifyRst = verifyId(nickname, email);
		// 昵称/邮箱都可用时
		if(true==verifyRst.get(0) && true==verifyRst.get(1))
		{
			CommonUser user = new CommonUser();
			user.setUid(FormatUtil.createRowKey()); // 创建用户的rowKey
			user.setNickname(nickname);
			user.setEmail(email);
			user.setPassword(SecurityUtil.md5(password));
			user.setRegip(log.getIp());
			user.setRegdate(log.getDateline());
			userDao.put(user); // 追加用户数据
			logDao.put(log); // 记录日志
			return true;
		}
		// 设置注册失败日志描述
		log.setDetail(String.format(SyslogConst.DETAIL_USER_TOKEN_UPDATE_FAILED,
				nickname));
		logDao.put(log); // 记录日志
		return false;
	}

	@Override
	public CommonUser updateStudentnum(CommonUser user, String studentnum, String realname,
			String college, String major, String classname, CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		FilterList fl = new FilterList();
		// 排除当前用户过滤器
		fl.addFilter(new RowFilter(CompareOp.NOT_EQUAL, new BinaryComparator(
				Bytes.toBytes(user.getUid()))));
		// 绑定学号过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("studentnum"), CompareOp.EQUAL,
				Bytes.toBytes(studentnum)));
		// 查询学号是否已被使用
		List<Result> results = userDao.findByPage("common_user", fl, null, 0);
		if(results == null) // 学号未被绑定时
		{
			user.setStudentnum(studentnum);
			user.setRealname(realname);
			user.setCollege(college);
			user.setMajor(major);
			user.setClassname(classname);
			userDao.put(user); // 更新用户
			logDao.put(log); // 记录日志
			
			return user;
		}
		// 设置绑定失败信息
		log.setDetail(SyslogConst.DETAIL_USER_STUDENTNUM_UPDATE_FAILED);
		logDao.put(log); // 记录日志
		return null;
	}

	@Override
	public UserInfoBean getUserInfo(String currentUid, String uid, String nickname)
	{
		FilterList fl = new FilterList();
		if(uid != null) // 用户ID非空时
		{
			// 用户ID过滤器
			fl.addFilter(new RowFilter(CompareOp.EQUAL, 
					new BinaryComparator(Bytes.toBytes(uid))));
		}
		else if(nickname != null) // 昵称非空时
		{
			// 昵称过滤器
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("nickname"), CompareOp.EQUAL,
					Bytes.toBytes(nickname)));
		}
		// 查询用户
		List<Result> results = userDao.findByPage("common_user", fl, null, 0);
		if(results != null) // 用户存在时
		{
			CommonUser user = new CommonUser();
			user.fillByResult(results.get(0)); // 填充字段
			UserInfoBean userInfo = new UserInfoBean(user); // 新建并填充字段
			/* 关注关联数据处理 */
			FilterList followFl = new FilterList();
			// 用户ID过滤器
			followFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("userid"), CompareOp.EQUAL,
					Bytes.toBytes(currentUid)));
			// 关注用户ID过滤器
			followFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("followid"), CompareOp.EQUAL,
					Bytes.toBytes(user.getUid())));
			// 查询关注记录
			List<Result> followRsts = wbFollowDao.findByPage("weibo_follow", followFl, null, 0);
			if(followRsts != null) // 存在关注记录时
			{
				Result followRst = followRsts.get(0);
				// 设置备注名
				userInfo.setRemark(Bytes.toString(followRst.getValue(Bytes.toBytes("info"),
						Bytes.toBytes("remark"))));
			}
			/* 最后发表微博数据处理 */
			FilterList weiboFl = new FilterList();
			// 作者ID过滤器
			weiboFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("authorid"), CompareOp.EQUAL,
					Bytes.toBytes(user.getUid())));
			weiboFl.addFilter(new PageFilter(1L)); // 只查询1条记录
			// 查询微博
			List<Result> weiboRsts = weiboDao.findByPage("weibo_content", weiboFl, null, 1);
			if(weiboRsts != null) // 微博存在时
			{
				Result weiboRst = weiboRsts.get(0);
				UserInfoBean.Latest latest = userInfo.new Latest();
				// 设置微博ID
				latest.setWid(Bytes.toString(weiboRst.getRow()));
				// 设置微博内容
				latest.setContent(Bytes.toString(weiboRst.getValue(Bytes.toBytes("info"),
						Bytes.toBytes("content"))));
				userInfo.setLatest(latest); // 设置最后发表微博对象
			}
			
			return userInfo; // 返回用户信息Bean
		}
		return null;
	}

	@Override
	public UserInfoBean updateUserInfo(String uid, String nickname,
			String email, byte gender, String birth, byte bloodtype, String qq,
			String selfintro, CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志对象的rowKey
		CommonUser user = getUser(uid); // 获取用户
		if(user != null) // 用户存在时
		{
			if(nickname != null) // 昵称非空时更新
			{
				user.setNickname(nickname);
			}
			if(email != null) // 邮箱非空时更新
			{
				user.setEmail(email);
			}
			if(gender >= 0) // 性别有值时更新
			{
				user.setGender(gender);
			}
			if(birth != null) // 生日日期非空时更新
			{
				String[] birthDate = birth.split("-");
				int year = Integer.parseInt(birthDate[0]);
				byte month = Byte.parseByte(birthDate[1]);
				byte day = Byte.parseByte(birthDate[2]);
				// 生日改变时
				if(year!=user.getBirthyear() && month!=user.getBirthmonth()
						&& day!=user.getBirthday())
				{
					user.setBirthyear(year);
					user.setBirthmonth(month);
					user.setBirthday(day);
					user.setZodiac(FormatUtil.calcZodiac(year)); // 生肖
					user.setConstellation(FormatUtil.calcConstellation(month, day)); // 星座
				}
			}
			if(bloodtype >= 0) // 血型有值时更新
			{
				String bloodType = "其它";
				switch(bloodtype)
				{
				case UserConst.BLOODTYPE_OTHER:
					break;
				case UserConst.BLOODTYPE_A:
					bloodType = "A型";
					break;
				case UserConst.BLOODTYPE_B:
					bloodType = "B型";
					break;
				case UserConst.BLOODTYPE_AB:
					bloodType = "AB型";
					break;
				case UserConst.BLOODTYPE_O:
					bloodType = "O型";
					break;
				}
				user.setBloodtype(bloodType);
			}
			if(qq != null) // QQ非空时更新
			{
				user.setQq(qq);
			}
			if(selfintro != null) // 自我简介非空时更新
			{
				user.setSelfintro(selfintro);
			}
			userDao.put(user); // 更新用户
			logDao.put(log); // 记录日志
			UserInfoBean userInfo = new UserInfoBean(user); // 创建用户Bean并填充字段
			return userInfo; // 返回用户信息Bean
		}
		// 设置更新失败描述
		log.setDetail(SyslogConst.DETAIL_USER_INFO_UPDATE_FAILED);
		logDao.put(log); // 记录日志
		return null;
	}

	@Override
	public boolean resetNewCount(String type, CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		CommonUser user = getUser(log.getUserid()); // 查询用户
		if(user != null) // 用户存在时
		{
			switch(type) // 未读信息类型
			{
			case UserConst.CNT_NEWAT: // 清零新@提到
				user.setNewat(0);
				break;
			case UserConst.CNT_NEWCOMMENT: // 清零新评论
				user.setNewcomment(0);
				break;
			case UserConst.CNT_NEWFOLLOWER: // 清零新粉丝
				user.setNewfollower(0);
				break;
			case UserConst.CNT_NEWMSG: // 清零新消息
				user.setNewmsg(0);
				break;
			}
			userDao.put(user); // 更新用户
			logDao.put(log); // 记录日志
			return true;
		}
		// 设置失败描述
		log.setDetail(SyslogConst.DETAIL_USER_CNT_NEW_RESET_FAILED);
		logDao.put(log); // 记录日志
		return false;
	}

	@Override
	public List<Object> searchUser(String keyword, byte type, byte trimUser,
			int pageSize, String startRow)
	{
		FilterList fl = new FilterList();
		fl.addFilter(new PageFilter(pageSize + 1)); // 单页查询数量过滤器
		if(type == UserConst.SEARCH_TYPE_NICKNAME) // 按昵称查询时
		{
			// 昵称模糊匹配过滤器
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("nickname"), CompareOp.EQUAL,
					new SubstringComparator(keyword)));
		}
		else if(type == UserConst.SEARCH_TYPE_MAJOR) // 按专业查询时
		{
			// 专业模糊匹配过滤器
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("major"), CompareOp.EQUAL,
					new SubstringComparator(keyword)));
		}
		// 分页查询
		List<Result> results = userDao.findByPage("common_user", fl, startRow, pageSize+1);
		if(results != null) // 结果非空时
		{
			List<Object> rst = new ArrayList<Object>();
			List<UserInfoBean> users = new ArrayList<UserInfoBean>();
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
				CommonUser user = new CommonUser();
				user.fillByResult(result); // 填充用户字段
				UserInfoBean userBean = new UserInfoBean(user); // 创建用户Bean并填充字段
				users.add(userBean); // 添加Bean到列表仲
			}
			rst.add(users); // 添加用户列表到返回结果列表中
			if(results.size() > pageSize) // 存在更多记录时
			{
				// 添加下一页起始行的rowKey到结果列表中
				rst.add(Bytes.toString(results.get(pageSize).getRow()));
			}
			return rst; // 返回结果列表
		}
		return null;
	}
}
