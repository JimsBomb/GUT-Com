package org.chingo.gutcom.dao.impl;

import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.ShareComment;
import org.chingo.gutcom.hbase.HBaseSupport;

public class ShareCommentDaoImpl extends HBaseSupport implements BaseDao<ShareComment>
{

	@Override
	public void put(ShareComment instance)
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
