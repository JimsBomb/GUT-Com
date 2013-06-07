package org.chingo.gutcom.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.WeiboContent;
import org.chingo.gutcom.hbase.HBaseSupport;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;

public class WeiboContentDaoImpl extends HBaseSupport implements BaseDao<WeiboContent>
{
	private static String TAB_NAME = "weibo_content";

	@Override
	public void put(final WeiboContent instance)
	{
		ht.execute(TAB_NAME, new TableCallback<Result>()
				{

					@Override
					public Result doInTable(HTableInterface htable) throws Throwable
					{
						Put p = new Put(Bytes.toBytes(instance.getWid()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("content")
								, Bytes.toBytes(instance.getContent()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("type")
								, Bytes.toBytes(String.valueOf(instance.getFormat())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("visibility")
								, Bytes.toBytes(String.valueOf(instance.getVisibility())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("format")
								, Bytes.toBytes(String.valueOf(instance.getFormat())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("status")
								, Bytes.toBytes(String.valueOf(instance.getStatus())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("thumbnail_pic")
								, Bytes.toBytes(instance.getThumbnailPic()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("middle_pic")
								, Bytes.toBytes(instance.getMiddlePic()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("original_pic")
								, Bytes.toBytes(instance.getOriginalPic()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("dateline")
								, Bytes.toBytes(String.valueOf(instance.getDateline())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("authorid")
								, Bytes.toBytes(instance.getAuthorid()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("sourceid")
								, Bytes.toBytes(instance.getSourceid()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("source_content")
								, Bytes.toBytes(instance.getSourceContent()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("topic_titles")
								, Bytes.toBytes(instance.getTopicTitles()));
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
