<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>日志管理</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/style.css" media="all">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/admin/general.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/admin/datepicker-i18n.js"></script>
<script type="text/javascript">
$(function(){
				
});
</script>
</head>
<body>
	<div class="nav">当前位置：系统管理 -&gt; 日志管理</div>
	<div class="search">
		<h3>搜索</h3>
		<s:form cssClass="searcharea" action="syslogmgr" namespace="/admin">
			<input name="searchMode" type="hidden" value="1" />
			<table>
			<tr>
			<td>日志类型：<s:select
				list="#{'-1':'所有日志', '0':'登录日志', '1':'后台日志', '2':'微博日志'}"
				name="type" listKey="key" listValue="value" 
				onchange="selectChanged(this.id)" /></td>
			<td>用户昵称：<s:textfield name="username" /></td>
			<td>起始时间：<s:textfield name="startTime" id="startTime" /></td>
			<td>截止时间：<s:textfield name="endTime" id="endTime" /></td>
			<td><s:submit value="查找" theme="simple" /></td>
			</tr>
			</table>
		</s:form>
	</div>
	<div class="mgrlist">
		<s:form id="formList" action="syslogAction.do" namespace="/admin">
			<table>
				<tr>
					<th><input id="checkAll" type="checkbox" /></th>
					<th>类型</th>
					<th style="width:100px;">用户</th>
					<th style="width:300px;">描述</th>
					<th>IP</th>
					<th>时间</th>
					<th>操作</th>
				</tr>
				<s:iterator var="log" value="#request.lstLog">
					<tr>
						<td><input name="checkbox" class="checkbox" type="checkbox"
							value='<s:property value="#log.lid" />' /></td>
						<td><s:if test="#log.type==0">登录日志</s:if> <s:elseif
									test="#log.type==1">后台日志</s:elseif> <s:elseif
									test="#log.type==2">微博日志</s:elseif></td>
						<td style="text-align: center;"><s:url action="syslogmgr"
								id="userfilter">
								<s:param name="searchMode">1</s:param>
								<s:param name="username">
									<s:property value="#log.commonUser.nickname" />
								</s:param>
							</s:url> <s:a href="%{userfilter}" title="只查看该用户">
								<s:property value="#log.commonUser.nickname" />
							</s:a></td>
						<td><s:property value="#log.detail" /></td>
						<td><s:property value="#log.ip" /></td>
						<td><s:text name="format.datetime"><s:param value="#log.dateline" /></s:text></td>
						<td><s:url action="syslogdel" id="delurl">
								<s:param name="id">
									<s:property value="#log.lid" />
								</s:param>
							</s:url> <s:a cssClass="delete" href="%{delurl}">删除</s:a></td>
					</tr>
				</s:iterator>
			</table>
			选中项操作：<s:submit cssClass="delete" value="删除选中" method="del" />
			<div class="pager">
				<s:hidden name="searchMode" />
				<s:hidden name="username" />
				<s:hidden name="startTime" />
				<s:hidden name="endTime" />
				<s:hidden name="type" />
				<s:if test="#request.searchMode == 1">
					<s:url action="syslogmgr" id="mgrurl">
						<s:param name="searchMode">
							${ attr.searchMode }
						</s:param>
						<s:param name="username">
							${ attr.username }
						</s:param>
						<s:param name="startTime">
							${ attr.startTime }
						</s:param>
						<s:param name="endTime">
							${ attr.endTime }
						</s:param>
						<s:param name="type">
							${ attr.type }
						</s:param>
					</s:url>
				</s:if>
				<s:else>
					<s:url action="syslogmgr" id="mgrurl">
						<s:param name="searchMode">
							${ attr.searchMode }
						</s:param>
					</s:url>
				</s:else>
				<s:if test="#request.logPage > 1">
					<s:a href="%{mgrurl}">首页</s:a>
				</s:if>
				<s:if test="#request.logPage > 1">
					<s:a href="%{mgrurl}&p=%{logPage-1}">上一页</s:a>
				</s:if>
				<s:if test="#request.logPage < #request.logTotalSize/20+1">
					<s:a href="%{mgrurl}&p=%{logPage+1}">下一页</s:a>
				</s:if>
				<s:if test="#request.logPage != #request.logTotalSize/20+1">
					<s:a href="%{mgrurl}&p=%{logTotalSize/20+1}">尾页</s:a>
				</s:if>
				当前第<s:property value="%{logPage}" />页 <input type="text" id="txtPage" class="txtPage" name="p"
					style="width: 30px;" onkeypress="return txtPageKeyPress(event);" />
				<s:submit id="goto" value="跳转" method="mgr"
					onclick="return checkPage(%{logTotalSize/20+1});" />
				共<s:property value="%{logTotalSize/20+1}" />页
				总计<s:property value="%{logTotalSize}" />条记录
			</div>
		</s:form>
	</div>
</body>
</html>