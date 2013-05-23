package org.chingo.gutcom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonUserProfile;
import org.chingo.gutcom.hbase.HBaseSupport;
import org.chingo.gutcom.hibernate4.support.HDaoSupport;

public class CommonUserProfileDaoImpl extends HBaseSupport implements BaseDao<CommonUserProfile>
{

	@Override
	public void put(CommonUserProfile instance)
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
