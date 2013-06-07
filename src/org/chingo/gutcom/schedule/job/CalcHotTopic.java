package org.chingo.gutcom.schedule.job;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.struts2.interceptor.ApplicationAware;

public class CalcHotTopic implements ApplicationAware
{
	private Map<String, Object> application; // 服务器上下文Map
	private static int cnt = 0;

	@Override
	public void setApplication(Map<String, Object> application)
	{
		this.application = application;
	}

	class WeiboCntMap extends Mapper<LongWritable, Text, Text, IntWritable>
	{
		private IntWritable i = new IntWritable(1);
		public void map(LongWritable key, Text value, Context context) throws 
		IOException, InterruptedException
		{
			String[] topics = value.toString().split("#");
			for(String topic : topics)
			{
				context.write(new Text(topic), i);
			}
		}
	}
	
	class WeiboCntReduce extends Reducer<Text, IntWritable, Text, LongWritable>
	{
		private LongWritable result = new LongWritable();
		
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws
		IOException, InterruptedException
		{
			long sum = 0;
			for(IntWritable i : values)
			{
				sum += i.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}
	
	class TopicSortMap extends Mapper<LongWritable, Text, LongWritable, Text>
	{
		private LongWritable cnt = new LongWritable();
		private Text topic = new Text();
		
		public void map(LongWritable key, Text value, Context context) throws 
		IOException, InterruptedException
		{
			String[] line = value.toString().trim().split(" ");
			topic.set(line[0]);
			cnt.set(Long.parseLong(line[1]));
			context.write(cnt, topic);
		}
	}
	
	class TopicSortReduce extends TableReducer<LongWritable, Text, NullWritable>
	{
		private LongWritable linenum = new LongWritable(1L);
		
		public void reduce(LongWritable key, Iterable<Text> values, Context context) throws
		IOException, InterruptedException
		{
			for(Text topic : values)
			{
				if(++cnt <= 10)
				{
					Put put = new Put(Bytes.toBytes(String.valueOf(linenum.get())));
					put.add(Bytes.toBytes("info"), Bytes.toBytes("topic_title"), 
							Bytes.toBytes(topic.toString()));
					context.write(NullWritable.get(), put);
				}
				else
				{
					cnt = 0;
					
				}
			}
		}
	}
}
