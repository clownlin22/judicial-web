package com.rds.trace.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.rds.trace.service.RdsTraceRegisterService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.trace.model.RdsTraceArchiveModel;
import com.rds.trace.model.RdsTraceArchiveReadModel;
import com.rds.trace.model.RdsTraceCaseInfoModel;
import com.rds.trace.service.RdsTraceArchiveService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("trace/archive")
public class RdsTraceArchiveController {

    @Autowired
    private RdsTraceArchiveService rdsTraceArchiveService;



    @RequestMapping("addArchiveInfo")
    @ResponseBody
    public boolean addArchiveInfo(@RequestBody Map<String, Object> params,HttpSession session){
        String case_in_per="";
        if(session.getAttribute("user")!=null){
            RdsUpcUserModel user=(RdsUpcUserModel)session.getAttribute("user");
            case_in_per=user.getUserid();
        }
        params.put("archive_per", case_in_per);
        rdsTraceArchiveService.addArchiveInfo(params);
        return true;
    }

    @RequestMapping("getArchiveInfo")
    @ResponseBody
    public Object getArchiveInfo(@RequestBody Map<String,Object> params){
        return rdsTraceArchiveService.getArchiveInfo(params);
    }

    @RequestMapping("getReadInfo")
    @ResponseBody
    public List<RdsTraceArchiveReadModel> getReadInfo(@RequestBody Map<String,Object> params){
        return rdsTraceArchiveService.getReadInfo(params);
    }

    @RequestMapping("addReadInfo")
    @ResponseBody
    public boolean addReadInfo(@RequestBody Map<String, Object> params){
        return rdsTraceArchiveService.addReadInfo(params);
    }

}
