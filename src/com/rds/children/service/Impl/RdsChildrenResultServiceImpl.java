package com.rds.children.service.Impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rds.activiti.mapper.RdsActivitiJudicialMapper;
import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.children.mapper.RdsChildrenAgentiaMapper;
import com.rds.children.mapper.RdsChildrenRegisterMapper;
import com.rds.children.mapper.RdsChildrenResultMapper;
import com.rds.children.mapper.RdsChildrenSampleReceiveMapper;
import com.rds.children.model.RdsChildrenCaseInfoModel;
import com.rds.children.model.RdsChildrenCaseLocusModel;
import com.rds.children.model.RdsChildrenCaseResultModel;
import com.rds.children.model.RdsChildrenResponse;
import com.rds.children.service.RdsChildrenResultService;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.upc.model.RdsUpcUserModel;

@Service
@Transactional
public class RdsChildrenResultServiceImpl implements RdsChildrenResultService {

	public static final Map<String, Integer> map20 = new HashMap<>();
	static {
		map20.put("D19S433", 1);
		map20.put("D5S818", 2);
		map20.put("D21S11", 3);
		map20.put("D18S51", 4);
		map20.put("D6S1043", 5);
		map20.put("D3S1358", 6);
		map20.put("D13S317", 7);
		map20.put("D7S820", 8);
		map20.put("D16S539", 9);
		map20.put("CSF1PO", 10);
		map20.put("Penta D", 11);
		map20.put("vWA", 12);
		map20.put("D8S1179", 13);
		map20.put("TPOX", 14);
		map20.put("Penta E", 15);
		map20.put("TH01", 16);
		map20.put("D12S391", 17);
		map20.put("D2S1338", 18);
		map20.put("FGA", 19);
	}
	@Autowired
	private RdsChildrenRegisterMapper registerMapper;

	@Autowired
	private RdsChildrenAgentiaMapper agMapper;

	@Autowired
	private RdsChildrenResultMapper RdsChildrenResultMapper;

	@Setter
	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;

	@Autowired
	private RdsChildrenSampleReceiveMapper rdsChildrenSampleReceiveMapper;

	@Autowired
	private RdsActivitiJudicialMapper rdsActivitiJudicialMapper;

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String ATTACHMENT_PATH = PropertiesUtils.readValue(
			FILE_PATH, "children_result_file");

	@Override
	public RdsChildrenResponse getResultInfo(Map<String, Object> params) {
		RdsChildrenResponse response = new RdsChildrenResponse();
		List<RdsChildrenCaseResultModel> resultModels = RdsChildrenResultMapper
				.getResultInfo(params);
		int count = RdsChildrenResultMapper.countResultInfo(params);
		response.setCount(count);
		response.setItems(resultModels);
		return response;
	}

