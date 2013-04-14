package org.chingo.gutcom.dao;

import java.util.List;

/**
 * Hibernate���������ӿ�
 * @author Chingo.Org
 * 
 * @param <T> ��������
 */
public interface IBasicHibernateDao<T>
{
	/**
	 * ���沢�־û����󣬲����������������ݿ�
	 * @param instance Ҫ�����Ķ���
	 */
	public void persist(T instance);
	
	/**
	 * ���沢�־û����󣬻������������ݿ�
	 * @param instance Ҫ�����Ķ���
	 * @return ���ز�����������ֵ
	 */
	public java.io.Serializable save(T instance);
	
	/**
	 * �Զ��жϱ�����Ǹ��²��־û�����
	 * @param instance Ҫ�����Ķ���
	 */
	public void saveOrUpdate(T instance);
	
	/**
	 * ���²��־û�����
	 * @param instance Ҫ�����Ķ���
	 */
	public void update(T instance);
	
	/**
	 * ���µ����־û�����
	 * @param instance Ҫ�����Ķ���
	 * @return ���º�Ķ���ʵ��
	 */
	public T merge(T instance);
	
	/**
	 * ɾ������
	 * @param instance Ҫ�����Ķ���
	 */
	public void delete(T instance);
	
	/**
	 * ��ȡ���־û�ָ��ID�Ķ��󣬻������������ݿ�
	 * @param c ��
	 * @param id ����ֵ
	 * @return ��ȡ���Ķ��󣬲�����ʱ����null
	 */
	public T get(Class<T> c, java.io.Serializable id);
	
	/**
	 * ��ȡ���־û�ָ��ID�Ķ��󣬲��������������ݿ�
	 * @param c ��
	 * @param id ����ֵ
	 * @return ��ȡ���Ķ��󣬲�����ʱ���ܷ���һ��δ��ʼ���Ĵ������
	 */
	public T load(Class<T> c, java.io.Serializable id);
	
	/**
	 * ��ȡ���ж���
	 * @return ��ȡ���Ķ����б�
	 */
	public List<T> list();
	
	/**
	 * ����Hql��ȡ����
	 * @param hql Hql���
	 * @return ��ȡ���Ķ����б�
	 */
	public List<T> fetchByHql(String hql);
}
