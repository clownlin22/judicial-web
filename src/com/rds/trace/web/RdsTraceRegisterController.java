package com.rds.trace.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.model.MsgModelUtil;
import com.rds.trace.service.RdsTraceRegisterService;
import com.rds.upc.model.RdsUpcMessageModel;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/20
 */
@Controller
@RequestMapping("trace/register")
public class RdsTraceRegisterController {
    @Autowired
    @Setter
    RdsTraceRegisterService rdsTraceRegisterService;


    @RequestMapping("queryCount")
    @ResponseBody
    public int queryCountCaseInfo(@RequestBody Map<String, Object> params) {
        return rdsTraceRegisterService.queryCountCaseInfo(params);
    }

    @RequestMapping("queryCaseInfo")
    @ResponseBody
    public Object queryCaseInfo(@RequestBody Map<String, Object> params) {
        Map<String,Object> results = new HashMap<>();
        results.put("data",rdsTraceRegisterService.queryCaseInfo(params));
        results.put("total",rdsTraceRegisterService.queryCountCaseInfo(params));
        return results;
    }

    @RequestMapping("delete")
    @ResponseBody
    public Object deleteCaseInfo(@RequestBody Map<String,Object> params) {
        String case_id = (String)params.get("case_id");
        if(rdsTraceRegisterService.deleteCaseInfo(case_id)>0){
            return MsgModelUtil.getModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
        }else{
            return MsgModelUtil.getModel(false, RdsUpcConstantUtil.DELETE_FAILED);
        }
    }

    @SuppressWarnings("unchecked")
	@RequestMapping("insertOrUpdate")
    @ResponseBody
    public Object insertCaseInfo(@RequestBody Map<String, Object> params) throws Exception{
        String case_attachment_desc = "";
        if(params.get("case_attachment_desc") instanceof List){
            List<String> list = (ArrayList<String>)params.get("case_attachment_desc");
            for(String str:list){
                case_attachment_desc = case_attachment_desc + str + ',';
            }
            case_attachment_desc = case_attachment_desc.substring(0,case_attachment_desc.length()-1);
            params.put("case_attachment_desc",case_attachment_desc);
        }
        if(params.get("case_type") instanceof List){
            List<String> list = (ArrayList<String>)params.get("case_type");
            String case_type = "";
            for(String str:list){
                case_type = case_type + str + ',';
            }
            case_type = case_type.substring(0,case_type.length()-1);
            params.put("case_type",case_type);
        }
        try {
            if(params.get("opertype").equals("add")) {
                    rdsTraceRegisterService.insertCaseInfo(params);
                    return MsgModelUtil.getModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);

            }else{
                 if(rdsTraceRegisterService.updateCaseInfo(params)) {
                     return MsgModelUtil.getModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
                 }
                 else{
                    return MsgModelUtil.getModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
                }
            }
        }catch (Exception e) {
            RdsUpcMessageModel rdsUpcMessageModel = null;
            rdsUpcMessageModel = MsgModelUtil.getModel(false, e.toString().split(":")[1]);
            return rdsUpcMessageModel;
        }
    }

    @RequestMapping("exportInfo")
    public void exportInfo(@RequestParam String case_no,
                           @RequestParam String start_time, @RequestParam String end_time,
                           @RequestParam String status, @RequestParam Integer is_delete,@RequestParam String invoice_number,
                           HttpServletResponse response) {
        rdsTraceRegisterService.exportInfo(case_no,start_time,end_time,status,is_delete,invoice_number,response);
    }
    
    @RequestMapping("traceMailDely")
    @ResponseBody
    public boolean traceMailDely(@RequestBody Map<String,Object> params,HttpServletRequest request){
    	RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
    	params.put("dely_person", user.getUserid());
        return rdsTraceRegisterService.traceMailDely(params);
    }
}