	@Override
	public Map<String, Object> addCaseResult(MultipartFile[] files,
			Map<String, Object> params) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 返回消息
		String message = "";
		if (files.length == 1) {
			MultipartFile file = files[0];
			String name = file.getOriginalFilename();
			String attachmentPath = ATTACHMENT_PATH + new Date().getTime()
					+ File.separatorChar + name.split(".rar")[0];
			if (!RdsFileUtil.getState(attachmentPath)) {
				try {
					// 上传文件
					RdsFileUtil.fileUpload(attachmentPath + File.separatorChar
							+ name, file.getInputStream());
					// 解压文件
					RdsFileUtil.unrar(attachmentPath + File.separatorChar
							+ name, attachmentPath + File.separatorChar);
					// 获取所有文件
					String[] txtFiles = RdsFileUtil.getAllFile(attachmentPath);
					// 判断案例是否已登记
					for (String txtFile : txtFiles) {
						params.put("case_code", txtFile.split(".txt")[0]);
						if (txtFile.endsWith(".txt")) {
							if (registerMapper.exsitCaseCode(params) == 0) {
								message += txtFile.split(".txt")[0]
										+ "不存在对应案例;<br>";
							}
						}
					}
					// 如果有没有登记的案例就不让上传
					if (!"".equals(message)) {
						RdsFileUtil.delAllFile(ATTACHMENT_PATH);
						map.put("result", false);
						map.put("success", true);
						map.put("message", message);
						return map;
					}
					for (String txtFile : txtFiles) {
						if (txtFile.endsWith(".txt")) {
							// 获取txt样本编号
							String case_code = txtFile.split(".txt")[0];
							RdsChildrenCaseInfoModel caseInfo = RdsChildrenResultMapper
									.getCaseInfoModel(case_code);
							if (5 > caseInfo.getVerify_state()) {
								map.put("result", false);
								map.put("success", true);
								map.put("message", case_code + "案例状态有误，请查看！");
								return map;
							}
							params.put("case_id", caseInfo.getCase_id());
							// 更新实验所用试剂
							if (!RdsChildrenResultMapper
									.updateCaseAgentia(params)) {
								map.put("result", false);
								map.put("success", true);
								map.put("message", "案例状态更新失败，请联系管理员");
								return map;
							}
							// 根据试剂名，查询位点信息
							List<String> locusName = agMapper
									.getLocusName(params.get("agentia_name")
											.toString());

							if (RdsFileUtil.getState(attachmentPath
									+ File.separatorChar + txtFile)) {
								List<String> textlines = FileUtils
										.readLines(new File(attachmentPath
												+ File.separatorChar + txtFile));
								// 如果试剂结果长度不对不能输入，判断试剂是否匹配
								if (textlines.size() - 1 != locusName.size()) {
									message += case_code + "案例实验试剂有误；<br>";
									map.put("result", false);
									map.put("success", true);
									map.put("message", message);
									return map;
								}
								String value = "";
								List<RdsChildrenCaseLocusModel> locusList = new ArrayList<RdsChildrenCaseLocusModel>();
								RdsChildrenCaseLocusModel locusModel;
								// 校验数据用
								String sb = "";
								String[] strTemp = new String[21];
								for (int i = 0; i < textlines.size(); i++) {
									if (i != 0) {

										String[] strs = textlines.get(i).split(
												"\t");
										// 位点名称不对就报错。
										if (!locusName.contains(strs[3].trim())) {
											message += case_code
													+ "案例实验试剂有误；<br>";
											map.put("result", false);
											map.put("success", true);
											map.put("message", message);
											return map;
										}
										// 位点信息错误
										if (strs[5].toUpperCase().equals("OL")
												|| strs[5].isEmpty()
												|| strs[6].toUpperCase()
														.equals("OL")) {
											message += case_code
													+ "案例实验数据有误；<br>";
											map.put("result", false);
											map.put("success", true);
											map.put("message", message);
											return map;
										}
										// 性别位点错误
										if (strs[5].equals("Y")) {
											message += case_code
													+ "案例性别位点错误<br>";
											map.put("result", false);
											map.put("success", true);
											map.put("message", message);
											return map;
										}
										// 性别位点和登记信息不一致
										if (strs[6].equals("Y")) {
											if (0 == caseInfo.getChild_sex()) {
												message += case_code
														+ "案例性别位点错误<br>";
												map.put("result", false);
												map.put("success", true);
												map.put("message", message);
												return map;
											}
										}
										if (StringUtils.isNotEmpty(strs[6])) {
											value += strs[5] + "," + strs[6];
										} else {
											value += strs[5] + "," + strs[5];
										}
										locusModel = new RdsChildrenCaseLocusModel(
												caseInfo.getCase_id(), strs[3],
												value);
										locusList.add(locusModel);
										value = "";
										if (map20.get(strs[3]) != null) {
											String atestr = strs[5]
													+ (strs[6].isEmpty() ? strs[5]
															: strs[6]);
											int j = map20.get(strs[3]);
											strTemp[j - 1] = atestr;
										}
									}
								}
								for (String str : strTemp) {
									if (str != null) {
										sb = sb + str;
									}
								}
								params.put("resultstr", sb);
								Map<String, Object> resultMap = new HashMap<>();
								resultMap.put("case_code", case_code);
								resultMap.put("resultstr", sb);
								// 校验历史数据是否存在
								List<String> lists = RdsChildrenResultMapper
										.queryOtherRecord(resultMap);
								if (lists.size() > 0) {
									String returnStr = "";
									for (String string : lists) {
										returnStr += case_code + "和" + string
												+ "数据一致;<br>";
									}
									map.put("result", false);
									map.put("success", true);
									map.put("message", returnStr);
									return map;
								}

								RdsChildrenCaseResultModel resultModel = new RdsChildrenCaseResultModel();
								resultModel.setCase_id(caseInfo.getCase_id());
								resultModel.setCase_code(case_code);
								resultModel.setResult_id(UUIDUtil.getUUID());
								if (RdsChildrenResultMapper
										.exsitCase_code(case_code) > 0) {
									RdsChildrenResultMapper
											.deleteCaseResult(caseInfo
													.getCase_id());
									RdsChildrenResultMapper
											.deleteCaseLocus(caseInfo
													.getCase_id());
									RdsChildrenResultMapper
											.deleteCaseHistory(caseInfo
													.getCase_code());
								}
								// 没问题则插入历史位点信息记录
								RdsChildrenResultMapper
										.insertHistoryRecord(resultMap);
								// 插入结果记录
								RdsChildrenResultMapper
										.insertResult(resultModel);
								// 插入结果值
								RdsChildrenResultMapper
										.insertCaseLocus(locusList);
								/** 流程加入start **/
								// 判断当前为实验状态时才更新流程
								if (5 == caseInfo.getVerify_state()) {
									Map<String, Object> variables = new HashMap<>();
									variables.put("isback", 0);
									String taskId = rdsActivitiJudicialMapper
											.getChildCaseTask(case_code);
									if (taskId == null) {
										map.put("success", true);
										map.put("result", false);
										map.put("message", "操作失败，请联系管理员！");
										return map;
									}
									// 流程加入
									rdsActivitiJudicialService
											.runByChildCaseCode(case_code,
													variables,
													(RdsUpcUserModel) params
															.get("user"));
									Map<String, Object> verify_state = new HashMap<>();
									verify_state.put("case_code", case_code);
									verify_state.put("verify_state", 6);
									// 更新基本信息
									rdsChildrenSampleReceiveMapper
											.updateCaseState(verify_state);
								}
								/** 流程加入end **/
								message += case_code + "上传成功；<br>";
							}
						}
					}
					// RdsFileUtil.delAllFile(ATTACHMENT_PATH);
					map.put("success", true);
					map.put("result", true);
					map.put("message", message);
					return map;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		map.put("message", "文件上传有误！");
		return map;
	}

	@Override
	public Map<String, Object> getLoucsInfo(Map<String, Object> params) {
		List<RdsChildrenCaseLocusModel> caselocus = RdsChildrenResultMapper
				.getLoucsInfo(params);
		params.clear();
		params.put("data", caselocus);
		return params;
	}

	@Override
	public Map<String, Object> uploadSampleByIdentify(Map<String, Object> params)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 根据试剂名，查询位点信息
		List<String> locusName = agMapper.getLocusName(params.get(
				"agentia_name").toString());
		String sample_codes = params.get("sample_codes").toString();
		String[] list = sample_codes.split(",");
		for (String string : list) {
			params.put("sample_code", string);
			// 更新实验所用试剂
			if (!RdsChildrenResultMapper.updateCaseAgentia(params)) {
				map.put("result", false);
				map.put("success", true);
				map.put("message", "案例状态更新失败，请联系管理员");
				return map;
			}
			map = sampleUpload(string, locusName,
					(RdsUpcUserModel) params.get("user"));
			if (!(boolean) map.get("result"))
				return map;
		}
		return map;
	}

	private Map<String, Object> sampleUpload(String string,
			List<String> locusName, RdsUpcUserModel user) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询亲子鉴定实验数据
		List<Map<String, String>> sampleList = RdsChildrenResultMapper
				.queryIdentifySample(string);
		// 查询儿童基因库案例编号并校验
		List<Map<String, String>> case_info = RdsChildrenResultMapper
				.queryCaseCodeBySampleCode(string);
		if (case_info.size() <= 0) {
			map.put("result", false);
			map.put("success", true);
			map.put("message", string + "样本不存在该案例，请查看！");
			return map;
		} else if (case_info.size() > 1) {
			map.put("result", false);
			map.put("success", true);
			map.put("message", string + "样本存在多个儿童基因库案例，请查看！");
			return map;
		}
		// 亲子鉴定数据判断
		if (sampleList.size() <= 0) {
			map.put("result", false);
			map.put("success", true);
			map.put("message", string + "样本亲子鉴定未上传实验数据，请查看！");
			return map;
		} else if (sampleList.size() >= 1) {
			boolean flag = true;
			// 判断是否存在不一样的实验数据
			Map<String, String> temp = new HashMap<>();
			// 亲子鉴定实验数据只有一个
			if (sampleList.size() == 1) {
				temp = sampleList.get(0);
				flag = true;
			} else {
				// 遍历判断结果集和价位点信息存在一样数据时
				for (int i = 0; i < sampleList.size(); i++) {
					for (int j = i + 1; j < sampleList.size(); j++) {
						if (!sampleList.get(i).get("resultstr")
								.equals(sampleList.get(j).get("resultstr"))
								&& sampleList
										.get(i)
										.get("ext_flag")
										.equals(sampleList.get(j).get(
												"ext_flag"))) {
							flag = false;
						}
					}
				}
			}
			// 存在不一样数据直接给出提示
			if (!flag) {
				map.put("result", false);
				map.put("success", true);
				map.put("message", string + "样本亲子鉴定存在不同实验数据，请查看！");
				return map;
			} else {
				// 初始化实验数据
				temp = sampleList.get(0);
				// 判断存在突变数据则加载突变实验数据
				for (Map<String, String> map2 : sampleList) {
					if ("Y".equals(map2.get("ext_flag")))
						temp = map2;
				}
				Map<String, Object> params = new HashMap<String, Object>();
				// 实验编号
				String experiment_no = temp.get("experiment_no");
				// 结果集
				String resultstr = temp.get("resultstr");
				// 案例编号
				String case_code = case_info.get(0).get("case_code");
				// 案例id
				String case_id = case_info.get(0).get("case_id");
				params.put("resultstr", resultstr);
				params.put("case_code", case_code);
				params.put("experiment_no", experiment_no);
				params.put("sample_code", string);
				// 校验历史数据是否存在
				List<String> lists = RdsChildrenResultMapper
						.queryOtherRecord(params);
				if (lists.size() > 0) {
					String returnStr = "";
					for (String string3 : lists) {
						returnStr += case_code + "和" + string3 + "数据一致;<br>";
					}
					map.put("result", false);
					map.put("success", true);
					map.put("message", returnStr);
					return map;
				}
				// 查询亲子鉴定对应位点数据
				List<Map<String, String>> listRecord = RdsChildrenResultMapper
						.queryChildrenRecordData(params);
				RdsChildrenCaseLocusModel locusModel;
				List<RdsChildrenCaseLocusModel> locusList = new ArrayList<RdsChildrenCaseLocusModel>();

				// 查询案例信息
				RdsChildrenCaseInfoModel caseInfo = RdsChildrenResultMapper
						.getCaseInfoModel(case_code);
				// 获取亲子鉴定实验数据
				for (String name : locusName) {
					for (Map<String, String> map2 : listRecord) {
						if (name.equals(map2.get("name"))) {
							// 性别位点判断是否正确
							if ("AMEL".equals(map2.get("name").toUpperCase())) {
								if (map2.get("value").contains("Y")
										&& (0 == caseInfo.getChild_sex())) {
									map.put("result", false);
									map.put("success", true);
									map.put("message", "样本" + string
											+ "试剂和亲子鉴定性别位点有误，请查看！");
									return map;
								}
								if (!map2.get("value").contains("Y")
										&& (1 == caseInfo.getChild_sex())) {
									map.put("result", false);
									map.put("success", true);
									map.put("message", "样本" + string
											+ "试剂和亲子鉴定性别位点有误，请查看！");
									return map;
								}
							}
							// 判断试剂是否选择正确
							if ("".equals(map2.get("value"))
									|| null == map2.get("value")) {
								map.put("result", false);
								map.put("success", true);
								map.put("message", "样本" + string
										+ "试剂和亲子鉴定位点有误，请查看！");
								return map;
							}
							// 判断如果位点值只有一个
							if (map2.get("value").split(",").length == 1)
								locusModel = new RdsChildrenCaseLocusModel(
										case_id,
										name,
										map2.get("value").split(",")[0]
												+ ","
												+ map2.get("value").split(",")[0]);
							else
								locusModel = new RdsChildrenCaseLocusModel(
										case_id, name, map2.get("value"));
							locusList.add(locusModel);
						}
					}
				}
				// 判断是否取到正确的位点值
				if (locusList.size() != locusName.size()) {
					map.put("result", false);
					map.put("success", true);
					map.put("message", "样本" + string + "试剂和亲子鉴定位点有误，请查看！");
					return map;
				}
				RdsChildrenCaseResultModel resultModel = new RdsChildrenCaseResultModel();
				resultModel.setCase_id(case_id);
				resultModel.setCase_code(case_code);
				resultModel.setResult_id(UUIDUtil.getUUID());
				// 删除原来存的值，先删后曾
				RdsChildrenResultMapper.deleteCaseResult(case_id);
				RdsChildrenResultMapper.deleteCaseLocus(case_id);
				RdsChildrenResultMapper.deleteCaseHistory(case_code);

				// 没问题则插入历史位点信息记录
				RdsChildrenResultMapper.insertHistoryRecord(params);
				// 插入结果记录
				RdsChildrenResultMapper.insertResult(resultModel);
				// 插入结果值
				RdsChildrenResultMapper.insertCaseLocus(locusList);
				/** 流程加入start **/
				// 判断当前为实验状态时才更新流程
				if (5 == caseInfo.getVerify_state()) {
					Map<String, Object> variables = new HashMap<>();
					variables.put("isback", 0);
					String taskId = rdsActivitiJudicialMapper
							.getChildCaseTask(case_code);
					if (taskId == null) {
						map.put("success", true);
						map.put("result", false);
						map.put("message", "操作失败，请联系管理员！");
						return map;
					}
					// 流程加入
					rdsActivitiJudicialService.runByChildCaseCode(case_code,
							variables, user);
					Map<String, Object> verify_state = new HashMap<>();
					verify_state.put("case_code", case_code);
					verify_state.put("verify_state", 6);
					// 更新基本信息
					rdsChildrenSampleReceiveMapper
							.updateCaseState(verify_state);
				}
				/** 流程加入end **/

			}
		}
		map.put("result", true);
		map.put("success", true);
		map.put("message", "亲子鉴定实验数据导入成功！");
		return map;
	}

