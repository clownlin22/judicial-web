package com.rds.statistics.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.statistics.mapper.RdsExperimentalReportMapper;
import com.rds.statistics.model.RdsExperimentalReportModel;
import com.rds.statistics.service.RdsExperimentalReportService;

@Service("RdsExperimentalReportService")
public class RdsExperimentalReportServiceImpl implements RdsExperimentalReportService{
	@Autowired
	@Setter
	private RdsExperimentalReportMapper rdsExperimentalReportMapper;
	
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "laboratory_path");
	
	@Override
	public void download(HttpServletResponse response, Map<String,Object> map) throws Exception {
		String uuid= map.get("uuid").toString();
		RdsExperimentalReportModel rm = rdsExperimentalReportMapper.queryReport(uuid);
		String filename=rm.getReport();
		String id=rm.getId();
		RdsExperimentalReportModel rm1 = rdsExperimentalReportMapper.querySubjectById(id);
        String subject=rm1.getSubject().toString();
		File file = new File(ATTACHMENTPATH +subject+File.separatorChar+ filename);
		if (file.exists()) {
			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(filename,"utf-8"));
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
	@Transactional(rollbackFor = Exception.class)
	public Map<String, Object> upload(String username,String report,String id,MultipartFile[] files)
			throws Exception {
		String msg = "";
		String attachmentPath = ATTACHMENTPATH +report+File.separatorChar;
		if (files.length > 0) {
			for (MultipartFile mfile : files) {
				Map<String,Object> map = new HashMap<String, Object>();
              
				RdsFileUtil.fileUpload(
						attachmentPath + mfile.getOriginalFilename(),
						mfile.getInputStream());
				Map<String, Object> attachParams = new HashMap<>();
				attachParams.put("uuid", UUIDUtil.getUUID());
				attachParams
						.put("report", mfile.getOriginalFilename());
				attachParams.put("upload_username", username);
				attachParams.put("id", id);
				String rep=mfile.getOriginalFilename();
				map.put("report", rep);
				map.put("id", id);
				int object = rdsExperimentalReportMapper.queryReportByName(map);
				if(object>0){
					return setMsg(false, msg+"该附件名已存在，请重新上传");
				}
				rdsExperimentalReportMapper.insertReport(attachParams);
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
	public int insertReport(Map<String, Object> params) {
		return rdsExperimentalReportMapper.insertReport(params);
		 
	}

	@Override
	public List<RdsExperimentalReportModel> queryAllReport(
			Map<String, Object> params) {
		return rdsExperimentalReportMapper.queryAllReport(params);
	}
	@Override
	public List<RdsExperimentalReportModel> queryAllSubject(
			Map<String, Object> params) {
		return rdsExperimentalReportMapper.queryAllSubject(params);
	}
	@Override
	public int queryAllCount(Map<String, Object> params) {
		
		return rdsExperimentalReportMapper.queryAllCount(params);
	}

	@Override
	public int deleteReport(Map<String, Object> params) {
		RdsExperimentalReportModel rm= rdsExperimentalReportMapper.queryReport(params.get("uuid").toString());
		RdsExperimentalReportModel rm1=rdsExperimentalReportMapper.querySubjectById(rm.getId());
		String attachmentPath = ATTACHMENTPATH
				+ rm1.getSubject()+File.separatorChar+rm.getReport();
		RdsFileUtil.delFile(attachmentPath);
		return rdsExperimentalReportMapper.deleteReport(params);
	}

	@Override
	public int updateReport(Map<String, Object> params) {
		return rdsExperimentalReportMapper.updateReport(params);
	}

	@Override
	public int insert(Map<String, Object> params) {
		return rdsExperimentalReportMapper.insert(params);
	}

	@Override
	public RdsExperimentalReportModel queryReport(String uuid) {
		
		return rdsExperimentalReportMapper.queryReport(uuid);
	}

	@Override
	public int deleteSubject(Map<String, Object> params) {
		return rdsExperimentalReportMapper.deleteSubject(params);
	}

	@Override
	public int querySubject(String subject) {
		return rdsExperimentalReportMapper.querySubject(subject);
	}



	
}
