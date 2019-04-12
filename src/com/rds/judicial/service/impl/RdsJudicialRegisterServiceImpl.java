package com.rds.judicial.service.impl;

/**
 * @author yxb
 */
import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.code.date.DateUtils;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.XmlParseUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.finance.mapper.RdsCaseFinanceMapper;
import com.rds.finance.model.RdsFinanceSpecialModel;
import com.rds.finance.service.RdsCaseFinanceService;
import com.rds.finance.service.RdsFinanceChargeStandardService;
import com.rds.judicial.mapper.RdsJudicialFeeQuationMapper;
import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.mapper.RdsJudicialSubCaseMapper;
import com.rds.judicial.mapper.RdsJudicialVerifyMapper;
import com.rds.judicial.model.RdsJudicialApplySampleCodeModel;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.judicial.model.RdsJudicialParamsModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleExpressModel;
import com.rds.judicial.model.RdsJudicialSampleInfoModel;
import com.rds.judicial.model.RdsJudicialSecondModel;
import com.rds.judicial.model.RdsJudicialSubCaseInfoModel;
import com.rds.judicial.service.RdsJudicialCaseAttachmentService;
import com.rds.judicial.service.RdsJudicialRegisterService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.service.RdsUpcCompanyService;
import com.rds.upc.service.RdsUpcPartnerConfigService;

