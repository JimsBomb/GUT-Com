<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/style.css" media="all">
</head>
<body>
	<div style="margin:0 auto;">
		<s:form action="login" namespace="/admin">
			<table>
				<tr>
					<td>用户名：</td>
					<td><input type="text" name="name" /></td>
				</tr>
				<tr>
					<td>密码：</td>
					<td><input type="password" name="pwd" /></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" value="登录" /></td>
				</tr>
			</table>
		</s:form>
	</div>
</body>
</html>