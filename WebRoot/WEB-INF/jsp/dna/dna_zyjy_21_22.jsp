<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
  <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML>
<HEAD>
<META content="text/html; charset=UTF-8" http-equiv="Content-Type">
<style type="text/css">
  .page{
		page-break-after: always;
		margin-right:35px;
		margin-left:35px;
		word-spacing:14px;
		letter-spacing : normal;
		margin-top:10px;
		clear:both;
		text-align:justify; text-justify:inter-ideograph;
	}
	.page_title{
		 float:right;
		 font-size:20px;
		 font-weight: lighter;
	}
	.page_title_name{
		 float:left;
		 font-size:20px;
		 font-weight: lighter;
	}
	hr{
		clear:both;
		border:none;
		border-top:2px solid #555555;
		width: 100%;
		margin-bottom:40px;
	}
	.title{
		font-size: 38px;
		padding-left:120px;
		text-align: center;
		font-family:'黑体';
	}
	.title_mark{
		font-size:24px;
		float:right;
		margin-top: 30px;
		margin-bottom: 25px;
	}
	.panel_title{
		clear:both;
		font-size:35px;
		margin-top:10px;
		font-family:'黑体';
		text-indent: 2em;
	}
	.p_no{
	    margin-left:155px;
	    margin-top: 30px;
	}
	.spanel{
		font-size:31px;
		margin-top:30px;
		text-indent: 2em;
	}
	.p_title{
		font-family:'黑体';
	}
	.pageem{
	    text-indent: 2em;
    }
	.content{
	    font-size:31px;
		margin-top:15px;
		word-break:break-all;
	}
	.table{
		width:100%;
		font-size:24px;
		text-align:center;
		margin-top:30px;
		line-height:40px;
		border: 3px solid;
	}
	.table_3{
		width:100%;
		font-size:24px;
		text-align:center;
		margin-top:30px;
		line-height:40px;
		border:3px solid;
	}
	.table_3 td{	
	    border-bottom:none;
		border-top:none;
		border-right:none;
		width:21%;
		font-family: "Times New Roman";
	}
	.table_3 th{
	   width:21%;
	   border-right:none;	
	   height: 100px;
	   border-top:none;
	   font-weight: bolder;
	}
	.border_no_bottom{
	    border-bottom: none;
	}
	.border_no_top{
	    border-top: none;
	}
	.table td{	
	    border-bottom:none;
		border-top:none;
		border-right:none;
		width:33.3%;
		font-family: "Times New Roman";
	}
	.table th{	
		width:33.3%;
		height:70px;
		padding-top:10px;
		padding-bottom:10px;
		font-weight: bolder;
		border-top:none;
		border-right:none;
	}
	.ending_name{
	   clear:both;
	   padding-top:30px;
	   margin-left:400px;
	   font-size:31px;
	}
	.ending_num{
	   clear:both;
	   margin-top:30px;
	   float:right;
	   font-size:31px;
	}
	.ending_time{
	   clear:both;
	   float:right;
	   margin-top:30px;
	   font-size:31px;
	}
	SUP{
	  font-size: 14px;
	}
	.paragraph{
	   	line-height: 65px;
	}
	.page_center{
		text-align:center;
		font-size:29px;
		font-weight:bolder;
		margin-top: 15px;
		margin-bottom: 20px;
	}
    td{
	  height:50px;
	}
	.ending_mark{
		font-size:26px;
		font-weight:lighter;
		float:left;
		margin-top:300px;
	}
	.ending_mark div{
	    margin-top:20px;
	}
	.no_margin{
	  margin-top: 0px;
	}
	body{
	    font-family: "FangSong";
	    width:1090px;
	}
	.first-content{
	    margin-top: 20px;
	}
	.intent{
	  margin-bottom: 30px;
	}
</style>
</head>

