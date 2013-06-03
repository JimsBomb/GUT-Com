package org.chingo.gutcom.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.chingo.gutcom.bean.WeiboInfoBean;
import org.chingo.gutcom.bean.WeiboTopicBean;
import org.chingo.gutcom.common.constant.FilterWordConst;
import org.chingo.gutcom.common.constant.MsgConst;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.UserConst;
import org.chingo.gutcom.common.constant.WeiboConst;
import org.chingo.gutcom.common.util.FormatUtil;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonMsgRecv;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.domain.WeiboAt;
import org.chingo.gutcom.domain.WeiboContent;
import org.chingo.gutcom.domain.WeiboFollow;
import org.chingo.gutcom.domain.WeiboReport;
import org.chingo.gutcom.domain.WeiboTopic;
import org.chingo.gutcom.domain.WeiboTopicRelation;
import org.chingo.gutcom.service.WeiboManager;

public class WeiboManagerImpl implements WeiboManager
{
	private BaseDao<WeiboContent> weiboDao; // 微博DAO
	private BaseDao<WeiboTopic> topicDao; // 话题DAO
	private BaseDao<WeiboTopicRelation> topicRelaDao; // 话题关联DAO
	private BaseDao<WeiboAt> atDao; // @提到DAO
	private BaseDao<CommonUser> userDao; // 用户DAO
	private BaseDao<WeiboReport> reportDao; // 举报DAO
	private BaseDao<CommonSyslog> logDao; // 日志DAO
	private BaseDao<CommonMsgRecv> msgRecvDao; // 消息接收DAO
	private BaseDao<WeiboFollow> wbFollowDao; // 关注DAO
	
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
	
	public void setReportDao(BaseDao<WeiboReport> reportDao)
	{
		this.reportDao = reportDao;
	}
	
	public void setLogDao(BaseDao<CommonSyslog> logDao)
	{
		this.logDao = logDao;
	}
	
	public void setMsgRecvDao(BaseDao<CommonMsgRecv> msgRecvDao)
	{
		this.msgRecvDao = msgRecvDao;
	}
	
	public void setWbFollowDao(BaseDao<WeiboFollow> wbFollowDao)
	{
		this.wbFollowDao = wbFollowDao;
	}

	@Override
	public WeiboContent putWeibo(WeiboContent weibo, CommonSyslog log)
	{
		if(weibo.getWid()==null || weibo.getWid().isEmpty()) // 当ID为空即新增时
		{
			weibo.setWid(FormatUtil.createRowKey()); // 创建微博的rowKey
		}
		
		/*** 内容解析开始 ***/
		String content = weibo.getContent(); // 存储微博内容
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
				topic.setLastpost(weibo.getDateline());
				topic.setSponsorId(weibo.getAuthorid());
				topic.setCount(0);
			}
			topic.setCount(topic.getCount() + 1); // 微博数+1
			topicDao.put(topic); // 追加话题
			WeiboTopicRelation wtr = new WeiboTopicRelation(); // 微博话题关联对象
			wtr.setId(FormatUtil.createRowKey()); // 创建关联对象的rowKey
			wtr.setTopicTitle(topic.getTitle()); // 设置话题标题
			wtr.setWeiboId(weibo.getWid()); // 设置微博ID
			topicRelaDao.put(wtr); // 追加数据
			
