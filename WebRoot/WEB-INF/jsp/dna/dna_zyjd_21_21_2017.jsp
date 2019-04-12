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
	.table_check {
	  font-size:31px;
	  text-align:center;
	  width:100%;
	}
	.table_check th{
        border: 1px solid;
	}
	.table_check tr{
        border: 1px solid;
	}
	.table_check td{
        border: 1px solid;
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
		             宿迁子渊司法鉴定所司法鉴定意见书
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
		           <div class="spanel"><span class="p_title">委托鉴定事项：</span><c:if test="${type.sampleModel.mother_info.sample_code!=''&&type.sampleModel.mother_info.sample_code!=null}">对${type.sampleModel.father_info.sample_username}、${type.sampleModel.mother_info.sample_username}与${type.sampleModel.child_info.sample_username}之间有无亲生血缘关系的鉴定。</c:if><c:if test="${type.sampleModel.mother_info.sample_code==''||type.sampleModel.mother_info.sample_code==null}">对${type.sampleModel.father_info.sample_username}与${type.sampleModel.child_info.sample_username}之间有无亲生血缘关系的鉴定。</c:if></div>
		           <div class="spanel"><span class="p_title"> 委托日期：</span>
		             <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				        ${caseModel.accept_time}
				      </c:if>
				      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
							${caseModel.addextnew_time}
					  </c:if> 
				  </div>
		          <div class="spanel"><span class="p_title">受理日期：</span>
		          <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				      ${caseModel.accept_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					 ${caseModel.addextnew_time}
					  
			  </c:if>
			  </div>
		           <div class="spanel"><span class="p_title">鉴定材料：</span>${type.sampleModel.father_info.sample_username}<c:if test="${type.sampleModel.mother_info.sample_code!=''&&type.sampleModel.mother_info.sample_code!=null}">、${type.sampleModel.mother_info.sample_username}</c:if>与${type.sampleModel.child_info.sample_username}的血样。</div>
		          <div class="spanel"><span class="p_title">被鉴定人概况</span></div>
		          <div align=center>
		           <table class="table_check" cellSpacing="0" cellPadding="0" >
					     <tr><th>被鉴定人</th><th>性别</th><th>称谓</th><th>出生日期</th><th>身份证件号码</th><th>样本编号</th></tr>
					 <c:if test="${type.sampleModel.mother_info.sample_code!=''&&type.sampleModel.mother_info.sample_code!=null}">
						 <tr>
							 <td>${type.sampleModel.mother_info.sample_username}</td>
							 <td><c:if test="${type.sampleModel.mother_info.sample_callname=='父亲'}">男</c:if><c:if test="${type.sampleModel.mother_info.sample_callname=='母亲'}">女</c:if></td>
							 <td>${type.sampleModel.mother_info.sample_callname}</td>
							 <td><c:if test="${type.sampleModel.mother_info.birth_date_format!=null&&fn:trim(type.sampleModel.mother_info.birth_date_format)!=''}">${type.sampleModel.mother_info.birth_date_format}</c:if></td>
							 <td><c:if test="${type.sampleModel.mother_info.id_number!=''&&type.sampleModel.mother_info.id_number!=null}">${type.sampleModel.mother_info.id_number}</c:if></td>
							 <td>${type.sampleModel.mother_info.sample_code}</td>
						 </tr>
	           	     </c:if>
						 <tr>
							 <td>${type.sampleModel.child_info.sample_username}</td>
							 <td><c:if test="${type.sampleModel.child_info.sample_callname=='儿子'}">男</c:if><c:if test="${type.sampleModel.child_info.sample_callname=='女儿'}">女</c:if></td>
							 <td>${type.sampleModel.child_info.sample_callname}</td>
							 <td><c:if test="${type.sampleModel.child_info.birth_date_format!=null&&fn:trim(type.sampleModel.child_info.birth_date_format)!=''}">${type.sampleModel.child_info.birth_date_format}</c:if></td>
							 <td><c:if test="${type.sampleModel.child_info.id_number!=''&&type.sampleModel.child_info.id_number!=null}">${type.sampleModel.child_info.id_number}</c:if></td>
							 <td>${type.sampleModel.child_info.sample_code}</td>
						 </tr>
	 					<tr>
		 					<td class="bottom_border">${type.sampleModel.father_info.sample_username}</td>
		 					<td class="bottom_border"><c:if test="${type.sampleModel.father_info.sample_callname=='父亲'}">男</c:if><c:if test="${type.sampleModel.father_info.sample_callname=='母亲'}">女</c:if></td>
		 					<td class="bottom_border">${type.sampleModel.father_info.sample_callname}</td>
		 					<td class="bottom_border"><c:if test="${type.sampleModel.father_info.birth_date_format!=null&&fn:trim(type.sampleModel.father_info.birth_date_format)!=''}">${type.sampleModel.father_info.birth_date_format}</c:if></td>
		 					<td class="bottom_border"><c:if test="${type.sampleModel.father_info.id_number!=''&&type.sampleModel.father_info.id_number!=null}">${type.sampleModel.father_info.id_number }</c:if></td>
		 					<td class="bottom_border">${type.sampleModel.father_info.sample_code}</td>
	 					</tr>
					</table>
				 </div>
		          <div class="spanel"><span class="p_title">鉴定日期：</span>
				  <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				       ${caseModel.accept_time}-${caseModel.close_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
					${caseModel.addextnew_time}-${caseModel.close_time}
					
			  </c:if>
			  </div>
		          <div class="spanel  intent"><span class="p_title">鉴定地点：</span>宿迁子渊司法鉴定所</div>
		           <div class="panel_title">二、基本案情</div>
		          <div class="pageem content paragraph">
		           ${type.sampleModel.father_info.sample_username}<c:if test="${type.sampleModel.mother_info.sample_code!=''&&type.sampleModel.mother_info.sample_code!=null}">和${type.sampleModel.mother_info.sample_username}</c:if>因申报户口需要明确其本人是否是${type.sampleModel.child_info.sample_username}的生物学<c:if test="${type.sampleModel.mother_info.sample_code!=''&&type.sampleModel.mother_info.sample_code!=null}">父母</c:if><c:if test="${type.sampleModel.mother_info.sample_code==''||type.sampleModel.mother_info.sample_code==null}">${type.sampleModel.father_info.sample_callname}</c:if>,故委托本所进行鉴定。
		          </div>
		          <div class="panel_title">三、资料摘要</div>
		          <div class="pageem content paragraph">
		          <span>${type.sampleModel.father_info.sample_username}的血痕编号为${type.sampleModel.father_info.sample_code}，</span>
		          <span><c:if test="${type.sampleModel.mother_info.sample_code!=''&&type.sampleModel.mother_info.sample_code!=null}">
						${type.sampleModel.mother_info.sample_username}的血痕编号为${type.sampleModel.mother_info.sample_code}，  
	           	     </c:if>${type.sampleModel.child_info.sample_username}的血痕编号为${type.sampleModel.child_info.sample_code}，</span>
		          <span><c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
				        ${caseModel.accept_time}-${caseModel.close_time}送至实验室，
			      </c:if></span>
			      <span><c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
						${caseModel.addextnew_time}-${caseModel.close_time}送至实验室，
				  </c:if>按照《法庭科学DNA实验室检验规范》（GA/T 383-2014）、《法庭科学DNA亲子鉴定规范》（GA/T 965-2011）、《亲权鉴定技术规范》（SF/Z JD0105001-2016）等标准进行鉴定。
				  </span>
		          </div>
		         <div class="panel_title">四、鉴定过程</div>
		          <div class="pageem content paragraph"><span>（一）检材处理和检验方法</span></div>
		          <div class="pageem content paragraph">1.参照《法庭科学&ensp;DNA&ensp;实验室检验规范》（GA/T&ensp;383-2014）中 Chelex-100法提取上述检材的DNA。 </div>
		          <div class="pageem content paragraph">2.取上述检材的DNA适量，用荧光标记STR复合扩增试剂盒<c:if test="${reagent_name=='MR20' }">GOLDENEYE</c:if><c:if test="${reagent_name=='MR21' }">MICROREADER</c:if><sup>TM</sup>进行10μL体系PCR扩增（操作步骤按试剂盒说明），并设立阴性及阳性对照。</div>
		          <c:if test="${type.sampleModel.mother_info.sample_code==''||type.sampleModel.mother_info.sample_code==null}">
		          <div class="pageem content paragraph">3.扩增产物用ABI3130xl遗传分析仪进行毛细管电泳、分离并采用GeneMapper®ID软件进行基因型分析。</div>
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
				          <div class="pageem content paragraph">3.扩增产物用ABI3130xl遗传分析仪进行毛细管电泳、分离并采用GeneMapper®ID软件进行基因型分析。</div>
				         <div class="pageem content paragraph"><span>（二）鉴定结果</span></div>
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
			  
						  
						  </div>
						   </th>
					           <th><div>${type.sampleModel.mother_info.sample_username}</div><div class="no_margin">${type.sampleModel.mother_info.sample_code}</div></th>
						       <th><div>${type.sampleModel.child_info.sample_username}</div><div class="no_margin">${type.sampleModel.child_info.sample_code}</div></th>
					           <th><div>${type.sampleModel.father_info.sample_username}</div><div class="no_margin">${type.sampleModel.father_info.sample_code}</div></th>
				          </tr>
						   <tr><td style="width:37%;border-bottom: solid 1px;font-weight: bold;">基因位点</td><td colspan="3" style="border-bottom: solid 1px;font-weight: bold;">等位基因</td></tr>
				          <c:forEach items="${type.sampleModel.sampleResults}" var="list" begin="0" end="19" step="1"><tr><c:forEach items="${list}" var="map">
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
				          <div class="page_title">共&ensp;3&ensp;页第&ensp;3&ensp;页</div>
				         <hr/>
				         <div class="panel_title">五、分析说明</div>
					        <div class="pageem content paragraph">
					            D19S433等20个STR基因座均为人类的遗传学标记，遵循孟德尔遗传定律，联合应用可进行亲权鉴定，其累积非父排除概率大于0.9999。
					          </div>
					           <div class="pageem content paragraph">
					                                     综上鉴定结果分析：在每一个STR基因座，${type.sampleModel.father_info.sample_username}和${type.sampleModel.mother_info.sample_username}均能提供给${type.sampleModel.child_info.sample_username}必需的等位基因。经计算，累积亲权指数为${type.result.pi}（注：大于10000）。
					            </div>
					         <div class="panel_title">六、鉴定意见</div>   
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
					           <div class="ending_name" style="margin-top:150px;">
					                     授权签字人：陈太基
					          </div>
					          <div class="ending_num">
					                    《司法鉴定人执业证》证号：321314004004
					          </div>
					          <div class="ending_time">
					               ${caseModel.close_time_china}
					          </div>
					          <div class="ending_mark">注：
					              <div>1.本鉴定仅对送检检材负责；</div>
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
				         <div class="pageem content paragraph">3.扩增产物用ABI3130xl遗传分析仪进行毛细管电泳、分离并采用GeneMapper®ID软件进行基因型分析。</div>
				         <div class="pageem content paragraph"><span>（二）鉴定结果</span></div>
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
				             <c:forEach items="${type.sampleModel.sampleResults}" var="list" varStatus="status" begin="0" end="19" step="1"><tr><c:forEach items="${list}" var="map">
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
				                  </c:forEach>
				                  </tr></c:forEach>
		                 </table>
		                 <div class="panel_title">五、分析说明</div>
		                 <div class="pageem content paragraph" style="margin-top: 40px;">
		                     D19S433等39个STR基因座均为人类的遗传学标记，遵循孟德尔遗传定律，联合应用可进行亲权鉴定，其累积非父排除概率大于0.9999。
		                 </div>
		                 <div class="pageem content paragraph" style="margin-top: 40px;">
		                 		综上鉴定结果分析：除${type.result.compareResultModel.unmatched_node}基因座以外，${type.sampleModel.father_info.sample_username}和${type.sampleModel.mother_info.sample_username}均能提供给${type.sampleModel.child_info.sample_username}必需的等位基因。在${type.result.compareResultModel.unmatched_node}基因座，${type.sampleModel.father_info.sample_username}的基因型为“13，14”，${type.sampleModel.mother_info.sample_username}的基因型为“17，19”，${type.sampleModel.child_info.sample_username}的基因型为“17，17”，${type.sampleModel.father_info.sample_username}不能提供给孩子必须的等位基因17，不符合遗传规律。按照GA/T965-2011《法庭科学DNA亲子鉴定规范》和SF/Z JD0105001-2016《亲权鉴定技术规范》中不符合遗传规律情形时亲权指数的计算方法，${type.result.compareResultModel.unmatched_node}基因座的亲权指数为XXXX。综上38个STR基因座的累积亲权指数为${type.result.pi}（注：大于10000）。
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
				         <div class="panel_title">六、鉴定意见</div>
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
					           <div class="ending_name" style="margin-top:150px;">
					                     授权签字人：陈太基
					          </div>
					          <div class="ending_num">
					                    《司法鉴定人执业证》证号：321314004004
					          </div>
				          <div class="ending_time">
				               ${caseModel.close_time_china}
				          </div>
				          <div class="ending_mark">注：
				              <div>1.本鉴定仅对送检检材负责；</div>
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
				        <div class="pageem content paragraph"><span>（二）鉴定结果</span></div>
				         <div class="pageem content first-content">
				           STR基因分型结果：
				          </div>
				          <div class="page_center">20-STR基因座分型结果表</div><table class="table" cellSpacing="0" border="solid" cellPadding="0">
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
				          <div class="page_title">共&ensp;3&ensp;页第&ensp;3&ensp;页</div>
				         <hr/>
					          <div class="panel_title">五、分析说明</div>
					          <div class="pageem content paragraph">
					            D19S433等20个STR基因座均为人类的遗传学标记，遵循孟德尔遗传定律，联合应用可进行亲权鉴定。</div>
					          <div class="pageem content paragraph">
					                                     综上鉴定结果分析：在每一个STR基因座，${type.sampleModel.father_info.sample_username}均能提供给${type.sampleModel.child_info.sample_username}必需的等位基因。经计算，累积亲权指数为${type.result.pi}（注：大于10000）,即${type.sampleModel.father_info.sample_username}为${type.sampleModel.child_info.sample_username}生物学${type.sampleModel.father_info.sample_callname}的可能性是无关<c:if test="${type.sampleModel.father_info.sample_callname=='父亲'}">男</c:if><c:if test="${type.sampleModel.father_info.sample_callname=='母亲'}">女</c:if>性个体为${type.sampleModel.child_info.sample_username}生<c:if test="${type.sampleModel.father_info.sample_callname=='父亲'}">父</c:if><c:if test="${type.sampleModel.father_info.sample_callname=='母亲'}">母</c:if>可能性的${type.result.pi }倍。                        
					            </div>
					         <div class="panel_title">六、鉴定意见</div> 
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
					          <div class="ending_name" style="margin-top:150px;">
					                     授权签字人：陈太基
					          </div>
					          <div class="ending_num">
					                    《司法鉴定人执业证》证号：321314004004
					          </div>
					          <div class="ending_time">
					               ${caseModel.close_time_china}
					          </div>
					          <div class="ending_mark">注：
					              <div>1.本鉴定仅对送检检材负责；</div>
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
				       <div class="pageem content paragraph"><span>（二）鉴定结果</span></div>
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
				          <div class="page_title_name"><c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
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
				          <div class="panel_title">五、分析说明</div>
				          <div class="pageem content paragraph" style="margin-top: 40px;">
		                     D19S433等39个STR基因座均为人类的遗传学标记，遵循孟德尔遗传定律，联合应用可进行亲权鉴定，其累积非父排除概率大于0.9999。
		                 </div>
				            <c:if test="${type.result.compareResultModel.unmatched_count>0}">
		                 <div class="pageem content paragraph" style="margin-top: 40px;">
		                 		综上鉴定结果分析：除${type.result.compareResultModel.unmatched_node}基因座以外，${type.sampleModel.father_info.sample_username}和${type.sampleModel.mother_info.sample_username}均能提供给${type.sampleModel.child_info.sample_username}必需的等位基因。在${type.result.compareResultModel.unmatched_node}基因座，${type.sampleModel.father_info.sample_username}的基因型为“13，14”，${type.sampleModel.mother_info.sample_username}的基因型为“17，19”，${type.sampleModel.child_info.sample_username}的基因型为“17，17”，${type.sampleModel.father_info.sample_username}不能提供给孩子必须的等位基因17，不符合遗传规律。按照GA/T965-2011《法庭科学DNA亲子鉴定规范》和SF/Z JD0105001-2016《亲权鉴定技术规范》中不符合遗传规律情形时亲权指数的计算方法，${type.result.compareResultModel.unmatched_node}基因座的亲权指数为XXXX。综上38个STR基因座的累积亲权指数为${type.result.pi}（注：大于10000）。
		                 </div>
				              </c:if>
				              <c:if test="${type.result.compareResultModel.unmatched_count==0}">
				                       <div class="pageem content paragraph">
					                                     综上鉴定结果分析：在每一个STR基因座，${type.sampleModel.father_info.sample_username}和${type.sampleModel.mother_info.sample_username}均能提供给${type.sampleModel.child_info.sample_username}必需的等位基因。经计算，累积亲权指数为${type.result.pi}（注：大于10000）。
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
				         <div class="panel_title">六、鉴定意见</div>
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
					             <div class="ending_name" style="margin-top:150px;">
					                     授权签字人：陈太基
					          </div>
					          <div class="ending_num">
					                    《司法鉴定人执业证》证号：321314004004
					          </div>
					          <div class="ending_time">
					               ${caseModel.close_time_china}
					          </div>
					          <div class="ending_mark">注：
					              <div>1.本鉴定仅对送检检材负责；</div>
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
					 宿迁子渊司法鉴定所[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]法物鉴字第${caseModel.case_code}号 
			  </c:if>
			  </div>
						          <div class="page_title">共&ensp;3&ensp;页第&ensp;2&ensp;页</div>
						         <hr/>
				         <div class="pageem content paragraph"><span>（二）鉴定结果</span></div>
						         <div class="pageem content first-content">
						           STR基因分型结果：
						          </div>
						          <div class="page_center">20-STR基因座分型结果表</div>
						          <table class="table" cellSpacing="0" border="solid" cellPadding="0">
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
					          <div class="page_title">共&ensp;3&ensp;页第&ensp;3&ensp;页</div>
					         <hr/>
					         <div class="panel_title">五、分析说明</div>
						          <div class="pageem content paragraph">
						          D19S433等20个STR基因座均为人类的遗传学标记，遵循孟德尔遗传定律，联合应用可进行亲权鉴定，其累积非父排除概率大于0.9999。
						          </div>
						          <div class="pageem content paragraph">
						         	 综上鉴定结果分析：${type.sampleModel.child_info.sample_username}在${type.result.compareResultModel.unmatched_node}基因座上的等位基因不能从${type.sampleModel.father_info.sample_username}的基因型中找到来源。经计算，累积亲权指数为${type.result.pi}（注：小于0.0001）。
						          </div>
						         <div class="panel_title">六、鉴定意见</div> 
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
					          <div class="ending_name" style="margin-top:150px;">
					                     授权签字人：陈太基
					          </div>
					          <div class="ending_num">
					                    《司法鉴定人执业证》证号：321314004004
					          </div>
						          <div class="ending_time">
						               ${caseModel.close_time_china}
						          </div>
						          <div class="ending_mark">注：
						              <div>1.本鉴定仅对送检检材负责；</div>
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
						          <div class="panel_title">四、鉴定结果</div>
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
						            <div class="panel_title">五、分析说明</div>
		                 <div class="pageem content paragraph" style="margin-top: 40px;">
		                     D19S433等39个STR基因座均为人类的遗传学标记，遵循孟德尔遗传定律，联合应用可进行亲权鉴定，其累积非父排除概率大于0.9999。
		                 </div>
		                 <div class="pageem content paragraph" style="margin-top: 40px;">
		                 		综上鉴定结果分析：${type.sampleModel.child_info.sample_username}在${type.result.compareResultModel.unmatched_node}基因座上的等位基因不能从${type.sampleModel.father_info.sample_username}的基因型中找到来源。经计算，累积亲权指数为${type.result.pi}（注：小于0.0001）。
		                 </div>
		                  <div class="panel_title">六、鉴定意见</div>
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
					           <div class="ending_name" style="margin-top:150px;">
					                     授权签字人：陈太基
					          </div>
					          <div class="ending_num">
					                    《司法鉴定人执业证》证号：321314004004
					          </div>
						          <div class="ending_time">
						               ${caseModel.close_time_china}
						          </div>
						          <div class="ending_mark">注：
						              <div>1.本鉴定仅对送检检材负责；</div>
						              <div>2.本实验室不负责送检检材的保存；</div>
						              <div>3.本报告书未经书面批准，不得复制。</div>
						          </div>
					      </div>   
					 </c:if>
		           </c:if> 
    </c:forEach>
</body>
</html>
