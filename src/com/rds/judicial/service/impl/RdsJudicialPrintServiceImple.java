package com.rds.judicial.service.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.misc.BASE64Encoder;

import com.ibm.icu.text.SimpleDateFormat;
import com.rds.activiti.model.RdsActivitiTaskDetailModel;
import com.rds.code.date.DateUtils;
import com.rds.code.utils.FileUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.judicial.mapper.RdsJudicialCaseAttachmentMapper;
import com.rds.judicial.mapper.RdsJudicialCaseParamMapper;
import com.rds.judicial.mapper.RdsJudicialPrintMapper;
import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.mapper.RdsJudicialSampleMapper;
import com.rds.judicial.mapper.RdsJudicialSubCaseMapper;
import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialCompareResultModel;
import com.rds.judicial.model.RdsJudicialIdentifyModel;
import com.rds.judicial.model.RdsJudicialIdentifyPer;
import com.rds.judicial.model.RdsJudicialPiInfoModel;
import com.rds.judicial.model.RdsJudicialPrintCaseModel;
import com.rds.judicial.model.RdsJudicialPrintCaseResultModel;
import com.rds.judicial.model.RdsJudicialPrintModel;
import com.rds.judicial.model.RdsJudicialPrintResultModel;
import com.rds.judicial.model.RdsJudicialPrintSampleModel;
import com.rds.judicial.model.RdsJudicialPrintTableModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleInfoModel;
import com.rds.judicial.model.RdsJudicialSampleResultDataHis;
import com.rds.judicial.model.RdsJudicialSampleResultModel;
import com.rds.judicial.model.RdsJudicialSubCaseInfoModel;
import com.rds.judicial.service.RdsJudicialIdentifyPerService;
import com.rds.judicial.service.RdsJudicialPrintService;
import com.rds.judicial.service.RdsJudicialReagentsService;
import com.rds.judicial.service.RdsJudicialSampleService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service("RdsJudicialPrintServiceImple")
@Transactional
public class RdsJudicialPrintServiceImple implements RdsJudicialPrintService {

	private static final String PASS = "passed";
	private static final String FAILED = "failed";
	private static final String TWO_TRUE = "passed_2";
	private static final String THREE_TRUE = "passed_3";
	private static final String TWO_FALSE = "failed_2";
	private static final String FATHER = "father";
	private static final String Mother = "mother";

	@Autowired
	private RdsJudicialPrintMapper rdsJudicialPrintMapper;
	@Autowired
	private RdsJudicialCaseAttachmentMapper attachmentMapper;
	@Autowired
	private RdsJudicialCaseParamMapper rdsJudicialCaseParamMapper;
	@Autowired
	private RdsJudicialSubCaseMapper rdsJudicialSubCaseMapper;
	@Autowired
	private RdsJudicialReagentsService rdsJudicialReagentsService;
	@Autowired
	private RdsJudicialSampleService rdsJudicialSampleService;
	@Autowired
	private RdsJudicialIdentifyPerService rdsJudicialIdentifyPerService;
	@Autowired
	private RdsJudicialRegisterMapper rdsJudicialRegisterMapper;
	
	 @Autowired
    private HistoryService historyService;

	@Autowired
	private RdsJudicialSampleMapper rdsJudicialSampleMapper;

	private static String sql_first = "SELECT name,value as %s FROM tb_judicial_sample_result_data WHERE experiment_no IN( SELECT experiment_no FROM tb_judicial_sample_result WHERE sample_code='%s' AND enable_flag='Y' GROUP BY ext_flag) AND sample_code='%s'";

	private static String sql_then = "SELECT name,value as %s FROM tb_judicial_sample_result_data WHERE experiment_no IN( SELECT experiment_no FROM tb_judicial_sample_result WHERE sample_code='%s' AND enable_flag='Y' GROUP BY ext_flag) AND sample_code='%s'";

	/**
	 * 获取案例列表
	 */
	@Override
	public RdsJudicialResponse getPrintCaseInfo(Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialCaseInfoModel> caseInfoModels = rdsJudicialPrintMapper
				.getPrintCaseInfo(params);
		int count = rdsJudicialPrintMapper.countPrintCaseInfo(params);
		response.setItems(caseInfoModels);
		response.setCount(count);
		return response;
	}

	/**
	 * 根据条件获取案例的基本信息
	 */
	@Override
	public RdsJudicialResponse getPrintCaseInfoForWord(
			Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialCaseInfoModel> caseInfoModels = rdsJudicialPrintMapper
				.getPrintCaseInfoForWord(params);
		int count = rdsJudicialPrintMapper.countPrintCaseInfoForWord(params);
		response.setItems(caseInfoModels);
		response.setCount(count);
		return response;
	}

	/**
	 * 
	 * @param sample_codes
	 *            所需报告下所有的案例编号
	 * @param sampeInfoModels
	 *            案例下所有的样本信息
	 * @param subCases
	 *            子案例结果信息
	 * @param length
	 *            截取不匹配位点长度
	 * @param case_id
	 *            案例编号
	 * @param type
	 *            模板类型
	 * @return
	 */
	private RdsJudicialPrintResultModel getCaseModel(List<String> sample_codes,
			List<RdsJudicialSampleInfoModel> sampeInfoModels,
			List<RdsJudicialSubCaseInfoModel> subCases, int length,
			String case_id, String type) {
		RdsJudicialPrintResultModel printResultModel = new RdsJudicialPrintResultModel();
		RdsJudicialPrintSampleModel sampleModel = new RdsJudicialPrintSampleModel();
		List<RdsJudicialSampleInfoModel> child_infos = new ArrayList<RdsJudicialSampleInfoModel>();
		for (String sample_code : sample_codes) { // 遍历填空案例的人员
			for (RdsJudicialSampleInfoModel sampleInfoModel : sampeInfoModels) {
				if (sample_code.equals(sampleInfoModel.getSample_code())) {
					if (FATHER.equals(sampleInfoModel.getSample_call())) {
						sampleModel.setFather_info(sampleInfoModel);
					} else if (Mother.equals(sampleInfoModel.getSample_call())) {
						sampleModel.setMother_info(sampleInfoModel);
					} else {
						child_infos.add(sampleInfoModel);
					}
				}
			}
		}
		if (StringUtils.isEmpty(sampleModel.getFather_info().getSample_code())) { // 无父亲
																					// 母亲取代父亲
			sampleModel.setFather_info(sampleModel.getMother_info());
			sampleModel.setMother_info(new RdsJudicialSampleInfoModel());
		}
		if (child_infos.size() == 1) {// 小孩为一个
			sampleModel.setChild_info(child_infos.get(0));
		}
		sampleModel.setChilds_info(child_infos);
		String select = "select ";
		String content = " from ";
		String where = " where ";
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("name", 0);// 根据之前的人员变号排列
		for (int i = 0; i < sample_codes.size(); i++) {
			String code = sample_codes.get(i).trim();

			params.put(code, i + 1);
			String str = "";
			// 可以在此处改变表格位置
			if (i == 0) {
				select += "tb" + i + ".name,tb" + i + ".`" + code.trim() + "`,";
				str = String.format(sql_first, "\'" + code + "\'", code, code);
				content += "(" + str + ") tb" + i + ",";
			} else {
				select += "tb" + i + ".`" + code + "`,";
				str = String.format(sql_then, "\'" + code + "\'", code, code);
				content += "(" + str + ") tb" + i + ",";
			}
			if (i < sample_codes.size() - 1) {
				where += "tb" + i + ".name=tb" + (i + 1) + ".name and ";
			}
		}
		String sql = select.substring(0, select.length() - 1)
				+ content.substring(0, content.length() - 1) + where + " 1=1";

		Map<String, String> map = new HashMap<String, String>();
		String reagent_name = rdsJudicialSubCaseMapper
				.queryReagentNameByCaseId(case_id);
		String reagent_name_ext = rdsJudicialSubCaseMapper
				.queryReagentNameEXTByCaseId(case_id);
		map.put("reagent_name_ext", reagent_name_ext);
		map.put("reagent_name", reagent_name);
		map.put("sql", sql);
		map.put("type", type);
		List<Map<String, String>> sampleResults = rdsJudicialPrintMapper
				.getSampleResult(map); // 查出此案例的人员位点
		List<Map<String, String>> sampleResultsBysort = new ArrayList<Map<String, String>>();// 根据之前的人员变号排列
		for (Map<String, String> sample : sampleResults) {
			sampleResultsBysort.add(sortMapByKey(sample, params));
		}
		sampleModel.setSampleResults(sampleResultsBysort);
		RdsJudicialCaseAttachmentModel attachmentModel = rdsJudicialPrintMapper
				.getPrintAttachment(case_id);// 获取照片附件
		printResultModel.setSampleModel(sampleModel);
		printResultModel.setAttachmentModel(attachmentModel);

		List<RdsJudicialPrintCaseResultModel> results = new ArrayList<RdsJudicialPrintCaseResultModel>();
		for (RdsJudicialSubCaseInfoModel subCase : subCases) {
			RdsJudicialPrintCaseResultModel result = new RdsJudicialPrintCaseResultModel();
			RdsJudicialCompareResultModel compareResultModel = rdsJudicialPrintMapper
					.getCompareResult(subCase.getSub_case_code());
			if (StringUtils.isNotEmpty(subCase.getPi())) {
				result.setPi_double(Double.valueOf(subCase.getPi())); // 设置PI样式
				result.setPi(getPiValue(subCase.getPi()));
			}
			if (compareResultModel != null
					&& StringUtils.isNotEmpty(compareResultModel
							.getUnmatched_node())) {
				String unmatche_node = compareResultModel.getUnmatched_node();
				String unmatche_node_sorted = sortUnmatchedNode(unmatche_node,
						reagent_name, reagent_name_ext, ",");
				compareResultModel.setUnmatched_node(unmatche_node_sorted);
				String[] codes = compareResultModel.getUnmatched_node().split(
						",");
				if (codes.length > length) {
					String unmatch_code = "";
					for (int i = 0; i < codes.length; i++) {
						if (i < length) {
							unmatch_code += codes[i] + ",";
						}
					}
					unmatch_code = unmatch_code.substring(0,
							unmatch_code.length() - 1)
							+ "等";
					compareResultModel.setUnmatched_node(unmatch_code);// 如何截取不匹配位点长度
				}
			}
			result.setCompareResultModel(compareResultModel);
			results.add(result);
		}
		if (results.size() == 1) {
			printResultModel.setResult(results.get(0));// 报告结果为一
		}
		printResultModel.setResults(results);
		return printResultModel;
	}

	/**
	 * 判断pi为负数留四位小数为正数直接返回值
	 * 
	 * @param value
	 * @return
	 */
	public String getPiValue(String value) {
		// 是否有小数点
		if (value.indexOf('.') > 0) {
			String[] values = value.split("\\.");
			String end = values[1];
			if ((values[1].length() > 4)
					&& (Double.valueOf(value).doubleValue() >= 10000)) {
				end = values[1].substring(0, 4);
			} else if (values[1].length() < 4) {
				// 不足四位小数补零
				end = String.valueOf(Integer.valueOf(values[1]).intValue()
						* (int) Math.pow(10, 4 - values[1].length()));
			}
			return values[0] + "." + end;
		}
		return value;
	}

	public Map<String, String> sortMapByKey(Map<String, String> oriMap,
			final Map<String, Integer> params) {
		if (oriMap == null || oriMap.isEmpty()) {
			return null;
		}
		Map<String, String> sortedMap = new TreeMap<String, String>(
				new Comparator<String>() {
					public int compare(String key1, String key2) {
						int intKey1 = params.get(key1), intKey2 = params
								.get(key2);
						return intKey1 - intKey2;
					}
				});
		sortedMap.putAll(oriMap);
		return sortedMap;
	}

