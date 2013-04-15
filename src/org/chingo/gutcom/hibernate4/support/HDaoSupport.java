package org.chingo.gutcom.hibernate4.support;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Hibernate֧���࣬�ṩSessionFactory��Session����ʵ�ַ�ҳ��ѯ
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
	 * ����HQL��ҳ��ѯ���ݣ��޲�����
	 * @param hql ��ѯ���
	 * @param offset ��һ����¼������
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @return ��ѯҳ�ļ�¼��
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
	 * ����HQL��ҳ��ѯ���ݣ�����������
	 * @param hql ��ѯ���
	 * @param value ��ѯ����
	 * @param offset ��һ����¼������
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @return ��ѯҳ�ļ�¼��
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
	 * ����HQL��ҳ��ѯ���ݣ����������
	 * @param hql ��ѯ���
	 * @param values ��ѯ��������
	 * @param offset ��һ����¼������
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @return ��ѯҳ�ļ�¼��
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
}
