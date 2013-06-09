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
					<td><strong>用户ID：</strong></td>
					<td><s:text name="user.uid" /></td>
				</tr>
				<tr>
					<td><strong>用户昵称：</strong></td>
					<td><s:text name="user.nickname" /></td>
				</tr>
				<tr>
					<td><strong>邮箱地址：</strong></td>
					<td><s:text name="user.email" /></td>
				</tr>
				<tr>
					<td><strong>绑定学号：</strong></td>
					<td><s:if test="#request.user.studentnum==''"><i>未绑定</i></s:if>
					<s:else><s:text name="user.studentnum" /></s:else></td>
				</tr>
				<tr>
					<td><strong>用户状态：</strong></td>
					<td><s:if test="#request.user.status==0">已审核</s:if><s:elseif
							test="#request.user.status==1">未审核</s:elseif></td>
				</tr>
				<tr>
					<td><strong>注册时间：</strong></td>
					<td><s:text name="format.datetime">
							<s:param value="#request.user.regdate" />
						</s:text></td>
				</tr>
				<tr>
					<td><strong>真实姓名：</strong></td>
					<td><s:if test="#request.user.realname==''"><i>未绑定</i></s:if>
					<s:else><s:text name="user.realname" /></s:else></td>
				</tr>
				<tr>
					<td><strong>所在学院：</strong></td>
					<td><s:if test="#request.user.college==''"><i>未绑定</i></s:if>
					<s:else><s:text name="user.college" /></s:else></td>
				</tr>
				<tr>
					<td><strong>所读专业：</strong></td>
					<td><s:if test="#request.user.major==''"><i>未绑定</i></s:if>
					<s:else><s:text name="user.major" /></s:else></td>
				</tr>
				<tr>
					<td><strong>所在班级：</strong></td>
					<td><s:if test="#request.user.classname==''"><i>未绑定</i></s:if>
					<s:else><s:text name="user.classname" /></s:else></td>
				</tr>
				<tr>
					<td><strong>上次登录时间：</strong></td>
					<td><s:text name="format.datetime">
							<s:param value="#request.user.lastlogin" />
						</s:text></td>
				</tr>
				<tr>
					<td><strong>上次登录IP：</strong></td>
					<td><s:if test="#request.user.lastip==''"><i>未绑定</i></s:if>
					<s:else><s:text name="user.lastip" /></s:else></td>
				</tr>
				<tr>
					<td><strong>微博总数：</strong></td>
					<td><s:text name="user.weibocnt" /></td>
				</tr>
				<tr>
					<td><strong>关注总数：</strong></td>
					<td><s:text name="user.following" /></td>
				</tr>
				<tr>
					<td><strong>粉丝总数：</strong></td>
					<td><s:text name="user.follower" /></td>
				</tr>
			</table>
		</s:form>
	</div>
</body>
</html>