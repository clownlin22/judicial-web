package com.rds.judicial.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialAttachmentMapper;
import com.rds.judicial.model.RdsJudicialAttachmentModel;
import com.rds.judicial.service.RdsJudicialAttachmentService;

@Service("RdsJudicialAttachmentService")
public class RdsJudicialAttachmentServiceImpl implements
		RdsJudicialAttachmentService {
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "experiment_path");

	@Autowired
	@Setter
	private RdsJudicialAttachmentMapper rdsJudicialAttachmentMapper;

	@Override
	public void insertAttachement(Map<String, Object> params) {
		params.put("uuid", UUIDUtil.getUUID());
		rdsJudicialAttachmentMapper.insertAttachment(params);
	}

	@Override
	public List<RdsJudicialAttachmentModel> queryAttachment(
			Map<String, Object> params) {
		return rdsJudicialAttachmentMapper.queryAttachment(params);
	}

	@Override
	public int queryCountAttachment(Map<String, Object> params) {
		return rdsJudicialAttachmentMapper.queryCountAttachment(params);
	}

	@Override
	public String getDataPath() {
		return PropertiesUtils.readValue(ConfigPath.getWebInfPath()
				+ File.separatorChar + "spring" + File.separatorChar
				+ "properties" + File.separatorChar + "config.properties",
				"expriment_path");
	}

	@Override
	public void download(HttpServletResponse response, Map<String,Object> map) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("uuid", map.get("uuid"));
		String filename = rdsJudicialAttachmentMapper.queryAttachment(params)
				.get(0).getAttachment_path();
		File file = new File(ATTACHMENTPATH + filename);
		if (file.exists()) {
			
			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(filename,"UTF-8"));
			response.setContentType("application/octet-stream; charset=utf-8");
			FileInputStream in = null;
			OutputStream toClient = null;
			try {
				//记录下载样本时间和人员
				File directory = new File(ATTACHMENTPATH + filename.split(".rar")[0]);
	            File[] lists = directory.listFiles();
	            //保存文件中样本名
	            for(File list:lists){
	            	if(!list.getName().endsWith("pdf"))
	                    continue;
	                Map<String,Object> temp = new HashMap<>();
	                temp.put("downloadPer",map.get("down_userid"));
	                temp.put("sample_code",list.getName().split(".pdf")[0]);
	                rdsJudicialAttachmentMapper.updateExperimentLog(temp);
	            }
	            //删除解压的文件夹
	            RdsFileUtil.delFolder(ATTACHMENTPATH + filename.split(".rar")[0]);
	            
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
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> upload(String userid, MultipartFile[] files)
			throws Exception {
		String msg = "";
		String attachmentPath = ATTACHMENTPATH + File.separatorChar;

		if (files.length > 0) {
			for (MultipartFile mfile : files) {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("attachment_name", mfile.getOriginalFilename());
				if(rdsJudicialAttachmentMapper.queryCountAttachment(map) > 0)
				{
					return setMsg(false, msg + "上传失败！存在相同文件名，请查看修改后重新上传！");
				}
				RdsFileUtil.fileUpload(
						attachmentPath + mfile.getOriginalFilename(),
						mfile.getInputStream());
				Map<String, Object> attachParams = new HashMap<>();
				attachParams.put("uuid", UUIDUtil.getUUID());
				attachParams
						.put("attachment_path", mfile.getOriginalFilename());
				attachParams.put("upload_userid", userid);
				rdsJudicialAttachmentMapper.insertAttachment(attachParams);
				//解压当前文件
				RdsFileUtil.unrar(attachmentPath + mfile.getOriginalFilename(),attachmentPath);
				File directory = new File(attachmentPath + mfile.getOriginalFilename().split(".rar")[0]);
                File[] lists = directory.listFiles();
                //保存文件中样本名
                for(File file:lists){
                	if(!file.getName().endsWith("pdf"))
                        continue;
                    Map<String,Object> temp = new HashMap<>();
                    temp.put("uuid",UUIDUtil.getUUID());
                    temp.put("uploadPer",userid);
                    temp.put("sample_code",file.getName().split(".pdf")[0]);
                    rdsJudicialAttachmentMapper.insertExperimentLog(temp);
                }
                //删除解压文件
                //RdsFileUtil.delFolder(attachmentPath + mfile.getOriginalFilename().split(".rar")[0]);
				msg = msg + "文件：" + mfile.getOriginalFilename() + "上传成功。</br>";
			}
			return setMsg(true, msg);
		} else
			return setMsg(false, msg + "未收到文件,上传失败");
	}

	private Map<String, Object> setMsg(boolean result, String message) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", result);
		map.put("message", message);
		return map;
	}

	@Override
	public int updateAttachment(Map<String, Object> params) {
		return rdsJudicialAttachmentMapper.updateAttachment(params);
	}

	@Override
	public int deleteAttachment(Map<String, Object> params) {
		String attachmentPath = ATTACHMENTPATH
				+ params.get("attachment_path").toString();
		RdsFileUtil.delFolder(attachmentPath);
		return rdsJudicialAttachmentMapper.deleteAttachement(params.get("uuid")
				.toString());
	}

	@Override
	public List<RdsJudicialAttachmentModel> queryExperimentLog(
			Map<String, Object> params) {
		return rdsJudicialAttachmentMapper.queryExperimentLog(params);
	}

	@Override
	public int countExperimentLog(Map<String, Object> params) {
		return rdsJudicialAttachmentMapper.countExperimentLog(params);
	}

	@Override
	public void exportExperimentLog(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "实验室数据时间日志";
		List<RdsJudicialAttachmentModel> list = rdsJudicialAttachmentMapper.queryExperimentLog(params);
		Object[] titles = { "案例样本编号", "最后上传日期", "上传人员", "最后下载日期", "下载人员" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsJudicialAttachmentModel logInfo = list.get(i);
				objects.add(logInfo.getSample_code());
				objects.add(logInfo.getAttachment_date());
				objects.add(logInfo.getUpload_username());
				objects.add(logInfo.getDownload_time());
				objects.add(logInfo.getDownload_username());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "实验室数据时间日志");
	}
}
