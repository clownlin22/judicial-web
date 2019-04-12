package com.rds.judicial.web.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.service.RdsJudicialArchiveService;
import com.rds.judicial.web.common.PageUtil;
import com.rds.upc.model.RdsUpcUserModel;

/**
 * 2015年4月27日
 * @author ThinK
 * @description 归档
 *
 */
@Controller
@RequestMapping("judicial/archive")
public class RdsJudicialArchiveController {
	
	@Setter
	@Autowired
	private RdsJudicialArchiveService archiveService;
	
	/**
	 * 归档
	 * @param params
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("save")
	@ResponseBody
	public Map<String,Object> doArchive(@RequestBody Map<String, Object> params,HttpServletRequest request) throws ParseException{
		
		return archiveService.doArchive(params,request);
	}
	
	/**
	 * 归档信息显示
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryallpage")
	@ResponseBody
	public Map<String, Object> queryAll(@RequestBody Map<String, Object> params) throws Exception{
		return archiveService.queryAll(params);
	}
	
	/**
	 * 归档阅读记录
	 * @param params
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("readsave")
	@ResponseBody
	public Map<String, Object> readSave(@RequestBody Map<String, Object> params) throws ParseException{
		return archiveService.readSave(params);
	}
	
	/**
	 * 查询阅读历史
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryRead")
	@ResponseBody
	public Map<String, Object> queryRead(@RequestBody Map<String, Object> params) throws Exception{
		return archiveService.queryRead(PageUtil.pageSet(params));
	}
	/**
	 * 查询阅读历史
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryMailCount")
	@ResponseBody
	public Map<String, Object> queryMailCount(@RequestBody Map<String, Object> params) throws Exception{
		return archiveService.queryMailCount(params);
	}
	
	@RequestMapping("getCaseInfo")
	@ResponseBody
	public Map<String,Object> getCaseInfo(
			@RequestBody Map<String, Object> params,HttpSession session) throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        String sql_str = "";
        String partner_name="";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			sql_str = user.getSql_str();
			partner_name = user.getParnter_name();
		}
		params.put("sql_str", sql_str);
		params.put("partner_name", partner_name);
        map.put("data",archiveService.queryAllCase(params));
        map.put("total",archiveService.queryALLCasseCount(params));
		return map;
	}
}
