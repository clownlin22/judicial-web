<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>条形图</title>
<style type="text/css">
       .page{
			page-break-after: always;
			padding: 0px;
			margin: 0px;
		}
</style>
</head>
<body style="padding: 0px">
   <c:forEach items="${imgs}" var="img">
       <div class="page">
           <img  width="100%" height="96%" src="<%=basePath%>judicial/attachment/getImg.do?filename=${img}"/>
       </div>
   </c:forEach>    
</html>