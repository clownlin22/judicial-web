package com.rds.upc.web.controller;

import com.rds.code.utils.model.MsgModelUtil;
import com.rds.upc.service.RdsUpcLaboratoryService;
import com.rds.upc.web.common.RdsUpcConstantUtil;
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
 * @date 2015/8/31
 */
@Controller
@RequestMapping("upc/laboratory")
public class RdsUpcLaboratoryController {
    @Autowired
    @Setter
    RdsUpcLaboratoryService rdsUpcLaboratoryService;

    @RequestMapping("queryLaboratory")
    @ResponseBody
    public Object queryLaboratory(@RequestBody Map<String,Object> params){
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("data",rdsUpcLaboratoryService.
                queryLaboratory(params));
        result.put("total",rdsUpcLaboratoryService.
                queryCountLaboratory(params));
        return result;
    }

    @RequestMapping("insertOrUpdateLaboratory")
    @ResponseBody
    public Object insertLaboratory(@RequestBody Map<String,Object> params){
        if((params.get("opertype")).equals("add")){
            try{
                rdsUpcLaboratoryService.insertLaboratory(params);
                return MsgModelUtil.getModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
            }catch (Exception e){
                return MsgModelUtil.getModel(false, RdsUpcConstantUtil.INSERT_FAILED);
            }
        }else{
            if(rdsUpcLaboratoryService.updateLaboratory(params)>0){
                return MsgModelUtil.getModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
            }else{
                return MsgModelUtil.getModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
            }
        }
    }

    @RequestMapping("deleteLaboratory")
    @ResponseBody
    public Object deleteLaboratory(@RequestBody Map<String,Object> params){
        if(rdsUpcLaboratoryService.deleteLaboratory((Integer)params.get("laboratory_no"))>0){
            return MsgModelUtil.getModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
        }else{
            return MsgModelUtil.getModel(false, RdsUpcConstantUtil.DELETE_FAILED);
        }
    }
}
