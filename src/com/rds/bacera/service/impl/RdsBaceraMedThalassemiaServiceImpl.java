package com.rds.bacera.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.bacera.mapper.RdsBaceraMedThalassemiaMapper;
import com.rds.bacera.model.RdsBaceraMedThalassemiaModel;
import com.rds.bacera.service.RdsBaceraMedThalassemiaService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.model.RdsJudicialKeyValueModel;

@Service("rdsBaceraMedThalassemiaServiceImpl")
public class RdsBaceraMedThalassemiaServiceImpl implements
		RdsBaceraMedThalassemiaService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	@Setter
	@Autowired
	private RdsBaceraMedThalassemiaMapper rdsBaceraMedThalassemiaMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return 0;
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsBaceraMedThalassemiaMapper.queryAllCount(params));
		result.put("data", rdsBaceraMedThalassemiaMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int update(Object params) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", UUIDUtil.getUUID());
			map.put("program_name",
					(((Map<String, Object>) params).get("program").toString()
							.trim()));
			if (rdsBaceraMedThalassemiaMapper.queryProgramCount(map) == 0) {
				rdsBaceraMedThalassemiaMapper.insertProgram(map);
			}
			return rdsBaceraMedThalassemiaMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int insert(Object params) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", UUIDUtil.getUUID());
			map.put("program_name",
					(((Map<String, Object>) params).get("program").toString()
							.trim()));
			if (rdsBaceraMedThalassemiaMapper.queryProgramCount(map) == 0) {
				rdsBaceraMedThalassemiaMapper.insertProgram(map);
			}
			return rdsBaceraMedThalassemiaMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsBaceraMedThalassemiaMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int queryNumExit(Map map) {
		return rdsBaceraMedThalassemiaMapper.queryNumExit(map);
	}

	@Override
	public void exportMedThalassemia(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "地中海贫血";
		List<Object> list = rdsBaceraMedThalassemiaMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			// 导出excel头列表
			Object[] titles = { "案例编号", "物流条码", "日期", "姓名", "项目", "送检人",
					"应收款项", "所付款项", "到款时间", "确认时间", "优惠价格", "财务备注", "快递日期",
					"快递类型", "快递单号", "快递备注", "归属地", "归属人", "代理商", "是否发报告",
					"备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraMedThalassemiaModel rdsBaceraMedThalassemiaModel = (RdsBaceraMedThalassemiaModel) list
						.get(i);
				// 按照顺序对应表头
				objects.add(rdsBaceraMedThalassemiaModel.getNum());
				objects.add(rdsBaceraMedThalassemiaModel.getCode());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedThalassemiaModel
								.getDate()));
				objects.add(rdsBaceraMedThalassemiaModel.getName());
				objects.add(rdsBaceraMedThalassemiaModel.getProgram());
				objects.add(rdsBaceraMedThalassemiaModel.getInspection());
				// 应收款项
				objects.add(("".equals(rdsBaceraMedThalassemiaModel
						.getReceivables()) || null == rdsBaceraMedThalassemiaModel
						.getReceivables()) ? 0 : Float
						.parseFloat(rdsBaceraMedThalassemiaModel
								.getReceivables()));
				// 所付款项
				objects.add(("".equals(rdsBaceraMedThalassemiaModel
						.getPayment()) || null == rdsBaceraMedThalassemiaModel
						.getPayment()) ? 0 : Float
						.parseFloat(rdsBaceraMedThalassemiaModel.getPayment()));
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedThalassemiaModel
								.getParagraphtime()));
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedThalassemiaModel
								.getConfirm_date()));
				objects.add(rdsBaceraMedThalassemiaModel.getDiscountPrice());
				objects.add(rdsBaceraMedThalassemiaModel.getRemarks());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedThalassemiaModel
								.getExpresstime()));
				objects.add(rdsBaceraMedThalassemiaModel.getExpresstype());
				objects.add(rdsBaceraMedThalassemiaModel.getExpressnum());
				objects.add(rdsBaceraMedThalassemiaModel.getExpressremark());

				objects.add(rdsBaceraMedThalassemiaModel.getAreaname());
				objects.add(rdsBaceraMedThalassemiaModel.getOwnpersonname());
				objects.add(rdsBaceraMedThalassemiaModel.getAgentname());
				objects.add("1".equals(rdsBaceraMedThalassemiaModel
						.getReportif()) ? "是" : "否");
				objects.add(rdsBaceraMedThalassemiaModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "地中海贫血");
		} else {
			// 导出excel头列表
			Object[] titles = { "案例编号", "物流条码", "日期", "姓名", "项目", "送检人",
					"快递日期", "快递类型", "快递单号", "快递备注", "归属地", "归属人", "代理商",
					"是否发报告", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraMedThalassemiaModel rdsBaceraMedThalassemiaModel = (RdsBaceraMedThalassemiaModel) list
						.get(i);
				// 按照顺序对应表头
				objects.add(rdsBaceraMedThalassemiaModel.getNum());
				objects.add(rdsBaceraMedThalassemiaModel.getCode());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedThalassemiaModel
								.getDate()));
				objects.add(rdsBaceraMedThalassemiaModel.getName());
				objects.add(rdsBaceraMedThalassemiaModel.getProgram());
				objects.add(rdsBaceraMedThalassemiaModel.getInspection());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedThalassemiaModel
								.getExpresstime()));
				objects.add(rdsBaceraMedThalassemiaModel.getExpresstype());
				objects.add(rdsBaceraMedThalassemiaModel.getExpressnum());
				objects.add(rdsBaceraMedThalassemiaModel.getExpressremark());

				objects.add(rdsBaceraMedThalassemiaModel.getAreaname());
				objects.add(rdsBaceraMedThalassemiaModel.getOwnpersonname());
				objects.add(rdsBaceraMedThalassemiaModel.getAgentname());
				objects.add("1".equals(rdsBaceraMedThalassemiaModel
						.getReportif()) ? "是" : "否");
				objects.add(rdsBaceraMedThalassemiaModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "地中海贫血");
		}

	}

	@Override
	public List<RdsJudicialKeyValueModel> queryProgram() {
		return rdsBaceraMedThalassemiaMapper.queryProgram();
	}

}
