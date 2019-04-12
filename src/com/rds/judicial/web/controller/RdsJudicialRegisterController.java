package com.rds.judicial.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.bacera.mapper.RdsBaceraIdentifyMapper;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.file.XmlParseUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.finance.mapper.RdsCaseFinanceMapper;
import com.rds.finance.service.RdsFinanceChargeStandardService;
import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialParamsModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleExpressModel;
import com.rds.judicial.model.RdsJudicialSampleInfoModel;
import com.rds.judicial.service.RdsJudicialCaseAttachmentService;
import com.rds.judicial.service.RdsJudicialRegisterService;
import com.rds.upc.model.RdsUpcUserModel;

@Controller
@RequestMapping("judicial/register")
public class RdsJudicialRegisterController {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String USER_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "user_permit");

	private static final String ZY_REORT_MODEL = PropertiesUtils.readValue(
			FILE_PATH, "zyReportModel");

	@Setter
	@Autowired
	private RdsJudicialRegisterService RdsJudicialRegisterService;

	@Setter
	@Autowired
	private RdsJudicialCaseAttachmentService rdsJudicialCaseAttachmentService;

	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;

	@Autowired
	private RdsFinanceChargeStandardService rdsFinanceChargeStandardService;

	@Autowired
	private RdsBaceraIdentifyMapper rdsBaceraIdentifyMapper;

	@Autowired
	private RdsJudicialRegisterMapper rdsJudicialRegisterMapper;

	@Autowired
	private RdsCaseFinanceMapper caseFeeMapper;
	@Getter
	@Setter
	private String KEY = "id";

	/**
	 * 根据条件获取案例的基本信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getCaseInfo")
	@ResponseBody
	public RdsJudicialResponse getCaseInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		// String sql_str = "";
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
			// sql_str = user.getSql_str();
		}
		if (!USER_PERMIT.contains(user.getUsercode())) {
			if (null != user.getAreacode() && !"".equals(user.getAreacode())) {
				// if (null != user.getParnter_name()
				// && !"".equals(user.getParnter_name())) {
				// params.put("receiver_area_user", user.getReceiver_area());
				// params.put("parnter_name", user.getParnter_name());
				// } else
				params.put("receiver_area_user", user.getReceiver_area());
			} else {
				params.put("receiver_area_user", user.getReceiver_area());
				// params.put("parnter_name", user.getParnter_name());
				params.put("userid", user.getUserid());
			}
		}
		return RdsJudicialRegisterService.getCaseInfo(params);
	}

	/**
	 * 根据条件获取案例的基本信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getExperimentCaseInfo")
	@ResponseBody
	public RdsJudicialResponse getExperimentCaseInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		// String sql_str = "";
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
			// sql_str = user.getSql_str();
		}
		if (!USER_PERMIT.contains(user.getUsercode())) {
			params.put("parnter_name", user.getParnter_name());
		}
		return RdsJudicialRegisterService.getCaseInfo(params);
	}

	/**
	 * 删除案例的信息
	 * 
	 * @param params
	 *            case_id
	 * @return
	 */
	@RequestMapping("deleteCaseInfo")
	@ResponseBody
	public boolean deleteCaseInfo(@RequestBody Map<String, Object> params,HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		boolean result =false;
		if("6c7485822dd14b6185aad519bf7387ea".equals(user.getUserid().toString())||"6847ae28b8f34c27aa8ed97558fa5559".equals(user.getUserid().toString())){
		 result = RdsJudicialRegisterService.deleteCaseInfo(params);
		return result;
		}
		return result;
	}

	/**
	 * 获取案例的样本信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getSampleInfo")
	@ResponseBody
	public RdsJudicialResponse getSampleInfo(
			@RequestBody Map<String, Object> params) {
		return RdsJudicialRegisterService.getSampleInfo(params);
	}

	/**
	 * 获取案例关联信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getCaseSecond")
	@ResponseBody
	public RdsJudicialResponse getCaseSecond(
			@RequestBody Map<String, Object> params) {
		RdsJudicialResponse resJudicialResponse = new RdsJudicialResponse();
		resJudicialResponse.setItems(RdsJudicialRegisterService
				.queryCaseCodeSecond(params));
		return resJudicialResponse;
	}

	/**
	 * 保存案例信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("saveCaseInfo")
	@ResponseBody
	public Object saveCaseInfo(@RequestParam int[] filetype,
			@RequestParam MultipartFile[] files,
			@RequestParam String[] sample_code,
			@RequestParam String[] sample_username,
			@RequestParam String[] id_number, @RequestParam String[] special,
			@RequestParam String[] sample_type,
			@RequestParam String[] sample_call,
			@RequestParam String[] birth_date,
			@RequestParam (value="purpose",required=false)String purpose,
			@RequestParam(value="address",required=false)  String[] address,
			@RequestParam Map<String, Object> params,
			HttpServletRequest request, HttpServletResponse response) {
		// 随机案例id
		String case_id = UUIDUtil.getUUID();

		// 案例登记人
		String case_in_per = "";
		// 获取该案例登记人信息
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		case_in_per = user.getUserid();
		// 判断session过期
		if (StringUtils.isEmpty(case_in_per)) {
			Map<String, Object> resultTemp = new HashMap<String, Object>();
			resultTemp.put("success", true);
			resultTemp.put("result", false);
			resultTemp.put("message", "登录状态失效，请重新登录！");
			return resultTemp;
		}
       if(null !=purpose){
		purpose=purpose.replaceAll(",","和");
        purpose=purpose.replaceAll(" ","");
          }
		// 样本信息
		params.put("sample_code", sample_code);
		params.put("sample_username", sample_username);
		params.put("id_number", id_number);
		params.put("special", special);
		params.put("sample_type", sample_type);
		params.put("sample_call", sample_call);
		params.put("birth_date", birth_date);
		params.put("address", address);
		params.put("case_in_per", case_in_per);
		params.put("case_id", case_id);
		// 上传图片信息
		params.put("filetype", filetype);
		params.put("files", files);
		params.put("purpose", purpose);
        
		// 判断样本编码是否上传过;外省条形码问题，先去除
		// List<String> sample_codes = getValues(sample_code);
		// for (String string : sample_codes) {
		// Map<String, Object> map = new HashMap<>();
		// map.put("sample_code", string);
		// boolean res = RdsJudicialRegisterService.exsitSampleCode(map);
		// // 如已上传返回结果
		// if (!res) {
		// Map<String, Object> resultTemp = new HashMap<String, Object>();
		// resultTemp.put("success", true);
		// resultTemp.put("result", false);
		// resultTemp.put("message", "样本条形码"+string+"已录入，请查看或联系管理员！");
		// return resultTemp;
		// }
		// if (RdsJudicialRegisterService.countApplySampleCode(map) == 0) {
		// Map<String, Object> resultTemp = new HashMap<String, Object>();
		// resultTemp.put("success", true);
		// resultTemp.put("result", false);
		// resultTemp.put("message", "样本条形码" + string + "未申请，请查看或联系管理员！");
		// return resultTemp;
		// }
		// }

		// 结果返回存放map
		Map<String, Object> result = new HashMap<>();
		// 从系统出报告则存在模版类型，否则为空
		String report_model = (params.get("report_model") == null) ? ""
				: params.get("report_model").toString();
		// 判断是否子渊的模版，先保存案例，审核时生成案例编号和样本条码
		if (ZY_REORT_MODEL.contains(report_model) && !"".equals(report_model)&&!"qsjdmodel".equals(report_model)) {
			// 案例保存（审核时生成案例号和样本编号重新生成）
			result = (Map<String, Object>) RdsJudicialRegisterService
					.saveZYCaseInfo(params, user);
		} else {
			// 案例保存（保存时生成案例号和样本编号）
			result = (Map<String, Object>) RdsJudicialRegisterService
					.saveCaseInfo(params, user);
		}
		// 案例流程控制map
		Map<String, Object> variables = new HashMap<String, Object>();
		// 判断是否直接提交审核
		if (null != params.get("submitFlag")) {
			// 不出报告标识
			// variables.put("isNoReport", 0);
			// 判断案例保存是否成功
			if (!(boolean) result.get("result"))
				return result;
			else {
				// 更新提交审核流程
				boolean result1 = rdsActivitiJudicialService.runByCaseCode(
						case_id, variables, user);
				if (result1) {
					Map<String, Object> resultTemp = new HashMap<String, Object>();
					resultTemp.put("case_id", case_id);
					// 更新登记表的案例状态
					RdsJudicialRegisterService
							.updateCaseVerifyState(resultTemp);
					resultTemp.put("result", true);
					resultTemp.put("success", true);
					resultTemp.put("message", "新增成功！");
					return resultTemp;
				} else {
					Map<String, Object> resultTemp = new HashMap<String, Object>();
					resultTemp.put("result", false);
					resultTemp.put("success", true);
					resultTemp.put("message", "流程提交失败，请联系管理员");
					return resultTemp;
				}
			}
		} else {
			// 案例有模版则为保存，或者service保存不成功直接提示
			// if (null != params.get("report_model")
			// || !(boolean) result.get("result"))
			return result;
			// else {
			// // 登记到邮寄流程标识
			// variables.put("isNoReport", 1);
			// //没有模版保存为直接到邮寄流程
			// boolean result1 = rdsActivitiJudicialService.runByCaseCode(
			// case_id, variables, user);
			// if (result1) {
			// Map<String, Object> resultTemp = new HashMap<String, Object>();
			// resultTemp.put("result", true);
			// resultTemp.put("success", true);
			// resultTemp.put("message", "新增成功！");
			// return resultTemp;
			// } else {
			// Map<String, Object> resultTemp = new HashMap<String, Object>();
			// resultTemp.put("result", false);
			// resultTemp.put("success", true);
			// resultTemp.put("message", "流程提交失败，请联系管理员");
			// return resultTemp;
			// }
			// }
		}

	}

	/**
	 * 保存案例信息,没有图片
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("saveCaseInfoTemp")
	@ResponseBody
	public Object saveCaseInfoTemp(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		// 随机案例id
		String case_id = UUIDUtil.getUUID();

		// 案例登记人
		String case_in_per = "";
		// 获取该案例登记人信息
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		case_in_per = user.getUserid();

		// 判断session过期
		if (StringUtils.isEmpty(case_in_per)) {
			return false;
		}
		params.put("case_in_per", case_in_per);
		params.put("case_id", case_id);

		// List<String> sample_codes = getValues(params.get("sample_code"));
		//
		// for (String string : sample_codes) {
		// Map<String, Object> map = new HashMap<>();
		// map.put("sample_code", string);

		// 判断样本编号是否存在
		// boolean res = RdsJudicialRegisterService.exsitSampleCode(map);
		// if (!res) {
		// Map<String, Object> resultTemp = new HashMap<String, Object>();
		// resultTemp.put("result", false);
		// resultTemp.put("message", "样本编号已存在，请查看！");
		// return resultTemp;
		// }
		// if (RdsJudicialRegisterService.countApplySampleCode(map) == 0) {
		// Map<String, Object> resultTemp = new HashMap<String, Object>();
		// resultTemp.put("success", true);
		// resultTemp.put("result", false);
		// resultTemp.put("message", "样本条形码" + string + "未申请，请查看或联系管理员！");
		// return resultTemp;
		// }
		// }

		Map<String, Object> result = (Map<String, Object>) RdsJudicialRegisterService
				.saveCaseInfo(params, user);
		Map<String, Object> variables = new HashMap<String, Object>();
		if (null != params.get("submitFlag")) {
			// 不出报告标识
			// variables.put("isNoReport", 0);
			// 判断案例保存是否成功
			if (!(boolean) result.get("result"))
				return result;
			else {
				// 更新提交审核流程
				boolean result1 = rdsActivitiJudicialService.runByCaseCode(
						case_id, variables, user);
				if (result1) {
					Map<String, Object> resultTemp = new HashMap<String, Object>();
					resultTemp.put("case_id", case_id);
					// 更新登记表的案例状态
					RdsJudicialRegisterService
							.updateCaseVerifyState(resultTemp);
					resultTemp.put("result", true);
					resultTemp.put("success", true);
					resultTemp.put("message", "新增成功！");
					return resultTemp;
				} else {
					Map<String, Object> resultTemp = new HashMap<String, Object>();
					resultTemp.put("result", false);
					resultTemp.put("success", true);
					resultTemp.put("message", "流程提交失败，请联系管理员");
					return resultTemp;
				}
			}
		} else {
			return result;
		}

		// variables.put("isNoReport", 1);
		// if (null != params.get("report_model")
		// || !(boolean) result.get("result"))
		// return result;
		// else {
		// boolean result1 = rdsActivitiJudicialService.runByCaseCode(case_id,
		// variables, user);
		// if (result1) {
		// Map<String, Object> resultTemp = new HashMap<String, Object>();
		// // resultTemp.put("case_id", case_id);
		// //
		// // RdsJudicialRegisterService.updateCaseVerifyState(resultTemp);
		// resultTemp.put("result", true);
		// resultTemp.put("success", true);
		// resultTemp.put("message", "新增成功！");
		// return resultTemp;
		// } else {
		// Map<String, Object> resultTemp = new HashMap<String, Object>();
		// resultTemp.put("result", false);
		// resultTemp.put("success", true);
		// resultTemp.put("message", "流程提交失败，请联系管理员");
		// return resultTemp;
		// }
		// }
	}

	/**
	 * 修改案例信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("updateChangeCaseInfo")
	@ResponseBody
	public Object updateChangeCaseInfo(@RequestBody Map<String, Object> params,
			HttpSession session) {

		// 案例登记人
		String case_in_per = "";
		// 获取该案例登记人信息
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		case_in_per = user.getUserid();
		// 判断session过期
		if (StringUtils.isEmpty(case_in_per)) {
			return false;
		}

		params.put("case_in_per", case_in_per);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap = (Map<String, Object>) RdsJudicialRegisterService
				.updateChangeCaseInfo(params, user);
		return resultMap;
	}

	/**
	 * 修改案例信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("updateCaseInfo")
	@ResponseBody
	public Object updateCaseInfo(@RequestBody Map<String, Object> params,
			HttpSession session) {

		// 案例登记人
		String case_in_per = "";
		// 获取该案例登记人信息
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		case_in_per = user.getUserid();
		// 判断session过期
		if (StringUtils.isEmpty(case_in_per)) {
			return false;
		}
		/** 判断案例编号是否存在 start **/
		Map<String, Object> map = new HashMap<>();
		map.put("case_id", params.get("case_id"));
		map.put("case_code", params.get("case_code"));   
		if (RdsJudicialRegisterService.exsitCaseCode(map)) {
			Map<String, Object> map1 = new HashMap<>();
			map1.put("case_code", params.get("case_code"));
			if (!RdsJudicialRegisterService.exsitCaseCode(map1)) {
				Map<String, Object> resultTemp = new HashMap<String, Object>();
				resultTemp.put("result", false);
				resultTemp.put("message", "案例编号已存在，请查看！");
				return resultTemp;
			}
		}
		/** 判断案例编号是否存在 end **/
		// 判断样本编码是否存在
		// List<String> sample_codes = getValues(params.get("sample_code"));
		//
		// for (String string : sample_codes) {
		// Map<String, Object> map = new HashMap<>();
		// map.put("case_id", params.get("case_id"));
		// map.put("sample_code", string);
		// // 判断样本编号是否存在
		// boolean res = RdsJudicialRegisterService.exsitSampleCode(map);
		// if (res) {
		// Map<String, Object> mapTemp = new HashMap<>();
		// mapTemp.put("sample_code", string);
		// boolean res1 = RdsJudicialRegisterService
		// .exsitSampleCode(mapTemp);
		// if (!res1) {
		// Map<String, Object> resultTemp = new HashMap<String, Object>();
		// resultTemp.put("result", false);
		// resultTemp.put("message", "样本编号已存在，请查看！");
		// return resultTemp;
		// }
		//
		// }
		// }
		String purpose=params.get("purpose").toString();
		if(null !=purpose && !"".equals(purpose)){
			purpose=purpose.substring(1, purpose.length()-1);
			purpose=purpose.replaceAll(",","和");
            purpose=purpose.replaceAll(" ","");
			params.put("purpose", purpose);
	          }
		params.put("case_in_per", case_in_per);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap = (Map<String, Object>) RdsJudicialRegisterService
				.updateCaseInfo(params, user);
		if ((boolean) resultMap.get("result")) {
			Map<String, Object> variables = new HashMap<String, Object>();
			// variables.put("isNoReport", 0);
			if (null != params.get("submit_flag")) {
				boolean result1 = rdsActivitiJudicialService.runByCaseCode(
						params.get("case_id").toString(), variables, user);
				if (result1) {
					Map<String, Object> resultTemp = new HashMap<String, Object>();
					resultTemp.put("case_id", params.get("case_id"));

					RdsJudicialRegisterService
							.updateCaseVerifyState(resultTemp);
					resultTemp.put("result", true);
					resultTemp.put("success", true);
					resultTemp.put("message", "操作成功！");
					return resultTemp;
				} else {
					Map<String, Object> resultTemp = new HashMap<String, Object>();
					resultTemp.put("result", false);
					resultTemp.put("success", true);
					resultTemp.put("message", "流程提交失败，请联系管理员");
					return resultTemp;
				}
			} else
				return resultMap;
		} else
			return resultMap;
	}

	/**
	 * 修改案例信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("updateCaseInfoNoRept")
	@ResponseBody
	public Object updateCaseInfoNoRept(@RequestBody Map<String, Object> params,
			HttpSession session) {

		// 案例登记人
		String case_in_per = "";
		// 获取该案例登记人信息
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		case_in_per = user.getUserid();
		// 判断session过期
		if (StringUtils.isEmpty(case_in_per)) {
			return false;
		}
		/** 判断案例编号是否存在 start **/
		Map<String, Object> map = new HashMap<>();
		map.put("case_id", params.get("case_id"));
		map.put("case_code", params.get("case_code"));

		if (RdsJudicialRegisterService.exsitCaseCode(map)) {
			Map<String, Object> map1 = new HashMap<>();
			map1.put("case_code", params.get("case_code"));
			if (!RdsJudicialRegisterService.exsitCaseCode(map1)) {
				Map<String, Object> resultTemp = new HashMap<String, Object>();
				resultTemp.put("result", false);
				resultTemp.put("message", "案例编号已存在，请查看！");
				return resultTemp;
			}
		}
		/** 判断案例编号是否存在 end **/
		// // 判断样本编码是否存在
		// List<String> sample_codes = getValues(params.get("sample_code"));
		//
		// for (String string : sample_codes) {
		// Map<String, Object> map = new HashMap<>();
		// map.put("case_id", params.get("case_id"));
		// map.put("sample_code", string);
		// // 判断样本编号是否存在
		// boolean res = RdsJudicialRegisterService.exsitSampleCode(map);
		// if (res) {
		// Map<String, Object> mapTemp = new HashMap<>();
		// mapTemp.put("sample_code", string);
		// boolean res1 = RdsJudicialRegisterService
		// .exsitSampleCode(mapTemp);
		// if (!res1) {
		// Map<String, Object> resultTemp = new HashMap<String, Object>();
		// resultTemp.put("result", false);
		// resultTemp.put("message", "样本编号已存在，请查看！");
		// return resultTemp;
		// }
		//
		// }
		// }

		params.put("case_in_per", case_in_per);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap = (Map<String, Object>) RdsJudicialRegisterService
				.updateCaseInfo(params, user);
		return resultMap;
	}

	/**
	 * 修改案例信息
	 */
	@RequestMapping("updateCaseVerifyState")
	@ResponseBody
	public boolean updateCaseVerifyState(@RequestBody Map<String, Object> params) {
		return RdsJudicialRegisterService.updateCaseVerifyState(params) > 0 ? true
				: false;
	}

	// /**
	// * 导出案例信息
	// */
	// @RequestMapping("exportCaseInfo")
	// public void exportCaseInfo(RdsJudicialParamsModel params,
	// HttpServletResponse response) {
	// RdsJudicialRegisterService.exportCaseInfo(params, response);
	// }
	//
	/**
	 * 导出样本信息
	 * 
	 * @param params
	 * @param response
	 */
	@RequestMapping("exportSampleInfo")
	public void exportSampleInfo(RdsJudicialParamsModel params,
			HttpServletResponse response, HttpSession session) {
		String sql_str = "";
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
			sql_str = user.getSql_str();
		}
		if (!"admin".equals(user.getUsercode())) {
			params.setSql_str(sql_str);
		}
		RdsJudicialRegisterService.exportSampleInfo(params, response);
	}

	/**
	 * 查重案例编号
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("exsitCaseCode")
	@ResponseBody
	public boolean exsitCaseCode(@RequestBody Map<String, Object> params) {
		return RdsJudicialRegisterService.exsitCaseCode(params);
	}

	/**
	 * 查重样本编号
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("exsitSampleCode")
	@ResponseBody
	public boolean exsitSampleCode(@RequestBody Map<String, Object> params) {
		return RdsJudicialRegisterService.exsitSampleCode(params);
	}

	/**
	 * 获取委托人
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getClient")
	@ResponseBody
	public Map<String, String> getClient(@RequestBody Map<String, Object> params) {
		return RdsJudicialRegisterService.getClient(params);
	}

	/**
	 * 验证身份证
	 */
	@RequestMapping("verifyId_Number")
	@ResponseBody
	public boolean verifyId_Number(@RequestBody Map<String, Object> params) {
		return RdsJudicialRegisterService.verifyId_Number(params);
	}

	@RequestMapping("returnCaseInfoState")
	@ResponseBody
	public boolean returnCaseInfoState(@RequestBody Map<String, Object> params) {
		return RdsJudicialRegisterService.returnCaseInfoState(params);
	}

	/**
	 * 查询案例快递信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("querySampleExpress")
	@ResponseBody
	public List<RdsJudicialSampleExpressModel> querySampleExpress(
			@RequestBody Map<String, Object> params) {
		return RdsJudicialRegisterService.querySampleExpress(params);
	}

	/**
	 * 查询案例快递信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("insertSampleExpress")
	@ResponseBody
	public Object insertSampleExpress(@RequestBody Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (RdsJudicialRegisterService.insertSampleExpress(params) > 0) {
			result.put("result", true);
			result.put("message", "插入成功！");
		} else {
			result.put("result", false);
			result.put("message", "插入失败！");
		}
		return result;
	}

	/**
	 * 查询案例快递信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("updateSampleExpress")
	@ResponseBody
	public Object updateSampleExpress(@RequestBody Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (RdsJudicialRegisterService.updateSampleExpress(params) > 0) {
			result.put("result", true);
			result.put("message", "更新成功！");
		} else {
			result.put("result", false);
			result.put("message", "更新失败！");
		}
		return result;
	}

	/**
	 * 查询案例快递信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("delSampleExpress")
	@ResponseBody
	public Object delSampleExpress(@RequestBody Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (RdsJudicialRegisterService.querySampleExpress(params).size() > 1) {
			if (RdsJudicialRegisterService.delSampleExpress(params) > 0) {
				result.put("result", true);
				result.put("message", "更新成功！");
			} else {
				result.put("result", false);
				result.put("message", "更新失败！");
			}
		} else {
			result.put("result", false);
			result.put("message", "一条记录你也忍心删除！不给！");
		}

		return result;
	}

	/**
	 * 获取案例的样本信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getCaseSubmit")
	@ResponseBody
	public Object getCaseSubmit(@RequestBody Map<String, Object> params) {
		String[] case_ids = params.get("case_id").toString().split(",");

		Map<String, Object> result = new HashMap<String, Object>();
		String message = "";
		for (String string : case_ids) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("case_id", string);
			if (RdsJudicialRegisterService.querySampleExpress(map).size() == 0) {
				message += "存在没有采样快递信息案例，";
				break;
			}
			if (rdsJudicialCaseAttachmentService.queryCount(map) == 0) {
				message += "存在没有上传附件案例，";
				break;
			}
		}

		if ("".equals(message)) {
			result.put("result", true);
			result.put("message", "");
		} else {

			result.put("result", false);
			result.put("message", message + "提交审核失败！请查看");
		}
		return result;
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

	/**
	 * 批量插入样本编码
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("insertSampleCode")
	@ResponseBody
	public Object insertSampleCode(@RequestBody Map<String, Object> params,
			HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
		}
		params.put("create_per", user.getUserid());
		int before = RdsJudicialRegisterService.queryMaxApplyBefore();
		if (RdsJudicialRegisterService.insertSampleCode(params) > 0) {
			int after = RdsJudicialRegisterService.queryMaxApplyAfter();
			result.put("result", true);
			result.put("message", "插入成功！样本条码区间为：" + before + "~" + after);
		} else {
			result.put("result", false);
			result.put("message", "插入失败！");
		}

		return result;
	}

	@RequestMapping("queryApplySampleCode")
	@ResponseBody
	public RdsJudicialResponse queryApplySampleCode(
			@RequestBody Map<String, Object> params, HttpSession session) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
		}
		if (!USER_PERMIT.contains(user.getUsercode())) {
			params.put("userid", user.getUserid());
		}
		return RdsJudicialRegisterService.queryApplySampleCode(params);
	}

	private static final String XML_PATH = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config.xml";

	@RequestMapping("setXMLnum")
	@ResponseBody
	public boolean setXMLnum(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		try {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			if ("admin".equals(user.getUsercode())
					|| user.getUsercode().startsWith("subo_chir")) {
				if (null != params.get("value")
						&& !"".equals(params.get("value"))) {
					XmlParseUtil.updateXmlValue(XML_PATH, params.get("key")
							.toString(), params.get("value").toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static final String XML_PATH_1 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config1.xml";

	@RequestMapping("setXMLnum1")
	@ResponseBody
	public boolean setXMLnum1(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		try {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			if ("admin".equals(user.getUsercode())
					|| user.getUsercode().startsWith("subo_chir")) {
				if (null != params.get("value")
						&& !"".equals(params.get("value"))) {
					XmlParseUtil.updateXmlValue(XML_PATH_1, params.get("key")
							.toString(), params.get("value").toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static final String XML_PATH_2 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config2.xml";

	@RequestMapping("setXMLnum2")
	@ResponseBody
	public boolean setXMLnum2(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		try {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			if ("admin".equals(user.getUsercode())
					|| user.getUsercode().startsWith("subo_chir")) {
				if (null != params.get("value")
						&& !"".equals(params.get("value"))) {
					XmlParseUtil.updateXmlValue(XML_PATH_2, params.get("key")
							.toString(), params.get("value").toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static final String XML_PATH_3 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config3.xml";

	@RequestMapping("setXMLnum3")
	@ResponseBody
	public boolean setXMLnum3(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		try {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			if ("admin".equals(user.getUsercode())
					|| user.getUsercode().startsWith("subo_chir")) {
				if (null != params.get("value")
						&& !"".equals(params.get("value"))) {
					XmlParseUtil.updateXmlValue(XML_PATH_3, params.get("key")
							.toString(), params.get("value").toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static final String XML_PATH_6 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config6.xml";

	@RequestMapping("setXMLnum6")
	@ResponseBody
	public Object setXMLnum6(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		try {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			if ("admin".equals(user.getUsercode())
					|| user.getUsercode().startsWith("subo_chir")) {
				if (null != params.get("value")
						&& !"".equals(params.get("value"))) {
					XmlParseUtil.updateXmlValue(XML_PATH_6, params.get("key")
							.toString(), params.get("value").toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static final String XML_PATH_7 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config7.xml";

	@RequestMapping("setXMLnum7")
	@ResponseBody
	public Object setXMLnum7(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		try {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			if ("admin".equals(user.getUsercode())
					|| user.getUsercode().startsWith("subo_chir")) {
				if (null != params.get("value")
						&& !"".equals(params.get("value"))) {
					XmlParseUtil.updateXmlValue(XML_PATH_7, params.get("key")
							.toString(), params.get("value").toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@RequestMapping("caseCodeRun")
	@ResponseBody
	public void caseCodeRun(HttpServletRequest request) {
		String case_code = request.getParameter("case_code");
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		Map<String, Object> variables = new HashMap<String, Object>();
		// variables.put("issamplepass", 1);
		variables.put("isyesresult", 1);
		variables.put("isback", -1);
		// variables.put("yes", 0);
		// variables.put("isreportpass", 1);
		rdsActivitiJudicialService.runByCaseCode(case_code, variables, user);
	}

	@RequestMapping("fillReport")
	@ResponseBody
	public Object fillReport(@RequestBody Map<String, Object> params,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
			params.put("id", UUIDUtil.getUUID());
			params.put("update_user", user.getUserid());
			boolean result = RdsJudicialRegisterService.addCaseFeeOther(params);
			map.put("result", result);
			if (result)
				map.put("message", "补报告成功，请前往打印处打印案例!");
			else
				map.put("message", "操作失败，请联系管理员!");
			return map;
		} else {
			map.put("result", false);
			map.put("message", "操作失败，请联系管理员!");
			return map;
		}
	}

	@RequestMapping("changeReport")
	@ResponseBody
	public Object changeReport(@RequestBody Map<String, Object> params,
			HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
			params.put("id", UUIDUtil.getUUID());
			params.put("update_user", user.getUserid());
			boolean result = RdsJudicialRegisterService.addCaseFeeOther(params);
			map.put("result", result);
			if (result)
				map.put("message", "改报告成功，请前往打印处修改打印案例!");
			else
				map.put("message", "操作失败，请联系管理员!");
			return map;
		} else {
			map.put("result", false);
			map.put("message", "操作失败，请联系管理员!");
			return map;
		}
	}

	@RequestMapping("getChangeCaseInfo")
	@ResponseBody
	public RdsJudicialResponse getChangeCaseInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		// String sql_str = "";
		RdsUpcUserModel user = new RdsUpcUserModel();
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session.getAttribute("user");
			// sql_str = user.getSql_str();
		}
		if (!USER_PERMIT.contains(user.getUsercode())) {
			if (null != user.getAreacode() && !"".equals(user.getAreacode())) {
				if (null != user.getParnter_name()
						&& !"".equals(user.getParnter_name())) {
					params.put("receiver_area_user", user.getReceiver_area());
					params.put("parnter_name", user.getParnter_name());
				} else
					params.put("receiver_area_user", user.getReceiver_area());
			} else {
				params.put("receiver_area_user", user.getReceiver_area());
				params.put("parnter_name", user.getParnter_name());
				params.put("userid", user.getUserid());
			}
		}
		return RdsJudicialRegisterService.getChangeCaseInfo(params);
	}

	@RequestMapping("pdfToImage")
	@ResponseBody
	public void pdfToImage(HttpServletRequest request) {
		File pdfFile = null;
		pdfFile = new File("D:\\2017001292.pdf");
		try {
			RdsFileUtil.PDFchangToImage(pdfFile, "N");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
