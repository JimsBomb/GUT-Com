package org.chingo.gutcom.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.chingo.gutcom.dao.BaseDao;
import org.chingo.gutcom.domain.CommonUser;
import org.chingo.gutcom.hbase.HBaseSupport;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;

public class CommonUserDaoImpl extends HBaseSupport implements BaseDao<CommonUser>
{
	private static String TAB_NAME = "common_user";

	@Override
	public void put(final CommonUser instance)
	{
		ht.execute(TAB_NAME, new TableCallback<Result>()
				{

					@Override
					public Result doInTable(HTableInterface htable) throws Throwable
					{
						Put p = new Put(Bytes.toBytes(instance.getUid()));
						/* family : info */
						p.add(Bytes.toBytes("info"), Bytes.toBytes("nickname")
								, Bytes.toBytes(instance.getNickname()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("email")
								, Bytes.toBytes(instance.getEmail()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("studentnum")
								, Bytes.toBytes(instance.getStudentnum()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("password")
								, Bytes.toBytes(instance.getPassword()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("status")
								, Bytes.toBytes(String.valueOf(instance.getStatus())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("regip")
								, Bytes.toBytes(instance.getRegip()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("regdate")
								, Bytes.toBytes(String.valueOf(instance.getRegdate())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("realname")
								, Bytes.toBytes(instance.getRealname()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("college")
								, Bytes.toBytes(instance.getCollege()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("major")
								, Bytes.toBytes(instance.getMajor()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("classname")
								, Bytes.toBytes(instance.getClassname()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("gender")
								, Bytes.toBytes(String.valueOf(instance.getGender())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("birthyear")
								, Bytes.toBytes(String.valueOf(instance.getBirthyear())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("birthmonth")
								, Bytes.toBytes(String.valueOf(instance.getBirthmonth())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("birthday")
								, Bytes.toBytes(String.valueOf(instance.getBirthday())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("constellation")
								, Bytes.toBytes(instance.getConstellation()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("zodiac")
								, Bytes.toBytes(instance.getZodiac()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("bloodtype")
								, Bytes.toBytes(instance.getBloodtype()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("qq")
								, Bytes.toBytes(instance.getQq()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("selfintro")
								, Bytes.toBytes(instance.getSelfintro()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("avatarurl")
								, Bytes.toBytes(instance.getAvatarurl()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("bigavatarurl")
								, Bytes.toBytes(instance.getBigavatarurl()));
						/* family : status */
						p.add(Bytes.toBytes("info"), Bytes.toBytes("lastlogin")
								, Bytes.toBytes(String.valueOf(instance.getLastlogin())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("lastip")
								, Bytes.toBytes(instance.getLastip()));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("weibocnt")
								, Bytes.toBytes(String.valueOf(instance.getWeibocnt())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("follower")
								, Bytes.toBytes(String.valueOf(instance.getFollower())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("following")
								, Bytes.toBytes(String.valueOf(instance.getFollowing())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("newfollower")
								, Bytes.toBytes(String.valueOf(instance.getNewfollower())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("newmsg")
								, Bytes.toBytes(String.valueOf(instance.getNewmsg())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("newat")
								, Bytes.toBytes(String.valueOf(instance.getNewat())));
						p.add(Bytes.toBytes("info"), Bytes.toBytes("newcomment")
								, Bytes.toBytes(String.valueOf(instance.getNewcomment())));
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
