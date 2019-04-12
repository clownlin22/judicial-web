<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div style="font-size:18pt;font-family:宋体;">附件:</div>
	<div style="font-size:18pt;margin-left: 40px;margin-bottom: 20px;margin-top: 15px;font-family:宋体;">编号:${case_code}</div>
	<div align="center">
		<img src="<%=basePath%>alcohol/register/getImg.do?filename=${att.att_path}" width="960" height="1280" />
	</div>
</body>
</html>