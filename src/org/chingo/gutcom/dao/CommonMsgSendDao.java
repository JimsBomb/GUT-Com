package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.CommonMsgSend;

public interface CommonMsgSendDao
{
	public void save(CommonMsgSend conf);
	public void update(CommonMsgSend conf);
	public void delete(CommonMsgSend conf);
	public void delete(Serializable id);
	public CommonMsgSend get(Serializable id);
	public List<CommonMsgSend> findAll();
}
