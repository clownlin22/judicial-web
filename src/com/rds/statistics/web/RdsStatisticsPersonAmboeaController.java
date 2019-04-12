package com.rds.statistics.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.statistics.mapper.RdsFinanceConfigMapper;
import com.rds.statistics.model.RdsFinanceCaseDetailStatisticsModel;
import com.rds.statistics.service.RdsFinanceConfigService;
import com.rds.statistics.service.RdsStatisticsPersonAmboeaService;
import com.rds.upc.model.RdsUpcUserModel;

/**
 * @author yxb
 * @className
 * @description
 * @date 2018/5/16
 */
@Controller
@RequestMapping("statistics/personAmboea")
public class RdsStatisticsPersonAmboeaController {

	@Autowired
	private RdsFinanceConfigMapper rdsFinanceConfigMapper;

	@Autowired
	private RdsFinanceConfigService rdsFinanceConfigService;
	
	@Autowired
	private RdsStatisticsPersonAmboeaService rdsStatisticsPersonAmboeaService;

	@RequestMapping("queryAllPage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", rdsStatisticsPersonAmboeaService.queryAllPage(params));
		map.put("total", rdsStatisticsPersonAmboeaService.queryAllCount(params));
		return map;
	}
	
	@RequestMapping("exportPersonAmboeaInfo")
	@ResponseBody
	public void exportPersonAmboeaInfo(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String confirm_date_start = request.getParameter("confirm_date_start");
			params.put("confirm_date_start", confirm_date_start);
			String confirm_date_end = request.getParameter("confirm_date_end");
			params.put("confirm_date_end", confirm_date_end);
			String sqrq_start = request.getParameter("sqrq_start");
			params.put("sqrq_start", sqrq_start);
			String sqrq_end = request.getParameter("sqrq_end");
			params.put("sqrq_end", sqrq_end);
			String operatedate_start = request.getParameter("operatedate_start");
			params.put("operatedate_start", operatedate_start);
			String operatedate_end = request.getParameter("operatedate_end");
			params.put("operatedate_end", operatedate_end);
			String wages_month_start = request.getParameter("wages_month_start");
			params.put("wages_month_start", wages_month_start);
			String wages_month_end = request.getParameter("wages_month_end");
			params.put("wages_month_end", wages_month_end);
			String user_dept_level1 = request.getParameter("user_dept_level1");
			params.put("user_dept_level1", user_dept_level1);
			String user_dept_level2 = request.getParameter("user_dept_level2");
			params.put("user_dept_level2", user_dept_level2);
			String user_dept_level3 = request.getParameter("user_dept_level3");
			params.put("user_dept_level3", user_dept_level3);
			String user_dept_level4 = request.getParameter("user_dept_level4");
			params.put("user_dept_level4", user_dept_level4);
			String user_dept_level5 = request.getParameter("user_dept_level5");
			params.put("user_dept_level5", user_dept_level5);
			String username = request.getParameter("username");
			params.put("username", username);
			String webchart = request.getParameter("webchart");
			params.put("webchart", webchart);
			String usertype = request.getParameter("usertype");
			params.put("usertype", usertype);
			String telphone = request.getParameter("telphone");
			params.put("telphone", telphone);
			rdsStatisticsPersonAmboeaService.exportPersonAmboeaInfo(params, response);;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("queryCaseAllPage")
	@ResponseBody
	public Object queryCaseAllPage(@RequestBody Map<String, Object> params,HttpServletRequest request) {
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
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<RdsFinanceCaseDetailStatisticsModel> list = rdsStatisticsPersonAmboeaService.queryCaseAll(params);
		map.put("data", list);
		map.put("total", rdsStatisticsPersonAmboeaService.queryCaseAllCount(params));
		return map;
	}
	
	@RequestMapping("exportCaseAll")
	@ResponseBody
	public void exportCaseAll(HttpServletRequest request,HttpServletResponse response) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		try{
			Map<String, Object> params = new HashMap<String, Object>();
			String case_user = request.getParameter("case_user");
			params.put("case_user", case_user);
			String case_code = request.getParameter("case_code");
			params.put("case_code", case_code);
			String case_area = request.getParameter("case_area");
			params.put("case_area", case_area);
			String queryFlag = request.getParameter("queryFlag");
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
			String type = request.getParameter("type");
			params.put("type", type);
			String client = request.getParameter("client");
			params.put("client", client);
			String webchart = request.getParameter("webchart");
			params.put("webchart", webchart);
			String confirm_date_start = request.getParameter("confirm_date_start");
			params.put("confirm_date_start", confirm_date_start);
			String confirm_date_end = request.getParameter("confirm_date_end");
			params.put("confirm_date_end", confirm_date_end);
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
			rdsStatisticsPersonAmboeaService.exportCaseAll(params,response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@RequestMapping("queryAmboeaPerson")
	@ResponseBody
	public Object queryAmboeaPerson(@RequestBody Map<String, Object> params,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", rdsStatisticsPersonAmboeaService.queryAmboeaPerson(params));
		map.put("total", rdsStatisticsPersonAmboeaService.queryAmboeaPersonCount(params));
		return map;
	}

	@RequestMapping("exportAmboeaPerson")
	@ResponseBody
	public void exportAmboeaPerson(HttpServletRequest request,HttpServletResponse response) {
//		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
//				.getAttribute("user");
		try{
			Map<String, Object> params = new HashMap<String, Object>();
			String username = request.getParameter("username");
			params.put("username", username);
			String webchart = request.getParameter("webchart");
			params.put("webchart", webchart);
			String usertype = request.getParameter("usertype");
			params.put("usertype", usertype);
			String confirm_date_start = request.getParameter("confirm_date_start");
			params.put("confirm_date_start", confirm_date_start);
			String confirm_date_end = request.getParameter("confirm_date_end");
			params.put("confirm_date_end", confirm_date_end);

			rdsStatisticsPersonAmboeaService.exportAmboeaPerson(params,response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
