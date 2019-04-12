package com.rds.judicial.web.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.model.MsgModelUtil;
import com.rds.judicial.model.RdsJudicialSampleResultModel;
import com.rds.judicial.service.RdsJudicialExperimentService;
import com.rds.judicial.service.RdsJudicialSampleCompareService;
import com.rds.judicial.service.RdsJudicialSampleService;
import com.rds.upc.model.RdsUpcMessageModel;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;

/**
 * @author lys1
 * @className RdsJudicialSampleCompareController
 * @description 样本管理controller
 * @date 2015/4/9
 */
@Controller
@RequestMapping("judicial/sample")
public class RdsJudicialSampleCompareController {

	@Autowired
	@Qualifier("RdsJudicialSampleCompareServiceImpl")
	private RdsJudicialSampleCompareService rdsJudicialSampleCompareService;

	@Autowired
	@Qualifier("RdsJudicialTongBaoCompareServiceImpl")
	private RdsJudicialSampleCompareService rdsJudicialTongBaoCompareServiceImpl;

	@Autowired
	private RdsJudicialSampleService rdsJudicialSampleService;

	@Autowired
	private RdsJudicialExperimentService rdsJudicialExperimentService;

	// 上传路径
	String dataPath;
	// 上传路径+文件名称
	String filePath;

	private static Logger logger = Logger
			.getLogger(RdsJudicialSampleCompareController.class);

