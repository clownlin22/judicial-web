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

import com.rds.bacera.mapper.RdsBaceraSafeMedicationMapper;
import com.rds.bacera.model.RdsBaceraSafeMedicationModel;
import com.rds.bacera.service.RdsBaceraSafeMedicationService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;

@Service("rdsBaceraSafeMedicationService")
public class RdsBaceraSafeMedicationServiceImpl implements RdsBaceraSafeMedicationService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	@Setter
	@Autowired
	private RdsBaceraSafeMedicationMapper rdsSafeMedicationMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return 0;
		// return rdsUpcBlackListMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsSafeMedicationMapper.queryAllCount(params));
		result.put("data", rdsSafeMedicationMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Object params) throws Exception {
		try {
			return rdsSafeMedicationMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsSafeMedicationMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsSafeMedicationMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryAudltMaxNum(@SuppressWarnings("rawtypes") Map map) {
		return rdsSafeMedicationMapper.queryAudltMaxNum(map);
	}

	@Override
	public int queryChildMaxNum(@SuppressWarnings("rawtypes") Map map) {
		return rdsSafeMedicationMapper.queryChildMaxNum(map);
	}

	@Override
	public void exportSafeMedInfo(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "安全用药";
		List<Object> list = rdsSafeMedicationMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			Object[] titles = { "编号", "类型", "日期", "姓名", "检测项目", "应收款项", "所付款项",
					"到款时间","确认时间", "优惠价格", "财务备注", "快递日期", "快递类型",
					"快递单号","快递备注", "归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraSafeMedicationModel rdsSafeMedicationModel = (RdsBaceraSafeMedicationModel) list
						.get(i);
				objects.add(rdsSafeMedicationModel.getNum());
				objects.add("audlt".equals(rdsSafeMedicationModel.getType()) ? "成人"
						: "儿童");
				objects.add(StringUtils.dateToChineseTen(rdsSafeMedicationModel
						.getDate()));
				objects.add(rdsSafeMedicationModel.getName());
				objects.add(rdsSafeMedicationModel.getTestitems());
				// 应收款项
				objects.add(("".equals(rdsSafeMedicationModel.getReceivables()) || null == rdsSafeMedicationModel
						.getReceivables()) ? 0 : Float
						.parseFloat(rdsSafeMedicationModel.getReceivables()));
				// 所付款项
				objects.add(("".equals(rdsSafeMedicationModel.getPayment()) || null == rdsSafeMedicationModel
						.getPayment()) ? 0 : Float
						.parseFloat(rdsSafeMedicationModel.getPayment()));
				objects.add(StringUtils.dateToChineseTen(rdsSafeMedicationModel
						.getParagraphtime()));
				objects.add(StringUtils.dateToChineseTen(rdsSafeMedicationModel
						.getConfirm_date()));
				objects.add(rdsSafeMedicationModel.getDiscountPrice());
				objects.add(rdsSafeMedicationModel.getRemarks());
				objects.add(StringUtils.dateToChineseTen(rdsSafeMedicationModel
						.getExpresstime()));
				objects.add(rdsSafeMedicationModel.getExpresstype());
				objects.add(rdsSafeMedicationModel.getExpressnum());
				objects.add(rdsSafeMedicationModel.getExpressremark());
				objects.add(rdsSafeMedicationModel.getAreaname());
				objects.add(rdsSafeMedicationModel.getOwnpersonname());
				objects.add(rdsSafeMedicationModel.getAgentname());
				objects.add(rdsSafeMedicationModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "安全用药");
		} else {
			Object[] titles = { "编号", "类型", "日期", "姓名", "检测项目", "快递日期", "快递类型",
					"快递单号","快递备注", "归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraSafeMedicationModel rdsSafeMedicationModel = (RdsBaceraSafeMedicationModel) list
						.get(i);
				objects.add(rdsSafeMedicationModel.getNum());
				objects.add("audlt".equals(rdsSafeMedicationModel.getType()) ? "成人"
						: "儿童");
				objects.add(StringUtils.dateToChineseTen(rdsSafeMedicationModel
						.getDate()));
				objects.add(rdsSafeMedicationModel.getName());
				objects.add(rdsSafeMedicationModel.getTestitems());
				objects.add(StringUtils.dateToChineseTen(rdsSafeMedicationModel
						.getExpresstime()));
				objects.add(rdsSafeMedicationModel.getExpresstype());
				objects.add(rdsSafeMedicationModel.getExpressnum());
				objects.add(rdsSafeMedicationModel.getExpressremark());
				objects.add(rdsSafeMedicationModel.getAreaname());
				objects.add(rdsSafeMedicationModel.getOwnpersonname());
				objects.add(rdsSafeMedicationModel.getAgentname());
				objects.add(rdsSafeMedicationModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "安全用药");
		}

	}

}
