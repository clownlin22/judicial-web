package com.rds.alcohol.web;

import com.rds.alcohol.model.*;
import com.rds.alcohol.service.RdsAlcoholPrintService;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.judicial.model.RdsJudicialPrintModel;
import com.rds.upc.model.RdsUpcUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.*;

@RequestMapping("alcohol/print")
@Controller
public class RdsAlcoholPrintController {

	@Autowired
	private RdsAlcoholPrintService RdsAlcoholPrintService;

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "alcohol_path");

	private static final String TEMP_ZIP_PATH = PropertiesUtils.readValue(
			FILE_PATH, "temp_zip_path");
	/**
	 * 获取案例列表
	 * @param params
	 * @param session
	 * @return
	 */
	@RequestMapping("getPrintCaseInfo")
	@ResponseBody
	public RdsAlcoholResponse  getPrintCaseInfo(@RequestBody Map<String, Object> params,HttpSession session){
		String sql_str="";
		if(session.getAttribute("user")!=null){
			RdsUpcUserModel user=(RdsUpcUserModel)session.getAttribute("user");
			sql_str=user.getSql_str2();
		}
		params.put("sql_str", sql_str);
		return RdsAlcoholPrintService.getPrintCaseInfo(params);
	}

	/**
	 * 获取打印模板
	 *
	 * @param case_code
	 * @param type
	 * @param web
	 * @param request
	 * @return
	 */
	@RequestMapping("getPrintModel")
	public String getPrintModel(String case_code,String type,String web,HttpServletRequest request){
		ArrayList<RdsAlcoholIdentifyModel> list = new ArrayList<RdsAlcoholIdentifyModel>();
		List<RdsJudicialPrintModel> model=RdsAlcoholPrintService.getPrintModel(type);
		request.setAttribute("model1", model.get(0));
		request.setAttribute("model2", model.get(1));
		List<RdsAlcoholCaseInfoModel> caseInfoModels = new ArrayList<RdsAlcoholCaseInfoModel>();

		String[] case_codes = case_code.split(",");
		for (String code : case_codes) {
			RdsAlcoholCaseInfoModel caseInfoModel=RdsAlcoholPrintService.getCaseInfo(code);
			RdsAlcoholSampleInfoModel sampleInfoModel=RdsAlcoholPrintService.getSampleInfo(caseInfoModel.getSample_id());

			List<RdsAlcoholAttachmentModel> attachmentModel=RdsAlcoholPrintService.getAttachment(caseInfoModel.getCase_id());
			caseInfoModel.setSi(sampleInfoModel);
			caseInfoModel.setAm(attachmentModel);

			caseInfoModels.add(caseInfoModel);
		}
		request.setAttribute("caseInfos", caseInfoModels);
		return web;
	}

	@RequestMapping("printCase")
	@ResponseBody
	public boolean printCase(@RequestBody Map<String, Object> params){
		return RdsAlcoholPrintService.printCase(params);
	}

	@RequestMapping("getAlcoholWord")
	public void getAlcoholWord(HttpServletResponse response,
			@RequestParam String case_code, @RequestParam String report_model,
			@RequestParam String type, HttpServletRequest request)
					throws Exception {
		FileInputStream hFile = null;
		OutputStream toClient = null;
		File directory = new File(ATTACHMENTPATH);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		String file_name = ATTACHMENTPATH + File.separatorChar + case_code
				+ File.separatorChar + case_code + ".doc";
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("case_code", case_code);
			params.put("file_name", file_name);
			params.put("type", type);
			params.put("report_model", report_model);
			RdsAlcoholPrintService.createJudicialDocByCaseCode(params);
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
		} catch (Exception e) {
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
		RdsFileUtil.delFile(TEMP_ZIP_PATH + "alcohol.zip");
		File directory = new File(TEMP_ZIP_PATH);
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
			params.put("report_model", data[i].get("report_model"));
			params.put("file_name", file_name);
			params.put("type", data[i].get("type"));
			RdsAlcoholPrintService.createJudicialDocByCaseCode(params);
			list.add(new File(file_name));
		}
		RdsFileUtil.zipFiles(list, new File(TEMP_ZIP_PATH + "alcohol.zip"));
		return "success";
	}

	@RequestMapping("getZipAfter")
	public void getZipAfter(HttpServletResponse response) throws Exception {
		FileInputStream hFile = null;
		OutputStream toClient = null;
		try {
			File file = new File(TEMP_ZIP_PATH + "/alcohol.zip");
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

}
