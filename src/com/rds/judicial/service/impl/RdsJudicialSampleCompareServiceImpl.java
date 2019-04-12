package com.rds.judicial.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Setter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialCaseAttachmentMapper;
import com.rds.judicial.mapper.RdsJudicialPhoneMapper;
import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.mapper.RdsJudicialSampleRelayMapper;
import com.rds.judicial.model.AlgReturnValueModel;
import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;
import com.rds.judicial.model.RdsJudicialCaseExceptionModel;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialPiInfoModel;
import com.rds.judicial.model.RdsJudicialSampleResultModel;
import com.rds.judicial.model.RdsJudicialSubCaseInfoModel;
import com.rds.judicial.service.RdsJudicialCaseExceptionService;
import com.rds.judicial.web.controller.AlgMain;
import com.rds.upc.model.RdsUpcUserModel;

/**
 * @author lys
 * @className
 * @description
 * @date 2015/4/10
 */
@Service("RdsJudicialSampleCompareServiceImpl")
public class RdsJudicialSampleCompareServiceImpl extends
		RdsJudicialSampleCompareServiceBase {
	@Autowired
	private RdsJudicialCaseExceptionService rdsJudicialCaseExceptionService;
	@Autowired
	private RdsJudicialRegisterMapper rdsJudicialRegisterMapper;
	@Autowired
	private RdsJudicialSampleRelayMapper RdsJudicialSampleRelayMapper;
	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;
	
	@Autowired
	private RdsJudicialCaseAttachmentMapper judicialCaseAttachmentMapper;
	
	@Autowired
	private RdsJudicialPhoneMapper pMapper;

	// 使用新算法模板
	private final String[] reportModel = { "zyjdmodel", "zyjdmodeltq",
			"zyjdmodelzk", "xzjdmodel", "cxjdmodel","dcjdmodel","dnmodel","ltjdmodel",
			"ntjdmodel","mzjdmodel", "syjdmodel","wsjdmodel","ydjdmodel","ztjdmodel",
			"zxjdmodel","zzjdmodel","qsjdmodel", "ydjdmodelyx", "jdjdmodel","qdwfjdmodel",
			"bjqjdmodel","sqjdmodel","gxjdmodel","xajdmodel","jzjdmodel","fyxhjdmodel","ahzzjdmodel","ynqsjdmodel"};

	private List<String> reportModelTempList = Arrays.asList(reportModel);

	private static Logger logger = Logger
			.getLogger(RdsJudicialSampleCompareServiceBase.class);

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public String beginCompare(String experiment_no, String laboratory_no,
			String reagent_name, String identify_id, String exception_per,
			RdsUpcUserModel user) throws Exception {
		Map<String, Object> paramsExperiment = new HashMap<String, Object>();
		paramsExperiment.put("experiment_no", experiment_no);
		paramsExperiment.put("laboratory_no", laboratory_no);
		paramsExperiment.put("reagent_name", reagent_name);
		// 查看该次实验是否为加位点
		String ext_flag = rdsJudicialReagentsService
				.queryExtFlag(paramsExperiment);
		// 更新该次实验所用试剂
		rdsJudicialExperimentMapper.updateReagentName(paramsExperiment);
		// 根据实验编号找出该次实验样本集合，并对样本数据进行填充
		List<RdsJudicialSampleResultModel> list = rdsJudicialSampleService
				.queryByExperimentNo(paramsExperiment);

		// 交叉比对
		crossCompare(list, laboratory_no);

		// 历史比对
		historyCompare(list, laboratory_no);

		// 该次实验有多少case
		Set<String> set = new HashSet<String>();

		// 通过case找到分case
		List<String> case_ids = new ArrayList<>();
		for (RdsJudicialSampleResultModel model : list) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("case_code", model.getCase_code());
			// 查询没有结果的子案例
			List<String> subCaseCode = rdsJudicialSubCaseMapper
					.querySubCase(params);

			/** update by yuanxiaobo 20170417 start **/
			params.put("laboratory_no", laboratory_no);
			// 更新案例的实验室编号
			//rdsJudicialSubCaseMapper.updateCaseLaboratoryNo(params);
			/** update by yuanxiaobo 20170417 start **/

			for (String sub_case : subCaseCode) {
	
				case_ids.add(sub_case);
				set.add(sub_case);
			}
		}
	    Map<String,Object> mapCheck = new HashMap<>();
        mapCheck.put("sub_case_code", case_ids);
        List<Map<String,Object>> listcaseids=rdsJudicialSubCaseMapper.queryCaseIdBySubCaseCode1(mapCheck);
		
		// 添加鉴定人信息
		for (Map<String, Object> map  : listcaseids) {
			
			String[] identify_ids = identify_id.split(",");
			// 先查询之前该案例是否有鉴定人 有则删除
			int rdsJudicialIdentifyModels = rdsJudicialIdentifyPerService
					.queryIdentifynameByCaseId(map.get("case_id").toString());
			if (rdsJudicialIdentifyModels > 0) {
				Map<String, Object> params = new HashMap<>();
				params.put("case_id", map.get("case_id").toString());
				rdsJudicialIdentifyPerMapper.deleteIdentifyByCaseid(params);
			}
			for (int i = 0; i < identify_ids.length; i++) {
				Map<String, Object> identifyPer = new HashMap<>();
				identifyPer.put("trans_date", new Date());
				identifyPer.put("identify_id", identify_ids[i]);
				identifyPer.put("case_id", map.get("case_id").toString());
				rdsJudicialIdentifyPerService.insertCaseToIdentify(identifyPer);
			}
		}

		// 基本点位比对
		if ("N".equals(ext_flag)) {
			for (String sub_case_code : set) {
				if (currentCountUnmatchedNode(sub_case_code) != 1
						&& currentCountUnmatchedNode(sub_case_code) != 2
						&& !queryNeedExt(sub_case_code).equals("Y")) {
					validateSample(sub_case_code, "N", laboratory_no,
							exception_per, user);
				}
			}
			// 加位点比对
		} else {
			for (String sub_case_code : set) {
				if (currentCountUnmatchedNode(sub_case_code) == 1
						|| currentCountUnmatchedNode(sub_case_code) == 2
						|| queryNeedExt(sub_case_code).equals("Y")) {
					validateSample(sub_case_code, "Y", laboratory_no,
							exception_per, user);
				}
			}
		}
		return "success";
	}

	/**
	 *
	 * @param sub_case_code
	 * @param exception_per
	 * @throws Exception
	 */
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public Map<Boolean, String> validateSample(String sub_case_code,
			String ext_flag, String laboratory_no, String exception_per,
			RdsUpcUserModel user) throws Exception {
		logger.debug("sub_case_code validate=============================================="
				+ sub_case_code);
		// 返回结果
		Map<Boolean, String> result = new HashMap<Boolean, String>();
		// 根据sub_case_code查询样本编号，最多3个
		RdsJudicialSubCaseInfoModel rdsJudicialSubCaseInfoModel = rdsJudicialSubCaseMapper
				.querySubCaseRecord(sub_case_code);
		// 存放code123 分别是父母子
		List<String> sample_codes = new LinkedList<String>();// [Z2015091509F,
																// Z2015091509M,
																// Z2015091509SC]
		sample_codes.add(rdsJudicialSubCaseInfoModel.getSample_code1());
		if (rdsJudicialSubCaseInfoModel.getSample_code2() != null)
			sample_codes.add(rdsJudicialSubCaseInfoModel.getSample_code2());
		sample_codes.add(rdsJudicialSubCaseInfoModel.getSample_code3());

		// 用于存放需比对的样本对象
		List<RdsJudicialSampleResultModel> toCompare = new LinkedList<RdsJudicialSampleResultModel>();
		Map<String, Object> params = new HashMap<String, Object>();
		List<RdsJudicialSampleResultModel> enableResult = null;
		if (ext_flag.equals("N")) {
			for (String sample_code : sample_codes) {
				params.put("enable_flag", "Y");
				params.put("ext_flag", "N");
				params.put("sample_code", sample_code);
				params.put("case_id", rdsJudicialSubCaseMapper
						.queryCaseIdBySubCaseCode(sub_case_code));
				// 查询有效样本编号是否唯一
				enableResult = rdsJudicialSampleMapper
						.queryBySampleCode(params);
				// 存放20A数据信息
				Set<Map<String, Object>> atelierData = new HashSet();
				// 根据样本编号，有效标识，以及加位点标识查询唯一实验编号
				List<String> experiment_nos = rdsJudicialSampleMapper
						.queryExperiment(params);
				for (String experiment_no : experiment_nos) {
					Map<String, Object> atelierDataParam = new HashMap<>();
					atelierDataParam.put("sample_code", sample_code);
					atelierDataParam.put("experiment_no", experiment_no);
					atelierDataParam.put("laboratory_no", laboratory_no);
					atelierData.add(rdsJudicialSampleService
							.queryOneRecordData(atelierDataParam));
				}

				if (enableResult == null || enableResult.size() == 0
						|| atelierData == null || atelierData.size() == 0) {
					result.put(false, "该案例缺少样本数据，请核实");
					return result;
				}
				// 有效样本编号数量大于1
				else if (atelierData.size() > 1) {
					rdsJudicialExceptionService.insert("A004",
							rdsJudicialSubCaseMapper
									.queryMainCaseCode(sub_case_code),
							sample_code, "", laboratory_no);
					result.put(false, "该案例同一样本号下存在不同样本数据，请核实");
					return result;
				}
				for (Map map : atelierData) {
					enableResult.get(0).setRecord(map);
				}
				// 需要进行比对的样本
				if (currentCountUnmatchedNode(sub_case_code) == 1
						|| currentCountUnmatchedNode(sub_case_code) == 2
						|| queryNeedExt(sub_case_code).equals("Y"))
					throw new Exception(sub_case_code + "样本类型有误，应上传加位点数据");
				toCompare.add(enableResult.get(0));
			}
		} else {
			// 需要加位点
			for (String sample_code : sample_codes) {
				params.put("enable_flag", "Y");
				params.put("ext_flag", "N");
				params.put("sample_code", sample_code);
				params.put("case_id", rdsJudicialSubCaseMapper
						.queryCaseIdBySubCaseCode(sub_case_code));
				// 查询有效样本编号是否唯一
				enableResult = rdsJudicialSampleMapper
						.queryBySampleCode(params);
				Set<Map<String, Object>> atelierData = new HashSet();// 一组实验数据name和value
				List<String> experiment_nos = rdsJudicialSampleMapper
						.queryExperiment(params);
				for (String experiment_no : experiment_nos) {
					Map<String, Object> atelierDataParam = new HashMap<>();
					atelierDataParam.put("sample_code", sample_code);
					atelierDataParam.put("experiment_no", experiment_no);
					atelierDataParam.put("laboratory_no", laboratory_no);
					atelierData.add(rdsJudicialSampleService
							.queryOneRecordData(atelierDataParam));
				}
				if (enableResult == null || enableResult.size() == 0
						|| atelierData == null || atelierData.size() == 0) {
					result.put(false, "该案例缺少样本数据，请核实");
					return result;
				}
				// 有效样本编号数量大于1
				else if (atelierData.size() > 1) {
					rdsJudicialExceptionService.insert("A004",
							rdsJudicialSubCaseMapper
									.queryMainCaseCode(sub_case_code),
							sample_code, "", laboratory_no);
					result.put(false, "该案例同一样本号下存在不同样本数据，请核实");
					return result;
				} else {
					for (Map map : atelierData) {
						enableResult.get(0).setRecord(map);
					}
				}
				// 查询加位点有效样本编号是否唯一
				params.put("ext_flag", "Y");
				params.put("case_id", rdsJudicialSubCaseMapper
						.queryCaseIdBySubCaseCode(sub_case_code));
				Set<Map<String, Object>> atelierExtData = new HashSet();
				List<String> experiment_nos_ext = rdsJudicialSampleMapper
						.queryExperiment(params);
				for (String experiment_no : experiment_nos_ext) {
					Map<String, Object> atelierDataParam = new HashMap<>();
					atelierDataParam.put("sample_code", sample_code);
					atelierDataParam.put("experiment_no", experiment_no);
					atelierDataParam.put("laboratory_no", laboratory_no);
					atelierExtData.add(rdsJudicialSampleService
							.queryOneRecordData(atelierDataParam));
				}
				List<RdsJudicialSampleResultModel> enableExtResult = rdsJudicialSampleMapper
						.queryBySampleCode(params);
				if (enableExtResult == null || enableExtResult.size() == 0
						|| atelierExtData == null || atelierExtData.size() == 0) {
					result.put(false, "该案例缺少加位点样本数据，请核实");
					return result;
				}
				// 有效样本编号数量大于1
				else if (atelierExtData.size() > 1) {
					rdsJudicialExceptionService.insert("A004",
							rdsJudicialSubCaseMapper
									.queryMainCaseCode(sub_case_code),
							sample_code, "", laboratory_no);
					result.put(false, "该案例同一样本号下存在不同样本数据，请核实");
					return result;
				} else {
					ext_flag = "Y";
					for (Map map : atelierExtData) {
						enableExtResult.get(0).setRecord(map);
					}
					enableResult.set(0,
							enableResult.get(0).merge(enableExtResult.get(0)));
				}
				if (currentCountUnmatchedNode(sub_case_code) != 1
						&& currentCountUnmatchedNode(sub_case_code) != 2
						&& !queryNeedExt(sub_case_code).equals("Y"))
					throw new Exception(sub_case_code + "样本类型有误，应上传普通位点数据");
				// 需要进行比对的样本
				toCompare.add(enableResult.get(0));
			}
		}
		return compare(toCompare, sub_case_code, ext_flag, false,
				laboratory_no, exception_per, user);
	}

	private Map<Boolean, String> compare(
			List<RdsJudicialSampleResultModel> toCompare, String sub_case_code,
			String ext_flag, boolean final_flag, String laboratory_no,
			String exception_per, RdsUpcUserModel user) throws Exception {
		Map<Boolean, String> result = new HashMap<Boolean, String>();
		int count = 0;
		List<RdsJudicialSampleResultModel> parantsSample = new LinkedList<RdsJudicialSampleResultModel>();
		List<RdsJudicialSampleResultModel> childrenSample = new LinkedList<RdsJudicialSampleResultModel>();
		Set<String> checkreagent = new HashSet<>();
		for (RdsJudicialSampleResultModel model : toCompare) {
			checkreagent.add(model.getReagent_name());// 试剂名称
			// 样本归属人称谓
			if (model.getSample_call().equals(MOTHER)
					|| model.getSample_call().equals(FATHER)) {
				parantsSample.add(model);
			} else {
				childrenSample.add(model);
			}
		}
		if (checkreagent.size() > 1) {
			throw new Exception(sub_case_code + "该案例使用了两种不同的试剂");
		}
		// 父母+孩子三个
		if (parantsSample.size() == 2) {
			for (RdsJudicialSampleResultModel child : childrenSample) {
				Map<String, Object> match = threeModelCompare(child,
						parantsSample.get(0), parantsSample.get(1));

				count = (Integer) match.get("count");
				String unmatchedNode = (String) match.get("unmatchedNode");

				// String unmatchedNodef =(String)match.get("unmatchedNodef");
				// int countf = (Integer)match.get("countf");
				//
				// String unmatchedNodem =(String)match.get("unmatchedNodem");
				// int countm = (Integer)match.get("countm");

				if (!addResultFor3(sub_case_code, parantsSample.get(0),
						parantsSample.get(1), child, ext_flag, count,
						unmatchedNode, laboratory_no, exception_per, user)) {
					result.put(false, "添加比对记录失败");
					return result;
				}
			}
		} else {
			// 二联体
			for (RdsJudicialSampleResultModel child : childrenSample) {
				Map<String, Object> match = twoModelCompare(child,
						parantsSample.get(0));
				count = (Integer) match.get("count");
				String unmatchedNode = (String) match.get("unmatchedNode");
				if (!addResultFor2(sub_case_code, parantsSample.get(0), child,
						ext_flag, count, final_flag, unmatchedNode,
						laboratory_no, exception_per, user)) {
					result.put(false, "添加比对记录失败");
					return result;
				}
			}
		}
		result.put(true, "比对成功");
		return result;
	}

	private String queryGen(Map<String, Object> params) {
		// 根据基因名和试剂名和基因值找对应值若找不到就取最小值
		String result = rdsJudicialCaseParamMapper.queryGen(params);
		if (result == null || result.isEmpty())
			return rdsJudicialCaseParamMapper.queryGenMin(params);
		else
			return result;
	}

	/**
	 * 历史比对
	 * 
	 * @param list
	 * @param laboratory_no
	 */
	private void historyCompare(List<RdsJudicialSampleResultModel> list,
			String laboratory_no) {
		for (RdsJudicialSampleResultModel model : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resultstr", model.getResultstr());
			map.put("sample_code", model.getSample_code());
			List<String> otherRecord = rdsJudicialSampleMapper
					.queryOtherRecord(map);
			if (otherRecord.size() > 0) {
				rdsJudicialExceptionService.insert("A003",
						model.getCase_code(), model.getSample_code(),
						otherRecord.get(0), laboratory_no);
			}
		}
	}

	/*
	 * 交叉比对
	 */
	private boolean crossCompare(List<RdsJudicialSampleResultModel> list,
			String laboratory_no) throws Exception {
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				try {
					if (list.get(i).getCase_code()
							.equals(list.get(j).getCase_code()))
						continue;
				} catch (Exception e) {
					throw new Exception(list.get(i).getSample_code()
							+ "样本对应案例编号没有找到，请查询");
				}
				if ((Integer) twoModelCompare(list.get(i), list.get(j)).get(
						"count") == 0) {
					rdsJudicialExceptionService.insert("A002", list.get(i)
							.getCase_code(), list.get(i).getSample_code(), list
							.get(j).getSample_code(), laboratory_no);
					rdsJudicialExceptionService.insert("A002", list.get(j)
							.getCase_code(), list.get(i).getSample_code(), list
							.get(j).getSample_code(), laboratory_no);
				}
			}
		}
		return true;
	}

	private boolean parentsCompare(RdsJudicialSampleResultModel parent1,
			RdsJudicialSampleResultModel parent2) throws Exception {
		if ((Integer) twoModelCompare(parent1, parent2).get("count") < 3)
			return true;
		return false;
	}

	private boolean addResultFor2(String sub_case_code,
			RdsJudicialSampleResultModel parent1,
			RdsJudicialSampleResultModel child, String ext_flag, int count,
			boolean final_flag, String unmatched_node, String laboratory_no,
			String exception_per, RdsUpcUserModel user) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String mainCaseCode = rdsJudicialSubCaseMapper
				.queryMainCaseCode(sub_case_code);
		RdsJudicialCaseInfoModel mainCaseCodeId = rdsJudicialSubCaseMapper
				.queryCaseInfoByCaseCode(mainCaseCode);

		Map<String, Object> caseReagentParam = new HashMap<String, Object>();
		// 写入案例所属试剂
		caseReagentParam.put("reagent_name", child.getReagent_name());
		caseReagentParam.put("case_code", mainCaseCode);
		caseReagentParam.put("laboratory_no", laboratory_no);
		if ("N".equals(ext_flag)) {
			 caseReagentParam.put("compare_date", StringUtils.dateToSecond(new Date()));
			rdsJudicialSubCaseMapper.updateCaseReagentName(caseReagentParam);
		} else {
			 caseReagentParam.put("compare_date", StringUtils.dateToSecond(new Date()));
			rdsJudicialSubCaseMapper.updateCaseReagentNameExt(caseReagentParam);
		}
		map.put("reagent_name", child.getReagent_name());
		map.put("uuid", UUIDUtil.getUUID());
		map.put("case_code", sub_case_code);
		map.put("parent1", parent1.getSample_username());
		map.put("child", child.getSample_username());
		map.put("sample_code1", parent1.getSample_code());
		map.put("sample_code3", child.getSample_code());
		map.put("ext_flag", ext_flag);
		map.put("unmatched_count", count);
		map.put("unmatched_node", unmatched_node);
		map.put("compare_date", StringUtils.dateToSecond(new Date()));
		map.put("laboratory_no", laboratory_no);
		int existsCount = rdsJudicialResultMapper.isSecondeTimeFor2(map);

		if (existsCount > 0) {
			if (rdsJudicialSampleMapper.queryCountBySampleCode(parent1
					.getSample_code()) < 2
					|| rdsJudicialSampleMapper.queryCountBySampleCode(child
							.getSample_code()) < 2) {
				return true;
			}
		}
		if (final_flag) {
			existsCount = 1;
		}

		// if (mainCaseCodeId.getReport_model().equals("zyjdmodeltq")
		// || mainCaseCodeId.getReport_model().equals("xzjdmodel")
		// || mainCaseCodeId.getReport_model().equals("cxjdmodel")
		// || mainCaseCodeId.getReport_model().equals("zyjdmodelzk")
		// || mainCaseCodeId.getReport_model().equals("mzjdmodel")
		// || mainCaseCodeId.getReport_model().equals("ydjdmodel")
		// || mainCaseCodeId.getReport_model().equals("ydjdmodelyx")
		// || mainCaseCodeId.getReport_model().equals("zyjdmodel")) {
		if (reportModelTempList.contains(mainCaseCodeId.getReport_model())) {
			// 修改按照亲权值范围来出结果
			// 根据不匹配基因位点个数来计算出来pi
			DecimalFormat dfPi = new DecimalFormat("#.0000");
			DecimalFormat dfRcp = new DecimalFormat("#.0000000000");
			double cpi = 0.0;
			double con_cpi = 0.0;
			double reg_cpi = 0.0;
			BigDecimal pi = new BigDecimal(1);
			BigDecimal pi2 = new BigDecimal(1);
			BigDecimal pi3 = new BigDecimal(1);

			if (count == 0 && ext_flag.equals("N")) {
				// 没有不匹配的且不为加位点就出肯定
				pi = calPI(sub_case_code, parent1, child, count, unmatched_node);
				cpi = Double.valueOf(dfPi.format(pi.doubleValue()));
			} else {
				// 有不匹配的 匹配的用肯定算法 不匹配的用肯定算法
				pi = calPI(sub_case_code, parent1, child, count, unmatched_node);
				pi2 = calPIByTb(sub_case_code, parent1, child, unmatched_node);
				pi = pi.multiply(pi2);
				cpi = Double.valueOf(pi.doubleValue());
				// 判断是否加位点
				if (ext_flag.equals("Y")) {
					RdsJudicialSubCaseInfoModel rdsJudicialSubCaseInfoModel = rdsJudicialSubCaseMapper
							.querySubCaseRecord(sub_case_code);
					// 普通位点pi
					BigDecimal piforcomment = new BigDecimal(
							rdsJudicialSubCaseInfoModel.getPi());
					// MR20(S20A(MR20))与MR23SP重复位点D16S539,D2S1338,D12S391
					if (mainCaseCodeId.getReagent_name().equals("S20A(MR20)")
							&& child.getReagent_name().equals("MR23SP")) {

						BigDecimal PID16S539 =calPIForOne(sub_case_code + "_1", parent1,child, "D16S539", unmatched_node);

						BigDecimal PID2S1338 =calPIForOne(sub_case_code + "_1", parent1,child, "D2S1338", unmatched_node);
						
						BigDecimal PID12S391 =calPIForOne(sub_case_code + "_1", parent1,child, "D12S391", unmatched_node);
						
						pi3 = PID16S539.multiply(PID2S1338);
						pi3 = PID12S391.multiply(pi3);
					}
					// MR20(S20A(MR20))与AGCU+1重复位点为D19S433
					if (mainCaseCodeId.getReagent_name().equals("S20A(MR20)")
							&& child.getReagent_name().equals("AGCU_21+1")) {
						
						BigDecimal PID19S433 = calPIForOne(sub_case_code + "_1", parent1,child, "D19S433", unmatched_node);
						
						pi3 = PID19S433;
					}
					// MR20(S20A(MR20))与NC22plex 重复位点为D16S539
					if (mainCaseCodeId.getReagent_name().equals("S20A(MR20)")
							&& child.getReagent_name().equals("NC22plex")) {

						BigDecimal PID16S539 = calPIForOne(sub_case_code + "_1", parent1,child, "D16S539", unmatched_node);
						pi3 = PID16S539;
					}
					// MR21和MR23SP重复位点 D2S1338 D12S391 D16S539
					if (mainCaseCodeId.getReagent_name().equals("MR21")
							&& child.getReagent_name().equals("MR23SP")) {

						BigDecimal PID2S1338 = calPIForOne(sub_case_code + "_1", parent1,child, "D2S1338", unmatched_node);

						BigDecimal PID12S391 = calPIForOne(sub_case_code + "_1", parent1,child, "D12S391", unmatched_node);
						
						pi3 = PID2S1338.multiply(PID12S391);

						BigDecimal PID16S539 = calPIForOne(sub_case_code + "_1", parent1,child, "D16S539", unmatched_node);
						
						pi3 = PID16S539.multiply(pi3);
					}
					// MR21与AGCU+1（加位点）重复位点为D19S433，D2S411
					if (mainCaseCodeId.getReagent_name().equals("MR21")
							&& child.getReagent_name().equals("AGCU_21+1")) {

						BigDecimal PID19S433 = calPIForOne(sub_case_code + "_1", parent1,child, "D19S433", unmatched_node);

						BigDecimal PID2S411 =calPIForOne(sub_case_code + "_1", parent1,child, "D2S411", unmatched_node);
						
						pi3 = PID19S433.multiply(PID2S411);
					}
					// MR21+NC22plex D16S539 D2S441
					if (mainCaseCodeId.getReagent_name().equals("MR21")
							&& child.getReagent_name().equals("NC22plex")) {

						BigDecimal PID16S539 = calPIForOne(sub_case_code + "_1", parent1,child, "D16S539", unmatched_node);

						BigDecimal PID2S411 = calPIForOne(sub_case_code + "_1", parent1,child, "D2S441", unmatched_node);
						
						pi3 = PID16S539.multiply(PID2S411);
					}
					// SUBO21+NC22plex D16S539 D1S1656
					if (mainCaseCodeId.getReagent_name().equals("SUBO21")
							&& child.getReagent_name().equals("NC22plex")) {

						BigDecimal PID16S539 = calPIForOne(sub_case_code + "_1", parent1,child, "D16S539", unmatched_node);

						BigDecimal PID1S1656 = calPIForOne(sub_case_code + "_1", parent1,child, "D1S1656", unmatched_node);
						
						pi3 = PID16S539.multiply(PID1S1656);
					}
					// SUBO21+MR23SP D1S1656 D16S539 D12S391 D2S1338
					if (mainCaseCodeId.getReagent_name().equals("SUBO21")
							&& child.getReagent_name().equals("MR23SP")) {

						BigDecimal PID1S1656 =calPIForOne(sub_case_code + "_1", parent1,child, "D1S1656", unmatched_node);

						BigDecimal PID16S539 = calPIForOne(sub_case_code + "_1", parent1,child, "D16S539", unmatched_node);
						
						BigDecimal PID12S391 = calPIForOne(sub_case_code + "_1", parent1,child, "D12S391", unmatched_node);
						
						BigDecimal PID2S1338 = calPIForOne(sub_case_code + "_1", parent1,child, "D2S1338", unmatched_node);
						
						pi3 = PID1S1656.multiply(PID16S539);
						pi3 = PID12S391.multiply(pi3);
						pi3 = PID2S1338.multiply(pi3);
					}
					// SUBO21+AGCU_21+1 D19S433
					if (mainCaseCodeId.getReagent_name().equals("SUBO21")
							&& child.getReagent_name().equals("AGCU_21+1")) {

						BigDecimal PID19S433 = calPIForOne(sub_case_code + "_1", parent1,child, "D19S433", unmatched_node);
						
						pi3 = PID19S433;
					}
					pi = pi.multiply(piforcomment);
					pi = pi.divide(pi3,BigDecimal.ROUND_HALF_UP);
					cpi = Double.valueOf(pi.doubleValue());
				}
			}
			double rcp = cpi / (cpi + 1);

			if (count == 0 && cpi >= 10000) {
				// 1.肯定的标准：没有否定位点，且CPI>10000
				// 如果案例CPI>10000，给出支持结论;
				map.put("final_result_flag", "passed");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("result", "passed");
				params.put("sub_case_code", sub_case_code);
				params.put("pi", StringUtils.complete0(dfPi.format(pi), 4));
				params.put("rcp", Double.valueOf(dfRcp.format(rcp)));
				params.put("laboratory_no", laboratory_no);
				sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
						parent1.getSample_code(), child.getSample_code());

				/** 流程 start **/
				if (RdsJudicialSampleRelayMapper
						.queryCaseCodeVerify(mainCaseCode) == 0) {
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("isyesresult", 1);
					variables.put("isback", -1);
					rdsActivitiJudicialService.runByCaseCode(mainCaseCode,
							variables, user);
					Map<String, Object> mapTemp = new HashMap<String, Object>();
					mapTemp.put("case_code", mainCaseCode);
					mapTemp.put("verify_state", 6);
					RdsJudicialSampleRelayMapper
							.updateCaseVerifyBycode(mapTemp);
				}
				/** 流程 end **/

				rdsJudicialSubCaseMapper.update(params);
			} else if ((count == 0 && cpi < 10000 && ext_flag.equals("N"))
					|| (count > 0 && 0.0001 < cpi && ext_flag.equals("N"))) {
				// 2.无否定位点且CPI<10000”或“有否定位点CPI>0.0001
				// 如果处于0.0001< CPI< 1000,最后计算总的CPI值，直到满足CPI>1000或CPI<0.0001为止。
				// 做加位点
				Map<String, Object> parent1params = new HashMap<String, Object>();
				parent1params.put("sub_case_code", sub_case_code);
				if (parent1.getSample_call().equals("father")) {
					parent1params.put("con_pi", "F");
					this.rdsJudicialSubCaseMapper
							.updatePi1ForUnPaent(parent1params);
				} else if (parent1.getSample_call().equals("mother")) {
					parent1params.put("con_pi", "M");
					this.rdsJudicialSubCaseMapper
							.updatePi1ForUnPaent(parent1params);
				}
				map.put("need_ext", "Y");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("sub_case_code", sub_case_code);
				params.put("pi", pi);
				params.put("rcp", Double.valueOf(dfRcp.format(rcp)));
				params.put("laboratory_no", laboratory_no);
				sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
						parent1.getSample_code(), child.getSample_code());
				rdsJudicialSubCaseMapper.update(params);
			} else if (ext_flag.equals("Y") && cpi >= 10000 && count<3) {

				Map<String, Object> parent1params = new HashMap<String, Object>();
				parent1params.put("sub_case_code", sub_case_code);
				if (parent1.getSample_call().equals("father")) {
					parent1params.put("con_pi", "F");
					this.rdsJudicialSubCaseMapper
							.updatePi1ForUnPaent(parent1params);
				} else if (parent1.getSample_call().equals("mother")) {
					parent1params.put("con_pi", "M");
					this.rdsJudicialSubCaseMapper
							.updatePi1ForUnPaent(parent1params);
				}

				map.put("final_result_flag", "passed");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("result", "passed");
				params.put("pi", StringUtils.complete0(dfPi.format(pi), 4));
				params.put("rcp", Double.valueOf(dfRcp.format(rcp)));
				params.put("sub_case_code", sub_case_code);
				params.put("laboratory_no", laboratory_no);
				sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
						parent1.getSample_code(), child.getSample_code());
				rdsJudicialSubCaseMapper.update(params);

				/** 流程 start **/
				if (RdsJudicialSampleRelayMapper
						.queryCaseCodeVerify(mainCaseCode) == 0) {
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("isyesresult", 1);
					variables.put("isback", -1);
					rdsActivitiJudicialService.runByCaseCode(mainCaseCode,
							variables, user);
					Map<String, Object> mapTemp = new HashMap<String, Object>();
					mapTemp.put("case_code", mainCaseCode);
					mapTemp.put("verify_state", 6);
					RdsJudicialSampleRelayMapper
							.updateCaseVerifyBycode(mapTemp);
				}
				/** 流程 end **/

			} else if ((cpi <= 0.0001 && existsCount > 0&& count>=3)
					|| (ext_flag.equals("Y") && cpi <= 0.0001 && existsCount > 0 && count>=3)) {
				// 3.否定标准：CPI<0.0001
				// 如果CPI<0.0001，并且第二次实验给出排除结论；
				map.put("final_result_flag", "failed");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("result", "failed");
				params.put("sub_case_code", sub_case_code);
				params.put("laboratory_no", laboratory_no);
				params.put("pi", cpi);
				params.put("rcp", Double.valueOf(rcp));
				sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
						parent1.getSample_code(), child.getSample_code());
				rdsJudicialSubCaseMapper.update(params);
				Map<String, Object> checkNegParams = new HashMap<>();
				checkNegParams.put("case_code", mainCaseCode);
				checkNegParams.put("sub_case_code", sub_case_code);
				checkNegParams.put("check_flag", "N");
				rdsJudicialSubCaseMapper.insertCheckNegReport(checkNegParams);
				// 添加确认是否定案例的时候增加异常处理
				Map<String, Object> params_caseid = new HashMap<String, Object>();
				params_caseid.put("case_id", mainCaseCodeId.getCase_id());
				List<RdsJudicialCaseExceptionModel> listcaseid = rdsJudicialExperimentMapper
						.getExceptionCaseId(params_caseid);
				if (listcaseid.size() <= 0) {
					Map<String, Object> params_exception = new HashMap<String, Object>();
					params_exception.put("exception_per", exception_per);
					params_exception.put("exception_desc", "否定案例报告");
					params_exception
							.put("case_id", mainCaseCodeId.getCase_id());
					params_exception.put("exception_type",
							"af767495f35e468f9b0d7b28904e61dd");
					rdsJudicialCaseExceptionService
							.saveExceptionInfo(params_exception);
				}

				/** 流程 start **/
				if (RdsJudicialSampleRelayMapper
						.queryCaseCodeVerify(mainCaseCode) == 0) {
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("isyesresult", 1);
					variables.put("isback", -1);
					rdsActivitiJudicialService.runByCaseCode(mainCaseCode,
							variables, user);
					Map<String, Object> mapTemp = new HashMap<String, Object>();
					mapTemp.put("case_code", mainCaseCode);
					mapTemp.put("verify_state", 6);
					RdsJudicialSampleRelayMapper
							.updateCaseVerifyBycode(mapTemp);
				}
				/** 流程 end **/
			}
		} else {
			if (count == 0 && ext_flag.equals("N")) {
				DecimalFormat dfPi = new DecimalFormat("#.0000");
				DecimalFormat dfRcp = new DecimalFormat("#.0000000000");
				BigDecimal pi = calPIbymodel(sub_case_code, parent1, child);
				double pi_value = Double.valueOf(dfPi.format(pi.doubleValue()));
				double rcp = pi_value / (pi_value + 1);
				if (pi_value >= 10000){
					map.put("final_result_flag", "passed");
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("result", "passed");
					params.put("sub_case_code", sub_case_code);
					params.put("pi", StringUtils.complete0(dfPi.format(pi), 4));
					params.put("rcp", Double.valueOf(dfRcp.format(rcp)));
					params.put("laboratory_no", laboratory_no);
					sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
							parent1.getSample_code(), child.getSample_code());
					rdsJudicialSubCaseMapper.update(params);

					/** 流程 start **/
					if (RdsJudicialSampleRelayMapper
							.queryCaseCodeVerify(mainCaseCode) == 0) {
						Map<String, Object> variables = new HashMap<String, Object>();
						variables.put("isyesresult", 1);
						variables.put("isback", -1);
						rdsActivitiJudicialService.runByCaseCode(mainCaseCode,
								variables, user);
						Map<String, Object> mapTemp = new HashMap<String, Object>();
						mapTemp.put("case_code", mainCaseCode);
						mapTemp.put("verify_state", 6);
						RdsJudicialSampleRelayMapper
								.updateCaseVerifyBycode(mapTemp);
					}
					/** 流程 end **/

				} else {
					map.put("need_ext", "Y");
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("sub_case_code", sub_case_code);
					params.put("pi", pi);
					params.put("rcp", Double.valueOf(dfRcp.format(rcp)));
					params.put("laboratory_no", laboratory_no);
					rdsJudicialSubCaseMapper.update(params);
				}
			} else if (count < 3 && ext_flag.equals("Y")) {
				map.put("final_result_flag", "passed");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("result", "passed");
				params.put("sub_case_code", sub_case_code);
				params.put("laboratory_no", laboratory_no);
				sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
						parent1.getSample_code(), child.getSample_code());
				rdsJudicialSubCaseMapper.update(params);

				/** 流程 start **/
				if (RdsJudicialSampleRelayMapper
						.queryCaseCodeVerify(mainCaseCode) == 0) {
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("isyesresult", 1);
					variables.put("isback", -1);
					rdsActivitiJudicialService.runByCaseCode(mainCaseCode,
							variables, user);
					Map<String, Object> mapTemp = new HashMap<String, Object>();
					mapTemp.put("case_code", mainCaseCode);
					mapTemp.put("verify_state", 6);
					RdsJudicialSampleRelayMapper
							.updateCaseVerifyBycode(mapTemp);
				}
				/** 流程 end **/
			} else if (count >= 3 && existsCount > 0) {
				map.put("final_result_flag", "failed");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("result", "failed");
				params.put("sub_case_code", sub_case_code);
				params.put("laboratory_no", laboratory_no);
				// params.put("pi",StringUtils.complete0(dfPi.format(pi),4));
				// params.put("rcp",Double.valueOf(dfRcp.format(rcp)));
				sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
						parent1.getSample_code(), child.getSample_code());

				/** 流程 start **/
				if (RdsJudicialSampleRelayMapper
						.queryCaseCodeVerify(mainCaseCode) == 0) {
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("isyesresult", 0);
					variables.put("isback", -1);
					rdsActivitiJudicialService.runByCaseCode(mainCaseCode,
							variables, user);
					Map<String, Object> mapTemp = new HashMap<String, Object>();
					mapTemp.put("case_code", mainCaseCode);
					mapTemp.put("verify_state", 6);
					RdsJudicialSampleRelayMapper
							.updateCaseVerifyBycode(mapTemp);
				}
				/** 流程 end **/

				rdsJudicialSubCaseMapper.update(params);
				Map<String, Object> checkNegParams = new HashMap<>();
				checkNegParams.put("case_code", mainCaseCode);
				checkNegParams.put("sub_case_code", sub_case_code);
				checkNegParams.put("check_flag", "N");
				rdsJudicialSubCaseMapper.insertCheckNegReport(checkNegParams);
				// 添加确认是否定案例的时候增加异常处理
				Map<String, Object> params_caseid = new HashMap<String, Object>();
				params_caseid.put("case_id", mainCaseCodeId.getCase_id());
				List<RdsJudicialCaseExceptionModel> listcaseid = rdsJudicialExperimentMapper
						.getExceptionCaseId(params_caseid);
				if (listcaseid.size() <= 0) {
					Map<String, Object> params_exception = new HashMap<String, Object>();
					params_exception.put("exception_per", exception_per);
					params_exception.put("exception_desc", "否定案例报告");
					params_exception
							.put("case_id", mainCaseCodeId.getCase_id());
					params_exception.put("exception_type",
							"af767495f35e468f9b0d7b28904e61dd");
					rdsJudicialCaseExceptionService
							.saveExceptionInfo(params_exception);
				}
			}
		}
		rdsJudicialResultMapper.addResult(map);
		// 更新案例基本表里的比对时间
		Map<String, Object> compareMap = new HashMap<String, Object>();
