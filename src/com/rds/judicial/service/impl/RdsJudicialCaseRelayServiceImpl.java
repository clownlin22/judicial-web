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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.bacera.model.RdsBaceraInvasivePreModel;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.file.XmlParseUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialCaseRelayMapper;
import com.rds.judicial.mapper.RdsJudicialSampleRelayMapper;
import com.rds.judicial.model.RdsJudicialCaseConfirmModel;
import com.rds.judicial.model.RdsJudicialCaseRelayModel;
import com.rds.judicial.model.RdsJudicialConfirmCaseInfo;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleReciveModel;
import com.rds.judicial.service.RdsJudicialCaseRelayService;
import com.rds.upc.model.RdsUpcUserModel;

@Service
public class RdsJudicialCaseRelayServiceImpl implements
		RdsJudicialCaseRelayService {
	// 配置文件地址
	private static final String XML_PATH = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config.xml";
	private static final String CASE_SIGN = "CS";
	@Autowired
	private RdsJudicialCaseRelayMapper RdsJudicialCaseRelayMapper;

	@Autowired
	private RdsJudicialSampleRelayMapper RdsJudicialSampleRelayMapper;

	@Setter
	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;

	@Override
	public RdsJudicialResponse getCaseRelayInfo(Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialCaseConfirmModel> caseConfirmModels = RdsJudicialCaseRelayMapper
				.getCaseRelays(params);
		int count = RdsJudicialCaseRelayMapper.countCaseRelays(params);
		response.setCount(count);
		response.setItems(caseConfirmModels);
		return response;
	}

	@Override
	public List<RdsJudicialConfirmCaseInfo> getRelayCaseInfo(
			Map<String, Object> params) {
		return RdsJudicialCaseRelayMapper.getRelayCaseInfo(params);
	}

	@Override
	public RdsJudicialResponse getPrintCaseCode(Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialConfirmCaseInfo> caseInfos = RdsJudicialCaseRelayMapper
				.getPrintCaseCode(params);
		int count = RdsJudicialCaseRelayMapper.countPrintCaseCode(params);
		response.setCount(count);
		response.setItems(caseInfos);
		return response;
	}

	@Override
	public List<RdsJudicialConfirmCaseInfo> getRelayCaseCode(
			Map<String, Object> params) {
		return RdsJudicialCaseRelayMapper.getRelayCaseCode(params);
	}

	@Override
	@Transactional
	public boolean saveCaseRelayInfo(Map<String, Object> params) {
		String xml_time = XmlParseUtil.getXmlValue(XML_PATH, "case_date");
		String now_time = com.rds.code.utils.StringUtils
				.dateToEight(new Date());
		DecimalFormat df = new DecimalFormat("0000");
		String relay_code = "";
		if (xml_time.equals(now_time)) {
			relay_code = now_time
					+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
							XML_PATH, "case_relay_num")) + 1);
		} else {
			XmlParseUtil.updateXmlValue(XML_PATH, "case_date", now_time);
			XmlParseUtil.updateXmlValue(XML_PATH, "case_relay_num", "1");
			relay_code = now_time
					+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
							XML_PATH, "case_relay_num")));
		}
		XmlParseUtil.updateXmlValue(XML_PATH, "case_relay_num", String
				.valueOf(Integer.parseInt(XmlParseUtil.getXmlValue(XML_PATH,
						"case_relay_num")) + 1));
		RdsJudicialCaseRelayModel caseRelayModel = new RdsJudicialCaseRelayModel(
				UUIDUtil.getUUID(), CASE_SIGN + relay_code, params.get(
						"relay_per").toString(), params.get("relay_remark")
						.toString(),params.get("relay_check").toString(),params.get("relay_checktwo").toString(),params.get("relay_Gluing").toString(),params.get("relay_Seal").toString(),params.get("relay_split").toString());
		boolean result = RdsJudicialCaseRelayMapper
				.saveCaseRelayInfo(caseRelayModel);
		RdsUpcUserModel user = (RdsUpcUserModel) params.get("user");
		if (result) {
			String case_ids = params.get("case_ids").toString();
			String[] cases = case_ids.split(",");
			for (String case_id : cases) {
				RdsJudicialConfirmCaseInfo caseInfo = new RdsJudicialConfirmCaseInfo(
						case_id, caseRelayModel.getRelay_id());
				RdsJudicialCaseRelayMapper.addCaseInfo(caseInfo);
			}
			String[] case_codes = params.get("case_code").toString().split(",");
			// 案例打印确认流程
			Map<String, Object> variables = new HashMap<>();
			for (String string : case_codes) {
				rdsActivitiJudicialService.runByCaseCode(string, variables,
						user);
			}
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public boolean updateCaseRelayInfo(Map<String, Object> params) {
		RdsJudicialCaseRelayModel caseRelayModel = new RdsJudicialCaseRelayModel(
				params.get("relay_id").toString(), params.get("relay_remark")
						.toString());
		boolean result = RdsJudicialCaseRelayMapper
				.updateCaseRelayInfo(caseRelayModel);
		if (result) {
			RdsJudicialCaseRelayMapper.deleteCaseInfo(caseRelayModel);
			String case_ids = params.get("case_ids").toString();
			String[] cases = case_ids.split(",");
			for (String case_id : cases) {
				RdsJudicialConfirmCaseInfo caseInfo = new RdsJudicialConfirmCaseInfo(
						case_id, caseRelayModel.getRelay_id());
				RdsJudicialCaseRelayMapper.addCaseInfo(caseInfo);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteRelayCaseInfo(Map<String, Object> params) {
		return RdsJudicialCaseRelayMapper.deleteRelayCaseInfo(params);
	}

	@Override
	public List<RdsJudicialSampleReciveModel> getCaseInfo(String relay_id) {
		return RdsJudicialCaseRelayMapper.getCaseInfo(relay_id);
	}

	@Override
	@Transactional
	public boolean confirmCaseRelayInfo(Map<String, Object> params) {
		RdsJudicialCaseConfirmModel caseConfirmModel = new RdsJudicialCaseConfirmModel(
				UUIDUtil.getUUID(), params.get("confirm_per").toString(),
				params.get("confirm_remark").toString(), params.get("relay_id")
						.toString());
		boolean result = RdsJudicialCaseRelayMapper
				.confirmCaseRelayInfo(caseConfirmModel);
		if (result) {
			RdsJudicialCaseRelayMapper.updateCaseInfoToFalse(params);
			String case_ids = params.get("case_ids").toString();
			if (StringUtils.isNotEmpty(case_ids)) {
				String[] cases = case_ids.split(",");
				for (String case_id : cases) {
					RdsJudicialConfirmCaseInfo caseInfo = new RdsJudicialConfirmCaseInfo(
							case_id, params.get("relay_id").toString());
					RdsJudicialCaseRelayMapper.updateCaseInfo(caseInfo);
				}
			}

			RdsUpcUserModel user = (RdsUpcUserModel) params.get("user");
			List<String> list =  RdsJudicialCaseRelayMapper.getCaseConfirmCode(params.get("relay_id").toString());
			if(list.size() > 0)
			{
				List<String> temp = getValues(params.get("case_code"));
				List<String> listTemp =getDiffrent(temp,list);
				if(listTemp.size()>0)
				{
					for (String string : listTemp) {
						if(StringUtils.isNotEmpty(string)){
							Map<String, Object> variables = new HashMap<>();
							variables.put("isreportpass", 0);
							variables.put("comment", params.get("confirm_remark"));
							rdsActivitiJudicialService.runByCaseCode(string, variables,
									user);
//							rdsActivitiJudicialService.addComment(string, variables, user);
						}
					}
				}
			}
			
			// 案例打印交接确认流程
			String[] case_code = params.get("case_code").toString().split(",");
			for (String string : case_code) {
				System.out.println(string+"---------------------------------------------------");
				// 确认通过案例
				Map<String, Object> variables = new HashMap<>();
				variables.put("isreportpass", 1);
				variables.put("comment", params.get("confirm_remark"));
				rdsActivitiJudicialService.runByCaseCode(string, variables,
						user);
				Map<String, Object> mapTemp = new HashMap<String, Object>();
				mapTemp.put("case_code", string);
				mapTemp.put("verify_state", 8);
				RdsJudicialSampleRelayMapper.updateCaseVerifyBycode(mapTemp);
			}

			return true;
		}
		return false;
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
	
	public static List<String> getValues(Object object) {
		List<String> values = new ArrayList<String>();
		if (object != null) {
			String str = object.toString();
			String[] objects = str.split(",");
			if (objects.length > 1) {
//				str = str.substring(1, str.length() - 1);
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
	public List<RdsJudicialConfirmCaseInfo> getPrintCaseCodeOnline(
			String relay_id) {
		return RdsJudicialCaseRelayMapper.getPrintCaseCodeOnline(relay_id);
	}

	@Override
	public void exportCaseCode(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String relay_id=params.get("relay_id").toString();
		List<RdsJudicialConfirmCaseInfo> list =RdsJudicialCaseRelayMapper.getPrintCaseCodeOnline(relay_id);
		String filename=list.get(0).getRelay_code();
		Object[] titles = { "案例编号", "交接日期","报告核对员1","报告核对员2","胶装人员","盖章人员","拆分人员"};
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsJudicialConfirmCaseInfo rdsJudicialConfirmCaseInfo = (RdsJudicialConfirmCaseInfo) list
					.get(i);
			objects.add(rdsJudicialConfirmCaseInfo.getCase_code());
			objects.add(rdsJudicialConfirmCaseInfo.getRelay_time());
            	objects.add(rdsJudicialConfirmCaseInfo.getRelay_check());
            	objects.add(rdsJudicialConfirmCaseInfo.getRelay_checktwo());
            	objects.add(rdsJudicialConfirmCaseInfo.getRelay_Gluing());
            	objects.add(rdsJudicialConfirmCaseInfo.getRelay_Seal());
            	objects.add(rdsJudicialConfirmCaseInfo.getRelay_split());
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, filename);
	
		
	}

}
