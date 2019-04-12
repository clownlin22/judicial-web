package com.rds.judicial.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.code.utils.model.MsgModelUtil;
import com.rds.judicial.mapper.RdsJudicialCaseExceptionMapper;
import com.rds.judicial.mapper.RdsJudicialExperimentMapper;
import com.rds.judicial.mapper.RdsJudicialSubCaseMapper;
import com.rds.judicial.model.RdsJudicialCaseExceptionModel;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialRegisterService;
import com.rds.judicial.service.RdsJudicialSampleCompareService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;

import lombok.Setter;

import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.service.RdsJudicialSubCaseService;

import javax.servlet.http.HttpSession;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/23
 */
@RequestMapping("judicial/subCase")
@Controller
public class RdsJudicialSubCaseController {
    @Autowired
    private RdsJudicialSubCaseService rdsJudicialSubCaseService;

	@Autowired
	private RdsJudicialCaseExceptionMapper RdsJudicialCaseExceptionMapper;
	
    @Autowired
    protected RdsJudicialSubCaseMapper rdsJudicialSubCaseMapper;
    
    
    @Autowired
    protected RdsJudicialExperimentMapper rdsJudicialExperimentMapper;

    @Autowired
    private RdsActivitiJudicialService rdsActivitiJudicialService;
	
    @Autowired
    @Qualifier("RdsJudicialSampleCompareServiceImpl")
    private RdsJudicialSampleCompareService rdsJudicialSampleCompareService;

    @RequestMapping("queryAll")
    @ResponseBody
    public Map<String,Object> querySubCaseResult(@RequestBody Map<String,Object> params,HttpSession session){
        Map<String,Object> map = new HashMap<String, Object>();
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        map.put("data",rdsJudicialSubCaseService.queryAll(params));
        map.put("total",rdsJudicialSubCaseService.queryAllCount(params));
        return map;
    }

    @RequestMapping("queryAllCountForZhengTaiExt")
    @ResponseBody
    public Map<String,Object> queryAllCountForZhengTaiExt(@RequestBody Map<String,Object> params,HttpSession session){
        Map<String,Object> map = new HashMap<String, Object>();
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        map.put("data",rdsJudicialSubCaseService.queryAllForZhengTaiExt(params));
        map.put("total",rdsJudicialSubCaseService.queryAllCountForZhengTaiExt(params));
        return map;
    }

    @RequestMapping("updateResult")
    @ResponseBody
    public Object updateResult(@RequestBody Map<String,Object> params,HttpSession session){
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        if(rdsJudicialSubCaseService.update(params)>0){
            return MsgModelUtil.getModel(true, "确认案例成功");
        }else{
            return MsgModelUtil.getModel(false, "确认案例失败");
        }
    }

    @RequestMapping("checkNegReport")
    @ResponseBody
    public Object checkNegReport(@RequestBody Map<String,Object> params,HttpSession session){
        if(rdsJudicialSubCaseService.updateCheckNegReport((String)params.get("case_code"))>0){
        	//添加审核成功后自动处理异常
        	RdsJudicialCaseInfoModel mainCaseCodeId = rdsJudicialSubCaseMapper.queryCaseInfoByCaseCode((String)params.get("case_code"));
        	Map<String,Object> params_caseid = new HashMap<String, Object>();
        	 params_caseid.put("case_id", mainCaseCodeId.getCase_id());
        	List<RdsJudicialCaseExceptionModel> listcaseid=rdsJudicialExperimentMapper.getExceptionCaseId(params_caseid);
        	if (listcaseid.size()>0) {
        		for (RdsJudicialCaseExceptionModel model : listcaseid) {
        			if(model.getException_desc().equals("否定案例报告")){
        				Map<String,Object> params_exceptionid= new HashMap<String, Object>();
        				params_exceptionid.put("exception_id",model.getException_id() );
        				RdsJudicialCaseExceptionMapper.handleExceptionInfo(params_exceptionid);
        			}
				}
			}

            RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
            String caseCode = params.get("case_code").toString();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("yes", 0);
            boolean result = rdsActivitiJudicialService.runByCaseCode(caseCode, "taskNonReportAudit", variables, user);

            return MsgModelUtil.getModel(true, "审核成功");
        }else{
            return MsgModelUtil.getModel(false, "审核失败");
        }
    }

    @RequestMapping("deleteData")
    @ResponseBody
    public Object deleteData(@RequestBody Map<String,Object> params){
        if(rdsJudicialSubCaseService.deleteData((String)params.get("case_code"))>0){
            return MsgModelUtil.getModel(true, "删除数据成功");
        }else{
            return MsgModelUtil.getModel(false, "删除数据失败");
        }
    }
    
    @RequestMapping("allDelete")
    @ResponseBody
    public Object allDelete(@RequestBody Map<String, String>[] data){
        if(rdsJudicialSubCaseService.allDelete(data)>0){
            return MsgModelUtil.getModel(true, "删除数据成功");
        }else{
            return MsgModelUtil.getModel(false, "删除数据失败");
        }
    }
}
