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
import com.rds.bacera.service.RdsBaceraInvasiveDNAService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("bacera/invasiveDNA")
public class RdsBaceraInvasiveDNAController extends RdsBaceraAbstractController {

	@Setter
	@Autowired
	private RdsBaceraInvasiveDNAService rdsInvasiveDNAService;
	
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
//		params = this.pageSet(params);
		return rdsInvasiveDNAService.queryAllPage(params);
	}

	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsInvasiveDNAService.delete(params);
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
			map.put("num", params.get("num").toString());
			int numResult = rdsInvasiveDNAService.queryNumExit(map);
			if (numResult > 0) {
				return this.setModel(false, "编号已存在，请重新输入");
			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
			
			params.put("case_type", "无创亲子鉴定");
			rdsBoneAgeService.insertFinance(params);
//			Map<String, Object> map	= new HashMap<String, Object>();
//			if("".equals(params.get("agentid")))
//				map.put("ownperson", params.get("ownperson"));
//			else
//				map.put("agentid", params.get("agentid"));
//			
//			String receivables = (null == rdsInvasiveDNAService.queryInvasiveDNAFeeByRec(map).getFees()) ? "" : rdsInvasiveDNAService
//					.queryInvasiveDNAFeeByRec(map).getFees();
//			params.put("case_type", "无创亲子鉴定");
//			params.put("receivables", receivables);
//			rdsBoneAgeService.insertFinance(params);
			
			int result = rdsInvasiveDNAService.insert(params);
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
			map.put("num", params.get("num").toString());
			map.put("id", params.get("id").toString());
			int numResult = rdsInvasiveDNAService.queryNumExit(map);
			if (numResult <= 0) {
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("num", params.get("num").toString().trim());
				if (rdsInvasiveDNAService.queryNumExit(map1) > 0)
					return this.setModel(false, "编号已存在，请重新修改");
			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
			rdsBoneAgeService.updateFinance(params);
			int result = rdsInvasiveDNAService.update(params);
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
	 * 导出无创信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("exportInvasiveInfo")
	public void exportInvasiveInfo(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String num = request.getParameter("num");
			params.put("num", num);
			String name = request.getParameter("name");
			params.put("name", name);
			String ownperson = request.getParameter("ownperson");
			params.put("ownperson", ownperson);
			params.put("invasive_starttime", request.getParameter("invasive_starttime"));
			params.put("invasive_endtime", request.getParameter("invasive_endtime"));
			params.put("reportif", request.getParameter("reportif"));
			params.put("cancelif","0".equals(request.getParameter("cancelif"))?"":request.getParameter("cancelif"));
			String agent = request.getParameter("agent");
			params.put("agent", agent);
			params.put("areacode", "null".equals(request.getParameter("areacode"))?"":request.getParameter("areacode"));
			String invasiceNum = request.getParameter("invasiceNum");
			params.put("invasiceNum", invasiceNum); 
			String age = request.getParameter("age");
			params.put("age", age); 
			String gestationalAge = request.getParameter("gestationalAge");
			params.put("gestationalAge", gestationalAge);
			String inspectionunit = request.getParameter("inspectionunit");
			params.put("inspectionunit", inspectionunit); 
			params.put("invasive_sample_starttime", request.getParameter("invasive_sample_starttime")); 
			params.put("invasive_sample_endtime", request.getParameter("invasive_sample_endtime")); 
			params.put("expressnum", request.getParameter("expressnum")); 
			params.put("express_starttime", request.getParameter("express_starttime")); 
			params.put("express_endtime", request.getParameter("express_endtime")); 
			RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
			params.put("roleType", user.getRoletype());
			params.put("userCode", user.getUsercode());
			rdsInvasiveDNAService.exportInvasiveInfo(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("queryInvasiveDNAFee")
	@ResponseBody
	public Object queryInvasiveDNAFee(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsInvasiveDNAService.queryInvasiveDNAFee(this.pageSet(params));
	}
	
	/**
	 * 保存骨龄人员对应财务应收金额
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("saveInvasiveDNAFee")
	@ResponseBody
	public Object saveInvasiveDNAFee(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		Object id = params.get("id");
		if (id == null || "".equals(id)) {
			try {
				//判断该人员是否配置金额
				if (rdsInvasiveDNAService.queryInvasiveDNAFeeCount(params) > 0) {
					return this.setModel(false, "该人员已配置过金额");
				} else {
					params.put("id", RdsUpcUUIDUtil.getUUID());
					RdsUpcUserModel user = (RdsUpcUserModel) request
							.getSession().getAttribute("user");
					params.put("inputperson", user.getUserid());
					int result = rdsInvasiveDNAService.saveInvasiveDNAFee(params);
					if (result > 0) {
						return this.setModel(true,
								RdsUpcConstantUtil.INSERT_SUCCESS);
					} else {
						return this.setModel(false,
								RdsUpcConstantUtil.INSERT_FAILED);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return this.setModel(true, false, RdsUpcConstantUtil.EXCEPTION
						+ "\n" + e.getMessage());
			}
		} else {
			try {
				//判断是否为当前用户
				if (rdsInvasiveDNAService.queryInvasiveDNAFeeCount(params) > 0) {
					RdsUpcUserModel user = (RdsUpcUserModel) request
							.getSession().getAttribute("user");
					params.put("inputperson", user.getUserid());
					int result = rdsInvasiveDNAService.updateInvasiveDNAFee(params);
					if (result > 0) {
						return this.setModel(true, "操作成功");
					} else {
						return this.setModel(false, "操作成功");
					}
				} else {
					//判断修改用户后该用户是否已配置
					Map<String , Object> map = new HashMap<String, Object>();
					map.put("userid", params.get("userid"));
					if(rdsInvasiveDNAService.queryInvasiveDNAFeeCount(map) > 0)
					{
						return this.setModel(false, "该人员已配置过金额");
					}
					else
					{
						int result = rdsInvasiveDNAService.updateInvasiveDNAFee(params);
						if (result > 0) {
							return this.setModel(true, "操作成功");
						} else {
							return this.setModel(false, "操作成功");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return this.setModel(true, false, e.getMessage());
			}
		}
	}
	
	@RequestMapping("deleteInvasiveDNAFee")
	@ResponseBody
	public Object deleteInvasiveDNAFee(@RequestBody Map<String, Object> params) {
		try {
			rdsInvasiveDNAService.deleteInvasiveDNAFee(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, RdsUpcConstantUtil.EXCEPTION);
		}

	}

}
