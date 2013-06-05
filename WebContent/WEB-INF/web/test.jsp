<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="css/style.css" media="all">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-1.9.1.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#toggle-bar a").click(function() {
			$("#aside").toggle(300);
		});
		$(".menu").click(function() {
			$(this.getElementsByTagName("ul")).toggle(300);
		});
		$(".menuitem").click(function() {
			document.getElementById("mainFrame").src = this.getElementsByTagName("a")[0].href;
			return false;
		});
	});
</script>
<style>
/*---此CSS为DIV仿FRAME,不要置于样式表中,以免冲突---*/
html {
	height: 100%;
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
	padding: 80px 0 35px 0;
	overflow: hidden;
	/*min-width:1000px; margin:0 auto;overflow-y:hidden;*/
}
</style>
</head>
<body>
	<!--#header{{{-->
	<div id="header" class="tc"><b>校园通管理系统</b></div>
	<!--}}}#header-->
	<!--#aside{{{-->
	<div id="aside">
		<div class="menu-wrap">
			<%@ include file="include/leftmenu.jsp" %>
		</div>
	</div>
	<!--}}}#aside-->
	<!--#toggle-bar{{{-->
	<div id="toggle-bar">
		<a href="javascript:void(0);" title="折叠/展开菜单栏"></a>
	</div>
	<!--}}}#toggle-bar-->
	<!--#main-content{{{-->
	<div id="main-content">
		<iframe name="mainFrame" id="mainFrame" src="frame.html"
			scrolling="auto" frameborder="0"></iframe>
	</div>
	<!--}}}#main-content-->
	<!--#footer{{{-->
	<div id="footer" class="tc">©2013 计本09-1班 陈豪 3099990019</div>
	<!--}}}#footer-->
</body>
</html>