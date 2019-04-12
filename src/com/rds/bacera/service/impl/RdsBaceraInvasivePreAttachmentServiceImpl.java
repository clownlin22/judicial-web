package com.rds.bacera.service.impl;

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

import com.rds.bacera.mapper.RdsBaceraInvasivePreAttachmentMapper;
import com.rds.bacera.mapper.RdsBaceraInvasivePreMapper;
import com.rds.bacera.model.RdsBaceraInvasivePreAttachmentModel;
import com.rds.bacera.service.RdsBaceraInvasivePreAttachmentService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.model.RdsJudicialAttachmentModel;
@Service("RdsBaceraInvasivePreAttachmentService")
public class RdsBaceraInvasivePreAttachmentServiceImpl implements RdsBaceraInvasivePreAttachmentService{

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String ATTACHMENTPATH = PropertiesUtils.readValue(
			FILE_PATH, "invasivePre_path");

	@Autowired
	@Setter
	private RdsBaceraInvasivePreAttachmentMapper rdsBaceraInvasivePreAttachmentMapper;
	@Autowired
	private  RdsBaceraInvasivePreMapper  rdsBaceraInvasivePreMapper;
	@Override
	public void insertAttachement(Map<String, Object> params) {
		params.put("uuid", UUIDUtil.getUUID());
		rdsBaceraInvasivePreAttachmentMapper.insertAttachment(params);
	}

	@Override
	public List<RdsBaceraInvasivePreAttachmentModel> queryAttachment(
			Map<String, Object> params) {
		return rdsBaceraInvasivePreAttachmentMapper.queryAttachment(params);
	}

