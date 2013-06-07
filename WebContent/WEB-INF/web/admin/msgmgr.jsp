<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发布公告</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/style.css" media="all">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/admin/general.js"></script>
</head>
<body>
	<div class="nav">当前位置：系统管理 -&gt; 发布公告</div>
	<div class="mgrarea">
		<s:form action="msgsend.do" namespace="/admin">
			<h3>添加关键词</h3>
			<table>
				<tr>
					<td>公告内容：<br /><s:textarea name="content" value="" style="width:300px; height:100px;" /></td>
				</tr>
				<!-- <tr>
					<td>每次发送用户数：<s:textfield name="size" value="500" />（若发送失败请减少该值）</td>
				</tr> -->
				<tr>
					<td><s:submit value="开始发送" onClick="return confirm('用户较多时将需要消耗较长时间。确认开始发送？');" /></td>
				</tr>
			</table>
		</s:form>
	</div>
</body>
</html>