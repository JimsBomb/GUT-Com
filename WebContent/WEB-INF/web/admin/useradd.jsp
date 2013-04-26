<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加用户</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/style.css" media="all">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/admin/general.js"></script>
</head>
<script type="text/javascript">
	function nicknameVerify(id) {
		var txt = document.getElementById(id);
		var len = txt.value.replace(/\s*/g, "").length;
		var msg = document.getElementById("nicknameMsg");
		var reg = new RegExp("(^[a-zA-Z]{1}([a-zA-Z0-9_]){4,14}|(^[\u4E00-\uFA29]{1}[a-zA-Z0-9\u4E00-\uFA29]{2,7}))$");
		if(!reg.test(txt.value)) {
			msg.innerHTML = "昵称长度只能为6-20个字符";
		} else {
			msg.innerHTML = "";
		}
	}
	function pwdVerify(id) {
		var txt = document.getElementById(id);
		var msg = document.getElementById("pwdMsg");
		var reg = new RegExp("[A-Za-z0-9_!@#$%^&*\(\)\.+=,/?-]{6,20}");
		if(!reg.test(txt.value)) {
			msg.innerHTML = "密码格式不正确";
		} else {
			msg.innerHTML = "";
		}
	}
	function emailVerify(id) {
		var txt  = document.getElementById(id);
		alert(txt.value);
		var msg = document.getElementById("emailMsg");
		var reg =  new RegExp("^[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\.[A-Za-z0-9-]+)*((\.[A-Za-z0-9]{2,})|(\.[A-Za-z0-9]{2,}\.[A-Za-z0-9]{2,}))$");
		if(!reg.test(txt.value)) {
			msg.innerHTML = "邮箱格式不正确。";
		} else {
			msg.innerHTML = "";
		}
			
	}
</script>
<body>
	<div class="nav">当前位置：用户管理 -&gt; 添加用户</div>
	<div class="mgrarea">
		<s:form action="usercreate.do" namespace="admin">
			<table>
				<tr>
					<td colspan="2">带*的为必填项目</td>
				</tr>
				<tr>
					<td>*用户昵称：</td>
					<td><s:textfield name="user.nickname" id="nickname" onblur="nicknameVerify(this.id)" /><div id="nicknameMsg"></div></td>
				</tr>
				<tr>
					<td>*登录密码：</td>
					<td><s:textfield name="user.password" id="pwd" onblur="pwdVerify(this.id)" /><div id="pwdMsg"></div></td>
				</tr>
				<tr>
					<td>*邮箱地址：</td>
					<td><s:textfield name="user.email" id="email" onblur="emailVerify(this.id)" /><div id="emailMsg"></div></td>
				</tr>
				<tr>
					<td>用户状态：</td>
					<td><s:radio list="#{'0':'已审核', '1':'未审核'}" name="user.status"
							listKey="key" listValue="value" value="1" /></td>
				</tr>
				<tr>
					<td><s:submit value="添加" /></td>
					<td></td>
				</tr>
			</table>
		</s:form>
	</div>
</body>
</html>