	@Override
	public int queryCountAttachment(Map<String, Object> params) {
		return rdsBaceraInvasivePreAttachmentMapper.queryCountAttachment((String)params.get("uuid"));
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
		String filename = rdsBaceraInvasivePreAttachmentMapper.queryAttachment(params)
				.get(0).getAttachment_path();
		System.out.println(filename);
		File file = new File(ATTACHMENTPATH + filename);
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
	public Map<String, Object> upload(String userid, String id,String num,String attachment_path,String task_def_key,MultipartFile[] files)
			throws Exception {
		String msg = "";
		String attachmentPath = ATTACHMENTPATH +File.separatorChar;

		if (files.length > 0) {
			for (MultipartFile mfile : files) {
				Map<String,Object> map = new HashMap<String, Object>();
				//map.put("attachment_name", mfile.getOriginalFilename());
				//map.put("case_id", num);
				String filename=mfile.getOriginalFilename();
//				if(rdsBaceraInvasivePreAttachmentMapper.queryCountAttachment(id) > 0)
//				{
//					return setMsg(false, msg + "上传失败！该案例已上传过报告，若需要重新上传，请先删除附件！");
//				}
				if(rdsBaceraInvasivePreAttachmentMapper.queryCountSameAttachment(filename) > 0)
				{
					return setMsg(false, msg + "上传失败！已有同名报告上传，请重新上传！");
				}
				RdsFileUtil.fileUpload(
						attachmentPath + mfile.getOriginalFilename(),
						mfile.getInputStream());
				Map<String, Object> attachParams = new HashMap<>();
				attachParams.put("uuid", UUIDUtil.getUUID());
				attachParams
						.put("attachment_path", mfile.getOriginalFilename());
				attachParams.put("upload_userid", userid);
				attachParams.put("id",id);
				rdsBaceraInvasivePreAttachmentMapper.insertAttachment(attachParams);
				if(!"taskMail".equals(task_def_key)){
				rdsBaceraInvasivePreMapper.updateVerifyState(id);}
				//解压当前文件
//				RdsFileUtil.unrar(attachmentPath + mfile.getOriginalFilename(),attachmentPath);
//				File directory = new File(attachmentPath + mfile.getOriginalFilename().split(".rar")[0]);
//                File[] lists = directory.listFiles();
                //保存文件中样本名
//                for(File file:lists){
//                	if(!file.getName().endsWith("pdf"))
//                        continue;
//                    Map<String,Object> temp = new HashMap<>();
//                    temp.put("uuid",UUIDUtil.getUUID());
//                    temp.put("uploadPer",userid);
//                    temp.put("sample_code",file.getName().split(".pdf")[0]);
//                    rdsBaceraInvasivePreAttachmentMapper.insertExperimentLog(temp);
//                }
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
		return rdsBaceraInvasivePreAttachmentMapper.updateAttachment(params);
	}
	@Transactional
	@Override
	public int deleteAttachment(Map<String, Object> params) {
		String attachmentPath = ATTACHMENTPATH
				+ params.get("attachment_path").toString();
		RdsFileUtil.delFolder(attachmentPath);
		String task_def_key=params.get("task_def_key").toString();
		if(!"taskMail".equals(task_def_key)){
			rdsBaceraInvasivePreMapper.downVerifyState(params);}
		
		return rdsBaceraInvasivePreAttachmentMapper.deleteAttachement(params);
	}

	@Override
	public List<RdsJudicialAttachmentModel> queryExperimentLog(
			Map<String, Object> params) {
		return rdsBaceraInvasivePreAttachmentMapper.queryExperimentLog(params);
	}

	@Override
	public int countExperimentLog(Map<String, Object> params) {
		return rdsBaceraInvasivePreAttachmentMapper.countExperimentLog(params);
	}

	@Override
	public void exportExperimentLog(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "实验室数据时间日志";
		List<RdsJudicialAttachmentModel> list = rdsBaceraInvasivePreAttachmentMapper.queryExperimentLog(params);
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

	@Override
	public int mailAttachment(Map<String, Object> params) {
       
		return rdsBaceraInvasivePreMapper.mailAttachment(params);
	}

	@Override
	public int onMailOver(Map<String, Object> params) {
		return rdsBaceraInvasivePreMapper.onMailOver(params);
	}

	@Override
	public int onFile(Map<String, Object> params) {
		return rdsBaceraInvasivePreMapper.onFile(params);
	}

	@Override
	public int queryAllCount(Map<String, Object> params) {
	
		return rdsBaceraInvasivePreAttachmentMapper.queryAllCount(params);
	}
	@Override
	public void exportPreAttachment(Map<String, Object> params,
			HttpServletResponse response) throws Exception {

			String filename = "无创产前报告";
			List<RdsBaceraInvasivePreAttachmentModel> list = rdsBaceraInvasivePreAttachmentMapper.queryAttachment(params);

				Object[] titles = { "案例编号", "姓名", "市场归属人", "附件", "案例状态",
						"最后上传日期", "上传人员", "下载人员", "最后下载日期", "是否发送邮件","财务确认状态"};
				List<List<Object>> bodys = new ArrayList<List<Object>>();
				for (int i = 0; i < list.size(); i++) {
					List<Object> objects = new ArrayList<Object>();
					RdsBaceraInvasivePreAttachmentModel attachmentModel =list
							.get(i);
					objects.add(attachmentModel.getNum());
					objects.add(attachmentModel.getName());
					objects.add(attachmentModel.getOwnpersonname());
					objects.add("".equals(attachmentModel.getAttachment_path()) || null == attachmentModel.getAttachment_path()? "":attachmentModel.getAttachment_path());
					String type=(attachmentModel.getType());
					if ("0".equals(type)){
						objects.add("正常");
					}else if ("1".equals(type)){
						objects.add("先出报告后付款");
					}else if ("2".equals(type)){
						objects.add("免单");
					}else if ("3".equals(type)){
						objects.add("优惠");
					}else if ("4".equals(type)){
						objects.add("月结");
					}else if ("5".equals(type)){
						objects.add("二次采样");
					}else if ("6".equals(type)){
						objects.add("补样");
					}else {
						objects.add("正常");
					}
					objects.add(attachmentModel.getAttachment_date());
					objects.add(attachmentModel.getUpload_username());
					objects.add(attachmentModel.getDownload_username());
					objects.add(attachmentModel.getDownload_time());
					objects.add("0".equals(attachmentModel.getEmailflag())?"未发送":"已发送");
					String confirm_state=attachmentModel.getConfirm_state();
					if("".equals(confirm_state)||null==confirm_state){
						objects.add("未汇款");
					}else if("-1".equals(confirm_state)){
						objects.add("未确认");
					}else if("1".equals(confirm_state)){
						objects.add("汇款确认通过");
					}else if("2".equals(confirm_state)){
						objects.add("汇款确认不通过");
					}
					bodys.add(objects);
				}
				ExportUtils.export(response, filename, titles, bodys, "无创产前报告");
			} 

		

}
