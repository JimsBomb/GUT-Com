package org.chingo.gutcom.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.chingo.gutcom.action.base.SystemBaseAction;
import org.chingo.gutcom.common.constant.ErrorMsg;
import org.chingo.gutcom.common.constant.ResultMsg;
import org.chingo.gutcom.common.constant.SysconfConst;
import org.chingo.gutcom.common.constant.SyslogConst;
import org.chingo.gutcom.common.constant.SystemConst;
import org.chingo.gutcom.common.util.UploadFileUtil;
import org.chingo.gutcom.common.util.WebUtil;
import org.chingo.gutcom.domain.CommonFilterWord;
import org.chingo.gutcom.domain.CommonSyslog;
import org.chingo.gutcom.exception.GcException;

public class FilterWordAction extends SystemBaseAction
{
	private List<CommonFilterWord> lstWord; // 保存关键词对象
	private int searchMode = 0; // 搜索模式：0-否，1-是
	private String word; // 关键词
	private byte level = -1; // 过滤级别
	
	private int pageCount = 1; // 当前页数
	private String prevP; // 上一页起始行的rowKey
	private String nextP; // 下一页起始行的rowKey
	
	private File importFile; // 上传的文件
	private String importFileContentType; // 上传文件的内容类型
	private String importFileFileName; // 上传文件的名字
	
	private String resultMsg; // 操作结果信息
	private String backTo; //返回页面
	
	public String getPrevP()
	{
		return this.prevP;
	}
	
	public void setPrevP(String prevP)
	{
		this.prevP = prevP;
	}
	
	public String getNextP()
	{
		return this.nextP;
	}
	
	public void setNextP(String nextP)
	{
		this.nextP = nextP;
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
	public void setPageCount(int pageCount)
	{
		this.pageCount = pageCount;
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
		String p = null;
		
		if(searchMode == 1) // 当是搜索模式时
		{
				if(word!=null && word.isEmpty()==false) // 搜索关键词非空时进行获取
				{
					values.put("word", word);
				}
				if(level >= 0) // 搜索过滤级别非空时进行获取
				{
					values.put("level", level);
				}
		}
		/* 处理分页 */
		if (parameters.containsKey("p")) // 分页时获取下一页起始行
		{
			p = ((String[])parameters.get("p"))[0];
		}
		int sizePerPage = Integer.parseInt(getConfigurations().get(SysconfConst.RECORDS_PER_PAGE));
		
		/* 按条件分页查询关键词 */
		List<Object> rst = sysMgr.findFilterWordByPage(values, p, sizePerPage);
		if(rst != null)
		{
			lstWord = (List<CommonFilterWord>) rst.get(0); // 获取查询结果list
			if(rst.size() > 1) // 有下一页时
			{
				nextP = (String) rst.get(1); // 获取并设置下一页起始行的rowKey
			}
		}
		
		/* SESSION分页历史处理 */
		List<String> pageKey; // 存储分页历史的首行rowKey
		if(session.containsKey(SystemConst.SESSION_PAGE)) // session中有分页历史时获取
		{
			pageKey = (List<String>) session.get(SystemConst.SESSION_PAGE);
		}
		else // 否则新建
		{
			pageKey = new ArrayList<String>();
		}
		if(pageCount > 1) // 当前不是第一页时
		{
			prevP = pageKey.get(pageCount-2); // 设置上一页首行rowKey
		}
		else // 否则清空历史
		{
			pageKey.clear();
		}
		if(lstWord!=null && lstWord.size()>0) // 当内容列表非空时
		{
			pageKey.add(lstWord.get(0).getWord()); // 将当前首行rowKey添加到历史列表中
		}
		session.put(SystemConst.SESSION_PAGE, pageKey); // 替换session中的旧分页历史
		
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
		/* 生成日志对象 */
		CommonSyslog log = new CommonSyslog();
		log.setIp(WebUtil.getRemoteAddr(request));
		log.setUserid(WebUtil.getUser(session).getUid());
		log.setNickname(WebUtil.getUser(session).getNickname());
		log.setType(SyslogConst.TYPE_OP_ADMIN);
		log.setDetail(SyslogConst.DETAIL_ADMIN_WORD_ADD);
		log.setDateline(new Date().getTime());
		sysMgr.putFilterWord(cfw, log); // 添加
		
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
		List<String> ids = new ArrayList<String>(); // 存放ID列表
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
			for(int i=0; i<params.length; i++)
			{
				ids.add(params[i].toString());
			}
		}
		catch(Exception ex)
		{
			throw new GcException(ErrorMsg.INVALID_PARAM);
		}
		/* 生成日志对象 */
		CommonSyslog log = new CommonSyslog();
		log.setIp(WebUtil.getRemoteAddr(request));
		log.setUserid(WebUtil.getUser(session).getUid());
		log.setNickname(WebUtil.getUser(session).getNickname());
		log.setType(SyslogConst.TYPE_OP_ADMIN);
		log.setDetail(SyslogConst.DETAIL_ADMIN_WORD_DEL);
		log.setDateline(new Date().getTime());
		sysMgr.delFilterWord(ids, log); // 提交删除
		
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
			/* 生成日志对象 */
			CommonSyslog log = new CommonSyslog();
			log.setIp(WebUtil.getRemoteAddr(request));
			log.setUserid(WebUtil.getUser(session).getUid());
			log.setNickname(WebUtil.getUser(session).getNickname());
			log.setType(SyslogConst.TYPE_OP_ADMIN);
			log.setDetail(SyslogConst.DETAIL_ADMIN_WORD_IMPORT);
			log.setDateline(new Date().getTime());
			sysMgr.putFilterWord(toFile, log); // 导入关键词
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
