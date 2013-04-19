<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看系统日志</title>
</head>
<body>
	<table>
		<tr>
			<th>编号</th>
			<th>用户</th>
			<th>描述</th>
			<th>IP</th>
			<th>时间</th>
			<th></th>
		</tr>
		<s:iterator var="log" value="#request.lstSyslog">
			<tr>
				<td><s:property value="#log.lid" /></td>
				<td><s:property value="#log.commonUser.nickname" /></td>
				<td><s:property value="#log.detail" /></td>
				<td><s:property value="#log.ip" /></td>
				<td><s:property value="#log.dateline" /></td>
				<td><s:url action="syslogdel" id="delurl">
						<s:param name="lid">
							<s:property value="#log.lid" />
						</s:param>
					</s:url> <s:a href="%{delurl}">删除</s:a></td>
			</tr>
		</s:iterator>
	</table>
</body>
</html>