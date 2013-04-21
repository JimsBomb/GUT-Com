package org.chingo.gutcom.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.chingo.gutcom.exception.GcException;

public class UploadFileUtil
{
	public static String upload(File file, String fileName, String uploadPath) throws GcException
	{
		InputStream is = null;
		OutputStream os = null;
		File toFile = null;
		try
		{
			is = new FileInputStream(file);
			toFile = new File(uploadPath, fileName);
			if(toFile.exists() == false)
			{
				toFile.createNewFile();
			}
			os = new FileOutputStream(toFile);
			byte[] buffer = new byte[1024];
			int length = 0;
			while((length=is.read(buffer)) > 0)
			{
				os.write(buffer, 0, length);
			}
		} catch(FileNotFoundException fnfex)
		{
			throw new GcException("导入失败。路径错误或文件不存在。");
		} catch(IOException ioex)
		{
			throw new GcException("导入失败。文件读写异常，请联系管理员。");
		} finally
		{
			try
			{
				if(is != null)
				{
					is.close();
				}
				if(os != null)
				{
					os.close();
				}
			} catch(IOException ioex)
			{
				throw new GcException("读写流操作异常，请联系管理员。");
			}
		}
		return toFile.getAbsolutePath();
	}
	
	public static void delete(String filePath) throws GcException
	{
		try
		{
			File f = new File(filePath);
			if(f != null)
			{
				f.delete();
			}
		} catch(Exception ex)
		{

		}
	}
}
