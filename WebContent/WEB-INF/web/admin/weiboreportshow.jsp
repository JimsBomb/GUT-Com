<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看微博</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin/style.css"
	media="all">
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/admin/general.js"></script>
</head>
<body>
	<div class="nav">当前位置：微博管理 -&gt; 查看举报</div>
	<div class="mgrarea">
		<table>
			<tr>
				<td colspan="2"><h3>举报信息</h3></td>
			</tr>
			<tr>
				<td>举报人：</td>
				<td><s:text name="report.commonUser.niakcname" /></td>
			</tr>
			<tr>
				<td>举报理由：</td>
				<td><s:textarea readonly="readonly"
						value='<s:property value="#request.report.content" />' /></td>
			</tr>
			<tr>
				<td>举报时间：</td>
				<td><s:text name="format.datetime">
						<s:param value="#request.report.dateline" />
					</s:text></td>
			</tr>
			<tr>
				<td>举报状态：</td>
				<td><s:if test="#request.report.status==0">未处理</s:if> <s:elseif
						test="#request.report.status==1">已处理</s:elseif></td>
			</tr>
			<tr>
				<td colspan="2"><h3>举报微博详情</h3></td>
			</tr>
			<s:if test="#request.report.weiboContent == null">
				<tr>
					<td colspan="2">微博不存在或已被删除。</td>
				</tr>
			</s:if>
			<s:else>
				<tr>
					<td>作者：</td>
					<td><s:text name="report.weiboContent.commonUser.nickname" /></td>
				</tr>
				<tr>
					<td>发表时间：</td>
					<td><s:text name="format.datetime">
							<s:param value="#request.report.weiboContent.dateline" />
						</s:text></td>
				</tr>
				<tr>
					<td>所在话题：</td>
					<td><s:if
							test="#request.report.weiboContent.weiboTopicRelations == null">
							<i>无</i>
						</s:if> <s:else>
							<s:iterator var="topic"
								value="#request.report.weiboContent.weiboTopicRelations.weiboTopic">
								<s:property value="#topic.title" />&nbsp;&nbsp;
							</s:iterator>
						</s:else></td>
				</tr>
				<tr>
					<td>状态：</td>
					<td><s:if test="#request.report.weiboContent.status==0">未审核</s:if>
						<s:elseif test="#request.report.weiboContent.status==1">已审核</s:elseif></td>
				</tr>
				<tr>
					<td>可见性：</td>
					<td><s:if test="#request.report.weiboContent.visibility==0">公开</s:if>
						<s:elseif test="#request.report.weiboContent.visibility==1">关注可见</s:elseif></td>
				</tr>
				<tr>
					<td>微博类型：</td>
					<td><s:if test="#request.report.weiboContent.type==0">普通微博</s:if>
						<s:elseif test="#request.report.weiboContent.type==1">转发微博</s:elseif>
						<s:elseif test="#request.report.weiboContent.type==2">微博评论</s:elseif></td>
				</tr>
				<s:if test="#request.report.weiboContent.type!=0">
					<tr>
						<td>源微博：</td>
						<td><s:url action="weiboshow" id="showurl">
								<s:param name="id">
									<s:property value="#request.report.weiboContent.sourceid" />
								</s:param>
							</s:url> <s:a href="%{showurl}">查看</s:a></td>
					</tr>
				</s:if>
				<tr>
					<td>内容：</td>
					<td><s:textarea readonly="readonly"
							value='<s:property value="#request.report.weiboContent.content" />' /></td>
				</tr>
				<s:if test="#request.report.weiboContent.middlePic.isEmpty()==false">
					<tr>
						<td>微博图片：</td>
						<td><a
							href='<s:property value="#request.report.weiboContent.originalPic" />'
							title="查看原图" target="_blank"> <img
								src='<s:property value="#request.report.weiboContent.middlePic" />' />
						</a></td>
					</tr>
				</s:if>
			</s:else>
		</table>
		<s:form action="wbReportdeal.do" namespace="/admin">
			<s:if test="#request.report.status==0 && #request.report.weiboContent!=null">
				<s:submit value="删除微博并标记为已处理" />
			</s:if>
			<button onclick="javascript:window.location='weiboreportmgr.do';">返回列表</button>
		</s:form>
	</div>
</body>
</html>