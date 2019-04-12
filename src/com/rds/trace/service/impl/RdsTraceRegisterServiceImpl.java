package com.rds.trace.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.code.date.DateUtils;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.trace.mapper.RdsTraceRegisterMapper;
import com.rds.trace.model.RdsTraceCaseInfoModelExt;
import com.rds.trace.service.RdsTracePersonService;
import com.rds.trace.service.RdsTraceRegisterService;
import com.rds.trace.service.RdsTraceVehicleService;
import com.rds.trace.service.RdsTraceVerifyService;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/7/20
 */
@Service("RdsTraceRegisterService")
public class RdsTraceRegisterServiceImpl implements RdsTraceRegisterService {
	@Autowired
	@Setter
	private RdsTraceRegisterMapper rdsTraceRegisterMapper;

	@Autowired
	private RdsTraceVehicleService rdsTraceVehicleService;

	@Autowired
	private RdsTraceVerifyService rdsTraceVerifyService;

	@Autowired
	private RdsTracePersonService rdsTracePersonService;

	@Override
	public int queryCountCaseInfo(Map<String, Object> params) {
		return rdsTraceRegisterMapper.queryCountCaseInfo(params);
	}

	@Override
	public List<RdsTraceCaseInfoModelExt> queryCaseInfo(
			Map<String, Object> params) {
		return rdsTraceRegisterMapper.queryCaseInfo(params);
	}

	@Override
	public boolean updateCaseInfo(Map<String, Object> params) throws Exception {
		try {
			rdsTraceRegisterMapper.updateCaseFee(params);
			rdsTraceRegisterMapper.updateCaseInfo(params);
		} catch (Exception e) {
			throw new Exception("案例编号有重复");
		}
		rdsTraceVehicleService.deleteVehicle((String) params.get("case_id"));
		rdsTracePersonService.deletePerson((String) params.get("case_id"));
		insertVehicleInfo(params);
		insertPersonInfo(params);
		Map<String, Object> verifyMap = new HashMap<String, Object>();
		verifyMap.put("case_id", params.get("case_id"));
		verifyMap.put("verify_baseinfo_state", 0);
		rdsTraceVerifyService.insertVerify(verifyMap);
		// 标记状态未审核
		Map<String, Object> status = new HashMap<>();
		status.put("case_id", params.get("case_id"));
		status.put("status", 0);
		updateStatus(status);
		return true;
	}