<body>
    <c:forEach items="${caseModel.results}" var="type" >
             <div class="page">
		      <div class="page_title_name">
			  <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
		      <div class="page_title">共&ensp;<c:if test="${type.result.compareResultModel.ext_flag=='N'}">3</c:if><c:if test="${type.result.compareResultModel.ext_flag=='Y'}">4</c:if>&ensp;页第&ensp;1&ensp;页</div>
		       <hr/>
		       <div class="title">
		             宿迁子渊司法鉴定所检验报告书
		      </div>
		       <div class="title_mark">
		            <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>    
		      </div>
		          <div class="panel_title">一、基本情况</div>
		          <div class="spanel"><span class="p_title">委&ensp;托&ensp;人：</span>${caseModel.client}</div>
		          <div class="spanel"><span class="p_title">委托检验事项：</span><c:if test="${type.sampleModel.mother_info.sample_code!=''&&type.sampleModel.mother_info.sample_code!=null}">${type.sampleModel.father_info.sample_username}和${type.sampleModel.mother_info.sample_username}是否是${type.sampleModel.child_info.sample_username}的生物学父母</c:if><c:if test="${type.sampleModel.mother_info.sample_code==''||type.sampleModel.mother_info.sample_code==null}">${type.sampleModel.father_info.sample_username}是否是${type.sampleModel.child_info.sample_username}的生物学${type.sampleModel.father_info.sample_callname}</c:if></div>
		          <div class="spanel"><span class="p_title">受理日期：</span>
				  <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				      ${caseModel.accept_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					${caseModel.addextnew_time}  
			  </c:if>
			  
			  </div>
		          <div class="spanel"><span class="p_title">检验材料：</span>1：${type.sampleModel.father_info.sample_username}的${type.sampleModel.father_info.sample_typename}（编号：${type.sampleModel.father_info.sample_code}）
		           <c:if test="${type.sampleModel.mother_info.sample_code!=''&&type.sampleModel.mother_info.sample_code!=null}">
					    <div class="p_no">2：${type.sampleModel.mother_info.sample_username}的${type.sampleModel.mother_info.sample_typename}（编号：${type.sampleModel.mother_info.sample_code}）</div>
				   </c:if>
		           <div class="p_no"><c:if test="${type.sampleModel.mother_info.sample_code!=''&&type.sampleModel.mother_info.sample_code!=null}">3：</c:if><c:if test="${type.sampleModel.mother_info.sample_code==''||type.sampleModel.mother_info.sample_code==null}">2：</c:if>${type.sampleModel.child_info.sample_username}的${type.sampleModel.child_info.sample_typename}（编号：${type.sampleModel.child_info.sample_code}）</div>
		          </div>
		          <div class="spanel"><span class="p_title">被检验人：</span>${type.sampleModel.father_info.sample_callname}—${type.sampleModel.father_info.sample_username}<c:if test="${type.sampleModel.father_info.sample_callname=='父亲'}">（男</c:if><c:if test="${type.sampleModel.father_info.sample_callname=='母亲'}">（女</c:if><c:if test="${type.sampleModel.father_info.id_number!=''&&type.sampleModel.father_info.id_number!=null}">,身份证号: ${type.sampleModel.father_info.id_number }</c:if><c:if test="${fn:trim(type.sampleModel.father_info.id_number)==''&&type.sampleModel.father_info.birth_date_format!=null&&fn:trim(type.sampleModel.father_info.birth_date_format)!=''}">,出生日期: ${type.sampleModel.father_info.birth_date_format}</c:if>）
				    <c:if test="${type.sampleModel.mother_info.sample_code!=''&&type.sampleModel.mother_info.sample_code!=null}"><div class="p_no">${type.sampleModel.mother_info.sample_callname}—${type.sampleModel.mother_info.sample_username}<c:if test="${type.sampleModel.mother_info.sample_callname=='父亲'}">（男</c:if><c:if test="${type.sampleModel.mother_info.sample_callname=='母亲'}">（女</c:if><c:if test="${type.sampleModel.mother_info.id_number!=''&&type.sampleModel.mother_info.id_number!=null}">,身份证号: ${type.sampleModel.mother_info.id_number}</c:if><c:if test="${fn:trim(type.sampleModel.mother_info.id_number)==''&&type.sampleModel.mother_info.birth_date_format!=null&&fn:trim(type.sampleModel.mother_info.birth_date_format)!=''}">,出生日期: ${type.sampleModel.mother_info.birth_date_format}</c:if>）
					  </div>												  																  
            	     </c:if>		
		          	<div class="p_no">孩子—${type.sampleModel.child_info.sample_username}<c:if test="${type.sampleModel.child_info.sample_callname=='儿子'}">（男</c:if><c:if test="${type.sampleModel.child_info.sample_callname=='女儿'}">（女</c:if><c:if test="${type.sampleModel.child_info.id_number!=''&&type.sampleModel.child_info.id_number!=null}">,身份证号: ${type.sampleModel.child_info.id_number}</c:if><c:if test="${fn:trim(type.sampleModel.child_info.id_number)==''&&type.sampleModel.child_info.birth_date_format!=null&&fn:trim(type.sampleModel.child_info.birth_date_format)!=''}">,出生日期: ${type.sampleModel.child_info.birth_date_format}</c:if>）
					</div>												    																	  																  
		          </div>
		          <div class="spanel"><span class="p_title">检验日期：</span>
				  
			  <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				        ${caseModel.accept_time}-${caseModel.close_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					   ${caseModel.addextnew_time}-${caseModel.close_time} 
			  </c:if>
			</div>
		          <div class="spanel  intent"><span class="p_title">检验地点：</span>宿迁子渊司法鉴定所</div>
		          <div class="panel_title">二、检案摘要</div>
		          <div class="pageem content paragraph">
                  ${type.sampleModel.father_info.sample_username}<c:if test="${type.sampleModel.mother_info.sample_code!=''&&type.sampleModel.mother_info.sample_code!=null}">和${type.sampleModel.mother_info.sample_username}</c:if>为${type.sampleModel.child_info.sample_username}${caseModel.purpose}需要明确${type.sampleModel.father_info.sample_username}<c:if test="${type.sampleModel.mother_info.sample_code!=''&&type.sampleModel.mother_info.sample_code!=null}">和${type.sampleModel.mother_info.sample_username}</c:if>是否是${type.sampleModel.child_info.sample_username}的生物学<c:if test="${type.sampleModel.mother_info.sample_code!=''&&type.sampleModel.mother_info.sample_code!=null}">父母</c:if><c:if test="${type.sampleModel.mother_info.sample_code==''||type.sampleModel.mother_info.sample_code==null}">${type.sampleModel.father_info.sample_callname}</c:if>,故委托本所进行检验。 </div>
		          <div class="panel_title">三、检验过程</div>
		          <div class="pageem content paragraph">1.参照《法庭科学&ensp;DNA&ensp;实验室检验规范》（GA/T&ensp;383-2014）中 Chelex-100法提取上述检材的DNA。 </div>
		          <div class="pageem content paragraph">2.取上述检材的DNA提取产物适量，用<c:if test="${reagent_name=='MR20'|| reagent_name=='S20A(MR20)'}">GOLDENEYE<sup>TM</sup></c:if><c:if test="${reagent_name=='MR21' }">MICROREADER<sup>TM</sup></c:if><c:if test="${reagent_name=='SUBO21' }">21plex</c:if>荧光标记STR复合扩增试剂盒<c:if test="${reagent_name=='MR20'}">（阅微基因技术有限公司）</c:if>进行10μL体系PCR扩增（操作步骤按试剂盒说明），并设立阴性及阳性对照。</div>
		          <c:if test="${type.sampleModel.mother_info.sample_code==''||type.sampleModel.mother_info.sample_code==null}">
		          <div class="pageem content paragraph">3.扩增产物用ABI3130遗传分析仪进行毛细管电泳分离和基因分型。</div>
		          </c:if>
		    </div>
		    <c:if test="${type.result_type=='passed_3'}">
			    <c:if test="${type.result.compareResultModel.ext_flag=='N'}">
			         <div class="page">
				          <div class="page_title_name">
						  <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
				          <div class="page_title">共&ensp;3&ensp;页第&ensp;2&ensp;页</div>
				         <hr/>
				          <div class="pageem content paragraph">3.扩增产物用ABI3130遗传分析仪进行毛细管电泳分离和基因分型。</div>
				          <div class="panel_title">四、检验结果</div>
				         <div class="pageem content first-content">
				           STR基因分型结果：
				          </div>
				          <div class="page_center">20-STR基因座分型结果表</div>
				          <table class="table_3" cellSpacing="0" border="solid" cellPadding="0">
				             <tr><th style="width:37%">案号：${caseModel.case_code}<div class="no_margin">委托人：${caseModel.client}</div>
							 <div class="no_margin">
							 <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       受理日期：${caseModel.accept_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 受理日期：${caseModel.addextnew_time} 
			  </c:if>
			  
			  
							 </div></th><th><div>${type.sampleModel.father_info.sample_callname}:${type.sampleModel.father_info.sample_username}</div><div class="no_margin">${type.sampleModel.father_info.sample_code}</div></th><th><div>孩子：${type.sampleModel.child_info.sample_username}</div><div class="no_margin">${type.sampleModel.child_info.sample_code}</div></th><th><div>${type.sampleModel.mother_info.sample_callname}:${type.sampleModel.mother_info.sample_username}</div><div class="no_margin">${type.sampleModel.mother_info.sample_code}</div></th></tr>
				             <tr><td style="width:37%;border-bottom: solid 1px;font-weight: bold;">基因位点</td><td colspan="3" style="border-bottom: solid 1px;font-weight: bold;">等位基因</td></tr>
				             <c:forEach items="${type.sampleModel.sampleResults}" var="list" begin="0" end="18" step="1"><tr>
				                  <c:forEach items="${list}" var="map">
				                       <c:choose>  
										   <c:when test="${map.key=='name'}">
										       <td style="width:37%">${map.value}</td>
										   </c:when>  
										   <c:otherwise>  
										    <td>
                                               <c:if test="${fn:substringAfter(map.value,',')==''}">
											        <table style='width:100% ;border:none;'>
												        <tr>
													        <td style='text-align: right;'>
								                                 ${fn:substringBefore(map.value,",")}
															</td>
															<td style='text-align: center;'>,
															</td>
															<td style='text-align:left;'>
															     ${fn:substringBefore(map.value, ',')}
															</td>
														</tr>
													</table>
											   </c:if>  
											    <c:if test="${fn:substringAfter(map.value,',')!=''}">
												        <table style='width:100% ;border:none;'>
													        <tr>
														        <td style='text-align: right;'>
									                                ${fn:substringBefore(map.value,",")}
																</td>
																<td style='text-align: center;'>,
																</td>
																<td style='text-align:left;'>
																    ${fn:substringAfter(map.value,",")}
																</td>
															</tr>
														</table>
											     </c:if> 
											    </td>       
										   </c:otherwise>  
										</c:choose>  
				                  </c:forEach>
				                  </tr></c:forEach>
				          </table>
				    </div>
				     <div class="page">
					      <div class="page_title_name">
<c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
				          <div class="page_title">共&ensp;3&ensp;页第&ensp;3&ensp;页</div>
				         <hr/>
					        <div class="pageem content paragraph">
					            D19S433等20个STR基因座均为人类的遗传学标记，遵循孟德尔遗传定律，联合应用可进行亲权鉴定。上述20个STR基因座检测结果显示：${type.sampleModel.child_info.sample_username}的等位基因可从${type.sampleModel.father_info.sample_username}和${type.sampleModel.mother_info.sample_username}的基因型中找到来源。经计算,累积亲权指数大于10000。
					          </div>
					        <div class="pageem content paragraph">
					                     依据现有资料和DNA分析结果，在排除同卵多胞胎、近亲属和外源干扰的前提下，支持${type.sampleModel.father_info.sample_username}和${type.sampleModel.mother_info.sample_username}是${type.sampleModel.child_info.sample_username}的生物学父母。 
					          </div>  
					          <c:forEach items="${pers}" var="per" varStatus="status" >
					             <c:if test="${status.index==0}">
					             <div class="ending_name" style="margin-top:150px;">
					                     鉴定人：${per.identify_per}
					          </div>
					          <div class="ending_num">
					                    《司法鉴定人执业证》证号：${per.identify_code}
					          </div></c:if>
					           <c:if test="${status.index>0}">
					              <div class="ending_name">
					                     鉴定人：${per.identify_per}
					          </div>
					          <div class="ending_num">
					        	《司法鉴定人执业证》证号：${per.identify_code}
					          </div>
					           </c:if>
					          </c:forEach>
					          <div class="ending_time">
					               ${caseModel.close_time_china}
					          </div>
					          <div class="ending_mark">注：
					              <div>1.本检验仅对送检检材负责；</div>
					              <div>2.本实验室不负责送检检材的保存；</div>
					              <div>3.本报告书未经书面批准，不得复制。</div>
					          </div>
					    </div>
			    </c:if>
		     <c:if test="${type.result.compareResultModel.ext_flag=='Y'}">
		        <div class="page">
				          <div class="page_title_name">
						  <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
				          <div class="page_title">共&ensp;4&ensp;页第&ensp;2&ensp;页</div>
				         <hr/>
				          <div class="pageem content paragraph">3.扩增产物用ABI3130遗传分析仪进行毛细管电泳分离和基因分型。</div>
				          <div class="panel_title">四、检验结果</div>
				          <div class="pageem content first-content">
				           STR基因分型结果：
				          </div>
				          <div class="page_center">39-STR基因座分型结果表</div>
				          <table class="table_3 border_no_bottom" cellSpacing="0" border="solid" cellPadding="0">
				           <tr><th style="width:37%">案号：${caseModel.case_code}<div class="no_margin">委托人：${caseModel.client}</div>
						   <div class="no_margin">
						   <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       受理日期：${caseModel.accept_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 受理日期：${caseModel.addextnew_time}
			  </c:if>
			  
						   </div>
						   </th><th><div>${type.sampleModel.father_info.sample_callname}:${type.sampleModel.father_info.sample_username}</div><div class="no_margin">${type.sampleModel.father_info.sample_code}</div></th><th><div>孩子：${type.sampleModel.child_info.sample_username}</div><div class="no_margin">${type.sampleModel.child_info.sample_code}</div></th><th><div>${type.sampleModel.mother_info.sample_callname}:${type.sampleModel.mother_info.sample_username}</div><div class="no_margin">${type.sampleModel.mother_info.sample_code}</div></th></tr>
				             <tr><td style="width:37%;border-bottom: solid 1px;font-weight: bold;">基因位点</td><td colspan="3" style="border-bottom: solid 1px;font-weight: bold;">等位基因</td></tr>
				             <c:forEach items="${type.sampleModel.sampleResults}" var="list" varStatus="status" begin="0" end="19" step="1"><tr>
				                  <c:forEach items="${list}" var="map">
				                       <c:choose>  
										   <c:when test="${map.key=='name'}">
										       <td style="width:37%">${map.value}</td>
										   </c:when>  
										   <c:otherwise>  
										      <td>
                                               <c:if test="${fn:substringAfter(map.value,',')==''}">
											        <table style='width:100% ;border:none;'>
												        <tr>
													        <td style='text-align: right;'>
								                                 ${fn:substringBefore(map.value,",")}
															</td>
															<td style='text-align: center;'>,
															</td>
															<td style='text-align:left;'>
															     ${fn:substringBefore(map.value, ',')}
															</td>
														</tr>
													</table>
											   </c:if>  
											    <c:if test="${fn:substringAfter(map.value,',')!=''}">
												        <table style='width:100% ;border:none;'>
													        <tr>
														        <td style='text-align: right;'>
									                                ${fn:substringBefore(map.value,",")}
																</td>
																<td style='text-align: center;'>,
																</td>
																<td style='text-align:left;'>
																    ${fn:substringAfter(map.value,",")}
																</td>
															</tr>
														</table>
											   </c:if>  
											    </td>      
										   </c:otherwise>  
										</c:choose>  
				                  </c:forEach></tr></c:forEach>
				          </table>
				    </div>
				   <div class="page">
				          <div class="page_title_name">
<c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
				          <div class="page_title">共&ensp;4&ensp;页第&ensp;3&ensp;页</div>
				         <hr/>
						  <table class="table_3 border_no_top" cellSpacing="0" border="solid" cellPadding="0" style="border-top: none;">
						  <c:forEach items="${type.sampleModel.sampleResults}" var="list" varStatus="status" begin="20" step="1"><tr>
				                  <c:forEach items="${list}" var="map">
				                       <c:choose>  
										   <c:when test="${map.key=='name'}">
										       <td style="width:37%">${map.value}</td>
										   </c:when>  
										   <c:otherwise> 
										      <td>
											    <c:if test="${fn:substringAfter(map.value,',')==''}">
											        <table style='width:100% ;border:none;'>
												        <tr>
													        <td style='text-align: right;'>
								                                 ${fn:substringBefore(map.value,",")}
															</td>
															<td style='text-align: center;'>,
															</td>
															<td style='text-align:left;'>
															     ${fn:substringBefore(map.value, ',')}
															</td>
														</tr>
													</table>
											   </c:if>  
											    <c:if test="${fn:substringAfter(map.value,',')!=''}">
												        <table style='width:100% ;border:none;'>
													        <tr>
														        <td style='text-align: right;'>
									                                ${fn:substringBefore(map.value,",")}
																</td>
																<td style='text-align: center;'>,
																</td>
																<td style='text-align:left;'>
																    ${fn:substringAfter(map.value,",")}
																</td>
															</tr>
														</table>
											   </c:if>
											   </td>
										   </c:otherwise>  
										</c:choose>  
				                  </c:forEach></tr></c:forEach>
		                 </table>
		                 <div class="pageem content paragraph" style="margin-top: 40px;">
		                     D19S433等39个STR基因座均为人类的遗传学标记，遵循孟德尔遗传定律，联合应用可进行亲权鉴定。上述39个STR基因座检测结果显示：除${type.result.compareResultModel.unmatched_node}基因座以外，${type.sampleModel.child_info.sample_username}的等位基因可从${type.sampleModel.father_info.sample_username}和${type.sampleModel.mother_info.sample_username}的基因型中找到来源。经计算，累积亲权指数大于10000。
		                 </div>
				   </div>
				   <div class="page">
				          <div class="page_title_name">
						  <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
				          <div class="page_title">共&ensp;4&ensp;页第&ensp;4&ensp;页</div>
				         <hr/>
			              <div class="pageem content paragraph">
				                     依据现有资料和DNA分析结果，在排除同卵多胞胎、近亲属和外源干扰的前提下，在考虑${type.result.compareResultModel.unmatched_node}基因座发生突变的情况下，支持${type.sampleModel.father_info.sample_username}和${type.sampleModel.mother_info.sample_username}是${type.sampleModel.child_info.sample_username}的生物学父母。
				          </div>  
				         <c:forEach items="${pers}" var="per" varStatus="status" >
					             <c:if test="${status.index==0}">
					             <div class="ending_name" style="margin-top:150px;">
					                     鉴定人：${per.identify_per}
					          </div>
					          <div class="ending_num">
					                    《司法鉴定人执业证》证号：${per.identify_code}
					          </div></c:if>
					           <c:if test="${status.index>0}">
					              <div class="ending_name">
					                     鉴定人：${per.identify_per}
					          </div>
					          <div class="ending_num">
					        	《司法鉴定人执业证》证号：${per.identify_code}
					          </div>
					           </c:if>
					          </c:forEach>
				          <div class="ending_time">
				               ${caseModel.close_time_china}
				          </div>
				          <div class="ending_mark">注：
				              <div>1.本检验仅对送检检材负责；</div>
				              <div>2.本实验室不负责送检检材的保存；</div>
				              <div>3.本报告书未经书面批准，不得复制。</div>
				          </div>
				   </div>
		        </c:if>
		     </c:if>
		     <c:if test="${type.result_type=='passed_2'}">
		          <c:if test="${type.result.compareResultModel.ext_flag=='N'}">
		             <div class="page">
				          <div class="page_title_name">
<c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
				          <div class="page_title">共&ensp;3&ensp;页第&ensp;2&ensp;页</div>
				         <hr/>
				          <div class="panel_title">四、检验结果</div>
				         <div class="pageem content first-content">
				           STR基因分型结果：
				          </div>
				          <div class="page_center">20-STR基因座分型结果表</div>
				          <table class="table" cellSpacing="0" border="solid" cellPadding="0">
				             <tr><th>基因座</th><th><div>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}</div><div class="no_margin">${type.sampleModel.father_info.sample_code}</div></th><th><div>孩子：${type.sampleModel.child_info.sample_username}</div><div class="no_margin">${type.sampleModel.child_info.sample_code}</div></th></tr>
				             <c:forEach items="${type.sampleModel.sampleResults}" var="list" varStatus="status" begin="0" end="18" step="1"><tr>
				                  <c:forEach items="${list}" var="map">
										<c:choose>  
										   <c:when test="${map.key=='name'}">
										       <td>${map.value}</td>
										   </c:when>  
										   <c:otherwise> 
										     <td>
											    <c:if test="${fn:substringAfter(map.value,',')==''}">
											        <table style='width:100% ;border:none;'>
												        <tr>
													        <td style='text-align: right;'>
								                                 ${fn:substringBefore(map.value,",")}
															</td>
															<td style='text-align: center;'>,
															</td>
															<td style='text-align:left;'>
															     ${fn:substringBefore(map.value, ',')}
															</td>
														</tr>
													</table>
											   </c:if>  
											    <c:if test="${fn:substringAfter(map.value,',')!=''}">
												        <table style='width:100% ;border:none;'>
													        <tr>
														        <td style='text-align: right;'>
									                                ${fn:substringBefore(map.value,",")}
																</td>
																<td style='text-align: center;'>,
																</td>
																<td style='text-align:left;'>
																    ${fn:substringAfter(map.value,",")}
																</td>
															</tr>
														</table>
											   </c:if>
											  </td>   
										   </c:otherwise>  
										</c:choose>  
				                  </c:forEach></tr></c:forEach>
				          </table>
				    </div>
				     <div class="page">
					      <div class="page_title_name">
						  <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
				          <div class="page_title">共&ensp;3&ensp;页第&ensp;3&ensp;页</div>
				         <hr/>
					          <div class="pageem content paragraph">
					            D19S433等20个STR基因座均为人类的遗传学标记，遵循孟德尔遗传定律，联合应用可进行亲权鉴定。上述20个STR基因座检测结果显示:${type.sampleModel.child_info.sample_username}的等位基因可从${type.sampleModel.father_info.sample_username}的基因型中找到来源。经计算，累积亲权指数为${type.result.pi},即${type.sampleModel.father_info.sample_username}为${type.sampleModel.child_info.sample_username}生物学${type.sampleModel.father_info.sample_callname}的可能性是无关<c:if test="${type.sampleModel.father_info.sample_callname=='父亲'}">男</c:if><c:if test="${type.sampleModel.father_info.sample_callname=='母亲'}">女</c:if>性个体为${type.sampleModel.child_info.sample_username}生<c:if test="${type.sampleModel.father_info.sample_callname=='父亲'}">父</c:if><c:if test="${type.sampleModel.father_info.sample_callname=='母亲'}">母</c:if>可能性的${type.result.pi }倍。
					          </div>
					          <div class="pageem content paragraph">
					                     依据现有资料和DNA分析结果,在排除同卵多胞胎、近亲属和外源干扰的前提下,支持${type.sampleModel.father_info.sample_username}是${type.sampleModel.child_info.sample_username}的生物学${type.sampleModel.father_info.sample_callname}。
					          </div>  
					          <c:forEach items="${pers}" var="per" varStatus="status" >
					             <c:if test="${status.index==0}">
					             <div class="ending_name" style="margin-top:150px;">
					                     鉴定人：${per.identify_per}
					          </div>
					          <div class="ending_num">
					                    《司法鉴定人执业证》证号：${per.identify_code}
					          </div></c:if>
					           <c:if test="${status.index>0}">
					              <div class="ending_name">
					                     鉴定人：${per.identify_per}
					          </div>
					          <div class="ending_num">
					        	《司法鉴定人执业证》证号：${per.identify_code}
					          </div>
					           </c:if>
					          </c:forEach>
					          <div class="ending_time">
					               ${caseModel.close_time_china}
					          </div>
					          <div class="ending_mark">注：
					              <div>1.本检验仅对送检检材负责；</div>
					              <div>2.本实验室不负责送检检材的保存；</div>
					              <div>3.本报告书未经书面批准，不得复制。</div>
					          </div>
					    </div>
		          </c:if>
		           <c:if test="${type.result.compareResultModel.ext_flag=='Y'}">
		                 <div class="page">
				          <div class="page_title_name">
<c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
				          <div class="page_title">共&ensp;4&ensp;页第&ensp;2&ensp;页</div>
				         <hr/>
				          <div class="panel_title">四、检验结果</div>
				         <div class="pageem content first-content">
				           STR基因分型结果：
				          </div>
				          <div class="page_center">39-STR基因座分型结果表</div>
				          <table class="table border_no_bottom" cellSpacing="0" border="solid" cellPadding="0">
				          <tr><th>基因座</th><th><div>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}</div><div class="no_margin">${type.sampleModel.father_info.sample_code}</div></th><th><div>孩子：${type.sampleModel.child_info.sample_username}</div><div class="no_margin">${type.sampleModel.child_info.sample_code}</div></th></tr>
				              <c:forEach items="${type.sampleModel.sampleResults}" var="list" varStatus="status" begin="0" end="19" step="1"><tr>
				                  <c:forEach items="${list}" var="map">
										     <c:choose>  
										   <c:when test="${map.key=='name'}">
										       <td>${map.value}</td>
										   </c:when>  
										   <c:otherwise> 
										    <td>
											    <c:if test="${fn:substringAfter(map.value,',')==''}">
											        <table style='width:100% ;border:none;'>
												        <tr>
													        <td style='text-align: right;'>
								                                 ${fn:substringBefore(map.value,",")}
															</td>
															<td style='text-align: center;'>,
															</td>
															<td style='text-align:left;'>
															     ${fn:substringBefore(map.value, ',')}
															</td>
														</tr>
													</table>
											   </c:if>  
											    <c:if test="${fn:substringAfter(map.value,',')!=''}">
												        <table style='width:100% ;border:none;'>
													        <tr>
														        <td style='text-align: right;'>
									                                ${fn:substringBefore(map.value,",")}
																</td>
																<td style='text-align: center;'>,
																</td>
																<td style='text-align:left;'>
																    ${fn:substringAfter(map.value,",")}
																</td>
															</tr>
														</table>
											   </c:if>
											    </td>     
										   </c:otherwise>  
										</c:choose>  
				                  </c:forEach></tr></c:forEach>
				          </table>
				        </div>
				        <div class="page">
				          <div class="page_title_name">
<c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
				          <div class="page_title">共&ensp;4&ensp;页第&ensp;3&ensp;页</div>
				         <hr/>
				          <table class="table border_no_top" cellSpacing="0" border="solid" cellPadding="0">
				          <c:forEach items="${type.sampleModel.sampleResults}" var="list" varStatus="status" begin="20" step="1"><tr>
				                  <c:forEach items="${list}" var="map">
										    <c:choose>  
										   <c:when test="${map.key=='name'}">
										       <td>${map.value}</td>
										   </c:when>  
										   <c:otherwise> 
										      <td>
											    <c:if test="${fn:substringAfter(map.value,',')==''}">
											        <table style='width:100% ;border:none;'>
												        <tr>
													        <td style='text-align: right;'>
								                                 ${fn:substringBefore(map.value,",")}
															</td>
															<td style='text-align: center;'>,
															</td>
															<td style='text-align:left;'>
															     ${fn:substringBefore(map.value, ',')}
															</td>
														</tr>
													</table>
											   </c:if>  
											    <c:if test="${fn:substringAfter(map.value,',')!=''}">
												        <table style='width:100% ;border:none;'>
													        <tr>
														        <td style='text-align: right;'>
									                                ${fn:substringBefore(map.value,",")}
																</td>
																<td style='text-align: center;'>,
																</td>
																<td style='text-align:left;'>
																    ${fn:substringAfter(map.value,",")}
																</td>
															</tr>
														</table>
											   </c:if>
											   </td>     
										   </c:otherwise>  
										</c:choose>  
				                  </c:forEach></tr></c:forEach>
				          </table>
				            <c:if test="${type.result.compareResultModel.unmatched_count>0}">
				                       <div class="pageem content paragraph">
							             D19S433等39个STR基因座均为人类的遗传学标记，遵循孟德尔遗传定律，联合应用可进行亲权鉴定。上述39个STR基因座检测结果显示：除${type.result.compareResultModel.unmatched_node}基因座以外，${type.sampleModel.child_info.sample_username}的等位基因可从${type.sampleModel.father_info.sample_username}的基因型中找到来源。
							          </div>
				              </c:if>
				              <c:if test="${type.result.compareResultModel.unmatched_count==0}">
				                       <div class="pageem content paragraph">
							             D19S433等39个STR基因座均为人类的遗传学标记，遵循孟德尔遗传定律，联合应用可进行亲权鉴定。上述39个STR基因座检测结果显示：${type.sampleModel.child_info.sample_username}的等位基因可从${type.sampleModel.father_info.sample_username}的基因型中找到来源。
							          </div>
				              </c:if>
				        </div>
				        <div class="page">
					      <div class="page_title_name">
<c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
				          <div class="page_title">共&ensp;4&ensp;页第&ensp;4&ensp;页</div>
				         <hr/>
				              <c:if test="${type.result.compareResultModel.unmatched_count>0}">
							          <div class="pageem content paragraph">
							                     依据现有资料和DNA分析结果，在排除同卵多胞胎、近亲属和外源干扰的前提下，在考虑${type.result.compareResultModel.unmatched_node}基因座发生突变的情况下，支持${type.sampleModel.father_info.sample_username}是${type.sampleModel.child_info.sample_username}的生物学${type.sampleModel.father_info.sample_callname}。
							          </div>  
				              </c:if>
				              <c:if test="${type.result.compareResultModel.unmatched_count==0}">
							          <div class="pageem content paragraph">
							                      依据现有资料和DNA分析结果，在排除同卵多胞胎、近亲属和外源干扰的前提下，支持${type.sampleModel.father_info.sample_username}是${type.sampleModel.child_info.sample_username}的生物学${type.sampleModel.father_info.sample_callname}。
							          </div>  
				              </c:if>
					          <c:forEach items="${pers}" var="per" varStatus="status" >
					             <c:if test="${status.index==0}">
					             <div class="ending_name" style="margin-top:150px;">
					                     鉴定人：${per.identify_per}
					          </div>
					          <div class="ending_num">
					                    《司法鉴定人执业证》证号：${per.identify_code}
					          </div></c:if>
					           <c:if test="${status.index>0}">
					              <div class="ending_name">
					                     鉴定人：${per.identify_per}
					          </div>
					          <div class="ending_num">
					        	《司法鉴定人执业证》证号：${per.identify_code}
					          </div>
					           </c:if>
					          </c:forEach>
					          <div class="ending_time">
					               ${caseModel.close_time_china}
					          </div>
					          <div class="ending_mark">注：
					              <div>1.本检验仅对送检检材负责；</div>
					              <div>2.本实验室不负责送检检材的保存；</div>
					              <div>3.本报告书未经书面批准，不得复制。</div>
					          </div>
					         </div>
		                 </c:if>
		              </c:if>
		              <c:if test="${type.result_type=='failed_2'}">
		              <c:if test="${type.result.compareResultModel.ext_flag=='N'}"> 
		                 <div class="page">
						          <div class="page_title_name">
								  <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />] 法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
						          <div class="page_title">共&ensp;3&ensp;页第&ensp;2&ensp;页</div>
						         <hr/>
						          <div class="panel_title">四、检验结果</div>
						         <div class="pageem content first-content">
						           STR基因分型结果：
						          </div>
						          <div class="page_center">20-STR基因座分型结果表</div>
						          <table class="table" cellSpacing="0" border="solid" cellPadding="0">
						          <tr><th>基因座</th><th><div>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}</div><div class="no_margin">${type.sampleModel.father_info.sample_code}</div></th><th><div>孩子：${type.sampleModel.child_info.sample_username}</div><div class="no_margin">${type.sampleModel.child_info.sample_code}</div></th></tr>
						            <c:forEach items="${type.sampleModel.sampleResults}" var="list" varStatus="status" begin="0" end="18" step="1"><tr>
							                  <c:forEach items="${list}" var="map">
													    <c:choose>  
										   <c:when test="${map.key=='name'}">
										       <td>${map.value}</td>
										   </c:when>  
										   <c:otherwise> 
										   	 <td>
											    <c:if test="${fn:substringAfter(map.value,',')==''}">
											        <table style='width:100% ;border:none;'>
												        <tr>
													        <td style='text-align: right;'>
								                                 ${fn:substringBefore(map.value,",")}
															</td>
															<td style='text-align: center;'>,
															</td>
															<td style='text-align:left;'>
															     ${fn:substringBefore(map.value, ',')}
															</td>
														</tr>
													</table>
											   </c:if>  
											    <c:if test="${fn:substringAfter(map.value,',')!=''}">
												        <table style='width:100% ;border:none;'>
													        <tr>
														        <td style='text-align: right;'>
									                                ${fn:substringBefore(map.value,",")}
																</td>
																<td style='text-align: center;'>,
																</td>
																<td style='text-align:left;'>
																    ${fn:substringAfter(map.value,",")}
																</td>
															</tr>
														</table>
											   </c:if>
											    </td>  
										   </c:otherwise>  
										</c:choose></c:forEach></tr></c:forEach>
						          </table>
				         </div>
				         <div class="page">
						      <div class="page_title_name">
							  <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
					          <div class="page_title">共&ensp;3&ensp;页第&ensp;3&ensp;页</div>
					         <hr/>
						          <div class="pageem content paragraph">
						            D19S433等20个STR基因座均为人类的遗传学标记，遵循孟德尔遗传定律，联合应用可进行亲权鉴定。上述20个STR基因座检测结果显示：${type.sampleModel.child_info.sample_username}在${type.result.compareResultModel.unmatched_node}基因座上的等位基因不能从${type.sampleModel.father_info.sample_username}的基因型中找到来源。
						          </div>
						          <div class="pageem content paragraph">
						                     依据现有资料和DNA分析结果，排除${type.sampleModel.father_info.sample_username}是${type.sampleModel.child_info.sample_username}的生物学${type.sampleModel.father_info.sample_callname}。
						          </div>  
						        <c:forEach items="${pers}" var="per" varStatus="status" >
					             <c:if test="${status.index==0}">
					             <div class="ending_name" style="margin-top:150px;">
					                     鉴定人：${per.identify_per}
					          </div>
					          <div class="ending_num">
					                    《司法鉴定人执业证》证号：${per.identify_code}
					          </div></c:if>
					           <c:if test="${status.index>0}">
					              <div class="ending_name">
					                     鉴定人：${per.identify_per}
					          </div>
					          <div class="ending_num">
					        	《司法鉴定人执业证》证号：${per.identify_code}
					          </div>
					           </c:if>
					          </c:forEach>
						          <div class="ending_time">
						               ${caseModel.close_time_china}
						          </div>
						          <div class="ending_mark">注：
						              <div>1.本检验仅对送检检材负责；</div>
						              <div>2.本实验室不负责送检检材的保存；</div>
						              <div>3.本报告书未经书面批准，不得复制。</div>
						          </div>
					     </div>
					     </c:if>
					      <c:if test="${type.result.compareResultModel.ext_flag=='Y'}"> 
					         <div class="page">
						          <div class="page_title_name">
