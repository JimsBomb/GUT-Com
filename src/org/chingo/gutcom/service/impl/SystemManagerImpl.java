package org.chingo.gutcom.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonFilterWord;
import org.chingo.gutcom.domain.CommonSysconf;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.service.SystemManager;

public class SystemManagerImpl implements SystemManager
{
	private BaseDao<CommonSysconf> sysconfDao;
	private BaseDao<CommonSyslog> syslogDao;
	private BaseDao<CommonFilterWord> wordDao;
	
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

	@Override
	public void addConf(CommonSysconf conf)
	{
		sysconfDao.save(conf);
	}

	@Override
	public void updateConf(CommonSysconf conf)
	{
		sysconfDao.update(conf);
	}
	
	@Override
	public void updateConf(Map<String, String> confs)
	{
		String hql = "from CommonSysconf cs where cs.confname=:confname";
		Map<String, Object> v = new HashMap<String, Object>(1);
		CommonSysconf conf;
		for(Entry c : confs.entrySet())
		{
			v.clear();
			v.put("confname", c.getKey());
			conf = (CommonSysconf) sysconfDao.query(hql, v).get(0);
			conf.setConfvalue((String) c.getValue());
		}
	}

	@Override
	public void delConf(java.io.Serializable id)
	{
		sysconfDao.delete(id);
	}

	@Override
	public List<CommonSysconf> findAllConf()
	{
		return sysconfDao.list();
	}

	@Override
	public void addSyslog(CommonSyslog log)
	{
		syslogDao.save(log);
	}

	@Override
	public void delSyslog(Serializable[] ids)
	{
		for(Serializable id : ids)
		{
			syslogDao.delete(id);
		}
	}

	@Override
	public List<CommonSyslog> findAllSyslog()
	{
		return syslogDao.list();
	}

	@Override
	public List<CommonSyslog> findSyslogByPage(int offset, int pageSize)
	{
		String hql = "from CommonSyslog cs order by cs.lid desc";
		return syslogDao.findByPage(hql, offset, pageSize);
	}
	
	@Override
	public List findSyslogByPage(Map<String, Object> values, int offset, int pageSize)
	{
		StringBuffer hql = new StringBuffer("select cs from CommonSyslog cs ");
		StringBuffer hqlCnt = new StringBuffer("select count(cs) from CommonSyslog cs ");
		StringBuffer froms = new StringBuffer();
		StringBuffer wheres = new StringBuffer(" where 1=1 ");
		if(values.containsKey("username"))
		{
//			froms.append(" left join CommonUser cu ");
//			wheres.append(" with cs.commonUser.uid=cu.uid and cu.nickname like '%?%' ");
			wheres.append(" and cs.commonUser.nickname like :username ");
		}
//		else
//		{
//			wheres.append(" where 1=1 ");
//		}
		if(values.containsKey("startTime"))
		{
			wheres.append(" and cs.dateline>=:startTime ");
		}
		if(values.containsKey("endTime"))
		{
			wheres.append(" and cs.dateline<=:endTime ");
		}
		if(values.containsKey("type"))
		{
			wheres.append(" and cs.type=:type ");
		}
		hql.append(froms).append(wheres).append(" order by cs.lid desc ");
		hqlCnt.append(froms).append(wheres);
		
		List<Object> rst = new ArrayList<Object>();
//		Object[] v = values.values().toArray();
		rst.add(syslogDao.findByPage(hql.toString(), values, offset, pageSize));
		rst.add((long) syslogDao.query(hqlCnt.toString(), values).get(0));
		
		return rst;
	}

	@Override
	public long getSyslogTotalSize()
	{
		String hql = "select count(*) from CommonSyslog l";
		return (long) syslogDao.query(hql, new Object[0]).get(0);
	}

	@Override
	public void addFilterWord(CommonFilterWord word)
	{
		wordDao.save(word);
	}

	@Override
	public void updateFilterWord(CommonFilterWord word)
	{
		wordDao.update(word);
	}

	@Override
	public void delFilterWord(CommonFilterWord word)
	{
		wordDao.delete(word);
	}

	@Override
	public void delFilterWord(Serializable id)
	{
		wordDao.delete(id);
	}
	
	@Override
	public void delFilterWord(Serializable[] ids)
	{
		for(Serializable id : ids)
		{
			wordDao.delete(id);
		}
	}

	@Override
	public List<CommonFilterWord> findAllFilterWord()
	{
		return wordDao.list();
	}

	@Override
	public List findFilterWordByPage(Map<String, Object> values, int offset,
			int pageSize)
	{
		StringBuffer hql = new StringBuffer("select cfw from CommonFilterWord cfw ");
		StringBuffer hqlCnt = new StringBuffer("select count(cfw) from CommonFilterWord cfw ");
		StringBuffer froms = new StringBuffer();
		StringBuffer wheres = new StringBuffer(" where 1=1 ");
		if(values.containsKey("word"))
		{
			wheres.append(" and cfw.word like :word ");
		}
		if(values.containsKey("level"))
		{
			wheres.append(" and cfw.level=:level ");
		}
		hql.append(froms).append(wheres);
		hqlCnt.append(froms).append(wheres);
		
		List<Object> rst = new ArrayList<Object>();
		rst.add(syslogDao.findByPage(hql.toString(), values, offset, pageSize));
		rst.add((long) syslogDao.query(hqlCnt.toString(), values).get(0));
		
		return rst;
	}
	
}
