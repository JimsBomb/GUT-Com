package org.chingo.gutcom.common.util;

/**
 * 数据检查/验证Util类
 * @author Chingo.Org
 *
 */
public class VerifyUtil
{
	/**
	 * 双字节字符匹配正则表达式
	 */
	private static final String REGX_DOUBLE_BYTE = "^[^\\x00-\\xff]{1}$";
	/**
	 * 昵称格式校验正则表达式
	 */
	private static final String REGX_NICKNAME = "(^[a-zA-Z]{1}([a-zA-Z0-9_]){4,14}|" 
			+ "(^[\\u4E00-\\uFA29]{1}[a-zA-Z0-9\\u4E00-\\uFA29]{2,7}))$";
	/**
	 * 邮箱格式校验正则表达式
	 */
	private static final String REGX_EMAIL = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@" 
			+ "([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|" 
			+ "(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$";
	/**
	 * 密码格式校验正则表达式
	 */
	private static final String REGX_PWD = "[A-Za-z0-9_!@#$%^&*\\(\\)\\.+=,/?-]{6,20}";
	/**
	 * QQ号格式校验正则表达式
	 */
	private static final String REGX_QQ = "^[0-9]{1,12}$";
	/**
	 * 消息内容格式校验正则表达式
	 */
	private static final String REGX_MSG_CONTENT = "^.{1,300}$";
	/**
	 * 微博内容格式校验正则表达式
	 */
	private static final String REGX_WEIBO_CONTENT = "^.{1,140}$";
	/**
	 * 关注用户备注名格式校验正则表达式
	 */
	private static final String REGX_FOLLOW_REMARK = "^.{1,10}$";
	/**
	 * 举报理由格式校验正则表达式
	 */
	private static final String REGX_REPORT_REASON = "^.{1,10}$";
	/**
	 * 搜索关键词格式校验正则表达式
	 */
	private static final String REGX_SEARCH_KEYWORD = "^.{1,100}$";
	
	/**
	 * 统计字符数，计算是否超过上限值（可选）
	 * @param content 要统计字符数的内容
	 * @param max 字符数上限值，不限制则设为0
	 * @return 字符数，有上限且超过上限时，返回-1
	 */
	public static final int countWord(String content, int max)
	{
        int result=0;
        for(int i=0; i<content.length(); i++){
        	String b = Character.toString(content.charAt(i));
        	if(b.matches(REGX_DOUBLE_BYTE))
        	{
        		result += 2;
        	}
        	else
        	{
        		result++;
        	}
        	if(max!=0 && result>max)
        	{
        		return -1;
        	}
        }
        
        return result;
	}
	
	/**
	 * 检查密码格式
	 * @param pwd 密码（明文）
	 * @return true-格式正确，false-格式不正确
	 */
	public static final boolean checkPwd(String pwd)
	{
		if(pwd.matches(REGX_PWD)) // 匹配正则表达式时
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 检查邮箱格式
	 * @param email 邮箱地址
	 * @return true-格式正确，false-格式不正确
	 */
	public static final boolean checkEmail(String email)
	{
		// 邮箱长度不超过50且匹配正则表达式时
		if(email.length()<=50 && email.matches(REGX_EMAIL))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 检查昵称格式
	 * @param nickname 昵称
	 * @return true-格式正确，false-格式不正确
	 */
	public static final boolean checkNickname(String nickname)
	{
		if(nickname.matches(REGX_NICKNAME)) // 匹配正则表达式时
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 检查生日日期格式
	 * @param birth 生日日志yyyy-MM-dd
	 * @return true-格式正确，false-格式不正确
	 */
	public static final boolean checkBirth(String birth)
	{
		try
		{
			String[] birthDate = birth.split("-");
			int year = Integer.parseInt(birthDate[0]);
			byte month = Byte.parseByte(birthDate[1]);
			byte day = Byte.parseByte(birthDate[2]);
			if(year>=1900 && year<=9999
					&& month>=1 && month<=12
					&& day>=1 && day<=31)
			{
				return true;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**
	 * 检查QQ号格式
	 * @param qq QQ号
	 * @return true-格式正确，false-格式不正确
	 */
	public static final boolean checkQq(String qq)
	{
		if(qq.matches(REGX_QQ)) // QQ号匹配格式时
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 检查自我简介格式
	 * @param selfintro 自我简介
	 * @return true-格式正确，false-格式不正确
	 */
	public static final boolean checkSelfIntro(String selfintro)
	{
		if(countWord(selfintro, 400) > 0) // 字数不超过上限400个字符时
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 检查消息内容格式
	 * @param content 消息内容
	 * @return true-格式正确，false-格式不正确
	 */
	public static final boolean checkMsgContent(String content)
	{
		if(countWord(content, 600) > 0) // 字数不超过600个字符时content.matches(REGX_MSG_CONTENT))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 检查微博内容格式
	 * @param content 微博内容
	 * @return true-格式正确，false-格式不正确
	 */
	public static final boolean checkWeiboContent(String content)
	{
		
		if(countWord(content, 280) > 0) // 字数不超过280个字符时，content.matches(REGX_WEIBO_CONTENT))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 检查关注用户的备注名格式
	 * @param remark 备注名
	 * @return true-格式正确，false-格式不正确
	 */
	public static final boolean checkRemark(String remark)
	{
		if(countWord(remark, 20) > 0) // 字数不超过20个字符时 remark.matches(REGX_FOLLOW_REMARK))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 检查举报理由格式
	 * @param reason 举报理由
	 * @return true-格式正确，false-格式不正确
	 */
	public static final boolean checkReportReason(String reason)
	{
		if(countWord(reason, 200) > 0) // 字数不超过200个字符时 reason.matches(REGX_REPORT_REASON))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 检查搜索关键词格式
	 * @param keyword 关键词
	 * @return true-格式正确，false-格式不正确
	 */
	public static final boolean checkSearchKeyword(String keyword)
	{
		if(countWord(keyword, 200) > 0) // 字数不超过200个字符时 keyword.matches(REGX_SEARCH_KEYWORD))
		{
			return true;
		}
		return false;
	}
}
