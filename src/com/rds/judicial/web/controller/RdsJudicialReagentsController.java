package com.rds.judicial.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.model.MsgModelUtil;
import com.rds.judicial.service.RdsJudicialReagentsService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/9/7
 */
@Controller
@RequestMapping("judicial/reagents")
public class RdsJudicialReagentsController {
    
    @Autowired
    RdsJudicialReagentsService rdsJudicialReagentsService;
    
    @RequestMapping("queryReagents")
    @ResponseBody
    public Object queryReagents(@RequestBody Map<String,Object> params,HttpSession session){
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("data",rdsJudicialReagentsService.
                queryAll(params));
        result.put("total",rdsJudicialReagentsService.
                queryAllCount(params));
        return result;
    }

    @RequestMapping("insertOrUpdateReagents")
    @ResponseBody
    public Object insertReagents(@RequestBody Map<String,Object> params,HttpSession session){
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        if((params.get("opertype")).equals("add")){
            try{
                rdsJudicialReagentsService.insert(params);
                return MsgModelUtil.getModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
            }catch (Exception e){
                return MsgModelUtil.getModel(false, RdsUpcConstantUtil.INSERT_FAILED);
            }
        }else{
            if(rdsJudicialReagentsService.update(params)>0){
                return MsgModelUtil.getModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
            }else{
                return MsgModelUtil.getModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
            }
        }
    }

    @RequestMapping("deleteReagents")
    @ResponseBody
    public Object deleteReagents(@RequestBody Map<String,Object> params,HttpSession session){
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        if(rdsJudicialReagentsService.delete(params)>0){
            return MsgModelUtil.getModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
        }else{
            return MsgModelUtil.getModel(false, RdsUpcConstantUtil.DELETE_FAILED);
        }
    }
}
