package com.rds.bacera.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.bacera.service.RdsBaceraBoneAgeService;
import com.rds.bacera.service.RdsBaceraSlimBeautyPCRService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("bacera/slimBeautyPCR")
public class RdsBaceraSlimBeautyPCRController extends RdsBaceraAbstractController {

	@Setter
	@Autowired
	private RdsBaceraSlimBeautyPCRService rdsSlimBeautyPCRService;

	@Setter
	@Autowired
	private RdsBaceraBoneAgeService rdsBoneAgeService;
	
	@RequestMapping("queryall")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params)
			throws Exception {
		return 0;
		// return rdsUpcBlackListService.queryAll(params);
	}

	@RequestMapping("queryallpage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsSlimBeautyPCRService.queryAllPage(params);
	}

	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsSlimBeautyPCRService.delete(params);
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
			int numResult = rdsSlimBeautyPCRService.queryNumExit(params);
			if (numResult > 0) {
				return this.setModel(false, "该类型编号已存在，请重新输入");
			}
			params.put("id", RdsUpcUUIDUtil.getUUID());
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", params.get("id"));
			map.put("num", params.get("num"));
			map.put("inputperson", params.get("inputperson"));
			map.put("receivables", params.get("receivables"));
			if("1".equals(params.get("case_type").toString()))
				map.put("case_type", "减肥美容基因检测");
			else if("2".equals(params.get("case_type").toString()))
				map.put("case_type", "个体特征基因检测");
			else 
				map.put("case_type", "重大慢病基因检测");
			//更新财务应收
			rdsBoneAgeService.insertFinance(map);
			//更新基本信息
			int result = rdsSlimBeautyPCRService.insert(params);
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
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("num", params.get("num").toString());
//			map.put("id", params.get("id").toString());
			int numResult = rdsSlimBeautyPCRService.queryNumExit(params);
			if (numResult <= 0) {
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("num", params.get("num").toString().trim());
				map1.put("case_type", params.get("case_type").toString().trim());
				if (rdsSlimBeautyPCRService.queryNumExit(map1) > 0)
					return this.setModel(false, "该类型编号已存在，请重新修改");
			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
			params.put("inputperson", user.getUserid());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", params.get("id"));
			map.put("num", params.get("num"));
			map.put("inputperson", params.get("inputperson"));
			map.put("receivables", params.get("receivables"));
			if("1".equals(params.get("case_type").toString()))
				map.put("case_type", "减肥美容基因检测");
			else if("2".equals(params.get("case_type").toString()))
				map.put("case_type", "个体特征基因检测");
			else 
				map.put("case_type", "重大慢病基因检测");
			//更新财务应收
			rdsBoneAgeService.updateFinance(map);
			//更新基本信息
			int result = rdsSlimBeautyPCRService.update(params);
			if (result > 0) {
				return this.setModel(true, "修改操作成功");
			} else {
				return this.setModel(false, "修改操作失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}
	
	/**
	 * 导出肿瘤易感基因
	 * @param response
	 * @param request
	 */
	@RequestMapping("exportSlimBeautyPCR")
	public void exportSlimBeautyPCR(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String num = request.getParameter("num");
			params.put("num", num);
			String name = request.getParameter("name");
			params.put("name", name);
			String ownperson = request.getParameter("ownperson");
			params.put("ownperson", ownperson);
			params.put("slimBeautyPCR_starttime", request.getParameter("slimBeautyPCR_starttime"));
			params.put("slimBeautyPCR_endtime", request.getParameter("slimBeautyPCR_endtime"));
			params.put("reportif", request.getParameter("reportif"));
			params.put("cancelif","0".equals(request.getParameter("cancelif"))?"":request.getParameter("cancelif"));
			String agent = request.getParameter("agent");
			params.put("agent", agent);
			params.put("areacode", "null".equals(request.getParameter("areacode"))?"":request.getParameter("areacode"));
			String checkper = request.getParameter("checkper");
			params.put("checkper", checkper); 
			String gender = request.getParameter("gender");
			params.put("gender", gender); 
			RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
			params.put("roleType", user.getRoletype());
			params.put("userCode", user.getUsercode());
			rdsSlimBeautyPCRService.exportSlimBeautyPCR(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
