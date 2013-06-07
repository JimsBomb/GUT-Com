package org.chingo.gutcom.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.chingo.gutcom.bean.UserInfoBean;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.UserConst;
import org.chingo.gutcom.common.util.FormatUtil;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.domain.WeiboContent;
import org.chingo.gutcom.domain.WeiboFollow;
import org.chingo.gutcom.service.UserRelationManager;

public class UserRelationManagerImpl implements UserRelationManager
{
	private BaseDao<WeiboFollow> followDao; // 微博关注DAO
	private BaseDao<WeiboContent> weiboDao; // 微博DAO
	private BaseDao<CommonUser> userDao; // 用户DAO
	private BaseDao<CommonSyslog> logDao; // 日志DAO

	public void setFollowDao(BaseDao<WeiboFollow> followDao)
	{
		this.followDao = followDao;
	}
	
	public void setWeiboDao(BaseDao<WeiboContent> weiboDao)
	{
		this.weiboDao = weiboDao;
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
	public List<Object> fetchFollowing(String uid, String nickname,
			long timestamp, String startRow, int pageSize)
	{
		FilterList fl = new FilterList();
		fl.addFilter(new PageFilter(pageSize + 1)); // 单页查询数量
		if(uid != null) // 按用户ID查询时
		{
			// 用户ID过滤器
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("userid"), CompareOp.EQUAL,
					Bytes.toBytes(uid)));
		}
		else if(nickname != null) // 按昵称查询时
		{
			FilterList userFl = new FilterList();
			// 用户昵称过滤器
			userFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("nickname"), CompareOp.EQUAL,
					Bytes.toBytes(nickname)));
			// 查询用户
			List<Result> userRsts = userDao.findByPage("common_user", userFl, null, 0);
			if(userRsts != null) // 用户存在时
			{
				// 用户ID过滤器
				fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
						Bytes.toBytes("userid"), CompareOp.EQUAL,
						userRsts.get(0).getRow()));
			}
			else // 不存在时
			{
				return null;
			}
		}
		
		if(startRow == null) // 当查询最新列表时
		{
			// 时间戳过滤器，只查询上次查询时间戳后的数据
			fl.addFilter(new RowFilter(CompareOp.LESS, 
					new BinaryComparator(Bytes.toBytes(
							String.valueOf(Long.MAX_VALUE - timestamp)))));
		}
		// 查询关注关联记录
		List<Result> results = followDao.findByPage("weibo_follow", fl, startRow, pageSize+1);
		if(results != null) // 存在关注记录时
		{
			List<Object> rst = new ArrayList<Object>();
			List<UserInfoBean> userBeans = new ArrayList<UserInfoBean>();
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
				// 查询关注用户
				Result usrRst = userDao.get(Bytes.toString(result.getValue(
						Bytes.toBytes("info"), Bytes.toBytes("followid"))), null, null);
				if(usrRst != null) // 所关注用户存在时
				{
					CommonUser user = new CommonUser();
					user.fillByResult(usrRst); // 填充用户字段
					// 通过用户对象构造用户Bean
					UserInfoBean userBean = new UserInfoBean(user);
					// 设置关注标识为已关注
					userBean.setIsfollowed(UserConst.IS_FOLLOWED_YES);
					// 设置用户备注名
					userBean.setRemark(Bytes.toString(result.getValue(
							Bytes.toBytes("info"), Bytes.toBytes("remark"))));
					FilterList wbFl = new FilterList(); // 最近微博过滤器
					wbFl.addFilter(new PageFilter(1L)); // 只查询1条
					// 作者过滤器
					wbFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
						Bytes.toBytes("authorid"), CompareOp.EQUAL,
						Bytes.toBytes(user.getUid())));
					// 查询最新微博
					List<Result> wbRsts = weiboDao.findByPage("weibo_content", wbFl, null, 1);
					if(wbRsts != null) // 微博非空时
					{
						Result wbRst = wbRsts.get(0);
						// 构造最新发表微博对象
						UserInfoBean.Latest lt = userBean.new Latest(
								Bytes.toString(wbRst.getRow()),
								Bytes.toString(wbRst.getValue(Bytes.toBytes("info"),
										Bytes.toBytes("content"))));
						userBean.setLatest(lt); // 设置最新发表微博对象
					}
					userBeans.add(userBean); // 添加用户Bean到列表
				}
			}
			rst.add(userBeans); // 添加微博列表到返回结果列表中
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
	public List<Object> fetchFollower(String uid, String nickname,
			long timestamp, String startRow, int pageSize)
	{
		FilterList fl = new FilterList();
		fl.addFilter(new PageFilter(pageSize + 1)); // 单页查询数量
		String userId = null;
		if(uid != null) // 按用户ID查询时
		{
			userId = uid;
		}
		else if(nickname != null) // 按昵称查询时
		{
			FilterList userFl = new FilterList();
			// 用户昵称过滤器
			userFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("nickname"), CompareOp.EQUAL,
					Bytes.toBytes(nickname)));
			// 查询用户
			List<Result> userRsts = userDao.findByPage("common_user", userFl, null, 0);
			if(userRsts != null) // 用户存在时
			{
				userId = Bytes.toString(userRsts.get(0).getRow());
			}
		}
		// 关注用户ID过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("followid"), CompareOp.EQUAL,
				Bytes.toBytes(userId)));
					
		if(startRow == null) // 当查询最新列表时
		{
			// 时间戳过滤器，只查询上次查询时间戳后的数据
			fl.addFilter(new RowFilter(CompareOp.LESS, 
					new BinaryComparator(Bytes.toBytes(
							String.valueOf(Long.MAX_VALUE - timestamp)))));
		}
		// 查询关注关联记录
		List<Result> results = followDao.findByPage("weibo_follow", fl, startRow, pageSize+1);
		if(results != null) // 存在关注记录时
		{
			List<Object> rst = new ArrayList<Object>();
			List<UserInfoBean> userBeans = new ArrayList<UserInfoBean>();
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
				// 查询粉丝用户
				Result usrRst = userDao.get(Bytes.toString(result.getValue(
						Bytes.toBytes("info"), Bytes.toBytes("userid"))), null, null);
				if(usrRst != null) // 粉丝用户存在时
				{
					CommonUser user = new CommonUser();
					user.fillByResult(usrRst); // 填充用户字段
					// 通过用户对象构造用户Bean
					UserInfoBean userBean = new UserInfoBean(user);
					/* 查询被设置被关注用户对于粉丝的关注标识 */
					FilterList userFl = new FilterList();
					userFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
							Bytes.toBytes("followid"), CompareOp.EQUAL,
							Bytes.toBytes(userBean.getUid())));
					userFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
							Bytes.toBytes("userid"), CompareOp.EQUAL,
							Bytes.toBytes(userId)));
					List<Result> userRsts = followDao.findByPage("weibo_follow",
							userFl, null, 0);
					if(userRsts != null) // 关注记录存在时
					{
						userBean.setIsfollowed(UserConst.IS_FOLLOWED_YES);
					}
					else
					{
						userBean.setIsfollowed(UserConst.IS_FOLLOWED_NO);
					}
					
					// 设置用户备注名
					userBean.setRemark(Bytes.toString(result.getValue(
							Bytes.toBytes("info"), Bytes.toBytes("remark"))));
					/* 查询并设置粉丝最近发表微博对象 */
					FilterList wbFl = new FilterList(); // 最近微博过滤器
					wbFl.addFilter(new PageFilter(1L)); // 只查询1条
					// 作者过滤器
					wbFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
						Bytes.toBytes("authorid"), CompareOp.EQUAL,
						Bytes.toBytes(user.getUid())));
					// 查询最新微博
					List<Result> wbRsts = weiboDao.findByPage("weibo_content", wbFl, null, 1);
					if(wbRsts != null) // 微博非空时
					{
						Result wbRst = wbRsts.get(0);
						// 构造最新发表微博对象
						UserInfoBean.Latest lt = userBean.new Latest(
								Bytes.toString(wbRst.getRow()),
								Bytes.toString(wbRst.getValue(Bytes.toBytes("info"),
										Bytes.toBytes("content"))));
						userBean.setLatest(lt); // 设置最新发表微博对象
					}
					
					userBeans.add(userBean); // 添加用户Bean到列表
				}
			}
			rst.add(userBeans); // 添加微博列表到返回结果列表中
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
	public boolean follow(String uid, String nickname, String remark, CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志对象的rowKey
		CommonUser followUser = null; // 被关注的用户
		if(uid != null) // 按用户ID关注
		{
			Result rst = userDao.get(uid, null, null);
			if(rst != null)
			{
				followUser = new CommonUser();
				followUser.fillByResult(rst); // 填充用户字段
			}
		}
		else if(nickname != null) // 按昵称关注
		{
			FilterList userFl = new FilterList();
			// 用户昵称过滤器
			userFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("nickname"), CompareOp.EQUAL,
					Bytes.toBytes(nickname)));
			// 查询用户
			List<Result> userRsts = userDao.findByPage("common_user", userFl, null, 0);
			if(userRsts != null) // 用户存在时
			{
				Result tmp = userRsts.get(0);
				followUser = new CommonUser();
				followUser.fillByResult(tmp); // 填充用户字段
			}
		}
		if(followUser != null) // 被关注用户存在时
		{
			/* 构造关注对象 */
			WeiboFollow wf = new WeiboFollow();
			wf.setId(FormatUtil.createRowKey());
			wf.setFollowId(followUser.getUid());
			wf.setUserId(log.getUserid());
			if(remark != null) // 有备注时更新
			{
				wf.setRemark(remark);
			}
			followDao.put(wf); // 追加关注记录
			
			/* 更新被关注用户的相关计数 */
			followUser.setFollower(followUser.getFollower() + 1);
			followUser.setNewfollower(followUser.getNewfollower() + 1);
			userDao.put(followUser);
			/* 更新关注用户的相关计数 */
			Result userRst = userDao.get(wf.getUserId(), null, null);
			if(userRst != null)
			{
				CommonUser user = new CommonUser();
				user.fillByResult(userRst);
				user.setFollowing(user.getFollowing() + 1);
				userDao.put(user);
			}
			
			logDao.put(log); // 记录日志
			
			return true; // 关注成功
		}
		
		log.setDetail(String.format(SyslogConst.DETAIL_USER_FOLLOW_FAILED,
				uid==null?"昵称："+nickname:"ID："+uid)); // 关注失败信息
		logDao.put(log); // 记录日志
		return false; // 关注失败
	}

	@Override
	public boolean drop(String uid, String nickname, CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志对象的rowKey
		FilterList fl = new FilterList();
		// 关注用户（粉丝）过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("userid"), CompareOp.EQUAL,
					Bytes.toBytes(log.getUserid())));
		CommonUser followUser = null; // 被取消关注的用户
		if(uid != null) // 按用户ID取消关注
		{
			Result rst = userDao.get(uid, null, null);
			if(rst != null)
			{
				followUser = new CommonUser();
				followUser.fillByResult(rst); // 填充用户字段
			}
		}
		else if(nickname != null) // 按取消昵称关注
		{
			FilterList userFl = new FilterList();
			// 用户昵称过滤器
			userFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("nickname"), CompareOp.EQUAL,
					Bytes.toBytes(nickname)));
			// 查询用户
			List<Result> userRsts = userDao.findByPage("common_user", userFl, null, 0);
			if(userRsts != null) // 用户存在时
			{
				Result tmp = userRsts.get(0);
				followUser = new CommonUser();
				followUser.fillByResult(tmp); // 填充用户字段
			}
		}
		if(followUser != null) // 用户存在时
		{
			// 关注用户ID过滤器
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("followid"), CompareOp.EQUAL,
					Bytes.toBytes(followUser.getUid())));
			// 查询关注记录
			List<Result> results = followDao.findByPage("weibo_follow", fl, null, 0);
			if(results != null) // 关注记录存在时
			{
				// 移除关注记录
				followDao.delete(Bytes.toString(results.get(0).getRow()));
				
				/* 更新被取消关注的用户的相关计数 */
				followUser.setFollower(followUser.getFollower() - 1);
				userDao.put(followUser);
				/* 更新取消关注的用户的相关计数 */
				Result userRst = userDao.get(log.getUserid(), null, null);
				if(userRst != null)
				{
					CommonUser user = new CommonUser();
					user.fillByResult(userRst);
					user.setFollowing(user.getFollowing() - 1);
					userDao.put(user);
				}
				
				logDao.put(log); // 记录日志
				
				return true; // 取消关注成功
			}
		}
		// 取消关注失败描述
		log.setDetail(String.format(SyslogConst.DETAIL_USER_FOLLOW_DROP_FAILED,
				uid==null?"昵称："+nickname:"ID："+uid));
		logDao.put(log); // 记录日志
		
		return false; // 取消关注失败
	}

	@Override
	public UserInfoBean remark(String uid, String nickname, String remark,
			CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志对象的rowKey
		FilterList fl = new FilterList();
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("userid"), CompareOp.EQUAL,
					Bytes.toBytes(log.getUserid())));
		String userId = null; // 被关注用户的ID
		if(uid != null) // 按用户ID查询时
		{
			userId = uid;
		}
		else if(nickname != null) // 按昵称查询时
		{
			FilterList userFl = new FilterList();
			// 用户昵称过滤器
			userFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("nickname"), CompareOp.EQUAL,
					Bytes.toBytes(nickname)));
			// 查询用户
			List<Result> userRsts = userDao.findByPage("common_user", userFl, null, 0);
			if(userRsts != null) // 用户存在时
			{
				userId = Bytes.toString(userRsts.get(0).getRow());
			}
		}

		if(userId != null) // 被关注用户存在时
		{
			// 用户ID过滤器
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("followid"), CompareOp.EQUAL,
					Bytes.toBytes(userId)));
			// 查询关注记录
			List<Result> results = followDao.findByPage("weibo_follow", fl, null, 0);
			if(results != null) // 存在记录时
			{
				WeiboFollow wf = new WeiboFollow();
				wf.fillByResult(results.get(0)); // 填充关注对象字段
				wf.setRemark(remark); // 更新备注名
				followDao.put(wf); // 更新关注记录

				// 查询被关注用户
				Result userRst = userDao.get(wf.getFollowId(), null, null);
				if(userRst != null) // 用户存在时
				{
					/* 构造用户Bean */
					CommonUser user = new CommonUser();
					user.fillByResult(userRst);
					UserInfoBean userBean = new UserInfoBean(user);
					// 设置关注标识为已关注
					userBean.setIsfollowed(UserConst.IS_FOLLOWED_YES);
					userBean.setRemark(remark); // 设置新备注名

					return userBean; // 返回被关注用户Bean
				}
			}
		}
		// 更新备注名失败描述
		log.setDetail(String.format(SyslogConst.DETAIL_USER_FOLLOW_REMARK_FAILED
				, uid==null?"昵称："+nickname:"ID："+uid));
		logDao.put(log); // 记录日志
		return null;
	}

	
}
