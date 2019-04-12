package com.rds.judicial.service.impl;

/**
 * yuanxiaobo 2015-05-18
 */
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialAgentMapper;
import com.rds.judicial.mapper.RdsJudicialDicValuesMapper;
import com.rds.judicial.model.RdsJudicialAgentModel;
import com.rds.judicial.model.RdsJudicialAreaInfo;
import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.judicial.service.RdsJudicialAgentService;

@Service("RdsJudicialAgentService")
public class RdsJudicialAgentServiceImpl implements RdsJudicialAgentService{

	// 配置文件地址
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	// 附件存放地址
	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "agentAttachment_path");
	
	@Setter
	@Autowired
	private RdsJudicialAgentMapper rdsJudicialAgentMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsJudicialAgentMapper.queryAllCount(params));
		result.put("data", rdsJudicialAgentMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Object params){
		try{
			return rdsJudicialAgentMapper.update(params);
		}catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int insert(Object params){
		try
		{
			return rdsJudicialAgentMapper.insert(params);
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsJudicialAgentMapper.delete(params);
	}

	@Override
	public List<RdsJudicialKeyValueModel> queryUserByType(
			Map<String, Object> params) {
		// TODO Auto-generated method stub
		return rdsJudicialAgentMapper.queryUserByType(params);
	}
	
	@Override
	public Object queryAgent(Map<String, Object> params){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsJudicialAgentMapper.queryAgentCount(params));
		result.put("data", rdsJudicialAgentMapper.queryAgent(params));
		return result;
	}
	
	@Override
	public int queryAgentCount(Map<String, Object> params)
	{
		return rdsJudicialAgentMapper.queryAgentCount(params);
	}

	@Override
	public Object queryAttachment(Map<String, Object> params) {
		return rdsJudicialAgentMapper.queryAttachment(params);
	}

	@Override
	public boolean deleteAttachment(Map<String, Object> params) {
		return rdsJudicialAgentMapper.deleteAttachment(params)>0?true:false;
	}


	@Override
	public Map<String, Object> uploadAttachment(String case_id, MultipartFile[] files,
			int[] filetype, String userid) throws Exception {
		String msg = "";
		String attachmentPath = ATTACHMENTPATH + case_id + File.separatorChar;
//		String case_id = pMapper.getCaseID(case_code);
		if (case_id == null || "".equals(case_id)) {
			return setMsg(true, false, "案例不存在,附件上传失败");
		}
		RdsJudicialCaseAttachmentModel attachmentModel = new RdsJudicialCaseAttachmentModel();
		attachmentModel.setCase_id(case_id);
		attachmentModel.setAttachment_date(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		attachmentModel.setCreate_per(userid);
		if (files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				if(!"".equals(files[i].getOriginalFilename()))
				{
					String id = UUIDUtil.getUUID();
					attachmentModel.setId(id);
					attachmentModel.setAttachment_path(case_id
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
						if (rdsJudicialAgentMapper
								.uploadAttachment(attachmentModel) > 0) {
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
	
	private Map<String, Object> setMsg(boolean success, boolean result,
			String message) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

}
