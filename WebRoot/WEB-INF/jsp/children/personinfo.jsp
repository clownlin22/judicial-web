<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
      <%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
  <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
.left{
	float: left;
}
.info{
	width:460px;
	margin-top:115px;
	font-size:20px;
	font-family:"宋体";
}
.info_panel{
	margin-bottom:55px;
}
.right{
	float:right;
}
.info_panel_end{
	width:345px;
	min-height:21px;
	border-bottom:1px solid;
	text-align:center;
}
</style>
</head>

<body>
<div class="left info">
         <div class="info_panel">编&ensp;号<div class="info_panel_end right">${case_info.case_code}</div></div>
         <div class="info_panel">父亲姓名<div class="info_panel_end right">${case_info.father_name==""||case_info.father_name==null?"":case_info.father_name}</div></div>
         <div class="info_panel">身份证号<div class="info_panel_end right">${case_info.father_number==""||case_info.father_number==null?"":case_info.father_number}</div></div>
         <div class="info_panel">手机号码<div class="info_panel_end right">${case_info.father_phone==""||case_info.father_phone==null?"":case_info.father_phone}</div></div>
         <div class="info_panel" style="margin-top:75px;">母亲姓名<div class="info_panel_end right">${case_info.mother_name==""||case_info.mother_name==null?"":case_info.mother_name}</div></div>
         <div class="info_panel">身份证号<div class="info_panel_end right">${case_info.mother_number==""||case_info.mother_number==null?"":case_info.mother_number}</div></div>
         <div class="info_panel">手机号码<div class="info_panel_end right">${case_info.mother_phone==""||case_info.mother_phone==null?"":case_info.mother_phone}</div></div>
     </div>
</body>
</html>