	@Override
	public Map<String, Object> addCaseResultByHand(Map<String, Object> params)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String case_code = params.get("case_code").toString();
		RdsChildrenCaseInfoModel caseInfo = RdsChildrenResultMapper
				.getCaseInfoModel(case_code);
		// 根据试剂名，查询位点信息
		List<String> locusName = agMapper.getLocusName(params.get(
				"agentia_name").toString());
		String[] strTemp = new String[21];
		// 校验数据用
		String sb = "";
		// 位点值列表
		List<RdsChildrenCaseLocusModel> locusList = new ArrayList<RdsChildrenCaseLocusModel>();
		RdsChildrenCaseLocusModel locusModel;
		// 遍历位点信息
		for (String string : locusName) {
			// 判断位点值有误
			if (params.get(string).toString().isEmpty()
					|| params.get(string).toString().contains("OL")
					|| params.get(string).toString().contains("oL")
					|| params.get(string).toString().contains("Ol")) {
				map.put("result", false);
				map.put("success", true);
				map.put("message", "案例实验数据有误；<br>");
				return map;
			}
			//性别位点判断
			if(string.toUpperCase().equals("AMEL")){
				if(params.get(string).toString().toUpperCase().contains("Y")){
					if(caseInfo.getChild_sex()==0){
						map.put("result", false);
						map.put("success", true);
						map.put("message", "案例性别位点有误；<br>");
						return map;
					}
				}else{
					if(caseInfo.getChild_sex()==1){
						map.put("result", false);
						map.put("success", true);
						map.put("message", "案例性别位点有误；<br>");
						return map;
					}
				}
			}
			String atestr = "";
			// 插入比对历史数据
			if (map20.get(string) != null) {
				atestr = params.get(string).toString().split(",").length == 1 ? params
						.get(string).toString()
						+ params.get(string).toString().split(",")[0] : params
						.get(string).toString();
				int j = map20.get(string);
				strTemp[j - 1] = atestr;
			}
			// 位点值信息
			locusModel = new RdsChildrenCaseLocusModel(
					caseInfo.getCase_id(),
					string,
					params.get(string).toString().split(",").length == 1 ? params
							.get(string).toString()
							+ params.get(string).toString().split(",")[0]
							: params.get(string).toString());
			locusList.add(locusModel);
		}
		for (String str : strTemp) {
			if (str != null) {
				sb = sb + str;
			}
		}
		// 查询历史数据
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("case_code", case_code);
		resultMap.put("resultstr", sb);
		// 校验历史数据是否存在
		List<String> lists = RdsChildrenResultMapper
				.queryOtherRecord(resultMap);
		if (lists.size() > 0) {
			String returnStr = "";
			for (String string : lists) {
				returnStr += case_code + "和" + string + "数据一致;<br>";
			}
			map.put("result", false);
			map.put("success", true);
			map.put("message", returnStr);
			return map;
		}
		params.put("case_id", caseInfo.getCase_id());
		// 更新实验所用试剂
		if (!RdsChildrenResultMapper
				.updateCaseAgentia(params)) {
			map.put("result", false);
			map.put("success", true);
			map.put("message", "案例状态更新失败，请联系管理员");
			return map;
		}
		// 插入结果信息
		RdsChildrenCaseResultModel resultModel = new RdsChildrenCaseResultModel();
		resultModel.setCase_id(caseInfo.getCase_id());
		resultModel.setCase_code(case_code);
		resultModel.setResult_id(UUIDUtil.getUUID());
		// 判断案例编号是否存在
		if (RdsChildrenResultMapper.exsitCase_code(case_code) > 0) {
			// 删除原来的数据
			RdsChildrenResultMapper.deleteCaseResult(caseInfo.getCase_id());
			RdsChildrenResultMapper.deleteCaseLocus(caseInfo.getCase_id());
			// 删除历史数据
			RdsChildrenResultMapper.deleteCaseHistory(caseInfo.getCase_code());
		}
		// 没问题则插入历史位点信息记录
		RdsChildrenResultMapper.insertHistoryRecord(resultMap);
		// 插入结果记录
		RdsChildrenResultMapper.insertResult(resultModel);
		// 插入结果值
		RdsChildrenResultMapper.insertCaseLocus(locusList);
		/** 流程加入start **/
		// 判断当前为实验状态时才更新流程
		if (5 == caseInfo.getVerify_state()) {
			Map<String, Object> variables = new HashMap<>();
			variables.put("isback", 0);
			String taskId = rdsActivitiJudicialMapper
					.getChildCaseTask(case_code);
			if (taskId == null) {
				map.put("success", true);
				map.put("result", false);
				map.put("message", "操作失败，请联系管理员！");
				return map;
			}
			// 流程加入
			rdsActivitiJudicialService.runByChildCaseCode(case_code, variables,
					(RdsUpcUserModel) params.get("user"));
			Map<String, Object> verify_state = new HashMap<>();
			verify_state.put("case_code", case_code);
			verify_state.put("verify_state", 6);
			// 更新基本信息
			rdsChildrenSampleReceiveMapper.updateCaseState(verify_state);
		}
		map.put("success", true);
		map.put("result", true);
		map.put("message", case_code + "上传成功；<br>");
		return map;
	}
}
