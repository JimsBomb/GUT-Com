<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSON Test</title>
</head>
<body>
	<s:form action="api/common/authorize/login.json">
		<p>
			昵称：
			<s:textfield name="nickname" />
		</p>
		<p>
			密码：
			<s:password name="password" />
		</p>
		<p>
			<s:submit value="登录" />
		</p>
	</s:form>
</body>
</html>