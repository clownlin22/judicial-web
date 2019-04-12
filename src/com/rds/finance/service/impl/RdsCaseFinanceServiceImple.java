package com.rds.finance.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rds.code.image.ImgUtil;
import com.rds.code.utils.DownLoadUtils;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.FileUtils;
import com.rds.code.utils.JScriptInvoke;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.file.XmlParseUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.finance.mapper.RdsCaseFinanceMapper;
import com.rds.finance.mapper.RdsFinanceChargeStandardMapper;
import com.rds.finance.model.RdsCaseFinanceAttachmentModel;
import com.rds.finance.model.RdsCaseFinanceModel;
import com.rds.finance.model.RdsContractPlanInfoModel;
import com.rds.finance.model.RdsFinanceChargeStandardModel;
import com.rds.finance.model.RdsFinanceContractAttachmentModel;
import com.rds.finance.model.RdsFinancePromptInfo;
import com.rds.finance.model.RdsFinanceSpecialModel;
import com.rds.finance.model.RdsRemittanceInfoModel;
import com.rds.finance.model.RdsRemittanceLogInfoModel;
import com.rds.finance.model.RdsRemittancePlanInfoModel;
import com.rds.finance.service.RdsCaseFinanceService;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialPhoneRegisterService;
import com.rds.upc.mapper.RdsUpcUserMapper;
import com.rds.upc.model.RdsUpcUserModel;

@Service
public class RdsCaseFinanceServiceImple implements RdsCaseFinanceService {

