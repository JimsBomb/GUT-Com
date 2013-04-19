<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin/style.css"
	media="all">
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/admin/general.js"></script>
</head>
<body>
	<div class="search">搜索层</div>
	<div class="mgrlist">
		<s:form id="formList" action="userAction.do" namespace="/admin">
			<table>
				<tr>
					<th><input id="checkAll" type="checkbox" /></th>
					<th>学号</th>
					<th>姓名</th>
					<th>昵称</th>
					<th>邮箱</th>
					<th>注册时间</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
				<s:iterator var="u" value="#request.users">
					<tr>
						<td><input name="checkbox" class="checkbox" type="checkbox"
							value='<s:property value="#u.uid" />' /></td>
						<td><s:property value="#u.studentnum" /></td>
						<td><s:property value="#u.commonUserProfile.realname" /></td>
						<td><s:property value="#u.nickname" /></td>
						<td><s:property value="#u.email" /></td>
						<td><s:property value="#u.regdate" /></td>
						<td><s:property value="#u.status" /></td>
						<td><s:url action="useredit" id="updateurl">
								<s:param name="id">
									<s:property value="#u.uid" />
								</s:param>
							</s:url> <s:a href="%{updateurl}">编辑</s:a></td>
					</tr>
				</s:iterator>
			</table>
			<s:submit value="删除选中" method="del" />
		</s:form>
	</div>
</body>
</html>