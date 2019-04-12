package com.rds.alcohol.service.impl;

import com.rds.alcohol.mapper.RdsAlcoholAttachmentMapper;
import com.rds.alcohol.mapper.RdsAlcoholRegisterMapper;
import com.rds.alcohol.model.*;
import com.rds.alcohol.service.RdsAlcoholIdentifyService;
import com.rds.alcohol.service.RdsAlcoholRegisterService;
import com.rds.code.date.DateUtils;
import com.rds.code.utils.DownLoadUtils;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.file.XmlParseUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.model.RdsJudicialCaseFeeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
@Service
public class RdsAlcoholRegisterServiceImple implements
RdsAlcoholRegisterService {

	@Autowired
	private RdsAlcoholAttachmentMapper aMapper;
	@Autowired
	private RdsAlcoholRegisterMapper RdsAlcoholRegisterMapper;

	@Autowired
	private RdsAlcoholIdentifyService RdsAlcoholIdentifyService;

	private static final String XML_PATH_9 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config9.xml";

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";
	private static final String ATTACHMENT_PATH = PropertiesUtils.readValue(
			FILE_PATH, "alcohol_file");

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	@Override
	public RdsAlcoholResponse getCaseInfo(Map<String, Object> params) {
		RdsAlcoholResponse response = new RdsAlcoholResponse();
		List<RdsAlcoholCaseInfoModel> caseInfos = RdsAlcoholRegisterMapper
				.getCaseInfo(params);
		int count = RdsAlcoholRegisterMapper.countCaseInfo(params);
		response.setCount(count);
		response.setItems(caseInfos);
		return response;
	}

	@Override
	public boolean deleteCaseInfo(Map<String, Object> params) {
		int count = RdsAlcoholRegisterMapper.deleteCaseInfo(params);
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean exsitCaseCode(String case_code) {
		int count = RdsAlcoholRegisterMapper.exsitCaseCode(case_code);
		if (count > 0) {
			return false;
		}
		return true;
	}

	public static int printArray(MultipartFile[] array,MultipartFile value){  
		for(int i = 0;i<array.length;i++){  
			if(array[i].equals(value)){  
				return i;  
			}  
		}  
		return 0; 
	}

	@Override
	public boolean addCaseInfo(RdsAlcoholCaseInfoModel caseInfoModel,
			RdsAlcoholSampleInfoModel sampleInfomodel,
			RdsJudicialCaseFeeModel casefee, MultipartFile[] files,RdsAlcoholAttachmentModel att) {
		if (files.length > 0) {
			caseInfoModel.setCase_code(caseInfoModel.getCase_code().trim());
			if (exsitCaseCode(caseInfoModel.getCase_code())) {//判断案例码是否已经存在
				String attachmentPath = ATTACHMENT_PATH   //ATTACHMENT  附件
						+ caseInfoModel.getCase_code() + File.separatorChar;  


				String case_id = UUIDUtil.getUUID();
				try {
					for (MultipartFile file : files) {    //文件上传spring的
						long filetempname = new Date().getTime();
						caseInfoModel.setAttachment(caseInfoModel.getCase_code()
								+ File.separator + filetempname 
								+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));

						//					if (!RdsFileUtil.getState(attachmentPath)) {  // 判断指定路径文件或目录是否存在

						RdsFileUtil
						.fileUpload(   
								attachmentPath
								+filetempname 
								+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")),
								file.getInputStream());
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("att_id", UUIDUtil.getUUID());
						params.put("case_id", case_id);
						//						MultipartFile[] photo = (MultipartFile[]) params.get("files");
						int o = printArray(files, file);
						String att_type = att.getAtt_type();
						String[] att_types = att_type.split(",");
						List<String> asList = Arrays.asList(att_types);
						params.put("att_type",asList.get(o));
						params.put("att_path",
								caseInfoModel.getCase_code()
								+ File.separator+filetempname 
								+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
						aMapper.insertAttachment(params);   //  RdsAlcoholAttachmentMapper.xml  附件
					}
					caseInfoModel.setCase_id(case_id);
					caseInfoModel.setSample_id(UUIDUtil.getUUID());

					/** 设置案例num**/
					int case_num=1;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
					Date date = new Date();
					String xml_time = XmlParseUtil.getXmlValue(XML_PATH_9,
							"alcohol_date");
					String now_time = sdf.format(date);
					if (xml_time.equals(now_time)) {
						case_num = Integer.parseInt(XmlParseUtil
								.getXmlValue(XML_PATH_9, "alcohol_num")) + 1;
						XmlParseUtil
						.updateXmlValue(XML_PATH_9, "alcohol_num",
								String.valueOf(Integer
										.parseInt(XmlParseUtil.getXmlValue(
												XML_PATH_9,
												"alcohol_num")) + 1));
					} else {
						XmlParseUtil.updateXmlValue(XML_PATH_9,
								"alcohol_date", now_time);
						XmlParseUtil.updateXmlValue(XML_PATH_9, "alcohol_num",
								"1");
						case_num = 1;
					}
					String s = caseInfoModel.getCase_checkper();
					if(s!=null){
						String[] split = s.split(",");
						for (String case_checkper : split) {
							RdsAlcoholIdentifyCaseinfo ps =new RdsAlcoholIdentifyCaseinfo();
							String pid=RdsAlcoholIdentifyService.selectper_id(case_checkper);
							ps.setCid(caseInfoModel.getCase_id());
							ps.setPid(pid);
							ps.setId(UUIDUtil.getUUID());
							boolean flag=RdsAlcoholIdentifyService.add(ps);
						}
					 
					}
					 
					 
					caseInfoModel.setCase_num(case_num);
					/** 设置案例num**/

					int count = RdsAlcoholRegisterMapper
							.addCaseInfo(caseInfoModel); //RdsAlcoholRegisterMapper.xml
					if (count > 0) {
						sampleInfomodel.setSample_id(caseInfoModel
								.getSample_id());
						RdsAlcoholRegisterMapper
						.addSampleInfo(sampleInfomodel); //RdsAlcoholRegisterMapper.xml
						casefee.setCase_id(caseInfoModel.getCase_id());
						casefee.setId(UUIDUtil.getUUID());
						RdsAlcoholRegisterMapper.addCaseFee(casefee); //RdsAlcoholRegisterMapper.xml
						//								return true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		//		}
		return false;
	}

	@Override
	public void getImg(HttpServletResponse response, String filename) {
		DownLoadUtils.download(response, ATTACHMENT_PATH + filename);
	}

	@Override
	public boolean updateCaseCode(RdsAlcoholCaseInfoModel caseInfoModel,
			RdsAlcoholSampleInfoModel sampleInfoModel) {
		int count = RdsAlcoholRegisterMapper.updateCaseCode(caseInfoModel);
		 
		String s = caseInfoModel.getCase_checkper();
		if(s!=null){
			RdsAlcoholIdentifyService.deleteCaseIdetity(caseInfoModel.getCase_id());
			String[] split = s.split(",");
			for (String case_checkper : split) {
				RdsAlcoholIdentifyCaseinfo ps =new RdsAlcoholIdentifyCaseinfo();
				String pid=RdsAlcoholIdentifyService.selectper_id(case_checkper);
				ps.setCid(caseInfoModel.getCase_id());
				ps.setPid(pid);
				ps.setId(UUIDUtil.getUUID());
				boolean add = RdsAlcoholIdentifyService.add(ps);
			}
		 
		}
		if (count > 0) {
			if(caseInfoModel.getStand_sum()!=null||caseInfoModel.getReal_sum()!=null){
				RdsAlcoholRegisterMapper.updateCasefee(caseInfoModel);
			}
			RdsAlcoholRegisterMapper.updateSampleCode(sampleInfoModel);
			return true;
		}
		return false;
	}
 

	@Override
	public void exportCaseInfo(RdsAlcoholQueryParam param,
			HttpServletResponse response) {
		List<RdsAlcoholCaseInfoModel> caseInfos = RdsAlcoholRegisterMapper
				.getAllCaseInfo(param);
		String filename = "案例列表";
		Object[] titles = { "案例编号", "案例条形码", "委托人","送检人","送检人号码", "所属人", "所属地区",
				"地址", "当事人姓名", "身份证号", "样品剂量", "事件描述", "邮寄地址", "邮件接收人", "联系电话",
				"附件", "模板名称", "受理时间", "登记人", "状态" };
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < caseInfos.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			objects.add(caseInfos.get(i).getCase_id());
			objects.add(caseInfos.get(i).getCase_code());
			objects.add(caseInfos.get(i).getClient());
			objects.add(caseInfos.get(i).getCheckper());
			objects.add(caseInfos.get(i).getCheckper_phone());
			objects.add(caseInfos.get(i).getReceiver_area() + "-"
					+ caseInfos.get(i).getReceiver());
			objects.add(caseInfos.get(i).getArea_name());
			objects.add(caseInfos.get(i).getAddress());
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sample_id", caseInfos.get(i).getSample_id());
			RdsAlcoholSampleInfoModel sampleInfoModel = RdsAlcoholRegisterMapper
					.getSampleInfo(params);
			objects.add(sampleInfoModel.getSample_name());
			objects.add(sampleInfoModel.getId_number());
			objects.add(sampleInfoModel.getSample_ml() + "mL");
			objects.add(caseInfos.get(i).getEvent_desc());
			objects.add(caseInfos.get(i).getMail_address());
			objects.add(caseInfos.get(i).getMail_per());
			objects.add(caseInfos.get(i).getMail_phone());
			objects.add(caseInfos.get(i).getAttachment());
			objects.add(caseInfos.get(i).getReport_modelname());
			objects.add(caseInfos.get(i).getAccept_time());
			objects.add(caseInfos.get(i).getCase_in_pername());
			objects.add(caseInfos.get(i).getStateStr());
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "案例信息"
				+ DateUtils.Date2String(new Date()));
	}

	@Override
	public RdsAlcoholSampleInfoModel getSampleInfo(Map<String, Object> params) {
		return RdsAlcoholRegisterMapper.getSampleInfo(params);
	}

	@Override
	public Map<String, Object> getClient() {
		List<RdsAlcoholDicValueModel> dvList = RdsAlcoholRegisterMapper.getClient();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", dvList);
		return result;
	}

	@Override
	public Map<String, Object> getClient2() {
		List<RdsAlcoholDicValueModel> dvList = RdsAlcoholRegisterMapper.getClient2();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", dvList);
		return result;
	}

	/**
	 * 导出毒物检测数据
	 * @throws Exception 
	 */
	@Override
	public void exportCaseInfo(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "毒物检测";
		List<RdsAlcoholCaseExportModel> lists = RdsAlcoholRegisterMapper.exportCaseInfo(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			Object[] titles = { "案例编号","受理日期","打印日期","登记日期","登记人","模板名称","状态","委托人", "送检人","送检人电话", "委托日期", "归属人","归属地","所属地","被检验人",
					"身份证号","血管类型","是否检出","应收款项","所付款项", "到款时间",
					"优惠价格", "手续费", "场地费", "财务备注","样本描述", "备注和要求" ,"鉴定人",
					"血液编号","血液容量","案例简介","案例详情","邮件地址","邮件接收人","联系电话","是否重新鉴定"};
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < lists.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsAlcoholCaseExportModel models = lists.get(i);
				objects.add(models.getCase_code());
				objects.add(models.getAccept_time());
				objects.add(models.getClose_time());
				objects.add(models.getSample_time());
				objects.add(models.getCase_in_per());
				objects.add(models.getReport_modelname());
				//0:登记状态，1是审核未通过，2可以实验中，3可以打印，4可以邮寄，5可以归档，6已归档,7删除
				if(models.getState()==0){
					objects.add("已登记");
				}else if(models.getState()==1){
					objects.add("审核未通过");
				}else if(models.getState()==2){
					objects.add("可以实验");
				}else if(models.getState()==3){
					objects.add("可以打印");
				}else if(models.getState()==4){
					objects.add("可以邮寄");
				}else if(models.getState()==5){
					objects.add("可以归档");
				}else if(models.getState()==6){
					objects.add("已归档");
				}else if(models.getState()==7){
					objects.add("已删除");
				}
				objects.add(models.getClient());
				objects.add(models.getCheckper());
				objects.add(models.getCheckper_phone());
				objects.add(models.getClient_time());
				objects.add(models.getReceiver());
				objects.add(models.getReceiver_area());
				objects.add(models.getArea_name());
				objects.add(models.getSample_name());
				objects.add(models.getId_number());
				objects.add(((null == models.getIsDoubleTube() || ""
						.equals(models.getIsDoubleTube())) ? 0 : models
								.getIsDoubleTube()) == 0 ? "促凝管" : "真空采血管");
				objects.add(((null == models.getIs_detection() || ""
						.equals(models.getIs_detection())) ? 0 : models
								.getIs_detection()) == 0 ? "否" : "是");
				objects.add(models.getReal_sum());
				objects.add(models.getReturn_sum());
				objects.add(StringUtils.dateToChineseTen(models.getParagraphtime()));
				objects.add(models.getDiscountPrice());
				objects.add(models.getFees());
				objects.add(models.getSiteFee());
				objects.add(models.getFinanceRemark());
				objects.add(models.getSample_remark());
				objects.add(models.getRemark());
				objects.add(models.getCase_checkper());
				objects.add(models.getBloodnumA());
				objects.add(models.getBloodnumB());
				objects.add(models.getCase_intr());
				objects.add(models.getCase_det());
				objects.add(models.getMail_address());
				objects.add(models.getMail_per());
				objects.add(models.getMail_phone());
				objects.add(((null == models.getIs_check() || "".equals(models.getIs_check())) ? 0 : models.getIs_check()) == 0 ? "否" : "是");
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "毒物检测");
		}else{

			Object[] titles = { "案例编号","受理日期","打印日期","登记日期","登记人","模板名称","状态", "委托人", "送检人","送检人电话",
					"委托日期", "归属人","归属地","所属地","被检验人","身份证号","血管类型","是否检出","样本描述", "备注和要求"
					,"鉴定人","血液编号","血液容量","案例简介","案例详情","邮件地址","邮件接收人","联系电话","是否重新鉴定"};
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < lists.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsAlcoholCaseExportModel models = lists.get(i);
				objects.add(models.getCase_code());//案例编号
				objects.add(models.getAccept_time());//
				objects.add(models.getClose_time());//
				objects.add(models.getSample_time());
				objects.add(models.getCase_in_per());
				objects.add(models.getReport_modelname());
				if(models.getState()==0){
					objects.add("已登记");
				}else if(models.getState()==1){
					objects.add("审核未通过");
				}else if(models.getState()==2){
					objects.add("可以实验");
				}else if(models.getState()==3){
					objects.add("可以打印");
				}else if(models.getState()==4){
					objects.add("可以邮寄");
				}else if(models.getState()==5){
					objects.add("可以归档");
				}else if(models.getState()==6){
					objects.add("已归档");
				}else if(models.getState()==7){
					objects.add("已删除");
				}
				objects.add(models.getClient());
				objects.add(models.getCheckper());
				objects.add(models.getCheckper_phone());
				objects.add(models.getClient_time());
				objects.add(models.getReceiver());
				objects.add(models.getReceiver_area());
				objects.add(models.getArea_name());
				objects.add(models.getSample_name());
				objects.add(models.getId_number());
				objects.add(((null == models.getIsDoubleTube() || ""
						.equals(models.getIsDoubleTube())) ? 0 : models
								.getIsDoubleTube()) == 0 ? "促凝管" : "真空采血管");
				objects.add(((null == models.getIs_detection() || ""
						.equals(models.getIs_detection())) ? 0 : models
								.getIs_detection()) == 0 ? "否" : "是");
				objects.add(models.getSample_remark());
				objects.add(models.getRemark());
				objects.add(models.getCase_checkper());
				objects.add(models.getBloodnumA());
				objects.add(models.getBloodnumB());
				objects.add(models.getCase_intr());
				objects.add(models.getCase_det());
				objects.add(models.getMail_address());
				objects.add(models.getMail_per());
				objects.add(models.getMail_phone());
				objects.add(((null == models.getIs_check() || "".equals(models.getIs_check())) ? 0 : models.getIs_check()) == 0 ? "否" : "是");
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "毒物检测");
		}
	}

	@Override
	public boolean exsitcase_code(String case_code) {
		int count = RdsAlcoholRegisterMapper.exsitcase_code(case_code);
		if (count > 0) {
			return false;
		}
		return true;
	}

	@Override
	public Map<String, Object> getIdentificationPer() {
		List<RdsAlcoholDicValueModel> dvList = RdsAlcoholRegisterMapper.getIdentificationPer();
		Map<String, Object> result = new HashMap<>();
		result.put("data", dvList);
		return result;
	}


}
