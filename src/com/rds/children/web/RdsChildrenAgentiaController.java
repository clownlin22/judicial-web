package com.rds.children.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.children.service.RdsChildrenAgentiaService;

@RequestMapping("children/agentia")
@Controller
public class RdsChildrenAgentiaController {

	@Autowired
	private RdsChildrenAgentiaService aService;

	/**
	 * 获取试剂类型信息
	 * @param params
	 * @return
	 */
	@RequestMapping("getAgentiaInfo")
	@ResponseBody
	public Map<String, Object> getAgentiaInfo(
			@RequestBody Map<String, Object> params) {
		return aService.getAgentiaInfo(params);
	}
	
	/**
	 * 获取试剂下拉框
	 * @return
	 */
	@RequestMapping("getAgentiaCombo")
	@ResponseBody
	public Map<String, Object> getAgentiaCombo(){
		return aService.getAgentiaCombo();
	}
	
	/**
	 * 获取位点信息
	 * @param params
	 * @return
	 */
	@RequestMapping("getlocusInfo")
	@ResponseBody
	public Map<String, Object> getlocusInfo(@RequestBody Map<String, Object> params){
		return aService.getLocusInfo(params);
	}

	/**
	 * 保存试剂
	 * @param params
	 * @return
	 */
	@RequestMapping("save")
	@ResponseBody
	public Map<String, Object> save(@RequestBody Map<String, Object> params) {
		return aService.save(params);
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Map<String, Object> delete(@RequestBody Map<String, Object> params){
		return aService.delete(params);
	}
}
