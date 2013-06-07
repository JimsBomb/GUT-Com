package org.chingo.gutcom.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.chingo.gutcom.common.constant.ErrorMsg;
import org.chingo.gutcom.common.constant.FilterWordConst;
import org.chingo.gutcom.common.util.FormatUtil;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonFilterWord;
import org.chingo.gutcom.domain.CommonSysconf;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.exception.GcException;
import org.chingo.gutcom.service.SystemManager;

public class SystemManagerImpl implements SystemManager
{
	private BaseDao<CommonSysconf> sysconfDao; // 系统配置DAO
	private BaseDao<CommonSyslog> syslogDao; // 日志DAO
	private BaseDao<CommonFilterWord> wordDao; // 过滤关键词DAO
	private BaseDao<CommonUser> userDao; // 用户DAO
	
	public void setSysconfDao(BaseDao<CommonSysconf> sysconfDao)
	{
		this.sysconfDao = sysconfDao;
	}
	
	public void setSyslogDao(BaseDao<CommonSyslog> syslogDao)
	{
		this.syslogDao = syslogDao;
	}
	
	public void setWordDao(BaseDao<CommonFilterWord> wordDao)
	{
		this.wordDao = wordDao;
	}
	
	public void setUserDao(BaseDao<CommonUser> userDao)
	{
		this.userDao = userDao;
	}

	@Override
	public CommonSysconf putConf(CommonSysconf conf, CommonSyslog log)
	{
		sysconfDao.put(conf); // 更新数据
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		syslogDao.put(log); // 记录日志
		
		return conf;
	}
	
	@Override
	public List<CommonSysconf> putConf(Map<String, String> confs, CommonSyslog log)
	{
		// 存储返回列表
		List<CommonSysconf> rstConfs = new ArrayList<CommonSysconf>();
		/* 批量更新数据 */
		Set<Entry<String, String>> tmp = confs.entrySet();
		for(Entry<String, String> entry : tmp)
		{
			// 新建系统配置对象
			CommonSysconf conf = new CommonSysconf(entry.getKey(), entry.getValue());
			rstConfs.add(conf); // 添加到返回列表中
			sysconfDao.put(conf); // 追加数据
		}
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		syslogDao.put(log); // 记录日志
		
		return rstConfs;
	}
	
	@Override
	public void delConf(List<String> rowKeys, CommonSyslog log)
	{
		sysconfDao.delete(rowKeys); // 执行删除
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		syslogDao.put(log); // 记录日志
	}

	@Override
	public List<CommonSysconf> findAllConf()
	{
		// 查询所有配置
		List<Result> results = sysconfDao.findByPage("common_sysconf", null, null, 0);
		if(results != null) // 存在配置项时
		{
			List<CommonSysconf> confs = new ArrayList<CommonSysconf>();
			/* 逐个解析并添加到列表中 */
			for(Result result : results)
			{
				CommonSysconf conf = new CommonSysconf();
				conf.fillByResult(result); // 填充字段
				confs.add(conf);
			}
			return confs; // 返回列表
		}
		
		return null;
	}

	@Override
	public void putSyslog(CommonSyslog log)
	{
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		syslogDao.put(log);
	}

	@Override
	public void delSyslog(List<String> rowKeys, CommonSyslog log)
	{
		syslogDao.delete(rowKeys); // 执行删除
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		syslogDao.put(log); // 记录日志
	}

	@Override
	public List<CommonSyslog> findAllSyslog()
	{
		// 查询日志
		List<Result> results = syslogDao.findByPage("common_syslog", null, null, 0);
		if(results != null) // 日志列表非空时
		{
			List<CommonSyslog> logs = new ArrayList<CommonSyslog>();
			/* 逐个解析并添加到列表中 */
			for(Result result : results)
			{
				CommonSyslog log = new CommonSyslog();
				log.fillByResult(result); // 填充日志字段
				// 查询用户
				Result userRst = userDao.get(log.getUserid(), null, null);
				if(userRst != null) // 用户存在时
				{
					CommonUser user = new CommonUser();
					user.fillByResult(userRst); // 填充用户字段
					log.setUser(user); // 设置用户
				}
				logs.add(log);
			}
			return logs; // 返回结果列表
		}
		return null;
	}

