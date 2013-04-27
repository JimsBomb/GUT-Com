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
	<div class="nav">当前位置：微博管理 -&gt; 查看微博</div>
	<div class="mgrarea">
		<s:form action="weibomgr.do" namespace="/admin">
			<table>
				<tr>
					<td>作者：</td>
					<td><s:text name="weibo.commonUser.nickname" /></td>
				</tr>
				<tr>
					<td>发表时间：</td>
					<td>
						<s:text name="format.datetime">
							<s:param value="#request.weibo.dateline" />
						</s:text>
					</td>
				</tr>
				<tr>
					<td>所在话题：</td>
					<td>
						<s:if test="#request.weibo.weiboTopicRelations == null">
							<i>无</i>
						</s:if>
						<s:else>
							<s:iterator var="topic"
									value="#request.weibo.weiboTopicRelations.weiboTopic">
								<s:property value="#topic.title" />&nbsp;&nbsp;
							</s:iterator>
						</s:else>
					</td>
				</tr>
				<tr>
					<td>状态：</td>
					<td>
						<s:if test="#request.weibo.status==0">未审核</s:if>
						<s:elseif test="#request.weibo.status==1">已审核</s:elseif>
					</td>
				</tr>
				<tr>
					<td>可见性：</td>
					<td>
						<s:if test="#request.weibo.visibility==0">公开</s:if>
						<s:elseif test="#request.weibo.visibility==1">关注可见</s:elseif>
					</td>
				</tr>
				<tr>
					<td>微博类型：</td>
					<td>
						<s:if test="#request.weibo.type==0">普通微博</s:if>
						<s:elseif test="#request.weibo.type==1">转发微博</s:elseif>
						<s:elseif test="#request.weibo.type==2">微博评论</s:elseif>
					</td>
				</tr>
				<s:if test="#request.weibo.type!=0">
					<tr>
						<td>源微博：</td>
						<td>
							<s:url action="weiboshow" id="showurl">
								<s:param name="id">
									<s:property value="#request.weibo.sourceid" />
								</s:param>
							</s:url> <s:a href="%{showurl}">查看</s:a>
						</td>
					</tr>
				</s:if>
				<tr>
					<td>内容：</td>
					<td>
						<s:textarea readonly="readonly" value='<s:property value="#request.weibo.content" />' />
					</td>
				</tr>
				<s:if test="#request.weibo.middlePic.isEmpty()==false">
					<tr>
						<td>微博图片：</td>
						<td>
							<a href='<s:property value="#request.weibo.originalPic" />' title="查看原图">
								<img src='<s:property value="#request.weibo.middlePic" />' />
							</a>
						</td>
					</tr>
				</s:if>
			</table>
		</s:form>
		<button onclick="javascript:window.location='weibomgr.do';">返回列表</button>
	</div>
</body>
</html>