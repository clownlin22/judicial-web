package com.rds.judicial.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.StringUtils;
import com.rds.judicial.mapper.RdsJudicialCaseFeeMapper;
import com.rds.judicial.model.RdsJudicialCaseFeeModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.service.RdsJudicialCaseFeeService;

@Service
public class RdsJudicialCaseFeeServiceImple implements RdsJudicialCaseFeeService{

	@Setter
	@Autowired
	private RdsJudicialCaseFeeMapper caseFeeMapper;

	@Override
	public RdsJudicialResponse getCaseFeeInfo(Map<String, Object> params) {
		RdsJudicialResponse response=new RdsJudicialResponse();
		List<RdsJudicialCaseFeeModel> caseFeeModels=caseFeeMapper.getCaseFeeInfo(params);
		int count=caseFeeMapper.countCaseFeeInfo(params);
		response.setCount(count);
		response.setItems(caseFeeModels);
		return response;
	}

	@Override
	public boolean saveCaseFee(Map<String, Object> params) {
		return caseFeeMapper.confirmCaseFee(params);
	}

	@Override
	public boolean caseFeeConfirm(Map<String, Object> params) {
		return caseFeeMapper.caseFeeConfirm(params);
	}
	@Override
	@Transactional
	public boolean insertOAnum(Map<String, Object> params) {
		String ids_str=params.get("ids").toString();
		String[] ids=ids_str.split(",");
		for(String id:ids){
			params.put("fee_id", id);
			caseFeeMapper.insertOAnum(params);
		}
		return true;
	}

	@Override
	public void exportCaseInfoOther(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "案例信息";
		List<RdsJudicialCaseFeeModel> list = caseFeeMapper.getCaseFeeInfo(params);

		Object[] titles = { "编号", "日期", "委托人", "归属人", "归属地","应收", "回款", "汇款时间",
				"确认时间", "优惠价格", "财务备注", "样本信息","是否确认"};
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsJudicialCaseFeeModel rdsJudicialCaseFeeModel = (RdsJudicialCaseFeeModel) list.get(i);
			objects.add(rdsJudicialCaseFeeModel.getCase_code());
			objects.add(StringUtils.dateToChineseTen(rdsJudicialCaseFeeModel.getDate()));
			objects.add(rdsJudicialCaseFeeModel.getClient());
			objects.add(rdsJudicialCaseFeeModel.getReceive_name());
			objects.add(rdsJudicialCaseFeeModel.getAreaname());
			objects.add(rdsJudicialCaseFeeModel.getReal_sum());
			objects.add(rdsJudicialCaseFeeModel.getReturn_sum());
			objects.add(StringUtils.dateToChineseTen(rdsJudicialCaseFeeModel.getParagraphtime()));
			objects.add(StringUtils.dateToChineseTen(rdsJudicialCaseFeeModel.getConfirm_date()));
			objects.add(rdsJudicialCaseFeeModel.getDiscountPrice());
			objects.add(rdsJudicialCaseFeeModel.getFinance_remark());
			objects.add(rdsJudicialCaseFeeModel.getSample_str());
			objects.add("0".equals(rdsJudicialCaseFeeModel.getStatus())?"是":"否");
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "案例信息");
	

	}

}
