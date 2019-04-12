package com.rds.trace.web;

import com.rds.trace.service.RdsTraceVehicleService;
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
@RequestMapping("trace/vehicle")
public class RdsTraceVehicleController {
    @Setter
    @Autowired
    RdsTraceVehicleService rdsTraceVehicleService;

    @RequestMapping("queryVehicle")
    @ResponseBody
    public Object queryVehicle(@RequestBody Map<String,Object> params){
        Map<String,Object> results = new HashMap<>();
        results.put("data",rdsTraceVehicleService.queryVehicle(params));
        results.put("total",rdsTraceVehicleService.queryCountVehicle(params));
        return results;
    }
}
