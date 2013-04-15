package org.chingo.gutcom.dao;

import java.util.List;

/**
 * Hibernate���������ӿ�
 * @author Chingo.Org
 * 
 * @param <T> ��������
 */
public interface BasicDao<T>
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
}
