<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户审核</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/style.css" media="all">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/admin/general.js"></script>
</head>
<body>
	<div class="nav">当前位置：用户管理 -&gt; 审核用户</div>
	<div class="mgrlist">
		<s:form id="formList" action="userAction.do" namespace="/admin">
			<table>
				<tr>
					<th><input id="checkAll" type="checkbox" /></th>
					<th>学号</th>
					<th style="width:100px;">用户昵称</th>
					<th>性别</th>
					<th>注册时间</th>
					<th>操作</th>
				</tr>
				<s:iterator var="u" value="#request.lstUser">
					<tr>
						<td><input name="checkbox" class="checkbox" type="checkbox"
							value='<s:property value="#u.uid" />' /></td>
						<td><s:property value="#u.studentnum" /></td>
						<td style="text-align: center;"><s:property
								value="#u.nickname" /></td>
						<td><s:if test="#u.commonUserProfile.gender==0">保密</s:if>
							<s:elseif test="#u.status==1">男</s:elseif> <s:elseif
								test="#u.commonUserProfile.gender==2">女</s:elseif></td>
						<td><s:text name="format.datetime">
								<s:param value="#u.regdate" />
							</s:text></td>
						<td><s:url action="userupdateStatus" id="updatestatusurl">
								<s:param name="id">
									<s:property value="#u.uid" />
								</s:param>
								<s:param name="status">
									<s:property value="#u.status" />
								</s:param>
							</s:url> <s:a href="%{updatestatusurl}">
								审核用户
							</s:a> <s:url action="userdel" id="delurl">
								<s:param name="id">
									<s:property value="#u.uid" />
								</s:param>
							</s:url> <s:a cssClass="delete" href="%{delurl}">删除</s:a></td>
					</tr>
				</s:iterator>
			</table>
			选中项操作：<s:submit cssClass="delete" value="删除选中" method="del" />
			<s:submit value="审核选中" method="audit" />
			<div class="pager">
					<s:url action="userauditmgr" id="mgrurl">
						<s:param name="searchMode">
							1
						</s:param>
						<s:param name="status">1</s:param>
					</s:url>
				<s:if test="#request.pageCount > 1">
					<s:a href="%{mgrurl}">首页</s:a>
				</s:if>
				<s:if test="#request.pageCount > 1">
					<s:a href="%{mgrurl}&p=%{prevP}&pageCount=%{pageCount-1}">上一页</s:a>
				</s:if>
				<s:if test="#request.nextP != null">
					<s:a href="%{mgrurl}&p=%{nextP}&pageCount=%{pageCount+1}">下一页</s:a>
				</s:if>
				当前第<s:property value="%{pageCount}" />页
			</div>
		</s:form>
	</div>
</body>
</html>