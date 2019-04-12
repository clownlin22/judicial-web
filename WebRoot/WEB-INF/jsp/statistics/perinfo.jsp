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
	width:300px;
}
table td {
 	text-align:center;
	border-width: 1px;
	padding: 8px;
	width:150px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
	    $("#year").change(function(){
	    	getTotalInfo($("#year").val()+"-"+$("#month").val());
		});
	    $("#month").change(function(){
	    	getTotalInfo($("#year").val()+"-"+$("#month").val());
		});
        getTotalInfo(<%=year%>+"-01");
});
function getTotalInfo(year){
	$.ajax({
		url:'getPerStatistics.do',
		type:'get',
		dataType : 'json',
		data:{"date":year},
		success:function(data){
			var types=data.types;
			var ownPersons=data.ownPersons;
			var totalModels=data.totalModels;
			var head_str="<tr><th>姓名</th>";
			$.each(types,function(i,type){
				head_str+="<th>"+type.case_type+"</th>";
			});
			head_str+="<th>合计</th></tr>";
			$("#head").html(head_str);
			var body_str="";
			$.each(ownPersons,function(i,ownperson){
				body_str+="<tr><td>"+ownperson+"</td>";
				var sum=0;
				$.each(types,function(i,type){
					   var total=0;
						$.each(totalModels,function(i,obj){
							if(obj.case_type==type.key&&obj.ownperson==ownperson){
								total=(obj.pre_total-obj.rec_total);
							}
						});
						sum+=total;
						body_str+="<td>"+total+"</td>";
				});
				body_str+="<td>"+sum+"</td></tr>";
			});
			$("#body").html(body_str);
			var foot_str="<tr><th>合计</th>";
			var all_sum=0;
			$.each(types,function(i,type){
				var total=0;
				$.each(totalModels,function(i,obj){
					if(obj.case_type==type.key){
						total+=(obj.pre_total-obj.rec_total);
					}
				});
				foot_str+="<th>"+total+"</th>";
				all_sum+=total;
		    });
		    foot_str+="<th>"+all_sum+"</th></tr>";
			$("#foot").html(foot_str);
	    },
	    error:function(){
			alert("出错了！");
		}
	})
}
function exportPer(){
	window.location.href = "exportPerBanlance.do?date="+$("#year").val()+"-"+$("#month").val();
}
</script>
</head>
<body>
    <div style="margin-top: 15px; margin-left:200px;text-align:center;font-family: 黑体;font-size: 24px;">
       <select id="year" style="width: 90px;height: 28px;font-size: 24px;">
			<%for(int i = year ; i >=2000 ; i--) {%>
				<option><%=i%></option>
			<%} %>
		</select>年
		<select id="month" style="width: 58px;height: 28px;font-size: 24px;">
				<option value="01">1</option>
				<option value="02">2</option>
				<option value="03">3</option>
				<option value="04">4</option>
				<option value="05">5</option>
				<option value="06">6</option>
				<option value="07">7</option>
				<option value="08">8</option>
				<option value="09">9</option>
				<option value="10">10</option>
				<option value="11">11</option>
				<option value="12">12</option>
		</select>
		月项目汇总统计表<button onclick="exportPer();" style="width: 80px;height: 26px;font-size: 22px;">导出</button>
    </div>
	<table align="center" style="margin-top: 15px;">
		<thead id="head">
		</thead>
		<tbody id="body"></tbody>
		<tfoot id="foot"></tfoot>
	</table>
</body>
</html>