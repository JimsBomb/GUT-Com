package org.chingo.gutcom.common.util;

import java.security.MessageDigest;

/**
 * ��ȫ������
 * @author TNT
 * 
 */
public class SecurityUtil
{
	/**
	 * ����MD5����
	 * @param string Ҫ���ܵ��ַ�
	 * @return ���ܺ���ַ�
	 */
	public final static String md5(String string) {
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            byte[] md = mdTemp.digest(string.getBytes());
            String s = "";
            for (int i = 0; i < md.length; i++) {
                s += "0123456789ABCDEF".charAt(md[i] >>> 4 & 0xf);
                s += "0123456789ABCDEF".charAt(md[i] & 0xf);
            }
            return s;
        } catch (Exception e) {
            return null;
        }
    }
}
