package com.rds.judicial.webservice.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.judicial.mapper.RdsJudicialPhoneMapper;
import com.rds.judicial.model.RdsJudicialCaseStatusModel;
import com.rds.judicial.webservice.RdsJudicialCaseStatueWebService;

@Service("com.rds.judicial.webservice.RdsJudicialCaseStatueWebService")
public class RdsJudicialCaseStatueWebServiceImpl implements
		RdsJudicialCaseStatueWebService {
	// 成功
	private static final String SUCCESS = "0000";
	// 无匹配值
	private static final String NOMATCHEDDATE = "0001";
	// 内部错误
	private static final String RUNTIMEERROR = "1000";
	// 必填值为空
	private static final String REQUIREDVALUEISNULL = "1001";

	@Autowired
	private RdsJudicialPhoneMapper pMapper;

	@Override
	public String queryCaseProcessStatus(String input, String other,
			String callback) {
		String phone = "";
		String id_number = "";
		if (StringUtils.trim(other).length() == 18)
			id_number = StringUtils.trim(other);
		else
			phone = StringUtils.trim(other);

		Map<String, Object> resultMap;
		// 结果码
		String result = "";
		// 结果码说明
		String info = "";
		// 数据状态
		List<Map<String, Object>> datas = new ArrayList<>();
		Map<String, Object> data = new HashMap<>();
		// 委托人
		String principal = "";
		// 状态列表
		List<Map<String, String>> rows = new ArrayList<>();
		// 状态
		Map<String, String> row;
		;
		// map2json 工具
		ObjectMapper mapper = new ObjectMapper();

		// 案例编号是否为空
		if (StringUtils.isBlank(input)) {
			result = REQUIREDVALUEISNULL;
			info = "必填值不能为空";
			resultMap = getResult(result, info, datas);
		} else {

			List<RdsJudicialCaseStatusModel> csmodel;
			try {

				// 组装查询map
				Map<String, String> params = new HashMap<>();
				params.put("case_code", input);
				params.put("phone", phone);
				params.put("id_number", id_number);

				// 查询数据库
				csmodel = pMapper.getCaseStatue(params);

				// 如果查询为空
				if (csmodel.get(0) == null) {
					result = NOMATCHEDDATE;
					info = "无匹配值";
					resultMap = getResult(result, info, datas);
				} else {
					for (RdsJudicialCaseStatusModel smodel : csmodel) {

						//委托人有可能为空
						String client = StringUtils.isBlank(smodel.getClient()) ? ""
								: smodel.getClient();
						principal = new StringBuilder(client).replace(0,
								client.length() - 1, "*").toString();
						if (StringUtils.isNotBlank(smodel.getAccept_time())) {
							row = new HashMap<String, String>();
							row.put("processtime", smodel.getAccept_time());
							row.put("processstatus", "案例受理中");
							rows.add(row);
						}
						if (StringUtils.isNotBlank(smodel.getSample_in_time())) {
							row = new HashMap<String, String>();
							row.put("processtime", smodel.getSample_in_time());
							row.put("processstatus", "案例信息审核中");
							rows.add(row);
						}
						if (StringUtils.isNotBlank(smodel
								.getVerify_baseinfo_time())) {
							row = new HashMap<String, String>();
							row.put("processtime",
									smodel.getVerify_baseinfo_time());
							row.put("processstatus", "样本审核中");
							rows.add(row);
						}
						if (StringUtils.isNotBlank(smodel
								.getVerify_sampleinfo_time())) {
							row = new HashMap<String, String>();
							row.put("processtime",
									smodel.getVerify_sampleinfo_time());
							row.put("processstatus", "试验中");
							rows.add(row);
						}
						if (StringUtils.isNotBlank(smodel.getTrans_date())) {
							row = new HashMap<String, String>();
							row.put("processtime", smodel.getTrans_date());
							row.put("processstatus", "报告打印中");
							rows.add(row);
						}
						if (StringUtils.isNotBlank(smodel.getClose_time())) {
							row = new HashMap<String, String>();
							row.put("processtime", smodel.getClose_time());
							row.put("processstatus", "等待邮寄");
							rows.add(row);
						}
						if (StringUtils.isNotBlank(smodel.getMail_time())) {
							row = new HashMap<String, String>();
							row.put("processtime", smodel.getMail_time());
							row.put("processstatus", "已邮寄报告");
							rows.add(row);
						}
						data.put("rows", rows);
						data.put("principal", principal);
						datas.add(data);
					}
					result = SUCCESS;
					info = "查询成功";
					resultMap = getResult(result, info, datas);
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = RUNTIMEERROR;
				info = "内部错误";
				resultMap = getResult(result, info, datas);
				String respString = "";
				try {

					// 集合类转jsonString
					respString = mapper.writeValueAsString(resultMap);
				} catch (JsonGenerationException e1) {
					e1.printStackTrace();
				} catch (JsonMappingException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return callback + "(" + respString + ")";
			}
		}
		String respString = "";
		try {
			// 集合类转jsonString
			respString = mapper.writeValueAsString(resultMap);
		} catch (JsonGenerationException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return callback + "(" + respString + ")";
	}

	private Map<String, Object> getResult(String result, String info,
			List<Map<String, Object>> datas) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("datas", datas);
		map.put("info", info);
		map.put("result", result);
		return map;
	}

	@Override
	public String queryCaseProcessStatus(String identitycard, String callback) {
		String id_number = StringUtils.trim(identitycard);

		Map<String, Object> resultMap;
		// 结果码
		String result = "";
		// 结果码说明
		String info = "";
		// 数据状态
		List<Map<String, Object>> datas = new ArrayList<>();
		Map<String, Object> data = new HashMap<>();
		// 委托人
		String principal = "";
		// 状态列表
		List<Map<String, String>> rows = new ArrayList<>();
		// 状态
		Map<String, String> row;
		// map2json 工具
		ObjectMapper mapper = new ObjectMapper();
		// 身份证是否为空
		if (StringUtils.isBlank(identitycard)) {
			result = REQUIREDVALUEISNULL;
			info = "必填值不能为空";
			resultMap = getResult(result, info, datas);
		} else {
			List<RdsJudicialCaseStatusModel> csmodel;
			try {
				Map<String, String> params = new HashMap<>();
				params.put("id_number", id_number);
				
				//查询数据库
				csmodel = pMapper.getCaseStatueByIdnumber(params);
				if (csmodel.size()==0) {
					result = NOMATCHEDDATE;
					info = "无匹配值";
					resultMap = getResult(result, info, datas);
				} else {
					for (RdsJudicialCaseStatusModel smodel : csmodel) {

						String client = StringUtils.isBlank(smodel.getClient()) ? ""
								: smodel.getClient();
						principal = new StringBuilder(client).replace(0,
								client.length() - 1, "*").toString();
						if (StringUtils.isNotBlank(smodel.getAccept_time())) {
							row = new HashMap<String, String>();
							row.put("processtime", smodel.getAccept_time());
							row.put("processstatus", "案例受理中");
							rows.add(row);
						}
						if (StringUtils.isNotBlank(smodel.getSample_in_time())) {
							row = new HashMap<String, String>();
							row.put("processtime", smodel.getSample_in_time());
							row.put("processstatus", "案例信息审核中");
							rows.add(row);
						}
						if (StringUtils.isNotBlank(smodel
								.getVerify_baseinfo_time())) {
							row = new HashMap<String, String>();
							row.put("processtime",
									smodel.getVerify_baseinfo_time());
							row.put("processstatus", "样本审核中");
							rows.add(row);
						}
						if (StringUtils.isNotBlank(smodel
								.getVerify_sampleinfo_time())) {
							row = new HashMap<String, String>();
							row.put("processtime",
									smodel.getVerify_sampleinfo_time());
							row.put("processstatus", "试验中");
							rows.add(row);
						}
						if (StringUtils.isNotBlank(smodel.getTrans_date())) {
							row = new HashMap<String, String>();
							row.put("processtime", smodel.getTrans_date());
							row.put("processstatus", "报告打印中");
							rows.add(row);
						}
						if (StringUtils.isNotBlank(smodel.getClose_time())) {
							row = new HashMap<String, String>();
							row.put("processtime", smodel.getClose_time());
							row.put("processstatus", "等待邮寄");
							rows.add(row);
						}
						if (StringUtils.isNotBlank(smodel.getMail_time())) {
							row = new HashMap<String, String>();
							row.put("processtime", smodel.getMail_time());
							row.put("processstatus", "已邮寄报告");
							rows.add(row);
						}
						data.put("rows", rows);
						data.put("principal", principal);
						datas.add(data);
					}
					result = SUCCESS;
					info = "查询成功";
					resultMap = getResult(result, info, datas);
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = RUNTIMEERROR;
				info = "内部错误";
				resultMap = getResult(result, info, datas);
				String respString = "";
				try {
					respString = mapper.writeValueAsString(resultMap);
				} catch (JsonGenerationException e1) {
					e1.printStackTrace();
				} catch (JsonMappingException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return callback + "(" + respString + ")";
			}
		}
		String respString = "";
		try {
			respString = mapper.writeValueAsString(resultMap);
		} catch (JsonGenerationException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return callback + "(" + respString + ")";
	}

}
