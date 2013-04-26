<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看用户</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/style.css" media="all">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/admin/general.js"></script>
</head>
<body>
	<div class="nav">当前位置：用户管理 -&gt; 查看用户</div>
	<div class="mgrarea">
		<s:form action="usermgr.do" namespace="admin">
			<table>
				<tr>
					<td>用户昵称：</td>
					<td><s:text name="user.nickname" /></td>
				</tr>
				<tr>
					<td>邮箱地址：</td>
					<td><s:text name="user.email" /></td>
				</tr>
				<tr>
					<td>绑定学号：</td>
					<td><s:text name="user.studentnum" /></td>
				</tr>
				<tr>
					<td>用户状态：</td>
					<td><s:if test="#request.user.status==0">已审核</s:if><s:elseif
							test="#request.user.status==1">未审核</s:elseif></td>
				</tr>
				<tr>
					<td>注册时间：</td>
					<td><s:text name="format.datetime">
							<s:param value="#request.user.regdate" />
						</s:text></td>
				</tr>
				<tr>
					<td colspan="2">更多资料</td>
				</tr>
			</table>
		</s:form>
	</div>
</body>
</html>