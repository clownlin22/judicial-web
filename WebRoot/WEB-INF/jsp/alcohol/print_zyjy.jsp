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
	padding-top: 20px;
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
	padding-top: 20px;
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
	line-height: 60px;
	text-indent: 2em;
	margin-top: 10px;
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
	margin-top: 20px;
	font-size: 30px;
}

.end_time {
	text-align: right;
	font-size: 30px;
	margin-top: 20px;
}

.end {
	margin-top: 300px;
	font-size: 22px;
	line-height: 60px;
}

.time {
	text-align: right;
	font-size: 30px;
	margin-top: 50px;
}

.file {
	font-size: 34px;
}
</style>
</head>

<body style="width: 1060px;">
	<div class="page">
		<div class="left pagetitle">
			宿迁子渊司法鉴定所[
			<c:out value="${fn:substring(caseInfo.accept_time, 0, 4)}" />
			]法物鉴字第${caseInfo.case_code}号
		</div>
		<div class="right pagetitle">共4页第1页</div>
		<hr>
		<div class="title bold">
			<B>宿迁子渊司法鉴定所检验报告书</B>
		</div>
		<div class="title_mark">
			宿迁子渊司法鉴定所[
			<c:out value="${fn:substring(caseInfo.accept_time, 0, 4)}" />
			]法物鉴字第${caseInfo.case_code}号
		</div>
		<div class="panel_title bold">一、基本情况</div>
		<div class="panel">
			<span class="bold">委 托 单 位：</span><span>${caseInfo.client}</span>
		</div>
		<div class="panel">
			<span class="bold">受理日期：</span><span>${caseInfo.accept_time}</span>
		</div>
		<div class="panel">
			<span class="bold">委托检验事项：</span><span>血液中乙醇含量鉴定</span>
		</div>
		<div class="panel">
			<span class="bold">检验材料：</span>
		</div>
		<div class="panel">
			<span>标称“${sampleInfo.sample_name}”、编号为“${caseInfo.case_code}”的血液一支（约${sampleInfo.sample_ml}mL）。
			</span>
		</div>
		<div class="panel">
			<span class="bold">检验要求：</span>
		</div>
		<div class="panel">
			<span>送检标称“${sampleInfo.sample_name}”、编号为“${caseInfo.case_code}”的血液中乙醇含量。</span>
		</div>
		<div class="panel">
			<span class="bold">检验日期：</span><span>${printInfo.exper_time}</span>
		</div>
		<div class="panel">
			<span class="bold">检验地点：</span><span>宿迁子渊司法鉴定所</span>
		</div>
		<div class="panel_title bold">二、检案摘要</div>
		<div class="panel_p" style="line-height: 70px;">
			据委托人介绍：${caseInfo.event_desc}。${caseInfo.accept_time}，我中心接受${caseInfo.client}的委托，对${sampleInfo.sample_name}血液中的乙醇含量进行鉴定。
		</div>
	</div>
	<div class="page">
		<div class="left pagetitle">
			宿迁子渊司法鉴定所[
			<c:out value="${fn:substring(caseInfo.accept_time, 0, 4)}" />
			]法物鉴字第${caseInfo.case_code}号
		</div>
		<div class="right pagetitle">共4页第2页</div>
		<hr>
		<div class="panel_title bold">三、检验过程</div>
		<div class="content_p">1. 检验用仪器设备：PERSEE G5气相色谱仪，PERSEE
			HS7顶空自动进样器。</div>
		<div class="content_p">2. 色谱条件：色谱柱:
			KB-BAC2（30m*0.32mm*1.2μm）；柱温：50℃；检测器：火焰离子化检测器（FID）；检测器温度：250℃；烘箱温度：65℃；样品平衡时间：15min；样品环境度105℃；传输线温度：110℃。
		</div>
		<div class="content_p">
			3. 检验规程及检验过程
			<div class="content_div">
				<div class="content_p">3.1&nbsp;检验规程：参照SF/Z JD0107001-2010标准，采用顶空自动进样、叔丁醇内标法测定含量。</div>
				<p class="content_p">
					3.2&nbsp;检验过程：精密吸取待测血液0.5mL与浓度为40.0μg/mL的0.5mL叔丁醇内标液于20mL样品瓶内，密封，混合均为，置HS7顶空自动进样器测定。记录和打印检材血液的色谱图，计算血中乙醇含量。
     样品平行测定两次，同时设立阳性和阴性对照。</p>
			</div>
		</div>
		<div class="content_p">样品平行测定两次，同时设立阳性和阴性对照。</div>
		<div class="panel_title bold">四、分析说明</div>
		<div class="content_p">
			本检测采用校正曲线，由色谱工作站软件自动计算测定结果。校正曲线方程为：${printInfo.expression},
			r=${printInfo.reg_R2}；线性范围：10~200.0mg•100mL<sup>-1</sup>。
		</div>
		<div class="content_p">采用上述方法测定和计算，结果如下：</div>
		<div class="content_p">
			第一次测定值为：${printInfo.result_1}mg•100mL<sup>-1</sup>
		</div>
		<div class="content_p">
			第二次测定值为：${printInfo.result_2}mg•100mL<sup>-1</sup>
		</div>
		<div class="content_p">
			平均值为：${printInfo.avg_result}mg•100mL<sup>-1</sup>
		</div>
	</div>
	<div class="page">
		<div class="left pagetitle">
			宿迁子渊司法鉴定所[
			<c:out value="${fn:substring(caseInfo.accept_time, 0, 4)}" />
			]法物鉴字第${caseInfo.case_code}号
		</div>
		<div class="right pagetitle">共4页第3页</div>
		<hr>
		<div class="panel_title bold">五、检验意见</div>
		<div class="content_p">
			送检标称“${sampleInfo.sample_name}”、编号为“${caseInfo.case_code}”的血液中乙醇含量为：${printInfo.avg_result}mg•100mL<sup>-1</sup>。
		</div>
		<div class="end">
			<div class="end_per">鉴定人：${model.identify_per1}</div>
			<div class="end_time">《司法鉴定人执业证》证号：${model.identify_code1}</div>
			<div class="end_per">鉴定人：${model.identify_per2}</div>
			<div class="end_time">《司法鉴定人执业证》证号：${model.identify_code2}</div>
			<div class="time">${caseInfo.close_time}</div>
		</div>
		<div class="end">
			注：
			<div>1.本鉴定仅对送检检材负责；</div>
			<div>2.本实验室不负责送检检材的保存；</div>
			<div>3.本报告书未经书面批准，不得复制。</div>
		</div>
	</div>
	<div class="page">
		<div class="left pagetitle">
			宿迁子渊司法鉴定所[
			<c:out value="${fn:substring(caseInfo.accept_time, 0, 4)}" />
			]法物鉴字第${caseInfo.case_code}号
		</div>
		<div class="right pagetitle">共4页第4页</div>
		<hr>
		<div class="file" style="margin-top: 30px;">附件：</div>
		<div class="file">检材图片</div>
		<div>
			<img src="<%=basePath%>alcohol/register/getImg.do?filename=${caseInfo.attachment}" width="900" height="1000"/>
		</div>
	</div>
</body>
</html>
