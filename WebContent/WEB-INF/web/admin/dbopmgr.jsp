<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>备份/恢复</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/style.css" media="all">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/admin/general.js"></script>
<script type="text/javascript">
	function importConfirm()
	{
		return confirm("导入后原数据将被全部覆盖！确认要导入数据？");
	}
</script>
</head>
<body>
	<div class="nav">当前位置：数据管理 -&gt; 备份/恢复</div>
	<div class="mgrarea">
		<s:form action="dbopbackup.do" namespace="/admin">
			<input type="hidden" name="act" value="backup" />
			<s:submit value="马上备份" />
		</s:form>
	</div>
	<div class="mgrlist">
		<table>
			<tr>
				<th>编号</th>
				<th>文件名</th>
				<th>创建时间</th>
				<th>文件大小</th>
				<th>操作</th>
			</tr>
			<s:iterator var="f" value="#request.lstFile" status="stat">
				<tr>
					<td><s:property value="%{#stat.index+1}" /></td>
					<td><s:property value="#f.name" /></td>
					<td><s:text name="format.datetime"><s:param value="#f.time" /></s:text></td>
					<td><s:property value="#f.size" /></td>
					<td>
						<s:url action="dboprestore" id="restoreurl">
							<s:param name="act">restore</s:param>
							<s:param name="fileName">
								<s:property value="#f.name" />
							</s:param>
						</s:url>
						<s:a href="%{restoreurl}" onclick="return importConfirm();">导入</s:a>
						<s:url action="dbopdel" id="delurl">
							<s:param name="act">del</s:param>
							<s:param name="fileName">
								<s:property value="#f.name" />
							</s:param>
						</s:url> 
						<s:a cssClass="delete" href="%{delurl}">删除</s:a>
					</td>
				</tr>
			</s:iterator>
		</table>
	</div>
</body>
</html>