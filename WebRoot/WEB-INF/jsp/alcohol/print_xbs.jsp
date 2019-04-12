<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>酒精</title>

<style type="text/css">
.page {
	page-break-after: always;
	margin-left: 40px;
	margin-right: 20px;
	padding-top: 40px;
}

.pagetitle {
	font-size: 22px;
	margin-bottom: 6px;
}

.left {
	float: left;
}

.right {
	float: right;
}

SUP {
	font-size: 20px;
}

hr {
	min-width: 800px;
	clear: both;
	height: 1px;
	border: none;
	border-top: 2px solid #555555;
}

.title {
	font-size: 38px;
	padding-left: 110px;
	text-align: center;
	padding-top: 30px;
}

.bold {
	font-family: "黑体";
}

.title_mark {
	margin-top: 20px;
	font-size: 22px;
	padding-left: 240px;
	font-weight: lighter;
	text-align: right;
}

.panel_title {
	font-size: 34px;
	padding-top: 20px;
	margin-left: 50px;
	margin-bottom: 20px;
}

.panel_titlet {
	font-size: 20px;
	/* 	padding-top: 20px; */
	margin-left: 660px;
	/* 	margin-bottom: 20px; */
}

.panel {
	font-size: 32px;
	margin-top: 30px;
	margin-left: 50px;
	margin-bottom: 10px;
}

.panelbox {
	font-size: 30px;
	line-height: 60px;
	text-indent: 2em;
	margin-top: 5px;
	word-wrap: break-word;
	word-break: break-all;
}

.panel_p {
	font-size: 30px;
}

.file {
	font-size: 34px;
}

.content_p {
	font-size: 30px;
	line-height: 60px;
	text-indent: 2em;
	margin-top: 5px;
}

.content_div {
	padding-left: 60px;
}

.end_per {
	text-align: left;
	margin-left: 350px;
	/*
	margin-top: 20px;
	*/
	font-size: 30px;
}

.end_time {
	text-align: right;
	margin-right: 30px;
	font-size: 30px;
	/*
	margin-top: 20px;
	*/
}

.end {
	margin-top: 30px;
	font-size: 22px;
	line-height: 60px;
}

.time {
	text-align: right;
	font-size: 30px;
	margin-top: 50px;
	margin-right: 20px;
}

body {
	font-family: "FangSong";
}

 

</style>
</head>

