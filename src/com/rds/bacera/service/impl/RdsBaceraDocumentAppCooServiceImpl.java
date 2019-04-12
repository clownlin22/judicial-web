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

import com.rds.bacera.mapper.RdsBaceraDocumentAppCooMapper;
import com.rds.bacera.model.RdsBaceraCaseAttachmentModel;
import com.rds.bacera.model.RdsBaceraDocumentAppCooModel;
import com.rds.bacera.service.RdsBaceraBoneAgeService;
import com.rds.bacera.service.RdsBaceraDocumentAppCooService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.upc.model.RdsUpcUserModel;

@Service("rdsBaceraDocumentAppCooService")
public class RdsBaceraDocumentAppCooServiceImpl implements
		RdsBaceraDocumentAppCooService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	// 附件存放地址
	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "document_app_coo_path");

	@Setter
	@Autowired
	private RdsBaceraDocumentAppCooMapper rdsBaceraDocumentAppCooMapper;

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
		result.put("total", rdsBaceraDocumentAppCooMapper.queryAllCount(params));
		result.put("data", rdsBaceraDocumentAppCooMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return rdsBaceraDocumentAppCooMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		try {
			return rdsBaceraDocumentAppCooMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsBaceraDocumentAppCooMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsBaceraDocumentAppCooMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@Override
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map) {
		return rdsBaceraDocumentAppCooMapper.queryNumExit(map);
	}

	@Override
	public void exportDocumentAppCoo(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "文书鉴定";
		List<Object> list = rdsBaceraDocumentAppCooMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			Object[] titles = { "案例编号", "是否结案", "委托人", "委托人联系方式", "委托时间", "受理时间",
					"鉴定项目","基本案情","鉴定完成日期","鉴定书，材料归还方式","开票金额",
					"应收款项", "所付款项", "到款时间", "优惠价格", "财务备注", "快递日期", "快递类型",
					"快递单号", "快递备注", "归属地", "归属人", "代理商", "备注和要求","是否作废"};
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraDocumentAppCooModel mode = (RdsBaceraDocumentAppCooModel) list
						.get(i);
				objects.add(mode.getNum());
				objects.add(mode.getCase_close());
				objects.add(mode.getClient());
				objects.add(mode.getPhone());
				objects.add(StringUtils.dateToChineseTen(mode.getClient_date()));
				objects.add(StringUtils.dateToChineseTen(mode.getAccept_date()));
				objects.add(mode.getAppraisal_pro());
				objects.add(mode.getBasic_case());
				objects.add(StringUtils.dateToChineseTen(mode.getAppraisal_end_date()));
				objects.add(mode.getReturn_type());
				objects.add(mode.getInvoice_exp());
				// 应收款项
				objects.add(("".equals(mode.getReceivables()) || null == mode
						.getReceivables()) ? 0 : Float.parseFloat(mode
						.getReceivables()));
				// 所付款项
				objects.add(("".equals(mode.getPayment()) || null == mode
						.getPayment()) ? 0 : Float.parseFloat(mode
						.getPayment()));
				objects.add(StringUtils.dateToChineseTen(mode
						.getParagraphtime()));
				objects.add(mode.getDiscountPrice());
				objects.add(mode.getFinance_remark());

				objects.add(StringUtils.dateToChineseTen(mode
						.getExpresstime()));
				objects.add(mode.getExpresstype());
				objects.add(mode.getExpressnum());
				objects.add(mode.getExpressremark());

				objects.add(mode.getAreaname());
				objects.add(mode.getOwnpersonname());
				objects.add(mode.getAgentname());
				objects.add(mode.getRemark());
				objects.add("1".equals(mode.getCancelif())?"是":"否");
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		} else {
			Object[] titles = { "案例编号", "是否结案", "委托人", "委托人联系方式", "委托时间", "受理时间",
					"鉴定项目","基本案情","鉴定完成日期","鉴定书，材料归还方式","开票金额","快递日期", "快递类型",
					"快递单号", "快递备注", "归属地", "归属人", "代理商", "备注和要求","是否作废" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraDocumentAppCooModel mode = (RdsBaceraDocumentAppCooModel) list
						.get(i);
				objects.add(mode.getNum());
				objects.add(mode.getCase_close());
				objects.add(mode.getClient());
				objects.add(mode.getPhone());
				objects.add(StringUtils.dateToChineseTen(mode.getClient_date()));
				objects.add(StringUtils.dateToChineseTen(mode.getAccept_date()));
				objects.add(mode.getAppraisal_pro());
				objects.add(mode.getBasic_case());
				objects.add(StringUtils.dateToChineseTen(mode.getAppraisal_end_date()));
				objects.add(mode.getReturn_type());
				objects.add(mode.getInvoice_exp());

				objects.add(StringUtils.dateToChineseTen(mode
						.getExpresstime()));
				objects.add(mode.getExpresstype());
				objects.add(mode.getExpressnum());
				objects.add(mode.getExpressremark());

				objects.add(mode.getAreaname());
				objects.add(mode.getOwnpersonname());
				objects.add(mode.getAgentname());
				objects.add(mode.getRemark());
				objects.add("1".equals(mode.getCancelif())?"是":"否");
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		}

	}

	@Override
	public Map<String, Object> saveCaseInfo(Map<String, Object> params,
			RdsUpcUserModel user) {
		String id = params.get("id").toString();
		String num = params.get("num").toString();
		// 案例返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("num", num);
		map.put("inputperson", user.getUserid());
		map.put("receivables", params.get("receivables"));
		map.put("case_type", "文书鉴定合作");
		try {
			// 更新财务应收
			if (rdsBoneAgeService.insertFinance(map) > 0) {

				int case_result = 0;
				// 插入基本信息
				case_result = rdsBaceraDocumentAppCooMapper.insert(params);
				// 案例实体插入成功，插入案例样本信息
				if (case_result > 0) {
					// 上传照片
					try {
						// 上传图片信息
						if (null != params.get("files")) {
							uploadAttacment(id, num,
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
					result.put("result", true);
					result.put("success", true);
					result.put("message", "操作成功！");
					return result;
				} else {
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
		String id = params.get("id").toString();
		String num = params.get("num").toString();
		// 案例返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("num", num);
		map.put("inputperson", user.getUserid());
		map.put("receivables", params.get("receivables"));
		try {
			// 更新财务应收
			if (rdsBoneAgeService.updateFinance(map) > 0) {
				// 更新基本信息
				rdsBaceraDocumentAppCooMapper.update(params);
				result.put("result", true);
				result.put("success", true);
				result.put("message", "操作成功！");
				return result;

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
	private boolean uploadAttacment(String id, String num ,
			MultipartFile[] files, int[] filetype, String userid)
			throws Exception {
		String attachmentPath = ATTACHMENTPATH + num
				+ File.separatorChar;
		RdsBaceraCaseAttachmentModel attachmentModel = new RdsBaceraCaseAttachmentModel();
		attachmentModel.setAppraisal_cpp_id(id);;
		attachmentModel.setCreate_date(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		attachmentModel.setCreate_per(userid);
		if (files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				attachmentModel.setAttachment_id(UUIDUtil.getUUID());
				attachmentModel.setAttachment_path(num
						+ File.separatorChar + files[i].getOriginalFilename());
				attachmentModel.setAttachment_type(filetype[i]);
				if (!RdsFileUtil.getState(attachmentPath
						+ files[i].getOriginalFilename())) {
					RdsFileUtil.fileUpload(
							attachmentPath + files[i].getOriginalFilename(),
							files[i].getInputStream());
					rdsBaceraDocumentAppCooMapper
							.insertDocumentAppCooAttachment(attachmentModel);
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
		String filename = rdsBaceraDocumentAppCooMapper
				.queryDocumentAppCooPathById(attachment_id);
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
	public List<RdsBaceraCaseAttachmentModel> queryAttacmByDocumentAppCoo(
			Object params) {
		return rdsBaceraDocumentAppCooMapper
				.queryAttacmByDocumentAppCoo(params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int delAttachment(Object param) {
		Map<String, String> map = (Map<String, String>) param;
		String attachment_id = map.get("attachment_id");
		String filename = rdsBaceraDocumentAppCooMapper
				.queryDocumentAppCooPathById(attachment_id);
		RdsFileUtil.delFolder(ATTACHMENTPATH + File.separatorChar + filename);
		return rdsBaceraDocumentAppCooMapper.delAttachment(param);
	}

	@Override
	public void uploadDocumentAppCooAttachment(String id, String num,
			MultipartFile[] files, int[] filetype, String userid)
			throws Exception {

		String attachmentPath = ATTACHMENTPATH + num + File.separatorChar;
		RdsBaceraCaseAttachmentModel attachmentModel = new RdsBaceraCaseAttachmentModel();
		attachmentModel.setAppraisal_cpp_id(id);
		attachmentModel.setCreate_date(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		attachmentModel.setCreate_per(userid);
		if (files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				attachmentModel.setAttachment_id(UUIDUtil.getUUID());
				attachmentModel.setAttachment_path(num + File.separatorChar
						+ files[i].getOriginalFilename());
				attachmentModel.setAttachment_type(filetype[i]);
				if (!RdsFileUtil.getState(attachmentPath
						+ files[i].getOriginalFilename())) {
					RdsFileUtil.fileUpload(
							attachmentPath + files[i].getOriginalFilename(),
							files[i].getInputStream());
					rdsBaceraDocumentAppCooMapper
							.insertDocumentAppCooAttachment(attachmentModel);
				}
			}
		}

	}
}
