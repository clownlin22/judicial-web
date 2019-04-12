package com.rds.judicial.service.impl;

/**
 * @description 案例审核
 * @author fushaoming
 * 2015年4月15日
 */
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.XmlParseUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.mapper.RdsJudicialSampleRelayMapper;
import com.rds.judicial.mapper.RdsJudicialSubCaseMapper;
import com.rds.judicial.mapper.RdsJudicialVerifyMapper;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.judicial.model.RdsJudicialSampleInfoModel;
import com.rds.judicial.model.RdsJudicialSubCaseInfoModel;
import com.rds.judicial.model.RdsJudicialVerifyCaseInfoModel;
import com.rds.judicial.service.RdsJudicialVerifyService;
import com.rds.upc.model.RdsUpcUserModel;

@Service("RdsJudicialVerifyService")
public class RdsJudicialVerifyServiceImpl implements RdsJudicialVerifyService {

	@Setter
	@Autowired
	private RdsJudicialVerifyMapper rdsJudicialVerifyMapper;

	@Setter
	@Autowired
	private RdsJudicialRegisterMapper RdsJudicialRegisterMapper;

	@Setter
	@Autowired
	private RdsJudicialSubCaseMapper rdsJudicialSubCaseMapper;
	

	@Autowired
	RdsJudicialSampleRelayMapper rdsJudicialSampleRelayMapper;
	
	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String ZY_REORT_MODEL = PropertiesUtils.readValue(
			FILE_PATH, "zyReportModel");

	private static final String XML_PATH = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config.xml";

	private static final String XML_PATH_1 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config1.xml";

	private static final String XML_PATH_2 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config2.xml";

	private static final String XML_PATH_3 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config3.xml";

	private static final String XML_PATH_7 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config7.xml";
	
	private static final String XML_PATH_8 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config8.xml";
	
	private static final String XML_PATH_10 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config10.xml";
	
	private static final String XML_PATH_11 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config11.xml";

	/**
	 * 登记审核通过
	 * 
	 * 向`tb_judicial_baseinfo_verify`插入审核记录，并更新case_info 表中state字段
	 */
	@Override
	@Transactional
	public Map<String, Object> yesVerify(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<>();
		params.put("uuid", UUIDUtil.getUUID());
		params.put("verify_baseinfo_time", new SimpleDateFormat(
				"yyyy.MM.dd-HH:mm:ss").format(new Date()));
		params.put("verify_baseinfo_state", 1);
		// 2表示登记审核通过样本未审核
		// params.put("state", 2);
		// update by yuanxiaobo 一次性审核通过
		// params.put("state", 3);
		String report_model = (params.get("report_model") == null) ? ""
				: params.get("report_model").toString();

		String parnter_name = (params.get("parnter_name") == null) ? ""
				: params.get("parnter_name").toString();
		if ("".equals(report_model))
			params.put("state", 8);
		else
			params.put("state", 3);

		if (rdsJudicialVerifyMapper.verifyBaseInfo(params) > 0
				&& rdsJudicialVerifyMapper.verifyCaseInfo(params) > 0) {
			if ((ZY_REORT_MODEL.contains(report_model) || "sbyxmodel"
					.equals(report_model)) && (!"".equals(report_model))) {
				String case_id = params.get("case_id") == null ? "" : params
						.get("case_id").toString();
				String case_type = params.get("case_type") == null ? "0"
						: params.get("case_type").toString();
				if ("sbyxmodel".equals(report_model))
					case_type = "1";
				String receiver_area = params.get("receiver_area") == null ? ""
						: params.get("receiver_area").toString();
				String case_code = params.get("case_code") == null ? ""
						: params.get("case_code").toString();
				String case_state = params.get("case_state") == null ? ""
						: params.get("case_state").toString();
				if ("".equals(case_code) || "6".equals(case_state)) {
					// 系统生成新的案例编号
			    String	code_serial= getCaseCode(receiver_area, case_type,
							report_model, params.get("case_userid").toString(),
							params.get("case_receiver") == null ? "" : params
									.get("case_receiver").toString(),params.get("CMA").toString());
					String[] str = code_serial.split(",");
					case_code=str[0];
					String serial_number="";
					//获取流水号
					if(str.length>1){
						serial_number=str[1];
					}
					Map<String, Object> map = new HashMap<>();
					map.put("case_code", case_code);
					// 校验案例编号是否存在
					if (RdsJudicialRegisterMapper.exsitCaseCode(map) > 0) {
						System.out.println("已存在case_code=================="
								+ case_code);
						result.put("success", false);
						
						return result;
					}
					//流水号不为空，便添加
					if(!"".equals(serial_number)&& null !=serial_number){
					Map<String, Object> serial = new HashMap<>();
					serial.put("case_id", case_id);
					serial.put("case_code", case_code);
					serial.put("serial_number", serial_number);
					RdsJudicialRegisterMapper.insertSerialNumber(serial);
					}
					map.put("case_id", case_id);
					rdsJudicialVerifyMapper.updateCaseInfoCaseCode(map);
					params.put("case_code", case_code);
				}
				// else {
				// case_code = case_code.split("_")[0];
				// }
				// 判断是否为补样，插入关联关系
				if ("6".equals(case_state)) {
					Map<String, Object> map = new HashMap<>();
					map.put("case_id", case_id);
					map.put("case_code_second", case_code);
					map.put("case_state", case_state);
					RdsJudicialRegisterMapper.updateCaseCodeSecond(map);
				}
				// List<String> sample_codes = getValues(params
				// .get("sample_code"));
				/* 自动生成样本条形码 start */
				List<String> sample_calls = getValues(params.get("sample_call"));

				List<String> sample_code_sys = new ArrayList<String>();
				if("qdwfjdmodel".equals(report_model))
				{sample_code_sys = getSampleCodeQD(sample_calls, case_code);}
				else if("ahzzjdmodel".equals(report_model)){sample_code_sys = getSampleCodeAhzz(sample_calls, case_code);}
				else{sample_code_sys = getSampleCode(sample_calls, case_code);}
				params.put("sample_code_sys", sample_code_sys);
				/* 自动生成样本条形码 end */
				List<String> sample_codes = getValues(params.get("sample_code"));
				List<String> sample_types = getValues(params.get("sample_type"));
				List<String> sample_usernames = getValues(params
						.get("sample_username"));
				List<String> id_numbers = getValues(params.get("id_number"));
				List<String> birth_dates = getValues(params.get("birth_date"));
				List<String> specials = getValues(params.get("special"));
				List<String> address = getValues(params.get("address"));
				// 插入子案例
				addSubCaseInfo(sample_code_sys, sample_calls, params, null);
				String msg = "";
				// 删除样本信息
				RdsJudicialRegisterMapper.deleteSampleInfo(params);
				for (int i = 0; i < sample_codes.size(); i++) {
					RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel();
					sampleInfoModel.setSample_id(UUIDUtil.getUUID());
					sampleInfoModel.setSample_code(sample_code_sys.get(i));
					sampleInfoModel.setSample_code_sys(sample_codes.get(i));
					sampleInfoModel.setSample_call(sample_calls.get(i));
					sampleInfoModel.setSample_username(sample_usernames.get(i));
					sampleInfoModel.setId_number(id_numbers.get(i));
					sampleInfoModel.setBirth_date(birth_dates.get(i));
					sampleInfoModel.setSample_type(sample_types.get(i));
					sampleInfoModel
							.setCase_id(params.get("case_id").toString());
					sampleInfoModel.setSpecial(specials.get(i));
					sampleInfoModel.setAddress(address.get(i));
					RdsJudicialRegisterMapper.insertSampleInfo(sampleInfoModel);
					msg += sampleInfoModel.getSample_username() + ":"
							+ sampleInfoModel.getSample_code() + ";";
				}
				params.put("updateFlag", 1);
				addCaseSample(params);
				result.put("sample_code", msg);
			} else if ("河南商都司法鉴定中心".equals(parnter_name)
					&& "".equals(report_model)) {
				String case_id = params.get("case_id") == null ? "" : params
						.get("case_id").toString();
				String case_type = params.get("case_type") == null ? "0"
						: params.get("case_type").toString();
				String receiver_area = params.get("receiver_area") == null ? ""
						: params.get("receiver_area").toString();
				String case_code = params.get("case_code") == null ? ""
						: params.get("case_code").toString();
				String case_state = params.get("case_state") == null ? ""
						: params.get("case_state").toString();
				//判断如果不是二次采样则直接生成案例条码
				if (!"5".equals(case_state)) {
					// 系统生成新的案例编号
					case_code = getCaseCodeByParnterName(receiver_area,
							case_type, parnter_name, params.get("case_userid")
									.toString(),
							params.get("case_receiver") == null ? "" : params
									.get("case_receiver").toString());
					Map<String, Object> map = new HashMap<>();
					map.put("case_code", case_code);
					// 校验案例编号是否存在
					if (RdsJudicialRegisterMapper.exsitCaseCode(map) > 0) {
						System.out.println("已存在case_code=================="
								+ case_code);
						result.put("success", false);
						return result;
					}
					map.put("case_id", case_id);
					rdsJudicialVerifyMapper.updateCaseInfoCaseCode(map);
					params.put("case_code", case_code);
				} 

				// 判断是否为补样，插入关联关系
				if ("6".equals(case_state) || "5".equals(case_state)) {
					Map<String, Object> map1 = new HashMap<>();
					map1.put("case_id", case_id);
					map1.put("case_code_second", case_code);
					map1.put("case_state", case_state);
					RdsJudicialRegisterMapper.updateCaseCodeSecond(map1);
				}
				// List<String> sample_codes = getValues(params
				// .get("sample_code"));
				/* 自动生成样本条形码 start */
				List<String> sample_calls = getValues(params.get("sample_call"));

				List<String> sample_code_sys = new ArrayList<String>();
				// 获取同胞，亲缘样本条码
				sample_code_sys = getSampleCodeOther(sample_calls, case_code);
				params.put("sample_code_sys", sample_code_sys);
				/* 自动生成样本条形码 end */
				List<String> sample_codes = getValues(params.get("sample_code"));
				List<String> sample_types = getValues(params.get("sample_type"));
				List<String> sample_usernames = getValues(params
						.get("sample_username"));
				List<String> id_numbers = getValues(params.get("id_number"));
				List<String> birth_dates = getValues(params.get("birth_date"));
				List<String> specials = getValues(params.get("special"));
				// 插入子案例
				// addSubCaseInfo(sample_code_sys, sample_calls, params, null);
				String msg = "";
				// 删除样本信息
				RdsJudicialRegisterMapper.deleteSampleInfo(params);
				for (int i = 0; i < sample_codes.size(); i++) {
					RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel();
					sampleInfoModel.setSample_id(UUIDUtil.getUUID());
					sampleInfoModel.setSample_code(sample_code_sys.get(i));
					sampleInfoModel.setSample_code_sys(sample_codes.get(i));
					sampleInfoModel.setSample_call(sample_calls.get(i));
					sampleInfoModel.setSample_username(sample_usernames.get(i));
					sampleInfoModel.setId_number(id_numbers.get(i));
					sampleInfoModel.setBirth_date(birth_dates.get(i));
					sampleInfoModel.setSample_type(sample_types.get(i));
					sampleInfoModel
							.setCase_id(params.get("case_id").toString());
					sampleInfoModel.setSpecial(specials.get(i));
					RdsJudicialRegisterMapper.insertSampleInfo(sampleInfoModel);
					msg += sampleInfoModel.getSample_username() + ":"
							+ sampleInfoModel.getSample_code() + ";";
				}
				params.put("updateFlag", 1);
				addCaseSample(params);
				result.put("sample_code", msg);

			}
			 else if ("广东永建法医物证司法鉴定所".equals(parnter_name)
						&& "".equals(report_model)) {

					String case_id = params.get("case_id") == null ? "" : params
							.get("case_id").toString();
					String case_type = params.get("case_type") == null ? "0"
							: params.get("case_type").toString();
					String receiver_area = params.get("receiver_area") == null ? ""
							: params.get("receiver_area").toString();
					String case_code = params.get("case_code") == null ? ""
							: params.get("case_code").toString();
					String case_state = params.get("case_state") == null ? ""
							: params.get("case_state").toString();
					//判断如果不是二次采样则直接生成案例条码
					if (!"5".equals(case_state)) {
						// 系统生成新的案例编号
						case_code = getCaseCodeByParnterName(receiver_area,
								case_type, parnter_name, params.get("case_userid")
										.toString(),
								params.get("case_receiver") == null ? "" : params
										.get("case_receiver").toString());
						Map<String, Object> map = new HashMap<>();
						map.put("case_code", case_code);
						// 校验案例编号是否存在
						if (RdsJudicialRegisterMapper.exsitCaseCode(map) > 0) {
							System.out.println("已存在case_code=================="
									+ case_code);
							result.put("success", false);
							return result;
						}
						map.put("case_id", case_id);
						rdsJudicialVerifyMapper.updateCaseInfoCaseCode(map);
						params.put("case_code", case_code);
					} 

					// 判断是否为补样，插入关联关系
					if ("6".equals(case_state) || "5".equals(case_state)) {
						Map<String, Object> map1 = new HashMap<>();
						map1.put("case_id", case_id);
						map1.put("case_code_second", case_code);
						map1.put("case_state", case_state);
						RdsJudicialRegisterMapper.updateCaseCodeSecond(map1);
					}
					// List<String> sample_codes = getValues(params
					// .get("sample_code"));
					/* 自动生成样本条形码 start */
					List<String> sample_calls = getValues(params.get("sample_call"));

					List<String> sample_code_sys = new ArrayList<String>();
					// 获取同胞，亲缘样本条码
					sample_code_sys = getSampleCodeOther(sample_calls, case_code);
					params.put("sample_code_sys", sample_code_sys);
					/* 自动生成样本条形码 end */
					List<String> sample_codes = getValues(params.get("sample_code"));
					List<String> sample_types = getValues(params.get("sample_type"));
					List<String> sample_usernames = getValues(params
							.get("sample_username"));
					List<String> id_numbers = getValues(params.get("id_number"));
					List<String> birth_dates = getValues(params.get("birth_date"));
					List<String> specials = getValues(params.get("special"));
					// 插入子案例
					// addSubCaseInfo(sample_code_sys, sample_calls, params, null);
					String msg = "";
					// 删除样本信息
					RdsJudicialRegisterMapper.deleteSampleInfo(params);
					for (int i = 0; i < sample_codes.size(); i++) {
						RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel();
						sampleInfoModel.setSample_id(UUIDUtil.getUUID());
						sampleInfoModel.setSample_code(sample_code_sys.get(i));
						sampleInfoModel.setSample_code_sys(sample_codes.get(i));
						sampleInfoModel.setSample_call(sample_calls.get(i));
						sampleInfoModel.setSample_username(sample_usernames.get(i));
						sampleInfoModel.setId_number(id_numbers.get(i));
						sampleInfoModel.setBirth_date(birth_dates.get(i));
						sampleInfoModel.setSample_type(sample_types.get(i));
						sampleInfoModel
								.setCase_id(params.get("case_id").toString());
						sampleInfoModel.setSpecial(specials.get(i));
						RdsJudicialRegisterMapper.insertSampleInfo(sampleInfoModel);
						msg += sampleInfoModel.getSample_username() + ":"
								+ sampleInfoModel.getSample_code() + ";";
					}
					params.put("updateFlag", 1);
					addCaseSample(params);
					result.put("sample_code", msg);

				
			 }
			result.put("success", true);
		} else {
			result.put("success", false);
		}
		return result;
	}

