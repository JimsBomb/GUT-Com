<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/style.css" media="all">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/admin/general.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/admin/datepicker-i18n.js"></script>
</head>
<body>
	<div class="nav">当前位置：用户管理 -&gt; 用户管理</div>
	<div class="search">
		<h3>查找用户</h3>
		<s:form cssClass="searcharea" action="usermgr" namespace="/admin">
			<input name="searchMode" type="hidden" value="1" />
			<table>
			<tr>
			<td>用户状态：<s:select
				list="#{'-1':'不限', '1':'正常', '0':'禁止发表'}"
				name="status" listKey="key" listValue="value" 
				onchange="selectChanged(this.id)" /></td>
			<td>用户昵称：<s:textfield name="nickname" /></td>
			<td>学号：<s:textfield name="studentnum" /></td>
			<!-- <td>注册时间：<s:textfield name="createTime" id="createTime" /></td> -->
			<td><s:submit value="查找" /></td>
			</tr>
			</table>
		</s:form>
	</div>
	<div class="mgrlist">
		<s:form id="formList" action="userAction.do" namespace="/admin">
			<table>
				<tr>
					<th><input id="checkAll" type="checkbox" /></th>
					<th>学号</th>
					<th style="width:100px;">用户昵称</th>
					<th>性别</th>
					<th>注册时间</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
				<s:iterator var="u" value="#request.lstUser">
					<tr>
						<td><input name="checkbox" class="checkbox" type="checkbox"
							value='<s:property value="#u.uid" />' /></td>
						<td><s:if test="#u.studentnum==''"><i>未绑定</i></s:if>
						<s:else><s:property value="#u.studentnum" /></s:else></td>
						<td style="text-align: center;"><s:property
								value="#u.nickname" /></td>
						<td><s:if test="#u.gender==0">保密</s:if>
							<s:elseif test="#u.gender==1">男</s:elseif> <s:elseif
								test="#u.gender==2">女</s:elseif></td>
						<td><s:text name="format.datetime">
								<s:param value="#u.regdate" />
							</s:text></td>
						<td><s:if test="#u.status==0">禁止发表</s:if> <s:elseif
								test="#u.status==1">正常</s:elseif></td>
						<td><s:url action="usershow" id="showurl">
								<s:param name="id">
									<s:property value="#u.uid" />
								</s:param>
							</s:url> <s:a href="%{showurl}">查看</s:a>
							<s:url action="userchangePwd" id="pwdurl">
								<s:param name="id">
									<s:property value="#u.uid" />
								</s:param>
							</s:url>
							<s:a href="%{pwdurl}">更改密码</s:a>
						<s:url action="userupdateStatus" id="updatestatusurl">
								<s:param name="id">
									<s:property value="#u.uid" />
								</s:param>
								<s:param name="status">
									<s:property value="#u.status" />
								</s:param>
							</s:url> <s:a href="%{updatestatusurl}">
								<s:if test="#u.status==0">
						允许发表</s:if>
								<s:elseif test="#u.status==1">禁止发表</s:elseif>
							</s:a> <s:url action="userdel" id="delurl">
								<s:param name="id">
									<s:property value="#u.uid" />
								</s:param>
							</s:url> <s:a cssClass="delete" href="%{delurl}">删除</s:a></td>
					</tr>
				</s:iterator>
			</table>
			选中项操作：<s:submit cssClass="delete" value="删除选中" method="del" />
			<div class="pager">
				<s:hidden name="searchMode" />
				<s:hidden name="nickname" />
				<s:hidden name="studentnum" />
				<s:hidden name="createTime" />
				<s:hidden name="status" />
				<s:if test="#request.searchMode == 1">
					<s:url action="usermgr" id="mgrurl">
						<s:param name="searchMode">
							${ attr.searchMode }
						</s:param>
						<s:param name="nickname">
							${ attr.nickname }
						</s:param>
						<s:param name="studentnum">
							${ attr.studentnum }
						</s:param>
						<s:param name="createTime">
							${ attr.createTime }
						</s:param>
						<s:param name="status">
							${ attr.status }
						</s:param>
					</s:url>
				</s:if>
				<s:else>
					<s:url action="usermgr" id="mgrurl">
						<s:param name="searchMode">
							${ attr.searchMode }
						</s:param>
					</s:url>
				</s:else>
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