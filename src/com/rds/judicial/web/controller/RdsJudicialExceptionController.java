package com.rds.judicial.web.controller;

import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.model.MsgModelUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.judicial.mapper.RdsJudicialExceptionMapper;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialExceptionService;
import com.rds.upc.model.RdsUpcUserModel;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/16
 */
@Controller
@RequestMapping("/judicial/exception")
public class RdsJudicialExceptionController {
    @Autowired
    RdsJudicialExceptionService rdsJudicialExceptionService;
    
  

    @RequestMapping("queryAllException")
    @ResponseBody
    public Map<String,Object> queryAllException(@RequestBody Map<String,Object> params,HttpSession session){
        Map<String,Object> map = new HashMap<String,Object>();
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        map.put("data",rdsJudicialExceptionService.queryAll(params));
        map.put("total",rdsJudicialExceptionService.queryAllCount(params));
        return map;
    }

    @RequestMapping("deleteException")
    @ResponseBody
    public Object deleteException(@RequestBody Map<String,Object> params){
        if(rdsJudicialExceptionService.update(params)>0)
            return MsgModelUtil.getModel(true,"异常信息处理成功");
        else
            return MsgModelUtil.getModel(false,"异常信息处理失败");
    }

    @RequestMapping("queryCrossCompare")
    @ResponseBody
    public Map<String,Object> queryCrossCompare(@RequestBody Map<String,Object> params,HttpSession session){
        Map<String,Object> map = new HashMap<String, Object>();
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        map.put("data",rdsJudicialExceptionService.queryCrossCompare(params));
        map.put("total",rdsJudicialExceptionService.queryCountCrossCompare(params));
        return map;
    }
    
    @RequestMapping("deleteAllException")
    @ResponseBody
    public Object  getZip(@RequestBody Map<String,String>[] data) throws Exception{
        int count=0;
    	  for(int i=0;i<data.length;i++){
    		  Map<String,Object> params = new HashMap<>();
              String uuid = data[i].get("uuid");
              params.put("uuid",uuid);
              params.put("choose_flag", 0);
              count=rdsJudicialExceptionService.update(params);
              count= count+1;
    	  }
    	  if(count>0) 
	            return MsgModelUtil.getModel(true,"异常信息处理成功");
		   else 
	            return MsgModelUtil.getModel(false,"异常信息处理失败");
    }
 
}
