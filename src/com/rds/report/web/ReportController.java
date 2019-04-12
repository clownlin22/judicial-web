package com.rds.report.web;

import com.alibaba.druid.pool.DruidDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2016/1/6
 */
@Controller
@RequestMapping("report")
public class ReportController {
    @RequestMapping("getReport")
    public ModelAndView getReport(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Map map = new HashMap();
        map.put(JasperReportsMultiFormatView.DEFAULT_FORMAT_KEY, "pdf");
        return new ModelAndView("datasourceCustomerReport",map);
    }
}