	@RequestMapping("upload")
	@ResponseBody
	public RdsUpcMessageModel upload(@RequestParam String experiment_no,
			@RequestParam MultipartFile[] mfiles,
			@RequestParam String reagent_name,
			@RequestParam String identify_id, HttpSession session)
			throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		String laboratory_no = ((RdsUpcUserModel) session.getAttribute("user"))
				.getLaboratory_no();
		System.out.println("laboratory_no============================"
				+ laboratory_no);
		for (MultipartFile mFile : mfiles) {
			try {
				if (mFile.isEmpty()) {
					return MsgModelUtil.getModel(false,
							RdsUpcConstantUtil.UPDATE_FAILED);
				} else {
					// 获取系统config配置datapath存放路径
					dataPath = rdsJudicialSampleCompareService.getDataPath();
					System.out.println("dataPath============================"
							+ dataPath);
					// 上传实验数据存放路径文件
					filePath = dataPath + laboratory_no + File.separatorChar
							+ experiment_no + File.separatorChar
							+ mFile.getOriginalFilename();
					System.out.println("filePath============================"
							+ filePath);
					// 上传实验数据压缩包
					rdsJudicialSampleCompareService.dataFileUpload(filePath,
							mFile.getInputStream());
				}
				// 解压rar文件到指定文件目录
				RdsFileUtil.unrar(filePath, dataPath + laboratory_no
						+ File.separatorChar + experiment_no
						+ File.separatorChar);

				// 数据入库前验证+入库；校验实验数据有效性和
				if (!rdsJudicialSampleCompareService.preValidate(dataPath
						+ laboratory_no + File.separatorChar + experiment_no
						+ File.separatorChar, experiment_no, laboratory_no,
						reagent_name)) {
//					RdsFileUtil.delFolder(dataPath + experiment_no
//							+ File.separatorChar);
					return MsgModelUtil.getModel(false, "文件中数据有错误，请检查，"
							+ "没有数据导入");
				}
				// 比对
				String exception_per = "";
				if (user != null) {
					exception_per = user.getUserid();
				}
				rdsJudicialSampleCompareService.beginCompare(experiment_no,
						laboratory_no, reagent_name, identify_id,
						exception_per, user);
			} catch (Exception e) {
				Logger.getLogger(RdsJudicialSampleCompareController.class)
						.error(e.getStackTrace());
				Map<String, Object> deleteParams = new HashMap<>();
				deleteParams.put("experiment_no", experiment_no);
				deleteParams.put("laboratory_no", laboratory_no);
				rdsJudicialExperimentService.deleteSample(deleteParams);
				// rdsJudicialExperimentService.deleteSampleData(deleteParams);
				RdsUpcMessageModel rdsUpcMessageModel = null;
				try {
					rdsUpcMessageModel = MsgModelUtil.getModel(false, e
							.toString().split(":")[1]);
				} catch (Exception e1) {
					throw e;
				}
				return rdsUpcMessageModel;
			}
		}
		return MsgModelUtil.getModel(true, "导入数据成功");
	}

	@RequestMapping("tongbaoUpload")
	@ResponseBody
	public RdsUpcMessageModel tongbaoUpload(@RequestParam String experiment_no,
			@RequestParam MultipartFile[] mfiles,
			@RequestParam String reagent_name,
			@RequestParam String identify_id, HttpSession session)
			throws Exception {

		RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
		String laboratory_no = user.getLaboratory_no();
		for (MultipartFile mFile : mfiles) {
			try {
				if (mFile.isEmpty()) {
					return MsgModelUtil.getModel(false,
							RdsUpcConstantUtil.UPDATE_FAILED);
				} else {
					dataPath = rdsJudicialSampleCompareService.getDataPath();
					filePath = dataPath + laboratory_no + File.separatorChar
							+ experiment_no + File.separatorChar
							+ mFile.getOriginalFilename();
					rdsJudicialSampleCompareService.dataFileUpload(filePath,
							mFile.getInputStream());
				}
				// 解压rar文件
				RdsFileUtil.unrar(filePath, dataPath + laboratory_no
						+ File.separatorChar + experiment_no
						+ File.separatorChar);

				// 数据入库前验证+入库
				if (!rdsJudicialSampleCompareService.preValidate(dataPath
						+ laboratory_no + File.separatorChar + experiment_no
						+ File.separatorChar, experiment_no, laboratory_no,
						reagent_name)) {
//					RdsFileUtil.delFolder(dataPath + experiment_no
//							+ File.separatorChar);
					return MsgModelUtil.getModel(false, "文件中数据有错误，请检查，"
							+ "没有数据导入");
				}
				// 比对
				String exception_per = "";
				if (session.getAttribute("user") != null) {
					exception_per = user.getUserid();
				}
				rdsJudicialTongBaoCompareServiceImpl.beginCompare(
						experiment_no, laboratory_no, reagent_name,
						identify_id, exception_per, user);
			} catch (Exception e) {
				Logger.getLogger(RdsJudicialSampleCompareController.class)
						.error(e.getStackTrace());
				Map<String, Object> deleteParams = new HashMap<>();
				deleteParams.put("experiment_no", experiment_no);
				deleteParams.put("laboratory_no", laboratory_no);
				rdsJudicialExperimentService.deleteSample(deleteParams);
				// rdsJudicialExperimentService.deleteSampleData(deleteParams);
				RdsUpcMessageModel rdsUpcMessageModel = null;
				try {
					rdsUpcMessageModel = MsgModelUtil.getModel(false, e
							.toString().split(":")[1]);
				} catch (Exception e1) {
					throw e;
				}
				return rdsUpcMessageModel;
			}
		}
		return MsgModelUtil.getModel(true, "导入数据成功");
	}

	@RequestMapping("compare")
	@ResponseBody
	public Object compare(@RequestBody Map<String, Object> params,
			HttpSession session) throws Exception {
		try {

			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			String laboratory_no = user.getLaboratory_no();
			String exception_per = "";
			if (session.getAttribute("user") != null) {
				exception_per = user.getUserid();
			}
			String sub_case_code = (String) params.get("sub_case_code");
			String ext_flag = (String) params.get("ext_flag");
			Map<Boolean, String> rst = rdsJudicialSampleCompareService
					.validateSample(sub_case_code, ext_flag, laboratory_no,
							exception_per, user);
			Set<Boolean> resultSet = rst.keySet();
			for (Boolean flag : resultSet) {
				return MsgModelUtil.getModel(flag, rst.get(flag));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return MsgModelUtil.getModel(false, e.toString().split(":")[1]);
		}
		return MsgModelUtil.getModel(false, "发生未知错误");
	}

	@RequestMapping("queryAllRecord")
	@ResponseBody
	public Map<String, Object> queryAllRecord(
			@RequestBody Map<String, Object> params, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		params.put("grid", "Y");
		params.put("laboratory_no", ((RdsUpcUserModel) session
				.getAttribute("user")).getLaboratory_no());
		map.put("total", rdsJudicialSampleService.queryAllCount(params));
		map.put("data", rdsJudicialSampleService.queryAll(params));
		return map;
	}

	@RequestMapping("queryOneRecord")
	@ResponseBody
	public RdsJudicialSampleResultModel queryOneRecord(
			@RequestBody Map<String, Object> params, HttpSession session) {
		params.put("laboratory_no", ((RdsUpcUserModel) session
				.getAttribute("user")).getLaboratory_no());
		RdsJudicialSampleResultModel rdsJudicialSampleResultModel = rdsJudicialSampleService
				.queryOneRecord(params);
		return rdsJudicialSampleResultModel;
	}

	@RequestMapping("updateRecord")
	@ResponseBody
	public Object updateRecord(@RequestBody Map<String, Object> params) {
		int count = rdsJudicialSampleCompareService.updateRecord(params);
		if (count > 0)
			return MsgModelUtil.getModel(true,
					RdsUpcConstantUtil.UPDATE_SUCCESS);
		else
			return MsgModelUtil.getModel(false,
					RdsUpcConstantUtil.UPDATE_FAILED);
	}

	@RequestMapping("queryMissingData")
	@ResponseBody
	public Map<String, Object> queryMissingData(
			@RequestBody Map<String, Object> params, HttpSession session)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		params.put("laboratory_no", ((RdsUpcUserModel) session
				.getAttribute("user")).getLaboratory_no());
		map.put("data",
				rdsJudicialSampleCompareService.queryMissingData(params));
		map.put("total",
				rdsJudicialSampleCompareService.queryCountMissingData(params));
		return map;
	}

	@RequestMapping("querySampleHistory")
	@ResponseBody
	public Map<String, Object> querySampleHistory(
			@RequestBody Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", rdsJudicialSampleService.queryHistory(params));
		map.put("total", rdsJudicialSampleService.queryHistoryCount(params));
		return map;
	}

	@RequestMapping("uploadCheck")
	@ResponseBody
	public RdsUpcMessageModel uploadCheck(@RequestParam MultipartFile[] mfiles,
			HttpSession session) throws Exception {
		Map<String, Object> result = new HashMap<>();
		String laboratory_no = ((RdsUpcUserModel) session.getAttribute("user"))
				.getLaboratory_no();
		System.out.println("laboratory_no============================"
				+ laboratory_no);
		for (MultipartFile mFile : mfiles) {
			try {
				if (mFile.isEmpty()) {
					return MsgModelUtil.getModel(false,
							RdsUpcConstantUtil.UPDATE_FAILED);
				} else {
					// 获取系统config配置datapath存放路径
					dataPath = rdsJudicialSampleCompareService.getDataPath();
					System.out.println("dataPath============================"
							+ dataPath);
					// 上传实验数据存放路径文件
					filePath = dataPath + laboratory_no + File.separatorChar
							+ "temp" + File.separatorChar
							+ mFile.getOriginalFilename();
					System.out.println("filePath============================"
							+ filePath);
					// 上传实验数据压缩包
					rdsJudicialSampleCompareService.dataFileUpload(filePath,
							mFile.getInputStream());
				}
				// 解压rar文件到指定文件目录
				RdsFileUtil.unrar(filePath, dataPath + laboratory_no
						+ File.separatorChar + "temp" + File.separatorChar);
				// 删除压缩包
				RdsFileUtil.delFolder(filePath);
				result = rdsJudicialSampleCompareService.preValidateCheck(
						dataPath + laboratory_no + File.separatorChar + "temp"
								+ File.separatorChar, laboratory_no);
			} catch (Exception e) {
				Logger.getLogger(RdsJudicialSampleCompareController.class)
						.error(e.getStackTrace());
				RdsUpcMessageModel rdsUpcMessageModel = null;
				try {
					rdsUpcMessageModel = MsgModelUtil.getModel(false, e
							.toString().split(":")[1]);
				} catch (Exception e1) {
					throw e;
				}
				return rdsUpcMessageModel;
			}
		}
		if ((boolean) result.get("success")) {
			return MsgModelUtil.getModel(true, "该压缩包数据校验通过！");
		} else {
			return MsgModelUtil.getModel(false, result.get("returnStr")
					.toString());
		}
	}

}