<body style="width: 1060px;">
	<c:forEach items="${caseInfos}" var="caseInfo">
		<div class="page">
			<div class="left pagetitle">
				子渊司鉴所[
				<c:out value="${fn:substring(caseInfo.accept_time, 0, 4)}" />
				]毒鉴字第${caseInfo.case_num}号
			</div>
			<div class="right pagetitle">共3页第1页</div>
			<hr>
			<div class="title bold">
				<B>宿迁子渊司法鉴定所司法鉴定意见书</B>
			</div>

			<div style="margin-top: 30px;"></div>
			<div class="panel_titlet bold">
				子渊司鉴所[
				<c:out value="${fn:substring(caseInfo.accept_time, 0, 4)}" />
				]毒鉴字第${caseInfo.case_num}号
			</div>
			<div style="margin-top: 20px;"></div>

			<div class="panel_title bold">一、基本情况</div>
			<div class="panel">
				<span class="bold">委托单位：</span><span class="panel_p">${caseInfo.client}</span>
			</div>
			<div class="panel">
				<span class="bold">委托事项：</span><span class="panel_p">乙醇成分分析
					${caseInfo.is_check gt 0 ? '（重新鉴定）' : ''}</span>
			</div>
			<div class="panel">
				<span class="bold">委托日期：</span><span class="panel_p">${caseInfo.client_time}</span>
			</div>
			<div class="panel">
				<span class="bold">受理日期：</span><span class="panel_p">${caseInfo.accept_time}</span>
			</div>
			<div class="panel">
				<span class="bold">被检验人：</span><span class="panel_p">${caseInfo.si.sample_name}<c:if
						test="${caseInfo.si.id_number!= null && caseInfo.si.id_number!=''}">（身份证号：${caseInfo.si.id_number}）</c:if></span>
			</div>
			<div class="panel">
				<span class="bold">送检材料：</span><span class="panel_p">血液<c:if
						test="${caseInfo.case_code !=  null && caseInfo.case_code != ''}">（样品编号：<span>${caseInfo.case_code}）</span>
					</c:if></span>
			</div>

			<div class="panel_title bold">二、基本案情</div>
			<div class="panelbox">
				<c:if
					test="${caseInfo.case_intr !=  null && caseInfo.case_intr != ''}">&nbsp;${caseInfo.case_intr} </c:if>
			</div>
			<div class="panelbox">
				<c:if
					test="${caseInfo.case_det !=  null && caseInfo.case_det != ''}">&nbsp;${caseInfo.case_det}</c:if>
			</div>

			<div class="panel_title bold">三、资料摘要</div>
			<div class="content_p">
				<span>1.委托方提供的鉴定委托书原件1份。</span>
			</div>
			<div class="content_p">
				2.${caseInfo.si.sample_name}${caseInfo.isDoubleTube gt 0 ? '真空采血管' : '促凝管'}血液1支。
				<c:if
					test="${caseInfo.bloodnumA !=  null && caseInfo.bloodnumA != ''}">（采血管编号：${caseInfo.bloodnumA}</c:if>
				<c:if
					test="${caseInfo.bloodnumB ==  null &&  caseInfo.bloodnumA != ''}">）。</c:if>
				<c:if
					test="${caseInfo.bloodnumB !=  null && caseInfo.bloodnumB != ''}">；样品量：约${caseInfo.bloodnumB}mL）。</c:if>
			</div>

			<div class="panel_title bold">四、鉴定过程</div>
			<div class="content_p">
				1.<span>送检血液${caseInfo.sample_remark}</span>
			</div>
			<div class="content_p">
				2.送检血液运用《生物样品血液、尿液中乙醇、甲醇、正丙醇、乙醛、</span>
			</div>
			<br><br><br>	
		
			<div class="left pagetitle">
				子渊司鉴所[
				<c:out value="${fn:substring(caseInfo.accept_time, 0, 4)}" />
				]毒鉴字第${caseInfo.case_num}号
			</div>
			<div class="right pagetitle">共3页第1页</div>
			<hr>
			
			<div class="content_p">
				丙酮、 丙酮、异丙醇和正丁醇的顶空-气相色谱检验方法》（GA/T
				1073-2013）方法分析，<span>${caseInfo.sample_remark2}</span>
			</div>

			<div class="panel_title bold">五、鉴定意见</div>
			<div class="content_p">
				<span>所送检血液中${caseInfo.is_detection gt 0 ? '' : '未'}检出乙醇成分。</span>
			</div>

			<div class="end">
				<c:forEach items="${caseInfo.iy}" var="iyy" varStatus="status">
					<div class="end_per">鉴定人：${iyy.per_name}</div>
					<div class="end_per">&nbsp;《司法鉴定人执业证》证号：${iyy.per_code}</div>
				</c:forEach>
				<div class="time">${caseInfo.close_time}</div>
			</div>

			<div class="content_p">
				<div class="content_p">附件：${caseInfo.si.sample_name}${caseInfo.isDoubleTube gt 0 ? '真空采血管' : '促凝管'}血液1支照片2张（共1页）</div>
			</div>
			
			<div style="margin-top: 1000px;"></div>
			
			<div class="left pagetitle">
				子渊司鉴所[
				<c:out value="${fn:substring(caseInfo.accept_time, 0, 4)}" />
				]毒鉴字第${caseInfo.case_num}号
			</div>
			<div class="right pagetitle">共1页第1页</div>
			<hr>

			<c:forEach items="${caseInfo.am}" var="att" varStatus="status">
			<div align="center"> 
				<table border="1" style="border:0px;width:500px;text-align:center;">
					<c:if test="${status.index==0}">
					<tr style="height:600px;">
						<td><img src="${pageContext.request.contextPath}/alcohol/register/getImg.do?filename=${att.att_path}"
										height="600px" width="400px" /></td>
					</tr>
					<tr style="height:50px;">
						<td>${caseInfo.si.sample_name}${caseInfo.isDoubleTube gt 0 ? '真空采血管' : '促凝管'}血液1支&nbsp;正面 （图一）</td>
					</tr>
					</c:if>
					<c:if test="${status.index==1}">
					<tr style="height:600px;">
						<td><img src="${pageContext.request.contextPath}/alcohol/register/getImg.do?filename=${att.att_path}"
										height="600px" width="400px" /></td>
					</tr>
					<tr style="height:50px;">
						<td>${caseInfo.si.sample_name}${caseInfo.isDoubleTube gt 0 ? '真空采血管' : '促凝管'}血液1支&nbsp; 侧面 （图二）</td>
					</tr>
					</c:if>
				</table>
			</div>
			</c:forEach>
		</div>
	</c:forEach>
</body>


</html>
