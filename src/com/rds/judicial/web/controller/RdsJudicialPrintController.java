package com.rds.judicial.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.Setter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialIdentifyPer;
import com.rds.judicial.model.RdsJudicialPrintCaseModel;
import com.rds.judicial.model.RdsJudicialPrintTableModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialPrintService;
import com.rds.judicial.service.RdsJudicialReagentsService;
import com.rds.judicial.service.RdsJudicialRegisterService;
import com.rds.judicial.service.RdsJudicialSampleRelayService;
import com.rds.judicial.service.RdsJudicialSubCaseService;
import com.rds.upc.model.RdsUpcUserModel;

@RequestMapping("judicial/print")
@Controller
public class RdsJudicialPrintController {

	@Autowired
	private RdsJudicialPrintService rdsJudicialPrintService;
	@Setter
	@Autowired
	private RdsJudicialSubCaseService rdsJudicialSubCaseService;

	@Setter
	@Autowired
	private RdsJudicialReagentsService rdsJudicialReagentsService;

	@Setter
	@Autowired
	private RdsJudicialRegisterService rdsJudicialRegisterService;

	@Autowired
	private RdsJudicialSampleRelayService rdsJudicialSampleRelayService;

	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;

	@Autowired
	private RdsJudicialRegisterMapper rdsJudicialRegisterMapper;

