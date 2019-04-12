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
import com.rds.bacera.service.RdsBaceraFolicAcidService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("bacera/folicAcid")
public class RdsBaceraFolicAcidController extends RdsBaceraAbstractController {

	@Setter
	@Autowired
	private RdsBaceraFolicAcidService rdsFolicAcidService;
	
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
//		params = this.pageSet(params);
		return rdsFolicAcidService.queryAllPage(params);
	}

	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsFolicAcidService.delete(params);
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
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("num", params.get("num").toString());
//			int numResult = rdsTumorPerService.queryNumExit(map);
//			if (numResult > 0) {
//				return this.setModel(false, "编号已存在，请重新输入");
//			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
			
			params.put("case_type", "叶酸代谢能力检测");
			rdsBoneAgeService.insertFinance(params);
			int result = rdsFolicAcidService.insert(params);
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
//			int numResult = rdsTumorPerService.queryNumExit(map);
//			if (numResult <= 0) {
//				Map<String, Object> map1 = new HashMap<String, Object>();
//				map1.put("num", params.get("num").toString().trim());
//				if (rdsTumorPerService.queryNumExit(map1) > 0)
//					return this.setModel(false, "编号已存在，请重新修改");
//			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());

			rdsBoneAgeService.updateFinance(params);
			int result = rdsFolicAcidService.update(params);
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
	 * 导出叶酸检测
	 * @param response
	 * @param request
	 */
	@RequestMapping("exportFolicAcid")
	public void exportFolicAcid(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String num = request.getParameter("num");
			params.put("num", num);
			String name = request.getParameter("name");
			params.put("name", name);
			String ownperson = request.getParameter("ownperson");
			params.put("ownperson", ownperson);
			params.put("folicAcid_starttime", request.getParameter("folicAcid_starttime"));
			params.put("folicAcid_endtime", request.getParameter("folicAcid_endtime"));
			params.put("reportif", request.getParameter("reportif"));
			params.put("cancelif","0".equals(request.getParameter("cancelif"))?"":request.getParameter("cancelif"));
			String agent = request.getParameter("agent");
			params.put("agent", agent);
			params.put("areacode", "null".equals(request.getParameter("areacode"))?"":request.getParameter("areacode"));
			String checkper = request.getParameter("checkper");
			params.put("checkper", checkper); 
			String hospital = request.getParameter("hospital");
			params.put("hospital", hospital); 
			RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
			params.put("roleType", user.getRoletype());
			params.put("userCode", user.getUsercode());
			rdsFolicAcidService.exportFolicAcid(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
