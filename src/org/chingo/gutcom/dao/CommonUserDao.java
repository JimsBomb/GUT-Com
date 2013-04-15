package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.CommonUser;

public interface CommonUserDao
{
	public void save(CommonUser conf);
	public void update(CommonUser conf);
	public void delete(CommonUser conf);
	public void delete(Serializable id);
	public CommonUser get(Serializable id);
	public List<CommonUser> findAll();
}
