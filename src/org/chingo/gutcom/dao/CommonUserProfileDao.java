package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.CommonUserProfile;

public interface CommonUserProfileDao
{
	public void save(CommonUserProfile conf);
	public void update(CommonUserProfile conf);
	public void delete(CommonUserProfile conf);
	public void delete(Serializable id);
	public CommonUserProfile get(Serializable id);
	public List<CommonUserProfile> findAll();
}
