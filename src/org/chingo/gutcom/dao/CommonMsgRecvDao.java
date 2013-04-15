package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.CommonMsgRecv;

public interface CommonMsgRecvDao
{
	public void save(CommonMsgRecv conf);
	public void update(CommonMsgRecv conf);
	public void delete(CommonMsgRecv conf);
	public void delete(Serializable id);
	public CommonMsgRecv get(Serializable id);
	public List<CommonMsgRecv> findAll();
}