@Service("RdsJudicialRegisterService")
@Transactional
public class RdsJudicialRegisterServiceImpl implements
		RdsJudicialRegisterService {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static final String XML_PATH = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config.xml";

	private static final String XML_PATH_1 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config1.xml";

	private static final String XML_PATH_2 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config2.xml";

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String REORT_MODEL = PropertiesUtils.readValue(
			FILE_PATH, "reportModel");

	private static final String UNKNOWN = "unknown";
	@Setter
	@Autowired
	private RdsJudicialRegisterMapper RdsJudicialRegisterMapper;

	@Setter
	@Autowired
	private RdsJudicialSubCaseMapper rdsJudicialSubCaseMapper;

	@Setter
	@Autowired
	private RdsCaseFinanceMapper caseFeeMapper;

	@Setter
	@Autowired
	private RdsJudicialFeeQuationMapper equationMapper;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private RuntimeService runtimeService;

	@Setter
	@Autowired
	private RdsCaseFinanceService rdsCaseFinanceService;

	@Setter
	@Autowired
	private RdsFinanceChargeStandardService rdsFinanceChargeStandardService;

	@Setter
	@Autowired
	private RdsUpcCompanyService rdsUpcCompanyService;

	@Setter
	@Autowired
	private RdsUpcPartnerConfigService rdsUpcPartnerConfigService;

	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;

	@Autowired
	private RdsJudicialCaseAttachmentService rdsJudicialCaseAttachmentService;

	@Setter
	@Autowired
	private RdsJudicialVerifyMapper rdsJudicialVerifyMapper;

	/**
	 * 根据条件获取案例的基本信息
	 */
	@Override
	public RdsJudicialResponse getCaseInfo(Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialCaseInfoModel> caseInfoModels = RdsJudicialRegisterMapper
				.queryCaseInfo(params);
		int count = RdsJudicialRegisterMapper.countCaseInfo(params);
		response.setItems(caseInfoModels);
		response.setCount(count);
		return response;
	}

	/**
	 * 删除案例的信息
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public boolean deleteCaseInfo(Map<String, Object> params) {

		int result = RdsJudicialRegisterMapper.deleteCaseInfo(params);
		// 增加删除样本信息
		RdsJudicialRegisterMapper.deleteSampleInfo(params);
		// 增加删除子案例信息
		RdsJudicialRegisterMapper.deleteSubCaseInfo(params);
		if (result > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 删除样本的信息
	 */
	public int deleteSampleInfo(Map<String, Object> params) {
		return RdsJudicialRegisterMapper.deleteSampleInfo(params);
	}

	/**
	 * 获取案例样本的信息
	 */
	@Override
	public RdsJudicialResponse getSampleInfo(Map<String, Object> params) {
		RdsJudicialResponse resJudicialResponse = new RdsJudicialResponse();
		List<RdsJudicialSampleInfoModel> sampleInfoModels = RdsJudicialRegisterMapper
				.getSampleInfo(params.get("case_id"));
		resJudicialResponse.setItems(sampleInfoModels);
		return resJudicialResponse;
	}

	/**
	 * 保存案例信息
	 */
	@Override
	public Object saveCaseInfo(Map<String, Object> params, RdsUpcUserModel user) {

		// 案例返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		// 案例优惠金额
		int discount_amount = 0;
		// 案例状态（正常，优惠，先出报告后付款，月结，免单）
		// 案例类型
		String case_type = params.get("case_type") == null ? "0" : params.get(
				"case_type").toString();
		// 判断二次采样情况下插入非系统生成案例编号关联
		Map<String, Object> map = new HashMap<>();
		// 二次采样和补样时存在
		map.put("case_code", params.get("case_code"));
		map.put("id", UUIDUtil.getUUID());
		map.put("case_id", params.get("case_id").toString());

		// 插入关联关系（不由系统生成编号时进入，医学案例都由系统生成案例条码）
		if (null == params.get("report_model") && !"1".equals(case_type)) {
			if (null != params.get("case_state")) {
				// 判断补样或二次采样存入关联编码
				if ("5".equals(params.get("case_state").toString())
						|| "6".equals(params.get("case_state").toString())) {
					// 其中case_code_update为前端手动输入的二次采样或补样的新案例条码名称
					map.put("case_code_second", params.get("case_code_update"));
					// 将新案例条码作为case_code存入系统，上面case_code_update只是为了存入关联关系使用
					params.put("case_code", params.get("case_code_update"));
				}
			}
		}
		// 判断没有案例状态情况下处理5：二次采样；6：补样
		if (null != params.get("case_state")) {
			// 不是补样或而二次采样情况下，案例状态默认为0，即正常
			if (!"5".equals(params.get("case_state").toString())
					&& !"6".equals(params.get("case_state").toString()))
				params.put("case_state", 0);
			else {
				// 否则根据案例编号和状态判断是否已存在二次采样案例
				map.put("case_state", params.get("case_state"));
				if ("5".equals(params.get("case_state").toString())) {
					if (RdsJudicialRegisterMapper.queryCaseCodeSecond(map)
							.size() > 0) {
						result.put("result", false);
						result.put("success", true);
						result.put("message", "该案例已存在二次采样案例！");
						return result;
					}
				}
			}
			// 判断只登记信息案例的二次采样和补样，插入关联表
			if (null == params.get("report_model") && !"1".equals(case_type)) {
				if ("5".equals(params.get("case_state").toString())
						|| "6".equals(params.get("case_state").toString())) {
					RdsJudicialRegisterMapper.insertCaseCodeSecond(map);
				}
			}
		} else {
			params.put("case_state", '0');
		}

		// 判断是否填写激活码，判断案例是否为优惠或免单的情况
		if (null != params.get("confirm_code")
				&& !"".equals(params.get("confirm_code"))) {
			try {
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("confirm_code", params.get("confirm_code"));
				// 根据优惠码查询到对应的优惠信息
				List<RdsFinanceSpecialModel> model = rdsFinanceChargeStandardService
						.queryAllSpecialFinance(map1);
				if (model.size() == 0) {
					result.put("success", true);
					result.put("result", false);
					result.put("message", "优惠码有误，请查看！");
					return result;
				}
				// 写入对应状态到案例中
				params.put("case_state", model.get(0).getCase_state());
				// 状态为优惠申请
				if ("3".equals(model.get(0).getCase_state())) {
					// 判断该激活码是否被使用
					if ("2".equals(model.get(0).getUser_state())) {
						result.put("result", false);
						result.put("success", true);
						result.put("message", "优惠码已使用，请查看！");
						return result;
					}
					// 否则查询出优惠金额
					discount_amount = Integer.parseInt(model.get(0)
							.getDiscount_amount());

					// 设置使用状态
					result.put("user_state", 2);
					result.put("confirm_code", params.get("confirm_code"));
					result.put("case_id", params.get("case_id"));
					rdsFinanceChargeStandardService.updateCodeUsed(result);

				}
				// 该优惠码为免单
				else if ("2".equals(model.get(0).getCase_state())) {
					// 判断该激活码是否被使用
					if ("2".equals(model.get(0).getUser_state())) {
						result.put("result", false);
						result.put("success", true);
						result.put("message", "该优惠码已使用，请查看！");
						return result;
					}
					// 设置使用状态
					result.put("user_state", 2);
					result.put("confirm_code", params.get("confirm_code"));
					result.put("case_id", params.get("case_id"));
					rdsFinanceChargeStandardService.updateCodeUsed(result);
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("result", false);
				result.put("success", true);
				result.put("message", "新增有误，请联系管理员！");
				return result;
			}
		}
		params.put("type", "0");
		try {
			// 判断该登记案例是否为先出报告后付款或月结
			Map<String, Object> map2 = new HashMap<String, Object>();
			// 申请人
			map2.put("monthly_per", params.get("case_userid"));
			// 结算区域
			map2.put("monthly_area", params.get("case_areacode"));
			// 案例类型
			map2.put("case_type", "dna");
			// 判断该登记案例是否为先出报告后付款或月结
			List<RdsFinanceSpecialModel> model = rdsFinanceChargeStandardService
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
					}
					if ("5".equals(params.get("case_state").toString())) {
						params.put(
								"remark",
								params.get("remark") == null ? ("该案例为"
										+ params.get("case_code") + "的二次采样案例")
										: (params.get("remark").toString()
												+ ";该案例为"
												+ params.get("case_code") + "的二次采样案例"));
					} else if ("6".equals(params.get("case_state").toString())) {
						params.put(
								"remark",
								params.get("remark") == null ? ("该案例为"
										+ params.get("case_code") + "补样案例")
										: (params.get("remark").toString()
												+ ";该案例为"
												+ params.get("case_code") + "的补样案例"));
						// params.put("case_state",
						// model.get(0).getCase_state());
					}
					// else
					// {
					// params.put("case_state", model.get(0).getCase_state());
					// }
					if ("4".equals(model.get(0).getCase_state())) {
						params.put("type", model.get(0).getCase_state());
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
			result.put("message", "新增有误，请联系管理员！");
			return result;
		}
		// 案例实验室
		String laboratory_no = "";
		// 判断该案例是否合作商案例，合作商案例则匹配合作商实验室编号，否则为子渊实验室编号
		if (null == params.get("parnter_name")
				|| "".equals(params.get("parnter_name")))
			laboratory_no = rdsUpcCompanyService.queryLaboratoryNo(user
					.getCompanyid());// 根据公司编号获取实验室ID
		else {
			try {
				// 给合作商配置的实验室编号
				laboratory_no = rdsUpcPartnerConfigService
						.getLaboratoryNo(params.get("parnter_name").toString());
			} catch (Exception e) {
				laboratory_no = "";
				e.printStackTrace();
			}
		}
		// }
		// 实验室编号插入
		params.put("laboratory_no", laboratory_no);

		// 添加财务类型（司法和医学案例）
		params.put("finance_type",
				params.get("case_type").equals("0") ? "亲子鉴定-司法" : "亲子鉴定-医学");
		// 项目类型：亲子鉴定
		// params.put("program_type", 0);
		// 优惠金额
		params.put("discount_amount", discount_amount);

		// 添加财务信息
		if (rdsCaseFinanceService.addCaseFee(params)) {
			// 系统生成报告或医学报告
			if (null != params.get("report_model") || "1".equals(case_type)) {

				/** 案例编号生成 start **/
				// 案例归属地，根据归属地判断自动生成案例编号
				String receiver_area = params.get("receiver_area") == null ? ""
						: params.get("receiver_area").toString();
				String report_model = (params.get("report_model") == null) ? ""
						: params.get("report_model").toString();
				// 判断是否子渊案例，子渊模版或者医学案例案例自动生成案例编号，其他模版手动输入
				if (REORT_MODEL.contains(report_model)&&!"qsjdmodel".equals(report_model) || "1".equals(case_type)) {
					// 判断二次采样案例编号加_2
					if ("5".equals(params.get("case_state"))) {
						params.put("case_code", params.get("case_code")
								.toString() + "_2");
					} else {
						// 系统生成新的案例编号
						String case_code = getCaseCode(receiver_area,
								case_type, report_model,
								params.get("case_userid").toString(),
								params.get("case_username") == null ? ""
										: params.get("case_username")
												.toString());
						params.put("case_code", case_code);
					}
					// 判断是否为二次采样或补样，插入关联关系
					if ("5".equals(params.get("case_state").toString())
							|| "6".equals(params.get("case_state").toString())) {
						map.put("id", UUIDUtil.getUUID());
						map.put("case_code_second", params.get("case_code"));
						RdsJudicialRegisterMapper.insertCaseCodeSecond(map);
					}
				} else {
					// 非系统生成案例编号
					// 补样或二次采样
					if ("5".equals(params.get("case_state").toString())
							|| "6".equals(params.get("case_state").toString())) {
						map.put("id", UUIDUtil.getUUID());
						map.put("case_code_second",
								params.get("case_code_update"));
						params.put("case_code", params.get("case_code_update"));
						RdsJudicialRegisterMapper.insertCaseCodeSecond(map);
					}
					Map<String, Object> map1 = new HashMap<>();
					map1.put("case_code", params.get("case_code"));
					// 其他模版手动输入案例编号验证重复
					if (RdsJudicialRegisterMapper.exsitCaseCode(map1) > 0) {
						result.put("result", false);
						result.put("success", true);
						result.put("message", "案例编码已存在");
						return result;
					}
				}
				/** 案例编号生成 end **/
				/*
				 * 案例编号生成 start Calendar c = Calendar.getInstance(); Map<String,
				 * Object> map = new HashMap<String, Object>(); map.put("id",
				 * params.get("case_id")); map.put("letter", "Z");
				 * map.put("year", c.get(Calendar.YEAR)); map.put("month",
				 * c.get(Calendar.MONTH) < 9 ? "0" + (c.get(Calendar.MONTH) + 1)
				 * : c .get(Calendar.MONTH) + 1); String num =
				 * RdsJudicialRegisterMapper.queryCaseCodeNum(map).get(0)
				 * .get("num").toString(); map.put("num", num);
				 * RdsJudicialRegisterMapper.insertCaseCodeNum(map); String
				 * case_code = map.get("letter").toString() + map.get("year") +
				 * map.get("month") + num;
				 */

				// params.put("case_code", params.get("case_code"));
				/* 案例编号生成 end */

				/** 生成案例统一流水号 start **/
				String xml1_time = XmlParseUtil.getXmlValue(XML_PATH_1,
						"company_code_date");
				String now1_time = com.rds.code.utils.StringUtils
						.dateToEight(new Date());
				DecimalFormat df = new DecimalFormat("00000");
				String company_code = "";
				if (xml1_time.equals(now1_time)) {
					company_code = "Z"
							+ now1_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_1, "company_code_num")) + 1);
					XmlParseUtil
							.updateXmlValue(XML_PATH_1, "company_code_num",
									String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_1,
													"company_code_num")) + 1));
				} else {
					XmlParseUtil.updateXmlValue(XML_PATH_1,
							"company_code_date", now1_time);
					XmlParseUtil.updateXmlValue(XML_PATH_1, "company_code_num",
							"1");
					company_code = "Z"
							+ now1_time
							+ df.format(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_1, "company_code_num")));
				}
				params.put("company_code", company_code);
				RdsJudicialRegisterMapper.insertCompanyCodeCaseid(params);
				/** 生成案例统一流水号 end **/

				if (null == params.get("case_code")
						|| "".equals(params.get("case_code"))
						|| "子渊模版案例自动生成，毋须填写！".equals(params.get("case_code"))) {
					System.out.println("case_code==================="
							+ params.get("case_code"));
					result.put("result", false);
					result.put("success", true);
					result.put("message", "案例新增异常，请联系管理员！");
					return result;
				}
				// 判断案例新增成功状态
				int case_result = 0;
				// 新增案例实体
				RdsJudicialCaseInfoModel caseInfoModel = new RdsJudicialCaseInfoModel();
				try {
					caseInfoModel.setCase_id(params.get("case_id").toString());
					caseInfoModel.setCase_code(params.get("case_code")
							.toString());
					caseInfoModel.setAccept_time(params.get("accept_time")
							.toString());
					caseInfoModel.setConsignment_time(params.get(
							"consignment_time").toString());
					caseInfoModel.setReport_model(report_model);
					caseInfoModel.setUnit_type(params.get("unit_type")
							.toString());
					// 单双亲类型
					caseInfoModel.setTypeid(params.get("typeid").toString());
					// 亲属关系
					String sample_relation = params.get("sample_relation") == null ? "0"
							: params.get("sample_relation").toString();
					caseInfoModel.setSample_relation(Integer
							.parseInt(sample_relation));
					// 委托人
					caseInfoModel.setClient(params.get("client").toString());
					caseInfoModel.setPhone(params.get("phone").toString());
					// 案例状态
					String case_state = params.get("case_state") == null ? "0"
							: params.get("case_state").toString();
					caseInfoModel.setCase_state(Integer.parseInt(case_state));

					caseInfoModel.setCase_areacode(params.get("case_areacode")
							.toString());
					caseInfoModel.setCase_userid(params.get("case_userid")
							.toString());
					caseInfoModel.setReceiver_area(params.get("receiver_area")
							.toString());
					caseInfoModel.setCase_in_per(params.get("case_in_per")
							.toString());
					caseInfoModel
							.setReceiver_id(params.get("receiver_id") == null ? ""
									: params.get("receiver_id").toString());
					if (params.get("sample_code") != null) {
						caseInfoModel.setSample_in_time(DATE_FORMAT
								.format(new Date()));
					}
					caseInfoModel.setSample_in_per(params.get("sample_in_per")
							.toString());

					caseInfoModel.setCase_type(Integer.parseInt(case_type));
					// 紧急程度
					String urgent_state = params.get("urgent_state") == null ? "0"
							: params.get("urgent_state").toString();
					caseInfoModel.setUrgent_state(Integer
							.parseInt(urgent_state));
					String copies = params.get("copies") == null ? "2" : params
							.get("copies").toString();
					caseInfoModel.setCopies(Integer.parseInt(copies));

					caseInfoModel.setRemark(params.get("remark").toString());
					caseInfoModel.setLaboratory_no((String) params
							.get("laboratory_no"));
					caseInfoModel.setConfirm_code(params.get("confirm_code")
							.toString());
					caseInfoModel.setParnter_name(params.get("parnter_name")
							.toString());
					caseInfoModel.setSource_type(params.get("source_type")
							.toString());
					// 判断PC端还是手机端
					if (params.get("sign") == null) {
						caseInfoModel.setIs_new(1);
					} else {
						caseInfoModel.setIs_new(0);
					}
					// 判断医学模版其他新增直接到邮寄状态
					// if ("1".equals(case_type) && "".equals(report_model))
					// caseInfoModel.setVerify_state(8);

					/* 案例流程增加 start */
					try {
						identityService
								.setAuthenticatedUserId(user.getUserid());
						ProcessInstance processInstance = runtimeService
								.startProcessInstanceByKey("processJudicial",
										caseInfoModel.getCase_id());
						caseInfoModel.setProcess_instance_id(processInstance
								.getId());
					} finally {
						identityService.setAuthenticatedUserId(null);
					}
					/* 案例流程增加 end */

					// 插入新增案例实体
					case_result = RdsJudicialRegisterMapper
							.insertCaseInfo(caseInfoModel);

				} catch (Exception e) {
					e.printStackTrace();
					// 异常回退案例编码
					if ("0".equals(params.get("case_type").toString())) {
						// 省内案例编号回退
						if (receiver_area.contains("江苏省")
								&& RdsJudicialRegisterMapper
										.selectProvinceUser(caseInfoModel
												.getCase_userid()) > 0
								&& params.get("report_model").toString()
										.startsWith("zyjd")) {
							String xml_time = XmlParseUtil.getXmlValue(
									XML_PATH, "case_code_date");
							String now_time = com.rds.code.utils.StringUtils
									.dateToSix(new Date());
							if (xml_time.equals(now_time)) {
								XmlParseUtil
										.updateXmlValue(
												XML_PATH,
												"case_code_num",
												String.valueOf(Integer.parseInt(XmlParseUtil
														.getXmlValue(XML_PATH,
																"case_code_num")) - 1));
							}
						} else
						// 省外案例编号回退
						{
							String xml_time = XmlParseUtil.getXmlValue(
									XML_PATH_1, "case_code_date");
							String now_time = com.rds.code.utils.StringUtils
									.dateToSix(new Date());
							if (xml_time.equals(now_time)) {
								XmlParseUtil
										.updateXmlValue(
												XML_PATH_1,
												"case_code_num",
												String.valueOf(Integer.parseInt(XmlParseUtil
														.getXmlValue(
																XML_PATH_1,
																"case_code_num")) - 1));
							}

						}
						// 医学案例编号回退
					} else {
						String xml_time = XmlParseUtil.getXmlValue(XML_PATH,
								"case_medical_date");
						String now_time = com.rds.code.utils.StringUtils
								.dateToSix(new Date());
						if (xml_time.equals(now_time)) {
							XmlParseUtil.updateXmlValue(XML_PATH,
									"case_medical_num", String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH,
													"case_medical_num")) - 1));
						}
					}
					result.put("result", false);
					result.put("success", true);
					result.put("message", "案例新增失败，请联系管理员！");
					return result;
				}

				/* 自动生成样本条形码 start */
				// List<String> sample_calls =
				// getValues(params.get("sample_call"));
				//
				// List<String> sample_codes = new ArrayList<String>();
				// sample_codes = getSampleCode(sample_calls, case_code);
				// params.put("sample_code", sample_codes);
				/* 自动生成样本条形码 end */

				// 案例实体插入成功，插入案例样本信息
				if (case_result > 0) {

					if (params.get("sample_code") != null) {
						// 上传照片
						try {

							List<String> sample_codes = getValues(params
									.get("sample_code"));
							List<String> sample_types = getValues(params
									.get("sample_type"));
							List<String> sample_usernames = getValues(params
									.get("sample_username"));
							List<String> id_numbers = getValues(params
									.get("id_number"));
							List<String> sample_calls = getValues(params
									.get("sample_call"));
							List<String> birth_dates = getValues(params
									.get("birth_date"));
							List<String> specials = getValues(params
									.get("special"));
							// 拆分子案例
							if (!"".equals(caseInfoModel.getReport_model())) {
								if (params.get("sign") == null) {
									addSubCaseInfo(sample_codes, sample_calls,
											params, UNKNOWN);
								} else {
									addSubCaseInfo(sample_codes, sample_calls,
											params, null);
								}
							}

							// 插入案例样本信息
							for (int i = 0; i < sample_codes.size(); i++) {

								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel();
								sampleInfoModel
										.setSample_id(UUIDUtil.getUUID());
								sampleInfoModel.setSample_code(sample_codes
										.get(i));
								sampleInfoModel.setSample_call(sample_calls
										.get(i));
								sampleInfoModel
										.setSample_username(sample_usernames
												.get(i));
								if (id_numbers.size() > 0)
									sampleInfoModel.setId_number(id_numbers
											.get(i));
								if (birth_dates.size() > 0)
									sampleInfoModel.setBirth_date(birth_dates
											.get(i));
								sampleInfoModel.setSample_type(sample_types
										.get(i));
								sampleInfoModel.setCase_id(caseInfoModel
										.getCase_id());
								sampleInfoModel.setSpecial(specials.get(i));
								// 插入样本信息
								RdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
							}
							// 判断如果委托人未填写，将样本名字作为委托人
							if (StringUtils.isEmpty(caseInfoModel.getClient())) {
								RdsJudicialRegisterMapper
										.updateClient(caseInfoModel);
							}
							/* 保存采样快递信息 start */
							if (!("".equals(params.get("express_num"))
									&& "".equals(params.get("express_type"))
									&& "".equals(params.get("express_concat")) && ""
										.equals(params.get("express_recive")))) {
								Map<String, Object> mapEpxress = new HashMap<String, Object>();
								mapEpxress.put("express_num",
										params.get("express_num"));
								mapEpxress.put("express_type",
										params.get("express_type"));
								mapEpxress.put("express_recive",
										params.get("express_recive"));
								mapEpxress.put("express_concat",
										params.get("express_concat"));
								mapEpxress.put("express_remark",
										params.get("express_remark"));
								mapEpxress.put("id", UUIDUtil.getUUID());
								mapEpxress.put("case_id",
										caseInfoModel.getCase_id());
								RdsJudicialRegisterMapper
										.insertSampleExpress(mapEpxress);
							}
							/* 保存采样快递信息 start */
							// 添加样本信息到组合表里
							addCaseSample(params);
							// 上传图片信息
							if (null != params.get("files")) {
								rdsJudicialCaseAttachmentService.upload(
										caseInfoModel.getCase_id(),
										(MultipartFile[]) params.get("files"),
										(int[]) params.get("filetype"),
										user.getUserid());
							}
						} catch (Exception e) {
							e.printStackTrace();
							// 异常回退案例编码
							if ("0".equals(params.get("case_type").toString())) {
								String xml_time = XmlParseUtil.getXmlValue(
										XML_PATH, "case_code_date");
								String now_time = com.rds.code.utils.StringUtils
										.dateToSix(new Date());
								if (xml_time.equals(now_time)) {
									XmlParseUtil
											.updateXmlValue(
													XML_PATH,
													"case_code_num",
													String.valueOf(Integer
															.parseInt(XmlParseUtil
																	.getXmlValue(
																			XML_PATH,
																			"case_code_num")) - 1));
								}
							} else {
								String xml_time = XmlParseUtil.getXmlValue(
										XML_PATH, "case_medical_date");
								String now_time = com.rds.code.utils.StringUtils
										.dateToSix(new Date());
								if (xml_time.equals(now_time)) {
									XmlParseUtil
											.updateXmlValue(
													XML_PATH,
													"case_medical_num",
													String.valueOf(Integer
															.parseInt(XmlParseUtil
																	.getXmlValue(
																			XML_PATH,
																			"case_medical_num")) - 1));
								}
							}
							result.put("result", false);
							result.put("success", true);
							result.put("message", "保存失败，请重新保存！");
							return result;
						}
						result.put("case_code", params.get("case_code"));
						result.put("result", true);
						result.put("success", true);
						result.put("message", "操作成功！");
					} else {
						result.put("result", false);
						result.put("success", true);
						result.put("message", "操作失败，请联系管理员！");
					}
					return result;
				}
				// 插入只登记案例
			} else {
				Map<String, Object> mapCode = new HashMap<>();
				mapCode.put("case_code", params.get("case_code"));
				if ((!"".equals(params.get("case_code")))
						&& RdsJudicialRegisterMapper.exsitCaseCode(mapCode) > 0) {
					result.put("result", false);
					result.put("success", true);
					result.put("message", "案例编码已存在!");
					return result;
				} else {
					// 插入不需要用系统出报告的案例
					try {
						/** 不出系统出报告案例生成统一流水号 start **/
						String xml1_time = XmlParseUtil.getXmlValue(XML_PATH_1,
								"company_code_date");
						String now1_time = com.rds.code.utils.StringUtils
								.dateToEight(new Date());
						DecimalFormat df = new DecimalFormat("00000");
						String company_code = "";
						if (xml1_time.equals(now1_time)) {
							company_code = "Z"
									+ now1_time
									+ df.format(Integer.parseInt(XmlParseUtil
											.getXmlValue(XML_PATH_1,
													"company_code_num")) + 1);
							XmlParseUtil.updateXmlValue(XML_PATH_1,
									"company_code_num", String.valueOf(Integer
											.parseInt(XmlParseUtil.getXmlValue(
													XML_PATH_1,
													"company_code_num")) + 1));
						} else {
							XmlParseUtil.updateXmlValue(XML_PATH_1,
									"company_code_date", now1_time);
							XmlParseUtil.updateXmlValue(XML_PATH_1,
									"company_code_num", "1");
							company_code = "Z"
									+ now1_time
									+ df.format(Integer.parseInt(XmlParseUtil
											.getXmlValue(XML_PATH_1,
													"company_code_num")));
						}
						params.put("company_code", company_code);
						RdsJudicialRegisterMapper
								.insertCompanyCodeCaseid(params);
						/** 不出系统出报告案例生成统一流水号 end **/

						insertNoReport(params, user);
						result.put("case_code", params.get("case_code"));
						result.put("result", true);
						result.put("success", true);
						result.put("message", "操作成功！");
					} catch (Exception e) {
						result.put("case_code", params.get("case_code"));
						result.put("result", false);
						result.put("success", true);
						result.put("message", "操作成功！");
					}
					return result;
				}
			}
			result.put("result", false);
			result.put("success", true);
			result.put("message", "操作失败，请联系管理员！");
			return result;
		} else {
			// 设置使用状态
			result.put("user_state", 1);
			result.put("confirm_code", params.get("confirm_code"));
			result.put("case_id", "");
			try {
				rdsFinanceChargeStandardService.updateCodeUsed(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.put("success", true);
			result.put("result", false);
			result.put("message", "案例保存失败，请查看该地区是否配置收费或联系管理员！");
			return result;
		}
	}

	/**
	 * 保存案例信息
	 */
	@Override
	public Object saveZYCaseInfo(Map<String, Object> params,
			RdsUpcUserModel user) {

		// 案例返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		// 案例优惠金额
		int discount_amount = 0;
		// 案例类型(司法：0，医学：1)
		String case_type = params.get("case_type") == null ? "0" : params.get(
				"case_type").toString();

		// 判断二次采样情况下插入非系统生成案例编号关联保存
		Map<String, Object> map = new HashMap<>();
		// 二次采样或补样时会存在
		map.put("case_code", params.get("case_code"));
		map.put("case_id", params.get("case_id").toString());

		// 判断没有案例状态情况下处理5：二次采样；6：补样
		if (null != params.get("case_state")) {
			if (!"5".equals(params.get("case_state").toString())
					&& !"6".equals(params.get("case_state").toString()))
				// 非补样或二次采样案例状态正常，可能是累赘，以防万一
				params.put("case_state", 0);
			else {
				map.put("case_state", params.get("case_state"));
				if ("5".equals(params.get("case_state").toString())) {
					// 判断该案例是否已存在二次采样案例
					if (RdsJudicialRegisterMapper.queryCaseCodeSecond(map)
							.size() > 0) {
						result.put("result", false);
						result.put("success", true);
						result.put("message", "该案例已存在二次采样案例！");
						return result;
					}
				}
			}
		} else {
			params.put("case_state", '0');
		}
		// 判断是否填写激活码，判断案例是否为优惠或免单的情况
		if (null != params.get("confirm_code")
				&& !"".equals(params.get("confirm_code"))) {
			try {
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("confirm_code", params.get("confirm_code"));
				// 根据优惠码查询到对应的优惠信息
				List<RdsFinanceSpecialModel> model = rdsFinanceChargeStandardService
						.queryAllSpecialFinance(map1);
				if (model.size() == 0) {
					result.put("success", true);
					result.put("result", false);
					result.put("message", "优惠码有误，请查看！");
					return result;
				}
				// 写入对应状态到案例中
				params.put("case_state", model.get(0).getCase_state());
				// 状态为优惠申请
				if ("3".equals(model.get(0).getCase_state())) {
					// 判断该激活码是否被使用
					if ("2".equals(model.get(0).getUser_state())) {
						result.put("result", false);
						result.put("success", true);
						result.put("message", "该优惠码已使用，请查看！");
						return result;
					}
					// 否则查询出优惠金额
					discount_amount = Integer.parseInt(model.get(0)
							.getDiscount_amount());

					// 设置使用状态
					result.put("user_state", 2);
					result.put("confirm_code", params.get("confirm_code"));
					result.put("case_id", params.get("case_id"));
					rdsFinanceChargeStandardService.updateCodeUsed(result);

				}
				// 该优惠码为免单
				else if ("2".equals(model.get(0).getCase_state())) {
					// 判断该激活码是否被使用
					if ("2".equals(model.get(0).getUser_state())) {
						result.put("result", false);
						result.put("success", true);
						result.put("message", "该优惠码已使用，请查看！");
						return result;
					}
					// 设置使用状态
					result.put("user_state", 2);
					result.put("confirm_code", params.get("confirm_code"));
					result.put("case_id", params.get("case_id"));
					rdsFinanceChargeStandardService.updateCodeUsed(result);
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("result", false);
				result.put("success", true);
				result.put("message", "新增有误，请联系管理员！");
				return result;
			}
		}
		// 财务状态默认置0：日结
		params.put("type", "0");
		try {
			// 判断该登记案例是否为先出报告后付款或月结
			Map<String, Object> map2 = new HashMap<String, Object>();
			// 申请人
			map2.put("monthly_per", params.get("case_userid"));
			// 结算区域
			map2.put("monthly_area", params.get("case_areacode"));
			// 案例类型
			map2.put("case_type", "dna");
			// 判断该登记案例是否为先出报告后付款或月结
			List<RdsFinanceSpecialModel> model = rdsFinanceChargeStandardService
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
					}
					if ("5".equals(params.get("case_state").toString())) {
						params.put(
								"remark",
								params.get("remark") == null ? ("该案例为"
										+ params.get("case_code") + "的二次采样案例")
										: (params.get("remark").toString()
												+ ";该案例为"
												+ params.get("case_code") + "的二次采样案例"));
					} else if ("6".equals(params.get("case_state").toString())) {
						params.put(
								"remark",
								params.get("remark") == null ? ("该案例为"
										+ params.get("case_code") + "补样案例")
										: (params.get("remark").toString()
												+ ";该案例为"
												+ params.get("case_code") + "的补样案例"));
						// params.put("case_state",
						// model.get(0).getCase_state());
					}
					// else
					// {
					// params.put("case_state", model.get(0).getCase_state());
					// }
					// 财务置月结状态
					if ("4".equals(model.get(0).getCase_state())) {
						params.put("type", model.get(0).getCase_state());
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
			result.put("message", "新增有误，请联系管理员！");
			return result;
		}
		// 案例实验室
		String laboratory_no = "";
		// 判断该案例是否合作商案例，合作商案例则匹配合作商实验室编号，否则为子渊实验室编号
		if (null == params.get("parnter_name")
				|| "".equals(params.get("parnter_name")))
			laboratory_no = rdsUpcCompanyService.queryLaboratoryNo(user
					.getCompanyid());// 根据公司编号获取实验室ID
		else {
			try {
				// 给合作商配置的实验室编号
				laboratory_no = rdsUpcPartnerConfigService
						.getLaboratoryNo(params.get("parnter_name").toString());
			} catch (Exception e) {
				laboratory_no = "";
				e.printStackTrace();
			}
		}
		// }
		// 实验室编号插入
		params.put("laboratory_no", laboratory_no);

		// 添加财务类型（司法和医学案例）
		params.put("finance_type",
				params.get("case_type").equals("0") ? "亲子鉴定-司法" : "亲子鉴定-医学");
		// 项目类型：亲子鉴定
		// params.put("program_type", 0);
		// 优惠金额
		params.put("discount_amount", discount_amount);

		// 添加财务信息
		if (rdsCaseFinanceService.addCaseFee(params)) {
			// String receiver_area = params.get("receiver_area").toString();
			/** 案例编号生成 start **/
			String report_model = (params.get("report_model") == null) ? ""
					: params.get("report_model").toString();
			// 判断二次采样案例编号加_2
			if ("5".equals(params.get("case_state"))) {
				params.put("case_code", params.get("case_code").toString()
						+ "_2");
			} else {
				// 系统生成新的案例编号
				// String case_code = getCaseCode(receiver_area,
				// case_type, report_model, params.get("case_userid")
				// .toString(), params
				// .get("case_username")==null?"":params
				// .get("case_username").toString());
				params.put("case_code", "");
			}
			// 判断是否为二次采样或补样，插入关联关系
			if ("5".equals(params.get("case_state").toString())
					|| "6".equals(params.get("case_state").toString())) {
				map.put("id", UUIDUtil.getUUID());
				map.put("case_code_second", params.get("case_code"));
				RdsJudicialRegisterMapper.insertCaseCodeSecond(map);
			}

			/** 案例编号生成 end **/
			/*
			 * 案例编号生成 start Calendar c = Calendar.getInstance(); Map<String,
			 * Object> map = new HashMap<String, Object>(); map.put("id",
			 * params.get("case_id")); map.put("letter", "Z"); map.put("year",
			 * c.get(Calendar.YEAR)); map.put("month", c.get(Calendar.MONTH) < 9
			 * ? "0" + (c.get(Calendar.MONTH) + 1) : c .get(Calendar.MONTH) +
			 * 1); String num =
			 * RdsJudicialRegisterMapper.queryCaseCodeNum(map).get(0)
			 * .get("num").toString(); map.put("num", num);
			 * RdsJudicialRegisterMapper.insertCaseCodeNum(map); String
			 * case_code = map.get("letter").toString() + map.get("year") +
			 * map.get("month") + num;
			 */

			// params.put("case_code", params.get("case_code"));
			/* 案例编号生成 end */

			/** 生成案例统一流水号 start **/
			String xml1_time = XmlParseUtil.getXmlValue(XML_PATH_1,
					"company_code_date");
			String now1_time = com.rds.code.utils.StringUtils
					.dateToEight(new Date());
			DecimalFormat df = new DecimalFormat("00000");
			String company_code = "";
			if (xml1_time.equals(now1_time)) {
				company_code = "Z"
						+ now1_time
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH_1, "company_code_num")) + 1);
				XmlParseUtil
						.updateXmlValue(XML_PATH_1, "company_code_num", String
								.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_1,
												"company_code_num")) + 1));
			} else {
				XmlParseUtil.updateXmlValue(XML_PATH_1, "company_code_date",
						now1_time);
				XmlParseUtil
						.updateXmlValue(XML_PATH_1, "company_code_num", "1");
				company_code = "Z"
						+ now1_time
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH_1, "company_code_num")));
			}
			params.put("company_code", company_code);
			RdsJudicialRegisterMapper.insertCompanyCodeCaseid(params);
			/** 生成案例统一流水号 end **/

			// 判断案例新增成功状态
			int case_result = 0;
			// 新增案例实体
			RdsJudicialCaseInfoModel caseInfoModel = new RdsJudicialCaseInfoModel();
			try {
				caseInfoModel.setCase_id(params.get("case_id").toString());
				caseInfoModel.setCase_code(params.get("case_code").toString());
				caseInfoModel.setAccept_time(params.get("accept_time")
						.toString());
				caseInfoModel.setConsignment_time(params
						.get("consignment_time").toString());
				caseInfoModel.setReport_model(report_model);
				caseInfoModel.setUnit_type(params.get("unit_type").toString());
				// 单双亲类型
				caseInfoModel.setTypeid(params.get("typeid").toString());
				// 亲属关系
				String sample_relation = params.get("sample_relation") == null ? "0"
						: params.get("sample_relation").toString();
				caseInfoModel.setSample_relation(Integer
						.parseInt(sample_relation));
				// 委托人
				caseInfoModel.setClient(params.get("client").toString());
				caseInfoModel.setPhone(params.get("phone").toString());
				// 案例状态
				String case_state = params.get("case_state") == null ? "0"
						: params.get("case_state").toString();
				caseInfoModel.setCase_state(Integer.parseInt(case_state));

				caseInfoModel.setCase_areacode(params.get("case_areacode")
						.toString());
				caseInfoModel.setCase_userid(params.get("case_userid")
						.toString());
				caseInfoModel.setReceiver_area(params.get("receiver_area")
						.toString());
				caseInfoModel.setCase_in_per(params.get("case_in_per")
						.toString());
				caseInfoModel.setReceiver_id(params.get("receiver_id")
						.toString());
				caseInfoModel.setPurpose(params.get("purpose")
						.toString());
				if (params.get("sample_code") != null) {
					caseInfoModel.setSample_in_time(DATE_FORMAT
							.format(new Date()));
				}
				caseInfoModel.setSample_in_per(params.get("sample_in_per")
						.toString());

				caseInfoModel.setCase_type(Integer.parseInt(case_type));
				// 紧急程度
				String urgent_state = params.get("urgent_state") == null ? "0"
						: params.get("urgent_state").toString();
				caseInfoModel.setUrgent_state(Integer.parseInt(urgent_state));
				String copies = params.get("copies") == null ? "2" : params
						.get("copies").toString();
				caseInfoModel.setCopies(Integer.parseInt(copies));

				caseInfoModel.setRemark(params.get("remark").toString());
				caseInfoModel.setLaboratory_no((String) params
						.get("laboratory_no"));
				caseInfoModel.setConfirm_code(params.get("confirm_code")
						.toString());
				caseInfoModel.setParnter_name(params.get("parnter_name")
						.toString());
				caseInfoModel.setSource_type(params.get("source_type")
						.toString());
				// 判断PC端还是手机端
				if (params.get("sign") == null) {
					caseInfoModel.setIs_new(1);
				} else {
					caseInfoModel.setIs_new(0);
				}

				/* 案例流程增加 start */
				try {
					identityService.setAuthenticatedUserId(user.getUserid());
					ProcessInstance processInstance = runtimeService
							.startProcessInstanceByKey("processJudicial",
									caseInfoModel.getCase_id());
					caseInfoModel.setProcess_instance_id(processInstance
							.getId());
				} finally {
					identityService.setAuthenticatedUserId(null);
				}
				/* 案例流程增加 end */

				// 插入新增案例实体
				case_result = RdsJudicialRegisterMapper
						.insertCaseInfo(caseInfoModel);

			} catch (Exception e) {
				e.printStackTrace();
				result.put("result", false);
				result.put("success", true);
				result.put("message", "案例新增失败，请联系管理员！");
				return result;
			}

			/* 自动生成样本条形码 start */
			// List<String> sample_calls =
			// getValues(params.get("sample_call"));
			//
			// List<String> sample_codes = new ArrayList<String>();
			// sample_codes = getSampleCode(sample_calls, case_code);
			// params.put("sample_code", sample_codes);
			/* 自动生成样本条形码 end */

			// 案例实体插入成功，插入案例样本信息
			if (case_result > 0) {

				if (params.get("sample_code") != null) {
					// 上传照片
					try {

						List<String> sample_codes = getValues(params
								.get("sample_code"));
						List<String> sample_types = getValues(params
								.get("sample_type"));
						List<String> sample_usernames = getValues(params
								.get("sample_username"));
						List<String> id_numbers = getValues(params
								.get("id_number"));
						List<String> sample_calls = getValues(params
								.get("sample_call"));
						List<String> birth_dates = getValues(params
								.get("birth_date"));
						List<String> addresses = getValues(params
								.get("address"));
						List<String> specials = getValues(params.get("special"));
						// 拆分子案例
						// if (params.get("sign") == null) {
						// addSubCaseInfo(sample_codes, sample_calls,
						// params, UNKNOWN);
						// } else {
						// addSubCaseInfo(sample_codes, sample_calls,
						// params, null);
						// }

						// 插入案例样本信息
						for (int i = 0; i < sample_codes.size(); i++) {

							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel();
							sampleInfoModel.setSample_id(UUIDUtil.getUUID());
							sampleInfoModel.setSample_code(sample_codes.get(i));
							sampleInfoModel.setSample_call(sample_calls.get(i));
							sampleInfoModel.setSample_username(sample_usernames
									.get(i));
							sampleInfoModel.setId_number(id_numbers.get(i));
							sampleInfoModel.setBirth_date(birth_dates.get(i));
                            sampleInfoModel.setAddress(addresses.get(i));
							sampleInfoModel.setSample_type(sample_types.get(i));
							sampleInfoModel.setCase_id(caseInfoModel
									.getCase_id());
							sampleInfoModel.setSpecial(specials.get(i));
							// 插入样本信息
							RdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
						}
						// 判断如果委托人未填写，将样本名字作为委托人
						if (StringUtils.isEmpty(caseInfoModel.getClient())) {
							RdsJudicialRegisterMapper
									.updateClient(caseInfoModel);
						}
						/* 保存采样快递信息 start */
						if (!("".equals(params.get("express_num"))
								&& "".equals(params.get("express_type"))
								&& "".equals(params.get("express_concat")) && ""
									.equals(params.get("express_recive")))) {
							Map<String, Object> mapEpxress = new HashMap<String, Object>();
							mapEpxress.put("express_num",
									params.get("express_num"));
							mapEpxress.put("express_type",
									params.get("express_type"));
							mapEpxress.put("express_recive",
									params.get("express_recive"));
							mapEpxress.put("express_concat",
									params.get("express_concat"));
							mapEpxress.put("express_remark",
									params.get("express_remark"));
							mapEpxress.put("id", UUIDUtil.getUUID());
							mapEpxress.put("case_id",
									caseInfoModel.getCase_id());
							RdsJudicialRegisterMapper
									.insertSampleExpress(mapEpxress);
						}
						/* 保存采样快递信息 start */
						// 添加样本信息到组合表里
						addCaseSample(params);
						// 上传图片信息
						if (null != params.get("files")) {
							rdsJudicialCaseAttachmentService.upload(
									caseInfoModel.getCase_id(),
									(MultipartFile[]) params.get("files"),
									(int[]) params.get("filetype"),
									user.getUserid());
							// rdsJudicialCaseAttachmentService.upload(params
							// .get("case_code").toString(),
							// (MultipartFile[]) params.get("files"),
							// (int[]) params.get("filetype"), user
							// .getUserid());
						}
					} catch (Exception e) {
						e.printStackTrace();
						// 异常回退案例编码
						if ("0".equals(params.get("case_type").toString())) {
							String xml_time = XmlParseUtil.getXmlValue(
									XML_PATH, "case_code_date");
							String now_time = com.rds.code.utils.StringUtils
									.dateToSix(new Date());
							if (xml_time.equals(now_time)) {
								XmlParseUtil
										.updateXmlValue(
												XML_PATH,
												"case_code_num",
												String.valueOf(Integer.parseInt(XmlParseUtil
														.getXmlValue(XML_PATH,
																"case_code_num")) - 1));
							}
						} else {
							String xml_time = XmlParseUtil.getXmlValue(
									XML_PATH, "case_medical_date");
							String now_time = com.rds.code.utils.StringUtils
									.dateToSix(new Date());
							if (xml_time.equals(now_time)) {
								XmlParseUtil
										.updateXmlValue(
												XML_PATH,
												"case_medical_num",
												String.valueOf(Integer.parseInt(XmlParseUtil
														.getXmlValue(XML_PATH,
																"case_medical_num")) - 1));
							}
						}
						result.put("result", false);
						result.put("success", true);
						result.put("message", "保存失败，请重新保存！");
						return result;
					}
					result.put("case_code", params.get("case_code"));
					result.put("result", true);
					result.put("success", true);
					result.put("message", "操作成功！");
				} else {
					result.put("result", false);
					result.put("success", true);
					result.put("message", "操作失败，请联系管理员！");
				}
				return result;
			}
			result.put("result", false);
			result.put("success", true);
			result.put("message", "操作失败，请联系管理员！");
			return result;
		} else {
			// 设置使用状态为未使用
			result.put("user_state", 1);
			result.put("confirm_code", params.get("confirm_code"));
			result.put("case_id", "");
			try {
				rdsFinanceChargeStandardService.updateCodeUsed(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.put("success", true);
			result.put("result", false);
			result.put("message", "案例保存失败，请查看该地区是否配置收费或联系管理员！");
			return result;
		}
	}

	/**
	 * 修改案例信息
	 */
	@Override
	public Object updateCaseInfo(Map<String, Object> params,
			RdsUpcUserModel user) {

		// 案例实验室
		String laboratory_no = "";
		String case_id = params.get("case_id").toString();
		// 案例优惠金额
		int discount_amount = 0;
		// 案例状态（正常，优惠，先出报告后付款，月结，免单）
		// params.put("case_state", 0);
		// 案例返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		// 判断是否填写激活码，判断案例是否为优惠或免单的情况
		if (null != params.get("confirm_code")
				&& !"".equals(params.get("confirm_code"))) {
			try {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("confirm_code", params.get("confirm_code"));
				// 根据优惠码查询到对应的优惠信息
				List<RdsFinanceSpecialModel> model = rdsFinanceChargeStandardService
						.queryAllSpecialFinance(map);
				if (model.size() == 0) {
					result.put("result", false);
					result.put("message", "优惠码有误，请查看！");
					return result;
				}
				// 判断是否同一个案例同一个激活码
				if (case_id.equals(model.get(0).getCase_id())) {
					if ("3".equals(model.get(0).getCase_state()))
						discount_amount = Integer.parseInt(model.get(0)
								.getDiscount_amount());
					params.put("case_state", model.get(0).getCase_state());
				} else {
					// 写入对应状态到案例中
					params.put("case_state", model.get(0).getCase_state());
					// 状态为优惠申请
					if ("3".equals(model.get(0).getCase_state())) {
						// 判断该激活码是否被使用
						if ("2".equals(model.get(0).getUser_state())) {
							if (!model.get(0).getCase_id()
									.equals(params.get("case_id"))) {
								result.put("result", false);
								result.put("message", "该优惠码已使用，请查看！");
								return result;
							}
						}
						// 否则查询出优惠金额
						discount_amount = Integer.parseInt(model.get(0)
								.getDiscount_amount());

						// 设置使用状态
						result.put("user_state", 2);
						result.put("confirm_code", params.get("confirm_code"));
						result.put("case_id", params.get("case_id"));
						rdsFinanceChargeStandardService.updateCodeUsed(result);

					}
					// 该优惠码为免单
					else if ("2".equals(model.get(0).getCase_state())) {
						// 判断该激活码是否被使用
						if ("2".equals(model.get(0).getUser_state())) {
							result.put("result", false);
							result.put("message", "该优惠码已使用，请查看！");
							return result;
						}
						// 设置使用状态
						result.put("user_state", 2);
						result.put("confirm_code", params.get("confirm_code"));
						result.put("case_id", params.get("case_id"));
						rdsFinanceChargeStandardService.updateCodeUsed(result);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				result.put("result", false);
				result.put("message", "新增有误，请联系管理员！");
				return result;
			}
		}
		params.put("type", "0");
		try {
			// 判断该登记案例是否为先出报告后付款或月结
			Map<String, Object> map = new HashMap<String, Object>();
			// 申请人
			map.put("monthly_per", params.get("case_userid"));
			// 结算区域
			map.put("monthly_area", params.get("case_areacode"));
			// 判断该登记案例是否为先出报告后付款或月结
			List<RdsFinanceSpecialModel> model = rdsFinanceChargeStandardService
					.queryAllSpecialFinance(map);
			// 存在则说明该案例属于月结或先出报告后付款区域
			if (model.size() > 0) {
				if ("1".equals(model.get(0).getCase_state())
						|| "4".equals(model.get(0).getCase_state())) {
					// 判断该激活码是否被失效
					if ("3".equals(model.get(0).getUser_state())) {
						result.put("result", false);
						result.put("message", "该地区配置已失效，请查看！");
						return result;
					}

					if ("5".equals(params.get("case_state").toString())) {
						params.put(
								"remark",
								params.get("remark") == null ? ("该案例为"
										+ params.get("case_code") + "的二次采样案例")
										: (params.get("remark").toString()
												+ ";该案例为"
												+ params.get("case_code") + "的二次采样案例"));
					}
					if ("6".equals(params.get("case_state").toString())) {
						params.put(
								"remark",
								params.get("remark") == null ? ("该案例为"
										+ params.get("case_code") + "补样案例")
										: (params.get("remark").toString()
												+ ";该案例为"
												+ params.get("case_code") + "的补样案例"));
					}
					// params.put("case_state", model.get(0).getCase_state());
					if ("4".equals(model.get(0).getCase_state())) {
						params.put("type", model.get(0).getCase_state());
					}

				}

				// 判断改区域是否出现预期未付款的情况
				if ("3".equals(model.get(0).getSettlement_state())) {
					result.put("result", false);
					result.put("message", "存在逾期未结算情况，请联系管理员！");
					return result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("message", "新增有误，请联系管理员！");
			return result;
		}
		// 判断该案例是否合作商案例，合作商案例则匹配合作商实验室编号，否则为子渊实验室编号
		if (null == params.get("parnter_name")
				|| "".equals(params.get("parnter_name")))
			laboratory_no = rdsUpcCompanyService.queryLaboratoryNo(user
					.getCompanyid());// 根据公司编号获取实验室ID
		else {
			try {
				// 给合作商配置的实验室编号
				laboratory_no = rdsUpcPartnerConfigService
						.getLaboratoryNo(params.get("parnter_name").toString());
			} catch (Exception e) {
				laboratory_no = "";
				e.printStackTrace();
			}
		}
		// }
		// 实验室插入
		params.put("laboratory_no", laboratory_no);

		// 添加财务类型（司法和医学案例）
		params.put("finance_type",
				params.get("case_type").equals("0") ? "亲子鉴定-司法" : "亲子鉴定-医学");
		// 项目类型：亲子鉴定
		// params.put("program_type", 0);
		// 优惠金额
		params.put("discount_amount", discount_amount);

		// 更新财务信息
		if (rdsCaseFinanceService.updateCaseFee(params)) {
			if (null != params.get("report_model")) {
				// 案例修改标识
				params.put("updateFlag", true);

				RdsJudicialCaseInfoModel caseInfoModel = new RdsJudicialCaseInfoModel();
				caseInfoModel.setCase_id((params.get("case_id").toString()));
				caseInfoModel.setCase_code(params.get("case_code").toString());
				caseInfoModel.setAccept_time(params.get("accept_time")
						.toString());
				caseInfoModel.setConsignment_time(params
						.get("consignment_time").toString());
				caseInfoModel.setReport_model(params.get("report_model")
						.toString());
				caseInfoModel.setUnit_type(params.get("unit_type").toString());
				caseInfoModel.setTypeid(params.get("typeid").toString());
				// 亲属关系
				String sample_relation = params.get("sample_relation") == null ? "0"
						: params.get("sample_relation").toString();
				caseInfoModel.setSample_relation(Integer
						.parseInt(sample_relation));
				caseInfoModel.setClient(params.get("client").toString());
				caseInfoModel.setPhone(params.get("phone").toString());
				// 案例状态
				String case_state = params.get("case_state") == null ? "0"
						: params.get("case_state").toString();
				caseInfoModel.setCase_state(Integer.parseInt(case_state));
				caseInfoModel.setCase_areacode(params.get("case_areacode")
						.toString());
				caseInfoModel.setCase_userid(params.get("case_userid")
						.toString());
				caseInfoModel.setReceiver_area(params.get("receiver_area")
						.toString());
				caseInfoModel.setCase_in_per(params.get("case_in_per")
						.toString());
				caseInfoModel.setPurpose(params.get("purpose") == null ? ""
						: params.get("purpose").toString());
				caseInfoModel
						.setReceiver_id(params.get("receiver_id") == null ? ""
								: params.get("receiver_id").toString());
				if (params.get("sample_code") != null) {
					caseInfoModel.setSample_in_time(DATE_FORMAT
							.format(new Date()));
				}
				caseInfoModel.setSample_in_per(params.get("sample_in_per")
						.toString());
				// 案例类型
				String case_type = params.get("case_type") == null ? "0"
						: params.get("case_type").toString();
				caseInfoModel.setCase_type(Integer.parseInt(case_type));
				// 紧急程度
				String urgent_state = params.get("urgent_state") == null ? "0"
						: params.get("urgent_state").toString();
				caseInfoModel.setUrgent_state(Integer.parseInt(urgent_state));
				String copies = params.get("copies") == null ? "2" : params
						.get("copies").toString();
				caseInfoModel.setCopies(Integer.parseInt(copies));
				caseInfoModel.setRemark(params.get("remark").toString());
				caseInfoModel.setLaboratory_no((String) params
						.get("laboratory_no"));
				caseInfoModel.setConfirm_code(params.get("confirm_code")
						.toString());
				caseInfoModel.setParnter_name(params.get("parnter_name")
						.toString());
				caseInfoModel.setVerify_state(0);
				caseInfoModel.setSource_type(params.get("source_type")
						.toString());
				// caseInfoModel.setAttach_need_case(Integer.valueOf(params.get(
				// "attach_need").toString()));
				// 更新案例基本信息
				int case_result = RdsJudicialRegisterMapper
						.updateCaseInfo(caseInfoModel);
				// 更新案例统一流水号
				RdsJudicialRegisterMapper.updateCompanyCodeCaseid(params);
				if (case_result > 0) {
					/* 自动生成样本条形码 start */
					// List<String> sample_calls = getValues(params
					// .get("sample_call"));
					//
					// List<String> sample_codes = new ArrayList<String>();
					// sample_codes = getSampleCode(sample_calls,
					// params.get("case_code").toString());
					// params.put("sample_code", sample_codes);
					/* 自动生成样本条形码 end */

					// 删除样本信息
					deleteSampleInfo(params);
					// 重新插入子案例和样本信息
					if (params.get("sample_code") != null) {
						List<String> sample_codes = getValues(params
								.get("sample_code"));
						List<String> sample_types = getValues(params
								.get("sample_type"));
						List<String> sample_usernames = getValues(params
								.get("sample_username"));
						List<String> id_numbers = getValues(params
								.get("id_number"));
						List<String> sample_calls = getValues(params
								.get("sample_call"));
						List<String> birth_dates = getValues(params
								.get("birth_date"));
						List<String> addresses = getValues(params
								.get("address"));
						List<String> specials = getValues(params.get("special"));

						if (params.get("sign") == null) {
							// 修改子案例信息
							if ("1".equals(params.get("is_new").toString())) {
								addSubCaseInfo(sample_codes, sample_calls,
										params, UNKNOWN);
							} else {
								addSubCaseInfo(sample_codes, sample_calls,
										params, null);
							}
						}
						for (int i = 0; i < sample_codes.size(); i++) {
							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel();
							sampleInfoModel.setSample_id(UUIDUtil.getUUID());
							sampleInfoModel.setSample_code(sample_codes.get(i));
							sampleInfoModel.setSample_call(sample_calls.get(i));
							sampleInfoModel.setSample_username(sample_usernames
									.get(i));
							sampleInfoModel.setId_number(id_numbers.get(i));
							sampleInfoModel.setBirth_date(birth_dates.get(i));
							sampleInfoModel.setSample_type(sample_types.get(i));
							sampleInfoModel.setCase_id(caseInfoModel
									.getCase_id());
                            sampleInfoModel.setAddress(addresses.get(i));
							sampleInfoModel.setSpecial(specials.get(i));
							RdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
						}
						if (StringUtils.isEmpty(caseInfoModel.getClient())) {
							RdsJudicialRegisterMapper
									.updateClient(caseInfoModel);
						}
					}
					addCaseSample(params);
					result.put("result", true);
					result.put("message", "操作成功！");
					return result;
				}
			} else {
				// 插入不需要用系统出报告的案例
				if (updateNoReport(params, user)) {
					result.put("result", true);
					result.put("message", "操作成功！");
					return result;
				}
			}
			result.put("result", false);
			result.put("message", "案例模版有问题请联系管理员");
			return result;
		} else {
			result.put("result", false);
			result.put("message", "案例保存失败，请查看该地区是否配置收费或联系管理员！");
			return result;
		}
	}

	private String getCaseCode(String receiver_area, String case_type,
			String report_model, String userid, String case_username) {
		String case_code = "";
		if ("0".equals(case_type)) {
			// 省内案例编号生成
			if (receiver_area.contains("江苏省")
					&& RdsJudicialRegisterMapper.selectProvinceUser(userid) > 0
					&& report_model.startsWith("zyjd")) {
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
				} else {
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
							+ now_time.substring(2, 6)
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
							+ now_time.substring(2, 6)
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

	// /**
	// * 导出案例信息
	// */
	// @Override
	// public void exportCaseInfo(RdsJudicialParamsModel params,
	// HttpServletResponse response) {
	// String filename = "案例列表";
	// List<RdsJudicialCaseInfoModel> caseInfoModels = RdsJudicialRegisterMapper
	// .queryAllCaseInfo(params);
	// Object[] titles = { "案例编号", "案例条形码", "身份证地址", "案例归属地", "归属人", "紧急程度",
	// "打印状态", "案例审核状态", "样品审核状态", "快递信息", "样本信息", "受理日期", "截止日期",
	// "模板名称", "登记人", "样本登记日期", "是否废除" };
	// List<List<Object>> bodys = new ArrayList<List<Object>>();
	// for (int i = 0; i < caseInfoModels.size(); i++) {
	// List<Object> objects = new ArrayList<Object>();
	// List<RdsJudicialSampleInfoModel> sampleInfoModels =
	// RdsJudicialRegisterMapper
	// .getSampleInfo(caseInfoModels.get(i).getCase_id());
	// String sample_info = "";
	// for (RdsJudicialSampleInfoModel sampleInfoModel : sampleInfoModels) {
	// if (StringUtils.isNotEmpty(sampleInfoModel.getSample_code())) {
	// sample_info += sampleInfoModel.getSample_code();
	// }
	// if (StringUtils
	// .isNotEmpty(sampleInfoModel.getSample_callname())) {
	// sample_info += "_" + sampleInfoModel.getSample_callname();
	// }
	// if (StringUtils
	// .isNotEmpty(sampleInfoModel.getSample_username())) {
	// sample_info += "_" + sampleInfoModel.getSample_username();
	// }
	// if (StringUtils.isNotEmpty(sampleInfoModel.getSample_type())) {
	// sample_info += "_" + sampleInfoModel.getSample_typename();
	// }
	// if (StringUtils.isNotEmpty(sampleInfoModel.getId_number())) {
	// sample_info += "_" + sampleInfoModel.getId_number();
	// }
	// if (StringUtils.isNotEmpty(sampleInfoModel.getBirth_date())) {
	// sample_info += "_" + sampleInfoModel.getBirth_date();
	// }
	// sample_info += ";";
	// }
	// objects.add(caseInfoModels.get(i).getCase_id());
	// objects.add(caseInfoModels.get(i).getCase_code());
	// objects.add(caseInfoModels.get(i).getCase_areaname());
	// objects.add(caseInfoModels.get(i).getReceiver_area());
	// objects.add(caseInfoModels.get(i).getCase_receiver());
	// if (caseInfoModels.get(i).getUrgent_state() == 0) {
	// objects.add("普通");
	// } else if (caseInfoModels.get(i).getUrgent_state() == 1) {
	// objects.add("紧急");
	// } else {
	// objects.add("");
	// }
	// objects.add("可以打印" + caseInfoModels.get(i).getPrint_copies()
	// + "次,已经打印了" + caseInfoModels.get(i).getPrint_count() + "次");
	// String baseinfo_state = "";
	// if (caseInfoModels.get(i).getBaseinfo_state() == 1) {
	// baseinfo_state = "通过";
	// } else if (caseInfoModels.get(i).getBaseinfo_state() == 2) {
	// baseinfo_state = "未通过";
	// } else {
	// baseinfo_state = "未审核";
	// }
	// String sampleinfo_state = "";
	// if (caseInfoModels.get(i).getSampleinfo_state() == 1) {
	// sampleinfo_state = "通过";
	// } else if (caseInfoModels.get(i).getSampleinfo_state() == 2) {
	// sampleinfo_state = "未通过";
	// } else {
	// sampleinfo_state = "未审核";
	// }
	// objects.add(baseinfo_state);
	// objects.add(sampleinfo_state);
	// Map<String, Object> maps = new HashMap<String, Object>();
	// maps.put("case_id", caseInfoModels.get(i).getCase_id());
	// List<RdsJudicialMailInfoModel> mailInfoModels = null;
	// String mailInfo = "";
	// for (RdsJudicialMailInfoModel mailInfoModel : mailInfoModels) {
	// mailInfo += mailInfoModel.getMail_typename() + "_"
	// + mailInfoModel.getMail_code() + ";";
	// }
	// if (mailInfo != "") {
	// mailInfo = mailInfo.substring(0, mailInfo.length() - 1);
	// }
	// objects.add(mailInfo);
	// if (sample_info != "") {
	// sample_info = sample_info
	// .substring(0, sample_info.length() - 1);
	// }
	// objects.add(sample_info);
	// objects.add(caseInfoModels.get(i).getAccept_time());
	// objects.add(caseInfoModels.get(i).getClose_time());
	// objects.add(caseInfoModels.get(i).getReport_modelname());
	// objects.add(caseInfoModels.get(i).getCase_in_pername());
	// objects.add(caseInfoModels.get(i).getSample_in_time());
	// if (caseInfoModels.get(i).getIs_delete() == 0) {
	// objects.add("未废除");
	// } else {
	// objects.add("已废除");
	// }
	// bodys.add(objects);
	// }
	// ExportUtils.export(response, filename, titles, bodys,"案例信息"+
	// DateUtils.Date2String(new Date()));
	// }

	/**
	 * 导出样本信息
	 */
	@Override
	public void exportSampleInfo(RdsJudicialParamsModel params,
			HttpServletResponse response) {
		String filename = "样本列表";
		// 根据查询条件查询案例信息
		List<RdsJudicialCaseInfoModel> caseInfoModels = RdsJudicialRegisterMapper
				.queryAllCaseInfo(params);
		List<RdsJudicialSampleInfoModel> parentSampleInfoslist = RdsJudicialRegisterMapper
				.queryParentSampleInfoList(caseInfoModels);
		List<RdsJudicialSampleInfoModel> childSampleInfoslist = RdsJudicialRegisterMapper
				.queryChildSampleInfoList(caseInfoModels);
		// excel表格列头
		Object[] titles = { "案例编号", "日期", "父母亲", "身份证", "孩子", "出生日期", "应收款项",
				"所付款项", "到款时间", "财务备注", "是否检验报告", "所属分公司", "员工名字", "备注和要求",
				"地址", "电话", "资料&照片", "样本数", "报告日期&快递", "送样本时间", "未出原因", "开票时间",
				"开票金额", "发票号码" };
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		// 循环案例列表拼装表格一行
		for (int i = 0; i < caseInfoModels.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			// 根据案例查询父母样本信息
			List<RdsJudicialSampleInfoModel> parentSampleInfos = new ArrayList<RdsJudicialSampleInfoModel>();
			for (RdsJudicialSampleInfoModel m : parentSampleInfoslist) {
				if (m.getCase_id().equals(caseInfoModels.get(i).getCase_id())) {
					parentSampleInfos.add(m);
				}
			}
			// List<RdsJudicialSampleInfoModel> parentSampleInfos =
			// RdsJudicialRegisterMapper
			// .queryParentSampleInfo(caseInfoModels.get(i).getCase_id());
			// if (parentSampleInfos.size()<1)
			// continue;
			// 根据案例查询孩子样本信息
			List<RdsJudicialSampleInfoModel> childSampleInfos = new ArrayList<RdsJudicialSampleInfoModel>();
			for (RdsJudicialSampleInfoModel m : childSampleInfoslist) {
				if (m.getCase_id().equals(caseInfoModels.get(i).getCase_id())) {
					childSampleInfos.add(m);
				}
			}
			// List<RdsJudicialSampleInfoModel> childSampleInfos =
			// RdsJudicialRegisterMapper
			// .queryChildSampleInfo(caseInfoModels.get(i).getCase_id());
			// 表格中父母样本信息列
			String sample_info_parent = "";
			// 身份证列
			String sample_info_id_number = "";
			// 孩子信息列
			String sample_info_child = "";
			// 孩子生日列
			String sample_info_child_birth = "";
			// 拼装列信息
			for (RdsJudicialSampleInfoModel sampleInfoModel : parentSampleInfos) {

				if (StringUtils
						.isNotEmpty(sampleInfoModel.getSample_callname())) {
					sample_info_parent += sampleInfoModel.getSample_callname();
				}
				if (StringUtils
						.isNotEmpty(sampleInfoModel.getSample_username())) {
					sample_info_parent += "-"
							+ sampleInfoModel.getSample_username();
				}
				if (StringUtils.isNotEmpty(sampleInfoModel.getSample_type())) {
					sample_info_parent += "-"
							+ sampleInfoModel.getSample_typename();
				}
				if (StringUtils.isNotEmpty(sampleInfoModel.getId_number())) {
					sample_info_id_number += sampleInfoModel.getId_number();
				}
				sample_info_parent += ";";
				sample_info_id_number += ";";
			}
			// 父母顺序排序，根据样本编号后面的 F （父亲） M（母亲）排，F在前M在后
			if (parentSampleInfos.size() > 1) {
				// 比较器
				Collections.sort(parentSampleInfos,
						new Comparator<RdsJudicialSampleInfoModel>() {
							@Override
							public int compare(RdsJudicialSampleInfoModel o1,
									RdsJudicialSampleInfoModel o2) {
								if (o1.getSample_code()
										.substring(
												o1.getSample_code().length() - 1)
										.charAt(0) < o2
										.getSample_code()
										.substring(
												o2.getSample_code().length() - 1)
										.charAt(0))
									return -1;
								else
									return 1;
							}
						});
			}

			// 孩子顺序排序，根据样本编号后面的ABC排序
			if (childSampleInfos.size() > 1) {
				Collections.sort(childSampleInfos,
						new Comparator<RdsJudicialSampleInfoModel>() {

							@Override
							public int compare(RdsJudicialSampleInfoModel o1,
									RdsJudicialSampleInfoModel o2) {
								if (o1.getSample_code()
										.substring(
												o1.getSample_code().length() - 1)
										.charAt(0) < o2
										.getSample_code()
										.substring(
												o2.getSample_code().length() - 1)
										.charAt(0))
									return -1;
								else
									return 1;
							}
						});
			}
			// 拼装孩子列
			for (RdsJudicialSampleInfoModel sampleInfoModel : childSampleInfos) {
				if (StringUtils
						.isNotEmpty(sampleInfoModel.getSample_callname())) {
					sample_info_child += sampleInfoModel.getSample_callname();
				}
				if (StringUtils
						.isNotEmpty(sampleInfoModel.getSample_username())) {
					sample_info_child += "-"
							+ sampleInfoModel.getSample_username();
				}
				if (StringUtils.isNotEmpty(sampleInfoModel.getSample_type())) {
					sample_info_child += "-"
							+ sampleInfoModel.getSample_typename();
				}
				if (StringUtils.isNotEmpty(sampleInfoModel.getBirth_date())) {
					sample_info_child_birth += DateUtils
							.DateString2DateString(sampleInfoModel
									.getBirth_date());
				}
				sample_info_child += ";";
				sample_info_child_birth += ";";
			}
			// 案例编号
			objects.add(caseInfoModels.get(i).getCase_code());
			// 受理日期
			objects.add(DateUtils.DateString2DateString(caseInfoModels.get(i)
					.getAccept_time()));
			// 父母
			objects.add("".equals(sample_info_parent) ? "" : sample_info_parent
					.substring(0, sample_info_parent.length() - 1));
			// 身份证
			objects.add("".equals(sample_info_id_number) ? ""
					: sample_info_id_number.substring(0,
							sample_info_id_number.length() - 1));
			// 孩子
			objects.add("".equals(sample_info_child) ? "" : sample_info_child
					.substring(0, sample_info_child.length() - 1));
			// 生日
			objects.add("".equals(sample_info_child_birth) ? ""
					: sample_info_child_birth.subSequence(0,
							sample_info_child_birth.length() - 1));
			// 应收款项
			objects.add(caseInfoModels.get(i).getReturn_sum());
			// 所付款项
			objects.add(caseInfoModels.get(i).getReal_sum());
			// 到款时间
			objects.add(caseInfoModels.get(i).getFee_date());
			// 财务备注
			objects.add(caseInfoModels.get(i).getFee_remark());
			// 是否检验报告
			objects.add("zyjymodel".equals(caseInfoModels.get(i)
					.getReport_model()) ? "检验" : "");
			// 所属分公司
			String address = StringUtils.isEmpty(caseInfoModels.get(i)
					.getReceiver_area()) ? "" : caseInfoModels.get(i)
					.getReceiver_area() + "-";
			String name = StringUtils.isEmpty(caseInfoModels.get(i)
					.getCase_receiver()) ? "" : caseInfoModels.get(i)
					.getCase_receiver();
			objects.add(address + name);
			// 员工名字
			objects.add(name);
			// 备注和要求
			objects.add(caseInfoModels.get(i).getRemark());
			// 地址
			objects.add("");
			// 电话
			objects.add(caseInfoModels.get(i).getPhone());
			// 资料&照片
			objects.add("");
			// 样本数
			objects.add(childSampleInfos.size() + parentSampleInfos.size());
			// 报告日期&快递
			objects.add(StringUtils.isEmpty(caseInfoModels.get(i)
					.getMail_info()) ? "" : caseInfoModels.get(i)
					.getMail_info());
			// 送样本时间
			objects.add("");
			// 未出原因
			objects.add("");
			// 开票时间
			objects.add("");
			// 开票金额
			objects.add("");
			// 发票号码
			objects.add("");
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "亲子鉴定-宿迁子渊"
				+ DateUtils.Date2String(new Date()));
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
					subCase.setResult(UNKNOWN);
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
					subCase.setResult(UNKNOWN);
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

	/**
	 * 判断是否存在
	 */
	@Override
	public boolean exsitCaseCode(Map<String, Object> params) {
		if (params.get("case_code") != null) {
			params.put("case_code", params.get("case_code").toString().trim());
			int result = RdsJudicialRegisterMapper.exsitCaseCode(params);
			if (result == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否存在
	 */
	@Override
	public boolean exsitSampleCode(Map<String, Object> params) {
		int result = RdsJudicialRegisterMapper.exsitSampleCode(params);
		if (result > 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean verifyId_Number(Map<String, Object> params) {
		Pattern p = Pattern.compile("[0-9]+");
		if (params.get("id_number") != null) {
			String id_number = params.get("id_number").toString();
			int count = RdsJudicialRegisterMapper.exsitBlackNumber(id_number);// 判断此身份证是否存在黑名单里
			if (count > 0) {
				return false;
			} else {
				Matcher m = p.matcher(id_number.substring(0,
						id_number.length() - 1));// 判断前17位是否都是数字
				boolean flg = m.matches();
				if (flg) {
					if (id_number.length() == 18) {
						char c = com.rds.code.utils.StringUtils
								.getValidateCode(id_number.substring(0,
										id_number.length() - 1));
						if (id_number.endsWith(String.valueOf(c))) {// 审核最后一位是否为计算出的字符
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * 
	 */
	@Override
	public Map<String, String> getClient(Map<String, Object> params) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", RdsJudicialRegisterMapper.getClient(params));
		return map;
	}

	@Override
	public boolean returnCaseInfoState(Map<String, Object> params) {
		return RdsJudicialRegisterMapper.returnCaseInfoState(params);
	}

	/**
	 * 保存修改案例对应的样本组合
	 * 
	 * @param params
	 */
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
		List<String> sample_code = getValues(params.get("sample_code") == null ? ""
				: params.get("sample_code"));
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
				if (birth_dates.size() > 0)
					fm.setBirth_date(birth_dates.get(i));
				if (id_numbers.size() > 0)
					fm.setId_number(id_numbers.get(i));
				sampleInfoFM.add(fm);
			} else {
				RdsJudicialSampleInfoModel fm = new RdsJudicialSampleInfoModel();
				fm.setSample_call(modelCall == null ? "" : modelCall.getValue());
				fm.setSample_username(sample_usernames.get(i));
				fm.setSample_type(sample_type == null ? "" : sample_type
						.getValue());
				fm.setSample_code(sample_code.get(i));
				if (birth_dates.size() > 0)
					fm.setBirth_date(birth_dates.get(i));
				if (id_numbers.size() > 0)
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

	// @SuppressWarnings({ "rawtypes", "unchecked" })
	// private List<String> getSampleCode(List<String> list, String case_code) {
	// List<String> sampleCodes = new ArrayList<String>();
	//
	// if ("".equals(sampleCall(list.get(0).toString()))) {
	// for (int i = 0; i < list.size(); i++) {
	// sampleCodes.add(case_code + sampleNum(i + 1));
	// }
	// } else {
	// String strings[] = new String[list.size()];
	//
	// for (int i = 0, j = list.size(); i < j; i++) {
	// strings[i] = list.get(i).toString();
	// }
	// Set uniqueSet = new HashSet(list);
	// for (Object temp : uniqueSet) {
	//
	// String call = sampleCall(temp.toString());
	//
	// List listTemp1 = new ArrayList();
	// if (Collections.frequency(list, temp) > 1) {
	// for (int j = 0; j < Collections.frequency(list, temp); j++) {
	// listTemp1.add(temp);
	// }
	// for (int k = 1; k <= listTemp1.size(); k++) {
	// for (int m = 0; m < strings.length; m++) {
	// if (strings[m].equals(listTemp1.get(0).toString())) {
	// strings[m] = case_code + call + sampleNum(k);
	// break;
	// }
	// }
	// }
	//
	// } else {
	// for (int m = 0; m < strings.length; m++) {
	// if (strings[m].equals(temp.toString())) {
	// strings[m] = case_code + call;
	// }
	// }
	// }
	// }
	// if (strings.length > 1)
	// sampleCodes = Arrays.asList(strings);
	// else
	// sampleCodes.add(strings[0]);
	// }
	//
	// return sampleCodes;
	// }

	// private String sampleCall(String call) {
	// switch (call) {
	// case "father":
	// return "F";
	// case "mother":
	// return "M";
	// case "son":
	// return "S";
	// case "daughter":
	// return "D";
	// case "child":
	// return "H";
	// default:
	// return "";
	// }
	// }
	//
	// private String sampleNum(int num) {
	// switch (num) {
	// case 1:
	// return "A";
	// case 2:
	// return "B";
	// case 3:
	// return "C";
	// case 4:
	// return "D";
	// case 5:
	// return "E";
	// case 6:
	// return "F";
	// case 7:
	// return "G";
	// case 8:
	// return "H";
	// case 9:
	// return "I";
	// case 10:
	// return "J";
	// case 11:
	// return "K";
	// case 12:
	// return "L";
	// case 13:
	// return "M";
	// default:
	// return "";
	// }
	// }

	@Override
	public List<RdsJudicialSampleExpressModel> querySampleExpress(
			Map<String, Object> params) {
		return RdsJudicialRegisterMapper.querySampleExpress(params);
	}

	@Override
	public int insertSampleExpress(Map<String, Object> params) {
		String[] case_ids = params.get("case_id").toString().split(",");
		int reslut = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("express_type", params.get("express_type"));
		map.put("express_num", params.get("express_num"));
		map.put("express_recive", params.get("express_recive"));
		map.put("express_remark", params.get("express_remark"));
		map.put("express_concat", params.get("express_concat"));
		for (String string : case_ids) {
			map.put("id", UUIDUtil.getUUID());
			map.put("case_id", string);
			reslut = RdsJudicialRegisterMapper.insertSampleExpress(map);
		}
		return reslut;
	}

	@Override
	public int updateSampleExpress(Map<String, Object> params) {
		return RdsJudicialRegisterMapper.updateSampleExpress(params);
	}

	@Override
	public int delSampleExpress(Map<String, Object> params) {
		return RdsJudicialRegisterMapper.deleteSampleExpress(params);
	}

	@Override
	public int updateCaseVerifyState(Map<String, Object> params) {
		try {
			String[] case_ids = params.get("case_id").toString().split(",");
			for (String string : case_ids) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("case_id", string);
				map.put("verify_state", 1);
				RdsJudicialRegisterMapper.updateCaseVerifyState(map);
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int insertCaseCodeSecond(Map<String, Object> params) {
		return RdsJudicialRegisterMapper.insertCaseCodeSecond(params);
	}

	@Override
	public List<RdsJudicialSecondModel> queryCaseCodeSecond(
			Map<String, Object> params) {
		return RdsJudicialRegisterMapper.queryCaseCodeSecond(params);
	}

	private boolean insertNoReport(Map<String, Object> params,
			RdsUpcUserModel user) {
		RdsJudicialCaseInfoModel caseInfoModel = new RdsJudicialCaseInfoModel();
		caseInfoModel.setCase_id(params.get("case_id").toString());
		caseInfoModel.setCase_code(params.get("case_code").toString());
		caseInfoModel.setAccept_time(params.get("accept_time").toString());
		caseInfoModel.setConsignment_time(params.get("consignment_time")
				.toString());
		caseInfoModel.setUnit_type(params.get("unit_type").toString());
		caseInfoModel.setTypeid(params.get("typeid").toString());
		caseInfoModel.setSample_relation((int) params.get("sample_relation"));
		caseInfoModel.setClient(params.get("client").toString());
		caseInfoModel.setPhone(params.get("phone").toString());
		caseInfoModel.setCase_state(Integer.parseInt(params.get("case_state")
				.toString()));
		caseInfoModel.setCase_areacode(params.get("case_areacode").toString());
		caseInfoModel.setCase_userid(params.get("case_userid").toString());
		caseInfoModel.setReceiver_area(params.get("receiver_area").toString());
		caseInfoModel.setCase_in_per(params.get("case_in_per").toString());
		caseInfoModel.setReceiver_id(params.get("receiver_id") == null ? ""
				: params.get("receiver_id").toString());
		if (params.get("sample_code") != null) {
			caseInfoModel.setSample_in_time(DATE_FORMAT.format(new Date()));
		}
		caseInfoModel.setSample_in_per(params.get("sample_in_per").toString());
		caseInfoModel.setCase_type(Integer.parseInt(params.get("case_type")
				.toString()));
		caseInfoModel.setUrgent_state((int) params.get("urgent_state"));
		caseInfoModel.setCopies(Integer.parseInt(params.get("copies")
				.toString()));
		caseInfoModel.setRemark(params.get("remark").toString());
		caseInfoModel.setLaboratory_no((String) params.get("laboratory_no"));
		caseInfoModel.setConfirm_code(params.get("confirm_code").toString());
		caseInfoModel.setParnter_name(params.get("parnter_name").toString());
		caseInfoModel.setSource_type(params.get("source_type").toString());
		// caseInfoModel.setVerify_state(8);
		// 作用不明
		if (params.get("sign") == null) {
			caseInfoModel.setIs_new(1);
		} else {
			caseInfoModel.setIs_new(0);
		}

		/* 案例流程增加 start */
		try {
			identityService.setAuthenticatedUserId(user.getUserid());
			ProcessInstance processInstance = runtimeService
					.startProcessInstanceByKey("processJudicial",
							caseInfoModel.getCase_id());
			caseInfoModel.setProcess_instance_id(processInstance.getId());
		} finally {
			identityService.setAuthenticatedUserId(null);
		}
		/* 案例流程增加 end */

		// 插入新增案例实体
		int case_result = RdsJudicialRegisterMapper
				.insertCaseInfo(caseInfoModel);
		// 案例实体插入成功，插入案例样本信息
		if (case_result > 0) {
			try {
				if (params.get("sample_code") != null) {
					List<String> sample_codes = getValues(params
							.get("sample_code"));
					List<String> sample_types = getValues(params
							.get("sample_type"));
					List<String> sample_usernames = getValues(params
							.get("sample_username"));
					List<String> id_numbers = getValues(params.get("id_number"));
					List<String> sample_calls = getValues(params
							.get("sample_call"));
					List<String> birth_dates = getValues(params
							.get("birth_date"));
					List<String> specials = getValues(params.get("special"));
					// 拆分子案例 去除
					// if (params.get("sign") == null) {
					// addSubCaseInfo(sample_codes, sample_calls, params,
					// UNKNOWN);
					// } else {
					// addSubCaseInfo(sample_codes, sample_calls, params,
					// null);
					// }

					// 插入案例样本信息
					for (int i = 0; i < sample_codes.size(); i++) {

						RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel();
						sampleInfoModel.setSample_id(UUIDUtil.getUUID());
						sampleInfoModel.setSample_code(sample_codes.get(i));
						sampleInfoModel.setSample_call(sample_calls.get(i));
						sampleInfoModel.setSample_username(sample_usernames
								.get(i));
						sampleInfoModel.setId_number(id_numbers.get(i));
						sampleInfoModel.setBirth_date(birth_dates.get(i));
						sampleInfoModel.setSample_type(sample_types.get(i));
						sampleInfoModel.setCase_id(caseInfoModel.getCase_id());
						sampleInfoModel.setSpecial(specials.get(i));
						// 插入样本信息
						RdsJudicialRegisterMapper
								.insertSampleInfo(sampleInfoModel);
					}
					// 判断如果委托人未填写，将样本名字作为委托人
					if (StringUtils.isEmpty(caseInfoModel.getClient())) {
						RdsJudicialRegisterMapper.updateClient(caseInfoModel);
					}

					/* 保存采样快递信息 start */
					if (!("".equals(params.get("express_num"))
							&& "".equals(params.get("express_type"))
							&& "".equals(params.get("express_concat")) && ""
								.equals(params.get("express_recive")))) {
						Map<String, Object> mapEpxress = new HashMap<String, Object>();
						mapEpxress
								.put("express_num", params.get("express_num"));
						mapEpxress.put("express_type",
								params.get("express_type"));
						mapEpxress.put("express_recive",
								params.get("express_recive"));
						mapEpxress.put("express_concat",
								params.get("express_concat"));
						mapEpxress.put("id", UUIDUtil.getUUID());
						mapEpxress.put("case_id", caseInfoModel.getCase_id());
						RdsJudicialRegisterMapper
								.insertSampleExpress(mapEpxress);
					}
					/* 保存采样快递信息 start */

					// 添加样本信息到组合表里
					addCaseSample(params);
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		} else
			return case_result > 0 ? true : false;
	}

	/**
	 * 更新亲子鉴定
	 * 
	 * @param params
	 * @param user
	 * @return
	 */
	private boolean updateNoReport(Map<String, Object> params,
			RdsUpcUserModel user) {
		// 案例修改标识
		params.put("updateFlag", true);

		RdsJudicialCaseInfoModel caseInfoModel = new RdsJudicialCaseInfoModel();
		caseInfoModel.setCase_id((params.get("case_id").toString()));
		caseInfoModel.setCase_code(params.get("case_code").toString());
		caseInfoModel.setAccept_time(params.get("accept_time").toString());
		caseInfoModel.setConsignment_time(params.get("consignment_time")
				.toString());
		caseInfoModel.setUnit_type(params.get("unit_type").toString());
		caseInfoModel.setTypeid(params.get("typeid").toString());
		caseInfoModel.setSample_relation((int) params.get("sample_relation"));
		caseInfoModel.setClient(params.get("client").toString());
		caseInfoModel.setPhone(params.get("phone").toString());
		caseInfoModel.setCase_state(Integer.parseInt(params.get("case_state")
				.toString()));
		caseInfoModel.setCase_areacode(params.get("case_areacode").toString());
		caseInfoModel.setCase_userid(params.get("case_userid").toString());
		caseInfoModel.setReceiver_area(params.get("receiver_area").toString());
		caseInfoModel.setCase_in_per(params.get("case_in_per").toString());
		caseInfoModel.setReceiver_id(params.get("receiver_id") == null ? ""
				: params.get("receiver_id").toString());
		if (params.get("sample_code") != null) {
			caseInfoModel.setSample_in_time(DATE_FORMAT.format(new Date()));
		}
		caseInfoModel.setSample_in_per(params.get("sample_in_per").toString());
		caseInfoModel.setCase_type(Integer.parseInt(params.get("case_type")
				.toString()));
		caseInfoModel.setUrgent_state((int) params.get("urgent_state"));
		caseInfoModel.setCopies(Integer.parseInt(params.get("copies")
				.toString()));
		caseInfoModel.setRemark(params.get("remark").toString());
		caseInfoModel.setLaboratory_no((String) params.get("laboratory_no"));
		caseInfoModel.setConfirm_code(params.get("confirm_code").toString());
		caseInfoModel.setParnter_name(params.get("parnter_name").toString());
		// caseInfoModel.setVerify_state(8);
		caseInfoModel.setSource_type(params.get("source_type").toString());
		// 更新案例基本信息
		int case_result = RdsJudicialRegisterMapper
				.updateCaseInfo(caseInfoModel);
		// 更新案例统一流水号
		RdsJudicialRegisterMapper.updateCompanyCodeCaseid(params);
		if (case_result > 0) {
			try {
				// 删除样本信息
				deleteSampleInfo(params);
				// 重新插入子案例和样本信息
				if (params.get("sample_code") != null) {
					List<String> sample_codes = getValues(params
							.get("sample_code"));
					List<String> sample_types = getValues(params
							.get("sample_type"));
					List<String> sample_usernames = getValues(params
							.get("sample_username"));
					List<String> id_numbers = getValues(params.get("id_number"));
					List<String> sample_calls = getValues(params
							.get("sample_call"));
					List<String> birth_dates = getValues(params
							.get("birth_date"));
					List<String> specials = getValues(params.get("special"));

					// if (params.get("sign") == null) {
					// // 修改子案例信息
					// if ("1".equals(params.get("is_new").toString())) {
					// addSubCaseInfo(sample_codes, sample_calls, params,
					// UNKNOWN);
					// } else {
					// addSubCaseInfo(sample_codes, sample_calls, params,
					// null);
					// }
					// }
					for (int i = 0; i < sample_codes.size(); i++) {
						RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel();
						sampleInfoModel.setSample_id(UUIDUtil.getUUID());
						sampleInfoModel.setSample_code(sample_codes.get(i));
						sampleInfoModel.setSample_call(sample_calls.get(i));
						sampleInfoModel.setSample_username(sample_usernames
								.get(i));
						sampleInfoModel.setId_number(id_numbers.get(i));
						sampleInfoModel.setBirth_date(birth_dates.get(i));
						sampleInfoModel.setSample_type(sample_types.get(i));
						sampleInfoModel.setCase_id(caseInfoModel.getCase_id());
						sampleInfoModel.setSpecial(specials.get(i));
						RdsJudicialRegisterMapper
								.insertSampleInfo(sampleInfoModel);
					}
					if (StringUtils.isEmpty(caseInfoModel.getClient())) {
						RdsJudicialRegisterMapper.updateClient(caseInfoModel);
					}
				}
				addCaseSample(params);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		} else
			return case_result > 0 ? true : false;
	}

	@Override
	public int countCaseInfo(Map<String, Object> params) {
		return RdsJudicialRegisterMapper.countCaseInfo(params);
	}

	@Override
	public int insertSampleCode(Map<String, Object> params) {
		return RdsJudicialRegisterMapper.insertSampleCode(params);
	}

	@Override
	public RdsJudicialResponse queryApplySampleCode(Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialApplySampleCodeModel> list = RdsJudicialRegisterMapper
				.queryApplySampleCode(params);
		int count = RdsJudicialRegisterMapper.countApplySampleCode(params);
		response.setItems(list);
		response.setCount(count);
		return response;
	}

	@Override
	public int countApplySampleCode(Map<String, Object> params) {
		return RdsJudicialRegisterMapper.countApplySampleCode(params);
	}

	@Override
	public int queryMaxApplyBefore() {
		return RdsJudicialRegisterMapper.queryMaxApplyBefore();
	}

	@Override
	public int queryMaxApplyAfter() {
		return RdsJudicialRegisterMapper.queryMaxApplyAfter();
	}

	@Override
	public boolean addCaseFeeOther(Map<String, Object> params) {
		return caseFeeMapper.addCaseFeeOther(params);
	}

	@Override
	public RdsJudicialResponse getChangeCaseInfo(Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialCaseInfoModel> caseInfoModels = RdsJudicialRegisterMapper
				.queryChangeCaseInfo(params);
		int count = RdsJudicialRegisterMapper.countChangeCaseInfo(params);
		response.setItems(caseInfoModels);
		response.setCount(count);
		return response;
	}

	@Override
	public Object updateChangeCaseInfo(Map<String, Object> params,
			RdsUpcUserModel user) {
		// 案例返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		params.put("updateFlag", true);
		// RdsJudicialCaseInfoModel caseInfoModel = new
		// RdsJudicialCaseInfoModel();
		// caseInfoModel.setCase_id((params.get("case_id").toString()));
		// caseInfoModel.setCase_code(params.get("case_code").toString());
		// caseInfoModel.setAccept_time(params.get("accept_time").toString());
		// caseInfoModel.setConsignment_time(params.get("consignment_time")
		// .toString());
		// caseInfoModel.setReport_model(params.get("report_model").toString());
		// caseInfoModel.setUnit_type(params.get("unit_type").toString());
		// caseInfoModel.setTypeid(params.get("typeid").toString());
		// // 亲属关系
		// String sample_relation = params.get("sample_relation") == null ? "0"
		// : params.get("sample_relation").toString();
		// caseInfoModel.setSample_relation(Integer.parseInt(sample_relation));
		// caseInfoModel.setClient(params.get("client").toString());
		// caseInfoModel.setPhone(params.get("phone").toString());
		// // 案例状态
		// String case_state = params.get("case_state") == null ? "0" : params
		// .get("case_state").toString();
		// caseInfoModel.setCase_state(Integer.parseInt(case_state));
		// caseInfoModel.setCase_areacode(params.get("case_areacode").toString());
		// caseInfoModel.setCase_userid(params.get("case_userid").toString());
		// caseInfoModel.setReceiver_area(params.get("receiver_area").toString());
		// caseInfoModel.setCase_in_per(params.get("case_in_per").toString());
		// if (params.get("sample_code") != null) {
		// caseInfoModel.setSample_in_time(DATE_FORMAT.format(new Date()));
		// }
		// caseInfoModel.setSample_in_per(params.get("sample_in_per").toString());
		// // 案例类型
		// String case_type = params.get("case_type") == null ? "0" :
		// params.get(
		// "case_type").toString();
		// caseInfoModel.setCase_type(Integer.parseInt(case_type));
		// // 紧急程度
		// String urgent_state = params.get("urgent_state") == null ? "0" :
		// params
		// .get("urgent_state").toString();
		// caseInfoModel.setUrgent_state(Integer.parseInt(urgent_state));
		// String copies = params.get("copies") == null ? "2" : params.get(
		// "copies").toString();
		// caseInfoModel.setCopies(Integer.parseInt(copies));
		// caseInfoModel.setRemark(params.get("remark").toString());
		// caseInfoModel.setLaboratory_no((String) params.get("laboratory_no"));
		// caseInfoModel.setConfirm_code(params.get("confirm_code").toString());
		// caseInfoModel.setParnter_name(params.get("parnter_name").toString());
		// caseInfoModel.setSource_type(params.get("source_type").toString());
		// // 更新案例基本信息
		// int case_result = RdsJudicialRegisterMapper
		// .updateCaseInfo(caseInfoModel);

		// if (case_result > 0) {
		rdsJudicialVerifyMapper.updateCaseRemark(params);
		// 删除样本信息
		deleteSampleInfo(params);
		// 重新插入子案例和样本信息
		if (params.get("sample_code") != null) {
			List<String> sample_codes = getValues(params.get("sample_code"));
			List<String> sample_types = getValues(params.get("sample_type"));
			List<String> sample_usernames = getValues(params
					.get("sample_username"));
			List<String> id_numbers = getValues(params.get("id_number"));
			List<String> sample_calls = getValues(params.get("sample_call"));
			List<String> birth_dates = getValues(params.get("birth_date"));
			List<String> specials = getValues(params.get("special"));

			for (int i = 0; i < sample_codes.size(); i++) {
				RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel();
				sampleInfoModel.setSample_id(UUIDUtil.getUUID());
				sampleInfoModel.setSample_code(sample_codes.get(i));
				sampleInfoModel.setSample_call(sample_calls.get(i));
				sampleInfoModel.setSample_username(sample_usernames.get(i));
				sampleInfoModel.setId_number(id_numbers.get(i));
				sampleInfoModel.setBirth_date(birth_dates.get(i));
				sampleInfoModel.setSample_type(sample_types.get(i));
				sampleInfoModel.setCase_id(params.get("case_id").toString());
				sampleInfoModel.setSpecial(specials.get(i));
				RdsJudicialRegisterMapper.insertSampleInfo(sampleInfoModel);
			}
			// if (StringUtils.isEmpty(caseInfoModel.getClient())) {
			// RdsJudicialRegisterMapper.updateClient(caseInfoModel);
			// }
		}
		addCaseSample(params);
		result.put("result", true);
		result.put("message", "操作成功！");
		return result;
		// }

	}

}