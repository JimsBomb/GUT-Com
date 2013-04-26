<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>更加用户密码</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/style.css" media="all">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/admin/general.js"></script>
</head>
<body>
	<div class="nav">当前位置：用户管理 -&gt; 更改用户密码</div>
	<div class="mgrarea">
		<s:form action="userchangePwd.do" namespace="admin">
			<s:hidden name="id" value="%{#request.id}" />
			<table>
				<tr>
					<td>新密码：<input type="text" name="pwd" /></td>
				</tr>
				<tr>
					<td><s:submit value="更新" /></td>
				</tr>
			</table>
		</s:form>
	</div>
</body>
</html>