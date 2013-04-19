package org.chingo.gutcom.hibernate4.support;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Hibernate支持类，提供SessionFactory和Session，并实现条件、分页查询
 * @author Chingo.Org
 *
 */
public class HDaoSupport
{
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Session getSession()
	{
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 根据HQL分页查询数据（无参数）
	 * @param hql 查询语句
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示的记录数
	 * @return 查询页的记录集
	 */
	public List findByPage(final String hql, final int offset, final int pageSize)
	{
		List lst = getSession().createQuery(hql)
				.setFirstResult(offset)
				.setMaxResults(pageSize)
				.list();
		
		return lst;
	}
	
	/**
	 * 根据HQL分页查询数据（单个参数）
	 * @param hql 查询语句
	 * @param value 查询参数
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示的记录数
	 * @return 查询页的记录集
	 */
	public List findByPage(final String hql, final Object value,
			final int offset, final int pageSize)
	{
		List lst = getSession().createQuery(hql)
				.setParameter(0, value)
				.setFirstResult(offset)
				.setMaxResults(pageSize)
				.list();
		
		return lst;
	}
	
	/**
	 * 根据HQL分页查询数据（多个参数）
	 * @param hql 查询语句
	 * @param values 查询参数数组
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示的记录数
	 * @return 查询页的记录集
	 */
	public List findByPage(final String hql, final Object[] values,
			final int offset, final int pageSize)
	{
		Query query = getSession().createQuery(hql);
		for(int i=0; i<values.length; i++)
		{
			query.setParameter(i, values[i]);
		}
		List lst = query.setFirstResult(offset)
				.setMaxResults(pageSize)
				.list();
		
		return lst;
	}
	
	/**
	 * 根据HQL语句进行查询
	 * @param hql HQL语句
	 * @param values 查询语句参数值，无参数则置null
	 * @return 查询结果
	 */
	public List query(final String hql, final Object[] values)
	{
		Query query = getSession().createQuery(hql);
		if(values != null)
		{
			for(int i=0; i<values.length; i++)
			{
				query.setParameter(i, values[i]);
			}
		}
		List lst = query.list();
		
		return lst;
	}
	
	/**
	 * 根据HQL分页查询数据（多个参数）
	 * @param hql 查询语句
	 * @param values 查询参数数组
	 * @param offset 第一条记录的索引
	 * @param pageSize 每页显示的记录数
	 * @return 查询页的记录集
	 */
	public List findByPage(final String hql, final Map<String, Object> values, final int offset, final int pageSize)
	{
		Query query = getSession().createQuery(hql);
		for(Entry e : values.entrySet())
		{
			query.setParameter(e.getKey().toString(), e.getValue());
		}
		List lst = query.setFirstResult(offset)
		.setMaxResults(pageSize)
		.list();
		
		return lst;
	}
	
	/**
	 * 根据HQL语句进行查询
	 * @param hql HQL语句
	 * @param values 查询语句参数值，无参数则置null
	 * @return 查询结果
	 */
	public List query(final String hql, final Map<String, Object> values)
	{
		Query query = getSession().createQuery(hql);
		for(Entry e : values.entrySet())
		{
			query.setParameter(e.getKey().toString(), e.getValue());
		}
		List lst = query.list();
		
		return lst;
	}
}