	@Override
	public boolean usePrintCount(Map<String, Object> params) {
		int count = rdsJudicialPrintMapper.usePrintCount(params);
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<RdsJudicialPrintTableModel> getPrintTable(String case_code,
			String case_id, String type) {
		List<RdsJudicialPrintTableModel> printTableModels = rdsJudicialPrintMapper
				.getPrintCaseTable(case_code);
		for (RdsJudicialPrintTableModel caseInfoModel : printTableModels) {
			if (PASS.equals(caseInfoModel.getResult())) {
				if (StringUtils.isNotEmpty(caseInfoModel.getSample_code2())) {
					caseInfoModel.setModel_type(THREE_TRUE);
				} else {
					caseInfoModel.setModel_type(TWO_TRUE);
				}
			}
			String reagent_name = rdsJudicialSubCaseMapper
					.queryReagentNameByCaseId(case_id);
			Map<String, String> params = new HashMap<String, String>();
			params.put("reagent_name", reagent_name);
			params.put("type", type);
			params.put("case_code", caseInfoModel.getSub_case_code());
			if (StringUtils.isNotEmpty(caseInfoModel.getPi())) {
				caseInfoModel
						.setPi_double(Double.valueOf(caseInfoModel.getPi())); // 设置PI样式
			}
			List<RdsJudicialPiInfoModel> piInfoModels = rdsJudicialPrintMapper
					.getPiInfoModel(params);
			caseInfoModel.setPiInfoModels(piInfoModels);
		}
		return printTableModels;
	}

	@Override
	public boolean exsitPrintTable(Map<String, Object> params) {
		boolean result = false;
		if (params.get("case_code") != null) {
			List<RdsJudicialPrintTableModel> printTableModels = rdsJudicialPrintMapper
					.getPrintCaseTable(params.get("case_code").toString());
			if (printTableModels.size() > 0) {
				return true;
			}
		}
		return result;
	}

	@Override
	public RdsJudicialPrintModel getPrintModel(Map<String, Object> params) {
		return rdsJudicialPrintMapper.getPrintModel(params);
	}

	@Override
	public boolean exsitSampleImg(Map<String, Object> params) {
		int count = rdsJudicialPrintMapper.exsitSampleImg(params);
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<String> getSampleImg(String case_code) {
		List<String> imgs = rdsJudicialPrintMapper.getSampleImg(case_code);
		return imgs;
	}

	/**
	 * 子渊模板的数据获取方式
	 */
	@Override
	public RdsJudicialPrintCaseModel getPrintCaseZY(String case_code,
			String case_id, String type) {
		RdsJudicialPrintCaseModel printCaseModel = rdsJudicialPrintMapper
				.getPrintCase(case_code); // 案例基本信息

		if (StringUtils.isEmpty(printCaseModel.getClose_time())) {
			printCaseModel.setClose_time(DateUtils.zhformat.format(new Date()));
		}
		// 开始判断受理时间和打印时间,如果受理时间和打印时间相等则把受时间减去一天后插入新增的字段addextnew_time
		String acceptTime = printCaseModel.getAccept_time();
		String closeTime = printCaseModel.getClose_time();

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月d日");
			Date dateTime1 = dateFormat.parse(acceptTime);
			Date dateTime2 = dateFormat.parse(closeTime);
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(dateTime1);

			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(dateTime2);

			boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
					.get(Calendar.YEAR);
			boolean isSameMonth = isSameYear
					&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
			boolean isSameDate = isSameMonth
					&& cal1.get(Calendar.DAY_OF_MONTH) == cal2
							.get(Calendar.DAY_OF_MONTH);

			if (isSameYear && isSameMonth && isSameDate) {
				// 时间相同 判断addextnew_time 是否为空 否则直接取作为受理时间
				String addnewTime = printCaseModel.getAddextnew_time();
				if ("".equals(acceptTime) || addnewTime == null) {
					// 空则减去一天
					Date dNow = dateTime1; // 当前时间
					Date dBefore = new Date();
					Calendar calendar = Calendar.getInstance(); // 得到日历
					calendar.setTime(dNow);// 把当前时间赋给日历
					calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
					dBefore = calendar.getTime(); // 得到前一天的时间
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss"); // 设置时间格式
					String defaultStartDate = sdf.format(dBefore); // 格式化前一天
					printCaseModel.setAccept_time("");
					try {
						printCaseModel
								.setAddextnew_time(com.rds.code.utils.StringUtils
										.dateToChineseTen(defaultStartDate));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					printCaseModel.setAddextnew_time(DateUtils.formatzh2
							.format(DateUtils.lineformat
									.parse(defaultStartDate)));

					// 更新数据库字段值
					String addextnew_time = defaultStartDate;
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("addextnew_time", addextnew_time);
					param.put("case_code", case_code);
					rdsJudicialRegisterMapper.updatenewtimebycode(param);

				} else {
					// printCaseModel.setAccept_time("");
					printCaseModel.setAddextnew_time(addnewTime);
				}
			}

		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		List<RdsJudicialSampleInfoModel> sampeInfoModels = rdsJudicialPrintMapper
				.getSampleInfo(case_id); // 案例下所有的样本数据
		List<RdsJudicialSubCaseInfoModel> subCaseInfoModels = rdsJudicialPrintMapper
				.getSubCaseInfos(case_code); // 获取需要打印的子案例信息
		List<RdsJudicialPrintResultModel> results = new ArrayList<RdsJudicialPrintResultModel>();
		for (RdsJudicialSubCaseInfoModel subCaseInfoModel : subCaseInfoModels) {
			List<String> sample_codes = new ArrayList<String>();
			sample_codes.add(subCaseInfoModel.getSample_code1());
			sample_codes.add(subCaseInfoModel.getSample_code3()); // 添加需要打印案例所有的样本
			List<RdsJudicialSubCaseInfoModel> subCases = new ArrayList<RdsJudicialSubCaseInfoModel>();
			subCases.add(subCaseInfoModel);// 子案例的信息，以方便下面获取理论结果
			if (PASS.equals(subCaseInfoModel.getResult())) {
				if (StringUtils.isNotEmpty(subCaseInfoModel.getSample_code2())) {
					sample_codes.add(subCaseInfoModel.getSample_code2());
					RdsJudicialPrintResultModel printResultModel = getCaseModel(
							sample_codes, sampeInfoModels, subCases, 3,
							case_id, type);
					printResultModel.setResult_type(THREE_TRUE);
					results.add(printResultModel);
				} else {
					RdsJudicialPrintResultModel printResultModel = getCaseModel(
							sample_codes, sampeInfoModels, subCases, 3,
							case_id, type);
					printResultModel.setResult_type(TWO_TRUE);
					results.add(printResultModel);
				}
			} else if (FAILED.equals(subCaseInfoModel.getResult())) {
				RdsJudicialPrintResultModel printResultModel = getCaseModel(
						sample_codes, sampeInfoModels, subCases, 3, case_id,
						type);
				printResultModel.setResult_type(TWO_FALSE);
				results.add(printResultModel);
			}
		}
		printCaseModel.setResults(results);
		return printCaseModel;
	}

	private String sortUnmatchedNode(String unmatched_node,
			String reagent_name, String reagent_name_ext, String sign) {
		String originalStr = rdsJudicialReagentsService
				.queryOriginalReagentAtelier(reagent_name);
		if (reagent_name_ext != null) {
			originalStr += ",";
			originalStr += rdsJudicialReagentsService
					.queryOriginalReagentAtelier(reagent_name_ext);
		}
		String[] unmatched_nodes = unmatched_node.split(",");
		Map<Integer, String> map = new TreeMap<Integer, String>();
		for (int i = 0; i < unmatched_nodes.length; i++) {
			map.put(originalStr.indexOf(unmatched_nodes[i]), unmatched_nodes[i]);
		}
		Set<Integer> set = map.keySet();
		String sb = "";
		for (int i : set) {
			sb += map.get(i);
			sb += sign;
		}
		return sb.substring(0, sb.length() - 1);
	}

	@Override
	/**
	 * @params
	 * case_code
	 * case_id
	 * file_name 输出文件路径
	 */
	public void createJudicialDoc(Map<String, Object> params) throws Exception {
		// 要填入模本的数据文件
		Map<String, Object> createparams = new HashMap<>();
		Map dataMap = new HashMap();
		String file_name = (String) params.get("file_name");
		String case_code = (String) params.get("case_code");

		// 案例基本信息,设置时间格式
		RdsJudicialCaseInfoModel rdsJudicialCaseInfoModel = rdsJudicialRegisterMapper
				.queryCaseInfoByCaseCode(case_code);

		String case_id = rdsJudicialCaseInfoModel.getCase_id();

		// 设置打印时间
		if (StringUtils.isEmpty(rdsJudicialCaseInfoModel.getClose_time())) {
			rdsJudicialCaseInfoModel
					.setClose_time(com.rds.code.utils.StringUtils
							.dateToTen(new Date()));
		}
		// 打印时间
		dataMap.put("date", com.rds.code.utils.StringUtils
				.dateToChinese(rdsJudicialCaseInfoModel.getClose_time()));

		// 开始判断受理时间和打印时间,如果受理时间和打印时间相等则把受时间减去一天后插入新增的字段addextnew_time
		rdsJudicialCaseInfoModel.setAccept_time(com.rds.code.utils.StringUtils
				.dateToChineseTen(rdsJudicialCaseInfoModel.getAccept_time()));
		rdsJudicialCaseInfoModel.setClose_time(com.rds.code.utils.StringUtils
				.dateToChineseTen(rdsJudicialCaseInfoModel.getClose_time()));

		String acceptTime = rdsJudicialCaseInfoModel.getAccept_time();
		String closeTime = rdsJudicialCaseInfoModel.getClose_time();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月d日");

		Date dateTime1 = dateFormat.parse(acceptTime);
		Date dateTime2 = dateFormat.parse(closeTime);

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(dateTime1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(dateTime2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth
				&& cal1.get(Calendar.DAY_OF_MONTH) == cal2
						.get(Calendar.DAY_OF_MONTH);

		if (isSameYear && isSameMonth && isSameDate) {
			// 时间相同 判断addextnew_time 是否为空 否则直接取作为受理时间
			String addnewTime = rdsJudicialCaseInfoModel.getAddextnew_time();
			if ("".equals(acceptTime) || addnewTime == null) {
				// 空则减去一天
				Date dNow = dateTime1; // 当前时间
				Date dBefore = new Date();
				Calendar calendar = Calendar.getInstance(); // 得到日历
				calendar.setTime(dNow);// 把当前时间赋给日历
				calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
				dBefore = calendar.getTime(); // 得到前一天的时间
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss"); // 设置时间格式
				String defaultStartDate = sdf.format(dBefore); // 格式化前一天
				rdsJudicialCaseInfoModel.setAccept_time("");
				rdsJudicialCaseInfoModel
						.setAddextnew_time(com.rds.code.utils.StringUtils
								.dateToChineseTen(defaultStartDate));
				// 更新数据库字段值
				String addextnew_time = defaultStartDate;
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("addextnew_time", addextnew_time);
				param.put("case_code", case_code);
				rdsJudicialRegisterMapper.updatenewtimebycode(param);

			} else {
				rdsJudicialCaseInfoModel.setAccept_time("");
				rdsJudicialCaseInfoModel
						.setAddextnew_time(com.rds.code.utils.StringUtils
								.dateToChineseTen(rdsJudicialCaseInfoModel
										.getAddextnew_time()));
			}
		}

		String template_name = rdsJudicialCaseInfoModel.getReport_model()
				+ ".ftl";

		// 在场人员
		List<RdsJudicialIdentifyModel> rdsJudicialIdentifyModels = rdsJudicialIdentifyPerService
				.queryIdentifyByCaseId(case_id);

		// 点位信息
		Map<String, Object> sampleResultParams = new HashMap<>();
		sampleResultParams.put("case_code", case_code);
		sampleResultParams.put("ext_flag", "N");
		// 非加位点
		List<RdsJudicialSampleResultModel> rdsJudicialSampleResultModels = rdsJudicialSampleService
				.queryRecordsByCaseCode(sampleResultParams);
		// 加位点
		sampleResultParams.put("ext_flag", "Y");
		List<RdsJudicialSampleResultModel> rdsJudicialSampleResultExtModels = rdsJudicialSampleService
				.queryRecordsByCaseCode(sampleResultParams);

		// 样本基本信息，如身份证号等
		List<RdsJudicialSampleInfoModel> rdsJudicialSampleInfoModels = rdsJudicialPrintMapper
				.getSampleInfo(case_id);

		if (rdsJudicialCaseInfoModel.getReport_model().equals("ztjdmodel")) {
			List list = sortSampleInfo(rdsJudicialSampleInfoModels,
					rdsJudicialSampleResultModels,
					rdsJudicialSampleResultExtModels);
			rdsJudicialSampleInfoModels = (List<RdsJudicialSampleInfoModel>) list
					.get(0);
			rdsJudicialSampleResultModels = (List<RdsJudicialSampleResultModel>) list
					.get(1);
			rdsJudicialSampleResultExtModels = (List<RdsJudicialSampleResultModel>) list
					.get(2);
		}

		// List<RdsJudicialSampleResultModel> parentSameResults = new
		// ArrayList<>();
		// if (rdsJudicialSampleResultModels.size()>0 ) {
		// parentSameResults.add(rdsJudicialSampleResultModels.get(0));
		// parentSameResults.add(rdsJudicialSampleResultModels.get(1));
		// }
		// if (rdsJudicialSampleResultExtModels.size()>0) {
		// parentSameResults.add(rdsJudicialSampleResultExtModels.get(0));
		// parentSameResults.add(rdsJudicialSampleResultExtModels.get(1));
		// }

		// 没身份证号的被鉴定人
		List<Integer> identifiedsNoId = new LinkedList<>();
		for (int i = 0; i < rdsJudicialSampleInfoModels.size(); i++) {
			if (rdsJudicialSampleInfoModels.get(i).getId_number() == null
					|| rdsJudicialSampleInfoModels.get(i).getId_number()
							.isEmpty()) {
				identifiedsNoId.add(i);
			}
		}

		// 案例使用的试剂Map，（reagent_name,）(reagent_name_ext)
		Map<String, Object> reagents = rdsJudicialReagentsService
				.queryReagentByCaseCode(case_code);
		Map<String, Object> queryOrderParam = new HashMap<>();
		queryOrderParam.put("reagent_name", reagents.get("reagent_name"));
		queryOrderParam
				.put("model", rdsJudicialCaseInfoModel.getReport_model());
		// 根据配置表tb_dic_print_gene查询排好序的试剂点位
		String[] ateliers = rdsJudicialReagentsService
				.queryOrder(queryOrderParam);
		// entry的集合，所有基因位，所有样本的数据
		List<Map<String, Object>> list = new ArrayList();
		for (String atelier : ateliers) {
			// 两个entry，第一个为基因位名称，第二个为该基因位下所有样本数据
			Map<String, Object> sublist = new HashMap<>();
			sublist.put("name", atelier);
			// N个样本一个点位的数据，如三人11,12 11,12 11，12
			List<Map<String, String>> subdata = new LinkedList();
			for (int i = 0; i < rdsJudicialSampleResultModels.size(); i++) {
				// 一个样本一个点位的数据，如11,12
				Map<String, String> oneData = new HashMap();
				String str = (String) rdsJudicialSampleResultModels.get(i)
						.getRecord().get(atelier);
				String strs[] = str.split(",");
				oneData.put("value1", strs[0]);
				oneData.put("value2", strs.length == 1 ? strs[0] : strs[1]);
				subdata.add(oneData);
			}
			sublist.put("data", subdata);
			list.add(sublist);
		}

		// 所有基因位，所有样本的数据,这部分是加位点
		List<Map<String, Object>> listext = new ArrayList();
		queryOrderParam.put("reagent_name", reagents.get("reagent_name_ext"));
		String[] ateliersExt = rdsJudicialReagentsService
				.queryOrder(queryOrderParam);
		if (reagents.get("reagent_name_ext") != null) {
			for (String atelier : ateliersExt) {
				// 两个entry，第一个为基因位名称，第二个为该基因位下所有样本数据
				Map<String, Object> sublist = new HashMap<>();
				sublist.put("name", atelier);
				// N个样本一个点位的数据，如三人11,12 11,12 11，12
				List<Map<String, String>> subdata = new LinkedList<>();
				for (int i = 0; i < rdsJudicialSampleResultExtModels.size(); i++) {
					// 一个样本一个点位的数据，如11,12
					Map<String, String> oneData = new HashMap<>();
					String str = (String) rdsJudicialSampleResultExtModels
							.get(i).getRecord().get(atelier);
					String strs[] = str.split(",");
					oneData.put("value1", strs[0]);
					oneData.put("value2", strs.length == 1 ? strs[0] : strs[1]);
					subdata.add(oneData);
				}
				sublist.put("data", subdata);
				listext.add(sublist);
			}
		}

		// 拆分子案例，算亲权值
		List<String> subcases = rdsJudicialSubCaseMapper
				.querySubCaseCode(case_code);
		List<Integer> paragraph = new ArrayList<>();
		// 存放需要计算亲权值的肯定案例
		List<Map<String, Object>> listPiAll = new ArrayList<>();

		// 子案例否定的结果
		List<RdsJudicialCompareResultModel> rdsJudicialCompareResultFailedModels = new LinkedList<>();

		// 子案例肯定的结果
		List<RdsJudicialCompareResultModel> rdsJudicialCompareResultSuccessModels = new LinkedList<>();

		// 子案例不做加位点直接肯定的结果
		List<RdsJudicialCompareResultModel> rdsJudicialCompareResultSuccessNoExtModels = new LinkedList<>();

		// 子案例加位点的结果
		List<RdsJudicialCompareResultModel> rdsJudicialCompareResultExtModels = new LinkedList<>();

		// 子案例加位点的结果
		List<RdsJudicialCompareResultModel> rdsJudicialCompareResultExtSuccessModels = new LinkedList<>();

		// 子案例加位点的结果
		List<RdsJudicialCompareResultModel> rdsJudicialCompareResultExtFailedModels = new LinkedList<>();

		String originalStr = rdsJudicialReagentsService
				.queryOriginalReagentAtelier((String) reagents
						.get("reagent_name"));

		// String originalExtStr = "";
		// if(reagents.get("reagent_name_ext")!= null && !((String)
		// reagents.get("reagent_name_ext")).isEmpty()){
		// originalExtStr =
		// rdsJudicialReagentsService.queryOriginalReagentAtelier((String)reagents.get("reagent_name_ext"));
		// }

		for (String sub_case_code : subcases) {
			RdsJudicialCompareResultModel m1 = rdsJudicialSubCaseMapper
					.querySubCaseCompareResult(sub_case_code);
			if (m1 == null) {
				continue;
			}
			// 否定，加位点跳过亲权值的取数
			if (m1.getExt_flag().equals("Y")) {
				rdsJudicialCompareResultExtModels.add(m1);
				if (m1.getFinal_result_flag().equals("passed")) {
					String unmatched_node = m1.getUnmatched_node();
					String[] unmatched_nodes = unmatched_node.split(",");
					if (unmatched_nodes.length == 1) {
						m1.setUnmatched_node_part1(unmatched_nodes[0]);
					} else if (originalStr.contains(unmatched_nodes[1])) {
						m1.setUnmatched_node_part1(unmatched_nodes[0] + ","
								+ unmatched_nodes[1]);
					} else {
						m1.setUnmatched_node_part1(unmatched_nodes[0]);
						m1.setUnmatched_node_part2(unmatched_nodes[1]);
					}
					rdsJudicialCompareResultExtSuccessModels.add(m1);
					rdsJudicialCompareResultSuccessModels.add(m1);
				} else {
					rdsJudicialCompareResultExtFailedModels.add(m1);
					rdsJudicialCompareResultFailedModels.add(m1);
				}
				continue;
			} else if (m1.getFinal_result_flag().equals("failed")) {
				rdsJudicialCompareResultFailedModels.add(m1);
				rdsJudicialCompareResultSuccessNoExtModels.add(m1);
				continue;
			} else {
				rdsJudicialCompareResultSuccessModels.add(m1);
				rdsJudicialCompareResultSuccessNoExtModels.add(m1);
			}
			// 根据子案例存放pi信息
			Map<String, Object> listpi = new HashMap<>();
			listpi.put("rdsJudicialCompareResultModel", m1);
			List<RdsJudicialPiInfoModel> rdsJudicialPiInfoModels = rdsJudicialCaseParamMapper
					.queryPiInfo(sub_case_code);
			RdsJudicialSubCaseInfoModel rdsJudicialSubCaseInfoModel = rdsJudicialSubCaseMapper
					.querySubCaseRecord(sub_case_code);
			listpi.put("sample_code1",
					rdsJudicialSubCaseInfoModel.getSample_code1());
			listpi.put("sample_code2", rdsJudicialSubCaseInfoModel
					.getSample_code2() == null ? ""
					: rdsJudicialSubCaseInfoModel.getSample_code2());
			listpi.put("sample_code3",
					rdsJudicialSubCaseInfoModel.getSample_code3());

			String pi = rdsJudicialSubCaseInfoModel.getPi();
			if (pi == null) {
				continue;
			}

			BigDecimal realpi = new BigDecimal(pi);
			// pi小数点位置
			int place = pi.indexOf(".") - 1;
			realpi = realpi.divide(new BigDecimal(Math.pow(10, place)));
			DecimalFormat dfPi = new DecimalFormat("#.00");
			pi = dfPi.format(realpi);
			if (rdsJudicialSubCaseInfoModel.getRcp().equals("1.0")) {
				rdsJudicialSubCaseInfoModel.setRcp("0.9999999999");
			}
			listpi.put("realpi", realpi);
			listpi.put("pi", pi);
			listpi.put("place", place);
			listpi.put("rcp", rdsJudicialSubCaseInfoModel.getRcp());
			List data = new ArrayList();

			for (String atelier : ateliers) {
				List sublist = new ArrayList();
				for (RdsJudicialPiInfoModel rdsJudicialPiInfoModel : rdsJudicialPiInfoModels) {
					if (atelier.equals(rdsJudicialPiInfoModel.getParam_type())) {
						sublist.add(atelier);
						String parent = rdsJudicialPiInfoModel.getParent();
						String child = rdsJudicialPiInfoModel.getChild();
						String[] parents = parent.split(",");
						String[] childs = child.split(",");
						sublist.add(parents[0]);
						sublist.add(parents.length == 1 ? parents[0]
								: parents[1]);
						sublist.add(childs[0]);
						sublist.add(childs.length == 1 ? childs[0] : childs[1]);
						sublist.add(rdsJudicialPiInfoModel.getGene1());
						sublist.add(rdsJudicialPiInfoModel.getGene2() == null ? ""
								: rdsJudicialPiInfoModel.getGene2());
						sublist.add(rdsJudicialPiInfoModel.getFunction());
						sublist.add(rdsJudicialPiInfoModel.getPi());
						if (rdsJudicialPiInfoModel.getParent2() != null) {
							String parent2 = rdsJudicialPiInfoModel
									.getParent2();
							String[] parents2 = parent2.split(",");
							sublist.add(parents2[0]);
							sublist.add(parents2.length == 1 ? parents2[0]
									: parents2[1]);
						}
					}
				}
				if (sublist.size() > 0) {
					data.add(sublist);
				}
			}
			listpi.put("data", data);
			listPiAll.add(listpi);
		}
		if (listPiAll.size() > 0) {
			for (int i = 0; i < 21 - ((List) listPiAll.get(0).get("data"))
					.size(); i++) {
				// 亲权值表底部再加一空行
				paragraph.add(i);
			}
		}
		List<String> unmathjycodes = new ArrayList<>();
		for (RdsJudicialCompareResultModel rdsJudicialCompareResultModel : rdsJudicialCompareResultFailedModels) {
			String unmatchedNode = "";

			if (reagents.get("reagent_name_ext") != null
					&& !((String) reagents.get("reagent_name_ext")).isEmpty()) {
				unmatchedNode = sortUnmatchedNode(
						rdsJudicialCompareResultModel.getUnmatched_node(),
						(String) reagents.get("reagent_name"),
						(String) reagents.get("reagent_name_ext"), "、");
			} else {
				unmatchedNode = sortUnmatchedNode(
						rdsJudicialCompareResultModel.getUnmatched_node(),
						(String) reagents.get("reagent_name"), null, "、");
			}
			// 获取所有的不匹配的点位
			unmathjycodes.add(unmatchedNode);
			if (unmatchedNode.contains("、")) {
				String[] unmatched_nodes = unmatchedNode.split("、");
				if (unmatched_nodes.length > 3) {
					unmatchedNode = "";
					for (int i = 0; i < 3; i++) {
						unmatchedNode += unmatched_nodes[i];
						unmatchedNode += "、";
					}
					unmatchedNode = unmatchedNode.substring(0,
							unmatchedNode.length() - 1);
				}
			}
			rdsJudicialCompareResultModel.setUnmatched_node(unmatchedNode);
			rdsJudicialCompareResultModel
					.setUnmatched_count_str((com.rds.code.utils.StringUtils
							.intToChinese(rdsJudicialCompareResultModel
									.getUnmatched_count())));
		}
		dataMap.put("firstAtelier", ateliers[0]);
		if (ateliersExt.length > 0) {
			dataMap.put("firstAtelierExt", ateliersExt[0]);
			dataMap.put("atelierExtCount", ateliersExt.length);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		dataMap.put("atelierCount", ateliers.length);
		dataMap.put("identifiedsNoId", identifiedsNoId);
		dataMap.put("sample_in_time",
				sdf.parse(rdsJudicialCaseInfoModel.getSample_in_time()));
		dataMap.put("case_code", case_code);
		dataMap.put("rdsJudicialCaseInfoModel", rdsJudicialCaseInfoModel);
		dataMap.put("rdsJudicialSampleInfoModels", rdsJudicialSampleInfoModels);
		dataMap.put("rdsJudicialSampleResultModels",
				rdsJudicialSampleResultModels);
		dataMap.put("rdsJudicialSampleResultExtModels",
				rdsJudicialSampleResultExtModels);
		dataMap.put("rdsJudicialIdentifyModels", rdsJudicialIdentifyModels);
		dataMap.put("rdsJudicialCompareResultFailedModels",
				rdsJudicialCompareResultFailedModels);
		dataMap.put("rdsJudicialCompareResultSuccessModels",
				rdsJudicialCompareResultSuccessModels);
		dataMap.put("rdsJudicialCompareResultExtModels",
				rdsJudicialCompareResultExtModels);
		dataMap.put("rdsJudicialCompareResultExtSuccessModels",
				rdsJudicialCompareResultExtSuccessModels);
		dataMap.put("rdsJudicialCompareResultExtFailedModels",
				rdsJudicialCompareResultExtFailedModels);
		dataMap.put("list", list);
		dataMap.put("listext", listext);
		dataMap.put("listPiAll", listPiAll);
		dataMap.put("subcases", subcases);

		dataMap.put("unmathjycodes", unmathjycodes);

		dataMap.put("paragraph", paragraph);

		createparams.put("dataMap", dataMap);
		createparams.put("template_name", template_name);
		createparams.put("file_name", file_name);
		create(createparams);
	}

	/**
	 * 模板不同方法不同
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void createJudicialDocBySubCaseCode(Map<String, Object> params)
			throws Exception {
		// 要填入模版的数据文件
		Map<String, Object> createparams = new HashMap<>();
		Map dataMap = new HashMap();
		String file_name = (String) params.get("file_name");
		String case_code = (String) params.get("case_code");
		String report_model = (String) params.get("report_model");
		String dateString =null;
		String ydDateStart =null;
		String ydDateEnd =null;
		/** 鸣正添加取样类型 **/
		Map<String, Object> sampeltypeformz = new HashMap<String, Object>();
		List<Map<String, Object>> sampeltypeformzs = rdsJudicialSubCaseMapper
				.querySampleType(case_code);

		for (Map<String, Object> sampeltype : sampeltypeformzs) {
			sampeltypeformz.put("samplecode", sampeltype.get("sample_code")
					.toString());
			sampeltypeformz.put("sampletype", sampeltype.get("sample_type")
					.toString());
		}
		/** 鸣正添加取样类型 **/

		Map<String, Object> sub_case_params = new HashMap<>();
		sub_case_params.put("case_code", case_code);
		sub_case_params.put("result", "Y");
		// 子案例信息
		List<String> sub_case_codes = rdsJudicialSubCaseMapper
				.querySubCaseForWord(sub_case_params);

		/** 中信模版使用（中信案例不由系统出报告，此处无用） **/
		List<Map<String, Object>> wordtreesubcasinfo = rdsJudicialSubCaseMapper
				.queryALLcaseForWord(sub_case_params);
		/** 中信模版使用（中信案例不由系统出报告，此处无用） **/

		// 案例基本信息
		RdsJudicialCaseInfoModel rdsJudicialCaseInfoModel = rdsJudicialRegisterMapper
				.queryCaseInfoByCaseCode(case_code);
		String case_id = rdsJudicialCaseInfoModel.getCase_id();
		String processInstanceId =rdsJudicialCaseInfoModel.getProcess_instance_id();
		int aaa=1;
		   List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByTaskCreateTime().desc().list();
	        for (HistoricTaskInstance item :
	                historicTaskInstanceList) {
	        	
	          if("taskSampleRecive".equals(item.getTaskDefinitionKey())){
	        	  Date currentTime = new Date();  
	  	          SimpleDateFormat formatter = new SimpleDateFormat("yyyy年M月d日");
	  	          currentTime=item.getStartTime();
	  	          
	  	          dateString = formatter.format(currentTime); 
	          }
	          if("taskSampleConfim".equals(item.getTaskDefinitionKey())){
	        	  Date currentTime = new Date();  
	  	          SimpleDateFormat formatter = new SimpleDateFormat("yyyy年M月d日");
	  	          currentTime=item.getStartTime();
	  	          String consignment_time=rdsJudicialCaseInfoModel.getConsignment_time();
	  	          consignment_time=com.rds.code.utils.StringUtils
	  					.dateToChineseTenTwo(consignment_time);
	  	          ydDateStart = formatter.format(currentTime); 
	  	          if(consignment_time.equals(ydDateStart)){
	  	        	Date dateTime1 = formatter.parse(ydDateStart);
	  	        	Date dNow = dateTime1; // 当前时间
					Date dBefore = new Date();
					Calendar calendar = Calendar.getInstance(); // 得到日历
					calendar.setTime(dNow);// 把当前时间赋给日历
					calendar.add(Calendar.DAY_OF_MONTH, 1); // 设置为前一天
					dBefore = calendar.getTime(); // 得到前一天的时间
					 // 设置时间格式
					 ydDateStart = formatter.format(dBefore);
					 aaa=0;
	  	          }
	          }}
	        List<HistoricTaskInstance> historicTaskInstanceList1 = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByTaskCreateTime().desc().list();
	        for (HistoricTaskInstance item :
	                historicTaskInstanceList1) {

	          if("taskExperiment".equals(item.getTaskDefinitionKey())){
	        	  Date currentTime = new Date();  
	  	          SimpleDateFormat formatter = new SimpleDateFormat("yyyy年M月d日");
	  	          currentTime=item.getEndTime();
	  	          ydDateEnd = formatter.format(currentTime); 
	  	          if(0==aaa){
	  	        	Date dateTime1 = formatter.parse(ydDateEnd);
	  	        	Date dNow = dateTime1; // 当前时间
					Date dBefore = new Date();
					Calendar calendar = Calendar.getInstance(); // 得到日历
					calendar.setTime(dNow);// 把当前时间赋给日历
					calendar.add(Calendar.DAY_OF_MONTH, 1); // 设置为前一天
					dBefore = calendar.getTime(); // 得到前一天的时间
					 // 设置时间格式
					ydDateEnd = formatter.format(dBefore);
	  	          }
	          }
	        }

	        
	       
		// 委托时间格式修改（年-月-日）
		if(report_model.startsWith("zyjd")||report_model.startsWith("qdwfjd")
				||report_model.startsWith("ydjd")||report_model.startsWith("cxjd")||report_model.startsWith("ynqsjd")){ 
			rdsJudicialCaseInfoModel
			.setConsignment_time(com.rds.code.utils.StringUtils
					.dateToChineseTenTwo(rdsJudicialCaseInfoModel
							.getConsignment_time()));
		}else{
		rdsJudicialCaseInfoModel
				.setConsignment_time(com.rds.code.utils.StringUtils
						.dateToChineseTen(rdsJudicialCaseInfoModel
								.getConsignment_time()));
		}
		// 设置打印时间格式设置（年-月-日）
		if (StringUtils.isEmpty(rdsJudicialCaseInfoModel.getClose_time())) {
			rdsJudicialCaseInfoModel
					.setClose_time(com.rds.code.utils.StringUtils
							.dateToTen(new Date()));
		}

		// 打印时间
		dataMap.put("date", com.rds.code.utils.StringUtils
				.dateToChinese(rdsJudicialCaseInfoModel.getClose_time()));

		/** 鼎城模版专用 打印时间 **/
		dataMap.put("datedc", com.rds.code.utils.StringUtils
				.dateToChineseTen(rdsJudicialCaseInfoModel.getClose_time()));
		/** 鼎城模版专用 **/

		/** 开始判断受理时间和打印时间,如果受理时间和打印时间相等则把受时间减去一天后插入新增的字段addextnew_time start **/
		if(report_model.startsWith("zyjd")||report_model.startsWith("qdwfjd")
				||report_model.startsWith("ydjd")||report_model.startsWith("cxjd")||report_model.startsWith("ynqsjd")){
		rdsJudicialCaseInfoModel.setAccept_time(com.rds.code.utils.StringUtils
				.dateToChineseTenTwo(rdsJudicialCaseInfoModel.getAccept_time()));}
		else{
			rdsJudicialCaseInfoModel.setAccept_time(com.rds.code.utils.StringUtils
					.dateToChineseTen(rdsJudicialCaseInfoModel.getAccept_time()));
		}
		if(report_model.startsWith("zyjd")||report_model.startsWith("qdwfjd")
				||report_model.startsWith("ydjd")||report_model.startsWith("cxjd")||report_model.startsWith("ynqsjd")){
			rdsJudicialCaseInfoModel.setClose_time(com.rds.code.utils.StringUtils
					.dateToChineseTenTwo(rdsJudicialCaseInfoModel.getClose_time()));}
		else{
			rdsJudicialCaseInfoModel.setClose_time(com.rds.code.utils.StringUtils
					.dateToChineseTen(rdsJudicialCaseInfoModel.getClose_time()));
		}

		String acceptTime = rdsJudicialCaseInfoModel.getAccept_time();
		String closeTime = rdsJudicialCaseInfoModel.getClose_time();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月d日");

		Date dateTime1 = dateFormat.parse(acceptTime);
		Date dateTime2 = dateFormat.parse(closeTime);

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(dateTime1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(dateTime2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth
				&& cal1.get(Calendar.DAY_OF_MONTH) == cal2
						.get(Calendar.DAY_OF_MONTH);

		if (isSameYear && isSameMonth && isSameDate) {
			// 时间相同 判断addextnew_time 是否为空 否则直接取作为受理时间
			String addnewTime = rdsJudicialCaseInfoModel.getAddextnew_time();
			if ("".equals(acceptTime) || addnewTime == null) {
				// 空则减去一天
				Date dNow = dateTime1; // 当前时间
				Date dBefore = new Date();
				Calendar calendar = Calendar.getInstance(); // 得到日历
				calendar.setTime(dNow);// 把当前时间赋给日历
				calendar.add(Calendar.DAY_OF_MONTH, -1); // 设置为前一天
				dBefore = calendar.getTime(); // 得到前一天的时间
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss"); // 设置时间格式
				String defaultStartDate = sdf.format(dBefore); // 格式化前一天
				if(report_model.startsWith("zyjd")||report_model.startsWith("qdwfjd")
						||report_model.startsWith("ydjd")||report_model.startsWith("cxjd")||report_model.startsWith("ynqsjd")){
					rdsJudicialCaseInfoModel.setAccept_time("");
					rdsJudicialCaseInfoModel
							.setAddextnew_time(com.rds.code.utils.StringUtils
									.dateToChineseTenTwo(defaultStartDate));
				}else{
				rdsJudicialCaseInfoModel.setAccept_time("");
				rdsJudicialCaseInfoModel
						.setAddextnew_time(com.rds.code.utils.StringUtils
								.dateToChineseTen(defaultStartDate));}
				// 更新数据库字段值
				String addextnew_time = defaultStartDate;
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("addextnew_time", addextnew_time);
				param.put("case_code", case_code);
				rdsJudicialRegisterMapper.updatenewtimebycode(param);

			} else {
				rdsJudicialCaseInfoModel.setAccept_time("");
				if(report_model.startsWith("zyjd")||report_model.startsWith("qdwfjd")
						||report_model.startsWith("ydjd")||report_model.startsWith("cxjd")||report_model.startsWith("ynqsjd")){
				rdsJudicialCaseInfoModel
						.setAddextnew_time(com.rds.code.utils.StringUtils
								.dateToChineseTenTwo(rdsJudicialCaseInfoModel
										.getAddextnew_time()));}
				else{
					rdsJudicialCaseInfoModel
					.setAddextnew_time(com.rds.code.utils.StringUtils
							.dateToChineseTen(rdsJudicialCaseInfoModel
									.getAddextnew_time()));	
				}
			}
		}
		/** 开始判断受理时间和打印时间,如果受理时间和打印时间相等则把受时间减去一天后插入新增的字段addextnew_time end **/

		String template_name = "";
		if (report_model.equals("zyjdmodeltablereg")
				|| report_model.equals("zyjdmodeltableregext")
				|| report_model.equals("zyjdmodeltable")) {
			template_name = report_model + ".ftl";
		} else {
			template_name = rdsJudicialCaseInfoModel.getReport_model() + ".ftl";
		}

		Map<String, Object> sampelresultpibyparent = new HashMap<String, Object>();
		List<Map<String, Object>> sampelresultpis = rdsJudicialSubCaseMapper
				.querySampleResult(case_code);// 得到父母分别的pi值
		// 先判断是否为大于两条数据且父母皆疑使用新模板
		if ((report_model.equals("zyjdmodelzk"))
				&& (sampelresultpis.size() == 2)
				&& (!sampelresultpis.get(0).get("samplecode")
						.equals(sampelresultpis.get(1).get("samplecode")))
						||(report_model.equals("bjqjdmodel"))
						&& (sampelresultpis.size() == 2)
						&& (!sampelresultpis.get(0).get("samplecode")
								.equals(sampelresultpis.get(1).get("samplecode")))
								||(report_model.equals("ynqsjdmodel"))
						          && (sampelresultpis.size() == 2)
						          && (!sampelresultpis.get(0).get("samplecode")
								.equals(sampelresultpis.get(1).get("samplecode")))
								||(report_model.equals("fyxhjdmodel"))
									&& (sampelresultpis.size() == 2)
									&& (!sampelresultpis.get(0).get("samplecode")
								.equals(sampelresultpis.get(1).get("samplecode")))
								||(report_model.equals("xajdmodel"))
									&& (sampelresultpis.size() == 2)
									&& (!sampelresultpis.get(0).get("samplecode")
								     .equals(sampelresultpis.get(1).get("samplecode")))
								||(report_model.equals("sqjdmodel"))
								&& (sampelresultpis.size() == 2)
								&& (!sampelresultpis.get(0).get("samplecode")
										.equals(sampelresultpis.get(1).get("samplecode")))
								||(report_model.equals("qdwfjdmodel"))
								&& (sampelresultpis.size() == 2)
								&& (!sampelresultpis.get(0).get("samplecode")
										.equals(sampelresultpis.get(1).get("samplecode")))
										||(report_model.equals("ydjdmodel"))
										&& (sampelresultpis.size() == 2)
										&& (!sampelresultpis.get(0).get("samplecode")
												.equals(sampelresultpis.get(1).get("samplecode")))
												||(report_model.equals("cxjdmodel"))
												&& (sampelresultpis.size() == 2)
												&& (!sampelresultpis.get(0).get("samplecode")
														.equals(sampelresultpis.get(1).get("samplecode"))))
					 {
			if(report_model.equals("zyjdmodelzk")){
			template_name = "zyjdmodelzkfmjy.ftl";
			}else if(report_model.equals("bjqjdmodel")){
			template_name = "bjqjdmodelfmjy.ftl";
			}else if(report_model.equals("qdwfjdmodel"))
				template_name = "qdwfjdmodelfmjy.ftl";
			else if(report_model.equals("ydjdmodel")){
				template_name = "ydjdmodelfmjy.ftl";}
			else if(report_model.equals("cxjdmodel")){
				template_name = "cxjdmodelfmjy.ftl";}
			else if(report_model.equals("xajdmodel")){
				template_name = "xajdmodelfmjy.ftl";}
			else if(report_model.equals("sqjdmodel")){
				template_name = "sqjdmodelfmjy.ftl";}
			else if(report_model.equals("fyxhjdmodel")){
				template_name = "fyxhjdmodelfmjy.ftl";}
			else if(report_model.equals("ynqsjdmodel")){
				template_name = "ynqsjdmodelfmjy.ftl";}
			String sample_code1 = sampelresultpis.get(0).get("samplecode")
					.toString();
			String sample_code2 = sampelresultpis.get(1).get("samplecode")
					.toString();
			// 通过上面查询出的样本编号查询出结果表中不相等的点位

			String sample_code1call = rdsJudicialSubCaseMapper
					.querysamplecallBySamplecode(sample_code1);
			String sample_code2call = rdsJudicialSubCaseMapper
					.querysamplecallBySamplecode(sample_code2);
			Map<String, Object> queryunmatchcodeParam1 = new HashMap<>();
			queryunmatchcodeParam1.put("case_code", case_code);
			queryunmatchcodeParam1.put("sample_code", sample_code1);

			Map<String, Object> queryunmatchcodeParam2 = new HashMap<>();
			queryunmatchcodeParam2.put("case_code", case_code);
			queryunmatchcodeParam2.put("sample_code", sample_code2);

			// 截取三个不同位点
			String cFString = rdsJudicialSubCaseMapper
					.querysampleunmathcodeBySamplecode(queryunmatchcodeParam1);
			String[] unmarhFStrings = cFString.split(",");
			if (unmarhFStrings.length > 3&& !"qdwfjdmodelfmjy.ftl".equals(template_name)&& !"gxjdmodel.ftl".equals(template_name)) {
				cFString = "";
				for (int i = 0; i < 3; i++) {
					cFString += unmarhFStrings[i];
					cFString += "、";
				}
				cFString = cFString.substring(0, cFString.length() - 1);
			}

			String cMString = rdsJudicialSubCaseMapper
					.querysampleunmathcodeBySamplecode(queryunmatchcodeParam2);
			String[] unmarhMStrings = cMString.split(",");
			if (unmarhMStrings.length > 3 && !"qdwfjdmodelfmjy.ftl".equals(template_name)&& !"gxjdmodel.ftl".equals(template_name)) {
				cMString = "";
				for (int i = 0; i < 3; i++) {
					cMString += unmarhMStrings[i];
					cMString += "、";
				}
				cMString = cMString.substring(0, cMString.length() - 1);
			}

			// 截取pi
			BigDecimal realpif = new BigDecimal(sampelresultpis.get(0)
					.get("pi").toString());
			double f = realpif.setScale(4, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			String t = "";
			String tt = "";
			if (f == 0.0 || f == 0.00 || f == 0.000 || f == 0.0000) {
				String aa = String.valueOf(realpif);
				String[] s = aa.split("E");
				if (s.length == 2) {
					double temp = Double.parseDouble(s[0]);
					t = new DecimalFormat("0.0000").format(temp);
				}
				if (s.length == 1) {
					tt = s[0];
				} else {
					tt = s[1];
				}
			} else if (f == 1.0E-4) {
				String a = String.valueOf(f);
				String[] s = a.split("E");
				String a1 = String.valueOf(realpif);
				String[] s1 = a1.split(".0000");
				double temp1 = Double.parseDouble(s1[1]);
				t = new DecimalFormat("0.0000").format(temp1);
				tt = s[1];
			}

			// 截取pi
			BigDecimal realpim = new BigDecimal(sampelresultpis.get(1)
					.get("pi").toString());
			double ff = realpim.setScale(4, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			String t2 = "";
			String tt2 = "";
			if (ff == 0.0 || ff == 0.00 || ff == 0.000 || ff == 0.0000) {
				String aa = String.valueOf(realpim);
				String[] s = aa.split("E");
				if (s.length == 2) {
					double temp = Double.parseDouble(s[0]);
					t2 = new DecimalFormat("0.0000").format(temp);
				}
				if (s.length == 1) {
					tt2 = s[0];
				} else {
					tt2 = s[1];
				}
			} else if (f == 1.0E-4) {
				String a = String.valueOf(f);
				String[] s = a.split("E");
				String a1 = String.valueOf(realpif);
				String[] s1 = a1.split(".0000");
				double temp1 = Double.parseDouble(s1[1]);
				t2 = new DecimalFormat("0.0000").format(temp1);
				tt2 = s[1];
			}
			if (sample_code1call.equals("father")) {
				sampelresultpibyparent.put("fatherpit1", t);
				sampelresultpibyparent.put("fatherpit2", tt);
				sampelresultpibyparent.put("motherpit1", t2);
				sampelresultpibyparent.put("motherpit2", tt2);
				sampelresultpibyparent.put("motherpi", sampelresultpis.get(1)
						.get("pi"));
				sampelresultpibyparent.put("fatherpi", sampelresultpis.get(0)
						.get("pi"));
				sampelresultpibyparent.put("fatherunmatchcode", cFString);
				sampelresultpibyparent.put("motherunmatchcode", cMString);
			} else if (sample_code2call.equals("father")) {
				sampelresultpibyparent.put("fatherpit1", t2);
				sampelresultpibyparent.put("fatherpit2", tt2);
				sampelresultpibyparent.put("motherpit1", t);
				sampelresultpibyparent.put("motherpit2", tt);
				sampelresultpibyparent.put("motherpi", sampelresultpis.get(0)
						.get("pi"));
				sampelresultpibyparent.put("fatherpi", sampelresultpis.get(1)
						.get("pi"));
				sampelresultpibyparent.put("fatherunmatchcode", cMString);
				sampelresultpibyparent.put("motherunmatchcode", cFString);
			}
		}
		// String template_name="zyjdmodelzk.ftl";

		/** 苏博医学谢井新模版 特殊化 **/
		if (rdsJudicialCaseInfoModel.getCase_receiver().equals("谢井新")
				&& rdsJudicialCaseInfoModel.getReport_model().equals(
						"sbyxmodel")) {
			template_name = "xjxmodel.ftl";
		}
		// 添加永康鉴定模板
		if (sub_case_codes.size() == 2
				&& (template_name.equals("ydjdmodel.ftl"))) {
			template_name = "ydjdmodel.ftl";
		}
		// 求实案例取值问题
		Date dateTime3 = new Date();
		int nianfday = 0;
		if (template_name.equals("qsjdmodel.ftl")) {
			if (!"".equals(rdsJudicialCaseInfoModel.getAccept_time())
					&& rdsJudicialCaseInfoModel.getAccept_time() != null) {
				dateTime3 = dateFormat.parse(rdsJudicialCaseInfoModel
						.getAccept_time());
				Calendar a = Calendar.getInstance();
				a.setTime(dateTime3);
				nianfday = a.get(Calendar.YEAR);
			} else if (!"".equals(rdsJudicialCaseInfoModel.getAddextnew_time())
					&& rdsJudicialCaseInfoModel.getAddextnew_time() != null) {
				dateTime3 = dateFormat.parse(rdsJudicialCaseInfoModel
						.getAddextnew_time());
				Calendar b = Calendar.getInstance();
				b.setTime(dateTime3);
				nianfday = b.get(Calendar.YEAR);
			}

		}
		/** 求实案例取值问题 end **/

		// 中信模板 三个人否定合一起
		// if(template_name.equals("zxjdmodel.ftl") ) {
		// //去除三个人否定的重复
		//
		// List<String> copu=new ArrayList<>();
		// Set<String> set = new HashSet<String>();
		// for (Map<String, Object> ls : wordtreesubcasinfo) {
		// String[] keysString= ls.get("sub_case_code").toString().split("_");
		// if (keysString.length==3) {
		// if (set.contains(ls.get("sample_code3").toString())) {
		// copu.add(ls.get("sample_code3").toString());
		// }else {
		// set.add(ls.get("sample_code3").toString());
		// }
		// }
		// }
		// for(int i=0;i<wordtreesubcasinfo.size();i++){
		// if(copu.contains(wordtreesubcasinfo.get(i).get("sample_code3").toString())){
		// wordtreesubcasinfo.remove(i);
		// copu.remove(i);
		// i--;
		// }
		// }
		//
		// List<String> a=new ArrayList<>();
		// for(int i=0;i<wordtreesubcasinfo.size();i++){//[Z2016031381_1_1,
		// Z2016031381_1_2, Z2016031381_2, Z2016031381_3]
		// a.add(wordtreesubcasinfo.get(i).get("sub_case_code").toString());
		// }
		// for(int i=0;i<sub_case_codes.size();i++){//[Z2016031381_1_2,
		// Z2016031381_2, Z2016031381_3]
		// if(!a.contains(sub_case_codes.get(i))){
		// sub_case_codes.remove(i);
		// i--;
		// }
		// }
		//
		//
		// }

		// 在场人员(报告鉴定人)
		List<RdsJudicialIdentifyModel> rdsJudicialIdentifyModels = rdsJudicialIdentifyPerService
				.queryIdentifyByCaseId(case_id);
		 String receiver_area = rdsJudicialCaseInfoModel.getReceiver_area();
		    String consignment_time = rdsJudicialCaseInfoModel.getConsignment_time();
		    consignment_time = consignment_time.substring(consignment_time.length() - 2, consignment_time.length() - 1);
		    if (report_model.startsWith("ydjd"))
		    {
		      rdsJudicialIdentifyModels.clear();
		      if ((receiver_area.startsWith("湖北")) || (receiver_area.startsWith("湖南")) || (receiver_area.startsWith("福建")) || (receiver_area.startsWith("浙江")))
		      {
		        RdsJudicialIdentifyModel rdsJudicialIdentifyModel = new RdsJudicialIdentifyModel();
		        rdsJudicialIdentifyModel.setIdentify_code("441716237003");
		        rdsJudicialIdentifyModel.setIdentify_per("黄开荣");
		        rdsJudicialIdentifyModels.add(rdsJudicialIdentifyModel);
		        RdsJudicialIdentifyModel rdsJudicialIdentifyModel1 = new RdsJudicialIdentifyModel();
		        rdsJudicialIdentifyModel1.setIdentify_code("441717237007");
		        rdsJudicialIdentifyModel1.setIdentify_per("陶霞");
		        rdsJudicialIdentifyModels.add(rdsJudicialIdentifyModel1);
		      }
		      else if ((consignment_time.equals("1")) || (consignment_time.equals("3")) || (consignment_time.equals("5")) || (consignment_time.equals("7")) || 
		        (consignment_time.equals("9")))
		      {
		        RdsJudicialIdentifyModel rdsJudicialIdentifyModel = new RdsJudicialIdentifyModel();
		        rdsJudicialIdentifyModel.setIdentify_code("441718237009");
		        rdsJudicialIdentifyModel.setIdentify_per("查松华");
		        rdsJudicialIdentifyModels.add(rdsJudicialIdentifyModel);
		        RdsJudicialIdentifyModel rdsJudicialIdentifyModel1 = new RdsJudicialIdentifyModel();
		        rdsJudicialIdentifyModel1.setIdentify_code("441717237007");
		        rdsJudicialIdentifyModel1.setIdentify_per("陶霞");
		        rdsJudicialIdentifyModels.add(rdsJudicialIdentifyModel1);
		      }
		      else if ((consignment_time.equals("0")) || (consignment_time.equals("2")) || (consignment_time.equals("4")) || (consignment_time.equals("6")) || 
		        (consignment_time.equals("8")))
		      {
		        RdsJudicialIdentifyModel rdsJudicialIdentifyModel = new RdsJudicialIdentifyModel();
		        rdsJudicialIdentifyModel.setIdentify_code("441718237008");
		        rdsJudicialIdentifyModel.setIdentify_per("邹辉祥");
		        rdsJudicialIdentifyModels.add(rdsJudicialIdentifyModel);
		        RdsJudicialIdentifyModel rdsJudicialIdentifyModel1 = new RdsJudicialIdentifyModel();
		        rdsJudicialIdentifyModel1.setIdentify_code("441716237005");
		        rdsJudicialIdentifyModel1.setIdentify_per("孙雪梅");
		        rdsJudicialIdentifyModels.add(rdsJudicialIdentifyModel1);
		      }
		    }
		// 子案例结果
		Map<String, Object> sub_results = new HashMap<>();

		Map<String, Object> sub_results_childe = new HashMap<>();

		// 案例使用的试剂Map，（reagent_name,）(reagent_name_ext) 加位点
		Map<String, Object> reagents = rdsJudicialReagentsService
				.queryReagentByCaseCode(case_code);

		// 查询模版和试剂对应设置好的试剂位点值（该值是对应试剂特定写好的值）
		Map<String, Object> queryOrderParam = new HashMap<>();
		queryOrderParam.put("reagent_name", reagents.get("reagent_name"));
		queryOrderParam
				.put("model", rdsJudicialCaseInfoModel.getReport_model());
		// 根据配置表tb_dic_print_gene查询排好序的试剂点位名，即tb_dic_print_gene表里name
		String[] ateliers = rdsJudicialReagentsService
				.queryOrder(queryOrderParam);
		// 根据配置表tb_dic_print_gene查询排好序的试剂点位信息，即tb_dic_print_gene表里所有值
		List<Map<String, String>> atelierstall = rdsJudicialReagentsService
				.newqueryOrder(queryOrderParam);

		// 用加位点试剂替换查询位点信息
		queryOrderParam.put("reagent_name", reagents.get("reagent_name_ext"));
		// 查询价位点试剂点位名，即tb_dic_print_gene表里name
		String[] ateliersExt = rdsJudicialReagentsService
				.queryOrder(queryOrderParam);
		// 查询价位点试剂点位信息，即tb_dic_print_gene表里所有值
		List<Map<String, String>> ateliersexttall = rdsJudicialReagentsService
				.newqueryOrder(queryOrderParam);

		/** 中信模板添加试剂分类讨论(中信不从系统出报告)，此处无用 start **/
		Map<String, Object> reagentName = new HashMap<String, Object>();
		reagentName.put("noext_reagentname", reagents.get("reagent_name"));
		if (reagents.get("reagent_name_ext") != null
				&& reagents.get("reagent_name_ext") != "") {
			reagentName
					.put("ext_reagentname", reagents.get("reagent_name_ext"));
		}
		/** 中信模板添加试剂分类讨论(中信不从系统出报告)，此处无用 end **/

		// 样本基本信息，如身份证号等
		List<RdsJudicialSampleInfoModel> rdsJudicialSampleInfoModels = rdsJudicialPrintMapper
				.getSampleInfo(case_id);
		// 基因位点
		/**青岛万方模板年龄start **/
		if(report_model.startsWith("qdwfjd")){
			for (RdsJudicialSampleInfoModel model : rdsJudicialSampleInfoModels) {
				String birth_date=model.getBirth_date();
				if(null!=birth_date&& !"".equals(birth_date)){
					model.setAge(Integer.parseInt( com.rds.code.utils.StringUtils
							.dateToSix(new Date()).substring(0, 4))-Integer.parseInt(birth_date.substring(0, 4)));
	
				}
			}
		}
		/**青岛万方模板出生日期和年龄end **/
		Map<String, Object> parmamapsbyalert = new HashMap<>();
		parmamapsbyalert.put("case_code", case_code);
		List<Map<String, Object>> sampelresultalierts = rdsJudicialSubCaseMapper
				.querySampleResultAlierts(parmamapsbyalert);
		// 遇到只有一方做加位点情况
		List<Map<String, Object>> sampelresultaliertsExtList = rdsJudicialSubCaseMapper
				.querySampleResultAliertsExt(parmamapsbyalert);

		List<Map<String, Object>> sampelresultaliertslist = new ArrayList<>();

		List<Map<String, Object>> sampelresultaliertsExtlist = new ArrayList<>();

		Map<String, Object> sampelresultinforbymother = new HashMap<>();
		Map<String, Object> sampelresultinforbyfather = new HashMap<>();
		Map<String, Object> sampelresultinforbychild = new HashMap<>();
		Map<String, Object> sampelresultinforbyparent = new HashMap<>();
		if (template_name.equals("zyjdmodelzkfmjy.ftl")||template_name.equals("xajdmodelfmjy.ftl")||template_name.equals("bjqjdmodelfmjy.ftl")
				||template_name.equals("qdwfjdmodelfmjy.ftl")||template_name.equals("ydjdmodelfmjy.ftl")
				||template_name.equals("cxjdmodelfmjy.ftl")||template_name.equals("ynqsjdmodelfmjy.ftl")||template_name.equals("sqjdmodelfmjy.ftl")||template_name.equals("fyxhjdmodelfmjy.ftl")) {

			for (RdsJudicialSampleInfoModel model : rdsJudicialSampleInfoModels) {
				if (model.getSample_call().equals("mother")) {
					sampelresultinforbymother.put("motherinfo", model);
				} else if (model.getSample_call().equals("father")) {
					sampelresultinforbyfather.put("fatherinfo", model);
				} else {
					sampelresultinforbychild.put("childinfo", model);
				}
			}
			for (String atelier : ateliers) {
				for (Map<String, Object> sampelresultaliert : sampelresultalierts) {
					if (sampelresultaliert.get("type").toString()
							.equals(atelier)) {
						Map<String, Object> sublist = new HashMap<>();
						sublist.put("type", sampelresultaliert.get("type"));
						// 一个样本一个点位的数据，如11,12
						String fatherstr = (String) sampelresultaliert
								.get("father");
						String motherstr = (String) sampelresultaliert
								.get("mother");
						String childstr = (String) sampelresultaliert
								.get("child");
						List<String> str = new ArrayList<String>();
						if("ynqsjdmodelfmjy.ftl".equals(template_name)||"qdwfjdmodelfmjy.ftl".equals(template_name)||"bjqjdmodelfmjy.ftl".equals(template_name)){
							str.add(fatherstr);
							str.add(motherstr);
							str.add(childstr);	
						}else{
						str.add(motherstr);
						str.add(childstr);
						str.add(fatherstr);}
						List<Map<String, String>> subdata = new LinkedList<>();
						for (int i = 0; i < str.size(); i++) {
							Map<String, String> oneData = new HashMap<>();
							// 一个样本一个点位的数据，如11,12
							String strs[] = str.get(i).split(",");
							oneData.put("value1", strs[0]);
							oneData.put("value2", strs.length == 1 ? strs[0]
									: strs[1]);
							subdata.add(oneData);
						}
						sublist.put("data", subdata);
						sampelresultaliertslist.add(sublist);
						
					}
					
					}
				if("AMEL".equals(atelier)&& "qdwfjdmodelfmjy.ftl".equals(template_name)){
					Map<String, Object> sublist = new HashMap<>();
                    List<Map<String, String>> subdata = new LinkedList<>();
                    sublist.put("type", "AMEL");
				for (RdsJudicialSampleInfoModel model : rdsJudicialSampleInfoModels) {
					
					Map<String, String> oneData = new HashMap<>();
					if (model.getSample_call().equals("father")) {
						oneData.put("value1", "X");
						oneData.put("value2", "Y");
						subdata.add(oneData);
					} else if (model.getSample_call().equals("mother")) {
						oneData.put("value1", "X");
						oneData.put("value2", "X");
						subdata.add(oneData);
					} else if(model.getSample_call().equals("son")){
						oneData.put("value1", "X");
						oneData.put("value2", "Y");
						subdata.add(oneData);
					}else if(model.getSample_call().equals("daughter")){
						oneData.put("value1", "X");
						oneData.put("value2", "X");
						subdata.add(oneData);
						}
					}
				sublist.put("data", subdata);
				sampelresultaliertslist.add(sublist);
				}

			}
			// 判断是否等于一条 一条则取出一方加位点数据 ateliersExt加位点数据点位信息
			if (sampelresultaliertsExtList.size() == 1) {
				String scall_code = sampelresultaliertsExtList.get(0)
						.get("scall").toString();// 称呼
				for (RdsJudicialSampleInfoModel model : rdsJudicialSampleInfoModels) {
					if (scall_code.equals("father")
							&& model.getSample_call().equals("father")) {
						sampelresultinforbyparent.put("parentinfo", model);
					} else if (scall_code.equals("mother")
							&& model.getSample_call().equals("mother")) {
						sampelresultinforbyparent.put("parentinfo", model);
					}
				}
				// 获取所有点位值
				List<Map<String, Object>> sampelpiinforesultaliertsExtList = rdsJudicialSubCaseMapper
						.querySampleResultpiinfoAliertsExt(parmamapsbyalert);
				for (Map<String, Object> sampelpiinforesultaliertsExt : sampelpiinforesultaliertsExtList) {
					if (ateliersExt.equals(sampelpiinforesultaliertsExt.get(
							"typeext").toString())) {
						// 是加位点数据
						Map<String, Object> sublist = new HashMap<>();
						sublist.put("type",
								sampelpiinforesultaliertsExt.get("typeext"));
						String parstr = (String) sampelpiinforesultaliertsExt
								.get("parent");
						String chrstr = (String) sampelpiinforesultaliertsExt
								.get("child");
						String pistr = sampelpiinforesultaliertsExt
								.get("piext").toString();
						List<String> str = new ArrayList<String>();
						str.add(parstr);
						str.add(chrstr);
						if (!ateliersExt.equals("AMEL")) {
							Map<String, Object> paramMap = new HashMap<String, Object>();
							paramMap.put("sub_case_code", case_code);
							paramMap.put("param_type", ateliersExt);
							sublist.put("pi", rdsJudicialCaseParamMapper
									.queryPiInfobyalient(paramMap).get(0)
									.getPi());
						}
						List<Map<String, String>> subdata = new LinkedList<>();
						for (int i = 0; i < str.size(); i++) {
							Map<String, String> oneData = new HashMap<>();
							// 一个样本一个点位的数据，如11,12
							String strs[] = str.get(i).split(",");
							oneData.put("value1", strs[0]);
							oneData.put("value2", strs.length == 1 ? strs[0]
									: strs[1]);
							subdata.add(oneData);
						}
						sublist.put("data", subdata);
						sampelresultaliertsExtlist.add(sublist);
					}
				}

			}
		}
		List<RdsJudicialSampleInfoModel> parantsSample = new LinkedList<RdsJudicialSampleInfoModel>();
		for (RdsJudicialSampleInfoModel model : rdsJudicialSampleInfoModels) {
			if (model.getSample_call().equals("mother")
					|| model.getSample_call().equals("father")) {
				parantsSample.add(model);
			}
		}

		// 拆分子案例，算亲权值
		List<Integer> paragraph = new ArrayList<>();
		// 存放需要计算亲权值的肯定案例，只存放单亲的
		List<Map<String, Object>> listPiAll = new ArrayList<>();

		// 存放需要计算亲权值的肯定案例，单亲双亲都存放
		List<Map<String, Object>> listPiAllForAll = new ArrayList<>();

		// 存放需要计算亲权值的肯定案例，只存放普通的
		List<Map<String, Object>> listPiAllreg = new ArrayList<>();

		// 存放需要计算亲权值的肯定案例，只存放加位点的
		List<Map<String, Object>> listPiAllregext = new ArrayList<>();

		// 子案例的结果
		Map<String, RdsJudicialCompareResultModel> rdsJudicialCompareResults = new HashMap<>();

		// 基本位点所用试剂的基因座字符串
		String originalStr = rdsJudicialReagentsService
				.queryOriginalReagentAtelier((String) reagents
						.get("reagent_name"));

		// 新增点位信息
		// Map<String,Object> sampleAllResultParams = new HashMap<>();
		// sampleAllResultParams.put("case_code",case_code);
		// sampleAllResultParams.put("ext_flag","N");
		// //非加位点
		// List<RdsJudicialSampleResultModel> rdsJudicialSampleAllResultModels =
		// rdsJudicialSampleService.queryRecordsByCaseCode(sampleAllResultParams);
		// //table里小孩模板放中间
		// if(!template_name.equals("ntjdmodel.ftl") ){
		// if(rdsJudicialSampleAllResultModels.size()==3){
		// putChildMid(rdsJudicialSampleAllResultModels);
		// }
		// }
		Map<String, Object> tallPi = new HashMap<>();
		/** 循环子案例信息 **/
		for (String sub_case_code : sub_case_codes) {
			// 该子案例下所用样本否定案例就是父母分别的
			List<String> sample_codes = new LinkedList<>();
			RdsJudicialSubCaseInfoModel rdsJudicialSubCaseInfoModel = rdsJudicialSubCaseMapper
					.querySubCaseRecord(sub_case_code);
			if (null != rdsJudicialSubCaseInfoModel.getCon_pi()) {
				if (rdsJudicialSubCaseInfoModel.getCon_pi().equals("M")) {
					tallPi.put("samplename", "母亲");
				} else if (rdsJudicialSubCaseInfoModel.getCon_pi().equals("F")) {
					tallPi.put("samplename", "父亲");
				} else {
					tallPi.put("samplename", "父亲和母亲皆突变");
				}
			}

			// 该子案例结果m1否定案例就是父母分别比对结果
			RdsJudicialCompareResultModel m1 = rdsJudicialSubCaseMapper
					.querySubCaseCompareResult(sub_case_code);

			List<RdsJudicialSampleInfoModel> temprdsJudicialSampleInfoModels = new LinkedList<>();
			temprdsJudicialSampleInfoModels.addAll(rdsJudicialSampleInfoModels);
			sample_codes.add(rdsJudicialSubCaseInfoModel.getSample_code1());
			if (rdsJudicialSubCaseInfoModel.getSample_code2() != null
					&& !"".equals(rdsJudicialSubCaseInfoModel.getSample_code2())) {
				sample_codes.add(rdsJudicialSubCaseInfoModel.getSample_code2());
			}
			sample_codes.add(rdsJudicialSubCaseInfoModel.getSample_code3());

			// 筛选子案例中的样本
			for (int i = 0; i < temprdsJudicialSampleInfoModels.size(); i++) {
				if (!sample_codes.contains(temprdsJudicialSampleInfoModels.get(
						i).getSample_code())) {
					temprdsJudicialSampleInfoModels.remove(i);
					i--;
				}
			}

			// 把小孩样本放中间
			List<RdsJudicialSampleInfoModel> sampleInfoModelsChildMid = new LinkedList<>();
			sampleInfoModelsChildMid.addAll(temprdsJudicialSampleInfoModels);
			if (sampleInfoModelsChildMid.size() == 3) {
				RdsJudicialSampleInfoModel temp = sampleInfoModelsChildMid
						.get(1);
				sampleInfoModelsChildMid
						.set(1, sampleInfoModelsChildMid.get(2));
				sampleInfoModelsChildMid.set(2, temp);
			}

			Map<String, Object> sampleResultParams = new HashMap<>();
			sampleResultParams.put("case_code", case_code);
			sampleResultParams.put("ext_flag", "N");
			sampleResultParams.put("sample_codes", sample_codes);
			sampleResultParams.put("queryBySub", "Y");
			// 非加位点
			List<RdsJudicialSampleResultModel> rdsJudicialSampleResultModels = rdsJudicialSampleService
					.queryRecordsByCaseCode(sampleResultParams);

			// table里小孩模板放中间
			if (!template_name.equals("mzjdmodel.ftl")
					&& !template_name.equals("syjdmodel.ftl")
					&& !template_name.equals("xjxmodel.ftl")
					&&!template_name.equals("qdwfjdmodel.ftl")
					&&!template_name.equals("gxjdmodel.ftl")
					&&!template_name.equals("ynqsjdmodel.ftl")) {
				if (rdsJudicialSampleResultModels.size() == 3) {
					putChildMid(rdsJudicialSampleResultModels);
				}
			}

			// 鼎城 二联体小孩放前面
			if (template_name.equals("dcjdmodel.ftl")
					|| template_name.equals("zyjdmodel.ftl")
					|| template_name.equals("xzjdmodel.ftl")
					|| template_name.equals("zyjdmodelzk.ftl")					
					|| template_name.equals("qsjdmodel.ftl")
					|| template_name.equals("zyjdmodeltq.ftl")
					|| template_name.equals("cxjdmodel.ftl")
					|| template_name.equals("ydjdmodel.ftl")
					|| template_name.equals("sqjdmodel.ftl")
					|| template_name.equals("fyxhjdmodel.ftl")
					|| template_name.equals("xajdmodel.ftl")
					) {
				if (rdsJudicialSampleResultModels.size() == 2) {
					putChildFrist(rdsJudicialSampleResultModels);
				}
				if (rdsJudicialSampleResultModels.size() == 3) {
					putMotherAndFather(rdsJudicialSampleResultModels);
				}
			}
			// if(rdsJudicialSampleResultModels.size()==3 &&
			// template_name.equals("zyjdmodel.ftl")){
			// putMotherAndFather(rdsJudicialSampleResultModels);
			// }
			// 加位点
			sampleResultParams.put("ext_flag", "Y");
			List<RdsJudicialSampleResultModel> rdsJudicialSampleResultExtModels = rdsJudicialSampleService
					.queryRecordsByCaseCode(sampleResultParams);

			// table里小孩模板放中间
			if (!template_name.equals("mzjdmodel.ftl")
					&& !template_name.equals("syjdmodel.ftl")
					&& !template_name.equals("xjxmodel.ftl")
					&&!template_name.equals("qdwfjdmodel.ftl")
					&&!template_name.equals("gxjdmodel.ftl")
					&&!template_name.equals("ynqsjdmodel.ftl")) {
				if (rdsJudicialSampleResultExtModels.size() == 3) {
					putChildMid(rdsJudicialSampleResultExtModels);

				}
			}
			// 鼎城 二联体小孩放前面
			if (template_name.equals("dcjdmodel.ftl")
					|| template_name.equals("zyjdmodel.ftl")
					|| template_name.equals("xzjdmodel.ftl")
					|| template_name.equals("zyjdmodelzk.ftl")					
					|| template_name.equals("qsjdmodel.ftl")
					|| template_name.equals("zyjdmodeltq.ftl")
					|| template_name.equals("ydjdmodel.ftl")
					|| template_name.equals("cxjdmodel.ftl")
					|| template_name.equals("sqjdmodel.ftl")
					|| template_name.equals("fyxhjdmodel.ftl")
					|| template_name.equals("xajdmodel.ftl")
					) {
				if (rdsJudicialSampleResultExtModels.size() == 2) {
					putChildFrist(rdsJudicialSampleResultExtModels);
				}
				if (rdsJudicialSampleResultExtModels.size() == 3) {
					putMotherAndFather(rdsJudicialSampleResultExtModels);
				}
			}
			// if(rdsJudicialSampleResultExtModels.size() == 3 &&
			// template_name.equals("zyjdmodel.ftl")){
			// putMotherAndFather(rdsJudicialSampleResultExtModels);
			// }

			// entry的集合，所有基因位，所有样本的数据
			List<Map<String, Object>> list = new ArrayList<>();
			for (String atelier : ateliers) {
				// 两个entry，第一个为基因位名称，第二个为该基因位下所有样本数据
				Map<String, Object> sublist = new HashMap<>();
				sublist.put("name", atelier);
				// N个样本一个点位的数据，如三人11,12 11,12 11，12

				List<Map<String, String>> subdata = new LinkedList<>();
				List<RdsJudicialPiInfoModel> rdsJudicialPiInfoModelpi = new ArrayList<>();
				if (template_name.equals("zyjdmodelzk.ftl")
						|| template_name.equals("zyjdmodeltq.ftl")
						|| template_name.equals("xzjdmodel.ftl")
						|| template_name.equals("jzjdmodel.ftl")//金证
						|| template_name.equals("ahzzjdmodel.ftl")
						|| template_name.equals("ydjdmodel.ftl")
						|| template_name.equals("ydjdmodeldghz.ftl")
						|| template_name.equals("ydjdmodelyx.ftl")
						|| template_name.equals("cxjdmodel.ftl")
						|| template_name.equals("bjqjdmodel.ftl")
						|| template_name.equals("qdwfjdmodel.ftl")
						|| template_name.equals("sqjdmodel.ftl")
						|| template_name.equals("fyxhjdmodel.ftl")
						|| template_name.equals("gxjdmodel.ftl")
						|| template_name.equals("xajdmodel.ftl")
						|| template_name.equals("ynqsjdmodel.ftl")
						) {
					if (!atelier.equals("AMEL")) {
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("sub_case_code", sub_case_code);
						paramMap.put("param_type", atelier);
						rdsJudicialPiInfoModelpi = rdsJudicialCaseParamMapper
								.queryPiInfobyalient(paramMap);
						sublist.put("pi", rdsJudicialPiInfoModelpi.get(0)
								.getPi());
					}
				}
				for (int i = 0; i < rdsJudicialSampleResultModels.size(); i++) {
					Map<String, String> oneData = new HashMap<>();
					// 一个样本一个点位的数据，如11,12
					String str = (String) rdsJudicialSampleResultModels.get(i)
							.getRecord().get(atelier);
					String strs[] = str.split(",");
					oneData.put("value1", strs[0]);
					oneData.put("value2", strs.length == 1 ? strs[0] : strs[1]);
					subdata.add(oneData);
				}
				sublist.put("data", subdata);
				list.add(sublist);
			}

			// 求实的加位点基因座分开展示
			int atelierExtCount = 0;
			List<Map<String, Object>> listext = new ArrayList<>();
			if (template_name.equals("qsjdmodel.ftl")
					|| template_name.equals("zyjdmodeltq.ftl")
					|| template_name.equals("zyjdmodelzk.ftl")
					|| template_name.equals("ydjdmodel.ftl")
					|| template_name.equals("ydjdmodeldghz.ftl")
					|| template_name.equals("ydjdmodelyx.ftl")
					|| template_name.equals("xzjdmodel.ftl")
					|| template_name.equals("ahzzjdmodel.ftl")
					|| template_name.equals("jzjdmodel.ftl")
					|| template_name.equals("cxjdmodel.ftl")
					|| template_name.equals("bjqjdmodel.ftl")
					|| template_name.equals("qdwfjdmodel.ftl")
					|| template_name.equals("gxjdmodel.ftl")
					|| template_name.equals("sqjdmodel.ftl")
					|| template_name.equals("fyxhjdmodel.ftl")
					|| template_name.equals("xajdmodel.ftl")
					|| template_name.equals("ynqsjdmodel.ftl")) {
				// 所有基因位，所有样本的数据,这部分是加位点
				if (m1.getExt_flag().equals("Y")) {
					out: for (String atelier : ateliersExt) {
						for (String str : ateliers) {
							if (atelier.equals(str)) {
								continue out;
							}
						}
						atelierExtCount++;
						// 两个entry，第一个为基因位名称，第二个为该基因位下所有样本数据
						Map<String, Object> sublist = new HashMap<>();
						sublist.put("name", atelier);
						// N个样本一个点位的数据，如三人11,12 11,12 11，12
						List<Map<String, String>> subdata = new LinkedList<>();
						List<RdsJudicialPiInfoModel> rdsJudicialPiInfoModelpi = new ArrayList<>();
						if (!atelier.equals("AMEL")
								&& !template_name.equals("qsjdmodel.ftl")) {
							Map<String, Object> paramMap = new HashMap<String, Object>();
							paramMap.put("sub_case_code", sub_case_code);
							paramMap.put("param_type", atelier);
							rdsJudicialPiInfoModelpi = rdsJudicialCaseParamMapper
									.queryPiInfobyalient(paramMap);
							sublist.put("pi", rdsJudicialPiInfoModelpi.get(0)
									.getPi());
						}
						for (int i = 0; i < rdsJudicialSampleResultExtModels
								.size(); i++) {
							// 一个样本一个点位的数据，如11,12
							Map<String, String> oneData = new HashMap<>();
							String str = (String) rdsJudicialSampleResultExtModels
									.get(i).getRecord().get(atelier);
							String pi = rdsJudicialSubCaseInfoModel.getPi();
							String strs[] = str.split(",");
							oneData.put("value1", strs[0]);
							oneData.put("value2", strs.length == 1 ? strs[0]
									: strs[1]);
							subdata.add(oneData);
						}
						sublist.put("data", subdata);
						listext.add(sublist);
					}
				}
			} else {
				// 所有基因位，所有样本的数据,这部分是加位点
				// List<Map<String,Object>> listext = new ArrayList();
				if (m1.getExt_flag().equals("Y")) {
					out: for (String atelier : ateliersExt) {
						for (String str : ateliers) {
							if (atelier.equals(str)) {
								continue out;
							}
						}
						atelierExtCount++;
						// 两个entry，第一个为基因位名称，第二个为该基因位下所有样本数据
						Map<String, Object> sublist = new HashMap<>();
						sublist.put("name", atelier);
						// N个样本一个点位的数据，如三人11,12 11,12 11，12
						List<Map<String, String>> subdata = new LinkedList<>();
						for (int i = 0; i < rdsJudicialSampleResultExtModels
								.size(); i++) {
							// 一个样本一个点位的数据，如11,12
							Map<String, String> oneData = new HashMap<>();
							String str = (String) rdsJudicialSampleResultExtModels
									.get(i).getRecord().get(atelier);
							String strs[] = str.split(",");
							oneData.put("value1", strs[0]);
							oneData.put("value2", strs.length == 1 ? strs[0]
									: strs[1]);
							subdata.add(oneData);
						}
						sublist.put("data", subdata);
						list.add(sublist);
					}
				}
			}

			String[] keysString = sub_case_code.split("_");
			Map<String, Object> one_result = new HashMap<>();
			one_result.put("numbers", keysString.length);
			one_result.put("list", list);
			one_result.put("listext", listext);
			one_result.put("atelierCount", ateliers.length);
			one_result.put("atelierExtCount", atelierExtCount);
			one_result.put("rdsJudicialSampleInfoModels",
					temprdsJudicialSampleInfoModels);
			one_result
					.put("sampleInfoModelsChildMid", sampleInfoModelsChildMid);

			one_result.put("name_reagent", m1.getReagent_name());
			one_result.put("ext_flage", m1.getExt_flag());
			sub_results.put(sub_case_code, one_result);

			rdsJudicialCompareResults.put(sub_case_code, m1);

			// 根据子案例存放pi信息
			Map<String, Object> listpi = new HashMap<>();
			Map<String, Object> listpireg = new HashMap<>();
			Map<String, Object> listpiregext = new HashMap<>();
			listpi.put("rdsJudicialCompareResultModel", m1);
			listpireg.put("rdsJudicialCompareResultModel", m1);
			List<RdsJudicialPiInfoModel> rdsJudicialPiInfoModels = rdsJudicialCaseParamMapper
					.queryPiInfo(sub_case_code);
			// 新增
			List<RdsJudicialPiInfoModel> rdsJudicialPiInfoModelsF = rdsJudicialCaseParamMapper
					.queryPiInfo(sub_case_code + "_1");

			List<RdsJudicialPiInfoModel> rdsJudicialPiInfoModelsM = rdsJudicialCaseParamMapper
					.queryPiInfo(sub_case_code + "_2");

			listpi.put("resultpp", rdsJudicialSubCaseInfoModel.getResult());
			listpi.put("sample_code1",
					rdsJudicialSubCaseInfoModel.getSample_code1());
			listpi.put("sample_code2", rdsJudicialSubCaseInfoModel
					.getSample_code2() == null ? ""
					: rdsJudicialSubCaseInfoModel.getSample_code2());
			listpi.put("sample_code3",
					rdsJudicialSubCaseInfoModel.getSample_code3());
			
			listpireg.put("resultpp", rdsJudicialSubCaseInfoModel.getResult());
			listpireg.put("sample_code1",
					rdsJudicialSubCaseInfoModel.getSample_code1());
			listpireg.put("sample_code2", rdsJudicialSubCaseInfoModel
					.getSample_code2() == null ? ""
					: rdsJudicialSubCaseInfoModel.getSample_code2());
			listpireg.put("sample_code3",
					rdsJudicialSubCaseInfoModel.getSample_code3());

			listpiregext.put("sample_code1",
					rdsJudicialSubCaseInfoModel.getSample_code1());
			listpiregext.put("sample_code2", rdsJudicialSubCaseInfoModel
					.getSample_code2() == null ? ""
					: rdsJudicialSubCaseInfoModel.getSample_code2());
			listpiregext.put("sample_code3",
					rdsJudicialSubCaseInfoModel.getSample_code3());
			// 鸣正三联体亲权值分开计算
			if (template_name.equals("mzjdmodel.ftl")
					&& parantsSample.size() == 2
					||template_name.equals("xajdmodel.ftl")
					&& parantsSample.size() == 2
					|| template_name.equals("zyjdmodeltq.ftl")
					&& parantsSample.size() == 2
					|| template_name.equals("zyjdmodel.ftl")
					&& parantsSample.size() == 2
					|| template_name.equals("qsjdmodel.ftl")
					&& parantsSample.size() == 2
					|| template_name.equals("zyjdmodelzk.ftl")
					&& parantsSample.size() == 2
					|| template_name.equals("bjqjdmodel.ftl")
					&& parantsSample.size() == 2
					|| template_name.equals("ydjdmodel.ftl")
					&& parantsSample.size() == 2
					|| template_name.equals("zyjdmodeltablereg.ftl")
					&& parantsSample.size() == 2
					|| template_name.equals("qdwfjdmodel.ftl")
					&& parantsSample.size() == 2
					|| template_name.equals("cxjdmodel.ftl")
					&& parantsSample.size() == 2
					|| template_name.equals("sqjdmodel.ftl")
					&& parantsSample.size() == 2
					|| template_name.equals("fyxhjdmodel.ftl")
					&& parantsSample.size() == 2
					|| template_name.equals("gxjdmodel.ftl")
					&& parantsSample.size() == 2
					|| template_name.equals("ynqsjdmodel.ftl")
					&& parantsSample.size() == 2) {
				String parent1pi = rdsJudicialSubCaseInfoModel.getParent1_pi();
				String parent2pi = rdsJudicialSubCaseInfoModel.getParent2_pi();
				if (!(parent1pi == null)) {
					BigDecimal realparent1pi = new BigDecimal(parent1pi);
					listpi.put("realparent1pi", realparent1pi);
					listpireg.put("realparent1pi", realparent1pi);
					if (!(parent2pi == null)) {
						BigDecimal realparent2pi = new BigDecimal(parent2pi);
						listpi.put("realparent2pi", realparent2pi);
						listpireg.put("realparent2pi", realparent2pi);
					}
				}

			}

			String pi = rdsJudicialSubCaseInfoModel.getPi();
			if (pi == null) {
				continue;
			}

			BigDecimal realpi = new BigDecimal(pi.toString());
			double f = realpi.setScale(4, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			if (f == 0.0 || f == 0.00 || f == 0.000 || f == 0.0000) {
				String a = String.valueOf(realpi);
				String[] s = a.split("E");
				String t = "";
				if (s.length == 2) {
					double temp = Double.parseDouble(s[0]);
					t = new DecimalFormat("0.0000").format(temp);
				}
				String realpifushu = "";
				if (s.length == 1) {
					realpifushu = "" + t + "*10^" + s[0] + "";
					listpi.put("t2", s[0]);
					listpireg.put("t2", s[0]);
					listpiregext.put("t2", s[0]);
				} else {
					realpifushu = "" + t + "*10^" + s[1] + "";
					listpi.put("t2", s[1]);
					listpireg.put("t2", s[1]);
					listpiregext.put("t2", s[1]);
				}
				listpi.put("t1", t);
				listpi.put("realpi", realpifushu);
				listpireg.put("t1", t);
				listpiregext.put("t1", t);
				listpireg.put("realpi", realpifushu);
				listpiregext.put("realpi", realpifushu);
			} else if (f == 1.0E-4) {
				String a = String.valueOf(f);
				String[] s = a.split("E");
				String t = "";
				String a1 = String.valueOf(realpi);
				String[] s1 = a1.split(".0000");
				double temp1 = Double.parseDouble(s1[1]);
				t = new DecimalFormat("0.0000").format(temp1);
				String realpifushu = "" + t + "*10^" + s[1] + "";
				listpi.put("t1", t);
				listpi.put("t2", s[1]);
				listpi.put("realpi", realpifushu);
				listpireg.put("t1", t);
				listpireg.put("t2", s[1]);
				listpireg.put("realpi", realpifushu);
				listpiregext.put("t1", t);
				listpiregext.put("t2", s[1]);
				listpiregext.put("realpi", realpifushu);

			}
			listpi.put("realpiold", new BigDecimal(pi));
			listpireg.put("realpiold", new BigDecimal(pi));
			listpiregext.put("realpiold", new BigDecimal(pi));
			
	if(m1.getFinal_result_flag().equals("passed")&&template_name.equals("ahzzjdmodel.ftl")){
		
				String pp = rdsJudicialSubCaseInfoModel.getPi();
				String ahzzpi=pp.substring(0,pp.indexOf("."));
			    String	aa=ahzzpi.substring(0,1);
			    String  bb=ahzzpi.substring(1,5);
			    String t=aa+"."+bb;
				int lenth=ahzzpi.length();
				lenth=lenth-1;
				listpi.put("t1", t);
				listpi.put("t2", lenth);
				
			}
			// listpi.put("realpi",String.valueOf(f));
			// pi小数点位置
			int place = pi.indexOf(".") - 1;
			int placea = place + 4;
			DecimalFormat dfPi = new DecimalFormat("#.00");
			DecimalFormat dfPi1 = new DecimalFormat("#.0");
			if ((template_name.equals("dcjdmodel.ftl"))
					|| template_name.equals("syjdmodel.ftl")) {
				realpi = realpi.movePointLeft(place);
				pi = dfPi1.format(realpi);
			} else {
				pi = dfPi.format(realpi);
			}
			String piString = "" + pi + "*10^" + place + "";
			if (rdsJudicialSubCaseInfoModel.getRcp() != null) {
				if (rdsJudicialSubCaseInfoModel.getRcp().equals("1.0")) {
					rdsJudicialSubCaseInfoModel.setRcp("0.9999999999");
				}
				listpi.put("rcp", rdsJudicialSubCaseInfoModel.getRcp());
				Double d = Double.parseDouble(rdsJudicialSubCaseInfoModel
						.getRcp().toString());
				String fString = String.valueOf((d) * 100);
				BigDecimal rcpb = new BigDecimal(fString);
				DecimalFormat rcpqsfomat = new DecimalFormat("#.0000000000");
				String rcpqsString = rcpqsfomat.format(rcpb) + "%";
				listpi.put("rcpqsString", rcpqsString);
			}

			listpi.put("pi", pi);
			listpi.put("place", place);
			listpi.put("piString", piString);
			
			listpireg.put("pi", pi);
			listpireg.put("place", place);
			listpireg.put("piString", piString);

			List data = new ArrayList();
			List dataF = new ArrayList();
			List dataM = new ArrayList();
			List datareg = new ArrayList();
			List dataregext = new ArrayList();
			if (rdsJudicialSubCaseInfoModel.getParent1_pi() == null) {
				for (String atelier : ateliers) {
					List sublist = new ArrayList();
					for (RdsJudicialPiInfoModel rdsJudicialPiInfoModel : rdsJudicialPiInfoModels) {
						if (atelier.equals(rdsJudicialPiInfoModel
								.getParam_type())) {
							sublist.add(atelier);
							String parent = rdsJudicialPiInfoModel.getParent();
							String child = rdsJudicialPiInfoModel.getChild();
							String[] parents = parent.split(",");
							String[] childs = child.split(",");
							sublist.add(parents[0]);
							sublist.add(parents.length == 1 ? parents[0]
									: parents[1]);
							sublist.add(childs[0]);
							sublist.add(childs.length == 1 ? childs[0]
									: childs[1]);
							sublist.add(rdsJudicialPiInfoModel.getGene1());
							sublist.add(rdsJudicialPiInfoModel.getGene2() == null ? ""
									: rdsJudicialPiInfoModel.getGene2());
							sublist.add(rdsJudicialPiInfoModel.getFunction());
							sublist.add(rdsJudicialPiInfoModel.getPi());
							if (rdsJudicialPiInfoModel.getParent2() != null) {
								String parent2 = rdsJudicialPiInfoModel
										.getParent2();
								String[] parents2 = parent2.split(",");
								sublist.add(parents2[0]);
								sublist.add(parents2.length == 1 ? parents2[0]
										: parents2[1]);
							}
						}
					}
					if (sublist.size() > 0) {
						data.add(sublist);
						datareg.add(sublist);
					}
				}
			} else {
				for (String atelier : ateliers) {
					List sublistF = new ArrayList();
					for (RdsJudicialPiInfoModel rdsJudicialPiInfoModel : rdsJudicialPiInfoModelsF) {
						if (atelier.equals(rdsJudicialPiInfoModel
								.getParam_type())) {
							sublistF.add(atelier);
							String parent = rdsJudicialPiInfoModel.getParent();
							String child = rdsJudicialPiInfoModel.getChild();
							String[] parents = parent.split(",");
							String[] childs = child.split(",");
							sublistF.add(parents[0]);
							sublistF.add(parents.length == 1 ? parents[0]
									: parents[1]);
							sublistF.add(childs[0]);
							sublistF.add(childs.length == 1 ? childs[0]
									: childs[1]);
							sublistF.add(rdsJudicialPiInfoModel.getGene1());
							sublistF.add(rdsJudicialPiInfoModel.getGene2() == null ? ""
									: rdsJudicialPiInfoModel.getGene2());
							sublistF.add(rdsJudicialPiInfoModel.getFunction());
							sublistF.add(rdsJudicialPiInfoModel.getPi());
							if (rdsJudicialPiInfoModel.getParent2() != null) {
								String parent2 = rdsJudicialPiInfoModel
										.getParent2();
								String[] parents2 = parent2.split(",");
								sublistF.add(parents2[0]);
								sublistF.add(parents2.length == 1 ? parents2[0]
										: parents2[1]);
							}
						}
					}

					if (sublistF.size() > 0) {
						dataF.add(sublistF);
						datareg.add(sublistF);
					}
				}
				for (String atelier : ateliers) {
					List sublistM = new ArrayList();
					for (RdsJudicialPiInfoModel rdsJudicialPiInfoModel : rdsJudicialPiInfoModelsM) {
						if (atelier.equals(rdsJudicialPiInfoModel
								.getParam_type())) {
							sublistM.add(atelier);
							String parent = rdsJudicialPiInfoModel.getParent();
							String child = rdsJudicialPiInfoModel.getChild();
							String[] parents = parent.split(",");
							String[] childs = child.split(",");
							sublistM.add(parents[0]);
							sublistM.add(parents.length == 1 ? parents[0]
									: parents[1]);
							sublistM.add(childs[0]);
							sublistM.add(childs.length == 1 ? childs[0]
									: childs[1]);
							sublistM.add(rdsJudicialPiInfoModel.getGene1());
							sublistM.add(rdsJudicialPiInfoModel.getGene2() == null ? ""
									: rdsJudicialPiInfoModel.getGene2());
							sublistM.add(rdsJudicialPiInfoModel.getFunction());
							sublistM.add(rdsJudicialPiInfoModel.getPi());
							if (rdsJudicialPiInfoModel.getParent2() != null) {
								String parent2 = rdsJudicialPiInfoModel
										.getParent2();
								String[] parents2 = parent2.split(",");
								sublistM.add(parents2[0]);
								sublistM.add(parents2.length == 1 ? parents2[0]
										: parents2[1]);
							}
						}
					}

					if (sublistM.size() > 0) {
						dataM.add(sublistM);
						datareg.add(sublistM);
					}
				}
			}
			listpireg.put("data", data);
			listpireg.put("dataF", dataF);
			listpireg.put("dataM", dataM);
			listPiAllreg.add(listpireg);

			if (m1.getExt_flag().equals("Y")) {
				if (rdsJudicialSubCaseInfoModel.getParent1_pi() == null) {
						out: for (String atelier : ateliersExt) {
							for (String str : ateliers) {
								if (atelier.equals(str)) {
									continue out;
								}
							}
						List sublist = new ArrayList();
						for (RdsJudicialPiInfoModel rdsJudicialPiInfoModel : rdsJudicialPiInfoModels) {
							if (atelier.equals(rdsJudicialPiInfoModel
									.getParam_type())) {
								sublist.add(atelier);
								String parent = rdsJudicialPiInfoModel
										.getParent();
								String child = rdsJudicialPiInfoModel
										.getChild();
								String[] parents = parent.split(",");
								String[] childs = child.split(",");
								sublist.add(parents[0]);
								sublist.add(parents.length == 1 ? parents[0]
										: parents[1]);
								sublist.add(childs[0]);
								sublist.add(childs.length == 1 ? childs[0]
										: childs[1]);
								sublist.add(rdsJudicialPiInfoModel.getGene1());
								sublist.add(rdsJudicialPiInfoModel.getGene2() == null ? ""
										: rdsJudicialPiInfoModel.getGene2());
								sublist.add(rdsJudicialPiInfoModel
										.getFunction());
								sublist.add(rdsJudicialPiInfoModel.getPi());
								if (rdsJudicialPiInfoModel.getParent2() != null) {
									String parent2 = rdsJudicialPiInfoModel
											.getParent2();
									String[] parents2 = parent2.split(",");
									sublist.add(parents2[0]);
									sublist.add(parents2.length == 1 ? parents2[0]
											: parents2[1]);
								}
							}
						}
						if (sublist.size() > 0) {
							data.add(sublist);
							dataregext.add(sublist);
						}
					}
				} else {
						out: for (String atelier : ateliersExt) {
							for (String str : ateliers) {
								if (atelier.equals(str)) {
									continue out;
								}
							}
						List sublistF = new ArrayList();
						for (RdsJudicialPiInfoModel rdsJudicialPiInfoModel : rdsJudicialPiInfoModelsF) {
							if (atelier.equals(rdsJudicialPiInfoModel
									.getParam_type())) {
								sublistF.add(atelier);
								String parent = rdsJudicialPiInfoModel
										.getParent();
								String child = rdsJudicialPiInfoModel
										.getChild();
								String[] parents = parent.split(",");
								String[] childs = child.split(",");
								sublistF.add(parents[0]);
								sublistF.add(parents.length == 1 ? parents[0]
										: parents[1]);
								sublistF.add(childs[0]);
								sublistF.add(childs.length == 1 ? childs[0]
										: childs[1]);
								sublistF.add(rdsJudicialPiInfoModel.getGene1());
								sublistF.add(rdsJudicialPiInfoModel.getGene2() == null ? ""
										: rdsJudicialPiInfoModel.getGene2());
								sublistF.add(rdsJudicialPiInfoModel
										.getFunction());
								sublistF.add(rdsJudicialPiInfoModel.getPi());
								if (rdsJudicialPiInfoModel.getParent2() != null) {
									String parent2 = rdsJudicialPiInfoModel
											.getParent2();
									String[] parents2 = parent2.split(",");
									sublistF.add(parents2[0]);
									sublistF.add(parents2.length == 1 ? parents2[0]
											: parents2[1]);
								}
							}
						}

						if (sublistF.size() > 0) {
							dataF.add(sublistF);
							datareg.add(sublistF);
						}
					}
						out: for (String atelier : ateliersExt) {
							for (String str : ateliers) {
								if (atelier.equals(str)) {
									continue out;
								}
							}
						List sublistM = new ArrayList();
						for (RdsJudicialPiInfoModel rdsJudicialPiInfoModel : rdsJudicialPiInfoModelsM) {
							if (atelier.equals(rdsJudicialPiInfoModel
									.getParam_type())) {
								sublistM.add(atelier);
								String parent = rdsJudicialPiInfoModel
										.getParent();
								String child = rdsJudicialPiInfoModel
										.getChild();
								String[] parents = parent.split(",");
								String[] childs = child.split(",");
								sublistM.add(parents[0]);
								sublistM.add(parents.length == 1 ? parents[0]
										: parents[1]);
								sublistM.add(childs[0]);
								sublistM.add(childs.length == 1 ? childs[0]
										: childs[1]);
								sublistM.add(rdsJudicialPiInfoModel.getGene1());
								sublistM.add(rdsJudicialPiInfoModel.getGene2() == null ? ""
										: rdsJudicialPiInfoModel.getGene2());
								sublistM.add(rdsJudicialPiInfoModel
										.getFunction());
								sublistM.add(rdsJudicialPiInfoModel.getPi());
								if (rdsJudicialPiInfoModel.getParent2() != null) {
									String parent2 = rdsJudicialPiInfoModel
											.getParent2();
									String[] parents2 = parent2.split(",");
									sublistM.add(parents2[0]);
									sublistM.add(parents2.length == 1 ? parents2[0]
											: parents2[1]);
								}
							}
						}

						if (sublistM.size() > 0) {
							dataM.add(sublistM);
							datareg.add(sublistM);
						}
					}

				}
			}
			listpiregext.put("data", dataregext);
			listPiAllregext.add(listpiregext);

			listpi.put("data", data);
			listpi.put("dataF", dataF);
			listpi.put("dataM", dataM);

			if ("".equals(listpi.get("sample_code2"))) {
				listPiAll.add(listpi);
			}
			listPiAllForAll.add(listpi);
			one_result.put("listpi", listpi);
			sub_results.put(sub_case_code, one_result);
		}

		// 往word中添加图谱
		List<String> urls = getSampleImg(case_code);
		List<String> imgEncodes = new LinkedList<>();
		if(urls.size()>0){
		for (String url : urls) {
			String filePath = PropertiesUtils.readValue(
					ConfigPath.getWebInfPath() + File.separatorChar + "spring"
							+ File.separatorChar + "properties"
							+ File.separatorChar + "config.properties",
					"judicial_path")
					+ url;
			File file = new File(filePath);
			if(file.exists()){
			String imgEncode = getImgEncoded(filePath);
			imgEncodes.add(imgEncode);}
		}}
		if (listPiAllForAll.size() > 0) {
			for (int i = 0; i < 21 - ((List) listPiAllForAll.get(0).get("data"))
					.size(); i++) {
				// 亲权值表底部再加一空行
				paragraph.add(i);
			}
		}
		String childsubcodeString = "";
		for (RdsJudicialCompareResultModel rdsJudicialCompareResultModel : rdsJudicialCompareResults
				.values()) {
			// 如果肯定则不用拼不匹配基因座
			if (StringUtils.isBlank(rdsJudicialCompareResultModel
					.getUnmatched_node()))
				continue;
			String unmatchedNode = "";

			if (reagents.get("reagent_name_ext") != null
					&& !((String) reagents.get("reagent_name_ext")).isEmpty()) {
				unmatchedNode = sortUnmatchedNode(
						rdsJudicialCompareResultModel.getUnmatched_node(),
						(String) reagents.get("reagent_name"),
						(String) reagents.get("reagent_name_ext"), "、");
			} else {
				unmatchedNode = sortUnmatchedNode(
						rdsJudicialCompareResultModel.getUnmatched_node(),
						(String) reagents.get("reagent_name"), null, "、");
			}
			if (unmatchedNode.contains("、")) {
				String[] unmatched_nodes = unmatchedNode.split("、");
				if (unmatched_nodes.length > 3&& !"jdjdmodel".equals(params.get("report_model").toString())&&!"qdwfjdmodel".equals(params.get("report_model").toString())&&!"gxjdmodel".equals(params.get("report_model").toString())) {
					unmatchedNode = "";
					for (int i = 0; i < 3; i++) {
						unmatchedNode += unmatched_nodes[i];
						unmatchedNode += "、";
					}
					unmatchedNode = unmatchedNode.substring(0,
							unmatchedNode.length() - 1);
				}
			}
			rdsJudicialCompareResultModel.setUnmatched_node(unmatchedNode);
			rdsJudicialCompareResultModel
					.setUnmatched_count_str((com.rds.code.utils.StringUtils
							.intToChinese(rdsJudicialCompareResultModel
									.getUnmatched_count())));

			// 根据不匹配基因型找到其值
			if (rdsJudicialCompareResultModel.getUnmatched_count() > 0) {
				String[] unmatcheds = rdsJudicialCompareResultModel
						.getUnmatched_node().split("、");

				// 三联体父母皆疑突变情况
				if ((unmatcheds.length > 1)
						&& (tallPi.size() > 0 ? tallPi.get("samplename")
								.equals("父亲和母亲皆突变") : false)) {
					List<String> parentGen1list = new ArrayList<String>();
					List<String> parentGen2list = new ArrayList<String>();
					List<String> childGenlist = new ArrayList<String>();
					List<String> piByGenlist = new ArrayList<String>();
					String childGenstr = "";
					for (String name : unmatcheds) {
						String sample_code = (String) params.get("case_code");
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("sample_code", sample_code);
						paramMap.put("name", name);
						List<RdsJudicialSampleResultDataHis> piForUnmatchNode = rdsJudicialSampleMapper
								.queryPiForUnmatchNode(paramMap);
						if (!piForUnmatchNode.isEmpty()
								&& piForUnmatchNode != null
								&& piForUnmatchNode.size() != 0) {
							for (RdsJudicialSampleResultDataHis piForUnmatchNodes : piForUnmatchNode) {
								if (piForUnmatchNodes.getSample_call().equals(
										"father")) {
									String parentGen1 = piForUnmatchNodes
											.getValue();
									String[] parentGen1Pi = parentGen1
											.split(",");
									if (parentGen1Pi.length == 1) {

										StringBuffer sb = new StringBuffer();
										sb.append(parentGen1Pi[0]);
										sb.append(",");
										sb.append(parentGen1Pi[0]);
										parentGen1 = sb.toString();
									}
									parentGen1list.add(parentGen1);
								} else if (piForUnmatchNodes.getSample_call()
										.equals("mother")) {
									String parentGen2 = piForUnmatchNodes
											.getValue();
									String[] parentGen2Pi = parentGen2
											.split(",");
									if (parentGen2Pi.length == 1) {
										StringBuffer sb = new StringBuffer();
										sb.append(parentGen2Pi[0]);
										sb.append(",");
										sb.append(parentGen2Pi[0]);
										parentGen2 = sb.toString();
									}
									parentGen2list.add(parentGen2);
								} else if ((piForUnmatchNodes.getSample_call()
										.equals("son") || piForUnmatchNodes
										.getSample_call().equals("daughter"))
										&& (piForUnmatchNodes.getSample_code()
												.equals(rdsJudicialCompareResultModel
														.getSample_code3()))) {
									String childGen = piForUnmatchNodes
											.getValue();
									String[] childGenPi = childGen.split(",");
									if (childGenPi.length == 1) {
										childGen = childGen.replace(",", "");
										childGenstr = childGen;
										StringBuffer sb = new StringBuffer();
										sb.append(childGenPi[0]);
										sb.append(",");
										sb.append(childGenPi[0]);
										childGen = sb.toString();
									} else {
										StringBuffer sb = new StringBuffer();
										sb.append(childGenPi[0]);
										sb.append("或");
										sb.append(childGenPi[1]);
										childGenstr = sb.toString();
									}
									childGenlist.add(childGen);
								}
								if (null == tallPi.get("samplename")) {
									tallPi.put("samplename", "父亲");
								}
							}
						}
						Map<String, Object> paramcasecode = new HashMap<String, Object>();
						paramcasecode.put("case_code", case_code);
						String reagent_name = rdsJudicialCaseParamMapper
								.queryregientgByKey(paramcasecode);
						Map<String, Object> parampis1 = new HashMap<String, Object>();
						parampis1.put("param_type", name);
						parampis1.put("reagent_name", reagent_name);
						String piByGen = "";
						Map<String, Object> parampis = new HashMap<>();
						parampis.put("sub_case_code", case_code);
						parampis.put("param_type", name);
						List<RdsJudicialPiInfoModel> rdsJudicialPiInfoModels = rdsJudicialCaseParamMapper
								.queryPiByKeybyone(parampis);
						if (rdsJudicialPiInfoModels != null
								&& rdsJudicialPiInfoModels.size() != 0) {
							if (rdsJudicialPiInfoModels.get(0).getParam_type()
									.equals(unmatcheds[1])) {
								piByGen = rdsJudicialPiInfoModels.get(1)
										.getPi();
							} else {
								piByGen = rdsJudicialPiInfoModels.get(0)
										.getPi();
							}
						} else {
							piByGen = rdsJudicialCaseParamMapper
									.queryGenMin(parampis1);
						}
						piByGenlist.add(piByGen);
					}
					tallPi.put("piByGen1", piByGenlist.get(0).toString());
					tallPi.put("piByGen2", piByGenlist.get(1).toString());
					tallPi.put("parentGen11", parentGen1list.get(0).toString());
					tallPi.put("parentGen21", parentGen2list.get(0).toString());
					tallPi.put("childGen11", childGenlist.get(0).toString());
					tallPi.put("parentGen12", parentGen1list.get(1).toString());
					tallPi.put("parentGen22", parentGen2list.get(1).toString());
					tallPi.put("childGen12", childGenlist.get(1).toString());
					tallPi.put("unmatched1", unmatcheds[0].toString());
					tallPi.put("unmatched2", unmatcheds[1].toString());
					// tallPi.put("childGenstr", childGenlist.toString());
				} else if ((unmatcheds.length > 1)
						&& (tallPi.size() > 0 ? !tallPi.get("samplename")
								.equals("父亲和母亲皆突变") : false)) {
					List<String> parentGen1list = new ArrayList<String>();
					List<String> parentGen2list = new ArrayList<String>();
					List<String> childGenlist = new ArrayList<String>();
					List<String> piByGenlist = new ArrayList<String>();
					String piByGen = "";
					String childGenstr = "";
					for (String name : unmatcheds) {
						String sample_code = (String) params.get("case_code");
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("sample_code", sample_code);
						paramMap.put("name", name);
						List<RdsJudicialSampleResultDataHis> piForUnmatchNode = rdsJudicialSampleMapper
								.queryPiForUnmatchNode(paramMap);
						if (!piForUnmatchNode.isEmpty()
								&& piForUnmatchNode != null
								&& piForUnmatchNode.size() != 0) {
							for (RdsJudicialSampleResultDataHis piForUnmatchNodes : piForUnmatchNode) {
								if (piForUnmatchNodes.getSample_call().equals(
										"father")) {
									String parentGen1 = piForUnmatchNodes
											.getValue();
									String[] parentGen1Pi = parentGen1
											.split(",");
									if (parentGen1Pi.length == 1) {

										StringBuffer sb = new StringBuffer();
										sb.append(parentGen1Pi[0]);
										sb.append(",");
										sb.append(parentGen1Pi[0]);
										parentGen1 = sb.toString();
									}
									parentGen1list.add(parentGen1);
								} else if (piForUnmatchNodes.getSample_call()
										.equals("mother")) {
									String parentGen2 = piForUnmatchNodes
											.getValue();
									String[] parentGen2Pi = parentGen2
											.split(",");
									if (parentGen2Pi.length == 1) {
										StringBuffer sb = new StringBuffer();
										sb.append(parentGen2Pi[0]);
										sb.append(",");
										sb.append(parentGen2Pi[0]);
										parentGen2 = sb.toString();
									}
									parentGen2list.add(parentGen2);
								} else if ((piForUnmatchNodes.getSample_call()
										.equals("son") || piForUnmatchNodes
										.getSample_call().equals("daughter"))
										&& (piForUnmatchNodes.getSample_code()
												.equals(rdsJudicialCompareResultModel
														.getSample_code3()))) {
									String childGen = piForUnmatchNodes
											.getValue();
									String[] childGenPi = childGen.split(",");
									if (childGenPi.length == 1) {
										childGen = childGen.replace(",", "");
										childGenstr = childGen;
										StringBuffer sb = new StringBuffer();
										sb.append(childGenPi[0]);
										sb.append(",");
										sb.append(childGenPi[0]);
										childGen = sb.toString();
									} else {
										StringBuffer sb = new StringBuffer();
										sb.append(childGenPi[0]);
										sb.append("或");
										sb.append(childGenPi[1]);
										childGenstr = sb.toString();
									}
									childGenlist.add(childGen);
									Map<String, Object> paramcasecode = new HashMap<String, Object>();
									paramcasecode.put("case_code", case_code);
									String reagent_name = rdsJudicialCaseParamMapper
											.queryregientgByKey(paramcasecode);
									Map<String, Object> parampis1 = new HashMap<String, Object>();
									parampis1.put("param_type", name);
									parampis1.put("reagent_name", reagent_name);
									Map<String, Object> parampis = new HashMap<>();
									parampis.put("sub_case_code", case_code);
									parampis.put("param_type", name);
									List<RdsJudicialPiInfoModel> rdsJudicialPiInfoModels = rdsJudicialCaseParamMapper
											.queryPiByKeybyone(parampis);
									if (rdsJudicialPiInfoModels != null
											&& rdsJudicialPiInfoModels.size() != 0) {
										for (RdsJudicialPiInfoModel rdsJudicialPiInfoModel : rdsJudicialPiInfoModels) {
											if (rdsJudicialPiInfoModel
													.getChild()
													.equals(childGen)) {
												piByGen = rdsJudicialPiInfoModel
														.getPi();
											}
										}
									} else {
										piByGen = rdsJudicialCaseParamMapper
												.queryGenMin(parampis1);
									}
									piByGenlist.add(piByGen);
								}
								if (null == tallPi.get("samplename")) {
									tallPi.put("samplename", "父亲");
								}
								tallPi.put("parentGen1",
										parentGen1list.toString());
								tallPi.put("parentGen2",
										parentGen2list.toString());
								tallPi.put("childGen", childGenlist.toString());
								tallPi.put("unmatched", unmatcheds.toString());
								tallPi.put("childGenstr",
										childGenlist.toString());
								tallPi.put("piByGen", piByGenlist.toString());
							}
						}
					}
				} else {
					// 突变只有一个点位值不一样
					String name = rdsJudicialCompareResultModel
							.getUnmatched_node();
					String sample_code = (String) params.get("case_code");
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("sample_code", sample_code);
					paramMap.put("name", name);
					List<RdsJudicialSampleResultDataHis> piForUnmatchNode = rdsJudicialSampleMapper
							.queryPiForUnmatchNode(paramMap);// 获取点位信息

					if (!piForUnmatchNode.isEmpty() && piForUnmatchNode != null
							&& piForUnmatchNode.size() != 0) {
						String parentGen1 = "";
						String parentGen2 = "";
						for (RdsJudicialSampleResultDataHis piForUnmatchNodes : piForUnmatchNode) {
							if (piForUnmatchNodes.getSample_call().equals(
									"father")) {
								parentGen1 = piForUnmatchNodes.getValue();
								String[] parentGen1Pi = parentGen1.split(",");
								if (parentGen1Pi.length == 1) {
									StringBuffer sb = new StringBuffer();
									sb.append(parentGen1Pi[0]);
									sb.append(",");
									sb.append(parentGen1Pi[0]);
									parentGen1 = sb.toString();
								}
								tallPi.put("parentGen1", parentGen1);
							} else if (piForUnmatchNodes.getSample_call()
									.equals("mother")) {
								parentGen2 = piForUnmatchNodes.getValue();
								String[] parentGen2Pi = parentGen2.split(",");
								if (parentGen2Pi.length == 1) {
									StringBuffer sb = new StringBuffer();
									sb.append(parentGen2Pi[0]);
									sb.append(",");
									sb.append(parentGen2Pi[0]);
									parentGen2 = sb.toString();
								}
								tallPi.put("parentGen2", parentGen2);
							} else {
								if (rdsJudicialCompareResultModel
										.getSample_code3().equals(
												piForUnmatchNodes
														.getSample_code()
														.toString())) {
									String childGen = piForUnmatchNodes
											.getValue();
									childsubcodeString = piForUnmatchNodes
											.getSample_code().toString();
									String piByGen = "";
									String childGenstr = "";
									String[] childGenPi = childGen.split(",");
									if (childGenPi.length == 1) {
										childGen = childGen.replace(",", "");
										childGenstr = childGen;
										StringBuffer sb = new StringBuffer();
										sb.append(childGenPi[0]);
										sb.append(",");
										sb.append(childGenPi[0]);
										childGen = sb.toString();
									} else {
										StringBuffer sb = new StringBuffer();
										sb.append(childGenPi[0]);
										sb.append("或");
										sb.append(childGenPi[1]);
										childGenstr = sb.toString();
									}
									for (String pi : childGenPi) {
										Map<String, Object> paramcasecode = new HashMap<String, Object>();
										paramcasecode.put("case_code",
												case_code);
										String reagent_name = rdsJudicialCaseParamMapper
												.queryregientgByKey(paramcasecode);
										Map<String, Object> parampis1 = new HashMap<String, Object>();
										// parampis1.put("param_name",pi);
										// parampis1.put("param_type",name);
										// parampis1.put("reagent_name",reagent_name);
										// piByGen
										// =rdsJudicialCaseParamMapper.queryGen(parampis);
										Map<String, Object> parampis = new HashMap<>();
										parampis.put("sub_case_code", case_code);
										parampis.put("param_type", name);
										List<RdsJudicialPiInfoModel> rdsJudicialPiInfoModels = rdsJudicialCaseParamMapper
												.queryPiByKeybyone(parampis);
										if (rdsJudicialPiInfoModels != null
												&& rdsJudicialPiInfoModels
														.size() != 0) {
											String genparent1 = rdsJudicialPiInfoModels
													.get(0).getParent();
											// String genparent2 =
											// rdsJudicialPiInfoModels.get(0).getParent2();
											String child = rdsJudicialPiInfoModels
													.get(0).getChild();
											String[] genparent11 = genparent1
													.split(",");
											// String[] genparent22 =
											// genparent2.split(",");
											String[] child2 = child.split(",");
											List<String> list = Arrays
													.asList(genparent11);
											if (null == tallPi
													.get("samplename")) {
												if (child2.length == 1) {
													if (list.contains(child2[0])) {
														tallPi.put(
																"samplename",
																"母亲");
													} else {
														tallPi.put(
																"samplename",
																"父亲");
													}
												} else {
													if (list.contains(child2[0])
															|| list.contains(child2[1])) {
														tallPi.put(
																"samplename",
																"母亲");
													} else {
														tallPi.put(
																"samplename",
																"父亲");
													}
												}
											}

											Map<String, Object> paramval = new HashMap<String, Object>();
											paramval.put("param_type", name);
											paramval.put("case_code",
													sample_code);
											if (tallPi.get("samplename")
													.toString().equals("父亲")) {
												paramval.put("sample_call",
														"father");
												String fString = rdsJudicialCaseParamMapper
														.queryValuebycode(paramval);
												/** 有问题 问题问题 2017-12-06 by yxb start**/
												for (RdsJudicialPiInfoModel rdsJudicialPiInfoModel : rdsJudicialPiInfoModels) {
													if ((rdsJudicialPiInfoModel
															.getParent()
															.toString()
															.equals(fString))
															&& (rdsJudicialPiInfoModel
																	.getChild()
																	.equals(piForUnmatchNodes
																			.getValue()
																			.toString()))) {
														piByGen = rdsJudicialPiInfoModel
																.getPi();
													}
												}
												/** 有问题 问题问题 2017-12-06 by yxb end**/
//												if ((rdsJudicialPiInfoModels
//														.get(0).getParent()
//														.toString()
//														.equals(fString))
//														&& (rdsJudicialPiInfoModels
//																.get(0)
//																.getChild()
//																.equals(piForUnmatchNodes
//																		.getValue()
//																		.toString()))) {
//													piByGen = rdsJudicialPiInfoModels
//															.get(0).getPi();
//												}

											} else if (tallPi.get("samplename")
													.toString().equals("母亲")) {
												paramval.put("sample_call",
														"mother");
												String mString = rdsJudicialCaseParamMapper
														.queryValuebycode(paramval);
												/** 有问题 问题问题2017-12-06 by yxb**/
												for (RdsJudicialPiInfoModel rdsJudicialPiInfoModel : rdsJudicialPiInfoModels) {
													if ((rdsJudicialPiInfoModel
															.getParent()
															.toString()
															.equals(mString))
															&& (rdsJudicialPiInfoModel
																	.getChild()
																	.equals(piForUnmatchNodes
																			.getValue()
																			.toString()))) {
														piByGen = rdsJudicialPiInfoModel
																.getPi();
													}
												}
												/** 有问题 问题问题 2017-12-06 by yxb**/
//												if ((rdsJudicialPiInfoModels
//														.get(0).getParent()
//														.toString()
//														.equals(mString))
//														&& (rdsJudicialPiInfoModels
//																.get(0)
//																.getChild()
//																.equals(piForUnmatchNodes
//																		.getValue()
//																		.toString()))) {
//													piByGen = rdsJudicialPiInfoModels
//															.get(0).getPi();
//												}

											}
										} else {
											parampis1.put("param_type", name);
											parampis1.put("reagent_name",
													reagent_name);
											piByGen = rdsJudicialCaseParamMapper
													.queryGenMin(parampis1);
										}
									}
									tallPi.put("piByGen", piByGen);
									tallPi.put("childGen", childGen);
									sub_results_childe.put("sample_code3",
											rdsJudicialCompareResultModel
													.getSample_code3()
													.toString());
									sub_results_childe.put("childGenstr",
											childGenstr);
									sub_results_childe
											.put("childGen", childGen);
									tallPi.put("childGenstr", childGenstr);
									tallPi.put("unmatched", name.toString());
								}
							}
						}
					}
				}
			}
		}

		dataMap.put("firstAtelier", ateliers[0]);
		if (ateliersExt.length > 0) {
			dataMap.put("firstAtelierExt", ateliersExt[0]);
			// dataMap.put("atelierExtCount",ateliersExt.length);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// dataMap.put("atelierCount",ateliers.length);
		dataMap.put("atelierstall", atelierstall);
		dataMap.put("ateliersexttall", ateliersexttall);

		dataMap.put("rdsJudicialCompareResults", rdsJudicialCompareResults);
		// dataMap.put("identifiedsNoId",identifiedsNoId);
		dataMap.put("sample_in_time",
				sdf.parse(rdsJudicialCaseInfoModel.getSample_in_time()));
		dataMap.put("case_code", case_code);
		dataMap.put("time", dateString);//改鉴定日期开始为审核结束节点
		//在线打印获取打印次数，识别参数print区别打印和下载功能
		dataMap.put("ydDateStart", ydDateStart);
		dataMap.put("ydDateEnd", ydDateEnd);
		if(params.containsKey("print")&& template_name.equals("zyjdmodelzk.ftl")||params.containsKey("print")&& template_name.equals("zyjdmodelzkfmjy.ftl")){
			int print=(int) params.get("print");
			if(0==print){dataMap.put("print",1);}
			else dataMap.put("print",3);
		}
	     else if(params.containsKey("print")&& template_name.equals("sqjdmodel.ftl")){
	    	 int print=(int) params.get("print");
	    	 if(0==print){dataMap.put("print",1);}
	    	 else dataMap.put("print",2);
	    }
	     else if(params.containsKey("print")&& template_name.equals("sbyxmodel.ftl")){
	    	 int print=(int) params.get("print");
	    	 if(0==print){dataMap.put("print",1);}
	    	 else  dataMap.put("print",2);
	     }else{ dataMap.put("print",1);}
		dataMap.put("sub_case_codes", sub_case_codes);
		dataMap.put("rdsJudicialCaseInfoModel", rdsJudicialCaseInfoModel);
		dataMap.put("sub_results", sub_results);
		System.out.println("sub_results++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+sub_results);
		dataMap.put("sub_results_childe", sub_results_childe);
		dataMap.put("rdsJudicialIdentifyModels", rdsJudicialIdentifyModels);
		dataMap.put("listPiAll", listPiAll);
		dataMap.put("listPiAllreg", listPiAllreg);
		dataMap.put("listPiAllregext", listPiAllregext);
		dataMap.put("listPiAllForAll", listPiAllForAll);
		dataMap.put("paragraph", paragraph);
		dataMap.put("imgEncodes", imgEncodes);
		dataMap.put("tallPi", tallPi);

		/** 求实鉴定 专用 **/
		dataMap.put("nianfday", nianfday);
		/** 求实鉴定 专用 **/

		dataMap.put("sampeltypeformz", sampeltypeformz);
		// 新增
		dataMap.put("sampelresultinforbymother", sampelresultinforbymother);
		dataMap.put("sampelresultinforbyfather", sampelresultinforbyfather);
		dataMap.put("sampelresultinforbychild", sampelresultinforbychild);
		dataMap.put("sampelresultinforbyparent", sampelresultinforbyparent);
		dataMap.put("sampelresultpibyparent", sampelresultpibyparent);
		dataMap.put("sampelresultalierts", sampelresultalierts);
		dataMap.put("sampelresultaliertslist", sampelresultaliertslist);
		dataMap.put("sampelresultaliertsExtlist", sampelresultaliertsExtlist);
		dataMap.put("wordtreesubcasinfo", wordtreesubcasinfo);
		/** 中信模版使用（中信案例不由系统出报告，此处无用） **/

		dataMap.put("reagent_name", reagents);
		dataMap.put("reagent_name", reagents.get("reagent_name"));
		dataMap.put("reagent_name_ext", reagents.get("reagent_name_ext"));
		dataMap.put("reagentName", reagentName);

		// dataMap.put("rdsJudicialSampleAllResultModels",rdsJudicialSampleAllResultModels);
		dataMap.put("rdsJudicialSampleInfoModels", rdsJudicialSampleInfoModels);
		createparams.put("dataMap", dataMap);
		createparams.put("template_name", template_name);
		createparams.put("file_name", file_name);
		create(createparams);
	}

	private int inter(Object object) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void create(Map<String, Object> params) throws IOException,
			TemplateException {
		// 要填入模本的数据文件
		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		Map<String, Object> dataMap = (Map<String, Object>) params
				.get("dataMap");
		configuration.setClassForTemplateLoading(this.getClass(),
				"/com/rds/judicial/template");
		Template t = null;
		// test.ftl为要装载的模板
		t = configuration.getTemplate((String) params.get("template_name"));
		// 输出文档路径及名称
		String filepath = (String) params.get("file_name");
		File outFile = new File(filepath);
		File directory = new File(filepath.substring(0,
				filepath.lastIndexOf(File.separator)));
		if (!directory.exists())
			FileUtils.createFolder(filepath.substring(0,
					filepath.lastIndexOf(File.separator)));
		Writer out = null;
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				outFile), "UTF-8"));
		try{
		t.process(dataMap, out);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

	@Override
	public List<RdsJudicialIdentifyPer> getIdentifyPer(String case_id) {
		return rdsJudicialPrintMapper.getIdentifyPer(case_id);
	}

	private String getImgEncoded(String filePath) throws Exception {
		BufferedImage bi = null;
		bi = ImageIO.read(new File(filePath));// 获取源图片
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, "jpg", baos);// 保存生成图片
		byte[] bytes = baos.toByteArray();// 获取内存缓冲区中的数据
		BASE64Encoder encoder = new BASE64Encoder();// 加密
		return encoder.encode(bytes).trim();
	}

	private void putChildMid(
			List<RdsJudicialSampleResultModel> rdsJudicialSampleResultModels) {
		RdsJudicialSampleResultModel temp = rdsJudicialSampleResultModels
				.get(1);// 母亲
		rdsJudicialSampleResultModels.set(1,
				rdsJudicialSampleResultModels.get(2));// 小孩
		rdsJudicialSampleResultModels.set(2, temp);
	}

	private void putChildFrist(
			List<RdsJudicialSampleResultModel> rdsJudicialSampleResultModels) {
		RdsJudicialSampleResultModel temp = rdsJudicialSampleResultModels
				.get(0);// 母亲
		rdsJudicialSampleResultModels.set(0,
				rdsJudicialSampleResultModels.get(1));// 小孩
		rdsJudicialSampleResultModels.set(1, temp);
	}

	private void putMotherAndFather(
			List<RdsJudicialSampleResultModel> rdsJudicialSampleResultModels) {
		RdsJudicialSampleResultModel temp = rdsJudicialSampleResultModels
				.get(0);// 父亲
		rdsJudicialSampleResultModels.set(0,
				rdsJudicialSampleResultModels.get(2));// 母亲
		rdsJudicialSampleResultModels.set(2, temp);
	}

	// 正泰小孩按照ABCD排列，其他地方按照儿子在女儿前
	private List sortSampleInfo(
			List<RdsJudicialSampleInfoModel> rdsJudicialSampleInfoModels,
			List<RdsJudicialSampleResultModel> rdsJudicialSampleResultModels,
			List<RdsJudicialSampleResultModel> rdsJudicialSampleResultExtModels) {

		List<RdsJudicialSampleInfoModel> parents_info = new LinkedList<>();
		List<RdsJudicialSampleResultModel> parents_result = new LinkedList<>();
		List<RdsJudicialSampleResultModel> parents_result_ext = new LinkedList<>();
		List<RdsJudicialSampleInfoModel> children_info = new LinkedList<>();
		List<RdsJudicialSampleResultModel> children_result = new LinkedList<>();
		List<RdsJudicialSampleResultModel> children_result_ext = new LinkedList<>();
		if (rdsJudicialSampleInfoModels.get(1).getSample_call()
				.equals("mother")) {
			parents_info = rdsJudicialSampleInfoModels.subList(0, 2);
			parents_result = rdsJudicialSampleResultModels.subList(0, 2);
			if (!rdsJudicialSampleResultExtModels.isEmpty()) {
				parents_result_ext = rdsJudicialSampleResultExtModels.subList(
						0, 2);
				children_result_ext = rdsJudicialSampleResultExtModels.subList(
						2, rdsJudicialSampleResultExtModels.size());

			}
			children_info = rdsJudicialSampleInfoModels.subList(2,
					rdsJudicialSampleInfoModels.size());
			children_result = rdsJudicialSampleResultModels.subList(2,
					rdsJudicialSampleResultModels.size());
		} else {
			parents_info = rdsJudicialSampleInfoModels.subList(0, 1);
			parents_result = rdsJudicialSampleResultModels.subList(0, 1);
			if (!rdsJudicialSampleResultExtModels.isEmpty()) {
				parents_result_ext = rdsJudicialSampleResultExtModels.subList(
						0, 1);
				children_result_ext = rdsJudicialSampleResultExtModels.subList(
						1, rdsJudicialSampleResultExtModels.size());
			}
			children_info = rdsJudicialSampleInfoModels.subList(1,
					rdsJudicialSampleInfoModels.size());
			children_result = rdsJudicialSampleResultModels.subList(1,
					rdsJudicialSampleResultModels.size());
		}
		Map<Character, RdsJudicialSampleInfoModel> children_info_map = new TreeMap<>();
		Map<Character, RdsJudicialSampleResultModel> children_result_map = new TreeMap<>();
		Map<Character, RdsJudicialSampleResultModel> children_result_ext_map = new TreeMap<>();
		for (int i = 0; i < children_info.size(); i++) {
			children_info_map.put(
					children_info
							.get(i)
							.getSample_code()
							.charAt(children_info.get(i).getSample_code()
									.length() - 1), children_info.get(i));
			children_result_map.put(
					children_result
							.get(i)
							.getSample_code()
							.charAt(children_result.get(i).getSample_code()
									.length() - 1), children_result.get(i));

		}
		for (int i = 0; i < children_result_ext.size(); i++) {
			if (!children_result_ext.isEmpty()) {
				children_result_ext_map.put(
						children_result_ext
								.get(i)
								.getSample_code()
								.charAt(children_result_ext.get(i)
										.getSample_code().length() - 1),
						children_result_ext.get(i));
			}
		}
		Iterator<RdsJudicialSampleInfoModel> it1 = children_info_map.values()
				.iterator();
		Iterator<RdsJudicialSampleResultModel> it2 = children_result_map
				.values().iterator();
		Iterator<RdsJudicialSampleResultModel> it3 = children_result_ext_map
				.values().iterator();
		for (int i = 0; i < children_info.size(); i++) {
			children_info.set(i, it1.next());
			children_result.set(i, it2.next());
		}
		for (int i = 0; i < children_result_ext.size(); i++) {
			if (!children_result_ext.isEmpty()) {
				children_result_ext.set(i, it3.next());
			}
		}
		List list = new LinkedList();
		parents_info.addAll(children_info);
		parents_result.addAll(children_result);
		parents_result_ext.addAll(children_result_ext);
		list.add(parents_info);
		list.add(parents_result);
		list.add(parents_result_ext);
		return list;

	}

	@Override
	public RdsJudicialResponse getChangePrintCaseInfo(Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialCaseInfoModel> caseInfoModels = rdsJudicialPrintMapper
				.getChangePrintCaseInfo(params);
		int count = rdsJudicialPrintMapper.countChangePrintCaseInfo(params);
		response.setItems(caseInfoModels);
		response.setCount(count);
		return response;
	}

	@Override
	public RdsJudicialResponse getChangePrintCaseInfoForWord(
			Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialCaseInfoModel> caseInfoModels = rdsJudicialPrintMapper
				.getChangePrintCaseInfoForWord(params);
		int count = rdsJudicialPrintMapper
				.countChangePrintCaseInfoForWord(params);
		response.setItems(caseInfoModels);
		response.setCount(count);
		return response;
	}

}
