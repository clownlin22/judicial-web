package com.rds.judicial.service.impl;

/**
 * yuanxiaobo 2015-05-18
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.judicial.mapper.RdsJudicialCaseStateMapper;
import com.rds.judicial.mapper.RdsJudicialPhoneMapper;
import com.rds.judicial.model.RdsJudicialCaseStateInfoModel;
import com.rds.judicial.model.RdsJudicialCaseStateModel;
import com.rds.judicial.model.RdsJudicialCaseStatusModel;
import com.rds.judicial.service.RdsJudicialCaseStateService;

@Service("RdsJudicialCaseStateService")
public class RdsJudicialCaseStateServiceImpl implements RdsJudicialCaseStateService{
	
	@Setter
	@Autowired
	private RdsJudicialCaseStateMapper rdsJudicialCaseStateMapper;

	@Autowired
	private RdsJudicialPhoneMapper pMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object queryModel(Object object) throws Exception {
		Map<String, String> map = (Map<String, String>)object;
		List<RdsJudicialCaseStatusModel> csmodel;
//		List<Object> lists = (List<Object>)rdsJudicialCaseStateMapper.queryAll(object);
//		RdsJudicialCaseStateInfoModel model = new RdsJudicialCaseStateInfoModel();
//		if(lists.size()>0) 
//			model = (RdsJudicialCaseStateInfoModel)lists.get(0);
//		String[] list = {};
//		if(model != null)
//		{
//			if(model.getId_number() != null  && !"".equals(model.getId_number()))
//			{
//				list = model.getId_number().split(",");
//			}
//		}
//		String id_number="";
//		for (String string : list) {
//			if(!"".equals(string))
//			{
//				id_number=string;
//			}
//		}
//		if("".equals(id_number)) return null;
		RdsJudicialCaseStateModel row;
		List<RdsJudicialCaseStateModel> rows = new ArrayList<>();
		try {
			
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("id_number", id_number);
			
			//查询数据库
			csmodel = pMapper.getCaseStatueByIdnumber(map);
			if (csmodel.size()==0) {
				row = new RdsJudicialCaseStateModel();
			} else {
				for (RdsJudicialCaseStatusModel smodel : csmodel) {
//
//					String client = StringUtils.isBlank(smodel.getClient()) ? ""
//							: smodel.getClient();
					if (StringUtils.isNotBlank(smodel.getAccept_time())) {
						row = new RdsJudicialCaseStateModel();
						row.setProcesstime(smodel.getAccept_time());
						row.setProcessstatus("案例受理中");
						rows.add(row);
					}
					if (StringUtils.isNotBlank(smodel.getSample_in_time())) {
						row = new RdsJudicialCaseStateModel();
						row.setProcesstime(smodel.getSample_in_time());
						row.setProcessstatus("案例信息审核中");
						rows.add(row);
					}
					if (StringUtils.isNotBlank(smodel
							.getVerify_baseinfo_time())) {
						row = new RdsJudicialCaseStateModel();
						row.setProcesstime(smodel.getVerify_baseinfo_time());
						row.setProcessstatus("样本审核中");
						rows.add(row);
					}
					if (StringUtils.isNotBlank(smodel
							.getVerify_sampleinfo_time())) {
						row = new RdsJudicialCaseStateModel();
						row.setProcesstime(smodel.getVerify_sampleinfo_time());
						row.setProcessstatus("实验中");
						rows.add(row);
					}
					if (StringUtils.isNotBlank(smodel.getTrans_date())) {
						row = new RdsJudicialCaseStateModel();
						if(StringUtils.isNotBlank(smodel.getFinal_result_flag()))
						{
							row.setProcesstime(smodel.getTrans_date());
							row.setProcessstatus("报告打印中");
							rows.add(row);
						}else
						{
							if (StringUtils.isNotBlank(smodel
									.getVerify_sampleinfo_time())){
								row.setProcesstime("");
								row.setProcessstatus("二次采样中");
								rows.add(row);
							}
						}
					}
					if (StringUtils.isNotBlank(smodel.getClose_time())) {
						row = new RdsJudicialCaseStateModel();
						row.setProcesstime(smodel.getClose_time());
						row.setProcessstatus("等待邮寄");
						rows.add(row);
					}
					if (StringUtils.isNotBlank(smodel.getMail_time())) {
						row = new RdsJudicialCaseStateModel();
						row.setProcesstime(smodel.getMail_time());
						row.setProcessstatus("已邮寄报告");
						rows.add(row);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
//			result = RUNTIMEERROR;
//			info = "内部错误";
//			resultMap = getResult(result, info, datas);
//			String respString = "";
			return null;
		}
		return rows;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsJudicialCaseStateMapper.queryAllCount(params));
		result.put("data", rdsJudicialCaseStateMapper.queryAllPage(params));
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
			return rdsJudicialCaseStateMapper.update(params);
		}catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public List<RdsJudicialCaseStateInfoModel> queryCaseState(
			Map<String, Object> params) {

		return null;
	}

	@Override
	public int queryCaseStateCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@SuppressWarnings("unused")
	private Map<String, Object> getResult(String result, String info,
			List<Map<String, Object>> datas) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("datas", datas);
		map.put("info", info);
		map.put("result", result);
		return map;
	}

	@Override
	public int queryCompareResultCount(Map<String, Object> params) {
		return rdsJudicialCaseStateMapper.queryCompareResultCount(params);
	}

}
