package com.rds.judicial.web.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.rds.judicial.service.RdsJudicialIdentifyPerService;
import com.rds.upc.model.RdsUpcUserModel;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.model.MsgModelUtil;
import com.rds.judicial.service.RdsJudicialExperimentService;
import com.rds.judicial.service.RdsJudicialSubCaseService;
import com.rds.upc.web.common.RdsUpcConstantUtil;

/**
 * @author linys
 * @ClassName: RdsJudicialRegisterController
 * @Description: 试验管理Controller
 * @date 2015年4月7日 上午10:28:24
 */
@Controller
@RequestMapping("judicial/experiment")
public class RdsJudicialExperimentController {
    @Autowired
    private RdsJudicialExperimentService rdsJudicialExperimentService;

    @Autowired
    private RdsJudicialSubCaseService rdsJudicialSubCaseService;

    @Autowired
    private RdsJudicialIdentifyPerService rdsJudicialIdentifyPerService;

    @RequestMapping("queryExperiment")
    @ResponseBody
    public Object queryExperiment(@RequestBody Map<String, Object> params,HttpSession session) {
        Map<String,Object> map = new HashMap<String, Object>();
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        map.put("data",rdsJudicialExperimentService.queryAll(params));
        map.put("total",rdsJudicialExperimentService.queryAllCount(params));
        return map;
    }

    @RequestMapping("queryExperimentBySample")
    @ResponseBody
    public Object queryExperimentBySample(@RequestBody Map<String, Object> params,HttpSession session) {
        Map<String,Object> map = new HashMap<String, Object>();
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        map.put("data",rdsJudicialExperimentService.queryAllBySample(params));
        map.put("total",rdsJudicialExperimentService.queryAllCountBySample(params));
        return map;
    }
    
    @RequestMapping("saveExperiment")
    @ResponseBody
    public Object save(@RequestBody Map<String, Object> params,HttpSession session) {
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        params.put("experimenter",((RdsUpcUserModel)session.getAttribute("user")).getUsercode());
        String operType = (String) params.get("operType");
        if (operType.equals("add")) {
            if (rdsJudicialExperimentService.experimentCount(params)>0){
                return MsgModelUtil.getModel(false, "该实验编号已经导入");
            }
            rdsJudicialExperimentService.insert(params);
            return MsgModelUtil.getModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
        } else if (operType.equals("update")) {
            if (rdsJudicialExperimentService.update(params) > 0)
                return MsgModelUtil.getModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
            else return MsgModelUtil.getModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
        }
        return MsgModelUtil.getModel(false, RdsUpcConstantUtil.INSERT_FAILED);
    }

    @RequestMapping("deleteExperiment")
    @ResponseBody
    public Object delete(@RequestBody Map<String, Object> params) {
        if (rdsJudicialExperimentService.delete(params) > 0)
            return MsgModelUtil.getModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
        else return MsgModelUtil.getModel(false, RdsUpcConstantUtil.DELETE_FAILED);
    }

