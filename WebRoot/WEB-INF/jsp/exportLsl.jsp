<%@page import="com.rds.upc.model.RdsUpcStResultModel"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%
  response.reset();
  response.setContentType("application/vnd.ms-excel;charset=UTF-8");
  response.setHeader("Content-disposition","inline; filename="+new String("流失率".getBytes("gb2312"), "ISO8859-1" )+".xls");
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="../../resources/assets/js/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
.word{font-size: 15px}
table.gridtable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
}
table.gridtable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
}
table.gridtable td {
 	text-align:center;
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
}
</style>
<%		
String[] export12Months = (String[])request.getAttribute("export12Months");
List<String[]> list = (List<String[]>)request.getAttribute("list");
%>
<script type="text/javascript">
</script>
</head>
<body>
<table class="gridtable">
	<tr id="tableHead">
		<td rowspan="2" style="width: 65px;">省</td>
		<td rowspan="2" style="width: 137px;">市</td>
		<td rowspan="2" style="width: 125px;">县</td>
		<%for(int i = 0 ; i < export12Months.length ; i ++){ %>
			<td colspan="2"><%=export12Months[i]%></td>
		<%} %>
	</tr>
	<tr>
		<td>上半月</td>
		<td>下半月</td>
		<td>上半月</td>
		<td>下半月</td>
		<td>上半月</td>
		<td>下半月</td>
		<td>上半月</td>
		<td>下半月</td>
		<td>上半月</td>
		<td>下半月</td>
		<td>上半月</td>
		<td>下半月</td>
		<td>上半月</td>
		<td>下半月</td>
		<td>上半月</td>
		<td>下半月</td>
		<td>上半月</td>
		<td>下半月</td>
		<td>上半月</td>
		<td>下半月</td>
		<td>上半月</td>
		<td>下半月</td>
		<td>上半月</td>
		<td>下半月</td>
	</tr>
	<% 
	int temp = -1;
	for(int i = 0 ; i < list.size() ; i ++){ 
		if("0".equals(list.get(i)[26]))
		{
			for(int k = 25 ; k >=3 ; k --)
			{
				if(Integer.parseInt(list.get(i)[k]) >0) 
				{
					temp = k;
					break;
				}
			}
		}
	%>
		<tr>
		<%for(int j = 0 ; j < 27 ; j ++){ 
			if(temp == j ){
		%>
			<td style="background-color: red;"><%= list.get(i)[j] %></td>
		<% }else{  %>
		<td><%= list.get(i)[j] %></td>
		<%}} %>
		</tr>
	<%} %>
</table>
</body>
</html>