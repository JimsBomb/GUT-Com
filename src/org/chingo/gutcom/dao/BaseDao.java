package org.chingo.gutcom.dao;

import java.util.List;
import java.util.Map;

/**
 * Hibernate基础支持泛型接口
 * @author Chingo.Org
 * 
 * @param <T> 具体类型
 */
public interface BaseDao<T>
{
	
	/**
	 * 保存并持久化新对象
	 * @param instance 要保存的实例
	 * @return 对象键值
	 */
	public java.io.Serializable save(T instance);
	
	/**
	 * 更新并持久化对象
	 * @param instance 要更新的实例
	 */
	public void update(T instance);
	
	/**
	 * 删除对象
	 * @param instance 要删除的实例
	 */
	public void delete(T instance);
	
	/**
	 * 删除对象
	 * @param id 要删除的实例的键值
	 */
	public void delete(java.io.Serializable id);
	
	/**
	 * 获取并持久化对象
	 * @param id 要获取的对象的键值
	 * @return
	 */
	public T get(java.io.Serializable id);
	
	/**
	 * 获取对象全部集合的列表
	 * @return 对象集合
	 */
	public List<T> list();
	
	/**
	 * 根据HQL查询数据
	 * @param hql HQL查询语句
	 * @param values 条件参数集，无则置NULL
	 * @return 查询结果集
	 */
	public List query(final String hql, final Object[] values);
	
	/**
	 * 根据HQL分页查询数据
	 * @param hql HQL查询语句
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示记录数
	 * @return 查询结果集
	 */
	public List findByPage(final String hql, final int offset, final int pageSize);
	
	
	/**
	 * 根据HQL分页查询数据
	 * @param hql HQL查询语句
	 * @param value 单个条件参数值
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示记录数
	 * @return 查询结果集
	 */
	public List findByPage(final String hql, final Object value, final int offset, final int pageSize);
	
	/**
	 * 根据HQL分页查询数据
	 * @param hql HQL语句
	 * @param values 条件参数值数组
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示记录数
	 * @return 查询结果集
	 */
	public List findByPage(final String hql, final Object[] values, final int offset, final int pageSize);
	
	/**
	 * 根据HQL分页查询数据
	 * @param hql HQL查询语句
	 * @param values 条件参数值集合（Map<参数名,参数值 >）
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示记录数
	 * @return 查询结果集
	 */
	public List findByPage(final String hql, final Map<String, Object> values, final int offset, final int pageSize);
	
	/**
	 * 根据HQL查询数据
	 * @param hql HQL查询语句
	 * @param values 条件参数值集合（Map<参数名,参数值 >）
	 * @return 查询结果集
	 */
	public List query(final String hql, final Map<String, Object> values);
}
