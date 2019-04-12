package com.rds.judicial.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rds.code.image.ImgUtil;
import com.rds.code.utils.DownLoadUtils;
import com.rds.code.utils.FileUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialCaseAttachmentMapper;
import com.rds.judicial.mapper.RdsJudicialPhoneMapper;
import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;
import com.rds.judicial.model.RdsJudicialCaseAttachmentPrintModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialCaseAttachmentService;
import com.rds.judicial.service.RdsJudicialRegisterService;

/**
 * 附件管理
 * 
 * @author 少明
 *
 */
@Service("RdsJudicialCaseAttachmentService")
public class RdsJudicialCaseAttachmentServiceImpl implements
		RdsJudicialCaseAttachmentService {

	@Setter
	@Autowired
	private RdsJudicialRegisterService RdsJudicialRegisterService;

	@Setter
	@Autowired
	private RdsJudicialRegisterMapper rdsJudicialRegisterMapper;

	@Autowired
	private RdsJudicialCaseAttachmentMapper judicialCaseAttachmentMapper;

	@Setter
	@Autowired
	private RdsJudicialPhoneMapper pMapper;

	// 配置文件地址
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	// 附件存放地址
	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "judicial_path");

	/**
	 * 获取附件数据库信息
	 */
	@Override
	public List<RdsJudicialCaseAttachmentModel> getAttachMent(
			Map<String, Object> params) {
		return judicialCaseAttachmentMapper.getAttachMent(params);
	}
	/**
	 * 获取附件数据库信息11
	 */
	@Override
	public List<RdsJudicialCaseAttachmentModel> getAttachMentOne(
			Map<String, Object> map) {
		return judicialCaseAttachmentMapper.getAttachMentOne(map);
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

	/**
	 * 手机端上传附件
	 */
	@Override
	@Transactional
	public Map<String, Object> uploadAttachment(String case_id,
			String case_code, MultipartFile[] file, String filetype)
			throws IOException {
		// 返回信息
		String msg = "";
		// 文件保存在应得案例条形码同名文件夹下
		String attachmentPath = ATTACHMENTPATH + case_code + File.separatorChar;
		if (case_id == null || "".equals(case_id))
			case_id = pMapper.getCaseID(case_code);
		// 组装附件pojo
		RdsJudicialCaseAttachmentModel attachmentModel = new RdsJudicialCaseAttachmentModel();
		attachmentModel.setCase_id(case_id);
		attachmentModel.setCase_code(case_code);
		attachmentModel.setAttachment_date(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));

		if (file.length > 0) {
			int i = 0;
			// 可多个文件上传
			for (MultipartFile mfile : file) {
				attachmentModel.setId(UUIDUtil.getUUID());
				attachmentModel.setAttachment_path(case_code
						+ File.separatorChar + mfile.getOriginalFilename());
				attachmentModel.setAttachment_type(Integer.parseInt(filetype
						.substring(i, i + 1)));
				i++;

				if (!RdsFileUtil.getState(attachmentPath
						+ mfile.getOriginalFilename())) {
					// 上传
					RdsFileUtil.fileUpload(
							attachmentPath + mfile.getOriginalFilename(),
							mfile.getInputStream());
					// 插入数据库
					if (judicialCaseAttachmentMapper
							.insertAttachment(attachmentModel) > 0)
						msg = msg + "文件：" + mfile.getOriginalFilename()
								+ "上传成功";
					else
						msg = msg + "文件：" + mfile.getOriginalFilename()
								+ "上传失败";
				} else {
					msg = msg + "文件：" + mfile.getOriginalFilename()
							+ "已存在,上传失败<br>";
				}
			}
			return setMsg(true, true, msg);
		} else {
			msg = msg + "未收到文件,上传失败";
			return setMsg(false, false, msg);
		}
	}

	private Map<String, Object> setMsg(boolean success, boolean result,
			String message) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	@Override
	@Transactional
	public Map<String, Object> queryAllPage(Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<RdsJudicialCaseAttachmentModel> attachmentList = judicialCaseAttachmentMapper
				.queryAllPage(params);
		int total = judicialCaseAttachmentMapper.queryCount(params);
		map.put("data", attachmentList);
		map.put("total", total);
		return map;
	}

	/**
	 * 页面文件上传
	 */
	@Override
	@Transactional
	public Map<String, Object> upload(String case_code, MultipartFile[] files,
			int[] filetype, String userid) throws Exception {
		String msg = "";
		String attachmentPath = ATTACHMENTPATH + case_code + File.separatorChar;
//		String case_id = pMapper.getCaseID(case_code);
		String case_id = case_code;
		if (case_id == null || "".equals(case_id)) {
			return setMsg(true, false, "案例不存在,附件上传失败");
		}
		RdsJudicialCaseAttachmentModel attachmentModel = new RdsJudicialCaseAttachmentModel();
		attachmentModel.setCase_id(case_id);
		attachmentModel.setCase_code(case_code);
		attachmentModel.setAttachment_date(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		attachmentModel.setCreate_per(userid);
		if (files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				if(!"".equals(files[i].getOriginalFilename()))
				{
					String id = UUIDUtil.getUUID();
					attachmentModel.setId(id);
					attachmentModel.setAttachment_path(case_code
							+ File.separatorChar + files[i].getOriginalFilename());
					attachmentModel.setAttachment_type(filetype[i]);
					if (!RdsFileUtil.getState(attachmentPath
							+ files[i].getOriginalFilename())) {
						RdsFileUtil.fileUpload(
								attachmentPath + files[i].getOriginalFilename(),
								files[i].getInputStream());
						// 判断是否存在该案例以上传过的照片类型
						// List<String> attachmentId = judicialCaseAttachmentMapper
						// .isFileExist(attachmentModel);
						// if (attachmentId.size() > 0) {
						// attachmentModel.setId(attachmentId.get(0));
						// // 插入数据库
						// if (judicialCaseAttachmentMapper
						// .updateFile(attachmentModel) > 0)
						// msg = msg + "文件：" + files[i].getOriginalFilename()
						// + "上传成功。<br>";
						// else
						// msg = msg + "文件：" + files[i].getOriginalFilename()
						// + "上传失败。<br>";
						// } else {
						if (judicialCaseAttachmentMapper
								.insertAttachment(attachmentModel) > 0) {
							 msg = msg + "文件：" + files[i].getOriginalFilename()
							 + "上传成功。<br>";
						} else
							msg = msg + "文件：" + files[i].getOriginalFilename()
									+ "上传失败。<br>";
						// }
					} else {
						msg = msg + "文件：" + files[i].getOriginalFilename()
								+ "已存在,上传失败。<br>";
					}
				}
			}
			return setMsg(true, true, msg);
		} else
			return setMsg(true, false, msg + "未收到文件,上传失败");
	}

	/**
	 * 文件下载
	 */
	@Override
	public void download(HttpServletResponse response, String id) {
		String filename = judicialCaseAttachmentMapper.queryPathById(id);
		File file = new File(ATTACHMENTPATH + filename);
		if (file.exists()) {
			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename="
					+ filename.substring(filename.lastIndexOf(File.separator)+1));
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

	/**
	 * 样本附件上传
	 */
	@Override
	@Transactional
	public void sampleAttachmentUpload(String case_code, List<File> files)
			throws Exception {
		String attachmentPath = ATTACHMENTPATH + case_code + File.separatorChar;
		String case_id = pMapper.getCaseID(case_code);
		if (case_id == null || "".equals(case_id)) {
			throw new Exception("Case is not exist");
		}
		RdsJudicialCaseAttachmentModel attachmentModel = new RdsJudicialCaseAttachmentModel();
		attachmentModel.setCase_id(case_id);
		attachmentModel.setCase_code(case_code);
		attachmentModel.setAttachment_date(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		if (files.size() > 0) {
			String fileName = "";
			for (File mfile : files) {

				if (RdsFileUtil.getState(attachmentPath + mfile.getName())) {
					fileName = new Date().getTime() + mfile.getName();
				} else
					fileName = mfile.getName();

				RdsFileUtil.fileUpload(attachmentPath + fileName,
						new FileInputStream(mfile));
				attachmentModel.setId(UUIDUtil.getUUID());
				attachmentModel.setAttachment_path(case_code
						+ File.separatorChar + fileName);
				// pdf 类型为6
				attachmentModel.setAttachment_type(6);
				if (judicialCaseAttachmentMapper
						.insertAttachment(attachmentModel) == 0)
					throw new Exception("Error!Upload fail");
			}
		} else
			throw new Exception("Error!File is null");
	}

	/**
	 * 打印附件
	 */
	@Override
	public RdsJudicialResponse getPrintAttachment(Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialCaseAttachmentPrintModel> printModels = rdsJudicialRegisterMapper
				.getPrintAttachment(params);
		int count = rdsJudicialRegisterMapper.countPrintAttachment(params);
		response.setCount(count);
		response.setItems(printModels);
		return response;
	}

	@Override
	public boolean printAttachment(Map<String, Object> params) {
		return rdsJudicialRegisterMapper.printAttachment(params);
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
	 * pdf 文件上传
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void pdfAttachmentUpload(String case_code, List<File> files)
			throws Exception {
		String attachmentPath = ATTACHMENTPATH + case_code + File.separatorChar;
		String case_id = pMapper.getCaseID(case_code);
		if (case_id == null || "".equals(case_id)) {
			throw new Exception("Case is not exist");
		}
		RdsJudicialCaseAttachmentModel attachmentModel = new RdsJudicialCaseAttachmentModel();
		attachmentModel.setCase_id(case_id);
		attachmentModel.setCase_code(case_code);
		attachmentModel.setAttachment_date(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		if (files.size() > 0) {
			String fileName = "";
			for (File mfile : files) {
				if (RdsFileUtil.getState(attachmentPath + mfile.getName())) {
					continue;
				} else
					fileName = mfile.getName();
				RdsFileUtil.fileUpload(attachmentPath + fileName,
						new FileInputStream(mfile));
				attachmentModel.setId(UUIDUtil.getUUID());
				attachmentModel.setAttachment_path(case_code
						+ File.separatorChar + fileName);
				// pdf 类型为6
				attachmentModel.setAttachment_type(6);
				if (judicialCaseAttachmentMapper
						.insertAttachment(attachmentModel) == 0)
					throw new Exception("Error!Upload fail");
			}
		} else
			throw new Exception("Error!File is null");
	}

	@Override
	public Map<String, Object> getCaseInfo(Map<String, Object> params) {
		return null;
	}

	@Override
	public boolean updateAllAttachment(Map<String, Object> params) {
		String case_code = params.get("case_code").toString();
		List<String> list = Arrays.asList(case_code.split("[|]"));
		if (judicialCaseAttachmentMapper.updateAllAttachment(list) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public void printAllCaseAttachment(String case_code, String count,
			HttpServletRequest request) {
		List<String> clist = Arrays.asList(case_code.split("[|]"));
		List<String> ilist = Arrays.asList(count.split("[|]"));
		List<RdsJudicialCaseAttachmentModel> acalist = judicialCaseAttachmentMapper
				.getAllCaseAttachment(clist);
		for (int i = 0; i < clist.size(); i++) {
			for (int j = 0; j < acalist.size(); j++) {
				if (acalist.get(j).getCase_code().equals(clist.get(i))) {
					acalist.get(j).setCount(ilist.get(i));
				}
			}
		}
		request.setAttribute("att", acalist);
		// request.setAttribute("count", count);
	}

	@Override
	public boolean deleteFile(Map<String, Object> params) {
		try {
			String attachmentPath = ATTACHMENTPATH
					+ params.get("attachment_path").toString();
			RdsFileUtil.delFolder(attachmentPath);
			judicialCaseAttachmentMapper.deleteFile(params);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int queryCount(Map<String, Object> params) {
		try {
			return judicialCaseAttachmentMapper.queryCount(params);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<RdsJudicialCaseAttachmentModel> queryAll(
			Map<String, Object> params) {
		return judicialCaseAttachmentMapper.queryAll(params);
	}

}
