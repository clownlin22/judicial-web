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

import com.rds.bacera.mapper.RdsBaceraWuHanMedExamineMapper;
import com.rds.bacera.model.RdsBaceraWuHanMedExamineModel;
import com.rds.bacera.service.RdsBaceraWuHanMedExamineService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.model.RdsJudicialKeyValueModel;

@Service("rdsBaceraWuHanMedExamineServiceImpl")
public class RdsBaceraWuHanMedExamineServiceImpl implements
		RdsBaceraWuHanMedExamineService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	@Setter
	@Autowired
	private RdsBaceraWuHanMedExamineMapper rdsMedExamineMapper;

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
		result.put("total", rdsMedExamineMapper.queryAllCount(params));
		result.put("data", rdsMedExamineMapper.queryAllPage(params));
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
			if (rdsMedExamineMapper.queryProgramCount(map) == 0) {
				rdsMedExamineMapper.insertProgram(map);
			}
			return rdsMedExamineMapper.update(params);
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
			if (rdsMedExamineMapper.queryProgramCount(map) == 0) {
				rdsMedExamineMapper.insertProgram(map);
			}
			return rdsMedExamineMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsMedExamineMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int queryNumExit(Map map) {
		return rdsMedExamineMapper.queryNumExit(map);
	}

	@Override
	public void exportMedExamine(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "医学检测项（武汉）";
		List<Object> list = rdsMedExamineMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			// 导出excel头列表
			Object[] titles = { "案例编号", "日期", "姓名", "项目", "应收款项", "所付款项",
					"到款时间", "确认时间", "优惠价格", "财务备注", "快递日期", "快递类型", "快递单号",
					"快递备注", "归属地", "归属人", "代理商", "是否发报告", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraWuHanMedExamineModel rdsBaceraMedExamineModel = (RdsBaceraWuHanMedExamineModel) list
						.get(i);
				// 按照顺序对应表头
				objects.add(rdsBaceraMedExamineModel.getNum());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedExamineModel.getDate()));
				objects.add(rdsBaceraMedExamineModel.getName());
				objects.add(rdsBaceraMedExamineModel.getProgram());
				// 应收款项
				objects.add(("".equals(rdsBaceraMedExamineModel
						.getReceivables()) || null == rdsBaceraMedExamineModel
						.getReceivables()) ? 0 : Float
						.parseFloat(rdsBaceraMedExamineModel.getReceivables()));
				// 所付款项
				objects.add(("".equals(rdsBaceraMedExamineModel.getPayment()) || null == rdsBaceraMedExamineModel
						.getPayment()) ? 0 : Float
						.parseFloat(rdsBaceraMedExamineModel.getPayment()));
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedExamineModel
								.getParagraphtime()));
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedExamineModel
								.getConfirm_date()));
				objects.add(rdsBaceraMedExamineModel.getDiscountPrice());
				objects.add(rdsBaceraMedExamineModel.getRemarks());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedExamineModel
								.getExpresstime()));
				objects.add(rdsBaceraMedExamineModel.getExpresstype());
				objects.add(rdsBaceraMedExamineModel.getExpressnum());
				objects.add(rdsBaceraMedExamineModel.getExpressremark());

				objects.add(rdsBaceraMedExamineModel.getAreaname());
				objects.add(rdsBaceraMedExamineModel.getOwnpersonname());
				objects.add(rdsBaceraMedExamineModel.getAgentname());
				objects.add("1".equals(rdsBaceraMedExamineModel.getReportif()) ? "是"
						: "否");
				objects.add(rdsBaceraMedExamineModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "医学检测项（武汉）");
		} else {
			// 导出excel头列表
			Object[] titles = { "案例编号", "日期", "姓名", "项目", "快递日期", "快递类型",
					"快递单号", "快递备注", "归属地", "归属人", "代理商", "是否发报告", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraWuHanMedExamineModel rdsBaceraMedExamineModel = (RdsBaceraWuHanMedExamineModel) list
						.get(i);
				// 按照顺序对应表头
				objects.add(rdsBaceraMedExamineModel.getNum());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedExamineModel.getDate()));
				objects.add(rdsBaceraMedExamineModel.getName());
				objects.add(rdsBaceraMedExamineModel.getProgram());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedExamineModel
								.getExpresstime()));
				objects.add(rdsBaceraMedExamineModel.getExpresstype());
				objects.add(rdsBaceraMedExamineModel.getExpressnum());
				objects.add(rdsBaceraMedExamineModel.getExpressremark());

				objects.add(rdsBaceraMedExamineModel.getAreaname());
				objects.add(rdsBaceraMedExamineModel.getOwnpersonname());
				objects.add(rdsBaceraMedExamineModel.getAgentname());
				objects.add("1".equals(rdsBaceraMedExamineModel.getReportif()) ? "是"
						: "否");
				objects.add(rdsBaceraMedExamineModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "医学检测项（武汉）");
		}

	}

	@Override
	public List<RdsJudicialKeyValueModel> queryProgram() {
		return rdsMedExamineMapper.queryProgram();
	}

}
