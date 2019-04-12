<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
  <%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML>
<HEAD>
<META content="text/html; charset=UTF-8" http-equiv="Content-Type">
<title></title>
<style type="text/css"> 
     .page{
		page-break-after: always;
		margin-right:45px;
		margin-left:45px;
		clear:both;
		padding-top:30px;
		word-spacing:14px;
		letter-spacing : normal;
		text-align:justify; text-justify:inter-ideograph;
	}
	.left{
		float:left;
	}
	.right{
		float:right;
	}
	hr{
		clear:both;
		border:none;
		border-top:2.1px solid black;
		width: 100%;
	}
	.pageno{
		font-size:20px;
		padding-bottom:20px;
		font-family:"STSong";
	}
	.title{
		margin-top:70px;
		font-size:40px;
		text-align: center;
		font-family:'黑体';
	}
	.title_remark{
		font-size:24px;
		margin-top:20px;
		font-family:"SimSun";
	}
	.clear{
		clear:both;
	}
	.panel_title{
		font-size:35px;
		font-family:'黑体';
	}
	.panel{
		line-height:2.17;
		padding-top:20px;
	}
	.spanel{
		font-size:31px;
		margin-left:50px;
	}
	body{
		font-family: "FangSong";
		width:1055px;
	}
	.p_title{
		font-family:'黑体';
	}
	.paragraph{
		text-indent: 2em;
		font-size:31px;
	}
	.paragraph_2{
		font-size:31px;
	}
	.table td{	
	    border: 1.5px solid;
	    border-bottom:none;
		border-top:none;
		border-right:none;
		font-size:22px;
		font-family: "Times New Roman";
	}
	.table td table td{	
	    border: none;
	}
	.table th{
	    border: 1.5px solid;
		padding-top:10px;
		padding-bottom:10px;
		font-weight: bolder;
		border-top:none;
		border-right:none;
	}
	.no_margin{
		margin-top:0px;
	}
	.table{
		width:100%;
		font-size:22px;
		text-align:center;
		margin-top:30px;
		border: 3px solid;
		line-height:30px;;
	}
	.ending_name{
		font-size:31px;
		margin-left:390px;
	}
	.ending_code{
		font-size:31px;
	}
	.ending_time{
		font-size:31px;
	}
	.file{
	  width: 100%;
	}
	tr div{
	  line-height: 31px;
	}
</style>

</head>