<c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
						          <div class="page_title">共&ensp;4&ensp;页第&ensp;1&ensp;页</div>
						         <hr/>
						          <div class="panel_title">四、检验结果</div>
						         <div class="pageem content first-content">
						           STR基因分型结果：
						         </div>
							      <div class="page_center">39-STR基因座分型结果表</div>
					              <table class="table border_no_bottom" cellSpacing="0" border="solid" cellPadding="0">
					              <tr><th>基因座</th><th><div>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}</div><div class="no_margin">${type.sampleModel.father_info.sample_code}</div></th><th><div>孩子：${type.sampleModel.child_info.sample_username}</div><div class="no_margin">${type.sampleModel.child_info.sample_code}</div></th></tr>
					              <c:forEach items="${type.sampleModel.sampleResults}" var="list" varStatus="status" begin="0" end="19" step="1"><tr>
				                  <c:forEach items="${list}" var="map">
										     <c:choose>  
										   <c:when test="${map.key=='name'}">
										       <td>${map.value}</td>
										   </c:when>  
										   <c:otherwise> 
										     <td>
											    <c:if test="${fn:substringAfter(map.value,',')==''}">
											        <table style='width:100% ;border:none;'>
												        <tr>
													        <td style='text-align: right;'>
								                                 ${fn:substringBefore(map.value,",")}
															</td>
															<td style='text-align: center;'>,
															</td>
															<td style='text-align:left;'>
															     ${fn:substringBefore(map.value, ',')}
															</td>
														</tr>
													</table>
											   </c:if>  
											    <c:if test="${fn:substringAfter(map.value,',')!=''}">
												        <table style='width:100% ;border:none;'>
													        <tr>
														        <td style='text-align: right;'>
									                                ${fn:substringBefore(map.value,",")}
																</td>
																<td style='text-align: center;'>,
																</td>
																<td style='text-align:left;'>
																    ${fn:substringAfter(map.value,",")}
																</td>
															</tr>
														</table>
											   </c:if>
											    </td>  
										   </c:otherwise>  
										</c:choose>  
				                  </c:forEach>
				                  </tr></c:forEach>
				          </table>
				         </div>
				         <div class="page">
						      <div class="page_title_name">
							  <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
					          <div class="page_title">共&ensp;4&ensp;页第&ensp;3&ensp;页</div>
					         <hr/>
					         <table class="table border_no_top" cellSpacing="0" border="solid" cellPadding="0">
				                <c:forEach items="${type.sampleModel.sampleResults}" var="list" varStatus="status" begin="20" step="1"><tr>
				                  <c:forEach items="${list}" var="map">
										    <c:choose>  
										   <c:when test="${map.key=='name'}">
										       <td>${map.value}</td>
										   </c:when>  
										   <c:otherwise>
										      <td> 
											    <c:if test="${fn:substringAfter(map.value,',')==''}">
											        <table style='width:100% ;border:none;'>
												        <tr>
													        <td style='text-align: right;'>
								                                 ${fn:substringBefore(map.value,",")}
															</td>
															<td style='text-align: center;'>,
															</td>
															<td style='text-align:left;'>
															     ${fn:substringBefore(map.value, ',')}
															</td>
														</tr>
													</table>
											   </c:if>  
											    <c:if test="${fn:substringAfter(map.value,',')!=''}">
												        <table style='width:100% ;border:none;'>
													        <tr>
														        <td style='text-align: right;'>
									                                ${fn:substringBefore(map.value,",")}
																</td>
																<td style='text-align: center;'>,
																</td>
																<td style='text-align:left;'>
																    ${fn:substringAfter(map.value,",")}
																</td>
															</tr>
														</table>
											   </c:if>
											   </td>
										   </c:otherwise>  
										</c:choose>  
				                  </c:forEach>
				                  </tr></c:forEach>
				                      </table>
						          <div class="pageem content paragraph">
						            D19S433等39个STR基因座均为人类的遗传学标记，遵循孟德尔遗传定律，联合应用可进行亲权鉴定。上述39个STR基因座检测结果显示：${type.sampleModel.child_info.sample_username}在${type.result.compareResultModel.unmatched_node}基因座上的等位基因不能从${type.sampleModel.father_info.sample_username}的基因型中找到来源。
						          </div>
						          <div class="pageem content paragraph">
						                     依据现有资料和DNA分析结果，排除${type.sampleModel.father_info.sample_username}是${type.sampleModel.child_info.sample_username}的生物学${type.sampleModel.father_info.sample_callname}。
						          </div> 
					       </div>
					      <div class="page">
						      <div class="page_title_name">
<c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
					          <div class="page_title">共&ensp;4&ensp;页第&ensp;4&ensp;页</div>
					         <hr/>
					           <c:forEach items="${pers}" var="per" varStatus="status" >
					             <c:if test="${status.index==0}">
					             <div class="ending_name" style="margin-top:150px;">
					                     鉴定人：${per.identify_per}
					          </div>
					          <div class="ending_num">
					                    《司法鉴定人执业证》证号：${per.identify_code}
					          </div></c:if>
					           <c:if test="${status.index>0}">
					              <div class="ending_name">
					                     鉴定人：${per.identify_per}
					          </div>
					          <div class="ending_num">
					        	《司法鉴定人执业证》证号：${per.identify_code}
					          </div>
					           </c:if>
					          </c:forEach>
						          <div class="ending_time">
						               ${caseModel.close_time_china}
						          </div>
						          <div class="ending_mark">注：
						              <div>1.本检验仅对送检检材负责；</div>
						              <div>2.本实验室不负责送检检材的保存；</div>
						              <div>3.本报告书未经书面批准，不得复制。</div>
						          </div>
					      </div>   
					 </c:if>
		           </c:if> 
    </c:forEach>
</body>
</html>
