package org.chingo.gutcom.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.filechooser.FileFilter;

import org.chingo.gutcom.action.base.BaseAction;
import org.chingo.gutcom.common.constant.ErrorMsg;
import org.chingo.gutcom.common.constant.ResultMsg;
import org.chingo.gutcom.common.util.FormatUtil;
import org.chingo.gutcom.exception.GcException;

public class DbOpAction extends BaseAction
{
	private final String FILENAME_PREFIX ="backup_";
	private final String FILENAME_SUFFIX = ".sql";
	private final String FILE_STORE_PATH = "backup/";
	
	private String act;
	private String fileName;
	private List<DbFile> lstFile;
	
	private String resultMsg;
	private String backTo;
	
	public String getAct()
	{
		return this.act;
	}
	
	public void setAct(String act)
	{
		this.act = act;
	}
	
	public String getFileName()
	{
		return this.fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public List<DbFile> getLstFile()
	{
		findDbFiles();
		return this.lstFile;
	}
	
	public String getResultMsg()
	{
		return this.resultMsg;
	}
	
	public String getBackTo()
	{
		return this.backTo;
	}
	
	/**
	 * 数据库备份文件POJO
	 * @author Chingo.Org
	 *
	 */
	class DbFile
	{
		private String name; // 文件名
		private long time; // 创建时间戳
		private String size; // 文件大小
		
		DbFile(String name, long time, String size)
		{
			this.name = name;
			this.time = time;
			this.size = size;
		}
		
		public String getName()
		{
			return this.name;
		}
		
		public long getTime()
		{
			return this.time;
		}
		
		public String getSize()
		{
			return this.size;
		}
	}
	
	/**
	 * 数据备份/恢复管理Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String mgr() throws Exception
	{
		return "mgr";
	}
	
	/**
	 * 数据备份Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String backup() throws Exception
	{
		if(act!=null && act.equals("backup")) // 为备份操作时
		{
			String rst = doBackUp(); // 执行备份，获取备份文件名
			if(rst != null) // 文件名非空则备份成功
			{
				this.resultMsg = ResultMsg.DB_BACKUP + rst;
			}
			else // 备份失败
			{
				this.resultMsg = ResultMsg.DB_OP_FAILED;
			}

			return "result";
		}
		return "mgr";
	}
	
	/**
	 * 恢复数据Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String restore() throws Exception
	{
		if(act!=null && act.equals("restore")
				&& fileName!=null && !fileName.isEmpty()) // 必要参数正确时
		{
			if(doRestore() == true) // 恢复成功
			{
				this.resultMsg = ResultMsg.DB_RESTORE;
			}
			else // 恢复失败
			{
				this.resultMsg = ResultMsg.DB_OP_FAILED;
			}
			
			return "result";
		}
		
		return "mgr";
	}
	
	/**
	 * 删除备份文件Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String del() throws Exception
	{
		if(act!=null && act.equals("del")
				&& fileName!=null && !fileName.isEmpty()) // 必要参数正确时
		{
			doDel(); // 执行删除
			this.resultMsg = ResultMsg.DB_DEL;
			this.backTo = "dbopmgr.do";
			
			return SUCCESS;
		}
		else // 参数不正确时
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
	}
	
	/**
	 * 数据备份具体操作
	 * @return 备份文件名，返回null则备份失败
	 */
	private String doBackUp()
	{
        try
        {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
        	Random rd = new Random();
            String name = FILENAME_PREFIX + sdf.format(new Date()) + rd.nextLong() + FILENAME_SUFFIX; // 备份文件名
            Runtime rt = Runtime.getRuntime();
            String user = "root"; // 数据库帐号
            String password = "root"; // 登陆密码
            String database = "gutcom"; // 需要备份的数据库名
            String filepath = request.getServletContext().getRealPath("/")
            		+ FILE_STORE_PATH; // 备份的路径地址
            
            File path = new File(filepath);
            if(path.exists() == false)
            {
            	path.mkdirs();
            }
            /* 通过Runtime实现备份 */
            StringBuffer cmd = new StringBuffer();
            cmd.append("mysqldump ").append(" -R -h localhost ")
            .append(" -u ").append(user).append(" -p").append(password)
            .append(" ").append(database)
            // 设置导出编码为utf8。这里必须是utf8
            .append(" --default-character-set=utf8 --triggers -R --hex-blob -x --result-file=") 
            .append(filepath).append(name);
            // 调用 mysql 的 cmd:
            rt.exec(cmd.toString());
            
            return name;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }
	
	/**
	 * 数据恢复具体操作
	 * @return 恢复结果
	 * @throws GcException 自定义异常信息，直接抛出给Struts即可
	 */
	private boolean doRestore() throws GcException
	{
		OutputStream out = null;
		BufferedReader br = null;
		OutputStreamWriter writer = null;
		try 
		{
			String fPath = request.getServletContext().getRealPath("/")
            		+ FILE_STORE_PATH + fileName; // 备份文件路径
			Runtime rt = Runtime.getRuntime();
			String user = "root"; // 数据库帐号 
			String password = "root"; // 登陆密码 
			String database = "gutcom"; // 需要还原的数据库名 
			String sour = "localhost"; // 数据库地址
			// 调用 mysql 的 cmd:
			String st = "mysql -h "+sour+" -u "+user+" -p"+password+" -f "+database;
			Process child = rt.exec(st);
			out = child.getOutputStream();//控制台的输入信息作为输出流
			String inStr;
			StringBuffer sb = new StringBuffer("");
			String outStr;
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream(fPath), "utf8")); // 编码为UTF-8
			/* 读取数据文件 */
			while ((inStr = br.readLine()) != null)
			{
				sb.append(inStr + "\r\n");
			}
			outStr = sb.toString();

			writer = new OutputStreamWriter(out, "utf8");
			writer.write(outStr); // 写入数据库
			// 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免
			writer.flush();
			
			return true;
		}
		catch (FileNotFoundException fnfEx)  // 文件不存在时
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		catch (UnsupportedEncodingException ueEx)
		{
			ueEx.printStackTrace();
		}
		catch (IOException ioEx)
		{
			ioEx.printStackTrace();
		}
		finally // 关闭所有流
		{
			try
			{
				if(out != null)
				{
					out.close();
				}
				if(br != null)
				{
					br.close();
				}
				if(writer != null)
				{
					writer.close();
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
		return false;
	}
	
	/**
	 * 删除文件具体操作
	 */
	private void doDel()
	{
		String fPath = request.getServletContext().getRealPath("/")
        		+ FILE_STORE_PATH + fileName;
		new File(fPath).delete(); // 删除文件
	}
	
	/**
	 * 检索并设置数据库备份文件列表
	 */
	private void findDbFiles()
	{
		String path = request.getServletContext().getRealPath("/")
        		+ FILE_STORE_PATH;
		File folder = new File(path);
		if(folder.exists() && folder.isDirectory()) // 备份目录存在时
		{
			File[] files = folder.listFiles(new FilenameFilter()
			{

				@Override
				public boolean accept(File dir, String name)
				{
					/* 排除目录和非SQL后缀的文件 */
					if(new File(dir, name).isFile())
					{
						int idx = name.lastIndexOf(".");
						if(idx >= 0)
						{
							if(name.substring(idx+1).toLowerCase().equals("sql"))
							{
								return true;
							}
						}
					}
					return false;
				}
				
			});
			
			/* 设置文件列表 */
			this.lstFile = new ArrayList<DbFile>();
			for(File f : files)
			{
				lstFile.add(new DbFile(f.getName(), f.lastModified()
						, FormatUtil.calcFileSize(f.length())));
			}
		}
	}
}