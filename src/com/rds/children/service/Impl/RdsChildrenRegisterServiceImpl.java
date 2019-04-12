package com.rds.children.service.Impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.icu.text.SimpleDateFormat;
import com.rds.children.mapper.RdsChildrenPrintMapper;
import com.rds.children.mapper.RdsChildrenRegisterMapper;
import com.rds.children.model.RdsChildrenCaseInfoModel;
import com.rds.children.model.RdsChildrenCasePhotoModel;
import com.rds.children.model.RdsChildrenCustodyInfoModel;
import com.rds.children.model.RdsChildrenGatherInfoModel;
import com.rds.children.model.RdsChildrenResponse;
import com.rds.children.model.RdsChildrenTariffModel;
import com.rds.children.service.RdsChildrenRegisterService;
import com.rds.code.date.DateUtils;
import com.rds.code.utils.DownLoadUtils;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.FileUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.file.XmlParseUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.finance.service.RdsCaseFinanceService;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
@Transactional
public class RdsChildrenRegisterServiceImpl implements
		RdsChildrenRegisterService {

	@Autowired
	private RdsChildrenRegisterMapper rdsChildrenRegisterMapper;

	@Setter
	@Autowired
	private RdsCaseFinanceService rdsCaseFinanceService;

	@Autowired
	private RdsChildrenPrintMapper RdsChildrenPrintMapper;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private RuntimeService runtimeService;

	// 配置文件地址
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	// 附件存放地址
	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "children_head_photo") + File.separatorChar;

	private static final String XML_PATH_6 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config6.xml";

	@Override
	public RdsChildrenResponse getCaseInfo(Map<String, Object> params) {
		RdsChildrenResponse response = new RdsChildrenResponse();
		List<RdsChildrenCaseInfoModel> caseInfoModels = rdsChildrenRegisterMapper
				.getCaseInfo(params);
		int count = rdsChildrenRegisterMapper.countCaseInfo(params);
		response.setItems(caseInfoModels);
		response.setCount(count);
		return response;
	}

	@Override
	public List<RdsChildrenCustodyInfoModel> getCustodyInfo(
			Map<String, Object> params) {
		return rdsChildrenRegisterMapper.getCustodyInfo(params);
	}

	@Override
	public RdsChildrenGatherInfoModel getGatherInfo(Map<String, Object> params) {
		return rdsChildrenRegisterMapper.getGatherInfo(params);
	}

	@Override
	public boolean deleteCaseInfo(Map<String, Object> params) {
		return rdsChildrenRegisterMapper.deleteCaseInfo(params);
	}

	@Override
	public Map<String, Object> saveCaseInfo(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<>();
		String case_id = params.get("case_id").toString();
		/** 案例编号生成 start **/
		String case_code = "";
		String xml_time = XmlParseUtil.getXmlValue(XML_PATH_6,
				"case_code_child_date");
		String now_time = com.rds.code.utils.StringUtils.dateToSix(new Date());
		DecimalFormat df = new DecimalFormat("000");
		if (xml_time.equals(now_time)) {
			case_code = "J"
					+ now_time
					+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
							XML_PATH_6, "case_code_child")) + 1);
			XmlParseUtil.updateXmlValue(XML_PATH_6, "case_code_child", String
					.valueOf(Integer.parseInt(XmlParseUtil.getXmlValue(
							XML_PATH_6, "case_code_child")) + 1));
		} else {
			XmlParseUtil.updateXmlValue(XML_PATH_6, "case_code_child_date",
					now_time);
			XmlParseUtil.updateXmlValue(XML_PATH_6, "case_code_child", "1");
			case_code = "J"
					+ now_time
					+ df.format(Integer.parseInt(XmlParseUtil.getXmlValue(
							XML_PATH_6, "case_code_child")));
		}
		/** 案例编号生成 end **/
		params.put("case_code", case_code);
		if ("".equals(params.get("sample_code")))
			params.put("sample_code", case_code);
		// 添加财务信息
		if ((boolean) rdsCaseFinanceService.addCaseFeeChildren(params).get(
				"result")) {
			// 添加采集人信息
			if (StringUtils.isNotEmpty(params.get("gather_name").toString()
					.trim())) {
				RdsChildrenGatherInfoModel gatherInfoModel = new RdsChildrenGatherInfoModel(
						UUIDUtil.getUUID(), params.get("gather_name")
								.toString().trim(), params
								.get("gather_id_number").toString().trim(),
						params.get("gather_phone").toString().trim(), params
								.get("gather_company_name").toString().trim());
				params.put("gather_id", gatherInfoModel.getId());
				rdsChildrenRegisterMapper.addGatherInfo(gatherInfoModel);
			}
			// 添加监护人信息
			List<String> custody_names = getValues(params.get("custody_name"));
			List<String> custody_id_numbers = getValues(params
					.get("custody_id_number"));
			List<String> custody_calls = getValues(params.get("custody_call"));
			List<String> custody_phones = getValues(params.get("custody_phone"));
			for (int i = 0; i < custody_names.size(); i++) {
				RdsChildrenCustodyInfoModel custodyInfoModel = new RdsChildrenCustodyInfoModel(
						UUIDUtil.getUUID(), custody_names.get(i),
						custody_calls.get(i),
						custody_id_numbers.size() > 0 ? custody_id_numbers
								.get(i) : "",
						custody_phones.size() > 0 ? custody_phones.get(i) : "",
						case_id);
				rdsChildrenRegisterMapper.addCustodyInfo(custodyInfoModel);
			}
			/* 案例流程增加 start */
			try {
				identityService.setAuthenticatedUserId(params
						.get("case_in_per").toString());
				ProcessInstance processInstance = runtimeService
						.startProcessInstanceByKey("processChildren", case_id);
				params.put("process_instance_id", processInstance.getId());
			} finally {
				identityService.setAuthenticatedUserId(null);
			}
			/* 案例流程增加 end */
			if (rdsChildrenRegisterMapper.addCaseInfo(params)) {
				if (null != params.get("files")) {
					RdsChildrenCasePhotoModel pmodel = new RdsChildrenCasePhotoModel();
					pmodel.setUpload_user(params.get("case_in_per").toString());
					pmodel.setCase_id(case_id);
					try {
						this.uploadChildrenPhoto(pmodel,
								(MultipartFile[]) params.get("files"),
								(String[]) params.get("filetype"));
					} catch (IOException e) {
						result.put("success", true);
						result.put("result", false);
						result.put("msg", "案例新增异常，请联系管理员!");
					}
				}
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("msg", "案例新增异常，请联系管理员!");
			}

			/* 案例流程增加 end */
			result.put("success", true);
			result.put("result", true);
			result.put("msg", "案例新增成功！");
		} else {
			result.put("success", true);
			result.put("result", false);
			result.put("msg", "财务新增异常，请联系管理员!");
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

	@Override
	public boolean exsitCaseCode(Map<String, Object> params) {
		int result = rdsChildrenRegisterMapper.exsitCaseCode(params);
		if (result > 0) {
			return false;
		}
		return true;
	}

	@Override
	public Map<String, Object> updateCaseInfo(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<>();
		// 更新财务信息
		if ((boolean) rdsCaseFinanceService.updateCaseFeeChildren(params).get(
				"result")) {
			// 更新采集人信息
			if (StringUtils.isNotEmpty(params.get("gather_name").toString()
					.trim())) {
				RdsChildrenGatherInfoModel gatherInfoModel = new RdsChildrenGatherInfoModel(
						UUIDUtil.getUUID(), params.get("gather_name")
								.toString().trim(), params
								.get("gather_id_number").toString().trim(),
						params.get("gather_phone").toString().trim(), params
								.get("gather_company_name").toString().trim());
				params.put("gather_id", gatherInfoModel.getId());
				rdsChildrenRegisterMapper.deleteGatherInfo(params);
				rdsChildrenRegisterMapper.addGatherInfo(gatherInfoModel);
			}
			// 更新监护人信息
			List<String> custody_names = getValues(params.get("custody_name"));
			List<String> custody_id_numbers = getValues(params
					.get("custody_id_number"));
			List<String> custody_calls = getValues(params.get("custody_call"));
			List<String> custody_phones = getValues(params.get("custody_phone"));
			rdsChildrenRegisterMapper.deleteCustodyInfo(params);
			for (int i = 0; i < custody_names.size(); i++) {
				RdsChildrenCustodyInfoModel custodyInfoModel = new RdsChildrenCustodyInfoModel(
						UUIDUtil.getUUID(), custody_names.get(i),
						custody_calls.get(i), custody_id_numbers.get(i),
						custody_phones.get(i), params.get("case_id").toString());
				rdsChildrenRegisterMapper.addCustodyInfo(custodyInfoModel);
			}
			if (rdsChildrenRegisterMapper.updateCaseInfo(params)) {
				result.put("success", true);
				result.put("result", true);
				result.put("msg", "案例更新成功！");
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("msg", "案例更新异常，请联系管理员!");
			}
		} else {
			result.put("success", true);
			result.put("result", false);
			result.put("msg", "财务更新异常，请联系管理员!");
		}
		return result;
	}

	@Override
	public Map<String, Object> getTariff() {
		Map<String, Object> map = new HashMap<>();
		List<RdsChildrenTariffModel> tlist = rdsChildrenRegisterMapper
				.getTariff();
		map.put("data", tlist);
		return map;
	}

	@Override
	public Map<String, Object> photoUpload(RdsChildrenCasePhotoModel pmodel,
			MultipartFile[] photo) throws IOException {
		String msg = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isBlank(pmodel.getPhoto_id())) {
			if (!(rdsChildrenRegisterMapper.deleteCasePhoto(pmodel) > 0)) {
				msg = msg + "文件：" + photo[0].getOriginalFilename()
						+ "上传失败,请联系管理员！";
				map.put("success", false);
				map.put("message", msg);
				return map;
			}
			// 删除原来照片
			if (!RdsFileUtil.delFile(ATTACHMENTPATH + pmodel.getPhoto_path())) {
				msg = msg + "文件：" + photo[0].getOriginalFilename()
						+ "上传失败,请联系管理员！";
				map.put("success", false);
				map.put("message", msg);
				return map;
			}
		}
		// 文件路径
		String attachmentPath = pmodel.getCase_id()
				+ File.separatorChar
				+ new Date().getTime()
				+ photo[0].getOriginalFilename().substring(
						photo[0].getOriginalFilename().lastIndexOf("."),
						photo[0].getOriginalFilename().length());

		String photo_id = UUIDUtil.getUUID();
		if (StringUtils.isNotEmpty(pmodel.getPhoto_type_old()))
			pmodel.setPhoto_type(pmodel.getPhoto_type_old());
		pmodel.setPhoto_id(photo_id);
		pmodel.setPhoto_path(attachmentPath);
		pmodel.setUpload_time(new SimpleDateFormat("yyyy-MM-dd HH:MM:SS")
				.format(new Date()));
		if (!RdsFileUtil.getState(attachmentPath
				+ photo[0].getOriginalFilename())) {
			// 上传
			RdsFileUtil.fileUpload(ATTACHMENTPATH + attachmentPath,
					photo[0].getInputStream());
			// 插入数据库
			if (rdsChildrenRegisterMapper.insertHeadPhoto(pmodel) > 0)
				msg = msg + "文件：" + photo[0].getOriginalFilename() + "上传成功";
			else
				msg = msg + "文件：" + photo[0].getOriginalFilename() + "上传失败";
		} else {
			msg = msg + "文件：" + photo[0].getOriginalFilename() + "已存在,上传失败";
		}
		map.put("success", true);
		map.put("message", msg);
		return map;
	}

	private boolean uploadChildrenPhoto(RdsChildrenCasePhotoModel pmodel,
			MultipartFile[] photo, String[] filetype) throws IOException {
		for (int i = 0; i < filetype.length; i++) {
			// 文件路径
			String attachmentPath = pmodel.getCase_id()
					+ File.separatorChar
					+ new Date().getTime()
					+ photo[i].getOriginalFilename().substring(
							photo[i].getOriginalFilename().lastIndexOf("."),
							photo[i].getOriginalFilename().length());

			String photo_id = UUIDUtil.getUUID();
			pmodel.setPhoto_id(photo_id);
			pmodel.setPhoto_path(attachmentPath);
			pmodel.setPhoto_type(filetype[i]);
			pmodel.setUpload_time(new SimpleDateFormat("yyyy-MM-dd HH:MM:SS")
					.format(new Date()));
			if (!RdsFileUtil.getState(attachmentPath
					+ photo[i].getOriginalFilename())) {
				// 上传
				RdsFileUtil.fileUpload(ATTACHMENTPATH + attachmentPath,
						photo[i].getInputStream());
				// 插入数据库
				if (rdsChildrenRegisterMapper.insertHeadPhoto(pmodel) <= 0)
					return false;
			} else {
				return false;
			}
		}

		return true;

	}

	@Override
	public void exportInfo(String case_code, String starttime, String endtime,
			String child_name, Integer is_delete, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("case_code", case_code);
		params.put("starttime", starttime);
		params.put("endtime", endtime);
		params.put("child_name", child_name);
		params.put("is_delete", is_delete);
		String filename = "样本列表";
		// 根据查询条件查询案例信息
		List<RdsChildrenCaseInfoModel> caseInfoModels = rdsChildrenRegisterMapper
				.exportInfo(params);
		// excel表格列头
		Object[] titles = { "编号", "样本编号", "日期", "儿童姓名", "出生日期", "儿童身份证",
				"户籍所在地", "反馈寄送地", "父亲姓名", "父亲身份证", "父亲手机号码", "母亲姓名", "母亲身份证",
				"母亲手机号码", "套餐类型", "应收款项", "所付款项", "到款日期","确认确认状态","确认日期", "财务备注", "案例所属人",
				"归属地", "案例备注" };
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		// 循环案例列表拼装表格一行
		for (int i = 0; i < caseInfoModels.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			objects.add(caseInfoModels.get(i).getCase_code());
			objects.add(caseInfoModels.get(i).getSample_code());
			objects.add(DateUtils.DateString2DateString(caseInfoModels.get(i)
					.getCase_in_time()));
			objects.add(caseInfoModels.get(i).getChild_name());
			objects.add(DateUtils.DateString2DateString(caseInfoModels.get(i)
					.getBirth_date()));
			objects.add(caseInfoModels.get(i).getId_number());
			objects.add(caseInfoModels.get(i).getHouse_area());
			objects.add(caseInfoModels.get(i).getMail_area());
			objects.add(caseInfoModels.get(i).getFather_name());
			objects.add(caseInfoModels.get(i).getFather_id_number());
			objects.add(caseInfoModels.get(i).getFather_phone());
			objects.add(caseInfoModels.get(i).getMother_name());
			objects.add(caseInfoModels.get(i).getMother_id_number());
			objects.add(caseInfoModels.get(i).getMother_phone());
			objects.add(caseInfoModels.get(i).getTariff_name());
			objects.add(caseInfoModels.get(i).getReal_sum());
			objects.add(caseInfoModels.get(i).getReturn_sum());
			objects.add(caseInfoModels.get(i).getParagraphtime());
			if(null == caseInfoModels.get(i).getConfirm_state()){
				objects.add("未回款");
			}else if("-1".equals(caseInfoModels.get(i).getConfirm_state())){
				objects.add("回款未确认");
			}else if("1".equals(caseInfoModels.get(i).getConfirm_state())){
				objects.add("回款确认");
			}else if("2".equals(caseInfoModels.get(i).getConfirm_state())){
				objects.add("回款确认未通过");
			}
			objects.add(caseInfoModels.get(i).getConfirm_date());
			objects.add(caseInfoModels.get(i).getFinance_remark());
			objects.add(caseInfoModels.get(i).getCase_username());
			objects.add(caseInfoModels.get(i).getCase_areaname());
			objects.add(caseInfoModels.get(i).getRemark());
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "儿童基因库-宿迁子渊"
				+ DateUtils.Date2String(new Date()));
	}

	@Override
	public boolean updateCaseState(Map<String, Object> params) {
		try {
			String[] case_ids = params.get("case_id").toString().split(",");
			for (String string : case_ids) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("case_id", string);
				map.put("verify_state", params.get("verify_state"));
				rdsChildrenRegisterMapper.updateCaseState(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<RdsChildrenCasePhotoModel> queryCasePhoto(
			Map<String, Object> params) {
		return rdsChildrenRegisterMapper.queryCasePhoto(params);
	}

	@Override
	public RdsChildrenResponse queryPageCasePhoto(Map<String, Object> params) {
		RdsChildrenResponse response = new RdsChildrenResponse();
		List<RdsChildrenCasePhotoModel> caseInfoModels = rdsChildrenRegisterMapper
				.queryCasePhoto(params);
		int count = rdsChildrenRegisterMapper.queryCasePhotoCount(params);
		response.setItems(caseInfoModels);
		response.setCount(count);
		return response;
	}

	/**
	 * 通过流传输文件
	 */
	@Override
	public void getImg(HttpServletResponse response, String filename) {
		if (StringUtils.isNotEmpty(filename)) {
			if (FileUtils.getState(ATTACHMENTPATH + filename)) {
				DownLoadUtils.download(response, ATTACHMENTPATH + filename);
			}
		}
	}

	@Override
	@Transactional
	public boolean yesVerify(Map<String, Object> params) throws Exception {
		return rdsChildrenRegisterMapper.updateCaseState(params) > 0 ? true
				: false;
	}

	@Override
	public void download(HttpServletResponse response, String case_id,
			String case_code) {
		Map<String, Object> map = new HashMap<>();
		map.put("case_id", case_id);
		List<RdsChildrenCasePhotoModel> list = rdsChildrenRegisterMapper
				.queryProductCasePhoto(map);
		List<File> listFile = new LinkedList<>();
		for (RdsChildrenCasePhotoModel rdsChildrenCasePhotoModel : list) {
			String filename = rdsChildrenCasePhotoModel.getPhoto_path();
			File file = new File(ATTACHMENTPATH + filename);
			listFile.add(file);
		}
		// 删除压缩包
		RdsFileUtil.delFile(ATTACHMENTPATH + "/" + case_code + ".zip");
		// 添加压缩文件
		RdsFileUtil.zipFiles(listFile, new File(ATTACHMENTPATH + "/"
				+ case_code + ".zip"));
		// 下载压缩包
		String filename = ATTACHMENTPATH + "/" + case_code + ".zip";
		File file = new File(filename);
		if (file.exists()) {
			response.reset();
			response.setHeader(
					"Content-Disposition",
					"attachment; filename="
							+ filename.substring(filename
									.lastIndexOf(File.separator) + 1));
			response.setContentType("application/octet-stream; charset=utf-8");
			FileInputStream in = null;
			OutputStream toClient = null;
			try {
				in = new FileInputStream(file);
				// 得到文件大小
				int i = in.available();
				byte data[] = new byte[i];
				// 读数据
				in.read(data);
				toClient = response.getOutputStream();
				toClient.write(data);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (toClient != null) {
					try {
						toClient.flush();
						toClient.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}
		// 设置打印时间
		RdsChildrenPrintMapper.changePrintState(map);
	}

	public void create(Map<String, Object> params) throws IOException,
			TemplateException {
		// 要填入模本的数据文件
		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		Map<String, Object> dataMap = (Map<String, Object>) params
				.get("dataMap");
		configuration.setClassForTemplateLoading(this.getClass(),
				"/com/rds/children/template");
		Template t = null;
		// test.ftl为要装载的模板
		t = configuration.getTemplate((String) params.get("template_name"));
		// 输出文档路径及名称
		String filepath = (String) params.get("file_name");
		File outFile = new File(filepath);
		File directory = new File(filepath.substring(0,
				filepath.lastIndexOf(File.separator)));
		if (!directory.exists())
			FileUtils.createFolder(filepath.substring(0,
					filepath.lastIndexOf(File.separator)));
		Writer out = null;
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				outFile), "UTF-8"));
		try {
			t.process(dataMap, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
	
	
	@Override
	public void downWord(HttpServletResponse response, String case_id,
			String case_code,String file_name)  	throws Exception{ 
		// 要填入模版的数据文件
				Map<String, Object> createparams = new HashMap<>();
				Map dataMap = new HashMap();
				String template_name   =  "jysfzmodel.ftl";
				// 案例基本信息
				Map<String, Object>  params = new HashMap<>();
				params.put("case_id", case_id);
				RdsChildrenCaseInfoModel rdsChildrenCaseInfoModel=rdsChildrenRegisterMapper.getcaseinfobyself(params);
				//监护人信息
				List<RdsChildrenCustodyInfoModel>  list = rdsChildrenRegisterMapper.getCustodyInfo(params);
					
				// 根据配置表tb_children_locus_info查询排好序的试剂点位信息，即tb_children_locus_info表里所有值
				List<Map<String, String>> atelierstall = rdsChildrenRegisterMapper
						.queryOrderbyid(params);
				
				//获取基因座
				List<String> locusNamelist = new ArrayList<String>();//所以基因位点
				List<String> locusValueonelist = new ArrayList<String>();//点位值 
				List<String> locusValuetwolist = new ArrayList<String>();//点位值 
				for (Map<String, String>areatelist: atelierstall) {
				String locusName=	areatelist.get("locus_name");
				locusNamelist.add(locusName);
				String locusValue=	areatelist.get("locus_value");
				String strs[] = locusValue.split(",");
				locusValueonelist.add(strs[0]);
				locusValuetwolist.add (strs.length == 1 ? strs[0] : strs[1]);
				}
		dataMap.put("case_code", case_code);		
		dataMap.put("case_name", rdsChildrenCaseInfoModel.getChild_name());
		dataMap.put("gather_time", rdsChildrenCaseInfoModel.getGather_time());
		dataMap.put("child_sex", rdsChildrenCaseInfoModel.getChild_sex());//0女1男
		dataMap.put("birth_date", rdsChildrenCaseInfoModel.getBirth_date());//出生日期
		dataMap.put("custody_callname", list .get(0).getCustody_callname());//称谓
		dataMap.put("custody_name", list .get(0).getCustody_name() );//监护人姓名
		dataMap.put("custody_idnumber", list .get(0).getId_number() );//监护人身份证
		dataMap.put("phone", list .get(0).getPhone() );//监护人联系方式
		dataMap.put("lifearea", rdsChildrenCaseInfoModel.getLife_area());//监护人生活地址
		dataMap.put("atelierstall", atelierstall);//基因点所有值
		dataMap.put("locusNamelist", locusNamelist);//基因点名称
		dataMap.put("locusValueonelist", locusValueonelist);//基因点值1
		dataMap.put("locusValuetwolist", locusValuetwolist);//基因点值2
		createparams.put("dataMap", dataMap);
		createparams.put("template_name", template_name);
		createparams.put("file_name", file_name);
			create(createparams);
	}
	
	@Override
	public boolean exsitSampleCode(Map<String, Object> params) {
		int result = rdsChildrenRegisterMapper.exsitSampleCode(params);
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public void downloadPhoto(HttpServletResponse response, String photo_id) {
		Map<String, Object> map = new HashMap<>();
		map.put("photo_id", photo_id);
		List<RdsChildrenCasePhotoModel> list = rdsChildrenRegisterMapper
				.queryCasePhoto(map);
		String filename = list.get(0).getPhoto_path();
		File file = new File(ATTACHMENTPATH + filename);
		if (file.exists()) {
			response.reset();
			response.setHeader(
					"Content-Disposition",
					"attachment; filename="
							+ filename.substring(filename
									.lastIndexOf(File.separator) + 1));
			response.setContentType("application/octet-stream; charset=utf-8");
			FileInputStream in = null;
			OutputStream toClient = null;
			try {
				in = new FileInputStream(file);
				// 得到文件大小
				int i = in.available();
				byte data[] = new byte[i];
				// 读数据
				in.read(data);
				toClient = response.getOutputStream();
				toClient.write(data);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (toClient != null) {
					try {
						toClient.flush();
						toClient.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	

}
