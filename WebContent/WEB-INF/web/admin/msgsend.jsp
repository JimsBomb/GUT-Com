<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>操作结果</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/style.css" media="all">
<script type="text/javascript">
var flg = 1;
function remainTime()
{
    if (flg > 0)
    {
    	account = setTimeout("remainTime()",2000);
    	flg--;
    }
    else {
    	document.getElementById("aGo").click();
    }
}
</script>
</head>
<body onload="remainTime()">
	<div class="nav">当前位置：系统管理 -&gt; 发布公告</div>
	<div class="opresult">
		<s:if test="#request.offset == -1">
			操作完毕，已向所有用户发送公告。
		</s:if>
		<s:else>
			<p>正在向第<s:text name="offset" />到第<s:text name="%{offset+size-1}" />个用户发送消息。</p>
			<s:url action="msgsend" id="sendurl">
				<s:param name="offset">${ attr.offset }</s:param>
				<s:param name="size">${ attr.size }</s:param>
				<s:param name="content">${ attr.content }</s:param>
			</s:url>
			<p>请勿刷新或关闭本页面，稍后页面将自动跳转继续操作。若页面长时间没有响应或跳转，请点击<s:a id="aGo" href="%{sendurl}">这里</s:a>
				手动跳转。
			</p>
		</s:else>
	</div>
</body>
</html>