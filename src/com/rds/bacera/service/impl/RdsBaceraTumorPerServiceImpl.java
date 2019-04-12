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

import com.rds.bacera.mapper.RdsBaceraTumorPreMapper;
import com.rds.bacera.model.RdsBaceraTumorPerModel;
import com.rds.bacera.service.RdsBaceraTumorPerService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;

@Service("rdsBaceraTumorPreService")
public class RdsBaceraTumorPerServiceImpl implements RdsBaceraTumorPerService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");
	
	@Setter
	@Autowired
	private RdsBaceraTumorPreMapper rdsTumorPreMapper;

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
		result.put("total", rdsTumorPreMapper.queryAllCount(params));
		result.put("data", rdsTumorPreMapper.queryAllPage(params));
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
			return rdsTumorPreMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsTumorPreMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsTumorPreMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map) {
		return rdsTumorPreMapper.queryNumExit(map);
	}

	/**
	 * 导出肿瘤个体
	 */
	@Override
	public void exportTumorPer(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "肿瘤个体化登记";
		List<Object> list = rdsTumorPreMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
		Object[] titles = { "编号", "日期", "姓名", "年龄", "病理诊断", "检测项目", "送检人",
					"电话", "应收款项", "所付款项", "到款时间","确认时间","优惠价格", "财务备注", "快递日期", "快递类型",
					"快递单号","快递备注", "归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraTumorPerModel rdsTumorPerModel = (RdsBaceraTumorPerModel) list
						.get(i);
				objects.add(rdsTumorPerModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsTumorPerModel
						.getDate()));
				objects.add(rdsTumorPerModel.getName());
				objects.add(rdsTumorPerModel.getAge());
				objects.add(rdsTumorPerModel.getDiagnosis());
				objects.add(rdsTumorPerModel.getTestitems());
				objects.add(rdsTumorPerModel.getCheckper());
				objects.add(rdsTumorPerModel.getPhonenum());
				//应收款项
				objects.add(("".equals(rdsTumorPerModel.getReceivables()) || null == rdsTumorPerModel
						.getReceivables()) ? 0 : Float.parseFloat(rdsTumorPerModel.getReceivables()));
				//所付款项
				objects.add(("".equals(rdsTumorPerModel.getPayment()) || null == rdsTumorPerModel
						.getPayment()) ? 0 : Float.parseFloat(rdsTumorPerModel.getPayment()));
				objects.add(StringUtils.dateToChineseTen(rdsTumorPerModel
						.getParagraphtime()));
				objects.add(StringUtils.dateToChineseTen(rdsTumorPerModel
						.getConfirm_date()));
				objects.add(rdsTumorPerModel.getDiscountPrice());
				objects.add(rdsTumorPerModel.getRemarks());
				objects.add(StringUtils.dateToChineseTen(rdsTumorPerModel
						.getExpresstime()));
				objects.add(rdsTumorPerModel.getExpresstype());
				objects.add(rdsTumorPerModel.getExpressnum());
				objects.add(rdsTumorPerModel.getExpressremark());
				objects.add(rdsTumorPerModel.getAreaname());
				objects.add(rdsTumorPerModel.getOwnpersonname());
				objects.add(rdsTumorPerModel.getAgentname());
				objects.add(rdsTumorPerModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "肿瘤个体化登记");
		} else {
			Object[] titles = { "编号", "日期", "姓名", "年龄", "病理诊断", "检测项目", "送检人",
					"电话", "快递日期", "快递类型", "快递单号","快递备注", "归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraTumorPerModel rdsTumorPerModel = (RdsBaceraTumorPerModel) list
						.get(i);
				objects.add(rdsTumorPerModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsTumorPerModel
						.getDate()));
				objects.add(rdsTumorPerModel.getName());
				objects.add(rdsTumorPerModel.getAge());
				objects.add(rdsTumorPerModel.getDiagnosis());
				objects.add(rdsTumorPerModel.getTestitems());
				objects.add(rdsTumorPerModel.getCheckper());
				objects.add(rdsTumorPerModel.getPhonenum());
				objects.add(StringUtils.dateToChineseTen(rdsTumorPerModel
						.getExpresstime()));
				objects.add(rdsTumorPerModel.getExpresstype());
				objects.add(rdsTumorPerModel.getExpressnum());
				objects.add(rdsTumorPerModel.getExpressremark());
				objects.add(rdsTumorPerModel.getAreaname());
				objects.add(rdsTumorPerModel.getOwnpersonname());
				objects.add(rdsTumorPerModel.getAgentname());
				objects.add(rdsTumorPerModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "肿瘤个体化登记");
		}

	}

	@Override
	public Object queryTumorPerItems(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsTumorPreMapper.countTumorPerItems(params));
		result.put("data", rdsTumorPreMapper.queryTumorPerItems(params));
		return result;
	}

	@Override
	public int countTumorPerItems(Object params) throws Exception {
		return rdsTumorPreMapper.countTumorPerItems(params);
	}

	@Override
	public int saveTumorPerItems(Object params) throws Exception {
		return rdsTumorPreMapper.saveTumorPerItems(params);
	}

	@Override
	public int updateTumorPerItems(Object params) throws Exception {
		return rdsTumorPreMapper.updateTumorPerItems(params);
	}

	@Override
	public int deleteTumorPerItems(Object params) throws Exception {
		return rdsTumorPreMapper.deleteTumorPerItems(params);
	}

	@Override
	public Object queryTumorPerItemsAll(Object params) throws Exception {
		return rdsTumorPreMapper.queryTumorPerItems(params);
	}

}
