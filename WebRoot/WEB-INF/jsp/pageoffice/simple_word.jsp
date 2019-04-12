<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.zhuozhengsoft.pageoffice.*,java.awt.*"%>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>word在线编辑</title>
</head>
<body>
<%
    PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
    poCtrl1.setServerPage(request.getContextPath()+"/poserver.zz"); //此行必须
    poCtrl1.setSaveFilePage(request.getContextPath()+"/pageoffice/savefile.jsp");
    poCtrl1.setJsFunction_AfterDocumentOpened("AfterDocumentOpened");
    //添加自定义按钮
    poCtrl1.webOpen((String)request.getAttribute("file_name"), OpenModeType.docNormalEdit, "");
    poCtrl1.setTagId("PageOfficeCtrl1");
%>
<script type="text/javascript">
    function AfterDocumentOpened() {
        document.getElementById("PageOfficeCtrl1").SetEnableFileCommand(3, false); // 禁止保存
        document.getElementById("PageOfficeCtrl1").SetEnableFileCommand(4, false); // 禁止另存
        document.getElementById("PageOfficeCtrl1").SetEnableFileCommand(6, false); // 禁止页面设置
    }
</script>
<div style='width:1300px;height:750px;'><po:PageOfficeCtrl id="PageOfficeCtrl1" /></div>

</body>

</html>