	Logger logger = Logger.getLogger(RdsJudicialPrintController.class);

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "judicial_path");

	private static final String USER_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "user_permit");
	
	private static final String TEMP_ZIP_PATH = PropertiesUtils.readValue(
			FILE_PATH, "temp_zip_path");

	/**
	 * 根据条件获取案例的基本信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getPrintCaseInfo")
	@ResponseBody
	public RdsJudicialResponse getPrintCaseInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			if (!USER_PERMIT.contains(user.getUsercode())) {
				{
					params.put("parnter_name", user.getParnter_name());// 根据登录人得到的筛选条件
				}
			}
		}
		return rdsJudicialPrintService.getPrintCaseInfo(params);
	}

	@ResponseBody
	@RequestMapping("usePrintCount")
	public boolean usePrintCount(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		if (rdsJudicialPrintService.usePrintCount(params)) {
			if (Integer.parseInt(params.get("print_count").toString()) == 0) {
				String caseCode = params.get("case_code").toString();
				RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
						.getAttribute("user");
				rdsActivitiJudicialService.runByCaseCode(caseCode, "taskPrint",
						null, user);
				Map<String, Object> mapTemp = new HashMap<String, Object>();
				mapTemp.put("case_code", caseCode);
				mapTemp.put("verify_state", 7);
				rdsJudicialSampleRelayService.updateCaseVerifyBycode(mapTemp);
			}
			return true;
		} else {
			return false;
		}
	}

	@RequestMapping("getPrintModelZY")
	public String getPrintModelZY(String case_code, HttpServletRequest request,
			String web, String type, String case_id) {
		RdsJudicialPrintCaseModel caseModel = rdsJudicialPrintService
				.getPrintCaseZY(case_code, case_id, type);
		request.setAttribute("caseModel", caseModel);
		List<RdsJudicialIdentifyPer> pers = rdsJudicialPrintService
				.getIdentifyPer(case_id);
		request.setAttribute("pers", pers);
		Map<String, Object> reagent_name = rdsJudicialReagentsService
				.queryReagentByCaseCode(case_code);
		request.setAttribute("reagent_name", reagent_name.get("reagent_name"));
		request.setAttribute("reagent_name_ext",
				reagent_name.get("reagent_name_ext"));
		// 案例基本信息
		RdsJudicialCaseInfoModel rdsJudicialCaseInfoModel = rdsJudicialRegisterMapper
				.queryCaseInfoByCaseCode(case_code);
		int copies=rdsJudicialCaseInfoModel.getCopies();
		request.setAttribute("copies", copies);
		Map<String, Object> queryOrderParam = new HashMap<>();
		queryOrderParam.put("reagent_name", reagent_name.get("reagent_name"));
		queryOrderParam
				.put("model", rdsJudicialCaseInfoModel.getReport_model());
		// 根据配置表tb_dic_print_gene查询排好序的试剂点位
		String[] ateliers = rdsJudicialReagentsService
				.queryOrder(queryOrderParam);
		queryOrderParam.put("reagent_name",
				reagent_name.get("reagent_name_ext"));
		String[] ateliersExt = rdsJudicialReagentsService
				.queryOrder(queryOrderParam);
		int atelierCount = ateliers.length;
		int atelierExtCount = ateliersExt.length;
		request.setAttribute("lenthatelier", atelierCount + atelierExtCount);
		return web;
	}

	@RequestMapping("exsitPrintTable")
	@ResponseBody
	public boolean exsitPrintTable(@RequestBody Map<String, Object> params) {
		return rdsJudicialPrintService.exsitPrintTable(params);
	}

	@RequestMapping("getPrintTable")
	public String getPrintTable(String case_code, HttpServletRequest request,
			String web, String case_id, String type) {
		List<RdsJudicialPrintTableModel> printTableModels = rdsJudicialPrintService
				.getPrintTable(case_code, case_id, type);
		request.setAttribute("printtables", printTableModels);
		return web;
	}

	@RequestMapping("exsitSampleImg")
	@ResponseBody
	public boolean exsitSampleImg(@RequestBody Map<String, Object> params) {
		return rdsJudicialPrintService.exsitSampleImg(params);
	}

	@RequestMapping("getSampleImg")
	public String getSampleImg(String case_code, HttpServletRequest request) {
		List<String> imgs = rdsJudicialPrintService.getSampleImg(case_code);
		request.setAttribute("imgs", imgs);
		return "dna/sample_dna";
	}

	@RequestMapping("getWord")
	public void getWord(HttpServletResponse response,
			@RequestParam String case_code, @RequestParam String report_model,
			@RequestParam int verify_state,@RequestParam String print_count,@RequestParam (value="print",required=false)int print, HttpServletRequest request,HttpSession session)
			throws Exception {
		FileInputStream hFile = null;
		OutputStream toClient = null;
		File directory = new File(FILE_PATH);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		String file_name = "";
		if (report_model.equals("zyjdmodeltableregext")) {
			file_name = ATTACHMENTPATH + File.separatorChar + case_code
					+ File.separatorChar + case_code + "_ext" + ".doc";
			verify_state=7;
		} else if (report_model.equals("zyjdmodeltablereg")) {
			file_name = ATTACHMENTPATH + File.separatorChar + case_code
					+ File.separatorChar + case_code + "_reg" + ".doc";
			verify_state=7;
		} else if (report_model.equals("zyjdmodeltable")) {
			file_name = ATTACHMENTPATH + File.separatorChar + case_code
					+ File.separatorChar + case_code + "_tab" + ".doc";
			verify_state=7;
		} else {
			file_name = ATTACHMENTPATH + File.separatorChar + case_code
					+ File.separatorChar + case_code + ".doc";
		}

		try {
			String code = "";
			if (session.getAttribute("user") != null) {
				RdsUpcUserModel user = (RdsUpcUserModel) session
						.getAttribute("user");
				code = user.getUserid();
			}
			Map<String, Object> params = new HashMap<>();
			params.put("case_code", case_code);
			params.put("file_name", file_name);
			params.put("print_count", print_count);
			params.put("report_model", report_model);
			if(8>=verify_state){//案例邮寄后，将不再改动，直接从服务器上下载
			if( print !=0){
			params.put("print", print);}
			if (report_model.equals("sbyxmodel")
					|| report_model.equals("syjdmodel")
					|| report_model.equals("zzjdmodel")
					|| report_model.equals("sqjdmodel")
					|| report_model.equals("fyxhjdmodel")
					|| report_model.equals("temporary")
					|| report_model.equals("qsjdmodel")
					|| report_model.equals("jzjdmodel")
					|| report_model.equals("mzjdmodel")
					|| report_model.equals("zxjdmodel")
					|| report_model.equals("dcjdmodel")
					|| report_model.equals("zyjdmodel")
					|| report_model.equals("zyjdmodeltq")
					|| report_model.equals("xzjdmodel")
					|| report_model.equals("ahzzjdmodel")
					|| report_model.equals("cxjdmodel")
					|| report_model.equals("zyjdmodeltableregext")
					|| report_model.equals("zyjdmodeltablereg")
					|| report_model.equals("zyjdmodeltable")
					|| report_model.equals("zyjdmodelzk")
					|| report_model.equals("ydjdmodel")
					|| report_model.equals("jdjdmodel")
					|| report_model.equals("bjqjdmodel")
					|| report_model.equals("ynqsjdmodel")
					|| report_model.equals("ydjdmodelyx")
					|| report_model.equals("qdwfjdmodel")
					|| report_model.equals("zyjymodel")&&"fc210d8ea6cf4996a3e718418940ccbf".equals(code)
					|| report_model.equals("gxjdmodel")
					|| report_model.equals("xajdmodel")) {
				rdsJudicialPrintService.createJudicialDocBySubCaseCode(params);
			} else {
				rdsJudicialPrintService.createJudicialDoc(params);
			}}
			File file = new File(file_name);
			hFile = new FileInputStream(file);
			String fileName = file.getName();
			// 得到文件大小
			int i = hFile.available();
			byte data[] = new byte[i];
			// 读数据
			hFile.read(data);
			// 设置response的Header
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(
							fileName.replaceAll(" ", "").getBytes("utf-8"),
							"iso8859-1"));
			// 得到向客户端输出二进制数据的对象
			toClient = response.getOutputStream();
			// 输出数据
			toClient.write(data);
			toClient.flush();
			Map<String, Object> updateClose_time = new HashMap<>();
			updateClose_time.put("case_code", case_code);
			updateClose_time.put("print_count", print_count);
			usePrintCount(updateClose_time, request);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			toClient.close();
			hFile.close();
		}
	}

	@RequestMapping("getZip")
	@ResponseBody
	public String getZip(@RequestBody Map<String, String>[] data,
			HttpServletRequest request) throws Exception {
		RdsFileUtil.delFile(TEMP_ZIP_PATH + "/temp.zip");
		File directory = new File(FILE_PATH);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		List<File> list = new LinkedList<>();
		for (int i = 0; i < data.length; i++) {
			Map<String, Object> params = new HashMap<>();
			String case_code = data[i].get("case_code");
			params.put("case_code", case_code);
			String file_name = ATTACHMENTPATH + File.separatorChar + case_code
					+ File.separatorChar + case_code + ".doc";
			String report_model = data[i].get("report_model");
			params.put("report_model", data[i].get("report_model"));
			params.put("file_name", file_name);
			params.put("print_count", data[i].get("print_count"));
			int verify_state=Integer.parseInt(data[i].get("verify_state"));
			if(8>=verify_state){//案例邮寄后，将不再改动，直接从服务器上下载
			if (report_model.equals("sbyxmodel")
					|| report_model.equals("syjdmodel")
					|| report_model.equals("zzjdmodel")
					|| report_model.equals("sqjdmodel")
					|| report_model.equals("fyxhjdmodel")
					|| report_model.equals("temporary")
					|| report_model.equals("qsjdmodel")
					|| report_model.equals("jzjdmodel")
					|| report_model.equals("mzjdmodel")
					|| report_model.equals("zxjdmodel")
					|| report_model.equals("dcjdmodel")
					|| report_model.equals("zyjdmodel")
					|| report_model.equals("zyjdmodeltq")
					|| report_model.equals("xzjdmodel")
					|| report_model.equals("ahzzjdmodel")
					|| report_model.equals("cxjdmodel")
					|| report_model.equals("zyjdmodeltableregext")
					|| report_model.equals("zyjdmodeltablereg")
					|| report_model.equals("zyjdmodeltable")
					|| report_model.equals("zyjdmodelzk")
					|| report_model.equals("ydjdmodel")
					|| report_model.equals("jdjdmodel")
					|| report_model.equals("bjqjdmodel")
					|| report_model.equals("ynqsjdmodel")
					|| report_model.equals("ydjdmodelyx")
					|| report_model.equals("qdwfjdmodel")
					|| report_model.equals("gxjdmodel")
					|| report_model.equals("xajdmodel")) {
				rdsJudicialPrintService.createJudicialDocBySubCaseCode(params);
			} else {
				rdsJudicialPrintService.createJudicialDoc(params);
			}
			 }
			Map<String, Object> updateClose_time = new HashMap<>();
			updateClose_time.put("case_code", case_code);
			updateClose_time.put("print_count", data[i].get("print_count"));
			usePrintCount(updateClose_time, request);
			list.add(new File(file_name));
		}
		RdsFileUtil.zipFiles(list, new File(TEMP_ZIP_PATH + "/temp.zip"));
		return "success";
	}
	@RequestMapping("getZipTwo")
	@ResponseBody
	public String getZipTwo(@RequestBody Map<String, String>[] data,
			HttpServletRequest request) throws Exception {
		RdsFileUtil.delFile(TEMP_ZIP_PATH + "/temp.zip");
		File directory = new File(FILE_PATH);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		List<File> list = new LinkedList<>();
		for (int i = 0; i < data.length; i++) {
			Map<String, Object> params = new HashMap<>();
			String case_code = data[i].get("case_code");
			params.put("case_code", case_code);
			String file_name = ATTACHMENTPATH + File.separatorChar + case_code
					+ File.separatorChar + case_code + ".doc";
			String report_model = data[i].get("report_model");
			params.put("report_model", data[i].get("report_model"));
			params.put("file_name", file_name);
			params.put("print_count", data[i].get("print_count"));
			params.put("print", 2);
			int verify_state=Integer.parseInt(data[i].get("verify_state"));
			if(8>=verify_state){//案例邮寄后，将不再改动，直接从服务器上下载
			if (report_model.equals("sbyxmodel")
					|| report_model.equals("syjdmodel")
					|| report_model.equals("zzjdmodel")
					|| report_model.equals("sqjdmodel")
					|| report_model.equals("fyxhjdmodel")
					|| report_model.equals("temporary")
					|| report_model.equals("qsjdmodel")
					|| report_model.equals("jzjdmodel")
					|| report_model.equals("mzjdmodel")
					|| report_model.equals("zxjdmodel")
					|| report_model.equals("dcjdmodel")
					|| report_model.equals("zyjdmodel")
					|| report_model.equals("zyjdmodeltq")
					|| report_model.equals("xzjdmodel")
					|| report_model.equals("cxjdmodel")
					|| report_model.equals("zyjdmodeltableregext")
					|| report_model.equals("zyjdmodeltablereg")
					|| report_model.equals("zyjdmodeltable")
					|| report_model.equals("zyjdmodelzk")
					|| report_model.equals("ydjdmodel")
					|| report_model.equals("jdjdmodel")
					|| report_model.equals("bjqjdmodel")
					|| report_model.equals("ydjdmodelyx")
					|| report_model.equals("qdwfjdmodel")
					|| report_model.equals("xajdmodel")) {
				rdsJudicialPrintService.createJudicialDocBySubCaseCode(params);
			} else {
				rdsJudicialPrintService.createJudicialDoc(params);
			}}
			Map<String, Object> updateClose_time = new HashMap<>();
			updateClose_time.put("case_code", case_code);
			updateClose_time.put("print_count", data[i].get("print_count"));
			usePrintCount(updateClose_time, request);
			list.add(new File(file_name));
		}
		RdsFileUtil.zipFiles(list, new File(TEMP_ZIP_PATH + "/temp.zip"));
		return "success";
	}

	@RequestMapping("getZipAfter")
	public void getZip(HttpServletResponse response) throws Exception {
		FileInputStream hFile = null;
		OutputStream toClient = null;
		try {
			File file = new File(TEMP_ZIP_PATH + "/temp.zip");
			hFile = new FileInputStream(file);
			String fileName = file.getName();
			// 得到文件大小
			int i = hFile.available();
			byte data[] = new byte[i];
			// 读数据
			hFile.read(data);
			// 设置response的Header
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(
							fileName.replaceAll(" ", "").getBytes("utf-8"),
							"iso8859-1"));
			// 得到向客户端输出二进制数据的对象
			toClient = response.getOutputStream();
			// 输出数据
			toClient.write(data);
			toClient.flush();
		} finally {
			hFile.close();
			toClient.close();
		}
	}

	@RequestMapping("getPdf")
	public void getPdf(HttpServletResponse response, String case_code,
			String report_model) throws Exception {
		FileInputStream hFile = null;
		OutputStream toClient = null;
		try {
			String file_name = ATTACHMENTPATH + File.separatorChar + case_code
					+ File.separatorChar + case_code + ".doc";
			String pdf_file_name = ATTACHMENTPATH + File.separatorChar
					+ case_code + File.separatorChar + case_code + ".pdf";
			Map<String, Object> params = new HashMap<>();
			params.put("case_code", case_code);
			params.put("file_name", file_name);
			params.put("report_model", report_model);
			File directory = new File(ATTACHMENTPATH + File.separatorChar
					+ case_code);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			if (report_model.equals("sbyxmodel")
					|| report_model.equals("syjdmodel")) {
				rdsJudicialPrintService.createJudicialDocBySubCaseCode(params);
			} else {
				rdsJudicialPrintService.createJudicialDoc(params);
			}
			
			RdsFileUtil.wordToPDF(file_name, pdf_file_name);
			hFile = new FileInputStream(pdf_file_name);
			// 得到文件大小
			int i = hFile.available();
			byte data[] = new byte[i];
			// 读数据
			hFile.read(data);

			// 得到向客户端输出二进制数据的对象
			toClient = response.getOutputStream();
			// 输出数据
			toClient.write(data);

			toClient.flush();
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			toClient.close();
			hFile.close();
		}
	}

	/**
	 * 根据条件获取案例的基本信息
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping("getPrintCaseInfoForWord")
	@ResponseBody
	public RdsJudicialResponse getPrintCaseInfoForWord(
			@RequestBody Map<String, Object> params, HttpSession session) {
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			if (!USER_PERMIT.contains(user.getUsercode())) {
				{
					params.put("parnter_name", user.getParnter_name());// 根据登录人得到的筛选条件
				}
			}
		}
		return rdsJudicialPrintService.getPrintCaseInfoForWord(params);
	}

	@RequestMapping("getChangePrintCaseInfo")
	@ResponseBody
	public RdsJudicialResponse getChangePrintCaseInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			if (!USER_PERMIT.contains(user.getUsercode())) {
				{
					params.put("parnter_name", user.getParnter_name());// 根据登录人得到的筛选条件
				}
			}
		}
		return rdsJudicialPrintService.getChangePrintCaseInfo(params);
	}

	@RequestMapping("getChangePrintCaseInfoForWord")
	@ResponseBody
	public RdsJudicialResponse getChangePrintCaseInfoForWord(
			@RequestBody Map<String, Object> params, HttpSession session) {
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			if (!USER_PERMIT.contains(user.getUsercode())) {
				{
					params.put("parnter_name", user.getParnter_name());// 根据登录人得到的筛选条件
				}
			}
		}
		return rdsJudicialPrintService.getChangePrintCaseInfoForWord(params);
	}

//	@RequestMapping("getJudicialWord")
//	public Object getJudicialWord(HttpServletRequest request, String file_name)
//			throws Exception {
//		request.setAttribute("file_name", file_name);
//		return "pageoffice/print_word";
//	}
	

	@RequestMapping("getJudicialWord")
	public Object getJudicialWord(HttpServletResponse response,
			@RequestParam String case_code, @RequestParam String report_model,
			@RequestParam String print_count, HttpServletRequest request)
			throws Exception {
//		FileInputStream hFile = null;
//		OutputStream toClient = null;
		File directory = new File(FILE_PATH);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		String file_name = "";
		if (report_model.equals("zyjdmodeltableregext")) {
			file_name = ATTACHMENTPATH + File.separatorChar + case_code
					+ File.separatorChar + case_code + "_ext" + ".doc";
		} else if (report_model.equals("zyjdmodeltablereg")) {
			file_name = ATTACHMENTPATH + File.separatorChar + case_code
					+ File.separatorChar + case_code + "_reg" + ".doc";
		} else if (report_model.equals("zyjdmodeltable")) {
			file_name = ATTACHMENTPATH + File.separatorChar + case_code
					+ File.separatorChar + case_code + "_tab" + ".doc";
		} else {
			file_name = ATTACHMENTPATH + File.separatorChar + case_code
					+ File.separatorChar + case_code + ".doc";
		}
		try {
           
			Map<String, Object> params = new HashMap<>();
			params.put("case_code", case_code);
			params.put("file_name", file_name);
			params.put("print_count", print_count);
			params.put("report_model", report_model);
			if (report_model.equals("sbyxmodel")
					|| report_model.equals("syjdmodel")
					|| report_model.equals("zzjdmodel")
					|| report_model.equals("sqjdmodel")
					|| report_model.equals("fyxhjdmodel")
					|| report_model.equals("temporary")
					|| report_model.equals("qsjdmodel")
					|| report_model.equals("jzjdmodel")
					|| report_model.equals("mzjdmodel")
					|| report_model.equals("zxjdmodel")
					|| report_model.equals("dcjdmodel")
					|| report_model.equals("zyjdmodel")
					|| report_model.equals("zyjdmodeltq")
					|| report_model.equals("xzjdmodel")
					|| report_model.equals("cxjdmodel")
					|| report_model.equals("zyjdmodeltableregext")
					|| report_model.equals("zyjdmodeltablereg")
					|| report_model.equals("zyjdmodeltable")
					|| report_model.equals("zyjdmodelzk")
					|| report_model.equals("ydjdmodel")
					|| report_model.equals("jdjdmodel")
					|| report_model.equals("bjqjdmodel")
					|| report_model.equals("ydjdmodelyx")
					|| report_model.equals("qdwfjdmodel")
					|| report_model.equals("xajdmodel")) {
				rdsJudicialPrintService.createJudicialDocBySubCaseCode(params);
			} else {
				rdsJudicialPrintService.createJudicialDoc(params);
			}
//			File file = new File(file_name);
//			hFile = new FileInputStream(file);
//			String fileName = file.getName();
//			// 得到文件大小
//			int i = hFile.available();
//			byte data[] = new byte[i];
//			// 读数据
//			hFile.read(data);
//			// 设置response的Header
//			response.reset();
//			response.setContentType("application/octet-stream; charset=utf-8");
//			response.addHeader("Content-Disposition", "attachment;filename="
//					+ new String(
//							fileName.replaceAll(" ", "").getBytes("utf-8"),
//							"iso8859-1"));
//			// 得到向客户端输出二进制数据的对象
//			toClient = response.getOutputStream();
//			// 输出数据
//			toClient.write(data);
//			toClient.flush();
			Map<String, Object> updateClose_time = new HashMap<>();
			updateClose_time.put("case_code", case_code);
			updateClose_time.put("print_count", print_count);
			usePrintCount(updateClose_time, request);
			request.setAttribute("file_name","file://"+file_name);
	        return "pageoffice/simple_word";
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
//			toClient.close();
//			hFile.close();
		}
	}

}
