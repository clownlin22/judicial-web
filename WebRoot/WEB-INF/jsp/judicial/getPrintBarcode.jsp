<%--
  Created by IntelliJ IDEA.
  User: XK
  Date: 2017/1/19
  Time: 1:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>Title</title>
    <style type="text/css">
        .page {
            page-break-after: always;
            padding: 0px;
            margin: 0px;
        }
    </style>
</head>
<body style="padding: 0px">
<c:forEach items="${msgs}" var="msg">
<%--<div>${msg}</div>--%>
<br/>
<div class="page">
    <img width="100%" height="96%" src="<%=basePath%>barcode?msg=${msg}&fmt=image/jpeg&type=code39"/>
</div>
</c:forEach>
</html>
