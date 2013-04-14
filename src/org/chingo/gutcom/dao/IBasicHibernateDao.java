package org.chingo.gutcom.dao;

import java.util.List;

/**
 * Hibernate基本操作接口
 * @author Chingo.Org
 * 
 * @param <T> 泛型类名
 */
public interface IBasicHibernateDao<T>
{
	/**
	 * 保存并持久化对象，并不会立即插入数据库
	 * @param instance 要操作的对象
	 */
	public void persist(T instance);
	
	/**
	 * 保存并持久化对象，会立即插入数据库
	 * @param instance 要操作的对象
	 * @return 返回插入对象的主键值
	 */
	public java.io.Serializable save(T instance);
	
	/**
	 * 自动判断保存或是更新并持久化对象
	 * @param instance 要操作的对象
	 */
	public void saveOrUpdate(T instance);
	
	/**
	 * 更新并持久化对象
	 * @param instance 要操作的对象
	 */
	public void update(T instance);
	
	/**
	 * 更新但不持久化对象
	 * @param instance 要操作的对象
	 * @return 更新后的对象实例
	 */
	public T merge(T instance);
	
	/**
	 * 删除对象
	 * @param instance 要操作的对象
	 */
	public void delete(T instance);
	
	/**
	 * 获取并持久化指定ID的对象，会立即访问数据库
	 * @param c 类
	 * @param id 主键值
	 * @return 获取到的对象，不存在时返回null
	 */
	public T get(Class<T> c, java.io.Serializable id);
	
	/**
	 * 获取并持久化指定ID的对象，不会立即访问数据库
	 * @param c 类
	 * @param id 主键值
	 * @return 获取到的对象，不存在时可能返回一个未初始化的代理对象
	 */
	public T load(Class<T> c, java.io.Serializable id);
	
	/**
	 * 获取所有对象
	 * @return 获取到的对象列表
	 */
	public List<T> list();
	
	/**
	 * 根据Hql获取对象
	 * @param hql Hql语句
	 * @return 获取到的对象列表
	 */
	public List<T> fetchByHql(String hql);
}