//		compareMap.put("case_code", mainCaseCode);
//		compareMap.put("compare_date", map.get("compare_date"));
//		rdsJudicialRegisterMapper.updateCaseCompareDate(compareMap);
		return true;
	}

	private boolean addResultFor3(String sub_case_code,
			RdsJudicialSampleResultModel parent1,
			RdsJudicialSampleResultModel parent2,
			RdsJudicialSampleResultModel child, String ext_flag, int count,
			String unmatched_node, String laboratory_no, String exception_per,
			RdsUpcUserModel user) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String mainCaseCode = rdsJudicialSubCaseMapper
				.queryMainCaseCode(sub_case_code);// 案例编号
		RdsJudicialCaseInfoModel mainCaseCodeId = rdsJudicialSubCaseMapper
				.queryCaseInfoByCaseCode(mainCaseCode);
		Map<String, Object> caseReagentParam = new HashMap<String, Object>();
		// 写入案例所属试剂
		caseReagentParam.put("reagent_name", child.getReagent_name());
		caseReagentParam.put("case_code", mainCaseCode);
		if ("N".equals(ext_flag)) {
			caseReagentParam.put("laboratory_no", laboratory_no);
			 caseReagentParam.put("compare_date", StringUtils.dateToSecond(new Date()));
			rdsJudicialSubCaseMapper.updateCaseReagentName(caseReagentParam);

		} else {
			 caseReagentParam.put("compare_date", StringUtils.dateToSecond(new Date()));
			rdsJudicialSubCaseMapper.updateCaseReagentNameExt(caseReagentParam);// 更新试剂
		}
		map.put("uuid", UUIDUtil.getUUID());
		map.put("reagent_name", child.getReagent_name());
		map.put("case_code", sub_case_code);
		map.put("parent1", parent1.getSample_username());
		map.put("parent2", parent2.getSample_username());
		map.put("child", child.getSample_username());
		map.put("sample_code1", parent1.getSample_code());
		map.put("sample_code2", parent2.getSample_code());
		map.put("sample_code3", child.getSample_code());
		map.put("ext_flag", ext_flag);
		map.put("unmatched_count", count);
		map.put("unmatched_node", unmatched_node);
		map.put("compare_date", StringUtils.dateToSecond(new Date()));
		map.put("laboratory_no", laboratory_no);
		// if (mainCaseCodeId.getReport_model().equals("zyjdmodeltq")
		// || mainCaseCodeId.getReport_model().equals("xzjdmodel")
		// || mainCaseCodeId.getReport_model().equals("cxjdmodel")
		// || mainCaseCodeId.getReport_model().equals("zyjdmodelzk")
		// || mainCaseCodeId.getReport_model().equals("ydjdmodel")
		// || mainCaseCodeId.getReport_model().equals("ydjdmodelyx")
		// || mainCaseCodeId.getReport_model().equals("mzjdmodel")
		// || mainCaseCodeId.getReport_model().equals("zyjdmodel")) {
		if (reportModelTempList.contains(mainCaseCodeId.getReport_model())) {
			// 修改按照亲权值范围来出结果
			// 根据不匹配基因位点个数来计算出来pi
			DecimalFormat dfPi = new DecimalFormat("#.0000");
			DecimalFormat dfRcp = new DecimalFormat("#.0000000000");
			double cpi = 0.0;
			BigDecimal pi = new BigDecimal(1);
			BigDecimal pi2 = new BigDecimal(1);
			BigDecimal pi3 = new BigDecimal(1);
			if (count == 0 && ext_flag.equals("N")) {
				// 没有不匹配的且不是加位点就出肯定
				pi = calPI(sub_case_code, parent1, parent2, child, count,
						unmatched_node);
				cpi = Double.valueOf(dfPi.format(pi.doubleValue()));
			} else {
				// 有不匹配的 匹配的用肯定算法 不匹配的用肯定算法
				pi = calPI(sub_case_code, parent1, parent2, child, count,
						unmatched_node);
				pi2 = calPIByTb(sub_case_code, parent1, parent2, child,
						unmatched_node);
				// double a=pi2.doubleValue();
				pi = pi.multiply(pi2);
				cpi = Double.valueOf(pi.doubleValue());
				// System.out.printf("%1.11e\r\n", cpi);
				// 判断是否加位点
				if (ext_flag.equals("Y")) {
					RdsJudicialSubCaseInfoModel rdsJudicialSubCaseInfoModel = rdsJudicialSubCaseMapper
							.querySubCaseRecord(sub_case_code);
					// 普通位点pi

					String piString = rdsJudicialSubCaseInfoModel.getPi();
					BigDecimal piforcomment = new BigDecimal(1);
					if (piString == null) {
						piforcomment = new BigDecimal(1);
					} else {
						piforcomment = new BigDecimal(
								rdsJudicialSubCaseInfoModel.getPi());
					}

					// MR20与MR23SP重复位点D16S539,D2S1338,D12S391
					// MR21与MR23SP重复位点D16S539,D2S1338,D12S391
					if ((mainCaseCodeId.getReagent_name().equals("S20A(MR20)") && child
							.getReagent_name().equals("MR23SP"))
							|| mainCaseCodeId.getReagent_name().equals("MR21")
							&& child.getReagent_name().equals("MR23SP")) {

						BigDecimal PID16S539 = calPiForTwo(sub_case_code, parent1, parent2, child, "D16S539",
								unmatched_node);

						BigDecimal PID2S1338 = calPiForTwo(sub_case_code, parent1, parent2, child, "D2S1338",
								unmatched_node);
						pi3 = PID16S539.multiply(PID2S1338);

						BigDecimal PID12S391 = calPiForTwo(sub_case_code, parent1, parent2, child, "D12S391",
								unmatched_node);
						pi3 = PID12S391.multiply(pi3);
					}
					// MR20(S20A(MR20))与NC22plex 重复位点为D16S539
					if (mainCaseCodeId.getReagent_name().equals("S20A(MR20)")
							&& child.getReagent_name().equals("NC22plex")) {

						BigDecimal PID16S539 = calPiForTwo(sub_case_code, parent1, parent2, child, "D16S539",
								unmatched_node);
						pi3 = PID16S539;
					}
					// SUBO21+NC22plex D16S539 D1S1656
					if (mainCaseCodeId.getReagent_name().equals("SUBO21")
							&& child.getReagent_name().equals("NC22plex")) {

						BigDecimal PID16S539 = calPiForTwo(sub_case_code, parent1, parent2, child, "D16S539",
								unmatched_node);

						BigDecimal PID1S1656 = calPiForTwo(sub_case_code, parent1, parent2, child, "D1S1656",
								unmatched_node);
						pi3 = PID16S539.multiply(PID1S1656);
					}
					// SUBO21+MR23SP D1S1656 D16S539 D12S391 D2S1338
					if (mainCaseCodeId.getReagent_name().equals("SUBO21")
							&& child.getReagent_name().equals("MR23SP")) {
			
						BigDecimal PID1S1656 =calPiForTwo(sub_case_code, parent1, parent2, child, "D1S1656",
								unmatched_node);

						BigDecimal PID16S539 = calPiForTwo(sub_case_code, parent1, parent2, child, "D16S539",
								unmatched_node);
						pi3 = PID16S539.multiply(PID1S1656);

						BigDecimal PID12S391 = calPiForTwo(sub_case_code, parent1, parent2, child, "D12S391",
								unmatched_node);
						pi3 = PID12S391.multiply(pi3);

						BigDecimal PID2S1338 =calPiForTwo(sub_case_code, parent1, parent2, child, "D2S1338",
								unmatched_node);
						pi3 = PID2S1338.multiply(pi3);
					}
					// SUBO21+AGCU_21+1 D19S433
					if (mainCaseCodeId.getReagent_name().equals("SUBO21")
							&& child.getReagent_name().equals("AGCU_21+1")) {

						BigDecimal PID19S433 = calPiForTwo(sub_case_code, parent1, parent2, child, "D19S433",
								unmatched_node);
						pi3 = PID19S433;
					}
					// MR21+NC22plex D16S539 D2S441
					if (mainCaseCodeId.getReagent_name().equals("MR21")
							&& child.getReagent_name().equals("NC22plex")) {

						BigDecimal PID16S539 =calPiForTwo(sub_case_code, parent1, parent2, child, "D16S539",
								unmatched_node);

						BigDecimal PID2S411 =calPiForTwo(sub_case_code, parent1, parent2, child, "D2S441",
								unmatched_node);
						pi3 = PID16S539.multiply(PID2S411);
					}
					// MR20与AGCU+1重复位点为D19S433
					if (mainCaseCodeId.getReagent_name().equals("S20A(MR20)")
							&& child.getReagent_name().equals("AGCU_21+1")) {
				
						BigDecimal PID19S433 = calPiForTwo(sub_case_code, parent1, parent2, child, "D19S433",
								unmatched_node);
						pi3 = PID19S433;
					}
					// MR21与AGCU+1（加位点）重复位点为D19S433，D2S411
					if (mainCaseCodeId.getReagent_name().equals("MR21")
							&& child.getReagent_name().equals("AGCU_21+1")) {
		
						BigDecimal PID19S433 = calPiForTwo(sub_case_code, parent1, parent2, child, "D19S433",
								unmatched_node);

						BigDecimal PID2S411 = calPiForTwo(sub_case_code, parent1, parent2, child, "D2S411",
								unmatched_node);
						pi3 = PID19S433.multiply(PID2S411);
					}
					pi = pi.multiply(piforcomment);
					pi = pi.divide(pi3,BigDecimal.ROUND_HALF_UP);
					cpi = Double.valueOf(pi.doubleValue());
				}
			}
			double rcp = cpi / (cpi + 1);

			RdsJudicialSubCaseInfoModel rdsJudicialSubCaseInfoModel_new = rdsJudicialSubCaseMapper
					.querySubCaseRecord(sub_case_code);

			if (count == 0 && cpi >= 10000 && ext_flag.equals("N")) {
				// 1.肯定的标准：没有否定位点，且CPI>10000
				// 如果案例CPI>10000，给出支持结论;
				if (mainCaseCodeId.getReport_model().equals("ydjdmodel")
						|| mainCaseCodeId.getReport_model().equals("zyjdmodeltq")
						|| mainCaseCodeId.getReport_model().equals("qsjdmodel")
						|| mainCaseCodeId.getReport_model().equals("zyjdmodelzk")
						|| mainCaseCodeId.getReport_model().equals("zyjdmodel")
						|| mainCaseCodeId.getReport_model().equals("bjqjdmodel")
						|| mainCaseCodeId.getReport_model().equals("mzjdmodel")
						|| mainCaseCodeId.getReport_model().equals("qdwfjdmodel")
						|| mainCaseCodeId.getReport_model().equals("cxjdmodel")
						|| mainCaseCodeId.getReport_model().equals("sqjdmodel")
						|| mainCaseCodeId.getReport_model().equals("xajdmodel")
						|| mainCaseCodeId.getReport_model().equals("fyxhjdmodel")
						|| mainCaseCodeId.getReport_model().equals("ynqsjdmodel")) {
					

					BigDecimal parent1pi = calPI(sub_case_code + "_1", parent1,
							child, count, unmatched_node);
					BigDecimal parent2pi = calPI(sub_case_code + "_2", parent2,
							child, count, unmatched_node);

					double parent1pi_value = Double.valueOf(
							dfPi.format(parent1pi.doubleValue())).doubleValue();

					double parent2pi_value = Double.valueOf(
							dfPi.format(parent2pi.doubleValue())).doubleValue();

					if (parent1pi_value > 10000.0D&&parent2pi_value > 10000.0D) {
						Map<String, Object> parent1params = new HashMap<String, Object>();
						parent1params.put("sub_case_code", sub_case_code);
						parent1params.put("parent1_pi", StringUtils.complete0(
								dfPi.format(parent1pi), 4));
						this.rdsJudicialSubCaseMapper
								.updatePi1Formz(parent1params);
						Map<String, Object> parent2params = new HashMap<String, Object>();
						parent2params.put("sub_case_code", sub_case_code);
						parent2params.put("parent2_pi", StringUtils.complete0(
								dfPi.format(parent2pi), 4));
						this.rdsJudicialSubCaseMapper
								.updatePi2Formz(parent2params);
						
						map.put("final_result_flag", "passed");
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("result", "passed");
						params.put("sub_case_code", sub_case_code);
						params.put("pi", StringUtils.complete0(dfPi.format(pi), 4));
						params.put("rcp", Double.valueOf(dfRcp.format(rcp)));
						params.put("laboratory_no", laboratory_no);
						sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
								parent1.getSample_code(), parent2.getSample_code(),
								child.getSample_code());

						/** 流程插入 start **/
						if (RdsJudicialSampleRelayMapper
								.queryCaseCodeVerify(mainCaseCode) == 0) {
							Map<String, Object> variables = new HashMap<String, Object>();
							variables.put("isyesresult", 1);
							variables.put("isback", -1);
							rdsActivitiJudicialService.runByCaseCode(mainCaseCode,
									variables, user);
							Map<String, Object> mapTemp = new HashMap<String, Object>();
							mapTemp.put("case_code", mainCaseCode);
							mapTemp.put("verify_state", 6);
							RdsJudicialSampleRelayMapper
									.updateCaseVerifyBycode(mapTemp);
						}
						/** 流程插入 end **/

						rdsJudicialSubCaseMapper.update(params);// tb_judicial_sub_case_info数据
						}else {
							map.put("need_ext", "Y");
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("sub_case_code", sub_case_code);
							params.put("pi", pi);
							params.put("parent1_pi", parent1pi);
							params.put("parent2_pi", parent2pi);
							params.put("laboratory_no", laboratory_no);
							rdsJudicialSubCaseMapper.update(params);
							sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
									parent1.getSample_code(), parent2.getSample_code(),
									child.getSample_code());
						}
				}else{
						map.put("final_result_flag", "passed");
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("result", "passed");
						params.put("sub_case_code", sub_case_code);
						params.put("pi", StringUtils.complete0(dfPi.format(pi), 4));
						params.put("rcp", Double.valueOf(dfRcp.format(rcp)));
						params.put("laboratory_no", laboratory_no);
						sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
								parent1.getSample_code(), parent2.getSample_code(),
								child.getSample_code());

				/** 流程插入 start **/
				if (RdsJudicialSampleRelayMapper
						.queryCaseCodeVerify(mainCaseCode) == 0) {
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("isyesresult", 1);
					variables.put("isback", -1);
					rdsActivitiJudicialService.runByCaseCode(mainCaseCode,
							variables, user);
					Map<String, Object> mapTemp = new HashMap<String, Object>();
					mapTemp.put("case_code", mainCaseCode);
					mapTemp.put("verify_state", 6);
					RdsJudicialSampleRelayMapper
							.updateCaseVerifyBycode(mapTemp);
				}
				/** 流程插入 end **/

				rdsJudicialSubCaseMapper.update(params);// tb_judicial_sub_case_info数据
				}
				}
                else if ((count == 0 && ext_flag.equals("N") && cpi < 10000)
					|| (count > 0 && 0.0001 < cpi && ext_flag.equals("N"))) {
				// 2.无否定位点且CPI<10000”或“有否定位点CPI>0.0001
				// 如果处于0.0001< CPI< 1000,最后计算总的CPI值，直到满足CPI>1000或CPI<0.0001为止。
				// 需要加位点
				if (mainCaseCodeId.getReport_model().equals("ydjdmodel")
						|| mainCaseCodeId.getReport_model().equals("zyjdmodeltq")
						|| mainCaseCodeId.getReport_model().equals("qsjdmodel")
						|| mainCaseCodeId.getReport_model().equals("zyjdmodelzk")
						|| mainCaseCodeId.getReport_model().equals("zyjdmodel")
						|| mainCaseCodeId.getReport_model().equals("bjqjdmodel")
						|| mainCaseCodeId.getReport_model().equals("mzjdmodel")
						|| mainCaseCodeId.getReport_model().equals("qdwfjdmodel")
						|| mainCaseCodeId.getReport_model().equals("cxjdmodel")
						|| mainCaseCodeId.getReport_model().equals("sqjdmodel")
						|| mainCaseCodeId.getReport_model().equals("fyxhjdmodel")
						|| mainCaseCodeId.getReport_model().equals("xajdmodel")
						|| mainCaseCodeId.getReport_model().equals("ynqsjdmodel")) {
					Map<String, Object> match1 = twoModelCompare(child, parent1);
					Map<String, Object> match2 = twoModelCompare(child, parent2);
					String unmatched_nodeP11 =(String) match1.get("unmatchedNode");
					String unmatched_nodeP22 =(String) match2.get("unmatchedNode");

					// 分别计算父母孩子亲权 不包含不匹配点
					BigDecimal parent1pi = calPI(sub_case_code + "_1", parent1,
							child, count, unmatched_nodeP11);
					BigDecimal parent2pi = calPI(sub_case_code + "_2", parent2,
							child, count, unmatched_nodeP22);

					// 只计算不匹配点位的亲权
					BigDecimal parent11pi = new BigDecimal(1);
					BigDecimal parent22pi = new BigDecimal(1);
					// 父亲
					int count1 = (Integer) match1.get("count");
					if (count1 > 0) {
						parent11pi = calPIByTb(sub_case_code + "_1", parent1,
								child, unmatched_nodeP11);
						Map<String, Object> parent1params = new HashMap<String, Object>();
						parent1params.put("sub_case_code", sub_case_code);
						parent1params.put("con_pi", "F");
						this.rdsJudicialSubCaseMapper
								.updatePi1ForUnPaent(parent1params);
					} else {
						parent11pi = calPIbyOne(sub_case_code + "_1", parent1,
								child, count, unmatched_nodeP11);
					}
					// 母亲
					int count2 = (Integer) match2.get("count");
					if (count2 > 0) {
						parent22pi = calPIByTb(sub_case_code + "_2", parent2,
								child, unmatched_nodeP22);

						Map<String, Object> parent1params = new HashMap<String, Object>();
						parent1params.put("sub_case_code", sub_case_code);
						parent1params.put("con_pi", "M");
						this.rdsJudicialSubCaseMapper
								.updatePi1ForUnPaent(parent1params);
					} else {
						parent22pi = calPIbyOne(sub_case_code + "_2", parent2,
								child, count, unmatched_nodeP22);
					}

					if (count1 > 0 && count2 > 0) {
						Map<String, Object> parent1params = new HashMap<String, Object>();
						parent1params.put("sub_case_code", sub_case_code);
						parent1params.put("con_pi", "F&M");
						this.rdsJudicialSubCaseMapper
								.updatePi1ForUnPaent(parent1params);

					}
//					Map<String, Object> parent11params = new HashMap<String, Object>();
//					parent11params.put("sub_case_code", sub_case_code);
//					parent11params.put("parent1_pi",parent11pi);
//							//StringUtils.complete0(dfPi.format(parent11pi), 4));
//					this.rdsJudicialSubCaseMapper
//							.updatePi1Formz(parent11params);
//					Map<String, Object> parent22params = new HashMap<String, Object>();
//					parent22params.put("sub_case_code", sub_case_code);
//					parent22params.put("parent2_pi",parent22pi);
//							//StringUtils.complete0(dfPi.format(parent22pi), 4));
//					this.rdsJudicialSubCaseMapper
//							.updatePi2Formz(parent22params);

					// 计算总的cpi
					BigDecimal parent111pi = parent1pi.multiply(parent11pi);
					BigDecimal parent222pi = parent2pi.multiply(parent22pi);
//					double parent1pi_value = Double.valueOf(
//							dfPi.format(parent111pi.doubleValue()))
//							.doubleValue();
//					double parent2pi_value = Double.valueOf(
//							dfPi.format(parent222pi.doubleValue()))
//							.doubleValue();
//
//					if (parent1pi_value > 10000.0D) {
						Map<String, Object> parent1params = new HashMap<String, Object>();
						parent1params.put("sub_case_code", sub_case_code);
						parent1params.put("parent1_pi", parent111pi);
								//StringUtils.complete0(dfPi.format(parent111pi), 4));
						this.rdsJudicialSubCaseMapper
								.updatePi1Formz(parent1params);
//					}
//					if (parent2pi_value > 10000.0D) {
						Map<String, Object> parent2params = new HashMap<String, Object>();
						parent2params.put("sub_case_code", sub_case_code);
						parent2params.put("parent2_pi", parent222pi);
								//StringUtils.complete0(\dfPi.format(parent222pi), 4));
						this.rdsJudicialSubCaseMapper
								.updatePi2Formz(parent2params);
					}
//				}

				map.put("need_ext", "Y");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("sub_case_code", sub_case_code);
				params.put("pi", pi);
				params.put("rcp", Double.valueOf(dfRcp.format(rcp)));
				params.put("laboratory_no", laboratory_no);
				rdsJudicialSubCaseMapper.update(params);
				sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
						parent1.getSample_code(), parent2.getSample_code(),
						child.getSample_code());
			} else if (ext_flag.equals("Y") && cpi >= 10000&& count<3) {
				if (mainCaseCodeId.getReport_model().equals("ydjdmodel")
						|| mainCaseCodeId.getReport_model().equals("zyjdmodeltq")
						|| mainCaseCodeId.getReport_model().equals("qsjdmodel")
						|| mainCaseCodeId.getReport_model().equals("zyjdmodelzk")
						|| mainCaseCodeId.getReport_model().equals("zyjdmodel")
						|| mainCaseCodeId.getReport_model().equals("bjqjdmodel")
						|| mainCaseCodeId.getReport_model().equals("mzjdmodel")
						|| mainCaseCodeId.getReport_model().equals("qdwfjdmodel")
						|| mainCaseCodeId.getReport_model().equals("cxjdmodel")
						|| mainCaseCodeId.getReport_model().equals("sqjdmodel")
						|| mainCaseCodeId.getReport_model().equals("fyxhjdmodel")
						|| mainCaseCodeId.getReport_model().equals("xajdmodel")
						|| mainCaseCodeId.getReport_model().equals("ynqsjdmodel")) {
					
					Map<String, Object> match1 = twoModelCompare(child, parent1);
					Map<String, Object> match2 = twoModelCompare(child, parent2);
					String unmatched_nodeP111 =(String) match1.get("unmatchedNode");
					String unmatched_nodeP222 =(String) match2.get("unmatchedNode");

					// 分别计算父母孩子亲权 不包含不匹配点
					BigDecimal parent1pi = calPI(sub_case_code + "_1", parent1,
							child, count, unmatched_nodeP111);
					BigDecimal parent2pi = calPI(sub_case_code + "_2", parent2,
							child, count, unmatched_nodeP222);
					// 只计算不匹配点位的亲权
					BigDecimal parent11pi = new BigDecimal(1);
					BigDecimal parent22pi = new BigDecimal(1);
					// 父亲
					int count1 = (Integer) match1.get("count");
					if (count1 > 0) {
						parent11pi = calPIByTb(sub_case_code + "_1", parent1,
								child, unmatched_nodeP111);
						Map<String, Object> parent1params = new HashMap<String, Object>();
						parent1params.put("sub_case_code", sub_case_code);
						parent1params.put("con_pi", "F");
						this.rdsJudicialSubCaseMapper
								.updatePi1ForUnPaent(parent1params);
					} else {
						parent11pi = calPIbyOne(sub_case_code + "_1", parent1,
								child, count, unmatched_nodeP111);
					}
					// 母亲
					int count2 = (Integer) match2.get("count");
					if (count2 > 0) {
						parent22pi = calPIByTb(sub_case_code + "_2", parent2,
								child, unmatched_nodeP222);
						Map<String, Object> parent1params = new HashMap<String, Object>();
						parent1params.put("sub_case_code", sub_case_code);
						parent1params.put("con_pi", "M");
						this.rdsJudicialSubCaseMapper
								.updatePi1ForUnPaent(parent1params);
					} else {
						parent22pi = calPIbyOne(sub_case_code + "_2", parent2,
								child, count, unmatched_nodeP222);
					}

					if (count1 > 0 && count2 > 0) {
						Map<String, Object> parent1params = new HashMap<String, Object>();
						parent1params.put("sub_case_code", sub_case_code);
						parent1params.put("con_pi", "F&M");
						this.rdsJudicialSubCaseMapper
								.updatePi1ForUnPaent(parent1params);

					}
//
//					Map<String, Object> parent11params = new HashMap<String, Object>();
//					parent11params.put("sub_case_code", sub_case_code);
//					parent11params.put("parent1_pi",
//							StringUtils.complete0(dfPi.format(parent11pi), 4));
//					this.rdsJudicialSubCaseMapper
//							.updatePi1Formz(parent11params);
//					Map<String, Object> parent22params = new HashMap<String, Object>();
//					parent22params.put("sub_case_code", sub_case_code);
//					parent22params.put("parent2_pi",
//							StringUtils.complete0(dfPi.format(parent22pi), 4));
//					this.rdsJudicialSubCaseMapper
//							.updatePi2Formz(parent22params);

					// 普通位点pi
					String parent1pi_String = rdsJudicialSubCaseInfoModel_new
							.getParent1_pi();// 父亲
					String parent2pi_String = rdsJudicialSubCaseInfoModel_new
							.getParent2_pi();// 母亲
					BigDecimal parent1pi_Stringforcomment = new BigDecimal(1);
					BigDecimal parent2pi_Stringforcomment = new BigDecimal(1);
					if (parent1pi_String == null) {
						parent1pi_Stringforcomment = new BigDecimal(1);
					} else {
						parent1pi_Stringforcomment = new BigDecimal(
								rdsJudicialSubCaseInfoModel_new.getParent1_pi());
					}

					if (parent2pi_String == null) {
						parent2pi_Stringforcomment = new BigDecimal(1);
					} else {
						parent2pi_Stringforcomment = new BigDecimal(
								rdsJudicialSubCaseInfoModel_new.getParent2_pi());
					}

					// 计算总的cpi
					BigDecimal parent111pi = parent1pi.multiply(parent11pi); // 加位点
					BigDecimal parent222pi = parent2pi.multiply(parent22pi); // 加位点

					BigDecimal parentspi1 = parent1pi_Stringforcomment
							.multiply(parent111pi);
					BigDecimal parentspi2 = parent2pi_Stringforcomment
							.multiply(parent222pi);

					// 去掉重复
					BigDecimal pi3f = new BigDecimal(1);
					BigDecimal pi3m = new BigDecimal(1);
					if (mainCaseCodeId.getReagent_name().equals("SUBO21")
							&& child.getReagent_name().equals("NC22plex")) {	
						BigDecimal PID16S539=calPIForOne(sub_case_code + "_1", parent1,
								child, "D16S539", unmatched_nodeP111);
						BigDecimal PID1S1656 = calPIForOne(sub_case_code + "_1", parent1,
								child, "D1S1656", unmatched_nodeP111);
								
	                    BigDecimal PID16S539m = calPIForOne(sub_case_code + "_2", parent2,
								child, "D16S539", unmatched_nodeP222);
						BigDecimal PID1S1656m = calPIForOne(sub_case_code + "_2", parent2,
								child, "D1S1656", unmatched_nodeP222);
						
						pi3f = PID16S539.multiply(PID1S1656);
						pi3m = PID16S539m.multiply(PID1S1656m);
						
					}else if ((mainCaseCodeId.getReagent_name().equals("S20A(MR20)") && child
							.getReagent_name().equals("MR23SP"))
							|| mainCaseCodeId.getReagent_name().equals("MR21")
							&& child.getReagent_name().equals("MR23SP")) {

						BigDecimal PID16S539 =calPIForOne(sub_case_code + "_1", parent1,child, "D16S539", unmatched_nodeP111);

						BigDecimal PID16S539m = calPIForOne(sub_case_code + "_2", parent2,child, "D16S539", unmatched_nodeP222);

						BigDecimal PID2S1338 =calPIForOne(sub_case_code + "_1", parent1,child, "D2S1338", unmatched_nodeP111);

						BigDecimal PID2S1338m= calPIForOne(sub_case_code + "_2", parent2,child, "D2S1338", unmatched_nodeP222);

						BigDecimal PID12S391 =calPIForOne(sub_case_code + "_1", parent1,child, "D12S391", unmatched_nodeP111);
						
						BigDecimal PID12S391m =calPIForOne(sub_case_code + "_2", parent2,child, "D12S391", unmatched_nodeP222);
					
						pi3f = PID16S539.multiply(PID2S1338);
						pi3f=pi3f.multiply(PID12S391);
						pi3m = PID16S539m.multiply(PID2S1338m);
						pi3m=pi3m.multiply(PID12S391m);
						
					}else if (mainCaseCodeId.getReagent_name().equals("S20A(MR20)")
							&& child.getReagent_name().equals("AGCU_21+1")) {

						BigDecimal PID19S433 = calPIForOne(sub_case_code + "_1", parent1,child, "D19S433", unmatched_nodeP111);

						BigDecimal PID19S433m = calPIForOne(sub_case_code + "_2", parent2,child, "D19S433", unmatched_nodeP222);
					
						pi3f = PID19S433;
						pi3m = PID19S433m;
						
					}else  if(mainCaseCodeId.getReagent_name().equals("S20A(MR20)")
							&& child.getReagent_name().equals("NC22plex")){

						BigDecimal PID16S539 = calPIForOne(sub_case_code + "_1", parent1,child, "D16S539", unmatched_nodeP111);

						BigDecimal PID16S539m = calPIForOne(sub_case_code + "_2", parent2,child, "D16S539", unmatched_nodeP222);

						pi3f = PID16S539;
						pi3m = PID16S539m;	
					}else if(mainCaseCodeId.getReagent_name().equals("MR21")
							&& child.getReagent_name().equals("AGCU_21+1")){
						
						BigDecimal PID19S433 = calPIForOne(sub_case_code + "_1", parent1,child, "D19S433", unmatched_nodeP111);

						BigDecimal PID19S433m = calPIForOne(sub_case_code + "_2", parent2,child, "D19S433", unmatched_nodeP222);

						BigDecimal PID2S411 = calPIForOne(sub_case_code + "_1", parent1,child, "D2S411", unmatched_nodeP111);

						BigDecimal PID2S411m = calPIForOne(sub_case_code + "_2", parent2,child, "D2S411", unmatched_nodeP222);

						pi3f = PID19S433.multiply(PID2S411);
						pi3m = PID19S433m.multiply(PID2S411m);
						
					}else if (mainCaseCodeId.getReagent_name().equals("MR21")
							&& child.getReagent_name().equals("NC22plex")){

						BigDecimal PID19S433 = calPIForOne(sub_case_code + "_1", parent1,child, "D16S539", unmatched_nodeP111);

						BigDecimal PID19S433m =calPIForOne(sub_case_code + "_2", parent2,child, "D16S539", unmatched_nodeP222);

						BigDecimal PID2S411 =calPIForOne(sub_case_code + "_1", parent1,child, "D2S441", unmatched_nodeP111);

						BigDecimal PID2S411m =calPIForOne(sub_case_code + "_2", parent2,child, "D2S441", unmatched_nodeP222);

						pi3f = PID19S433.multiply(PID2S411);
						pi3m = PID19S433m.multiply(PID2S411m);	
						
					}else if(mainCaseCodeId.getReagent_name().equals("SUBO21")
							&& child.getReagent_name().equals("MR23SP")){

						BigDecimal PID1S1656 = calPIForOne(sub_case_code + "_1", parent1,child, "D1S1656", unmatched_nodeP111);

						BigDecimal PID1S1656m =calPIForOne(sub_case_code + "_2", parent2,child, "D1S1656", unmatched_nodeP222);

						BigDecimal PID16S539 = calPIForOne(sub_case_code + "_1", parent1,child, "D16S539", unmatched_nodeP111);

						BigDecimal PID16S539m =calPIForOne(sub_case_code + "_2", parent2,child, "D16S539", unmatched_nodeP222);

						BigDecimal PID12S391 =calPIForOne(sub_case_code + "_1", parent1,child, "D12S391", unmatched_nodeP111);

						BigDecimal PID12S391m = calPIForOne(sub_case_code + "_2", parent2,child, "D12S391", unmatched_nodeP222);

						BigDecimal PID2S1338 = calPIForOne(sub_case_code + "_1", parent1,child, "D2S1338", unmatched_nodeP111);

						BigDecimal PID2S1338m = calPIForOne(sub_case_code + "_2", parent2,child, "D2S1338", unmatched_nodeP222);
						
						pi3f = PID1S1656.multiply(PID16S539);
						pi3f=pi3f.multiply(PID12S391);
						pi3f=pi3f.multiply(PID2S1338);
						
						pi3m = PID1S1656m.multiply(PID16S539m);
						pi3m=pi3m.multiply(PID12S391m);
						pi3m=pi3m.multiply(PID2S1338m);
	
					}else if(mainCaseCodeId.getReagent_name().equals("SUBO21")
					&& child.getReagent_name().equals("AGCU_21+1")){

						BigDecimal PID19S433 = calPIForOne(sub_case_code + "_1", parent1,child, "D19S433", unmatched_nodeP111);

						BigDecimal PID19S433m = calPIForOne(sub_case_code + "_2", parent2,child, "D19S433", unmatched_nodeP222);
						
						pi3f=PID19S433;
						pi3m=PID19S433m;
					}

					parentspi1 = parentspi1.divide(pi3f,BigDecimal.ROUND_HALF_UP);
					parentspi2 = parentspi2.divide(pi3m,BigDecimal.ROUND_HALF_UP);

					double parent1pi_value = Double.valueOf(
							dfPi.format(parentspi1.doubleValue()))
							.doubleValue();
					double parent2pi_value = Double.valueOf(
							dfPi.format(parentspi2.doubleValue()))
							.doubleValue();
					//三联体拆分，父亲孩子，母亲孩子，各自累积pi同时大于10000，肯定结论
                        if (parent1pi_value > 10000.0D&&parent2pi_value > 10000.0D) {
						Map<String, Object> parent1params = new HashMap<String, Object>();
						parent1params.put("sub_case_code", sub_case_code);
						parent1params.put("parent1_pi", StringUtils.complete0(
								dfPi.format(parentspi1), 4));
						this.rdsJudicialSubCaseMapper
								.updatePi1Formz(parent1params);
						Map<String, Object> parent2params = new HashMap<String, Object>();
						parent2params.put("sub_case_code", sub_case_code);
						parent2params.put("parent2_pi", StringUtils.complete0(
								dfPi.format(parentspi2), 4));
						this.rdsJudicialSubCaseMapper
								.updatePi2Formz(parent2params);
						
						map.put("final_result_flag", "passed");
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("result", "passed");
						params.put("sub_case_code", sub_case_code);
						params.put("pi", StringUtils.complete0(dfPi.format(pi), 4));
						params.put("rcp", Double.valueOf(dfRcp.format(rcp)));
						params.put("laboratory_no", laboratory_no);
						sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
								parent1.getSample_code(), parent2.getSample_code(),
								child.getSample_code());

						/** 流程插入 start **/
						if (RdsJudicialSampleRelayMapper
								.queryCaseCodeVerify(mainCaseCode) == 0) {
							Map<String, Object> variables = new HashMap<String, Object>();
							variables.put("isyesresult", 1);
							variables.put("isback", -1);
							rdsActivitiJudicialService.runByCaseCode(mainCaseCode,
									variables, user);
							Map<String, Object> mapTemp = new HashMap<String, Object>();
							mapTemp.put("case_code", mainCaseCode);
							mapTemp.put("verify_state", 6);
							RdsJudicialSampleRelayMapper
									.updateCaseVerifyBycode(mapTemp);
						}
						/** 流程插入 end **/

						rdsJudicialSubCaseMapper.update(params);// tb_judicial_sub_case_info数据
						}else{
							map.put("need_ext", "Y");
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("sub_case_code", sub_case_code);
							params.put("pi", pi);
							params.put("parent1_pi",parentspi1);
							params.put("parent2_pi",parentspi2);
							params.put("laboratory_no", laboratory_no);
							rdsJudicialSubCaseMapper.update(params);
							sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
									parent1.getSample_code(), parent2.getSample_code(),
									child.getSample_code());
						}
				}

				map.put("final_result_flag", "passed");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("result", "passed");
				params.put("sub_case_code", sub_case_code);
				params.put("pi", StringUtils.complete0(dfPi.format(pi), 4));
				params.put("rcp", Double.valueOf(dfRcp.format(rcp)));
				params.put("laboratory_no", laboratory_no);
				sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
						parent1.getSample_code(), parent2.getSample_code(),
						child.getSample_code());
				rdsJudicialSubCaseMapper.update(params);

				/** 流程 start **/
				if (RdsJudicialSampleRelayMapper
						.queryCaseCodeVerify(mainCaseCode) == 0) {
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("isyesresult", 1);
					variables.put("isback", -1);
					rdsActivitiJudicialService.runByCaseCode(mainCaseCode,
							variables, user);
					Map<String, Object> mapTemp = new HashMap<String, Object>();
					mapTemp.put("case_code", mainCaseCode);
					mapTemp.put("verify_state", 6);
					RdsJudicialSampleRelayMapper
							.updateCaseVerifyBycode(mapTemp);
				}
				/** 流程 end **/

			} else if ((cpi <= 0.0001)) {
				// 3.否定标准：CPI<0.0001
				if (parentsCompare(parent1, parent2)) {
					rdsJudicialExceptionService.insert("B001",
							rdsJudicialSubCaseMapper
									.queryMainCaseCode(sub_case_code), "", "",
							laboratory_no);// 父母匹配结果为肯定结果
					return false;
				}
				if (parent1.equals(child) || parent2.equals(child)) {
					rdsJudicialExceptionService.insert("B002",
							rdsJudicialSubCaseMapper
									.queryMainCaseCode(sub_case_code), "", "",
							laboratory_no);// 父母和孩子有相同结果
					return false;
				}
				// 判断是不是第二次比对
				int existsCount = rdsJudicialResultMapper
						.isSecondeTimeFor3(map);
				if (existsCount > 0) {
					if (rdsJudicialSampleMapper.queryCountBySampleCode(parent1
							.getSample_code()) < 2
							|| rdsJudicialSampleMapper
									.queryCountBySampleCode(parent2
											.getSample_code()) < 2
							|| rdsJudicialSampleMapper
									.queryCountBySampleCode(child
											.getSample_code()) < 2) {
						return true;
					}
				}
				if (existsCount > 0 && count>=3) {
					String sub_case_code1 = null;
					String sub_case_code2 = null;
					if (existsCount == 1) {
						sub_case_code1 = sub_case_code + "_1";
						sub_case_code2 = sub_case_code + "_2";
					}
					// 如果CPI<0.0001，并且第二次实验给出排除结论；
					// map.put("final_result_flag","failed");
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("result", "failed");
					params.put("sub_case_code", sub_case_code);
					params.put("laboratory_no", laboratory_no);
					params.put("pi", cpi);
					params.put("rcp", Double.valueOf(dfRcp.format(rcp)));
					sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
							parent1.getSample_code(), child.getSample_code());

					/** 流程插入 start **/
					if (RdsJudicialSampleRelayMapper
							.queryCaseCodeVerify(mainCaseCode) == 0) {
						Map<String, Object> variables = new HashMap<String, Object>();
						variables.put("isyesresult", 0);
						variables.put("isback", -1);
						rdsActivitiJudicialService.runByCaseCode(mainCaseCode,
								variables, user);
						Map<String, Object> mapTemp = new HashMap<String, Object>();
						mapTemp.put("case_code", mainCaseCode);
						mapTemp.put("verify_state", 6);
						RdsJudicialSampleRelayMapper
								.updateCaseVerifyBycode(mapTemp);
					}
					/** 流程插入 end **/

					rdsJudicialSubCaseMapper.update(params);

					// 3个人不符点第二次达到3，则拆分进行比对
					RdsJudicialSubCaseInfoModel rdsJudicialSubCaseInfoModel1 = new RdsJudicialSubCaseInfoModel();
					rdsJudicialSubCaseInfoModel1.setSample_code1(parent1
							.getSample_code());
					rdsJudicialSubCaseInfoModel1.setSample_code3(child
							.getSample_code());
					rdsJudicialSubCaseInfoModel1
							.setCase_code(rdsJudicialSubCaseMapper
									.queryMainCaseCode(sub_case_code));
					rdsJudicialSubCaseInfoModel1
							.setSub_case_code(sub_case_code1);
					rdsJudicialSubCaseMapper
							.insert(rdsJudicialSubCaseInfoModel1);

					RdsJudicialSubCaseInfoModel rdsJudicialSubCaseInfoModel2 = new RdsJudicialSubCaseInfoModel();

					rdsJudicialSubCaseInfoModel2.setSample_code1(parent2
							.getSample_code());
					rdsJudicialSubCaseInfoModel2.setSample_code3(child
							.getSample_code());
					rdsJudicialSubCaseInfoModel2
							.setCase_code(rdsJudicialSubCaseMapper
									.queryMainCaseCode(sub_case_code));
					rdsJudicialSubCaseInfoModel2
							.setSub_case_code(sub_case_code2);
					rdsJudicialSubCaseMapper
							.insert(rdsJudicialSubCaseInfoModel2);

					List<RdsJudicialSampleResultModel> list = new LinkedList<RdsJudicialSampleResultModel>();
					List<RdsJudicialSampleResultModel> list2 = new LinkedList<RdsJudicialSampleResultModel>();
					// Map<String,Object> paramP1 = new HashMap<>();
					// paramP1.put("experiment_no",parent1.getExperiment_no());
					// paramP1.put("sample_code",parent1.getSample_code());
					// paramP1.put("laboratory_no",laboratory_no);
					// parent1 =
					// rdsJudicialSampleService.queryOneRecord(paramP1);
					// Map<String,Object> paramP2 = new HashMap<>();
					// paramP2.put("experiment_no",parent2.getExperiment_no());
					// paramP2.put("sample_code",parent2.getSample_code());
					// paramP2.put("laboratory_no",laboratory_no);
					// parent22 =
					// rdsJudicialSampleService.queryOneRecord(paramP2);
					// Map<String,Object> paramchild = new HashMap<>();
					// paramchild.put("experiment_no",child.getExperiment_no());
					// paramchild.put("sample_code",child.getSample_code());
					// paramchild.put("laboratory_no",laboratory_no);
					// child2 =
					// rdsJudicialSampleService.queryOneRecord(paramchild);
					list.add(parent1);
					list.add(child);
					list2.add(parent2);
					list2.add(child);
					compare(list, sub_case_code1, "N", true, laboratory_no,
							exception_per, user);
					compare(list2, sub_case_code2, "N", true, laboratory_no,
							exception_per, user);
				}
			}
		} else {
			if (count == 0 && ext_flag.equals("N")) {
				// 判断不匹配个数0 且不是加位点
				DecimalFormat dfPi = new DecimalFormat("#.0000");
				DecimalFormat dfRcp = new DecimalFormat("#.0000000000");
				// 计算亲权值
				BigDecimal pi = calPIbymodel(sub_case_code, parent1, parent2,
						child);
				if (mainCaseCodeId.getReport_model().equals("mzjdmodel")) {
					BigDecimal parent1pi = calPIbymodel(sub_case_code, parent1,
							child);
					BigDecimal parent2pi = calPIbymodel(sub_case_code, parent2,
							child);

					double parent1pi_value = Double.valueOf(
							dfPi.format(parent1pi.doubleValue())).doubleValue();

					double parent2pi_value = Double.valueOf(
							dfPi.format(parent2pi.doubleValue())).doubleValue();

					if (parent1pi_value > 10000.0D) {
						Map parent1params = new HashMap();
						parent1params.put("sub_case_code", sub_case_code);
						parent1params.put("parent1_pi", StringUtils.complete0(
								dfPi.format(parent1pi), 4));
						this.rdsJudicialSubCaseMapper
								.updatePi1Formz(parent1params);
					}
					if (parent2pi_value > 10000.0D) {
						Map parent2params = new HashMap();
						parent2params.put("sub_case_code", sub_case_code);
						parent2params.put("parent2_pi", StringUtils.complete0(
								dfPi.format(parent2pi), 4));
						this.rdsJudicialSubCaseMapper
								.updatePi2Formz(parent2params);
					}
				}
				double pi_value = Double.valueOf(dfPi.format(pi.doubleValue()));
				double rcp = pi_value / (pi_value + 1);
				// 判断pi和所打印模板
				if (pi_value > 10000) {
					map.put("final_result_flag", "passed");
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("result", "passed");
					params.put("sub_case_code", sub_case_code);
					params.put("pi", StringUtils.complete0(dfPi.format(pi), 4));
					params.put("rcp", Double.valueOf(dfRcp.format(rcp)));
					params.put("laboratory_no", laboratory_no);
					sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
							parent1.getSample_code(), parent2.getSample_code(),
							child.getSample_code());
					rdsJudicialSubCaseMapper.update(params);// tb_judicial_sub_case_info数据

					/** 流程 start **/
					if (RdsJudicialSampleRelayMapper
							.queryCaseCodeVerify(mainCaseCode) == 0) {
						Map<String, Object> variables = new HashMap<String, Object>();
						variables.put("isyesresult", 1);
						variables.put("isback", -1);
						rdsActivitiJudicialService.runByCaseCode(mainCaseCode,
								variables, user);
						Map<String, Object> mapTemp = new HashMap<String, Object>();
						mapTemp.put("case_code", mainCaseCode);
						mapTemp.put("verify_state", 6);
						RdsJudicialSampleRelayMapper
								.updateCaseVerifyBycode(mapTemp);
					}
					/** 流程 end **/

				} else {
					// 需要加位点
					map.put("need_ext", "Y");
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("sub_case_code", sub_case_code);
					params.put("pi", pi);
					params.put("rcp", Double.valueOf(dfRcp.format(rcp)));
					params.put("laboratory_no", laboratory_no);
					rdsJudicialSubCaseMapper.update(params);
					sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
							parent1.getSample_code(), parent2.getSample_code(),
							child.getSample_code());
				}
			} else if ((count == 1 || count == 2) && ext_flag.equals("Y")) {
				map.put("final_result_flag", "passed");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("result", "passed");
				params.put("sub_case_code", sub_case_code);
				params.put("laboratory_no", laboratory_no);
				sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
						parent1.getSample_code(), parent2.getSample_code(),
						child.getSample_code());
				rdsJudicialSubCaseMapper.update(params);

				/** 流程 start **/
				if (RdsJudicialSampleRelayMapper
						.queryCaseCodeVerify(mainCaseCode) == 0) {
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("isyesresult", 1);
					variables.put("isback", -1);
					rdsActivitiJudicialService.runByCaseCode(mainCaseCode,
							variables, user);
					Map<String, Object> mapTemp = new HashMap<String, Object>();
					mapTemp.put("case_code", mainCaseCode);
					mapTemp.put("verify_state", 6);
					RdsJudicialSampleRelayMapper
							.updateCaseVerifyBycode(mapTemp);
				}
				/** 流程 end **/

			} else if (count >= 3) {
				// 判断不匹配个数大于3个 可以排除亲权关系存在
				if (parentsCompare(parent1, parent2)) {
					rdsJudicialExceptionService.insert("B001",
							rdsJudicialSubCaseMapper
									.queryMainCaseCode(sub_case_code), "", "",
							laboratory_no);// 父母匹配结果为肯定结果
					return false;
				}
				if (parent1.equals(child) || parent2.equals(child)) {
					rdsJudicialExceptionService.insert("B002",
							rdsJudicialSubCaseMapper
									.queryMainCaseCode(sub_case_code), "", "",
							laboratory_no);// 父母和孩子有相同结果
					return false;
				}
				// 判断是不是第二次比对
				int existsCount = rdsJudicialResultMapper
						.isSecondeTimeFor3(map);
				if (existsCount > 0) {
					if (rdsJudicialSampleMapper.queryCountBySampleCode(parent1
							.getSample_code()) < 2
							|| rdsJudicialSampleMapper
									.queryCountBySampleCode(parent2
											.getSample_code()) < 2
							|| rdsJudicialSampleMapper
									.queryCountBySampleCode(child
											.getSample_code()) < 2) {
						return true;
					}
				}
				if (existsCount > 0) {
					String sub_case_code1 = null;
					String sub_case_code2 = null;
					if (existsCount == 1) {
						sub_case_code1 = sub_case_code + "_1";
						sub_case_code2 = sub_case_code + "_2";
					}
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("result", "failed");
					params.put("sub_case_code", sub_case_code);
					params.put("laboratory_no", laboratory_no);
					sendPDFFile(mainCaseCode, ext_flag, laboratory_no,
							parent1.getSample_code(), parent2.getSample_code(),
							child.getSample_code());
					// params.put("pi",StringUtils.complete0(dfPi.format(pi),4));
					// params.put("rcp",Double.valueOf(dfRcp.format(rcp)));
					rdsJudicialSubCaseMapper.update(params);
					// 3个人不符点第二次达到3，则拆分进行比对
					RdsJudicialSubCaseInfoModel rdsJudicialSubCaseInfoModel1 = new RdsJudicialSubCaseInfoModel();
					rdsJudicialSubCaseInfoModel1.setSample_code1(parent1
							.getSample_code());
					rdsJudicialSubCaseInfoModel1.setSample_code3(child
							.getSample_code());
					rdsJudicialSubCaseInfoModel1
							.setCase_code(rdsJudicialSubCaseMapper
									.queryMainCaseCode(sub_case_code));
					rdsJudicialSubCaseInfoModel1
							.setSub_case_code(sub_case_code1);
					rdsJudicialSubCaseMapper
							.insert(rdsJudicialSubCaseInfoModel1);

					RdsJudicialSubCaseInfoModel rdsJudicialSubCaseInfoModel2 = new RdsJudicialSubCaseInfoModel();

					rdsJudicialSubCaseInfoModel2.setSample_code1(parent2
							.getSample_code());
					rdsJudicialSubCaseInfoModel2.setSample_code3(child
							.getSample_code());
					rdsJudicialSubCaseInfoModel2
							.setCase_code(rdsJudicialSubCaseMapper
									.queryMainCaseCode(sub_case_code));
					rdsJudicialSubCaseInfoModel2
							.setSub_case_code(sub_case_code2);
					rdsJudicialSubCaseMapper
							.insert(rdsJudicialSubCaseInfoModel2);

					List<RdsJudicialSampleResultModel> list = new LinkedList<RdsJudicialSampleResultModel>();
					List<RdsJudicialSampleResultModel> list2 = new LinkedList<RdsJudicialSampleResultModel>();
					Map<String, Object> paramP1 = new HashMap<>();
					paramP1.put("experiment_no", parent1.getExperiment_no());
					paramP1.put("sample_code", parent1.getSample_code());
					paramP1.put("laboratory_no", laboratory_no);
					// parent1 =
					// rdsJudicialSampleService.queryOneRecord(paramP1);
					// Map<String,Object> paramP2 = new HashMap<>();
					// paramP2.put("experiment_no",parent2.getExperiment_no());
					// paramP2.put("sample_code",parent2.getSample_code());
					// paramP2.put("laboratory_no",laboratory_no);
					// parent22 =
					// rdsJudicialSampleService.queryOneRecord(paramP2);
					// Map<String,Object> paramchild = new HashMap<>();
					// paramchild.put("experiment_no",child.getExperiment_no());
					// paramchild.put("sample_code",child.getSample_code());
					// paramchild.put("laboratory_no",laboratory_no);
					// child2 =
					// rdsJudicialSampleService.queryOneRecord(paramchild);
					list.add(parent1);
					list.add(child);
					list2.add(parent2);
					list2.add(child);
					compare(list, sub_case_code1, "N", true, laboratory_no,
							exception_per, user);
					compare(list2, sub_case_code2, "N", true, laboratory_no,
							exception_per, user);
				}
			}
		}
		rdsJudicialResultMapper.addResult(map);
		// 更新案例基本表里的比对时间
