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

import com.rds.bacera.mapper.RdsBaceraDrugDetectionMapper;
import com.rds.bacera.model.RdsBaceraDrugDetectionModel;
import com.rds.bacera.service.RdsBaceraDrugDetectionService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.model.RdsJudicialKeyValueModel;

@Service("rdsBaceraDrugDetectionServiceImpl")
public class RdsBaceraDrugDetectionServiceImpl implements
		RdsBaceraDrugDetectionService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	@Setter
	@Autowired
	private RdsBaceraDrugDetectionMapper rdsBaceraDrugDetectionMapper;

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
		result.put("total", rdsBaceraDrugDetectionMapper.queryAllCount(params));
		result.put("data", rdsBaceraDrugDetectionMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
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
			if (rdsBaceraDrugDetectionMapper.queryProgramCount(map) == 0) {
				rdsBaceraDrugDetectionMapper.insertProgram(map);
			}
			return rdsBaceraDrugDetectionMapper.update(params);
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
			if (rdsBaceraDrugDetectionMapper.queryProgramCount(map) == 0) {
				rdsBaceraDrugDetectionMapper.insertProgram(map);
			}
			return rdsBaceraDrugDetectionMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsBaceraDrugDetectionMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int queryNumExit(Map map) {
		return rdsBaceraDrugDetectionMapper.queryNumExit(map);
	}

	@Override
	public void exportDrugDetection(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "毒品检测";
		List<Object> list = rdsBaceraDrugDetectionMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			// 导出excel头列表
			Object[] titles = { "案例编号", "委托日期", "委托单位","被鉴定人", "样品类型", "样本数", "鉴定事项","项目类型",
					"送检人", "应收款项", "所付款项", "到款时间", "确认时间", "优惠价格", "财务备注",
					"快递日期", "快递类型", "快递单号", "快递备注", "归属地", "归属人", "代理商","合作方","登记人",
					"是否发报告", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraDrugDetectionModel rdsBaceraDrugDetectionModel = (RdsBaceraDrugDetectionModel) list
						.get(i);
				// 按照顺序对应表头
				objects.add(rdsBaceraDrugDetectionModel.getNum());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraDrugDetectionModel.getDate()));
				objects.add(rdsBaceraDrugDetectionModel.getEntrusted_unit());
				objects.add(rdsBaceraDrugDetectionModel.getName());
				objects.add(rdsBaceraDrugDetectionModel.getSample_type());
				objects.add(rdsBaceraDrugDetectionModel.getSample_count());
				objects.add(rdsBaceraDrugDetectionModel.getProgram());
				objects.add(rdsBaceraDrugDetectionModel.getProgram_type());
				objects.add(rdsBaceraDrugDetectionModel.getInspection());
				// 应收款项
				objects.add(("".equals(rdsBaceraDrugDetectionModel
						.getReceivables()) || null == rdsBaceraDrugDetectionModel
						.getReceivables()) ? 0 : Float
						.parseFloat(rdsBaceraDrugDetectionModel
								.getReceivables()));
				// 所付款项
				objects.add(("".equals(rdsBaceraDrugDetectionModel.getPayment()) || null == rdsBaceraDrugDetectionModel
						.getPayment()) ? 0 : Float
						.parseFloat(rdsBaceraDrugDetectionModel.getPayment()));
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraDrugDetectionModel
								.getParagraphtime()));
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraDrugDetectionModel
								.getConfirm_date()));
				objects.add(rdsBaceraDrugDetectionModel.getDiscountPrice());
				objects.add(rdsBaceraDrugDetectionModel.getRemarks());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraDrugDetectionModel
								.getExpresstime()));
				objects.add(rdsBaceraDrugDetectionModel.getExpresstype());
				objects.add(rdsBaceraDrugDetectionModel.getExpressnum());
				objects.add(rdsBaceraDrugDetectionModel.getExpressremark());

				objects.add(rdsBaceraDrugDetectionModel.getAreaname());
				objects.add(rdsBaceraDrugDetectionModel.getOwnpersonname());
				objects.add(rdsBaceraDrugDetectionModel.getAgentname());
				objects.add(rdsBaceraDrugDetectionModel.getPartner());
				objects.add(rdsBaceraDrugDetectionModel.getInputperson());
				objects.add("1".equals(rdsBaceraDrugDetectionModel
						.getReportif()) ? "是" : "否");
				objects.add(rdsBaceraDrugDetectionModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "毒品检测");
		} else {
			// 导出excel头列表
			Object[] titles = { "案例编号", "委托日期", "委托单位","被鉴定人", "样品类型", "样本数", "鉴定事项","项目类型",
					"送检人", "快递日期", "快递类型", "快递单号", "快递备注", "归属地", "归属人", "代理商","合作方","登记人",
					"是否发报告", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraDrugDetectionModel rdsBaceraDrugDetectionModel = (RdsBaceraDrugDetectionModel) list
						.get(i);
				// 按照顺序对应表头
				objects.add(rdsBaceraDrugDetectionModel.getNum());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraDrugDetectionModel.getDate()));
				objects.add(rdsBaceraDrugDetectionModel.getEntrusted_unit());
				objects.add(rdsBaceraDrugDetectionModel.getName());
				objects.add(rdsBaceraDrugDetectionModel.getSample_type());
				objects.add(rdsBaceraDrugDetectionModel.getSample_count());
				objects.add(rdsBaceraDrugDetectionModel.getProgram());
				objects.add(rdsBaceraDrugDetectionModel.getProgram_type());
				objects.add(rdsBaceraDrugDetectionModel.getInspection());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraDrugDetectionModel
								.getExpresstime()));
				objects.add(rdsBaceraDrugDetectionModel.getExpresstype());
				objects.add(rdsBaceraDrugDetectionModel.getExpressnum());
				objects.add(rdsBaceraDrugDetectionModel.getExpressremark());
				objects.add(rdsBaceraDrugDetectionModel.getAreaname());
				objects.add(rdsBaceraDrugDetectionModel.getOwnpersonname());
				objects.add(rdsBaceraDrugDetectionModel.getAgentname());
				objects.add(rdsBaceraDrugDetectionModel.getPartner());
				objects.add(rdsBaceraDrugDetectionModel.getInputperson());
				objects.add("1".equals(rdsBaceraDrugDetectionModel
						.getReportif()) ? "是" : "否");
				objects.add(rdsBaceraDrugDetectionModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "毒品检测");
		}

	}

	@Override
	public List<RdsJudicialKeyValueModel> queryProgram() {
		return rdsBaceraDrugDetectionMapper.queryProgram();
	}

}
