<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
<title>图片打印</title>
<style type="text/css">
.page {
	page-break-after: always;
	padding: 0px;
	margin: 0px;
}
.case_code{
	float: right;
}
</style>
</head>
<body>
	<c:if test="${fn:length(att) ==0}">
		<div>没有上传照片</div>
	</c:if>
	<c:forEach items="${att }" var="att">
		<c:if test="${att.attachment_type==3}">
			<div width="1000px" height="1400px" class="page">
				<div style="font-size: 24px; margin-left: 20px;">附件&nbsp;&nbsp;被检验人照片</div><div class="case_code">${att.case_code}</div>
				<img height='1000px' src="<%=basePath%>judicial/attachment/getImg.do?filename=${att.attachment_path}" />
			</div>
			<div width="1000px" height="1400px" class="page">
				<div style="font-size: 24px; margin-left: 20px;">附件&nbsp;&nbsp;被检验人照片</div><div class="case_code">${att.case_code}</div>
				<img height='1000px' src="<%=basePath%>judicial/attachment/getImg.do?filename=${att.attachment_path}" />
			</div>
			<c:if test="${att.count ==0}">
				<div width="1000px" height="1400px" class="page">
					<div style="font-size: 24px; margin-left: 20px;">附件&nbsp;&nbsp;被检验人照片</div><div class="case_code">${att.case_code}</div>
					<img height='1000px'
						src="<%=basePath%>judicial/attachment/getImg.do?filename=${att.attachment_path}" />
				</div>
			</c:if>
		</c:if>
	</c:forEach>
</body>
</html>