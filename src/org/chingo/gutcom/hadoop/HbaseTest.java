package org.chingo.gutcom.hadoop;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.TableCallback;

public class HbaseTest
{
	private HbaseTemplate ht;
	
	public void setHt(HbaseTemplate ht)
	{
		this.ht = ht;
	}
	
	public void test()
	{
		ht.execute("springtest", new TableCallback<Object>()
				{

					@Override
					public Object doInTable(HTableInterface table)
							throws Throwable
					{
						Put p = new Put(Bytes.toBytes("row1"));
						p.add(Bytes.toBytes("f1"), Bytes.toBytes("c1"), Bytes.toBytes("val11"));
						table.put(p);
						return null;
					}
			
				});
	}
}
