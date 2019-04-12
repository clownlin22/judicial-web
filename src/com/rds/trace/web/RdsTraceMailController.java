package com.rds.trace.web;

import com.rds.trace.service.RdsTraceMailService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/29
 */
@Controller
@RequestMapping("trace/mail")
public class RdsTraceMailController {
    @Autowired
    @Setter
    RdsTraceMailService rdsTraceMailService;

    @RequestMapping("delMailInfo")
    @ResponseBody
    public boolean delMailInfo(@RequestBody Map<String,Object> params){
        return rdsTraceMailService.delMailInfo(params);
    }

    @RequestMapping("addMailInfo")
    @ResponseBody
    public boolean addMailInfo(@RequestBody Map<String,Object> params){
        return rdsTraceMailService.addMailInfo(params);
    }

    @RequestMapping("updateMailInfo")
    @ResponseBody
    public boolean updateMailInfo(@RequestBody Map<String,Object> params){
        return rdsTraceMailService.updateMailInfo(params);
    }

    @RequestMapping("getMailInfo")
    @ResponseBody
    public Object getMailInfo(@RequestBody Map<String,Object> params){
        Map<String,Object> result = new HashMap<>();
        result.put("data",rdsTraceMailService.getMailInfo(params));
        result.put("total",rdsTraceMailService.getCountMailInfo(params));
        return result;
    }
}