	/**
	 * 登记审核不通过 向`tb_judicial_baseinfo_verify`插入审核记录，并更新case_info 表中state字段
	 */
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public boolean noVerify(Map<String, Object> params) {
		params.put("uuid", UUIDUtil.getUUID());
		params.put("verify_baseinfo_time", new SimpleDateFormat(
				"yyyy.MM.dd-HH:mm:ss").format(new Date()));
		params.put("verify_baseinfo_state", 2);
		// 1表示登记审核未通过
		params.put("state", 2);
		if (rdsJudicialVerifyMapper.verifyBaseInfo(params) > 0
				&& rdsJudicialVerifyMapper.verifyCaseInfo(params) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 样本审核通过
	 * 
	 * 向`tb_judicial_sampleinfo_verify`表中插入数据并更新case_info表中state字段
	 */
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public boolean yesSampleVerify(Map<String, Object> params) {
		params.put("uuid", UUIDUtil.getUUID());
		params.put("verify_sampleinfo_time", new SimpleDateFormat(
				"yyyy.MM.dd-HH:mm:ss").format(new Date()));
		params.put("verify_sampleinfo_state", 1);
		params.put("verify_sampleinfo_count", 0);
		// 3表示样本审核通过
		params.put("state", 3);
		if (rdsJudicialVerifyMapper.verifySampleInfo(params) > 0
				&& rdsJudicialVerifyMapper.verifyCaseInfo(params) > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 审核样本信息不通过
	 * 
	 * 向tb_judicial_sampleinfo_verify表中插入数据并作废案例更新case_info中is_delete 字段
	 */
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public boolean noSampleVerify(Map<String, Object> params) {
		params.put("uuid", UUIDUtil.getUUID());
		params.put("verify_sampleinfo_time", new SimpleDateFormat(
				"yyyy.MM.dd-HH:mm:ss").format(new Date()));
		params.put("verify_sampleinfo_state", 2);
		params.put("verify_sampleinfo_count", 0);
		params.put("state", 4);
		if (rdsJudicialVerifyMapper.verifySampleInfo(params) > 0
				&& rdsJudicialVerifyMapper.verifyCaseInfo(params) > 0) {
			rdsJudicialVerifyMapper
					.deleteCase((String) params.get("case_code"));
			return true;
		}
		return false;
	}

	// /**
	// * 根据条件获取案例的基本信息
	// */
	// @Override
	// public RdsJudicialResponse getCaseInfo(Map<String, Object> params) {
	// RdsJudicialResponse response = new RdsJudicialResponse();
	// List<RdsJudicialCaseInfoModel> caseInfoModels = registerMapper
	// .queryCaseForArchive(params);
	// int count = registerMapper.countCaseForArchive(params);
	// response.setItems(caseInfoModels);
	// response.setCount(count);
	// return response;
	// }

	@Override
	public List<RdsJudicialVerifyCaseInfoModel> queryAll(
			Map<String, Object> params) {
		return rdsJudicialVerifyMapper.queryAll(params);
	}

	@Override
	public int queryCount(Map<String, Object> params) {
		return rdsJudicialVerifyMapper.queryCount(params);
	}

	@Override
	public List<Map<String, Object>> queryVerifyHistory(String case_id) {
		return rdsJudicialVerifyMapper.queryVerifyHistory(case_id);
	}

	@Override
	public List<String> getSampleCodes(String[] caseCodes) {
		return rdsJudicialVerifyMapper.getSampleCodes(caseCodes);
	}

	private String getCaseCode(String receiver_area, String case_type,
			String report_model, String userid, String case_username,String CMA) {
		String case_code = "";
		String	serial_number="";
		if ("0".equals(case_type)) {
			// 省内案例编号生成
			if (receiver_area.contains("江苏省")
					&& RdsJudicialRegisterMapper.selectProvinceUser(userid) > 0
					&& report_model.startsWith("zyjd")) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH,
						"case_code_date");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date()).substring(0, 4);
				if (xml_time.equals(now_time)) {
					case_code = "Z"
							+ now_time
							+ String.valueOf(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH, "case_code_num")) + 1);
					//江苏省内子渊案例生成流水号
					serial_number = "子渊司鉴所["
							+ now_time
							+  "]物鉴字第"+String.valueOf(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH, "case_code_num")) + 1)+"号";
					XmlParseUtil
							.updateXmlValue(XML_PATH, "case_code_num", String
									.valueOf(Integer.parseInt(XmlParseUtil
											.getXmlValue(XML_PATH,
													"case_code_num")) + 1));
					
					
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH, "case_code_date",
							now_time);
					XmlParseUtil.updateXmlValue(XML_PATH, "case_code_num", "1");
					case_code = "Z"
							+ now_time
							+ String.valueOf(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH, "case_code_num")));
					//江苏省内子渊案例生成流水号
					serial_number = "子渊司鉴所["
							+ now_time
							+ "]物鉴字第"+Integer.parseInt(XmlParseUtil.getXmlValue(
									XML_PATH, "case_code_num"))+"号";
				}
			} else if (report_model.startsWith("sqjd")) {
				// 判断非合作方案例生成新编号
				if (!case_username.contains("商都")) {
					String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
							"case_code_date_d");
					String now_time = com.rds.code.utils.StringUtils
							.dateToSix(new Date());
					DecimalFormat df = new DecimalFormat("0000");
					if (xml_time.equals(now_time)) {
						case_code = "D"
								+ now_time
								+ df.format(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_2,
												"case_code_num_d")) + 1);
						XmlParseUtil.updateXmlValue(XML_PATH_2,
								"case_code_num_d",
								String.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_2,
												"case_code_num_d")) + 1));
					} else {
						XmlParseUtil.updateXmlValue(XML_PATH_2,
								"case_code_date_d", now_time);
						XmlParseUtil.updateXmlValue(XML_PATH_2,
								"case_code_num_d", "1");
						case_code = "D"
								+ now_time
								+ df.format(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_2,
												"case_code_num_d")));
					}
				} else if ("商都".equals(case_username)) {
					String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
							"case_code_date_sd");
					String now_time = com.rds.code.utils.StringUtils
							.dateToSix(new Date());
					DecimalFormat df = new DecimalFormat("0000");
					if (xml_time.equals(now_time)) {
						case_code = "SD"
								+ now_time
								+ df.format(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_2,
												"case_code_num_sd")) + 1);
						XmlParseUtil.updateXmlValue(XML_PATH_2,
								"case_code_num_sd",
								String.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_2,
												"case_code_num_sd")) + 1));
					} else {
						XmlParseUtil.updateXmlValue(XML_PATH_2,
								"case_code_date_sd", now_time);
						XmlParseUtil.updateXmlValue(XML_PATH_2,
								"case_code_num_sd", "1");
						case_code = "SD"
								+ now_time
								+ df.format(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_2,
												"case_code_num_sd")));
					}
				} else if ("商都法院".equals(case_username)) {
					String xml_time = XmlParseUtil.getXmlValue(XML_PATH_7,
							"case_code_date_sdf");
					String now_time = com.rds.code.utils.StringUtils.dateToSix(
							new Date()).substring(0, 4);
					DecimalFormat df = new DecimalFormat("000");
					if (xml_time.equals(now_time)) {
						case_code = "SDF"
								+ df.format(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_7,
												"case_code_num_sdf")) + 1);
						XmlParseUtil.updateXmlValue(XML_PATH_7,
								"case_code_num_sdf",
								String.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_7,
												"case_code_num_sdf")) + 1));
					} else {
						XmlParseUtil.updateXmlValue(XML_PATH_7,
								"case_code_date_sdf", now_time);
						XmlParseUtil.updateXmlValue(XML_PATH_7,
								"case_code_num_sdf", "1");
						case_code = "SDF"
								+ df.format(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_7,
												"case_code_num_sdf")));
					}

				}
			}
			// 云南乾盛案例编码生成
						else if (report_model.startsWith("ynqsjd")) {
							String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
									"case_code_date_ynqs");
							String now_time = com.rds.code.utils.StringUtils
									.dateToSix(new Date()).substring(0, 4);
							DecimalFormat df = new DecimalFormat("000");
							if (xml_time.equals(now_time)) {
								int num=Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_2, "case_code_num_ynqs"))+1;
								if(num>999){
									int mm=Integer.parseInt(XmlParseUtil
											.getXmlValue(XML_PATH_2, "case_code_num_ynqs")) + 1;
									case_code =now_time+"105"
											+ mm;}
								else{
								case_code =now_time+"105"
										+ df.format(Integer.parseInt(XmlParseUtil
												.getXmlValue(XML_PATH_2, "case_code_num_ynqs")) + 1);
								}
								XmlParseUtil
										.updateXmlValue(XML_PATH_2, "case_code_num_ynqs",
												String.valueOf(Integer
														.parseInt(XmlParseUtil.getXmlValue(
																XML_PATH_2,
																"case_code_num_ynqs")) + 1));
							} else {
								XmlParseUtil.updateXmlValue(XML_PATH_2,
										"case_code_date_ynqs", now_time);
								XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_ynqs",
										"1");
								case_code =now_time+"105"
										+ df.format(Integer.parseInt(XmlParseUtil
												.getXmlValue(XML_PATH_2, "case_code_num_ynqs")));
							}
						}
			// 求实鉴定案例编码生成
			else if (report_model.startsWith("qsjd")) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
						"case_code_date_qs");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("000");
				if (xml_time.equals(now_time)) {
					case_code = "QS"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_qs")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_2, "case_code_num_qs",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_2,
													"case_code_num_qs")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_2,
							"case_code_date_qs", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_qs",
							"1");
					case_code = "QS"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_qs")));
				}
			}
			// 金证鉴定案例编码生成
			else if (report_model.startsWith("jzjd")) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
						"case_code_date_jz");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("000");
				if (xml_time.equals(now_time)) {
					case_code = "JZ"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_jz")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_2, "case_code_num_jz",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_2,
													"case_code_num_jz")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_2,
							"case_code_date_jz", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_jz",
							"1");
					case_code = "JZ"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_jz")));
				}
			}
			//金盾模板编号
			else if (report_model.startsWith("jdjd")) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
						"case_code_date_jd");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("0000");
				if (xml_time.equals(now_time)) {
					case_code = "JD"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_jd")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_2, "case_code_num_jd",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_2,
													"case_code_num_jd")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_2,
							"case_code_date_jz", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_jd",
							"1");
					case_code = "JD"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_jd")));
				}
			}//商丘法医协会鉴定所
			else if (report_model.startsWith("fyxhjd")) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_7,
						"case_code_date_fyxh");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("0000");
				if (xml_time.equals(now_time)) {
					case_code = "F"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_7, "case_code_num_fyxh")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_7, "case_code_num_fyxh",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_7,
													"case_code_num_fyxh")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_7,
							"case_code_date_fyxh", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_7, "case_code_num_fyxh",
							"1");
					case_code = "F"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_7, "case_code_num_fyxh")));
				}
			}
			//毕节黔案例模板编号
			else if (report_model.startsWith("bjqjd")) {
				if("2".equals(CMA)){
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
						"case_code_date_bj");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("0000");
				if (xml_time.equals(now_time)) {
					case_code = "BJ"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_bj")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_2, "case_code_num_bj",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_2,
													"case_code_num_bj")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_2,
							"case_code_date_bj", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_bj",
							"1");
					case_code = "BJ"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_bj")));
				}
				}else{
                    //毕节省内案例编号生成
					String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
							"case_code_date_bj_CMA");
					String now_time = com.rds.code.utils.StringUtils
							.dateToSix(new Date()).substring(0, 4);
					if (xml_time.equals(now_time)) {
						case_code = "BJ"
								+ now_time
								+ String.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_2, "case_code_num_bj_CMA")) + 1);

						XmlParseUtil
								.updateXmlValue(XML_PATH_2, "case_code_num_bj_CMA", String
										.valueOf(Integer.parseInt(XmlParseUtil
												.getXmlValue(XML_PATH_2,
														"case_code_num_bj_CMA")) + 1));
							
					} else {
						XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_date_bj_CMA",
								now_time);
						XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_bj_CMA", "1");
						case_code = "BJ"
								+ now_time
								+ String.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_2, "case_code_num_bj_CMA")));
					}
				}
			}else if(report_model.startsWith("tempor")){//商都临时模板编号生成

				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_11,
						"case_code_date");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date()).substring(0,4);
				DecimalFormat df = new DecimalFormat("0000");
				if (xml_time.equals(now_time)) {
					case_code ="H"+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_11, "case_code_num")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_11, "case_code_num",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_11,
													"case_code_num")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_11,
							"case_code_date", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_11, "case_code_num",
							"1");
					case_code ="H"+now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_11, "case_code_num")));
				}
			
			}
			//青岛万方案例模板编号
			else if (report_model.startsWith("qdwfjd")) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_8,
						"case_code_date_qdwf");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date()).substring(0,4);
				DecimalFormat df = new DecimalFormat("0000");
				if (xml_time.equals(now_time)) {
					case_code = now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_8, "case_code_num_qdwf")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_8, "case_code_num_qdwf",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_8,
													"case_code_num_qdwf")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_8,
							"case_code_date_qdwf", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_8, "case_code_num_qdwf",
							"1");
					case_code =now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_8, "case_code_num_qdwf")));
				}
			}
			// 楚雄三和鉴定案例编码生成
			else if (report_model.startsWith("cxjd")) {
				if("2".equals(CMA)){
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_3,
						"case_code_date_cxsh");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("0000");
				if (xml_time.equals(now_time)) {
					case_code = "SH"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_3,
											"case_code_num_cxsh")) + 1);
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_cxsh",
							String.valueOf(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_3,
											"case_code_num_cxsh")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_date_cxsh", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_cxsh", "1");
					case_code = "SH"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_3,
											"case_code_num_cxsh")));
				}
				}else{//楚雄三和生成CMA编号
					String xml_time = XmlParseUtil.getXmlValue(XML_PATH_3,
							"case_code_date_cxsh_CMA");
					String now_time = com.rds.code.utils.StringUtils
							.dateToSix(new Date()).substring(0, 4);
					DecimalFormat df = new DecimalFormat("000");
					if (xml_time.equals(now_time)) {
						case_code = "WZ-"
								+ df.format(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_3, "case_code_num_cxsh_CMA")) + 1);

						XmlParseUtil
								.updateXmlValue(XML_PATH_3, "case_code_num_cxsh_CMA", String
										.valueOf(Integer.parseInt(XmlParseUtil
												.getXmlValue(XML_PATH_3,
														"case_code_num_cxsh_CMA")) + 1));
							
					} else {
						XmlParseUtil.updateXmlValue(XML_PATH_3, "case_code_date_cxsh_CMA",
								now_time);
						XmlParseUtil.updateXmlValue(XML_PATH_3, "case_code_num_cxsh_CMA", "1");
						case_code = "WZ-"
								+ df.format(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_3, "case_code_num_cxsh_CMA")));
					}
					
				}
			}
			// 鼎城鉴定案例编码生成
			else if (report_model.startsWith("dcjd")) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
						"case_code_date_dc");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("0000");
				if (xml_time.equals(now_time)) {
					case_code = "DC"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_dc")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_2, "case_code_num_dc",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_2,
													"case_code_num_dc")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_2,
							"case_code_date_dc", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_dc",
							"1");
					case_code = "DC"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_dc")));
				}
			}
			// 鸣正鉴定案例编码生成
			else if (report_model.startsWith("mzjd")) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
						"case_code_date_mz");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("0000");
				if (xml_time.equals(now_time)) {
					case_code = "M"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_mz")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_2, "case_code_num_mz",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_2,
													"case_code_num_mz")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_2,
							"case_code_date_mz", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_mz",
							"1");
					case_code = "M"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_mz")));
				}
			}
			// 十堰鉴定案例编码生成
			else if (report_model.startsWith("syjd")) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
						"case_code_date_sy");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("000");
				if (xml_time.equals(now_time)) {
					case_code = "Y"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_sy")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_2, "case_code_num_sy",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_2,
													"case_code_num_sy")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_2,
							"case_code_date_sy", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_sy",
							"1");
					case_code = "Y"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_sy")));
				}
			}
			// 四川鑫正鉴定案例编码生成
			else if (report_model.startsWith("xzjd")) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_3,
						"case_code_date_scxz");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("0000");
				if (xml_time.equals(now_time)) {
					case_code = "XZ"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_3,
											"case_code_num_scxz")) + 1);
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_scxz",
							String.valueOf(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_3,
											"case_code_num_scxz")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_date_scxz", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_scxz", "1");
					case_code = "XZ"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_3,
											"case_code_num_scxz")));
				}
			}//新安鉴定
			else if (report_model.startsWith("xajd")) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_11,
						"case_code_date_xa");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("0000");
				if (xml_time.equals(now_time)) {
					case_code = "XA"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_11,
											"case_code_num_xa")) + 1);
					XmlParseUtil.updateXmlValue(XML_PATH_11,
							"case_code_num_xa",
							String.valueOf(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_11,
											"case_code_num_xa")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_11,
							"case_code_date_xa", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_11,
							"case_code_num_xa", "1");
					case_code = "XA"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_11,
											"case_code_num_scxa")));
				}
			}
			else if (report_model.startsWith("ahzzjd")) {

					case_code =""+(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_10,
											"case_code_num_ahzz")) + 1);
					XmlParseUtil.updateXmlValue(XML_PATH_10,
							"case_code_num_ahzz",
							String.valueOf(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_10,
											"case_code_num_ahzz")) + 1));
	
			}else if(report_model.startsWith("ydjd")){//永健模板，省内案例编号生成
				if ("1".equals(CMA)) {
                      
					String xml_time = XmlParseUtil.getXmlValue(XML_PATH_10,
							"case_code_date");
					String now_time = com.rds.code.utils.StringUtils
							.dateToSix(new Date()).substring(0, 4);
					if (xml_time.equals(now_time)) {
						case_code = "WA"
								+ now_time
								+ String.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_10, "case_code_num")) + 1);

						XmlParseUtil
								.updateXmlValue(XML_PATH_10, "case_code_num", String
										.valueOf(Integer.parseInt(XmlParseUtil
												.getXmlValue(XML_PATH_10,
														"case_code_num")) + 1));
							
					} else {
						XmlParseUtil.updateXmlValue(XML_PATH_10, "case_code_date",
								now_time);
						XmlParseUtil.updateXmlValue(XML_PATH_10, "case_code_num", "1");
						case_code = "WA"
								+ now_time
								+ String.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_10, "case_code_num")));
					}
					//永健模板，省外案例编号生成
				}else{
					    DecimalFormat df = new DecimalFormat("0000");

						String xml_time = XmlParseUtil.getXmlValue(XML_PATH_10,
								"case_code_date_yd");
						String now_time = com.rds.code.utils.StringUtils
								.dateToSix(new Date());

						if (xml_time.equals(now_time)) {
							case_code = "YJ"
									+ now_time
									+ df.format(Integer.parseInt(XmlParseUtil
											.getXmlValue(XML_PATH_10, "case_code_num_yd")) + 1);
							XmlParseUtil
									.updateXmlValue(XML_PATH_10, "case_code_num_yd", String
											.valueOf(Integer.parseInt(XmlParseUtil
													.getXmlValue(XML_PATH_10,
															"case_code_num_yd")) + 1));
						} else {
							XmlParseUtil.updateXmlValue(XML_PATH_10, "case_code_date_yd",
									now_time);
							XmlParseUtil.updateXmlValue(XML_PATH_10, "case_code_num_yd",
									"0001");
							case_code = "YJ"
									+ now_time
									+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
											XML_PATH_10, "case_code_num_yd")));
						}
					}		
						}
			else {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_1,
						"case_code_date");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());

				if (xml_time.equals(now_time)) {
					case_code = "Z"
							+ now_time
							+ String.valueOf(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_1, "case_code_num")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_1, "case_code_num", String
									.valueOf(Integer.parseInt(XmlParseUtil
											.getXmlValue(XML_PATH_1,
													"case_code_num")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_1, "case_code_date",
							now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_1, "case_code_num",
							"1000");
					case_code = "Z"
							+ now_time
							+ Integer.parseInt(XmlParseUtil.getXmlValue(
									XML_PATH_1, "case_code_num"));
				}
			}

			// 医学案例编号生成
		} else {
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH,
					"case_medical_date");
			String now_time = com.rds.code.utils.StringUtils
					.dateToSix(new Date());
			DecimalFormat df = new DecimalFormat("0000");
			if (xml_time.equals(now_time)) {
				case_code = "Q"
						+ now_time
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH, "case_medical_num")) + 1);
				XmlParseUtil
						.updateXmlValue(XML_PATH, "case_medical_num", String
								.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH,
												"case_medical_num")) + 1));
			} else {
				XmlParseUtil.updateXmlValue(XML_PATH, "case_medical_date",
						now_time);
				XmlParseUtil.updateXmlValue(XML_PATH, "case_medical_num", "1");
				case_code = "Q"
						+ now_time
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH, "case_medical_num")));
			}
		}
		return case_code+","+serial_number;
	}

	/**
	 * 根据合作商名生成案例编号
	 * 
	 * @param receiver_area
	 * @param case_type
	 * @param parnter_name
	 * @param userid
	 * @param case_username
	 * @return
	 */
	private String getCaseCodeByParnterName(String receiver_area,
			String case_type, String parnter_name, String userid,
			String case_username) {
		String case_code = "";
		if ("0".equals(case_type)) {
			// 省内案例编号生成
			if (receiver_area.contains("江苏省")
					&& RdsJudicialRegisterMapper.selectProvinceUser(userid) > 0
					&& ("".equals(parnter_name))) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH,
						"case_code_date");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("0000");
				if (xml_time.equals(now_time)) {
					case_code = "Z"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH, "case_code_num")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH, "case_code_num", String
									.valueOf(Integer.parseInt(XmlParseUtil
											.getXmlValue(XML_PATH,
													"case_code_num")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH, "case_code_date",
							now_time);
					XmlParseUtil.updateXmlValue(XML_PATH, "case_code_num", "1");
					case_code = "Z"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH, "case_code_num")));
				}
			} else if ("河南商都司法鉴定中心".equals(parnter_name)) {
				// 判断非合作方案例生成新编号
				if (!case_username.contains("商都")) {
					String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
							"case_code_date_d");
					String now_time = com.rds.code.utils.StringUtils
							.dateToSix(new Date());
					DecimalFormat df = new DecimalFormat("0000");
					if (xml_time.equals(now_time)) {
						case_code = "D"
								+ now_time
								+ df.format(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_2,
												"case_code_num_d")) + 1);
						XmlParseUtil.updateXmlValue(XML_PATH_2,
								"case_code_num_d",
								String.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_2,
												"case_code_num_d")) + 1));
					} else {
						XmlParseUtil.updateXmlValue(XML_PATH_2,
								"case_code_date_d", now_time);
						XmlParseUtil.updateXmlValue(XML_PATH_2,
								"case_code_num_d", "1");
						case_code = "D"
								+ now_time
								+ df.format(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_2,
												"case_code_num_d")));
					}
				} else if ("商都".equals(case_username)) {
					String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
							"case_code_date_sd");
					String now_time = com.rds.code.utils.StringUtils
							.dateToSix(new Date());
					DecimalFormat df = new DecimalFormat("0000");
					if (xml_time.equals(now_time)) {
						case_code = "SD"
								+ now_time
								+ df.format(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_2,
												"case_code_num_sd")) + 1);
						XmlParseUtil.updateXmlValue(XML_PATH_2,
								"case_code_num_sd",
								String.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_2,
												"case_code_num_sd")) + 1));
					} else {
						XmlParseUtil.updateXmlValue(XML_PATH_2,
								"case_code_date_sd", now_time);
						XmlParseUtil.updateXmlValue(XML_PATH_2,
								"case_code_num_sd", "1");
						case_code = "SD"
								+ now_time
								+ df.format(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_2,
												"case_code_num_sd")));
					}
				} else if ("商都法院".equals(case_username)) {
					String xml_time = XmlParseUtil.getXmlValue(XML_PATH_7,
							"case_code_date_sdf");
					String now_time = com.rds.code.utils.StringUtils.dateToSix(
							new Date()).substring(0, 4);
					DecimalFormat df = new DecimalFormat("000");
					if (xml_time.equals(now_time)) {
						case_code = "SDF"+now_time
								+ df.format(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_7,
												"case_code_num_sdf")) + 1);
						XmlParseUtil.updateXmlValue(XML_PATH_7,
								"case_code_num_sdf",
								String.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_7,
												"case_code_num_sdf")) + 1));
					} else {
						XmlParseUtil.updateXmlValue(XML_PATH_7,
								"case_code_date_sdf", now_time);
						XmlParseUtil.updateXmlValue(XML_PATH_7,
								"case_code_num_sdf", "1");
						case_code = "SDF"
								+ df.format(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_7,
												"case_code_num_sdf")));
					}

				}
			}
			// 求实鉴定案例编码生成
			else if ("江西求实司法鉴定中心".equals(parnter_name)) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
						"case_code_date_qs");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("000");
				if (xml_time.equals(now_time)) {
					case_code = "QS"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_qs")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_2, "case_code_num_qs",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_2,
													"case_code_num_qs")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_2,
							"case_code_date_qs", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_qs",
							"1");
					case_code = "QS"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_qs")));
				}
			}
			// 金证鉴定案例编码生成
			else if ("甘肃金证司法医学鉴定所".equals(parnter_name)) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
						"case_code_date_jz");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("000");
				if (xml_time.equals(now_time)) {
					case_code = "JZ"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_jz")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_2, "case_code_num_jz",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_2,
													"case_code_num_jz")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_2,
							"case_code_date_jz", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_jz",
							"1");
					case_code = "JZ"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_jz")));
				}
			}
			// 楚雄三和鉴定案例编码生成
			else if ("楚雄三和司法鉴定所".equals(parnter_name)) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_3,
						"case_code_date_cxsh");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("0000");
				if (xml_time.equals(now_time)) {
					case_code = "SH"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_3,
											"case_code_num_cxsh")) + 1);
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_cxsh",
							String.valueOf(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_3,
											"case_code_num_cxsh")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_date_cxsh", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_cxsh", "1");
					case_code = "SH"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_3,
											"case_code_num_cxsh")));
				}
			}
			// 鼎城鉴定案例编码生成
			else if ("四川鼎城司法鉴定中心".equals(parnter_name)) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
						"case_code_date_dc");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("0000");
				if (xml_time.equals(now_time)) {
					case_code = "DC"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_dc")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_2, "case_code_num_dc",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_2,
													"case_code_num_dc")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_2,
							"case_code_date_dc", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_dc",
							"1");
					case_code = "DC"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_dc")));
				}
			}
			else if ("广东永建法医物证司法鉴定所".equals(parnter_name)) {

			    DecimalFormat df = new DecimalFormat("0000");

				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_10,
						"case_code_date_yd");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());

				if (xml_time.equals(now_time)) {
					case_code = "YJ"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_10, "case_code_num_yd")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_10, "case_code_num_yd", String
									.valueOf(Integer.parseInt(XmlParseUtil
											.getXmlValue(XML_PATH_10,
													"case_code_num_yd")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_10, "case_code_date_yd",
							now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_10, "case_code_num_yd",
							"0001");
					case_code = "YJ"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
									XML_PATH_10, "case_code_num_yd")));
				}
				
			}
			// 鸣正鉴定案例编码生成
			else if ("吉林鸣正司法鉴定中心".equals(parnter_name)) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
						"case_code_date_mz");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("0000");
				if (xml_time.equals(now_time)) {
					case_code = "M"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_mz")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_2, "case_code_num_mz",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_2,
													"case_code_num_mz")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_2,
							"case_code_date_mz", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_mz",
							"1");
					case_code = "M"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_mz")));
				}
			}
			// 十堰鉴定案例编码生成
			else if ("湖北十堰医药学院法医司法鉴定所".equals(parnter_name)) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
						"case_code_date_sy");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("000");
				if (xml_time.equals(now_time)) {
					case_code = "Y"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_sy")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_2, "case_code_num_sy",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_2,
													"case_code_num_sy")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_2,
							"case_code_date_sy", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_sy",
							"1");
					case_code = "Y"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_2, "case_code_num_sy")));
				}
			}
			// 四川鑫正鉴定案例编码生成
			else if ("四川鑫正司法鉴定所".equals(parnter_name)) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_3,
						"case_code_date_scxz");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				DecimalFormat df = new DecimalFormat("0000");
				if (xml_time.equals(now_time)) {
					case_code = "XZ"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_3,
											"case_code_num_scxz")) + 1);
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_scxz",
							String.valueOf(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_3,
											"case_code_num_scxz")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_date_scxz", now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_scxz", "1");
					case_code = "XZ"
							+ now_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_3,
											"case_code_num_scxz")));
				}
			} else {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_1,
						"case_code_date");
				String now_time = com.rds.code.utils.StringUtils
						.dateToSix(new Date());
				// DecimalFormat df = new DecimalFormat("1000");
				if (xml_time.equals(now_time)) {
					case_code = "Z"
							+ now_time
							+ String.valueOf(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_1, "case_code_num")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_1, "case_code_num", String
									.valueOf(Integer.parseInt(XmlParseUtil
											.getXmlValue(XML_PATH_1,
													"case_code_num")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_1, "case_code_date",
							now_time);
					XmlParseUtil.updateXmlValue(XML_PATH_1, "case_code_num",
							"1000");
					case_code = "Z"
							+ now_time
							+ Integer.parseInt(XmlParseUtil.getXmlValue(
									XML_PATH_1, "case_code_num"));
				}
			}
			// 医学案例编号生成
		} else {
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH,
					"case_medical_date");
			String now_time = com.rds.code.utils.StringUtils
					.dateToSix(new Date());
			DecimalFormat df = new DecimalFormat("0000");
			if (xml_time.equals(now_time)) {
				case_code = "Q"
						+ now_time
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH, "case_medical_num")) + 1);
				XmlParseUtil
						.updateXmlValue(XML_PATH, "case_medical_num", String
								.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH,
												"case_medical_num")) + 1));
			} else {
				XmlParseUtil.updateXmlValue(XML_PATH, "case_medical_date",
						now_time);
				XmlParseUtil.updateXmlValue(XML_PATH, "case_medical_num", "1");
				case_code = "Q"
						+ now_time
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH, "case_medical_num")));
			}
		}
		return case_code;
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

	/**
	 * 插入子案例
	 * 
	 * @param sample_codes
	 * @param sample_calls
	 * @param params
	 * @param sign
	 */
	private void addSubCaseInfo(List<String> sample_codes,
			List<String> sample_calls, Map<String, Object> params, String sign) {
		deleteSubCaseInfo(params);
		if (sample_codes.size() < 2) {
			return;
		}
		// List<Map<String, String>> parents = new LinkedList<Map<String,
		// String>>();
		List<Map<String, String>> fathers = new LinkedList<Map<String, String>>();
		List<Map<String, String>> mothers = new LinkedList<Map<String, String>>();
		List<Map<String, String>> children = new LinkedList<Map<String, String>>();
		List<Map<String, String>> others = new LinkedList<Map<String, String>>();

		for (int i = 0; i < sample_codes.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("sample_call", sample_calls.get(i));
			map.put("sample_code", sample_codes.get(i));
			if (sample_calls.get(i).equals("father")) {
				fathers.add(map);
			} else if (sample_calls.get(i).equals("mother")) {
				mothers.add(map);
			} else if (sample_calls.get(i).equals("son")
					|| sample_calls.get(i).equals("daughter")) {
				children.add(map);
			} else {
				others.add(map);
			}
		}
		if (mothers.size() > 0 || fathers.size() > 0) {
			int countChild = 1;
			for (Map<String, String> childrenMap : children) {
				RdsJudicialSubCaseInfoModel subCase = new RdsJudicialSubCaseInfoModel();
				if (!StringUtils.isEmpty(sign)) {
					subCase.setResult(null);
				}
				subCase.setCase_code((String) params.get("case_code"));
				if (fathers.size() > 0 && mothers.size() > 0) {
					for (Map<String, String> father : fathers) {
						subCase.setSample_code1(father.get("sample_code"));
						for (Map<String, String> mother : mothers) {
							subCase.setSample_code2(mother.get("sample_code"));
							subCase.setSample_code3(childrenMap
									.get("sample_code"));
							subCase.setSub_case_code(params.get("case_code")
									+ "_" + countChild);
							rdsJudicialSubCaseMapper.insert(subCase);
							countChild++;
						}
					}
				} else if (fathers.size() > 0) {
					for (Map<String, String> father : fathers) {
						subCase.setSample_code1(father.get("sample_code"));
						subCase.setSample_code3(childrenMap.get("sample_code"));
						subCase.setSub_case_code(params.get("case_code") + "_"
								+ countChild);
						rdsJudicialSubCaseMapper.insert(subCase);
						countChild++;
					}
				} else {
					for (Map<String, String> mother : mothers) {
						subCase.setSample_code1(mother.get("sample_code"));
						subCase.setSample_code3(childrenMap.get("sample_code"));
						subCase.setSub_case_code(params.get("case_code") + "_"
								+ countChild);
						rdsJudicialSubCaseMapper.insert(subCase);
						countChild++;
					}
				}
			}
			for (Map<String, String> childrenMap : others) {
				RdsJudicialSubCaseInfoModel subCase = new RdsJudicialSubCaseInfoModel();
				if (!StringUtils.isEmpty(sign)) {
					subCase.setResult(null);
				}
				subCase.setCase_code((String) params.get("case_code"));
				if (fathers.size() > 0 && mothers.size() > 0) {
					for (Map<String, String> father : fathers) {
						subCase.setSample_code1(father.get("sample_code"));
						for (Map<String, String> mother : mothers) {
							subCase.setSample_code2(mother.get("sample_code"));
							subCase.setSample_code3(childrenMap
									.get("sample_code"));
							subCase.setSub_case_code(params.get("case_code")
									+ "_" + countChild);
							rdsJudicialSubCaseMapper.insert(subCase);
							countChild++;
						}
					}
				} else if (fathers.size() > 0) {
					for (Map<String, String> father : fathers) {
						subCase.setSample_code1(father.get("sample_code"));
						subCase.setSample_code3(childrenMap.get("sample_code"));
						subCase.setSub_case_code(params.get("case_code") + "_"
								+ countChild);
						rdsJudicialSubCaseMapper.insert(subCase);
						countChild++;
					}
				} else {
					for (Map<String, String> mother : mothers) {
						subCase.setSample_code1(mother.get("sample_code"));
						subCase.setSample_code3(childrenMap.get("sample_code"));
						subCase.setSub_case_code(params.get("case_code") + "_"
								+ countChild);
						rdsJudicialSubCaseMapper.insert(subCase);
						countChild++;
					}
				}
			}
			// 同胞亲缘比对，案例中只有2人且一个子案例
		} else if (others.size() == 2) {
			RdsJudicialSubCaseInfoModel subCase = new RdsJudicialSubCaseInfoModel();
			subCase.setCase_code((String) params.get("case_code"));
			subCase.setSample_code1(others.get(0).get("sample_code"));
			subCase.setSample_code3(others.get(1).get("sample_code"));
			subCase.setSub_case_code(params.get("case_code") + "_" + 1);
			rdsJudicialSubCaseMapper.insert(subCase);
		}
	}

	private void deleteSubCaseInfo(Map<String, Object> params) {
		rdsJudicialSubCaseMapper.deleteSubCaseInfo(params);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<String> getSampleCode(List<String> list, String case_code) {
		List<String> sampleCodes = new ArrayList<String>();
        if(case_code.startsWith("WZ-")){
        	case_code=case_code.substring(0,3)+com.rds.code.utils.StringUtils
					.dateToSix(new Date()).substring(0, 4)+"-"+case_code.substring(3);
        }
		if ("".equals(sampleCall(list.get(0).toString()))) {
			for (int i = 0; i < list.size(); i++) {
				sampleCodes.add(case_code + sampleNum(i + 1));
			}
		} else {
			String strings[] = new String[list.size()];

			for (int i = 0, j = list.size(); i < j; i++) {
				strings[i] = list.get(i).toString();
			}
			Set uniqueSet = new HashSet(list);
			for (Object temp : uniqueSet) {

				String call = sampleCall(temp.toString());

				List listTemp1 = new ArrayList();
				if (Collections.frequency(list, temp) > 1) {
					for (int j = 0; j < Collections.frequency(list, temp); j++) {
						listTemp1.add(temp);
					}
					for (int k = 1; k <= listTemp1.size(); k++) {
						for (int m = 0; m < strings.length; m++) {
							if (strings[m].equals(listTemp1.get(0).toString())) {
								strings[m] = case_code + call + sampleNum(k);
								break;
							}
						}
					}

				} else {
					for (int m = 0; m < strings.length; m++) {
						if (strings[m].equals(temp.toString())) {
							strings[m] = case_code + call;
						}
					}
				}
			}
			if (strings.length > 1)
				sampleCodes = Arrays.asList(strings);
			else
				sampleCodes.add(strings[0]);
		}

		return sampleCodes;
	}
	//青岛万方模板生成-1，-2，-3等案例编号
		private List<String> getSampleCodeQD(List<String> list, String case_code) {
			List<String> sampleCodes = new ArrayList<String>();

				String strings[] = new String[list.size()];

				for (int i = 0, j = list.size(); i < j; i++) {
					strings[i] = list.get(i).toString();
				}
				for (Object temp : list) {
						for (int m = 0; m < strings.length; m++) {
							if (strings[m].equals(temp.toString())) {
								int a=m+1;
								strings[m] = case_code + "-"+a;
							}
						}
					}

				if (strings.length > 1)
					sampleCodes = Arrays.asList(strings);
				else
					sampleCodes.add(strings[0]);


			return sampleCodes;
		}
		//安徽至正生成带-F,M，S样本编号
		@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
		private List<String> getSampleCodeAhzz(List<String> list, String case_code) {
			List<String> sampleCodes = new ArrayList<String>();
				String strings[] = new String[list.size()];

				for (int i = 0, j = list.size(); i < j; i++) {
					strings[i] = list.get(i).toString();
				}
				Set uniqueSet = new HashSet(list);
				for (Object temp : uniqueSet) {

					String call = sampleCall(temp.toString());

					List listTemp1 = new ArrayList();
					if (Collections.frequency(list, temp) > 1) {
						for (int j = 0; j < Collections.frequency(list, temp); j++) {
							listTemp1.add(temp);
						}
						for (int k = 1; k <= listTemp1.size(); k++) {
							for (int m = 0; m < strings.length; m++) {
								if (strings[m].equals(listTemp1.get(0).toString())) {
									strings[m] = case_code +"-"+call + sampleNum(k);
									break;
								}
							}
						}

					} else {
						for (int m = 0; m < strings.length; m++) {
							if (strings[m].equals(temp.toString())) {
								strings[m] = case_code +"-"+call;
							}
						}
					}
				}
				if (strings.length > 1)
					sampleCodes = Arrays.asList(strings);
				else
					sampleCodes.add(strings[0]);
			

			return sampleCodes;
		}
	private List<String> getSampleCodeOther(List<String> list, String case_code) {
		List<String> sampleCodes = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			sampleCodes.add(case_code + sampleNum(i + 1));
		}

		return sampleCodes;
	}

	private String sampleCall(String call) {
		switch (call) {
		case "father":
			return "F";
		case "mother":
			return "M";
		case "son":
			return "S";
		case "daughter":
			return "D";
		case "child":
			return "H";
		default:
			return "";
		}
	}

	private String sampleNum(int num) {
		switch (num) {
		case 1:
			return "A";
		case 2:
			return "B";
		case 3:
			return "C";
		case 4:
			return "D";
		case 5:
			return "E";
		case 6:
			return "F";
		case 7:
			return "G";
		case 8:
			return "H";
		case 9:
			return "I";
		case 10:
			return "J";
		case 11:
			return "K";
		case 12:
			return "L";
		case 13:
			return "M";
		default:
			return "";
		}
	}

	@SuppressWarnings("unchecked")
	private void addCaseSample(Map<String, Object> params) {
		List<String> sample_types = getValues(params.get("sample_type") == null ? ""
				: params.get("sample_type"));
		// 样本姓名
		List<String> sample_usernames = getValues(params.get("sample_username"));
		// 样本身份证号
		List<String> id_numbers = getValues(params.get("id_number") == null ? ""
				: params.get("id_number"));
		// 样本称谓
		List<String> sample_calls = getValues(params.get("sample_call"));
		// 样本出生日期
		List<String> birth_dates = getValues(params.get("birth_date") == null ? ""
				: params.get("birth_date"));
		// 样本条形码
		List<String> sample_code = (List<String>) params.get("sample_code_sys");
		String fandm = "";
		String child = "";
		String id_card = "";
		String birth_date = "";
		int sample_count = 0;
		sample_count = sample_usernames.size();
		// 父母亲，孩子排序
		List<RdsJudicialSampleInfoModel> sampleInfoFM = new ArrayList<RdsJudicialSampleInfoModel>();
		List<RdsJudicialSampleInfoModel> sampleInfoC = new ArrayList<RdsJudicialSampleInfoModel>();
		for (int i = 0; i < sample_count; i++) {
			String sample_call = sample_calls.get(i);
			RdsJudicialKeyValueModel modelCall = RdsJudicialRegisterMapper
					.getSampleCallKey(sample_call);
			RdsJudicialKeyValueModel sample_type = RdsJudicialRegisterMapper
					.getSampleCallKey(sample_types.get(i));
			// 判断哪些在父母亲那一栏
			if ("1".equals(modelCall.getCallstatus())) {
				RdsJudicialSampleInfoModel fm = new RdsJudicialSampleInfoModel();
				fm.setSample_call(modelCall == null ? "" : modelCall.getValue());
				fm.setSample_username(sample_usernames.get(i));
				fm.setSample_type(sample_type == null ? "" : sample_type
						.getValue());
				fm.setSample_code(sample_code.get(i));
				fm.setBirth_date(birth_dates.get(i));
				fm.setId_number(id_numbers.get(i));
				sampleInfoFM.add(fm);
			} else {
				RdsJudicialSampleInfoModel fm = new RdsJudicialSampleInfoModel();
				fm.setSample_call(modelCall == null ? "" : modelCall.getValue());
				fm.setSample_username(sample_usernames.get(i));
				fm.setSample_type(sample_type == null ? "" : sample_type
						.getValue());
				fm.setSample_code(sample_code.get(i));
				fm.setBirth_date(birth_dates.get(i));
				fm.setId_number(id_numbers.get(i));
				sampleInfoC.add(fm);
			}
		}
		// 父母亲样本排序
		if (sampleInfoFM.size() > 1) {
			for (int i = 0; i < sampleInfoFM.size() - 1; i++) {
				for (int j = i + 1; j < sampleInfoFM.size(); j++) {
					// 判断样本条码为空情况
					if (!"".equals(sampleInfoFM.get(i).getSample_code())) {
						if ((sampleInfoFM.get(i).getSample_code()
								.substring(sampleInfoFM.get(i).getSample_code()
										.length() - 1, sampleInfoFM.get(i)
										.getSample_code().length()))
								.compareTo((sampleInfoFM.get(j)
										.getSample_code().substring(
										sampleInfoFM.get(j).getSample_code()
												.length() - 1, sampleInfoFM
												.get(j).getSample_code()
												.length()))) > 0) {
							RdsJudicialSampleInfoModel temp = new RdsJudicialSampleInfoModel();
							temp = sampleInfoFM.get(i);
							sampleInfoFM.set(i, sampleInfoFM.get(j));
							sampleInfoFM.set(j, temp);
						}
					}
				}
			}
		}
		// 孩子样本排序
		if (sampleInfoC.size() > 1) {
			for (int i = 0; i < sampleInfoC.size() - 1; i++) {
				for (int j = i + 1; j < sampleInfoC.size(); j++) {
					// 判断样本条码为空情况
					if (!"".equals(sampleInfoC.get(i).getSample_code())) {
						if ((sampleInfoC.get(i).getSample_code()
								.substring(sampleInfoC.get(i).getSample_code()
										.length() - 1, sampleInfoC.get(i)
										.getSample_code().length()))
								.compareTo((sampleInfoC.get(j).getSample_code()
										.substring(sampleInfoC.get(j)
												.getSample_code().length() - 1,
												sampleInfoC.get(j)
														.getSample_code()
														.length()))) > 0) {
							RdsJudicialSampleInfoModel temp = new RdsJudicialSampleInfoModel();
							temp = sampleInfoC.get(i);
							sampleInfoC.set(i, sampleInfoC.get(j));
							sampleInfoC.set(j, temp);
						}
					}
				}
			}
		}
		for (RdsJudicialSampleInfoModel model : sampleInfoFM) {
			fandm += model.getSample_call() + "-" + model.getSample_username()
					+ "-" + model.getSample_type() + ";";
			id_card += (null == model.getId_number() || "".equals(model
					.getId_number())) ? "" : (model.getId_number() + ";");
			try {
				birth_date += (null == model.getBirth_date() || "".equals(model
						.getBirth_date())) ? ""
						: (com.rds.code.utils.StringUtils
								.dateToChineseTen(model.getBirth_date()) + ";");
			} catch (Exception e) {
				System.out
						.println("judcialRegitster----------------string to date exception!!");
				e.printStackTrace();
			}
		}
		for (RdsJudicialSampleInfoModel model : sampleInfoC) {
			child += model.getSample_call() + "-" + model.getSample_username()
					+ "-" + model.getSample_type() + ";";
			id_card += (null == model.getId_number() || "".equals(model
					.getId_number())) ? "" : (model.getId_number() + ";");
			try {
				birth_date += (null == model.getBirth_date() || "".equals(model
						.getBirth_date())) ? ""
						: (com.rds.code.utils.StringUtils
								.dateToChineseTen(model.getBirth_date()) + ";");
			} catch (Exception e) {
				System.out
						.println("judcialRegitster----------------string to date exception!!");
				e.printStackTrace();
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fandm",
				fandm.endsWith(";") ? fandm.subSequence(0, fandm.length() - 1)
						: fandm);
		map.put("child",
				child.endsWith(";") ? child.subSequence(0, child.length() - 1)
						: child);
		map.put("id_card",
				id_card.endsWith(";") ? id_card.subSequence(0,
						id_card.length() - 1) : id_card);
		map.put("birth_date",
				birth_date.endsWith(";") ? birth_date.subSequence(0,
						birth_date.length() - 1) : birth_date);
		map.put("case_id", params.get("case_id"));
		map.put("sample_count", sample_count);
		map.put("case_area", params.get("case_area"));
		if (null == params.get("updateFlag"))
			RdsJudicialRegisterMapper.addCaseSample(map);
		else
			RdsJudicialRegisterMapper.updateCaseSample(map);
	}

	@Override
	@Transactional
	public Map<String, Object> updateSampleCaseInfo(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		result.put("result", true);
		try {
			String case_code = params.get("case_code").toString();
			String case_code_old = params.get("case_code_old").toString();
			// 判断案例编码改变的情况
			if (!case_code.equals(case_code_old)) {
				// 校验修改的案例编号是否已存在
				Map<String, Object> map1 = new HashMap<>();
				map1.put("case_code", case_code);
				if (RdsJudicialRegisterMapper.exsitCaseCode(map1) > 0) {
					System.out.println("已存在case_code=================="
							+ case_code);
					result.put("success", true);
					result.put("result", false);
					result.put("msg", "该案例编号已存在！");
					return result;
				}
				// 设置xml条码
				result = caseCodeSet(case_code, case_code_old);

			}
			if (!(boolean) result.get("result")) {
				return result;
			} else {
				// 案例编号不一致情况，删除旧案例的子案例
				Map<String, Object> map = new HashMap<>();
				map.put("case_code", case_code_old);
				rdsJudicialSubCaseMapper.deleteSubCaseInfo(map);
				// 更新案例条码
				rdsJudicialVerifyMapper.updateCaseInfoCaseCode(params);
			}
			List<String> sample_calls = getValues(params.get("sample_call"));
			/* 自动生成样本条形码 end */
			List<String> sample_codes = getValues(params.get("sample_code"));
			List<String> sample_code_sys = getValues(params
					.get("sample_code_sys"));
			List<String> sample_types = getValues(params.get("sample_type"));
			List<String> sample_usernames = getValues(params
					.get("sample_username"));
			List<String> id_numbers = getValues(params.get("id_number"));
			List<String> address = getValues(params.get("address"));
			List<String> birth_dates = getValues(params.get("birth_date"));
			List<String> specials = getValues(params.get("special"));
			List<String> sample_code_olds = getValues(params.get("sample_code_old"));
			// 插入子案例
			addSubCaseInfo(sample_codes, sample_calls, params, null);
			// 删除样本信息
			RdsJudicialRegisterMapper.deleteSampleInfo(params);
			for (int i = 0; i < sample_codes.size(); i++) {
				RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel();
				sampleInfoModel.setSample_id(UUIDUtil.getUUID());
				sampleInfoModel.setSample_code(sample_codes.get(i));
				sampleInfoModel.setSample_code_sys(sample_code_sys.get(i));
				sampleInfoModel.setSample_call(sample_calls.get(i));
				sampleInfoModel.setSample_username(sample_usernames.get(i));
				sampleInfoModel.setId_number(id_numbers.get(i));
				sampleInfoModel.setBirth_date(birth_dates.get(i));
				sampleInfoModel.setSample_type(sample_types.get(i));
				sampleInfoModel.setCase_id(params.get("case_id").toString());
				sampleInfoModel.setSpecial(specials.get(i));
				sampleInfoModel.setAddress(address.get(i));
				String sample_code=sample_codes.get(i);
				String sample_code_old=sample_code_olds.get(i);
				if(!sample_code_old.equals(sample_code)&&null!=sample_code&&null !=sample_code_old&&!"".equals(sample_code_old)&&!"".equals(sample_code)){
				Map<String, Object> samplecodes = new HashMap<String, Object>();
				samplecodes.put("sample_code_old", sample_code_old);samplecodes.put("sample_code", sample_code);
				rdsJudicialSampleRelayMapper.updateReceiveSamplecode(samplecodes);
				}
				RdsJudicialRegisterMapper.insertSampleInfo(sampleInfoModel);
			}
			params.put("updateFlag", 1);
			addCaseSample(params);
			result.put("success", true);
			result.put("result", true);
			result.put("msg", "操作成功！");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("result", false);
			result.put("msg", "出错了,请联系管理员！");
			return result;
		}
	}

	/**
	 * 更新案例条码
	 * 
	 * @param case_code
	 * @param case_code_old
	 * @return
	 */
	private Map<String, Object> caseCodeSet(String case_code,
			String case_code_old) {
		Map<String, Object> result = new HashMap<>();
		// 当前时间
		String now_time = com.rds.code.utils.StringUtils.dateToSix(new Date());
		String temp = "";
		String temp_old = "";
		// 商都法院特殊处理
		if (case_code.startsWith("SDF")) {
			temp = case_code.substring(case_code.length() - 3,
					case_code.length());
			temp_old = case_code_old.substring(case_code_old.length() - 3,
					case_code_old.length());
		} else {
			temp = case_code.substring(case_code.length() - 4,
					case_code.length());
			temp_old = case_code_old.substring(case_code_old.length() - 4,
					case_code_old.length());
		}
		// 新案例编号自增
		int new_num = Integer.parseInt(temp);
		// 被修改的案例编号
		int old_num_temp = Integer.parseInt(temp_old);
		// 子渊案例编号处理
		if (case_code.startsWith("Z") && case_code_old.startsWith("Z")) {
			// 原来案例省外，改成省内案例编号
			if (temp.startsWith("0")) {
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH,
						"case_code_date");
				// 判断是当前月份
				if (xml_time.equals(now_time)) {
					// 获取xml文件里的最新时间
					int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
							XML_PATH, "case_code_num"));
					// 判断修改的案例编号是否为当前月份最大案例
					if (case_code.contains(now_time)) {
						if (new_num > (old_num + 1)) {
							result.put("success", true);
							result.put("result", false);
							result.put("msg", "该案例编号太大了!");
							return result;
						}
					}
					// 修改最大的编号至小的案例编号
					if (old_num_temp == old_num) {
						XmlParseUtil.updateXmlValue(XML_PATH, "case_code_num",
								String.valueOf(old_num_temp - 1));
					}
					// 修改小的案例编号为最大的案例编号
					else if (new_num == (old_num + 1)) {
						XmlParseUtil.updateXmlValue(XML_PATH, "case_code_num",
								String.valueOf(new_num));
					}
				} else {
					result.put("success", true);
					result.put("result", false);
					result.put("msg", "本月审核过再改吧!");
					return result;
				}

			}
			// 省内改省外
			else if (temp.startsWith("1")||temp.startsWith("2")||temp.startsWith("3")) {
				// 获取当前配置文件中的日期
				String xml_time = XmlParseUtil.getXmlValue(XML_PATH_1,
						"case_code_date");
				// 判断是当前月份
				if (xml_time.equals(now_time)) {
					// 获取xml文件里的最新时间
					int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
							XML_PATH_1, "case_code_num"));
					// 判断修改的案例编号是否为当前月份最大案例
					if (case_code.contains(now_time)) {
						if (new_num > (old_num + 1)) {
							result.put("success", true);
							result.put("result", false);
							result.put("msg", "该案例编号太大了!");
							return result;
						}
					}
					// 修改最大的编号至小的案例编号
					if (old_num_temp == old_num) {
						XmlParseUtil.updateXmlValue(XML_PATH_1,
								"case_code_num",
								String.valueOf(old_num_temp - 1));
					}
					// 修改小的案例编号为最大的案例编号
					else if (new_num == (old_num + 1)) {
						XmlParseUtil.updateXmlValue(XML_PATH_1,
								"case_code_num", String.valueOf(new_num));
					}
				} else {
					result.put("success", true);
					result.put("result", false);
					result.put("msg", "本月审核过再改吧!");
					return result;
				}
			}
		}
