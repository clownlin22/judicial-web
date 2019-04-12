package com.rds.judicial.service.impl;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.activiti.mapper.RdsActivitiJudicialMapper;
import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.code.date.DateUtils;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.file.XmlParseUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialSampleRelayMapper;
import com.rds.judicial.model.RdsJudicialDicAreaModel;
import com.rds.judicial.model.RdsJudicialRelaySampleInfo;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleCaseCodeModel;
import com.rds.judicial.model.RdsJudicialSampleConfirmModel;
import com.rds.judicial.model.RdsJudicialSampleReceiveModel;
import com.rds.judicial.model.RdsJudicialSampleRelayModel;
import com.rds.judicial.service.RdsJudicialSampleRelayService;
import com.rds.upc.model.RdsUpcUserModel;

@Service
public class RdsJudicialSampleRelayServiceImpl implements
		RdsJudicialSampleRelayService {

	@Autowired
	private RdsJudicialSampleRelayMapper RdsJudicialSampleRelayMapper;

	@Setter
	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private RdsActivitiJudicialMapper rdsActivitiJudicialMapper;

	// 配置文件地址
	private static final String XML_PATH = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config.xml";

	private static final String SAMPLE_SIGN = "JS";

	@Override
	public RdsJudicialResponse getSampleReceiveInfos(Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialSampleReceiveModel> sampleReceiveModels = RdsJudicialSampleRelayMapper
				.getSampleReceiveInfos(params);
		int count = RdsJudicialSampleRelayMapper
				.countSampleReceiveInfos(params);
		response.setItems(sampleReceiveModels);
		response.setCount(count);
		return response;
	}

	@Override
	public List<RdsJudicialRelaySampleInfo> getReceiveSampleInfo(
			Map<String, Object> params) {
		return RdsJudicialSampleRelayMapper.getReceiveSampleInfo(params);
	}

	@Override
	public boolean exsitReceiveSampleCode(Map<String, Object> params) {
		int count = RdsJudicialSampleRelayMapper.exsitReceiveSampleCode(params);
		if (count > 0) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Object saveReceiveSampleInfo(Map<String, Object> params,
			RdsUpcUserModel user) {
		// 页面样本条形码list
		List<String> sample_codes = getValues(params.get("sample_code"));
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		map.put("sample_code", sample_codes);
		// 查询需要交接案例的所有样本
		List<String> sample_code_all = RdsJudicialSampleRelayMapper
				.querySampleByCode(map);
		System.out.println("sample_code_all==================="
				+ sample_code_all);
		System.out.println("sample_codes===================" + sample_codes);
		// 页面条形码和数据库查询条形码比较差异结果
		List<String> diff = new ArrayList<String>();
		diff = getDiffrent(sample_code_all, sample_codes);
		System.out.println("diff===================" + diff);
		// 最终确定哪些
		List<String> diffFinal = new ArrayList<String>();
		if (diff.size() > 0) {
			map.put("sample_code", diff);
			// 查询补全差异条形码
			List<String> diffSampleCodes = RdsJudicialSampleRelayMapper
					.querySampleByCode(map);

			diffFinal = getDiffrent(diffSampleCodes, diff);
			result.put("result", false);
			result.put("message", "保存失败，以下案例样本不全：<br>" + diffFinal);
			return result;

		} else {

			List<String> case_codes = RdsJudicialSampleRelayMapper
					.querySampleCaseCode(map);

			map.put("case_codes", case_codes);

			List<String> lists = RdsJudicialSampleRelayMapper
					.queryCaseVerifyState(map);
			if (lists.size() > 0) {
				result.put("result", false);
				result.put("message", "保存失败，以下案例未审核通过<br>" + lists);
				return result;
			}

			RdsJudicialSampleReceiveModel receiveModel = new RdsJudicialSampleReceiveModel(
					UUIDUtil.getUUID(), params.get("receive_per").toString(),
					params.get("receive_remark").toString());
			boolean flag = RdsJudicialSampleRelayMapper
					.addReceiveSampleInfo(receiveModel);

			Map<String, Object> variables = new HashMap<>();

			for (String string : case_codes) {
				rdsActivitiJudicialService.runByCaseCode(string, variables,
						user);
				Map<String, Object> mapTemp = new HashMap<String, Object>();
				mapTemp.put("case_code", string);
				mapTemp.put("verify_state", 4);
				RdsJudicialSampleRelayMapper.updateCaseVerifyBycode(mapTemp);
			}

			if (flag) {
				for (String sample_code : sample_codes) {
					RdsJudicialRelaySampleInfo sampleInfo = new RdsJudicialRelaySampleInfo(
							UUIDUtil.getUUID(), sample_code,
							receiveModel.getReceive_id());
					RdsJudicialSampleRelayMapper.addReceiveSample(sampleInfo);
				}
				result.put("result", true);
				result.put("receive_id", receiveModel.getReceive_id());
				result.put("message", "保存成功！");
				return result;
			} else {
				result.put("result", false);
				result.put("message", "保存失败，请联系管理员！");
				return result;
			}
		}

	}

	public static List<String> getValues(Object object) {
		List<String> values = new ArrayList<String>();
		if (object != null) {
			String str = object.toString();
			String[] objects = str.split(",");
			if (objects.length > 1) {
				str = str.substring(1, str.length() - 1);
				String[] objs = str.split(",");
				for (String s : objs) {
					values.add(s.trim());
				}
			} else {
				values.add(str.trim());
			}
		}
		return values;
	}

	@Override
	public boolean deleteReceiveSampleInfo(Map<String, Object> params) {
		return RdsJudicialSampleRelayMapper.deleteReceiveSampleInfo(params);
	}

	@Override
	@Transactional
	public boolean updateReceiveSampleInfo(Map<String, Object> params) {
		RdsJudicialSampleReceiveModel receiveModel = new RdsJudicialSampleReceiveModel(
				params.get("receive_id").toString(), params.get(
						"receive_remark").toString());
		boolean result = RdsJudicialSampleRelayMapper
				.updateReceiveSampleInfo(receiveModel);
		if (result) {
			List<String> sample_codes = getValues(params.get("sample_code"));
			RdsJudicialSampleRelayMapper.deleteReceiveSample(receiveModel);
			for (String sample_code : sample_codes) {
				RdsJudicialRelaySampleInfo sampleInfo = new RdsJudicialRelaySampleInfo(
						UUIDUtil.getUUID(), sample_code,
						receiveModel.getReceive_id());
				RdsJudicialSampleRelayMapper.addReceiveSample(sampleInfo);
			}
			return true;
		} else {
			return false;
		}
	}

	/****************************************************** 交接部分 ***********************************************/
	@Override
	public RdsJudicialResponse getSampleRelayInfos(Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialSampleRelayModel> sampleRelayModels = RdsJudicialSampleRelayMapper
				.getSampleRelayInfos(params);
		int count = RdsJudicialSampleRelayMapper.countSampleRelayInfos(params);
		response.setItems(sampleRelayModels);
		response.setCount(count);
		return response;
	}

	@Override
	public List<RdsJudicialRelaySampleInfo> getRelaySampleInfo(
			Map<String, Object> params) {
		return RdsJudicialSampleRelayMapper.getRelaySampleInfo(params);
	}

	@Override
	@Transactional
	public Object saveRelaySampleInfo(Map<String, Object> params,
			RdsUpcUserModel user) {
		Map<String, Object> result = new HashMap<>();
		String[] sample_ids = params.get("sampleids").toString().split(",");

		Map<String, Object> map = new HashMap<>();
		map.put("sample_ids", sample_ids);
		List<String> sample_code = RdsJudicialSampleRelayMapper
				.querySampleCodeByid(map);
		map.put("sample_code", sample_code);
		// 查询需要交接案例的所有样本
		List<String> sample_code_all = RdsJudicialSampleRelayMapper
				.querySampleByCode(map);
		// 页面条形码和数据库查询条形码比较差异结果
		List<String> diff = new ArrayList<String>();
		diff = getDiffrent(sample_code_all, sample_code);
		// 最终确定哪些
		List<String> diffFinal = new ArrayList<String>();

		if (diff.size() > 0) {
			map.put("sample_code", diff);
			// 查询补全差异条形码
			List<String> diffSampleCodes = RdsJudicialSampleRelayMapper
					.querySampleByCode(map);

			diffFinal = getDiffrent(diffSampleCodes, diff);
			result.put("result", false);
			result.put("message", "保存失败，以下案例样本不全：<br>" + diffFinal);
			return result;

		} else {
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH, "sample_date");
			String now_time = com.rds.code.utils.StringUtils
					.dateToEight(new Date());
			DecimalFormat df = new DecimalFormat("0000");
			String relay_code = "";
			if (xml_time.equals(now_time)) {
				relay_code = now_time
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH, "sample_relay_num")) + 1);
			} else {
				XmlParseUtil.updateXmlValue(XML_PATH, "sample_date", now_time);
				XmlParseUtil.updateXmlValue(XML_PATH, "sample_relay_num", "1");
				relay_code = now_time
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH, "sample_relay_num")));
			}
			XmlParseUtil.updateXmlValue(XML_PATH, "sample_relay_num", String
					.valueOf(Integer.parseInt(XmlParseUtil.getXmlValue(
							XML_PATH, "sample_relay_num")) + 1));
			RdsJudicialSampleRelayModel sampleRelayModel = new RdsJudicialSampleRelayModel(
					UUIDUtil.getUUID(), params.get("relay_per").toString(),
					SAMPLE_SIGN + relay_code, params.get("relay_remark")
							.toString());
			// 插入交接单号信息
			if (RdsJudicialSampleRelayMapper
					.addRelaySampleInfo(sampleRelayModel)) {

				List<String> case_codes = RdsJudicialSampleRelayMapper
						.querySampleCaseCode(map);

				Map<String, Object> variables = new HashMap<>();

				for (String string : case_codes) {
					// 判断当前流程节点位置
					String taskId = rdsActivitiJudicialMapper
							.getCaseTask(string);
					if (taskId == null) {
						result.put("result", false);
						result.put("message", "操作失败，请联系管理员！");
						return result;
					}
					Task task = taskService.createTaskQuery().taskId(taskId)
							.singleResult();
					String taskSampleTransfer = task.getName();
					if (!"案例样本确认".equals(taskSampleTransfer)) {
						rdsActivitiJudicialService.runByCaseCode(string,
								variables, user);
					}
					// Map<String, Object> mapTemp = new HashMap<String,
					// Object>();
					// mapTemp.put("case_code", string);
					// mapTemp.put("verify_state", 4);
					// RdsJudicialSampleRelayMapper.updateCaseVerifyBycode(mapTemp);
				}

				for (String sample_id : sample_ids) {
					RdsJudicialRelaySampleInfo sampleInfo = new RdsJudicialRelaySampleInfo(
							sample_id, sampleRelayModel.getRelay_id());
					RdsJudicialSampleRelayMapper.addRelaySample(sampleInfo);
				}

				result.put("result", true);
				result.put("message", "操作成功！");
				return result;
			} else {
				result.put("result", false);
				result.put("message", "操作失败，请联系管理员！");
				return result;
			}
		}

	}

	@Override
	public boolean deleteRelaySampleInfo(Map<String, Object> params) {
		return RdsJudicialSampleRelayMapper.deleteRelaySampleInfo(params);
	}

	@Override
	@Transactional
	public boolean updateRelaySampleInfo(Map<String, Object> params) {
		RdsJudicialSampleRelayModel relayModel = new RdsJudicialSampleRelayModel(
				params.get("relay_id").toString(), params.get("relay_remark")
						.toString());
		boolean result = RdsJudicialSampleRelayMapper
				.updateRelaySampleInfo(relayModel);
		if (result) {
			String[] sample_ids = params.get("sampleids").toString().split(",");
			RdsJudicialSampleRelayMapper.deleteRelaySample(relayModel);
			for (String sample_id : sample_ids) {
				RdsJudicialRelaySampleInfo sampleInfo = new RdsJudicialRelaySampleInfo(
						sample_id, relayModel.getRelay_id());
				RdsJudicialSampleRelayMapper.addRelaySample(sampleInfo);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public Object confirmRelaySampleInfo(Map<String, Object> params,
			RdsUpcUserModel user) {
		String[] samples = params.get("samples").toString().split(",");
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		map.put("sample_ids", samples);
		List<String> sample_code = RdsJudicialSampleRelayMapper
				.querySampleCodeByid(map);
		map.put("relay_id", params.get("relay_id"));
		List<String> sample_code_false = RdsJudicialSampleRelayMapper
				.queryFalseSampleCodeByid(map);

		map.put("sample_code", sample_code);

			RdsJudicialSampleConfirmModel confirmModel = new RdsJudicialSampleConfirmModel(
					UUIDUtil.getUUID(), params.get("confirm_per").toString(),
					params.get("relay_id").toString(), params.get(
							"confirm_remark").toString());
			if (RdsJudicialSampleRelayMapper
					.confirmRelaySampleInfo(confirmModel)) {
				Map<String, Object> variables = new HashMap<>();
				if (sample_code.size() > 0) {
					/* 流程插入 start */
					List<String> case_codes = RdsJudicialSampleRelayMapper
							.querySampleCaseCode2(map);

					variables.put("issamplepass", 1);
					for (String string : case_codes) {
						rdsActivitiJudicialService.runByCaseCode(string,
								variables, user);
						Map<String, Object> mapTemp = new HashMap<String, Object>();
						mapTemp.put("case_code", string);
						mapTemp.put("verify_state", 5);
						RdsJudicialSampleRelayMapper
								.updateCaseVerifyBycode(mapTemp);
					}
					/* 流程插入 end */
				}

				/* 未通过确认案例条形码 流程插入 start */
				if (sample_code_false.size() > 0) {
					Map<String, Object> maps = new HashMap<>();
					maps.put("sample_code", sample_code_false);
					maps.put("relay_id", params.get("relay_id"));
					List<String> case_code_fase = RdsJudicialSampleRelayMapper
							.querySampleCaseCode(maps);

					variables.put("issamplepass", 0);
					variables.put("comment", params.get("confirm_remark"));
					for (String string : case_code_fase) {
						rdsActivitiJudicialService.runByCaseCode(string,
								variables, user);
						// Map<String, Object> mapTemp = new HashMap<String,
						// Object>();
						// mapTemp.put("case_code", string);
						// mapTemp.put("verify_state", 5);
						// RdsJudicialSampleRelayMapper
						// .updateCaseVerifyBycode(mapTemp);
					}
				}
				/* 未通过确认案例条形码 流程插入 end */

				RdsJudicialSampleRelayMapper.updateSampleInfoToFalse(params);
				String sample = params.get("samples").toString();
				if (StringUtils.isNotEmpty(sample)) {
					for (String sample_id : samples) {
						RdsJudicialRelaySampleInfo relayInfo = new RdsJudicialRelaySampleInfo(
								sample_id, params.get("relay_id").toString());
						RdsJudicialSampleRelayMapper
								.updateSampleInfo(relayInfo);
					}
				}
				result.put("result", true);
				result.put("message", "操作成功");
				return result;
			} else {
				result.put("result", false);
				result.put("message", "操作失败，请联系管理员！");
				return result;
			}
		}

	@Override
	public RdsJudicialSampleRelayModel getRelaySample(Map<String, Object> params) {
		return RdsJudicialSampleRelayMapper.getRelaySample(params);
	}

	@Override
	public List<RdsJudicialRelaySampleInfo> getSelectSampleInfo(
			Map<String, Object> params) {
		return RdsJudicialSampleRelayMapper.getSelectSampleInfo(params);
	}

	@Override
	public List<RdsJudicialDicAreaModel> getSampleInfo(String relay_id) {
		return RdsJudicialSampleRelayMapper.getSampleInfo(relay_id);
	}

	@Override
	public List<String> querySampleByCode(Map<String, Object> params) {
		return RdsJudicialSampleRelayMapper.querySampleByCode(params);
	}

	/**
	 * 获取两个List的不同元素
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	private static List<String> getDiffrent(List<String> list1,
			List<String> list2) {
		long st = System.nanoTime();
		List<String> diff = new ArrayList<String>();
		List<String> maxList = list1;
		List<String> minList = list2;
		if (list2.size() > list1.size()) {
			maxList = list2;
			minList = list1;
		}
		Map<String, Integer> map = new HashMap<String, Integer>(maxList.size());
		for (String string : maxList) {
			map.put(string, 1);
		}
		for (String string : minList) {
			if (map.get(string) != null) {
				map.put(string, 2);
				continue;
			}
			diff.add(string);
		}
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue() == 1) {
				diff.add(entry.getKey());
			}
		}
		System.out.println("getDiffrent5 total times "
				+ (System.nanoTime() - st));
		return diff;

	}

	@Override
	public List<String> querySampleCaseCode(Map<String, Object> params) {
		return RdsJudicialSampleRelayMapper.querySampleCaseCode(params);
	}

	@Override
	public boolean updateCaseVerifyBycode(Map<String, Object> params) {
		return RdsJudicialSampleRelayMapper.updateCaseVerifyBycode(params);
	}

	/**
	 * 分页查询
	 */
	@Override
	public RdsJudicialResponse queryCaseCodeBySampleCode(
			Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialSampleCaseCodeModel> sampleCaseCodeModel = RdsJudicialSampleRelayMapper
				.queryCaseCodeBySampleCode(params);
		int count = RdsJudicialSampleRelayMapper
				.countCaseCodeBySampleCode(params);
		response.setItems(sampleCaseCodeModel);
		response.setCount(count);
		return response;
	}

	@Override
	public int countCaseCodeBySampleCod(Map<String, Object> params) {
		return RdsJudicialSampleRelayMapper.countCaseCodeBySampleCode(params);
	}

	@Override
	public boolean updateCaseReportmodel(Map<String, Object> params) {
		return RdsJudicialSampleRelayMapper.updateCaseReportmodel(params);
	}

	@Override
	public void exportCaseCodeBySampleCode(Map<String, Object> params,
			HttpServletResponse response) {
		String filename = "样本编码列表";
		// 导出案例对应样本编号
		List<RdsJudicialSampleCaseCodeModel> caseInfoModels = RdsJudicialSampleRelayMapper
				.exportCaseCodeBySampleCode(params);
		// excel表格列头
		Object[] titles = { "案例编号", "样本编号" };
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		// 循环案例列表拼装表格一行
		for (int i = 0; i < caseInfoModels.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			// 案例编号
			objects.add(caseInfoModels.get(i).getCase_code());
			// 样本条码
			objects.add(caseInfoModels.get(i).getSample_code());
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "样本编码列表"
				+ DateUtils.Date2String(new Date()));
	}

	@Override
	public List<String> querySampleIdByRece(Map<String, Object> params) {
		return RdsJudicialSampleRelayMapper.querySampleIdByRece(params);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<String, Object> saveRelaySampleInfoNow(
			Map<String, Object> params, RdsUpcUserModel user) {
		Map<String, Object> result = new HashMap<>();
		List<String> sample_ids = (List<String>) params.get("sample_ids");

		Map<String, Object> map = new HashMap<>();
		// map.put("sample_ids", sample_ids);
		List<String> sample_code = RdsJudicialSampleRelayMapper
				.querySampleCodeByid(params);
		map.put("sample_code", sample_code);
		// 查询需要交接案例的所有样本
		// List<String> sample_code_all = RdsJudicialSampleRelayMapper
		// .querySampleByCode(map);
		// 页面条形码和数据库查询条形码比较差异结果
		// List<String> diff = new ArrayList<String>();
		// diff = getDiffrent(sample_code_all, sample_code);
		// 最终确定哪些
		// List<String> diffFinal = new ArrayList<String>();

		// if (diff.size() > 0) {
		// map.put("sample_code", diff);
		// // 查询补全差异条形码
		// List<String> diffSampleCodes = RdsJudicialSampleRelayMapper
		// .querySampleByCode(map);
		//
		// diffFinal = getDiffrent(diffSampleCodes, diff);
		// result.put("result", false);
		// result.put("message", "保存失败，以下案例样本不全：<br>" + diffFinal);
		// return result;
		//
		// } else {
		String xml_time = XmlParseUtil.getXmlValue(XML_PATH, "sample_date");
		String now_time = com.rds.code.utils.StringUtils
				.dateToEight(new Date());
		DecimalFormat df = new DecimalFormat("0000");
		String relay_code = "";
		if (xml_time.equals(now_time)) {
			relay_code = now_time
					+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
							XML_PATH, "sample_relay_num")) + 1);
		} else {
			XmlParseUtil.updateXmlValue(XML_PATH, "sample_date", now_time);
			XmlParseUtil.updateXmlValue(XML_PATH, "sample_relay_num", "1");
			relay_code = now_time
					+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
							XML_PATH, "sample_relay_num")));
		}
		XmlParseUtil.updateXmlValue(XML_PATH, "sample_relay_num", String
				.valueOf(Integer.parseInt(XmlParseUtil.getXmlValue(XML_PATH,
						"sample_relay_num")) + 1));
		RdsJudicialSampleRelayModel sampleRelayModel = new RdsJudicialSampleRelayModel(
				UUIDUtil.getUUID(), params.get("relay_per").toString(),
				SAMPLE_SIGN + relay_code, params.get("relay_remark").toString());
		// 插入交接单号信息
		if (RdsJudicialSampleRelayMapper.addRelaySampleInfo(sampleRelayModel)) {

			List<String> case_codes = RdsJudicialSampleRelayMapper
					.querySampleCaseCode(map);

			Map<String, Object> variables = new HashMap<>();

			for (String string : case_codes) {
				// 判断当前流程节点位置
				String taskId = rdsActivitiJudicialMapper.getCaseTask(string);
				if (taskId == null) {
					result.put("result", false);
					result.put("message", "操作失败，请联系管理员！");
					return result;
				}
				Task task = taskService.createTaskQuery().taskId(taskId)
						.singleResult();
				String taskSampleTransfer = task.getName();
				if (!"案例样本确认".equals(taskSampleTransfer)) {
					rdsActivitiJudicialService.runByCaseCode(string, variables,
							user);
				}
				// Map<String, Object> mapTemp = new HashMap<String,
				// Object>();
				// mapTemp.put("case_code", string);
				// mapTemp.put("verify_state", 4);
				// RdsJudicialSampleRelayMapper.updateCaseVerifyBycode(mapTemp);
			}

			for (String sample_id : sample_ids) {
				RdsJudicialRelaySampleInfo sampleInfo = new RdsJudicialRelaySampleInfo(
						sample_id, sampleRelayModel.getRelay_id());
				RdsJudicialSampleRelayMapper.addRelaySample(sampleInfo);
			}

			result.put("result", true);
			result.put("message", "操作成功！");
			return result;
		} else {
			result.put("result", false);
			result.put("message", "操作失败，请联系管理员！");
			return result;
		}
		// }

	}
	@Override
	public void exportSampleCode(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		//查样本编号
		List<String> list =getValues(RdsJudicialSampleRelayMapper.getReceiveSamplecode(params));
		params.put("sample_codes", list);
		//查case_id
		List<String> list2 =RdsJudicialSampleRelayMapper.exportSampleCodeByCaseId(params);
		String filename=params.get("relay_code").toString();
		Object[] titles = { "案例编号和样本编号"};
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list2.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			Map<String, Object> result = new HashMap<>();
			String case_id=list2.get(i).toString();
			result.put("case_id", case_id);
			List<String> case_code=RdsJudicialSampleRelayMapper.getCaseCodeBycaseId(result);
			objects.add(case_code.get(0));
			bodys.add(objects);
			List<String> sample_codes=RdsJudicialSampleRelayMapper.getSampleCodesBycaseId(result);
			for (int j=0;j<sample_codes.size();j++){
				List<Object> objects1 = new ArrayList<Object>();
				objects1.add(sample_codes.get(j).toString());
				bodys.add(objects1);
			}
		}
		ExportUtils.export(response, filename, titles, bodys, "样本编号下载");
	
		
	}

	@Override
	public Object saveReceiveSampleInfoAuto(Map<String, Object> params,
			RdsUpcUserModel user) {

		// 页面样本条形码list
		List<String> sample_codes = getValues(params.get("sample_code"));
		@SuppressWarnings("unchecked")
		List<String> case_codes = (List<String>) params.get("case_codes");
		Map<String, Object> result = new HashMap<String, Object>();

			RdsJudicialSampleReceiveModel receiveModel = new RdsJudicialSampleReceiveModel(
					UUIDUtil.getUUID(), params.get("receive_per").toString(),
					params.get("receive_remark").toString());
			boolean flag = RdsJudicialSampleRelayMapper
					.addReceiveSampleInfo(receiveModel);

			Map<String, Object> variables = new HashMap<>();

			for (String string : case_codes) {
				rdsActivitiJudicialService.runByCaseCode(string, variables,
						user);
				Map<String, Object> mapTemp = new HashMap<String, Object>();
				mapTemp.put("case_code", string);
				mapTemp.put("verify_state", 4);
				RdsJudicialSampleRelayMapper.updateCaseVerifyBycode(mapTemp);
			}

			if (flag) {
				for (String sample_code : sample_codes) {
					RdsJudicialRelaySampleInfo sampleInfo = new RdsJudicialRelaySampleInfo(
							UUIDUtil.getUUID(), sample_code,
							receiveModel.getReceive_id());
					RdsJudicialSampleRelayMapper.addReceiveSample(sampleInfo);
				}
				result.put("result", true);
				result.put("receive_id", receiveModel.getReceive_id());
				result.put("message", "保存成功！");
				return result;
			} else {
				result.put("result", false);
				result.put("message", "保存失败，请联系管理员！");
				return result;
			}


	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<String, Object> saveRelaySampleInfoNowAuto(
			Map<String, Object> params, RdsUpcUserModel user) {
		Map<String, Object> result = new HashMap<>();
		List<String> sample_ids = (List<String>) params.get("sample_ids");

		Map<String, Object> map = new HashMap<>();
		// map.put("sample_ids", sample_ids);
		List<String> sample_code = RdsJudicialSampleRelayMapper
				.querySampleCodeByid(params);
		map.put("sample_code", sample_code);
		// 查询需要交接案例的所有样本
		// List<String> sample_code_all = RdsJudicialSampleRelayMapper
		// .querySampleByCode(map);
		// 页面条形码和数据库查询条形码比较差异结果
		// List<String> diff = new ArrayList<String>();
		// diff = getDiffrent(sample_code_all, sample_code);
		// 最终确定哪些
		// List<String> diffFinal = new ArrayList<String>();

		// if (diff.size() > 0) {
		// map.put("sample_code", diff);
		// // 查询补全差异条形码
		// List<String> diffSampleCodes = RdsJudicialSampleRelayMapper
		// .querySampleByCode(map);
		//
		// diffFinal = getDiffrent(diffSampleCodes, diff);
		// result.put("result", false);
		// result.put("message", "保存失败，以下案例样本不全：<br>" + diffFinal);
		// return result;
		//
		// } else {
		String xml_time = XmlParseUtil.getXmlValue(XML_PATH, "sample_date");
		String now_time = com.rds.code.utils.StringUtils
				.dateToEight(new Date());
		DecimalFormat df = new DecimalFormat("0000");
		String relay_code = "";
		if (xml_time.equals(now_time)) {
			relay_code = now_time
					+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
							XML_PATH, "sample_relay_num")) + 1);
		} else {
			XmlParseUtil.updateXmlValue(XML_PATH, "sample_date", now_time);
			XmlParseUtil.updateXmlValue(XML_PATH, "sample_relay_num", "1");
			relay_code = now_time
					+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
							XML_PATH, "sample_relay_num")));
		}
		XmlParseUtil.updateXmlValue(XML_PATH, "sample_relay_num", String
				.valueOf(Integer.parseInt(XmlParseUtil.getXmlValue(XML_PATH,
						"sample_relay_num")) + 1));
		RdsJudicialSampleRelayModel sampleRelayModel = new RdsJudicialSampleRelayModel(
				UUIDUtil.getUUID(), params.get("relay_per").toString(),
				SAMPLE_SIGN + relay_code, params.get("relay_remark").toString());
		// 插入交接单号信息
		if (RdsJudicialSampleRelayMapper.addRelaySampleInfo(sampleRelayModel)) {

			List<String> case_codes =(List<String>) params.get("case_codes");

			Map<String, Object> variables = new HashMap<>();

			for (String string : case_codes) {
				// 判断当前流程节点位置
				String taskId = rdsActivitiJudicialMapper.getCaseTask(string);
				if (taskId == null) {
					result.put("result", false);
					result.put("message", "操作失败，请联系管理员！");
					return result;
				}
				Task task = taskService.createTaskQuery().taskId(taskId)
						.singleResult();
				String taskSampleTransfer = task.getName();
				if (!"案例样本确认".equals(taskSampleTransfer)) {
					rdsActivitiJudicialService.runByCaseCode(string, variables,
							user);
				}
				// Map<String, Object> mapTemp = new HashMap<String,
				// Object>();
				// mapTemp.put("case_code", string);
				// mapTemp.put("verify_state", 4);
				// RdsJudicialSampleRelayMapper.updateCaseVerifyBycode(mapTemp);
			}

			for (String sample_id : sample_ids) {
				RdsJudicialRelaySampleInfo sampleInfo = new RdsJudicialRelaySampleInfo(
						sample_id, sampleRelayModel.getRelay_id());
				RdsJudicialSampleRelayMapper.addRelaySample(sampleInfo);
			}

			result.put("result", true);
			result.put("message", "操作成功！");
			return result;
		} else {
			result.put("result", false);
			result.put("message", "操作失败，请联系管理员！");
			return result;
		}
		// }

	}	
	
}
