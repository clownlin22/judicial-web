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
import com.rds.bacera.service.RdsBaceraFoodSafetyService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("bacera/foodSafety")
public class RdsBaceraFoodSatetyController extends RdsBaceraAbstractController {

	@Setter
	@Autowired
	private RdsBaceraFoodSafetyService rdsFoodSafetyService;
	
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
		return rdsFoodSafetyService.queryAllPage(params);
	}

	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsFoodSafetyService.delete(params);
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
//			int numResult = rdsFoodSafetyService.queryNumExit(map);
//			if (numResult > 0) {
//				return this.setModel(false, "编号已存在，请重新输入");
//			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
			params.put("case_type", "食品安全");
			rdsBoneAgeService.insertFinance(params);
			int result = rdsFoodSafetyService.insert(params);
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
//			int numResult = rdsFoodSafetyService.queryNumExit(map);
//			if (numResult <= 0) {
//				Map<String, Object> map1 = new HashMap<String, Object>();
//				map1.put("num", params.get("num").toString().trim());
//				if (rdsFoodSafetyService.queryNumExit(map1) > 0)
//					return this.setModel(false, "编号已存在，请重新修改");
//			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
			rdsBoneAgeService.updateFinance(params);
			int result = rdsFoodSafetyService.update(params);
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
	 * 导出骨龄信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("exportFoodSafe")
	public void exportFoodSafe(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			//编号
			String num = request.getParameter("num");
//			String num = new String(request.getParameter("num").getBytes("ISO-8859-1"),"UTF-8");
			params.put("num", num);
			//样品名称
			String samplename = request.getParameter("samplename");
//			String samplename = new String(request.getParameter("samplename").getBytes("ISO-8859-1"),"UTF-8");
			params.put("samplename", samplename);
			//数量
			String quantity = request.getParameter("quantity");
//			String quantity = new String(request.getParameter("quantity").getBytes("ISO-8859-1"),"UTF-8");
			params.put("quantity", quantity);
			//测试项目
//			String testitems = new String(request.getParameter("testitems").getBytes("ISO-8859-1"),"UTF-8");
			String testitems = request.getParameter("testitems");
			params.put("testitems", testitems);
			//测试方法
			String testmethod = request.getParameter("testmethod");
			params.put("testmethod", testmethod);
			//服务商
			String serviceprovider = request.getParameter("serviceprovider");
			params.put("serviceprovider", serviceprovider);
			//归属人
			String ownperson = request.getParameter("ownperson");
			params.put("ownperson", ownperson);
			//起始时间
			params.put("foodSafety_starttime", request.getParameter("foodSafety_starttime"));
			params.put("foodSafety_endtime", request.getParameter("foodSafety_endtime"));
			//是否发报告
			params.put("reportif", request.getParameter("reportif"));
			//是否作废
			params.put("cancelif","0".equals(request.getParameter("cancelif"))?"":request.getParameter("cancelif"));
			//代理商
			String agent = request.getParameter("agent");
			params.put("agent", agent);
			//所属地
			params.put("areacode", "null".equals(request.getParameter("areacode"))?"":request.getParameter("areacode"));
			RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
			params.put("roleType", user.getRoletype());
			params.put("userCode", user.getUsercode());
			rdsFoodSafetyService.exportFoodSafe(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
