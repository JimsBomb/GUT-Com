package org.chingo.gutcom.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.chingo.gutcom.action.base.SystemBaseAction;
import org.chingo.gutcom.common.util.UploadFileUtil;
import org.chingo.gutcom.domain.CommonFilterWord;
import org.chingo.gutcom.exception.GcException;

public class FilterWordAction extends SystemBaseAction
{
	private List<CommonFilterWord> lstWord;
	private long totalSize = 0;
	private int pageCount = 1;
	private int pageSize;
	private int searchMode = 0;
	private String word;
	private byte level = -1;
	
	private File importFile;
	private String importFileContentType;
	private String importFileFileName;
	
	public int getPageSize()
	{
		return pageSize;
	}
	public void setPageSize()
	{
		int sizePerPage = Integer.parseInt(getConfigurations().get("RECORDS_PER_PAGE"));
		this.pageSize = (int) (this.totalSize / sizePerPage);
		if(this.totalSize % sizePerPage > 0)
		{
			this.pageSize++;
		}
	}
	public int getSearchMode()
	{
		return searchMode;
	}
	public void setSearchMode(int searchMode)
	{
		this.searchMode = searchMode;
	}
	public String getWord()
	{
		return word;
	}
	public void setWord(String word)
	{
		this.word = word;
	}
	public byte getLevel()
	{
		return level;
	}
	public void setLevel(byte level)
	{
		this.level = level;
	}
	public List<CommonFilterWord> getLstWord()
	{
		return lstWord;
	}
	public long getTotalSize()
	{
		return totalSize;
	}
	public int getPageCount()
	{
		return pageCount;
	}
	
	public File getImportFile()
	{
		return importFile;
	}
	public void setImportFile(File importFile) 
	{
		this.importFile = importFile;
	}
	public String getImportFileContentType() 
	{
		return importFileContentType;
	}
	public void setImportFileContentType(String importFileContentType) 
	{
		this.importFileContentType = importFileContentType;
	}
	public String getImportFileFileName() 
	{
		return importFileFileName;
	}
	public void setImportFileFileName(String importFileFileName) 
	{
		this.importFileFileName = importFileFileName;
	}
	
	public String mgr() throws Exception 
	{
		Map<String, Object> values = new HashMap<String, Object>();
		if(searchMode == 1)
		{
				if(word!=null && word.isEmpty()==false)
				{
					values.put("word", "%" + word + "%");
				}
				if(level >= 0)
				{
					values.put("level", level);
				}
		}
		if (parameters.containsKey("p"))
		{
			String p = ((String[])parameters.get("p"))[0];
			try
			{
				pageCount = Integer.parseInt(p);
			}
			catch (Exception ex)
			{
				throw new GcException("Invalid parameter : " + p);
			}
		}
		int sizePerPage = Integer.parseInt(getConfigurations().get("RECORDS_PER_PAGE"));
		List rst = sysMgr.findFilterWordByPage(values, (pageCount-1)*sizePerPage, sizePerPage);
		lstWord = (List<CommonFilterWord>) rst.get(0);
		totalSize = (long) rst.get(1);
		if(totalSize == 0)
		{
			pageCount = 0;
		}
		setPageSize();
		
		return "mgr";
	}
	
	public String add() throws Exception
	{
		CommonFilterWord cfw = new CommonFilterWord(word, level);
		sysMgr.addFilterWord(cfw);
		
		return "add";
	}
	
	public String del() throws Exception
	{
		Object[] params;
		Integer[] ids;
		try
		{
			if(parameters.containsKey("id"))
			{
				params = (Object[]) parameters.get("id");
			}
			else if(parameters.containsKey("checkbox"))
			{
				params = (Object[]) parameters.get("checkbox");
				
			}
			else
			{
				throw new Exception();
			}
			ids = new Integer[params.length];
			for(int i=0; i<params.length; i++)
			{
				ids[i] = Integer.parseInt(params[i].toString());
			}
		}
		catch(Exception ex)
		{
			throw new GcException("Invalid parameter");
		}
		sysMgr.delFilterWord(ids);
		
		return "del";
	}
	
	public String upload() throws Exception
	{
		String uploadPath = ServletActionContext.getServletContext().getRealPath("/upload");
		String toFile = null;
		try
		{
			toFile = UploadFileUtil.upload(importFile, importFileFileName, uploadPath);
			sysMgr.addFilterWord(toFile);
			UploadFileUtil.delete(toFile);
		} catch(GcException ex)
		{
			throw ex;
		}
		
		return "upload";
	}
}
