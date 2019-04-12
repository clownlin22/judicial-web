<%@page import="com.ibm.icu.util.Calendar"%>
<%@page import="com.rds.upc.model.RdsUpcUserModel"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<% 
	response.setHeader("X-UA-Compatible","IE=EmulateIE9");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>司法鉴定业务管理系统V 1.0</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<script type="text/javascript" src="resources/assets/js/jquery.min.js"></script>
<script type="text/javascript" src="resources/assets/js/wizard/lib/jquery.cookie-1.3.1.js"></script>
<link rel="stylesheet" href="resources/assets/css/index.css">
<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
<!-- Fav and touch icons -->
<link rel="shortcut icon" href="resources/assets/img/img_logo.png">
<script type="text/javascript">
function init(){
	document.getElementById('username').focus();
	 document.onkeydown=function mykeyDown(e){  
	        //compatible IE and firefox because there is not event in firefox  
	         e = e||event;  
	         if(e.keyCode == 13) {
	             login();
	             }   
	         return;  
	  }  
		<% RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user"); 
		if(null!=user){%>
			window.location='success.do';
		<%}else{%>
		if ($.cookie("rmbUser") == "true") {
			$("#saveUserName").attr("checked", true);
			$("#username").val($.cookie("username"));
			$("#password").val($.cookie("password"));
			} 
		<%}%>
}
//登录按钮
function login()
{
	var usercode = $("#username").val().trim();
	var password = $("#password").val().trim();
	if(checkIsNull(usercode) || checkIsNull(password))
	{
		alert("请输入用户名和密码！");
		return;	
	}
	var val = 'usercode='+usercode+'&password='+password+'&token='+'&device=web';
	$.ajax({
		url:'upc/user/login.do',
		timeout:30000,
		async:true,
		type:'GET',
		cache:false,
		dataType:'json',
		data:val,
		success:function(data){
			if(!data.success)
			{
				alert("用户名密码错误，请联系管理员！");
			}
			else
			{
				//addCookie("userName", $("#username").val().trim(), 0); 
				window.location='success.do';
			}
	    },
	    error:function(){
			alert("出错了，请联系管理员！");
		}
	})
}
//验空
function checkIsNull(variable)
{
	if (variable == null || variable == undefined || variable == '') 
	{
		return true;
	}else
		return false;
}
//记住用户名密码
function remember()
{
	if($("#saveUserName").is(':checked'))
	{
		$.cookie("rmbUser", "true", { expires: 7 });
		$.cookie("username", $("#username").val().trim(), { expires: 7 });
		$.cookie("password", $("#password").val().trim(), { expires: 7 }); 
	}else
	{
		$.cookie("rmbUser", "false", { expire: -1 });
		$.cookie("username", "", { expires: -1 });
		$.cookie("password", "", { expires: -1 }); 
	}
}
function alock(){
	 var t=document.getElementById("time");
	 var a=new Date()   
	 var timea=a.getHours()+":"+a.getMinutes()+":"+(a.getSeconds()<10?"0"+a.getSeconds():a.getSeconds());
	 t.innerHTML=timea;
	 setTimeout('alock()',1000);
	}
function changeImg(obImg,sNewURL)
{
     if(sNewURL!="") 
         obImg.src=sNewURL;
}
</script>
</head>
<body  onLoad="alock();init();"> 
 <div id="head">
    <div class="logo">
    	<img class="headImg" src="resources/assets/img/img_logo.png">
		<span class="word">司法鉴定管理系统 </span> 
    </div>
    <%
    Calendar c = Calendar.getInstance();
    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    String week="";
    switch(dayOfWeek)
    {
    	case 1 :
    		week="周日";
    		break;
    	case 2 :
    		week="周一";
    		break;
    	case 3:
    		week="周二";
    		break;
    	case 4:
    		week="周三";
    		break;
    	case 5:
    		week="周四";
    		break;
    	case 6:
    		week="周五";
    		break;
    	case 7:
    		week="周六";
    		break;
    }
    java.util.Date now=new java.util.Date();
    %>
    <div class="right">
    	<span class="word"><%=now.getMonth()+1 %>月<%=now.getDate() %>日 &nbsp;<%=week %></span>&nbsp;&nbsp;
    	<span id="time" class="word"></span> 
    </div>
 </div>
<div align="center" class="center">
	<div class="centerHead">
		<div  class="welcome"><font style="font-family: 'Arial';">welcome</font></div>
		<div class="userName">
			<img class="userIcon" src="resources/assets/img/img_01_1.png">
			<div class="userIconOut" style="margin-top: 10px;">
				<div style="font-size: 12px;color: #666666;padding-left: 10px;padding-top: 3px;">用户名</div>
			</div>
			<div class="userNameInput"><input id="username" type="text" style="float: left;width: 120px;border-style:none"></div>
		</div>
		<div class="userPass">
			<img class="userIcon" src="resources/assets/img/img_01_2.png">
			<div class="userIconOut" style="margin-top: 10px;">
				<div class="password">密&nbsp;码</div>
			</div>
			<div class="userNameInput"><input id="password" type="password" style="float: left;width: 120px;border-style:none"></div>
		</div>
		<div class="img"><img id="loginImg" onmouseover="changeImg(this,'resources/assets/img/btn_hov_01_1.png');" onmouseout="changeImg(this,'resources/assets/img/btn_01_1.png');" onclick="login()" style="background-repeat: no-repeat;" src="resources/assets/img/btn_01_1.png"></div>
		<div class="img">
			<input type="checkbox" id="saveUserName" onclick="remember()" style="vertical-align: middle;margin-left: -140px;">
			<font style="vertical-align:middle;font-size: 14px;color: #333333;font-family: '微软雅黑';" >记住用户名</font>
		</div>
	</div>
</div>
<div class="boot" align="center">
	<div class="wordBoot">Copyright(C)2015 rds.com All Rights Reserved</div>
	<div class="wordBoot">江苏紫薇杏林信息科技有限公司 版权所有苏IP备号</div>
</div>
</body>

</html>
