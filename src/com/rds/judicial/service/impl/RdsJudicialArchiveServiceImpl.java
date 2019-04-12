package com.rds.judicial.service.impl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.rds.activiti.service.RdsActivitiJudicialService;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialArchiveMapper;
import com.rds.judicial.mapper.RdsJudicialArchiveReadMapper;
import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.mapper.RdsJudicialSampleRelayMapper;
import com.rds.judicial.model.RdsJudicialArchiveModel;
import com.rds.judicial.model.RdsJudicialArchiveReadModel;
import com.rds.judicial.model.RdsJudicialVerifyCaseInfoModel;
import com.rds.judicial.service.RdsJudicialArchiveService;
import com.rds.upc.model.RdsUpcUserModel;

@Service("RdsJudicialArchiveService")
@Transactional
public class RdsJudicialArchiveServiceImpl implements RdsJudicialArchiveService {

	@Setter
	@Autowired
	private RdsJudicialArchiveMapper archiveMapper;

	@Setter
	@Autowired
	private RdsJudicialRegisterMapper registerMapper;

	@Setter
	@Autowired
	private RdsJudicialArchiveReadMapper readMapper;

	@Autowired
	private RdsJudicialSampleRelayMapper RdsJudicialSampleRelayMapper;
	
	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;

	@Transactional
	public Map<String, Object> doArchive(Map<String, Object> params,
			HttpServletRequest request) throws ParseException {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		if(user == null){
			return setMsg(false,"请重新登录");
		}
		params.put("archive_id", UUIDUtil.getUUID());
		params.put("archive_date", new SimpleDateFormat("yyyy-MM-dd")
				.parse((String) params.get("archive_date")));
		params.put("archive_per", user.getUserid());
		params.put("archive_code",
				new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
		params.put("archive_path",(String)params.get("case_code") + File.separatorChar);
		if (archiveMapper.insert(params) > 0
				&& registerMapper.updateIsArchived(params) > 0) {
			String caseCode = params.get("case_code").toString();
			rdsActivitiJudicialService.runByCaseCode(caseCode, "taskFile", null, user);
			Map<String, Object> mapTemp = new HashMap<String, Object>();
			mapTemp.put("case_code", caseCode);
			mapTemp.put("verify_state", 10);
			RdsJudicialSampleRelayMapper.updateCaseVerifyBycode(mapTemp);
			return setMsg(true, "归档成功");
		} else {
			return setMsg(false, "归档失败,请联系管理员");
		}
	}

	private Map<String, Object> setMsg(boolean success, String message) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("message", message);
		return map;
	}

	@Override
	public Map<String, Object> queryAll(Map<String, Object> params) {
		List<RdsJudicialArchiveModel> list = archiveMapper.queryAll(params);
		int total = archiveMapper.queryAllCount(params);
		Map<String, Object> map = new HashMap<String, Object>();
		
			map.put("data", list);
			map.put("total", total);
			return map;
	}

	@Override
	@Transactional
	public Map<String, Object> readSave(Map<String, Object> params)
			throws ParseException {
		params.put("id", UUIDUtil.getUUID());
		params.put("read_date", new SimpleDateFormat("yyyy-MM-dd")
				.parse((String) params.get("read_date")));
		if (readMapper.insert(params) > 0) {
			return setMsg(true, "保存成功");
		} else {
			return setMsg(false, "保存失败,请联系管理员");
		}
	}

	@Override
	public Map<String, Object> queryRead(Map<String, Object> params) {
		List<RdsJudicialArchiveReadModel> list = readMapper.queryAll(params);
		Map<String, Object> map = new HashMap<String, Object>();
			map.put("data", list);
			return map;
	}

	@Override
	public Map<String, Object> queryMailCount(Map<String, Object> params) {
		if(archiveMapper.queryMailCount(params) > 0)
		{
			return setMsg(true, "通过");
		}else
		{
			return setMsg(false, "对不起，没有邮寄信息，不能归档");
		}
	}



	@Override
	public List<RdsJudicialVerifyCaseInfoModel> queryAllCase(
			Map<String, Object> params) {
		return archiveMapper.queryAllCase(params);
	}

	@Override
	public int queryALLCasseCount(Map<String, Object> params) {
		return archiveMapper.queryAllCaseCount(params);
	}
}
