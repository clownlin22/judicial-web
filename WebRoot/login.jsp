<%@page import="com.rds.upc.model.RdsUpcUserModel"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>司法鉴定业务管理系统V 1.0</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<!-- Le styles -->
<script type="text/javascript" src="resources/assets/js/jquery.min.js"></script>
<script type="text/javascript" src="resources/assets/js/wizard/lib/jquery.cookie-1.3.1.js"></script>
<!--  <link rel="stylesheet" href="assets/css/style.css"> -->
<link rel="stylesheet" href="resources/assets/css/loader-style.css">
<link rel="stylesheet" href="resources/assets/css/bootstrap.css">
<link rel="stylesheet" href="resources/assets/css/signin.css">
<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
<!-- Fav and touch icons -->
<link rel="shortcut icon" href="resources/assets/img/img_logo.png">
<script type="text/javascript">
//初始化方法
window.onload=function(){
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
		dataType:'json',
		data:val,
		success:function(data){
			if(!data.success)
			{
				alert(data.msg);
			}
			else
			{
				//addCookie("userName", $("#username").val().trim(), 0); 
				window.location='success.do';
			}
	    },
	    error:function(){
			alert("出错了！");
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
</script>
</head>
<body> 
    <!-- Preloader -->
    <div id="preloader">
        <div id="status">&nbsp;</div>
    </div>
    <div class="container">
        <div class="" id="login-wrapper">
            <div class="row">
                <div class="col-md-4 col-md-offset-4">
                    <div id="logo-login">
                        <h1>司法鉴定业务管理系统<span>v 1.0</span>
                        </h1>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 col-md-offset-4">
                    <div class="account-box"> 
                        <!-- <form role="form" action="adminlogin.do"> -->
                            <div class="form-group">
                                <!--a href="#" class="pull-right label-forgot">Forgot email?</a-->
                                <label for="inputUsernameEmail">用户名</label>
                                <input type="text" name="usercode" id="username" class="form-control">
                            </div>
                            <div class="form-group">
                                <!--a href="#" class="pull-right label-forgot">Forgot password?</a-->
                                <label for="inputPassword">密码</label>
                                <input type="password" name="password" id="password" class="form-control">
                            </div>
                            <div class="checkbox pull-left">
                                <label>
                                    <input type="checkbox" id="saveUserName" onclick="remember()">记住用户名密码</label>
                            </div>
                            <button class="btn btn btn-primary pull-right" onclick="login()">登 录
                            </button>
                       <!--  </form> -->
                       <!--  <a class="forgotLnk" href="index.html"></a> -->
                        <!--div class="or-box">
                          
                            <center><span class="text-center login-with">Login or <b>Sign Up</b></span></center>
                            <div class="row">
                                <div class="col-md-6 row-block">
                                    <a href="index.html" class="btn btn-facebook btn-block">
                                        <span class="entypo-facebook space-icon"></span>Facebook</a>
                                </div>
                                <div class="col-md-6 row-block">
                                    <a href="index.html" class="btn btn-twitter btn-block">
                                        <span class="entypo-twitter space-icon"></span>Twitter</a>
                                        
                                </div>

                            </div>
                            <div style="margin-top:25px" class="row">
                                <div class="col-md-6 row-block">
                                    <a href="index.html" class="btn btn-google btn-block"><span class="entypo-gplus space-icon"></span>Google +</a>
                                </div>
                                <div class="col-md-6 row-block">
                                    <a href="index.html" class="btn btn-instagram btn-block"><span class="entypo-instagrem space-icon"></span>Instagram</a>
                                </div>

                            </div>
                        </div>
                        <hr>
                        <div class="row-block">
                            <div class="row">
                                <div class="col-md-12 row-block">
                                    <a href="index.html" class="btn btn-primary btn-block">Create New Account</a>
                                </div>
                            </div>
                        </div-->
                      <!--   <div class="row-block">--> 
                      	<div class="row-block">
	                        <div class="row">
		                    </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div style="text-align:center;margin:0 auto;">
        <div style="color:#fff;margin-top: 10px;font-size: 11px;">Copyright(C)2015 rds.com All Rights Reserved</div>
        <div style="color:#fff;font-size: 12px;">南京紫薇杏林信息科技有限公司 版权所有 苏IP备号</div>
            <!-- <h6 style="color:#fff;">Copyright(C)2015 rds.com All Rights Reserved
				南京紫薇杏林信息科技有限公司 版权所有 苏IP备号</h6> -->
        </div>

    </div>
    <!--  END OF PAPER WRAP -->




    <!-- MAIN EFFECT
    <script type="text/javascript" src="resources/assets/js/preloader.js"></script>
    <script type="text/javascript" src="resources/assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="resources/assets/js/app.js"></script>
    <script type="text/javascript" src="resources/assets/js/load.js"></script>
    <script type="text/javascript" src="resources/assets/js/main.js"></script>
     -->




</body>

</html>
