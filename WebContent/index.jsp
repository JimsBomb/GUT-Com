<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>校园通管理系统</title>
</head>
<body>
<%
Object o = request.getSession().getAttribute("user");
if(o==null)
{
%>
<jsp:forward page="/login.jsp" />
<%
}
else
{%>
<% 
String method = request.getParameter("method");
if(method==null) {
%>
<jsp:forward page="/WEB-INF/web/admincp.jsp"/>
<%
} else {
	method="/WEB-INF/web/" + method;
%>
<jsp:forward page="<%=method %>"/>
<%
}
}
%>
<s:form action="test.do" namespace="/">
	<s:submit value="add" method="addLog" />
	<s:submit value="del" method="delUser" />
	<s:submit value="addUsers" method="addUsers" />
</s:form>
</body>
</html>