//		// 判断是否商都业务员案例
//		else if (case_code.startsWith("D2") && case_code_old.startsWith("D2")) {
//			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
//					"case_code_date_d");
//			// 判断是当前月份
//			if (xml_time.equals(now_time)) {
//				// 获取xml文件里的最新时间
//				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
//						XML_PATH_2, "case_code_num_d"));
//				// 判断修改的案例编号是否为当前月份最大案例
//				if (case_code.contains(now_time)) {
//					if (new_num > (old_num + 1)) {
//						result.put("success", true);
//						result.put("result", false);
//						result.put("msg", "该案例编号太大了!");
//						return result;
//					}
//				}
//				// 修改最大的编号至小的案例编号
//				if (old_num_temp == old_num) {
//					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_d",
//							String.valueOf(old_num_temp - 1));
//				}
//				// 修改小的案例编号为最大的案例编号
//				else if (new_num == (old_num + 1)) {
//					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_d",
//							String.valueOf(new_num));
//				}
//			} else {
//				result.put("success", true);
//				result.put("result", false);
//				result.put("msg", "本月审核过再改吧!");
//				return result;
//			}
//
//		}
//		else if (case_code.startsWith("2018") && case_code_old.startsWith("2018")&&case_code.length()==8&&case_code_old.length()==8) {
//			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_8,
//					"case_code_date_qdwf");
//			now_time=now_time.substring(0, 4);
//			// 判断是当前月份
//			if (xml_time.equals(now_time)) {
//				// 获取xml文件里的最新时间
//				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
//						XML_PATH_8, "case_code_num_qdwf"));
//				// 判断修改的案例编号是否为当前月份最大案例
//				if (case_code.contains(now_time)) {
//					if (new_num > (old_num + 1)) {
//						result.put("success", true);
//						result.put("result", false);
//						result.put("msg", "该案例编号太大了!");
//						return result;
//					}
//				}
//				// 修改最大的编号至小的案例编号
//				if (old_num_temp == old_num) {
//					XmlParseUtil.updateXmlValue(XML_PATH_8, "case_code_num_qdwf",
//							String.valueOf(old_num_temp - 1));
//				}
//				// 修改小的案例编号为最大的案例编号
//				else if (new_num == (old_num + 1)) {
//					XmlParseUtil.updateXmlValue(XML_PATH_8, "case_code_num_qdwf",
//							String.valueOf(new_num));
//				}
//			} else {
//				result.put("success", true);
//				result.put("result", false);
//				result.put("msg", "本月审核过再改吧!");
//				return result;
//			}
//
//		}
		// 判断是否商都案例
