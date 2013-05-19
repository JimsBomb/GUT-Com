package org.chingo.gutcom.dao.impl;

import org.apache.hadoop.hbase.client.Result;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonFilterWord;
import org.chingo.gutcom.hbase.HBaseSupport;

public class CommonFilterWordDaoImpl extends HBaseSupport implements BaseDao<CommonFilterWord>
{

	@Override
	public void put(CommonFilterWord instance)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String row)
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
