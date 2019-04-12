<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
  <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
.panel{
	font-size:28px;
}
.panel_table th{
	 height:80px; 
	  width:33.3%;
}
.panel_table td{
	 height:25px; 
     width:33.3%;
	 border-bottom:none;
	 border-top:none;
}
.table_thead th{
	  border-bottom:solid;
	 border-top:solid;
}
.page{
	page-break-after: always;
	line-height:30px;
	margin-top:50px;
}
tr{
	line-height:30px;
}
</style>
</head>
<body style="width: 100%;">
           <c:forEach items="${printtables}" var="type" >
		         <c:if test="${type.model_type=='passed_2'}">
		         <div class="page">
		                   <div class="panel font_bold" style="text-align:center;">${type.case_code}</div>
					                 <table cellspacing="0" cellpadding="0" style="text-align:center;width: 97%;font-size:28px;">
					                      <thead class="table_thead">
					                           <tr><th>STR基因座</th>
					                           <th>${type.sample_code1}</th>
					                           <th>${type.sample_code3}</th>
					                           <th>生父基因1</th>
					                           <th>生父基因2</th>
					                           <th>计算公式</th>
					                           <th>PI</th>
					                           </tr>
					                      </thead>
					                      <tbody>
					                      <c:forEach items="${type.piInfoModels}" var="pi">
					                       <tr><td style="border-right:solid">${pi.param_type}</td>
					                           <td  style="border-right:solid">
					                           <c:if test="${fn:substringAfter(pi.parent,',')==''}">
											        <table style='width:100% ;border:none;'>
												        <tr>
													        <td style='text-align: right;width:33.3%'>
								                                 ${fn:substringBefore(pi.parent,",")}
															</td>
															<td style='text-align: center;width:33.3%'>,
															</td>
															<td style='text-align:left;width:33.3%'>
															     ${fn:substringBefore(pi.parent, ',')}
															</td>
														</tr>
													</table>
											   </c:if>  
											    <c:if test="${fn:substringAfter(pi.parent,',')!=''}">
												        <table style='width:100% ;border:none;'>
													        <tr>
														        <td style='text-align: right;width:33.3%'>
									                                ${fn:substringBefore(pi.parent,",")}
																</td>
																<td style='text-align: center;width:33.3%'>,
																</td>
																<td style='text-align:left;width:33.3%'>
																    ${fn:substringAfter(pi.parent,",")}
																</td>
															</tr>
														</table>
											   </c:if>
											   </td>
					                           <td style="border-right:solid">
					                           <c:if test="${fn:substringAfter(pi.child,',')==''}">
											        <table style='width:100% ;border:none;'>
												        <tr>
													        <td style='text-align: right;width:33.3%'>
								                                 ${fn:substringBefore(pi.child,",")}
															</td>
															<td style='text-align: center;width:33.3%'>,
															</td>
															<td style='text-align:left;width:33.3%'>
															     ${fn:substringBefore(pi.child, ',')}
															</td>
														</tr>
													</table>
											   </c:if>  
											    <c:if test="${fn:substringAfter(pi.child,',')!=''}">
												        <table style='width:100% ;border:none;'>
													        <tr>
														        <td style='text-align: right;width:33.3%'>
									                                ${fn:substringBefore(pi.child,",")}
																</td>
																<td style='text-align: center;width:33.3%'>,
																</td>
																<td style='text-align:left;width:33.3%'>
																    ${fn:substringAfter(pi.child,",")}
																</td>
															</tr>
														</table>
											   </c:if>
											   </td>
					                           <td>${pi.gene1}</td>
					                           <td><c:if test="${pi.gene2!='0'}">
					                                  ${pi.gene2}
					                           </c:if></td>
					                           <td>${pi.function}</td>
					                           <td>${pi.pi}</td>
					                           </tr>
					                        </c:forEach>   
					                      </tbody>
					                      <tfoot style="text-align:right;">
					                           <tr>
					                           <th colspan="5" style="border-top:solid;">累积PI</th>
					                           <th colspan="2"  style="border-top:solid;">${type.pi}</th>
					                           </tr>
					                            <tr>
					                               <th colspan="5"  style="border-bottom:solid">RCP</th>
					                               <th colspan="2"  style="border-bottom:solid">${type.rcp}</th>
					                           </tr>
					                      </tfoot>
					                 </table>
					 </div>
		         </c:if>
		   </c:forEach>      
</body>
</html>