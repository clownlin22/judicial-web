package com.rds.judicial.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.rds.judicial.model.RdsJudicialKeyValueModel;

/**
 * 案例日报
 * @author 少明
 *
 */
public interface RdsJudicialCaseDailyService {

	void generateCaseDaily();

	Map<String, Object> getInfo();

	Map<String, Object> getAllinfo(Map<String, Object> params);

	Map<String, Object> getDaysStatistics(Map<String, Object> params);

	Map<String, Object> getNowinfo(Map<String, Object> params);

	Map<String, Object> getPerformance(Map<String, Object> params);

	List<RdsJudicialKeyValueModel> getCompany(HttpSession session);

}
