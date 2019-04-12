package com.rds.alcohol.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.alcohol.model.RdsAlcoholAttachmentModel;
import com.rds.alcohol.model.RdsAlcoholCaseInfoModel;
import com.rds.alcohol.model.RdsAlcoholQueryParam;
import com.rds.alcohol.model.RdsAlcoholResponse;
import com.rds.alcohol.model.RdsAlcoholSampleInfoModel;
import com.rds.alcohol.service.RdsAlcoholRegisterService;
import com.rds.judicial.model.RdsJudicialCaseFeeModel;
import com.rds.upc.model.RdsUpcMessageModel;
import com.rds.upc.model.RdsUpcUserModel;

import lombok.Getter;
import lombok.Setter;

@RequestMapping("alcohol/register")
@Controller
public class RdsAlcoholRegisterController {
	@Getter
	@Setter
	private String KEY = "id";

	@Autowired
	private RdsAlcoholRegisterService RdsAlcoholRegisterService;

	/**
	 *
	 获取鉴定人
	 */
	@RequestMapping(value = "getIdentificationPer")
	@ResponseBody
	public Map<String, Object> getIdentificationPer() {
		return RdsAlcoholRegisterService.getIdentificationPer();
	}

	/**
	 * 获取案例信息
	 * 
	 * @param params
	 * @param session
	 * @return
	 */
	@RequestMapping("getCaseInfo")
	@ResponseBody
	public RdsAlcoholResponse getCaseInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		String case_in_per = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			case_in_per = user.getSql_str2();
		}
		params.put("case_in_per", case_in_per);
		return RdsAlcoholRegisterService.getCaseInfo(params);
	}

	/**
	 * 作废案例信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("deleteCaseInfo")
	@ResponseBody
	public boolean deleteCaseInfo(@RequestBody Map<String, Object> params) {
		return RdsAlcoholRegisterService.deleteCaseInfo(params);
	}

	/**
	 * 查重
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("exsitCaseCode")
	@ResponseBody
	public boolean exsitCaseCode(@RequestBody Map<String, Object> params) {
		String case_code = "";
		if (params.get("case_code") != null) {
			case_code = params.get("case_code").toString();
		}
		return RdsAlcoholRegisterService.exsitCaseCode(case_code);
	}



	/**
	 * 获取样本
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getSampleInfo")
	@ResponseBody
	public RdsAlcoholSampleInfoModel getSampleInfo(
			@RequestBody Map<String, Object> params) {
		return RdsAlcoholRegisterService.getSampleInfo(params);
	}

	/**
	 * 新增登记
	 * 
	 * @param caseInfoModel
	 * @param sampleInfomodel
	 * @param session
	 * @param response
	 * @param files
	 */
	@RequestMapping("addCaseInfo")
	public void addCaseInfo(RdsAlcoholCaseInfoModel caseInfoModel,
			RdsAlcoholSampleInfoModel sampleInfomodel,
			RdsJudicialCaseFeeModel casefee, HttpSession session,
			HttpServletResponse response, @RequestParam MultipartFile[] files,RdsAlcoholAttachmentModel att) {
		String case_in_per = "";
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			case_in_per = user.getUserid();
		}
		caseInfoModel.setCase_in_per(case_in_per);
		boolean result = false;
		if (StringUtils.isNotEmpty(case_in_per)) {
			result = RdsAlcoholRegisterService.addCaseInfo(caseInfoModel,
					sampleInfomodel, casefee, files,att);
		}
		try {
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取图片文件
	 */
	@RequestMapping("getImg")
	public void getImg(HttpServletResponse response, String filename) {
		RdsAlcoholRegisterService.getImg(response, filename);
	}

	public RdsUpcMessageModel setModel(boolean result, String message){
		RdsUpcMessageModel model = new RdsUpcMessageModel();
		model.setResult(result);
		model.setMessage(message);
		return model;
	}

	/**
	 * 更新案例
	 * 
	 * @param caseInfoModel
	 * @param session
	 * @param sampleInfoModel
	 * @param response
	 * @param files
	 */

	@RequestMapping("updateCaseInfo")
	@ResponseBody
	public RdsUpcMessageModel updateCaseInfo(RdsAlcoholCaseInfoModel caseInfoModel,
			HttpSession session, RdsAlcoholSampleInfoModel sampleInfoModel,
			HttpServletResponse response) {
	 
			RdsUpcMessageModel object = RdsAlcoholRegisterService.updateCaseCode(
					caseInfoModel, sampleInfoModel)?this.setModel(true, "修改成功"):this.setModel(false, "修改失败");
		return object;

	}


	//没有附件的
	//	@RequestMapping("updateCaseInfo")  
	//	public void updateCaseInfo(RdsAlcoholCaseInfoModel caseInfoModel,
	//			HttpSession session, RdsAlcoholSampleInfoModel sampleInfoModel,
	//			HttpServletResponse response, @RequestParam MultipartFile[] files) {
	//		boolean result = RdsAlcoholRegisterService.updateCaseCode(
	//				caseInfoModel, sampleInfoModel, files);
	//		try {
	//			response.setContentType("text/html; charset=utf-8");
	//			response.getWriter().print(result);
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}

	@RequestMapping("exportCaseInfo")
	public void exportCaseInfo(RdsAlcoholQueryParam param,
			HttpServletResponse response) {
		RdsAlcoholRegisterService.exportCaseInfo(param, response);
	}

	@RequestMapping("getClient")
	@ResponseBody
	public Map<String, Object> getClient() {
		return RdsAlcoholRegisterService.getClient();
	}
	@RequestMapping("getClient2")
	@ResponseBody
	public Map<String, Object> getClient2() {
		return RdsAlcoholRegisterService.getClient2();
	}

	/**
	 * 导出毒物检测信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("caseInfoExport")
	public void caseInfoExport(HttpServletResponse response,
			HttpServletRequest request, HttpSession session) {
		try {

			Map<String, Object> params = new HashMap<String, Object>();
			String case_code = request.getParameter("case_code");
			params.put("case_code", case_code);
			String client = request.getParameter("client");
			params.put("client", client);								//委托人
			String checkper = request.getParameter("checkper");
			params.put("checkper", checkper);								//送检人
			String checkper_phone = request.getParameter("checkper_phone");
			params.put("checkper_phone", checkper_phone);					//送检人电话
			String receiver = request.getParameter("receiver");
			params.put("receiver", receiver);								//归属人'
			String area_code = request.getParameter("areacode");
			if(area_code.equals("null")){
				params.put("area_code", null);								//'所属地区'
			}else{
				params.put("area_code", area_code.trim());								//'所属地区'
			}
			String case_in_pername = request.getParameter("case_in_pername");
			params.put("case_in_pername", case_in_pername);								//'登记人'
			String mail_address = request.getParameter("mail_address");
			params.put("mail_address", mail_address);						//'邮寄地址'
			String mail_per = request.getParameter("mail_per");
			params.put("mail_per", mail_per);								//邮件接收人'
			String mail_phone = request.getParameter("mail_phone");
			params.put("mail_phone", mail_phone);								//联系电话'
			String isDoubleTube = request.getParameter("isDoubleTube");
			params.put("isDoubleTube", isDoubleTube);								//血管类型
			String sample_time_start = request.getParameter("sample_time_start");
			params.put("sample_time_start", sample_time_start);								//登记日期
			String sample_time_end = request.getParameter("sample_time_end");
			params.put("sample_time_end", sample_time_end);
			
			
			
			String state = request.getParameter("state");
			params.put("state", state);
			String starttime = request.getParameter("starttime");
			params.put("starttime", starttime);
			String endtime = request.getParameter("endtime");
			params.put("endtime", endtime);
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			params.put("roleType", user.getRoletype());
			params.put("userCode", user.getUsercode());
			RdsAlcoholRegisterService.exportCaseInfo(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
