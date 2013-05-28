package org.chingo.gutcom.hbase;

import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;

/**
 * HBase数据库操作支持类，主要用于实现分页和表运算查询
 * @author Chingo.Org
 * 
 */
public class HBaseSupport
{
	protected HbaseTemplate ht; // Spring提供的HBase操作支持类
	
	public void setHt(HbaseTemplate ht)
	{
		this.ht = ht;
	}
	
	/**
	 * 分页查询数据（待解决：由于PageFilter本身的限制，当数据分散在不同的region server时，不保证返回的结果满足指定的单页显示记录数）
	 * @param table 要查询的表名
	 * @param fl 查询条件过滤器
	 * @param startRow 开始查询的第一行的row值
	 * @param pageSize 每页显示记录数，为记录下一次（页）查询的第一行的row值，该参数应该比实际值+1
	 * @return 查询结果集，如果还有下一页，则会在List末端额外多返回一条结果，以便记录下次查询的第一行的row值
	 */
	public List<Result> findByPage(final String table
			, final FilterList fl, final String startRow, final int pageSize)
	{
		Scan scan = new Scan();
		if(fl != null)
		{
			scan.setFilter(fl); // 设置过滤器
		}
		if(startRow != null)
		{
			scan.setStartRow(Bytes.toBytes(startRow)); // 设置第一行row值
		}
		/* 扫描表并返回结果 */
		List<Result> rst = ht.find(table, scan, new RowMapper<Result>()
		{

			@Override
			public Result mapRow(Result result, int rowNum) throws Exception
			{
				return result;
			}
			
		});
		
		return (rst==null||rst.size()==0)?null:rst; // 无数据则返回null
	}
	
	/**
	 * 统计行数
	 * @param table 要统计的表名
	 * @param fl 查询条件过滤器
	 * @return 总行数
	 * @throws Throwable
	 */
	public long rowCount(final String table, final FilterList fl) throws Throwable
	{
		Scan scan = new Scan();
		if(fl != null)
		{
			scan.setFilter(fl);
		}
		AggregationClient ac = new AggregationClient(ht.getConfiguration());
		return ac.rowCount(Bytes.toBytes(table), null, scan);
	}
	
	/**
	 * 对值进行求和
	 * @param table 要进行操作的表名
	 * @param fl 查询条件过滤器
	 * @return 结果值
	 * @throws Throwable
	 */
	public Object sum(final String table, final FilterList fl) throws Throwable
	{
		Scan scan = new Scan();
		if(fl != null)
		{
			scan.setFilter(fl);
		}
		AggregationClient ac = new AggregationClient(ht.getConfiguration());
		return ac.sum(Bytes.toBytes(table), null, scan);
	}
	
	/**
	 * 求最大值
	 * @param table 要进行操作的表名
	 * @param fl 查询条件过滤器
	 * @return 最大值
	 * @throws Throwable
	 */
	public Object max(final String table, final FilterList fl) throws Throwable
	{
		Scan scan = new Scan();
		if(fl != null)
		{
			scan.setFilter(fl);
		}
		AggregationClient ac = new AggregationClient(ht.getConfiguration());
		return ac.max(Bytes.toBytes(table), null, scan);
	}
	
	/**
	 * 求最小值
	 * @param table 要进行操作的表名
	 * @param fl 查询条件过滤器
	 * @return 最小值
	 * @throws Throwable
	 */
	public Object min(final String table, final FilterList fl) throws Throwable
	{
		Scan scan = new Scan();
		if(fl != null)
		{
			scan.setFilter(fl);
		}
		AggregationClient ac = new AggregationClient(ht.getConfiguration());
		return ac.min(Bytes.toBytes(table), null, scan);
	}
}
