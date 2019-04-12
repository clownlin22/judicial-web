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
import com.rds.bacera.service.RdsBaceraTumorMarkerService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("bacera/tumorMarker")
public class RdsBaceraTumorMarkerController extends RdsBaceraAbstractController {

	@Setter
	@Autowired
	private RdsBaceraTumorMarkerService rdsBaceraTumorMarkerService;

	@Setter
	@Autowired
	private RdsBaceraBoneAgeService rdsBoneAgeService;
	
	@RequestMapping("queryall")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params)
			throws Exception {
		return 0;
	}

	@RequestMapping("queryallpage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsBaceraTumorMarkerService.queryAllPage(params);
	}
	/**
	 * 查重案例编号
	 * @param params
	 * @return
	 */
	@RequestMapping("exsitCaseCode")
	@ResponseBody
	public boolean exsitCaseCode(@RequestBody Map<String, Object> params) {
		return rdsBaceraTumorMarkerService.exsitCaseCode(params);
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsBaceraTumorMarkerService.delete(params);
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
			if(!rdsBaceraTumorMarkerService.exsitCaseCode(params)){
				return this.setModel(false, RdsUpcConstantUtil.REPERT_CODE);
			}
			params.put("id", RdsUpcUUIDUtil.getUUID());
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
			params.put("case_type", "肿瘤标志物");
			//更新财务应收
			rdsBoneAgeService.insertFinance(params);
//			rdsBoneAgeService.insertExpress(params);
			//更新基本信息
			int result = rdsBaceraTumorMarkerService.insert(params);
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
			//判断是否更新原案例
			if (rdsBaceraTumorMarkerService.exsitCaseCode(params)) {
				//不是更新原案例，判断编码是否存在
				Map<String, Object> map = new HashMap<>();
				map.put("num", params.get("num"));
				if (!rdsBaceraTumorMarkerService.exsitCaseCode(params))
					return this.setModel(false, RdsUpcConstantUtil.REPERT_CODE);
			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
			//更新财务应收
			rdsBoneAgeService.updateFinance(params);
			//更新基本信息
			int result = rdsBaceraTumorMarkerService.update(params);
			if (result > 0) {
				return this.setModel(true, "操作成功");
			} else {
				return this.setModel(false, "操作成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}
	
	/**
	 * 导出hpv
	 * @param response
	 * @param request
	 */
	@RequestMapping("exportHpv")
	public void exportTumorSus(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String num = request.getParameter("num");
			params.put("num", num);
			String name = request.getParameter("name");
			params.put("name", name);
			String program = request.getParameter("program");
			params.put("program", program);
			String ownperson = request.getParameter("ownperson");
			params.put("ownperson", ownperson);
			params.put("starttime", request.getParameter("starttime"));
			params.put("endtime", request.getParameter("endtime"));
			params.put("reportif", request.getParameter("reportif"));
			params.put("cancelif","0".equals(request.getParameter("cancelif"))?"":request.getParameter("cancelif"));
			String inspection = request.getParameter("inspection");
			params.put("inspection", inspection);
			String sex = request.getParameter("sex");
			params.put("sex", sex); 
			RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
			params.put("roleType", user.getRoletype());
			params.put("userCode", user.getUsercode());
			rdsBaceraTumorMarkerService.exportTumor(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