	@Override
	public List<Object> findSyslogByPage(String startRow, int pageSize)
	{
		FilterList fl = new FilterList();
		// 单页数量过滤器
		fl.addFilter(new PageFilter(pageSize+1));
		// 分页查询
		List<Result> results = syslogDao.findByPage("common_syslog", fl, startRow, pageSize+1);
		if(results != null) // 结果非空时
		{
			List<Object> rst = new ArrayList<Object>();
			List<CommonSyslog> logs = new ArrayList<CommonSyslog>();
			int rstSize = results.size();
			if(rstSize > pageSize) // 结果数量大于需要的数量，即存在下一页时
			{
				rstSize = pageSize; // 循环判断上限设置为单页数量
			}
			Result result;
			/* 逐个解析并添加倒日志列表中 */
			for(int i=0; i<rstSize; i++)
			{
				result = results.get(i);
				CommonSyslog log = new CommonSyslog();
				log.fillByResult(result); // 填充日志字段
				// 查询用户
				Result userRst = userDao.get(log.getUserid(), null, null);
				if(userRst != null) // 用户存在时
				{
					CommonUser user = new CommonUser();
					user.fillByResult(userRst); // 填充用户字段
					log.setUser(user); // 设置用户
				}
				logs.add(log);
			}
			rst.add(logs); // 添加日志列表到返回结果列表中
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
	public List<Object> findSyslogByPage(Map<String, Object> values, String startRow, int pageSize)
	{
		FilterList fl = new FilterList();
		if(values != null)
		{
			if(values.containsKey("username")) // 模糊匹配昵称
			{
				fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("nickname"),
						CompareOp.EQUAL, new SubstringComparator(String.valueOf(values.get("username")))));
			}
			if(values.containsKey("startTime")) // 起始时间
			{
				fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("dateline"),
						CompareOp.GREATER_OR_EQUAL, Bytes.toBytes(String.valueOf(values.get("startTime")))));
			}
			if(values.containsKey("endTime")) // 终止时间
			{
				fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("dateline"),
						CompareOp.LESS_OR_EQUAL, Bytes.toBytes(String.valueOf(values.get("endTime")))));
			}
			if(values.containsKey("type")) // 匹配日志类型
			{
				fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("type"),
						CompareOp.EQUAL, Bytes.toBytes(String.valueOf(values.get("type")))));
			}
		}
		fl.addFilter(new PageFilter(pageSize+1)); // 设置单页显示数
		
		// 分页查询
		List<Result> results = syslogDao.findByPage("common_syslog", fl, startRow, pageSize+1);
		if(results != null) // 结果非空时
		{
			List<Object> rst = new ArrayList<Object>();
			List<CommonSyslog> logs = new ArrayList<CommonSyslog>();
			int rstSize = results.size();
			if(rstSize > pageSize) // 结果数量大于需要的数量，即存在下一页时
			{
				rstSize = pageSize; // 循环判断上限设置为单页数量
			}
			Result result;
			/* 逐个解析并添加倒日志列表中 */
			for(int i=0; i<rstSize; i++)
			{
				result = results.get(i);
				CommonSyslog log = new CommonSyslog();
				log.fillByResult(result); // 填充日志字段
				// 查询用户
				Result userRst = userDao.get(log.getUserid(), null, null);
				if(userRst != null) // 用户存在时
				{
					CommonUser user = new CommonUser();
					user.fillByResult(userRst); // 填充用户字段
					log.setUser(user); // 设置用户
				}
				logs.add(log);
			}
			rst.add(logs); // 添加日志列表到返回结果列表中
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
	public long getSyslogTotalSize()
	{
		long size = 0;
		try
		{
			size = syslogDao.rowCount("common_syslog", null); // 查询日志数量
		}
		catch(Throwable t)
		{
			
		}
		return size;
	}

	@Override
	public CommonFilterWord putFilterWord(CommonFilterWord word, CommonSyslog log)
	{
		wordDao.put(word); // 追加关键词
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		syslogDao.put(log); // 记录日志
		
		return word;
	}
	
	@Override
	public void putFilterWord(String filePath, CommonSyslog log) throws GcException
	{
		File file = new File(filePath);
		BufferedReader br = null;
		String line;
		try
		{
			br = new BufferedReader(new FileReader(file));
			String[] temp;
			CommonFilterWord word;
			Map<String, Object> value = new HashMap<String, Object>(1);
			while((line = br.readLine()) != null) // 读取文件
			{
				temp = line.split("=");
				value.put("word", temp[0]);
				if(temp.length == 1) // 没有填写过滤级别时
				{
					word = new CommonFilterWord(temp[0], FilterWordConst.LEVEL_SCREEN); // 默认为屏蔽
				}
				else // 填写了过滤级别时
				{
					switch(temp[1])
					{
					case "1": // 1为审核
						word = new CommonFilterWord(temp[0], FilterWordConst.LEVEL_AUDIT);
						break;
					case "2": // 2为禁止
						word = new CommonFilterWord(temp[0], FilterWordConst.LEVEL_BAN);
						break;
					default: // 其它为屏蔽
						word = new CommonFilterWord(temp[0], FilterWordConst.LEVEL_SCREEN);
					}
				}
				wordDao.put(word); // 追加关键词
			}
			log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
			syslogDao.put(log); // 记录日志
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.FILTER_WORD_PATH_WRONG); // 文件不存在或路径错误
		}
		finally // 关闭流
		{
			if(br != null)
			{
				try
				{
					br.close();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void delFilterWord(List<String> rowKeys, CommonSyslog log)
	{
		wordDao.delete(rowKeys); // 删除关键词
		log.setLid(FormatUtil.createRowKey()); // 创建日志rowKey
		syslogDao.put(log); // 记录日志
	}

	@Override
	public List<CommonFilterWord> findAllFilterWord()
	{
		// 查询关键词
		List<Result> results = wordDao.findByPage("common_filter_word", null, null, 0);
		if(results != null) // 查询结果非空时
		{
			List<CommonFilterWord> rst = new ArrayList<CommonFilterWord>();
			/* 解析结果并添加到结果列表中 */
			for(Result result : results)
			{
				CommonFilterWord word = new CommonFilterWord();
				word.fillByResult(result); // 填充关键词字段
				rst.add(word);
			}
			return rst; // 返回结果列表
		}
		
		return null;
	}

	@Override
	public List<Object> findFilterWordByPage(Map<String, Object> values, String startRow,
			int pageSize)
	{
		FilterList fl = new FilterList();
		if(values.containsKey("word")) // 模糊匹配关键词
		{
			fl.addFilter(new RowFilter(CompareOp.EQUAL, new SubstringComparator(
					String.valueOf(values.get("word")))));
		}
		if(values.containsKey("level")) // 匹配过滤级别
		{
			fl.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("level"),
					CompareOp.EQUAL, Bytes.toBytes(String.valueOf((values.get("level"))))));
		}
		fl.addFilter(new PageFilter(pageSize+1)); // 设置单页显示数
		
		// 分页查询
		List<Result> results = syslogDao.findByPage("common_filter_word", fl, startRow, pageSize+1);
		if(results != null) // 结果非空时
		{
			List<Object> rst = new ArrayList<Object>();
			List<CommonFilterWord> words = new ArrayList<CommonFilterWord>();
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
				CommonFilterWord word = new CommonFilterWord();
				word.fillByResult(result); // 填充关键词字段
				words.add(word);
			}
			rst.add(words); // 添加日志列表到返回结果列表中
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
