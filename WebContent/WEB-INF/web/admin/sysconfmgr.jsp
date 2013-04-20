<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>基本设置</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/style.css" media="all">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/admin/general.js"></script>
</head>
<body>
	<div class="nav">当前位置：系统管理 -&gt; 基本设置</div>
	<div class="mgrarea">
		<s:form action="sysconfupdate.do" namespace="admin">
			<table>
				<tr>
					<td>服务器状态：</td>
					<td><s:radio list="#{'0':'关闭', '1':'开启'}" name="serverStatus"
							listKey="key" listValue="value"
							value="#application.sysconf.SERVER_STATUS" /></td>
				</tr>
				<tr>
					<td>新用户审核：</td>
					<td><s:radio list="#{'0':'关闭', '1':'开启'}" name="userVerify"
							listKey="key" listValue="value"
							value="#application.sysconf.USER_VERIFY" /></td>
				</tr>
				<tr>
					<td>新微博审核：</td>
					<td><s:radio list="#{'0':'关闭', '1':'开启'}" name="weiboVerify"
							listKey="key" listValue="value"
							value="#application.sysconf.WEIBO_VERIFY" /></td>
				</tr>
				<tr>
					<td>新分享审核：</td>
					<td><s:radio list="#{'0':'关闭', '1':'开启'}" name="shareVerify"
							listKey="key" listValue="value"
							value="#application.sysconf.SHARE_VERIFY" /></td>
				</tr>
				<tr>
					<td>新分享评论审核：</td>
					<td><s:radio list="#{'0':'关闭', '1':'开启'}"
							name="shareCommentVerify" listKey="key" listValue="value"
							value="#application.sysconf.SHARE_COMMENT_VERIFY" /></td>
				</tr>
				<tr>
					<td>后台内容列表单页显示记录数：</td>
					<td><s:textfield name="recordsPerPage"
							value="%{#application.sysconf.RECORDS_PER_PAGE}" /></td>
				</tr>
				<tr>
					<td>日志保留时长：</td>
					<td><s:textfield name="logLifecycle"
							value="%{#application.sysconf.LOG_LIFECYCLE}" />天（0为永久保存）</td>
				</tr>
				<tr>
					<td><s:submit value="提交" /></td>
					<td></td>
				</tr>
			</table>
		</s:form>
	</div>
</body>
</html>