//		Map<String, Object> compareMap = new HashMap<String, Object>();
//		compareMap.put("case_code", mainCaseCode);
//		compareMap.put("compare_date", map.get("compare_date"));
//		rdsJudicialRegisterMapper.updateCaseCompareDate(compareMap);
		return true;
	}

	private BigDecimal calPIbymodel(String sub_case_code,
			RdsJudicialSampleResultModel parent1,
			RdsJudicialSampleResultModel child) throws IllegalAccessException {
		BigDecimal pi = new BigDecimal(1);
		double gene1 = 0;
		double gene2 = 0;
		Map<String, Object> record = parent1.getRecord();
		Set<String> set = record.keySet();
		for (String ateName : set) {
			String parentStr = (String) parent1.getRecord().get(ateName);
			String childStr = (String) child.getRecord().get(ateName);
			Map<String, String> result = findFunction(parentStr, childStr);
			if (result.size() == 3) {
				Map<String, Object> params = new HashMap<String, Object>();
				String param_type = ateName;
				params.put("param_type", param_type);
				params.put("param_name", result.get("param_name"));
				params.put("reagent_name", child.getReagent_name());
				String temp = queryGen(params);
				if (temp == null) {
					continue;
				}
				gene1 = Double.parseDouble(temp);
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.put("param_type", param_type);
				params1.put("param_name", result.get("param_name1"));
				params1.put("reagent_name", child.getReagent_name());
				String temp2 = queryGen(params1);
				if (temp2 == null) {
					continue;
				}
				gene2 = Double.parseDouble(temp2);
				double sub_pi = 0.25 / gene1 + 0.25 / gene2;
				pi = pi.multiply(new BigDecimal(sub_pi));
				sub_pi = new BigDecimal(sub_pi).setScale(4,
						BigDecimal.ROUND_HALF_UP).doubleValue();
				RdsJudicialPiInfoModel rdsJudicialPiInfoModel = new RdsJudicialPiInfoModel();
				rdsJudicialPiInfoModel.setSub_case_code(sub_case_code);
				rdsJudicialPiInfoModel.setParam_type(param_type);
				rdsJudicialPiInfoModel.setChild(childStr);
				rdsJudicialPiInfoModel.setParent(parentStr);
				rdsJudicialPiInfoModel.setGene1(StringUtils.complete0(
						new BigDecimal(gene1).setScale(4,
								BigDecimal.ROUND_HALF_UP).toString(), 4));
				rdsJudicialPiInfoModel.setGene2(StringUtils.complete0(
						new BigDecimal(gene2).setScale(4,
								BigDecimal.ROUND_HALF_UP).toString(), 4));
				rdsJudicialPiInfoModel.setFunction(result.get("function"));
				rdsJudicialPiInfoModel.setPi(StringUtils.complete0(
						(sub_pi + ""), 4));
				rdsJudicialCaseParamMapper.insertPiInfo(rdsJudicialPiInfoModel);

			} else {
				String function = result.get("function");
				Map<String, Object> params = new HashMap<String, Object>();
				String param_type = ateName;
				params.put("param_type", param_type);
				params.put("param_name", result.get("param_name"));
				params.put("reagent_name", child.getReagent_name());
				String temp = queryGen(params);
				if (temp == null) {
					continue;
				}
				gene1 = Double.parseDouble(temp);
				double sub_pi = 0;
				if (function.equals("1/p"))
					sub_pi = 1 / gene1;
				else if (function.equals("0.5/p"))
					sub_pi = 0.5 / gene1;
				else if (function.equals("0.25/p"))
					sub_pi = 0.25 / gene1;

				pi = pi.multiply(new BigDecimal(sub_pi));
				sub_pi = new BigDecimal(sub_pi).setScale(4,
						BigDecimal.ROUND_HALF_UP).doubleValue();
				RdsJudicialPiInfoModel rdsJudicialPiInfoModel = new RdsJudicialPiInfoModel();
				rdsJudicialPiInfoModel.setSub_case_code(sub_case_code);
				rdsJudicialPiInfoModel.setParam_type(param_type);
				rdsJudicialPiInfoModel.setChild(childStr);
				rdsJudicialPiInfoModel.setParent(parentStr);
				rdsJudicialPiInfoModel.setGene1(StringUtils.complete0(
						new BigDecimal(gene1).setScale(4,
								BigDecimal.ROUND_HALF_UP).toString(), 4));
				rdsJudicialPiInfoModel.setFunction(result.get("function"));
				rdsJudicialPiInfoModel.setPi(StringUtils.complete0(
						(sub_pi + ""), 4));
				rdsJudicialCaseParamMapper.insertPiInfo(rdsJudicialPiInfoModel);
			}
		}
		return pi;
	}

	/**
	 * 3个人亲权值
	 * 
	 * @param sub_case_code
	 * @param parent1
	 * @param parent2
	 * @param child
	 * @return
	 * @throws IllegalAccessException
	 */
	private BigDecimal calPIbymodel(String sub_case_code,
			RdsJudicialSampleResultModel parent1,
			RdsJudicialSampleResultModel parent2,
			RdsJudicialSampleResultModel child) throws IllegalAccessException {
		RdsJudicialSampleResultModel mother = null;
		RdsJudicialSampleResultModel father = null;
		// 判断性别
		if ("X".equals(parent1.getRecord().get("AMEL"))
				|| "X".equals(parent1.getRecord().get("Amel"))) {
			mother = parent1;
			father = parent2;
		} else {
			mother = parent2;
			father = parent1;
		}
		BigDecimal pi = new BigDecimal(1);
		double gene1 = 0;
		double gene2 = 0;
		Map<String, Object> record = parent1.getRecord();
		Set<String> set = record.keySet();
		for (String ateName : set) {
			// 比对每一组基因位
			String fatherStr = (String) father.getRecord().get(ateName);
			String motherStr = (String) mother.getRecord().get(ateName);
			String childStr = (String) child.getRecord().get(ateName);
			Map<String, String> result = findFunction(motherStr, fatherStr,
					childStr);// {param_name=14, function=1/(2r)}
			if (result.size() == 3) {
				String function = result.get("function");
				Map<String, Object> params = new HashMap<String, Object>();
				String param_type = ateName;
				params.put("param_type", param_type);
				params.put("param_name", result.get("param_name"));
				params.put("reagent_name", child.getReagent_name());
				String temp = queryGen(params);
				if (temp == null) {
					continue;
				}
				gene1 = Double.parseDouble(temp);
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.put("param_type", param_type);
				params1.put("param_name", result.get("param_name1"));
				params1.put("reagent_name", child.getReagent_name());
				String temp2 = queryGen(params1);
				if (temp2 == null) {
					continue;
				}
				gene2 = Double.parseDouble(temp2);
				double sub_pi = 0;
				if (function.equals("1/[2(p+q)]")) {
					sub_pi = 0.5 / (gene1 + gene2);
				} else if (function.equals("1/(p+q)")) {
					sub_pi = 1 / (gene1 + gene2);
				}
				pi = pi.multiply(new BigDecimal(sub_pi));
				sub_pi = new BigDecimal(sub_pi).setScale(4,
						BigDecimal.ROUND_HALF_UP).doubleValue();
				RdsJudicialPiInfoModel rdsJudicialPiInfoModel = new RdsJudicialPiInfoModel();
				rdsJudicialPiInfoModel.setSub_case_code(sub_case_code);
				rdsJudicialPiInfoModel.setParam_type(param_type);
				rdsJudicialPiInfoModel.setChild(childStr);
				rdsJudicialPiInfoModel.setParent(motherStr);
				rdsJudicialPiInfoModel.setParent2(fatherStr);
				rdsJudicialPiInfoModel.setGene1(StringUtils.complete0(
						new BigDecimal(gene1).setScale(4,
								BigDecimal.ROUND_HALF_UP).toString(), 4));
				rdsJudicialPiInfoModel.setGene2(StringUtils.complete0(
						new BigDecimal(gene2).setScale(4,
								BigDecimal.ROUND_HALF_UP).toString(), 4));
				rdsJudicialPiInfoModel.setFunction(result.get("function"));
				rdsJudicialPiInfoModel.setPi(StringUtils.complete0(
						(sub_pi + ""), 4));
				rdsJudicialCaseParamMapper.insertPiInfo(rdsJudicialPiInfoModel);// 每一组亲权值数值
			} else {
				String function = result.get("function");
				Map<String, Object> params = new HashMap<String, Object>();
				String param_type = ateName;
				params.put("param_type", param_type);
				params.put("param_name", result.get("param_name"));
				params.put("reagent_name", child.getReagent_name());
				String temp = queryGen(params);
				if (temp == null) {
					continue;
				}
				gene1 = Double.parseDouble(temp);
				double sub_pi = 0;
				if (function.equals("1/p") || function.equals("1/q")
						|| function.equals("1/r"))
					sub_pi = 1 / gene1;
				else if (function.equals("1/(2p)") || function.equals("1/(2q)")
						|| function.equals("1/(2r)"))
					sub_pi = 0.5 / gene1;
				pi = pi.multiply(new BigDecimal(sub_pi));
				sub_pi = new BigDecimal(sub_pi).setScale(4,
						BigDecimal.ROUND_HALF_UP).doubleValue();
				RdsJudicialPiInfoModel rdsJudicialPiInfoModel = new RdsJudicialPiInfoModel();
				rdsJudicialPiInfoModel.setSub_case_code(sub_case_code);
				rdsJudicialPiInfoModel.setParam_type(param_type);
				rdsJudicialPiInfoModel.setChild(childStr);
				rdsJudicialPiInfoModel.setParent(motherStr);
				rdsJudicialPiInfoModel.setParent2(fatherStr);
				rdsJudicialPiInfoModel.setGene1(StringUtils.complete0(
						new BigDecimal(gene1).setScale(4,
								BigDecimal.ROUND_HALF_UP).toString(), 4));
				rdsJudicialPiInfoModel.setFunction(result.get("function"));
				rdsJudicialPiInfoModel.setPi(StringUtils.complete0(
						(sub_pi + ""), 4));
				rdsJudicialCaseParamMapper.insertPiInfo(rdsJudicialPiInfoModel);
			}
		}
		return pi;
	}

	/**
	 * 两个人否定计算pi
	 */
	private BigDecimal calPIByTb(String sub_case_code,
			RdsJudicialSampleResultModel parent1,
			RdsJudicialSampleResultModel child, String unmatched_node)
			throws IllegalAccessException {
		BigDecimal pi = new BigDecimal(1);
		double gene1 = 0;
		double gene2 = 0;
		Map<String, Object> record = parent1.getRecord();
		Set<String> set = record.keySet();
		String[] nodes = unmatched_node.split(",");
		List<String> tempList = Arrays.asList(nodes);
		for (String ateName : set) {
			if (tempList.contains(ateName)) {
				// 取出不匹配的基因位点然后用否定算法出pi值
				String parentStr = (String) parent1.getRecord().get(ateName);
				String childStr = (String) child.getRecord().get(ateName);
				// Map<String,String> result =
				// findFunctionByTb(parentStr,childStr);
				// 排序
				// List<AlgModel> numbersort = AlgUtil.sort(numberList);
				// 获取可能性
				// AlgReturnValueModel result =
				// AlgUtil.algPossibility(numbersort);
				// 可能性字符串
				// String possibility = result.getPossibility();
				// 步长
				// double step = result.getStep();
				// 获取公式
				// String function = AlgUtil.getFormulaTranslation(possibility);
				// String[] possiblitys = possibility.split("+");
				AlgMain main = new AlgMain(childStr, parentStr);
				AlgReturnValueModel model = main.getAlgResult();

				System.out.println("    " + childStr + " " + parentStr + "--"
						+ model.getStep() + ", " + model.getPossibility()
						+ "   function:" + model.getFunction());

				double μ = 0.0;
				if (parent1.getSample_call().toString().equals("father")) {
					// 如果是父亲平均突变率μ=0.002
					μ = 0.002;
				} else {
					// 如果是母亲 平均突变率μ=0.0005
					μ = 0.0005;
				}
				double n = model.getStep();
				double A1 = 0.0;
				double A2 = 0.0;

				String[] childs = childStr.split(",");
				String[] parents = parentStr.split(",");
				if (childs.length == 1) {
					A1 = Double.parseDouble(childs[0]);
					A2 = Double.parseDouble(childs[0]);
				} else {
					A1 = Double.parseDouble(childs[0]);
					A2 = Double.parseDouble(childs[1]);
				}
				Map<String, Object> params = new HashMap<String, Object>();
				String function = model.getFunction();
				String param_type = ateName;
				params.put("param_type", param_type);
				params.put("param_name", A1);
				params.put("reagent_name", child.getReagent_name());
				String temp = queryGen(params);
				if (temp == null) {
					continue;
				}
				gene1 = Double.parseDouble(temp);
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.put("param_type", param_type);
				params1.put("param_name", A2);
				params1.put("reagent_name", child.getReagent_name());
				String temp2 = queryGen(params1);
				if (temp2 == null) {
					continue;
				}
				gene2 = Double.parseDouble(temp2);
				double sub_pi = 0;
				double mun = Math.pow(10, (Math.ceil(n) - 1));
				if (function.equals("μ/8p*10(n-1)")) {
					sub_pi = μ / (8 * gene1 * mun);
				} else if (function.equals("μ/8q*10(n-1)")) {
					sub_pi = μ / (8 * gene2 * mun);
				} else if (function.equals("μ/4p*10(n-1)")) {
					sub_pi = μ / (4 * gene1 * mun);
				} else if (function.equals("μ(p+q)/8pq*10(n-1)")) {
					sub_pi = μ * (gene1 + gene2) / (8 * gene1 * gene2 * mun);
				} else if (function.equals("μ/4q*10(n-1)")) {
					sub_pi = μ / (4 * gene2 * mun);
				} else if (function.equals("μ(2p+q)/8pq*10(n-1)")) {
					sub_pi = μ * (2 * gene1 + gene2)
							/ (8 * gene1 * gene2 * mun);
				} else if (function.equals("μ(p+2q)/8pq*10(n-1)")) {
					sub_pi = μ * (gene1 + 2 * gene2)
							/ (8 * gene1 * gene2 * mun);
				} else if (function.equals("μ(p+q)/4pq*10(n-1)")) {
					sub_pi = μ * (gene1 + gene2) / (4 * gene1 * gene2 * mun);
				} else if (function.equals("μ/2p*10(n-1)")) {
					sub_pi = μ / (2 * gene1 * mun);
				}
				pi = pi.multiply(new BigDecimal(sub_pi));
				double sub_pi1 = new BigDecimal(sub_pi).setScale(4,
						BigDecimal.ROUND_HALF_UP).doubleValue();
				if (sub_pi1 != 0.0 || sub_pi1 != 0.00 || sub_pi1 != 0.000
						|| sub_pi1 != 0.0000) {
					sub_pi = sub_pi1;
				} else {
					String a = String.valueOf(sub_pi);
					String[] s = a.split("E");
					String t = "";
					if (s.length == 2) {
						double temp1 = Double.parseDouble(s[0]);
						t = new DecimalFormat("0.0000").format(temp1);
					}
					String subpiString = "" + t + "E" + s[1] + "";
					sub_pi = Double.valueOf(subpiString);
				}
				RdsJudicialPiInfoModel rdsJudicialPiInfoModel = new RdsJudicialPiInfoModel();
				rdsJudicialPiInfoModel.setSub_case_code(sub_case_code);
				rdsJudicialPiInfoModel.setParam_type(param_type);
				rdsJudicialPiInfoModel.setChild(childStr);
				rdsJudicialPiInfoModel.setParent(parentStr);
				rdsJudicialPiInfoModel.setGene1(StringUtils.complete0(
						new BigDecimal(gene1).setScale(4,
								BigDecimal.ROUND_HALF_UP).toString(), 4));
				rdsJudicialPiInfoModel.setGene2(StringUtils.complete0(
						new BigDecimal(gene2).setScale(4,
								BigDecimal.ROUND_HALF_UP).toString(), 4));
				rdsJudicialPiInfoModel.setFunction(model.getFunction());
				rdsJudicialPiInfoModel.setPi(StringUtils.complete0(
						(sub_pi + ""), 4));
				rdsJudicialCaseParamMapper.insertPiInfo(rdsJudicialPiInfoModel);

				/*
				 * if(result.size()>=3){ //就是有两个点位 Map<String,Object> params=
				 * new HashMap<String, Object>(); String function =
				 * result.get("function"); String param_type = ateName;
				 * params.put("param_type",param_type);
				 * params.put("param_name",result.get("param_name"));
				 * params.put("reagent_name",child.getReagent_name()); String
				 * temp = queryGen(params); if(temp==null){ continue; } gene1 =
				 * Double.parseDouble(temp); Map<String,Object> params1= new
				 * HashMap<String, Object>();
				 * params1.put("param_type",param_type);
				 * params1.put("param_name",result.get("param_name1"));
				 * params1.put("reagent_name",child.getReagent_name()); String
				 * temp2 = queryGen(params1); if(temp2==null){ continue; } gene2
				 * = Double.parseDouble(temp2); double sub_pi = 0;
				 * if(function.equals("μ(2p+q)/(8pq)")){ sub_pi =
				 * 0.25*μ/gene1+0.125*μ/gene2; }else if
				 * (function.equals("μ(p+2q)/(8pq)")) { sub_pi =
				 * 0.125*μ/gene1+0.25*μ/gene2; }else
				 * if(function.equals("μ(2p+q)/(80pq)")){ sub_pi =
				 * 0.025*μ/gene1+0.0125*μ/gene2; }else if
				 * (function.equals("μ(p+2q)/(80pq)")) { sub_pi =
				 * 0.0125*μ/gene1+0.025*μ/gene2; }else
				 * if(function.equals("μ(2p+q)/(800pq)")){ sub_pi =
				 * 0.0025*μ/gene1+0.00125*μ/gene2; }else if
				 * (function.equals("μ(p+2q)/(800pq)")) { sub_pi =
				 * 0.00125*μ/gene1+0.0025*μ/gene2; }else
				 * if(function.equals("μ(2p+q)/(8000pq)")){ sub_pi =
				 * 0.00025*μ/gene1+0.000125*μ/gene2; }else if
				 * (function.equals("μ(p+2q)/(8000pq)")) { sub_pi =
				 * 0.000125*μ/gene1+0.00025*μ/gene2; }else if
				 * (function.equals("μ(p+q)/(8pq)")) { sub_pi =
				 * 0.125*(μ/gene1+μ/gene2); }else if
				 * (function.equals("μ(p+q)/(80pq)")) { sub_pi =
				 * 0.0125*(μ/gene1+μ/gene2); }else if
				 * (function.equals("μ(p+q)/(800pq)")) { sub_pi =
				 * 0.00125*(μ/gene1+μ/gene2); }else if
				 * (function.equals("μ(p+q)/(8000pq)")) { sub_pi =
				 * 0.000125*(μ/gene1+μ/gene2); }else
				 * if(function.equals("μ(p+q)/(4pq)")){ sub_pi =
				 * 0.25*(μ/gene1+μ/gene2); }else if
				 * (function.equals("μ(p+q)/(40pq)")) { sub_pi =
				 * 0.025*(μ/gene1+μ/gene2); }else if
				 * (function.equals("μ(p+q)/(400pq)")) { sub_pi =
				 * 0.0025*(μ/gene1+μ/gene2); }else if
				 * (function.equals("μ(p+q)/(4000pq)")) { sub_pi =
				 * 0.00025*(μ/gene1+μ/gene2); }else if
				 * (function.equals("μ(p+q)/(40000pq)")) { sub_pi =
				 * 0.000025*(μ/gene1+μ/gene2); }else if
				 * (function.equals("μ(p+q)/(400000pq)")) { sub_pi =
				 * 0.0000025*(μ/gene1+μ/gene2); }else if
				 * (result.get("n")!=null){ double n=
				 * Double.valueOf(result.get("n")); String dString =
				 * result.get("function"); if (dString.equals("μ/4p*")) { sub_pi
				 * = μ/(4*gene1*n); }else if(dString.equals("μ/2p*")){ sub_pi =
				 * μ/(2*gene1*n); }else if (dString.equals("μ/8p*")) { sub_pi =
				 * μ/(8*gene1*n); }else if (dString.equals("μ(p+q)/(8pq)/n")) {
				 * sub_pi = (μ*gene1+μ*gene2)/(8*gene1*gene2*n); }else if
				 * (dString.equals("μ(2p+q)/(8pq)/n")) { sub_pi =
				 * (2*μ*gene1+μ*gene2)/(8*gene1*gene2*n); }else if
				 * (dString.equals("μ(p+q)/(4pq)/n")) { sub_pi =
				 * (μ*gene1+μ*gene2)/(4*gene1*gene2*n); }else { sub_pi =
				 * (μ*gene1+2*μ*gene2)/(8*gene1*gene2*n); } } pi =
				 * pi.multiply(new BigDecimal(sub_pi)); double sub_pi1 = new
				 * BigDecimal
				 * (sub_pi).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
				 * if
				 * (sub_pi1!=0.0||sub_pi1!=0.00||sub_pi1!=0.000||sub_pi1!=0.0000
				 * ) { sub_pi=sub_pi1; }else { String a =
				 * String.valueOf(sub_pi); String [] s =a.split("E"); String
				 * t="" ; if(s.length==2){ double
				 * temp1=Double.parseDouble(s[0]); t=new
				 * DecimalFormat("0.0000").format(temp1); } String subpiString =
				 * ""+t+"E"+s[1]+""; sub_pi=Double.valueOf(subpiString); }
				 * RdsJudicialPiInfoModel rdsJudicialPiInfoModel = new
				 * RdsJudicialPiInfoModel();
				 * rdsJudicialPiInfoModel.setSub_case_code(sub_case_code);
				 * rdsJudicialPiInfoModel.setParam_type(param_type);
				 * rdsJudicialPiInfoModel.setChild(childStr);
				 * rdsJudicialPiInfoModel.setParent(parentStr);
				 * rdsJudicialPiInfoModel.setGene1(StringUtils.complete0(new
				 * BigDecimal
				 * (gene1).setScale(4,BigDecimal.ROUND_HALF_UP).toString(),4));
				 * rdsJudicialPiInfoModel.setGene2(StringUtils.complete0(new
				 * BigDecimal
				 * (gene2).setScale(4,BigDecimal.ROUND_HALF_UP).toString(),4));
				 * rdsJudicialPiInfoModel.setFunction(result.get("function"));
				 * rdsJudicialPiInfoModel
				 * .setPi(StringUtils.complete0((sub_pi+""),4));
				 * rdsJudicialCaseParamMapper
				 * .insertPiInfo(rdsJudicialPiInfoModel); }else{ String function
				 * = result.get("function"); Map<String,Object> params= new
				 * HashMap<String, Object>(); String param_type = ateName;
				 * params.put("param_type",param_type);
				 * params.put("param_name",result.get("param_name"));
				 * params.put("reagent_name",child.getReagent_name()); String
				 * temp = queryGen(params); if(temp == null){ continue; } gene1
				 * = Double.parseDouble(temp); double sub_pi = 0;
				 * if(function.equals("μ/2p")) sub_pi = 0.5*μ/gene1; else
				 * if(function.equals("μ/20p")) sub_pi = 0.05*μ/gene1; else
				 * if(function.equals("μ/200p")) sub_pi = 0.005*μ/gene1; else
				 * if(function.equals("μ/2000p")) sub_pi = 0.0005*μ/gene1; else
				 * if(function.equals("μ/20000p")) sub_pi = 0.00005*μ/gene1;
				 * else if(function.equals("μ/4p")) sub_pi = 0.25*μ/gene1; else
				 * if(function.equals("μ/40p")) sub_pi = 0.025*μ/gene1; else
				 * if(function.equals("μ/400p")) sub_pi = 0.0025*μ/gene1; else
				 * if(function.equals("μ/4000p")) sub_pi = 0.00025*μ/gene1; else
				 * if(function.equals("μ/40000p")) sub_pi = 0.000025*μ/gene1;
				 * else if(function.equals("μ/400000p")) sub_pi =
				 * 0.0000025*μ/gene1; else if(function.equals("μ/8p")) sub_pi =
				 * 0.125*μ/gene1; else if(function.equals("μ/80p")) sub_pi =
				 * 0.0125*μ/gene1; else if(function.equals("μ/800p")) sub_pi =
				 * 0.00125*μ/gene1; else if(function.equals("μ/8000p")) sub_pi =
				 * 0.000125*μ/gene1; pi = pi.multiply(new BigDecimal(sub_pi));
				 * sub_pi = new
				 * BigDecimal(sub_pi).setScale(8,BigDecimal.ROUND_HALF_UP
				 * ).doubleValue(); RdsJudicialPiInfoModel
				 * rdsJudicialPiInfoModel = new RdsJudicialPiInfoModel();
				 * rdsJudicialPiInfoModel.setSub_case_code(sub_case_code);
				 * rdsJudicialPiInfoModel.setParam_type(param_type);
				 * rdsJudicialPiInfoModel.setChild(childStr);
				 * rdsJudicialPiInfoModel.setParent(parentStr);
				 * rdsJudicialPiInfoModel.setGene1(StringUtils.complete0(new
				 * BigDecimal
				 * (gene1).setScale(4,BigDecimal.ROUND_HALF_UP).toString(),4));
				 * rdsJudicialPiInfoModel.setFunction(result.get("function"));
				 * rdsJudicialPiInfoModel
				 * .setPi(StringUtils.complete0((sub_pi+""),4));
				 * rdsJudicialCaseParamMapper
				 * .insertPiInfo(rdsJudicialPiInfoModel); }
				 */
			}
		}
		return pi;
	}

	/**
	 * 3个人否定亲权值
	 * */
	private BigDecimal calPIByTb(String sub_case_code,
			RdsJudicialSampleResultModel parent1,
			RdsJudicialSampleResultModel parent2,
			RdsJudicialSampleResultModel child, String unmatched_node)
			throws IllegalAccessException {
		RdsJudicialSampleResultModel mother = null;
		RdsJudicialSampleResultModel father = null;
		// 判断性别
		if ("X".equals(parent1.getRecord().get("AMEL"))
				|| "X".equals(parent1.getRecord().get("Amel"))) {
			mother = parent1;
			father = parent2;
		} else {
			mother = parent2;
			father = parent1;
		}
		BigDecimal pi = new BigDecimal(1);
		double gene1 = 0;
		double gene2 = 0;
		Map<String, Object> record = parent1.getRecord();
		Set<String> set = record.keySet();
		String[] nodes = unmatched_node.split(",");
		List<String> tempList = Arrays.asList(nodes);
		for (String ateName : set) {
			if (tempList.contains(ateName) && !ateName.equals("AMEL")) {
				// 比对每一组基因位,根据基因名称找到对应的值
				String fatherStr = (String) father.getRecord().get(ateName);
				String motherStr = (String) mother.getRecord().get(ateName);
				String childStr = (String) child.getRecord().get(ateName);
				// 取出不匹配的基因位点然后用否定算法出pi值
				// 获取函数
				Map<String, String> result = findFunctionByTb(motherStr,
						fatherStr, childStr);// {param_name=14, function=1/(2r)}
				if (result.size() == 3) {
					String function = result.get("function");
					Map<String, Object> params = new HashMap<String, Object>();
					String param_type = ateName;
					params.put("param_type", param_type);
					params.put("param_name", result.get("param_name"));
					params.put("reagent_name", child.getReagent_name());
					String temp = queryGen(params);
					// temp是一个数值就是函数里面的参数值
					if (temp == null) {
						continue;
					}
					gene1 = Double.parseDouble(temp);
					Map<String, Object> params1 = new HashMap<String, Object>();
					params1.put("param_type", param_type);
					params1.put("param_name", result.get("param_name1"));
					params1.put("reagent_name", child.getReagent_name());
					String temp2 = queryGen(params1);
					if (temp2 == null) {
						continue;
					}
					gene2 = Double.parseDouble(temp2);
					double sub_pi = 0;

					// 讨论函数参数值有两个参数
					if (function.equals("μ/[4(p+q)]")) {
						sub_pi = 0.002 / (4 * gene1 + 4 * gene2);
					} else if (function.equals("μ/[40(p+q)]")) {
						sub_pi = 0.002 / (40 * gene1 + 40 * gene2);
					} else if (function.equals("μ/[400(p+q)]")) {
						sub_pi = 0.002 / (400 * gene1 + 400 * gene2);
					} else if (function.equals("μ/[4000(p+q)]")) {
						sub_pi = 0.002 / (4000 * gene1 + 4000 * gene2);
					} else if (function.equals("μ/[2(p+q)]")) {
						sub_pi = 0.002 / (2 * gene1 + 2 * gene2);
					} else if (function.equals("μ/[20(p+q)]")) {
						sub_pi = 0.002 / (20 * gene1 + 20 * gene2);
					} else if (function.equals("μ/[200(p+q)]")) {
						sub_pi = 0.002 / (200 * gene1 + 200 * gene2);
					} else if (function.equals("μ/[2000(p+q)]")) {
						sub_pi = 0.002 / (2000 * gene1 + 2000 * gene2);
					} else if (function.equals("(3μ)/[4(p+q)]")) {
						sub_pi = 0.006 / (4 * gene1 + 4 * gene2);
					} else if (function.equals("(3μ)/[40(p+q)]")) {
						sub_pi = 0.006 / (40 * gene1 + 40 * gene2);
					} else if (function.equals("(3μ)/[400(p+q)]")) {
						sub_pi = 0.006 / (400 * gene1 + 400 * gene2);
					} else if (function.equals("(3μ)/[4000(p+q)]")) {
						sub_pi = 0.006 / (4000 * gene1 + 4000 * gene2);
					} else if (function.equals("μm/[4(p+q)]")) {
						sub_pi = 0.0005 / (4 * gene1 + 4 * gene2);
					} else if (function.equals("μm/[40(p+q)]")) {
						sub_pi = 0.0005 / (40 * gene1 + 40 * gene2);
					} else if (function.equals("μm/[400(p+q)]")) {
						sub_pi = 0.0005 / (400 * gene1 + 400 * gene2);
					} else if (function.equals("μm/[4000(p+q)]")) {
						sub_pi = 0.0005 / (4000 * gene1 + 4000 * gene2);
					} else if (function.equals("μm/[2(p+q)]")) {
						sub_pi = 0.0005 / (2 * gene1 + 2 * gene2);
					} else if (function.equals("μm/[20(p+q)]")) {
						sub_pi = 0.0005 / (20 * gene1 + 20 * gene2);
					} else if (function.equals("μm/[200(p+q)]")) {
						sub_pi = 0.0005 / (200 * gene1 + 200 * gene2);
					} else if (function.equals("μm/[2000(p+q)]")) {
						sub_pi = 0.0005 / (2000 * gene1 + 2000 * gene2);
					} else if (function.equals("(3μm)/[4(p+q)]")) {
						sub_pi = 0.0015 / (4 * gene1 + 4 * gene2);
					} else if (function.equals("(3μm)/[40(p+q)]")) {
						sub_pi = 0.0015 / (40 * gene1 + 40 * gene2);
					} else if (function.equals("(3μm)/[400(p+q)]")) {
						sub_pi = 0.0015 / (400 * gene1 + 400 * gene2);
					} else if (function.equals("(3μm)/[4000(p+q)]")) {
						sub_pi = 0.0015 / (4000 * gene1 + 4000 * gene2);
					} else if (result.get("n") != null
							&& function.equals("μ/4(p+q)*")) {
						double n = Double.valueOf(result.get("n"));
						sub_pi = 0.002 / (4 * n * gene1 + 4 * n * gene2);
					} else if (result.get("n") != null
							&& function.equals("μ/4p*")) {
						double n = Double.valueOf(result.get("n"));
						sub_pi = 0.002 / (4 * gene1 * n);
					} else if (result.get("n") != null
							&& function.equals("μ/2p*")) {
						double n = Double.valueOf(result.get("n"));
						sub_pi = 0.002 / (2 * gene1 * n);
					} else if (result.get("n") != null
							&& function.equals("μm/4p*")) {
						double n = Double.valueOf(result.get("n"));
						sub_pi = 0.0005 / (4 * gene1 * n);
					} else if (result.get("n") != null
							&& function.equals("μm/2p*")) {
						double n = Double.valueOf(result.get("n"));
						sub_pi = 0.0005 / (2 * gene1 * n);
					}
					// System.out.println(sub_pi);
					pi = pi.multiply(new BigDecimal(sub_pi));
					sub_pi = new BigDecimal(sub_pi).setScale(8,
							BigDecimal.ROUND_HALF_UP).doubleValue();

					RdsJudicialPiInfoModel rdsJudicialPiInfoModel = new RdsJudicialPiInfoModel();
					rdsJudicialPiInfoModel.setSub_case_code(sub_case_code);
					rdsJudicialPiInfoModel.setParam_type(param_type);
					rdsJudicialPiInfoModel.setChild(childStr);
					rdsJudicialPiInfoModel.setParent(motherStr);
					rdsJudicialPiInfoModel.setParent2(fatherStr);
					rdsJudicialPiInfoModel.setGene1(StringUtils.complete0(
							new BigDecimal(gene1).setScale(4,
									BigDecimal.ROUND_HALF_UP).toString(), 4));
					rdsJudicialPiInfoModel.setGene2(StringUtils.complete0(
							new BigDecimal(gene2).setScale(4,
									BigDecimal.ROUND_HALF_UP).toString(), 4));
					rdsJudicialPiInfoModel.setFunction(result.get("function"));
					rdsJudicialPiInfoModel.setPi(StringUtils.complete0(
							(sub_pi + ""), 4));
					rdsJudicialCaseParamMapper
							.insertPiInfo(rdsJudicialPiInfoModel);// 每一组亲权值数值
				} else {
					String function = result.get("function");
					Map<String, Object> params = new HashMap<String, Object>();
					String param_type = ateName;
					params.put("param_type", param_type);
					params.put("param_name", result.get("param_name"));
					params.put("reagent_name", child.getReagent_name());
					String temp = queryGen(params);
					if (temp == null) {
						continue;
					}
					gene1 = Double.parseDouble(temp);
					double sub_pi = 0;
					// 只有一个参数
					if (function.equals("μ/(2p)")) {
						sub_pi = 0.001 / (gene1);
					} else if (function.equals("μ/(20p)")) {
						sub_pi = 0.001 / (10 * gene1);
					} else if (function.equals("μ/(200p)")) {
						sub_pi = 0.001 / (100 * gene1);
					} else if (function.equals("μ/(2000p)")) {
						sub_pi = 0.001 / (1000 * gene1);
					} else if (function.equals("μm/(2p)")) {
						sub_pi = 0.0005 / (2 * gene1);
					} else if (function.equals("μm/(20p)")) {
						sub_pi = 0.0005 / (20 * gene1);
					} else if (function.equals("μm/(200p)")) {
						sub_pi = 0.0005 / (200 * gene1);
					} else if (function.equals("μm/(2000p)")) {
						sub_pi = 0.0005 / (2000 * gene1);
					} else if (function.equals("μ/(4p)")) {
						sub_pi = 0.001 / (2 * gene1);
					} else if (function.equals("μ/(40p)")) {
						sub_pi = 0.001 / (20 * gene1);
					} else if (function.equals("μ/(400p)")) {
						sub_pi = 0.001 / (200 * gene1);
					} else if (function.equals("μ/(4000p)")) {
						sub_pi = 0.001 / (2000 * gene1);
					} else if (function.equals("μm/(4p)")) {
						sub_pi = 0.0005 / (4 * gene1);
					} else if (function.equals("μm/(40p)")) {
						sub_pi = 0.0005 / (40 * gene1);
					} else if (function.equals("μm/(400p)")) {
						sub_pi = 0.0005 / (400 * gene1);
					} else if (function.equals("μm/(4000p)")) {
						sub_pi = 0.0005 / (4000 * gene1);
					} else if (function.equals("μf/(4p)")) {
						sub_pi = 0.002 / (4 * gene1);
					} else if (function.equals("μf/(40p)")) {
						sub_pi = 0.002 / (40 * gene1);
					} else if (function.equals("μf/(400p)")) {
						sub_pi = 0.002 / (400 * gene1);
					} else if (function.equals("(μf+μm)/(2p)")) {
						sub_pi = 0.0025 / (2 * gene1);
					} else if (function.equals("(μf+μm)/(20p)")) {
						sub_pi = 0.0025 / (20 * gene1);
					} else if (function.equals("(μf+μm)/(200p)")) {
						sub_pi = 0.0025 / (200 * gene1);
					} else if (function.equals("(μf+μm)/(4p)")) {
						sub_pi = 0.0025 / (4 * gene1);
					} else if (function.equals("(μf+μm)/(40p)")) {
						sub_pi = 0.0025 / (40 * gene1);
					} else if (function.equals("(μf+μm)/(400p)")) {
						sub_pi = 0.0025 / (400 * gene1);
					} else if (function.equals("(2μf+μm)/(4p)")) {
						sub_pi = 0.0045 / (4 * gene1);
					} else if (function.equals("(2μf+μm)/(40p)")) {
						sub_pi = 0.0045 / (40 * gene1);
					} else if (function.equals("(2μf+μm)/(400p)")) {
						sub_pi = 0.0045 / (400 * gene1);
					} else if (function.equals("(μf+2μm)/(4p)")) {
						sub_pi = 0.003 / (4 * gene1);
					} else if (function.equals("(μf+2μm)/(40p)")) {
						sub_pi = 0.003 / (40 * gene1);
					} else if (function.equals("(μf+2μm)/(400p)")) {
						sub_pi = 0.003 / (400 * gene1);
					}
					// System.out.println(sub_pi);
					pi = pi.multiply(new BigDecimal(sub_pi));
					sub_pi = new BigDecimal(sub_pi).setScale(8,
							BigDecimal.ROUND_HALF_UP).doubleValue();
					RdsJudicialPiInfoModel rdsJudicialPiInfoModel = new RdsJudicialPiInfoModel();
					rdsJudicialPiInfoModel.setSub_case_code(sub_case_code);
					rdsJudicialPiInfoModel.setParam_type(param_type);
					rdsJudicialPiInfoModel.setChild(childStr);
					rdsJudicialPiInfoModel.setParent(motherStr);
					rdsJudicialPiInfoModel.setParent2(fatherStr);
					rdsJudicialPiInfoModel.setGene1(StringUtils.complete0(
							new BigDecimal(gene1).setScale(4,
									BigDecimal.ROUND_HALF_UP).toString(), 4));
					rdsJudicialPiInfoModel.setFunction(result.get("function"));
					rdsJudicialPiInfoModel.setPi(StringUtils.complete0(
							(sub_pi + ""), 4));
					rdsJudicialCaseParamMapper
							.insertPiInfo(rdsJudicialPiInfoModel);
				}
			}
		}
		return pi;
	}

	/**
	 * 两个人否定公式
	 */
	private Map<String, String> findFunctionByTb(String parent, String child) {
		Map<String, String> result = new HashMap<String, String>();
		// 看孩子的两个位点跟父亲的两个位点比较
		String[] childs = child.split(",");
		String[] parents = parent.split(",");
		double genonebyone = 0.0;
		double genonebytwo = 0.0;
		double gentwobyone = 0.0;
		double gentwobytwo = 0.0;

		if (childs.length == 1) {
			if (parents.length == 1) {
				genonebyone = Math.abs(Double.parseDouble(childs[0])
						- Double.parseDouble(parents[0]));
				genonebytwo = genonebyone;
				gentwobyone = genonebyone;
				gentwobytwo = genonebyone;
			} else {
				genonebyone = Math.abs(Double.parseDouble(childs[0])
						- Double.parseDouble(parents[0]));
				genonebytwo = Math.abs(Double.parseDouble(childs[0])
						- Double.parseDouble(parents[1]));
				gentwobyone = genonebyone;
				gentwobytwo = genonebytwo;
			}
		} else {
			if (parents.length == 1) {
				genonebyone = Math.abs(Double.parseDouble(childs[0])
						- Double.parseDouble(parents[0]));
				genonebytwo = genonebyone;
				gentwobyone = Math.abs(Double.parseDouble(childs[1])
						- Double.parseDouble(parents[0]));
				gentwobytwo = gentwobyone;
			} else {
				genonebyone = Math.abs(Double.parseDouble(childs[0])
						- Double.parseDouble(parents[0]));
				genonebytwo = Math.abs(Double.parseDouble(childs[0])
						- Double.parseDouble(parents[1]));
				gentwobyone = Math.abs(Double.parseDouble(childs[1])
						- Double.parseDouble(parents[0]));
				gentwobytwo = Math.abs(Double.parseDouble(childs[1])
						- Double.parseDouble(parents[1]));
			}
		}

		DecimalFormat df = new DecimalFormat("#.0");
		genonebyone = Double.valueOf(df.format(genonebyone));
		genonebytwo = Double.valueOf(df.format(genonebytwo));
		gentwobyone = Double.valueOf(df.format(gentwobyone));
		gentwobytwo = Double.valueOf(df.format(gentwobytwo));

		if (childs.length == 1 || parents.length == 1) {
			if (parents.length == 1 && childs.length != 1) {
				// 孩子两个值父亲一个值 只要比孩子和父亲一个值
				if (genonebyone == 1 && gentwobyone == 1) {
					// 两种可能 +两种可能 一步
					result.put("param_name", childs[0]);
					result.put("param_name1", childs[1]);
					result.put("function", "μ(p+q)/(4pq)");
				} else if (genonebyone == 2 && gentwobyone == 2) {
					// 两种可能 +两种可能 二步
					result.put("param_name", childs[0]);
					result.put("param_name1", childs[1]);
					result.put("function", "μ(p+q)/(40pq)");
				} else if (genonebyone == 3 && gentwobyone == 3) {
					// 两种可能 +两种可能 三步
					result.put("param_name", childs[0]);
					result.put("param_name1", childs[1]);
					result.put("function", "μ(p+q)/(400pq)");
				} else if (genonebyone == 4 && gentwobyone == 4) {
					// 两种可能 +两种可能 四步
					result.put("param_name", childs[0]);
					result.put("param_name1", childs[1]);
					result.put("function", "μ(p+q)/(4000pq)");
				} else if (genonebyone == 5 && gentwobyone == 5) {
					result.put("param_name", childs[0]);
					result.put("param_name1", childs[1]);
					result.put("function", "μ(p+q)/(40000pq)");
				} else if (genonebyone == 6 && gentwobyone == 6) {
					result.put("param_name", childs[0]);
					result.put("param_name1", childs[1]);
					result.put("function", "μ(p+q)/(400000pq)");
				} else if ((genonebyone == 1 && gentwobyone > 1)
						|| (genonebyone > 1 && gentwobyone == 1)) {
					if (genonebyone == 1)
						result.put("param_name", childs[0]);
					else
						result.put("param_name", childs[1]);
					result.put("function", "μ/4p");
				} else if ((genonebyone == 2 && gentwobyone > 2)
						|| (genonebyone > 2 && gentwobyone == 2)) {
					if (genonebyone == 2)
						result.put("param_name", childs[0]);
					else
						result.put("param_name", childs[1]);
					result.put("function", "μ/40p");
				} else if ((genonebyone == 3 && gentwobyone > 3)
						|| (genonebyone > 3 && gentwobyone == 3)) {
					if (genonebyone == 3)
						result.put("param_name", childs[0]);
					else
						result.put("param_name", childs[1]);
					result.put("function", "μ/400p");
				} else if ((genonebyone == 4 && gentwobyone > 4)
						|| (genonebyone > 4 && gentwobyone == 4)) {
					if (genonebyone == 4)
						result.put("param_name", childs[0]);
					else
						result.put("param_name", childs[1]);
					result.put("function", "μ/4000p");
				} else if ((genonebyone == 5 && gentwobyone > 5)
						|| (genonebyone > 5 && gentwobyone == 5)) {
					if (genonebyone < gentwobyone)
						result.put("param_name", childs[0]);
					else
						result.put("param_name", childs[1]);
					result.put("function", "μ/40000p");
				} else if ((genonebyone == 6 && gentwobyone > 6)
						|| (genonebyone > 6 && gentwobyone == 6)) {
					if (genonebyone == 6)
						result.put("param_name", childs[0]);
					else
						result.put("param_name", childs[1]);
					result.put("function", "μ/400000p");
				} else {
					// 是否可以加一个小数点的
					double max1 = genonebyone > gentwobytwo ? gentwobytwo
							: genonebyone;
					if ((genonebyone == gentwobyone)
							|| (Math.ceil(genonebyone) == Math
									.ceil(gentwobyone) && genonebyone == gentwobyone)
							|| (Math.ceil(genonebyone) == Math
									.ceil(gentwobyone)
									&& genonebyone != gentwobyone
									&& Math.ceil(genonebyone) != gentwobyone && Math
									.ceil(gentwobyone) != genonebyone)) {
						// 两种可能 +两种可能 一步
						result.put("param_name", childs[0]);
						result.put("param_name1", childs[1]);
						String n = Double.toString(Math.pow(10,
								(Math.ceil(max1) - 1)));
						result.put("n", n);
						result.put("function", "μ(p+q)/(4pq)/n");
					} else {
						if ((Math.ceil(max1) == Math.ceil(genonebyone) && Math
								.ceil(max1) != gentwobyone)
								|| (Math.ceil(max1) == Math.ceil(genonebyone) && Math
										.ceil(max1) < gentwobyone))
							result.put("param_name", childs[0]);
						else
							result.put("param_name", childs[1]);
						if ((genonebyone == genonebytwo)
								|| (gentwobyone == gentwobytwo)) {
							// 两种可能
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max1) - 1)));
							result.put("n", n);
							result.put("function", "μ/4p*");
						} else {
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max1) - 1)));
							result.put("n", n);
							result.put("function", "μ/8p*");
						}
					}
				}
			} else if (genonebyone == 1 && genonebytwo == 1 && gentwobytwo == 1
					&& gentwobyone == 1) {
				// 四种可能，一步突变
				result.put("param_name", childs[0]);
				result.put("function", "μ/2p");
			} else if (genonebyone == 2 && genonebytwo == 2 && gentwobytwo == 2
					&& gentwobyone == 2) {
				// 四种可能，二步突变
				result.put("param_name", childs[0]);
				result.put("function", "μ/20p");
			} else if (genonebyone == 3 && genonebytwo == 3 && gentwobytwo == 3
					&& gentwobyone == 3) {
				// 四种可能，三步突变
				result.put("param_name", childs[0]);
				result.put("function", "μ/200p");
			} else if (genonebyone == 4 && genonebytwo == 4 && gentwobytwo == 4
					&& gentwobyone == 4) {
				// 四种可能，四步突变
				result.put("param_name", childs[0]);
				result.put("function", "μ/2000p");
			} else if (genonebyone == 5 && genonebytwo == 5 && gentwobytwo == 5
					&& gentwobyone == 5) {
				// 四种可能，五步突变
				result.put("param_name", childs[0]);
				result.put("function", "μ/20000p");
			} else if ((genonebyone == 1 && genonebytwo > 1)
					|| (genonebyone > 1 && genonebytwo == 1)
					|| (genonebyone == 1 && gentwobyone > 1)
					|| (genonebyone > 1 && gentwobyone == 1)) {
				// 两种可能，一步突变
				if (genonebyone == 1)
					result.put("param_name", childs[0]);
				else if (childs.length == 1)
					result.put("param_name", childs[0]);
				else
					result.put("param_name", childs[1]);

				result.put("function", "μ/4p");
			} else if ((genonebyone == 2 && genonebytwo > 2)
					|| (genonebyone > 2 && genonebytwo == 2)
					|| (genonebyone == 2 && gentwobyone > 2)
					|| (genonebyone > 2 && gentwobyone == 2)) {
				// 两种可能，二步突变
				if (genonebyone == 2)
					result.put("param_name", childs[0]);
				else if (childs.length == 1)
					result.put("param_name", childs[0]);
				else
					result.put("param_name", childs[1]);
				result.put("function", "μ/40p");
			} else if ((genonebyone == 3 && genonebytwo > 3)
					|| (genonebyone > 3 && genonebytwo == 3)
					|| (genonebyone == 3 && gentwobyone > 3)
					|| (genonebyone > 3 && gentwobyone == 3)) {
				// 两种可能，三步突变
				if (genonebyone == 3)
					result.put("param_name", childs[0]);
				else if (childs.length == 1)
					result.put("param_name", childs[0]);
				else
					result.put("param_name", childs[1]);
				result.put("function", "μ/400p");
			} else if ((genonebyone == 4 && genonebytwo > 4)
					|| (genonebyone > 4 && genonebytwo == 4)
					|| (genonebyone == 4 && gentwobyone > 4)
					|| (genonebyone > 4 && gentwobyone == 4)) {
				// 两种可能，四步突变
				if (genonebyone == 4)
					result.put("param_name", childs[0]);
				else if (childs.length == 1)
					result.put("param_name", childs[0]);
				else
					result.put("param_name", childs[1]);
				result.put("function", "μ/4000p");
			} else if ((genonebyone == 5 && genonebytwo > 5)
					|| (genonebyone > 5 && genonebytwo == 5)
					|| (genonebyone == 5 && gentwobyone > 5)
					|| (genonebyone > 5 && gentwobyone == 5)) {
				// 两种可能，五步突变
				if (genonebyone == 5)
					result.put("param_name", childs[0]);
				else if (childs.length == 1)
					result.put("param_name", childs[0]);
				else
					result.put("param_name", childs[1]);
				result.put("function", "μ/40000p");
			} else {
				// 是否可以加一个小数点的
				double max1 = genonebyone > genonebytwo ? genonebytwo
						: genonebyone;
				double max2 = gentwobyone > gentwobytwo ? gentwobytwo
						: gentwobyone;
				if ((Math.ceil(genonebyone) == Math.ceil(genonebytwo)
						&& Math.ceil(genonebyone) == Math.ceil(gentwobyone)
						&& Math.ceil(gentwobyone) == Math.ceil(gentwobytwo)
						&& genonebyone != Math.ceil(genonebytwo) && max1 == genonebytwo)
						|| (Math.ceil(genonebyone) == Math.ceil(genonebytwo)
								&& Math.ceil(genonebyone) == Math
										.ceil(gentwobyone)
								&& Math.ceil(gentwobyone) == Math
										.ceil(gentwobytwo)
								&& genonebytwo != Math.ceil(genonebyone) && max1 == genonebyone)
						|| (genonebyone == genonebytwo
								&& genonebyone == gentwobytwo && gentwobytwo == gentwobyone)) {
					// 四种可能
					result.put("param_name", childs[0]);
					String n = Double.toString(Math.pow(10,
							(Math.ceil(genonebyone) - 1)));
					result.put("n", n);
					result.put("function", "μ/2p*");
				} else {
					// 两种可能
					// result.put("param_name",childs[0]);
					// String n = Double.toString(Math.pow(10,
					// (Math.ceil(max1)-1)) );
					// result.put("n", n);
					// result.put("function", "μ/4p*");
					double max = max1 > max2 ? max2 : max1;
					if ((max == max1 && Math.ceil(max) < Math.ceil(max2))
							|| Math.ceil(max) != Math.ceil(max2))
						result.put("param_name", childs[0]);
					else if (childs.length == 1)
						result.put("param_name", childs[0]);
					else
						result.put("param_name", childs[1]);
					String n = Double.toString(Math.pow(10,
							(Math.ceil(max) - 1)));
					result.put("n", n);
					result.put("function", "μ/4p*");
				}
			}
		} else {
			if ((genonebyone == 1 && genonebytwo > 1 && gentwobyone == 1 && gentwobytwo > 1)
					|| (genonebyone == 1 && genonebytwo > 1 && gentwobyone > 1 && gentwobytwo == 1)
					|| (genonebyone > 1 && genonebytwo == 1 && gentwobyone == 1 && gentwobytwo > 1)
					|| (genonebyone > 1 && genonebytwo == 1 && gentwobyone > 1 && gentwobytwo == 1)) {
				// 一可能+一可能 一步突变
				result.put("param_name", childs[0]);
				result.put("param_name1", childs[1]);
				result.put("function", "μ(p+q)/(8pq)");
			} else if ((genonebyone == 2 && genonebytwo > 2 && gentwobyone == 2 && gentwobytwo > 2)
					|| (genonebyone == 2 && genonebytwo > 2 && gentwobyone > 2 && gentwobytwo == 2)
					|| (genonebyone > 2 && genonebytwo == 2 && gentwobyone == 2 && gentwobytwo > 2)
					|| (genonebyone > 2 && genonebytwo == 2 && gentwobyone > 2 && gentwobytwo == 2)) {
				// 一可能+一可能 两步突变
				result.put("param_name", childs[0]);
				result.put("param_name1", childs[1]);
				result.put("function", "μ(p+q)/(80pq)");
			} else if ((genonebyone == 3 && genonebytwo > 3 && gentwobyone == 3 && gentwobytwo > 3)
					|| (genonebyone == 3 && genonebytwo > 3 && gentwobyone > 3 && gentwobytwo == 3)
					|| (genonebyone > 3 && genonebytwo == 3 && gentwobyone == 3 && gentwobytwo > 3)
					|| (genonebyone > 3 && genonebytwo == 3 && gentwobyone > 3 && gentwobytwo == 3)) {
				// 一可能+一可能 三步突变
				result.put("param_name", childs[0]);
				result.put("param_name1", childs[1]);
				result.put("function", "μ(p+q)/(800pq)");
			} else if ((genonebyone == 4 && genonebytwo > 4 && gentwobyone == 4 && gentwobytwo > 4)
					|| (genonebyone == 4 && genonebytwo > 4 && gentwobyone > 4 && gentwobytwo == 4)
					|| (genonebyone > 4 && genonebytwo == 4 && gentwobyone == 4 && gentwobytwo > 4)
					|| (genonebyone > 4 && genonebytwo == 4 && gentwobyone > 4 && gentwobytwo == 4)) {
				// 一可能+一可能 四步突变
				result.put("param_name", childs[0]);
				result.put("param_name1", childs[1]);
				result.put("function", "μ(p+q)/(8000pq)");
			} else if ((genonebyone == 1 && genonebytwo > 1 && gentwobyone == 1 && gentwobytwo == 1)
					|| (genonebyone > 1 && genonebytwo == 1 && gentwobyone == 1 && gentwobytwo == 1)
					|| (genonebyone == 1 && genonebytwo == 1
							&& gentwobyone == 1 && gentwobytwo > 1)
					|| (genonebyone == 1 && genonebytwo == 1 && gentwobyone > 1 && gentwobytwo == 1)) {
				// 一可能+两可能 两可能+一可能 一步突变
				result.put("param_name", childs[0]);
				result.put("param_name1", childs[1]);
				if (gentwobyone == 1 && gentwobytwo == 1) {
					result.put("function", "μ(p+2q)/(8pq)");
				} else {
					result.put("function", "μ(2p+q)/(8pq)");
				}
			} else if ((genonebyone == 2 && genonebytwo > 2 && gentwobyone == 2 && gentwobytwo == 2)
					|| (genonebyone > 2 && genonebytwo == 2 && gentwobyone == 2 && gentwobytwo == 2)
					|| (genonebyone == 2 && genonebytwo == 2
							&& gentwobyone == 2 && gentwobytwo > 2)
					|| (genonebyone == 2 && genonebytwo == 2 && gentwobyone > 2 && gentwobytwo == 2)) {
				// 一可能+两可能 两可能+一可能 二步突变
				result.put("param_name", childs[0]);
				result.put("param_name1", childs[1]);
				if (gentwobyone == 2 && gentwobytwo == 2) {
					result.put("function", "μ(p+2q)/(80pq)");
				} else {
					result.put("function", "μ(2p+q)/(80pq)");
				}
			} else if ((genonebyone == 3 && genonebytwo > 3 && gentwobyone == 3 && gentwobytwo == 3)
					|| (genonebyone > 3 && genonebytwo == 3 && gentwobyone == 3 && gentwobytwo == 3)
					|| (genonebyone == 3 && genonebytwo == 3
							&& gentwobyone == 3 && gentwobytwo > 3)
					|| (genonebyone == 3 && genonebytwo == 3 && gentwobyone > 3 && gentwobytwo == 3)) {
				// 一可能+两可能 两可能+一可能 三步突变
				result.put("param_name", childs[0]);
				result.put("param_name1", childs[1]);
				if (gentwobyone == 3 && gentwobytwo == 3) {
					result.put("function", "μ(p+2q)/(800pq)");
				} else {
					result.put("function", "μ(2p+q)/(800pq)");
				}
			} else if ((genonebyone == 4 && genonebytwo > 4 && gentwobyone == 4 && gentwobytwo == 4)
					|| (genonebyone > 4 && genonebytwo == 4 && gentwobyone == 4 && gentwobytwo == 4)
					|| (genonebyone == 4 && genonebytwo == 4
							&& gentwobyone == 4 && gentwobytwo > 4)
					|| (genonebyone == 4 && genonebytwo == 4 && gentwobyone > 4 && gentwobytwo == 4)) {
				// 一可能+两可能 两可能+一可能 四步突变
				result.put("param_name", childs[0]);
				result.put("param_name1", childs[1]);
				if (gentwobyone == 4 && gentwobytwo == 4) {
					result.put("function", "μ(p+2q)/(8000pq)");
				} else {
					result.put("function", "μ(2p+q)/(8000pq)");
				}
			} else if ((genonebyone == 1 && genonebytwo > 1 && gentwobyone > 1 && gentwobytwo > 1)
					|| (genonebyone > 1 && genonebytwo == 1 && gentwobyone > 1 && gentwobytwo > 1)
					|| (genonebyone > 1 && genonebytwo > 1 && gentwobyone == 1 && gentwobytwo > 1)
					|| (genonebyone > 1 && genonebytwo > 1 && gentwobyone > 1 && gentwobytwo == 1)) {
				// 一步突变
				if (genonebyone == 1 || genonebytwo == 1)
					result.put("param_name", childs[0]);
				else
					result.put("param_name", childs[1]);

				result.put("function", "μ/8p");
			} else if ((genonebyone == 2 && genonebytwo > 2 && gentwobyone > 2 && gentwobytwo > 2)
					|| (genonebyone > 2 && genonebytwo == 2 && gentwobyone > 2 && gentwobytwo > 2)
					|| (genonebyone > 2 && genonebytwo > 2 && gentwobyone == 2 && gentwobytwo > 2)
					|| (genonebyone > 2 && genonebytwo > 2 && gentwobyone > 2 && gentwobytwo == 2)) {
				// 二步突变
				if (genonebyone == 2 || genonebytwo == 2)
					result.put("param_name", childs[0]);
				else
					result.put("param_name", childs[1]);

				result.put("function", "μ/80p");
			} else if ((genonebyone == 3 && genonebytwo > 3 && gentwobyone > 3 && gentwobytwo > 3)
					|| (genonebyone > 3 && genonebytwo == 3 && gentwobyone > 3 && gentwobytwo > 3)
					|| (genonebyone > 3 && genonebytwo > 3 && gentwobyone == 3 && gentwobytwo > 3)
					|| (genonebyone > 3 && genonebytwo > 3 && gentwobyone > 3 && gentwobytwo == 3)) {
				// 三步突变
				if (genonebyone == 3 || genonebytwo == 3)
					result.put("param_name", childs[0]);
				else
					result.put("param_name", childs[1]);

				result.put("function", "μ/800p");
			} else if ((genonebyone == 4 && genonebytwo > 4 && gentwobyone > 4 && gentwobytwo > 4)
					|| (genonebyone > 4 && genonebytwo == 4 && gentwobyone > 4 && gentwobytwo > 4)
					|| (genonebyone > 4 && genonebytwo > 4 && gentwobyone == 4 && gentwobytwo > 4)
					|| (genonebyone > 4 && genonebytwo > 4 && gentwobyone > 4 && gentwobytwo == 4)) {
				// 四步突变
				if (genonebyone == 4 || genonebytwo == 4)
					result.put("param_name", childs[0]);
				else
					result.put("param_name", childs[1]);

				result.put("function", "μ/8000p");
			} else if ((genonebyone == 1 && genonebytwo == 1 && gentwobyone > 1 && gentwobytwo > 1)
					|| (genonebyone > 1 && genonebytwo > 1 && gentwobyone == 1 && gentwobytwo == 1)) {
				// 两种可能，一步突变
				if (genonebyone == 1)
					result.put("param_name", childs[0]);
				else
					result.put("param_name", childs[1]);
				result.put("function", "μ/4p");
			} else if ((genonebyone == 2 && genonebytwo == 2 && gentwobyone > 2 && gentwobytwo > 2)
					|| (genonebyone > 2 && genonebytwo > 2 && gentwobyone == 2 && gentwobytwo == 2)) {
				// 两种可能，二步突变
				if (genonebyone == 2)
					result.put("param_name", childs[0]);
				else
					result.put("param_name", childs[1]);
				result.put("function", "μ/40p");

			} else if ((genonebyone == 3 && genonebytwo == 3 && gentwobyone > 3 && gentwobytwo > 3)
					|| (genonebyone > 3 && genonebytwo > 3 && gentwobyone == 3 && gentwobytwo == 3)) {
				// 两种可能，三步突变
				if (genonebyone == 3)
					result.put("param_name", childs[0]);
				else
					result.put("param_name", childs[1]);
				result.put("function", "μ/400p");
			} else if ((genonebyone == 4 && genonebytwo == 4 && gentwobyone > 4 && gentwobytwo > 4)
					|| (genonebyone > 4 && genonebytwo > 4 && gentwobyone == 4 && gentwobytwo == 4)) {
				// 两种可能，四步突变
				if (genonebyone == 4)
					result.put("param_name", childs[0]);
				else
					result.put("param_name", childs[1]);
				result.put("function", "μ/4000p");
			} else {
				// 是否可以加一个小数点的
				if (((genonebyone == genonebytwo)
						&& (Math.ceil(gentwobyone) >= genonebyone)
						&& (Math.ceil(gentwobytwo) >= genonebyone) && (Math
						.ceil(gentwobytwo) != Math.ceil(genonebyone) && (Math
						.ceil(gentwobyone) != Math.ceil(genonebyone))))
						|| ((gentwobyone == gentwobytwo)
								&& (Math.ceil(genonebyone) >= gentwobyone)
								&& Math.ceil(genonebytwo) >= gentwobyone
								&& (Math.ceil(genonebyone) != Math
										.ceil(gentwobyone)) && Math
								.ceil(genonebytwo) != Math.ceil(gentwobyone))) {
					String n = "";
					if ((Math.ceil(genonebyone) == Math.ceil(genonebytwo) && Math
							.ceil(genonebyone) != Math.ceil(gentwobyone))
							|| (Math.ceil(genonebyone) == Math
									.ceil(genonebytwo) && Math
									.ceil(genonebyone) < Math.ceil(gentwobyone))
							|| (Math.ceil(genonebyone) == Math
									.ceil(genonebytwo) && Math
									.ceil(genonebyone) != Math
									.ceil(gentwobytwo))
							|| (Math.ceil(genonebyone) == Math
									.ceil(genonebytwo) && Math
									.ceil(genonebyone) < Math.ceil(gentwobytwo))) {
						result.put("param_name", childs[0]);
						n = Double.toString(Math.pow(10,
								(Math.ceil(genonebyone) - 1)));
					} else {
						result.put("param_name", childs[1]);
						n = Double.toString(Math.pow(10,
								(Math.ceil(gentwobyone) - 1)));
					}
					result.put("n", n);
					result.put("function", "μ/4p*");
				} else {
					double max1 = genonebyone > genonebytwo ? genonebytwo
							: genonebyone;
					double max2 = gentwobyone > gentwobytwo ? gentwobytwo
							: gentwobyone;
					double max = max1 > max2 ? max2 : max1;

					if ((max1 == max2
							&& Math.ceil(genonebyone) != Math.ceil(genonebytwo) && Math
							.ceil(gentwobyone) != Math.ceil(gentwobytwo))
							|| (Math.ceil(max1) == Math.ceil(max2)
									&& Math.ceil(max2) != genonebyone
									&& Math.ceil(max2) != genonebytwo
									&& Math.ceil(max1) != gentwobyone
									&& Math.ceil(max1) != gentwobytwo
									&& max1 != Math.ceil(max2) && Math
									.ceil(max1) != max2)
							&& Math.ceil(genonebyone) != Math.ceil(genonebytwo)
							&& Math.ceil(gentwobyone) != Math.ceil(gentwobytwo)) {
						result.put("param_name", childs[0]);
						result.put("param_name1", childs[1]);
						String n = Double.toString(Math.pow(10,
								(Math.ceil(max1) - 1)));
						result.put("n", n);
						result.put("function", "μ(p+q)/(8pq)/n");
						// result.put("function", "μ(p+q)/(8pq)");
					} else if ((max1 == max2
							&& Math.ceil(genonebyone) != Math.ceil(genonebytwo) && Math
								.ceil(gentwobyone) == Math.ceil(gentwobytwo))) {
						result.put("param_name", childs[0]);
						result.put("param_name1", childs[1]);
						String n = Double.toString(Math.pow(10,
								(Math.ceil(max1) - 1)));
						result.put("n", n);
						result.put("function", "μ(2p+q)/(8pq)/n");
					} else if ((max1 == max2
							&& Math.ceil(genonebyone) == Math.ceil(genonebytwo) && Math
								.ceil(gentwobyone) != Math.ceil(gentwobytwo))) {
						result.put("param_name", childs[0]);
						result.put("param_name1", childs[1]);
						String n = Double.toString(Math.pow(10,
								(Math.ceil(max1) - 1)));
						result.put("n", n);
						result.put("function", "μ(p+2q)/(8pq)/n");
					} else if ((max == max1) && (max == genonebyone)) {
						if ((Math.ceil(max) == genonebytwo
								&& gentwobyone == Math.ceil(max) && gentwobytwo > Math
								.ceil(max))
								|| (Math.ceil(max) == genonebytwo
										&& gentwobyone > Math.ceil(max) && gentwobytwo == Math
										.ceil(max))) {
							// 一可能+一可能 一步突变
							result.put("param_name", childs[0]);
							result.put("param_name1", childs[1]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ(p+q)/(8pq)/n");
						} else if ((gentwobyone == Math.ceil(max)
								&& gentwobyone > genonebyone
								&& genonebytwo > genonebyone && gentwobytwo > genonebyone)
								|| (gentwobytwo == Math.ceil(max)
										&& gentwobytwo > genonebyone
										&& gentwobyone > genonebyone && genonebytwo > genonebyone)
								|| (Math.ceil(max) == gentwobyone
										&& genonebytwo != gentwobyone && gentwobyone != gentwobyone)
								|| (Math.ceil(max) == gentwobytwo
										&& genonebytwo != gentwobytwo && gentwobyone != gentwobytwo)) {
							result.put("param_name", childs[1]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/8p*");
						} else if ((gentwobyone > Math.ceil(max)
								&& Math.ceil(max) != Math.ceil(genonebytwo)
								&& Math.ceil(max) != Math.ceil(gentwobyone)
								&& Math.ceil(max) != Math.ceil(gentwobytwo)
								&& gentwobyone > genonebyone
								&& genonebytwo > genonebyone && gentwobytwo > genonebyone)
								|| (gentwobytwo > Math.ceil(max)
										&& Math.ceil(max) != Math
												.ceil(genonebytwo)
										&& Math.ceil(max) != Math
												.ceil(gentwobyone)
										&& Math.ceil(max) != Math
												.ceil(gentwobytwo)
										&& gentwobytwo > genonebyone
										&& gentwobyone > genonebyone && genonebytwo > genonebyone)
								|| (gentwobytwo > Math.ceil(max)
										&& Math.ceil(max) != Math
												.ceil(genonebytwo)
										&& Math.ceil(max) != Math
												.ceil(gentwobyone)
										&& Math.ceil(max) != Math
												.ceil(gentwobytwo)
										&& genonebytwo > Math.ceil(max) && gentwobyone > Math
										.ceil(max))
								|| (Math.ceil(max) == genonebytwo
										&& gentwobyone != genonebytwo && gentwobytwo != genonebytwo)) {
							result.put("param_name", childs[0]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/8p*");
						} else if ((Math.ceil(max) == Math.ceil(genonebytwo)
								&& Math.ceil(max) != genonebytwo
								&& gentwobyone > Math.ceil(max) && gentwobytwo > Math
								.ceil(max))) {
							// 两种可能，三步突变
							result.put("param_name", childs[0]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/4p*");
						} else {
							if (Math.ceil(genonebyone) == Math
									.ceil(genonebytwo)
									&& Math.ceil(genonebyone) == Math
											.ceil(gentwobyone)
									&& Math.ceil(gentwobyone) == Math
											.ceil(gentwobytwo)) {
								// 两种可能 +两种可能 一步
								result.put("param_name", childs[0]);
								result.put("param_name1", childs[1]);
								result.put("function", "μ(p+q)/(4pq)");
							} else {
								// 一可能+两可能 两可能+一可能 一步突变
								result.put("param_name", childs[0]);
								result.put("param_name1", childs[1]);
								String n = Double.toString(Math.pow(10,
										(Math.ceil(max1) - 1)));
								result.put("n", n);
								result.put("function", "μ(p+2q)/(8pq)/n");
							}
						}
					} else if ((max == max1) && (max == genonebytwo)) {
						if ((Math.ceil(max) == genonebyone
								&& gentwobyone == Math.ceil(max) && gentwobytwo > Math
								.ceil(max))
								|| (Math.ceil(max) == genonebyone
										&& gentwobyone > Math.ceil(max) && gentwobytwo == Math
										.ceil(max))) {
							// 一可能+一可能 一步突变
							result.put("param_name", childs[0]);
							result.put("param_name1", childs[1]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ(p+q)/(8pq)/n");
						} else if ((gentwobyone == Math.ceil(max)
								&& gentwobyone > genonebytwo
								&& genonebyone > genonebytwo && gentwobytwo > genonebytwo)
								|| (Double.valueOf(df.format(gentwobytwo)) == Math
										.ceil(max)
										&& gentwobytwo > genonebytwo
										&& gentwobyone > genonebytwo && genonebyone > genonebytwo)
								|| (Math.ceil(max) == genonebyone
										&& Math.ceil(max) > gentwobyone && Math
										.ceil(max) > gentwobytwo)
								|| (Math.ceil(max) == genonebyone
										&& gentwobyone != genonebyone && gentwobytwo != genonebyone)) {
							result.put("param_name", childs[1]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/8p*");
						} else if ((gentwobyone > Math.ceil(max)
								&& Math.ceil(genonebyone) != Math
										.ceil(genonebytwo)
								&& Math.ceil(max) != Math.ceil(gentwobyone)
								&& Math.ceil(max) != Math.ceil(gentwobytwo)
								&& gentwobyone > genonebytwo
								&& genonebyone > genonebytwo && gentwobytwo > genonebytwo)
								|| (gentwobytwo > Math.ceil(max)
										&& Math.ceil(genonebyone) != Math
												.ceil(genonebytwo)
										&& Math.ceil(max) != Math
												.ceil(gentwobyone)
										&& Math.ceil(max) != Math
												.ceil(gentwobytwo)
										&& gentwobytwo > genonebytwo
										&& gentwobyone > genonebytwo && genonebyone > genonebytwo)
								|| (genonebyone > Math.ceil(max)
										&& Math.ceil(genonebyone) != Math
												.ceil(genonebytwo)
										&& Math.ceil(max) != Math
												.ceil(gentwobyone)
										&& Math.ceil(max) != Math
												.ceil(gentwobytwo)
										&& gentwobytwo > Math.ceil(max) && gentwobyone > Math
										.ceil(max))
								|| genonebyone > Math.ceil(max)
								&& gentwobyone > Math.ceil(max)
								&& gentwobytwo > Math.ceil(max)) {
							result.put("param_name", childs[0]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/8p*");
						} else if ((Math.ceil(max) == Math.ceil(genonebyone)
								&& genonebytwo == genonebyone
								&& gentwobyone > Math.ceil(max) && gentwobytwo > Math
								.ceil(max))) {
							// 两种可能，三步突变
							result.put("param_name", childs[0]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/4p*");
						} else {
							if (Math.ceil(genonebyone) == Math
									.ceil(genonebytwo)
									&& Math.ceil(genonebyone) == Math
											.ceil(gentwobyone)
									&& Math.ceil(gentwobyone) == Math
											.ceil(gentwobytwo)) {
								// 两种可能 +两种可能 一步
								result.put("param_name", childs[0]);
								result.put("param_name1", childs[1]);
								result.put("function", "μ(p+q)/(4pq)");
							} else {
								// 一可能+两可能 两可能+一可能 一步突变
								result.put("param_name", childs[0]);
								result.put("param_name1", childs[1]);
								String n = Double.toString(Math.pow(10,
										(Math.ceil(max1) - 1)));
								result.put("n", n);
								result.put("function", "μ(p+2q)/(8pq)/n");
							}
						}
					} else if ((max == max2) && (max == gentwobyone)) {
						if ((Math.ceil(max) == gentwobytwo
								&& genonebyone == Math.ceil(max) && genonebytwo > Math
								.ceil(max))
								|| (Math.ceil(max) == gentwobytwo
										&& genonebyone > Math.ceil(max) && Math
										.ceil(max) == genonebytwo)) {
							// 一可能+一可能 一步突变
							result.put("param_name", childs[0]);
							result.put("param_name1", childs[1]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ(p+q)/(8pq)/n");
							// result.put("function", "μ(p+q)/(8pq)");
						} else if ((genonebyone == Math.ceil(max)
								&& genonebyone > gentwobyone
								&& genonebytwo > gentwobyone && gentwobytwo > gentwobyone)
								|| (genonebytwo == Math.ceil(max)
										&& genonebytwo > gentwobyone
										&& gentwobytwo > gentwobyone && genonebyone > gentwobyone)) {
							result.put("param_name", childs[0]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/8p*");
						} else if ((genonebyone > Math.ceil(max)
								&& genonebyone > gentwobyone
								&& genonebytwo > gentwobyone
								&& gentwobytwo > gentwobyone && Math.ceil(max) != Math
								.ceil(gentwobytwo))
								|| (genonebytwo > Math.ceil(max)
										&& genonebytwo > gentwobyone
										&& gentwobytwo > gentwobyone
										&& genonebyone > gentwobyone && Math
										.ceil(max) != Math.ceil(gentwobytwo))
								|| (genonebyone > Math.ceil(max)
										&& genonebytwo > Math.ceil(max) && gentwobytwo > Math
										.ceil(max))) {
							result.put("param_name", childs[1]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/8p*");
						} else if ((Math.ceil(gentwobyone) == Math
								.ceil(gentwobytwo)
								&& genonebyone > Math.ceil(gentwobyone) && genonebytwo > Math
								.ceil(gentwobytwo))) {
							// 两种可能，三步突变
							result.put("param_name", childs[0]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/4p*");
						} else {
							if (Math.ceil(genonebyone) == Math
									.ceil(genonebytwo)
									&& Math.ceil(genonebyone) == Math
											.ceil(gentwobyone)
									&& Math.ceil(gentwobyone) == Math
											.ceil(gentwobytwo)) {
								// 两种可能 +两种可能 一步
								result.put("param_name", childs[0]);
								result.put("param_name1", childs[1]);
								result.put("function", "μ(p+q)/(4pq)");
							} else {
								// 一可能+两可能 两可能+一可能 一步突变
								result.put("param_name", childs[0]);
								result.put("param_name1", childs[1]);
								String n = Double.toString(Math.pow(10,
										(Math.ceil(max1) - 1)));
								result.put("n", n);
								result.put("function", "μ(2p+q)/(8pq)/n");
							}
						}
					} else if ((max == max2) && (max == gentwobytwo)) {
						if ((Math.ceil(max) == gentwobyone
								&& genonebyone == Math.ceil(max) && genonebytwo > Math
								.ceil(max))
								|| (Math.ceil(max) == gentwobyone
										&& genonebyone > Math.ceil(max) && Math
										.ceil(max) == genonebytwo)) {
							result.put("param_name", childs[0]);
							result.put("param_name1", childs[1]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ(p+q)/(8pq)/n");
							// result.put("function", "μ(p+q)/(8pq)");
						} else if ((genonebyone == Math.ceil(max)
								&& genonebyone > gentwobytwo
								&& genonebytwo > gentwobytwo && gentwobyone > gentwobytwo)
								|| (genonebytwo == Math.ceil(max)
										&& genonebytwo > gentwobytwo
										&& genonebyone > gentwobytwo && gentwobyone > gentwobytwo)) {
							result.put("param_name", childs[0]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/8p*");
						} else if ((gentwobyone > Math.ceil(max)
								&& gentwobyone > gentwobytwo
								&& genonebytwo > gentwobytwo && genonebyone > gentwobytwo)
								|| (genonebyone > Math.ceil(max)
										&& genonebytwo > Math.ceil(max) && gentwobyone > Math
										.ceil(max))) {
							result.put("param_name", childs[1]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/8p*");
						} else if ((Math.ceil(gentwobyone) == Math
								.ceil(gentwobytwo)
								&& genonebyone > Math.ceil(gentwobyone) && genonebytwo > Math
								.ceil(gentwobytwo))) {
							// 两种可能，三步突变
							result.put("param_name", childs[0]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/4p*");
						} else {
							if (Math.ceil(genonebyone) == Math
									.ceil(genonebytwo)
									&& Math.ceil(genonebyone) == Math
											.ceil(gentwobyone)
									&& Math.ceil(gentwobyone) == Math
											.ceil(gentwobytwo)) {
								// 两种可能 +两种可能 一步
								result.put("param_name", childs[0]);
								result.put("param_name1", childs[1]);
								result.put("function", "μ(p+q)/(4pq)");
							} else {
								// 一可能+两可能 两可能+一可能 一步突变
								result.put("param_name", childs[0]);
								result.put("param_name1", childs[1]);
								String n = Double.toString(Math.pow(10,
										(Math.ceil(max1) - 1)));
								result.put("n", n);
								result.put("function", "μ(2p+q)/(8pq)/n");
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 三个人否定公式
	 * 
	 * @param mother
	 * @param father
	 * @param child
	 * @return
	 */
	private Map<String, String> findFunctionByTb(String mother, String father,
			String child) {
		Map<String, String> result = new HashMap<String, String>();

		String[] mothers = mother.split(",");
		String[] fathers = father.split(",");
		String[] childs = child.split(",");

		double genonebyone = 0.0;
		double genonebytwo = 0.0;
		double gentwobyone = 0.0;
		double gentwobytwo = 0.0;
		if (childs.length == 1) {
			if (fathers.length == 1) {
				genonebyone = Math.abs(Double.parseDouble(childs[0])
						- Double.parseDouble(fathers[0]));
				genonebytwo = genonebyone;
				gentwobyone = genonebyone;
				gentwobytwo = genonebyone;
			} else {
				genonebyone = Math.abs(Double.parseDouble(childs[0])
						- Double.parseDouble(fathers[0]));
				genonebytwo = Math.abs(Double.parseDouble(childs[0])
						- Double.parseDouble(fathers[1]));
				gentwobyone = genonebyone;
				gentwobytwo = genonebytwo;
			}
		} else {
			if (fathers.length == 1) {
				genonebyone = Math.abs(Double.parseDouble(childs[0])
						- Double.parseDouble(fathers[0]));
				genonebytwo = genonebyone;
				gentwobyone = Math.abs(Double.parseDouble(childs[1])
						- Double.parseDouble(fathers[0]));
				gentwobytwo = gentwobyone;
			} else {
				genonebyone = Math.abs(Double.parseDouble(childs[0])
						- Double.parseDouble(fathers[0]));
				genonebytwo = Math.abs(Double.parseDouble(childs[0])
						- Double.parseDouble(fathers[1]));
				gentwobyone = Math.abs(Double.parseDouble(childs[1])
						- Double.parseDouble(fathers[0]));
				gentwobytwo = Math.abs(Double.parseDouble(childs[1])
						- Double.parseDouble(fathers[1]));
			}
		}

		List<String> tempListm = Arrays.asList(mothers);
		List<String> tempListf = Arrays.asList(fathers);
		double mgentwobyone = 0.0;
		double mgentwobytwo = 0.0;
		// 母亲和孩子点位完全相同
		if (mother.equals(child)) {
			// 母亲该点位数据只有一个值 第一个跳板 1/2P
			if (mothers.length == 1) {
				result.put("param_name", childs[0]);
				if ((gentwobyone == 1 && gentwobytwo > 1)
						|| (gentwobyone > 1 && gentwobytwo == 1)) {
					// 一可能性 一步突变 1/2P*μ/2
					result.put("function", "μ/(4p)");
				} else if ((gentwobyone == 2 && gentwobytwo > 2)
						|| (gentwobyone > 2 && gentwobytwo == 2)) {
					// 一可能性 二步突变 1/2P*μ/20
					result.put("function", "μ/(40p)");
				} else if ((gentwobyone == 3 && gentwobytwo > 3)
						|| (gentwobyone > 3 && gentwobytwo == 3)) {
					// 一可能性 二步突变 1/2P*μ/200
					result.put("function", "μ/(400p)");
				} else if ((gentwobyone == 4 && gentwobytwo > 4)
						|| (gentwobyone > 4 && gentwobytwo == 4)) {
					// 一可能性 二步突变 1/2P*μ/2000
					result.put("function", "μ/(4000p)");
				} else {
					// 是否可以加一个小数点的 μ/4P*n
					double max1 = genonebyone > genonebytwo ? genonebytwo
							: genonebyone;
					double max2 = gentwobyone > gentwobytwo ? gentwobytwo
							: gentwobyone;
					double max = max1 > max2 ? max2 : max1;
					if (max == max1)
						result.put("param_name", childs[0]);
					else
						result.put("param_name", childs[1]);
					String n = Double.toString(Math.pow(10,
							(Math.ceil(max) - 1)));
					result.put("n", n);
					result.put("function", "μ/4p*");
				}
			} else {
				// 母亲该点位数据有两个值 当母=子=PQ 第二个 跳板1/[2(P+Q)] 然后在讨论 孩子 和 父亲之间的可能性
				result.put("param_name", childs[0]);
				result.put("param_name1", childs[1]);
				if ((genonebyone == 1 && genonebytwo > 1 && gentwobyone > 1 && gentwobytwo > 1)
						|| (genonebyone > 1 && genonebytwo == 1
								&& gentwobyone > 1 && gentwobytwo > 1)
						|| (genonebyone > 1 && genonebytwo > 1
								&& gentwobyone == 1 && gentwobytwo > 1)
						|| (genonebyone > 1 && genonebytwo > 1
								&& gentwobyone > 1 && gentwobytwo == 1)) {
					// 一可能 一步突变
					result.put("function", "μ/[4(p+q)]");
				} else if ((genonebyone == 2 && genonebytwo > 2
						&& gentwobyone > 2 && gentwobytwo > 2)
						|| (genonebyone > 2 && genonebytwo == 2
								&& gentwobyone > 2 && gentwobytwo > 2)
						|| (genonebyone > 2 && genonebytwo > 2
								&& gentwobyone == 2 && gentwobytwo > 2)
						|| (genonebyone > 2 && genonebytwo > 2
								&& gentwobyone > 2 && gentwobytwo == 2)) {
					// 一可能 二步突变
					result.put("function", "μ/[40(p+q)]");
				} else if ((genonebyone == 3 && genonebytwo > 3
						&& gentwobyone > 3 && gentwobytwo > 3)
						|| (genonebyone > 3 && genonebytwo == 3
								&& gentwobyone > 3 && gentwobytwo > 3)
						|| (genonebyone > 3 && genonebytwo > 3
								&& gentwobyone == 3 && gentwobytwo > 3)
						|| (genonebyone > 3 && genonebytwo > 3
								&& gentwobyone > 3 && gentwobytwo == 3)) {
					// 一可能 三步突变
					result.put("function", "μ/[400(p+q)]");
				} else if ((genonebyone == 4 && genonebytwo > 4
						&& gentwobyone > 4 && gentwobytwo > 4)
						|| (genonebyone > 4 && genonebytwo == 4
								&& gentwobyone > 4 && gentwobytwo > 4)
						|| (genonebyone > 4 && genonebytwo > 4
								&& gentwobyone == 4 && gentwobytwo > 4)
						|| (genonebyone > 4 && genonebytwo > 4
								&& gentwobyone > 4 && gentwobytwo == 4)) {
					// 一可能 四步突变
					result.put("function", "μ/[4000(p+q)]");
				} else if ((genonebyone == 1 && genonebytwo == 1
						&& gentwobyone > 1 && gentwobytwo > 1)
						|| (genonebyone > 1 && genonebytwo > 1
								&& gentwobyone == 1 && gentwobytwo == 1)
						|| (genonebyone == 1 && genonebytwo > 1
								&& gentwobyone == 1 && gentwobytwo > 1)
						|| (genonebyone == 1 && genonebytwo > 1
								&& gentwobyone > 1 && gentwobytwo == 1)
						|| (genonebyone > 1 && genonebytwo == 1
								&& gentwobyone == 1 && gentwobytwo > 1)
						|| (genonebyone > 1 && genonebytwo == 1
								&& gentwobyone > 1 && gentwobytwo == 1)) {
					// 两可能 一步突变
					result.put("function", "μ/[2(p+q)]");
				} else if ((genonebyone == 2 && genonebytwo == 2
						&& gentwobyone > 2 && gentwobytwo > 2)
						|| (genonebyone > 2 && genonebytwo > 2
								&& gentwobyone == 2 && gentwobytwo == 2)
						|| (genonebyone == 2 && genonebytwo > 2
								&& gentwobyone == 2 && gentwobytwo > 2)
						|| (genonebyone == 2 && genonebytwo > 2
								&& gentwobyone > 2 && gentwobytwo == 2)
						|| (genonebyone > 2 && genonebytwo == 2
								&& gentwobyone == 2 && gentwobytwo > 2)
						|| (genonebyone > 2 && genonebytwo == 2
								&& gentwobyone > 2 && gentwobytwo == 2)) {
					// 两可能 二步突变
					result.put("function", "μ/[20(p+q)]");
				} else if ((genonebyone == 3 && genonebytwo == 3
						&& gentwobyone > 3 && gentwobytwo > 3)
						|| (genonebyone > 3 && genonebytwo > 3
								&& gentwobyone == 3 && gentwobytwo == 3)
						|| (genonebyone == 3 && genonebytwo > 3
								&& gentwobyone == 3 && gentwobytwo > 3)
						|| (genonebyone == 3 && genonebytwo > 3
								&& gentwobyone > 3 && gentwobytwo == 3)
						|| (genonebyone > 3 && genonebytwo == 3
								&& gentwobyone == 3 && gentwobytwo > 3)
						|| (genonebyone > 3 && genonebytwo == 3
								&& gentwobyone > 3 && gentwobytwo == 3)) {
					// 两可能 三步突变
					result.put("function", "μ/[200(p+q)]");
				} else if ((genonebyone == 4 && genonebytwo == 4
						&& gentwobyone > 4 && gentwobytwo > 4)
						|| (genonebyone > 4 && genonebytwo > 4
								&& gentwobyone == 4 && gentwobytwo == 4)
						|| (genonebyone == 4 && genonebytwo > 4
								&& gentwobyone == 4 && gentwobytwo > 4)
						|| (genonebyone == 4 && genonebytwo > 4
								&& gentwobyone > 4 && gentwobytwo == 4)
						|| (genonebyone > 4 && genonebytwo == 4
								&& gentwobyone == 4 && gentwobytwo > 4)
						|| (genonebyone > 4 && genonebytwo == 4
								&& gentwobyone > 4 && gentwobytwo == 4)) {
					// 两可能 四步突变
					result.put("function", "μ/[2000(p+q)]");
				} else if ((genonebyone == 1 && genonebytwo == 1
						&& gentwobyone == 1 && gentwobytwo > 1)
						|| (genonebyone == 1 && genonebytwo == 1
								&& gentwobyone > 1 && gentwobytwo == 1)
						|| (genonebyone == 1 && genonebytwo > 1
								&& gentwobyone == 1 && gentwobytwo == 1)
						|| (genonebyone > 1 && genonebytwo == 1
								&& gentwobyone == 1 && gentwobytwo == 1)) {
					// 三可能 一步突变
					result.put("function", "(3μ)/[4(p+q)]");
				} else if ((genonebyone == 2 && genonebytwo == 2
						&& gentwobyone == 2 && gentwobytwo > 2)
						|| (genonebyone == 2 && genonebytwo == 2
								&& gentwobyone > 2 && gentwobytwo == 2)
						|| (genonebyone == 2 && genonebytwo > 2
								&& gentwobyone == 2 && gentwobytwo == 2)
						|| (genonebyone > 2 && genonebytwo == 2
								&& gentwobyone == 2 && gentwobytwo == 2)) {
					// 三可能 二步突变
					result.put("function", "(3μ)/[40(p+q)]");
				} else if ((genonebyone == 3 && genonebytwo == 3
						&& gentwobyone == 3 && gentwobytwo > 3)
						|| (genonebyone == 3 && genonebytwo == 3
								&& gentwobyone > 3 && gentwobytwo == 3)
						|| (genonebyone == 3 && genonebytwo > 3
								&& gentwobyone == 3 && gentwobytwo == 3)
						|| (genonebyone > 3 && genonebytwo == 3
								&& gentwobyone == 3 && gentwobytwo == 3)) {
					// 三可能 三步突变
					result.put("function", "(3μ)/[400(p+q)]");
				} else if ((genonebyone == 4 && genonebytwo == 4
						&& gentwobyone == 4 && gentwobytwo > 4)
						|| (genonebyone == 4 && genonebytwo == 42
								&& gentwobyone > 4 && gentwobytwo == 4)
						|| (genonebyone == 4 && genonebytwo > 4
								&& gentwobyone == 4 && gentwobytwo == 4)
						|| (genonebyone > 4 && genonebytwo == 4
								&& gentwobyone == 4 && gentwobytwo == 4)) {
					// 三可能 四步突变
					result.put("function", "(3μ)/[4000(p+q)]");
				} else {
					// 是否可以加一个小数点的 μ/4(P+Q)*n
					double max1 = genonebyone > genonebytwo ? genonebytwo
							: genonebyone;
					double max2 = gentwobyone > gentwobytwo ? gentwobytwo
							: gentwobyone;
					double max = max1 > max2 ? max2 : max1;
					String n = Double.toString(Math.pow(10,
							(Math.ceil(max) - 1)));
					result.put("n", n);
					result.put("function", "μ/4(p+q)*");
				}
			}
		} else {
			// 母亲和孩子不同， 第一个跳板 1/2P
			// 母亲该点位数据只有一个值
			if (mothers.length == 1 && !tempListf.contains(childs[0])) {
				// 母亲有一个值且父亲没有孩子点位
				if (childs.length != 1 && !tempListf.contains(childs[1])) {
					if (childs[0].equals(mothers[0])) {
						result.put("param_name", childs[1]);
						// 如果孩子第一个点在母亲里面 就比第二个点和父亲
						if (gentwobyone == 1 && gentwobytwo == 1) {
							// 两种可能 ,一步突变
							result.put("function", "μ/(2p)");
						} else if (gentwobyone == 2 && gentwobytwo == 2) {
							// 两种可能 ,二步突变
							result.put("function", "μ/(20p)");
						} else if (gentwobyone == 3 && gentwobytwo == 3) {
							// 两种可能 ,三步突变
							result.put("function", "μ/(200p)");
						} else if (gentwobyone == 4 && gentwobytwo == 4) {
							// 一种可能 ,四步突变
							result.put("function", "μ/(2000p)");
						} else if ((gentwobyone == 1 && gentwobytwo > 1)
								|| (gentwobyone > 1 && gentwobytwo == 1)) {
							// 一种可能 ,一步突变
							result.put("function", "μ/(4p)");
						} else if ((gentwobyone == 2 && gentwobytwo > 2)
								|| (gentwobyone > 2 && gentwobytwo == 2)) {
							// 一种可能 ,二步突变
							result.put("function", "μ/(40p)");
						} else if ((gentwobyone == 3 && gentwobytwo > 3)
								|| (gentwobyone > 3 && gentwobytwo == 3)) {
							// 一种可能 ,三步突变
							result.put("function", "μ/(400p)");
						} else if ((gentwobyone == 4 && gentwobytwo > 4)
								|| (gentwobyone > 4 && gentwobytwo == 4)) {
							// 一种可能 ,四步突变
							result.put("function", "μ/(4000p)");
						} else {
							// 是否可以加一个小数点的 母亲一个值父亲两个值
							double max = 0.0;
							result.put("param_name", childs[1]);
							max = gentwobyone > gentwobytwo ? gentwobytwo
									: gentwobyone;
							if (gentwobyone == gentwobytwo) {
								String n = Double.toString(Math.pow(10,
										(Math.ceil(max) - 1)));
								result.put("n", n);
								result.put("function", "μ/2p*");
							} else {
								String n = Double.toString(Math.pow(10,
										(Math.ceil(max) - 1)));
								result.put("n", n);
								result.put("function", "μ/4p*");
							}

						}
					} else if (childs[1].equals(mothers[0])) {
						result.put("param_name", childs[0]);
						// 如果第二个点在母亲里面 那比第一个点和 父亲
						if (genonebyone == 1 && genonebytwo == 1) {
							// 两种可能 ,一步突变
							result.put("function", "μ/(2p)");
						} else if (genonebyone == 2 && genonebytwo == 2) {
							// 两种可能 ,二步突变
							result.put("function", "μ/(20p)");
						} else if (genonebyone == 3 && genonebytwo == 3) {
							// 两种可能 ,三步突变
							result.put("function", "μ/(200p)");
						} else if (genonebyone == 4 && genonebytwo == 4) {
							// 一种可能 ,四步突变
							result.put("function", "μ/(2000p)");
						}
						if ((genonebyone == 1 && genonebytwo > 1)
								|| (genonebyone > 1 && genonebytwo == 1)) {
							// 一种可能 ,一步突变
							result.put("function", "μ/(4p)");
						} else if ((genonebyone == 2 && genonebytwo > 2)
								|| (genonebyone > 2 && genonebytwo == 2)) {
							// 一种可能 ,二步突变
							result.put("function", "μ/(40p)");
						} else if ((genonebyone == 3 && genonebytwo > 3)
								|| (genonebyone > 3 && genonebytwo == 3)) {
							// 一种可能 ,三步突变
							result.put("function", "μ/(400p)");
						} else if ((genonebyone == 4 && genonebytwo > 4)
								|| (genonebyone > 4 && genonebytwo == 4)) {
							// 一种可能 ,四步突变
							result.put("function", "μ/(4000p)");
						} else {
							// 是否可以加一个小数点的 母亲一个值父亲两个值
							double max = 0.0;
							result.put("param_name", childs[0]);
							max = genonebyone > genonebytwo ? genonebytwo
									: genonebyone;
							String n = Double.toString(Math.pow(10, (max - 1)));
							result.put("n", n);
							result.put("function", "μ/4p*");
						}
					} else {
						// 是否可以加一个小数点的 母亲一个值父亲两个值
						double max = 0.0;
						result.put("param_name", childs[0]);
						max = genonebyone > genonebytwo ? genonebytwo
								: genonebyone;
						if (genonebyone == genonebytwo) {
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/2p*");
						} else {
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/4p*");
						}
					}
				} else if (fathers.length == 1) {
					result.put("param_name", childs[0]);
					if ((genonebyone == 1) || (gentwobyone == 1)) {
						// 两可能 一步突变
						result.put("function", "μ/(2p)");
					} else if ((genonebyone == 2) || (gentwobyone == 2)) {
						// 两可能 二步突变
						result.put("function", "μ/(20p)");
					} else if ((genonebyone == 3) || (gentwobyone == 3)) {
						// 两可能 三步突变
						result.put("function", "μ/(200p)");
					} else if ((genonebyone == 4) || (gentwobyone == 4)) {
						// 两可能 四步突变
						result.put("function", "μ/(2000p)");
					} else {
						// 是否可以加一个小数点的
						double max = genonebyone > gentwobyone ? gentwobyone
								: genonebyone;
						if (max == genonebyone)
							result.put("param_name", childs[0]);
						else
							result.put("param_name", childs[1]);
						String n = Double.toString(Math.pow(10,
								(Math.ceil(max) - 1)));
						result.put("n", n);
						result.put("function", "μ/2p*");
					}
				} else {
					if ((genonebyone == 1 && genonebytwo > 1)
							|| (gentwobyone == 1 && gentwobytwo > 1)
							|| (genonebyone > 1 && genonebytwo == 1)
							|| (gentwobyone > 1 && gentwobytwo == 1)) {
						// 一种可能 ,一步突变
						if (genonebyone > 1 || genonebytwo > 1)
							result.put("param_name", childs[0]);
						else
							result.put("param_name", childs[1]);
						result.put("function", "μ/(4p)");
					} else if ((genonebyone == 2 && genonebytwo > 2)
							|| (gentwobyone == 2 && gentwobytwo > 2)
							|| (genonebyone > 2 && genonebytwo == 2)
							|| (gentwobyone > 2 && gentwobytwo == 2)) {
						// 一种可能 ,二步突变
						if (genonebyone > 2 || genonebytwo > 2)
							result.put("param_name", childs[0]);
						else
							result.put("param_name", childs[1]);
						result.put("function", "μ/(40p)");
					} else if ((genonebyone == 3 && genonebytwo > 3)
							|| (gentwobyone == 3 && gentwobytwo > 3)
							|| (genonebyone > 3 && genonebytwo == 3)
							|| (gentwobyone > 3 && gentwobytwo == 3)) {
						if (genonebyone > 3 || genonebytwo > 3)
							result.put("param_name", childs[0]);
						else
							result.put("param_name", childs[1]);
						// 一种可能 ,三步突变
						result.put("function", "μ/(400p)");
					} else if ((genonebyone == 4 && genonebytwo > 4)
							|| (gentwobyone == 4 && gentwobytwo > 4)
							|| (genonebyone > 4 && genonebytwo == 4)
							|| (gentwobyone > 4 && gentwobytwo == 4)) {
						if (genonebyone > 4 || genonebytwo > 4)
							result.put("param_name", childs[0]);
						else
							result.put("param_name", childs[1]);
						// 一种可能 ,四步突变
						result.put("function", "μ/(4000p)");
					} else {
						// 是否可以加一个小数点的 母亲一个值父亲两个值
						double max2 = gentwobyone > gentwobytwo ? gentwobytwo
								: gentwobyone;
						double max1 = genonebyone > genonebytwo ? genonebytwo
								: genonebyone;
						double max = max1 > max2 ? max2 : max1;
						if (max == max1) {
							result.put("param_name", childs[0]);
						} else {
							result.put("param_name", childs[1]);
						}
						String n = Double.toString(Math.pow(10,
								(Math.ceil(max) - 1)));
						result.put("n", n);
						result.put("function", "μ/4p*");
					}
				}
			} else if (childs.length == 1) {
				result.put("param_name", childs[0]);
				if (tempListf.contains(childs[0])) {
					// 孩子点来自于父亲 比母亲
					if (mothers.length == 1) {
						mgentwobyone = Math.abs(Double.parseDouble(childs[0])
								- Double.parseDouble(mothers[0]));
						mgentwobytwo = mgentwobyone;
					} else {
						mgentwobyone = Math.abs(Double.parseDouble(childs[0])
								- Double.parseDouble(mothers[0]));
						mgentwobytwo = Math.abs(Double.parseDouble(childs[0])
								- Double.parseDouble(mothers[1]));
					}

					if (mgentwobyone == 1 && mgentwobytwo == 1) {
						// 两可能 一步突变
						result.put("function", "μm/(2p)");
					} else if (mgentwobyone == 2 && mgentwobytwo == 2) {
						// 两可能 二步突变
						result.put("function", "μm/(20p)");
					} else if (mgentwobyone == 3 && mgentwobytwo == 3) {
						// 两可能 三步突变
						result.put("function", "μm/(200p)");
					} else if (mgentwobyone == 4 && mgentwobytwo == 4) {
						// 两可能 四步突变
						result.put("function", "μm/(2000p)");
					} else if ((mgentwobyone == 1 && mgentwobytwo > 1)
							|| (mgentwobyone > 1 && mgentwobytwo == 1)) {
						// 一可能 一步突变
						result.put("function", "μm/(4p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo > 2)
							|| (mgentwobyone > 2 && mgentwobytwo == 2)) {
						// 一可能 一步突变
						result.put("function", "μm/(40p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo > 3)
							|| (mgentwobyone > 3 && mgentwobytwo == 3)) {
						// 一可能 一步突变
						result.put("function", "μm/(400p)");
					} else if ((mgentwobyone == 4 && mgentwobytwo > 4)
							|| (mgentwobyone > 4 && mgentwobytwo == 4)) {
						// 一可能 一步突变
						result.put("function", "μm/(4000p)");
					} else {
						// 是否可以加一个小数点的
						double max = mgentwobyone > mgentwobytwo ? mgentwobyone
								: mgentwobytwo;
						String n = Double.toString(Math.pow(10,
								(Math.ceil(max) - 1)));
						result.put("n", n);
						result.put("function", "μm/4p*");
					}
				} else {
					if (genonebyone == 1 && genonebytwo == 1) {
						// 两可能 一步突变
						result.put("function", "μ/(2p)");
					} else if (genonebyone == 2 && genonebytwo == 2) {
						// 两可能 二步突变
						result.put("function", "μ/(20p)");
					} else if (genonebyone == 3 && genonebytwo == 3) {
						// 两可能 三步突变
						result.put("function", "μ/(200p)");
					} else if (genonebyone == 4 && genonebytwo == 4) {
						// 两可能 四步突变
						result.put("function", "μ/(2000p)");
					} else if ((genonebyone == 1 && genonebytwo > 1)
							|| (genonebyone > 1 && genonebytwo == 1)) {
						// 一可能 一步突变
						result.put("function", "μ/(4p)");
					} else if ((genonebyone == 2 && genonebytwo > 2)
							|| (genonebyone > 2 && genonebytwo == 2)) {
						// 一可能 一步突变
						result.put("function", "μ/(40p)");
					} else if ((genonebyone == 3 && genonebytwo > 3)
							|| (genonebyone > 3 && genonebytwo == 3)) {
						// 一可能 一步突变
						result.put("function", "μ/(400p)");
					} else if ((genonebyone == 4 && genonebytwo > 4)
							|| (genonebyone > 4 && genonebytwo == 4)) {
						// 一可能 一步突变
						result.put("function", "μ/(4000p)");
					} else {
						// 是否可以加一个小数点的
						double max = genonebyone > genonebytwo ? genonebytwo
								: genonebyone;
						if (genonebyone == genonebytwo) {
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/2p*");
						} else {
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/4p*");
						}

					}
				}
			} else if (fathers.length == 1 && !tempListf.contains(childs[0])
					&& !tempListf.contains(childs[1])) {
				if (tempListm.contains(childs[0])) {
					result.put("param_name", childs[1]);
					// 如果孩子第一个在母亲里面 只要比第二个和父亲
					if (gentwobyone == 1) {
						// 两可能 一步突变
						result.put("function", "μ/(2p)");
					} else if (gentwobyone == 2) {
						// 两可能 二步突变
						result.put("function", "μ/(20p)");
					} else if (gentwobyone == 3) {
						// 两可能 三步突变
						result.put("function", "μ/(200p)");
					} else if (gentwobyone == 4) {
						// 两可能 四步突变
						result.put("function", "μ/(2000p)");
					} else {
						// 是否可以加一个小数点的
						double max = gentwobyone;
						String n = Double.toString(Math.pow(10, (max - 1)));
						result.put("n", n);
						result.put("function", "μ/2p*");
					}
				} else if (tempListm.contains(childs[1])) {
					result.put("param_name", childs[0]);
					if (genonebyone == 1) {
						// 两可能 一步突变
						result.put("function", "μ/(2p)");
					} else if (genonebyone == 2) {
						// 两可能 二步突变
						result.put("function", "μ/(20p)");
					} else if (genonebyone == 3) {
						// 两可能 三步突变
						result.put("function", "μ/(200p)");
					} else if (genonebyone == 4) {
						// 两可能 四步突变
						result.put("function", "μ/(2000p)");
					} else {
						// 是否可以加一个小数点的
						double max = genonebyone;
						String n = Double.toString(Math.pow(10,
								(Math.ceil(max) - 1)));
						result.put("n", n);
						result.put("function", "μ/2p*");
					}
				} else {
					if (genonebyone == 1 || gentwobyone == 1) {
						if (genonebyone == 1)
							result.put("param_name", childs[0]);
						else
							result.put("param_name", childs[1]);
						// 两可能 一步突变
						result.put("function", "μ/(2p)");
					} else if (genonebyone == 2 || gentwobyone == 2) {
						if (genonebyone == 2)
							result.put("param_name", childs[0]);
						else
							result.put("param_name", childs[1]);
						// 两可能 二步突变
						result.put("function", "μ/(20p)");
					} else if (genonebyone == 3 || gentwobyone == 3) {
						if (genonebyone == 3)
							result.put("param_name", childs[0]);
						else
							result.put("param_name", childs[1]);
						// 两可能 三步突变
						result.put("function", "μ/(200p)");
					} else if (genonebyone == 4 || gentwobyone == 4) {
						if (genonebyone == 4)
							result.put("param_name", childs[0]);
						else
							result.put("param_name", childs[1]);
						// 两可能 四步突变
						result.put("function", "μ/(2000p)");
					} else {
						// 是否可以加一个小数点的
						double max = gentwobyone > genonebyone ? gentwobytwo
								: genonebyone;
						if (max == genonebyone)
							result.put("param_name", childs[0]);
						else
							result.put("param_name", childs[1]);
						String n = Double.toString(Math.pow(10,
								(Math.ceil(max) - 1)));
						result.put("n", n);
						result.put("function", "μ/2p*");
					}
				}
			} else {
				// 讨论孩子和父亲有没有相同点
				if (tempListm.contains(childs[0])
						&& tempListf.contains(childs[0])) {
					// 第一个点相同点 只要比第二个点和母亲 和父亲
					result.put("param_name", childs[1]);
					if (mothers.length == 1) {
						mgentwobyone = Math.abs(Double.parseDouble(childs[1])
								- Double.parseDouble(mothers[0]));
						mgentwobytwo = mgentwobyone;
					} else {
						mgentwobyone = Math.abs(Double.parseDouble(childs[1])
								- Double.parseDouble(mothers[0]));
						mgentwobytwo = Math.abs(Double.parseDouble(childs[1])
								- Double.parseDouble(mothers[1]));
					}

					if ((mgentwobyone == 1 && mgentwobytwo == 1
							&& gentwobyone == 1 && gentwobytwo == 1)) {
						// 第一种情况 孩子第二个位点分别和父母亲的两个点相减都等于1 这个是两可能一步+ 两可能一步
						result.put("function", "(μf+μm)/(2p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo == 2
							&& gentwobyone == 2 && gentwobytwo == 2)) {
						// 第二种情况 孩子第二个位点分别和父母亲的两个点相减都等于2 这个是两可能二步+ 两可能二步
						result.put("function", "(μf+μm)/(20p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo == 3
							&& gentwobyone == 3 && gentwobytwo == 3)) {
						// 第三种情况 孩子第二个位点分别和父母亲的两个点相减都等于3 这个是两可能三步+ 两可能三步
						result.put("function", "(μf+μm)/(200p)");
					} else if ((mgentwobyone == 1 && mgentwobytwo > 1
							&& gentwobyone == 1 && gentwobytwo > 1)
							|| (mgentwobyone == 1 && mgentwobytwo > 1
									&& gentwobyone > 1 && gentwobytwo == 1)
							|| (mgentwobyone > 1 && mgentwobytwo == 1
									&& gentwobyone == 1 && gentwobytwo > 1)
							|| (mgentwobyone > 1 && mgentwobytwo == 1
									&& gentwobyone > 1 && gentwobytwo == 1)) {
						// 第四种情况 孩子第二个位点和母亲两个点位有一个是等于一 有一个大于一
						// 相同孩子和父亲相减有一个等于一有一个大于一 这个就是一可能一步+一可能一步
						result.put("function", "(μf+μm)/(4p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo > 2
							&& gentwobyone == 2 && gentwobytwo > 2)
							|| (mgentwobyone == 2 && mgentwobytwo > 2
									&& gentwobyone > 2 && gentwobytwo == 2)
							|| (mgentwobyone > 2 && mgentwobytwo == 2
									&& gentwobyone == 2 && gentwobytwo > 2)
							|| (mgentwobyone > 2 && mgentwobytwo == 2
									&& gentwobyone > 2 && gentwobytwo == 2)) {
						// 第五种情况 一可能二步+一可能二步
						result.put("function", "(μf+μm)/(40p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo > 3
							&& gentwobyone == 3 && gentwobytwo > 3)
							|| (mgentwobyone == 3 && mgentwobytwo > 3
									&& gentwobyone > 3 && gentwobytwo == 3)
							|| (mgentwobyone > 3 && mgentwobytwo == 3
									&& gentwobyone == 3 && gentwobytwo > 3)
							|| (mgentwobyone > 3 && mgentwobytwo == 3
									&& gentwobyone > 3 && gentwobytwo == 3)) {
						// 第六种情况 一可能三步+一可能三步
						result.put("function", "(μf+μm)/(400p)");
					} else if ((mgentwobyone == 1 && mgentwobytwo > 1
							&& gentwobyone == 2 && gentwobytwo > 2)
							|| (mgentwobyone == 1 && mgentwobytwo > 1
									&& gentwobyone > 2 && gentwobytwo == 2)
							|| (mgentwobyone > 1 && mgentwobytwo == 1
									&& gentwobyone == 2 && gentwobytwo > 2)
							|| (mgentwobyone > 1 && mgentwobytwo == 1
									&& gentwobyone > 2 && gentwobytwo == 2)) {
						// 第七种情况 孩子第二个位点和母亲两个点位有一个是等于一 有一个大于一
						// 孩子和父亲相减有一个等于二有一个大于二 这个就是一可能一步+0
						result.put("function", "μm/(4p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo > 2
							&& gentwobyone == 1 && gentwobytwo > 1)
							|| (mgentwobyone == 2 && mgentwobytwo > 2
									&& gentwobyone > 1 && gentwobytwo == 1)
							|| (mgentwobyone > 2 && mgentwobytwo == 2
									&& gentwobyone == 1 && gentwobytwo > 1)
							|| (mgentwobyone > 2 && mgentwobytwo == 2
									&& gentwobyone > 1 && gentwobytwo == 1)) {
						// 第7'种情况 0+一可能一步
						result.put("function", "μf/(4p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo > 2
							&& gentwobyone == 3 && gentwobytwo > 3)
							|| (mgentwobyone == 2 && mgentwobytwo > 2
									&& gentwobyone > 3 && gentwobytwo == 3)
							|| (mgentwobyone > 2 && mgentwobytwo == 2
									&& gentwobyone == 3 && gentwobytwo > 3)
							|| (mgentwobyone > 2 && mgentwobytwo == 2
									&& gentwobyone > 3 && gentwobytwo == 3)) {
						// 第八种情况 孩子第二个位点和母亲两个点位有一个是等于二 有一个大于二
						// 孩子和父亲相减有一个等于三有一个大于三 这个就是一可能二步+0
						result.put("function", "μm/(40p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo > 3
							&& gentwobyone == 2 && gentwobytwo > 2)
							|| (mgentwobyone == 3 && mgentwobytwo > 3
									&& gentwobyone > 2 && gentwobytwo == 2)
							|| (mgentwobyone > 3 && mgentwobytwo == 3
									&& gentwobyone == 2 && gentwobytwo > 2)
							|| (mgentwobyone > 3 && mgentwobytwo == 3
									&& gentwobyone > 2 && gentwobytwo == 2)) {
						// 第8'种情况 0+一可能二步
						result.put("function", "μf/(40p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo > 3
							&& gentwobyone == 4 && gentwobytwo > 4)
							|| (mgentwobyone == 3 && mgentwobytwo > 3
									&& gentwobyone > 4 && gentwobytwo == 4)
							|| (mgentwobyone > 3 && mgentwobytwo == 3
									&& gentwobyone == 4 && gentwobytwo > 4)
							|| (mgentwobyone > 3 && mgentwobytwo == 3
									&& gentwobyone > 4 && gentwobytwo == 4)) {
						// 第九种情况 孩子第二个位点和母亲两个点位有一个是等于一 有一个大于一
						// 孩子和父亲相减有一个等于二有一个大于二 这个就是一可能三步+0
						result.put("function", "μm/(400p)");
					} else if ((mgentwobyone == 4 && mgentwobytwo > 4
							&& gentwobyone == 3 && gentwobytwo > 3)
							|| (mgentwobyone == 4 && mgentwobytwo > 4
									&& gentwobyone > 3 && gentwobytwo == 3)
							|| (mgentwobyone > 4 && mgentwobytwo == 4
									&& gentwobyone == 3 && gentwobytwo > 3)
							|| (mgentwobyone > 4 && mgentwobytwo == 4
									&& gentwobyone > 3 && gentwobytwo == 3)) {
						// 第9'种情况 0+一可能三步
						result.put("function", "μf/(400p)");
					} else if ((mgentwobyone == 1 && mgentwobytwo > 1
							&& gentwobyone == 1 && gentwobytwo == 1)
							|| (mgentwobyone > 1 && mgentwobytwo == 1
									&& gentwobyone == 1 && gentwobytwo == 1)) {
						// 第十种情况 孩子第二个位点和母亲两个点位有一个是等于一 有一个大于一
						// 孩子和父亲相减都等于一 一可能一步+二可能一步
						result.put("function", "(2μf+μm)/(4p)");
					} else if ((mgentwobyone == 1 && mgentwobytwo == 1
							&& gentwobyone == 1 && gentwobytwo > 1)
							|| (mgentwobyone == 1 && mgentwobytwo == 1
									&& gentwobyone > 1 && gentwobytwo == 1)) {
						// 第10'种情况 二可能一步+一可能一步
						result.put("function", "(μf+2μm)/(4p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo > 2
							&& gentwobyone == 2 && gentwobytwo == 2)
							|| (mgentwobyone > 2 && mgentwobytwo == 2
									&& gentwobyone == 2 && gentwobytwo == 2)) {
						// 第十一种情况 孩子第二个位点和母亲两个点位有一个是等于二 有一个大于二
						// 孩子和父亲相减都等于二 一可能二步+二可能二步
						result.put("function", "(2μf+μm)/(40p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo == 2
							&& gentwobyone == 2 && gentwobytwo > 2)
							|| (mgentwobyone == 2 && mgentwobytwo == 2
									&& gentwobyone > 2 && gentwobytwo == 2)) {
						// 第11'种情况 二可能二步+一可能二步
						result.put("function", "(μf+2μm)/(40p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo > 3
							&& gentwobyone == 3 && gentwobytwo == 3)
							|| (mgentwobyone > 3 && mgentwobytwo == 3
									&& gentwobyone == 3 && gentwobytwo == 3)) {
						// 第十二种情况 孩子第二个位点和母亲两个点位有一个是等于三 有一个大于三
						// 孩子和父亲相减都等于三 一可能三步+二可能三步
						result.put("function", "(2μf+μm)/(400p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo == 3
							&& gentwobyone == 3 && gentwobytwo > 3)
							|| (mgentwobyone == 3 && mgentwobytwo == 3
									&& gentwobyone > 3 && gentwobytwo == 3)) {
						// 第12'种情况 两可能三步+一可能三步
						result.put("function", "(μf+2μm)/(400p)");
					} else {
						// 是否可以加一个小数点的
						double max1 = gentwobyone > gentwobytwo ? gentwobytwo
								: gentwobyone;
						double max2 = mgentwobyone > mgentwobytwo ? mgentwobytwo
								: mgentwobyone;
						double max = max1 > max2 ? max2 : max1;
						String n = Double.toString(Math.pow(10,
								(Math.ceil(max) - 1)));
						result.put("n", n);
						if (max == max1)
							result.put("function", "μ/4p*");
						else
							result.put("function", "μm/4p*");

					}
				} else if (tempListm.contains(childs[1])
						&& tempListf.contains(childs[1])) {
					// 第二个点相同点 只要比第一个点和母亲 和父亲
					result.put("param_name", childs[0]);

					mgentwobyone = Math.abs(Double.parseDouble(childs[0])
							- Double.parseDouble(mothers[0]));
					mgentwobytwo = Math.abs(Double.parseDouble(childs[0])
							- Double.parseDouble(mothers[1]));

					if ((mgentwobyone == 1 && mgentwobytwo == 1
							&& genonebyone == 1 && genonebytwo == 1)) {
						// 第一种情况 孩子第一个位点分别和父母亲的两个点相减都等于1 这个是两可能一步+ 两可能一步
						result.put("function", "(μf+μm)/(2p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo == 2
							&& genonebyone == 2 && genonebytwo == 2)) {
						// 第二种情况 孩子第二个位点分别和父母亲的两个点相减都等于2 这个是两可能二步+ 两可能二步
						result.put("function", "(μf+μm)/(20p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo == 3
							&& genonebyone == 3 && genonebytwo == 3)) {
						// 第三种情况 孩子第二个位点分别和父母亲的两个点相减都等于3 这个是两可能三步+ 两可能三步
						result.put("function", "(μf+μm)/(200p)");
					} else if ((mgentwobyone == 1 && mgentwobytwo > 1
							&& genonebyone == 1 && genonebytwo > 1)
							|| (mgentwobyone == 1 && mgentwobytwo > 1
									&& genonebyone > 1 && genonebytwo == 1)
							|| (mgentwobyone > 1 && mgentwobytwo == 1
									&& genonebyone == 1 && genonebytwo > 1)
							|| (mgentwobyone > 1 && mgentwobytwo == 1
									&& genonebyone > 1 && genonebytwo == 1)) {
						// 第四种情况 孩子第二个位点和母亲两个点位有一个是等于一 有一个大于一
						// 相同孩子和父亲相减有一个等于一有一个大于一 这个就是一可能一步+一可能一步
						result.put("function", "(μf+μm)/(4p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo > 2
							&& genonebyone == 2 && genonebytwo > 2)
							|| (mgentwobyone == 2 && mgentwobytwo > 2
									&& genonebyone > 2 && genonebytwo == 2)
							|| (mgentwobyone > 2 && mgentwobytwo == 2
									&& genonebyone == 2 && genonebytwo > 2)
							|| (mgentwobyone > 2 && mgentwobytwo == 2
									&& genonebyone > 2 && genonebytwo == 2)) {
						// 第五种情况 一可能二步+一可能二步
						result.put("function", "(μf+μm)/(40p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo > 3
							&& genonebyone == 3 && genonebytwo > 3)
							|| (mgentwobyone == 3 && mgentwobytwo > 3
									&& genonebyone > 3 && genonebytwo == 3)
							|| (mgentwobyone > 3 && mgentwobytwo == 3
									&& genonebyone == 3 && genonebytwo > 3)
							|| (mgentwobyone > 3 && mgentwobytwo == 3
									&& genonebyone > 3 && genonebytwo == 3)) {
						// 第六种情况 一可能三步+一可能三步
						result.put("function", "(μf+μm)/(400p)");
					} else if ((mgentwobyone == 1 && mgentwobytwo > 1
							&& genonebyone == 2 && genonebytwo > 2)
							|| (mgentwobyone == 1 && mgentwobytwo > 1
									&& genonebyone > 2 && genonebytwo == 2)
							|| (mgentwobyone > 1 && mgentwobytwo == 1
									&& genonebyone == 2 && genonebytwo > 2)
							|| (mgentwobyone > 1 && mgentwobytwo == 1
									&& genonebyone > 2 && genonebytwo == 2)) {
						// 第七种情况 孩子第二个位点和母亲两个点位有一个是等于一 有一个大于一
						// 孩子和父亲相减有一个等于二有一个大于二 这个就是一可能一步+0
						result.put("function", "μm/(4p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo > 2
							&& genonebyone == 1 && genonebytwo > 1)
							|| (mgentwobyone == 2 && mgentwobytwo > 2
									&& genonebyone > 1 && genonebytwo == 1)
							|| (mgentwobyone > 2 && mgentwobytwo == 2
									&& genonebyone == 1 && genonebytwo > 1)
							|| (mgentwobyone > 2 && mgentwobytwo == 2
									&& genonebyone > 1 && genonebytwo == 1)) {
						// 第7'种情况 0+一可能一步
						result.put("function", "μf/(4p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo > 2
							&& genonebyone == 3 && genonebytwo > 3)
							|| (mgentwobyone == 2 && mgentwobytwo > 2
									&& genonebyone > 3 && genonebytwo == 3)
							|| (mgentwobyone > 2 && mgentwobytwo == 2
									&& genonebyone == 3 && genonebytwo > 3)
							|| (mgentwobyone > 2 && mgentwobytwo == 2
									&& genonebyone > 3 && genonebytwo == 3)) {
						// 第八种情况 孩子第二个位点和母亲两个点位有一个是等于二 有一个大于二
						// 孩子和父亲相减有一个等于三有一个大于三 这个就是一可能二步+0
						result.put("function", "μm/(40p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo > 3
							&& genonebyone == 2 && genonebytwo > 2)
							|| (mgentwobyone == 3 && mgentwobytwo > 3
									&& genonebyone > 2 && genonebytwo == 2)
							|| (mgentwobyone > 3 && mgentwobytwo == 3
									&& genonebyone == 2 && genonebytwo > 2)
							|| (mgentwobyone > 3 && mgentwobytwo == 3
									&& genonebyone > 2 && genonebytwo == 2)) {
						// 第8'种情况 0+一可能二步
						result.put("function", "μf/(40p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo > 3
							&& genonebyone == 4 && genonebytwo > 4)
							|| (mgentwobyone == 3 && mgentwobytwo > 3
									&& genonebyone > 4 && genonebytwo == 4)
							|| (mgentwobyone > 3 && mgentwobytwo == 3
									&& genonebyone == 4 && genonebytwo > 4)
							|| (mgentwobyone > 3 && mgentwobytwo == 3
									&& genonebyone > 4 && genonebytwo == 4)) {
						// 第九种情况 孩子第二个位点和母亲两个点位有一个是等于一 有一个大于一
						// 孩子和父亲相减有一个等于二有一个大于二 这个就是一可能三步+0
						result.put("function", "μm/(400p)");
					} else if ((mgentwobyone == 4 && mgentwobytwo > 4
							&& genonebyone == 3 && genonebytwo > 3)
							|| (mgentwobyone == 4 && mgentwobytwo > 4
									&& genonebyone > 3 && genonebytwo == 3)
							|| (mgentwobyone > 4 && mgentwobytwo == 4
									&& genonebyone == 3 && genonebytwo > 3)
							|| (mgentwobyone > 4 && mgentwobytwo == 4
									&& genonebyone > 3 && genonebytwo == 3)) {
						// 第9'种情况 0+一可能三步
						result.put("function", "μf/(400p)");
					} else if ((mgentwobyone == 1 && mgentwobytwo > 1
							&& genonebyone == 1 && genonebytwo == 1)
							|| (mgentwobyone > 1 && mgentwobytwo == 1
									&& genonebyone == 1 && genonebytwo == 1)) {
						// 第十种情况 孩子第二个位点和母亲两个点位有一个是等于一 有一个大于一
						// 孩子和父亲相减都等于一 一可能一步+二可能一步
						result.put("function", "(2μf+μm)/(4p)");
					} else if ((mgentwobyone == 1 && mgentwobytwo == 1
							&& genonebyone == 1 && genonebytwo > 1)
							|| (mgentwobyone == 1 && mgentwobytwo == 1
									&& genonebyone > 1 && genonebytwo == 1)) {
						// 第10'种情况 二可能一步+一可能一步
						result.put("function", "(μf+2μm)/(4p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo > 2
							&& genonebyone == 2 && genonebytwo == 2)
							|| (mgentwobyone > 2 && mgentwobytwo == 2
									&& genonebyone == 2 && genonebytwo == 2)) {
						// 第十一种情况 孩子第二个位点和母亲两个点位有一个是等于二 有一个大于二
						// 孩子和父亲相减都等于二 一可能二步+二可能二步
						result.put("function", "(2μf+μm)/(40p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo == 2
							&& genonebyone == 2 && genonebytwo > 2)
							|| (mgentwobyone == 2 && mgentwobytwo == 2
									&& genonebyone > 2 && genonebytwo == 2)) {
						// 第11'种情况 二可能二步+一可能二步
						result.put("function", "(μf+2μm)/(40p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo > 3
							&& genonebyone == 3 && genonebytwo == 3)
							|| (mgentwobyone > 3 && mgentwobytwo == 3
									&& genonebyone == 3 && genonebytwo == 3)) {
						// 第十二种情况 孩子第二个位点和母亲两个点位有一个是等于三 有一个大于三
						// 孩子和父亲相减都等于三 一可能三步+二可能三步
						result.put("function", "(2μf+μm)/(400p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo == 3
							&& genonebyone == 3 && genonebytwo > 3)
							|| (mgentwobyone == 3 && mgentwobytwo == 3
									&& genonebyone > 3 && genonebytwo == 3)) {
						// 第12'种情况 两可能三步+一可能三步
						result.put("function", "(μf+2μm)/(400p)");
					} else {
						// 是否可以加一个小数点的
						double max1 = genonebyone > genonebytwo ? genonebytwo
								: genonebyone;
						double max2 = mgentwobyone > mgentwobytwo ? mgentwobytwo
								: mgentwobyone;
						double max = max1 > max2 ? max2 : max1;
						String n = Double.toString(Math.pow(10,
								(Math.ceil(max) - 1)));
						result.put("n", n);
						result.put("function", "μ/4p*");
					}
				} else if (tempListm.contains(childs[0])) {
					// 孩子和父亲不同 孩子和母亲第一个点相同 那就比孩子第二个点父亲
					// 孩子跟母亲的数字完全相同用第二跳板，其余情况第一个跳板 1/2P
					result.put("param_name", childs[1]);
					if ((gentwobyone == 1 && gentwobytwo > 1)
							|| (gentwobyone > 1 && gentwobytwo == 1)) {
						// 一步一可能
						result.put("function", "μ/(4p)");
					} else if ((gentwobyone == 2 && gentwobytwo > 2)
							|| (gentwobyone > 2 && gentwobytwo == 2)) {
						// 二步一可能
						result.put("function", "μ/(40p)");
					} else if ((gentwobyone == 3 && gentwobytwo > 3)
							|| (gentwobyone > 3 && gentwobytwo == 3)) {
						// 三步一可能
						result.put("function", "μ/(400p)");
					} else if ((gentwobyone == 4 && gentwobytwo > 4)
							|| (gentwobyone > 4 && gentwobytwo == 4)) {
						// 四步一可能
						result.put("function", "μ/(4000p)");
					} else if ((gentwobyone == 1 && gentwobytwo == 1)) {
						// 一步两可能
						result.put("function", "μ/(2p)");
					} else if ((gentwobyone == 2 && gentwobytwo == 2)) {
						// 二步两可能
						result.put("function", "μ/(20p)");
					} else if ((gentwobyone == 3 && gentwobytwo == 3)) {
						// 三步两可能
						result.put("function", "μ/(200p)");
					} else if ((gentwobyone == 4 && gentwobytwo == 4)) {
						// 四步两可能
						result.put("function", "μ/(2000p)");
					} else {
						// 是否可以加一个小数点的
						double max = gentwobyone > gentwobytwo ? gentwobytwo
								: gentwobyone;
						if (gentwobyone == gentwobytwo) {
							String n = Double.toString(Math.pow(10, (max - 1)));
							result.put("n", n);
							result.put("function", "μ/2p*");
						} else {
							String n = Double.toString(Math.pow(10, (max - 1)));
							result.put("n", n);
							result.put("function", "μ/4p*");
						}
					}
				} else if (tempListm.contains(childs[1])) {
					// 孩子和父亲不同 孩子和母亲第二个点相同 那就比孩子第一个点父亲
					// 孩子跟母亲的数字完全相同用第二跳板，其余情况第一个跳板 1/2P
					result.put("param_name", childs[0]);
					if ((genonebyone == 1 && genonebytwo > 1)
							|| (genonebyone > 1 && genonebytwo == 1)) {
						// 一步一可能
						result.put("function", "μ/(4p)");
					} else if ((genonebyone == 2 && genonebytwo > 2)
							|| (genonebyone > 2 && genonebytwo == 2)) {
						// 二步一可能
						result.put("function", "μ/(40p)");
					} else if ((genonebyone == 3 && genonebytwo > 3)
							|| (genonebyone > 3 && genonebytwo == 3)) {
						// 三步一可能
						result.put("function", "μ/(400p)");
					} else if ((genonebyone == 4 && genonebytwo > 4)
							|| (genonebyone > 4 && genonebytwo == 4)) {
						// 四步一可能
						result.put("function", "μ/(4000p)");
					} else if ((genonebyone == 1 && genonebytwo == 1)) {
						// 一步两可能
						result.put("function", "μ/(2p)");
					} else if ((genonebyone == 2 && genonebytwo == 2)) {
						// 二步两可能
						result.put("function", "μ/(20p)");
					} else if ((genonebyone == 3 && genonebytwo == 3)) {
						// 三步两可能
						result.put("function", "μ/(200p)");
					} else if ((genonebyone == 4 && genonebytwo == 4)) {
						// 四步两可能
						result.put("function", "μ/(2000p)");
					} else {
						// 是否可以加一个小数点的
						double max = genonebyone > genonebytwo ? genonebytwo
								: genonebyone;
						if (genonebyone == genonebytwo) {
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/2p*");
						} else {
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/4p*");
						}
					}
				} else if (tempListf.contains(childs[0])) {
					// 孩子和母亲不同 孩子和父亲第一个点相同 那就比孩子第二个点母亲第一个跳板 1/2P
					// 母亲 13，15 孩子14，15.2 父亲 14.2 ，15.2 这个是孩子和父亲相同比孩子和母亲?
					// 两可能一步突变?μm/2P(14)
					mgentwobyone = Math.abs(Double.parseDouble(childs[1])
							- Double.parseDouble(mothers[0]));
					if (mothers.length == 1) {
						mgentwobytwo = Math.abs(Double.parseDouble(childs[1])
								- Double.parseDouble(mothers[0]));
					} else {
						mgentwobytwo = Math.abs(Double.parseDouble(childs[1])
								- Double.parseDouble(mothers[1]));
					}
					result.put("param_name", childs[1]);
					if ((mgentwobyone == 1 && mgentwobytwo > 1)
							|| (mgentwobyone > 1 && mgentwobytwo == 1)) {
						// 一步一可能
						result.put("function", "μm/(4p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo > 2)
							|| (mgentwobyone > 2 && mgentwobytwo == 2)) {
						// 二步一可能
						result.put("function", "μm/(40p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo > 3)
							|| (mgentwobyone > 3 && mgentwobytwo == 3)) {
						// 三步一可能
						result.put("function", "μm/(400p)");
					} else if ((mgentwobyone == 4 && mgentwobytwo > 4)
							|| (mgentwobyone > 4 && mgentwobytwo == 4)) {
						// 四步一可能
						result.put("function", "μm/(4000p)");
					} else if ((mgentwobyone == 1 && mgentwobytwo == 1)) {
						// 一步两可能
						result.put("function", "μm/(2p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo == 2)) {
						// 二步两可能
						result.put("function", "μm/(20p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo == 3)) {
						// 三步两可能
						result.put("function", "μm/(200p)");
					} else if ((mgentwobyone == 4 && mgentwobytwo == 4)) {
						// 四步两可能
						result.put("function", "μm/(2000p)");
					} else {
						// 是否可以加一个小数点的
						double max = mgentwobyone > mgentwobytwo ? mgentwobytwo
								: mgentwobyone;
						if (mgentwobyone == mgentwobytwo) {
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μm/2p*");
						} else {
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μm/4p*");
						}
					}
				} else if (tempListf.contains(childs[1])) {
					// 孩子和父亲不同 孩子和母亲第二个点相同 那就比孩子第一个点父亲
					// 孩子跟母亲的数字完全相同用第二跳板，其余情况第一个跳板 1/2P
					mgentwobyone = Math.abs(Double.parseDouble(childs[0])
							- Double.parseDouble(mothers[0]));
					mgentwobytwo = Math.abs(Double.parseDouble(childs[0])
							- Double.parseDouble(mothers[1]));
					result.put("param_name", childs[0]);
					if ((mgentwobyone == 1 && mgentwobytwo > 1)
							|| (mgentwobyone > 1 && mgentwobytwo == 1)) {
						// 一步一可能
						result.put("function", "μm/(4p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo > 2)
							|| (mgentwobyone > 2 && mgentwobytwo == 2)) {
						// 二步一可能
						result.put("function", "μm/(40p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo > 3)
							|| (mgentwobyone > 3 && mgentwobytwo == 3)) {
						// 三步一可能
						result.put("function", "μm/(400p)");
					} else if ((mgentwobyone == 4 && mgentwobytwo > 4)
							|| (mgentwobyone > 4 && mgentwobytwo == 4)) {
						// 四步一可能
						result.put("function", "μm/(4000p)");
					} else if ((mgentwobyone == 1 && mgentwobytwo == 1)) {
						// 一步两可能
						result.put("function", "μm/(2p)");
					} else if ((mgentwobyone == 2 && mgentwobytwo == 2)) {
						// 二步两可能
						result.put("function", "μm/(20p)");
					} else if ((mgentwobyone == 3 && mgentwobytwo == 3)) {
						// 三步两可能
						result.put("function", "μm/(200p)");
					} else if ((mgentwobyone == 4 && mgentwobytwo == 4)) {
						// 四步两可能
						result.put("function", "μm/(2000p)");
					} else {
						// 是否可以加一个小数点的
						double max = mgentwobyone > mgentwobytwo ? mgentwobytwo
								: mgentwobyone;
						if (mgentwobyone == mgentwobytwo) {
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μm/2p*");
						} else {
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μm/4p*");
						}
					}
				} else {
					mgentwobyone = Math.abs(Double.parseDouble(childs[1])
							- Double.parseDouble(mothers[0]));
					mgentwobytwo = Math.abs(Double.parseDouble(childs[1])
							- Double.parseDouble(mothers[1]));
					double mgenonebyone = Math.abs(Double
							.parseDouble(childs[0])
							- Double.parseDouble(mothers[0]));
					double mgenonebytwo = Math.abs(Double
							.parseDouble(childs[0])
							- Double.parseDouble(mothers[1]));
					if (father.equals(child) || tempListf.contains(childs[1])
							|| tempListf.contains(childs[0])) {
						// 孩子和母亲不同 孩子和父亲相同 那就比母亲
						// 第一个跳板 1/2P
						if (fathers.length == 1) {
							result.put("param_name", childs[0]);
							if ((mgentwobyone == 1 && mgentwobytwo > 1)
									|| (mgentwobyone > 1 && mgentwobytwo == 1)) {
								// 一可能性 一步突变 1/2P*μ/2
								result.put("function", "μm/(4p)");
							} else if ((mgentwobyone == 2 && mgentwobytwo > 2)
									|| (mgentwobyone > 2 && mgentwobytwo == 2)) {
								// 一可能性 二步突变 1/2P*μ/20
								result.put("function", "μm/(40p)");
							} else if ((mgentwobyone == 3 && mgentwobytwo > 3)
									|| (mgentwobyone > 3 && mgentwobytwo == 3)) {
								// 一可能性 二步突变 1/2P*μ/200
								result.put("function", "μm/(400p)");
							} else if ((mgentwobyone == 4 && mgentwobytwo > 4)
									|| (mgentwobyone > 4 && mgentwobytwo == 4)) {
								// 一可能性 二步突变 1/2P*μ/2000
								result.put("function", "μm/(4000p)");
							} else {
								// 是否可以加一个小数点的
								double max = mgentwobyone > mgentwobytwo ? mgentwobytwo
										: mgentwobyone;
								String n = Double.toString(Math.pow(10,
										(max - 1)));
								result.put("n", n);
								result.put("function", "μm/4p*");
							}
						} else {
							// 父亲该点位数据有两个值 当母=子=PQ 第二个 跳板1/[2(P+Q)] 然后在讨论 孩子 和
							// 父亲之间的可能性
							result.put("param_name", childs[0]);
							result.put("param_name1", childs[1]);
							if ((mgenonebyone == 1 && mgenonebytwo > 1
									&& mgentwobyone > 1 && mgentwobytwo > 1)
									|| (mgenonebyone > 1 && mgenonebytwo == 1
											&& mgentwobyone > 1 && mgentwobytwo > 1)
									|| (mgenonebyone > 1 && mgenonebytwo > 1
											&& mgentwobyone == 1 && mgentwobytwo > 1)
									|| (mgenonebyone > 1 && mgenonebytwo > 1
											&& mgentwobyone > 1 && mgentwobytwo == 1)) {
								// 一可能 一步突变
								result.put("function", "μm/[4(p+q)]");
							} else if ((mgenonebyone == 2 && mgenonebytwo > 2
									&& mgentwobyone > 2 && mgentwobytwo > 2)
									|| (mgenonebyone > 2 && mgenonebytwo == 2
											&& mgentwobyone > 2 && mgentwobytwo > 2)
									|| (mgenonebyone > 2 && mgenonebytwo > 2
											&& mgentwobyone == 2 && mgentwobytwo > 2)
									|| (mgenonebyone > 2 && mgenonebytwo > 2
											&& mgentwobyone > 2 && mgentwobytwo == 2)) {
								// 一可能 二步突变
								result.put("function", "μm/[40(p+q)]");
							} else if ((mgenonebyone == 3 && mgenonebytwo > 3
									&& mgentwobyone > 3 && mgentwobytwo > 3)
									|| (mgenonebyone > 3 && mgenonebytwo == 3
											&& mgentwobyone > 3 && mgentwobytwo > 3)
									|| (mgenonebyone > 3 && mgenonebytwo > 3
											&& mgentwobyone == 3 && mgentwobytwo > 3)
									|| (mgenonebyone > 3 && mgenonebytwo > 3
											&& mgentwobyone > 3 && mgentwobytwo == 3)) {
								// 一可能 三步突变
								result.put("function", "μm/[400(p+q)]");
							} else if ((mgenonebyone == 4 && mgenonebytwo > 4
									&& mgentwobyone > 4 && mgentwobytwo > 4)
									|| (mgenonebyone > 4 && mgenonebytwo == 4
											&& mgentwobyone > 4 && mgentwobytwo > 4)
									|| (mgenonebyone > 4 && mgenonebytwo > 4
											&& mgentwobyone == 4 && mgentwobytwo > 4)
									|| (mgenonebyone > 4 && mgenonebytwo > 4
											&& mgentwobyone > 4 && mgentwobytwo == 4)) {
								// 一可能 四步突变
								result.put("function", "μm/[4000(p+q)]");
							} else if ((mgenonebyone == 1 && mgenonebytwo == 1
									&& mgentwobyone > 1 && mgentwobytwo > 1)
									|| (mgenonebyone > 1 && mgenonebytwo > 1
											&& mgentwobyone == 1 && mgentwobytwo == 1)
									|| (mgenonebyone == 1 && mgenonebytwo > 1
											&& mgentwobyone == 1 && mgentwobytwo > 1)
									|| (mgenonebyone == 1 && mgenonebytwo > 1
											&& mgentwobyone > 1 && mgentwobytwo == 1)
									|| (mgenonebyone > 1 && mgenonebytwo == 1
											&& mgentwobyone == 1 && mgentwobytwo > 1)
									|| (mgenonebyone > 1 && mgenonebytwo == 1
											&& mgentwobyone > 1 && mgentwobytwo == 1)) {
								// 两可能 一步突变
								result.put("function", "μm/[2(p+q)]");
							} else if ((mgenonebyone == 2 && mgenonebytwo == 2
									&& mgentwobyone > 2 && mgentwobytwo > 2)
									|| (mgenonebyone > 2 && mgenonebytwo > 2
											&& mgentwobyone == 2 && mgentwobytwo == 2)
									|| (mgenonebyone == 2 && mgenonebytwo > 2
											&& mgentwobyone == 2 && mgentwobytwo > 2)
									|| (mgenonebyone == 2 && mgenonebytwo > 2
											&& mgentwobyone > 2 && mgentwobytwo == 2)
									|| (mgenonebyone > 2 && mgenonebytwo == 2
											&& mgentwobyone == 2 && mgentwobytwo > 2)
									|| (mgenonebyone > 2 && mgenonebytwo == 2
											&& mgentwobyone > 2 && mgentwobytwo == 2)) {
								// 两可能 二步突变
								result.put("function", "μm/[20(p+q)]");
							} else if ((mgenonebyone == 3 && mgenonebytwo == 3
									&& mgentwobyone > 3 && mgentwobytwo > 3)
									|| (mgenonebyone > 3 && mgenonebytwo > 3
											&& mgentwobyone == 3 && mgentwobytwo == 3)
									|| (mgenonebyone == 3 && mgenonebytwo > 3
											&& mgentwobyone == 3 && mgentwobytwo > 3)
									|| (mgenonebyone == 3 && mgenonebytwo > 3
											&& mgentwobyone > 3 && mgentwobytwo == 3)
									|| (mgenonebyone > 3 && mgenonebytwo == 3
											&& mgentwobyone == 3 && mgentwobytwo > 3)
									|| (mgenonebyone > 3 && mgenonebytwo == 3
											&& mgentwobyone > 3 && mgentwobytwo == 3)) {
								// 两可能 三步突变
								result.put("function", "μm/[200(p+q)]");
							} else if ((mgenonebyone == 4 && mgenonebytwo == 4
									&& mgentwobyone > 4 && mgentwobytwo > 4)
									|| (mgenonebyone > 4 && mgenonebytwo > 4
											&& mgentwobyone == 4 && mgentwobytwo == 4)
									|| (mgenonebyone == 4 && mgenonebytwo > 4
											&& mgentwobyone == 4 && mgentwobytwo > 4)
									|| (mgenonebyone == 4 && mgenonebytwo > 4
											&& mgentwobyone > 4 && mgentwobytwo == 4)
									|| (mgenonebyone > 4 && mgenonebytwo == 4
											&& mgentwobyone == 4 && mgentwobytwo > 4)
									|| (mgenonebyone > 4 && mgenonebytwo == 4
											&& mgentwobyone > 4 && mgentwobytwo == 4)) {
								// 两可能 四步突变
								result.put("function", "μm/[2000(p+q)]");
							} else if ((mgenonebyone == 1 && mgenonebytwo == 1
									&& mgentwobyone == 1 && mgentwobytwo > 1)
									|| (mgenonebyone == 1 && mgenonebytwo == 1
											&& mgentwobyone > 1 && mgentwobytwo == 1)
									|| (mgenonebyone == 1 && mgenonebytwo > 1
											&& mgentwobyone == 1 && mgentwobytwo == 1)
									|| (mgenonebyone > 1 && mgenonebytwo == 1
											&& mgentwobyone == 1 && mgentwobytwo == 1)) {
								// 三可能 一步突变
								result.put("function", "(3μm)/[4(p+q)]");
							} else if ((mgenonebyone == 2 && mgenonebytwo == 2
									&& mgentwobyone == 2 && mgentwobytwo > 2)
									|| (mgenonebyone == 2 && mgenonebytwo == 2
											&& mgentwobyone > 2 && mgentwobytwo == 2)
									|| (mgenonebyone == 2 && mgenonebytwo > 2
											&& mgentwobyone == 2 && mgentwobytwo == 2)
									|| (mgenonebyone > 2 && mgenonebytwo == 2
											&& mgentwobyone == 2 && mgentwobytwo == 2)) {
								// 三可能 二步突变
								result.put("function", "(3μm)/[40(p+q)]");
							} else if ((mgenonebyone == 3 && mgenonebytwo == 3
									&& mgentwobyone == 3 && mgentwobytwo > 3)
									|| (mgenonebyone == 3 && mgenonebytwo == 3
											&& mgentwobyone > 3 && mgentwobytwo == 3)
									|| (mgenonebyone == 3 && mgenonebytwo > 3
											&& mgentwobyone == 3 && mgentwobytwo == 3)
									|| (mgenonebyone > 3 && mgenonebytwo == 3
											&& mgentwobyone == 3 && mgentwobytwo == 3)) {
								// 三可能 三步突变
								result.put("function", "(3μm)/[400(p+q)]");
							} else if ((mgenonebyone == 4 && mgenonebytwo == 4
									&& mgentwobyone == 4 && mgentwobytwo > 4)
									|| (mgenonebyone == 4 && mgenonebytwo == 42
											&& mgentwobyone > 4 && mgentwobytwo == 4)
									|| (mgenonebyone == 4 && mgenonebytwo > 4
											&& mgentwobyone == 4 && mgentwobytwo == 4)
									|| (mgenonebyone > 4 && mgenonebytwo == 4
											&& mgentwobyone == 4 && mgentwobytwo == 4)) {
								// 三可能 四步突变
								result.put("function", "(3μm)/[4000(p+q)]");
							} else {
								// 是否可以加一个小数点的
								double max1 = mgenonebyone > mgenonebytwo ? mgenonebytwo
										: mgenonebyone;
								double max2 = mgentwobyone > mgentwobytwo ? mgentwobytwo
										: mgentwobyone;
								double max = max1 > max2 ? max2 : max1;
								if (max == max1)
									result.put("param_name", childs[0]);
								else
									result.put("param_name", childs[1]);
								String n = Double.toString(Math.pow(10,
										(Math.ceil(max) - 1)));
								result.put("n", n);
								result.put("function", "μm/4p*");
							}
						}
					} else if (father.equals(mother)) {
						// 父亲和母亲点位相同 比较父亲
						if (childs[0].equals(mothers[0])
								|| childs[0].equals(mothers[1])) {
							result.put("param_name", childs[1]);
						} else {
							result.put("param_name", childs[0]);
						}
						if ((genonebyone == 1 && genonebytwo == 1)
								|| (gentwobyone == 1 && gentwobytwo == 1)
								|| (genonebyone == 1 && genonebytwo > 1)
								&& (gentwobyone == 1 && gentwobytwo > 1)
								|| (genonebyone == 1 && genonebytwo > 1)
								&& (gentwobyone > 1 && gentwobytwo == 1)
								|| (genonebyone > 1 && genonebytwo == 1)
								&& (gentwobyone == 1 && gentwobytwo > 1)
								|| (genonebyone > 1 && genonebytwo == 1)
								&& (gentwobyone > 1 && gentwobytwo == 1)) {
							// 两可能 一步突变
							result.put("function", "μ/(2p)");
						} else if ((genonebyone == 2 && genonebytwo == 2)
								|| (gentwobyone == 2 && gentwobytwo == 2)
								|| (genonebyone == 2 && genonebytwo > 2)
								&& (gentwobyone == 2 && gentwobytwo > 2)
								|| (genonebyone == 2 && genonebytwo > 2)
								&& (gentwobyone > 2 && gentwobytwo == 2)
								|| (genonebyone > 2 && genonebytwo == 2)
								&& (gentwobyone == 2 && gentwobytwo > 2)
								|| (genonebyone > 2 && genonebytwo == 2)
								&& (gentwobyone > 2 && gentwobytwo == 2)) {
							// 两可能 二步突变
							result.put("function", "μ/(20p)");
						} else if ((genonebyone == 3 && genonebytwo == 3)
								|| (gentwobyone == 3 && gentwobytwo == 3)
								|| (genonebyone == 3 && genonebytwo > 3)
								&& (gentwobyone == 3 && gentwobytwo > 3)
								|| (genonebyone == 3 && genonebytwo > 3)
								&& (gentwobyone > 3 && gentwobytwo == 3)
								|| (genonebyone > 3 && genonebytwo == 3)
								&& (gentwobyone == 3 && gentwobytwo > 3)
								|| (genonebyone > 3 && genonebytwo == 3)
								&& (gentwobyone > 3 && gentwobytwo == 3)) {
							// 两可能 三步突变
							result.put("function", "μ/(200p)");
						} else if ((genonebyone == 4 && genonebytwo == 4)
								|| (gentwobyone == 4 && gentwobytwo == 4)
								|| (genonebyone == 4 && genonebytwo > 4)
								&& (gentwobyone == 4 && gentwobytwo > 4)
								|| (genonebyone == 4 && genonebytwo > 4)
								&& (gentwobyone > 4 && gentwobytwo == 4)
								|| (genonebyone > 4 && genonebytwo == 4)
								&& (gentwobyone == 4 && gentwobytwo > 4)
								|| (genonebyone > 4 && genonebytwo == 4)
								&& (gentwobyone > 4 && gentwobytwo == 4)) {
							// 两可能 四步突变
							result.put("function", "μ/(2000p)");
						} else if ((genonebyone == 1 && genonebytwo > 1)
								|| (gentwobyone == 1 && gentwobytwo > 1)
								|| (genonebyone > 1 && genonebytwo == 1)
								|| (gentwobyone > 1 && gentwobytwo == 1)) {
							// 一可能 一步突变
							result.put("function", "μ/(4p)");
						} else if ((genonebyone == 2 && genonebytwo > 2)
								|| (gentwobyone == 2 && gentwobytwo > 2)
								|| (genonebyone > 2 && genonebytwo == 2)
								|| (gentwobyone > 2 && gentwobytwo == 2)) {
							// 一可能 二步突变
							result.put("function", "μ/(40p)");
						} else if ((genonebyone == 3 && genonebytwo > 3)
								|| (gentwobyone == 3 && gentwobytwo > 3)
								|| (genonebyone > 3 && genonebytwo == 3)
								|| (gentwobyone > 3 && gentwobytwo == 3)) {
							// 一可能 三步突变
							result.put("function", "μ/(400p)");
						} else if ((genonebyone == 4 && genonebytwo > 4)
								|| (gentwobyone == 4 && gentwobytwo > 4)
								|| (genonebyone > 4 && genonebytwo == 4)
								|| (gentwobyone > 4 && gentwobytwo == 4)) {
							// 一可能 四步突变
							result.put("function", "μ/(4000p)");
						} else {
							// 是否可以加一个小数点的
							double max1 = genonebyone > genonebytwo ? genonebytwo
									: genonebyone;
							double max2 = gentwobyone > gentwobytwo ? gentwobytwo
									: gentwobyone;
							double max = max1 > max2 ? max2 : max1;
							if ((max1 == max2) || (genonebyone == gentwobyone)
									|| (genonebyone == gentwobytwo)
									|| (genonebytwo == gentwobytwo)
									|| (genonebytwo == gentwobytwo)) {
								if (max == max1)
									result.put("param_name", childs[0]);
								else
									result.put("param_name", childs[1]);
								String n = Double.toString(Math.pow(10,
										(Math.ceil(max) - 1)));
								result.put("n", n);
								result.put("function", "μ/2p*");
							} else {
								if (max == max1)
									result.put("param_name", childs[0]);
								else
									result.put("param_name", childs[1]);
								String n = Double.toString(Math.pow(10,
										(Math.ceil(max) - 1)));
								result.put("n", n);
								result.put("function", "μ/4p*");
							}
						}
					} else {
						// 这种情况有点问题
						if ((genonebyone == 1 && genonebytwo == 1)
								|| (gentwobyone == 1 && gentwobytwo == 1)
								|| (genonebyone == 1 && genonebytwo > 1)
								&& (gentwobyone == 1 && gentwobytwo > 1)
								|| (genonebyone == 1 && genonebytwo > 1)
								&& (gentwobyone > 1 && gentwobytwo == 1)
								|| (genonebyone > 1 && genonebytwo == 1)
								&& (gentwobyone == 1 && gentwobytwo > 1)
								|| (genonebyone > 1 && genonebytwo == 1)
								&& (gentwobyone > 1 && gentwobytwo == 1)) {
							// 两可能 一步突变
							// D5S818 母亲 12，13 孩子 10-15 父亲11-14
							// 公式改为μ/4P10+μ/4P15
							if (genonebyone == 1 && genonebytwo == 1) {
								result.put("param_name", childs[0]);
								result.put("function", "μ/(2p)");
							} else if (gentwobyone == 1 && gentwobytwo == 1) {
								result.put("param_name", childs[1]);
								result.put("function", "μ/(2p)");
							} else {
								result.put("param_name", childs[0]);
								result.put("param_name", childs[1]);
								result.put("function", "μ/(4p)+μ/(4q)");
							}
						} else if ((genonebyone == 2 && genonebytwo == 2)
								|| (gentwobyone == 2 && gentwobytwo == 2)
								|| (genonebyone == 2 && genonebytwo > 2)
								&& (gentwobyone == 2 && gentwobytwo > 2)
								|| (genonebyone == 2 && genonebytwo > 2)
								&& (gentwobyone > 2 && gentwobytwo == 2)
								|| (genonebyone > 2 && genonebytwo == 2)
								&& (gentwobyone == 2 && gentwobytwo > 2)
								|| (genonebyone > 2 && genonebytwo == 2)
								&& (gentwobyone > 2 && gentwobytwo == 2)) {
							// 两可能 二步突变
							// VWA 母亲 14，17 孩子 13，18 父亲 15，16
							// 公式改为μ/40P（13）+μ/40P(18）
							if (genonebyone == 2 && genonebytwo == 2) {
								result.put("param_name", childs[0]);
								result.put("function", "μ/(20p)");
							} else if (gentwobyone == 2 && gentwobytwo == 2) {
								result.put("param_name", childs[1]);
								result.put("function", "μ/(20p)");
							} else {
								result.put("param_name", childs[0]);
								result.put("param_name", childs[1]);
								result.put("function", "μ/(40p)+μ/(40q)");
							}
						} else if ((genonebyone == 3 && genonebytwo == 3)
								|| (gentwobyone == 3 && gentwobytwo == 3)
								|| (genonebyone == 3 && genonebytwo > 3)
								&& (gentwobyone == 3 && gentwobytwo > 3)
								|| (genonebyone == 3 && genonebytwo > 3)
								&& (gentwobyone > 3 && gentwobytwo == 3)
								|| (genonebyone > 3 && genonebytwo == 3)
								&& (gentwobyone == 3 && gentwobytwo > 3)
								|| (genonebyone > 3 && genonebytwo == 3)
								&& (gentwobyone > 3 && gentwobytwo == 3)) {
							// 两可能 三步突变
							if (genonebyone == 3 && genonebytwo == 3) {
								result.put("param_name", childs[0]);
								result.put("function", "μ/(200p)");
							} else if (gentwobyone == 3 && gentwobytwo == 3) {
								result.put("param_name", childs[1]);
								result.put("function", "μ/(200p)");
							} else {
								result.put("param_name", childs[0]);
								result.put("param_name", childs[1]);
								result.put("function", "μ/(400p)+μ/(400q)");
							}
						} else if ((genonebyone == 4 && genonebytwo == 4)
								|| (gentwobyone == 4 && gentwobytwo == 4)
								|| (genonebyone == 4 && genonebytwo > 4)
								&& (gentwobyone == 4 && gentwobytwo > 4)
								|| (genonebyone == 4 && genonebytwo > 4)
								&& (gentwobyone > 4 && gentwobytwo == 4)
								|| (genonebyone > 4 && genonebytwo == 4)
								&& (gentwobyone == 4 && gentwobytwo > 4)
								|| (genonebyone > 4 && genonebytwo == 4)
								&& (gentwobyone > 4 && gentwobytwo == 4)) {
							// 两可能 四步突变
							if (genonebyone == 4 && genonebytwo == 4) {
								result.put("param_name", childs[0]);
								result.put("function", "μ/(2000p)");
							} else if (gentwobyone == 4 && gentwobytwo == 4) {
								result.put("param_name", childs[1]);
								result.put("function", "μ/(2000p)");
							} else {
								result.put("param_name", childs[0]);
								result.put("param_name", childs[1]);
								result.put("function", "μ/(4000p)+μ/(4000q)");
							}
						} else if ((genonebyone == 1 && genonebytwo > 1)
								|| (gentwobyone == 1 && gentwobytwo > 1)
								|| (genonebyone > 1 && genonebytwo == 1)
								|| (gentwobyone > 1 && gentwobytwo == 1)) {
							// 一可能 一步突变
							if (genonebyone == 1)
								result.put("param_name", childs[0]);
							else
								result.put("param_name", childs[1]);
							result.put("function", "μ/(4p)");
						} else if ((genonebyone == 2 && genonebytwo > 2)
								|| (gentwobyone == 2 && gentwobytwo > 2)
								|| (genonebyone > 2 && genonebytwo == 2)
								|| (gentwobyone > 2 && gentwobytwo == 2)) {
							// 一可能 二步突变
							if (genonebyone == 2)
								result.put("param_name", childs[0]);
							else
								result.put("param_name", childs[1]);
							result.put("function", "μ/(40p)");
						} else if ((genonebyone == 3 && genonebytwo > 3)
								|| (gentwobyone == 3 && gentwobytwo > 3)
								|| (genonebyone > 3 && genonebytwo == 3)
								|| (gentwobyone > 3 && gentwobytwo == 3)) {
							// 一可能 三步突变
							if (genonebyone == 3)
								result.put("param_name", childs[0]);
							else
								result.put("param_name", childs[1]);
							result.put("function", "μ/(400p)");
						} else if ((genonebyone == 4 && genonebytwo > 4)
								|| (gentwobyone == 4 && gentwobytwo > 4)
								|| (genonebyone > 4 && genonebytwo == 4)
								|| (gentwobyone > 4 && gentwobytwo == 4)) {
							// 一可能 四步突变
							if (genonebyone == 4)
								result.put("param_name", childs[0]);
							else
								result.put("param_name", childs[1]);
							result.put("function", "μ/(4000p)");
						} else {
							// 是否可以加一个小数点的
							double max1 = genonebyone > genonebytwo ? genonebytwo
									: genonebyone;
							double max2 = gentwobyone > gentwobytwo ? gentwobytwo
									: gentwobyone;
							double max = max1 > max2 ? max2 : max1;
							if (max == max1)
								result.put("param_name", childs[0]);
							else
								result.put("param_name", childs[1]);
							String n = Double.toString(Math.pow(10,
									(Math.ceil(max) - 1)));
							result.put("n", n);
							result.put("function", "μ/4p*");
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 三人分别计算个人肯定亲权值
	 * 
	 * @param sub_case_code
	 * @param parent1
	 * @param child
	 * @return
	 * @throws IllegalAccessException
	 */
	private BigDecimal calPIbyOne(String sub_case_code,
			RdsJudicialSampleResultModel parent1,
			RdsJudicialSampleResultModel child, int count, String unmatched_node)
			throws IllegalAccessException {
		BigDecimal pi = new BigDecimal(1);
		double gene1 = 0;
		double gene2 = 0;

		Map<String, Object> record = parent1.getRecord();
		Set<String> set = record.keySet();
		String[] nodes = unmatched_node.split(",");
		List<String> tempList = Arrays.asList(nodes);
		for (String ateName : set) {
			if (tempList.contains(ateName)) {
				String parentStr = (String) parent1.getRecord().get(ateName);
				String childStr = (String) child.getRecord().get(ateName);
				Map<String, String> result = findFunction(parentStr, childStr);
				if (result.size() == 3) {
					Map<String, Object> params = new HashMap<String, Object>();
					String param_type = ateName;
					params.put("param_type", param_type);
					params.put("param_name", result.get("param_name"));
					params.put("reagent_name", child.getReagent_name());
					String temp = queryGen(params);
					if (temp == null) {
						continue;
					}
					gene1 = Double.parseDouble(temp);
					Map<String, Object> params1 = new HashMap<String, Object>();
					params1.put("param_type", param_type);
					params1.put("param_name", result.get("param_name1"));
					params1.put("reagent_name", child.getReagent_name());
					String temp2 = queryGen(params1);
					if (temp2 == null) {
						continue;
					}
					gene2 = Double.parseDouble(temp2);
					double sub_pi = 0.25 / gene1 + 0.25 / gene2;
					pi = pi.multiply(new BigDecimal(sub_pi));
					sub_pi = new BigDecimal(sub_pi).setScale(4,
							BigDecimal.ROUND_HALF_UP).doubleValue();
					RdsJudicialPiInfoModel rdsJudicialPiInfoModel = new RdsJudicialPiInfoModel();
					rdsJudicialPiInfoModel.setSub_case_code(sub_case_code);
					rdsJudicialPiInfoModel.setParam_type(param_type);
					rdsJudicialPiInfoModel.setChild(childStr);
					rdsJudicialPiInfoModel.setParent(parentStr);
					rdsJudicialPiInfoModel.setGene1(StringUtils.complete0(
							new BigDecimal(gene1).setScale(4,
									BigDecimal.ROUND_HALF_UP).toString(), 4));
					rdsJudicialPiInfoModel.setGene2(StringUtils.complete0(
							new BigDecimal(gene2).setScale(4,
									BigDecimal.ROUND_HALF_UP).toString(), 4));
					rdsJudicialPiInfoModel.setFunction(result.get("function"));
					rdsJudicialPiInfoModel.setPi(StringUtils.complete0(
							(sub_pi + ""), 4));
					rdsJudicialCaseParamMapper
							.insertPiInfo(rdsJudicialPiInfoModel);

				} else {
					String function = result.get("function");
					Map<String, Object> params = new HashMap<String, Object>();
					String param_type = ateName;
					params.put("param_type", param_type);
					params.put("param_name", result.get("param_name"));
					params.put("reagent_name", child.getReagent_name());
					String temp = queryGen(params);
					if (temp == null) {
						continue;
					}
					gene1 = Double.parseDouble(temp);
					double sub_pi = 0;
					if (function.equals("1/p"))
						sub_pi = 1 / gene1;
					else if (function.equals("0.5/p"))
						sub_pi = 0.5 / gene1;
					else if (function.equals("0.25/p"))
						sub_pi = 0.25 / gene1;

					pi = pi.multiply(new BigDecimal(sub_pi));
					sub_pi = new BigDecimal(sub_pi).setScale(4,
							BigDecimal.ROUND_HALF_UP).doubleValue();
					RdsJudicialPiInfoModel rdsJudicialPiInfoModel = new RdsJudicialPiInfoModel();
					rdsJudicialPiInfoModel.setSub_case_code(sub_case_code);
					rdsJudicialPiInfoModel.setParam_type(param_type);
					rdsJudicialPiInfoModel.setChild(childStr);
					rdsJudicialPiInfoModel.setParent(parentStr);
					rdsJudicialPiInfoModel.setGene1(StringUtils.complete0(
							new BigDecimal(gene1).setScale(4,
									BigDecimal.ROUND_HALF_UP).toString(), 4));
					rdsJudicialPiInfoModel.setFunction(result.get("function"));
					rdsJudicialPiInfoModel.setPi(StringUtils.complete0(
							(sub_pi + ""), 4));
					rdsJudicialCaseParamMapper
							.insertPiInfo(rdsJudicialPiInfoModel);
				}
			}
		}
		return pi;
	}

	/**
	 * 两个人肯定亲权值
	 * 
	 * @param sub_case_code
	 * @param parent1
	 * @param child
	 * @return
	 * @throws IllegalAccessException
	 */
	private BigDecimal calPI(String sub_case_code,
			RdsJudicialSampleResultModel parent1,
			RdsJudicialSampleResultModel child, int count, String unmatched_node)
			throws IllegalAccessException {
		BigDecimal pi = new BigDecimal(1);
		double gene1 = 0;
		double gene2 = 0;

		Map<String, Object> record = parent1.getRecord();
		Set<String> set = record.keySet();
		String[] nodes = unmatched_node.split(",");
		List<String> tempList = Arrays.asList(nodes);
		for (String ateName : set) {
			if (!tempList.contains(ateName)) {
				String parentStr = (String) parent1.getRecord().get(ateName);
				String childStr = (String) child.getRecord().get(ateName);
				Map<String, String> result = findFunction(parentStr, childStr);
				if (result.size() == 3) {
					Map<String, Object> params = new HashMap<String, Object>();
					String param_type = ateName;
					params.put("param_type", param_type);
					params.put("param_name", result.get("param_name"));
					params.put("reagent_name", child.getReagent_name());
					String temp = queryGen(params);
					if (temp == null) {
						continue;
					}
					gene1 = Double.parseDouble(temp);
					Map<String, Object> params1 = new HashMap<String, Object>();
					params1.put("param_type", param_type);
					params1.put("param_name", result.get("param_name1"));
					params1.put("reagent_name", child.getReagent_name());
					String temp2 = queryGen(params1);
					if (temp2 == null) {
						continue;
					}
					gene2 = Double.parseDouble(temp2);
					double sub_pi = 0.25 / gene1 + 0.25 / gene2;
					pi = pi.multiply(new BigDecimal(sub_pi));
					sub_pi = new BigDecimal(sub_pi).setScale(4,
							BigDecimal.ROUND_HALF_UP).doubleValue();
					RdsJudicialPiInfoModel rdsJudicialPiInfoModel = new RdsJudicialPiInfoModel();
					rdsJudicialPiInfoModel.setSub_case_code(sub_case_code);
					rdsJudicialPiInfoModel.setParam_type(param_type);
					rdsJudicialPiInfoModel.setChild(childStr);
					rdsJudicialPiInfoModel.setParent(parentStr);
					rdsJudicialPiInfoModel.setGene1(StringUtils.complete0(
							new BigDecimal(gene1).setScale(4,
									BigDecimal.ROUND_HALF_UP).toString(), 4));
					rdsJudicialPiInfoModel.setGene2(StringUtils.complete0(
							new BigDecimal(gene2).setScale(4,
									BigDecimal.ROUND_HALF_UP).toString(), 4));
					rdsJudicialPiInfoModel.setFunction(result.get("function"));
					rdsJudicialPiInfoModel.setPi(StringUtils.complete0(
							(sub_pi + ""), 4));
					rdsJudicialCaseParamMapper
							.insertPiInfo(rdsJudicialPiInfoModel);

				} else {
					String function = result.get("function");
					Map<String, Object> params = new HashMap<String, Object>();
					String param_type = ateName;
					params.put("param_type", param_type);
					params.put("param_name", result.get("param_name"));
					params.put("reagent_name", child.getReagent_name());
					String temp = queryGen(params);
					if (temp == null) {
						continue;
					}
					gene1 = Double.parseDouble(temp);
					double sub_pi = 0;
						if (function.equals("1/p"))
						sub_pi = 1 / gene1;
					else if (function.equals("0.5/p"))
						sub_pi = 0.5 / gene1;
					else if (function.equals("0.25/p"))
						sub_pi = 0.25 / gene1;

					pi = pi.multiply(new BigDecimal(sub_pi));
					sub_pi = new BigDecimal(sub_pi).setScale(4,
							BigDecimal.ROUND_HALF_UP).doubleValue();
					RdsJudicialPiInfoModel rdsJudicialPiInfoModel = new RdsJudicialPiInfoModel();
					rdsJudicialPiInfoModel.setSub_case_code(sub_case_code);
					rdsJudicialPiInfoModel.setParam_type(param_type);
					rdsJudicialPiInfoModel.setChild(childStr);
					rdsJudicialPiInfoModel.setParent(parentStr);
					rdsJudicialPiInfoModel.setGene1(StringUtils.complete0(
							new BigDecimal(gene1).setScale(4,
									BigDecimal.ROUND_HALF_UP).toString(), 4));
					rdsJudicialPiInfoModel.setFunction(result.get("function"));
					rdsJudicialPiInfoModel.setPi(StringUtils.complete0(
							(sub_pi + ""), 4));
					rdsJudicialCaseParamMapper
							.insertPiInfo(rdsJudicialPiInfoModel);
				}
			}
		}
		return pi;
	}

	/**
	 * 3个人肯定亲权值
	 * 
	 * @param sub_case_code
	 * @param parent1
	 * @param parent2
	 * @param child
	 * @return
	 * @throws IllegalAccessException
	 */
	private BigDecimal calPI(String sub_case_code,
			RdsJudicialSampleResultModel parent1,
			RdsJudicialSampleResultModel parent2,
			RdsJudicialSampleResultModel child, int count, String unmatched_node)
			throws IllegalAccessException {
		RdsJudicialSampleResultModel mother = null;
		RdsJudicialSampleResultModel father = null;
		// 判断性别
		if ("X".equals(parent1.getRecord().get("AMEL"))
				|| "X".equals(parent1.getRecord().get("Amel"))) {
			mother = parent1;
			father = parent2;
		} else {
			mother = parent2;
			father = parent1;
		}
		BigDecimal pi = new BigDecimal(1);
		double gene1 = 0;
		double gene2 = 0;
		Map<String, Object> record = parent1.getRecord();
		Set<String> set = record.keySet();
		String[] nodes = unmatched_node.split(",");
		List<String> tempList = Arrays.asList(nodes);
		for (String ateName : set) {
			if (!tempList.contains(ateName)) {
				// 比对每一组基因位
				String fatherStr = (String) father.getRecord().get(ateName);
				String motherStr = (String) mother.getRecord().get(ateName);
				String childStr = (String) child.getRecord().get(ateName);
				Map<String, String> result = findFunction(motherStr, fatherStr,
						childStr);// {param_name=14, function=1/(2r)}
				if (result.size() == 3) {
					String function = result.get("function");
					Map<String, Object> params = new HashMap<String, Object>();
					String param_type = ateName;
					params.put("param_type", param_type);
					params.put("param_name", result.get("param_name"));
					params.put("reagent_name", child.getReagent_name());
					String temp = queryGen(params);
					if (temp == null) {
						continue;
					}
					gene1 = Double.parseDouble(temp);
					Map<String, Object> params1 = new HashMap<String, Object>();
					params1.put("param_type", param_type);
					params1.put("param_name", result.get("param_name1"));
					params1.put("reagent_name", child.getReagent_name());
					String temp2 = queryGen(params1);
					if (temp2 == null) {
						continue;
					}
					gene2 = Double.parseDouble(temp2);
					double sub_pi = 0;
					if (function.equals("1/[2(p+q)]")) {
						sub_pi = 0.5 / (gene1 + gene2);
					} else if (function.equals("1/(p+q)")) {
						sub_pi = 1 / (gene1 + gene2);
					}
					pi = pi.multiply(new BigDecimal(sub_pi));
					sub_pi = new BigDecimal(sub_pi).setScale(4,
							BigDecimal.ROUND_HALF_UP).doubleValue();
					RdsJudicialPiInfoModel rdsJudicialPiInfoModel = new RdsJudicialPiInfoModel();
					rdsJudicialPiInfoModel.setSub_case_code(sub_case_code);
					rdsJudicialPiInfoModel.setParam_type(param_type);
					rdsJudicialPiInfoModel.setChild(childStr);
					rdsJudicialPiInfoModel.setParent(motherStr);
					rdsJudicialPiInfoModel.setParent2(fatherStr);
					rdsJudicialPiInfoModel.setGene1(StringUtils.complete0(
							new BigDecimal(gene1).setScale(4,
									BigDecimal.ROUND_HALF_UP).toString(), 4));
					rdsJudicialPiInfoModel.setGene2(StringUtils.complete0(
							new BigDecimal(gene2).setScale(4,
									BigDecimal.ROUND_HALF_UP).toString(), 4));
					rdsJudicialPiInfoModel.setFunction(result.get("function"));
					rdsJudicialPiInfoModel.setPi(StringUtils.complete0(
							(sub_pi + ""), 4));
					rdsJudicialCaseParamMapper
							.insertPiInfo(rdsJudicialPiInfoModel);// 每一组亲权值数值
				} else {
					String function = result.get("function");
					Map<String, Object> params = new HashMap<String, Object>();
					String param_type = ateName;
					params.put("param_type", param_type);
					params.put("param_name", result.get("param_name"));
					params.put("reagent_name", child.getReagent_name());
					String temp = queryGen(params);
					if (temp == null) {
						continue;
					}
					gene1 = Double.parseDouble(temp);
					double sub_pi = 0;
					if (function.equals("1/p") || function.equals("1/q")
							|| function.equals("1/r"))
						sub_pi = 1 / gene1;
					else if (function.equals("1/(2p)")
							|| function.equals("1/(2q)")
							|| function.equals("1/(2r)"))
						sub_pi = 0.5 / gene1;
					pi = pi.multiply(new BigDecimal(sub_pi));
					sub_pi = new BigDecimal(sub_pi).setScale(4,
							BigDecimal.ROUND_HALF_UP).doubleValue();
					RdsJudicialPiInfoModel rdsJudicialPiInfoModel = new RdsJudicialPiInfoModel();
					rdsJudicialPiInfoModel.setSub_case_code(sub_case_code);
					rdsJudicialPiInfoModel.setParam_type(param_type);
					rdsJudicialPiInfoModel.setChild(childStr);
					rdsJudicialPiInfoModel.setParent(motherStr);
					rdsJudicialPiInfoModel.setParent2(fatherStr);
					rdsJudicialPiInfoModel.setGene1(StringUtils.complete0(
							new BigDecimal(gene1).setScale(4,
									BigDecimal.ROUND_HALF_UP).toString(), 4));
					rdsJudicialPiInfoModel.setFunction(result.get("function"));
					rdsJudicialPiInfoModel.setPi(StringUtils.complete0(
							(sub_pi + ""), 4));
					rdsJudicialCaseParamMapper
							.insertPiInfo(rdsJudicialPiInfoModel);
				}
			}
		}
		return pi;
	}

	/**
	 * 两个人肯定公式
	 * 
	 * @param parent
	 * @param child
	 * @return
	 */
	private Map<String, String> findFunction(String parent, String child) {
		Map<String, String> result = new HashMap<String, String>();
		String[] parents = parent.split(",");
		String[] childs = child.split(",");

		if (childs.length == 1) {
			result.put("param_name", childs[0]);
			if (parents.length == 1) {
				result.put("function", "1/p");
				return result;
			} else {
				result.put("function", "0.5/p");
				return result;
			}
		} else {
			if (parents.length == 1) {
				result.put("param_name", parents[0]);
				result.put("function", "0.5/p");
				return result;
			} else if (parent.equals(child)) {
				result.put("param_name", parents[0]);
				result.put("param_name1", parents[1]);
				result.put("function", "0.25/p+0.25/q");
				return result;
			} else {
				if (parents[0].equals(childs[0])
						|| parents[1].equals(childs[0]))
					result.put("param_name", childs[0]);
				else
					result.put("param_name", childs[1]);
				result.put("function", "0.25/p");
				return result;
			}
		}
	}

	/**
	 * 三个人肯定公式
	 * 
	 * @param mother
	 * @param father
	 * @param child
	 * @return
	 */
	private Map<String, String> findFunction(String mother, String father,
			String child) {
		Map<String, String> result = new HashMap<String, String>();
		String[] mothers = mother.split(",");
		String[] fathers = father.split(",");
		String[] childs = child.split(",");
		// 母亲和孩子点位完全相同 6种可能
		if (mother.equals(child)) {
			// 母亲该点位数据只有一个值2种可能
			if (mothers.length == 1) {
				if (fathers.length == 1) {
					result.put("function", "1/p");
					result.put("param_name", childs[0]);
					return result;
				} else {
					result.put("function", "1/(2p)");
					result.put("param_name", childs[0]);
					return result;
				}
				// 母亲该点位数据有两个值4种可能
			} else {
				result.put("param_name", childs[0]);
				result.put("param_name1", childs[1]);
				if (fathers.length == 1 || mother.equals(father)) {
					result.put("function", "1/(p+q)");
					return result;
				} else {
					result.put("function", "1/[2(p+q)]");
					return result;
				}
			}
		} else {
			// 母亲和孩子不同，8种可能
			// 母亲该点位数据只有一个值，三种可能
			if (mothers.length == 1) {
				if (fathers.length == 1) {
					result.put("param_name", fathers[0]);
					result.put("function", "1/q");
					return result;
				} else {
					if (childs[0].equals(mothers[0])) {
						result.put("param_name", childs[1]);
					} else {
						result.put("param_name", childs[0]);
					}
					result.put("function", "1/(2q)");
					return result;
				}
			} else if (childs.length == 1) {
				result.put("param_name", childs[0]);
				if (fathers.length == 1) {
					result.put("function", "1/q");
					return result;
				} else {
					result.put("function", "1/(2q)");
					return result;
				}
			} else if (fathers.length == 1) {
				result.put("param_name", fathers[0]);
				result.put("function", "1/r");
				return result;
			} else {
				if (childs[0].equals(mothers[0])
						|| childs[0].equals(mothers[1])) {
					result.put("param_name", childs[1]);
				} else {
					result.put("param_name", childs[0]);
				}
				result.put("function", "1/(2r)");
				return result;
			}
		}
	}

	private void sendPDFFile(String case_code, String ext_flag,
			String laboratory_no, String... sample_codes) throws Exception {
		for (String sample_code : sample_codes) {
			if (ext_flag.equals("N")) {
			Map<String, Object> map = new HashMap<>();
			String attachment_path=sample_code+".jpg";
			map.put("attachment_path", attachment_path);
			int a=6;
			map.put("attachment_type",  a);
			List<RdsJudicialCaseAttachmentModel> One=rdsJudicialCaseAttachmentService.getAttachMentOne(map);
			if(One.size() ==0){//商丘D开始案例
			Map<String, Object> params = new HashMap<>();
			params.put("sample_code", sample_code);
			params.put("ext_flag", "N");
			List<String> experiments = rdsJudicialSampleMapper
					.queryExperiment(params);
			for (String experiment : experiments) {
				File pdfFile = null;
				pdfFile = new File(getDataPath() + File.separatorChar
						+ laboratory_no + File.separatorChar + experiment
						+ File.separatorChar + sample_code + ".pdf");
				File lock = new File(getDataPath() + File.separatorChar
						+ laboratory_no + File.separatorChar + experiment
						+ File.separatorChar + sample_code + ".lock");
				if (!lock.exists()&&pdfFile.exists()) {
					try {
						List<File> jpgFile = RdsFileUtil.PDFToJpg(pdfFile, "N");
						// List<File> jpgFile =
						// RdsFileUtil.PDFchangToImage(pdfFile, "N");
						lock.createNewFile();
						rdsJudicialCaseAttachmentService.pdfAttachmentUpload(
								case_code, jpgFile);
					} catch (Exception e) {
						e.printStackTrace();
						throw new Exception("转发pdf附件失败");
					}
				}
			}
			}else {
			String case_id = pMapper.getCaseID(case_code);
			String	attachment_path1=One.get(0).getAttachment_path();
			if((case_id).equals(One.get(0).getCase_id())){
				continue;
			}else{
				
				if (case_id == null || "".equals(case_id)) {
					throw new Exception("Case is not exist");
				}
				RdsJudicialCaseAttachmentModel attachmentModel = new RdsJudicialCaseAttachmentModel();
				attachmentModel.setCase_id(case_id);
				attachmentModel.setCase_code(case_code);
				attachmentModel.setAttachment_date(new SimpleDateFormat("yyyy-MM-dd")
						.format(new Date()));
				attachmentModel.setId(UUIDUtil.getUUID());
				attachmentModel.setAttachment_path(attachment_path1);
				attachmentModel.setAttachment_type(6);
if(judicialCaseAttachmentMapper
						.insertAttachment(attachmentModel)==0)throw new Exception("Error!PFD Change fail");
			}
				}}
			if (ext_flag.equals("Y")) {
				//基本位点pdf未转换需要转换
				Map<String, Object> params0 = new HashMap<>();
				params0.put("sample_code", sample_code);
				params0.put("ext_flag", "N");
				List<String> experiments0 = rdsJudicialSampleMapper
						.queryExperiment(params0);
				for (String experiment : experiments0) {
					File pdfFile = null;
					pdfFile = new File(getDataPath() + File.separatorChar
							+ laboratory_no + File.separatorChar + experiment
							+ File.separatorChar + sample_code + ".pdf");
					File lock = new File(getDataPath() + File.separatorChar
							+ laboratory_no + File.separatorChar + experiment
							+ File.separatorChar + sample_code + ".lock");
					if (!lock.exists()&& pdfFile.exists()) {
						try {
							List<File> jpgFile = RdsFileUtil.PDFToJpg(pdfFile, "N");
							// List<File> jpgFile =
							// RdsFileUtil.PDFchangToImage(pdfFile, "N");
							lock.createNewFile();
							rdsJudicialCaseAttachmentService.pdfAttachmentUpload(
									case_code, jpgFile);
						} catch (Exception e) {
							e.printStackTrace();
							throw new Exception("转发pdf附件失败");
						}
					}
				}
				Map<String, Object> map = new HashMap<>();
				String attachment_path=sample_code+"ext.jpg";
				map.put("attachment_path", attachment_path);
				int a=6;
				map.put("attachment_type",  a);
				List<RdsJudicialCaseAttachmentModel> One=	rdsJudicialCaseAttachmentService.getAttachMentOne(map);
				if(One.size() ==0){
				Map<String, Object> params = new HashMap<>();
				params.put("sample_code", sample_code);
				params.put("ext_flag", "Y");
				List<String> experiments = rdsJudicialSampleMapper.queryExperiment(params);
				for (String experiment : experiments) {
					File pdfFile = null;
					pdfFile = new File(getDataPath() + File.separatorChar
							+ laboratory_no + File.separatorChar + experiment
							+ File.separatorChar + sample_code + ".pdf");
					File lock = new File(getDataPath() + File.separatorChar
							+ laboratory_no + File.separatorChar + experiment
							+ File.separatorChar + sample_code + ".lock");
					if (!lock.exists()) {
						try {
							List<File> jpgFile = RdsFileUtil.PDFToJpg(pdfFile,
									"Y");
							// List<File> jpgFile =
							// RdsFileUtil.PDFchangToImage(pdfFile, "Y");
							lock.createNewFile();
							rdsJudicialCaseAttachmentService
									.pdfAttachmentUpload(case_code, jpgFile);
						} catch (Exception e) {
							e.printStackTrace();
							throw new Exception("转发pdf附件失败");
						}
					}
				}
			}else{
				String case_id = pMapper.getCaseID(case_code);
				String	attachment_path1=One.get(0).getAttachment_path();
				if((case_id).equals(One.get(0).getCase_id())){
					continue;
				}else{
				
				if (case_id == null || "".equals(case_id)) {
					throw new Exception("Case is not exist");
				}
				RdsJudicialCaseAttachmentModel attachmentModel = new RdsJudicialCaseAttachmentModel();
				attachmentModel.setCase_id(case_id);
				attachmentModel.setCase_code(case_code);
				attachmentModel.setAttachment_date(new SimpleDateFormat("yyyy-MM-dd")
						.format(new Date()));
				attachmentModel.setId(UUIDUtil.getUUID());
				attachmentModel.setAttachment_path(attachment_path1);
				attachmentModel.setAttachment_type(6);
if(judicialCaseAttachmentMapper
						.insertAttachment(attachmentModel)==0)throw new Exception("Error!PFD Change fail");
			}
				}}
		}
	}

	private int currentCountUnmatchedNode(String sub_case_code) {
		Map<String, Object> map = rdsJudicialSampleMapper
				.queryCurrentCountUnmatchedNode(sub_case_code);
		if (map == null) {
			return 0;
		}
		int unmatchedCount = (Integer) map.get("unmatched_count");
		return unmatchedCount;
	}

	private String queryNeedExt(String sub_case_code) {
		Map<String, Object> map = rdsJudicialSampleMapper
				.queryNeedExt(sub_case_code);
		if (map == null) {
			return "";
		}
		String need_ext = (String) map.get("need_ext");
		return need_ext;
	}
	
	/**
	 * 两个人肯定亲权值单个基因座
	 * 
	 * @param sub_case_code
	 * @param parent1
	 * @param child
	 * @return
	 * @throws IllegalAccessException
	 */
	private BigDecimal calPIForOne(String sub_case_code,
			RdsJudicialSampleResultModel parent1,
			RdsJudicialSampleResultModel child, String gen, String unmatched_node)
			throws IllegalAccessException {
		BigDecimal pi = new BigDecimal(1);
		double gene1 = 0;
		double gene2 = 0;
		String[] nodes = unmatched_node.split(",");
		List<String> tempList = Arrays.asList(nodes);
			if (!tempList.contains(gen)) {
				String parentStr = (String) parent1.getRecord().get(gen);
				String childStr = (String) child.getRecord().get(gen);
				Map<String, String> result = findFunction(parentStr, childStr);
				if (result.size() == 3) {
					Map<String, Object> params = new HashMap<String, Object>();
					String param_type = gen;
					params.put("param_type", param_type);
					params.put("param_name", result.get("param_name"));
					params.put("reagent_name", child.getReagent_name());
					String temp = queryGen(params);
					gene1 = Double.parseDouble(temp);
					Map<String, Object> params1 = new HashMap<String, Object>();
					params1.put("param_type", param_type);
					params1.put("param_name", result.get("param_name1"));
					params1.put("reagent_name", child.getReagent_name());
					String temp2 = queryGen(params1);
					gene2 = Double.parseDouble(temp2);
					double sub_pi = 0.25 / gene1 + 0.25 / gene2;
					pi = pi.multiply(new BigDecimal(sub_pi));


				} else {
					String function = result.get("function");
					Map<String, Object> params = new HashMap<String, Object>();
					String param_type = gen;
					params.put("param_type", param_type);
					params.put("param_name", result.get("param_name"));
					params.put("reagent_name", child.getReagent_name());
					String temp = queryGen(params);
					gene1 = Double.parseDouble(temp);
					double sub_pi = 0;
						if (function.equals("1/p"))
						sub_pi = 1 / gene1;
					else if (function.equals("0.5/p"))
						sub_pi = 0.5 / gene1;
					else if (function.equals("0.25/p"))
						sub_pi = 0.25 / gene1;

					pi = pi.multiply(new BigDecimal(sub_pi));
					
				}
			}else{
				// 取出不匹配的基因位点然后用否定算法出pi值
				String parentStr = (String) parent1.getRecord().get(gen);
				String childStr = (String) child.getRecord().get(gen);

				AlgMain main = new AlgMain(childStr, parentStr);
				AlgReturnValueModel model = main.getAlgResult();

				System.out.println("    " + childStr + " " + parentStr + "--"
						+ model.getStep() + ", " + model.getPossibility()
						+ "   function:" + model.getFunction());

				double μ = 0.0;
				if (parent1.getSample_call().toString().equals("father")) {
					// 如果是父亲平均突变率μ=0.002
					μ = 0.002;
				} else {
					// 如果是母亲 平均突变率μ=0.0005
					μ = 0.0005;
				}
				double n = model.getStep();
				double A1 = 0.0;
				double A2 = 0.0;

				String[] childs = childStr.split(",");
				String[] parents = parentStr.split(",");
				if (childs.length == 1) {
					A1 = Double.parseDouble(childs[0]);
					A2 = Double.parseDouble(childs[0]);
				} else {
					A1 = Double.parseDouble(childs[0]);
					A2 = Double.parseDouble(childs[1]);
				}
				Map<String, Object> params = new HashMap<String, Object>();
				String function = model.getFunction();
				String param_type = gen;
				params.put("param_type", param_type);
				params.put("param_name", A1);
				params.put("reagent_name", child.getReagent_name());
				String temp = queryGen(params);

				gene1 = Double.parseDouble(temp);
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.put("param_type", param_type);
				params1.put("param_name", A2);
				params1.put("reagent_name", child.getReagent_name());
				String temp2 = queryGen(params1);

				gene2 = Double.parseDouble(temp2);
				double sub_pi = 0;
				double mun = Math.pow(10, (Math.ceil(n) - 1));
				if (function.equals("μ/8p*10(n-1)")) {
					sub_pi = μ / (8 * gene1 * mun);
				} else if (function.equals("μ/8q*10(n-1)")) {
					sub_pi = μ / (8 * gene2 * mun);
				} else if (function.equals("μ/4p*10(n-1)")) {
					sub_pi = μ / (4 * gene1 * mun);
				} else if (function.equals("μ(p+q)/8pq*10(n-1)")) {
					sub_pi = μ * (gene1 + gene2) / (8 * gene1 * gene2 * mun);
				} else if (function.equals("μ/4q*10(n-1)")) {
					sub_pi = μ / (4 * gene2 * mun);
				} else if (function.equals("μ(2p+q)/8pq*10(n-1)")) {
					sub_pi = μ * (2 * gene1 + gene2)
							/ (8 * gene1 * gene2 * mun);
				} else if (function.equals("μ(p+2q)/8pq*10(n-1)")) {
					sub_pi = μ * (gene1 + 2 * gene2)
							/ (8 * gene1 * gene2 * mun);
				} else if (function.equals("μ(p+q)/4pq*10(n-1)")) {
					sub_pi = μ * (gene1 + gene2) / (4 * gene1 * gene2 * mun);
				} else if (function.equals("μ/2p*10(n-1)")) {
					sub_pi = μ / (2 * gene1 * mun);
				}
				pi = pi.multiply(new BigDecimal(sub_pi));
			
			}
		return pi;
	}
	/**
	 * 三联体 单个基因pi值计算，用于去除重复
	 * @param sub_case_code
	 * @param parent1
	 * @param parent2
	 * @param child
	 * @param count
	 * @param unmatched_node
	 * @return
	 * @throws IllegalAccessException
	 */
	private BigDecimal calPiForTwo(String sub_case_code,
			RdsJudicialSampleResultModel parent1,
			RdsJudicialSampleResultModel parent2,
			RdsJudicialSampleResultModel child, String gen, String unmatched_node)
			throws IllegalAccessException {
		RdsJudicialSampleResultModel mother = null;
		RdsJudicialSampleResultModel father = null;
		// 判断性别
		if ("X".equals(parent1.getRecord().get("AMEL"))
				|| "X".equals(parent1.getRecord().get("Amel"))) {
			mother = parent1;
			father = parent2;
		} else {
			mother = parent2;
			father = parent1;
		}
		BigDecimal pi = new BigDecimal(1);
		double gene1 = 0;
		double gene2 = 0;
		Map<String, Object> record = parent1.getRecord();
		Set<String> set = record.keySet();
		String[] nodes = unmatched_node.split(",");
		List<String> tempList = Arrays.asList(nodes);
			if (!tempList.contains(gen)) {
				// 比对每一组基因位
				String fatherStr = (String) father.getRecord().get(gen);
				String motherStr = (String) mother.getRecord().get(gen);
				String childStr = (String) child.getRecord().get(gen);
				Map<String, String> result = findFunction(motherStr, fatherStr,
						childStr);// {param_name=14, function=1/(2r)}
				if (result.size() == 3) {
					String function = result.get("function");
					Map<String, Object> params = new HashMap<String, Object>();
					String param_type = gen;
					params.put("param_type", param_type);
					params.put("param_name", result.get("param_name"));
					params.put("reagent_name", child.getReagent_name());
					String temp = queryGen(params);
					gene1 = Double.parseDouble(temp);
					Map<String, Object> params1 = new HashMap<String, Object>();
					params1.put("param_type", param_type);
					params1.put("param_name", result.get("param_name1"));
					params1.put("reagent_name", child.getReagent_name());
					String temp2 = queryGen(params1);
					gene2 = Double.parseDouble(temp2);
					double sub_pi = 0;
					if (function.equals("1/[2(p+q)]")) {
						sub_pi = 0.5 / (gene1 + gene2);
					} else if (function.equals("1/(p+q)")) {
						sub_pi = 1 / (gene1 + gene2);
					}
					pi = pi.multiply(new BigDecimal(sub_pi));
				} else {
					String function = result.get("function");
					Map<String, Object> params = new HashMap<String, Object>();
					String param_type = gen;
					params.put("param_type", param_type);
					params.put("param_name", result.get("param_name"));
					params.put("reagent_name", child.getReagent_name());
					String temp = queryGen(params);
					gene1 = Double.parseDouble(temp);
					double sub_pi = 0;
					if (function.equals("1/p") || function.equals("1/q")
							|| function.equals("1/r"))
						sub_pi = 1 / gene1;
					else if (function.equals("1/(2p)")
							|| function.equals("1/(2q)")
							|| function.equals("1/(2r)"))
						sub_pi = 0.5 / gene1;
					pi = pi.multiply(new BigDecimal(sub_pi));
				}
			}
		return pi;
	}
}
