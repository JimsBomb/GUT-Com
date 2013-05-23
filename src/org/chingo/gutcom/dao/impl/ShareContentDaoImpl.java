package org.chingo.gutcom.dao.impl;

import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.ShareContent;
import org.chingo.gutcom.hbase.HBaseSupport;

public class ShareContentDaoImpl extends HBaseSupport implements BaseDao<ShareContent>
{

	@Override
	public void put(ShareContent instance)
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
