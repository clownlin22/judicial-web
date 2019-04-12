package com.rds.statistics.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.JScriptInvoke;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.statistics.mapper.RdsFinanceConfigMapper;
import com.rds.statistics.model.RdsFinanceCaseDetailStatisticsModel;
import com.rds.statistics.service.RdsFinanceConfigService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("statistics/financeConfig")
public class RdsFinanceConfigController {
	
//	private static final String FILE_PATH = ConfigPath.getWebInfPath()
//			+ "spring" + File.separatorChar + "properties" + File.separatorChar
//			+ "config.properties";

//	private static final String AMOEBA_PERMIT = PropertiesUtils.readValue(
//			FILE_PATH, "amoeba_permit");
	
	@Autowired
	private RdsFinanceConfigService rdsFinanceConfigService;
	
	@Autowired
	private RdsFinanceConfigMapper rdsFinanceConfigMapper;

	@RequestMapping("queryPage")
	@ResponseBody
	public Object queryExperiment(@RequestBody Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", rdsFinanceConfigService.queryAll(params));
		map.put("total", rdsFinanceConfigService.queryAllCount(params));
		return map;
	}

	@RequestMapping("saveFinanceAptitude")
	@ResponseBody
	public Object saveFinanceAptitude(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("user", user);
		if (rdsFinanceConfigService.saveAptitudeConfig(params)) {
			map.put("success", true);
			map.put("msg", "操作成功!");
		} else {
			map.put("success", false);
			map.put("msg", "操作失败！请稍后再试，或联系管理员!");
		}
		return map;
	}

	@RequestMapping("saveFinanceConfig")
	@ResponseBody
	public Object saveFinanceConfig(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("user", user);
		if (null != params.get("front_settlement")
				&& !"".equals(params.get("front_settlement"))) {
			try {
				JScriptInvoke.getStandardFee(params.get("front_settlement")
						.toString().replaceAll("\n", ""), Integer.valueOf(1),
						Integer.valueOf(1));
			} catch (Exception e) {
				params.clear();
				params.put("result", false);
				params.put("msg", "新增失败，前段结算公式有误!");
				return params;
			}
		}
		if (null != params.get("finance_manage")
				&& !"".equals(params.get("finance_manage"))) {
			try {
				JScriptInvoke.getStandardFee(params.get("finance_manage")
						.toString().replaceAll("\n", ""), Integer.valueOf(1),
						Integer.valueOf(1));
			} catch (Exception e) {
				params.clear();
				params.put("result", false);
				params.put("msg", "新增失败，管理费公式有误!");
				return params;
			}
		}
		if (null != params.get("back_settlement")
				&& !"".equals(params.get("back_settlement"))) {
			try {
				JScriptInvoke.getStandardFee(params.get("back_settlement")
						.toString().replaceAll("\n", ""), Integer.valueOf(1),
						Integer.valueOf(1));
			} catch (Exception e) {
				params.clear();
				params.put("result", false);
				params.put("msg", "新增失败，后端结算公式有误!");
				return params;
			}
		}
		if (rdsFinanceConfigService.saveFinanceConfig(params)) {
			map.put("success", true);
			map.put("msg", "操作成功!");
		} else {
			map.put("success", false);
			map.put("msg", "操作失败！请稍后再试，或联系管理员!");
		}
		return map;
	}

	@RequestMapping("deleteFinanceConfig")
	@ResponseBody
	public Object deleteFinanceConfig(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("update_per", user.getUserid());
		if (rdsFinanceConfigService.deleteAptitudeConfig(params)) {
			map.put("success", true);
			map.put("msg", "操作成功!");
		} else {
			map.put("success", false);
			map.put("msg", "操作失败！请稍后再试，或联系管理员!");
		}
		return map;
	}

