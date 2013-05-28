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
<script type="text/javascript">
	function pwdChangeCheck() {
		var pwd = document.getElementById("pwd");
		var newPwd = document.getElementById("newPwd");
		var confirmPwd = document.getElementById("confirmPwd");
		if(pwd.value.length==0 || newPwd.value.length==0
				|| confirmPwd.value.length==0) {
			return false;
		}
		if(newPwd.value != confirmPwd.value) {
			return false;
		}
		return true;
	}
</script>
</head>
<body>
	<div class="nav">当前位置：登录管理 -&gt; 更改登录密码</div>
	<div class="mgrarea">
		<s:form action="changepwd.do" namespace="admin">
			<table>
				<tr>
					<td>原密码：</td>
					<td><input type="password" name="pwd" id="pwd" /></td>
				</tr>
				<tr>
					<td>新密码：</td>
					<td><input type="password" name="newPwd" id="newPwd" /></td>
				</tr>
				<tr>
					<td>确认新密码：</td>
					<td><input type="password" id="confirmPwd" /></td>
				</tr>
				<tr>
					<td><s:submit value="更新" onclick="return pwdChangeCheck();" /></td>
				</tr>
			</table>
		</s:form>
	</div>
</body>
</html>