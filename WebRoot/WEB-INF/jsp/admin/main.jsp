<%@page import="com.rds.upc.model.RdsUpcUserModel"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"
    %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>司法鉴定业务管理系统后台</title>
</head>
<body>
<link  rel="stylesheet"  href="resources/extjs/resources/css/ext-all.css"/>
<link  rel="stylesheet"  href="resources/extjs/resources/css/icon.css"/>
<link rel="shortcut icon" href="resources/assets/img/img_logo.png">
<style type="text/css">
.white-row .x-grid-cell{ background-color: white;}
.red-row .x-grid-cell{ background-color: #F5C0C0 !important; }
.yellow-row .x-grid-cell{ background-color: #FBF8BF !important; }
.green-row .x-grid-cell{ background-color: #99CC99 !important; }
.blue-row .x-grid-cell{  background-color: #0099FF !important; }
.brown-row .x-grid-cell{  background-color: #A52A2A !important; }
.x-column-header{}
.x-grid-cell-inner{}
</style>
<script type="text/javascript">
<% 
	String userName="";
	String companyid="";
	String usercode ="";
	String userid ="";
	String roletype="";
	String deptcode="";
	RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user"); 
	if(null == user){
	%>
		location.href='loginIndex.jsp';
	<%}else{
		userName = "'" + user.getUsername() +"'";
		companyid =  "'" + user.getCompanyid() +"'";
		usercode =  "'" + user.getUsercode() +"'";
		userid =  "'" + user.getUserid() +"'";
		roletype="'" + user.getRoletype() +"'";
		deptcode="'" + user.getDeptcode() +"'";
	}
%>
</script>
<script type="text/javascript" src="resources/iNotify.js"></script>
<script type="text/javascript" src="resources/extjs/bootstrap.js"></script>
<script type="text/javascript" src="resources/extjs/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="resources/App.js" charset="utf-8"></script>
<script type="text/javascript" src="resources/ux/TreeCombo.js" charset="utf-8"></script>
<script type="text/javascript" src="resources/ux/MonthPicker.js" charset="utf-8"></script>
<script type="text/javascript" src="resources/util/ObjectUtil.js" charset="utf-8"></script>
<script type="text/javascript" src="resources/util/Map.js" charset="utf-8"></script>
<script charset="text/javascript" src="resources/kindeditor/kindeditor.js" charset="utf-8"></script>
<script charset="text/javascript" src="resources/kindeditor/lang/zh_CN.js" charset="utf-8"></script>
<link rel="stylesheet" type="text/css" href="resources/ux/css/ItemSelector.css" />
<script type="text/javascript">
var companyid = <%= companyid%>;//'54783d747af74ef8bb8e9cf951b7abec';//
var usercode =  <%= usercode%>;//'super_admin';'${usercode}';
var userid = <%=userid%>;//'8fe97d74e24b4c568b40b8ceb4cbe5b3'; '${userid}';
var _super = 'admin';//'${_super}';
var userName = <%= userName%>;
var roletype = <%= roletype%>;
var deptcode = <%= deptcode%>
Ext.Ajax.on('requestexception',function(conn,response,options) {
    if(response.status=="999"){
        Ext.Msg.alert('提示', '会话超时，请重新登录!', function(){
            window.location = 'loginIndex.jsp';
        });
    }
});
</script>
</body>
<OBJECT ID="PrintAX" classid="clsid:AE1A309B-6FFA-4FCF-B07F-CB97FFD56B1B" codebase="<%=basePath%>IEprint.ocx#version=" width=0 height=0 hspace=0  vspace=0 ></OBJECT>
</html>