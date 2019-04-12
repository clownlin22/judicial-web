package com.rds.trace.web;

import com.rds.code.utils.model.MsgModelUtil;
import com.rds.trace.service.RdsTraceTypeService;
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
@RequestMapping("trace/type")
public class RdsTraceTypeController {
    @Autowired
    @Setter
    RdsTraceTypeService rdsTraceTypeService;

    @RequestMapping("queryType")
    @ResponseBody
    public Object queryType(@RequestBody Map<String,Object> params,HttpSession session){
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("data",rdsTraceTypeService.
                queryType(params));
        result.put("total",rdsTraceTypeService.
                queryCountType(params));
        return result;
    }

    @RequestMapping("insertOrUpdateType")
    @ResponseBody
    public Object insertType(@RequestBody Map<String,Object> params,HttpSession session){
        if((params.get("opertype")).equals("add")){
            try{
                rdsTraceTypeService.insertType(params);
                return MsgModelUtil.getModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
            }catch (Exception e){
                return MsgModelUtil.getModel(false, RdsUpcConstantUtil.INSERT_FAILED);
            }
        }else{
            if(rdsTraceTypeService.updateType(params)>0){
                return MsgModelUtil.getModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
            }else{
                return MsgModelUtil.getModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
            }
        }
    }

    @RequestMapping("deleteType")
    @ResponseBody
    public Object deleteType(@RequestBody Map<String,Object> params,HttpSession session){
        if(rdsTraceTypeService.deleteType(params)>0){
            return MsgModelUtil.getModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
        }else{
            return MsgModelUtil.getModel(false, RdsUpcConstantUtil.DELETE_FAILED);
        }
    }
}
