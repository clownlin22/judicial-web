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
	padding: 4px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
	width:300px;
}
table td {
 	text-align:center;
	border-width: 1px;
	padding: 4px;
	width:150px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
}
.floatLayer{ width:100%; height:100%; position:fixed; background:#000;  z-index:9990; top:0px; left:0px;filter:alpha(Opacity=50);-moz-opacity:0.50;opacity: 0.50;}
.liadloging{ width:100%; height:auto; position:absolute; top:45%; left:auto; z-index:9995; display:none }
.ldl_Conten{ width:100%; height:200px; line-height:200px; overflow:hidden; text-align:center; }
.ldl_Conten img{ width:300px;   height:200px; line-height:200px; margin:39px 5px; background-size:60px 20px; filter:chroma(color=#ffffff);}

</style>
<script type="text/javascript">
$(document).ready(function(){
	    $("#year").change(function(){
	    	getTotalInfo($("#year").val()+"-"+$("#month").val());
		});
	    $("#month").change(function(){
	    	getTotalInfo($("#year").val()+"-"+$("#month").val());
		});
});
function search(){
	$("#body").html("");
	document.getElementById('liadloging').style.display='block';
	$.ajax({
		url:'queryProgramProvice.do',
		type:'get',
		dataType : 'json',
		data:{
			"accept_time":$("#accept_time").val(),
			"case_area":$("#case_area").val(),
			"case_user":$("#case_user").val(),
			"user_dept_level1":$("#user_dept_level1").val(),
			"case_type":$("#case_type").val()
			},
		success:function(data){
			var head_str="<tr><th>所属省区</th><th>事业部</th><th>项目类型</th><th>所属业务员</th><th>代理</th></tr>";
			$("#head").html(head_str);
			var body_str="";
			for(var i = 0 ; i < data.length;i++){
				var temp0 = 1;
		    	var temp1 = 1;
		    	var temp2 = 1;
		    	var temp3 = 1;
		    	var temp4 = 1;
		    	body_str+="<tr>";
		    	body_str+="<td rowspan="+(temp0)+">"+data[i].case_area+"</td>";
		    	body_str+="<td rowspan="+(temp1)+">"+data[i].user_dept_level1+"</td>";
		    	body_str+="<td rowspan="+(temp2)+">"+data[i].case_type+"</td>";
		    	body_str+="<td rowspan="+(temp3)+">"+data[i].case_user+"</td>";
		    	body_str+="<td rowspan="+(temp4)+">"+data[i].case_agentuser+"</td>";
				body_str+="</tr>";
			}
			$("#body").html(body_str);
			document.getElementById('liadloging').style.display='none';
			if(data.length>1)
				test();
	    },
	    error:function(){
			alert("出错了！");
		}
	})
}
function test(){
	var tab = document.getElementById("body");
	console.log(tab.rows[0].cells[0].innerHTML);
    var maxCol = 4, val, count, start;

    for(var col = maxCol-1; col >= 0 ; col--){
        count = 1;
        val = "";
        for(var i=0; i<tab.rows.length; i++){
            if(val == tab.rows[i].cells[col].innerHTML){
                count++;
            }else{
                if(count > 1){ //合并
                    start = i - count;
                    tab.rows[start].cells[col].rowSpan = count;
                    for(var j=start+1; j<i; j++){
                        tab.rows[j].cells[col].style.display = "none";
                    }
                    count = 1;
                }
                val = tab.rows[i].cells[col].innerHTML;
            }
        }
        if(count > 1 ){ //合并，最后几行相同的情况下
            start = i - count;
            tab.rows[start].cells[col].rowSpan = count;
            for(var j=start+1; j<i; j++){
                tab.rows[j].cells[col].style.display = "none";
            }
        }
    }
	
}
</script>
</head>
<body>
    <div style="margin-top: 15px;text-align:center;font-family: 黑体;font-size: 15px;">
       年份：<select id="accept_time" style="width: 80px;height: 28px;font-size: 15px;">
			<%for(int i = year ; i >=2000 ; i--) {%>
				<option><%=i%></option>
			<%} %>
		</select>
		省区：
		<select id="case_area" style="width: 100px;height: 28px;font-size: 15px;">
				<option value="">全部</option>
				<option value="北京市">北京市</option>
				<option value="上海市">上海市</option>
				<option value="天津市">天津市</option>
				<option value="重庆市">重庆市</option>
				<option value="云南省">云南省</option>
				<option value="内蒙古">内蒙古</option>
				<option value="吉林省">吉林省</option>
				<option value="四川省">四川省</option>
				<option value="山东省">山东省</option>
				<option value="山西省">山西省</option>
				<option value="广东省">广东省</option>
				<option value="江苏省">江苏省</option>
				<option value="江西省">江西省</option>
				<option value="河北省">河北省</option>
				<option value="浙江省">浙江省</option>
				<option value="海南省">海南省</option>
				<option value="湖北省">湖北省</option>
				<option value="湖南省">湖南省</option>
				<option value="甘肃省">甘肃省</option>
				<option value="福建省">福建省</option>
				<option value="贵州省">贵州省</option>
				<option value="辽宁省">辽宁省</option>
				<option value="陕西省">陕西省</option>
				<option value="青海省">青海省</option>
				<option value="黑龙江">黑龙江</option>
				<option value="安徽">安徽</option>
				<option value="广西">广西</option>
				<option value="新疆">新疆</option>
				<option value="宁夏">宁夏</option>
				<option value="西藏">西藏</option>
				<option value="无地区">无地区</option>
		</select>
		事业部：
		<select id="user_dept_level1" style="width: 150px;height: 28px;font-size: 15px;">
				<option value="">全部</option>
				<option value="渠道事业部">渠道事业部</option>
				<option value="仪器设备事业部">仪器设备事业部</option>
				<option value="企管中心">企管中心</option>
				<option value="公司总部">公司总部</option>
				<option value="医学检验事业部">医学检验事业部</option>
				<option value="司法鉴定事业部">司法鉴定事业部</option>
				<option value="大健康事业部">大健康事业部</option>
				<option value="大客户事业部">大客户事业部</option>
				<option value="检测事业部">检测事业部</option>
				<option value="武汉医学检验事业部">武汉医学检验事业部</option>
				<option value="转化医学事业部">转化医学事业部</option>
				<option value="江苏宿迁子渊实验室">江苏宿迁子渊实验室</option>
				<option value="电商部">电商部</option>
				<option value="研发">研发</option>
				<option value="综合管理部">综合管理部</option>
				<option value="设备维护部">设备维护部</option>
		</select>
		姓名：
		<select id=case_user style="width: 150px;height: 28px;font-size: 15px;">
				<option value="">全部</option>
		<%
			Map<String, Object> map = new HashMap<String, Object>();
			List<RdsJudicialKeyValueModel> userModle = (List<RdsJudicialKeyValueModel>)request.getAttribute("userModle");
			for(int i = 0 ; i < userModle.size();i++){
		%>
				<option value="<%=userModle.get(i).getValue() %>"><%=userModle.get(i).getValue() %></option>
		<%
			}
		%>
		</select>
		项目类型：<input type="text" id="case_type" style="width: 150px;height: 22px"/>
	<button onclick="search();" style="width: 80px;height: 26px;font-size: 15px;">查询</button>
    </div>
	<table align="center" style="margin-top: 15px;">
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