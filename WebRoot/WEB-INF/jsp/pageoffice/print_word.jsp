<%@ page language="java"
         import="java.util.*,com.zhuozhengsoft.pageoffice.*"
         pageEncoding="gb2312"%>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po"%>
<%
    PageOfficeCtrl poCtrl=new PageOfficeCtrl(request);
//设置服务器页面
    poCtrl.setServerPage(request.getContextPath()+"/poserver.zz");

    poCtrl.setCustomToolbar(false);
    poCtrl.setOfficeToolbars(false);
    poCtrl.setAllowCopy(false);

    poCtrl.setJsFunction_AfterDocumentOpened("AfterDocumentOpened");
//打开Word文档
    poCtrl.webOpen((String)request.getAttribute("file_name"),OpenModeType.docReadOnly,"张佚名");
    poCtrl.setTagId("PageOfficeCtrl1");//此行必需
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>最简单的打开保存Word文件</title>
</head>
<body>
<script type="text/javascript">
    function AfterDocumentOpened() {
        document.getElementById("PageOfficeCtrl1").SetEnableFileCommand(3, false); // 禁止保存
        document.getElementById("PageOfficeCtrl1").SetEnableFileCommand(4, false); // 禁止另存
        document.getElementById("PageOfficeCtrl1").SetEnableFileCommand(6, false); // 禁止页面设置
    }
</script>
<form id="form1" >
    <div style=" width:auto; height:700px;">
        <po:PageOfficeCtrl id="PageOfficeCtrl1">
        </po:PageOfficeCtrl>
    </div>
</form>
</body>
</html>
