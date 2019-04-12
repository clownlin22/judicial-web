package com.rds.statistics.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.statistics.service.RdsAllCaseInfoService;

/**
 * @author lys
 * @className
 * @description
 * @date 2016/5/3
 */
@Controller
@RequestMapping("statistics/allCase")
public class RdsAllCaseInfoController {
    @Autowired
    private RdsAllCaseInfoService rdsAllCaseInfoService;

    @RequestMapping("query")
    @ResponseBody
    public Object queryExperiment(@RequestBody Map<String, Object> params) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("data",rdsAllCaseInfoService.queryAll(params));
        map.put("total",rdsAllCaseInfoService.queryAllCount(params));
        return map;
    }
    
    @RequestMapping("queryOld")
    @ResponseBody
    public Object queryOld(@RequestBody Map<String, Object> params) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("data",rdsAllCaseInfoService.queryAllOld(params));
        map.put("total",rdsAllCaseInfoService.queryAllCountOld(params));
        return map;
    }

    @RequestMapping("exportInfo")
    @ResponseBody
    public void export(@RequestParam String case_code,
    				   @RequestParam String ptype,
                       @RequestParam String receiver,
                       @RequestParam String deptname,
                       @RequestParam String agent,
                       @RequestParam String start_time,
                       @RequestParam String end_time,
                       @RequestParam String client,HttpServletResponse httpResponse) {
        rdsAllCaseInfoService.export(case_code,ptype,receiver,deptname,agent,start_time,end_time,client,httpResponse);
    }
    
    @RequestMapping("exportInfoOld")
    @ResponseBody
    public void exportInfoOld(@RequestParam String case_code,
    				   @RequestParam String ptype,
                       @RequestParam String receiver,
                       @RequestParam String deptname,
                       @RequestParam String agent,
                       @RequestParam String start_time,
                       @RequestParam String end_time,
                       @RequestParam String client,HttpServletResponse httpResponse) {
        rdsAllCaseInfoService.exportOld(case_code,ptype,receiver,deptname,agent,start_time,end_time,client,httpResponse);
    }
}
