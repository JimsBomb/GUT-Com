package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.CommonUserStatus;

public interface CommonUserStatusDao
{
	public void save(CommonUserStatus conf);
	public void update(CommonUserStatus conf);
	public void delete(CommonUserStatus conf);
	public void delete(Serializable id);
	public CommonUserStatus get(Serializable id);
	public List<CommonUserStatus> findAll();
}
