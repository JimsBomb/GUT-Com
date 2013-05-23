package org.chingo.gutcom.dao;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.FilterList;

/**
 * HBase数据库基础支持泛型接口
 * @author Chingo.Org
 * 
 * @param <T> 具体类型
 */
public interface BaseDao<T>
{
	/**
	 * 追加数据（新增、更新）
	 * @param instance 要追加的对象实例
	 */
	public void put(final T instance);
	
	/**
	 * 删除指定行
	 * @param row 要删除的行的rowKey
	 */
	public void delete(final String row);
	
	/**
	 * 批量删除指定行
	 * @param rows 要删除的行的rowKey集
	 */
	public void delete(final List<String> rows);
	
	/**
	 * 查询指定行、列族、列的值
	 * @param row 要查询的rowKey
	 * @param family 指定列族，不指定列族（即整行）则置null
	 * @param qualifier 指定列（此时也必须指定family），不指定列（即全部列）则置null
	 * @return 查询结果，无则返回null
	 */
	public Result get(final String row, final String family, final String qualifier);
	
	/**
	 * 分页查询数据（待解决：由于PageFilter本身的限制，当数据分散在不同的region server时，不保证返回的结果满足指定的单页显示记录数）
	 * @param table 要查询的表名
	 * @param fl 查询条件过滤器
	 * @param startRow 开始查询的第一行的row值
	 * @param pageSize 每页显示记录数，为记录下一次（页）查询的第一行的row值，该参数应该比实际值+1
	 * @return 查询结果集，如果还有下一页，则会在List末端额外多返回一条结果，以便记录下次查询的第一行的row值。无则返回null
	 */
	public List<Result> findByPage(final String table
			, final FilterList fl, final String startRow, final int pageSize);
	
	/**
	 * 统计行数
	 * @param table 要统计的表名
	 * @param fl 查询条件过滤器
	 * @return 总行数
	 * @throws Throwable
	 */
	public long rowCount(final String table, final FilterList fl) throws Throwable;
//	/**
//	 * 保存并持久化新对象
//	 * @param instance 要保存的实例
//	 * @return 对象键值
//	 */
//	public java.io.Serializable save(T instance);
//	
//	/**
//	 * 更新并持久化对象
//	 * @param instance 要更新的实例
//	 */
//	public void update(T instance);
//	
//	/**
//	 * 删除对象
//	 * @param instance 要删除的实例
//	 */
//	public void delete(T instance);
//	
//	/**
//	 * 删除对象
//	 * @param id 要删除的实例的键值
//	 */
//	public void delete(java.io.Serializable id);
//	
//	/**
//	 * 获取并持久化对象
//	 * @param id 要获取的对象的键值
//	 * @return
//	 */
//	public T get(java.io.Serializable id);
//	
//	/**
//	 * 获取对象全部集合的列表
//	 * @return 对象集合
//	 */
//	public List<T> list();
//	
//	/**
//	 * 根据HQL分页查询数据
//	 * @param hql HQL查询语句
//	 * @param offset 第一条记录的索引
//	 * @param pageSize 每页显示记录数
//	 * @return 查询结果集
//	 */
//	public List findByPage(final String hql, final int offset, final int pageSize);
//	
//	
//	/**
//	 * 根据HQL分页查询数据
//	 * @param hql HQL查询语句
//	 * @param value 单个条件参数值
//	 * @param offset 第一条记录的索引
//	 * @param pageSize 每页显示记录数
//	 * @return 查询结果集
//	 */
//	public List findByPage(final String hql, final Object value, final int offset, final int pageSize);
//	
//	/**
//	 * 根据HQL分页查询数据
//	 * @param hql HQL语句
//	 * @param values 条件参数值数组
//	 * @param offset 第一条记录的索引
//	 * @param pageSize 每页显示记录数
//	 * @return 查询结果集
//	 */
//	public List findByPage(final String hql, final Object[] values, final int offset, final int pageSize);
//	
//	/**
//	 * 根据HQL分页查询数据
//	 * @param hql HQL查询语句
//	 * @param values 条件参数值集合（Map<参数名,参数值 >）
//	 * @param offset 第一条记录的索引
//	 * @param pageSize 每页显示记录数
//	 * @return 查询结果集
//	 */
//	public List findByPage(final String hql, final Map<String, Object> values, final int offset, final int pageSize);
//	
//	/**
//	 * 根据HQL查询数据
//	 * @param hql HQL查询语句
//	 * @param values 条件参数值集合（Map<参数名,参数值 >）
//	 * @return 查询结果集
//	 */
//	public List query(final String hql, final Map<String, Object> values);
}
