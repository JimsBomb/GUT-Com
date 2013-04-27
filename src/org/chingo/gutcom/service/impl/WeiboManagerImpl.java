package org.chingo.gutcom.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.domain.WeiboContent;
import org.chingo.gutcom.domain.WeiboTopic;
import org.chingo.gutcom.service.WeiboManager;

public class WeiboManagerImpl implements WeiboManager
{
	private BaseDao<WeiboContent> weiboDao;
	private BaseDao<WeiboTopic> topicDao;
	private BaseDao<CommonUser> userDao;
	
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

	@Override
	public void addWeibo(WeiboContent weibo, Map<String, Object> logParams)
	{
		Integer uid = Integer.parseInt(logParams.get("uid").toString());
		CommonUser user = userDao.get(uid);
		weibo.setCommonUser(user); // 设置作者
		// TODO 内容分析……
		weiboDao.save(weibo);
		int weiboCnt = user.getCommonUserStatus().getWeibocnt();
		user.getCommonUserStatus().setWeibocnt(weiboCnt + 1); // 增加作者微博计数
	}

	@Override
	public void delWeibo(Serializable[] ids, Map<String, Object> logParams)
	{
		for(Serializable id : ids)
		{
			weiboDao.delete(id);
		}
	}

	@Override
	public void updateWeiboStatus(Serializable[] ids, byte status,
			Map<String, Object> logParams)
	{
		for(Serializable id : ids)
		{
			weiboDao.get(id).setStatus(status);
		}
	}

	@Override
	public WeiboContent getWeibo(Serializable id)
	{
		return weiboDao.get(id);
	}

	@Override
	public List findWeiboByPage(Map<String, Object> values, int offset,
			int pageSize)
	{
		StringBuffer hql = new StringBuffer("select wc from WeiboContent wc ");
		StringBuffer hqlCnt = new StringBuffer("select count(wc) from WeiboContent wc ");
		StringBuffer froms = new StringBuffer();
		StringBuffer wheres = new StringBuffer(" where 1=1 ");
		/* 填充查询条件 */
		if(values.containsKey("status")) // 状态
		{
			wheres.append(" and wc.status = :status ");
		}
		if(values.containsKey("visibility")) // 可见性
		{
			wheres.append(" and wc.visibility = :visibility ");
		}
		if(values.containsKey("uid")) // 作者ID
		{
			wheres.append(" and wc.commonUser.uid = :uid ");
		}
		if(values.containsKey("author")) // 作者昵称
		{
			wheres.append(" and wc.commonUser.nickname like :author ");
		}
		if(values.containsKey("content")) // 微博内容
		{
			wheres.append(" and wc.content like :content ");
		}
		if(values.containsKey("topic")) // 话题标题
		{
			wheres.append(" and wc.weiboTopicRelations.weiboTopic.title like :topic ");
		}
		if(values.containsKey("startTime")) // 起始时间
		{
			wheres.append(" and wc.dateline >= :startTime ");
		}
		if(values.containsKey("endTime")) // 截止时间
		{
			wheres.append(" and wc.dateline <= :endTime ");
		}
		hql.append(froms).append(wheres).append(" order by wid desc "); // 降序排序
		hqlCnt.append(froms).append(wheres);
		
		List<Object> rst = new ArrayList<Object>();
		rst.add(weiboDao.findByPage(hql.toString(), values, offset, pageSize));
		rst.add((long) weiboDao.query(hqlCnt.toString(), values).get(0));
		
		return rst;
	}

	@Override
	public void delTopic(Serializable[] ids, Map<String, Object> logParams)
	{
		for(Serializable id : ids)
		{
			topicDao.delete(id);
		}
	}

	@Override
	public void updateTopicStatus(Serializable[] ids, Map<String, Object> logParams)
	{
		byte isblock;
		WeiboTopic tmp;
		for(Serializable id : ids)
		{
			tmp = topicDao.get(id);
			isblock = tmp.getIsblock();
			isblock = (byte) (isblock ^ 1); // 根据当前状态取反
			topicDao.get(id).setIsblock(isblock);
		}
	}

	@Override
	public List findTopicByPage(Map<String, Object> values, int offset,
			int pageSize)
	{
		StringBuffer hql = new StringBuffer("select wt from WeiboTopic wt ");
		StringBuffer hqlCnt = new StringBuffer("select count(wt) from WeiboTopic wt ");
		StringBuffer froms = new StringBuffer();
		StringBuffer wheres = new StringBuffer(" where 1=1 ");
		/* 填充查询条件 */
		if(values.containsKey("status")) // 状态
		{
			wheres.append(" and wt.isblock = :status ");
		}
		if(values.containsKey("title")) // 话题标题
		{
			wheres.append(" and wt.title like :title ");
		}
		if(values.containsKey("startTime")) // 起始时间
		{
			wheres.append(" and wc.dateline >= :startTime ");
		}
		if(values.containsKey("endTime")) // 截止时间
		{
			wheres.append(" and wc.dateline <= :endTime ");
		}
		if(values.containsKey("sort")) // 排序
		{
			wheres.append(" order by wt.:sort desc ");
		}
		else
		{
			wheres.append(" order by wt.tid desc ");
		}
		hql.append(froms).append(wheres);
		hqlCnt.append(froms).append(wheres);
		
		List<Object> rst = new ArrayList<Object>();
		rst.add(topicDao.findByPage(hql.toString(), values, offset, pageSize));
		rst.add((long) topicDao.query(hqlCnt.toString(), values).get(0));
		
		return rst;
	}

}
