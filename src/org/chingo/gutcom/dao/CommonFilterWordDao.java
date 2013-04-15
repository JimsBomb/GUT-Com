package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.CommonFilterWord;

public interface CommonFilterWordDao
{
	public void save(CommonFilterWord conf);
	public void update(CommonFilterWord conf);
	public void delete(CommonFilterWord conf);
	public void delete(Serializable id);
	public CommonFilterWord get(Serializable id);
	public List<CommonFilterWord> findAll();
}
