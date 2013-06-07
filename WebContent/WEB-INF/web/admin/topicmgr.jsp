<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理话题</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin/style.css"
	media="all">
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css">
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript"
	src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/admin/general.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/admin/datepicker-i18n.js"></script>
</head>
<body>
	<div class="nav">当前位置：微博管理 -&gt; 管理话题</div>
	<div class="search">
		<h3>查找话题</h3>
		<s:form cssClass="searcharea" action="topicmgr" namespace="/admin">
			<input name="searchMode" type="hidden" value="1" />
			<table>
				<tr>
					<td>话题状态：</td>
					<td><s:select list="#{'-1':'不限', '0':'正常', '1':'屏蔽'}"
							name="status" listKey="key" listValue="value"
							onchange="selectChanged(this.id)" /></td>
				</tr>
				<tr>
					<td>话题关键字：</td>
					<td><s:textfield name="title" /></td>
				</tr>
				<tr>
					<td>起始时间：</td>
					<td><s:textfield name="startTime" id="startTime" /></td>
				</tr>
				<tr>
					<td>截止时间：</td>
					<td><s:textfield name="endTime" id="endTime" /></td>
				</tr>
				<tr>
					<td colspan="2"><s:submit value="查找" /></td>
				</tr>
			</table>
		</s:form>
	</div>
	<div class="mgrlist">
		<s:form id="formList" action="topicAction.do" namespace="/admin">
			<table>
				<tr>
					<th><input id="checkAll" type="checkbox" /></th>
					<th>话题名</th>
					<th style="width: 100px;">发起者</th>
					<th>发起时间</th>
					<th>状态</th>
					<th>下属微博数</th>
					<th>操作</th>
				</tr>
				<s:iterator var="t" value="#request.lstTopic">
					<tr>
						<td><input name="checkbox" class="checkbox" type="checkbox"
							value='<s:property value="#t.title" />' /></td>
						<td><s:property value="#t.title" /></td>
						<td style="text-align: center;"><s:property
								value="#t.sponsor.nickname" /></td>
						<td><s:text name="format.datetime">
								<s:param value="#t.dateline" />
							</s:text></td>
						<td><s:if test="#t.isblock==0">正常</s:if> <s:elseif
								test="#t.isblock==1">屏蔽</s:elseif></td>
						<td><s:property value="#t.count" /></td>
						<td><s:url action="weibomgr" id="showurl">
								<s:param name="searchMode">1</s:param>
								<s:param name="topic">
									<s:property value="#t.title" />
								</s:param>
							</s:url> <s:a href="%{showurl}">查看微博</s:a> <s:url
								action="topicupdateStatus" id="updatestatusurl">
								<s:param name="id">
									<s:property value="#t.title" />
								</s:param>
								<s:param name="newStatus">
									<s:property value="%{#t.isblock^1}" />
								</s:param>
							</s:url> <s:a href="%{updatestatusurl}">
								<s:if test="#t.isblock==0">屏蔽</s:if>
								<s:elseif test="#t.isblock==1">解封</s:elseif>
							</s:a> <s:url action="topicdel" id="delurl">
								<s:param name="id">
									<s:property value="#t.title" />
								</s:param>
							</s:url> <s:a cssClass="delete" href="%{delurl}">删除</s:a></td>
					</tr>
				</s:iterator>
			</table>
			选中项操作：<s:submit cssClass="delete" value="删除选中" method="del" />
			<!-- <s:submit value="屏蔽/解封选中" method="audit" /> -->
			<div class="pager">
				<s:hidden name="searchMode" />
				<s:hidden name="title" />
				<s:hidden name="startTime" />
				<s:hidden name="endTime" />
				<s:hidden name="status" />
				<s:if test="#request.searchMode == 1">
					<s:url action="topicmgr" id="mgrurl">
						<s:param name="searchMode">
							${ attr.searchMode }
						</s:param>
						<s:param name="title">
							${ attr.title }
						</s:param>
						<s:param name="startTime">
							${ attr.startTime }
						</s:param>
						<s:param name="endTime">
							${ attr.endTime }
						</s:param>
						<s:param name="status">
							${ attr.status }
						</s:param>
					</s:url>
				</s:if>
				<s:else>
					<s:url action="topicmgr" id="mgrurl">
						<s:param name="searchMode">
							${ attr.searchMode }
						</s:param>
					</s:url>
				</s:else>
				<s:if test="#request.pageCount > 1">
					<s:a href="%{mgrurl}">首页</s:a>
				</s:if>
				<s:if test="#request.pageCount > 1">
					<s:a href="%{mgrurl}&p=%{prevP}&pageCount=%{pageCount-1}">上一页</s:a>
				</s:if>
				<s:if test="#request.nextP != null">
					<s:a href="%{mgrurl}&p=%{nextP}&pageCount=%{pageCount+1}">下一页</s:a>
				</s:if>
				当前第<s:property value="%{pageCount}" />页
			</div>
		</s:form>
	</div>
</body>
</html>