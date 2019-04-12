package com.rds.trace.web;

import com.rds.code.utils.model.MsgModelUtil;
import com.rds.trace.service.RdsTraceDocumentService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/9/15
 */
@Controller
@RequestMapping("trace/document")
public class RdsTraceDocumentController {
    @Autowired
    @Setter
    RdsTraceDocumentService rdsTraceDocumentService;

    @RequestMapping("queryDocument")
    @ResponseBody
    public Object queryDocument(@RequestBody Map<String,Object> params,HttpSession session){
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("data",rdsTraceDocumentService.
                queryDocument(params));
        result.put("total",rdsTraceDocumentService.
                queryCountDocument(params));
        return result;
    }

    @RequestMapping("insertOrUpdateDocument")
    @ResponseBody
    public Object insertDocument(@RequestBody Map<String,Object> params,HttpSession session){
        if((params.get("opertype")).equals("add")){
            try{
                rdsTraceDocumentService.insertDocument(params);
                return MsgModelUtil.getModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
            }catch (Exception e){
                return MsgModelUtil.getModel(false, RdsUpcConstantUtil.INSERT_FAILED);
            }
        }else{
            if(rdsTraceDocumentService.updateDocument(params)>0){
                return MsgModelUtil.getModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
            }else{
                return MsgModelUtil.getModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
            }
        }
    }

    @RequestMapping("deleteDocument")
    @ResponseBody
    public Object deleteDocument(@RequestBody Map<String,Object> params,HttpSession session){
        if(rdsTraceDocumentService.deleteDocument(params)>0){
            return MsgModelUtil.getModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
        }else{
            return MsgModelUtil.getModel(false, RdsUpcConstantUtil.DELETE_FAILED);
        }
    }
}
