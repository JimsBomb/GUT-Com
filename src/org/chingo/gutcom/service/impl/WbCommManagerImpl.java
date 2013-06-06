package org.chingo.gutcom.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;
import org.chingo.gutcom.bean.WeiboInfoBean;
import org.chingo.gutcom.common.constant.FilterWordConst;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.WeiboConst;
import org.chingo.gutcom.common.util.FormatUtil;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.domain.WeiboAt;
import org.chingo.gutcom.domain.WeiboContent;
import org.chingo.gutcom.domain.WeiboTopic;
import org.chingo.gutcom.domain.WeiboTopicRelation;
import org.chingo.gutcom.service.WbCommManager;

public class WbCommManagerImpl implements WbCommManager
{
	private BaseDao<WeiboContent> weiboDao; // 微博DAO
	private BaseDao<WeiboTopic> topicDao; // 话题DAO
	private BaseDao<WeiboTopicRelation> topicRelaDao; // 话题关联DAO
	private BaseDao<WeiboAt> atDao; // @提到DAO
	private BaseDao<CommonUser> userDao; // 用户DAO
	private BaseDao<CommonSyslog> logDao; // 日志DAO

	public void setWeiboDao(BaseDao<WeiboContent> weiboDao)
	{
		this.weiboDao = weiboDao;
	}
	public void setTopicDao(BaseDao<WeiboTopic> topicDao)
	{
		this.topicDao = topicDao;
	}
	public void setTopicRelaDao(BaseDao<WeiboTopicRelation> topicRelaDao)
	{
		this.topicRelaDao = topicRelaDao;
	}
	public void setAtDao(BaseDao<WeiboAt> atDao)
	{
		this.atDao = atDao;
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
	 * 查询并填充微博对象中的关联对象
	 * @param weibo 要填充的微博对象
	 * @return 填充后的微博对象
	 */
	private WeiboContent fillWeiboRelation(WeiboContent weibo)
	{
		if(weibo.getAuthor() != null)
		{
			// 查询作者用户
			Result userRst = userDao.get(weibo.getAuthorid(), null, null);
			if(userRst != null) // 用户存在时
			{
				CommonUser user = new CommonUser();
				user.fillByResult(userRst); // 填充用户字段
				weibo.setAuthor(user); // 设置作者
			}
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
	public List<Object> fetchRecvComment(String uid, long timestamp,
			String startRow, int pageSize)
	{
		if(uid != null) // 用户非空时
		{
			FilterList fl = new FilterList();
			// 查询数量过滤器
			fl.addFilter(new PageFilter(pageSize + 1));
			// 用户ID过滤器
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("authorid"), CompareOp.EQUAL,
					Bytes.toBytes(uid)));
			// 排除评论过滤器
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("type"), CompareOp.NOT_EQUAL,
					Bytes.toBytes(String.valueOf(WeiboConst.TYPE_COMM))));
			// 查询用户微博记录
			List<Result> wbRsts = weiboDao.findByPage("weibo_content", fl, null, 0);
			if(wbRsts != null)
			{
				/* 存储用户微博ID */
				Set<String> wbIds = new HashSet<String>();
				for(Result result : wbRsts)
				{
					wbIds.add(Bytes.toString(result.getRow()));
				}
				
				FilterList commFl = new FilterList(); // 评论过滤器
				commFl.addFilter(new PageFilter(pageSize + 1)); // 查询数量过滤器
				// 评论过滤器
				commFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("type"), CompareOp.EQUAL,
					Bytes.toBytes(String.valueOf(WeiboConst.TYPE_COMM))));
				// 已审核微博过滤器
				commFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
						Bytes.toBytes("status"), CompareOp.EQUAL,
						Bytes.toBytes(String.valueOf(WeiboConst.STATUS_AUDIT))));
				if(startRow == null) // 当查询最新列表时
				{
					// 时间戳过滤器，只查询上次查询时间戳后的数据
					commFl.addFilter(new RowFilter(CompareOp.GREATER, 
							new BinaryComparator(Bytes.toBytes(
									String.valueOf(Long.MAX_VALUE - timestamp)))));
				}
				// 查询所有评论
				List<Result> commRsts = weiboDao.findByPage("weibo_content", commFl,
						startRow, pageSize + 1);
				if(commRsts != null) // 存在评论时
				{
					List<Object> rst = new ArrayList<Object>(); // 存放结果
					List<WeiboInfoBean> wbBeans = new ArrayList<WeiboInfoBean>();
					int cnt = 0; // 记录计数
					String nextRow = null; // 存放下页首行rowKey
					/* 遍历匹配评论 */
					for(Result result : commRsts)
					{
						// 是所收到的评论时
						if(wbIds.contains(Bytes.toString(result.getValue(
								Bytes.toBytes("info"), Bytes.toBytes("sourceid")))))
						{
							cnt++; // 计数+1
							if(cnt <= pageSize) // 不超过单页查询数量时
							{
								WeiboContent wb = new WeiboContent();
								wb.fillByResult(result);
								wb = fillWeiboRelation(wb);
								// 添加评论到微博Bean列表中
								wbBeans.add(new WeiboInfoBean(wb));
								continue;
							}
							else // 超过单页查询数量时
							{
								// 设置下页首行rowKey
								nextRow = Bytes.toString(result.getRow());
								break; // 跳出循环
							}
						}
					}
					if(wbBeans.size() > 0) // 有评论时
					{
						rst.add(wbBeans); // 添加微博Bean到结果列表中
					}
					if(nextRow != null) // 存在下页时
					{
						rst.add(nextRow); // 添加下页首行rowKey到结果列表中
					}
					
					return rst.size()>0?rst:null; // 结果列表非空时返回结果列表
				}
			}
		}
		return null;
	}
	@Override
	public List<Object> fetchPostComment(String uid, long timestamp,
			String startRow, int pageSize)
	{
		FilterList fl = new FilterList();
		fl.addFilter(new PageFilter(pageSize + 1)); // 查询数量过滤器
		// 用户ID过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("authorid"), CompareOp.EQUAL,
				Bytes.toBytes(uid)));
		// 评论过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("type"), CompareOp.EQUAL,
				Bytes.toBytes(String.valueOf(WeiboConst.TYPE_COMM))));
		if(startRow == null) // 当查询最新列表时
		{
			// 时间戳过滤器，只查询上次查询时间戳后的数据
			fl.addFilter(new RowFilter(CompareOp.GREATER, 
					new BinaryComparator(Bytes.toBytes(
							String.valueOf(Long.MAX_VALUE - timestamp)))));
		}
		// 查询发出的评论
		List<Result> results = weiboDao.findByPage("weibo_content", fl, null, pageSize + 1);
		if(results != null)
		{
			List<Object> rst = new ArrayList<Object>();
			List<WeiboInfoBean> weiboBeans = new ArrayList<WeiboInfoBean>();
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
				weiboBeans.add(new WeiboInfoBean(fillWeiboByResult(result)));
			}
			rst.add(weiboBeans); // 添加微博列表到返回结果列表中
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
	public List<Object> fetchWeiboComment(String wid, long timestamp,
			String startRow, int pageSize)
	{
		FilterList fl = new FilterList();
		fl.addFilter(new PageFilter(pageSize + 1)); // 查询数量过滤器
		// 微博ID过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("sourceid"), CompareOp.EQUAL,
				Bytes.toBytes(wid)));
		// 已审核过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("status"), CompareOp.EQUAL,
				Bytes.toBytes(String.valueOf(WeiboConst.STATUS_AUDIT))));
		if(startRow == null) // 当查询最新列表时
		{
			// 时间戳过滤器，只查询上次查询时间戳后的数据
			fl.addFilter(new RowFilter(CompareOp.GREATER, 
					new BinaryComparator(Bytes.toBytes(
							String.valueOf(Long.MAX_VALUE - timestamp)))));
		}
		// 查询发出的评论
		List<Result> results = weiboDao.findByPage("weibo_content", fl, null, pageSize + 1);
		if(results != null)
		{
			List<Object> rst = new ArrayList<Object>();
			List<WeiboInfoBean> weiboBeans = new ArrayList<WeiboInfoBean>();
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
				weiboBeans.add(new WeiboInfoBean(fillWeiboByResult(result)));
			}
			rst.add(weiboBeans); // 添加微博列表到返回结果列表中
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
	public WeiboInfoBean postComment(WeiboContent comment, 
			Map<String, Byte> words, CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志对象的rowKey
		
		/*** 内容解析开始 ***/
		String content = comment.getContent(); // 存储微博内容
		
		if(words != null)
		{
			/* 内容过滤处理 */
			for(Entry<String, Byte> entry : words.entrySet())
			{
				if(content.contains(entry.getKey())) // 包含关键词时
				{
					// 屏蔽处理
					if(entry.getValue() == FilterWordConst.LEVEL_SCREEN)
					{
						content = content.replace(entry.getKey(), FilterWordConst.SCREEN_WORD);
					}
					// 审核处理
					else if(entry.getValue() == FilterWordConst.LEVEL_AUDIT)
					{
						comment.setStatus(WeiboConst.STATUS_NOT_AUDIT);
						break;
					}
					// 禁止发表处理
					else if(entry.getValue() == FilterWordConst.LEVEL_BAN)
					{
						// 发表失败描述
						log.setDetail(SyslogConst.DETAIL_USER_WEIBO_POST_FAILED);
						logDao.put(log); // 记录日志
						return null;
					}
				}
			}
		}
		Set<WeiboTopic> topics = new HashSet<WeiboTopic>(); // 存放话题对象列表
		StringBuffer topicTitles = new StringBuffer(); // 存放话题标题
		/* 解析#话题# */
		Pattern ptn = Pattern.compile("#[^#]+#"); // #话题#过滤正则表达式
		Matcher mtr = ptn.matcher(content); // 获取匹配器
		int cnt = 0; // 记录索引
		while(mtr.find(cnt)) // 逐个匹配
		{
			String tmp = content.substring(mtr.start()+1, mtr.end()-1); // 截取话题
			WeiboTopic topic = new WeiboTopic();
			Result result = topicDao.get(tmp, null, null); // 查询话题
			if(result != null) // 话题存在时
			{
				topic.fillByResult(result); // 填充话题字段
			}
			else // 新话题时
			{
				topic.setTitle(tmp);
				topic.setDateline(new Date().getTime());
				topic.setIsblock(WeiboConst.TOPIC_NOT_BLOCK);
				topic.setLastpost(comment.getDateline());
				topic.setSponsorId(comment.getAuthorid());
				topic.setCount(0);
			}
			topic.setCount(topic.getCount() + 1); // 微博数+1
			topicDao.put(topic); // 追加话题
			WeiboTopicRelation wtr = new WeiboTopicRelation(); // 微博话题关联对象
			wtr.setId(FormatUtil.createRowKey()); // 创建关联对象的rowKey
			wtr.setTopicTitle(topic.getTitle()); // 设置话题标题
			wtr.setWeiboId(comment.getWid()); // 设置微博ID
			topicRelaDao.put(wtr); // 追加数据
			
			topics.add(topic); // 添加到话题对象列表中
			topicTitles.append(topic.getTitle()); // 添加到话题标题中
			topicTitles.append("#"); // 添加话题标题分隔符
			cnt = mtr.end(); // 设置索引为匹配串后的第一个字符的索引
		}
		comment.setTopics(topics); // 设置话题对象列表
		// 移除最后一个#，设置话题标题
		comment.setTopicTitles(topicTitles.substring(0, topicTitles.length()-1));
		/* 解析@提到 */
		ptn = Pattern.compile("@[^@#\\s]+"); // @提到过滤正则表达式
		mtr = ptn.matcher(content); // 获取匹配器
		cnt = 0; // 重置索引
		while(mtr.find(cnt)) // 逐个匹配
		{
			String tmp = content.substring(mtr.start()+1, mtr.end()); // 截取被@的用户的昵称
			FilterList fl = new FilterList();
			// 过滤用户昵称
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), 
					Bytes.toBytes("nickname"), CompareOp.EQUAL, Bytes.toBytes(tmp)));
			fl.addFilter(new PageFilter(1)); // 只查询一条记录
			// 查询用户
			List<Result> results = userDao.findByPage("common_user", fl, null, 1);
			if(results != null) // 用户存在时
			{
				Result result = results.get(0);
				CommonUser user = new CommonUser();
				user.fillByResult(result); // 填充用户字段
				user.setNewat(user.getNewat() + 1); // 新@提到数+1
				userDao.put(user); // 更新用户
				/* 追加@关联数据 */
				WeiboAt at = new WeiboAt();
				at.setId(FormatUtil.createRowKey()); // 创建@关联对象的rowKey
				at.setUserId(user.getUid());
				at.setWeiboId(comment.getWid());
				atDao.put(at); // 追加数据
			}
			cnt = mtr.end(); // 设置索引为匹配串后的第一个字符的索引
		}
		/*** 内容解析结束 ***/
		
		// 查询源微博
		Result wbRst = weiboDao.get(comment.getSourceid(), null, null);
		if(wbRst != null) // 源微博存在时
		{
			WeiboContent wb = new WeiboContent();
			wb.fillByResult(wbRst); // 填充源微博字段
			comment.setSource(wb); // 设置评论的源微博对象
			comment.setSourceContent(wb.getContent()); // 设置评论的源微博内容
			// 查询源微博的作者
			Result userRst = userDao.get(wb.getAuthorid(), null, null);
			if(userRst != null) // 用户存在时
			{
				CommonUser user = new CommonUser();
				user.fillByResult(userRst); // 填充用户字段
				user.setNewcomment(user.getNewcomment() + 1); // 更新新评论数
				userDao.put(user); // 更新用户信息
			}
		}
		else // 源微博不存在时不插入评论
		{
			log.setDetail(SyslogConst.DETAIL_USER_WEIBO_COMM_FAILED);
			logDao.put(log);
			return null;
		}
		// 查询评论作者
		Result authorRst = userDao.get(log.getUserid(), null, null);
		if(authorRst != null) // 用户存在时
		{
			CommonUser author = new CommonUser();
			author.fillByResult(authorRst); // 填充用户字段
			comment.setAuthor(author); // 设置作者对象
		}
		else // 评论作者不存在时不插入评论
		{
			log.setDetail(SyslogConst.DETAIL_USER_WEIBO_COMM_FAILED);
			logDao.put(log);
			return null;
		}
		weiboDao.put(comment); // 追加评论
		logDao.put(log); // 记录日志
	
		return new WeiboInfoBean(comment); // 返回评论的WeiboInfoBean
	}
	@Override
	public boolean dropComment(String wid, CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志对象的rowKey
		FilterList fl = new FilterList();
		fl.addFilter(new RowFilter(CompareOp.EQUAL, 
				new BinaryComparator(Bytes.toBytes(wid))));
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("authorid"), CompareOp.EQUAL,
				Bytes.toBytes(log.getUserid())));
		List<Result> results = weiboDao.findByPage("weibo_content", fl, null, 0);
		if(results != null)
		{
			weiboDao.delete(wid);
			logDao.put(log);
		}
		log.setDetail(SyslogConst.DETAIL_USER_WEIBO_DROP_FAILED); // 删除失败描述
		logDao.put(log); // 记录日志
		return false;
	}

	
}
