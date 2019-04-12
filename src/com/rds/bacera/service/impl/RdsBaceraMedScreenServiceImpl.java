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

import com.rds.bacera.mapper.RdsBaceraMedScreenMapper;
import com.rds.bacera.model.RdsBaceraMedScreenModel;
import com.rds.bacera.service.RdsBaceraMedScreenService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.model.RdsJudicialKeyValueModel;

@Service("rdsBaceraMedScreenServiceImpl")
public class RdsBaceraMedScreenServiceImpl implements RdsBaceraMedScreenService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	@Setter
	@Autowired
	private RdsBaceraMedScreenMapper rdsBaceraMedScreenMapper;

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
		result.put("total", rdsBaceraMedScreenMapper.queryAllCount(params));
		result.put("data", rdsBaceraMedScreenMapper.queryAllPage(params));
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
			if (rdsBaceraMedScreenMapper.queryProgramCount(map) == 0) {
				rdsBaceraMedScreenMapper.insertProgram(map);
			}
			return rdsBaceraMedScreenMapper.update(params);
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
			if (rdsBaceraMedScreenMapper.queryProgramCount(map) == 0) {
				rdsBaceraMedScreenMapper.insertProgram(map);
			}
			return rdsBaceraMedScreenMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsBaceraMedScreenMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int queryNumExit(Map map) {
		return rdsBaceraMedScreenMapper.queryNumExit(map);
	}

	@Override
	public void exportMedScreen(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "血清筛查";
		List<Object> list = rdsBaceraMedScreenMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			// 导出excel头列表
			Object[] titles = { "案例编号", "日期", "姓名", "项目", "送检人", "应收款项",
					"所付款项", "到款时间", "确认时间", "优惠价格", "财务备注", "快递日期", "快递类型",
					"快递单号", "快递备注", "归属地", "归属人", "代理商", "是否发报告", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraMedScreenModel rdsBaceraMedScreenModel = (RdsBaceraMedScreenModel) list
						.get(i);
				// 按照顺序对应表头
				objects.add(rdsBaceraMedScreenModel.getNum());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedScreenModel.getDate()));
				objects.add(rdsBaceraMedScreenModel.getName());
				objects.add(rdsBaceraMedScreenModel.getProgram());
				objects.add(rdsBaceraMedScreenModel.getInspection());
				// 应收款项
				objects.add(("".equals(rdsBaceraMedScreenModel.getReceivables()) || null == rdsBaceraMedScreenModel
						.getReceivables()) ? 0 : Float
						.parseFloat(rdsBaceraMedScreenModel.getReceivables()));
				// 所付款项
				objects.add(("".equals(rdsBaceraMedScreenModel.getPayment()) || null == rdsBaceraMedScreenModel
						.getPayment()) ? 0 : Float
						.parseFloat(rdsBaceraMedScreenModel.getPayment()));
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedScreenModel
								.getParagraphtime()));
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedScreenModel
								.getConfirm_date()));
				objects.add(rdsBaceraMedScreenModel.getDiscountPrice());
				objects.add(rdsBaceraMedScreenModel.getRemarks());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedScreenModel
								.getExpresstime()));
				objects.add(rdsBaceraMedScreenModel.getExpresstype());
				objects.add(rdsBaceraMedScreenModel.getExpressnum());
				objects.add(rdsBaceraMedScreenModel.getExpressremark());

				objects.add(rdsBaceraMedScreenModel.getAreaname());
				objects.add(rdsBaceraMedScreenModel.getOwnpersonname());
				objects.add(rdsBaceraMedScreenModel.getAgentname());
				objects.add("1".equals(rdsBaceraMedScreenModel.getReportif()) ? "是"
						: "否");
				objects.add(rdsBaceraMedScreenModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "血清筛查");
		} else {
			// 导出excel头列表
			Object[] titles = { "案例编号", "日期", "姓名", "项目", "送检人", "快递日期",
					"快递类型", "快递单号", "快递备注", "归属地", "归属人", "代理商", "是否发报告",
					"备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraMedScreenModel rdsBaceraMedScreenModel = (RdsBaceraMedScreenModel) list
						.get(i);
				// 按照顺序对应表头
				objects.add(rdsBaceraMedScreenModel.getNum());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedScreenModel.getDate()));
				objects.add(rdsBaceraMedScreenModel.getName());
				objects.add(rdsBaceraMedScreenModel.getProgram());
				objects.add(rdsBaceraMedScreenModel.getInspection());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedScreenModel
								.getExpresstime()));
				objects.add(rdsBaceraMedScreenModel.getExpresstype());
				objects.add(rdsBaceraMedScreenModel.getExpressnum());
				objects.add(rdsBaceraMedScreenModel.getExpressremark());

				objects.add(rdsBaceraMedScreenModel.getAreaname());
				objects.add(rdsBaceraMedScreenModel.getOwnpersonname());
				objects.add(rdsBaceraMedScreenModel.getAgentname());
				objects.add("1".equals(rdsBaceraMedScreenModel.getReportif()) ? "是"
						: "否");
				objects.add(rdsBaceraMedScreenModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "血清筛查");
		}

	}

	@Override
	public List<RdsJudicialKeyValueModel> queryProgram() {
		return rdsBaceraMedScreenMapper.queryProgram();
	}

}