	private static final String XML_PATH = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config.xml";

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";
	private static final String HOSPITAL = PropertiesUtils.readValue(FILE_PATH,
			"Hospital");
	private static final String Clerks = PropertiesUtils.readValue(FILE_PATH,
			"Clerks");

	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "contract_path");

	public static String INVERSIVE_TYPE = "inversive";

	@Setter
	@Autowired
	private RdsCaseFinanceMapper caseFeeMapper;

	@Setter
	@Autowired
	private RdsJudicialPhoneRegisterService rdsJudicialPhoneRegisterService;

	@Setter
	@Autowired
	private RdsFinanceChargeStandardMapper rdsFinanceChargeStandardMapper;

	@Setter
	@Autowired
	private RdsUpcUserMapper rdsUpcUserMapper;

	@Override
	public RdsJudicialResponse getCaseFeeInfo(Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsCaseFinanceModel> caseFeeModels = caseFeeMapper
				.getCaseFeeInfo(params);
		int count = caseFeeMapper.countCaseFeeInfo(params);
		response.setCount(count);
		response.setItems(caseFeeModels);
		return response;
	}

	@Override
	public boolean updateCaseFee(Map<String, Object> params) {
		Map<String, Object> map = new HashMap<>();
		map.put("case_id", params.get("case_id"));
		map.put("case_type", "dna");
		try {
			map = caseFeeMapper.queryCaseFeeById(map);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (null != map.get("remittance_id")
				&& !"".equals(map.get("remittance_id"))) {
			return true;
		}

		// 认为修改标识
		if ("1".equals(params.get("updateByPerson")))
			return caseFeeMapper.updateCaseFee(params);
		else {
			Map<String, Object> result = this.getStandFee(params);
			if (Boolean.valueOf(result.get("success").toString())) {
				params.put("stand_sum", result.get("stand_sum").toString());
				// 案例标识
				// params.put("type", params.get("case_state"));
				// 免单标识
				if ("2".equals(params.get("case_state"))) {
					params.put("real_sum", 0.0);
				} else
					params.put("real_sum", result.get("real_sum").toString());
				// 补样免费
				if ("5".equals(params.get("case_state").toString())) {
					params.put("stand_sum", 0);
					params.put("real_sum", 0);
				}
				// params.put("discount", result.get("discount").toString());
				params.put("return_sum", 0.0);
				if ("1".equals(params.get("urgent_state").toString()))
					params.put("finance_remark", "48小时加急");
				// 24小时加急
				if ("2".equals(params.get("urgent_state").toString()))
					params.put("finance_remark", "24小时加急");
				// 8小时加急
				if ("3".equals(params.get("urgent_state").toString()))
					params.put("finance_remark", "8小时加急");
				// 保存财务信息
				return caseFeeMapper.updateCaseFee(params);
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean confirmCaseFee(Map<String, Object> params) {
		return caseFeeMapper.confirmCaseFee(params);
	}

	/**
	 * 根据公式计算后，添加财务信息
	 */
	@Override
	public boolean addCaseFee(Map<String, Object> params) {
		Map<String, Object> result = this.getStandFee(params);
		if (Boolean.valueOf(result.get("success").toString())) {
			params.put("stand_sum", result.get("stand_sum").toString());
			// 案例标识
			// params.put("type", params.get("case_state"));
			// 免单标识和二次采样
			if ("2".equals(params.get("case_state").toString())) {
				params.put("real_sum", 0);
			} else
				params.put("real_sum", result.get("real_sum").toString());
			// 补样免费
			if ("5".equals(params.get("case_state").toString())) {
				params.put("stand_sum", 0);
				params.put("real_sum", 0);
			}

			// params.put("discount", result.get("discount").toString());
			params.put("id", UUIDUtil.getUUID());
			params.put("return_sum", 0);
			if ("1".equals(params.get("urgent_state").toString())) {
				params.put("finance_remark", "48小时加急");
			}
			if ("2".equals(params.get("urgent_state").toString())) {
				params.put("finance_remark", "24小时加急");
			}
			if ("3".equals(params.get("urgent_state").toString())) {
				params.put("finance_remark", "8小时加急");
			}
			// 保存财务信息
			return caseFeeMapper.addCaseFee(params);
		} else {
			return false;
		}

	}

	/**
	 * 获取收费标准
	 * 
	 * @param typeid
	 * @param pernum
	 * @param receiver_id
	 * @param case_type
	 * @return
	 */
	public Map<String, Object> getStandFee(Map<String, Object> params) {
		String receiver_area = params.get("receiver_area").toString();
		Map<String, Object> map = new HashMap<String, Object>();
		int pernum = Integer.parseInt(params.get("per_num").toString());
		int typeid = Integer.parseInt(params.get("typeid").toString());
		// 紧急程度
		String urgent_state = params.get("urgent_state").toString();
		map.put("case_areacode", params.get("case_areacode"));
		map.put("case_userid", params.get("case_userid"));
		map.put("type", params.get("case_type"));
		map.put("source_type", params.get("source_type"));
		// map.put("program_type", params.get("program_type"));
		map.put("program_type", params.get("sample_relation"));

		try {
			// 补样免费
			if ("5".equals(params.get("case_state").toString())) {
				map.put("stand_sum", 0.0);
				map.put("real_sum", 0.0);
				map.put("success", true);
				return map;
			}
			List<RdsFinanceChargeStandardModel> equation = rdsFinanceChargeStandardMapper
					.getEquation(map);
			List<String> specials = getValues(params.get("special"));
			if (params.get("parnter_name").toString().contains("广东正孚")
					&& "1".equals(params.get("source_type"))) {
				map.put("stand_sum", 1200 * pernum);
				map.put("real_sum",
						1200 * pernum - (int) params.get("discount_amount"));
				map.put("success", true);
				return map;
			}
			if (params.get("parnter_name").toString().contains("北京信诺")
					&& "1".equals(params.get("source_type"))) {
				map.put("stand_sum", 1200 * pernum);
				map.put("real_sum",
						1200 * pernum - (int) params.get("discount_amount"));
				map.put("success", true);
				return map;
			}
			if (params.get("parnter_name").toString().contains("广东永建")
					&& "1".equals(params.get("source_type"))) {
				map.put("stand_sum", 1200 * pernum);
				map.put("real_sum",
						1200 * pernum - (int) params.get("discount_amount"));
				map.put("success", true);
				return map;
			}
			// 判断不是亲子鉴定案例
			if (!"0".equals(params.get("sample_relation").toString())) {
				String username = equation.size() == 0 ? ""
						: (equation.get(0) == null ? "" : equation.get(0)
								.getUsername());
				if (params.get("parnter_name").toString().contains("三和")
						&& receiver_area.contains("吉林")) {
					map.put("stand_sum", 600 * pernum);
					map.put("real_sum",
							600 * pernum - (int) params.get("discount_amount"));
					map.put("success", true);
				} else if (params.get("parnter_name").toString().contains("三和")
						&& !receiver_area.contains("吉林")) {
					map.put("stand_sum", 800 * pernum);
					map.put("real_sum",
							800 * pernum - (int) params.get("discount_amount"));
					map.put("success", true);
				} else if (params.get("parnter_name").toString().contains("四川")) {
					map.put("stand_sum", 1000 * pernum);
					map.put("real_sum",
							1000 * pernum - (int) params.get("discount_amount"));
					map.put("success", true);
				} else if (!receiver_area.contains("德庆")
						&& params.get("parnter_name").toString()
								.contains("广东南天") && !"李凯信".equals(username)) {
					map.put("stand_sum", 1000 * pernum);
					map.put("real_sum",
							1000 * pernum - (int) params.get("discount_amount"));
					map.put("success", true);
				} else if (params.get("parnter_name").toString()
						.contains("广东永建")) {
					map.put("stand_sum", 800 * pernum);
					map.put("real_sum",
							800 * pernum - (int) params.get("discount_amount"));
					map.put("success", true);
					return map;
				} else {
					// 加样本单价
					int samplePrice = Integer.parseInt(equation.get(0)
							.getSamplePrice());
					int temp = 0;
					if (equation.size() > 0) {
						// 案例为补样案例
						if ("6".equals(params.get("case_state").toString())) {
							map.clear();
							map.put("stand_sum", pernum * samplePrice);
							map.put("real_sum", pernum * samplePrice);
							// map.put("discount",
							// equation.get(0).getDiscountrate());
							map.put("success", true);
						} else {

							String script = equation.get(0).getEquation()
									.replaceAll("\n", "");
							Double standFee;
							standFee = JScriptInvoke.getStandardFee(script,
									pernum, typeid);
							// 特殊样本加钱
							for (String string : specials) {
								// 一类特殊样本
								if ("1".equals(string))
									temp += equation.get(0).getSpecialPirce();
								// 二类特殊样本
								if ("2".equals(string))
									temp += equation.get(0).getSpecialPirce1();
								// 三类特殊样本
								if ("3".equals(string))
									temp += equation.get(0).getSpecialPirce2();
							}
							// 加上特殊样本价格
							standFee += temp;

							map.clear();
							map.put("stand_sum", standFee == null ? 0
									: standFee);
							// 减去优惠价格
							standFee -= (int) params.get("discount_amount");
							// 48小时加急
							if ("1".equals(urgent_state))
								standFee += equation.get(0).getUrgentPrice();
							// 24小时加急
							if ("2".equals(urgent_state))
								standFee += equation.get(0).getUrgentPrice1();
							// 8小时加急
							if ("3".equals(urgent_state))
								standFee += equation.get(0).getUrgentPrice2();
							map.put("real_sum", standFee == null ? 0 : standFee);
							// map.put("discount",
							// equation.get(0).getDiscountrate());
							map.put("success", true);
						}
					} else {
						map.put("stand_sum", "0");
						// map.put("discount", "1");
						map.put("success", false);
					}

				}
			} else {
				// 加样本单价
				int samplePrice = Integer.parseInt(equation.get(0)
						.getSamplePrice());
				int temp = 0;
				if (equation.size() > 0) {
					// 案例为补样案例
					if ("6".equals(params.get("case_state").toString())) {
						map.clear();
						map.put("stand_sum", pernum * samplePrice);
						map.put("real_sum", pernum * samplePrice);
						// map.put("discount",
						// equation.get(0).getDiscountrate());
						map.put("success", true);
					} else {

						String script = equation.get(0).getEquation()
								.replaceAll("\n", "");
						Double standFee;
						standFee = JScriptInvoke.getStandardFee(script, pernum,
								typeid);
						// 特殊样本加钱
						for (String string : specials) {
							// 一类特殊样本
							if ("1".equals(string))
								temp += equation.get(0).getSpecialPirce();
							// 二类特殊样本
							if ("2".equals(string))
								temp += equation.get(0).getSpecialPirce1();
							// 三类特殊样本
							if ("3".equals(string))
								temp += equation.get(0).getSpecialPirce2();
						}
						// 加上特殊样本价格
						standFee += temp;

						map.clear();
						map.put("stand_sum", standFee == null ? 0 : standFee);
						// 减去优惠价格
						standFee -= (int) params.get("discount_amount");
						// 48小时加急
						if ("1".equals(urgent_state))
							standFee += equation.get(0).getUrgentPrice();
						// 24小时加急
						if ("2".equals(urgent_state))
							standFee += equation.get(0).getUrgentPrice1();
						// 8小时加急
						if ("3".equals(urgent_state))
							standFee += equation.get(0).getUrgentPrice2();
						map.put("real_sum", standFee == null ? 0 : standFee);
						// map.put("discount",
						// equation.get(0).getDiscountrate());
						map.put("success", true);
					}
				} else {
					map.put("stand_sum", "0");
					// map.put("discount", "1");
					map.put("success", false);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			map.put("stand_sum", "0");
			// map.put("discount", "1");
			map.put("success", false);
		}
		return map;
	}

	@Override
	public List<RdsFinancePromptInfo> queryCasefeePrompt() {
		return caseFeeMapper.queryCasefeePrompt();
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

	@Override
	public boolean insertRemittanceRecord(Map<String, Object> params)
			throws Exception {
		String[] fee_ids = params.get("fee_id").toString().split(",");
		params.put("fee_ids", fee_ids);
		boolean result = false;
		if ("2".equals(params.get("daily_type").toString())) {
			result = caseFeeMapper.updateContractRemittance(params);
		} else {
			result = caseFeeMapper.updateCaseFeeRemittance(params);
		}

		if (result) {
			MultipartFile[] files = (MultipartFile[]) params.get("files");
			String remittance_num = "";
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH,
					"case_finance_date");
			String now_time = com.rds.code.utils.StringUtils
					.dateToEight(new Date());
			DecimalFormat df = new DecimalFormat("0000");
			if (xml_time.equals(now_time)) {
				remittance_num = "REM"
						+ now_time
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH, "case_finance_num")) + 1);
				XmlParseUtil
						.updateXmlValue(XML_PATH, "case_finance_num", String
								.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH,
												"case_finance_num")) + 1));
			} else {
				XmlParseUtil.updateXmlValue(XML_PATH, "case_finance_date",
						now_time);
				XmlParseUtil.updateXmlValue(XML_PATH, "case_finance_num", "1");
				remittance_num = "REM"
						+ now_time
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH, "case_finance_num")));
			}
			params.put("remittance_num", remittance_num);

			Map<String, Object> map = new HashMap<>();
			map.put("remittance_id", params.get("remittance_id"));
			map.put("confirm_per", params.get("create_per"));
			map.put("confirm_state", -1);
			String attachmentPath = ATTACHMENTPATH + remittance_num
					+ File.separatorChar;
			// 日志插入
			if (caseFeeMapper.insertRemittanceLogs(map)) {
				for (MultipartFile multipartFile : files) {
					Map<String, Object> mapAttachment = new HashMap<>();
					mapAttachment.put("attachment_path", remittance_num + File.separatorChar
					+ multipartFile.getOriginalFilename());
					mapAttachment.put("id", UUIDUtil.getUUID());
					mapAttachment.put("remittance_id", params.get("remittance_id"));
					mapAttachment.put("create_per", params.get("create_per"));
					// 上传附件
					RdsFileUtil.fileUpload(attachmentPath + File.separatorChar
							+ multipartFile.getOriginalFilename(), multipartFile.getInputStream());
					//插入附件表
					caseFeeMapper.insertFinanceAttachment(mapAttachment);
				}
				// 信息插入
				return caseFeeMapper.insertRemittanceRecord(params);
			} else
				return false;
		} else
			return false;
	}

	@Override
	public boolean updateRemittanceRecord(Map<String, Object> params)
			throws Exception {
		boolean result = false;
		String[] remittance_ids = params.get("remittance_id").toString()
				.split(",");
		params.put("remittance_ids", remittance_ids);
		// 判断是否亲子鉴定汇款单
		if ("2".equals(params.get("daily_type"))) {
			// params.put("status", 0);
			result = caseFeeMapper.updateContractSatus(params);
		} else {
			result = caseFeeMapper.updateCaseFeeStatus(params);
		}
		for (String string : remittance_ids) {
			Map<String, Object> maps = new HashMap<>();
			maps.put("remittance_id", string);
			maps.put("confirm_state", params.get("confirm_state"));
			maps.put("confirm_per", params.get("confirm_per"));
			maps.put("confirm_remark", params.get("confirm_remark"));
			caseFeeMapper.insertRemittanceLogs(maps);
		}

		// 更新每个案例财务确认状态
		if (result) {
			// 更新确认状态
			return caseFeeMapper.updateRemittanceRecord(params);
		} else
			return false;
	}

	@Override
	public RdsJudicialResponse queryPageRemittanceInfo(
			Map<String, Object> params) throws Exception {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsRemittanceInfoModel> rdsRemittanceInfoModels = caseFeeMapper
				.queryPageRemittanceInfo(params);
		int count = caseFeeMapper.countRemittanceInfo(params);
		response.setCount(count);
		response.setItems(rdsRemittanceInfoModels);
		return response;
	}

	@Override
	public int countRemittanceInfo(Map<String, Object> params) throws Exception {
		return caseFeeMapper.countRemittanceInfo(params);
	}

	@Override
	public boolean insertContractPlan(Map<String, Object> map) throws Exception {
		boolean result = true;
		// 插入回款计划
		if (null != map.get("remittance_date")) {
			List<String> remittance_dates = getValues(map
					.get("remittance_date"));
			List<String> remittances = getValues(map.get("remittance"));
			for (int i = 0; i < remittance_dates.size(); i++) {
				Map<String, Object> params = new HashMap<>();
				params.put("contract_remittance_planid", UUIDUtil.getUUID());
				params.put("contract_id", map.get("contract_id"));
				params.put("remittance", remittances.get(i));
				params.put("remittance_date", remittance_dates.get(i));
				result = caseFeeMapper.insertRemittancePlan(params);
			}
		}
		// 插入合同信息
		if (result) {
			// 生成合同编号
			String contract_num = "";
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH,
					"case_finance_date");
			String now_time = com.rds.code.utils.StringUtils
					.dateToEight(new Date());
			DecimalFormat df = new DecimalFormat("0000");
			if (xml_time.equals(now_time)) {
				contract_num = "CON"
						+ now_time
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH, "case_finance_num")) + 1);
				XmlParseUtil
						.updateXmlValue(XML_PATH, "case_finance_num", String
								.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH,
												"case_finance_num")) + 1));
			} else {
				XmlParseUtil.updateXmlValue(XML_PATH, "case_finance_date",
						now_time);
				XmlParseUtil.updateXmlValue(XML_PATH, "case_finance_num", "1");
				contract_num = "CON"
						+ now_time
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH, "case_finance_num")));
			}
			map.put("contract_num", contract_num);
			return caseFeeMapper.insertContractPlan(map);
		} else
			return false;
	}

	@Override
	public boolean updateContractPlan(Map<String, Object> map) throws Exception {
		boolean result = true;
		// caseFeeMapper.deleteRemittancePlan(map);
		// 插入回款计划
		// if (null != map.get("remittance_date")) {
		// List<String> remittance_dates = getValues(map
		// .get("remittance_date"));
		// List<String> remittances = getValues(map.get("remittance"));
		// List<String> status = getValues(map.get("status"));
		// for (int i = 0; i < remittance_dates.size(); i++) {
		// Map<String, Object> params = new HashMap<>();
		// params.put("contract_remittance_planid", UUIDUtil.getUUID());
		// params.put("contract_id", map.get("contract_id"));
		// params.put("remittance", remittances.get(i));
		// params.put("remittance_date", remittance_dates.get(i));
		// params.put("status", status.get(i));
		// result = caseFeeMapper.insertRemittancePlan(params);
		// }
		// }

		if (result) {
			return caseFeeMapper.updateContractPlan(map);
		} else {
			return false;
		}
	}

	@Override
	public RdsJudicialResponse queryPageContractPlan(Map<String, Object> map)
			throws Exception {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsContractPlanInfoModel> lists = caseFeeMapper
				.queryPageContractPlan(map);
		int count = caseFeeMapper.queryCountContractPlan(map);
		response.setCount(count);
		response.setItems(lists);
		return response;
	}

	@Override
	public int queryCountContractPlan(Map<String, Object> map) throws Exception {
		return caseFeeMapper.queryCountContractPlan(map);
	}

	@Override
	public List<RdsRemittancePlanInfoModel> queryAllContractPlan(
			Map<String, Object> map) throws Exception {
		return caseFeeMapper.queryAllContractPlan(map);
	}

	@Override
	public boolean updateRemittance(Map<String, Object> map) throws Exception {
		// Map<String, Object> maps = new HashMap<>();
		// maps.put("remittance_id", string);
		// maps.put("confirm_state", params.get("confirm_state"));
		// maps.put("confirm_per", params.get("confirm_per"));
		// maps.put("confirm_remark", params.get("confirm_remark"));
		if (caseFeeMapper.insertRemittanceLogs(map))
			return caseFeeMapper.updateRemittance(map);
		else
			return false;
	}

	@Override
	public List<RdsRemittanceLogInfoModel> queryRemittanceLogs(
			Map<String, Object> map) throws Exception {
		return caseFeeMapper.queryRemittanceLogs(map);
	}

	@Override
	public boolean deleteCaseFeeRemittance(Map<String, Object> map)
			throws Exception {
		caseFeeMapper.deleteCaseFeeRemittance(map);
		return caseFeeMapper.deleteRemittanceInfo(map);
	}

	@Override
	public int queryConfirmCase(Map<String, Object> map) throws Exception {
		return caseFeeMapper.queryConfirmCase(map);
	}

	@Override
	public Map<String, Object> confirmCodeAdd(Map<String, Object> map)
			throws Exception {
		int discount_amount = 0;
		// 案例返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		// 判断该案例财务是否已确认
		if (caseFeeMapper.queryConfirmCase(map) > 0) {
			result.put("success", true);
			result.put("result", false);
			result.put("message", "财务已确认，请查看！");
			return result;
		}

		// 查询申请的优惠码信息
		Map<String, Object> mapCode = new HashMap<String, Object>();
		mapCode.put("confirm_code", map.get("confirm_code"));
		List<RdsFinanceSpecialModel> model = rdsFinanceChargeStandardMapper
				.queryAllSpecialFinance(mapCode);
		if (model.size() == 0) {
			result.put("success", true);
			result.put("result", false);
			result.put("message", "优惠码有误，请查看！");
			return result;
		}
		// 判断该激活码是否被使用
		if ("2".equals(model.get(0).getUser_state())) {
			result.put("result", false);
			result.put("success", true);
			result.put("message", "优惠码已被使用，请查看！");
			return result;
		}
		// 判断是否当月免单案例
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String str = sdf.format(date);
		String accept = map.get("accept_time") == null ? "" : map.get(
				"accept_time").toString();
		// 查询出该案例现在的价格
		Map<String, Object> mapTemp = caseFeeMapper.queryCaseFeeById(map);
		// 判断该编码码是否为优惠码
		if ("3".equals(model.get(0).getCase_state())) {
			// 否则查询出优惠金额
			discount_amount = Integer.parseInt(model.get(0)
					.getDiscount_amount());
			// 如果不是当月，生成新的财务信息
			if (!accept.contains(str)) {
				// 更新优惠后价格
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", UUIDUtil.getUUID());
				params.put("real_sum", "-" + discount_amount);
				params.put("stand_sum", "-" + discount_amount);
				params.put("case_id", map.get("case_id"));
				params.put("update_user", map.get("update_user"));
				params.put("confirm_code", map.get("confirm_code"));
				params.put("case_type", map.get("case_type"));
				params.put("finance_type", map.get("finance_type"));
				// 判断财务状态是否月结或显出报告，如果是保留状态
				if ("1".equals(mapTemp.get("type"))
						|| "4".equals(mapTemp.get("type"))) {
					params.put("type", mapTemp.get("type"));
				} else {
					params.put("type", model.get(0).getCase_state());
				}
				if (caseFeeMapper.addCaseFeeOther(params)) {
					// 更新案例信息
					// if (caseFeeMapper.updateConfirmCodeByCaseId(map)) {
					// 更新优惠码状态
					map.put("user_state", 2);
					if (rdsFinanceChargeStandardMapper.updateCodeUsed(map) > 0) {
						result.put("result", true);
						result.put("success", true);
						result.put("message", "优惠码更新成功！");
					}
					// } else {
					// result.put("result", false);
					// result.put("success", true);
					// result.put("message", "优惠码案例更新失败，请查看！");
					// }

				} else {
					result.put("result", false);
					result.put("success", true);
					result.put("message", "优惠码财务更新失败，请查看！");

				}
			} else {
				Double real_sum = Double.parseDouble(mapTemp.get("real_sum")
						.toString());
				real_sum = real_sum - discount_amount;
				// 更新优惠后价格
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("real_sum", real_sum);
				params.put("case_id", map.get("case_id"));
				params.put("update_user", map.get("update_user"));
				params.put("case_type", map.get("case_type"));
				params.put("confirm_code", map.get("confirm_code"));
				params.put("discount_amount", discount_amount);
				// 判断财务状态是否月结或显出报告，如果是保留状态
				if ("1".equals(mapTemp.get("type"))
						|| "4".equals(mapTemp.get("type"))) {
					params.put("type", mapTemp.get("type"));
				} else {
					params.put("type", model.get(0).getCase_state());
				}
				// 更新财务信息
				if (caseFeeMapper.updateCaseFeeUnite(params)) {
					// 更新案例信息
					// if (caseFeeMapper.updateConfirmCodeByCaseId(map)) {
					// 更新优惠码状态
					map.put("user_state", 2);
					if (rdsFinanceChargeStandardMapper.updateCodeUsed(map) > 0) {
						result.put("result", true);
						result.put("success", true);
						result.put("message", "优惠码更新成功！");
					}
					// } else {
					// result.put("result", false);
					// result.put("success", true);
					// result.put("message", "优惠码案例更新失败，请查看！");
					// }
				} else {
					result.put("result", false);
					result.put("success", true);
					result.put("message", "优惠码财务更新失败，请查看！");
				}

			}
			// 免单优惠码
		} else if ("2".equals(model.get(0).getCase_state())) {
			// 更新优惠后价格
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("real_sum", 0);
			params.put("stand_sum", 0);
			params.put("case_id", map.get("case_id"));
			params.put("update_user", map.get("update_user"));
			params.put("confirm_code", map.get("confirm_code"));
			params.put("case_type", map.get("case_type"));
			params.put("finance_type", map.get("finance_type"));
			params.put("type", model.get(0).getCase_state());
			// 更新财务信息
			if (caseFeeMapper.updateCaseFeeUnite(params)) {
				// 更新案例信息
				// if (caseFeeMapper.updateConfirmCodeByCaseId(map)) {
				// 更新优惠码状态
				map.put("user_state", 2);
				if (rdsFinanceChargeStandardMapper.updateCodeUsed(map) > 0) {
					result.put("result", true);
					result.put("success", true);
					result.put("message", "激活码更新成功！");
				}
				// } else {
				// result.put("result", false);
				// result.put("success", true);
				// result.put("message", "激活码案例更新失败，请查看！");
				// }
			} else {
				result.put("result", false);
				result.put("success", true);
				result.put("message", "激活码财务更新失败，请查看！");
			}
		} else if ("1".equals(model.get(0).getCase_state())) {
			// 更新财务状态，先出报告后付款
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("case_id", map.get("case_id"));
			params.put("update_user", map.get("update_user"));
			params.put("confirm_code", map.get("confirm_code"));
			params.put("case_type", map.get("case_type"));
			params.put("finance_type", map.get("finance_type"));
			params.put("type", model.get(0).getCase_state());
			// 更新财务信息
			if (caseFeeMapper.updateCaseFeeUnite(params)) {
				result.put("result", true);
				result.put("success", true);
				result.put("message", "激活码更新成功！");
			} else {
				result.put("result", false);
				result.put("success", true);
				result.put("message", "激活码财务更新失败，请查看！");
			}
		} else {
			result.put("result", false);
			result.put("success", true);
			result.put("message", "该编码不是激活码！");
		}
		return result;
	}

	@Override
	public Map<String, Object> queryCaseConfirm(Map<String, Object> map)
			throws Exception {
		// 案例返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		List<RdsFinanceSpecialModel> model = rdsFinanceChargeStandardMapper
				.queryAllSpecialFinance(map);
		if (model.size() == 0) {
			result.put("success", true);
			result.put("result", false);
			result.put("message", "退款码有误，请查看！");
			return result;
		}
		if (!"5".equals(model.get(0).getCase_state())) {
			result.put("success", true);
			result.put("result", false);
			result.put("message", "该优惠码非退款码，请查看！");
			return result;
		}
		// 判断该激活码是否被使用
		if ("2".equals(model.get(0).getUser_state())) {
			result.put("result", false);
			result.put("success", true);
			result.put("message", "退款码已被使用，请查看！");
			return result;
		}
		result.put("result", true);
		result.put("success", true);
		result.put("discount_amount", model.get(0).getDiscount_amount());
		return result;
	}

	@Override
	public Map<String, Object> addCaseFeeOther(Map<String, Object> params)
			throws Exception {
		// 案例返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		if (caseFeeMapper.addCaseFeeOther(params)) {
			// 更新优惠码状态
			params.put("user_state", 2);
			if (rdsFinanceChargeStandardMapper.updateCodeUsed(params) > 0) {
				result.put("result", true);
				result.put("success", true);
				result.put("message", "操作成功！");
			} else {
				result.put("result", false);
				result.put("success", true);
				result.put("message", "退款码更新失败，请联系管理员！");
			}
		} else {
			result.put("result", false);
			result.put("success", true);
			result.put("message", "退款信息更新失败，请联系管理员！");
		}
		return result;
	}

	@Override
	public Map<String, Object> addCaseFeeUnite(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		// 归属人信息查询
		map.put("userid", params.get("case_userid"));
		RdsUpcUserModel case_user = new RdsUpcUserModel();
		try {
			case_user = (RdsUpcUserModel) rdsUpcUserMapper.queryModel(map);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		map.put("id", UUIDUtil.getUUID());
		map.put("case_id", params.get("case_id"));
		map.put("update_user", params.get("userid"));
		// 项目类型
		String case_type = params.get("case_type") == null ? "" : params.get(
				"case_type").toString();
		map.put("case_type", case_type);
		// 项目类型
		String program_type = params.get("program_type") == null ? "" : params
				.get("program_type").toString();
		map.put("program_type", program_type);
		// 财务备注
		map.put("finance_remark", params.get("finance_remark"));
		// 申请码：优惠，免单，先出报告后付款
		String confirm_code = params.get("confirm_code") == null ? "" : params
				.get("confirm_code").toString();
		double discountPrice = 0.0;
		// 激活码操作
		if (!"".equals(confirm_code)) {
			map.put("confirm_code", confirm_code);
			Map<String, Object> mapCode = new HashMap<String, Object>();
			mapCode.put("confirm_code", confirm_code);
			try {
				List<RdsFinanceSpecialModel> model = rdsFinanceChargeStandardMapper
						.queryAllSpecialFinance(mapCode);
				// 优惠码错误
				if (model.size() == 0) {
					result.put("success", true);
					result.put("result", false);
					result.put("message", "不存在该优惠码！");
					return result;
				} else {
					if (!"1".equals(model.get(0).getUser_state())) {
						result.put("success", true);
						result.put("result", false);
						result.put("message", "请查看该优惠码状态！");
						return result;
					}
					// 案例优惠标准
					switch (model.get(0).getCase_state()) {
					// 显出报告
					case "1":
						map.put("type", "1");
						// 设置使用状态
						result.put("user_state", 2);
						result.put("confirm_code", params.get("confirm_code"));
						result.put("case_id", params.get("case_id"));
						rdsFinanceChargeStandardMapper.updateCodeUsed(result);
						break;
					// 免单
					case "2":
						map.put("type", "2");
						map.put("stand_sum", 0.0);
						map.put("real_sum", 0.0);
						map.put("return_sum", 0.0);
						if (caseFeeMapper.addCaseFeeOther(map)) {

							// 设置使用状态
							result.put("user_state", 2);
							result.put("confirm_code",
									params.get("confirm_code"));
							result.put("case_id", params.get("case_id"));
							rdsFinanceChargeStandardMapper
									.updateCodeUsed(result);
							result.put("success", true);
							result.put("result", true);
							result.put("message", "操作成功！");
							return result;
						} else {
							result.put("success", false);
							result.put("result", false);
							result.put("message", "操作失败，请联系管理员！");
							return result;
						}
						// 优惠
					case "3":
						discountPrice = Double.parseDouble(model.get(0)
								.getDiscount_amount());
						map.put("discountPrice", discountPrice);
						map.put("discount_amount", discountPrice);
						map.put("type", "3");
						// 设置使用状态
						result.put("user_state", 2);
						result.put("confirm_code", params.get("confirm_code"));
						result.put("case_id", params.get("case_id"));
						rdsFinanceChargeStandardMapper.updateCodeUsed(result);
						break;
					// 月结
					case "4":
						map.put("type", "4");
						break;
					default:
						break;
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("success", false);
				result.put("result", false);
				result.put("message", "出错啦，请联系管理员！");
				return result;
			}
		} else {
			map.put("type", "0");
		}
		// 无创产前项目
		if (INVERSIVE_TYPE.equals(case_type)) {
			// 财务项目名
			switch (program_type) {
			case "1":
				map.put("finance_type", "NIPT(博奥)");
				break;
			case "5":
				map.put("finance_type", "NIPT(成都博奥)");
				break;
			case "2":
				map.put("finance_type", "NIPT(贝瑞)");
				break;
			case "3":
				map.put("finance_type", "NIPT-plus(博奥)");
				break;
			case "4":
				map.put("finance_type", "NIPT-plus(贝瑞");
				break;
			case "6":
				map.put("finance_type", "NIPT(精科)");
				break;
			case "7":
				map.put("finance_type", "NIPT-plus(精科");
				break;
			default:
				break;
			}
			String areaname = params.get("areaname") == null ? "" : params.get(
					"areaname").toString();
			map.put("areaname", areaname);
			map.put("hospital", params.get("hospital"));
			// 根据医院和归属地配置价格
			try {
				// 获取无创收费标准
				List<RdsFinanceChargeStandardModel> equation = rdsFinanceChargeStandardMapper
						.getInversivePerFinance(map);
				// 没有配置收费标准默认标准
				if (equation.size() == 0) {
					double mon = 0;
					switch (program_type) {
					case "1":
					case "2":
					case "5":
						map.put("stand_sum", 900.0);
						mon = 900.0 - discountPrice;
						map.put("real_sum", mon);
						map.put("return_sum", 0.0);
						break;
					case "3":
					case "4":
						map.put("stand_sum", 1500.0);
						mon = 1500.0 - discountPrice;
						map.put("real_sum", mon);
						map.put("return_sum", 0.0);
						break;
					default:
						break;
					}
				} else {
					double mon = Double.parseDouble(equation.get(0)
							.getSamplePrice());
					map.put("stand_sum", mon);
					map.put("real_sum", mon - discountPrice);
					map.put("return_sum", 0.0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("success", false);
				result.put("result", false);
				result.put("message", "出错啦，请联系管理员！");
				return result;
			}
		}
		// 招商中心无创价格
		if ("1004028".equals(case_user.getDeptcode())) {
			map.put("stand_sum", 700.0);
			map.put("real_sum", 700.0 - discountPrice);
			map.put("return_sum", 0.0);
		}
		// 转化医学事业部门NIPT价格配置
		else if (case_user.getDeptcode().startsWith("1009001")) {

			double mon = 0;
			switch (program_type) {
			case "1":
			case "2":
			case "5":
				map.put("stand_sum", 900.0);
				mon = 900.0 - discountPrice;
				map.put("real_sum", mon);
				map.put("return_sum", 0.0);
				break;
			case "3":
			case "4":
				map.put("stand_sum", 1500.0);
				mon = 1500.0 - discountPrice;
				map.put("real_sum", mon);
				map.put("return_sum", 0.0);
				break;
			default:
				break;
			}

		}
		// 判断月结条件（根据个别市场归属人“付敏”汪竹平等，医院名，福建所有地区业务员）
		if (Clerks.contains(case_user.getUsername())) {
			map.put("type", "4");
		}
		if (HOSPITAL.contains(params.get("hospital").toString())) {
			map.put("type", "4");
		}
		if ("350".equals(params.get("areacode").toString().substring(0, 3))) {
			map.put("type", "4");
		}
		// 财务登记
		boolean flag = caseFeeMapper.addCaseFeeOther(map);
		if (flag) {
			result.put("success", true);
			result.put("result", true);
			result.put("message", "操作成功！");
			return result;
		} else {
			result.put("success", false);
			result.put("result", false);
			result.put("message", "操作失败，请联系管理员！");
			return result;
		}
	}

	@Override
	public Map<String, Object> updateCaseFeeUnite(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		// 归属人信息查询
		map.put("userid", params.get("case_userid"));
		RdsUpcUserModel case_user = new RdsUpcUserModel();
		try {
			case_user = (RdsUpcUserModel) rdsUpcUserMapper.queryModel(map);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		map.put("case_id", params.get("case_id"));
		map.put("update_user", params.get("userid"));
		// 项目类型
		String case_type = params.get("case_type") == null ? "" : params.get(
				"case_type").toString();
		map.put("case_type", case_type);
		// 项目类型
		String program_type = params.get("program_type") == null ? "" : params
				.get("program_type").toString();
		map.put("program_type", program_type);
		// 财务备注
		map.put("finance_remark", params.get("finance_remark"));
		// 申请码：优惠，免单，先出报告后付款
		String confirm_code = params.get("confirm_code") == null ? "" : params
				.get("confirm_code").toString();
		double discountPrice = 0.0;
		// 激活码操作
		if (!"".equals(confirm_code)) {
			map.put("confirm_code", confirm_code);
			Map<String, Object> mapCode = new HashMap<String, Object>();
			mapCode.put("confirm_code", confirm_code);
			try {
				List<RdsFinanceSpecialModel> model = rdsFinanceChargeStandardMapper
						.queryAllSpecialFinance(mapCode);
				// 优惠码错误
				if (model.size() == 0) {
					result.put("success", true);
					result.put("result", false);
					result.put("message", "不存在该优惠码！");
					return result;
				} else {
					// 判断修改是否还是该案例的激活码
					if (!map.get("case_id").equals(model.get(0).getCase_id())) {
						// 新激活码
						if (!"1".equals(model.get(0).getUser_state())) {
							result.put("success", true);
							result.put("result", false);
							result.put("message", "请查看该优惠码状态！");
							return result;
						}
					}
					// 案例优惠标准
					switch (model.get(0).getCase_state()) {
					// 显出报告
					case "1":
						map.put("type", "1");
						break;
					// 免单
					case "2":
						map.put("type", "2");
						map.put("stand_sum", 0.0);
						map.put("real_sum", 0.0);
						map.put("return_sum", 0.0);
						if (caseFeeMapper.updateCaseFeeUnite(map)) {
							result.put("success", true);
							result.put("result", true);
							result.put("message", "操作成功！");
							return result;
						} else {
							result.put("success", false);
							result.put("result", false);
							result.put("message", "操作失败，请联系管理员！");
							return result;
						}
						// 优惠
					case "3":
						discountPrice = Double.parseDouble(model.get(0)
								.getDiscount_amount());
						map.put("discountPrice", discountPrice);
						map.put("type", "3");
						break;
					// 月结
					case "4":
						map.put("type", "4");
						break;
					default:
						break;
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("success", false);
				result.put("result", false);
				result.put("message", "出错啦，请联系管理员！");
				return result;
			}
		} else {
			map.put("type", "0");
		}
		// 无创产前项目
		if (INVERSIVE_TYPE.equals(case_type)) {
			// 财务项目名
			switch (program_type) {

			case "1":
				map.put("finance_type", "NIPT(博奥)");
				break;
			case "5":
				map.put("finance_type", "NIPT(成都博奥)");
				break;
			case "2":
				map.put("finance_type", "NIPT(贝瑞)");
				break;
			case "3":
				map.put("finance_type", "NIPT-plus(博奥)");
				break;
			case "4":
				map.put("finance_type", "NIPT-plus(贝瑞");
				break;
			case "6":
				map.put("finance_type", "NIPT(精科)");
				break;
			case "7":
				map.put("finance_type", "NIPT-plus(精科");
				break;
			default:
				break;
			}
			String areaname = params.get("areaname") == null ? "" : params.get(
					"areaname").toString();
			map.put("areaname", areaname);
			map.put("hospital", params.get("hospital"));
			try {
				// 获取无创收费标准
				List<RdsFinanceChargeStandardModel> equation = rdsFinanceChargeStandardMapper
						.getInversivePerFinance(map);
				// 没有配置收费标准默认标准
				if (equation.size() == 0) {
					double mon = 0;
					switch (program_type) {
					case "1":
					case "2":
						mon = 900.0 - discountPrice;
						map.put("stand_sum", mon);
						map.put("real_sum", mon);
						map.put("return_sum", 0.0);
						break;
					case "3":
					case "4":
						mon = 1500.0 - discountPrice;
						map.put("stand_sum", mon);
						map.put("real_sum", mon);
						map.put("return_sum", 0.0);
						break;
					default:
						break;
					}
				} else {
					double mon = Double.parseDouble(equation.get(0)
							.getSamplePrice());
					map.put("stand_sum", mon - discountPrice);
					map.put("real_sum", mon - discountPrice);
					map.put("return_sum", 0.0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("success", false);
				result.put("result", false);
				result.put("message", "出错啦，请联系管理员！");
				return result;
			}
		}
		// 招商中心无创价格
		if ("1004028".equals(case_user.getDeptcode())) {
			map.put("stand_sum", 700.0);
			map.put("real_sum", 700.0 - discountPrice);
			map.put("return_sum", 0.0);
		}
		// 判断月结条件（根据个别市场归属人“付敏”汪竹平等，医院名，福建所有地区业务员）
		if (Clerks.contains(case_user.getUsername())) {
			map.put("type", "4");
		}
		if (HOSPITAL.contains(params.get("hospital").toString())) {
			map.put("type", "4");
		}
		if ("350".equals(params.get("areacode").toString().substring(0, 3))) {
			map.put("type", "4");
		}
		// 财务更新
		boolean flag = caseFeeMapper.updateCaseFeeUnite(map);
		if (flag) {
			result.put("success", true);
			result.put("result", true);
			result.put("message", "操作成功！");
			return result;
		} else {
			result.put("success", false);
			result.put("result", false);
			result.put("message", "操作失败，请联系管理员！");
			return result;
		}
	}

	@Override
	public Map<String, Object> addCaseFeeChildren(Map<String, Object> params) {
		// 返回结果集
		Map<String, Object> result = new HashMap<>();
		// 财务参数
		Map<String, Object> map = new HashMap<>();
		// 应收金额
		Double stand_sum = Double.parseDouble(params.get("stand_sum")
				.toString());
		map.put("id", UUIDUtil.getUUID());
		map.put("case_id", params.get("case_id"));
		map.put("update_user", params.get("userid"));
		// 案例类型
		String case_type = params.get("case_type") == null ? "" : params.get(
				"case_type").toString();
		map.put("case_type", case_type);
		// 案例类型
		String finance_type = params.get("finance_type") == null ? "" : params
				.get("finance_type").toString();
		map.put("finance_type", finance_type);
		map.put("stand_sum", stand_sum);
		map.put("real_sum", stand_sum);
		// 项目类型
		String program_type = params.get("program_type") == null ? "" : params
				.get("program_type").toString();
		map.put("program_type", program_type);
		// 财务备注
		map.put("finance_remark", params.get("finance_remark"));
		map.put("type", 0);
		// 申请码：优惠，免单，先出报告后付款
		// String confirm_code = params.get("confirm_code") == null ? "" :
		// params
		// .get("confirm_code").toString();
		// double discountPrice = 0.0;
		// 激活码操作
		// if (!"".equals(confirm_code)) {
		// map.put("confirm_code", confirm_code);
		// Map<String, Object> mapCode = new HashMap<String, Object>();
		// mapCode.put("confirm_code", confirm_code);
		// try {
		// List<RdsFinanceSpecialModel> model = rdsFinanceChargeStandardMapper
		// .queryAllSpecialFinance(mapCode);
		// // 优惠码错误
		// if (model.size() == 0) {
		// result.put("success", true);
		// result.put("result", false);
		// result.put("message", "不存在该优惠码！");
		// return result;
		// } else {
		// // 优惠码不是未使用状态
		// if (!"1".equals(model.get(0).getUser_state())) {
		// result.put("success", true);
		// result.put("result", false);
		// result.put("message", "请查看该优惠码状态！");
		// return result;
		// }
		// // 案例优惠标准
		// switch (model.get(0).getCase_state()) {
		// // 先出报告后付款
		// case "1":
		// map.put("type", "1");
		// break;
		// // 免单
		// case "2":
		// map.put("type", "2");
		// map.put("stand_sum", 0.0);
		// map.put("real_sum", 0.0);
		// map.put("return_sum", 0.0);
		// break;
		// // 优惠
		// case "3":
		// discountPrice = Double.parseDouble(model.get(0)
		// .getDiscount_amount());
		// map.put("discountPrice", discountPrice);
		// map.put("discount_amount", discountPrice);
		// map.put("type", "3");
		// map.put("real_sum", stand_sum - discountPrice);
		// break;
		// // 月结
		// case "4":
		// map.put("type", "4");
		// break;
		// default:
		// break;
		// }
		//
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// result.put("success", false);
		// result.put("result", false);
		// result.put("message", "出错啦，请联系管理员！");
		// return result;
		// }
		// }

		try {
			// 判断该登记案例是否为先出报告后付款或月结
			Map<String, Object> map2 = new HashMap<String, Object>();
			// 申请人
			map2.put("monthly_per", params.get("case_userid"));
			// 结算区域
			map2.put("monthly_area", params.get("case_areacode"));
			// 项目类型
			map2.put("case_type", case_type);
			// 判断该登记案例是否为先出报告后付款或月结
			List<RdsFinanceSpecialModel> model = rdsFinanceChargeStandardMapper
					.queryAllSpecialFinance(map2);
			// 存在则说明该案例属于月结或先出报告后付款区域
			if (model.size() > 0) {
				if ("1".equals(model.get(0).getCase_state())
						|| "4".equals(model.get(0).getCase_state())) {
					// 判断该激活码是否被使用
					if ("3".equals(model.get(0).getUser_state())) {
						result.put("result", false);
						result.put("success", true);
						result.put("message", "该地区配置已失效，请查看！");
						return result;
					} else {
						map.put("type", model.get(0).getCase_state());
					}
				}

				// 判断改区域是否出现预期未付款的情况
				if ("3".equals(model.get(0).getSettlement_state())) {
					result.put("result", false);
					result.put("success", true);
					result.put("message", "存在逾期未结算情况，请联系管理员！");
					return result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("success", true);
			result.put("message", "月结或先出报告后付款设置有误，请联系管理员！");
			return result;
		}
		if (caseFeeMapper.addCaseFeeOther(map)) {
			// try {
			// rdsFinanceChargeStandardMapper.updateCodeUsed(result);
			// } catch (Exception e) {
			// e.printStackTrace();
			// result.put("success", false);
			// result.put("result", false);
			// result.put("message", "操作失败，请联系管理员！");
			// return result;
			// }

			// if (!"".equals(confirm_code)) {
			// // 设置使用状态
			// result.put("user_state", 2);
			// result.put("confirm_code", params.get("confirm_code"));
			// result.put("case_id", params.get("case_id"));
			//
			// }
			result.put("success", true);
			result.put("result", true);
			result.put("message", "操作成功！");
			return result;
		} else {
			result.put("success", false);
			result.put("result", false);
			result.put("message", "操作失败，请联系管理员！");
			return result;
		}
	}

	@Override
	public Map<String, Object> updateCaseFeeChildren(Map<String, Object> params) {
		// 返回结果集
		Map<String, Object> result = new HashMap<>();
		// 财务参数
		Map<String, Object> map = new HashMap<>();
		// 应收金额
		Double stand_sum = Double.parseDouble(params.get("stand_sum")
				.toString());
		String case_id = params.get("case_id").toString();
		map.put("case_id", case_id);
		map.put("update_user", params.get("userid"));
		// 案例类型
		String case_type = params.get("case_type") == null ? "" : params.get(
				"case_type").toString();
		map.put("case_type", case_type);
		map.put("stand_sum", stand_sum);
		map.put("real_sum", stand_sum);
		// 项目类型
		String program_type = params.get("program_type") == null ? "" : params
				.get("program_type").toString();
		map.put("program_type", program_type);
		// 财务备注
		map.put("finance_remark", params.get("finance_remark"));
		// 申请码：优惠，免单，先出报告后付款
		// String confirm_code = params.get("confirm_code") == null ? "" :
		// params
		// .get("confirm_code").toString();
		// double discountPrice = 0.0;
		// 激活码操作
		// if (!"".equals(confirm_code)) {
		// map.put("confirm_code", confirm_code);
		// Map<String, Object> mapCode = new HashMap<String, Object>();
		// mapCode.put("confirm_code", confirm_code);
		// try {
		// List<RdsFinanceSpecialModel> model = rdsFinanceChargeStandardMapper
		// .queryAllSpecialFinance(mapCode);
		// // 优惠码错误
		// if (model.size() == 0) {
		// result.put("success", true);
		// result.put("result", false);
		// result.put("message", "不存在该优惠码！");
		// return result;
		// } else {
		// //判断是否原来的优惠码，如果时继续使用，不是验证是否使用过
		// if(!case_id.equals(model.get(0).getCase_id())){
		// // 优惠码不是未使用状态
		// if (!"1".equals(model.get(0).getUser_state())) {
		// result.put("success", true);
		// result.put("result", false);
		// result.put("message", "请查看该优惠码状态！");
		// return result;
		// }
		// }
		// // 案例优惠标准
		// switch (model.get(0).getCase_state()) {
		// // 先出报告后付款
		// case "1":
		// map.put("type", "1");
		// break;
		// // 免单
		// case "2":
		// map.put("type", "2");
		// map.put("stand_sum", 0.0);
		// map.put("real_sum", 0.0);
		// map.put("return_sum", 0.0);
		// break;
		// // 优惠
		// case "3":
		// discountPrice = Double.parseDouble(model.get(0)
		// .getDiscount_amount());
		// map.put("discountPrice", discountPrice);
		// map.put("discount_amount", discountPrice);
		// map.put("type", "3");
		// map.put("real_sum", stand_sum - discountPrice);
		// break;
		// // 月结
		// case "4":
		// map.put("type", "4");
		// break;
		// default:
		// break;
		// }
		//
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// result.put("success", false);
		// result.put("result", false);
		// result.put("message", "出错啦，请联系管理员！");
		// return result;
		// }
		// }

		try {
			// 判断该登记案例是否为先出报告后付款或月结
			Map<String, Object> map2 = new HashMap<String, Object>();
			// 申请人
			map2.put("monthly_per", params.get("case_userid"));
			// 结算区域
			map2.put("monthly_area", params.get("case_areacode"));
			// 项目类型
			map2.put("case_type", case_type);
			// 判断该登记案例是否为先出报告后付款或月结
			List<RdsFinanceSpecialModel> model = rdsFinanceChargeStandardMapper
					.queryAllSpecialFinance(params);
			// 存在则说明该案例属于月结或先出报告后付款区域
			if (model.size() > 0) {
				if ("1".equals(model.get(0).getCase_state())
						|| "4".equals(model.get(0).getCase_state())) {
					// 判断该激活码是否被使用
					if ("3".equals(model.get(0).getUser_state())) {
						result.put("result", false);
						result.put("success", true);
						result.put("message", "该地区配置已失效，请查看！");
						return result;
					} else {
						map.put("type", model.get(0).getCase_state());
					}
				}
				// 判断改区域是否出现预期未付款的情况
				if ("3".equals(model.get(0).getSettlement_state())) {
					result.put("result", false);
					result.put("success", true);
					result.put("message", "存在逾期未结算情况，请联系管理员！");
					return result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("success", true);
			result.put("message", "月结或先出报告后付款设置有误，请联系管理员！");
			return result;
		}
		if (caseFeeMapper.updateCaseFeeUnite(map)) {
			// try {
			// rdsFinanceChargeStandardMapper.updateCodeUsed(result);
			// } catch (Exception e) {
			// e.printStackTrace();
			// result.put("success", false);
			// result.put("result", false);
			// result.put("message", "操作失败，请联系管理员！");
			// return result;
			// }
			// if (!"".equals(confirm_code)) {
			// // 设置使用状态
			// result.put("user_state", 2);
			// result.put("confirm_code", params.get("confirm_code"));
			// result.put("case_id", params.get("case_id"));
			//
			// }
			result.put("success", true);
			result.put("result", true);
			result.put("message", "操作成功！");
			return result;
		} else {
			result.put("success", false);
			result.put("result", false);
			result.put("message", "操作失败，请联系管理员！");
			return result;
		}
	}

	@Override
	public Map<String, Object> updateCaseFeeByRegister(
			Map<String, Object> params) {
		// 返回结果集
		Map<String, Object> result = new HashMap<>();
		if (caseFeeMapper.updateCaseFeeUnite(params)) {
			result.put("success", true);
			result.put("result", true);
			result.put("message", "操作成功！");
			return result;
		} else {
			result.put("success", false);
			result.put("result", false);
			result.put("message", "操作失败，请联系管理员！");
			return result;
		}
	}

	/**
	 * 页面文件上传
	 */
	@Override
	@Transactional
	public Map<String, Object> upload(String contract_id, String contract_num,
			MultipartFile[] files, int[] filetype, String userid)
			throws Exception {
		String msg = "";
		String attachmentPath = ATTACHMENTPATH + contract_id
				+ File.separatorChar;
		if (contract_id == null || "".equals(contract_id)) {
			return setMsg(true, false, "案例不存在,附件上传失败");
		}
		RdsFinanceContractAttachmentModel attachmentModel = new RdsFinanceContractAttachmentModel();
		attachmentModel.setContract_id(contract_id);
		attachmentModel.setId(UUIDUtil.getUUID());
		attachmentModel.setContract_num(contract_num);
		attachmentModel.setAttachment_date(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		attachmentModel.setCreate_per(userid);
		if (files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				if (!"".equals(files[i].getOriginalFilename())) {
					String id = UUIDUtil.getUUID();
					attachmentModel.setId(id);
					attachmentModel.setAttachment_path(contract_id
							+ File.separatorChar
							+ files[i].getOriginalFilename());
					attachmentModel.setAttachment_type(filetype[i]);
					if (!RdsFileUtil.getState(attachmentPath
							+ files[i].getOriginalFilename())) {
						RdsFileUtil
								.fileUpload(
										attachmentPath
												+ files[i]
														.getOriginalFilename(),
										files[i].getInputStream());
						// 判断是否存在该案例以上传过的文件
						Map<String, Object> map = new HashMap<>();
						map.put("contract_id", contract_id);
						map.put("attachment_path",
								attachmentModel.getAttachment_path());
						List<RdsFinanceContractAttachmentModel> attachmentId = caseFeeMapper
								.queryContractAttachment(map);
						if (attachmentId.size() > 0) {
							msg = msg + "文件：" + files[i].getOriginalFilename()
									+ "已上传过,上传失败。<br>";
						} else {
							if (caseFeeMapper
									.insertContractAttachment(attachmentModel)) {
								msg = msg + "文件："
										+ files[i].getOriginalFilename()
										+ "上传成功。<br>";
							} else
								msg = msg + "文件："
										+ files[i].getOriginalFilename()
										+ "上传失败。<br>";
						}
					} else {
						msg = msg + "文件：" + files[i].getOriginalFilename()
								+ "已上传过,上传失败。<br>";
					}
				}
			}
			return setMsg(true, true, msg);
		} else
			return setMsg(true, false, msg + "未收到文件,上传失败");
	}

	private Map<String, Object> setMsg(boolean success, boolean result,
			String message) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	@Override
	public boolean deleteFile(Map<String, Object> params) {
		try {
			return caseFeeMapper.deleteContractAttachment(params);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<RdsFinanceContractAttachmentModel> queryContractAttachment(
			Map<String, Object> params) {
		return caseFeeMapper.queryContractAttachment(params);
	}

	@Override
	public void downloadAttachment(HttpServletResponse response, String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		List<RdsFinanceContractAttachmentModel> list = caseFeeMapper
				.queryContractAttachment(map);
		String filename = list.get(0).getAttachment_path();
		File file = new File(ATTACHMENTPATH + filename);
		if (file.exists()) {
			response.reset();
			response.setHeader(
					"Content-Disposition",
					"attachment; filename="
							+ filename.substring(filename
									.lastIndexOf(File.separator) + 1));
			response.setContentType("application/octet-stream; charset=utf-8");
			FileInputStream in = null;
			OutputStream toClient = null;
			try {
				in = new FileInputStream(file);
				// 得到文件大小
				int i = in.available();
				byte data[] = new byte[i];
				// 读数据
				in.read(data);
				toClient = response.getOutputStream();
				toClient.write(data);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (toClient != null) {
					try {
						toClient.flush();
						toClient.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	@Override
	public boolean insertContractRemittance(Map<String, Object> map)
			throws Exception {
		return caseFeeMapper.insertRemittancePlan(map);
		// 插入回款计划
		// if (null != map.get("remittance_date")) {
		// List<String> remittance_dates = getValues(map
		// .get("remittance_date"));
		// List<String> remittances = getValues(map.get("remittance"));
		// for (int i = 0; i < remittance_dates.size(); i++) {
		// Map<String, Object> params = new HashMap<>();
		// params.put("contract_remittance_planid", UUIDUtil.getUUID());
		// params.put("contract_id", map.get("contract_id"));
		// params.put("remittance", remittances.get(i));
		// params.put("remittance_date", remittance_dates.get(i));
		// result = caseFeeMapper.insertRemittancePlan(map);
		// }
		// }
	}

	@Override
	public boolean updateRemittancePlan(Map<String, Object> map)
			throws Exception {
		return caseFeeMapper.updateRemittancePlan(map);
	}

	@Override
	public boolean deleteRemittancePlan(Map<String, Object> map)
			throws Exception {
		return caseFeeMapper.deleteRemittancePlan(map);
	}

	@Override
	public void exportCaseFinance(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "案例财务确认信息表";
		List<RdsRemittanceInfoModel> list = caseFeeMapper
				.queryPageRemittanceInfo(params);
		Object[] titles = { "日报类型", "汇款单号", "确认状态", "是否紧急", "是否抵扣", "汇款时间",
				"汇款金额", "汇款账户", "到款账户", "汇款人", "汇款创建人", "汇款创建时间", "汇款备注",
				"汇款确认人", "确认时间", "确认备注" };
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsRemittanceInfoModel model = list.get(i);
			int daily_type = Integer.parseInt(model.getDaily_type());
			switch (daily_type) {
			case 1:
				objects.add("亲子鉴定");
				break;
			case 2:
				objects.add("合同计划");
				break;
			case 3:
				objects.add("无创产前");
				break;
			case 4:
				objects.add("儿童基因库");
				break;
			default:
				objects.add("亲子鉴定");
			}
			objects.add(model.getRemittance_num());
			String confirm_state = model.getConfirm_state();
			if ("1".equals(confirm_state))
				objects.add("通过");
			else if ("2".equals(confirm_state))
				objects.add("未通过");
			else
				objects.add("未确认");
			int urgent_state = model.getUrgent_state();
			if (1 == urgent_state)
				objects.add("加急");
			else
				objects.add("正常");
			objects.add("2".equals(model.getDeductible()) ? "否" : "是");
			objects.add(model.getRemittance_date());
			objects.add(Double.parseDouble(model.getRemittance()));
			objects.add(model.getRemittance_account());
			objects.add(model.getArrival_account());
			objects.add(model.getRemittance_per_name());
			objects.add(model.getCreate_per_name());
			objects.add(model.getCreate_date());
			objects.add(model.getRemittance_remark());
			objects.add(model.getConfirm_per_name());
			objects.add(model.getConfirm_date());
			objects.add(model.getConfirm_remark());
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "案例财务确认信息表");

	}

	@Override
	public boolean deductibleCaseInfo(Map<String, Object> params)
			throws Exception {
		return caseFeeMapper.deductibleCaseInfo(params);
	}

	@Override
	public void exportCaseInfoOther(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "案例信息";
		List<RdsCaseFinanceModel> list = caseFeeMapper.getCaseFeeInfo(params);

		Object[] titles = { "编号", "日期", "委托人", "归属人", "归属地", "应收", "回款",
				"汇款时间", "确认时间", "优惠价格", "财务备注", "样本信息", "是否确认" };
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsCaseFinanceModel rdsJudicialCaseFeeModel = (RdsCaseFinanceModel) list
					.get(i);
			objects.add(rdsJudicialCaseFeeModel.getCase_code());
			objects.add(StringUtils.dateToChineseTen(rdsJudicialCaseFeeModel
					.getDate()));
			objects.add(rdsJudicialCaseFeeModel.getClient());
			objects.add(rdsJudicialCaseFeeModel.getReceive_name());
			objects.add(rdsJudicialCaseFeeModel.getAreaname());
			objects.add(rdsJudicialCaseFeeModel.getReal_sum());
			objects.add(rdsJudicialCaseFeeModel.getReturn_sum());
			objects.add(StringUtils.dateToChineseTen(rdsJudicialCaseFeeModel
					.getParagraphtime()));
			objects.add(StringUtils.dateToChineseTen(rdsJudicialCaseFeeModel
					.getConfirm_date()));
			objects.add(rdsJudicialCaseFeeModel.getDiscountPrice());
			objects.add(rdsJudicialCaseFeeModel.getFinance_remark());
			objects.add(rdsJudicialCaseFeeModel.getSample_str());
			objects.add((0 == rdsJudicialCaseFeeModel.getStatus()) ? "是" : "否");
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "案例信息");

	}

	@Override
	public void getImg(HttpServletResponse response, String filename) {
		if (!"".equals(filename) && filename != null) {
			if (FileUtils.getState(ATTACHMENTPATH + filename)) {
				DownLoadUtils.download(response, ATTACHMENTPATH + filename);
			}
		}
	}

	@Override
	public Map<String, Object> turnImg(Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		File file = new File(ATTACHMENTPATH + params.get("path"));
		if (file.exists()) {
			BufferedImage image = null;
			try {
				image = ImageIO.read(new FileInputStream(file));
				BufferedImage newImage = null;
				if ("left".equals(params.get("direction"))) {
					newImage = ImgUtil.rotate90SX(image);
				} else {
					newImage = ImgUtil.rotate90DX(image);
				}
				boolean flag = ImageIO.write(newImage, "jpg", file);
				if (flag) {
					map.put("success", flag);
				} else
					throw new IOException();
			} catch (IOException e) {
				e.printStackTrace();
				map.put("success", false);
			}
		}
		return map;
	}

	@Override
	public boolean updateRemittancePhoto(Map<String, Object> params) {
		// 上传附件
		try {
			MultipartFile file = (MultipartFile) params.get("file");
			String attachmentPath = ATTACHMENTPATH
					+ params.get("remittance_num") + File.separatorChar;
			// 上传新图片
			RdsFileUtil
					.fileUpload(
							attachmentPath + File.separatorChar
									+ file.getOriginalFilename(),
							file.getInputStream());
			params.put("attachment_path", params.get("remittance_num").toString()
					+ File.separatorChar + file.getOriginalFilename());
			//更新汇款单信息
			if(caseFeeMapper.insertRemittanceLogs(params)){
				caseFeeMapper.updateRemittancePhoto(params);
				return caseFeeMapper.insertFinanceAttachment(params);
			}
			else 
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<RdsCaseFinanceAttachmentModel> queryFinanceAttachment(
			Map<String, Object> params) throws Exception {
		return caseFeeMapper.queryFinanceAttachment(params);
	}

	@Override
	public boolean insertFinanceAttachment(Map<String, Object> map)
			throws Exception {
		return caseFeeMapper.insertFinanceAttachment(map);
	}

	@Override
	public boolean updateFinanceAttachment(Map<String, Object> map)
			throws Exception {
		String attachment_path = map.get("attachment_path").toString();
		// 删除旧照片
		RdsFileUtil.delFile(ATTACHMENTPATH + attachment_path);
		//记录日志
		caseFeeMapper.insertRemittanceLogs(map);
		//更新汇款单状态
		caseFeeMapper.updateRemittancePhoto(map);
		//更新附件状态
		return caseFeeMapper.updateFinanceAttachment(map);
	}

}
