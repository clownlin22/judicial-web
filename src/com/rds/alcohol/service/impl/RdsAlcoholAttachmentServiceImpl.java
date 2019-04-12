package com.rds.alcohol.service.impl;

import com.rds.alcohol.mapper.RdsAlcoholAttachmentMapper;
import com.rds.alcohol.model.RdsAlcoholAttachmentModel;
import com.rds.alcohol.service.RdsAlcoholAttachmentService;
import com.rds.children.model.RdsChildrenCasePhotoModel;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("RdsAlcoholAttachmentService")
public class RdsAlcoholAttachmentServiceImpl
implements RdsAlcoholAttachmentService
{
	private static final String FILE_PATH = ConfigPath.getWebInfPath() + 
			"spring" + File.separatorChar + "properties" + File.separatorChar + 
			"config.properties";

	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "alcohol_file");

	@Autowired
	private RdsAlcoholAttachmentMapper aMapper;

	public Map<String, Object> getAttachment(Map<String, Object> params)
	{
		Map map = new HashMap();
		map.put("data", this.aMapper.getAttachment(params));
		map.put("count", Integer.valueOf(this.aMapper.getAttachmentCount(params)));
		return map;
	}

	public List<RdsChildrenCasePhotoModel> getAtt(Map<String, Object> params)
	{
		return this.aMapper.getAtt(params);
	}

	public Map<String, Object> photoUpload(RdsAlcoholAttachmentModel pmodel, MultipartFile[] photo)
	{
		String msg = "";
		Map map = new HashMap();
		try
		{
			if (this.aMapper.deleteCasePhoto(pmodel) <= 0) {
				msg = msg + "文件：" + photo[0].getOriginalFilename() + 
						"上传失败,请联系管理员！";
				map.put("success", Boolean.valueOf(false));
				map.put("message", msg);
				return map;
			}

			if (!RdsFileUtil.delFile(ATTACHMENTPATH + pmodel.getAtt_path())) {
				msg = msg + "文件：" + photo[0].getOriginalFilename() + 
						"上传失败,请联系管理员！";
				map.put("success", Boolean.valueOf(false));
				map.put("message", msg);
				return map;
			}

			String attachmentPath = pmodel.getCase_id() + 
					File.separatorChar + 
					new Date().getTime() + 
					photo[0].getOriginalFilename().substring(
							photo[0].getOriginalFilename().lastIndexOf("."), 
							photo[0].getOriginalFilename().length());

			String photo_id = UUIDUtil.getUUID();
			pmodel.setAtt_id(photo_id);
			pmodel.setAtt_path(attachmentPath);

			if (!RdsFileUtil.getState(attachmentPath + 
					photo[0].getOriginalFilename()))
			{
				RdsFileUtil.fileUpload(ATTACHMENTPATH + attachmentPath, 
						photo[0].getInputStream());

				if (this.aMapper.insertHeadPhoto(pmodel) > 0)
					msg = msg + "文件：" + photo[0].getOriginalFilename() + "上传成功";
				else
					msg = msg + "文件：" + photo[0].getOriginalFilename() + "上传失败";
			} else {
				msg = msg + "文件：" + photo[0].getOriginalFilename() + "已存在,上传失败";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		map.put("success", Boolean.valueOf(true));
		map.put("message", msg);
		return map;
	}

	@Transactional
	public Map<String, Object> upload(String case_code, MultipartFile[] files)
	{
		String msg = "";

		String case_id = this.aMapper.getCaseID(case_code);

		if ((case_id == null) || ("".equals(case_id))) {
			return setMsg(false, "案例不存在,附件上传失败");
		}

		if (files.length > 0) {
			for (MultipartFile file : files) {
				String filepath = case_code + File.separatorChar + 
						new Date().getTime() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
				Map params = new HashMap();
				params.put("att_id", UUIDUtil.getUUID());
				params.put("case_id", case_id);
				params.put("att_path", filepath);
				if (!RdsFileUtil.getState(ATTACHMENTPATH + filepath)) {
					try {
						RdsFileUtil.fileUpload(
								ATTACHMENTPATH + filepath, 
								file.getInputStream());
					} catch (IOException e) {
						e.printStackTrace();
					}

					if (this.aMapper.insertAttachment(params) > 0)
						msg = msg + "文件：" + file.getOriginalFilename() + 
						"上传成功。<br>";
					else
						msg = msg + "文件：" + file.getOriginalFilename() + 
						"上传失败。<br>";
				} else {
					msg = msg + "文件：" + file.getOriginalFilename() + 
							"已存在,上传失败。<br>";
				}
			}
			return setMsg(true, msg);
		}
		return setMsg(false, msg + "未收到文件,上传失败");
	}

	private Map<String, Object> setMsg(boolean result, String message)
	{
		Map map = new HashMap();
		map.put("success", Boolean.valueOf(result));
		map.put("message", message);
		return map;
	}

	public Map<String, Object> getPrintAttachment(String att_id)
	{
		return this.aMapper.getPrintAtt(att_id);
	}

	public Map<String, Object> photoUploadt(RdsAlcoholAttachmentModel pmodel, MultipartFile[] photo)
	{
		String msg = "";
		Map map = new HashMap();
		try
		{
			String attachmentPath = pmodel.getCase_id() + 
					File.separatorChar + 
					new Date().getTime() + 
					photo[0].getOriginalFilename().substring(
							photo[0].getOriginalFilename().lastIndexOf("."), 
							photo[0].getOriginalFilename().length());

			String photo_id = UUIDUtil.getUUID();
			pmodel.setAtt_id(photo_id);
			pmodel.setAtt_path(attachmentPath);

			if (!RdsFileUtil.getState(attachmentPath + 
					photo[0].getOriginalFilename()))
			{
				RdsFileUtil.fileUpload(ATTACHMENTPATH + attachmentPath, 
						photo[0].getInputStream());

				if (this.aMapper.insertHeadPhoto(pmodel) > 0)
					msg = msg + "文件：" + photo[0].getOriginalFilename() + "上传成功";
				else
					msg = msg + "文件：" + photo[0].getOriginalFilename() + "上传失败";
			} else {
				msg = msg + "文件：" + photo[0].getOriginalFilename() + "已存在,上传失败";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		map.put("success", Boolean.valueOf(true));
		map.put("message", msg);
		return map;
	}

	@Override
	public Map photoUploadtd(RdsAlcoholAttachmentModel pmodel) {
		int count = aMapper.updatephoto(pmodel);
		Map map = new HashMap();
		if(count>0){
			map.put("success", true);
			map.put("message", "上传成功");
		}else{
			map.put("success", false);
			map.put("message", "上传失败");
		}
		return map;
	}
	
	public boolean deletAttInfo(Map<String, Object> params)
	{
		int count = this.aMapper.deletAttInfo(params);
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Map<String, Object> photoUpload(RdsAlcoholAttachmentModel pmodel) {
		return null;
	}


}