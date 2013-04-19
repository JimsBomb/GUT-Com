package org.chingo.gutcom.service;

import java.util.List;
import java.util.Map;

import org.chingo.gutcom.domain.CommonSysconf;
import org.chingo.gutcom.domain.CommonSyslog;

public interface SystemManager
{
	/********** ϵͳ���ù��� **********/
	/**
	 * ����������
	 * @param conf Ҫ��ӵ����ö���
	 */
	public void addConf(CommonSysconf conf);
	
	/**
	 * ����������
	 * @param conf Ҫ���µ����ö���
	 */
	public void updateConf(CommonSysconf conf);
	
	/**
	 * ɾ��������
	 * @param conf Ҫɾ�������ö����ID
	 */
	public void delConf(java.io.Serializable id);
	
	/**
	 * ��ȡ����������
	 * @return �������
	 */
	public List<CommonSysconf> findAllConf();
	
	
	/********** ϵͳ��־���� **********/
	/**
	 * ����ϵͳ��־��
	 * @param log Ҫ��ӵ�ϵͳ��־����
	 */
	public void addSyslog(CommonSyslog log);
	
	/**
	 * ����ɾ��ϵͳ��־��
	 * @param ids Ҫɾ����ϵͳ��־���ID����
	 */
	public void delSyslog(java.io.Serializable[] ids);
	
	/**
	 * ��ȡ����ϵͳ��־��
	 * @return ϵͳ��־���
	 */
	public List<CommonSyslog> findAllSyslog();
	
	/**
	 * ��ѯ��־��¼����
	 * @return ��־��¼����
	 */
	public long getSyslogTotalSize();
	
	/**
	 * ��ҳ��ȡϵͳ��־��
	 * @param offset ��һ����¼������
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @return ��ѯҳ�ļ�¼��
	 */
	public List<CommonSyslog> findSyslogByPage(int offset, int pageSize);
	
	/**
	 * ��������ҳ��ȡϵͳ��־��
	 * @param values ��������ֵ
	 * @param offset ��һ����¼������
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @return List�еĵ�һ������Ϊ��ѯҳ�ļ�¼�����ڶ�������Ϊ�����
	 */
	public List findSyslogByPage(Map<String, Object> values, int offset, int pageSize);
	
}
