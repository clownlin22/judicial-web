package com.rds.bacera.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Controller
@RequestMapping("bacera/boneAge")
public class RdsBaceraBoneAgeController extends RdsBaceraAbstractController {

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
		return rdsBoneAgeService.queryAllPage(params);
	}

	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsBoneAgeService.delete(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}

	}

	@RequestMapping("save")
	@ResponseBody
	public Object save(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		Object id = params.get("id");
		if (id == null || id.equals("")) {
			return insert(params, request);
		} else {
			return update(params, request);
		}
	}

	@RequestMapping("saveFinance")
	@ResponseBody
	public Object saveFinance(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		Object id = params.get("id");
		// Object num = params.get("num");
		Map map = new HashMap();
		map.put("id", id);
		// map.put("num", num);
		int temp = 0;
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("input_person", user.getUserid());
		try {
			temp = rdsBoneAgeService.queryFinanceExit(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (temp == 0) {
			return insertFinance(params, request);
		} else if (temp > 0) {
			return updateFinance(params, request);
		} else {
			return this.setModel(false, RdsUpcConstantUtil.EXCEPTION);
		}
	}

	@RequestMapping("saveFinanceList")
	@ResponseBody
	public Object saveFinanceList(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		try {
			String[] ids = params.get("ids").toString().split(",");
			String[] receivables = params.get("receivables").toString().split(",");
			String[] num = params.get("num").toString().split(",");
			List<String> insertList = new ArrayList<>();
			List<String> receivablesInsertList = new ArrayList<>();
			List<String> numInsertList = new ArrayList<>();
			List<String> updateList = new ArrayList<>();
			List<String> numUpdateList = new ArrayList<>();
			List<String> receivablesUpdateList = new ArrayList<>();
			for(int i = 0 ; i < ids.length ; i ++){

				int temp = 0;
				Map map = new HashMap();
				map.put("id", ids[i]);
				temp = rdsBoneAgeService.queryFinanceExit(map);
				if (temp == 0){
					insertList.add(ids[i]);
					receivablesInsertList.add(receivables[i]);
					numInsertList.add(num[i]);
				}
				else{
					updateList.add(ids[i]);
					receivablesUpdateList.add(receivables[i]);
					numUpdateList.add(num[i]);
				}
			}
			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
					.getAttribute("user");
			params.put("input_person", user.getUserid());
			if (insertList.size() > 0) {
				params.put("insertList", insertList);
				params.put("receivablesInsertList", receivablesInsertList);
				params.put("numInsertList", numInsertList);
				rdsBoneAgeService.insertFinanceList(params);
			}
			if (updateList.size() > 0) {
				params.put("updateList", updateList);
				params.put("receivables", null);
				params.put("num", null);
				rdsBoneAgeService.updateFinanceList(params);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(false, "操作失败，请重试或联系管理员！");
		}

		return this.setModel(true, "操作成功！");
	}

	@RequestMapping("saveExpress")
	@ResponseBody
	public Object saveExpress(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		Object id = params.get("id");
		Object num = params.get("num");
		Map map = new HashMap();
		map.put("id", id);
		map.put("num", num);
		int temp = 0;
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("input_person", user.getUserid());
		try {
			temp = rdsBoneAgeService.queryExpressExit(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (temp == 0) {
			return insertExpress(params, request);
		} else if (temp > 0) {
			return updateExpress(params, request);
		} else {
			return this.setModel(false, RdsUpcConstantUtil.EXCEPTION);
		}
	}

	/**
	 * 新增骨龄检测信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	private Object insert(Map<String, Object> params, HttpServletRequest request) {
		try {
			params.put("id", RdsUpcUUIDUtil.getUUID());
			// Map<String, Object> map = new HashMap<String, Object>();
			// map.put("num", params.get("num").toString());
			// int numResult = rdsBoneAgeService.queryNumExit(map);
			// if (numResult > 0) {
			// return this.setModel(false, "编号已存在，请重新输入");
			// }
			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
					.getAttribute("user");
			params.put("inputperson", user.getUserid());
			// Map<String, Object> map = new HashMap<String, Object>();
			// if("".equals(params.get("agentid")))
			// map.put("ownperson", params.get("ownperson"));
			// else
			// map.put("agentid", params.get("agentid"));
			// String receivables = (null ==
			// rdsBoneAgeService.queryBoneFeeByRec(
			// map).getFees()) ? "" : rdsBoneAgeService
			// .queryBoneFeeByRec(map).getFees();
			// params.put("receivables", receivables);
			params.put("case_type", "骨龄");
			rdsBoneAgeService.insertFinance(params);
			int result = rdsBoneAgeService.insert(params);
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

	/**
	 * 更新骨龄检测信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	private Object update(Map<String, Object> params, HttpServletRequest request) {
		try {
			// Map<String, Object> map = new HashMap<String, Object>();
			// map.put("num", params.get("num").toString());
			// map.put("id", params.get("id").toString());
			// int numResult = rdsBoneAgeService.queryNumExit(map);
			// if (numResult <= 0) {
			// Map<String, Object> map1 = new HashMap<String, Object>();
			// map1.put("num", params.get("num").toString().trim());
			// if (rdsBoneAgeService.queryNumExit(map1) > 0)
			// return this.setModel(false, "编号已存在，请重新修改");
			// }
			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
					.getAttribute("user");
			params.put("inputperson", user.getUserid());
			rdsBoneAgeService.updateFinance(params);
			int result = rdsBoneAgeService.update(params);
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
	 * 插入财务信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	private Object insertFinance(Map<String, Object> params,
			HttpServletRequest request) {
		try {
			if ("audlt".equals(params.get("type"))) {
				params.put("case_type", "成人安全用药");
			} else if ("child".equals(params.get("type"))) {
				params.put("case_type", "儿童安全用药");
			}
			int result = rdsBoneAgeService.insertFinance(params);
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

	/**
	 * 更新财务信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	private Object updateFinance(Map<String, Object> params,
			HttpServletRequest request) {
		try {
			int result = rdsBoneAgeService.updateFinance(params);
			if (result > 0) {
				return this.setModel(true, "操作成功");
			} else {
				return this.setModel(false, "操作失败，请重试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}

	/**
	 * 插入快递信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	private Object insertExpress(Map<String, Object> params,
			HttpServletRequest request) {
		try {
			int result = rdsBoneAgeService.insertExpress(params);
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

	/**
	 * 更新快递信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	private Object updateExpress(Map<String, Object> params,
			HttpServletRequest request) {
		try {
			int result = rdsBoneAgeService.updateExpress(params);
			if (result > 0) {
				return this.setModel(true, "操作成功");
			} else {
				return this.setModel(false, "操作失败，请重试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}

	/**
	 * 根据归属人查询归属人代理商
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryAgentByRece")
	@ResponseBody
	public Object queryAgentByRece(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsBoneAgeService.queryAgentByRece(params);
	}

	/**
	 * 插入OA编号
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("insertOAnum")
	@ResponseBody
	public Object insertOAnum(@RequestBody Map<String, Object> params) {
		try {
			int result = rdsBoneAgeService.insertOAnum(params);
			if (result > 0) {
				return this.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
			} else {
				return this.setModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, RdsUpcConstantUtil.EXCEPTION
					+ "\n" + e.getMessage());
		}
	}

	/**
	 * 导出骨龄信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("exportBoneInfo")
	public void exportBoneInfo(HttpServletResponse response,
			HttpServletRequest request, HttpSession session) {
		try {

			Map<String, Object> params = new HashMap<String, Object>();
			String num = request.getParameter("num");
			params.put("num", num);
			String name = request.getParameter("name");
			params.put("name", name);
			String ownperson = request.getParameter("ownperson");
			params.put("ownperson", ownperson);
			params.put("bone_starttime", request.getParameter("bone_starttime"));
			params.put("bone_endtime", request.getParameter("bone_endtime"));
			params.put("reportif", request.getParameter("reportif"));
			params.put(
					"cancelif",
					"0".equals(request.getParameter("cancelif")) ? "" : request
							.getParameter("cancelif"));
			String agent = request.getParameter("agent");
			params.put("agent", agent);
			params.put("areacode",
					"null".equals(request.getParameter("areacode")) ? ""
							: request.getParameter("areacode"));
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			params.put("roleType", user.getRoletype());
			params.put("userCode", user.getUsercode());
			rdsBoneAgeService.exportBoneInfo(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("queryBoneFee")
	@ResponseBody
	public Object queryBoneFee(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsBoneAgeService.queryBoneFee(this.pageSet(params));
	}

	/**
	 * 保存骨龄人员对应财务应收金额
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("saveBoneFee")
	@ResponseBody
	public Object saveBoneFee(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		Object id = params.get("id");
		if (id == null || "".equals(id)) {
			try {
				// 判断该人员是否配置金额
				if (rdsBoneAgeService.queryBoneFeeCount(params) > 0) {
					return this.setModel(false, "该人员已配置过金额");
				} else {
					params.put("id", RdsUpcUUIDUtil.getUUID());
					RdsUpcUserModel user = (RdsUpcUserModel) request
							.getSession().getAttribute("user");
					params.put("inputperson", user.getUserid());
					int result = rdsBoneAgeService.saveBoneFee(params);
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
				// 判断是否为当前用户
				if (rdsBoneAgeService.queryBoneFeeCount(params) > 0) {
					RdsUpcUserModel user = (RdsUpcUserModel) request
							.getSession().getAttribute("user");
					params.put("inputperson", user.getUserid());
					int result = rdsBoneAgeService.updateBoneFee(params);
					if (result > 0) {
						return this.setModel(true, "操作成功");
					} else {
						return this.setModel(false, "操作成功");
					}
				} else {
					// 判断修改用户后该用户是否已配置
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userid", params.get("userid"));
					if (rdsBoneAgeService.queryBoneFeeCount(map) > 0) {
						return this.setModel(false, "该人员已配置过金额");
					} else {
						int result = rdsBoneAgeService.updateBoneFee(params);
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

	@RequestMapping("deleteBoneFee")
	@ResponseBody
	public Object deleteBoneFee(@RequestBody Map<String, Object> params) {
		try {
			rdsBoneAgeService.deleteBoneFee(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, RdsUpcConstantUtil.EXCEPTION);
		}

	}

	@RequestMapping("caseFeeConfirm")
	@ResponseBody
	public boolean caseFeeConfirm(@RequestBody Map<String, Object> params,
			HttpSession session) {
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			params.put("update_user", user.getUserid());
		}
		return rdsBoneAgeService.caseFeeConfirm(params);
	}
	
	@RequestMapping("confirmFinanceList")
	@ResponseBody
	public Object confirmFinanceList(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		try {
			String[] ids = params.get("ids").toString().split(",");
			List<String> list = new ArrayList<>();
			for (String string : ids) {
				list.add(string);
			}
			params.put("ids", list);
			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
					.getAttribute("user");
			params.put("confirm_per", user.getUserid());
			if(!rdsBoneAgeService.confirmFinanceList(params)){
				return this.setModel(false, "请刷新查看是否成功或联系管理员！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(false, "操作失败，请重试或联系管理员！");
		}

		return this.setModel(true, "操作成功！");
	}
	
}
