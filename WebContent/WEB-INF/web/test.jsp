<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(".item").click(function() {
			var id = this.id;
			$.get("index.jsp",{method:id},function(data,status){
			    $("#content").html(data);
			  });
			return false;
		});
	});
</script>
<style type="text/css">
div
{
	border : 1px solid red;
}
</style>
</head>
<body>
	<div id="top" class="float:left;width:100%;">
		<%@ include file="include/header.jsp" %>
	</div>
	<div style="clear:all;border-width:0;"></div>
	<div style="float:left;width:150px;"><%@ include file="include/leftmenu.jsp" %></div>
	<div id="content" style="margin-left:150px;overflow:auto;">content</div>
</body>
</html>