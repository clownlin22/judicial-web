package com.rds.judicial.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.mapper.RdsJudicialSampleRelayMapper;
import com.rds.judicial.model.RdsJudicialDicAreaModel;
import com.rds.judicial.model.RdsJudicialRelaySampleInfo;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleRelayModel;
import com.rds.judicial.service.RdsJudicialSampleRelayService;
import com.rds.upc.model.RdsUpcUserModel;

@RequestMapping("judicial/sampleRelay")
@Controller
public class RdsJudicialSampleRelayController {

	@Autowired
	private RdsJudicialSampleRelayService RdsJudicialSampleRelayService;
	
	@Autowired
	RdsJudicialSampleRelayMapper   SampleRelayMapper;

	@RequestMapping("getSampleReceiveInfo")
	@ResponseBody
	public RdsJudicialResponse getSampleReceiveInfos(
			@RequestBody Map<String, Object> params, HttpSession session) {
		String receive_per = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			receive_per = user.getUserid();
		}
		params.put("receive_per", receive_per);
		return RdsJudicialSampleRelayService.getSampleReceiveInfos(params);
	}

	@RequestMapping("getReceiveSampleInfo")
	@ResponseBody
	public List<RdsJudicialRelaySampleInfo> getReceiveSampleInfo(
			@RequestBody Map<String, Object> params) {
		return RdsJudicialSampleRelayService.getReceiveSampleInfo(params);
	}
	
	@RequestMapping("onSampleExport")
	@ResponseBody
	public void onSampleExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		String receive_id=request.getParameter("receive_id");
		String relay_code=request.getParameter("relay_code");
		params.put("relay_code", relay_code);
		 params.put("receive_id", receive_id);
		 RdsJudicialSampleRelayService.exportSampleCode(params, response);
		
	}

	@RequestMapping("exsitReceiveSampleCode")
	@ResponseBody
	public boolean exsitReceiveSampleCode(
			@RequestBody Map<String, Object> params) {
		return RdsJudicialSampleRelayService.exsitReceiveSampleCode(params);
	}
	
	@RequestMapping("onSubmitSample")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Object onSubmitSample(@RequestBody Map<String, Object> params,HttpSession session) {
		List<String> case_ids = getValues(params.get("case_ids"));
        params.put("case_ids", case_ids);
        List<String> sample_code =	SampleRelayMapper.querySampleCodeByCaseid(params);
		params.put("sample_code", sample_code);
		List<String> case_codes = SampleRelayMapper.queryCaseCodeByCaseids(params);
		params.put("case_codes", case_codes);
		String receive_remark="";
		params.put("receive_remark", receive_remark);
		String receive_per = "";
		String relay_per = "";
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
			receive_per = user.getUserid();
			relay_per = user.getUserid();
		}
		if (StringUtils.isEmpty(receive_per)) {
			return false;
		}
		params.put("receive_per", receive_per);
		params.put("relay_per", relay_per);
		Map<String, Object> result = (Map<String, Object>) RdsJudicialSampleRelayService
				.saveReceiveSampleInfoAuto(params, user);
		//判断是否交接成功，成功则自动样本提交确认
		if (!(boolean) result.get("result")) {
			return result;
		} else {
			//查找提交审核样本
			List<String> listSampleId = RdsJudicialSampleRelayService
					.querySampleIdByRece(result);
			params.put("sample_ids", listSampleId);
			params.put(
					"relay_remark",
					params.get("receive_remark") == null ? "" : params
							.get("receive_remark"));
			//返回样本提交审核结果
			return RdsJudicialSampleRelayService.saveRelaySampleInfoNowAuto(params,
					user);
		}
		
}
	@SuppressWarnings("unchecked")
	@RequestMapping("saveReceiveSampleInfo")
	@ResponseBody
	public Object saveReceiveSampleInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		String receive_per = "";
		String relay_per = "";
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
			receive_per = user.getUserid();
			relay_per = user.getUserid();
		}
		if (StringUtils.isEmpty(receive_per)) {
			return false;
		}
		params.put("receive_per", receive_per);
		params.put("relay_per", relay_per);
		Map<String, Object> result = (Map<String, Object>) RdsJudicialSampleRelayService
				.saveReceiveSampleInfo(params, user);
		//判断是否交接成功，成功则自动样本提交确认
		if (!(boolean) result.get("result")) {
			return result;
		} else {
			//查找提交审核样本
			List<String> listSampleId = RdsJudicialSampleRelayService
					.querySampleIdByRece(result);
			params.put("sample_ids", listSampleId);
			params.put(
					"relay_remark",
					params.get("receive_remark") == null ? "" : params
							.get("receive_remark"));
			//返回样本提交审核结果
			return RdsJudicialSampleRelayService.saveRelaySampleInfoNow(params,
					user);
		}
	}

	@RequestMapping("deleteReceiveSampleInfo")
	@ResponseBody
	public boolean deleteReceiveSampleInfo(
			@RequestBody Map<String, Object> params) {
		return RdsJudicialSampleRelayService.deleteReceiveSampleInfo(params);
	}

	@RequestMapping("updateReceiveSampleInfo")
	@ResponseBody
	public boolean updateReceiveSampleInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		return RdsJudicialSampleRelayService.updateReceiveSampleInfo(params);
	}

	/************************************************************************* 交接部分 *********************************/
	@RequestMapping("getSampleRelayInfo")
	@ResponseBody
	public RdsJudicialResponse getSampleRelayInfos(
			@RequestBody Map<String, Object> params, HttpSession session) {
		String relay_per = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			relay_per = user.getUserid();
		}
		params.put("relay_per", relay_per);
		return RdsJudicialSampleRelayService.getSampleRelayInfos(params);
	}

	@RequestMapping("getConfirmSampleRelayInfo")
	@ResponseBody
	public RdsJudicialResponse getConfirmSampleRelayInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		String partner_name = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			partner_name = user.getParnter_name();
		}
		params.put("partner_name", partner_name);
		return RdsJudicialSampleRelayService.getSampleRelayInfos(params);
	}

	@RequestMapping("getRelaySampleInfo")
	@ResponseBody
	public List<RdsJudicialRelaySampleInfo> getRelaySampleInfo(
			@RequestBody Map<String, Object> params) {
		return RdsJudicialSampleRelayService.getRelaySampleInfo(params);
	}

	@RequestMapping("saveRelaySampleInfo")
	@ResponseBody
	public Object saveRelaySampleInfo(@RequestBody Map<String, Object> params,
			HttpSession session) {
		String relay_per = "";
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
			relay_per = user.getUserid();
		}
		if (StringUtils.isEmpty(relay_per)) {
			return false;
		}
		params.put("relay_per", relay_per);
		return RdsJudicialSampleRelayService.saveRelaySampleInfo(params, user);
	}

	@RequestMapping("getSelectSampleInfo")
	@ResponseBody
	public List<RdsJudicialRelaySampleInfo> getSelectSampleInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		String receive_per = "";
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
			receive_per = user.getUserid();
		}
		if (StringUtils.isEmpty(receive_per)) {
			return null;
		}
		params.put("receive_per", receive_per);
		return RdsJudicialSampleRelayService.getSelectSampleInfo(params);
	}

	@RequestMapping("deleteRelaySampleInfo")
	@ResponseBody
	public boolean deleteRelaySampleInfo(@RequestBody Map<String, Object> params) {
		return RdsJudicialSampleRelayService.deleteRelaySampleInfo(params);
	}

	@RequestMapping("updateRelaySampleInfo")
	@ResponseBody
	public boolean updateRelaySampleInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		return RdsJudicialSampleRelayService.updateRelaySampleInfo(params);
	}

	@RequestMapping("confirmRelaySampleInfo")
	@ResponseBody
	public Object confirmRelaySampleInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		String confirm_per = "";
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
			confirm_per = user.getUserid();
		}
		if (StringUtils.isEmpty(confirm_per)) {
			return false;
		}
		params.put("confirm_per", confirm_per);
		return RdsJudicialSampleRelayService.confirmRelaySampleInfo(params,
				user);
	}

	@RequestMapping("getSampleInfo")
	@ResponseBody
	public List<RdsJudicialDicAreaModel> getSampleInfo(String relay_id) {
		return RdsJudicialSampleRelayService.getSampleInfo(relay_id);
	}

	/******************* 打印模块 ************************/
	@RequestMapping("printRelay")
	public String printRelay(String relay_id, HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("relay_id", relay_id);
		List<RdsJudicialRelaySampleInfo> relaySampleInfos = RdsJudicialSampleRelayService
				.getRelaySampleInfo(params);
		List<RdsJudicialRelaySampleInfo> relaySampleInfos1 = null;
		List<RdsJudicialRelaySampleInfo> relaySampleInfos2 = null;
		List<RdsJudicialRelaySampleInfo> relaySampleInfos3 = null;
		List<RdsJudicialRelaySampleInfo> relaySampleInfos4 = null;
		int countSample=relaySampleInfos.size();
		if(countSample<=35)
		{
			relaySampleInfos1 = relaySampleInfos.subList(0,
					relaySampleInfos.size());
		}else if(countSample>35 && countSample<=70)
		{
			relaySampleInfos1 = relaySampleInfos.subList(0,35);
			relaySampleInfos2 = relaySampleInfos.subList(35, relaySampleInfos.size());
		}else if(countSample>70 && countSample<=105)
		{
			relaySampleInfos1 = relaySampleInfos.subList(0,35);
			relaySampleInfos2 = relaySampleInfos.subList(35, 70);
			relaySampleInfos3 = relaySampleInfos.subList(70, relaySampleInfos.size());
		}else if(countSample>105 && countSample<=140)
		{
			relaySampleInfos1 = relaySampleInfos.subList(0,35);
			relaySampleInfos2 = relaySampleInfos.subList(35, 70);
			relaySampleInfos3 = relaySampleInfos.subList(70, 105);
			relaySampleInfos4 = relaySampleInfos.subList(105, relaySampleInfos.size());
		}else
		{
			request.setAttribute("overload", 0);
		}
		
		
//		if (relaySampleInfos.size() % 2 == 0) {
//			relaySampleInfos1 = relaySampleInfos.subList(0,
//					relaySampleInfos.size() / 2);
//			relaySampleInfos2 = relaySampleInfos.subList(
//					relaySampleInfos.size() / 2, relaySampleInfos.size());
//		} else {
//			relaySampleInfos1 = relaySampleInfos.subList(0,
//					relaySampleInfos.size() / 2 + 1);
//			relaySampleInfos2 = relaySampleInfos.subList(
//					relaySampleInfos.size() / 2 + 1, relaySampleInfos.size());
//		}
		request.setAttribute("relaySampleInfos1", relaySampleInfos1);
		request.setAttribute("relaySampleInfos2", relaySampleInfos2);
		request.setAttribute("relaySampleInfos3", relaySampleInfos3);
		request.setAttribute("relaySampleInfos4", relaySampleInfos4);
		RdsJudicialSampleRelayModel relayModel = RdsJudicialSampleRelayService
				.getRelaySample(params);
		request.setAttribute("relayModel", relayModel);
		request.setAttribute("countSample", countSample);
		return "dna/relay_confirm";
	}

	@RequestMapping("getSampleCaseCode")
	@ResponseBody
	public RdsJudicialResponse getSampleCaseCode(
			@RequestBody Map<String, Object> params, HttpSession session) {
		String partner_name = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			partner_name = user.getParnter_name();
		}
		params.put("partner_name", partner_name);
		return RdsJudicialSampleRelayService.queryCaseCodeBySampleCode(params);
	}

	@RequestMapping("updateCaseReportmodel")
	@ResponseBody
	public boolean updateCaseReportmodel(
			@RequestBody Map<String, Object> params, HttpSession session) {
		return RdsJudicialSampleRelayService.updateCaseReportmodel(params);
	}

	@RequestMapping("exportCaseCodeBySampleCode")
	public void exportCaseCodeBySampleCode(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("case_code", request.getParameter("case_code"));
		params.put("starttime", request.getParameter("starttime"));
		params.put("endtime", request.getParameter("endtime"));
		params.put("sample_code", request.getParameter("sample_code"));
		params.put("sample_starttime", request.getParameter("sample_starttime"));
		params.put("sample_endtime", request.getParameter("sample_endtime"));
		RdsJudicialSampleRelayService.exportCaseCodeBySampleCode(params,
				response);
	}
	public static List<String> getValues(Object object) {
		List<String> values = new ArrayList<String>();
		if (object != null) {
			String str = object.toString();
			String[] objects = str.split(",");
			if (objects.length > 1) {
				str = str.substring(0, str.length());
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

}
