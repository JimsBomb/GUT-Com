package org.chingo.gutcom.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.WeiboReport;
import org.chingo.gutcom.hbase.HBaseSupport;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;

public class WeiboReportDaoImpl extends HBaseSupport implements BaseDao<WeiboReport>
{
	private static String TAB_NAME = "weibo_report";

	@Override
	public void put(final WeiboReport instance)
	{
		ht.execute(TAB_NAME, new TableCallback<Result>()
				{

					@Override
					public Result doInTable(HTableInterface htable) throws Throwable
					{
						Put p = new Put(Bytes.toBytes(instance.getRid()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("reason")
								, Bytes.toBytes(instance.getReason()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("reportuserid")
								, Bytes.toBytes(instance.getReportUserId()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("reportweiboid")
								, Bytes.toBytes(instance.getReportWeiboId()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("dateline")
								, Bytes.toBytes(String.valueOf(instance.getDateline())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("status")
								, Bytes.toBytes(String.valueOf(instance.getStatus())));
						htable.put(p);
						return null;
					}
					
				});
	}

	@Override
	public void delete(final String row)
	{
		ht.execute(TAB_NAME, new TableCallback<Result>()
				{

					@Override
					public Result doInTable(HTableInterface htable)
							throws Throwable
					{
						Delete del = new Delete(Bytes.toBytes(row));
						htable.delete(del);
						return null;
					}
			
				});
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
		Result rst = null;
		if(family == null) // 查询整行
		{
			rst = ht.get(TAB_NAME, row, new RowMapper<Result>()
					{

						@Override
						public Result mapRow(Result result, int rownum)
								throws Exception
						{
							return result;
						}
				
					});
		}
		else
		{
			if(qualifier == null) // 查询指定列族
			{
				rst = ht.get(TAB_NAME, row, family, new RowMapper<Result>()
						{

							@Override
							public Result mapRow(Result result, int rownum)
									throws Exception
							{
								return result;
							}
					
						});
			}
			else // 查询指定列
			{
				rst = ht.get(TAB_NAME, row, family, qualifier, new RowMapper<Result>()
						{

							@Override
							public Result mapRow(Result result, int rownum)
									throws Exception
							{
								return result;
							}
					
						});
			}
		}
		return (rst==null||rst.isEmpty())?null:rst; // 无结果则返回null
	}

}
