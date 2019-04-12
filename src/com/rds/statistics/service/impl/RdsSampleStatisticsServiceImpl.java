package com.rds.statistics.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.code.utils.ExportUtils;
import com.rds.statistics.mapper.RdsSampleStatisticsMapper;
import com.rds.statistics.model.RdsSampleStatisticsModel;
import com.rds.statistics.service.RdsSampleStatisticsService;

/**
 * @author yxb
 * @className
 * @description
 * @date 2017/8/15
 */
@Service
public class RdsSampleStatisticsServiceImpl implements
		RdsSampleStatisticsService {

	@Autowired
	private RdsSampleStatisticsMapper rdsSampleStatisticsMapper;

	public List<Object> queryAll(Map<String, Object> params) {
		return rdsSampleStatisticsMapper.queryAll(params);
	}

	public int queryAllCount(Map<String, Object> params) {
		return rdsSampleStatisticsMapper.queryAllCount(params);
	}

	Logger logger = Logger.getLogger(RdsAllCaseInfoServiceImpl.class);

	@Override
	public void export(String sample_in_per, String month,String deptname,
			HttpServletResponse httpResponse) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sample_in_per", sample_in_per);
		params.put("month", month);
		params.put("deptname", deptname);
		String filename = "采样统计";
		List<Object> list = rdsSampleStatisticsMapper.queryAll(params);
		// 导出excel头列表
		Object[] titles = { "部门", "采样员", "数量", "月份" };
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsSampleStatisticsModel rdsSampleStatisticsModel = (RdsSampleStatisticsModel) list
					.get(i);
			// 按照顺序对应表头
			objects.add(rdsSampleStatisticsModel.getDeptname());
			objects.add(rdsSampleStatisticsModel.getSample_in_per());
			objects.add(rdsSampleStatisticsModel.getCountSample());
			objects.add(rdsSampleStatisticsModel.getMonth());
			bodys.add(objects);
		}
		ExportUtils.export(httpResponse, filename, titles, bodys, "采样统计");

	}

	@Override
	public int sampleStatistisc() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar calendar1 = Calendar.getInstance();
		calendar1.add(Calendar.MONTH, -1);
		calendar1.set(Calendar.DAY_OF_MONTH, 1);
		String sys_date = sdf.format(calendar1.getTime());
		return rdsSampleStatisticsMapper.sampleStatistisc(sys_date);
	}

	@Override
	public List<Object> queryAllSample(Map<String, Object> params) {
		return rdsSampleStatisticsMapper.queryAllSample(params);
	}

	@Override
	public void exportSample(String sample_in_per_id, String month,
			HttpServletResponse httpResponse) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sample_in_per_id", sample_in_per_id);
		params.put("month", month);
		String filename = "采样案例统计";
		List<Object> list = rdsSampleStatisticsMapper.queryAllSample(params);
		// 导出excel头列表
		Object[] titles = { "受理时间", "委托人", "样本姓名", "样本条码","采样人员","月份","部门" };
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsSampleStatisticsModel rdsSampleStatisticsModel = (RdsSampleStatisticsModel) list
					.get(i);
			// 按照顺序对应表头
			objects.add(rdsSampleStatisticsModel.getAccept_time());
			objects.add(rdsSampleStatisticsModel.getClient());
			objects.add(rdsSampleStatisticsModel.getSample_username());
			objects.add(rdsSampleStatisticsModel.getSample_code());
			objects.add(rdsSampleStatisticsModel.getSample_in_per());
			objects.add(rdsSampleStatisticsModel.getMonth());
			objects.add(rdsSampleStatisticsModel.getDeptname());
			bodys.add(objects);
		}
		ExportUtils.export(httpResponse, filename, titles, bodys, "采样案例统计");
	}

	@Override
	public int queryAllSampleCount(Map<String, Object> params) {
		return rdsSampleStatisticsMapper.queryAllSampleCount(params);
	}

}