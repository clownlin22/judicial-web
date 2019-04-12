<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>酒精</title>
<style type="text/css">
.page {
	page-break-after: always;
	margin-left: 40px;
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
	padding-left: 120px;
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

.panel {
	font-size: 32px;
	margin-top: 30px;
	margin-left: 50px;
	margin-bottom: 10px;
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
	margin-left: 400px;
	/*
	margin-top: 20px;
	*/
	font-size: 30px;
}

.end_time {
	text-align: right;
	font-size: 30px;
	/*
	margin-top: 20px;
	*/
}

.end {
	margin-top: 170px;
	font-size: 22px;
	line-height: 60px;
}

.time {
	text-align: right;
	font-size: 30px;
	margin-top: 50px;
}
body{
	    font-family: "FangSong";
}
</style>
</head>

<body style="width: 1060px;">
<c:forEach items="${caseInfos}" var="caseInfo">
	<div class="page">
		<div class="left pagetitle">
			子渊司鉴所[<c:out value="${fn:substring(caseInfo.accept_time, 0, 4)}" />]毒检字第${caseInfo.case_num}号
		</div>
		<div class="right pagetitle">共1页第1页</div>
		<hr>
		<div class="title bold">
			<B>宿迁子渊司法鉴定所司法鉴定意见书</B>
		</div>
		<div class="title_mark">
			子渊司鉴所[<c:out value="${fn:substring(caseInfo.accept_time, 0, 4)}" />]毒检字第${caseInfo.case_num}号
		</div>
		<div class="panel_title bold">一、基本情况</div>
		<div class="panel">
			<span class="bold">委 托 人：</span><span class="panel_p">${caseInfo.client}</span>
		</div>
		<div class="panel">
			<span class="bold">委托事项：</span><span class="panel_p">乙醇成分分析</span>
		</div>
		<div class="panel">
			<span class="bold">委托日期：</span><span class="panel_p">${caseInfo.client_time}</span>
		</div>
		<div class="panel">
			<span class="bold">受理日期：</span><span class="panel_p">${caseInfo.accept_time}</span>
		</div>
		<div class="panel">
			<span class="bold">被检验人：</span><span class="panel_p">${caseInfo.si.sample_name}<c:if test="${caseInfo.si.id_number!=''}"><span>（身份证号：${caseInfo.si.id_number}）</span></c:if></span>
		</div>
		<div class="panel">
			<span class="bold">送检材料：</span><span class="panel_p">血液（<c:if test="${caseInfo.bloodnumA !=  null && caseInfo.bloodnumA != ''}">采血管编号  A管：<span>${caseInfo.bloodnumA}</span><c:if test="${caseInfo.bloodnumB !=  null && caseInfo.bloodnumB != ''}">、B管：<span>${caseInfo.bloodnumB}</span></c:if></c:if><c:if test="${caseInfo.bloodnumA == '' || caseInfo.bloodnumA == null}">样品编号：<span>${caseInfo.case_code}</span><c:if test="${caseInfo.isDoubleTube=='1'}"><span>，A、B两管</span></c:if></c:if>）</span>
		</div>
		<div class="panel_title bold">二、检验过程</div>
		<div class="content_p">1.<span>送检血液<c:if test="${caseInfo.isDoubleTube=='1'}"><span>（A管）</span></c:if>${caseInfo.sample_remark}</span></div>
		<div class="content_p">2.送检血液<c:if test="${caseInfo.isDoubleTube=='1'}"><span>（A管）</span></c:if>运用公共安全行业标准GA/T
			1073-2013方法分析，<span>${caseInfo.sample_remark2}</span></div>
		<div class="panel_title bold">三、检验结果</div>
		<div class="content_p">所送检<span>${caseInfo.sample_result}</span></div>
		<div class="end">
			<div class="end_per">鉴定人：${model1.per_name}</div>
			<div class="end_time">《司法鉴定人执业证》证号：${model1.per_code}</div>
			<div class="end_per">鉴定人：${model2.per_name}</div>
			<div class="end_time">《司法鉴定人执业证》证号：${model2.per_code}</div>
			<div class="time">${caseInfo.close_time}</div>
		</div>
	</div> 
</c:forEach>
</body>
</html>
