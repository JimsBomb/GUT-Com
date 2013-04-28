<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理举报</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/style.css" media="all">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/admin/general.js"></script>
</head>
<body>
	<div class="nav">当前位置：微博管理 -&gt; 管理举报</div>
	<div class="search">
		<h3>筛选</h3>
		<s:form cssClass="searcharea" action="wbReportmgr" namespace="/admin">
			<input name="searchMode" type="hidden" value="1" />
			<table>
			<tr>
			<td>状态：<s:select
				list="#{'-1':'不限', '0':'未处理', '1':'已处理'}"
				name="status" listKey="key" listValue="value" 
				onchange="selectChanged(this.id)" /></td>
			</tr>
			</table>
		</s:form>
	</div>
	<div class="mgrlist">
		<s:form id="formList" action="wbReportAction.do" namespace="/admin">
			<table>
				<tr>
					<th><input id="checkAll" type="checkbox" /></th>
					<th>举报微博</th>
					<th style="width:100px;">举报者</th>
					<th>举报理由</th>
					<th>举报时间</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
				<s:iterator var="r" value="#request.lstReport">
					<tr>
						<td><input name="checkbox" class="checkbox" type="checkbox"
							value='<s:property value="#r.rid" />' /></td>
						<td><s:property value="#r.weiboContent.content" /></td>
						<td style="text-align: center;"><s:property
								value="#r.commonUser.nickname" /></td>
						<td><s:property value="#r.reason" /></td>
						<td><s:text name="format.datetime">
								<s:param value="#r.dateline" />
							</s:text></td>
						<td><s:if test="#r.status==0">未处理</s:if>
							<s:elseif test="#r.status==1">已处理</s:elseif></td>
						<td><s:url action="wbReportshow" id="showurl">
								<s:param name="id">
									<s:property value="#r.rid" />
								</s:param>
							</s:url> <s:a href="%{showurl}">查看详情</s:a>
							<s:if test="#r.status == 0">
							<s:url action="wbReportupdateStatus" id="updatestatusurl">
								<s:param name="id">
									<s:property value="#r.rid" />
								</s:param>
							</s:url> <s:a href="%{updatestatusurl}">
								标记已处理
							</s:a></s:if>
							<s:url action="wbReportdel" id="delurl">
								<s:param name="id">
									<s:property value="#r.rid" />
								</s:param>
							</s:url> <s:a cssClass="delete" href="%{delurl}">删除</s:a></td>
					</tr>
				</s:iterator>
			</table>
			选中项操作：<s:submit cssClass="delete" value="删除选中" method="del" />
			<s:submit value="标记已处理" method="updateStatus" />
			<div class="pager">
				<s:hidden name="searchMode" />
				<s:hidden name="status" />
				<s:if test="#request.searchMode == 1">
					<s:url action="wbReportmgr" id="mgrurl">
						<s:param name="searchMode">
							${ attr.searchMode }
						</s:param>
						<s:param name="status">
							${ attr.status }
						</s:param>
					</s:url>
				</s:if>
				<s:else>
					<s:url action="wbReportmgr" id="mgrurl">
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