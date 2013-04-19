package org.chingo.gutcom.dao;

import java.util.List;
import java.util.Map;

/**
 * Hibernate���������ӿ�
 * @author Chingo.Org
 * 
 * @param <T> ��������
 */
public interface BaseDao<T>
{
	
	/**
	 * ���沢�־û����󣬻������������ݿ�
	 * @param instance Ҫ�����Ķ���
	 * @return ���ز�����������ֵ
	 */
	public java.io.Serializable save(T instance);
	
	/**
	 * ���²��־û�����
	 * @param instance Ҫ�����Ķ���
	 */
	public void update(T instance);
	
	/**
	 * ɾ������
	 * @param instance Ҫ�����Ķ���
	 */
	public void delete(T instance);
	
	/**
	 * ɾ������
	 * @param id Ҫɾ���Ķ����ID
	 */
	public void delete(java.io.Serializable id);
	
	/**
	 * ��ȡ���־û�ָ��ID�Ķ��󣬻������������ݿ�
	 * @param id ����ֵ
	 * @return ��ȡ���Ķ��󣬲�����ʱ����null
	 */
	public T get(java.io.Serializable id);
	
	/**
	 * ��ȡ���ж���
	 * @return ��ȡ���Ķ����б�
	 */
	public List<T> list();
	
	/**
	 * ����HQL�����в�ѯ
	 * @param hql HQL���
	 * @param values ��ѯ������ֵ���޲�������null
	 * @return ��ѯ���
	 */
	public List query(final String hql, final Object[] values);
	
	/**
	 * ����HQL��ҳ��ѯ���ݣ��޲�����
	 * @param hql ��ѯ���
	 * @param offset ��һ����¼������
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @return ��ѯҳ�ļ�¼��
	 */
	public List findByPage(final String hql, final int offset, final int pageSize);
	
	
	/**
	 * ����HQL��ҳ��ѯ���ݣ�����������
	 * @param hql ��ѯ���
	 * @param value ��ѯ����
	 * @param offset ��һ����¼������
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @return ��ѯҳ�ļ�¼��
	 */
	public List findByPage(final String hql, final Object value, final int offset, final int pageSize);
	
	/**
	 * ����HQL��ҳ��ѯ���ݣ����������
	 * @param hql ��ѯ���
	 * @param values ��ѯ��������
	 * @param offset ��һ����¼������
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @return ��ѯҳ�ļ�¼��
	 */
	public List findByPage(final String hql, final Object[] values, final int offset, final int pageSize);
	
	/**
	 * ����HQL��ҳ��ѯ���ݣ����������
	 * @param hql ��ѯ���
	 * @param values ��ѯ��������
	 * @param offset ��һ����¼������
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @return ��ѯҳ�ļ�¼��
	 */
	public List findByPage(final String hql, final Map<String, Object> values, final int offset, final int pageSize);
	
	/**
	 * ����HQL�����в�ѯ
	 * @param hql HQL���
	 * @param values ��ѯ������ֵ���޲�������null
	 * @return ��ѯ���
	 */
	public List query(final String hql, final Map<String, Object> values);
}