<body>
   <c:forEach items="${caseModel.results}" var="type" >
     <c:if test="${type.result_type=='passed_2'&&type.result.compareResultModel.ext_flag=='N'}">
       <div class="page">
          <div class="right pageno">
                     共&ensp;4&ensp;页 第&ensp;1&ensp;页
          </div>
          <hr/>
          <div class="title">安徽龙图司法鉴定中心亲子鉴定意见书</div>
          <div class="title_remark right">
           <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		皖龙图司鉴[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]
	          法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		 皖龙图司鉴[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]
          法物鉴字第${caseModel.case_code}号
			  </c:if>  
         </div>
          <div class="clear panel">
          <div class="panel_title">一、基本情况</div>
          <div class="spanel"><span class="p_title">委托人：</span>${caseModel.client}</div>
          <div class="spanel"><span class="p_title">委托鉴定事项：</span>为明确${type.sampleModel.father_info.sample_username}和${type.sampleModel.child_info.sample_username}之间是否存在亲生血缘关系，申请进行DNA亲子鉴定</div>
          <div class="spanel"><span class="p_title">受理日期：</span>
          <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		${caseModel.accept_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		 ${caseModel.addextnew_time}
			  </c:if>
          </div>
          <div class="spanel"><span class="p_title left">鉴定材料：</span>
          <div class="">标注为“${type.sampleModel.father_info.sample_username}”的血样(编号为${type.sampleModel.father_info.sample_code})、标注为“${type.sampleModel.child_info.sample_username}”的血样(编号为${type.sampleModel.child_info.sample_code})</div></div>
          <div class="spanel"><span class="p_title">鉴定日期：</span>
          
          <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		${caseModel.accept_time}----${caseModel.close_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		 ${caseModel.addextnew_time}----${caseModel.close_time}
			  </c:if>
          </div>
          <div class="spanel"><span class="p_title">鉴定地点：</span>安徽龙图司法鉴定中心</div>
          <div class="spanel"><span class="p_title">在场人员：</span><c:forEach items="${pers}" var="per" varStatus="status" ><c:if test="${status.index==0}">${per.identify_per}、</c:if><c:if test="${status.index>0}">${per.identify_per}</c:if></c:forEach></div>
          <div class="spanel"><span class="p_title">被鉴定人：</span>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}<c:if test="${type.sampleModel.father_info.id_number==''||type.sampleModel.father_info.id_number==null}"><c:if test="${type.sampleModel.father_info.birth_date!=''&&type.sampleModel.father_info.birth_date!=null}">，${type.sampleModel.father_info.birth_date}</c:if></c:if><c:if test="${type.sampleModel.father_info.id_number!=''&&type.sampleModel.father_info.id_number!=null }">，身份证号：${type.sampleModel.father_info.id_number}</c:if></div>
          <div class="spanel">
          <c:if test="${type.sampleModel.child_info.id_number==null||type.sampleModel.child_info.id_number==''}">
                         孩子：据委托人告知姓名为${type.sampleModel.child_info.sample_username}（<c:if test="${type.sampleModel.child_info.sample_callname=='儿子'}">男</c:if><c:if test="${type.sampleModel.child_info.sample_callname=='女儿'}">女</c:if><c:if test="${type.sampleModel.child_info.birth_date_format!=null&&type.sampleModel.child_info.birth_date_format!=''}">、${type.sampleModel.child_info.birth_date_format}生</c:if>）
          </c:if>
          <c:if test="${type.sampleModel.child_info.id_number!=null&&type.sampleModel.child_info.id_number!=''}">
                 <span style="margin-left: 156px;">${type.sampleModel.child_info.sample_callname}：${type.sampleModel.child_info.sample_username}，身份证号：${type.sampleModel.child_info.id_number}</span>
          </c:if>
          </div>
          <div class="panel_title">二、检案摘要</div>
	      <div class="paragraph ">
	      <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		 ${caseModel.accept_time}，
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		 ${caseModel.addextnew_time}， 
			  </c:if>
	      
	      因办理户口需要，要求对${type.sampleModel.father_info.sample_username}和${type.sampleModel.child_info.sample_username}之间是否存在亲生血缘关系进行DNA亲子鉴定。</div>
	      <div class="panel_title">三、检验过程</div>
	      <div class="paragraph_2"><span class="p_title">（一）检验方法</span></div>
	      <div class="paragraph ">检材DNA提取和基因分型检测按照中华人民共和国行业标准《法庭科学DNA实验室检验规范》（GA/T&ensp;383-2014）、《法庭科学DNA亲子鉴</div>
       </div>
     </div>
     <div class="page">
         <div class="right pageno">
                    共&ensp;4&ensp;页 第&ensp;2&ensp;页
         </div>
         <hr/>
          <div class="clear panel">
           <div class="paragraph_2">定规范》(GA/T&ensp;965-2011)和司法鉴定技术规范《亲权鉴定技术规范》(SF/Z&ensp;JD0105001-2010)进行。</div>
           <div class="paragraph_2"><span class="p_title">（二）DNA检验步骤</span></div>
           <div class="paragraph">1.PCR扩增：采用无锡中德美联生物技术有限公司AGCU&ensp;Expressmarker22荧光检测试剂盒，用&ensp;9700&ensp;型PCR扩增仪（美国Applied&ensp;Biosystems公司）对上述检材进行扩增。</div>
           <div class="paragraph">2.检测：扩增结果采用ABI3130遗传分析仪进行结果分析。</div>
           <div class="panel_title">四、检验结果</div>
           <div class="paragraph">1.基因分型结果表</div>
           <table class="table" cellSpacing="0" border="solid" cellPadding="0" style="line-height: 31px;">
              <tr  style="height: 50px;"><th width=30%>基因座</th><th width=35%><div>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}</div><div class="no_margin">${type.sampleModel.father_info.sample_code}</div></th><th width="35%"><div>孩子：${type.sampleModel.child_info.sample_username}</div><div class="no_margin">${type.sampleModel.child_info.sample_code}</div></th></tr>
              <c:forEach items="${type.sampleModel.sampleResults}" var="list"  begin="0" end="20" step="1"><tr style="height: 31px;"><c:forEach items="${list}" var="map">
              <c:choose>  
					   <c:when test="${map.key=='name'}">
					       <td width=30% >${fn:replace(map.value, ' ', '&ensp;')}</td>
					   </c:when>  
					   <c:otherwise>  
					   	 <td width=35%><c:if test="${fn:substringAfter(map.value,',')==''}">
						        <table style='width:100% ;border:none;'>
							        <tr>
								        <td style='text-align: right;width: 40%'>
			                                 ${fn:substringBefore(map.value,",")}
										</td>
										<td style='text-align: center;width: 20%'>,
										</td>
										<td style='text-align:left;width: 40%'>
										     ${fn:substringBefore(map.value, ',')}
										</td>
									</tr>
								</table>
						   </c:if>  
						    <c:if test="${fn:substringAfter(map.value,',')!=''}">
							        <table style='width:100% ;border:none;'>
								        <tr>
									        <td style='text-align: right;width: 40%'>
				                                ${fn:substringBefore(map.value,",")}
											</td>
											<td style='text-align: center;width: 20%'>,
											</td>
											<td style='text-align:left;width: 40%'>
											    ${fn:substringAfter(map.value,",")}
											</td>
										</tr>
									</table>
						   </c:if></td>   
					   </c:otherwise>  
					</c:choose>  
                </c:forEach></tr></c:forEach>
           </table>
         </div>
     </div>
     <div class="page">
         <div class="right pageno">
                   共&ensp;4&ensp;页 第&ensp;3&ensp;页
         </div>
         <hr/>
         <div class="paragraph ">2.累积非父排除率计算结果： </div>
         <div class="clear panel">
              <div class="paragraph ">本检测系统的累积非父排除率达0.9999以上。</div>
              <div class="panel_title">五、分析说明</div>
              <div class="paragraph ">上述所检基因座检测结果显示，${type.sampleModel.father_info.sample_username}和${type.sampleModel.child_info.sample_username}两者的遗传关系符合孟德尔遗传规律，其累计亲权指数为<c:if test="${type.result.pi_num==0}">${type.result.pi}</c:if><c:if test="${type.result.pi_num!=0}">${type.result.pi_str}×10<sup>${type.result.pi_num}</sup></c:if>。</div>
           </div>
         <div class="clear panel">
             <div class="panel_title">六、鉴定意见</div>
             <div class="paragraph ">依据DNA分析结果，在不考虑同卵多胞胎、近亲和其他外界因素干扰的前提下，支持${type.sampleModel.father_info.sample_username}和${type.sampleModel.child_info.sample_username}之间存在亲生血缘关系。</div>
             <div class="panel_title">七、落款</div>
             <c:forEach items="${pers}" var="per" varStatus="status" >
					   <div class="ending_name clear">司法鉴定人：${per.identify_per}</div>
                      <div class="ending_code right clear">《司法鉴定人执业证》证号：${per.identify_code}</div>
			</c:forEach>
             <div class="ending_time clear right" style="margin-top: 100px;">
             ${caseModel.close_time_china}</div>
             <div class="paragraph_2 clear" style="margin-top: 65%;">附件：照片共1张</div>
         </div>
     </div>
     <div class="page">
         <div class="right pageno">
                     共&ensp;4&ensp;页 第&ensp;4&ensp;页
         </div>
         <hr/>
         <div class="ending_code">被鉴定人照片如下：</div>
         <img  width="90%" height="90%" src="<%=basePath%>judicial/attachment/getImg.do?filename=${type.attachmentModel.attachment_path}"  class="file" align="middle"/>
     </div> 
     </c:if>
     
    <c:if test="${type.result_type=='passed_2_3' }">
     <div class="page">
          <div class="right pageno">
                     共&ensp;5&ensp;页 第&ensp;1&ensp;页
          </div>
          <hr/>
          <div class="title">安徽龙图司法鉴定中心亲子鉴定意见书</div>
          <div class="title_remark right">
          <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		 皖龙图司鉴[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]
          法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		 皖龙图司鉴[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]
          法物鉴字第${caseModel.case_code}号 
			  </c:if>
          </div>
          <div class="clear panel">
          <div class="panel_title">一、基本情况</div>
          <div class="spanel"><span class="p_title">委托人：</span>${caseModel.client}</div>
          <div class="spanel"><span class="p_title">委托鉴定事项：</span>为明确${type.sampleModel.father_info.sample_username}与<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><c:choose><c:when test="${status.last}">${child.sample_username}</c:when><c:otherwise>${child.sample_username}、</c:otherwise></c:choose></c:forEach>之间是否存在亲生血缘关系，申请进行DNA亲子鉴定</div>
          <div class="spanel"><span class="p_title">受理日期：</span>
          <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		${caseModel.accept_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		${caseModel.addextnew_time}
			  </c:if>
			</div>
          <div class="spanel"><span class="p_title left">鉴定材料：</span>
          <div class="">标注为“${type.sampleModel.father_info.sample_username}”的血样(编号为${type.sampleModel.father_info.sample_code})<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status">、标注为“${child.sample_username}”的血样(编号为${child.sample_code})</c:forEach></div></div>
          <div class="spanel"><span class="p_title">鉴定日期：</span>
          <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		${caseModel.accept_time}----${caseModel.close_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		${caseModel.addextnew_time}----${caseModel.close_time}
			  </c:if>
          </div>
          <div class="spanel"><span class="p_title">鉴定地点：</span>安徽龙图司法鉴定中心</div>
          <div class="spanel"><span class="p_title">在场人员：</span><c:forEach items="${pers}" var="per" varStatus="status" ><c:if test="${status.index==0}">${per.identify_per}、</c:if><c:if test="${status.index>0}">${per.identify_per}</c:if></c:forEach></div>
          <div class="spanel"><span class="p_title">被鉴定人：</span>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}<c:if test="${type.sampleModel.father_info.id_number==''||type.sampleModel.father_info.id_number==null}"><c:if test="${type.sampleModel.father_info.birth_date!=''&&type.sampleModel.father_info.birth_date!=null}">，${type.sampleModel.father_info.birth_date}</c:if></c:if><c:if test="${type.sampleModel.father_info.id_number!=''&&type.sampleModel.father_info.id_number!=null }">，身份证号：${type.sampleModel.father_info.id_number}</c:if></div>
          <c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status">
          <div class="spanel">
                   孩子${status.count}：据委托人告知姓名为${child.sample_username}（<c:if test="${child.sample_callname=='儿子'}">男</c:if><c:if test="${child.sample_callname=='女儿'}">女</c:if><c:if test="${child.birth_date_format!=null&&child.birth_date_format!=''}">、${child.birth_date_format}生</c:if>）
          </div>
          </c:forEach>
          <div class="panel_title">二、检案摘要</div>
	      <div class="paragraph ">
	      <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		 ${caseModel.accept_time}，
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		  ${caseModel.addextnew_time}，
			  </c:if>
	     因办理户口需要，要求对${type.sampleModel.father_info.sample_username}与<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><c:choose><c:when test="${status.last}">${child.sample_username}</c:when><c:otherwise>${child.sample_username}、</c:otherwise></c:choose></c:forEach>之间是否存在亲生血缘关系进行DNA亲子鉴定。</div>
       </div>
     </div>
     <div class="page">
         <div class="right pageno">
                    共&ensp;5&ensp;页 第&ensp;2&ensp;页
         </div>
         <hr/>
          <div class="clear panel">
          <div class="panel_title">三、检验过程</div>
	       <div class="paragraph_2"><span class="p_title">（一）检验方法</span></div>
		   <div class="paragraph ">检材DNA提取和基因分型检测按照中华人民共和国行业标准《法庭科学DNA实验室检验规范》（GA/T&ensp;383-2014）、《法庭科学DNA亲子鉴定规范》(GA/T&ensp;965-2011)和司法鉴定技术规范《亲权鉴定技术规范》(SF/Z&ensp;JD0105001-2010)进行。</div>
           <div class="paragraph_2"><span class="p_title">（二）DNA检验步骤</span></div>
           <div class="paragraph">1.PCR扩增：采用无锡中德美联生物技术有限公司AGCU&ensp;Expressmarker22荧光检测试剂盒，用&ensp;9700&ensp;型PCR扩增仪（美国Applied&ensp;Biosystems公司）对上述检材进行扩增。</div>
           <div class="paragraph">2.检测：扩增结果采用ABI3130遗传分析仪进行结果分析。</div>
           <div class="panel_title">四、检验结果</div>
           <div class="paragraph">1.基因分型结果表</div>
           <table class="table" cellSpacing="0" border="solid" cellPadding="0" style="line-height: 45px;">
              <tr style="height: 50px;"><th width="16%" >基因座</th><th width="21%"><div>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}</div><div class="no_margin">${type.sampleModel.father_info.sample_code}</div></th><c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><th width="21%"><div>孩子：${child.sample_username}</div><div class="no_margin">${child.sample_code}</div></th></c:forEach></tr>
              <c:forEach items="${type.sampleModel.sampleResults}" var="list" begin="0" end="7" step="1"><tr style="height: 45px;"><c:forEach items="${list}" var="map">
              <c:choose>  
					   <c:when test="${map.key=='name'}">
					       <td width=16% >${fn:replace(map.value, ' ', '&ensp;')}</td>
					   </c:when>  
					   <c:otherwise>  
					   	 <td width=21% ><c:if test="${fn:substringAfter(map.value,',')==''}">
						        <table style='width:100% ;border:none;'>
							        <tr>
								        <td style='text-align: right;width: 40%'>
			                                 ${fn:substringBefore(map.value,",")}
										</td>
										<td style='text-align: center;width: 20%'>,
										</td>
										<td style='text-align:left;width: 40%'>
										     ${fn:substringBefore(map.value, ',')}
										</td>
									</tr>
								</table>
						   </c:if>  
						    <c:if test="${fn:substringAfter(map.value,',')!=''}">
							        <table style='width:100% ;border:none;'>
								        <tr>
									        <td style='text-align: right;width: 40%'>
				                                ${fn:substringBefore(map.value,",")}
											</td>
											<td style='text-align: center;width: 20%'>,
											</td>
											<td style='text-align:left;width: 40%'>
											    ${fn:substringAfter(map.value,",")}
											</td>
										</tr>
									</table>
						   </c:if></td>   
					   </c:otherwise>  
					</c:choose>  
                </c:forEach></tr></c:forEach>
           </table>
         </div>
     </div>
      <div class="page">
         <div class="right pageno">
                   共&ensp;5&ensp;页 第&ensp;3&ensp;页
         </div>
         <hr/>
         <table class="table" cellSpacing="0" border="solid" cellPadding="0" style="line-height: 45px;">
              <tr style="height: 50px;"><th width="16%" >基因座</th><th width="21%"><div>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}</div><div class="no_margin">${type.sampleModel.father_info.sample_code}</div></th><c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><th width="21%"><div>孩子：${child.sample_username}</div><div class="no_margin">${child.sample_code}</div></th></c:forEach></tr>
              <c:forEach items="${type.sampleModel.sampleResults}" var="list" begin="8" step="1"><tr style="height: 45px;"><c:forEach items="${list}" var="map">
              <c:choose>  
					   <c:when test="${map.key=='name'}">
					       <td width=16% >${fn:replace(map.value, ' ', '&ensp;')}</td>
					   </c:when>  
					   <c:otherwise>  
					   	 <td width=21% ><c:if test="${fn:substringAfter(map.value,',')==''}">
						        <table style='width:100% ;border:none;'>
							        <tr>
								        <td style='text-align: right;width: 40%'>
			                                 ${fn:substringBefore(map.value,",")}
										</td>
										<td style='text-align: center;width: 20%'>,
										</td>
										<td style='text-align:left;width: 40%'>
										     ${fn:substringBefore(map.value, ',')}
										</td>
									</tr>
								</table>
						   </c:if>  
						    <c:if test="${fn:substringAfter(map.value,',')!=''}">
							        <table style='width:100% ;border:none;'>
								        <tr>
									        <td style='text-align: right;width: 40%'>
				                                ${fn:substringBefore(map.value,",")}
											</td>
											<td style='text-align: center;width: 20%'>,
											</td>
											<td style='text-align:left;width: 40%'>
											    ${fn:substringAfter(map.value,",")}
											</td>
										</tr>
									</table>
						   </c:if></td>   
					   </c:otherwise>  
					</c:choose>  
                </c:forEach></tr></c:forEach>
           </table>
           <div class="clear panel" style="margin-top: 60px;">
              <div class="paragraph ">2.累积非父排除率计算结果：</div>
              <div class="paragraph ">本检测系统的累积非父排除率达0.9999以上。</div>
              <div class="panel_title">五、分析说明</div>
              <c:forEach items="${type.results}" var="result" varStatus="status">
              <div class="paragraph ">上述所检基因座检测结果显示，${result.compareResultModel.parent1}和${result.compareResultModel.child}两者的遗传关系符合孟德尔遗传规律，其累计亲权指数为<c:if test="${result.pi_num==0}">${result.pi}</c:if><c:if test="${result.pi_num!=0}">${result.pi_str}×10<sup>${result.pi_num}</sup></c:if>。</div>
              </c:forEach>
           </div>
     </div>    
     <div class="page">
         <div class="right pageno">
                   共&ensp;5&ensp;页 第&ensp;4&ensp;页
         </div>
         <hr/>
        <div class="clear panel">
             <div class="panel_title">六、鉴定意见</div>
             <div class="paragraph ">依据DNA分析结果，在不考虑同卵多胞胎、近亲和其他外界因素干扰的前提下，支持${type.sampleModel.father_info.sample_username}与<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><c:choose><c:when test="${status.last}">${child.sample_username}</c:when><c:otherwise>${child.sample_username}、</c:otherwise></c:choose></c:forEach>之间存在亲生血缘关系。</div>
             <div class="panel_title">七、落款</div>
             <c:forEach items="${pers}" var="per" varStatus="status" >
					   <div class="ending_name clear">司法鉴定人：${per.identify_per}</div>
                      <div class="ending_code right clear">《司法鉴定人执业证》证号：${per.identify_code}</div>
			</c:forEach>
             <div class="ending_time clear right" style="margin-top: 150px;">${caseModel.close_time_china}</div>
             <div class="paragraph_2 clear" style="margin-top: 92%;">附件：照片共1张</div>
         </div>
     </div>
     <div class="page">
         <div class="right pageno">
                     共&ensp;5&ensp;页 第&ensp;5&ensp;页
         </div>
         <hr/>
         <div class="ending_code">被鉴定人照片如下：</div>
         <img  width="90%" height="90%" src="<%=basePath%>judicial/attachment/getImg.do?filename=${type.attachmentModel.attachment_path}"  class="file" align="middle"/>
     </div> 
     </c:if>
     
    <c:if test="${type.result_type=='passed_2_2' }">
     <div class="page">
          <div class="right pageno">
                     共&ensp;5&ensp;页 第&ensp;1&ensp;页
          </div>
          <hr/>
          <div class="title">安徽龙图司法鉴定中心亲子鉴定意见书</div>
          <div class="title_remark right">
          <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		 皖龙图司鉴[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]
          法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		 皖龙图司鉴[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]
          法物鉴字第${caseModel.case_code}号 
			  </c:if>
			 </div>
          <div class="clear panel">
          <div class="panel_title">一、基本情况</div>
          <div class="spanel"><span class="p_title">委托人：</span>${caseModel.client}</div>
          <div class="spanel"><span class="p_title">委托鉴定事项：</span>为明确${type.sampleModel.father_info.sample_username}与<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><c:choose><c:when test="${status.last}">${child.sample_username}</c:when><c:otherwise>${child.sample_username}、</c:otherwise></c:choose></c:forEach>之间是否存在亲生血缘关系，申请进行DNA亲子鉴定</div>
          <div class="spanel"><span class="p_title">受理日期：</span>
          <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		 ${caseModel.accept_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
          ${caseModel.addextnew_time}
			  </c:if>
			  </div>
          <div class="spanel"><span class="p_title left">鉴定材料：</span>
          <div class="">标注为“${type.sampleModel.father_info.sample_username}”的血样(编号为${type.sampleModel.father_info.sample_code})<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status">、标注为“${child.sample_username}”的血样(编号为${child.sample_code})</c:forEach></div></div>
          <div class="spanel"><span class="p_title">鉴定日期：</span>
          
          <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		${caseModel.accept_time}----${caseModel.close_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		${caseModel.addextnew_time}----${caseModel.close_time}
			  </c:if>
			  </div>
          <div class="spanel"><span class="p_title">鉴定地点：</span>安徽龙图司法鉴定中心</div>
          <div class="spanel"><span class="p_title">在场人员：</span><c:forEach items="${pers}" var="per" varStatus="status" ><c:if test="${status.index==0}">${per.identify_per}、</c:if><c:if test="${status.index>0}">${per.identify_per}</c:if></c:forEach></div>
          <div class="spanel"><span class="p_title">被鉴定人：</span>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}<c:if test="${type.sampleModel.father_info.id_number==''||type.sampleModel.father_info.id_number==null}"><c:if test="${type.sampleModel.father_info.birth_date!=''&&type.sampleModel.father_info.birth_date!=null}">，${type.sampleModel.father_info.birth_date}</c:if></c:if><c:if test="${type.sampleModel.father_info.id_number!=''&&type.sampleModel.father_info.id_number!=null }">，身份证号：${type.sampleModel.father_info.id_number}</c:if></div>
          <c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status">
          <div class="spanel">
                   孩子${status.count}：据委托人告知姓名为${child.sample_username}（<c:if test="${child.sample_callname=='儿子'}">男</c:if><c:if test="${child.sample_callname=='女儿'}">女</c:if><c:if test="${child.birth_date_format!=null&&child.birth_date_format!=''}">、${child.birth_date_format}生</c:if>）
          </div>
          </c:forEach>
          <div class="panel_title">二、检案摘要</div>
	      <div class="paragraph ">
	      
	      <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		  ${caseModel.accept_time}，
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		  ${caseModel.addextnew_time}，
			  </c:if>
			 因办理户口需要，要求对${type.sampleModel.father_info.sample_username}与<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><c:choose><c:when test="${status.last}">${child.sample_username}</c:when><c:otherwise>${child.sample_username}、</c:otherwise></c:choose></c:forEach>之间是否存在亲生血缘关系进行DNA亲子鉴定。</div>
	      <div class="panel_title">三、检验过程</div>
	      <div class="paragraph_2"><span class="p_title">（一）检验方法</span></div>
       </div>
     </div>
     <div class="page">
         <div class="right pageno">
                    共&ensp;5&ensp;页 第&ensp;2&ensp;页
         </div>
         <hr/>
          <div class="clear panel">
		   <div class="paragraph ">检材DNA提取和基因分型检测按照中华人民共和国行业标准《法庭科学DNA实验室检验规范》（GA/T&ensp;383-2014）、《法庭科学DNA亲子鉴定规范》(GA/T&ensp;965-2011)和司法鉴定技术规范《亲权鉴定技术规范》(SF/Z&ensp;JD0105001-2010)进行。</div>
           <div class="paragraph_2"><span class="p_title">（二）DNA检验步骤</span></div>
           <div class="paragraph">1.PCR扩增：采用无锡中德美联生物技术有限公司AGCU&ensp;Expressmarker22荧光检测试剂盒，用&ensp;9700&ensp;型PCR扩增仪（美国Applied&ensp;Biosystems公司）对上述检材进行扩增。</div>
           <div class="paragraph">2.检测：扩增结果采用ABI3130遗传分析仪进行结果分析。</div>
           <div class="panel_title">四、检验结果</div>
           <div class="paragraph">1.基因分型结果表</div>
           <table class="table" cellSpacing="0" border="solid" cellPadding="0" style="line-height: 64px;">
              <tr style="height: 86px;"><th width="16%" >基因座</th><th width="21%"><div>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}</div><div class="no_margin">${type.sampleModel.father_info.sample_code}</div></th><c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><th width="21%"><div>孩子：${child.sample_username}</div><div class="no_margin">${child.sample_code}</div></th></c:forEach></tr>
              <c:forEach items="${type.sampleModel.sampleResults}" var="list" begin="0" end="8" step="1"><tr style="height: 64px;"><c:forEach items="${list}" var="map">
              <c:choose>  
					   <c:when test="${map.key=='name'}">
					       <td width=16% >${fn:replace(map.value, ' ', '&ensp;')}</td>
					   </c:when>  
					   <c:otherwise>  
					   	 <td width=21% ><c:if test="${fn:substringAfter(map.value,',')==''}">
						        <table style='width:100% ;border:none;'>
							        <tr>
								        <td style='text-align: right;width: 40%'>
			                                 ${fn:substringBefore(map.value,",")}
										</td>
										<td style='text-align: center;width: 20%'>,
										</td>
										<td style='text-align:left;width: 40%'>
										     ${fn:substringBefore(map.value, ',')}
										</td>
									</tr>
								</table>
						   </c:if>  
						    <c:if test="${fn:substringAfter(map.value,',')!=''}">
							        <table style='width:100% ;border:none;'>
								        <tr>
									        <td style='text-align: right;width: 40%'>
				                                ${fn:substringBefore(map.value,",")}
											</td>
											<td style='text-align: center;width: 20%'>,
											</td>
											<td style='text-align:left;width: 40%'>
											    ${fn:substringAfter(map.value,",")}
											</td>
										</tr>
									</table>
						   </c:if></td>   
					   </c:otherwise>  
					</c:choose>  
                </c:forEach></tr></c:forEach>
           </table>
         </div>
     </div>
      <div class="page">
         <div class="right pageno">
                   共&ensp;5&ensp;页 第&ensp;3&ensp;页
         </div>
         <hr/>
         <table class="table" cellSpacing="0" border="solid" cellPadding="0" style="line-height: 65px;">
              <tr style="height: 86px;"><th width="16%" >基因座</th><th width="21%"><div>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}</div><div class="no_margin">${type.sampleModel.father_info.sample_code}</div></th><c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><th width="21%"><div>孩子：${child.sample_username}</div><div class="no_margin">${child.sample_code}</div></th></c:forEach></tr>
              <c:forEach items="${type.sampleModel.sampleResults}" var="list" begin="9" step="1"><tr style="height: 65px;"><c:forEach items="${list}" var="map">
              <c:choose>  
					   <c:when test="${map.key=='name'}">
					       <td width=16% >${fn:replace(map.value, ' ', '&ensp;')}</td>
					   </c:when>  
					   <c:otherwise>  
					   	 <td width=21% ><c:if test="${fn:substringAfter(map.value,',')==''}">
						        <table style='width:100% ;border:none;'>
							        <tr>
								        <td style='text-align: right;width: 40%'>
			                                 ${fn:substringBefore(map.value,",")}
										</td>
										<td style='text-align: center;width: 20%'>,
										</td>
										<td style='text-align:left;width: 40%'>
										     ${fn:substringBefore(map.value, ',')}
										</td>
									</tr>
								</table>
						   </c:if>  
						    <c:if test="${fn:substringAfter(map.value,',')!=''}">
							        <table style='width:100% ;border:none;'>
								        <tr>
									        <td style='text-align: right;width: 40%'>
				                                ${fn:substringBefore(map.value,",")}
											</td>
											<td style='text-align: center;width: 20%'>,
											</td>
											<td style='text-align:left;width: 40%'>
											    ${fn:substringAfter(map.value,",")}
											</td>
										</tr>
									</table>
						   </c:if></td>   
					   </c:otherwise>  
					</c:choose>  
                </c:forEach></tr></c:forEach>
           </table>
           <div class="clear panel" style="margin-top: 60px;">
              <div class="paragraph ">2.累积非父排除率计算结果：</div>
              <div class="paragraph ">本检测系统的累积非父排除率达0.9999以上。</div>
              <div class="panel_title">五、分析说明</div>
              <c:forEach items="${type.results}" var="result" varStatus="status">
              <div class="paragraph ">上述所检基因座检测结果显示，${result.compareResultModel.parent1}和${result.compareResultModel.child}两者的遗传关系符合孟德尔遗传规律，其累计亲权指数为<c:if test="${result.pi_num==0}">${result.pi}</c:if><c:if test="${result.pi_num!=0}">${result.pi_str}×10<sup>${result.pi_num}</sup></c:if>。</div>
              </c:forEach>
           </div>
     </div>    
     <div class="page">
         <div class="right pageno">
                   共&ensp;5&ensp;页 第&ensp;4&ensp;页
         </div>
         <hr/>
        <div class="clear panel">
             <div class="panel_title">六、鉴定意见</div>
             <div class="paragraph ">依据DNA分析结果，在不考虑同卵多胞胎、近亲和其他外界因素干扰的前提下，支持${type.sampleModel.father_info.sample_username}与<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><c:choose><c:when test="${status.last}">${child.sample_username}</c:when><c:otherwise>${child.sample_username}、</c:otherwise></c:choose></c:forEach>之间存在亲生血缘关系。</div>
             <div class="panel_title">七、落款</div>
             <c:forEach items="${pers}" var="per" varStatus="status" >
					   <div class="ending_name clear">司法鉴定人：${per.identify_per}</div>
                      <div class="ending_code right clear">《司法鉴定人执业证》证号：${per.identify_code}</div>
			</c:forEach>
             <div class="ending_time clear right" style="margin-top: 150px;">${caseModel.close_time_china}</div>
             <div class="paragraph_2 clear" style="margin-top: 92%;">附件：照片共1张</div>
         </div>
     </div>
     <div class="page">
         <div class="right pageno">
                     共&ensp;5&ensp;页 第&ensp;5&ensp;页
         </div>
         <hr/>
         <div class="ending_code">被鉴定人照片如下：</div>
         <img  width="90%" height="90%" src="<%=basePath%>judicial/attachment/getImg.do?filename=${type.attachmentModel.attachment_path}"  class="file" align="middle"/>
     </div> 
     </c:if>
     
     <c:if test="${type.result_type=='passed_3_2' }">
        <div class="page">
          <div class="right pageno">
                     共&ensp;4&ensp;页 第&ensp;1&ensp;页
          </div>
          <hr/>
          <div class="title">安徽龙图司法鉴定中心亲子鉴定意见书</div>
          <div class="title_remark right">
          <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		 皖龙图司鉴[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]
          法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		 皖龙图司鉴[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]
          法物鉴字第${caseModel.case_code}号 
			  </c:if>
			 </div>
          <div class="clear panel">
          <div class="panel_title">一、基本情况</div>
          <div class="spanel"><span class="p_title">委托人：</span>${caseModel.client}</div>
          <div class="spanel"><span class="p_title">委托鉴定事项：</span>为明确${type.sampleModel.father_info.sample_username}、${type.sampleModel.mother_info.sample_username}与<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><c:choose><c:when test="${status.last}">${child.sample_username}</c:when><c:otherwise>${child.sample_username}、</c:otherwise></c:choose></c:forEach>之间是否存在亲生血缘关系，申请进行DNA亲子鉴定</div>
          <div class="spanel"><span class="p_title">受理日期：</span>
          <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		 ${caseModel.accept_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		${caseModel.addextnew_time}
			  </c:if>
			  </div>
          <div class="spanel"><span class="p_title left">鉴定材料：</span>
          <div class="">标注为“${type.sampleModel.father_info.sample_username}”的血样(编号为${type.sampleModel.father_info.sample_code})、标注为“${type.sampleModel.mother_info.sample_username}”的血样(编号为${type.sampleModel.mother_info.sample_code})<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status">、标注为“${child.sample_username}”的血样(编号为${child.sample_code})</c:forEach></div></div>
          <div class="spanel"><span class="p_title">鉴定日期：</span>
          <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		${caseModel.accept_time}----${caseModel.close_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		${caseModel.addextnew_time}----${caseModel.close_time}
			  </c:if>
			  </div>
          <div class="spanel"><span class="p_title">鉴定地点：</span>安徽龙图司法鉴定中心</div>
          <div class="spanel"><span class="p_title">在场人员：</span><c:forEach items="${pers}" var="per" varStatus="status" ><c:if test="${status.index==0}">${per.identify_per}、</c:if><c:if test="${status.index>0}">${per.identify_per}</c:if></c:forEach></div>
          <div class="spanel"><span class="p_title">被鉴定人：</span>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}<c:if test="${type.sampleModel.father_info.id_number==''||type.sampleModel.father_info.id_number==null}"><c:if test="${type.sampleModel.father_info.birth_date!=''&&type.sampleModel.father_info.birth_date!=null}">，${type.sampleModel.father_info.birth_date}</c:if></c:if><c:if test="${type.sampleModel.father_info.id_number!=''&&type.sampleModel.father_info.id_number!=null }">，身份证号：${type.sampleModel.father_info.id_number}</c:if></div>
          <div class="spanel">${type.sampleModel.mother_info.sample_callname}：${type.sampleModel.mother_info.sample_username}<c:if test="${type.sampleModel.mother_info.id_number!=''&&type.sampleModel.mother_info.id_number!=null }">，身份证号：${type.sampleModel.mother_info.id_number}</c:if></div>
          <c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status">
          <div class="spanel">
                   孩子${status.count}：据委托人告知姓名为${child.sample_username}（<c:if test="${child.sample_callname=='儿子'}">男</c:if><c:if test="${child.sample_callname=='女儿'}">女</c:if><c:if test="${child.birth_date_format!=null&&child.birth_date_format!=''}">、${child.birth_date_format}生</c:if>）
          </div>
          </c:forEach>
          <div class="panel_title">二、检案摘要</div>
	      <div class="paragraph ">
	      <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		${caseModel.accept_time}，
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		 ${caseModel.addextnew_time}，
			  </c:if>
			  因办理户口需要，当事人${type.sampleModel.father_info.sample_username}携${type.sampleModel.mother_info.sample_username}、<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><c:choose><c:when test="${status.last}">${child.sample_username}</c:when><c:otherwise>${child.sample_username}、</c:otherwise></c:choose></c:forEach>来本中心，要求对${type.sampleModel.father_info.sample_username}、${type.sampleModel.mother_info.sample_username}与<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><c:choose><c:when test="${status.last}">${child.sample_username}</c:when><c:otherwise>${child.sample_username}、</c:otherwise></c:choose></c:forEach>之间是否存在亲生血缘关系进行DNA亲子鉴定。</div>
       </div>
     </div>
     <div class="page">
         <div class="right pageno">
                    共&ensp;4&ensp;页 第&ensp;2&ensp;页
         </div>
         <hr/>
          <div class="panel_title">三、检验过程</div>
          <div class="clear panel">
           <div class="paragraph_2"><span class="p_title">（一）检验方法</span></div>
		   <div class="paragraph ">检材DNA提取和基因分型检测按照中华人民共和国行业标准《法庭科学DNA实验室检验规范》（GA/T&ensp;383-2014）、《法庭科学DNA亲子鉴定规范》(GA/T&ensp;965-2011)和司法鉴定技术规范《亲权鉴定技术规范》(SF/Z&ensp;JD0105001-2010)进行。</div>
           <div class="paragraph_2"><span class="p_title">（二）DNA检验步骤</span></div>
           <div class="paragraph">1.PCR扩增：采用无锡中德美联生物技术有限公司AGCU&ensp;Expressmarker22荧光检测试剂盒，用&ensp;9700&ensp;型PCR扩增仪（美国Applied&ensp;Biosystems公司）对上述检材进行扩增。</div>
           <div class="paragraph">2.检测：扩增结果采用ABI3130遗传分析仪进行结果分析。</div>
           <div class="panel_title">四、检验结果</div>
           <div class="paragraph">1.基因分型结果表</div>
           <table class="table" cellSpacing="0" border="solid" cellPadding="0" style="line-height: 27px;">
              <tr style="height: 40px;"><th width="16%" >基因座</th><th width="21%"><div>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}</div><div class="no_margin">${type.sampleModel.father_info.sample_code}</div></th><th width="21%"><div>${type.sampleModel.mother_info.sample_callname}：${type.sampleModel.mother_info.sample_username}</div><div class="no_margin">${type.sampleModel.mother_info.sample_code}</div></th><c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><th width="21%"><div>孩子：${child.sample_username}</div><div class="no_margin">${child.sample_code}</div></th></c:forEach></tr>
              <c:forEach items="${type.sampleModel.sampleResults}" var="list" begin="0" step="1" end="16"><tr style="height: 27px;"><c:forEach items="${list}" var="map">
              <c:choose>  
					   <c:when test="${map.key=='name'}">
					       <td width=16% >${fn:replace(map.value, ' ', '&ensp;')}</td>
					   </c:when>  
					   <c:otherwise>  
					   	 <td width=21% ><c:if test="${fn:substringAfter(map.value,',')==''}">
						        <table style='width:100% ;border:none;'>
							        <tr>
								        <td style='text-align: right;width: 40%'>
			                                 ${fn:substringBefore(map.value,",")}
										</td>
										<td style='text-align: center;width: 20%'>,
										</td>
										<td style='text-align:left;width: 40%'>
										     ${fn:substringBefore(map.value, ',')}
										</td>
									</tr>
								</table>
						   </c:if>  
						    <c:if test="${fn:substringAfter(map.value,',')!=''}">
							        <table style='width:100% ;border:none;'>
								        <tr>
									        <td style='text-align: right;width: 40%'>
				                                ${fn:substringBefore(map.value,",")}
											</td>
											<td style='text-align: center;width: 20%'>,
											</td>
											<td style='text-align:left;width: 40%'>
											    ${fn:substringAfter(map.value,",")}
											</td>
										</tr>
									</table>
						   </c:if></td>   
					   </c:otherwise>  
					</c:choose>  
                </c:forEach></tr></c:forEach>
           </table>
         </div>
     </div>
      <div class="page">
         <div class="right pageno">
                   共&ensp;4&ensp;页 第&ensp;3&ensp;页
         </div>
         <hr/>
         <table class="table" cellSpacing="0" border="solid" cellPadding="0" style="line-height: 27px;">
              <tr style="height: 40px;"><th width="16%" >基因座</th><th width="21%"><div>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}</div><div class="no_margin">${type.sampleModel.mother_info.sample_code}</div></th><th width="21%"><div>${type.sampleModel.mother_info.sample_callname}：${type.sampleModel.mother_info.sample_username}</div><div class="no_margin">${type.sampleModel.mother_info.sample_code}</div></th><c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><th width="21%"><div>孩子：${child.sample_username}</div><div class="no_margin">${child.sample_code}</div></th></c:forEach></tr>
              <c:forEach items="${type.sampleModel.sampleResults}" var="list" begin="17" step="1"><tr style="height: 27px;"><c:forEach items="${list}" var="map">
              <c:choose>  
					   <c:when test="${map.key=='name'}">
					       <td width=16% >${fn:replace(map.value, ' ', '&ensp;')}</td>
					   </c:when>  
					   <c:otherwise>  
					   	 <td width=21% ><c:if test="${fn:substringAfter(map.value,',')==''}">
						        <table style='width:100% ;border:none;'>
							        <tr>
								        <td style='text-align: right;width: 40%'>
			                                 ${fn:substringBefore(map.value,",")}
										</td>
										<td style='text-align: center;width: 20%'>,
										</td>
										<td style='text-align:left;width: 40%'>
										     ${fn:substringBefore(map.value, ',')}
										</td>
									</tr>
								</table>
						   </c:if>  
						    <c:if test="${fn:substringAfter(map.value,',')!=''}">
							        <table style='width:100% ;border:none;'>
								        <tr>
									        <td style='text-align: right;width: 40%'>
				                                ${fn:substringBefore(map.value,",")}
											</td>
											<td style='text-align: center;width: 20%'>,
											</td>
											<td style='text-align:left;width: 40%'>
											    ${fn:substringAfter(map.value,",")}
											</td>
										</tr>
									</table>
						   </c:if></td>   
					   </c:otherwise>  
					</c:choose>  
                </c:forEach></tr></c:forEach>
           </table>
           <div class="clear panel">
              <div class="paragraph ">2.累积非父排除率计算结果：</div>
              <div class="paragraph ">本检测系统的累积非父排除率达0.9999以上。</div>
              <div class="panel_title">五、分析说明</div>
              <c:forEach items="${type.results}" var="result" varStatus="status">
              <div class="paragraph ">上述所检基因座检测结果显示，${result.compareResultModel.parent1}、${result.compareResultModel.parent2}与${result.compareResultModel.child}三者的遗传关系符合孟德尔遗传规律，其累计亲权指数为<c:if test="${result.pi_num==0}">${result.pi}</c:if><c:if test="${result.pi_num!=0}">${result.pi_str}×10<sup>${result.pi_num}</sup></c:if>。</div>
              </c:forEach>
              <div class="panel_title">六、鉴定意见</div>
              <div class="paragraph ">依据DNA分析结果，在不考虑同卵多胞胎、近亲和其他外界因素干扰的前提下，支持${type.sampleModel.father_info.sample_username}、${type.sampleModel.mother_info.sample_username}与<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><c:choose><c:when test="${status.last}">${child.sample_username}</c:when><c:otherwise>${child.sample_username}、</c:otherwise></c:choose></c:forEach>之间存在亲生血缘关系。</div>
              <div class="panel_title">七、落款</div>
              <c:forEach items="${pers}" var="per" varStatus="status" >
					   <div class="ending_name clear">司法鉴定人：${per.identify_per}</div>
                      <div class="ending_code right clear">《司法鉴定人执业证》证号：${per.identify_code}</div>
			</c:forEach>
              <div class="ending_time clear right" style="margin-top: 50px;">${caseModel.close_time_china}</div>
              <div class="paragraph_2 clear" style="margin-top: 10%;">附件：照片共1张</div>
           </div>
     </div>    
     <div class="page">
         <div class="right pageno">
                     共&ensp;4&ensp;页 第&ensp;4&ensp;页
         </div>
         <hr/>
         <div class="ending_code">被鉴定人照片如下：</div>
         <img  width="90%" height="90%" src="<%=basePath%>judicial/attachment/getImg.do?filename=${type.attachmentModel.attachment_path}"  class="file" align="middle"/>
     </div> 
     </c:if>
     
     <c:if test="${type.result_type=='passed_3' }">
          <div class="page">
          <div class="right pageno">
                     共&ensp;4&ensp;页 第&ensp;1&ensp;页
          </div>
          <hr/>
          <div class="title">安徽龙图司法鉴定中心亲子鉴定意见书</div>
          <div class="title_remark right">
          <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		 皖龙图司鉴[<c:out value="${fn:substring(caseModel.accept_time, 0, 4)}" />]
          法物鉴字第${caseModel.case_code}号
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		 皖龙图司鉴[<c:out value="${fn:substring(caseModel.addextnew_time, 0, 4)}" />]
          法物鉴字第${caseModel.case_code}号 
			  </c:if>
			 </div>
          <div class="clear panel">
          <div class="panel_title">一、基本情况</div>
          <div class="spanel"><span class="p_title">委托人：</span>${caseModel.client}</div>
          <div class="spanel"><span class="p_title">委托鉴定事项：</span>为明确${type.sampleModel.father_info.sample_username}、${type.sampleModel.mother_info.sample_username}与<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><c:choose><c:when test="${status.last}">${child.sample_username}</c:when><c:otherwise>${child.sample_username}、</c:otherwise></c:choose></c:forEach>之间是否存在亲生血缘关系，申请进行DNA亲子鉴定</div>
          <div class="spanel"><span class="p_title">受理日期：</span>
          <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		${caseModel.accept_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		${caseModel.addextnew_time}
			  </c:if>
			</div>
          <div class="spanel"><span class="p_title left">鉴定材料：</span>
          <div class="">标注为“${type.sampleModel.father_info.sample_username}”的血样(编号为${type.sampleModel.father_info.sample_code})、标注为“${type.sampleModel.mother_info.sample_username}”的血样(编号为${type.sampleModel.mother_info.sample_code})<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status">、标注为“${child.sample_username}”的血样(编号为${child.sample_code})</c:forEach></div></div>
          <div class="spanel"><span class="p_title">鉴定日期：</span>
          <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		 ${caseModel.accept_time}----${caseModel.close_time}
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		${caseModel.addextnew_time}----${caseModel.close_time}
			  </c:if>
			  </div>
          <div class="spanel"><span class="p_title">鉴定地点：</span>安徽龙图司法鉴定中心</div>
          <div class="spanel"><span class="p_title">在场人员：</span><c:forEach items="${pers}" var="per" varStatus="status" ><c:if test="${status.index==0}">${per.identify_per}、</c:if><c:if test="${status.index>0}">${per.identify_per}</c:if></c:forEach></div>
          <div class="spanel"><span class="p_title">被鉴定人：</span>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}<c:if test="${type.sampleModel.father_info.id_number==''||type.sampleModel.father_info.id_number==null}"><c:if test="${type.sampleModel.father_info.birth_date!=''&&type.sampleModel.father_info.birth_date!=null}">，${type.sampleModel.father_info.birth_date}</c:if></c:if><c:if test="${type.sampleModel.father_info.id_number!=''&&type.sampleModel.father_info.id_number!=null }">，身份证号：${type.sampleModel.father_info.id_number}</c:if></div>
          <div class="spanel">${type.sampleModel.mother_info.sample_callname}：${type.sampleModel.mother_info.sample_username}<c:if test="${type.sampleModel.mother_info.id_number!=''&&type.sampleModel.mother_info.id_number!=null }">，身份证号：${type.sampleModel.mother_info.id_number}</c:if></div>
          <div class="spanel">
                   孩子：据委托人告知姓名为${type.sampleModel.child_info.sample_username}（<c:if test="${type.sampleModel.child_info.sample_callname=='儿子'}">男</c:if><c:if test="${type.sampleModel.child_info.sample_callname=='女儿'}">女</c:if><c:if test="${type.sampleModel.child_info.birth_date_format!=null&&type.sampleModel.child_info.birth_date_format!=''}">、${type.sampleModel.child_info.birth_date_format}生</c:if>）
          </div>
          <div class="panel_title">二、检案摘要</div>
	      <div class="paragraph ">
	      <c:if test="${not empty caseModel.accept_time|| caseModel.accept_time!=''}">
		${caseModel.accept_time}，
		      </c:if>
		      <c:if test="${empty caseModel.accept_time|| caseModel.accept_time =='' }"> 
		${caseModel.addextnew_time}，
			  </c:if>
			  因办理户口需要，当事人${type.sampleModel.father_info.sample_username}携${type.sampleModel.mother_info.sample_username}、${type.sampleModel.child_info.sample_username}来本中心，要求对${type.sampleModel.father_info.sample_username}、${type.sampleModel.mother_info.sample_username}与<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><c:choose><c:when test="${status.last}">${child.sample_username}</c:when><c:otherwise>${child.sample_username}、</c:otherwise></c:choose></c:forEach>之间是否存在亲生血缘关系进行DNA亲子鉴定。</div>
	      <div class="panel_title">三、检验过程</div>
	      <div class="paragraph_2"><span class="p_title">（一）检验方法</span></div>
       </div>
     </div>
     <div class="page">
         <div class="right pageno">
                    共&ensp;4&ensp;页 第&ensp;2&ensp;页
         </div>
         <hr/>
          <div class="clear panel">
		   <div class="paragraph ">检材DNA提取和基因分型检测按照中华人民共和国行业标准《法庭科学DNA实验室检验规范》（GA/T&ensp;383-2014）、《法庭科学DNA亲子鉴定规范》(GA/T&ensp;965-2011)和司法鉴定技术规范《亲权鉴定技术规范》(SF/Z&ensp;JD0105001-2010)进行。</div>
           <div class="paragraph_2"><span class="p_title">（二）DNA检验步骤</span></div>
           <div class="paragraph">1.PCR扩增：采用无锡中德美联生物技术有限公司AGCU&ensp;Expressmarker22荧光检测试剂盒，用&ensp;9700&ensp;型PCR扩增仪（美国Applied&ensp;Biosystems公司）对上述检材进行扩增。</div>
           <div class="paragraph">2.检测：扩增结果采用ABI3130遗传分析仪进行结果分析。</div>
           <div class="panel_title">四、检验结果</div>
           <div class="paragraph">1.基因分型结果表</div>
           <table class="table" cellSpacing="0" border="solid" cellPadding="0" style="line-height: 27px;">
              <tr style="height: 40px;"><th width="16%" >基因座</th><th width="21%"><div>${type.sampleModel.father_info.sample_callname}：${type.sampleModel.father_info.sample_username}</div><div class="no_margin">${type.sampleModel.mother_info.sample_code}</div></th><th width="21%"><div>${type.sampleModel.mother_info.sample_callname}：${type.sampleModel.mother_info.sample_username}</div><div class="no_margin">${type.sampleModel.mother_info.sample_code}</div></th><c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><th width="21%"><div>孩子：${child.sample_username}</div><div class="no_margin">${child.sample_code}</div></th></c:forEach></tr>
              <c:forEach items="${type.sampleModel.sampleResults}" var="list" begin="0" step="1"><tr style="height: 27px;"><c:forEach items="${list}" var="map">
              <c:choose>  
					   <c:when test="${map.key=='name'}">
					       <td width=16% >${fn:replace(map.value, ' ', '&ensp;')}</td>
					   </c:when>  
					   <c:otherwise>  
					   	 <td width=21% ><c:if test="${fn:substringAfter(map.value,',')==''}">
						        <table style='width:100% ;border:none;'>
							        <tr>
								        <td style='text-align: right;width: 40%'>
			                                 ${fn:substringBefore(map.value,",")}
										</td>
										<td style='text-align: center;width: 20%'>,
										</td>
										<td style='text-align:left;width: 40%'>
										     ${fn:substringBefore(map.value, ',')}
										</td>
									</tr>
								</table>
						   </c:if>  
						    <c:if test="${fn:substringAfter(map.value,',')!=''}">
							        <table style='width:100% ;border:none;'>
								        <tr>
									        <td style='text-align: right;width: 40%'>
				                                ${fn:substringBefore(map.value,",")}
											</td>
											<td style='text-align: center;width: 20%'>,
											</td>
											<td style='text-align:left;width: 40%'>
											    ${fn:substringAfter(map.value,",")}
											</td>
										</tr>
									</table>
						   </c:if></td>   
					   </c:otherwise>  
					</c:choose>  
                </c:forEach></tr></c:forEach>
           </table>
         </div>
     </div>
      <div class="page">
         <div class="right pageno">
                   共&ensp;4&ensp;页 第&ensp;3&ensp;页
         </div>
         <hr/>
           <div class="clear panel">
              <div class="paragraph ">2.累积非父排除率计算结果：</div>
              <div class="paragraph ">本检测系统的累积非父排除率达0.9999以上。</div>
              <div class="panel_title">五、分析说明</div>
              <c:forEach items="${type.results}" var="result" varStatus="status">
              <div class="paragraph ">上述所检基因座检测结果显示，${result.compareResultModel.parent1}、${result.compareResultModel.parent2}与${result.compareResultModel.child}三者的遗传关系符合孟德尔遗传规律，其累计亲权指数为<c:if test="${result.pi_num==0}">${result.pi}</c:if><c:if test="${result.pi_num!=0}">${result.pi_str}×10<sup>${result.pi_num}</sup></c:if>。</div>
              </c:forEach>
              <div class="panel_title">六、鉴定意见</div>
              <div class="paragraph ">依据DNA分析结果，在不考虑同卵多胞胎、近亲和其他外界因素干扰的前提下，支持${type.sampleModel.father_info.sample_username}、${type.sampleModel.mother_info.sample_username}与<c:forEach items="${type.sampleModel.childs_info}" var="child" varStatus="status"><c:choose><c:when test="${status.last}">${child.sample_username}</c:when><c:otherwise>${child.sample_username}、</c:otherwise></c:choose></c:forEach>之间存在亲生血缘关系。</div>
              <div class="panel_title">七、落款</div>
              <c:forEach items="${pers}" var="per" varStatus="status" >
					   <div class="ending_name clear">司法鉴定人：${per.identify_per}</div>
                      <div class="ending_code right clear">《司法鉴定人执业证》证号：${per.identify_code}</div>
			</c:forEach>
              <div class="ending_time clear right" style="margin-top: 150px;">${caseModel.close_time_china}</div>
              <div class="paragraph_2 clear" style="margin-top: 65%;">附件：照片共1张</div>
           </div>
     </div>    
     <div class="page">
         <div class="right pageno">
                     共&ensp;4&ensp;页 第&ensp;4&ensp;页
         </div>
         <hr/>
         <div class="ending_code">被鉴定人照片如下：</div>
         <img  width="90%" height="90%" src="<%=basePath%>judicial/attachment/getImg.do?filename=${type.attachmentModel.attachment_path}"  class="file" align="middle"/>
     </div> 
     </c:if>
   </c:forEach>
</body>
</html>
