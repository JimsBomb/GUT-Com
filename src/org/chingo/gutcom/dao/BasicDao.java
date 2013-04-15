package org.chingo.gutcom.dao;

import java.util.List;

/**
 * Hibernate基本操作接口
 * @author Chingo.Org
 * 
 * @param <T> 泛型类名
 */
public interface BasicDao<T>
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
}
