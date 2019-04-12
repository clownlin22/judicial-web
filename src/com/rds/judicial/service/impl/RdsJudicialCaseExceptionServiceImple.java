package com.rds.judicial.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialCaseExceptionMapper;
import com.rds.judicial.model.RdsJudicialCaseExceptionInfo;
import com.rds.judicial.model.RdsJudicialCaseExceptionInfoModel;
import com.rds.judicial.model.RdsJudicialCaseExceptionModel;
import com.rds.judicial.model.RdsJudicialCaseExportExceptionModel;
import com.rds.judicial.model.RdsJudicialDicValuesModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialCaseExceptionService;

@Service
public class RdsJudicialCaseExceptionServiceImple implements RdsJudicialCaseExceptionService{
     
	@Autowired
	private RdsJudicialCaseExceptionMapper RdsJudicialCaseExceptionMapper;

	@Override
	public RdsJudicialResponse getCaseException(Map<String, Object> params) {
		RdsJudicialResponse response=new RdsJudicialResponse();
		List<RdsJudicialCaseExceptionInfoModel> exceptionInfoModels=RdsJudicialCaseExceptionMapper.getCaseException(params);
		int count=RdsJudicialCaseExceptionMapper.countCaseException(params);
		response.setCount(count);
		response.setItems(exceptionInfoModels);
		return response ;
	}

	@Override
	public List<RdsJudicialCaseExceptionModel> getOtherException(
			Map<String, Object> params) {
		return RdsJudicialCaseExceptionMapper.getOtherException(params);
	}

	@Override
	public boolean saveExceptionInfo(Map<String, Object> params) {
		params.put("exception_id", UUIDUtil.getUUID());
		return RdsJudicialCaseExceptionMapper.saveExceptionInfo(params);
	}

	@Override
	public List<RdsJudicialDicValuesModel> getExceptionTypes() {
		return RdsJudicialCaseExceptionMapper.getExceptionTypes();
	}

	@Override
	public boolean deleteExceptionInfo(Map<String, Object> params) {
		return RdsJudicialCaseExceptionMapper.deleteExceptionInfo(params);
	}

	@Override
	public boolean handleExceptionInfo(Map<String, Object> params) {
		return RdsJudicialCaseExceptionMapper.handleExceptionInfo(params);
	}

	@Override
	public boolean updateExceptionInfo(Map<String, Object> params) {
		return RdsJudicialCaseExceptionMapper.updateExceptionInfo(params);
	}

	@Override
	public RdsJudicialCaseExceptionInfo getCaseInfo(Map<String, Object> params) {
		List<RdsJudicialCaseExceptionInfo> caseInfo = (List<RdsJudicialCaseExceptionInfo>) RdsJudicialCaseExceptionMapper
				.getCaseInfo(params);
		if (caseInfo.size() > 0) {
			RdsJudicialCaseExceptionInfo ca = caseInfo.get(0);
			if (null != ca.getSample_in_time()
					&& !"".equals(ca.getSample_in_time())) {
				ca.setSample_in_time(ca.getSample_in_time().length() > 10 ? ca
						.getSample_in_time().substring(0,
								ca.getSample_in_time().length() - 2) : ca
						.getSample_in_time());
			}
			return ca;
		} else {
			return null;
		}
	}

	@Override
	public RdsJudicialResponse getExportException(
			Map<String, Object> params) {
		RdsJudicialResponse response=new RdsJudicialResponse();
		List<RdsJudicialCaseExportExceptionModel> list = RdsJudicialCaseExceptionMapper.getExportExceptionByPage(params);
		int count=RdsJudicialCaseExceptionMapper.countExportException(params);
		response.setCount(count);
		response.setItems(list);
		return response;
	}

	@Override
	public void exportCaseException(Map<String, Object> params,
			HttpServletResponse response) {
		String filename = "案例异常信息查看";
		List<RdsJudicialCaseExportExceptionModel> list =  RdsJudicialCaseExceptionMapper.getExportException(params);
			Object[] titles = { "案例编号", "归属人", "委托人","受理时间","异常描述", "异常登记时间" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for(int i = 0 ; i < list.size() ; i ++)
			{
				List<Object> objects = new ArrayList<Object>();
				RdsJudicialCaseExportExceptionModel rdsJudicialCaseExportExceptionModel = (RdsJudicialCaseExportExceptionModel)list.get(i);
				objects.add(rdsJudicialCaseExportExceptionModel.getCase_code());
				objects.add(rdsJudicialCaseExportExceptionModel.getUsername());
				objects.add(rdsJudicialCaseExportExceptionModel.getClient());
				objects.add(rdsJudicialCaseExportExceptionModel.getAccept_time());
				objects.add(rdsJudicialCaseExportExceptionModel.getException_desc());
				objects.add(rdsJudicialCaseExportExceptionModel.getException_time());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "案例异常信息查看");
		}		
}
