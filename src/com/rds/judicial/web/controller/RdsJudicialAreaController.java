package com.rds.judicial.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.StringUtils;
import com.rds.judicial.model.RdsJudicialDicAreaModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialUpcAreaModel;
import com.rds.judicial.service.RdsJudicialAreaService;
import com.rds.upc.model.RdsUpcMessageModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;

@Controller
@RequestMapping("judicial/area")
public class RdsJudicialAreaController {

	@Autowired
	private RdsJudicialAreaService rdsJudicialAreaService;

	/**
	 * 获取所有采样地区信息
	 */
	@RequestMapping("getUpcUserInfo")
	@ResponseBody
	public RdsJudicialResponse getUpcUserInfo(
			@RequestBody Map<String, Object> params) {
		return rdsJudicialAreaService.getUpcUserInfo(params);
	}

	/**
	 * 保存采样地区信息
	 */
	@RequestMapping("saveUpcUserInfo")
	@ResponseBody
	public Object saveUpcUserInfo(@RequestBody Map<String, Object> params) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Map<String, Integer> map = (Map) rdsJudicialAreaService
				.saveUpcUserInfo(params);
		// 校验数据是否存在
		if (map.get("check") > 0)
			return this.setModel(false, RdsUpcConstantUtil.REPERT);
		if (map.get("result") <= 0)
			return this.setModel(false, RdsUpcConstantUtil.INSERT_FAILED);
		else
			return this.setModel(true, RdsUpcConstantUtil.SAVE_SUCCESS);
	}

	/**
	 * 删除采样地区信息
	 */
	@RequestMapping("delUpcUserInfo")
	@ResponseBody
	public boolean delUpcUserInfo(@RequestBody Map<String, Object> params) {
		return rdsJudicialAreaService.delUpcUserInfo(params);
	}

	/**
	 * 修改采样地区信息
	 */
	@RequestMapping("updateUpcUserInfo")
	@ResponseBody
	public Object updateUpcUserInfo(@RequestBody Map<String, Object> params) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Map<String, Integer> map = (Map) rdsJudicialAreaService
				.updateUpcUserInfo(params);
		// 校验数据是否存在
		if (map.get("check") > 0)
			return this.setModel(false, RdsUpcConstantUtil.REPERT);
		if (map.get("result") <= 0)
			return this.setModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
		else
			return this.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
	}

	/**
	 * 获取字典表地区信息
	 */
	@RequestMapping("getDicAreaInfo")
	@ResponseBody
	public List<RdsJudicialDicAreaModel> getDicAreaInfo(
			@RequestBody Map<String, Object> params) {
		return rdsJudicialAreaService.getDicAreaInfo(params);
	}

	/**
	 * 判断地区编号是否存在
	 */
	@RequestMapping("exsitDicAreaCode")
	@ResponseBody
	public boolean exsitDicAreaCode(@RequestBody Map<String, Object> params) {
		return rdsJudicialAreaService.exsitDicAreaCode(params);
	}

	/**
	 * 保存字典表地区信息
	 */
	@RequestMapping("saveDicAreaInfo")
	@ResponseBody
	public boolean saveDicAreaInfo(@RequestBody Map<String, Object> params) {
		params.put("initials",
				StringUtils.getInitials(params.get("areaname").toString()));
		return rdsJudicialAreaService.saveDicAreaInfo(params);
	}

	/**
	 * 删除字典表地区信息
	 */
	@RequestMapping("delDicAreaInfo")
	@ResponseBody
	public boolean delDicAreaInfo(@RequestBody Map<String, Object> params) {
		System.out.println(params.toString());
		return rdsJudicialAreaService.delDicAreaInfo(params);
	}

	/**
	 * 获取采样字典表地区信息
	 */
	@RequestMapping("delUpcAreaInfo")
	@ResponseBody
	public boolean delUpcAreaInfo(@RequestBody Map<String, Object> params) {
		return rdsJudicialAreaService.delUpcAreaInfo(params);
	}

	/**
	 * 获取采样字典表地区信息
	 */
	@RequestMapping("getUpcAreaInfo")
	@ResponseBody
	public Map<String, Object> getUpcAreaInfo(
			@RequestBody Map<String, Object> params) {
		return rdsJudicialAreaService.getUpcAreaInfo(params);
	}

	/**
	 * 获取接收员工
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("getAreaForPer")
	@ResponseBody
	public List<RdsJudicialUpcAreaModel> getAreaForPer(String query,
			HttpServletRequest request) {
		String areacode=request.getParameter("areacode");
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("initials", query);
			if(com.mysql.jdbc.StringUtils.isNullOrEmpty(query))
			{
				params.put("areacode", areacode);
			}
			return (List<RdsJudicialUpcAreaModel>) rdsJudicialAreaService
					.getUpcAreaInfo(params).get("data");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 保存采样字典表地区信息
	 */
	@RequestMapping("saveUpcAreaInfo")
	@ResponseBody
	public Object saveUpcAreaInfo(@RequestBody Map<String, Object> params) {
		int result = rdsJudicialAreaService.saveUpcAreaInfo(params);
		if (result < 0)
			return this.setModel(false, RdsUpcConstantUtil.INSERT_FAILED);
		else if (result == 0)
			return this.setModel(false, RdsUpcConstantUtil.REPERT);
		else
			return this.setModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
	}

	/**
	 * 修改采样字典表地区信息
	 */
	@RequestMapping("updateUpcAreaInfo")
	@ResponseBody
	public Object updateUpcAreaInfo(@RequestBody Map<String, Object> params) {
		int result = rdsJudicialAreaService.updateUpcAreaInfo(params);
		if (result < 0)
			return this.setModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
		else if (result == 0)
			return this.setModel(false, RdsUpcConstantUtil.REPERT);
		else
			return this.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
	}

	protected RdsUpcMessageModel setModel(boolean result, String message) {

		RdsUpcMessageModel model = new RdsUpcMessageModel();
		model.setResult(result);
		model.setMessage(message);
		return model;
	}
}
