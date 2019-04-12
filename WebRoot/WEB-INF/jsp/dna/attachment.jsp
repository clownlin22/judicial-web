<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String width = request.getAttribute("width").toString();
String height = request.getAttribute("height").toString();
String case_code = request.getAttribute("case_code").toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图片打印</title>
<style type="text/css">
       .page{
			page-break-after: always;
			padding: 0px;
			margin: 0px;
			float: left;
		}
		.yemei{
			float: left;
			text-align: center;
			border-bottom:1px solid #808080;
			width: 80%;
			margin-left: 75px;
			margin-bottom: 15px;
			margin-top: 50px;
			font-family: "宋体";
			font-size:9pt;
			color:#808080;
			padding-bottom: 3px;
		}
</style>
</head>
<body>
		<c:if test="${count ==1}">
	<div class="yemei">
		<span style="float:left">子渊司鉴所[2018]物鉴字第${case_code}号</span><span style="float:right;">共1页第1页</span>
	</div>
</c:if>

     <div class="page">
		     <c:if test="${type==3}">
		        <div style="font-size:18pt;margin-left: 75px;margin-bottom: 10px; font-family:宋体;float: left;">附件&nbsp;被鉴定人照片</div>
		     </c:if>
		     <div align="center">
		        <img style="float: left;margin-left: 75px;" width="<%=width%>px" height="<%=height%>px" src="<%=basePath%>judicial/attachment/getImg.do?filename=${path}"/>
	        </div>
     </div>
</body>
</html>
