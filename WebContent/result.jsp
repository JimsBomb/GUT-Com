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
var iTime = 3;
var account;
function remainTime()
{
    if (iTime > 0)
    {
    	account = setTimeout("remainTime()",1000);
        //iTime=iTime-1;
    }
    else {
    	var backto = document.getElementById("backto");
    	if(backto.getAttribute("href") == "") {
    		backto.setAttribute("href", "./index.jsp");
    	}
    	backto.click();
    }
		document.getElementById("timer").innerHTML = iTime--;
	}
</script>
</head>
<body onload="remainTime()">
<div class="opresult">
	<s:property value="#request.resultMsg" />
	<a id="backto" href='<s:property value="#request.backTo" />'><span id="timer">3</span>秒后返回操作前页面，若没有自动跳转，请点击这里</a>
</div>
</body>
</html>