			topics.add(topic); // 添加到话题对象列表中
			topicTitles.append(topic.getTitle()); // 添加到话题标题中
			topicTitles.append("#"); // 添加话题标题分隔符
			cnt = mtr.end(); // 设置索引为匹配串后的第一个字符的索引
		}
		weibo.setTopics(topics); // 设置话题对象列表
		// 移除最后一个#，设置话题标题
		weibo.setTopicTitles(topicTitles.substring(0, topicTitles.length()-1));
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
				at.setWeiboId(weibo.getWid());
				atDao.put(at); // 追加数据
			}
			cnt = mtr.end(); // 设置索引为匹配串后的第一个字符的索引
		}
		/*** 内容解析结束 ***/
		
		/* 查询并设置源微博内容（如果有的话） */
		if(weibo.getSourceid()!=null && !weibo.getSourceid().equals("0"))
		{
			Result srcRst = weiboDao.get(weibo.getSourceid(), null, null);
			if(srcRst != null)
			{
				weibo.setSourceContent(Bytes.toString(srcRst.getValue(
						Bytes.toBytes("info"), Bytes.toBytes("content"))));
			}
		}
		
		weiboDao.put(weibo); // 追加微博
		// 查询作者信息
		Result result = userDao.get(weibo.getAuthorid(), null, null);
		CommonUser user = new CommonUser();
		user.fillByResult(result); // 填充用户字段
		user.setWeibocnt(user.getWeibocnt() + 1); // 已发微博计数+1
		userDao.put(user); // 更新用户
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		logDao.put(log); // 记录日志
		
		return weibo;
	}

	@Override
	public void delWeibo(List<String> rowKeys, CommonSyslog log)
	{
		/* 处理关联数据 */
		for(String rowKey : rowKeys)
		{
			handleWeiboRela(rowKey);
		}
		weiboDao.delete(rowKeys); // 执行删除
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		logDao.put(log); // 记录日志
	}

	@Override
	public void updateWeiboStatus(List<String> rowKeys, byte status,
			CommonSyslog log)
	{
		for(String rowKey : rowKeys)
		{
			// 查询微博
			Result result = weiboDao.get(rowKey, null, null);
			if(result != null) // 微博存在时
			{
				WeiboContent weibo = new WeiboContent();
				weibo.fillByResult(result); // 填充微博字段
				weibo.setStatus(status); // 设置新状态
				weiboDao.put(weibo); // 更新微博
			}
		}
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		logDao.put(log); // 记录日志
	}

	@Override
	public WeiboContent getWeibo(String rowKey)
	{
		// 查询微博
		Result result = weiboDao.get(rowKey, null, null);
		if(result != null) // 微博存在时
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
		return null;
	}

	@Override
	public List<Object> findWeiboByPage(Map<String, Object> values, String startRow,
			int pageSize)
	{
		FilterList fl = new FilterList();
		fl.addFilter(new PageFilter(pageSize+1));
		/* 填充查询条件 */
		if(values.containsKey("status")) // 匹配状态
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("status"), CompareOp.EQUAL, 
					Bytes.toBytes(String.valueOf(values.get("status")))));
		}
		if(values.containsKey("visibility")) // 匹配可见性
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("visibility"), CompareOp.EQUAL, 
					Bytes.toBytes(String.valueOf(values.get("visibility")))));
		}
		if(values.containsKey("uid")) // 匹配作者ID
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("authorid"), CompareOp.EQUAL, 
					Bytes.toBytes(String.valueOf(values.get("uid")))));
		}
		if(values.containsKey("author")) // 精确匹配作者昵称
		{
			FilterList tmpFl = new FilterList();
			tmpFl.addFilter(new PageFilter(1)); // 只查询一条记录
			// 昵称过滤器
			tmpFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("nickname"), CompareOp.EQUAL, 
					Bytes.toBytes((String.valueOf(values.get("author"))))));
			// 查询用户
			List<Result> results = userDao.findByPage("common_user", null, null, 1);
			if(results != null) // 用户存在时
			{
				// 用户ID过滤器
				fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
							Bytes.toBytes("authorid"), CompareOp.EQUAL, results.get(0).getRow()));
			}
			else // 用户不存在时，返回null
			{
				return null;
			}
		}
		if(values.containsKey("content")) // 模糊匹配微博内容
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("content"), CompareOp.EQUAL, 
					new SubstringComparator(String.valueOf(values.get("content")))));
		}
		if(values.containsKey("topic")) // 模糊匹配话题标题
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("topic_titles"), CompareOp.EQUAL, 
					new SubstringComparator(String.valueOf(values.get("topic")))));
		}
		if(values.containsKey("startTime")) // 起始时间
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("dateline"), CompareOp.GREATER_OR_EQUAL, 
					Bytes.toBytes(String.valueOf(values.get("startTime")))));
		}
		if(values.containsKey("endTime")) // 截止时间
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("dateline"), CompareOp.LESS_OR_EQUAL, 
					Bytes.toBytes(String.valueOf(values.get("endTime")))));
		}
		// 查询微博
		List<Result> results = weiboDao.findByPage("weibo_content", fl, startRow, pageSize+1);
		if(results != null) // 结果非空时
		{
			List<Object> rst = new ArrayList<Object>();
			List<WeiboContent> weibos = new ArrayList<WeiboContent>();
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
				weibos.add(fillWeiboByResult(result));
			}
			rst.add(weibos); // 添加微博列表到返回结果列表中
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
	public void delTopic(List<String> rowKeys, CommonSyslog log)
	{
		topicDao.delete(rowKeys); // 执行删除
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		logDao.put(log); // 记录日志
	}

	@Override
	public void updateTopicStatus(List<String> rowKeys, byte status, CommonSyslog log)
	{
		for(String rowKey : rowKeys)
		{
			// 查询话题
			Result result = topicDao.get(rowKey, null, null);
			if(result != null) // 话题存在时
			{
				WeiboTopic topic = new WeiboTopic();
				topic.fillByResult(result); // 填充话题字段
				topic.setIsblock(status); // 设置新状态
				topicDao.put(topic); // 更新话题
			}
		}
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		logDao.put(log); // 记录日志
	}

	@Override
	public List<Object> findTopicByPage(Map<String, Object> values, String startRow,
			int pageSize)
	{
		FilterList fl = new FilterList();
		fl.addFilter(new PageFilter(pageSize+1));
		/* 填充查询条件 */
		if(values.containsKey("status")) // 匹配状态
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), 
					Bytes.toBytes("isblock"), CompareOp.EQUAL,
					Bytes.toBytes(String.valueOf(values.get("status")))));
		}
		if(values.containsKey("title")) // 模糊匹配话题标题
		{
			fl.addFilter(new RowFilter(CompareOp.EQUAL,
					new SubstringComparator(String.valueOf(values.get("title")))));
		}
		if(values.containsKey("startTime")) // 起始时间
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), 
					Bytes.toBytes("dateline"), CompareOp.GREATER_OR_EQUAL,
					Bytes.toBytes(String.valueOf(values.get("startTime")))));
		}
		if(values.containsKey("endTime")) // 截止时间
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), 
					Bytes.toBytes("dateline"), CompareOp.LESS_OR_EQUAL,
					Bytes.toBytes(String.valueOf(values.get("endTime")))));
		}
		// 查询话题
		List<Result> results = topicDao.findByPage("weibo_topic", fl, startRow, pageSize+1);
		if(results != null) // 结果非空时
		{
			List<Object> rst = new ArrayList<Object>();
			List<WeiboTopic> topics = new ArrayList<WeiboTopic>();
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
				WeiboTopic topic = new WeiboTopic();
				topic.fillByResult(result); // 填充话题字段
				Result userRst = userDao.get(topic.getSponsorId(), null, null);
				if(userRst != null) // 用户存在时
				{
					CommonUser user = new CommonUser();
					user.fillByResult(userRst); // 填充用户字段
					topic.setSponsor(user); // 设置发起者
				}
				topics.add(topic);
			}
			rst.add(topics); // 添加话题列表到返回结果列表中
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
	public void delWeiboReport(List<String> rowKeys, CommonSyslog log)
	{
		reportDao.delete(rowKeys); // 执行删除
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		logDao.put(log); // 记录日志
	}

	@Override
	public WeiboReport getWeiboReport(String rowKey)
	{
		// 查询举报
		Result result = reportDao.get(rowKey, null, null);
		if(result != null) // 举报存在时
		{
			WeiboReport report = new WeiboReport();
			report.fillByResult(result); // 填充举报字段
			// 查询举报用户
			Result userRst = userDao.get(report.getReportUserId(), null, null);
			if(userRst != null) // 用户存在时
			{
				CommonUser user = new CommonUser();
				user.fillByResult(userRst); // 填充用户字段
				report.setReportUser(user); // 设置举报用户
			}
			// 查询并设置举报微博
			report.setReportWeibo(getWeibo(report.getReportWeiboId()));
			return report;
		}
		return null;
	}
	
	@Override
	public void updateWeiboReportStatus(List<String> rowKeys, byte status, CommonSyslog log)
	{
		for(String rowKey : rowKeys)
		{
			// 查询举报
			Result result = reportDao.get(rowKey, null, null);
			if(result != null) // 举报存在时
			{
				WeiboReport report = new WeiboReport();
				report.fillByResult(result); // 填充举报字段
				report.setStatus(status); // 设置新状态
				reportDao.put(report); // 更新举报
			}
		}
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		logDao.put(log); // 记录日志
	}
	
	@Override
	public void updateWeiboReportDeal(String rowKey, CommonSyslog log)
	{
		// 查询举报
		Result result = reportDao.get(rowKey, null, null);
		if(result != null) // 举报存在时
		{
			WeiboReport report = new WeiboReport();
			report.fillByResult(result); // 填充举报字段
			report.setStatus(WeiboConst.REPORT_DEALED); // 设置为已处理
			// 处理微博关联数据
			WeiboContent weibo = handleWeiboRela(report.getReportWeiboId()); 
			weiboDao.delete(report.getReportWeiboId()); // 删除被举报的微博
			reportDao.put(report); // 更新举报
			/* 向微博作者发送处理消息 */
			CommonMsgRecv msg = new CommonMsgRecv();
			msg.setMid(FormatUtil.createRowKey()); // 创建消息的rowKey
			msg.setDateline(new Date().getTime()); // 消息接收时间
			// 消息内容
			msg.setContent(MsgConst.MSG_SYS_WEIBO_ILLEGAL_DEALED + weibo.getContent());
			msg.setIsread(MsgConst.FLAG_NOT_READ); // 设置未未读
			msg.setRecvuserId(weibo.getAuthorid()); // 设置接收用户ID
			msg.setSenduserId(UserConst.SYSTEM_ID); // 设置发送用户ID为系统
			msgRecvDao.put(msg); // 追加消息
			/* 更新用户新消息数 */
			// 查询用户
			Result userRst = userDao.get(weibo.getAuthorid(), null, null);
			if(userRst != null) // 用户非空时
			{
				CommonUser user = new CommonUser();
				user.fillByResult(userRst); // 填充用户字段
				user.setNewmsg(user.getNewmsg() + 1); // 更新新消息数
				userDao.put(user); // 更新用户
			}
		}
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		logDao.put(log); // 记录日志
	}

	@Override
	public List<Object> findWeiboReportByPage(Map<String, Object> values, String startRow,
			int pageSize)
	{
		FilterList fl = new FilterList();
		fl.addFilter(new PageFilter(pageSize+1)); // 设置单页数量
		/* 填充查询条件 */
		if(values.containsKey("status")) // 匹配状态
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("status"), CompareOp.EQUAL,
					Bytes.toBytes(String.valueOf(values.get("status")))));
		}
		// 查询举报
		List<Result> results = reportDao.findByPage("weibo_report", fl, startRow, pageSize+1);
		if(results != null)
		{
			List<Object> rst = new ArrayList<Object>();
			List<WeiboReport> reports = new ArrayList<WeiboReport>();
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
				WeiboReport report = new WeiboReport();
				report.fillByResult(result); // 填充举报字段
				// 查询举报用户
				Result userRst = userDao.get(report.getReportUserId(), null, null);
				if(userRst != null) // 用户存在时
				{
					CommonUser user = new CommonUser();
					user.fillByResult(userRst); // 填充用户字段
					report.setReportUser(user); // 设置举报用户
				}
				// 设置举报微博
				report.setReportWeibo(getWeibo(report.getReportWeiboId()));
				reports.add(report);
			}
			rst.add(reports); // 添加举报列表到返回结果列表中
			if(results.size() > pageSize) // 存在更多记录时
			{
				// 添加下一页起始行的rowKey到结果列表中
				rst.add(Bytes.toString(results.get(pageSize).getRow()));
			}
			return rst; // 返回结果列表
		}
		
		return null;
	}

	/**
	 * 处理微博关联数据
	 * @param weiboid 微博的ID
	 * @return 被处理的微博对象
	 */
	private WeiboContent handleWeiboRela(String weiboid)
	{
		WeiboContent weibo = null;
		// 查询微博
		Result weiboRst = weiboDao.get(weiboid, null, null);
		if(weiboRst != null) // 微博存在时
		{
			weibo = new WeiboContent();
			weibo.fillByResult(weiboRst); // 填充微博字段
			// 查询微博作者
			Result userRst = userDao.get(weibo.getAuthorid(), null, null);
			if(userRst != null) // 用户存在时
			{
				CommonUser user = new CommonUser();
				user.fillByResult(userRst); // 填充用户字段
				user.setWeibocnt(user.getWeibocnt() - 1); // 更新微博计数
				userDao.put(user); // 更新用户
			}
		}
		/* 微博话题关联数据处理 */
		FilterList fl = new FilterList();
		// 微博ID过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("weiboid"), CompareOp.EQUAL,
				Bytes.toBytes(weiboid)));
		// 查询微博话题关联表中的对应记录
		List<Result> relaRsts = topicRelaDao.findByPage("weibo_topic_relation",
				fl, null, 0);
		if(relaRsts != null) // 存在关联数据时
		{
			/* 处理关联 */
			for(Result relaRst : relaRsts)
			{
				WeiboTopicRelation wtRela = new WeiboTopicRelation();
				wtRela.fillByResult(relaRst); // 填充关联字段
				/* 重新设置话题下属微博计数 */
				// 查询话题
				Result topicRst = topicDao.get(wtRela.getTopicTitle(), null, null);
				if(topicRst != null) // 话题存在时
				{
					WeiboTopic topic = new WeiboTopic();
					topic.fillByResult(topicRst); // 填充话题字段
					topic.setCount(topic.getCount() - 1); // 设置微博总数
					topicDao.put(topic); // 更新话题
				}
				topicRelaDao.delete(wtRela.getId()); // 删除关联记录
			}
		}
		/* 处理@提到关联数据 */
		// 查询@提到关联记录
		List<Result> atRsts = atDao.findByPage("weibo_at", fl, null, 0);
		if(atRsts != null) // 存在@关联记录时
		{
			// 存放关联记录的rowKey
			List<String> atKeys = new ArrayList<String>();
			for(Result atRst : atRsts)
			{
				// 添加rowKey到列表中
				atKeys.add(Bytes.toString(atRst.getRow()));
			}
			atDao.delete(atKeys); // 删除@关联记录
		}
		
		return weibo;
	}

	@Override
	public List<WeiboInfoBean> fetchPublicWeibo(int pageSize, byte trimUser,
			byte trimSource)
	{
		FilterList fl = new FilterList();
		fl.addFilter(new PageFilter(pageSize)); // 查询数量过滤器
		// 公开微博过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("visibility"), CompareOp.EQUAL,
				Bytes.toBytes(String.valueOf(WeiboConst.VISIBILITY_ALL))));
		// 已审核微博过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("status"), CompareOp.EQUAL,
				Bytes.toBytes(String.valueOf(WeiboConst.STATUS_AUDIT))));
		// 查询微博
		List<Result> results = weiboDao.findByPage("weibo_content", fl, null, pageSize);
		if(results != null) // 微博非空时
		{
			List<WeiboInfoBean> weiboBeans = new ArrayList<WeiboInfoBean>();
			/* 解析结果并添加到结果列表中 */
			for(Result result : results)
			{
				WeiboContent weibo = fillWeiboByResult(result); // 填充微博对象
				weiboBeans.add(new WeiboInfoBean(weibo)); // 添加微博Bean到列表中
			} 
			return weiboBeans; // 返回结果列表
		}
		return null;
	}

	@Override
	public List<Object> fetchListWeibo(String uid, long timestamp, String startRow, int pageSize)
	{
		FilterList fl = new FilterList();
		// 用户ID过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("userid"), CompareOp.EQUAL,
				Bytes.toBytes(uid)));
		// 查询关注用户列表
		List<Result> followRsts = wbFollowDao.findByPage("weibo_follow", fl, null, 0);
		FilterList weiboFl = new FilterList(); // 微博过滤器
		if(startRow == null) // 当查询最新列表时
		{
			// 时间戳过滤器，只查询上次查询时间戳后的数据
			weiboFl.addFilter(new RowFilter(CompareOp.GREATER, 
					new BinaryComparator(Bytes.toBytes(
							String.valueOf(Long.MAX_VALUE - timestamp)))));
		}
		// 查询所有微博，优化由配置文件设置
		List<Result> results = weiboDao.findByPage("weibo_content", weiboFl, startRow, 0);
		if(results != null) // 微博存在时
		{
			int cnt = 0; // 计数
			byte[] myId = Bytes.toBytes(uid); // 当前用户ID
			// 存放结果列表
			List<Object> rst = new ArrayList<Object>();
			// 存放微博Bean列表
			List<WeiboInfoBean> weiboBeans = new ArrayList<WeiboInfoBean>();
			// 嵌套循环进行连接查询
			for(Result result : results)
			{
				// 匹配当前用户ID和所关注用户ID时
				if((result.getRow().equals(myId))
						|| (followRsts!=null 
						&& followRsts.contains(result.getRow())))
				{
					if(cnt < pageSize) // 未达到单页数量上限时，添加到微博Bean列表
					{
						WeiboContent weibo = fillWeiboByResult(result);
						weiboBeans.add(new WeiboInfoBean(weibo));
					}
					else // 否则添加微博Bean列表和下页首行rowKey到结果列表中
					{
						rst.add(weiboBeans);
						rst.add(Bytes.toString(result.getRow()));
						break;
					}
				}
			}
			
			return rst.size()==0?null:rst; // 存在数据则返回结果列表
		}
		
		return null;
	}
	
	@Override
	public List<Object> fetchMyWeibo(String uid, long timestamp,
			String startRow, int pageSize)
	{
		FilterList fl = new FilterList();
		fl.addFilter(new PageFilter(pageSize + 1)); // 查询数量过滤器
		// 用户ID过滤器
		fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("authorid"), CompareOp.EQUAL,
				Bytes.toBytes(uid)));
		if(startRow == null) // 当查询最新列表时
		{
			// 时间戳过滤器，只查询上次查询时间戳后的数据
			fl.addFilter(new RowFilter(CompareOp.GREATER, 
					new BinaryComparator(Bytes.toBytes(
							String.valueOf(Long.MAX_VALUE - timestamp)))));
		}
		// 查询微博
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
	public List<Object> fetchOnesWeibo(String uid, String nickname,
			long timestamp, String startRow, int pageSize)
	{
		FilterList fl = new FilterList();
		fl.addFilter(new PageFilter(pageSize + 1)); // 查询数量过滤器
		if(uid != null) // 按用户ID查询时
		{
			// 用户ID过滤器
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("authorid"), CompareOp.EQUAL,
					Bytes.toBytes(uid)));
		}
		else if(nickname != null) // 按昵称查询时
		{
			/* 用户查询过滤器 */
			FilterList userFl = new FilterList();
			userFl.addFilter(new PageFilter(1L));
			userFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("nickname"), CompareOp.EQUAL,
					Bytes.toBytes(nickname)));
			// 查询用户
			List<Result> userRsts = userDao.findByPage("common_user", userFl, null, 0);
			if(userRsts != null) // 用户非空时
			{
				// 用户过滤器
				Result userRst = userRsts.get(0);
				fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("authorid"), CompareOp.EQUAL,
					userRst.getRow()));
			}
		}
		if(startRow == null) // 当查询最新列表时
		{
			// 时间戳过滤器，只查询上次查询时间戳后的数据
			fl.addFilter(new RowFilter(CompareOp.GREATER, 
					new BinaryComparator(Bytes.toBytes(
							String.valueOf(Long.MAX_VALUE - timestamp)))));
		}
		// 查询微博列表
		List<Result> results = weiboDao.findByPage("weibo_content", fl, startRow, pageSize + 1);
		if(results != null) // 列表非空时
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
	public List<Object> fetchTopicWeibo(String title, long timestamp,
			String startRow, int pageSize)
	{
		FilterList fl = new FilterList();
		fl.addFilter(new PageFilter(pageSize + 1)); // 查询数量过滤器
		if(title != null) // 标题非空时
		{
			// 话题过滤器
			FilterList topicFl = new FilterList();
			// 话题标题过滤器
			topicFl.addFilter(new RowFilter(CompareOp.EQUAL, 
					new BinaryComparator(Bytes.toBytes(title))));
			// 未屏蔽话题过滤器
			topicFl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("isblock"), CompareOp.EQUAL,
					Bytes.toBytes(String.valueOf(WeiboConst.TOPIC_NOT_BLOCK))));
			// 查询话题
			List<Result> topicRsts = topicDao.findByPage("weibo_topic", topicFl, null, 0);
			if(topicRsts != null) // 话题存在并未被屏蔽时
			{
				// 话题标题过滤器
				fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
						Bytes.toBytes("topic_titles"), CompareOp.EQUAL,
						new SubstringComparator(title)));
				if(startRow == null) // 当查询最新列表时
				{
					// 时间戳过滤器，只查询上次查询时间戳后的数据
					fl.addFilter(new RowFilter(CompareOp.GREATER, 
							new BinaryComparator(Bytes.toBytes(
									String.valueOf(Long.MAX_VALUE - timestamp)))));
				}
				// 查询微博列表
				List<Result> results = weiboDao.findByPage("weibo_content",
						fl, startRow, pageSize + 1);
				if(results != null) // 列表非空时
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
			}
		}
		
		return null;
	}

	@Override
	public WeiboInfoBean fetchSingleWeibo(String wid)
	{
		if(wid != null) // 微博ID非空时
		{
			WeiboContent weibo = getWeibo(wid); // 查询微博
			if(weibo != null) // 微博存在时
			{
				return new WeiboInfoBean(weibo); // 返回微博Bean
			}
		}
		return null;
	}

	@Override
	public List<Object> fetchAtWeibo(String uid, long timestamp,
			String startRow, int pageSize)
	{
		if(uid != null) // 用户非空时
		{
			FilterList fl = new FilterList();
			// 查询数量过滤器
			fl.addFilter(new PageFilter(pageSize + 1));
			// 用户ID过滤器
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"),
					Bytes.toBytes("userid"), CompareOp.EQUAL,
					Bytes.toBytes(uid)));
			if(startRow == null) // 当查询最新列表时
			{
				// 时间戳过滤器，只查询上次查询时间戳后的数据
				fl.addFilter(new RowFilter(CompareOp.GREATER, 
						new BinaryComparator(Bytes.toBytes(
								String.valueOf(Long.MAX_VALUE - timestamp)))));
			}
			// 查询@记录
			List<Result> atRsts = atDao.findByPage("weibo_at", fl, startRow, pageSize + 1);
			if(atRsts != null) // 存在@记录时
			{
				List<Object> rst = new ArrayList<Object>();
				List<WeiboInfoBean> weiboBeans = new ArrayList<WeiboInfoBean>();
				int rstSize = atRsts.size();
				if(rstSize > pageSize) // 结果数量大于需要的数量，即存在下一页时
				{
					rstSize = pageSize; // 循环判断上限设置为单页数量
				}
				Result result;
				/* 解析结果并添加到结果列表中 */
				for(int i=0; i<rstSize; i++)
				{
					result = atRsts.get(i);
					// 查询微博
					WeiboContent weibo = getWeibo(Bytes.toString(result.getValue(
							Bytes.toBytes("info"), Bytes.toBytes("weiboid"))));
					weiboBeans.add(new WeiboInfoBean(weibo)); // 添加微博Bean到列表中
				}
				rst.add(weiboBeans); // 添加微博列表到返回结果列表中
				if(atRsts.size() > pageSize) // 存在更多记录时
				{
					// 添加下一页起始行的rowKey到结果列表中
					rst.add(Bytes.toString(atRsts.get(pageSize).getRow()));
				}
				
				return rst;
			}
		}
		return null;
	}

	@Override
	public WeiboInfoBean postWeibo(WeiboContent weibo, Map<String, Byte> words,
			CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		String content = weibo.getContent();
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
						weibo.setStatus(WeiboConst.STATUS_NOT_AUDIT);
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
		WeiboContent wb = putWeibo(weibo, log); // 插入微博
		if(wb != null) // 插入成功时
		{
			return new WeiboInfoBean(wb); // 返回微博Bean
		}
		return null;
	}

	@Override
	public boolean dropWeibo(String wid, CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志的rowKey
		/* 检查微博作者是否为当前用户 */
		// 查询微博
		Result result = weiboDao.get(wid, null, null);
		if(result != null) // 微博非空时
		{
			// 当前用户为作者时
			if(log.getUserid().equals(Bytes.toString(
					result.getValue(Bytes.toBytes("info"), Bytes.toBytes("authorid")))))
			{
				List<String> rows = new ArrayList<String>();
				rows.add(wid);
				delWeibo(rows, log); // 删除微博并记录日志
				return true;
			}
		}
		log.setDetail(SyslogConst.DETAIL_USER_WEIBO_DROP_FAILED); // 删除失败描述
		logDao.put(log); // 记录日志
		return false;
	}

}
