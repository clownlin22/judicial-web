<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.rds.judicial.service.RdsJudicialDicValuesService"%>
<%@page import="com.rds.judicial.model.RdsJudicialKeyValueModel"%>
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
<title>业务汇总</title>
<script type="text/javascript"
	src="<%=basePath %>resources/assets/js/jquery.min.js"></script>
<style type="text/css">
table  {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
}
table th {
	border-width: 1px;
	padding: 4px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
}
table td {
 	text-align:center;
	border-width: 1px;
	padding: 4px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
}
.floatLayer{ width:100%; height:100%; position:fixed; background:#000;  z-index:9990; top:0px; left:0px;filter:alpha(Opacity=50);-moz-opacity:0.50;opacity: 0.50;}
.liadloging{ width:100%; height:auto; position:absolute; top:25%; left:auto; z-index:9995; display:none }
.ldl_Conten{ width:100%; height:200px; line-height:200px; overflow:hidden; text-align:center; }
.ldl_Conten img{ width:300px;   height:200px; line-height:200px; margin:39px 5px; background-size:60px 20px; filter:chroma(color=#ffffff);}
</style>
<script type="text/javascript">
$(document).ready(function(){
	console.log($("#deptname").val())
	    $("#year").change(function(){
	    	getTotalInfo($("#year").val()+"-"+$("#month").val());
		});
	    $("#month").change(function(){
	    	getTotalInfo($("#year").val()+"-"+$("#month").val());
		});
	    $("#body").html("");
		var start_time = $("#accept_time").val()+($("#accept_time_start").val()<10?("-0"+$("#accept_time_start").val()):"-"+$("#accept_time_start").val());
		var end_time = $("#accept_time").val()+($("#accept_time_end").val()<10?("-0"+$("#accept_time_end").val()):"-"+$("#accept_time_end").val());
		if(start_time>end_time)
		{
			alert("你这样会看不到数据的！");
			return;	
		}
		document.getElementById('liadloging').style.display='block';
		$.ajax({
			url:'statistics/financeConfigNew/queryAmoebaSecond.do',
			type:'get',
			dataType : 'json',
			data:{
				"confirm_date_start":start_time+'-01',
				"confirm_date_end":end_time+'-31',
				"deptname":$("#deptname").val()
				},
			success:function(data){
				var head = data[0];
				var head_str="<tr><th style='width:300px'></th>";
				for(var i = 0 ; i < data[0].length ; i ++)
				{
					head_str +="<th style='width:200px'>"+data[0][i]+"</th>";
				}
				document.getElementById("mainTable").style.width=(data[0].length+1)*120+'px';
				head_str+="</tr>";
				$("#head").html(head_str);
				var body_str="";
				for(var j = 1 ; j < data.length;j++){
			    	body_str+="<tr>";
			    	for(var k = 0 ; k < data[j].length ; k ++)
					{
			    		body_str +=data[j][k];
					}
					body_str+="</tr>";
				}
				$("#body").html(body_str);
				document.getElementById('liadloging').style.display='none';
		    },
		    error:function(){
				alert("出错了！");
				document.getElementById('liadloging').style.display='none';
			}
		})
});
function search(){
	$("#body").html("");
	var start_time = $("#accept_time").val()+($("#accept_time_start").val()<10?("-0"+$("#accept_time_start").val()):"-"+$("#accept_time_start").val());
	var end_time = $("#accept_time").val()+($("#accept_time_end").val()<10?("-0"+$("#accept_time_end").val()):"-"+$("#accept_time_end").val());
	if(start_time>end_time)
	{
		alert("你这样会看不到数据的！");
		return;	
	}
	document.getElementById('liadloging').style.display='block';
	$.ajax({
		url:'statistics/financeConfigNew/queryAmoebaSecond.do',
		type:'get',
		dataType : 'json',
		data:{
			"confirm_date_start":start_time+'-01',
			"confirm_date_end":end_time+'-31',
			"deptname":$("#deptname").val()
			},
		success:function(data){
			var head = data[0];
			var head_str="<tr><th style='width:300px'></th>";
			for(var i = 0 ; i < data[0].length ; i ++)
			{
				head_str +="<th  style='width:200px'>"+data[0][i]+"</th>";
			}
			document.getElementById("mainTable").style.width=(data[0].length+1)*120+'px';
			head_str+="</tr>";
			$("#head").html(head_str);
			var body_str="";
			for(var j = 1 ; j < data.length;j++){
		    	body_str+="<tr>";
		    	for(var k = 0 ; k < data[j].length ; k ++)
				{
		    		body_str +=data[j][k];
				}
				body_str+="</tr>";
			}
			$("#body").html(body_str);
			document.getElementById('liadloging').style.display='none';
	    },
	    error:function(){
			alert("出错了！");
			document.getElementById('liadloging').style.display='none';
		}
	})
}
// function jumpToNext(deptname){
// 	window.location.href='/judicial-web/financeAmoebaSecode.jsp?deptname=4';
// }
function jumpToNext(deptname,deptname1){
	window.location.href='/judicial-web/financeAmoebaThreeNew.jsp?deptname='+deptname
	+'&deptname1='+deptname1
	+'&accept_time_start='+$("#accept_time_start").val()
	+'&accept_time_end='+$("#accept_time_end").val();
}
function exportAmoeba(){
	var start_time = $("#accept_time").val()+($("#accept_time_start").val()<10?("-0"+$("#accept_time_start").val()):"-"+$("#accept_time_start").val());
	var end_time = $("#accept_time").val()+($("#accept_time_end").val()<10?("-0"+$("#accept_time_end").val()):"-"+$("#accept_time_end").val());
	if(start_time>end_time)
	{
		alert("你这样数据没有意义！");
		return;	
	}
	window.open("statistics/financeConfigNew/exportAmoebaSecond.do?confirm_date_start="+start_time+'-01'+"&confirm_date_end="+end_time+'-31'+"&deptname="+$("#deptname").val());
}
</script>
</head>
<a href="javascript:history.back(-1)" style="text-decoration: none;">返回</a>
<input id="deptname" type="hidden" value="<%=request.getParameter("deptname")%>">
<%-- <input id="deptname" type="hidden" value="<%=new String(request.getParameter("deptname").getBytes("iso-8859-1"),"utf-8") %>"> --%>
    <div style="margin-top: 15px;text-align:center;font-family: 黑体;font-size: 15px;">
       	年份：<select id="accept_time" style="width: 80px;height: 28px;font-size: 15px;" >
			<%for(int i = year ; i >=2010 ; i--) {%>
				<option><%=i%></option>
			<%} %>
		</select>
		月份：<select id="accept_time_start" style="width: 80px;height: 28px;font-size: 15px;">
			<%for(int i = 1 ; i <=12 ; i++) {
			if(Integer.parseInt(request.getParameter("accept_time_start"))==i){
			%>
				<option selected="selected"><%=i%></option>
			<%} else{ %>
			<option><%=i%></option>
			<%}}%>
		</select>
		到：<select id="accept_time_end" style="width: 80px;height: 28px;font-size: 15px;">
			<%for(int i = 1 ; i <=12 ; i++) {
				if(Integer.parseInt(request.getParameter("accept_time_end"))==i){
			%>
				<option selected="selected"><%=i%></option>
			<%}else{ %>
			    <option><%=i%></option>
			<%}}%>
		</select>
	<button onclick="search();" style="width: 80px;height: 26px;font-size: 15px;">查询</button>
	<button onclick="exportAmoeba();" style="width: 80px;height: 26px;font-size: 15px;">导出</button>
    </div>
	<table align="center" style="margin-top: 15px;" id="mainTable">
		<thead id="head">
		</thead>
		<tbody id="body"></tbody>
		<tfoot id="foot"></tfoot>
	</table>
	<div class="liadloging" id="liadloging">
	    <div class="ldl_Conten">
	        <img src="/judicial-web/resources/assets/img/loading.gif"/>
	    </div>
	    <!--灰色遮罩层 begin-->
		<div class="floatLayer"></div> 
		<!--灰色遮罩层 end-->
	</div>
</body>
</html>