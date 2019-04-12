<%@page import="com.rds.children.model.RdsChildrenPrintCaseModel"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
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
	width:200px;
	margin-left:20px;
	min-height:21px;
	border-bottom:1px solid;
	text-align:center;
}
.info_panel_end2{
	width:345px;
	min-height:21px;
	border-bottom:1px solid;
	text-align:center;
}
.img{
	width:936px;
	height:590px;
}
.img1{
	width:189px;
	height:189px;
	border-radius: 100px;
}
.page {
	page-break-after: always;
	margin-left: 40px;
	margin-right: 20px;
	padding-top: 40px;
}
</style>
</head>

<body>
<div class="page">
<%RdsChildrenPrintCaseModel case_info = (RdsChildrenPrintCaseModel)request.getAttribute("case_info"); %>
     	<div style="position:relative;">
	        <img src="/judicial-web/resources/assets/img/front.jpg"  class="img">
	        <img style="position:absolute; top:40px; left:372px;" src='<%=basePath%>children/print/getImg.do?filename=${photo}' class="img1"/>
	        <div style="position:absolute; top:310px; left:266px; font-size: 27px;color: #1F3C5F">身份证号：${case_info.id_number==""||case_info.id_number==null?"":case_info.id_number}</div>
	    	<div style="position:absolute; top:400px; left:80px; font-size: 25px;width:100%; color: #1F3C5F">
		    	<div style="width: 185px; float: left;">姓名：${case_info.child_name}</div>
		    	<div style="width: 140px; float: left;">性别：${case_info.child_sex==0?'女':'男'}</div>
		    	<%if(!"null".equals(case_info.getFather_name()) ) {%>
		    	<div style="width: 450px; float: left;letter-spacing: 1px;">父亲：${case_info.father_name}&nbsp;${case_info.father_number}</div>
		   		<%} else{%>
		   		<div style="width: 450px; float: left;letter-spacing: 1px;">母亲：${case_info.mother_name}&nbsp;${case_info.mother_number}</div>
		   		<%} %>
		    </div>
	    	<div style="position:absolute; top:450px; left:80px; font-size: 25px;color: #1F3C5F;">
	    		<div style="float: left;">出生：</div>
	    		<div style="float: left; width:250px;letter-spacing: 3.5px;">${case_info.birth_date}</div>
	    		<%if(null !=case_info.getFather_name() && null != case_info.getMother_name() ){ %>
		    	<div style="float: left;width: 450px;letter-spacing: 1px;">母亲：${case_info.mother_name}&nbsp;${case_info.mother_number}</div>
	    		<%} %>
	    	</div>
	    </div>
     <!-- 
         <img src='<%=basePath%>children/print/getImg.do?filename=${photo}' class="img right"/>
         <div class="info_panel">编&ensp;号<div class="info_panel_end right">${case_info.case_code}</div></div>
         <div class="info_panel">姓&ensp;名<div class="info_panel_end right">${case_info.child_name}</div></div>
         <div class="info_panel">性&ensp;别<div class="info_panel_end right">${case_info.child_sex==0?'女':'男'}</div></div>
         <div class="info_panel">出生日期<div class="info_panel_end2 right">${case_info.birth_date}</div></div>
         <div class="info_panel">身份证号码<div class="info_panel_end2 right">${case_info.id_number==""||case_info.id_number==null?"":case_info.id_number}</div></div>
         <div class="info_panel">户籍所在地<div class="info_panel_end2 right">${case_info.house_area==""||case_info.house_area==null?"":case_info.house_area}</div></div>
         <div class="info_panel">生活所在地<div class="info_panel_end2 right">${case_info.life_area==""||case_info.life_area==null?"":case_info.life_area}</div></div>
  -->
  </div>
  <div class="page">
     	<div style="position:relative;">
	        <img src="/judicial-web/resources/assets/img/back.jpg"  class="img">
	        <div style="position:absolute; top:503px; left:120px; font-size: 28px;color: #1F3C5F">${case_info.case_code}</div>
	        <%List<Map<String,String>> list = (List<Map<String,String>>)request.getAttribute("results"); 
	        	int top = 62;
	        	int size = list.size();
	        	for(int i = 0 ; i < size ; i ++){
	        %>
        	<div style="position:absolute; top:<%=top%>px; left:480px; font-family:'微软雅黑'; font-size: 20px;color: #1F3C5F;transform:scaleY(0.8);" ><%=list.get(i).get("name") %></div>
        	<div style="position:absolute; top:<%=top%>px; left:635px; font-family:'微软雅黑'; font-size: 20px;color: #1F3C5F;width: 60px;text-align:center;transform:scaleY(0.8)"><%=list.get(i).get("value").split(",")[0] %></div>
        	<div style="position:absolute; top:<%=top%>px; left:755px; font-family:'微软雅黑'; font-size: 20px;color: #1F3C5F;width: 60px;text-align:center;transform:scaleY(0.8)"><%=list.get(i).get("value").split(",").length==1?list.get(i).get("value").split(",")[0]:list.get(i).get("value").split(",")[1] %></div>
	        	
	        <%top=top+24;} %>
	    </div>
  </div>
</body>
</html>
