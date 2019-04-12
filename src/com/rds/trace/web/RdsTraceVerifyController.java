package com.rds.trace.web;

import com.rds.code.utils.model.MsgModelUtil;
import com.rds.trace.service.RdsTraceAttachmentService;
import com.rds.trace.service.RdsTraceRegisterService;
import com.rds.trace.service.RdsTraceVerifyService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/23
 */
@Controller
@RequestMapping("trace/verify")
public class RdsTraceVerifyController {
    @Autowired
    RdsTraceVerifyService rdsTraceVerifyService;

    @Autowired
    RdsTraceRegisterService rdsTraceRegisterService;

    @Autowired
    RdsTraceAttachmentService rdsTraceAttachmentService;

    @RequestMapping("insertVerify")
    @ResponseBody
    public Object insertVerify(@RequestBody Map<String,Object> params,HttpSession session){
        try{
            RdsUpcUserModel user=(RdsUpcUserModel)session.getAttribute("user");
            params.put("verify_baseinfo_person",user.getUsername());
            if((Integer)params.get("verify_baseinfo_state")==1){
                params.put("status",1);
            }else{
                params.put("status",-2);
            }
            rdsTraceRegisterService.updateStatus(params);
            rdsTraceVerifyService.insertVerify(params);
            return MsgModelUtil.getModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
        }catch (Exception e){
            return MsgModelUtil.getModel(false, RdsUpcConstantUtil.INSERT_FAILED);
        }
    }

    @RequestMapping("queryVerify")
    @ResponseBody
    public Object queryVerify(@RequestBody Map<String,Object> params){
        Map<String,Object> map = new HashMap<>();
        map.put("data",rdsTraceVerifyService.queryVerify(params));
        map.put("total",rdsTraceVerifyService.queryCountVerify(params));
        return map;
    }

    @RequestMapping("verifyWord")
    public Object queryVerify(HttpServletRequest request,String year,String case_no){

        String dataPath = rdsTraceAttachmentService.getDataPath();
        String file_name = dataPath + year + case_no +
                File.separatorChar + year + case_no + ".doc";
        request.setAttribute("file_name",file_name);
        return "pageoffice/readOnly_word";
    }

    @RequestMapping("updateStatus")
    @ResponseBody
    public Object updateStatus(@RequestBody Map<String,Object> params){
        if(rdsTraceRegisterService.updateStatus(params)==1)
            return MsgModelUtil.getModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
        else
            return MsgModelUtil.getModel(false, RdsUpcConstantUtil.INSERT_FAILED);
    }

    @RequestMapping("queryVerifyHistory")
    @ResponseBody
    public Object queryBaseinfoVerifyByCaseid(@RequestBody Map<String,Object> params){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("data",rdsTraceVerifyService.
                queryBaseinfoVerifyByCaseid((String)params.get("case_id")));
        return map;
    }
}
