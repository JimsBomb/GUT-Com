package org.chingo.gutcom.common.util;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * 安全Util类
 * @author Chingo.Org
 * 
 */
public class SecurityUtil
{
	/**
	 * 对字符串进行MD5加密
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
	
	/**
	 * 生成基于UUID的唯一访问令牌
	 * @return 访问令牌
	 */
	public final static String createAccessToken()
	{
		// 使用UUID生成令牌，去处-
		String result = UUID.randomUUID().toString().replace("-", "").toUpperCase();
		return result;
	}
}
