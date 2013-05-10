<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>备份数据</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/style.css" media="all">
</head>
<body>
	<div class="nav">当前位置：数据管理 -&gt; 备份数据</div>
	<div class="mgrarea">
		<s:form action="dbopbackup.do" namespace="/admin">
			<input type="hidden" name="act" value="backup" />
			<s:submit value="开始备份" />
		</s:form>
	</div>
</body>
</html>