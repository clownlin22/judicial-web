package com.rds.alcohol.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.pdf.codec.Base64.InputStream;
import com.rds.alcohol.mapper.RdsAlcoholPrintMapper;
import com.rds.alcohol.model.RdsAlcoholAttachmentModel;
import com.rds.alcohol.model.RdsAlcoholCaseInfoModel;
import com.rds.alcohol.model.RdsAlcoholIdentifyModel;
import com.rds.alcohol.model.RdsAlcoholResponse;
import com.rds.alcohol.model.RdsAlcoholSampleInfoModel;
import com.rds.alcohol.service.RdsAlcoholPrintService;
import com.rds.code.date.DateUtils;
import com.rds.code.utils.FileUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.judicial.model.RdsJudicialPrintModel;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import sun.misc.BASE64Encoder;


@Service
public class RdsAlcoholPrintServiceImpl implements RdsAlcoholPrintService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";
	private static final String ATTACHMENT_PATH = PropertiesUtils.readValue(
			FILE_PATH, "alcohol_file");

	@Autowired
	private RdsAlcoholPrintMapper RdsAlcoholPrintMapper;

	@Override
	public RdsAlcoholResponse getPrintCaseInfo(Map<String, Object> params) {
		RdsAlcoholResponse response = new RdsAlcoholResponse();
		List<RdsAlcoholCaseInfoModel> caseInfoModels = RdsAlcoholPrintMapper
				.getPrintCaseInfo(params);
		int count = RdsAlcoholPrintMapper.countPrintCaseInfo(params);
		response.setCount(count);
		response.setItems(caseInfoModels);
		return response;
	}

	@Override
	public List<RdsJudicialPrintModel> getPrintModel(String type) {
		return RdsAlcoholPrintMapper.getPrintModel(type);
	}

	@Override
	public RdsAlcoholCaseInfoModel getCaseInfo(String case_code) {
		RdsAlcoholCaseInfoModel caseInfoModel = RdsAlcoholPrintMapper
				.getCaseInfo(case_code);
		String close_time = caseInfoModel.getClose_time();
		String dateToCnDate = dateToCnDate(close_time);
		caseInfoModel.setClose_time(dateToCnDate);
		List<RdsAlcoholIdentifyModel> list=RdsAlcoholPrintMapper.getIdentifyById(caseInfoModel.getCase_id());
		
		caseInfoModel.setIy(list);
		caseInfoModel.setAccept_time(DateUtils
				.DateString2DateString(caseInfoModel.getAccept_time()));
		caseInfoModel.setClient_time(DateUtils
				.DateString2DateString(caseInfoModel.getClient_time()));
//		if (StringUtils.isNotEmpty(caseInfoModel.getClose_time())) {
//			caseInfoModel.setClose_time(DateUtils
//					.DateString2DateString(caseInfoModel.getClose_time()));
//		} else {
//			caseInfoModel.setClose_time(DateUtils.zhformat.format(new Date()));
//		}
		return caseInfoModel;
	}

	@Override
	public RdsAlcoholSampleInfoModel getSampleInfo(String sample_id) {
		return RdsAlcoholPrintMapper.getSampleInfo(sample_id);
	}

	@Override
	public RdsAlcoholIdentifyModel getIdentify(String per_name) {
		return RdsAlcoholPrintMapper.getIdentify(per_name);
	}

 

	@Override
	public List<RdsAlcoholAttachmentModel> getAttachment(String case_id) {
		List<RdsAlcoholAttachmentModel> attachment = RdsAlcoholPrintMapper.getAttachment(case_id);
		return attachment;
	}


	@Override
	public boolean printCase(Map<String, Object> params) {
	 
		int count = RdsAlcoholPrintMapper.printCase(params);
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public void createJudicialDocByCaseCode(Map<String, Object> params)
			throws Exception {
		ArrayList<RdsAlcoholIdentifyModel> list = new ArrayList<RdsAlcoholIdentifyModel>();
		Calendar ca=Calendar.getInstance();
		Map<String, Object> createparams = new HashMap<>();
		String type = params.get("type") == null ? "" : params.get("type")
				.toString();
		List<RdsJudicialPrintModel> model = RdsAlcoholPrintMapper
				.getPrintModel(type);
		String case_code = params.get("case_code") == null ? "" : params.get(
				"case_code").toString();
		RdsAlcoholCaseInfoModel caseInfoModel = RdsAlcoholPrintMapper
				.getCaseInfo(case_code);
		String close_time = caseInfoModel.getClose_time();
		String dateToCnDate = dateToCnDate(close_time);
		caseInfoModel.setClose_time(dateToCnDate);

		List<RdsAlcoholIdentifyModel> lists=RdsAlcoholPrintMapper.getIdentifyById(caseInfoModel.getCase_id());
		caseInfoModel.setIy(lists);
 
		List<RdsAlcoholAttachmentModel> attachmentModels=RdsAlcoholPrintMapper.getAttachment(caseInfoModel.getCase_id());
		for (RdsAlcoholAttachmentModel rd : attachmentModels) {
			String att_path = rd.getAtt_path();
			if(StringUtils.isNotEmpty(att_path)){
				String path=ATTACHMENT_PATH+ att_path; 
				if (RdsFileUtil.getState(path)) { 
					String ImageStr = GetImageStr(path);
					rd.setPath(ImageStr);
				}
			}
		}
		caseInfoModel.setAm(attachmentModels);

		caseInfoModel.setAccept_time(DateUtils
				.DateString2DateString(caseInfoModel.getAccept_time()));
		caseInfoModel.setClient_time(DateUtils
				.DateString2DateString(caseInfoModel.getClient_time()));
//		if (StringUtils.isNotEmpty(caseInfoModel.getClose_time())) {
//			caseInfoModel.setClose_time(DateUtils
//					.DateString2DateString(caseInfoModel.getClose_time()));
//		} else {
//			caseInfoModel.setClose_time(DateUtils.zhformat.format(new Date()));
//		}
		RdsAlcoholSampleInfoModel sampleInfoModel = RdsAlcoholPrintMapper
				.getSampleInfo(caseInfoModel.getSample_id());
		caseInfoModel.setSi(sampleInfoModel);
		String template_name = params.get("report_model")+".ftl";

		createparams.put("year", String.valueOf(ca.get(Calendar.YEAR)));
		createparams.put("caseInfoModel", caseInfoModel);
		createparams.put("template_name", template_name);
		createparams.put("file_name", params.get("file_name"));
		createparams.put("model1", model.get(0));
		createparams.put("model2", model.get(1));
		create(createparams);
	}

	//日期转中
	public  String dateToCnDate(String date) {     
		String result = "";     
		String[]  cnDate = new String[]{"○","一","二","三","四","五","六","七","八","九"};     
		String ten = "十";     
		String[] dateStr = date.split("-");     
		for (int i=0; i<dateStr.length; i++) {     
			for (int j=0; j<dateStr[i].length(); j++) {     
				String charStr = dateStr[i];     
				String str = String.valueOf(charStr.charAt(j));     
				if (charStr.length() == 2) {     
					if (charStr.equals("10")) {     
						result += ten;     
						break;     
					} else {     
						if (j == 0) {     
							if (charStr.charAt(j) == '1')      
								result += ten;     
							else if (charStr.charAt(j) == '0')     
								result += "";     
							else    
								result += cnDate[Integer.parseInt(str)] + ten;     
						}     
						if (j == 1) {     
							if (charStr.charAt(j) == '0')     
								result += "";     
							else    
								result += cnDate[Integer.parseInt(str)];     
						}     
					}     
				} else {     
					result += cnDate[Integer.parseInt(str)];     
				}     
			}     
			if (i == 0) {     
				result += "年";     
				continue;     
			}     
			if (i == 1) {     
				result += "月";     
				continue;     
			}     
			if (i == 2) {     
				result += "日";     
				continue;     
			}     
		}     
		return result;     
	}    
	// 图片转化成base64字符串
	public String GetImageStr(String imgFile) {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		FileInputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}


	public void create(Map<String, Object> params) throws IOException,
	TemplateException {
		// 要填入模本的数据文件
		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		configuration.setClassForTemplateLoading(this.getClass(),
				"/com/rds/alcohol/template");
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
		t.process(params, out);
		out.flush();
		out.close();
	}




}
