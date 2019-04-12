<%@page import="com.ibm.icu.util.Calendar"%>
<%@ page import="java.util.List" language="java"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
  <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML>
<HEAD>
<META content="text/html; charset=UTF-8" http-equiv="Content-Type">
<script type="text/javascript">
</script> 
<STYLE type="text/css">
.panel_title font_bold{
font:"黑体";
}
.font_nomal{
	font-weight:normal;
	font-family: "FangSong";
	font-size: 30px;
}
.font_bold{
	font-weight:normal;
	font-family: "黑体";
	font-size: 30px;
}
.headerNav {
	font-size: 38px;
	padding-left:120px;
	text-align: center;
	font-family:'黑体';
}
.header_mark {
	font-size: 22px;
	padding-left:240px;
	font-weight: lighter;
	text-align:right;
}
.panel_title{
	font-size:34px;
	font-family: "黑体";
}
.panel{
	font-size:30px;
}
.panel_column{
	margin-left:100px;
}

.body{
	width:100%;	
}
.page{
	page-break-after: always;
	line-height:40px;
	margin-left: 50px;
}
div{
	 margin-top:20px;
	 margin-left:10px;
	 line-height:40px;
}
.pageem{
	 text-indent: 2em;
}
li{
	margin-top:30px;
}
.panel_table{
	width:100%;
	text-align:center;
	border:1;
}

.panel_table_3{
	width:100%;
	text-align:center;
	border:1;
}
.panel_table th{
	 height:100px; 
	  width:33.3%;
}
.panel_table tbody td{
	 height:40px; 
     width:33.3%;
	 border-bottom:none;
	 border-top:none;
}

.panel_table_3 tbody td{
	 height:40px; 
     width:21%;
	 border-bottom:none;
	 border-top:none;
}

.panel_table_3 th{
	 height:100px; 
	  width:21%;
}

tr{
	line-height:33px;
}
SUP{
  font-size: 14px;
}
span{
  margin-left: 10px;
}
.topSize{
	margin-top:10px;
}


</STYLE>

<META name="GENERATOR" content="MSHTML 9.00.8112.16636"></HEAD>
<BODY style="width: 1060px;">
			<%
				Calendar now = Calendar.getInstance();
				int attachmentsSize = Integer.parseInt(request.getAttribute("attachmentsSize").toString()) ; 
				List<String> list = (List<String>)request.getAttribute("list"); 
				int listSize = list.size()-1;
				int pageSize = 0;
				pageSize = listSize/25+1;
				if(listSize%25==0)
				{
					pageSize = listSize/25;
				}
				for(int i = 0 ; i < pageSize ; i ++)
				{
			%>
			<DIV class="page" id="page">
				<DIV style="font-size: 20px; float: left; margin-bottom: 6px;"><%=request.getAttribute("judgename") %>[<%=request.getAttribute("case_date") %>]法临鉴字第<%=list.get(listSize) %>号</DIV>
				<% if(attachmentsSize>0) {%>
				<DIV style="font-size: 20px; float: right;">共<%=pageSize+1 %>页  第<%=i+1 %>页</DIV>
				<%}else{ %>
				<DIV style="font-size: 20px; float: right;">共<%=pageSize %>页  第<%=i+1 %>页</DIV>
				<%} %>
				<hr style="min-width:800px;clear: both; height:1px;border:none;border-top:2px solid #555555;">
				<!-- 上面为头 -->
				<%
					for(int j = i*25 ; j < i*25+25 ; j ++ )
					{
						if(list.get(j).contains("<td>") && j%25==0  && !(list.get(j).contains("<table")) )
						{
							out.println("<table style='width:90%;margin-top:10px' border='1' bordercolor='#000000' align='center' cellpadding='2' cellspacing='0'><tbody>");
						}
						if(list.size()-1 <= j)
						{
							break;
						}
						if(list.get(j).contains("<td>") && j+1==25*(i+1)  && !(list.get(j).contains("table>")) )
						{
							out.println(list.get(j)+"</tbody></table>");
							break;
						}
						out.println(list.get(j));

					}
				%>
			</DIV>
			<%} 
			%>
			<%
			if(attachmentsSize>0){
			%>
			<DIV class="page" id="page">
				<DIV style="font-size: 20px; float: left; margin-bottom: 6px;"><%=request.getAttribute("judgename") %>[<%=now.get(Calendar.YEAR) %>]法临鉴字第<%=list.get(listSize) %>号</DIV>
				<DIV style="font-size: 20px; float: right;">共<%=pageSize+1 %>页  第<%=pageSize+1 %>页</DIV>
				<hr style="min-width:800px;clear: both; height:1px;border:none;border-top:2px solid #555555;">
				<c:forEach items="${attachments}" var="attachment">
					<div style="text-align: center;">
						<img width="300" height="300" src="/judicial-web/appraisal/info/getImg.do?filename=${attachment.attachment_filename }"  >
						<div style="text-align: center;margin-top: 0px;">${attachment.attachment_name }</div>
					</div>
				</c:forEach>
			</DIV>
			<%} %>
  </BODY></HTML>