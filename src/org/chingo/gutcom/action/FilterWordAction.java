package org.chingo.gutcom.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.chingo.gutcom.action.base.SystemBaseAction;
import org.chingo.gutcom.common.constant.ErrorMsg;
import org.chingo.gutcom.common.constant.ResultMsg;
import org.chingo.gutcom.common.constant.SysconfConst;
import org.chingo.gutcom.common.util.UploadFileUtil;
import org.chingo.gutcom.domain.CommonFilterWord;
import org.chingo.gutcom.exception.GcException;

public class FilterWordAction extends SystemBaseAction
{
	private List<CommonFilterWord> lstWord; // 保存关键词对象
	private long totalSize = 0; // 记录总数
	private int pageCount = 1; // 当前页数
	private int pageSize; // 总页数
	private int searchMode = 0; // 搜索模式：0-否，1-是
	private String word; // 关键词
	private byte level = -1; // 过滤级别
	
	private File importFile; // 上传的文件
	private String importFileContentType; // 上传文件的内容类型
	private String importFileFileName; // 上传文件的名字
	
	private String resultMsg; // 操作结果信息
	private String backTo; //返回页面
	
	public int getPageSize()
	{
		return pageSize;
	}
	/**
	 * 计算并设置总页数
	 */
	public void setPageSize()
	{
		int sizePerPage = Integer.parseInt(getConfigurations().get(SysconfConst.RECORDS_PER_PAGE));
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
	
	public String getResultMsg()
	{
		return resultMsg;
	}
	
	public String getBackTo()
	{
		return backTo;
	}
	
	/**
	 * 内容过滤管理Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String mgr() throws Exception 
	{
		Map<String, Object> values = new HashMap<String, Object>(); // 存放参数
		
		if(searchMode == 1) // 当是搜索模式时
		{
				if(word!=null && word.isEmpty()==false) // 搜索关键词非空时进行获取
				{
					values.put("word", "%" + word + "%");
				}
				if(level >= 0) // 搜索过滤级别非空时进行获取
				{
					values.put("level", level);
				}
		}
		if (parameters.containsKey("p")) // 分页时获取页码
		{
			String p = ((String[])parameters.get("p"))[0];
			try
			{
				pageCount = Integer.parseInt(p);
			}
			catch (Exception ex)
			{
				throw new GcException(ErrorMsg.INVALID_PARAM);
			}
		}
		int sizePerPage = Integer.parseInt(getConfigurations().get(SysconfConst.RECORDS_PER_PAGE));
		/* 按条件分页查询关键词 */
		List rst = sysMgr.findFilterWordByPage(values, (pageCount-1)*sizePerPage, sizePerPage);
		lstWord = (List<CommonFilterWord>) rst.get(0); // 获取查询结果list
		totalSize = (long) rst.get(1); // 获取查询结果总数
		if(totalSize == 0) // 无结果返回时，页码置零
		{
			pageCount = 0;
		}
		setPageSize(); // 计算总页数
		
		return "mgr";
	}
	
	/**
	 * 添加关键词Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String add() throws Exception
	{
		CommonFilterWord cfw = new CommonFilterWord(word, level); // 封装对象
		sysMgr.addFilterWord(cfw); // 添加
		
		this.resultMsg = ResultMsg.FILTER_WORD_ADD; // 设置操作结果信息
		this.backTo = "filtermgr.do"; // 设置返回页面
		
		return SUCCESS;
	}
	
	/**
	 * 删除关键词Method
	 * @return Action Result
	 * @throws Exception
	 */
	public String del() throws Exception
	{
		Object[] params; // 存放参数
		Integer[] ids; // 存放ID列表
		try
		{
			if(parameters.containsKey("id")) // 当是删除单个关键词时
			{
				params = (Object[]) parameters.get("id");
			}
			else if(parameters.containsKey("checkbox")) // 通过勾选复选框删除时
			{
				params = (Object[]) parameters.get("checkbox");
				
			}
			else // 其它参数时抛出异常
			{
				throw new GcException();
			}
			/* 填充ID列表 */
			ids = new Integer[params.length];
			for(int i=0; i<params.length; i++)
			{
				ids[i] = Integer.parseInt(params[i].toString());
			}
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		sysMgr.delFilterWord(ids); // 提交删除
		
		this.resultMsg = ResultMsg.FILTER_WORD_DEL;
		this.backTo = "filtermgr.do";
		
		return SUCCESS;
	}
	
	/**
	 * 批量导入关键词Method
	 * @return
	 * @throws Exception
	 */
	public String upload() throws Exception
	{
		// 设置上传路径
		String uploadPath = ServletActionContext.getServletContext().getRealPath("/upload");
		String toFile = null; // 目标保存路径
		try
		{
			toFile = UploadFileUtil.upload(importFile, importFileFileName, uploadPath); // 上传
			sysMgr.addFilterWord(toFile); // 导入关键词
			UploadFileUtil.delete(toFile); // 删除上传的文件
		} catch(GcException ex)
		{
			throw ex;
		}
		
		this.resultMsg = ResultMsg.FILTER_WORD_IMPORT;
		this.backTo = "filtermgr.do";
		
		return SUCCESS;
	}
}
