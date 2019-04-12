<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@page import="java.util.Calendar"%>
 <%		
String[] last12Months = new String[12];  
Calendar cal = Calendar.getInstance();  
int year = cal.get(Calendar.YEAR);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇总</title>
<script type="text/javascript"
	src="<%=basePath %>resources/assets/js/jquery.min.js"></script>
<style type="text/css">
table  {
width:100%;
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
}
table th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
}
table td {
 	text-align:center;
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
	    $("#year").change(function(){
	    	getTotalInfo($("#year").val());
		});
        getTotalInfo(<%=year%>);
});
function getTotalInfo(year){
	var dates=["01","02","03","04","05","06","07","08","09","10","11","12"];
	$.ajax({
		url:'getTotalStatistics.do',
		type:'get',
		dataType : 'json',
		data:{"date":year},
		success:function(data){
			var totalModels=data.totalModels;
			var types=data.types;
			var body_str="";
			$.each(types,function(i,type){
				body_str+="<tr><td>"+type.type+"</td><td>"+type.case_type+"</td>";
				var rec_sum=0;
				$.each(dates,function(i,date){
					var total=0; 
					$.each(totalModels,function(j,obj){
                        if(date==obj.date&&obj.case_type==type.key){
                        	total=obj.rec_total;
                        	rec_sum+=obj.rec_total;
                        }
	                });
					body_str+="<td>"+total+"</td>";
				});
				body_str+="<td>"+rec_sum+"</td></tr>"
			});
			$("#body").html(body_str);
			var foot_str="<tr><th colspan='2'>合计</th>";
			var sum=0;
			$.each(dates,function(i,date){
				var total=0; 
				$.each(totalModels,function(i,obj){
					if(date==obj.date){
						total+=obj.rec_total;
					}
				});
				sum+=total;
				foot_str+="<th>"+total+"</th>";
			});
			foot_str+="<th>"+sum+"</th></tr>";
			$("#foot").html(foot_str);
	    },
	    error:function(){
			alert("出错了！");
		}
	})
}

function exportType(){
	window.location.href = "exportTotalStatistics.do?date="+$("#year").val();
}

</script>
</head>
<body>
    <div style="margin-top: 15px; margin-left:200px;text-align:center;font-family: 黑体;font-size: 24px;">
       <select id="year" style="width: 90px;height: 28px;font-size: 24px;">
			<%for(int i = year ; i >=2000 ; i--) {%>
				<option><%=i%></option>
			<%} %>
		</select>
		项目汇总统计表<button onclick="exportType();" style="width: 80px;height: 26px;font-size: 22px;">导出</button>
    </div>
	<table align="center" style="margin-top: 15px;">
		<thead>
			<tr>
				<th rowspan="2">类别</th>
				<th rowspan="2">名称</th>
				<th colspan="12">月份</th>
				<th rowspan="2">合计</th>
			</tr>
			<tr>
				<th>1月</th>
				<th>2月</th>
				<th>3月</th>
				<th>4月</th>
				<th>5月</th>
				<th>6月</th>
				<th>7月</th>
				<th>8月</th>
				<th>9月</th>
				<th>10月</th>
				<th>11月</th>
				<th>12月</th>
			</tr>
		</thead>
		<tbody id="body"></tbody>
		<tfoot id="foot"></tfoot>
	</table>
</body>
</html>