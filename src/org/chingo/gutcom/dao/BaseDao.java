package org.chingo.gutcom.dao;

import java.util.List;
import java.util.Map;

/**
 * Hibernate基本操作接口
 * @author Chingo.Org
 * 
 * @param <T> 泛型类名
 */
public interface BaseDao<T>
{
	
	/**
	 * 保存并持久化对象，会立即插入数据库
	 * @param instance 要操作的对象
	 * @return 返回插入对象的主键值
	 */
	public java.io.Serializable save(T instance);
	
	/**
	 * 更新并持久化对象
	 * @param instance 要操作的对象
	 */
	public void update(T instance);
	
	/**
	 * 删除对象
	 * @param instance 要操作的对象
	 */
	public void delete(T instance);
	
	/**
	 * 删除对象
	 * @param id 要删除的对象的ID
	 */
	public void delete(java.io.Serializable id);
	
	/**
	 * 获取并持久化指定ID的对象，会立即访问数据库
	 * @param id 主键值
	 * @return 获取到的对象，不存在时返回null
	 */
	public T get(java.io.Serializable id);
	
	/**
	 * 获取所有对象
	 * @return 获取到的对象列表
	 */
	public List<T> list();
	
	/**
	 * 根据HQL语句进行查询
	 * @param hql HQL语句
	 * @param values 查询语句参数值，无参数则置null
	 * @return 查询结果
	 */
	public List query(final String hql, final Object[] values);
	
	/**
	 * 根据HQL分页查询数据（无参数）
	 * @param hql 查询语句
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示的记录数
	 * @return 查询页的记录集
	 */
	public List findByPage(final String hql, final int offset, final int pageSize);
	
	
	/**
	 * 根据HQL分页查询数据（单个参数）
	 * @param hql 查询语句
	 * @param value 查询参数
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示的记录数
	 * @return 查询页的记录集
	 */
	public List findByPage(final String hql, final Object value, final int offset, final int pageSize);
	
	/**
	 * 根据HQL分页查询数据（多个参数）
	 * @param hql 查询语句
	 * @param values 查询参数数组
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示的记录数
	 * @return 查询页的记录集
	 */
	public List findByPage(final String hql, final Object[] values, final int offset, final int pageSize);
	
	/**
	 * 根据HQL分页查询数据（多个参数）
	 * @param hql 查询语句
	 * @param values 查询参数数组
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示的记录数
	 * @return 查询页的记录集
	 */
	public List findByPage(final String hql, final Map<String, Object> values, final int offset, final int pageSize);
	
	/**
	 * 根据HQL语句进行查询
	 * @param hql HQL语句
	 * @param values 查询语句参数值，无参数则置null
	 * @return 查询结果
	 */
	public List query(final String hql, final Map<String, Object> values);
}
