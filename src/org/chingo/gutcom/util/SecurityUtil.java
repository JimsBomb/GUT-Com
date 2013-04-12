package org.chingo.gutcom.util;

import java.security.MessageDigest;

/**
 * 安全工具类
 * @author TNT
 * 
 */
public class SecurityUtil
{
	/**
	 * 进行MD5加密
	 * @param string 要加密的字符串
	 * @return 加密后的字符串
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
