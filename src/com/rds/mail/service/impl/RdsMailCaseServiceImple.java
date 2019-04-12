package com.rds.mail.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.mail.mapper.RdsMailCaseMapper;
import com.rds.mail.model.RdsMailCaseInfo;
import com.rds.mail.model.RdsMailInfo;
import com.rds.mail.service.RdsMailCaseService;

@Service
@Transactional
public class RdsMailCaseServiceImple implements RdsMailCaseService{

	@Autowired
	private RdsMailCaseMapper RdsMailCaseMapper;
	
	@Override
	public Map<String, Object> getCaseInfo(Map<String, Object> params) {
		 Map<String, Object> resMap=new HashMap<String, Object>();
		List<RdsMailCaseInfo> caseInfos=RdsMailCaseMapper.getCaseInfos(params);
		int count =RdsMailCaseMapper.countCaseInfos(params);
		resMap.put("caseinfos", caseInfos);
		resMap.put("count", count);
		return resMap;
	}

	@Override
	public RdsMailCaseInfo getMailCase(Map<String, Object> params) {
		return RdsMailCaseMapper.getMailCase(params);
	}

	@Override
	public boolean saveMailInfo(Map<String, Object> params) {
		try {
			params.put("mail_id", UUIDUtil.getUUID());
			String[] cases=params.get("case_str").toString().split(";");
			RdsMailCaseMapper.insertMailInfo(params);
			if(cases.length>0){
				for(String ca:cases){
					params.put("case_id", ca.split(",")[0]);
					params.put("case_type", ca.split(",")[1]);
					RdsMailCaseMapper.insertCaseMailLink(params);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean exsitMailCode(Map<String, Object> params) {
		int count=RdsMailCaseMapper.exsitMailCode(params);
		if(count>0){
			return false;
		}
		return true;
	}

	@Override
	public RdsMailInfo getMailInfo(Map<String, Object> params) {
		RdsMailInfo mailInfo=RdsMailCaseMapper.getMailInfo(params);
		if(mailInfo!=null){
			List<RdsMailCaseInfo> caseInfos=RdsMailCaseMapper.getMailCaseInfo(params);
			mailInfo.setCaseInfos(caseInfos);
			return mailInfo;
		}else{
			return new RdsMailInfo();
		}
	}

	@Override
	public boolean updateMailInfo(Map<String, Object> params) {
		try {
			String[] cases=params.get("case_str").toString().split(";");
			RdsMailCaseMapper.updateMailInfo(params);
			RdsMailCaseMapper.deleteMailLinkByMap(params);
			if(cases.length>0){
				for(String ca:cases){
					params.put("case_id", ca.split(",")[0]);
					params.put("case_type", ca.split(",")[1]);
					RdsMailCaseMapper.insertCaseMailLink(params);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteMailInfo(Map<String, Object> params) {
		try {
			RdsMailInfo mailInfo=RdsMailCaseMapper.getMailInfo(params);
			RdsMailCaseMapper.deleteMailLinkByModel(mailInfo);
			RdsMailCaseMapper.deleteMailInfo(mailInfo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public List<RdsMailInfo> checkMails(Map<String, Object> params) {
		return RdsMailCaseMapper.checkMails(params);
	}
}
