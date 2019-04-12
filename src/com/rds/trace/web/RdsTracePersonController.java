package com.rds.trace.web;

import com.rds.trace.service.RdsTracePersonService;
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
 * @date 2015/7/22
 */
@Controller
@RequestMapping("trace/person")
public class RdsTracePersonController {
    @Setter
    @Autowired
    RdsTracePersonService rdsTracePersonService;

    @RequestMapping("queryPerson")
    @ResponseBody
    public Object queryPerson(@RequestBody Map<String,Object> params){
        Map<String,Object> results = new HashMap<>();
        results.put("data",rdsTracePersonService.queryPerson(params));
        results.put("total",rdsTracePersonService.queryCountPerson(params));
        return results;
    }
}
