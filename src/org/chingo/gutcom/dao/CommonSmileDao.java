package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.CommonSmile;

public interface CommonSmileDao
{
	public void save(CommonSmile conf);
	public void update(CommonSmile conf);
	public void delete(CommonSmile conf);
	public void delete(Serializable id);
	public CommonSmile get(Serializable id);
	public List<CommonSmile> findAll();
}
