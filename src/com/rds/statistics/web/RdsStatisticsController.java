package com.rds.statistics.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialDicValuesService;
import com.rds.statistics.mapper.RdsStatisticsMapper;
import com.rds.statistics.model.RdsStatisticsProgramModel;
import com.rds.statistics.model.RdsStatisticsResponse;
import com.rds.statistics.service.RdsStatisticsService;

@Controller
@RequestMapping("statistics")
public class RdsStatisticsController {

	@Autowired
	private RdsStatisticsMapper RdsStatisticsMapper;
	@Autowired
	private RdsStatisticsService RdsStatisticsService;

	@Setter
	@Autowired
	private RdsJudicialDicValuesService rdsJudicialDicValuesService;

	@RequestMapping("getTotalStatistics")
	@ResponseBody
	public RdsStatisticsResponse getTotalStatistics(String date) {
		return RdsStatisticsService.getTotalStatistics(date);
	}

	@RequestMapping("totalinfo")
	public String totalinfo(HttpServletRequest request) {
		return "statistics/totalinfo";
	}

	@RequestMapping("getPerStatistics")
	@ResponseBody
	public RdsStatisticsResponse getPerStatistics(String date) {
		return RdsStatisticsService.getPerStatistics(date);
	}

	@RequestMapping("pertotalinfo")
	public String pertotalinfo(HttpServletRequest request) {
		return "statistics/pertotalinfo";
	}

	@RequestMapping("perinfo")
	public String perinfo(HttpServletRequest request) {
		return "statistics/perinfo";
	}

	@RequestMapping("statCaseTime")
	@ResponseBody
	public RdsJudicialResponse statTime(@RequestBody Map<String, Object> params) {
		return RdsStatisticsService.statTime(params);
	}

	@RequestMapping("exportTotalStatistics")
	public void exportTotalStatistics(String date, HttpServletResponse response) {
		RdsStatisticsService.exportTotalStatistics(date, response);
	}

	@RequestMapping("exportPerStatistics")
	public void exportPerStatistics(String date, HttpServletResponse response) {
		RdsStatisticsService.exportPerStatistics(date, response);
	}

	@RequestMapping("exportPerBanlance")
	public void exportPerBanlance(String date, HttpServletResponse response) {
		RdsStatisticsService.exportPerBanlance(date, response);
	}

	@RequestMapping("queryProgramProvice")
	@ResponseBody
	public List<RdsStatisticsProgramModel> queryProgramProvice(
			String accept_time, String case_area, String user_dept_level1,
			String case_user, String case_type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("accept_time", accept_time);
		params.put("case_area", case_area);
		params.put("case_user", case_user);
		params.put("user_dept_level1", user_dept_level1);
		params.put("case_type", case_type);
		List<RdsStatisticsProgramModel> lists = RdsStatisticsMapper
				.queryProgramProvice(params);
		return lists;
	}

	@RequestMapping("queryProgram")
	public String queryProgram(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		request.setAttribute("userModle", RdsStatisticsMapper.getUsers(map));
		return "statistics/programProvice";
	}
}
