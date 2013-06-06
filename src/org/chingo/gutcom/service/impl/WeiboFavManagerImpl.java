package org.chingo.gutcom.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;
import org.chingo.gutcom.bean.UserInfoBean;
import org.chingo.gutcom.bean.WeiboFavBean;
import org.chingo.gutcom.bean.WeiboInfoBean;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.WeiboConst;
import org.chingo.gutcom.common.util.FormatUtil;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.domain.WeiboContent;
import org.chingo.gutcom.domain.WeiboFav;
import org.chingo.gutcom.domain.WeiboTopic;
import org.chingo.gutcom.service.WeiboFavManager;

public class WeiboFavManagerImpl implements WeiboFavManager
{
	private BaseDao<WeiboFav> wbFavDao; // 微博收藏DAO
	private BaseDao<WeiboContent> weiboDao; // 微博DAO
	private BaseDao<WeiboTopic> topicDao; // 话题DAO
	private BaseDao<CommonUser> userDao; // 用户DAO
	private BaseDao<CommonSyslog> logDao; // 日志DAO

	public void setWbFavDao(BaseDao<WeiboFav> wbFavDao)
	{
		this.wbFavDao = wbFavDao;
	}

	public void setWeiboDao(BaseDao<WeiboContent> weiboDao)
	{
		this.weiboDao = weiboDao;
	}
	
	public void setTopicDao(BaseDao<WeiboTopic> topicDao)
	{
		this.topicDao = topicDao;
	}

	public void setUserDao(BaseDao<CommonUser> userDao)
	{
		this.userDao = userDao;
	}

	public void setLogDao(BaseDao<CommonSyslog> logDao)
	{
		this.logDao = logDao;
	}
	
	/**
	 * 根据Result填充微博对象，包括所有关联对象
	 * @param result 源Result
	 * @return 填充后微博对象
	 */
	private WeiboContent fillWeiboByResult(Result result)
	{
		WeiboContent weibo = new WeiboContent();
		weibo.fillByResult(result); // 填充微博字段
		// 查询作者用户
		Result userRst = userDao.get(weibo.getAuthorid(), null, null);
		if(userRst != null) // 用户存在时
		{
			CommonUser user = new CommonUser();
			user.fillByResult(userRst); // 填充用户字段
			weibo.setAuthor(user); // 设置作者
		}
		// 微博存在源微博时
		if(weibo.getSourceid()!=null || !weibo.getSourceid().isEmpty())
		{
			// 查询源微博
			Result srcRst = weiboDao.get(weibo.getSourceid(), null, null);
			if(srcRst != null) // 源微博存在时
			{
				WeiboContent src = new WeiboContent();
				src.fillByResult(srcRst); // 填充源微博字段
				weibo.setSource(src); // 设置源微博
			}
		}
		// 存在所属话题时
		if(weibo.getTopicTitles()!=null || !weibo.getTopicTitles().isEmpty())
		{
			Set<WeiboTopic> topics = weibo.getTopics();
			// 多个话题时拆分
			String[] topicKeys = weibo.getTopicTitles().split("#");
			for(String topicKey : topicKeys)
			{
				// 查询话题
				Result topicRst = topicDao.get(topicKey, null, null);
				if(topicRst != null) // 话题存在时
				{
					WeiboTopic topic = new WeiboTopic();
					topic.fillByResult(topicRst); // 填充话题字段
					topics.add(topic); // 添加到话题对象列表中
				}
			}
			weibo.setTopics(topics); // 设置微博的话题对象列表
		}
		
		return weibo;
	}

