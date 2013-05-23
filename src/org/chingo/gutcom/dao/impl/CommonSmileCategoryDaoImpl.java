package org.chingo.gutcom.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonSmileCategory;
import org.chingo.gutcom.hbase.HBaseSupport;
import org.springframework.data.hadoop.hbase.TableCallback;

public class CommonSmileCategoryDaoImpl extends HBaseSupport implements BaseDao<CommonSmileCategory>
{
	private static String TAB_NAME = "common_smile";

	@Override
	public void put(CommonSmileCategory instance)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String row)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void delete(final List<String> rows)
	{
		ht.execute(TAB_NAME, new TableCallback<Result>()
				{

					@Override
					public Result doInTable(HTableInterface htable)
							throws Throwable
					{
						List<Delete> dels = new ArrayList<Delete>();
						for(String row : rows)
						{
							Delete del = new Delete(Bytes.toBytes(row));
							dels.add(del);
						}
						htable.delete(dels);
						return null;
					}
			
				});
	}

	@Override
	public Result get(String row, String family, String qualifier)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