	@RequestMapping("financeCreate")
	@ResponseBody
	public Object financeCreate(@RequestBody Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
//			String[] last36Months = new String[36];
//			Calendar cal = Calendar.getInstance();
//			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1); // 要先+1,才能把本月的算进去</span>
//			for (int i = 0; i < 36; i++) {
//				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1); // 逐次往前推1个月
//				last36Months[35 - i] = cal.get(Calendar.YEAR)
//						+ "-"
//						+ ((cal.get(Calendar.MONTH) + 1) < 10 ? "0"
//								+ (cal.get(Calendar.MONTH) + 1) : (cal
//								.get(Calendar.MONTH) + 1));
//			}
//			for (String string : last36Months) {
//				params.put("accept_time", string);
//				rdsFinanceConfigService.finance_config(params);
//			}
//			rdsFinanceConfigService.finance_config(params);
			rdsFinanceConfigService.financeCaseDetail(params);
			map.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
		}
		return map;
	}

	@RequestMapping("queryFinanceConfig")
	public String queryFinanceConfig(HttpServletRequest request) {
		return "statistics/financeAmoeba";
	}

	@RequestMapping("queryAmoeba")
	@ResponseBody
	public List<List<String>> queryAmoeba(String confirm_date_start,
			String confirm_date_end,HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>();
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if(user == null)
			return null;
		params.put("deptcode", user.getDeptcode());
		List<Map<String,Object>> listmap = rdsFinanceConfigMapper.selectUserLevel(user.getUsercode());
		String level = listmap.size()>0?(listmap.get(0).get("userlevel") == null ? "" : listmap
				.get(0).get("userlevel").toString()):"";

		Map<String,String> mapParams = rdsFinanceConfigService.queryUserDepartment(params).get(0);
		List<String> listDeptTemp = new ArrayList<>();
		
		if("1".equals(level)){
			params.put("listDeptTemp", null);
		}else if ("2".equals(level)) {
			//所属事业部
			listDeptTemp.add(mapParams.get("user_dept_level1"));
			params.put("listDeptTemp", listDeptTemp);
		}else if("3".equals(level)){
			listDeptTemp.add(mapParams.get("user_dept_level1"));
			params.put("listDeptTemp", listDeptTemp);
		}else if("4".equals(level)){
			listDeptTemp.add(mapParams.get("user_dept_level1"));
			params.put("listDeptTemp", listDeptTemp);
		}else{
			listDeptTemp.add(mapParams.get("user_dept_level1"));
			params.put("listDeptTemp", listDeptTemp);
		}
		
//		if (!AMOEBA_PERMIT.contains(user.getUsercode())){
//			deptname = rdsFinanceConfigService.queryUserDepartment(params).get(0).get("user_dept_level1");
//			listDeptTemp.add(deptname);
//			params.put("listDeptTemp", listDeptTemp);
//		}
		params.put("confirm_date_start", confirm_date_start);
		params.put("confirm_date_end", confirm_date_end);
		List<List<String>> lists = rdsFinanceConfigService.queryAmoeba(params);
		return lists;
	}
	
	@RequestMapping("exportAmoeba")
	@ResponseBody
	public void exportAmoeba(String confirm_date_start,
			String confirm_date_end,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("deptcode", user.getDeptcode());
		List<Map<String,Object>> listmap = rdsFinanceConfigMapper.selectUserLevel(user.getUsercode());
		String level = listmap.size()>0?(listmap.get(0).get("userlevel") == null ? "" : listmap
				.get(0).get("userlevel").toString()):"";
		//设置权限导出
		Map<String,String> mapParams = rdsFinanceConfigService.queryUserDepartment(params).get(0);
		List<String> listDeptTemp = new ArrayList<>();
		
		if("1".equals(level)){
			params.put("listDeptTemp", null);
		}else if ("2".equals(level)) {
			//所属事业部
			listDeptTemp.add(mapParams.get("user_dept_level1"));
			params.put("listDeptTemp", listDeptTemp);
		}else if("3".equals(level)){
			listDeptTemp.add(mapParams.get("user_dept_level1"));
			params.put("listDeptTemp", listDeptTemp);
		}else if("4".equals(level)){
			listDeptTemp.add(mapParams.get("user_dept_level1"));
			params.put("listDeptTemp", listDeptTemp);
		}else{
			listDeptTemp.add(mapParams.get("user_dept_level1"));
			params.put("listDeptTemp", listDeptTemp);
		}
//		String deptname="";
//		params.put("deptcode", user.getDeptcode());
//		List<String> listDeptTemp = new ArrayList<>();
//		if (!AMOEBA_PERMIT.contains(user.getUsercode())){
//			deptname = rdsFinanceConfigService.queryUserDepartment(params).get(0).get("user_dept_level1");
//			listDeptTemp.add(deptname);
//			params.put("listDeptTemp", listDeptTemp);
//		}
		params.put("confirm_date_start", confirm_date_start);
		params.put("confirm_date_end", confirm_date_end);
		rdsFinanceConfigService.exportAmoeba(params,response);
	}
	

	@RequestMapping("exportAmoebaSecond")
	@ResponseBody
	public void exportAmoebaSecond(String deptname,String confirm_date_start,
			String confirm_date_end,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("deptcode", user.getDeptcode());
		List<Map<String,Object>> listmap = rdsFinanceConfigMapper.selectUserLevel(user.getUsercode());
		String level = listmap.size()>0?(listmap.get(0).get("userlevel") == null ? "" : listmap
				.get(0).get("userlevel").toString()):"";

		Map<String,String> mapParams = rdsFinanceConfigService.queryUserDepartment(params).get(0);
		List<String> listDeptTemp = new ArrayList<>();
		
		if("1".equals(level)){
			params.put("listDeptTemp", null);
		}else if ("2".equals(level)) {
			//所属事业部
			params.put("listDeptTemp", null);
		}else if("3".equals(level)){
			listDeptTemp.add(mapParams.get("user_dept_level2"));
			params.put("listDeptTemp", listDeptTemp);
		}else if("4".equals(level)){
			listDeptTemp.add(mapParams.get("user_dept_level2"));
			params.put("listDeptTemp", listDeptTemp);
		}else{
			listDeptTemp.add(mapParams.get("user_dept_level2"));
			params.put("listDeptTemp", listDeptTemp);
		}
//		String deptnameSecond="";
//		params.put("deptcode", user.getDeptcode());
//		List<String> listDeptTemp = new ArrayList<>();
//		if (!AMOEBA_PERMIT.contains(user.getUsercode())){
//			deptnameSecond = rdsFinanceConfigService.queryUserDepartment(params).get(0).get("user_dept_level1");
//			listDeptTemp.add(deptnameSecond);
//			params.put("listDeptTemp", listDeptTemp);
//		}
		params.put("confirm_date_start", confirm_date_start);
		params.put("confirm_date_end", confirm_date_end);
		params.put("deptname", deptname);
		rdsFinanceConfigService.exportAmoebaSecond(params,response);
	}
	
	
	@RequestMapping("queryAmoebaSecond")
	@ResponseBody
	public List<List<String>> queryAmoebaSecond(String deptname,String confirm_date_start,
			String confirm_date_end,HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>();
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if(user == null)
			return null;
		params.put("deptcode", user.getDeptcode());
		List<Map<String,Object>> listmap = rdsFinanceConfigMapper.selectUserLevel(user.getUsercode());
		String level = listmap.size()>0?(listmap.get(0).get("userlevel") == null ? "" : listmap
				.get(0).get("userlevel").toString()):"";

		Map<String,String> mapParams = rdsFinanceConfigService.queryUserDepartment(params).get(0);
		List<String> listDeptTemp = new ArrayList<>();
		
		if("1".equals(level)){
			params.put("listDeptTemp", null);
		}else if ("2".equals(level)) {
			//所属事业部
			params.put("listDeptTemp", null);
		}else if("3".equals(level)){
			listDeptTemp.add(mapParams.get("user_dept_level2"));
			params.put("listDeptTemp", listDeptTemp);
		}else if("4".equals(level)){
			listDeptTemp.add(mapParams.get("user_dept_level2"));
			params.put("listDeptTemp", listDeptTemp);
		}else{
			listDeptTemp.add(mapParams.get("user_dept_level2"));
			params.put("listDeptTemp", listDeptTemp);
		}
//		String deptnameSecond="";
//		params.put("deptcode", user.getDeptcode());
//		List<String> listDeptTemp = new ArrayList<>();
//		if (!AMOEBA_PERMIT.contains(user.getUsercode())){
//			deptnameSecond = rdsFinanceConfigService.queryUserDepartment(params).get(0).get("user_dept_level2");
//			listDeptTemp.add(deptnameSecond);
//			params.put("listDeptTemp", listDeptTemp);
//		}
		params.put("confirm_date_start", confirm_date_start);
		params.put("confirm_date_end", confirm_date_end);
		params.put("deptname", deptname);
		List<List<String>> lists = rdsFinanceConfigService.queryAmoebaSecond(params);
		return lists;
	}
	
	@RequestMapping("queryAmoebaDept")
	@ResponseBody
	public String queryAmoebaDept(HttpServletRequest request) {
		return "statistics/test";
	}
	@RequestMapping("queryPageAptitude")
	@ResponseBody
	public Object queryPageAptitude(@RequestBody Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", rdsFinanceConfigService.queryAptitude(params));
		map.put("total", rdsFinanceConfigService.queryCountAptitude(params));
		return map;
	}

	@RequestMapping("queryUserDept")
	@ResponseBody
	public Object queryUserDept(@RequestBody Map<String, Object> params) {
		List<Map<String, Object>> list = new ArrayList<>();
		List<String> lists = rdsFinanceConfigService.queryUserDept();
		for (String string : lists) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deptname", string);
			list.add(map);
		}
		return list;
	}
	
	@RequestMapping("queryCaseDetailAllPage")
	@ResponseBody
	public Object query(@RequestBody Map<String, Object> params,HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if(user == null)
			return null;
		List<Map<String,Object>> listmap = rdsFinanceConfigMapper.selectUserLevel(user.getUsercode());
		String level = listmap.size()>0?(listmap.get(0).get("userlevel") == null ? "" : listmap
				.get(0).get("userlevel").toString()):"";

		params.put("deptcode", user.getDeptcode());
		
		Map<String,String> mapParams = rdsFinanceConfigService.queryUserDepartment(params).get(0);
		if("1".equals(level)){
//			params.put("user_dept_level1","");
		}else if ("2".equals(level)) {
			params.put("user_dept_level1",mapParams.get("user_dept_level1"));
		}else if("3".equals(level)){
			params.put("user_dept_level1",mapParams.get("user_dept_level1"));
			params.put("user_dept_level2",mapParams.get("user_dept_level2"));
		}else if("4".equals(level)){
			params.put("user_dept_level1",mapParams.get("user_dept_level1"));
			params.put("user_dept_level2",mapParams.get("user_dept_level2"));
			params.put("user_dept_level3",mapParams.get("user_dept_level3"));
		}else{
			params.put("case_user",user.getUsername());
		}
		
//		String user_dept_level1="";
//		String user_dept_level2="";
//		params.put("deptcode", user.getDeptcode());
//		if (!AMOEBA_PERMIT.contains(user.getUsercode())){
//			if("1".equals(user.getUsertype())){
//				user_dept_level1 = rdsFinanceConfigService.queryUserDepartment(params).get(0).get("user_dept_level1");
//				params.put("user_dept_level1", user_dept_level1);
//			}else if("2".equals(user.getUsertype())){
//				user_dept_level1 = rdsFinanceConfigService.queryUserDepartment(params).get(0).get("user_dept_level1");
//				params.put("user_dept_level1", user_dept_level1);
//				user_dept_level2 = rdsFinanceConfigService.queryUserDepartment(params).get(0).get("user_dept_level2");
//				params.put("user_dept_level2", user_dept_level2);
//			}else 
//			{
//				params.put("case_user", user.getUsername());
//			}
//		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<RdsFinanceCaseDetailStatisticsModel> list = rdsFinanceConfigService.queryCaseDetailAll(params);
		//添加合计行
//		list.add(rdsFinanceConfigService.queryCaseDetailAllSum(params).get(0));
		map.put("data", list);
		map.put("total", rdsFinanceConfigService.queryCaseDetailAllCount(params));
		return map;
	}
	
	@RequestMapping("exportCaseDetail")
	@ResponseBody
	public void exportCaseDetail(HttpServletRequest request,HttpServletResponse response) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		try{
			Map<String, Object> params = new HashMap<String, Object>();
			String case_user = request.getParameter("case_user");
			params.put("case_user", case_user);
			String case_area = request.getParameter("case_area");
			params.put("case_area", case_area);
			String queryFlag = request.getParameter("case_area");
			params.put("queryFlag", queryFlag);
			String aptitude_flag = request.getParameter("aptitude_flag");
			params.put("aptitude_flag", aptitude_flag);
			String user_dept_level1 = request.getParameter("user_dept_level1");
			params.put("user_dept_level1", user_dept_level1);
			String user_dept_level2 = request.getParameter("user_dept_level2");
			params.put("user_dept_level2", user_dept_level2);
			String user_dept_level3 = request.getParameter("user_dept_level3");
			params.put("user_dept_level3", user_dept_level3);
			String insideCostUnit = request.getParameter("insideCostUnit");
			params.put("insideCostUnit", insideCostUnit);
			String manageCostUnit = request.getParameter("manageCostUnit");
			params.put("manageCostUnit", manageCostUnit);
			String case_type = request.getParameter("case_type");
			params.put("case_type", case_type);
			//收入类型
			String type = request.getParameter("type");
			params.put("type", type);
			//委托人
			String client = request.getParameter("client");
			params.put("client", client);
			//工号
			String webchart = request.getParameter("webchart");
			params.put("webchart", webchart);
			//财务确认时间
			String confirm_date_start = request.getParameter("confirm_date_start");
			params.put("confirm_date_start", confirm_date_start);
			String confirm_date_end = request.getParameter("confirm_date_end");
			params.put("confirm_date_end", confirm_date_end);
			//受理时间
			String accept_time_start = request.getParameter("accept_time_start");
			params.put("accept_time_start", accept_time_start);
			String accept_time_end = request.getParameter("accept_time_end");
			params.put("accept_time_end", accept_time_end);
			
			
			params.put("deptcode", user.getDeptcode());
			//查询阿米巴权限级别
			List<Map<String,Object>> listmap = rdsFinanceConfigMapper.selectUserLevel(user.getUsercode());
			String level = listmap.size()>0?(listmap.get(0).get("userlevel") == null ? "" : listmap
					.get(0).get("userlevel").toString()):"";
	
			params.put("deptcode", user.getDeptcode());
			Map<String,String> mapParams = rdsFinanceConfigService.queryUserDepartment(params).get(0);
			if("1".equals(level)){
//				params.put("user_dept_level1","");
			}else if ("2".equals(level)) {
				params.put("user_dept_level1",mapParams.get("user_dept_level1"));
			}else if("3".equals(level)){
				params.put("user_dept_level1",mapParams.get("user_dept_level1"));
				params.put("user_dept_level2",mapParams.get("user_dept_level2"));
			}else if("4".equals(level)){
				params.put("user_dept_level1",mapParams.get("user_dept_level1"));
				params.put("user_dept_level2",mapParams.get("user_dept_level2"));
				params.put("user_dept_level3",mapParams.get("user_dept_level3"));
			}else{
				params.put("case_user",user.getUsername());
			}
			rdsFinanceConfigService.exportCaseDetailAll(params,response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@RequestMapping("queryPageAmoeba")
	@ResponseBody
	public Object queryPageAmoeba(@RequestBody Map<String, Object> params,HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if(user == null)
			return null;
		//阿米巴数据权限设置
		List<Map<String,Object>> listmap = rdsFinanceConfigMapper.selectUserLevel(user.getUsercode());
		String level = listmap.size()>0?(listmap.get(0).get("userlevel") == null ? "" : listmap
				.get(0).get("userlevel").toString()):"";
		params.put("deptcode", user.getDeptcode());
		Map<String,String> mapParams = rdsFinanceConfigService.queryUserDepartment(params).get(0);
		if("1".equals(level)){
//			params.put("amoeba_deptment","");
		}else{
			params.put("amoeba_deptment",mapParams.get("user_dept_level1"));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", rdsFinanceConfigService.queryAmoebaInfoPage(params));
		map.put("total", rdsFinanceConfigService.queryCountAmoebaInfo(params));
		return map;
	}
	
	@RequestMapping("insertAmoebaInfo")
	@ResponseBody
	public Object insertAmoebaInfo(
			@RequestBody Map<String, Object> params, HttpSession session)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		if(null == user)
			return false;
		List<Map<String,Object>> list = new ArrayList<>();
		List<String> amoeba_deptment = getValues(params.get("amoeba_deptment").toString());
		List<String> amoeba_deptment1 = getValues(params.get("amoeba_deptment1").toString());
		List<String> amoeba_sum = getValues(params.get("amoeba_sum").toString());
		for (int i = 0 ; i < amoeba_deptment.size() ; i ++) {
			Map<String,Object> map = new HashMap<>();
			map.put("create_per", user.getUserid());
			map.put("amoeba_id", UUIDUtil.getUUID());
			map.put("amoeba_month", params.get("amoeba_month"));
			map.put("remark", params.get("remark"));
			map.put("amoeba_program", params.get("amoeba_program"));
			map.put("remark", params.get("remark"));
			map.put("amoeba_deptment", amoeba_deptment.get(i));
			map.put("amoeba_deptment1", amoeba_deptment1.get(i));
			map.put("amoeba_sum", amoeba_sum.get(i));
			if(rdsFinanceConfigService.queryCountAmoebaInfo(map)>0){
				result.put("success", false);
				result.put("msg", "操作失败:"+amoeba_deptment.get(i)+","+amoeba_deptment1.get(i)+"已配置过！");
				return result;
			}
			list.add(map);
		}
		params.put("list", list);
		if(!rdsFinanceConfigService.insertAmoebaInfo(params)){
			result.put("success", false);
			result.put("msg", "操作失败，请重试或联系管理员!");
		}else
		{
			result.put("success", true);
			result.put("msg", "操作成功!");
		}
		return result;
	}
	
	public static List<String> getValues(Object object) {
		List<String> values = new ArrayList<String>();
		if (object != null) {
			if (!(object instanceof String[])) {
				String str = object.toString();
				String[] objects = str.split(",");
				if (objects.length > 1) {
					str = str.substring(1, str.length() - 1);
					String[] objs = str.split(",");
					for (String s : objs) {
						values.add(s.trim());
					}
				} else {
					if (str.contains("[")) {
						values.add(str.substring(1, str.length() - 1));
					} else {
						values.add(str.trim());
					}
				}
			} else {
				for (String s : (String[]) object) {
					values.add(s.trim());
				}
			}

		}
		return values;
	}
	
	@RequestMapping("queryDepartmentAll")
	@ResponseBody
	public Object queryDepartmentAll(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception{
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if(user == null)
			return null;
		//阿米巴数据权限设置
		List<Map<String,Object>> listmap = rdsFinanceConfigMapper.selectUserLevel(user.getUsercode());
		String level = listmap.size()>0?(listmap.get(0).get("userlevel") == null ? "" : listmap
				.get(0).get("userlevel").toString()):"";
		params.put("deptcode", user.getDeptcode());
		Map<String,String> mapParams = rdsFinanceConfigService.queryUserDepartment(params).get(0);
		if("0".equals(params.get("parentcode"))){
			if("1".equals(level)){
				
			}else if ("2".equals(level)) {
				//一级部门编号
				String deptcode1 = mapParams.get("deptcode1").toString();
				//根据一级部门编号查询一级部门id
				params.put("parentcode",
						rdsFinanceConfigMapper.queryDeptParentId(deptcode1).get("deptid").toString());
				params.put("allFlag", 1);
			}else if("3".equals(level)){
				//二级部门编号
				String deptcode2 = mapParams.get("deptcode2").toString();
				//根据二级部门编号查询一级部门id
				params.put("parentcode",
						rdsFinanceConfigMapper.queryDeptParentId(deptcode2).get("deptid").toString());
				params.put("allFlag", 1);
			}else if("4".equals(level)){
				//三级部门编号
				String deptcode3 = mapParams.get("deptcode3").toString();
				//根据二级部门编号查询一级部门id
				params.put("parentcode",
						rdsFinanceConfigMapper.queryDeptParentId(deptcode3).get("deptid").toString());
				params.put("allFlag", 1);
			}else{
				params.put("parentcode","");
			}
		}
		params.put("level", level);
		return rdsFinanceConfigService.queryDepartmentAll(params);
	}
	
	@RequestMapping("exportAmoebaTree")
	@ResponseBody
	public void exportAmoebaTree(HttpServletRequest request,HttpServletResponse response) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		Map<String, Object> params = new HashMap<String, Object>();
		//阿米巴数据权限设置
		List<Map<String,Object>> listmap = rdsFinanceConfigMapper.selectUserLevel(user.getUsercode());
		String level = listmap.size()>0?(listmap.get(0).get("userlevel") == null ? "" : listmap
				.get(0).get("userlevel").toString()):"";
		params.put("deptcode", user.getDeptcode());
		Map<String,String> mapParams = rdsFinanceConfigService.queryUserDepartment(params).get(0);
		String parentcode = request.getParameter("parentcode");
		params.put("parentcode", parentcode);
		
		if("0".equals(params.get("parentcode"))){
			if("1".equals(level)){
				
			}else if ("2".equals(level)) {
				//一级部门编号
				String deptcode1 = mapParams.get("deptcode1").toString();
				//根据一级部门编号查询一级部门id
				params.put("parentcode",
						rdsFinanceConfigMapper.queryDeptParentId(deptcode1).get("deptid").toString());
				params.put("allFlag", 1);
			}else if("3".equals(level)){
				//二级部门编号
				String deptcode2 = mapParams.get("deptcode2").toString();
				//根据二级部门编号查询一级部门id
				params.put("parentcode",
						rdsFinanceConfigMapper.queryDeptParentId(deptcode2).get("deptid").toString());
				params.put("allFlag", 1);
			}else if("4".equals(level)){
				//三级部门编号
				String deptcode3 = mapParams.get("deptcode3").toString();
				//根据二级部门编号查询一级部门id
				params.put("parentcode",
						rdsFinanceConfigMapper.queryDeptParentId(deptcode3).get("deptid").toString());
				params.put("allFlag", 1);
			}else{
				params.put("parentcode","");
			}
		}
		params.put("level", level);
		String deptname = request.getParameter("deptname");
		params.put("deptname", deptname);
		String confirm_date_start = request.getParameter("confirm_date_start");
		params.put("confirm_date_start", confirm_date_start);
		String confirm_date_end = request.getParameter("confirm_date_end");
		params.put("confirm_date_end", confirm_date_end);
		try {
			rdsFinanceConfigService.exportAmoebaTree(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
