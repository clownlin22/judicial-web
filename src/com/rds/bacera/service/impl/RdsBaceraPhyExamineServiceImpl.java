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

import com.rds.bacera.mapper.RdsBaceraPhyExamineMapper;
import com.rds.bacera.model.RdsBaceraPhyExamineModel;
import com.rds.bacera.service.RdsBaceraPhyExamineService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;

@Service("rdsBaceraPhyExamineService")
public class RdsBaceraPhyExamineServiceImpl implements
		RdsBaceraPhyExamineService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	@Setter
	@Autowired
	private RdsBaceraPhyExamineMapper rdsPhyExamineMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return null;
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsPhyExamineMapper.queryAllCount(params));
		result.put("data", rdsPhyExamineMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return rdsPhyExamineMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		try {
			return rdsPhyExamineMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsPhyExamineMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsPhyExamineMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@Override
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map) {
		return rdsPhyExamineMapper.queryNumExit(map);
	}

	/*
	 * export ChildPCR
	 * 
	 * @see com.rds.upc.service.RdsTumorSusService#exportTumorSus(java.util.Map,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void exportPhyExamine(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "健康体检类";
		List<Object> list = rdsPhyExamineMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			Object[] titles = { "案例编号", "日期", "姓名", "性别", "套餐类型", "应收款项",
					"所付款项", "到款时间", "确认时间", "优惠价格", "财务备注", "快递日期", "快递类型",
					"快递单号", "快递备注", "归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraPhyExamineModel rdsPhyExamineModel = (RdsBaceraPhyExamineModel) list
						.get(i);
				objects.add(rdsPhyExamineModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsPhyExamineModel
						.getDate()));
				objects.add(rdsPhyExamineModel.getName());
				objects.add(rdsPhyExamineModel.getSex());
				objects.add(rdsPhyExamineModel.getModel_type());
				// 应收款项
				objects.add(("".equals(rdsPhyExamineModel.getReceivables()) || null == rdsPhyExamineModel
						.getReceivables()) ? 0 : Float
						.parseFloat(rdsPhyExamineModel.getReceivables()));
				// 所付款项
				objects.add(("".equals(rdsPhyExamineModel.getPayment()) || null == rdsPhyExamineModel
						.getPayment()) ? 0 : Float
						.parseFloat(rdsPhyExamineModel.getPayment()));
				objects.add(StringUtils.dateToChineseTen(rdsPhyExamineModel
						.getParagraphtime()));
				objects.add(StringUtils.dateToChineseTen(rdsPhyExamineModel
						.getConfirm_date()));
				objects.add(rdsPhyExamineModel.getDiscountPrice());
				objects.add(rdsPhyExamineModel.getFinance_remark());

				objects.add(StringUtils.dateToChineseTen(rdsPhyExamineModel
						.getExpresstime()));
				objects.add(rdsPhyExamineModel.getExpresstype());
				objects.add(rdsPhyExamineModel.getExpressnum());
				objects.add(rdsPhyExamineModel.getExpressremark());

				objects.add(rdsPhyExamineModel.getAreaname());
				objects.add(rdsPhyExamineModel.getOwnpersonname());
				objects.add(rdsPhyExamineModel.getAgentname());
				objects.add(rdsPhyExamineModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		} else {
			Object[] titles = { "案例编号", "日期", "姓名", "性别", "套餐类型", "快递日期",
					"快递类型", "快递单号", "快递备注", "归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraPhyExamineModel rdsPhyExamineModel = (RdsBaceraPhyExamineModel) list
						.get(i);
				objects.add(rdsPhyExamineModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsPhyExamineModel
						.getDate()));
				objects.add(rdsPhyExamineModel.getName());
				objects.add(rdsPhyExamineModel.getSex());
				objects.add(rdsPhyExamineModel.getModel_type());

				objects.add(StringUtils.dateToChineseTen(rdsPhyExamineModel
						.getExpresstime()));
				objects.add(rdsPhyExamineModel.getExpresstype());
				objects.add(rdsPhyExamineModel.getExpressnum());
				objects.add(rdsPhyExamineModel.getExpressremark());

				objects.add(rdsPhyExamineModel.getAreaname());
				objects.add(rdsPhyExamineModel.getOwnpersonname());
				objects.add(rdsPhyExamineModel.getAgentname());
				objects.add(rdsPhyExamineModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		}

	}

}
