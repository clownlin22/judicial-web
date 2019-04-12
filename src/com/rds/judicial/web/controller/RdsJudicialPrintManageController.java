package com.rds.judicial.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.model.RdsJudicialDicAreaModel;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialPrintManageService;
import com.rds.judicial.service.RdsJudicialPrintService;
import com.rds.upc.model.RdsUpcUserModel;

@RequestMapping("judicial/printmanage")
@Controller
public class RdsJudicialPrintManageController {
	
	@Autowired
	private RdsJudicialPrintManageService rdsJudicialPrintManageService;
	
	@RequestMapping("getPrintInfo")
	@ResponseBody
	public RdsJudicialResponse getPrintInfos(@RequestBody Map<String, Object> params) {
		return rdsJudicialPrintManageService.getPrintCaseInfo(params);
	}
	
	/**
	 * 新增菜单模板配置
	 * @param params
	 * @return
	 */
	@RequestMapping("getPrintInfoFoModel")
	@ResponseBody
	public RdsJudicialResponse getPrintInfoFoModel(@RequestBody Map<String, Object> params) {
		return rdsJudicialPrintManageService.getPrintInfoFoModel(params);
	}
	
	@RequestMapping("getCompany")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> getCompany(){
		return rdsJudicialPrintManageService.getCompany();
	}
	
	@RequestMapping("getArea")
	@ResponseBody
	public List<RdsJudicialDicAreaModel> getArea(@RequestBody Map<String, Object> params){
		return rdsJudicialPrintManageService.getArea(params);
	}
	
	@RequestMapping("saveArea")
	@ResponseBody
	public boolean saveArea(@RequestBody Map<String, Object> params){
		return rdsJudicialPrintManageService.saveArea(params);
	}
	
	/**
	 * 新增保存模板配置tb_dic_print_model
	 * @param params
	 * @return
	 */
	@RequestMapping("saveDicPrintModel")
	@ResponseBody
	public boolean saveDicPrintModel(@RequestBody Map<String, Object> params){
		return rdsJudicialPrintManageService.saveDicPrintModel(params);
	}
}
