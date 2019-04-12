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
import com.rds.bacera.service.RdsBaceraTumorPerService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("bacera/tumorPre")
public class RdsBaceraTumorPreController extends RdsBaceraAbstractController {

	@Setter
	@Autowired
	private RdsBaceraTumorPerService rdsTumorPerService;

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
		return rdsTumorPerService.queryAllPage(params);
	}

	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsTumorPerService.delete(params);
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
//			String testitems="";
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
//			if (params.get("testitems") instanceof List) {
//				List<String> list = (ArrayList<String>) params.get("testitems");
//				for (String str : list) {
//					testitems = testitems + str + ',';
//				}
//				testitems = testitems.substring(0, testitems.length() - 1);
//				params.put("testitems", testitems);
//			}
			params.put("case_type", "肿瘤个体");
			//更新财务应收
			rdsBoneAgeService.insertFinance(params);
			//更新基本信息
			int result = rdsTumorPerService.insert(params);
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
//			String testitems="";
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
//			if (params.get("testitems") instanceof List) {
//				List<String> list = (ArrayList<String>) params.get("testitems");
//				for (String str : list) {
//					testitems = testitems + str + ',';
//				}
//				testitems = testitems.substring(0, testitems.length() - 1);
//				params.put("testitems", testitems);
//			}
			//更新财务应收
			rdsBoneAgeService.updateFinance(params);
			//更新基本信息
			int result = rdsTumorPerService.update(params);
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
	 * 导出肿瘤个体
	 * @param response
	 * @param request
	 */
	@RequestMapping("exportTumorPer")
	public void exportTumorPer(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String num = request.getParameter("num");
			params.put("num", num);
			String name = request.getParameter("name");
			params.put("name", name);
			String ownperson = request.getParameter("ownperson");
			params.put("ownperson", ownperson);
			params.put("tumorPer_starttime", request.getParameter("tumorPer_starttime"));
			params.put("tumorPer_endtime", request.getParameter("tumorPer_endtime"));
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
			rdsTumorPerService.exportTumorPer(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分页查询肿瘤个体检测项目
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryTumorPerItems")
	@ResponseBody
	public Object queryTumorPerItems(@RequestBody Map<String, Object> params)
			throws Exception {
		params = this.pageSet(params);
		return rdsTumorPerService.queryTumorPerItems(params);
	}
	
	/**
	 * 查询肿瘤个体检测项目
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryAllTumorPerItems")
	@ResponseBody
	public Object queryAllTumorPerItems(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsTumorPerService.queryTumorPerItemsAll(params);
	}
	
	/**
	 * 保存修改肿瘤个体检测项目
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("saveTumorPerItems")
	@ResponseBody
	public Object saveTumorPerItems(@RequestBody Map<String, Object> params,HttpServletRequest request) {
		Object id = params.get("id");
		if (id == null || id.equals("")) {
			try {
				if(rdsTumorPerService.countTumorPerItems(params)>0)
				{
					return this.setModel(false, "该项目已配置过");
				}else{
					params.put("id", RdsUpcUUIDUtil.getUUID());
					RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
					params.put("inputperson", user.getUserid());
					int result = rdsTumorPerService.saveTumorPerItems(params);
					if (result > 0) {
						return this.setModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
					} else {
						return this.setModel(false, RdsUpcConstantUtil.INSERT_FAILED);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return this.setModel(true, false, RdsUpcConstantUtil.EXCEPTION
						+ "\n" + e.getMessage());
			}
		} else {
			try {
				RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
				params.put("inputperson", user.getUserid());
				if(rdsTumorPerService.countTumorPerItems(params)>0)
				{

					int result = rdsTumorPerService.updateTumorPerItems(params);
					if (result > 0) {
						return this.setModel(true, "操作成功");
					} else {
						return this.setModel(false, "操作成功");
					}
				}else
				{
					//判断修改用户后该用户是否已配置
					Map<String , Object> map = new HashMap<String, Object>();
					map.put("items_name", params.get("items_name"));
					if(rdsTumorPerService.countTumorPerItems(map) > 0)
					{
						return this.setModel(false, "该项目已配置过金额");
					}
					else
					{
						int result = rdsTumorPerService.updateTumorPerItems(params);
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
	

	/**
	 * 删除肿瘤个体配置检测项目
	 * @param params
	 * @return
	 */
	@RequestMapping("deleteTumorPerItems")
	@ResponseBody
	public Object deleteTumorPerItems(@RequestBody Map<String, Object> params) {
		try {
			rdsTumorPerService.deleteTumorPerItems(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}

	}
}
