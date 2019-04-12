package com.rds.judicial.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rds.code.utils.DownLoadUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.file.RdsFileUtil;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialReportMapper;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialCaseReportModel;
import com.rds.judicial.model.RdsJudicialHeadReportModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSubCaseInfoModel;
import com.rds.judicial.service.RdsJudicialReportService;

@Service
@Transactional
public class RdsJudicialReportServiceImpl implements RdsJudicialReportService{
  
	@Autowired
	private RdsJudicialReportMapper RdsJudicialReportMapper;
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";
	private static final String ATTACHMENT_PATH=PropertiesUtils.readValue(FILE_PATH, "judicial_head_report");
	@Override
	public RdsJudicialResponse getCaseInfo(Map<String, Object> params) {
		RdsJudicialResponse response=new RdsJudicialResponse();
		List<RdsJudicialCaseInfoModel> caseInfoModels=RdsJudicialReportMapper.getCaseInfo(params);
		int count=RdsJudicialReportMapper.countCaseInfo(params);
		response.setCount(count);
		response.setItems(caseInfoModels);
		return response;
	}
	
	@Override
	public List<RdsJudicialCaseReportModel> getCaseReport(
			Map<String, Object> params) {
		List<RdsJudicialCaseReportModel> list=RdsJudicialReportMapper.getCaseReport(params);
		return list;
	}

	@Override
	public boolean updateReport(RdsJudicialHeadReportModel headReportModel,
			MultipartFile[] files) {
		if(files.length>0){
			String attachmentPath = ATTACHMENT_PATH + headReportModel.getSub_case_code() + File.separatorChar;
			for(MultipartFile file:files){
				if(!RdsFileUtil.getState(attachmentPath+file.getOriginalFilename())){
					 try {
						RdsFileUtil.fileUpload(attachmentPath+file.getOriginalFilename(), file.getInputStream());
						headReportModel.setReport_id(UUIDUtil.getUUID());
						headReportModel.setReport_path(headReportModel.getSub_case_code() + File.separatorChar+file.getOriginalFilename());
						RdsJudicialReportMapper.delReport(headReportModel);
						int count=RdsJudicialReportMapper.updateReport(headReportModel);
						if(count>0){
							RdsJudicialReportMapper.updateCaseResult(headReportModel);
							return true;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}

	@Override
	public void downloadReport(String filepath,HttpServletResponse response) {
		DownLoadUtils.downloadWord(response,ATTACHMENT_PATH+filepath,filepath);
	}

}
