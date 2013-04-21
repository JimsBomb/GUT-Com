<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>内容过滤</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/style.css" media="all">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/admin/general.js"></script>
<script type="text/javascript">
$(function(){
				
});
</script>
</head>
<body>
	<div class="nav">当前位置：系统管理 -&gt; 内容过滤</div>
	<div class="mgrarea">
		<s:form action="filteradd.do" namespace="/admin">
			<h3>添加关键词</h3>
			<table>
				<tr>
					<td>过滤级别：<s:select list="#{'0':'屏蔽', '1':'审核', '2':'禁止发表'}"
							name="level" listKey="key" listValue="value" value="0" /></td>
				</tr>
				<tr>
					<td>关键词：<s:textfield name="word" value="" /></td>
				</tr>
				<tr>
					<td><s:submit value="添加" />（重复添加将覆盖原有关键词）</td>
				</tr>
			</table>
		</s:form>
		<s:form action="filterupload.do" namespace="/admin" enctype="multipart/form-data">
			批量添加<img src="${pageContext.request.contextPath}/images/helpicon.gif" />：<s:file name="importFile" /><s:submit value="导入" />
		</s:form>
	</div>
	<div class="search">
		<h3>查找关键词</h3>
		<s:form cssClass="searcharea" action="filtermgr.do" namespace="/admin">
			<input name="searchMode" type="hidden" value="1" />
			<table>
			<tr>
			<td>过滤级别：<s:select list="#{'-1':'所有级别', '0':'屏蔽', '1':'审核', '2':'禁止发表'}"
							name="level" listKey="key" listValue="value" 
							onchange="selectChanged(this.id)" /></td>
			<td>关键词：<s:textfield name="word" /></td>
			<td><s:submit value="查找" /></td>
			</tr>
			</table>
		</s:form>
	</div>
	<div class="mgrlist">
		<s:form id="formList" action="filterAction.do" namespace="/admin">
			<table>
				<tr>
					<th><input id="checkAll" type="checkbox" /></th>
					<th>关键词</th>
					<th>过滤级别</th>
					<th>操作</th>
				</tr>
				<s:iterator var="word" value="#request.lstWord">
					<tr>
						<td><input name="checkbox" class="checkbox" type="checkbox"
							value='<s:property value="#word.wid" />' /></td>
						<td><s:property value="#word.word" /></td>
						<td style="text-align: center;"><s:if test="#word.level==0">屏蔽</s:if> <s:elseif
									test="#word.level==1">审核</s:elseif> <s:elseif
									test="#word.level==2">禁止发表</s:elseif></td>
						<td><s:url action="filterdel" id="delurl">
								<s:param name="id">
									<s:property value="#word.wid" />
								</s:param>
							</s:url> <s:a cssClass="delete" href="%{delurl}">删除</s:a></td>
					</tr>
				</s:iterator>
			</table>
			选中项操作：<s:submit cssClass="delete" value="删除选中" method="del" />
			<div class="pager">
				<s:hidden name="searchMode" />
				<s:hidden name="word" />
				<s:hidden name="level" />
				<s:if test="#request.searchMode == 1">
					<s:url action="filtermgr" id="mgrurl">
						<s:param name="searchMode">
							${ attr.searchMode }
						</s:param>
						<s:param name="word">
							${ attr.word }
						</s:param>
						<s:param name="level">
							${ attr.level }
						</s:param>
					</s:url>
				</s:if>
				<s:else>
					<s:url action="filtermgr" id="mgrurl">
						<s:param name="searchMode">
							${ attr.searchMode }
						</s:param>
					</s:url>
				</s:else>
				<s:if test="#request.pageCount > 1">
					<s:a href="%{mgrurl}">首页</s:a>
				</s:if>
				<s:if test="#request.pageCount > 1">
					<s:a href="%{mgrurl}&p=%{pageCount-1}">上一页</s:a>
				</s:if>
				<s:if test="#request.pageCount < #request.pageSize">
					<s:a href="%{mgrurl}&p=%{pageCount+1}">下一页</s:a>
				</s:if>
				<s:if test="#request.pageCount != #request.pageSize">
					<s:a href="%{mgrurl}&p=%{pageSize}">尾页</s:a>
				</s:if>
				当前第<s:property value="%{pageCount}" />页 <input type="text" id="txtPage" class="txtPage" name="p"
					style="width: 30px;" onkeypress="return txtPageKeyPress(event);" />
				<s:submit id="goto" value="跳转" method="mgr"
					onclick="return checkPage(%{pageSize});" />
				共<s:property value="%{pageSize}" />页
				总计<s:property value="%{totalSize}" />条记录
			</div>
		</s:form>
	</div>
</body>
</html>