package com.rds.judicial.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.model.RdsJudicialConfirmCaseInfo;
import com.rds.judicial.model.RdsJudicialRelaySampleInfo;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleReciveModel;
import com.rds.judicial.model.RdsJudicialSampleRelayModel;
import com.rds.judicial.service.RdsJudicialCaseRelayService;
import com.rds.upc.model.RdsUpcUserModel;

@RequestMapping("judicial/caseRelay")
@Controller
public class RdsJudicialCaseRelayController {
      
	@Autowired
	private RdsJudicialCaseRelayService RdsJudicialCaseRelayService;
	
	@RequestMapping("getCaseRelayInfo")
	@ResponseBody
	public RdsJudicialResponse getCaseRelayInfo(@RequestBody Map<String,Object> params,HttpSession session){
		String relay_per="";
		String partner_name="";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			relay_per = user.getUserid();
			partner_name = user.getParnter_name();
        }
		params.put("relay_per",relay_per);
		params.put("partner_name", partner_name);
		return RdsJudicialCaseRelayService.getCaseRelayInfo(params);
	}
	
	@RequestMapping("getRelayCaseInfo")
	@ResponseBody
	public List<RdsJudicialConfirmCaseInfo> getRelayCaseInfo(@RequestBody Map<String,Object> params){
		return RdsJudicialCaseRelayService.getRelayCaseInfo(params);
	}
	
	@RequestMapping("getPrintCaseCode")
	@ResponseBody
	public RdsJudicialResponse getPrintCaseCode(@RequestBody Map<String,Object> params,HttpSession session){
		String partner_name="";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			partner_name = user.getParnter_name();
        }
		params.put("partner_name", partner_name);
		return RdsJudicialCaseRelayService.getPrintCaseCode(params);
	}
	
	@RequestMapping("getRelayCaseCode")
	@ResponseBody
	public List<RdsJudicialConfirmCaseInfo> getRelayCaseCode(@RequestBody Map<String,Object> params){
		return RdsJudicialCaseRelayService.getRelayCaseCode(params);
	}
	
	@RequestMapping("saveCaseRelayInfo")
	@ResponseBody
	public boolean saveCaseRelayInfo(@RequestBody Map<String,Object> params,HttpSession session){
		String relay_per="";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			relay_per = user.getUserid();
			params.put("user", user);
        }
		params.put("relay_per",relay_per);
		return RdsJudicialCaseRelayService.saveCaseRelayInfo(params);
	}
	
	@RequestMapping("updateCaseRelayInfo")
	@ResponseBody
	public boolean updateCaseRelayInfo(@RequestBody Map<String,Object> params){
		return RdsJudicialCaseRelayService.updateCaseRelayInfo(params);
	}
	
	@RequestMapping("deleteRelayCaseInfo")
	@ResponseBody
	public boolean deleteRelayCaseInfo(@RequestBody Map<String,Object> params){
		return RdsJudicialCaseRelayService.deleteRelayCaseInfo(params);
	}
	
	@RequestMapping("getCaseInfo")
	@ResponseBody
	public List<RdsJudicialSampleReciveModel> getCaseInfo(String relay_id){
		return RdsJudicialCaseRelayService.getCaseInfo(relay_id);
	}
	
	@RequestMapping("confirmCaseRelayInfo")
	@ResponseBody
	public boolean confirmCaseRelayInfo(@RequestBody Map<String,Object> params,HttpSession session){
		String confirm_per="";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			confirm_per = user.getUserid();
			params.put("user", user);
        }
		params.put("confirm_per",confirm_per);
		return RdsJudicialCaseRelayService.confirmCaseRelayInfo(params);
	}
	
	@RequestMapping("printCaseRelay")
	public String printCaseRelay(String relay_id, HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("relay_id", relay_id);
		List<RdsJudicialConfirmCaseInfo> relaySampleInfos = RdsJudicialCaseRelayService.getPrintCaseCodeOnline(relay_id);
		List<RdsJudicialConfirmCaseInfo> relaySampleInfos1 = null;
		List<RdsJudicialConfirmCaseInfo> relaySampleInfos2 = null;
		List<RdsJudicialConfirmCaseInfo> relaySampleInfos3 = null;
		List<RdsJudicialConfirmCaseInfo> relaySampleInfos4 = null;
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
		
		request.setAttribute("relay_code", relaySampleInfos.get(0).getRelay_code());
		request.setAttribute("relay_remark", relaySampleInfos.get(0).getRelay_remark());
		request.setAttribute("relay_time", relaySampleInfos.get(0).getRelay_time());
		request.setAttribute("countSample", countSample);
		return "dna/case_relay_print";
	}


	@RequestMapping("exportCaseCode")
	@ResponseBody
	public void exportCaseCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		String relay_id=request.getParameter("relay_id");
		 params.put("relay_id", relay_id);
		 RdsJudicialCaseRelayService.exportCaseCode(params, response);
		
	}
}