	@Override
	public int deleteCaseInfo(String case_id) {
		return rdsTraceRegisterMapper.deleteCaseInfo(case_id);
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public void insertCaseInfo(Map<String, Object> params) throws Exception {
		params.put("case_id", UUIDUtil.getUUID());
		String receive_time = (String) params.get("receive_time");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = simpleDateFormat.parse(receive_time);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		// 系统自动计算案例编号，改为手动前台录入
		// if(rdsTraceRegisterMapper.queryCountCaseInfoByYear(year)>0){
		// params.put("case_no",rdsTraceRegisterMapper.queryCaseNo(year));
		// }else{
		// params.put("case_no",1);
		// }
		params.put("year", year);
		params.put("month", calendar.get(Calendar.MONTH) + 1);
		params.put("day", calendar.get(Calendar.DATE));
		params.put("status", 0);
		try {
			rdsTraceRegisterMapper.insertCaseInfo(params);
		} catch (DuplicateKeyException e) {
			throw new Exception("案例编号有重复");
		}
		params.put("id", UUIDUtil.getUUID());
		rdsTraceRegisterMapper.addCaseFee(params);

		insertVehicleInfo(params);
		insertPersonInfo(params);
	}

	@SuppressWarnings("rawtypes")
	private void insertVehicleInfo(Map<String, Object> params) {
		int countVehicle = 0;
		if (params.get("plate_number") instanceof List) {
			countVehicle = ((List) params.get("plate_number")).size();
		} else if (params.get("plate_number") instanceof String) {
			countVehicle = 1;
		}
		if (countVehicle > 1) {
			for (int i = 0; i < countVehicle; i++) {
				Map<String, Object> vehicleMap = new HashMap<String, Object>();
				vehicleMap.put("case_id", params.get("case_id"));
				vehicleMap.put("plate_number",
						((List) params.get("plate_number")).get(i));
				vehicleMap.put("vehicle_identification_number", ((List) params
						.get("vehicle_identification_number")).get(i));
				vehicleMap.put("engine_number",
						((List) params.get("engine_number")).get(i));
				vehicleMap.put("vehicle_type",
						((List) params.get("vehicle_type")).get(i));
				rdsTraceVehicleService.insertVehicle(vehicleMap);
			}
		} else if (countVehicle == 1) {
			Map<String, Object> vehicleMap = new HashMap<String, Object>();
			vehicleMap.put("case_id", params.get("case_id"));
			vehicleMap.put("plate_number", params.get("plate_number"));
			vehicleMap.put("vehicle_identification_number",
					params.get("vehicle_identification_number"));
			vehicleMap.put("engine_number", params.get("engine_number"));
			vehicleMap.put("vehicle_type", params.get("vehicle_type"));
			rdsTraceVehicleService.insertVehicle(vehicleMap);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void insertPersonInfo(Map<String, Object> params) {
		int countPerson = 0;
		if (params.get("person_name") instanceof List) {
			countPerson = ((List) params.get("person_name")).size();
		} else if (params.get("person_name") instanceof String) {
			countPerson = 1;
		}
		if (countPerson > 1) {
			Set set = new HashSet();

			for (int i = 0; i < countPerson; i++) {
				String idnum = (String) ((List) params.get("id_number")).get(i);
				Map<String, Object> vehicleMap = new HashMap<String, Object>();
				vehicleMap.put("case_id", params.get("case_id"));
				vehicleMap.put("person_name",
						((List) params.get("person_name")).get(i));
				vehicleMap.put("id_number",
						((List) params.get("id_number")).get(i));
				if (set.contains(idnum)) {
					throw new RuntimeException("存在相同的身份证号码，请核实");
				}
				set.add(idnum);
				vehicleMap
						.put("address", ((List) params.get("address")).get(i));
				rdsTracePersonService.insertPerson(vehicleMap);
			}
		} else if (countPerson == 1) {
			Map<String, Object> vehicleMap = new HashMap<String, Object>();
			vehicleMap.put("case_id", params.get("case_id"));
			vehicleMap.put("person_name", params.get("person_name"));
			vehicleMap.put("id_number", params.get("id_number"));
			vehicleMap.put("address", params.get("address"));
			rdsTracePersonService.insertPerson(vehicleMap);
		}
	}

	@Override
	public int updateStatus(Map<String, Object> params) {
		return rdsTraceRegisterMapper.updateStatus(params);
	}

	@Override
	public RdsTraceCaseInfoModelExt queryByCaseId(String case_id) {
		return rdsTraceRegisterMapper.queryByCaseId(case_id);
	}

	@Override
	public void exportInfo(String case_no, String start_time, String end_time,
			String status, Integer is_delete, String invoice_number,
			HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("case_no", case_no);
		params.put("start_time", start_time);
		params.put("end_time", end_time);
		params.put("status", status);
		params.put("is_delete", is_delete);
		params.put("invoice_number", invoice_number);
		String sheetname = "案例列表";
		// 根据查询条件查询案例信息
		List<RdsTraceCaseInfoModelExt> rdsTraceCaseInfoModelExts = rdsTraceRegisterMapper
				.exportInfo(params);
		// excel表格列头
		Object[] titles = { "编号", "接单日期", "委托日期",  "委托单位", "委托人",
				"委托项目","鉴定要求", "应收款项", "所付款项", "到款时间","发票编号", "案例所属人", "报告日期&快递", "备注" };
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		// 循环案例列表拼装表格一行
		for (int i = 0; i < rdsTraceCaseInfoModelExts.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			objects.add(rdsTraceCaseInfoModelExts.get(i).getCase_no() + "");
			objects.add(rdsTraceCaseInfoModelExts.get(i).getReceive_time());
			objects.add(rdsTraceCaseInfoModelExts.get(i)
					.getIdentification_date());
			objects.add(rdsTraceCaseInfoModelExts.get(i).getCompany_name());
			objects.add("");
			objects.add(rdsTraceCaseInfoModelExts.get(i).getCase_type());
			objects.add(rdsTraceCaseInfoModelExts.get(i).getIdentification_requirements());
			objects.add(rdsTraceCaseInfoModelExts.get(i).getReal_sum());
			objects.add(rdsTraceCaseInfoModelExts.get(i).getReturn_sum());
			objects.add(rdsTraceCaseInfoModelExts.get(i).getParagraphtime());
			objects.add(rdsTraceCaseInfoModelExts.get(i).getInvoice_number());
			objects.add(rdsTraceCaseInfoModelExts.get(i).getUsername());
			objects.add(rdsTraceCaseInfoModelExts.get(i).getMail_info());
			objects.add(rdsTraceCaseInfoModelExts.get(i).getRemark());
			bodys.add(objects);
		}
		ExportUtils.export(response, sheetname, titles, bodys, "痕迹部门"
				+ DateUtils.Date2String(new Date()));
	}

	@Override
	public boolean traceMailDely(Map<String, Object> params) {
		rdsTraceRegisterMapper.traceMailDely(params);
		return true;
	}
}
