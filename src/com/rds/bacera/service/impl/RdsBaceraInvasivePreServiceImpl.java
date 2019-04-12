package com.rds.bacera.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.icu.text.SimpleDateFormat;
import com.rds.bacera.mapper.RdsBaceraInvasivePreMapper;
import com.rds.bacera.model.RdsBaceraBoneAgeFeeModel;
import com.rds.bacera.model.RdsBaceraInvasivePhotoModel;
import com.rds.bacera.model.RdsBaceraInvasivePreModel;
import com.rds.bacera.service.RdsBaceraBoneAgeService;
import com.rds.bacera.service.RdsBaceraInvasivePreService;
import com.rds.code.date.DateUtils;
import com.rds.code.image.ImgUtil;
import com.rds.code.utils.DownLoadUtils;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.FileUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.file.XmlParseUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.finance.mapper.RdsCaseFinanceMapper;
import com.rds.finance.service.RdsCaseFinanceService;
import com.rds.upc.model.RdsUpcUserModel;

@Service("rdsBaceraInvasivePreService")
public class RdsBaceraInvasivePreServiceImpl implements
		RdsBaceraInvasivePreService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");
	
	private static final String XML_PATH_1 = ConfigPath.getWebInfPath() + "xml"
			+ File.separatorChar + "config5.xml";
	// 附件存放地址
	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
				FILE_PATH, "invasive_head_photo") + File.separatorChar;
	
	@Autowired
	private IdentityService identityService;

	@Setter
	@Autowired
	private RdsBaceraBoneAgeService rdsBoneAgeService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private RdsCaseFinanceService rdsCaseFinanceService;

	@Setter
	@Autowired
	private RdsBaceraInvasivePreMapper rdsInvasivePreMapper;

	@Autowired
	RdsCaseFinanceMapper rdsCaseFinanceMapper;

	
	private String getCaseCode(String inspectionunit){
        String num="";
		
		String now_time = com.rds.code.utils.StringUtils
				.dateToSix(new Date());
		 DecimalFormat df = new DecimalFormat("0000");
		if("5".equals(inspectionunit)||"2".equals(inspectionunit)||"1".equals(inspectionunit)){
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_1,
					"case_code_date");
		if (xml_time.equals(now_time)) {
			num = "02"
					+ now_time.substring(2, 6)
					+ df.format(Integer.parseInt(XmlParseUtil
							.getXmlValue(XML_PATH_1, "case_code_num")) + 1);
			XmlParseUtil
					.updateXmlValue(XML_PATH_1, "case_code_num", String
							.valueOf(Integer.parseInt(XmlParseUtil
									.getXmlValue(XML_PATH_1,
											"case_code_num")) + 1));
		} else {
			XmlParseUtil.updateXmlValue(XML_PATH_1, "case_code_date",
					now_time);
			XmlParseUtil.updateXmlValue(XML_PATH_1, "case_code_num",
					"1");
			num = "02"
					+ now_time.substring(2, 6)
					+ df.format(Integer.parseInt(XmlParseUtil
							.getXmlValue(XML_PATH_1, "case_code_num")));
		}
		return num;
		}else{
			String xml_time = XmlParseUtil.getXmlValue(XML_PATH_1,
					"case_code_date05");
			if (xml_time.equals(now_time)) {
				num = "05"
						+ now_time.substring(2, 6)
						+ df.format(Integer.parseInt(XmlParseUtil
								.getXmlValue(XML_PATH_1, "case_code_num05")));
				XmlParseUtil
						.updateXmlValue(XML_PATH_1, "case_code_num05", String
								.valueOf(Integer.parseInt(XmlParseUtil
										.getXmlValue(XML_PATH_1,
												"case_code_num05")) + 1));
			} else {
				XmlParseUtil.updateXmlValue(XML_PATH_1, "case_code_date05",
						now_time);
				XmlParseUtil.updateXmlValue(XML_PATH_1, "case_code_num05",
						"1");
				num = "05"
						+ now_time.substring(2, 6)
						+ df.format(Integer.parseInt(XmlParseUtil
								.getXmlValue(XML_PATH_1, "case_code_num05")));
			}
			return num;
		}
	}
	@Override
	public Object queryAll(Object params) throws Exception {
		return 0;
		// return rdsUpcBlackListMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsInvasivePreMapper.queryAllCount(params));
		result.put("data", rdsInvasivePreMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Transactional
	@Override
	public int update(Map<String, Object> params) throws Exception {
		       String cancelif=params.get("cancelif").toString();
               if("1".equals(cancelif)){
            	   return rdsInvasivePreMapper.update(params);
            	   }else{
            		   try {
           			Map<String, Object> map = new HashMap<>();

        			String case_id = params.get("id").toString();
        			map.put("case_id", case_id);
        			map.put("userid", params.get("inputperson"));
        			String program_type = params.get("inspectionunit").toString();
        			map.put("program_type", program_type);
        			String areaname = params.get("areaname").toString();
        			map.put("areaname", areaname);
        			String confirm_code = params.get("confirm_code").toString();
        			map.put("confirm_code", confirm_code);
        			map.put("case_type", "inversive");
        			map.put("hospital", params.get("hospital").toString());
        			map.put("areacode", params.get("areacode").toString());

        			//归属人id
        			map.put("case_userid", params.get("ownperson").toString());
        			rdsCaseFinanceService.updateCaseFeeUnite(map);
        			return rdsInvasivePreMapper.update(params);
        		} catch (Exception e) {
        			throw new Exception(e);
        		}
            }
	}

	@Override
	public int insert(Object params) throws Exception {
		try {

			return rdsInvasivePreMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	@Transactional
	@Override
	public int delete(Map<String, Object> params) throws Exception {
		try {
			String[] case_ids = params.get("id").toString().split(",");
			for (String string : case_ids) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", string);
				rdsInvasivePreMapper.delete(map);
				rdsCaseFinanceMapper.deleteInvasivePre(map);
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int queryNumExit(Map map) {
		return rdsInvasivePreMapper.queryNumExit(map);
	}

	/**
	 * 导出无创产前信息
	 */
	@Override
	public void exportInvasiveInfo(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "无创产前检测";
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			List<Object> list = rdsInvasivePreMapper.queryAllPageS(params);
			Object[] titles = { "案例编号", "条码", "日期", "姓名", "采样日期", "类型",
					"所属医院","标准价格" ,"实收金额", "回款金额", "汇款时间", "优惠价格", "财务确认状态","到款时间","财务备注", "报告上传时间","快递日期",
					"快递类型", "快递单号", "快递备注", "归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraInvasivePreModel rdsInvasivePreModel = (RdsBaceraInvasivePreModel) list
						.get(i);
				objects.add(rdsInvasivePreModel.getNum());
				objects.add(rdsInvasivePreModel.getCode());
				objects.add(StringUtils.dateToChineseTen(rdsInvasivePreModel
						.getDate()));
				objects.add(rdsInvasivePreModel.getName());
				objects.add(StringUtils.dateToChineseTen(rdsInvasivePreModel
						.getSampledate()));
				String inspectionunit=rdsInvasivePreModel.getInspectionunit().toString();
				if("1".equals(inspectionunit)){
					objects.add("NIPT(博奥)");
				}else if("2".equals(inspectionunit)){
					objects.add("NIPT(贝瑞)");
				}else if("3".equals(inspectionunit)){
					objects.add("NIPT-plus(博奥)");
				}else if("4".equals(inspectionunit)){
					objects.add("NIPT-plus(贝瑞)");
				}else if("5".equals(inspectionunit)){
					objects.add("NIPT(成都博奥)");
				}
				objects.add(rdsInvasivePreModel.getHospital());
				// 标准价格
				objects.add(("".equals(rdsInvasivePreModel.getReceivables()) || null == rdsInvasivePreModel
						.getReceivables()) ? 0 : Float
						.parseFloat(rdsInvasivePreModel.getReceivables()));
				//实收金额
				objects.add(("".equals(rdsInvasivePreModel.getPaid()) || null == rdsInvasivePreModel
						.getPaid()) ? 0 : Float
						.parseFloat(rdsInvasivePreModel.getPaid()));
				// 回款金额
				objects.add(("".equals(rdsInvasivePreModel.getPayment()) || null == rdsInvasivePreModel
						.getPayment()) ? 0 : Float
						.parseFloat(rdsInvasivePreModel.getPayment()));
				objects.add(StringUtils.dateToChineseTen(rdsInvasivePreModel.getRemittanceDate()));
				objects.add(rdsInvasivePreModel.getDiscountPrice());
				String confirm_state=rdsInvasivePreModel.getConfirm_state();
				if("-1".equals(confirm_state)){
					objects.add("未确认");
				}else if("1".equals(confirm_state))
				{objects.add("确认通过");}
				  else if("2".equals(confirm_state))
				  {objects.add("确认不通过");}
				  else if(null==confirm_state){
					  objects.add("无回款");
				  }
				objects.add(rdsInvasivePreModel.getParagraphtime());
				objects.add(rdsInvasivePreModel.getRemarks());
				objects.add(rdsInvasivePreModel.getAttachment_date());
//				objects.add("".equals(rdsInvasivePreModel.getEmaildate()) || null == rdsInvasivePreModel
//						.getEmaildate() ?"":DateUtils.Date2String(rdsInvasivePreModel
//						.getEmaildate()));//发邮件时间
				objects.add(rdsInvasivePreModel
								.getExpresstime());
				objects.add(rdsInvasivePreModel.getExpresstype());
				objects.add(rdsInvasivePreModel.getExpressnum());
				objects.add(rdsInvasivePreModel.getExpressremark());
				objects.add(rdsInvasivePreModel.getAreaname());
				objects.add(rdsInvasivePreModel.getOwnpersonname());
				objects.add(rdsInvasivePreModel.getAgentname());
				objects.add(rdsInvasivePreModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "无创产前检测");
		} else {
			List<Object> list = rdsInvasivePreMapper.queryAllPage(params);
			Object[] titles = { "案例编号", "条码", "日期", "姓名", "采样日期", "类型",
					"所属医院", "发邮件时间", "快递日期","快递类型", "快递单号", "快递备注", "归属地", "归属人",
					"代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraInvasivePreModel rdsInvasivePreModel = (RdsBaceraInvasivePreModel) list
						.get(i);
				objects.add(rdsInvasivePreModel.getNum());
				objects.add(rdsInvasivePreModel.getCode());
				objects.add(StringUtils.dateToChineseTen(rdsInvasivePreModel
						.getDate()));
				objects.add(rdsInvasivePreModel.getName());
				objects.add(StringUtils.dateToChineseTen(rdsInvasivePreModel
						.getSampledate()));
				String inspectionunit=rdsInvasivePreModel.getInspectionunit().toString();
				if("1".equals(inspectionunit)){
					objects.add("NIPT(博奥)");
				}else if("2".equals(inspectionunit)){
					objects.add("NIPT(贝瑞)");
				}else if("3".equals(inspectionunit)){
					objects.add("NIPT-plus(博奥)");
				}else if("4".equals(inspectionunit)){
					objects.add("NIPT-plus(贝瑞)");
				}else if("5".equals(inspectionunit)){
					objects.add("NIPT(成都博奥)");
				}
				objects.add(rdsInvasivePreModel.getHospital());
				objects.add("".equals(rdsInvasivePreModel.getEmaildate()) || null == rdsInvasivePreModel
						.getEmaildate() ?"":DateUtils.Date2String(rdsInvasivePreModel
						.getEmaildate()));
				objects.add(rdsInvasivePreModel
								.getExpresstime());
				objects.add(rdsInvasivePreModel.getExpresstype());
				objects.add(rdsInvasivePreModel.getExpressnum());
				objects.add(rdsInvasivePreModel.getExpressremark());
				objects.add(rdsInvasivePreModel.getAreaname());
				objects.add(rdsInvasivePreModel.getOwnpersonname());
				objects.add(rdsInvasivePreModel.getAgentname());
				objects.add(rdsInvasivePreModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "无创产前检测");
		}

	}

	/**
	 * 判断是否存在
	 */
	@Override
	public boolean exsitCaseCode(Map<String, Object> params) {
		if (params.get("num") != null) {
			params.put("num", params.get("num").toString().trim());
			int result = rdsInvasivePreMapper.exsitCaseCode(params);
			if (result == 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object queryInvasivePreFee(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total",
				rdsInvasivePreMapper.queryInvasivePreFeeCount(params));
		result.put("data", rdsInvasivePreMapper.queryInvasivePreFee(params));
		return result;
	}

	@Override
	public int saveInvasivePreFee(Object params) throws Exception {
		return rdsInvasivePreMapper.saveInvasivePreFee(params);
	}

	@Override
	public int updateInvasivePreFee(Object params) throws Exception {
		return rdsInvasivePreMapper.updateInvasivePreFee(params);
	}

	@Override
	public int queryInvasivePreFeeCount(Object params) throws Exception {
		return rdsInvasivePreMapper.queryInvasivePreFeeCount(params);
	}

	@Override
	public int deleteInvasivePreFee(Object params) throws Exception {
		return rdsInvasivePreMapper.deleteInvasivePreFee(params);
	}

	@Override
	public RdsBaceraBoneAgeFeeModel queryInvasivePreFeeByRec(Object params)
			throws Exception {
		List<Object> list = rdsInvasivePreMapper
				.queryInvasivePreFeeByRec(params);
		if (list.size() > 0)
			return (RdsBaceraBoneAgeFeeModel) list.get(0);
		else
			return new RdsBaceraBoneAgeFeeModel();
	}

	@Override
	public int updateVerify(Map<String, Object> params) throws Exception {
		try {
			return rdsInvasivePreMapper.updateVerify(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int saveVerify(Object params) throws Exception {
		try {
			return rdsInvasivePreMapper.saveVerify(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	@Transactional
	@Override
	public Map<String, Object> saveCaseInfo(Map<String, Object> params,
			RdsUpcUserModel user) {
		Map<String, Object> result = new HashMap<>();
		try {
			Map<String, Object> map = new HashMap<>();

			String case_id = params.get("id").toString();
			map.put("case_id", case_id);
			map.put("userid", user.getUserid());
			String program_type = params.get("inspectionunit").toString();
			map.put("program_type", program_type);
			String areaname = params.get("areaname").toString();
			map.put("areaname", areaname);
			String confirm_code = params.get("confirm_code").toString();
			map.put("confirm_code", confirm_code);
			map.put("case_type", "inversive");
			//归属人id
			map.put("case_userid", params.get("case_userid").toString());
			map.put("areacode",params.get("areacode"));
			map.put("hospital", params.get("hospital"));
			result = rdsCaseFinanceService.addCaseFeeUnite(map);
			if ((boolean) result.get("result")) {
				identityService.setAuthenticatedUserId(user.getUserid());
				ProcessInstance processInstance = runtimeService
						.startProcessInstanceByKey("processInvasivePre",
								(String) params.get("id"));
				String Process_instance_id = processInstance.getId();
				params.put("processInstanceId", Process_instance_id);
				String inspectionunit=	 params.get("inspectionunit").toString();
				String num=getCaseCode(inspectionunit);
				params.put("num", num);
				int a = rdsInvasivePreMapper.insert(params);
				if (a > 0) {
					if (null != params.get("files")) {
						RdsBaceraInvasivePhotoModel pmodel = new RdsBaceraInvasivePhotoModel();
						pmodel.setUpload_user(user.getUserid().toString());
						pmodel.setCase_id(case_id);
						try {
                                 this.uploadInvasivePhoto(pmodel,
									(MultipartFile) params.get("files"),
									(String) params.get("filetype"));
                                 }catch (IOException e) {
             						result.put("success", true);
            						result.put("result", false);
            						result.put("msg", "照片上传异常，请联系管理员!");
            						return result;
            					}
					}
					result.put("success", true);
					result.put("result", true);
					result.put("message", "数据添加成功！");
					return result;
				}
				
			}
			
		    } catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("result", false);
			result.put("message", "出错啦，请联系管理员！");
			return result;
		}
		return result;

	}
	@Transactional
	@Override
	public int updateVerifyAll(Map<String, Object> params) {
		try {
			String[] case_ids = params.get("id").toString().split(",");
			for (String string : case_ids) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", string);
				rdsInvasivePreMapper.updateVerify(map);
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object exsitOne(Object params) throws Exception {

		return rdsInvasivePreMapper.exsitOne(params);
	}
  
	@Override
	public int yesInvasivePreVerify(Map<String, Object> params) {
		try {
			return rdsInvasivePreMapper.verify(params);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}
    
	@Override
	public int noInvasivePreVerify(Map<String, Object> params) {
		try {
			return rdsInvasivePreMapper.noVerify(params);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	@Override
	public int update(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int updateEmailFlag(Map<String, Object> params) {
		
		return rdsInvasivePreMapper.updateEmailFlag(params);
	}
	
	private boolean uploadInvasivePhoto( RdsBaceraInvasivePhotoModel pmodel,
			MultipartFile photo, String filetype) throws IOException {
			// 文件路径
			String attachmentPath = pmodel.getCase_id()
					+ File.separatorChar
					+ new Date().getTime()
					+ photo.getOriginalFilename().substring(
							photo.getOriginalFilename().lastIndexOf("."),
							photo.getOriginalFilename().length());

			String photo_id = UUIDUtil.getUUID();
			pmodel.setPhoto_id(photo_id);
			pmodel.setPhoto_path(attachmentPath);
			pmodel.setPhoto_type(filetype);
			pmodel.setUpload_time(new SimpleDateFormat("yyyy-MM-dd HH:MM:SS")
					.format(new Date()));
			if (!RdsFileUtil.getState(attachmentPath
					+ photo.getOriginalFilename())) {
				// 上传
				RdsFileUtil.fileUpload(ATTACHMENTPATH + attachmentPath,
						photo.getInputStream());
				// 插入数据库
				if (rdsInvasivePreMapper.insertHeadPhoto(pmodel) <= 0)
					return false;
			} else {
				return false;
			}
		

		return true;

	}
	@Override
	public List<RdsBaceraInvasivePhotoModel> getAttachMent(
			Map<String, Object> params) {
		return rdsInvasivePreMapper.getAttachMent(params);
	}
	@Override
	public void getImg(HttpServletResponse response, String photo_path) {
		if( null != photo_path&&!"".equals(photo_path)){
			if (FileUtils.getState(ATTACHMENTPATH + photo_path)) {
				DownLoadUtils.download(response, ATTACHMENTPATH + photo_path);
			}
		}
		
	}

	/**
	 * 旋转图片
	 */
	@Override
	public Map<String, Object> turnImg(Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		File file = new File(ATTACHMENTPATH + params.get("path"));
		if (file.exists()) {
			BufferedImage image = null;
			try {
				image = ImageIO.read(new FileInputStream(file));
				BufferedImage newImage = null;
				if ("left".equals(params.get("direction"))) {
					newImage = ImgUtil.rotate90SX(image);
				} else {
					newImage = ImgUtil.rotate90DX(image);
				}
				boolean flag = ImageIO.write(newImage, "jpg", file);
				if (flag) {
					map.put("success", flag);
				} else
					throw new IOException();
			} catch (IOException e) {
				e.printStackTrace();
				map.put("success", false);
			}
		}
		return map;
	}

	
	
	/**
	 * 页面文件上传
	 */
	@Override
	@Transactional
	public Boolean upload(String id, MultipartFile files, String userid) throws Exception {
		RdsBaceraInvasivePhotoModel attachmentModel = new RdsBaceraInvasivePhotoModel();
		String attachmentPath = id
				+ File.separatorChar
				+ new Date().getTime()
				+ files.getOriginalFilename().substring(
						files.getOriginalFilename().lastIndexOf("."),
						files.getOriginalFilename().length());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("case_id", id);
		int a =rdsInvasivePreMapper.getPhoto(map);
		if(a<=0)
		{
		String photo_id = UUIDUtil.getUUID();
		attachmentModel.setCase_id(id);
		attachmentModel.setPhoto_id(photo_id);
		attachmentModel.setPhoto_path(attachmentPath);
		attachmentModel.setPhoto_type("1");
		attachmentModel.setUpload_time(new SimpleDateFormat("yyyy-MM-dd HH:MM:SS")
				.format(new Date()));
		attachmentModel.setUpload_user(userid);
		if (!RdsFileUtil.getState(attachmentPath
				+ files.getOriginalFilename())&&rdsInvasivePreMapper.insertHeadPhoto(attachmentModel)> 0) {
			// 上传
			RdsFileUtil.fileUpload(ATTACHMENTPATH + attachmentPath,
					files.getInputStream());
           return true;
             } else
               return  false;
	       }else{
					return false;
	       }

	       }
	
	@Override
	public boolean deleteFile(Map<String, Object> params) {
		try {
			String attachmentPath = ATTACHMENTPATH
					+ params.get("photo_path").toString();
			RdsFileUtil.delFolder(attachmentPath);
	        int a=	rdsInvasivePreMapper.deletePhoto(params);
			if(a>0){return true;}
			else {
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

	@Override
	public List<RdsBaceraInvasivePhotoModel> queryAllFile(Map<String, Object> params) {
		return rdsInvasivePreMapper.getAttachMent(params);
	}
}
