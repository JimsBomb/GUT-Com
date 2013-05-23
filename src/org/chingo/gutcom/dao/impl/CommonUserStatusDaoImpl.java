package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonUserStatus;
import org.chingo.gutcom.hbase.HBaseSupport;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class CommonUserStatusDaoImpl extends HBaseSupport implements BaseDao<CommonUserStatus>
{

	@Override
	public void put(CommonUserStatus instance)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String row)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(List<String> rows)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Result get(String row, String family, String qualifier)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
