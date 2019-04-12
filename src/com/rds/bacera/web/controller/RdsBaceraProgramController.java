package com.rds.bacera.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.bacera.service.RdsBaceraProgramService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("bacera/program")
public class RdsBaceraProgramController extends RdsBaceraAbstractController {

	@Setter
	@Autowired
	private RdsBaceraProgramService rdsBaceraProgramService;

	@RequestMapping("queryall")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsBaceraProgramService.queryAll(params);
	}
	
	@RequestMapping("queryAllAjax")
	@ResponseBody
	public Object queryAll(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HashMap<Object, Object> params = new HashMap<Object, Object>();
		params.put("program_type", request.getParameter("program_type"));
		return rdsBaceraProgramService.queryAll(params);
	}
	

	@RequestMapping("queryallpage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsBaceraProgramService.queryAllPage(params);
	}

	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsBaceraProgramService.delete(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}

	@RequestMapping("save")
	@ResponseBody
	public Object save(@RequestBody Map<String, Object> params,HttpServletRequest request) {
		Object id = params.get("id");
		if (id == null || id.equals("")) {
			return insert(params,request);
		} else {
			return update(params,request);
		}
	}

	private Object insert(Map<String, Object> params,HttpServletRequest request) {
		try {
			params.put("id", RdsUpcUUIDUtil.getUUID());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("program_name", params.get("program_name").toString());
			int numResult = rdsBaceraProgramService.queryNumExit(map);
			if (numResult > 0) {
				return this.setModel(false, "该项目已存在，请重新输入!");
			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("create_per", user.getUserid());
			int result = rdsBaceraProgramService.insert(params);
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

	private Object update(Map<String, Object> params,HttpServletRequest request) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("program_name", params.get("program_name").toString());
			map.put("id", params.get("id").toString());
			int numResult = rdsBaceraProgramService.queryNumExit(map);
			if (numResult <= 0) {
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("program_name", params.get("program_name").toString().trim());
				if (rdsBaceraProgramService.queryNumExit(map1) > 0)
					return this.setModel(false, "该项目已存在，请重新修改!");
			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("create_per", user.getUserid());
			int result = rdsBaceraProgramService.update(params);
			if (result > 0) {
				return this.setModel(true, "操作成功!");
			} else {
				return this.setModel(false, "操作成功!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}
}
