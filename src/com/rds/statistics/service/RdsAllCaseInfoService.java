package com.rds.statistics.service;

import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2016/5/3
 */
public interface RdsAllCaseInfoService {
    List<Object> queryAll(Map<String,Object> params);

    int queryAllCount(Map<String,Object> params);

    List<Object> queryAllOld(Map<String,Object> params);

    int queryAllCountOld(Map<String,Object> params);
    
    void export(@RequestParam String case_code,
    			@RequestParam String ptype,
                @RequestParam String receiver,
                @RequestParam String deptname,
                @RequestParam String agent,
                @RequestParam String start_time,
                @RequestParam String end_time,
                @RequestParam String client,
                HttpServletResponse httpResponse);

    void exportOld(@RequestParam String case_code,
    			@RequestParam String ptype,
                @RequestParam String receiver,
                @RequestParam String deptname,
                @RequestParam String agent,
                @RequestParam String start_time,
                @RequestParam String end_time,
                @RequestParam String client,
                HttpServletResponse httpResponse);
}
