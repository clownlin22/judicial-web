package com.rds.judicial.web.controller;

/**
 * @author fushaoming
 * 收费标准
 */
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.service.RdsJudicialFeeStandardService;

@Controller
@RequestMapping("judicial/feestandard")
public class RdsJudicialFeeStandardController {

	@Setter
	@Autowired
	private RdsJudicialFeeStandardService fsService;

	/**
	 * 保存公式
	 * @param params
	 * @return
	 */
	@RequestMapping("save")
	@ResponseBody
	public Map<String, Object> saveEquation(
			@RequestBody Map<String, Object> params) {
		return fsService.saveEquation(params);
	}
	/**
	 * 修改公式
	 * @param params
	 * @return
	 */
	@RequestMapping("update")
	@ResponseBody
	public Map<String, Object> updateEquation(
			@RequestBody Map<String, Object> params) {
		return fsService.updateEquation(params);
	}
	/**
	 * 查询所有收费标准
	 * @param params
	 * @return
	 */
	@RequestMapping("querytype")
	@ResponseBody
	public Map<String, Object> queryType(@RequestBody Map<String, Object> params) {
		return fsService.queryType(params);
	}

	/**
	 * 逻辑删除收费标准
	 * @param params
	 * @return
	 */
	@RequestMapping("delete")
	@ResponseBody
	public Map<String, Object> delete(@RequestBody Map<String, Object> params) {
		return fsService.delete((String)params.get("id"));
	}
}
