package com.rds.bacera.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rds.bacera.mapper.RdsBaceraAbilityMapper;
import com.rds.bacera.model.RdsBaceraCTDnaModel;
import com.rds.bacera.model.RdsBaceraCaseAttachmentModel;
import com.rds.bacera.service.RdsBaceraAbilityService;
import com.rds.bacera.service.RdsBaceraBoneAgeService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.upc.model.RdsUpcUserModel;

@Service("rdsBaceraAbilityService")
public class RdsBaceraAbilityServiceImpl implements RdsBaceraAbilityService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	// 附件存放地址
	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "ability_path");

	@Setter
	@Autowired
	private RdsBaceraAbilityMapper rdsBaceraAbilityMapper;

	@Setter
	@Autowired
	private RdsBaceraBoneAgeService rdsBoneAgeService;

	@Override
	public Object queryAll(Object params) throws Exception {
		return null;
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsBaceraAbilityMapper.queryAllCount(params));
		result.put("data", rdsBaceraAbilityMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return rdsBaceraAbilityMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		try {
			return rdsBaceraAbilityMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsBaceraAbilityMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsBaceraAbilityMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@Override
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map) {
		return rdsBaceraAbilityMapper.queryNumExit(map);
	}

	@Override
	public void exportAbility(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "能力验证";
		List<Object> list = rdsBaceraAbilityMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			Object[] titles = { "案例编号", "日期", "姓名", "性别", "临床诊断", "具体用药史和疗效",
					"应收款项", "所付款项", "到款时间", "优惠价格", "财务备注", "快递日期", "快递类型",
					"快递单号", "快递备注", "归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraCTDnaModel rdsCTDnaModel = (RdsBaceraCTDnaModel) list
						.get(i);
				objects.add(rdsCTDnaModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsCTDnaModel
						.getDate()));
				objects.add(rdsCTDnaModel.getName());
				objects.add(rdsCTDnaModel.getSex());
				objects.add(rdsCTDnaModel.getClinical_diagnosis());
				objects.add(rdsCTDnaModel.getHistort_effect());
				// 应收款项
				objects.add(("".equals(rdsCTDnaModel.getReceivables()) || null == rdsCTDnaModel
						.getReceivables()) ? 0 : Float.parseFloat(rdsCTDnaModel
						.getReceivables()));
				// 所付款项
				objects.add(("".equals(rdsCTDnaModel.getPayment()) || null == rdsCTDnaModel
						.getPayment()) ? 0 : Float.parseFloat(rdsCTDnaModel
						.getPayment()));
				objects.add(StringUtils.dateToChineseTen(rdsCTDnaModel
						.getParagraphtime()));
				objects.add(rdsCTDnaModel.getDiscountPrice());
				objects.add(rdsCTDnaModel.getFinance_remark());

				objects.add(StringUtils.dateToChineseTen(rdsCTDnaModel
						.getExpresstime()));
				objects.add(rdsCTDnaModel.getExpresstype());
				objects.add(rdsCTDnaModel.getExpressnum());
				objects.add(rdsCTDnaModel.getExpressremark());

				objects.add(rdsCTDnaModel.getAreaname());
				objects.add(rdsCTDnaModel.getOwnpersonname());
				objects.add(rdsCTDnaModel.getAgentname());
				objects.add(rdsCTDnaModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		} else {
			Object[] titles = { "案例编号", "日期", "姓名", "性别", "临床诊断", "具体用药史和疗效",
					"快递日期", "快递类型", "快递单号", "快递备注", "归属地", "归属人", "代理商",
					"备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraCTDnaModel rdsCTDnaModel = (RdsBaceraCTDnaModel) list
						.get(i);
				objects.add(rdsCTDnaModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsCTDnaModel
						.getDate()));
				objects.add(rdsCTDnaModel.getName());
				objects.add(rdsCTDnaModel.getSex());
				objects.add(rdsCTDnaModel.getClinical_diagnosis());
				objects.add(rdsCTDnaModel.getHistort_effect());

				objects.add(StringUtils.dateToChineseTen(rdsCTDnaModel
						.getExpresstime()));
				objects.add(rdsCTDnaModel.getExpresstype());
				objects.add(rdsCTDnaModel.getExpressnum());
				objects.add(rdsCTDnaModel.getExpressremark());

				objects.add(rdsCTDnaModel.getAreaname());
				objects.add(rdsCTDnaModel.getOwnpersonname());
				objects.add(rdsCTDnaModel.getAgentname());
				objects.add(rdsCTDnaModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		}

	}

	@Override
	public Map<String, Object> saveCaseInfo(Map<String, Object> params, RdsUpcUserModel user) {
		String ability_id = params.get("ability_id").toString();
		String ability_num = params.get("ability_num").toString();
		// 案例返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", ability_id);
		map.put("num", ability_num);
		map.put("inputperson", user.getUserid());
		map.put("receivables", params.get("receivables"));
		try {
			// 更新财务应收
			if (rdsBoneAgeService.insertFinance(map) > 0) {

				int case_result = 0;
				params.put("department_concatid", UUIDUtil.getUUID());
				//插入科室信息
				if(rdsBaceraAbilityMapper.insertAbilityDepartment(params)>0)
				{
					//插入基本信息
					case_result = rdsBaceraAbilityMapper.insert(params);
				}
				// 案例实体插入成功，插入案例样本信息
				if (case_result > 0) {
					// 上传照片
					try {
						// 上传图片信息
						if (null != params.get("files")) {
							uploadAttacment(ability_id, ability_num,
									(MultipartFile[]) params.get("files"),
									(int[]) params.get("filetype"),
									user.getUserid());
						}
					} catch (Exception e) {
						e.printStackTrace();
						result.put("result", false);
						result.put("success", true);
						result.put("message", "操作失败，请联系管理员！");
						return result;
					}
					result.put("case_code", params.get("case_code"));
					result.put("result", true);
					result.put("success", true);
					result.put("message", "操作成功！");
					return result;
				}else
				{
					result.put("result", false);
					result.put("success", true);
					result.put("message", "操作失败，请联系管理员！");
					return result;
				}
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("message", "操作失败，请联系管理员！");
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", true);
			result.put("result", false);
			result.put("message", "操作失败，请联系管理员！");
			return result;
		}

	}


	@Override
	public Map<String, Object> updateCaseInfo(Map<String, Object> params,
			RdsUpcUserModel user) {
		String ability_id = params.get("ability_id").toString();
		String ability_num = params.get("ability_num").toString();
		// 案例返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", ability_id);
		map.put("num", ability_num);
		map.put("inputperson", user.getUserid());
		map.put("receivables", params.get("receivables"));
		try {
			// 更新财务应收
			if (rdsBoneAgeService.updateFinance(map) > 0) {
				//更新科室信息
				if(rdsBaceraAbilityMapper.updateAbilityDepartment(params)>0)
				{
					//更新基本信息
					rdsBaceraAbilityMapper.update(params);
					result.put("result", true);
					result.put("success", true);
					result.put("message", "操作成功！");
					return result;
				}else
				{
					result.put("result", false);
					result.put("success", true);
					result.put("message", "操作失败，请联系管理员！");
					return result;
				}
			} else {
				result.put("success", true);
				result.put("result", false);
				result.put("message", "操作失败，请联系管理员！");
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", true);
			result.put("result", false);
			result.put("message", "操作失败，请联系管理员！");
			return result;
		}
	}
	
	/**
	 * 页面文件上传
	 * 
	 * @return
	 */
	private boolean uploadAttacment(String ability_id, String ability_num,
			MultipartFile[] files, int[] filetype, String userid)
			throws Exception {
		String attachmentPath = ATTACHMENTPATH + ability_num
				+ File.separatorChar;
		RdsBaceraCaseAttachmentModel attachmentModel = new RdsBaceraCaseAttachmentModel();
		attachmentModel.setAbility_id(ability_id);
		attachmentModel.setCreate_date(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		attachmentModel.setCreate_per(userid);
		if (files.length > 0) {
			for (int i = 0; i < files.length;i++) {
				String id = UUIDUtil.getUUID();
				attachmentModel.setAttachment_id(id);
				attachmentModel.setAttachment_path(ability_num
						+ File.separatorChar + files[i].getOriginalFilename());
				attachmentModel.setAttachment_type(filetype[i]);
				if (!RdsFileUtil.getState(attachmentPath
						+ files[i].getOriginalFilename())) {
					RdsFileUtil.fileUpload(
							attachmentPath + files[i].getOriginalFilename(),
							files[i].getInputStream());
					rdsBaceraAbilityMapper.insertAbilityAttachment(attachmentModel);
				} 
			}
			return true;
		} else
			return false;
	}

	/**
	 * 文件下载
	 */
	@Override
	public void download(HttpServletResponse response, String attachment_id) {
		String filename = rdsBaceraAbilityMapper.queryAbilityPathById(attachment_id);
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

	@Override
	public List<RdsBaceraCaseAttachmentModel> queryAttacmByAbility(Object params) {
		return rdsBaceraAbilityMapper.queryAttacmByAbility(params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int delAttachment(Object param) {
		Map<String,String> map = (Map<String,String>)param;
		String attachment_id = map.get("attachment_id");
		String filename = rdsBaceraAbilityMapper.queryAbilityPathById(attachment_id);
		RdsFileUtil.delFolder(ATTACHMENTPATH + File.separatorChar + filename);
		return rdsBaceraAbilityMapper.delAttachment(param);
	}

	@Override
	public void uploadAbilityAttachment(String ability_id, String ability_num,
			MultipartFile[] files, int[] filetype, String userid) throws Exception {

		String attachmentPath = ATTACHMENTPATH + ability_num
				+ File.separatorChar;
		RdsBaceraCaseAttachmentModel attachmentModel = new RdsBaceraCaseAttachmentModel();
		attachmentModel.setAbility_id(ability_id);
		attachmentModel.setCreate_date(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		attachmentModel.setCreate_per(userid);
		if (files.length > 0) {
			for (int i = 0; i < files.length;i++) {
				String id = UUIDUtil.getUUID();
				attachmentModel.setAttachment_id(id);
				attachmentModel.setAttachment_path(ability_num
						+ File.separatorChar + files[i].getOriginalFilename());
				attachmentModel.setAttachment_type(filetype[i]);
				if (!RdsFileUtil.getState(attachmentPath
						+ files[i].getOriginalFilename())) {
					RdsFileUtil.fileUpload(
							attachmentPath + files[i].getOriginalFilename(),
							files[i].getInputStream());
					rdsBaceraAbilityMapper.insertAbilityAttachment(attachmentModel);
				} 
			}
		} 
	
	}
}
