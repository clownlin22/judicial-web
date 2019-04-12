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
	width:800px;
	margin-top:185px;
	font-size:20px;
	font-family:"宋体";
}
.right{
	float:right;
}
.table{
	width:370px;
	margin-left:195px;
	margin-top:100px;
	float:left;
	font-size:18px;
	text-align:center;
}
td{
	height:20px;
}
.tb_name{
	width:167px;
}
.p_table{
   width:100% ;
   border:none; 
   font-size: 18px;
   font-family: "Times New Roman";
}
</style>
</head>

<body>
<div class="left info">
     </div>
     <table border="1px" cellspacing="0px" class="table">
       <tr><td class="tb_name" style="height:50px">基因座</td><td style="height:50px"><div>${case_info.child_name}</div><div>${case_info.case_code}</div></td></tr>
       <c:forEach items="${case_result}" var="list" varStatus="status"><tr>
				                  <c:forEach items="${list}" var="map">
				                       <c:choose>  
										   <c:when test="${map.key=='name'}">
										       <td class="tb_name">${map.value}</td>
										   </c:when>  
										   <c:otherwise> 
										      <td>
												        <table class="p_table" cellspacing="0px" cellpadding="0px;">
													        <tr>
														        <td style='text-align:center;width:85px;'>
									                                ${fn:substringBefore(map.value,",")}
																</td>
																<td style='text-align:center;width:30px;'>
																</td>
																<td style='text-align:center;width:85px;'>
																    ${fn:substringAfter(map.value,",")}
																</td>
															</tr>
														</table>
											   </td>
										   </c:otherwise>  
										</c:choose>  
				                  </c:forEach>
				                  </tr></c:forEach>
     </table>
</body>
</html>
