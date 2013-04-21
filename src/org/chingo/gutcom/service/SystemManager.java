package org.chingo.gutcom.service;

import java.util.List;
import java.util.Map;

import org.chingo.gutcom.domain.CommonFilterWord;
import org.chingo.gutcom.domain.CommonSysconf;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.exception.GcException;

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
	 * ��������������
	 * @param confs Ҫ���µ����ö���
	 */
	public void updateConf(Map<String, String> confs);
	
	/**
	 * ɾ��������
	 * @param conf Ҫɾ������ö����ID
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
	 * @param ids Ҫɾ���ϵͳ��־���ID����
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
	
	/********** ���˹ؼ�ʹ��� **********/
	/**
	 * �����ؼ��
	 * @param word Ҫ�����Ĺؼ�ʶ���
	 */
	public void addFilterWord(CommonFilterWord word);
	
	/**
	 * 通过文件批量导入过滤关键词
	 * @param filePath 导入文件的绝对路径
	 */
	public void addFilterWord(String filePath) throws GcException;
	
	/**
	 * ���¹ؼ��
	 * @param word Ҫ���µĹؼ�ʶ���
	 */
	public void updateFilterWord(CommonFilterWord word);
	
	/**
	 * ɾ��ؼ��
	 * @param word Ҫɾ��Ĺؼ�ʶ���
	 */
	public void delFilterWord(CommonFilterWord word);
	
	/**
	 * ���IDɾ��ؼ��
	 * @param id Ҫɾ��Ĺؼ�ʵ�ID
	 */
	public void delFilterWord(java.io.Serializable id);
	
	/**
	 * ���ID����ɾ��ؼ��
	 * @param ids Ҫɾ��Ĺؼ�ʵ�ID����
	 */
	public void delFilterWord(java.io.Serializable[] ids);
	
	/**
	 * ��ȡ���йؼ��
	 * @return ȫ���ؼ���б�
	 */
	public List<CommonFilterWord> findAllFilterWord();
	
	/**
	 * ��������ҳ��ѯ�ؼ��
	 * @param values ��������
	 * @param offset ��һ����¼������
	 * @param pageSize ÿҳ��ʾ�ļ�¼��
	 * @return List�еĵ�һ������Ϊ��ѯҳ�ļ�¼�����ڶ�������Ϊ�����
	 */
	public List findFilterWordByPage(Map<String, Object> values, int offset, int pageSize);
	
}
