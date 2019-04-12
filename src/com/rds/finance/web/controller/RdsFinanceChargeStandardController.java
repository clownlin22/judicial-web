package com.rds.finance.web.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.JScriptInvoke;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.finance.model.RdsFinanceAscriptionInfo;
import com.rds.finance.service.RdsFinanceChargeStandardService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.service.RdsUpcUserService;
import com.rds.upc.web.common.RdsUpcConstantUtil;

@Controller
@RequestMapping("finance/chargeStandard")
public class RdsFinanceChargeStandardController extends
		RdsFinanceAbstractController {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String USER_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "user_permit");

	@Setter
	@Autowired
	private RdsFinanceChargeStandardService rdsFinanceChargeStandardService;

	@Setter
	@Autowired
	private RdsUpcUserService rdsUpcUserService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping({ "saveStandardOld" })
	@ResponseBody
	public Object saveStandardOld(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		try {
			JScriptInvoke.getStandardFee(params.get("equation").toString()
					.replaceAll("\n", ""), Integer.valueOf(1),
					Integer.valueOf(1));
		} catch (Exception e) {
			params.clear();
			params.put("result", Boolean.valueOf(false));
			params.put("message", "新增失败，公式有误!");
			return params;
		}

		Map<String, Object> map = new HashMap<>();
		String[] areacodes = params.get("areacode").toString().split(",");
		int result = 0;
		//遍历多个地区插入归属
		for (String areacode : areacodes) {
			map.put("id", UUIDUtil.getUUID());
			//插入人员
			map.put("createuserid", user.getUserid());
			//归属地
			map.put("areacode", areacode);
			Map<String, Object> list = new HashMap();
			list.put("areacode", areacode);
			map.put("areaname", ((Map)rdsFinanceChargeStandardService
					.queryModel(list)).get("areaname"));
			map.put("areainitials", ((Map) this.rdsFinanceChargeStandardService
					.queryAreaInitials(list)).get("initials"));
			if (params.get("username") != null) {
				map.put("userinitials", StringUtils.getInitials(params.get(
						"username").toString()));
			}
			if ((params.get("userid") != null)
					&& (!"".equals(params.get("userid")))) {
				list.put("userid", params.get("userid"));
				RdsUpcUserModel users = (RdsUpcUserModel) this.rdsUpcUserService
						.queryModel(list);
				if (this.rdsFinanceChargeStandardService
						.queryMarketByAgent(list) != null) {
					map.put("agentid",
							((Map) this.rdsFinanceChargeStandardService
									.queryMarketByAgent(list)).get("userid"));
					map.put("agentname",
							((Map) this.rdsFinanceChargeStandardService
									.queryMarketByAgent(list)).get("username"));
				}
				map.put("companyid", users.getCompanyid());
			}else{
				return setModel(false, RdsUpcConstantUtil.INSERT_FAILED);
			}
			map.put("equation", params.get("equation"));
			map.put("discountrate", params.get("discountrate"));
			map.put("remark", params.get("remark"));
			map.put("username", params.get("username"));
			map.put("type", params.get("type"));
			map.put("userid", params.get("userid"));
			map.put("attach_need", params.get("attach_need"));
			result = rdsFinanceChargeStandardService.insertOld(map);

		}
		return result > 0 ? setModel(true, RdsUpcConstantUtil.INSERT_SUCCESS)
				: setModel(false, RdsUpcConstantUtil.INSERT_FAILED);

	}

	@RequestMapping("saveStandard")
	@ResponseBody
	public Object saveStandard(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		Object id = params.get("id");
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		// 收费公式自动生成
		String equation = "function exe(a,b){";
		equation += " if(b==1) return " + params.get("singlePrice") + "+"
				+ params.get("samplePrice") + "*(a-2)-"
				+ params.get("gapPrice");
		equation += "; else if(b==2) return " + params.get("doublePrice") + "+"
				+ params.get("samplePrice") + "*(a-3)-"
				+ params.get("gapPrice");
		equation += "; else return " + params.get("samplePrice") + "*a;}";

		System.out.println("equation==================" + equation);

		try {
			JScriptInvoke.getStandardFee(
					equation.toString().replaceAll("\n", ""), 1, 1);
		} catch (Exception e) {
			e.printStackTrace();
			params.clear();
			params.put("result", false);
			params.put("message", "新增失败，配置有误，请联系管理员!");
			return params;
		}
		// 新增公式
		if (null == id || "".equals(id)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("areacode", params.get("areacode"));
			map.put("userid", params.get("userid"));
			map.put("type", params.get("type"));
			map.put("source_type", params.get("source_type"));
			map.put("program_type", params.get("program_type"));
			// 判断该人员或地区是否添加过收费公式
			if (rdsFinanceChargeStandardService.queryExistCount(map) > 0)
				return this.setModel(false, "该人员或地区收费已配置，请查看！");
			else
				return insertStandard(user, params, equation) > 0 ? this
						.setModel(true, RdsUpcConstantUtil.INSERT_SUCCESS)
						: this.setModel(true, RdsUpcConstantUtil.INSERT_FAILED);
		}
		// 更新
		else {
			// 判断该收费标准是否已配置
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("areacode", params.get("areacode"));
			map.put("userid", params.get("userid"));
			map.put("type", params.get("type"));
			map.put("source_type", params.get("source_type"));
			map.put("program_type", params.get("program_type"));
			map.put("id", params.get("id"));
			// 判断修改的收费标准是否修改了人员或地区
			if (rdsFinanceChargeStandardService.queryExistCount(map) > 0) {
				return updateStandard(user, params, equation) > 0 ? this
						.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS)
						: this.setModel(true, RdsUpcConstantUtil.UPDATE_FAILED);
			} else {
				map.clear();
				map.put("areacode", params.get("areacode"));
				map.put("userid", params.get("userid"));
				map.put("type", params.get("type"));
				map.put("source_type", params.get("source_type"));
				map.put("program_type", params.get("program_type"));
				if (rdsFinanceChargeStandardService.queryExistCount(map) > 0)
					return this.setModel(false, "该人员或地区收费已配置，请查看！");
				else
					return updateStandard(user, params, equation) > 0 ? this
							.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS)
							: this.setModel(true,
									RdsUpcConstantUtil.UPDATE_FAILED);
			}

		}

	}

	/**
	 * 批量更新价格配置
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("updateStandards")
	@ResponseBody
	public Object updateStandards(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		try {
			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
					.getAttribute("user");
			// 收费公式自动生成
			String equation = "function exe(a,b){";
			equation += " if(b==1) return " + params.get("singlePrice") + "+"
					+ params.get("samplePrice") + "*(a-2)-"
					+ params.get("gapPrice");
			equation += "; else if(b==2) return " + params.get("doublePrice")
					+ "+" + params.get("samplePrice") + "*(a-3)-"
					+ params.get("gapPrice");
			equation += "; else return " + params.get("samplePrice") + "*a;}";

			System.out.println("equation==================" + equation);

			try {
				JScriptInvoke.getStandardFee(
						equation.toString().replaceAll("\n", ""), 1, 1);
			} catch (Exception e) {
				params.clear();
				params.put("result", false);
				params.put("message", "新增失败，配置有误，请联系管理员!");
				return params;
			}
			params.put("createuserid", user.getUserid());
			params.put("equation", equation);
			if (rdsFinanceChargeStandardService.updates(params) > 0)
				return this.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
			else
				return this.setModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
		} catch (Exception e) {
			return this.setModel(true, RdsUpcConstantUtil.EXCEPTION);
		}

	}

	@SuppressWarnings({ "rawtypes" })
	private int insertStandard(RdsUpcUserModel user,
			Map<String, Object> params, String equation) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", UUIDUtil.getUUID());
		// 创建人id
		map.put("createuserid", user.getUserid());
		// 保存县code
		map.put("areacode", params.get("areacode"));
		// 临时map
		Map<String, Object> list = new HashMap<String, Object>();
		// 判断该用户是否存在代理情况，如存在保存被代理人信息
		if (null != params.get("userid") && !"".equals(params.get("userid"))) {
			list.put("userid", params.get("userid"));
			// RdsUpcUserModel users =
			// (RdsUpcUserModel)rdsUpcUserService.queryModel(list);
			if (null != rdsFinanceChargeStandardService
					.queryMarketByAgent(list)) {
				map.put("agentid", ((Map) rdsFinanceChargeStandardService
						.queryMarketByAgent(list)).get("userid"));
				map.put("agentname", ((Map) rdsFinanceChargeStandardService
						.queryMarketByAgent(list)).get("username"));
			}
			// 保存归属人的公司id
			// map.put("companyid", users.getCompanyid());
		}
		// 公式
		map.put("equation", equation);
		// 备注
		map.put("remark", params.get("remark"));
		map.put("type", params.get("type"));
		map.put("source_type", params.get("source_type"));
		map.put("program_type", params.get("program_type"));
		map.put("userid", params.get("userid"));
		map.put("username", params.get("username"));
		map.put("singlePrice", params.get("singlePrice"));
		map.put("doublePrice", params.get("doublePrice"));
		map.put("samplePrice", params.get("samplePrice"));
		map.put("gapPrice", params.get("gapPrice"));
		map.put("specialPirce", params.get("specialPirce"));
		map.put("specialPirce1", params.get("specialPirce1"));
		map.put("specialPirce2", params.get("specialPirce2"));
		map.put("areaname", params.get("areaname"));
		map.put("urgentPrice", params.get("urgentPrice"));
		map.put("urgentPrice1", params.get("urgentPrice1"));
		map.put("urgentPrice2", params.get("urgentPrice2"));
		return rdsFinanceChargeStandardService.insert(map);
	}

	@SuppressWarnings({ "rawtypes" })
	private int updateStandard(RdsUpcUserModel user,
			Map<String, Object> params, String equation) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", params.get("id"));
		// 创建人id
		map.put("createuserid", user.getUserid());
		// 保存县code
		map.put("areacode", params.get("areacode"));

		// 临时map
		Map<String, Object> list = new HashMap<String, Object>();
		// 判断该用户是否存在代理情况，如存在保存被代理人信息
		if (null != params.get("userid") && !"".equals(params.get("userid"))) {
			list.put("userid", params.get("userid"));
			// RdsUpcUserModel users =
			// (RdsUpcUserModel)rdsUpcUserService.queryModel(list);
			if (null != rdsFinanceChargeStandardService
					.queryMarketByAgent(list)) {
				map.put("agentid", ((Map) rdsFinanceChargeStandardService
						.queryMarketByAgent(list)).get("userid"));
				map.put("agentname", ((Map) rdsFinanceChargeStandardService
						.queryMarketByAgent(list)).get("username"));
			}
			// 保存归属人的公司id
			// map.put("companyid", users.getCompanyid());
		}
		// 公式
		map.put("equation", equation);
		// 备注
		map.put("remark", params.get("remark"));
		map.put("username", params.get("username"));
		map.put("type", params.get("type"));
		map.put("source_type", params.get("source_type"));
		map.put("program_type", params.get("program_type"));
		map.put("userid", params.get("userid"));
		map.put("singlePrice", params.get("singlePrice"));
		map.put("doublePrice", params.get("doublePrice"));
		map.put("samplePrice", params.get("samplePrice"));
		map.put("gapPrice", params.get("gapPrice"));
		map.put("specialPirce", params.get("specialPirce"));
		map.put("specialPirce1", params.get("specialPirce1"));
		map.put("specialPirce2", params.get("specialPirce2"));
		map.put("areaname", params.get("areaname"));
		map.put("urgentPrice", params.get("urgentPrice"));
		map.put("urgentPrice1", params.get("urgentPrice1"));
		map.put("urgentPrice2", params.get("urgentPrice2"));
		return rdsFinanceChargeStandardService.update(map);
	}

	@Override
	public Integer insert(Map<String, Object> params) throws Exception {
		return null;
	}

	@Override
	public Integer update(Map<String, Object> params) throws Exception {
		return null;
	}

	@Override
	public Object delete(Map<String, Object> params) throws Exception {
		int result = rdsFinanceChargeStandardService.delete(params);
		return result > 0 ? this.setModel(true,
				RdsUpcConstantUtil.DELETE_SUCCESS) : this.setModel(true,
				RdsUpcConstantUtil.DELETE_FAILED);
	}

	@Override
	public Object queryAll(Map<String, Object> params) throws Exception {
		return rdsFinanceChargeStandardService.queryAll(params);
	}

	@Override
	public Object queryAllPage(Map<String, Object> params) throws Exception {
		return null;
	}

	@RequestMapping("queryPage")
	@ResponseBody
	public Object queryPage(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (!USER_PERMIT.contains(user.getUsercode()))
			params.put("userid", user.getUserid());
		params = this.pageSet(params);
		return rdsFinanceChargeStandardService.queryAllPage(params);
	}

	/**
	 * 无创公式配置
	 * 
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryInvasivePage")
	@ResponseBody
	public Object queryInvasivePage(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (!USER_PERMIT.contains(user.getUsercode()))
			params.put("userid", user.getUserid());
		params = this.pageSet(params);
		return rdsFinanceChargeStandardService.queryInvasiveAllPage(params);
	}

	@Override
	public Object queryModel(Map<String, Object> params) throws Exception {
		return rdsFinanceChargeStandardService.queryModel(params);
	}

	/**
	 * 获取接收员工
	 */
	@RequestMapping("getAscriptionPer")
	@ResponseBody
	public List<RdsFinanceAscriptionInfo> getAscriptionPer(String query,
			HttpServletRequest request) {
		String id = request.getParameter("id");
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!USER_PERMIT.contains(user.getUsercode()))
			map.put("companyid", user.getCompanyid());
		map.put("query", query==null?"":query.toLowerCase());
		if ("".equals(query) || null == query)
			map.put("id", id);
		try {
			return rdsFinanceChargeStandardService.queryAscription(map);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping("queryfianceSpecialPage")
	@ResponseBody
	public Object queryfianceSpecialPage(
			@RequestBody Map<String, Object> params, HttpServletRequest request)
			throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if (!USER_PERMIT.contains(user.getUsercode()))
			params.put("userid", user.getUserid());
		params = this.pageSet(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsFinanceChargeStandardService
				.queryCountSpecialFinance(params));
		result.put("data",
				rdsFinanceChargeStandardService.queryAllSpecialFinance(params));
		return result;
	}

	@RequestMapping("saveFinanceSpecial")
	@ResponseBody
	public Object saveFinanceSpecial(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("userid", user.getUserid());
		params.put("usercode", user.getUsercode());

		return rdsFinanceChargeStandardService.insertApplicationCode(params) > 0 ? this
				.setModel(true, RdsUpcConstantUtil.INSERT_SUCCESS) : this
				.setModel(false, RdsUpcConstantUtil.INSERT_FAILED);

	}

	@RequestMapping("updateFinanceSpecial")
	@ResponseBody
	public Object updateFinanceSpecial(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("confirm_per", user.getUserid());
		params.put("usercode", user.getUsercode());
		return rdsFinanceChargeStandardService.updateApplicationCode(params);
	}

	@RequestMapping("queryFinanceSpecialExist")
	@ResponseBody
	public Object queryFinanceSpecialExist(
			@RequestBody Map<String, Object> params) throws Exception {
		return rdsFinanceChargeStandardService.queryFinanceSpecialExist(params);
	}

	// @RequestMapping("sendMessage")
	// @ResponseBody
	// public Object sendMessage(@RequestBody Map<String, Object> params,
	// HttpServletRequest request) throws Exception {
	// RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
	// .getAttribute("user");
	// params.put("confirm_per", user.getUserid());
	// CRMessageUtils send = new CRMessageUtils();
	// boolean result = send.sendMessage("111", "18136869987");
	// return this.setModel(true, "激活成功！");
	// }
	//
	@RequestMapping("deleteConfirm")
	@ResponseBody
	public Object deleteConfirm(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("userid", user.getUserid());
		return rdsFinanceChargeStandardService.deleteConfirm(params);
	}

	@RequestMapping("saveInvasiveStandard")
	@ResponseBody
	public Object saveInvasiveStandard(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		Object id = params.get("id");
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		// 新增收费配置
		if (null == id || "".equals(id)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("areacode", params.get("areacode"));
			map.put("program_type", params.get("program_type"));
			map.put("hospital", params.get("hospital"));
			// 判断该人员或地区是否添加过收费公式
			if (rdsFinanceChargeStandardService.queryExistInversiveCount(map) > 0)
				return this.setModel(false, "该人员或地区收费已配置，请查看！");
			else
				return insertInvasiveStandard(user, params) > 0 ? this
						.setModel(true, RdsUpcConstantUtil.INSERT_SUCCESS)
						: this.setModel(true, RdsUpcConstantUtil.INSERT_FAILED);
		}
		// 更新
		else {
			// 判断该收费标准是否已配置
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("areacode", params.get("areacode"));
			map.put("program_type", params.get("program_type"));
			map.put("id", params.get("id"));
			map.put("hospital", params.get("hospital"));
			// 判断修改的收费标准是否修改了人员或地区
			if (rdsFinanceChargeStandardService.queryExistInversiveCount(map) > 0) {
				return updateInvasiveStandard(user, params) > 0 ? this
						.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS)
						: this.setModel(true, RdsUpcConstantUtil.UPDATE_FAILED);
			} else {
				map.clear();
				map.put("areacode", params.get("areacode"));
				map.put("program_type", params.get("program_type"));
				map.put("hospital", params.get("hospital"));
				if (rdsFinanceChargeStandardService
						.queryExistInversiveCount(map) > 0)
					return this.setModel(false, "该人员或地区收费已配置，请查看！");
				else
					return updateInvasiveStandard(user, params) > 0 ? this
							.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS)
							: this.setModel(true,
									RdsUpcConstantUtil.UPDATE_FAILED);
			}

		}

	}

	private int insertInvasiveStandard(RdsUpcUserModel user,
			Map<String, Object> params) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", UUIDUtil.getUUID());
		// 创建人id
		map.put("createuserid", user.getUserid());
		// 保存县code
		map.put("areacode", params.get("areacode"));
		// 所属医院
		map.put("hospital", params.get("hospital"));
		// 备注
		map.put("remark", params.get("remark"));
		map.put("program_type", params.get("program_type"));
		map.put("samplePrice", params.get("samplePrice"));
		map.put("areaname", params.get("areaname"));
		map.put("urgentPrice", params.get("urgentPrice"));
		map.put("urgentPrice1", params.get("urgentPrice1"));
		map.put("urgentPrice2", params.get("urgentPrice2"));
		return rdsFinanceChargeStandardService.insertInvasiveStandard(map);
	}

	private int updateInvasiveStandard(RdsUpcUserModel user,
			Map<String, Object> params) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", params.get("id"));
		// 创建人id
		map.put("createuserid", user.getUserid());
		// 保存县code
		map.put("areacode", params.get("areacode"));
		map.put("hospital", params.get("hospital"));
		// 备注
		map.put("remark", params.get("remark"));
		map.put("program_type", params.get("program_type"));
		map.put("samplePrice", params.get("samplePrice"));
		map.put("areaname", params.get("areaname"));
		map.put("urgentPrice", params.get("urgentPrice"));
		map.put("urgentPrice1", params.get("urgentPrice1"));
		map.put("urgentPrice2", params.get("urgentPrice2"));
		return rdsFinanceChargeStandardService.updateInvasiveStandard(map);
	}

	@RequestMapping("deleteInvasiveStandard")
	@ResponseBody
	public Object deleteInvasiveStandard(@RequestBody Map<String, Object> params)
			throws Exception {
		int result = rdsFinanceChargeStandardService
				.deleteInvasiveStandard(params);
		return result > 0 ? this.setModel(true,
				RdsUpcConstantUtil.DELETE_SUCCESS) : this.setModel(true,
				RdsUpcConstantUtil.DELETE_FAILED);
	}

}
