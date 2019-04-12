<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
  <%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
String[] last12Months = new String[12];  
Calendar cal = Calendar.getInstance();  
cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)); //要先+1,才能把本月的算进去</span>  
for(int i=0; i<12; i++){  
    cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1); //逐次往前推1个月  
    last12Months[11-i] = cal.get(Calendar.YEAR)+ "-" + (cal.get(Calendar.MONTH)+1);  
} 
int year = cal.get(Calendar.YEAR);
%>
<script type="text/javascript">
var i = 0;
var oldHtml = "";
$.ajax({
	url : 'queryallStatisticsl.do',
	timeout : 30000,
	async : true,
	type : 'GET',
	cache : false,
	dataType : 'json',
	data : "year=" ,
	success : function(data) {
		var html = "";
		var temp = "";
		for(var i = 0 ; i < data.length ; i ++)
		{
			if(data[i][26]==0)
			{
				for(var k = 26 ; k >=3 ; k --)
				{
					if(data[i][k]>0) 
					{
						temp = k;
						break;
					}
				}
			}
			html+='<tr>';
			for(var j = 0 ; j < 27 ; j ++)
			{
				if(temp != "" && temp == j )
					html+='<td style="background-color: red;">'+data[i][j]+'</td>';
				else
					html+='<td>'+data[i][j]+'</td>';
										
			}
			html+='</tr>';
		}
		console.log(html);
		$(".gridtable").append(html);
	},
	error : function() {
		alert("出错了！");
	}
})
function changeProvice(proviceId)
{
	var val = 'proviceId='+proviceId;
	$.ajax({
		url:'queryallcity.do',
		timeout:30000,
		async:true,
		type:'GET',
		cache:false,
		dataType:'json',
		data:val,
		success:function(data){
			var html="<option value =''>请选择</option>";
			$("#county").html(html);
			for(var i = 0 ; i < data.length ; i ++)
			{
				html += '<option value="'+data[i].key+'">'+data[i].value+'</option>'; 
			}
			console.log(html);
			$("#city").html(html);
	    },
	    error:function(){
			alert("出错了！");
		}
	})
}
function changeCity(cityId)
{
	var val = 'cityId='+cityId;
	$.ajax({
		url:'queryallcounty.do',
		timeout:30000,
		async:true,
		type:'GET',
		cache:false,
		dataType:'json',
		data:val,
		success:function(data){
			var html="<option value =''>请选择</option>";
			for(var i = 0 ; i < data.length ; i ++)
			{
				html += '<option value="'+data[i].key+'">'+data[i].value+'</option>'; 
			}
			console.log(html);
			$("#county").html(html);
	    },
	    error:function(){
			alert("出错了！");
		}
	})
}

function search(){
		val = 'provice=' + $("#provice").val() + "&city=" + $("#city").val()
					+ "&county=" + $("#county").val() + "&year=" + $("#year").val();
		if(i==0)
		{
			oldHtml = $("#tableHead").html();
		}
		$.ajax({
			url : 'queryallStatisticsl.do',
			timeout : 30000,
			async : true,
			type : 'GET',
			cache : false,
			dataType : 'json',
			data : val,
			success : function(data) {
				var html = "";
				if(<%= year%>+1 != $("#year").val())
				{
					var year = $("#year").val();
					html += '<tr id="tableHead"><td rowspan="2" style="width: 65px;">省</td><td rowspan="2" style="width: 137px;">市</td><td rowspan="2" style="width: 125px;">县</td>';
					for(var i = 1 ; i < 13 ; i ++)
					{
						html += '<td colspan="2">'+year+'-'+i+'</td>';
					}
					html+='</tr><tr>';
				}else
				{
					html += '<tr id="tableHead">' + oldHtml + '</tr><tr>';
				}
				for(var i =0 ; i < 12 ; i++)
				{
					html +='<td>上半月</td>';
					html +='<td>下半月</td>';
				}
				html +='</tr>';
				var temp1="";
				for(var i = 0 ; i < data.length ; i ++)
				{
					html+='<tr>';
					if(data[i][26]==0)
					{
						for(var k = 26 ; k >=3 ; k --)
						{
							if(data[i][k]>0) 
							{
								temp1 = k;
								break;
							}
						}
					}
					for(var j = 0 ; j < 27 ; j ++)
					{
						if(temp1 != "" && temp1 == j )
							html+='<td style="background-color: red;">'+data[i][j]+'</td>';
						else
							html+='<td>'+data[i][j]+'</td>';
					}
					html+='</tr>';
				}
				console.log(html);
				$(".gridtable").html(html);
			},
			error : function() {
				alert("出错了！");
			}
		})
		i++
	}

function exportExcel(){
	val = 'provice=' + $("#provice").val() + "&city=" + $("#city").val()
	+ "&county=" + $("#county").val() + "&year=" + $("#year").val();
	window.location = "export.do?"+val;
}
</script>
</head>
<body>
<form id="form1" >
	<div style="margin-top: 20px;" align="center">
		<span class="word">地区 &nbsp;&nbsp;省:</span>
			<select id="provice" name = "provice" onchange="changeProvice(this.value)" style="width: 80px;">
				<option value ="">请选择</option>
				<c:forEach items="${proviceList}" var="type" >
					<option value ="${type.key }">${type.value}</option>
				</c:forEach>
			</select> 
		<span class="word">&nbsp;市:</span>
		<select id="city" name = "city" onchange="changeCity(this.value)"  style="width: 150px;">
			<option value ="">请选择</option>
			<c:forEach items="${cityList}" var="type" >
				<option value ="${type.key }">${type.value}</option>
			</c:forEach>
		</select> 
 		<span class="word">&nbsp;县:</span>
 		<select id="county" name ="county" style="width: 140px;">
 			<option value ="">请选择</option>
			<c:forEach items="${countyList}" var="type" >
				<option value ="${type.key }">${type.value}</option>
			</c:forEach>
		</select> 
		 日期:
		<select id="year" style="width: 100px;">
			<%for(int i = year ; i >=1900 ; i--) {%>
				<option><%=i+1 %></option>
			<%} %>
		</select> 
	 	<input type="button" onclick="search()" value="查询">
	 	<input type="button" onclick="exportExcel()" value="导出">  
	</div>
	<hr>
	<div align="center">
		<table class="gridtable">
			<tr id="tableHead">
				<td rowspan="2" style="width: 65px;">省</td>
				<td rowspan="2" style="width: 137px;">市</td>
				<td rowspan="2" style="width: 125px;">县</td>
				<%for(int i = 0 ; i < last12Months.length ; i ++){ %>
					<td colspan="2"><%=last12Months[i] %></td>
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
		</table>
	</div>
</form>
</body>
</html>