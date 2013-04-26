package org.chingo.gutcom.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.chingo.gutcom.common.constant.UserConst;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.domain.CommonUserProfile;
import org.chingo.gutcom.domain.CommonUserStatus;
import org.chingo.gutcom.service.UserManager;
import org.chingo.gutcom.util.SecurityUtil;

public class UserManagerImpl implements UserManager
{
	private BaseDao<CommonUser> userDao;
	private BaseDao<CommonUserProfile> upDao;
	private BaseDao<CommonUserStatus> usDao;
	
	public void setUserDao(BaseDao<CommonUser> userDao)
	{
		this.userDao = userDao;
	}

	public void setUpDao(BaseDao<CommonUserProfile> upDao)
	{
		this.upDao = upDao;
	}

	public void setUsDao(BaseDao<CommonUserStatus> usDao)
	{
		this.usDao = usDao;
	}

	@Override
	public void addUser(CommonUser u, Map<String, Object> logParams)
	{
		CommonUser user = u;
		user.setPassword(SecurityUtil.md5(user.getPassword()));
		userDao.save(user);
		
		CommonUserProfile up = new CommonUserProfile(user, "",
				"", "", "", (byte) 0, (short) 0,  (byte) 0, (byte) 0,
				"", "", "", "", "", UserConst.AVATAR_URL, UserConst.BIG_AVATAR_URL);
		upDao.save(up);
		
		CommonUserStatus us = new CommonUserStatus(user, "",
				0l, "", 0, 0, 0, 0, 0, 0, 0, 0);
		usDao.save(us);
	}
	
	@Override
	public void updateStatus(Serializable id, byte status, Map<String, Object> logParams)
	{
		userDao.get(id).setStatus(status);
	}

	@Override
	public void updateStatus(Serializable[] ids, byte status, Map<String, Object> logParams)
	{
		for(Serializable id : ids)
		{
			userDao.get(id).setStatus(status);
		}
	}

	@Override
	public void updatePassword(Serializable id, String pwd, Map<String, Object> logParams)
	{
		userDao.get(id).setPassword(SecurityUtil.md5(pwd));
	}

	@Override
	public void delUser(Serializable id, Map<String, Object> logParams)
	{
		userDao.delete(id);
	}

	@Override
	public void delUser(Serializable[] ids, Map<String, Object> logParams)
	{
		for(Serializable id : ids)
		{
			userDao.delete(id);
		}
	}

	@Override
	public CommonUser getUser(Serializable id)
	{
		return userDao.get(id);
	}

	@Override
	public List findUserByPage(Map<String, Object> values, int offset,
			int pageSize)
	{
		StringBuffer hql = new StringBuffer("select cu from CommonUser cu ");
		StringBuffer hqlCnt = new StringBuffer("select count(cu) from CommonUser cu ");
		StringBuffer froms = new StringBuffer();
		StringBuffer wheres = new StringBuffer(" where cu.uid <> 0 "); // 排除管理员账号
		/* 填充查询条件 */
		if(values.containsKey("status"))
		{
			wheres.append(" and cu.status = :status ");
		}
		if(values.containsKey("nickname"))
		{
			wheres.append(" and cu.nickname like :nickname ");
		}
		if(values.containsKey("studentnum"))
		{
			wheres.append(" and cu.studentnum like :studentnum ");
		}
		if(values.containsKey("regdate"))
		{
			wheres.append(" and cu.regdate like :regdate ");
		}
		hql.append(froms).append(wheres);
		hqlCnt.append(froms).append(wheres);
		
		List<Object> rst = new ArrayList<Object>();
		rst.add(userDao.findByPage(hql.toString(), values, offset, pageSize));
		rst.add((long) userDao.query(hqlCnt.toString(), values).get(0));
		
		return rst;
	}

}
