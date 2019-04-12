package com.rds.statistics.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.statistics.mapper.RdsFinanceConfigMapper;
import com.rds.statistics.mapper.RdsFinanceConfigNewMapper;
import com.rds.statistics.model.RdsFinanceOaMessageModel;
import com.rds.statistics.model.RdsStatisticsTypeModel;
import com.rds.statistics.model.RdsStatisticsTypeModel2;
import com.rds.statistics.service.RdsFinanceConfigService;
import com.rds.statistics.service.RdsFinanceOAService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("statistics/financeOA")
public class RdsFinanceOAController {

	// private static final String FILE_PATH = ConfigPath.getWebInfPath()
	// + "spring" + File.separatorChar + "properties" + File.separatorChar
	// + "config.properties";
	//
	// private static final String AMOEBA_PERMIT = PropertiesUtils.readValue(
	// FILE_PATH, "amoeba_permit");

	@Autowired
	private RdsFinanceConfigService rdsFinanceConfigService;

	@Autowired
	private RdsFinanceOAService rdsFinanceOAService;

	@Autowired
	private RdsFinanceConfigMapper rdsFinanceConfigMapper;

	@RequestMapping("queryAllPage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("deptcode", user.getDeptcode());
		// 查询阿米巴权限级别
		List<Map<String, Object>> listmap = rdsFinanceConfigMapper
				.selectUserLevel(user.getUsercode());
		String level = listmap.size() > 0 ? (listmap.get(0).get("userlevel") == null ? ""
				: listmap.get(0).get("userlevel").toString())
				: "";

		params.put("deptcode", user.getDeptcode());
		Map<String, String> mapParams = rdsFinanceConfigService
				.queryUserDepartment(params).get(0);
		if ("1".equals(level)) {
			// params.put("ztsybmc","");
		} else if ("2".equals(level)) {
			params.put("ztsybmc", mapParams.get("user_dept_level1"));
		} else {
			params.put("tdrxm", user.getUsername());
		}
		// params.put("deptcode", user.getDeptcode());
		// if (!AMOEBA_PERMIT.contains(user.getUsercode())) {
		// params.put("ztsybmc",
		// rdsFinanceConfigService.queryUserDepartment(params)
		// .get(0).get("user_dept_level1"));
		// }
		if (null != params.get("amoebakmmc")) {
			List<String> list = new ArrayList<>();
			String[] amoebakmmc = params.get("amoebakmmc").toString()
					.split(",");
			for (String string : amoebakmmc) {
				list.add(string);
			}
			params.put("amoebakmmc", list);
		}
		if (null != params.get("amoebakmmc1")) {
			List<String> list = new ArrayList<>();
			String[] amoebakmmc1 = params.get("amoebakmmc1").toString()
					.split(",");
			for (String string : amoebakmmc1) {
				list.add(string);
			}
			params.put("amoebakmmc1", list);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", rdsFinanceOAService.queryAllPage(params));
		map.put("total", rdsFinanceOAService.queryAllCount(params));
		return map;
	}

	@RequestMapping("exportFinanceOA")
	@ResponseBody
	public void exportFinanceOA(HttpServletRequest request,
			HttpServletResponse response) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String amoebakmmc = request.getParameter("amoebakmmc");
			if (null != amoebakmmc) {
				List<String> list = new ArrayList<>();
				String[] amoebakmmcs = amoebakmmc.toString().split(",");
				for (String string : amoebakmmcs) {
					list.add(string);
				}
				params.put("amoebakmmc", list);
			}

			String amoebakmmc1 = request.getParameter("amoebakmmc1");
			if (null != amoebakmmc1) {
				List<String> list = new ArrayList<>();
				String[] amoebakmmc1s = amoebakmmc1.toString().split(",");
				for (String string : amoebakmmc1s) {
					list.add(string);
				}
				params.put("amoebakmmc1", list);
			}
			String kmmc = request.getParameter("kmmc");
			params.put("kmmc", kmmc);
			String djbh = request.getParameter("djbh");
			params.put("djbh", djbh);
			String user_dept_level = request.getParameter("user_dept_level");
			params.put("user_dept_level", user_dept_level);
			String sqrxm = request.getParameter("sqrxm");
			params.put("sqrxm", sqrxm);
			String tdrxm = request.getParameter("tdrxm");
			params.put("tdrxm", tdrxm);
			String sqrq_start = request.getParameter("sqrq_start");
			params.put("sqrq_start", sqrq_start);
			String sqrq_end = request.getParameter("sqrq_end");
			params.put("sqrq_end", sqrq_end);
			String operatedate_start = request
					.getParameter("operatedate_start");
			params.put("operatedate_start", operatedate_start);
			String operatedate_end = request.getParameter("operatedate_end");
			params.put("operatedate_end", operatedate_end);

			params.put("deptcode", user.getDeptcode());
			// 查询阿米巴权限级别
			List<Map<String, Object>> listmap = rdsFinanceConfigMapper
					.selectUserLevel(user.getUsercode());
			String level = listmap.size() > 0 ? (listmap.get(0)
					.get("userlevel") == null ? "" : listmap.get(0)
					.get("userlevel").toString()) : "";

			params.put("deptcode", user.getDeptcode());
			Map<String, String> mapParams = rdsFinanceConfigService
					.queryUserDepartment(params).get(0);
			if ("1".equals(level)) {
				// params.put("ztsybmc","");
			} else if ("2".equals(level)) {
				params.put("ztsybmc", mapParams.get("user_dept_level1"));
			} else {
				params.put("tdrxm", user.getUsername());
			}

			// params.put("deptcode", user.getDeptcode());
			// if (!AMOEBA_PERMIT.contains(user.getUsercode())) {
			// params.put("ztsybmc",
			// rdsFinanceConfigService.queryUserDepartment(params)
			// .get(0).get("user_dept_level1"));
			// }
			rdsFinanceOAService.exportFinanceOAAll(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@RequestMapping("queryUserDept1")
	@ResponseBody
	public List<RdsStatisticsTypeModel2> queryUserDept(@RequestBody Map<String, Object> params) {
		return rdsFinanceOAService.queryUserDeptToModel();
	}
	@RequestMapping("queryUserDept2")
	@ResponseBody
	public List<RdsStatisticsTypeModel2> queryUserDept2(@RequestBody Map<String, Object> params) {
		return rdsFinanceOAService.queryUserDeptToModel2(params);
	}
	
	public RdsFinanceOaMessageModel setModel(boolean result, String message){
		RdsFinanceOaMessageModel model = new RdsFinanceOaMessageModel();
		model.setResult(result);
		model.setMessage(message);
		return model;
	}
	
	@RequestMapping("updateOAInfo")
	@ResponseBody
	public RdsFinanceOaMessageModel updateOAInfo(@RequestBody Map<String, Object> params,
			HttpSession session) {
		return  rdsFinanceOAService.updateOAInfo(params)?this.setModel(true, "修改成功"):this.setModel(false, "修改失败");
	}

	@RequestMapping("updateOAdept")
	@ResponseBody
	public Object updateOAdept(@RequestBody Map<String, Object> params,
			HttpSession session) {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		if (null == user)
			return false;
		params.put("operatePer", user.getUserid());
		params.put("username", user.getUsername());
		Map<String, Object> result = new HashMap<String, Object>();
		if (rdsFinanceOAService.updateOAdept(params)) {
			result.put("success", true);
			result.put("msg", "操作成功");
		} else {
			result.put("success", false);
			result.put("msg", "操作失败，请稍后重试或联系管理员！");
		}
		return result;
	}

	@RequestMapping("queryOAtypePage")
	@ResponseBody
	public Object queryOAtypePage(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", rdsFinanceOAService.queryAllPage(params));
		map.put("total", rdsFinanceOAService.queryAllCount(params));
		return map;
	}

	@RequestMapping("insertOrUpdateOAtype")
	@ResponseBody
	public Object insertOrUpdateOAtype(@RequestBody Map<String, Object> params,
			HttpSession session) {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		if (null == user)
			return false;
		Map<String, Object> result = new HashMap<String, Object>();
		params.put("operateper", user.getUserid());
		try {
			if (null == params.get("id") || "".equals(params.get("id"))) {
				params.put("id", UUIDUtil.getUUID());
				if (rdsFinanceOAService.insertOAtype(params)) {
					result.put("success", true);
					result.put("msg", "操作成功");
				} else {
					result.put("success", false);
					result.put("msg", "操作失败，请稍后重试或联系管理员！");
				}
			} else {
				if (rdsFinanceOAService.updateOAtype(params)) {
					result.put("success", true);
					result.put("msg", "操作成功");
				} else {
					result.put("success", false);
					result.put("msg", "操作失败，请稍后重试或联系管理员！");
				}
			}
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "出现异常，请联系管理员！");
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("deleteOAtype")
	@ResponseBody
	public Object deleteOAtype(@RequestBody Map<String, Object> params,
			HttpSession session) {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		if (null == user)
			return false;
		Map<String, Object> result = new HashMap<String, Object>();
		params.put("operateper", user.getUserid());
		try {
			if(rdsFinanceOAService.deleteOAtype(params)){
				result.put("success", true);
				result.put("msg", "操作成功");
			}else
			{
				result.put("success", false);
				result.put("msg", "操作失败，请稍后重试或联系管理员！");
			}
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "出现异常，请联系管理员！");
			e.printStackTrace();
		}
		return result;
	}
}