	@Override
	public List<Object> listFav(String uid, long timestamp, String startRow,
			int pageSize)
	{
		FilterList fl = new FilterList();
		fl.addFilter(new PageFilter(pageSize + 1)); // 查询数量
		// 查询收藏记录
		List<Result> results = wbFavDao.findByPage("weibo_fav", fl, startRow, pageSize+1);
		if(results != null) // 结果非空时
		{
			List<Object> rst = new ArrayList<Object>();
			List<WeiboFavBean> favBeans = new ArrayList<WeiboFavBean>();
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
				WeiboFav fav = new WeiboFav();
				fav.fillByResult(result); // 填充收藏字段
				WeiboFavBean favBean = new WeiboFavBean(fav); // 根据收藏对象构造收藏Bean
				// 查询微博
				Result wbRst = weiboDao.get(fav.getWeiboId(), null, null);
				if(wbRst != null) // 微博存在时
				{
					// 构造微博Bean
					WeiboInfoBean weiboBean = new WeiboInfoBean(fillWeiboByResult(wbRst));
					weiboBean.setFav(WeiboConst.FAV_YES); // 设置收藏标识为已收藏
					favBean.setObj(weiboBean); // 设置收藏Bean中的微博Bean
				}
				favBeans.add(favBean); // 添加收藏Bean到列表中
			}
			rst.add(favBeans); // 添加收藏Bean列表到返回结果列表中
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
	public WeiboFavBean showFav(String uid, String fid)
	{
		FilterList fl = new FilterList();
		// 收藏ID过滤器
		fl.addFilter(new RowFilter(CompareOp.EQUAL, new BinaryComparator(
				Bytes.toBytes(fid))));
		// 用户ID过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("userid"), CompareOp.EQUAL,
				Bytes.toBytes(uid)));
		// 查询收藏记录
		List<Result> results = wbFavDao.findByPage("weibo_fav", fl, null, 0);
		if(results != null) // 收藏记录非空时
		{
			WeiboFav fav = new WeiboFav();
			fav.fillByResult(results.get(0));
			WeiboFavBean favBean = new WeiboFavBean(fav); // 根据收藏对象构造收藏Bean
			// 查询微博
			Result wbRst = weiboDao.get(fav.getWeiboId(), null, null);
			if(wbRst != null) // 微博存在时
			{
				// 构造微博Bean
				WeiboInfoBean weiboBean = new WeiboInfoBean(fillWeiboByResult(wbRst));
				weiboBean.setFav(WeiboConst.FAV_YES); // 设置收藏标识为已收藏
				favBean.setObj(weiboBean); // 设置收藏Bean中的微博Bean
			}
			return favBean; // 返回收藏Bean
		}
		return null;
	}

	@Override
	public boolean createFav(String wid, CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志对象的rowKey
		Result result = weiboDao.get(wid, null, null); // 查询微博
		if(result != null) // 微博存在时
		{
			/* 构建收藏对象 */
			WeiboFav fav = new WeiboFav();
			fav.setFid(FormatUtil.createRowKey());
			fav.setUserId(log.getUserid());
			fav.setWeiboId(wid);
			
			wbFavDao.put(fav); // 追加收藏记录
			logDao.put(log); // 记录日志
			return true; // 收藏成功
		}
		// 收藏失败描述
		log.setDetail(String.format(SyslogConst.DETAIL_USER_WEIBO_FAV_FAILED,
				"ID："+wid)); 
		logDao.put(log); // 记录日志
		return false; // 收藏失败
	}

	@Override
	public boolean dropFav(String fid, CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志对象的rowKey
		FilterList fl = new FilterList();
		// 收藏ID过滤器
		fl.addFilter(new RowFilter(CompareOp.EQUAL, new BinaryComparator(
				Bytes.toBytes(fid))));
		// 用户ID过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("userid"), CompareOp.EQUAL,
				Bytes.toBytes(log.getUserid())));
		// 查询收藏记录
		List<Result> results = wbFavDao.findByPage("weibo_fav", fl, null, 0);
		if(results != null) // 记录存在时
		{
			wbFavDao.delete(fid); // 删除收藏
			logDao.put(log); // 记录日志
			return true; // 删除成功
		}
		
		log.setDetail(SyslogConst.DETAIL_USER_WEIBO_FAV_DROP_FAILED); // 删除失败描述
		logDao.put(log); // 记录日志
		return false; // 删除失败
	}

}
