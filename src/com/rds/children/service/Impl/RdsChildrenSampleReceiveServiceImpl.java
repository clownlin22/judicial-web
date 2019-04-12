package com.rds.children.service.Impl;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.activiti.mapper.RdsActivitiJudicialMapper;
import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.children.mapper.RdsChildrenSampleReceiveMapper;
import com.rds.children.model.RdsChildrenResponse;
import com.rds.children.model.RdsChildrenSampleModel;
import com.rds.children.model.RdsChildrenSampleReceiveModel;
import com.rds.children.service.RdsChildrenSampleReceiveService;
import com.rds.code.utils.file.XmlParseUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.upc.model.RdsUpcUserModel;

@Service("RdsChildrenSampleReceiveService")
@Transactional
public class RdsChildrenSampleReceiveServiceImpl implements
		RdsChildrenSampleReceiveService {

	@Setter
	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private RdsActivitiJudicialMapper rdsActivitiJudicialMapper;

	@Autowired
	private RdsChildrenSampleReceiveMapper rdsChildrenSampleReceiveMapper;

	// 配置文件地址
	private static final String XML_PATH_6 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config6.xml";

	@Override
	public RdsChildrenResponse getSampleReceiveInfo(Map<String, Object> params) {
		RdsChildrenResponse response = new RdsChildrenResponse();
		List<RdsChildrenSampleReceiveModel> sampleInfoModels = rdsChildrenSampleReceiveMapper
				.getSampleReceiveInfo(params);
		int count = rdsChildrenSampleReceiveMapper
				.getCountSampleReceiveInfo(params);
		response.setItems(sampleInfoModels);
		response.setCount(count);
		return response;
	}

	@Override
	public RdsChildrenResponse getTransferInfo(Map<String, Object> param) {
		RdsChildrenResponse response = new RdsChildrenResponse();
		List<RdsChildrenSampleReceiveModel> transferInfoModels = rdsChildrenSampleReceiveMapper
				.getTransferInfo(param);
		int count = rdsChildrenSampleReceiveMapper.getTransferInfoCount(param);
		response.setItems(transferInfoModels);
		response.setCount(count);
		return response;
	}


	@Override
	public List<RdsChildrenSampleReceiveModel> getRelaySampleInfo(
			Map<String, Object> param) {
		return rdsChildrenSampleReceiveMapper.getRelaySampleInfo(param);
	}
	
	@Override
	public Map<String, Object> saveReceiveSampleInfo(Map<String, Object> params)
			throws Exception {
		// 交接结果集
		Map<String, Object> result = new HashMap<String, Object>();
		// 校验案例状态
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> sample_code = getValues(params.get("sample_code"));
		map.put("sample_code", sample_code);
		List<String> lists = rdsChildrenSampleReceiveMapper
				.queryVerifybySampleCode(map);

		List<String> list = rdsChildrenSampleReceiveMapper
				.queryTransferbySampleCode(map);
		if (lists.size() > 0) {
			result.put("result", false);
			result.put("message", "以下样本案例状态有误：" + lists);
			return result;
		}
		if (list.size() > 0) {
			result.put("result", false);
			result.put("message", "以下样本交接状态有误：" + list);
			return result;
		} else {
			/** 交接单号生成 start **/
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_6,
					"finance_code_child_date");
			String now_time = com.rds.code.utils.StringUtils
					.dateToEight(new Date());
			DecimalFormat df = new DecimalFormat("000");
			String transfer_num = "CHILD";
			if (xml_time.equals(now_time)) {
				transfer_num += now_time
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH_6, "finance_code_child")) + 1);
			} else {
				XmlParseUtil.updateXmlValue(XML_PATH_6,
						"finance_code_child_date", now_time);
				XmlParseUtil.updateXmlValue(XML_PATH_6, "finance_code_child",
						"1");
				transfer_num += now_time
						+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
								XML_PATH_6, "finance_code_child")));
			}
			XmlParseUtil.updateXmlValue(XML_PATH_6, "finance_code_child",
					String.valueOf(Integer.parseInt(XmlParseUtil.getXmlValue(
							XML_PATH_6, "finance_code_child")) + 1));
			/** 交接单号生成 end **/

			params.put("transfer_num", transfer_num);
			String receive_id = UUIDUtil.getUUID();
			params.put("receive_id", receive_id);
			if (!rdsChildrenSampleReceiveMapper.saveTransferNum(params)) {
				result.put("result", false);
				result.put("message", "操作失败，请联系管理员！");
				return result;
			}
			for (String string : sample_code) {
				Map<String, Object> mapSamle = new HashMap<String, Object>();
				mapSamle.put("receive_id", receive_id);
				mapSamle.put("id", UUIDUtil.getUUID());
				mapSamle.put("sample_code", string);
				rdsChildrenSampleReceiveMapper.saveTransferSample(mapSamle);
			}

			// 根据样本编号查询案例编号
			List<String> list_CaseCode = rdsChildrenSampleReceiveMapper
					.queryCaseCodebySampleCode(map);
			if (list_CaseCode.size() == 0)
				throw new Exception();

			for (String string : list_CaseCode) {
				Map<String, Object> variables = new HashMap<>();
				String taskId = rdsActivitiJudicialMapper
						.getChildCaseTask(string);
				if (taskId == null) {
					result.put("result", false);
					result.put("message", "操作失败，请联系管理员！");
					return result;
				}
				// 流程加入
				rdsActivitiJudicialService.runByChildCaseCode(string,
						variables, (RdsUpcUserModel) params.get("user"));
				Map<String, Object> verify_state = new HashMap<>();
				verify_state.put("case_code", string);;
				verify_state.put("verify_state", 4);
				// 更新基本信息
				rdsChildrenSampleReceiveMapper.updateCaseState(verify_state);
			}

			result.put("result", true);
			result.put("message", "操作成功！");
			return result;
		}
	}

	@Override
	public int getCountSampleReceiveInfo(Map<String, Object> params) {
		return rdsChildrenSampleReceiveMapper.getCountSampleReceiveInfo(params);
	}

	@Override
	public int existSampleCode(Map<String, Object> param) {
		return rdsChildrenSampleReceiveMapper.existSampleCode(param);
	}

	@Override
	public List<RdsChildrenSampleModel> getSampleInfo(Map<String, Object> param) {
		return rdsChildrenSampleReceiveMapper.getSampleInfo(param);
	}

	@Override
	public Map<String, Object> confirmRelaySampleInfo(Map<String, Object> params)
			throws Exception {
		Map<String,Object> result = new HashMap<>();
		
		String receive_id = params.get("receive_id").toString();
		Map<String,Object> map = new HashMap<>();
		map.put("receive_id", receive_id);
		map.put("state", 2);
		if(!rdsChildrenSampleReceiveMapper.updateTransferNum(map)){
			result.put("result", false);
			result.put("message", "操作1失败，请联系管理员！");
			return result;
		}
		//根据接收id查询所有案例编号
		List<String> caseCodeAll=rdsChildrenSampleReceiveMapper.queryCaseCodebyReceive(map);
		List<String> sample_codeids = getValues(params.get("sample_codeids"));
		map.put("sample_codeids", sample_codeids);
		//查询确认通过的案例编号
		List<String> caseCodePass=rdsChildrenSampleReceiveMapper.queryCaseCodebyReceive(map);
		//未确认通过案例编号
		List<String> caseCodeNoPass = getDiffrent(caseCodeAll,caseCodePass);
		//插入确认信息
		params.put("relay_id", UUIDUtil.getUUID());
		if(!rdsChildrenSampleReceiveMapper.saveReceiveConfirm(params)){
			result.put("result", false);
			result.put("message", "操作2失败，请联系管理员！");
			return result;
		}
		Map<String, Object> variables = new HashMap<>();
		//不通过案例
		for (String string : caseCodeNoPass) {
			variables.put("issamplepass", 0);
			variables.put("comment", params.get("receive_remark"));
			rdsActivitiJudicialService.runByChildCaseCode(string,
					variables, (RdsUpcUserModel) params.get("user"));
			//更新每个样本状态
			map.put("confirm_state", 2);
			rdsChildrenSampleReceiveMapper.updateTransferSample(map);
		}
		//通过案例
		for (String string : caseCodePass) {
			//案例流程状态更新
			variables.put("issamplepass", 1);
			variables.put("comment", params.get("receive_remark"));
			rdsActivitiJudicialService.runByChildCaseCode(string,
					variables, (RdsUpcUserModel) params.get("user"));
			Map<String, Object> verify_state = new HashMap<>();
			verify_state.put("case_code", string);;
			verify_state.put("verify_state", 5);
			// 更新基本信息状态
			rdsChildrenSampleReceiveMapper.updateCaseState(verify_state);
			//更新每个样本状态
			map.put("confirm_state", 1);
			rdsChildrenSampleReceiveMapper.updateTransferSample(map);
		}
		
		result.put("result", true);
		result.put("message", "操作成功！");
		return result;
	}

	public static List<String> getValues(Object object) {
		List<String> values = new ArrayList<String>();
		if (object != null) {
			String str = object.toString();
			String[] objects = str.split(",");
			if (objects.length > 1) {
				if(str.contains("[")&&str.contains("]"))
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
}
