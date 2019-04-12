package com.rds.judicial.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import com.rds.judicial.mapper.RdsJudicialCasePhotoMapper;
import com.rds.judicial.mapper.RdsJudicialPhoneMapper;
import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialCasePhotoService;

@Service("RdsJudicialCasePhotoService")
@Transactional
public class RdsJudicialCasePhotoServiceImpl implements
		RdsJudicialCasePhotoService {

	@Autowired
	private RdsJudicialPhoneMapper pMapper;

	@Autowired
	private RdsJudicialCaseAttachmentMapper attMapper;
	@Autowired
	private RdsJudicialCasePhotoMapper cpMapper;

	// 配置文件地址
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	// 附件存放地址
	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "judicial_path");

	@Override
	public RdsJudicialResponse getPrintCaseInfo(Map<String, Object> params) {
		RdsJudicialResponse response = new RdsJudicialResponse();
		List<RdsJudicialCaseInfoModel> caseInfoModels = cpMapper
				.getPrintCaseInfo(params);
		int count = cpMapper.countPrintCaseInfo(params);
		response.setItems(caseInfoModels);
		response.setCount(count);
		return response;
	}

	@Override
	public Map<String, Object> uploadTempPhoto(MultipartFile[] casephoto,
			String case_code, HttpSession session) {

		Map<String, Object> result = new HashMap<String, Object>();
		String attachmentPath = ATTACHMENTPATH + case_code + File.separatorChar;
		String case_id = pMapper.getCaseID(case_code);
		if (case_id == null || "".equals(case_id)) {
			result.put("success", false);
			result.put("msg", "案例不存在");
			return result;
		}
		long fnt = new Date().getTime();
		if (!RdsFileUtil.getState(attachmentPath
				+ fnt
				+ casephoto[0].getOriginalFilename().substring(
						casephoto[0].getOriginalFilename().lastIndexOf("."),
						casephoto[0].getOriginalFilename().length()))) {
			// 上传
			try {
				RdsFileUtil.fileUpload(
						attachmentPath + fnt
								+ casephoto[0].getOriginalFilename().substring(
										casephoto[0].getOriginalFilename().lastIndexOf("."),
										casephoto[0].getOriginalFilename().length()),
						casephoto[0].getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				result.put("success", false);
				result.put("msg", "上传失败！");
				return result;
			}
			session.setAttribute("case_code_temp", case_code);
			session.setAttribute(
					"case_photo_path",
					case_code + File.separatorChar + fnt
							+ casephoto[0].getOriginalFilename().substring(
									casephoto[0].getOriginalFilename().lastIndexOf("."),
									casephoto[0].getOriginalFilename().length()));
			result.put("success", true);
			result.put("msg", "OK");
			return result;
		}
		result.put("success", false);
		result.put("msg", "同名文件已存在！");
		return result;
	}

	public void getImg(HttpServletResponse response, String filename) {
		if (StringUtils.isNotEmpty(filename)) {
			if (FileUtils.getState(ATTACHMENTPATH + filename)) {
				DownLoadUtils.download(response, ATTACHMENTPATH + filename);
			}
		}
	}

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
					map.put("msg", "OK!");
					map.put("success", flag);
				} else {
					map.put("success", false);
					map.put("msg", "读写出错了！");
				}
				return map;
			} catch (IOException e) {
				e.printStackTrace();
				map.put("success", false);
				map.put("msg", "旋转失败");
			}
		} else {
			map.put("success", false);
			map.put("msg", "文件不存在");
		}
		return map;
	}

	@Override
	public Map<String, Object> getCaseReceiver(Map<String, Object> params,
			HttpSession session) {
		List<RdsJudicialCaseAttachmentModel> attlist = cpMapper
				.getCaseReceiver(params);
		params.clear();
		if (attlist.size() < 1) {
			params.put("success", false);
			params.put("msg", "案例还没有登记,请先登记");
		} else {
			session.setAttribute("case_code_temp", attlist.get(0)
					.getCase_code());
			session.setAttribute("case_photo_path", attlist.get(0)
					.getAttachment_path());
			params.put("success", true);
			params.put("data", attlist.get(0));
		}
		return params;
	}

	@Override
	public Map<String, Object> savephoto(Map<String, Object> params,
			HttpSession session) {
		if ("".equals(session.getAttribute("case_photo_path"))
				|| session.getAttribute("case_photo_path") == null) {
			params.clear();
			params.put("success", false);
			params.put("msg", "没有照片，请重新上传！");
			return params;
		}
		RdsJudicialCaseAttachmentModel attmodel = new RdsJudicialCaseAttachmentModel();
		attmodel.setCase_id(pMapper.getCaseID((String) params.get("case_code")));
		attmodel.setCase_code((String) params.get("case_code"));
		attmodel.setId(UUIDUtil.getUUID());
		attmodel.setAttachment_date(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
				.format(new Date()));
		attmodel.setAttachment_path(session.getAttribute("case_photo_path")
				.toString());
		attmodel.setAttachment_type(3);
		if (attMapper.insertAttachment(attmodel) > 0) {
			session.removeAttribute("case_photo_path");
			params.clear();
			params.put("success", true);
			params.put("msg", "保存成功");
		} else {
			session.removeAttribute("case_photo_path");
			params.clear();
			params.put("success", false);
			params.put("msg", "保存失败");
		}
		return params;

	}

	@Override
	public Map<String, Object> getAllCasePhoto(Map<String, Object> params) {
		List<RdsJudicialCaseAttachmentModel> attlist = cpMapper
				.getAllCasePhoto(params);
		int attcount = cpMapper.getAllCasePhotoCount(params);
		params.clear();
		params.put("data", attlist);
		params.put("count", attcount);
		return params;
	}

}