//		else if (case_code.startsWith("SD2") && case_code_old.startsWith("SD2")) {
//			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
//					"case_code_date_sd");
//			// 判断是当前月份
//			if (xml_time.equals(now_time)) {
//				// 获取xml文件里的最新时间
//				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
//						XML_PATH_2, "case_code_num_sd"));
//				// 判断修改的案例编号是否为当前月份最大案例
//				if (case_code.contains(now_time)) {
//					if (new_num > (old_num + 1)) {
//						result.put("success", true);
//						result.put("result", false);
//						result.put("msg", "该案例编号太大了!");
//						return result;
//					}
//				}
//				// 修改最大的编号至小的案例编号
//				if (old_num_temp == old_num) {
//					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_sd",
//							String.valueOf(old_num_temp - 1));
//				}
//				// 修改小的案例编号为最大的案例编号
//				else if (new_num == (old_num + 1)) {
//					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_sd",
//							String.valueOf(new_num));
//				}
//			} else {
//				result.put("success", true);
//				result.put("result", false);
//				result.put("msg", "本月审核过再改吧!");
//				return result;
//			}
//		}
		// 判断是否商都法院案例
//		else if (case_code.startsWith("SDF") && case_code_old.startsWith("SDF")) {
//			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_7,
//					"case_code_date_sdf");
//			now_time = com.rds.code.utils.StringUtils.dateToSix(new Date())
//					.substring(0, 4);
//			// 判断是当前年份
//			if (xml_time.equals(now_time)) {
//				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
//						XML_PATH_7, "case_code_num_sdf"));
//				// 判断修改的案例编号是否为当前月份最大案例
//				if (case_code.contains(now_time)) {
//					if (new_num > (old_num + 1)) {
//						result.put("success", true);
//						result.put("result", false);
//						result.put("msg", "该案例编号太大了!");
//						return result;
//					}
//				}
//				// 判断新编号如果大于xml文件内配置的最大值加1给出报错信息
//				if (new_num == (old_num + 1)) {
//					XmlParseUtil.updateXmlValue(XML_PATH_7,
//							"case_code_num_sdf", String.valueOf(new_num));
//				}// 判断是否拿最新案例条码修改
//				else if (old_num_temp == old_num) {
//					XmlParseUtil.updateXmlValue(XML_PATH_7,
//							"case_code_num_sdf",
//							String.valueOf(old_num_temp - 1));
//				}
//			} else {
//				result.put("success", true);
//				result.put("result", false);
//				result.put("msg", "本月审核过再改吧!");
//				return result;
//			}
//		}
		// 判断是否毕节黔案例,非CMA转化
				else if (case_code.startsWith("BJ2") && case_code_old.startsWith("BJ2")&&case_code.length()>=12&&case_code_old.length()>=12) {

					String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
							"case_code_date_bj");
					// 判断是当前月份
					if (xml_time.equals(now_time)) {
						int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH_2, "case_code_num_bj"));
						// 判断修改的案例编号是否为当前月份最大案例
						if (case_code.contains(now_time)) {
							if (new_num > (old_num + 1)) {
								result.put("success", true);
								result.put("result", false);
								result.put("msg", "该案例编号太大了!");
								return result;
							}
						}
						if (new_num == (old_num + 1)) {
							XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_bj",
									String.valueOf(new_num));
						}// 判断是否拿最新案例条码修改
						else if (old_num_temp == old_num) {
							XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_bj",
									String.valueOf(old_num_temp - 1));
						}
					} else {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "本月审核过再改吧!");
						return result;
					}
				}//毕节省外转省内
				else if (case_code.startsWith("BJ2") && case_code_old.startsWith("BJ2")&&case_code.length()<12&&case_code_old.length()>=12) {

					String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
							"case_code_date_bj_CMA");
					now_time=now_time.substring(0, 4);
					// 判断是当前月份
					if (xml_time.equals(now_time)) {
						int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH_2, "case_code_num_bj_CMA"));
						// 判断修改的案例编号是否为当前月份最大案例
						if (case_code.contains(now_time)) {
							if (new_num > (old_num + 1)) {
								result.put("success", true);
								result.put("result", false);
								result.put("msg", "该案例编号太大了!");
								return result;
							}
						}
						if (new_num == (old_num + 1)) {
							XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_bj_CMA",
									String.valueOf(new_num));
						}// 判断是否拿最新案例条码修改
						else if (old_num_temp == old_num) {
							XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_bj_CMA",
									String.valueOf(old_num_temp - 1));
						}
					} else {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "本月审核过再改吧!");
						return result;
					}
				}
		//毕节省内转省内
				else if (case_code.startsWith("BJ2") && case_code_old.startsWith("BJ2")&&case_code.length()<12&&case_code_old.length()<12) {

					String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
							"case_code_date_bj_CMA");
					now_time=now_time.substring(0, 4);
					// 判断是当前月份
					if (xml_time.equals(now_time)) {
						int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH_2, "case_code_num_bj_CMA"));
						// 判断修改的案例编号是否为当前月份最大案例
						if (case_code.contains(now_time)) {
							if (new_num > (old_num + 1)) {
								result.put("success", true);
								result.put("result", false);
								result.put("msg", "该案例编号太大了!");
								return result;
							}
						}
						if (new_num == (old_num + 1)) {
							XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_bj_CMA",
									String.valueOf(new_num));
						}// 判断是否拿最新案例条码修改
						else if (old_num_temp == old_num) {
							XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_bj_CMA",
									String.valueOf(old_num_temp - 1));
						}
					} else {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "本月审核过再改吧!");
						return result;
					}
				}//省内转省外
				else if (case_code.startsWith("BJ2") && case_code_old.startsWith("BJ2")&&case_code.length()>=12&&case_code_old.length()<12) {

					String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
							"case_code_date_bj");
					// 判断是当前月份
					if (xml_time.equals(now_time)) {
						int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH_2, "case_code_num_bj"));
						// 判断修改的案例编号是否为当前月份最大案例
						if (case_code.contains(now_time)) {
							if (new_num > (old_num + 1)) {
								result.put("success", true);
								result.put("result", false);
								result.put("msg", "该案例编号太大了!");
								return result;
							}
						}
						if (new_num == (old_num + 1)) {
							XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_bj",
									String.valueOf(new_num));
						}// 判断是否拿最新案例条码修改
						else if (old_num_temp == old_num) {
							XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_bj",
									String.valueOf(old_num_temp - 1));
						}
					} else {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "本月审核过再改吧!");
						return result;
					}
				}
		// 判断是否医学案例
		else if (case_code.startsWith("Q2") && case_code_old.startsWith("Q2")) {
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH,
					"case_medical_date");
			// 判断是当前月份
			if (xml_time.equals(now_time)) {
				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
						XML_PATH, "case_medical_num"));
				// 判断修改的案例编号是否为当前月份最大案例
				if (case_code.contains(now_time)) {
					if (new_num > (old_num + 1)) {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "该案例编号太大了!");
						return result;
					}
				}
				if (new_num == (old_num + 1)) {
					XmlParseUtil.updateXmlValue(XML_PATH, "case_medical_num",
							String.valueOf(new_num));
				}// 判断是否拿最新案例条码修改
				else if (old_num_temp == old_num) {
					XmlParseUtil.updateXmlValue(XML_PATH, "case_medical_num",
							String.valueOf(old_num_temp - 1));
				}
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("msg", "本月审核过再改吧!");
				return result;
			}
		}
		// 判断是否金证案例
		else if (case_code.startsWith("JZ2") && case_code_old.startsWith("JZ2")) {
			temp = case_code.substring(case_code.length() - 3,
					case_code.length());
			temp_old = case_code_old.substring(case_code_old.length() - 3,
					case_code_old.length());
			// 新案例编号自增
			new_num = Integer.parseInt(temp);
			old_num_temp = Integer.parseInt(temp_old);
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
					"case_code_date_jz");
			// 判断是当前月份
			if (xml_time.equals(now_time)) {
				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
						XML_PATH_2, "case_code_num_jz"));
				// 判断修改的案例编号是否为当前月份最大案例
				if (case_code.contains(now_time)) {
					if (new_num > (old_num + 1)) {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "该案例编号太大了!");
						return result;
					}
				}
				if (new_num == (old_num + 1)) {
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_jz",
							String.valueOf(new_num));
				}// 判断是否拿最新案例条码修改
				else if (old_num_temp == old_num) {
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_jz",
							String.valueOf(old_num_temp - 1));
				}
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("msg", "本月审核过再改吧!");
				return result;
			}
		}
		// 判断是否楚雄三和案例
		else if (case_code.startsWith("SH2") && case_code_old.startsWith("SH2")) {
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_3,
					"case_code_date_cxsh");
			// 判断是当前月份
			if (xml_time.equals(now_time)) {
				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
						XML_PATH_3, "case_code_num_cxsh"));
				// 判断修改的案例编号是否为当前月份最大案例
				if (case_code.contains(now_time)) {
					if (new_num > (old_num + 1)) {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "该案例编号太大了!");
						return result;
					}
				}
				if (new_num == (old_num + 1)) {
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_cxsh", String.valueOf(new_num));
				}// 判断是否拿最新案例条码修改
				else if (old_num_temp == old_num) {
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_cxsh",
							String.valueOf(old_num_temp - 1));
				}
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("msg", "本月审核过再改吧!");
				return result;
			}
		}
		else if (case_code.startsWith("SH2") && case_code_old.startsWith("WZ-")) {
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_3,
					"case_code_date_cxsh");
			// 判断是当前月份
			if (xml_time.equals(now_time)) {
				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
						XML_PATH_3, "case_code_num_cxsh"));
				// 判断修改的案例编号是否为当前月份最大案例
				if (case_code.contains(now_time)) {
					if (new_num > (old_num + 1)) {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "该案例编号太大了!");
						return result;
					}
				}
				if (new_num == (old_num + 1)) {
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_cxsh", String.valueOf(new_num));
				}// 判断是否拿最新案例条码修改
				else if (old_num_temp == old_num) {
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_cxsh",
							String.valueOf(old_num_temp - 1));
				}
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("msg", "本月审核过再改吧!");
				return result;
			}
		}//楚雄非CMA案例改CMA案例编号
		else if (case_code.startsWith("WZ-") && case_code_old.startsWith("SH2")) {
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_3,
					"case_code_date_cxsh_CMA");
			now_time=now_time.substring(0,4);
			// 判断是当前年份
			if (xml_time.equals(now_time)) {
				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
						XML_PATH_3, "case_code_num_cxsh_CMA"));
				// 判断修改的案例编号是否为当前月份最大案例
				if (case_code.contains(now_time)) {
					if (new_num > (old_num + 1)) {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "该案例编号太大了!");
						return result;
					}
				}
				if (new_num == (old_num + 1)) {
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_cxsh_CMA", String.valueOf(new_num));
				}// 判断是否拿最新案例条码修改
				else if (old_num_temp == old_num) {
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_cxsh_CMA",
							String.valueOf(old_num_temp - 1));
				}
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("msg", "本月审核过再改吧!");
				return result;
			}
		}//楚雄CMA案例改CMA
		else if (case_code.startsWith("WZ-") && case_code_old.startsWith("WZ-")) {
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_3,
					"case_code_date_cxsh_CMA");
			now_time=now_time.substring(0,4);
			// 判断是当前年份
			if (xml_time.equals(now_time)) {
				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
						XML_PATH_3, "case_code_num_cxsh_CMA"));
				// 判断修改的案例编号是否为当前月份最大案例
				if (case_code.contains(now_time)) {
					if (new_num > (old_num + 1)) {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "该案例编号太大了!");
						return result;
					}
				}
				if (new_num == (old_num + 1)) {
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_cxsh_CMA", String.valueOf(new_num));
				}// 判断是否拿最新案例条码修改
				else if (old_num_temp == old_num) {
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_cxsh_CMA",
							String.valueOf(old_num_temp - 1));
				}
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("msg", "本月审核过再改吧!");
				return result;
			}
		}//广东永健省内-省内
		else if (case_code.startsWith("WA") && case_code_old.startsWith("WA")) {
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_10,
					"case_code_date");
			now_time=now_time.substring(0, 4);
			// 判断是当前月份
			if (xml_time.equals(now_time)) {
				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
						XML_PATH_10, "case_code_num"));
				// 判断修改的案例编号是否为当前月份最大案例
				if (case_code.contains(now_time)) {
					if (new_num > (old_num + 1)) {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "该案例编号太大了!");
						return result;
					}
				}
				if (new_num == (old_num + 1)) {
					XmlParseUtil.updateXmlValue(XML_PATH_10,
							"case_code_num", String.valueOf(new_num));
				}// 判断是否拿最新案例条码修改
				else if (old_num_temp == old_num) {
					XmlParseUtil.updateXmlValue(XML_PATH_10,
							"case_code_num",
							String.valueOf(old_num_temp - 1));
				}
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("msg", "本月审核过再改吧!");
				return result;
			}
		}
		else if (case_code.startsWith("WA") && case_code_old.startsWith("YJ")) {
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_10,
					"case_code_date");
			now_time=now_time.substring(0, 4);
			// 判断是当前月份
			if (xml_time.equals(now_time)) {
				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
						XML_PATH_10, "case_code_num"));
				// 判断修改的案例编号是否为当前月份最大案例
				if (case_code.contains(now_time)) {
					if (new_num > (old_num + 1)) {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "该案例编号太大了!");
						return result;
					}
				}
				if (new_num == (old_num + 1)) {
					XmlParseUtil.updateXmlValue(XML_PATH_10,
							"case_code_num", String.valueOf(new_num));
				}// 判断是否拿最新案例条码修改
				else if (old_num_temp == old_num) {
					XmlParseUtil.updateXmlValue(XML_PATH_10,
							"case_code_num",
							String.valueOf(old_num_temp - 1));
				}
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("msg", "本月审核过再改吧!");
				return result;
			}
		}
		else if (case_code.startsWith("YJ") && case_code_old.startsWith("YJ")) {}
		else if (case_code.startsWith("YJ") && case_code_old.startsWith("WA")) {
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_10,
					"case_code_date_yd");
			// 判断是当前月份
			if (xml_time.equals(now_time)) {
				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
						XML_PATH_10, "case_code_num_yd"));
				// 判断修改的案例编号是否为当前月份最大案例
				if (case_code.contains(now_time)) {
					if (new_num > (old_num + 1)) {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "该案例编号太大了!");
						return result;
					}
				}
				if (new_num == (old_num + 1)) {
					XmlParseUtil.updateXmlValue(XML_PATH_10,
							"case_code_num_yd", String.valueOf(new_num));
				}// 判断是否拿最新案例条码修改
				else if (old_num_temp == old_num) {
					XmlParseUtil.updateXmlValue(XML_PATH_10,
							"case_code_num_yd",
							String.valueOf(old_num_temp - 1));
				}
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("msg", "本月审核过再改吧!");
				return result;
			}
		}
		// 判断是否鸣正案例
		else if (case_code.startsWith("M2") && case_code_old.startsWith("M2")) {
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
					"case_code_date_mz");
			// 判断是当前月份
			if (xml_time.equals(now_time)) {
				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
						XML_PATH_2, "case_code_num_mz"));
				// 判断修改的案例编号是否为当前月份最大案例
				if (case_code.contains(now_time)) {
					if (new_num > (old_num + 1)) {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "该案例编号太大了!");
						return result;
					}
				}
				if (new_num == (old_num + 1)) {
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_mz",
							String.valueOf(new_num));
				}// 判断是否拿最新案例条码修改
				else if (old_num_temp == old_num) {
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_mz",
							String.valueOf(old_num_temp - 1));
				}
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("msg", "本月审核过再改吧!");
				return result;
			}
		}
		// 判断是否十堰案例
		else if (case_code.startsWith("Y2") && case_code_old.startsWith("Y2")) {
			temp = case_code.substring(case_code.length() - 3,
					case_code.length());
			temp_old = case_code_old.substring(case_code_old.length() - 3,
					case_code_old.length());
			// 新案例编号自增
			new_num = Integer.parseInt(temp);
			old_num_temp = Integer.parseInt(temp_old);
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_2,
					"case_code_date_sy");
			// 判断是当前月份
			if (xml_time.equals(now_time)) {
				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
						XML_PATH_2, "case_code_num_sy"));
				// 判断修改的案例编号是否为当前月份最大案例
				if (case_code.contains(now_time)) {
					if (new_num > (old_num + 1)) {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "该案例编号太大了!");
						return result;
					}
				}
				if (new_num == (old_num + 1)) {
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num_sy",
							String.valueOf(new_num));
				}// 判断是否拿最新案例条码修改
				else if (old_num_temp == old_num) {
					XmlParseUtil.updateXmlValue(XML_PATH_2, "case_code_num",
							String.valueOf(old_num_temp - 1));
				}
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("msg", "本月审核过再改吧!");
				return result;
			}
		}
		// 判断是否鑫正案例
		else if (case_code.startsWith("XZ2") && case_code_old.startsWith("XZ2")) {
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_3,
					"case_code_date_scxz");
			// 判断是当前月份
			if (xml_time.equals(now_time)) {
				int old_num = Integer.parseInt(XmlParseUtil.getXmlValue(
						XML_PATH_3, "case_code_num_scxz"));
				// 判断修改的案例编号是否为当前月份最大案例
				if (case_code.contains(now_time)) {
					if (new_num > (old_num + 1)) {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "该案例编号太大了!");
						return result;
					}
				}
				if (new_num == (old_num + 1)) {
					XmlParseUtil.updateXmlValue(XML_PATH_3,
							"case_code_num_scxz", String.valueOf(new_num));
				}// 判断是否拿最新案例条码修改
				else if (old_num_temp == old_num) {
					XmlParseUtil.updateXmlValue(XML_PATH_3, "case_code_num",
							String.valueOf(old_num_temp - 1));
				}
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("msg", "本月审核过再改吧!");
				return result;
			}
		}
		// 判断案例编号类型不一致时提示
		else if (!case_code.substring(0, 3).equals(
				case_code_old.substring(0, 3))) {
			result.put("success", true);
			result.put("result", false);
			result.put("msg", "不同类型案例编号不支持修改!");
			return result;
		}
		result.put("success", true);
		result.put("result", true);
		return result;
	}

	@Override
	public int updateCaseRemark(Map<String, Object> params) {
		return rdsJudicialVerifyMapper.updateCaseRemark(params);
	}

	@Override
	public int updateCaseConsignment(Map<String, Object> params) {
		return rdsJudicialVerifyMapper.updateCaseConsignment(params);
	}

	@Override
	public int updateCaseVerifyToText(Map<String, Object> params) {
		RdsUpcUserModel user=(RdsUpcUserModel) params.get("user");
			try {
				String[] case_codes = params.get("case_codes").toString().split(",");
				for (String string : case_codes) {

					Map<String, Object> map = new HashMap<String, Object>();
					Map<String, Object> variables = new HashMap<>();
					rdsActivitiJudicialService.runByCaseCode(string, variables,
							user);
					rdsActivitiJudicialService.runByCaseCode(string, variables,
							user);
					variables.put("issamplepass", 1);
					rdsActivitiJudicialService.runByCaseCode(string,
							variables, user);
					map.put("case_code", string);
					map.put("verify_state", 5);
					rdsJudicialVerifyMapper.updateCaseVerifyTotest(map);
				}
				return 1;
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

