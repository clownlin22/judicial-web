package com.rds.children.service.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.activiti.mapper.RdsActivitiJudicialMapper;
import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.children.mapper.RdsChildrenMailMapper;
import com.rds.children.mapper.RdsChildrenResultMapper;
import com.rds.children.mapper.RdsChildrenSampleReceiveMapper;
import com.rds.children.model.RdsChildrenMailCaseModel;
import com.rds.children.model.RdsChildrenResponse;
import com.rds.children.service.RdsChildrenMailService;
import com.rds.code.date.DateUtils;
import com.rds.code.utils.ExportUtils;

@Service
public class RdsChildrenMailServiceImpl implements RdsChildrenMailService {
	@Setter
	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;
	@Autowired
	private RdsChildrenResultMapper RdsChildrenResultMapper;
	@Autowired
	private RdsChildrenSampleReceiveMapper rdsChildrenSampleReceiveMapper;
	@Autowired
	private TaskService taskService;

	@Autowired
	private RdsActivitiJudicialMapper rdsActivitiJudicialMapper;
	@Autowired
	private RdsChildrenMailMapper rdsChildrenMailMapper;

	@Override
	public RdsChildrenResponse getMailCaseInfo(Map<String, Object> params) {
		List<RdsChildrenMailCaseModel> caseModels = rdsChildrenMailMapper
				.getMailCaseInfo(params);
		int count = rdsChildrenMailMapper.countMailCaseInfo(params);
		return new RdsChildrenResponse(count, caseModels);
	}

	@Override
	public void exportChildrenMail(String endtime, String starttime, String child_sex,
			String mail_starttime, String mail_endtime, String child_name,
			String case_code, String sample_code, String case_userid,
			String case_areaname, Integer is_mail, Integer is_paid,
			HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("endtime", endtime);
		params.put("starttime", starttime);
		params.put("child_sex", child_sex);
		params.put("mail_starttime", mail_starttime);
		params.put("mail_endtime", mail_endtime);
		params.put("child_name", child_name);
		params.put("case_code", case_code);
		params.put("sample_code", sample_code);
		params.put("case_userid", case_userid);
		params.put("case_areaname", case_areaname);
		params.put("is_mail", is_mail);
		params.put("is_paid", is_paid);
		String filename = "信息列表";
		// 根据查询条件查询案例信息
		List<RdsChildrenMailCaseModel> caseInfoModels = rdsChildrenMailMapper
				.getMailCaseInfo(params);
		// excel表格列头
		Object[] titles = { "案例编号", "样本编号", "采集日期", "儿童姓名", "出生日期", "儿童身份证",
				"户籍所在地", "反馈寄送地", "快递日期",  "邮寄状态", "付款信息", "案例所属人",
				"归属地", "案例备注" };
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		// 循环案例列表拼装表格一行
		for (int i = 0; i < caseInfoModels.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			objects.add(caseInfoModels.get(i).getCase_code());
			objects.add(caseInfoModels.get(i).getSample_code());
			objects.add(DateUtils.DateString2DateString(caseInfoModels.get(i)
					.getCase_in_time()));
			objects.add(caseInfoModels.get(i).getChild_name());
			objects.add(DateUtils.DateString2DateString(caseInfoModels.get(i)
					.getBirth_date()));
			objects.add(caseInfoModels.get(i).getId_number());
			objects.add(caseInfoModels.get(i).getHouse_area());
			objects.add(caseInfoModels.get(i).getMail_area());
			objects.add(caseInfoModels.get(i).getMail_time());
			objects.add(caseInfoModels.get(i).getMail_count()>0?"已邮寄":"未邮寄");
			objects.add((caseInfoModels.get(i).getStatus()==0)?"已付款":"未付款");
			objects.add(caseInfoModels.get(i).getCase_username());
			objects.add(caseInfoModels.get(i).getCase_areaname());
			objects.add(caseInfoModels.get(i).getRemark());
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "儿童基因库"
				+ DateUtils.Date2String(new Date()));
	
	}
}
