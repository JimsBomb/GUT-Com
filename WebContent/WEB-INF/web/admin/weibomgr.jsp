<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理微博</title>
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
	<div class="nav">当前位置：微博管理 -&gt; 管理微博</div>
	<div class="search">
		<h3>查找微博</h3>
		<s:form cssClass="searcharea" action="weibomgr" namespace="/admin">
			<input name="searchMode" type="hidden" value="1" />
			<table>
				<tr>
					<td>微博状态：</td>
					<td><s:select list="#{'-1':'不限', '1':'已审核', '0':'未审核'}"
							name="status" listKey="key" listValue="value"
							onchange="selectChanged(this.id)" /></td>
				</tr>
				<tr>
					<td>微博可见性：</td>
					<td><s:select list="#{'-1':'不限', '0':'公开', '1':'关注可见'}"
							name="visibility" listKey="key" listValue="value"
							onchange="selectChanged(this.id)" /></td>
				</tr>
				<!-- <tr>
					<td>作者昵称：</td>
					<td><s:textfield name="author" /></td>
				</tr> -->
				<tr>
					<td>内容关键字：</td>
					<td><s:textfield name="content" /></td>
				</tr>
				<tr>
					<td>话题关键字：</td>
					<td><s:textfield name="topic" /></td>
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
		<s:form id="formList" action="weiboAction.do" namespace="/admin">
			<table>
				<tr>
					<th><input id="checkAll" type="checkbox" /></th>
					<th style="width: 100px;">作者</th>
					<th>内容</th>
					<th>所在话题</th>
					<th>发表时间</th>
					<th>状态</th>
					<th>可见性</th>
					<th>操作</th>
				</tr>
				<s:iterator var="w" value="#request.lstWeibo">
					<tr>
						<td><input name="checkbox" class="checkbox" type="checkbox"
							value='<s:property value="#w.wid" />' /></td>
						<td style="text-align: center;"><s:property
								value="#w.author.nickname" /></td>
						<td><s:property value="#w.content" /></td>
						<td><s:if test="#w.topics.size == 0">
								<i>无</i>
							</s:if>
							<s:else>
								<s:iterator var="topic"
									value="#w.topics">
									<s:property value="#topic.title" />&nbsp;&nbsp;
							</s:iterator>
							</s:else></td>
						<td><s:text name="format.datetime">
								<s:param value="#w.dateline" />
							</s:text></td>
						<td><s:if test="#w.status==0">未审核</s:if> <s:elseif
								test="#w.status==1">已审核</s:elseif></td>
						<td><s:if test="#w.visibility==0">公开</s:if> <s:elseif
								test="#w.visibility==1">关注可见</s:elseif></td>
						<td><s:url action="weiboshow" id="showurl">
								<s:param name="id">
									<s:property value="#w.wid" />
								</s:param>
							</s:url> <s:a href="%{showurl}">查看</s:a> <s:url
								action="weiboaudit" id="updatestatusurl">
								<s:param name="id">
									<s:property value="#w.wid" />
								</s:param>
								<s:param name="status">
									<s:property value="#w.status" />
								</s:param>
							</s:url> <s:a href="%{updatestatusurl}">
								<s:if test="#w.status==0">
						审核</s:if>
							</s:a> <s:url action="weibodel" id="delurl">
								<s:param name="id">
									<s:property value="#w.wid" />
								</s:param>
							</s:url> <s:a cssClass="delete" href="%{delurl}">删除</s:a></td>
					</tr>
				</s:iterator>
			</table>
			选中项操作：<s:submit cssClass="delete" value="删除选中" method="del" />
			<div class="pager">
				<s:hidden name="searchMode" />
				<s:hidden name="author" />
				<s:hidden name="content" />
				<s:hidden name="startTime" />
				<s:hidden name="endTime" />
				<s:hidden name="status" />
				<s:hidden name="visibility" />
				<s:if test="#request.searchMode == 1">
					<s:url action="weibomgr" id="mgrurl">
						<s:param name="searchMode">
							${ attr.searchMode }
						</s:param>
						<s:param name="author">
							${ attr.author }
						</s:param>
						<s:param name="content">
							${ attr.content }
						</s:param>
						<s:param name="topic">
							${ attr.topic }
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
						<s:param name="visibility">
							${ attr.visibility }
						</s:param>
					</s:url>
				</s:if>
				<s:else>
					<s:url action="weibomgr" id="mgrurl">
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