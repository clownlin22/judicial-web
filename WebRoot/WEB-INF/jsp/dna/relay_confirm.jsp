<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
  <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>     
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
<style type="text/css"> 
    body{
		font-family: "FangSong";
		width:1000px;
		font-size:30px;
	}
	.left{
		float:left;
	}
	.right{
		float:right;
	}
	.title{
		font-size:40px;
		text-align: center;
		font-family:'黑体';
	}
	.title_remark{
		font-size:24px;
		margin-top:20px;
		font-family:"SimSun";
		margin-bottom: 30px;
	}
	table{
	      width:100%;
		  clear:both;
	}
	.tb_per tr{
		height:80px;
	}
	.tb_per td{
		width: 25%;
	}
	.tb_code td{
	    width:25%;
	    text-align: center;
	    font-size:19px;
	}
	.paragraph{
		text-indent: 2em;
		font-size:30px;
		margin-top: 10px;
	}
</style>
</head>
<body>
   <div class="title" style="margin-top:30px;">
     样本交接确认单
   </div> 
   <div class="title_remark right">
      单号：${relayModel.relay_code}    样本数量：${countSample}
   </div> 
   <div style="height: 1000px;">
   <table cellSpacing="0" cellPadding="0" border="solid;" class="tb_code">
       <tr><td colspan="4" style="font-family: '黑体';height: 30px;">样本编号</td></tr>
       <c:choose>
	       <c:when test="${overload==0}">
				 <tr>
				 	<td colspan="4" style="font-family: '黑体'">是你逼我不显示的！</td>
				 </tr>
			</c:when>
	       <c:otherwise> 
	       <c:forEach items="${relaySampleInfos1}" var="sample" begin="0" step="1" varStatus="status"><tr></tr>
	      		<tr>
					<td>${sample.sample_code}</td>
					<td>${relaySampleInfos2[status.index].sample_code}</td>
					<td>${relaySampleInfos3[status.index].sample_code}</td>
					<td>${relaySampleInfos4[status.index].sample_code}</td>
				 </tr>
	       </c:forEach>
		 </c:otherwise>
	 </c:choose> 
       <!--<c:choose>  
		<c:when test="${status.index==0}">
			 <tr>
			 	<td rowspan="35" width="180px">样本编号：</td>
				<td>${sample.sample_code}</td><td>${relaySampleInfos2[status.index].sample_code}</td>
			 </tr>
		</c:when>  
		<c:otherwise>  
		     <tr><td>${sample.sample_code}</td><td>${relaySampleInfos2[status.index].sample_code}</td></tr>
		</c:otherwise>
		</c:choose> 
		-->
   </table>
   <div style="font-size: 30px;font-family:'黑体';margin-top: 20px;">交接备注：</div>
   <div class="paragraph">${relayModel.relay_remark}</div>
   </div>
   <table class="tb_per">
       <tr><td>交接日期:</td><td style="width:30%">${relayModel.relay_time}</td></tr>
       <tr><td>交接人签字:</td><td></td><td>确认人签字:</td><td></td></tr>
       <tr><td>打印日期:</td><td><fmt:formatDate value="<%=new Date() %>" pattern="yyyy-MM-dd" /></td><td>确认日期:</td><td></td></tr>
   </table>
</body>
</html>