    @RequestMapping("savePlaces")
    @ResponseBody
    public Object savePlaces(@RequestBody Map<String, Object> params) {
        Set set = params.keySet();
        Iterator it = set.iterator();
        StringBuffer sb = new StringBuffer();
        while(it.hasNext()){
            String key = (String)it.next();
            if(!key.equals("uuid")) {
                sb.append(key);
                sb.append("=");
                sb.append(((String) params.get(key)).trim());
                sb.append(",");
            }
        }
        if(!sb.toString().equals("")){
            sb.deleteCharAt(sb.length()-1);
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uuid",params.get("uuid"));
        map.put("places",sb.toString());
        if (rdsJudicialExperimentService.updatePlaces(map) > 0)
            return MsgModelUtil.getModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
        else return MsgModelUtil.getModel(false, RdsUpcConstantUtil.INSERT_FAILED);
    }

    @RequestMapping("queryPlaces")
    @ResponseBody
    public Object queryPlaces(@RequestBody Map<String, Object> params) {
        String result = rdsJudicialExperimentService.queryPlaces(params);
        return result;
    }

    @RequestMapping("ExportTxt")
    public void exportTxt(HttpServletResponse response,String experiment_no,HttpSession session)
            throws Exception {
        //rdsJudicialExperimentService.exportTxt(response,experiment_no);
        String laboratory_no = ((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + experiment_no + ".txt");
        response.setContentType("application/octet-stream; charset=utf-8");
        OutputStream os = null;
        BufferedOutputStream buff = null;
        os = response.getOutputStream();// 取得输出流
        buff = new BufferedOutputStream(os);
        buff.write(rdsJudicialExperimentService.exportTxt(experiment_no,laboratory_no).getBytes("UTF-8"));
        buff.flush();
    }

    @RequestMapping("verifySampleCode")
    @ResponseBody
    public boolean verifySampleCode(@RequestBody Map<String,Object> params){
        if(rdsJudicialSubCaseService.verifySampleCode(params)>0)
            return false;
        return true;
    }

    @RequestMapping("printModel")
    public String printModel(HttpServletRequest request,
                             String experiment_no,HttpSession session) throws IOException{

        //response.getWriter().write(rdsJudicialExperimentService.printTxt(experiment_no));
        //rdsJudicialExperimentService.exportTxt(experiment_no).getBytes("UTF-8");
        Map<Long,String> places = new HashMap<Long, String>();
        String laboratory_no = ((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no();
        places = rdsJudicialExperimentService.getPlaces(experiment_no,laboratory_no);
        request.setAttribute("places",places);
        return "dna/experimentModel";
    }

    @RequestMapping("queryResultsGroup")
    @ResponseBody
    public Object queryResultsGroup(@RequestBody Map<String,Object> params,HttpSession session){
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("data",rdsJudicialExperimentService.
                queryResultsGroup(params));
        result.put("total",rdsJudicialExperimentService.
                queryCountResultsGroup(params));
        return result;
    }

    @RequestMapping("insertOrUpdateResultsGroup")
    @ResponseBody
    public Object insertResultsGroup(@RequestBody Map<String,Object> params,HttpSession session){
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        if((params.get("opertype")).equals("add")){
            try{
                rdsJudicialExperimentService.insertResultsGroup(params);
                return MsgModelUtil.getModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
            }catch (Exception e){
                return MsgModelUtil.getModel(false, RdsUpcConstantUtil.INSERT_FAILED);
            }
        }else{
            if(rdsJudicialExperimentService.updateResultsGroup(params)>0){
                return MsgModelUtil.getModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
            }else{
                return MsgModelUtil.getModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
            }
        }
    }

    @RequestMapping("deleteResultsGroup")
    @ResponseBody
    public Object deleteResultsGroup(@RequestBody Map<String,Object> params,HttpSession session){
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        if(rdsJudicialExperimentService.deleteResultsGroup(params)>0){
            return MsgModelUtil.getModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
        }else{
            return MsgModelUtil.getModel(false, RdsUpcConstantUtil.DELETE_FAILED);
        }
    }

    @RequestMapping("queryInstrumentProtocol")
    @ResponseBody
    public Object queryInstrumentProtocol(@RequestBody Map<String,Object> params,HttpSession session){
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("data",rdsJudicialExperimentService.
                queryInstrumentProtocol(params));
        result.put("total",rdsJudicialExperimentService.
                queryCountInstrumentProtocol(params));
        return result;
    }

    @RequestMapping("insertOrUpdateInstrumentProtocol")
    @ResponseBody
    public Object insertInstrumentProtocol(@RequestBody Map<String,Object> params,HttpSession session){
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        if((params.get("opertype")).equals("add")) {
            try {
                rdsJudicialExperimentService.insertInstrumentProtocol(params);
                return MsgModelUtil.getModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
            } catch (Exception e) {
                return MsgModelUtil.getModel(false, RdsUpcConstantUtil.INSERT_FAILED);
            }
        }else{
            if(rdsJudicialExperimentService.updateInstrumentProtocol(params)>0){
                return MsgModelUtil.getModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
            }else{
                return MsgModelUtil.getModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
            }
        }
    }

    @RequestMapping("deleteInstrumentProtocol")
    @ResponseBody
    public Object deleteInstrumentProtocol(@RequestBody Map<String,Object> params,HttpSession session){
        params.put("laboratory_no",((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no());
        if(rdsJudicialExperimentService.deleteInstrumentProtocol(params)>0){
            return MsgModelUtil.getModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
        }else{
            return MsgModelUtil.getModel(false, RdsUpcConstantUtil.DELETE_FAILED);
        }
    }

    @RequestMapping("identifyPer")
    @ResponseBody
    public Object queryIdentifyPer(HttpSession session){
        Map<String,Object> result = new HashMap<>();
        String laboratory_no = ((RdsUpcUserModel)session.getAttribute("user")).getLaboratory_no();
        result.put("data",rdsJudicialIdentifyPerService.queryIdentify(laboratory_no));
        return result;
    }
}
