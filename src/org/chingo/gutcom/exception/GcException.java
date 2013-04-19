package org.chingo.gutcom.exception;

/**
 * 自定义系统异常类
 * @author Chingo.Org
 *
 */
public class GcException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3435244256244168127L;

	public GcException()
	{
		
	}
	
	public GcException(String msg)
	{
		super(msg);
	}
}
