package com.rds.upc.web.controller;

import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.upc.service.RdsUpcModuleService;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("upc/module")
public class RdsUpcModuleController extends RdsUpcAbstractController {

	@Setter
	@Autowired
	private RdsUpcModuleService rdsUpcModuleService;

	@RequestMapping("queryAllByParent")
	@ResponseBody
	public Object queryAllByParent(@RequestBody Map<String, Object> params)
			throws Exception {
		String node = params.get("node")==null?"":params.get("node").toString();
		if(!"root".equals(node))
		{
			params.put("parentcode", node);
		}
		Object result = rdsUpcModuleService.queryAllByParent(params);
		return result;
	}

	@RequestMapping("insertOrUpdate")
	@ResponseBody
	public Object insertOrUpdate(@RequestBody Map<String, Object> params) {
		Object p_modulecode = params.get("modulecode");
		if (p_modulecode == null||p_modulecode.equals("")) {
			return insert(params);
		} else {
			return update(params);
		}
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			
			int result = rdsUpcModuleService.delete(params);
			if (result > 0) {
				return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
			} else {
				return this.setModel(false, RdsUpcConstantUtil.DELETE_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}

	/**
	 * 修改模版
	 * 
	 * @param params
	 * @return
	 */
	private Object update(Map<String, Object> params) {
		try {
			int result = rdsUpcModuleService.update(params);
			if (result > 0) {
				return this.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
			} else {
				return this.setModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}

	/**
	 * 新增模版
	 * 
	 * @param params
	 * @return
	 */
	private Object insert(Map<String, Object> params) {
		try {
			params.put("modulecode", RdsUpcUUIDUtil.getUUID());
			int result = rdsUpcModuleService.insert(params);
			if (result > 0) {
				return this.setModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
			} else {
				return this.setModel(false, RdsUpcConstantUtil.INSERT_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, RdsUpcConstantUtil.EXCEPTION
					+ "\n" + e.getMessage());
		}
	}

}
