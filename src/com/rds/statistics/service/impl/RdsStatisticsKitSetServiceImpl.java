package com.rds.statistics.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.code.date.DateUtils;
import com.rds.code.utils.ExportUtils;
import com.rds.statistics.mapper.RdsStatisticsKitSetMapper;
import com.rds.statistics.model.RdsFinanceKitSetModel;
import com.rds.statistics.service.RdsStatisticsKitSetService;

@Service
public class RdsStatisticsKitSetServiceImpl implements RdsStatisticsKitSetService {
	
	@Autowired
	private RdsStatisticsKitSetMapper rdsStatisticsKitSetMapper;

	@Override
	public List<Object> queryAllPage(Map<String, Object> params) {
		return rdsStatisticsKitSetMapper.queryAllPage(params);
	}

	@Override
	public int queryAllCount(Map<String, Object> params) {
		return rdsStatisticsKitSetMapper.queryAllCount(params);
	}

	@Override
	public boolean insertKitSet(Map<String, Object> params) {
		return rdsStatisticsKitSetMapper.insertKitSet(params);
	}

	@Override
	public boolean deleteKitSet(Map<String, Object> params) {
		return rdsStatisticsKitSetMapper.deleteKitSet(params);
	}

	@Override
	public boolean updateKitSet(Map<String, Object> params) {
		return rdsStatisticsKitSetMapper.updateKitSet(params);
	}

	@Override
	public boolean comfirmKitSet(Map<String, Object> params) {
		return rdsStatisticsKitSetMapper.comfirmKitSet(params);
	}

	@Override
	public List<String> queryDeptList() {
		return rdsStatisticsKitSetMapper.queryDeptList();
	}

	@Override
	public void exportKit(Map<String, Object> params,HttpServletResponse httpResponse) {
		
		List<Object> objs = rdsStatisticsKitSetMapper.queryAllPage(params);
		// excel表格列头
		Object[] titles = { "申请部门", "申请人", "泛薇申请流程编号", "日期", "试剂名称", "规格（RXN）",
				"数量（盒）", "批次号", "目的地", "顺风单号", "收件人", "备注", "登记人", "登记时间"};
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		// 循环案例列表拼装表格一行
		for (int i = 0; i < objs.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsFinanceKitSetModel rdsFinanceKitSetModel = (RdsFinanceKitSetModel) objs
					.get(i);
			objects.add(rdsFinanceKitSetModel.getUser_dept_level1());
			objects.add(rdsFinanceKitSetModel.getApply_per());
			objects.add(rdsFinanceKitSetModel.getApply_num());
			objects.add(rdsFinanceKitSetModel.getApply_date());
			objects.add(rdsFinanceKitSetModel.getKit_name());
			objects.add(rdsFinanceKitSetModel.getKit_spec());
			objects.add(rdsFinanceKitSetModel.getKit_count());
			objects.add(rdsFinanceKitSetModel.getKit_bathc_num());
			objects.add(rdsFinanceKitSetModel.getKit_dest());
			objects.add(rdsFinanceKitSetModel.getKit_express_num());
			objects.add(rdsFinanceKitSetModel.getKit_receive_per());
			objects.add(rdsFinanceKitSetModel.getKit_receive_per());
			objects.add(rdsFinanceKitSetModel.getCreate_pername());
			objects.add(rdsFinanceKitSetModel.getCreate_date());
			bodys.add(objects);
		}

		ExportUtils.export(httpResponse, "试剂销售", titles, bodys, "试剂销售"
				+ DateUtils.Date2String(new Date()));
		
	
	}
	
}