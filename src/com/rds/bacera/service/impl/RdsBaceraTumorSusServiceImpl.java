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

import com.rds.bacera.mapper.RdsBaceraTumorSusMapper;
import com.rds.bacera.model.RdsBaceraTumorSusModel;
import com.rds.bacera.service.RdsBaceraTumorSusService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;

@Service("rdsBaceraTumorSusService")
public class RdsBaceraTumorSusServiceImpl implements RdsBaceraTumorSusService {
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");
	@Setter
	@Autowired
	private RdsBaceraTumorSusMapper rdsTumorSusMapper;

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
		result.put("total", rdsTumorSusMapper.queryAllCount(params));
		result.put("data", rdsTumorSusMapper.queryAllPage(params));
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
			return rdsTumorSusMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsTumorSusMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsTumorSusMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map) {
		return rdsTumorSusMapper.queryNumExit(map);
	}

	/*
	 * export tumorSus
	 * 
	 * @see com.rds.upc.service.RdsTumorSusService#exportTumorSus(java.util.Map,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void exportTumorSus(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "肿瘤易感基因检测";
		List<Object> list = rdsTumorSusMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
		Object[] titles = { "编号", "日期", "姓名", "性别", "年龄", "检测项目", "送检人",
					"电话", "应收款项", "所付款项", "到款时间","确认时间",  "优惠价格", "财务备注", "快递日期", "快递类型",
					"快递单号", "归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraTumorSusModel rdsTumorSusModel = (RdsBaceraTumorSusModel) list
						.get(i);
				objects.add(rdsTumorSusModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsTumorSusModel
						.getDate()));
				objects.add(rdsTumorSusModel.getName());
				objects.add(rdsTumorSusModel.getGender());
				objects.add(rdsTumorSusModel.getAge());
				objects.add(rdsTumorSusModel.getTestitems());
				objects.add(rdsTumorSusModel.getCheckper());
				objects.add(rdsTumorSusModel.getPhonenum());
				//应收款项
				objects.add(("".equals(rdsTumorSusModel.getReceivables()) || null == rdsTumorSusModel
						.getReceivables()) ? 0 : Float.parseFloat(rdsTumorSusModel.getReceivables()));
				//所付款项
				objects.add(("".equals(rdsTumorSusModel.getPayment()) || null == rdsTumorSusModel
						.getPayment()) ? 0 : Float.parseFloat(rdsTumorSusModel.getPayment()));
				objects.add(StringUtils.dateToChineseTen(rdsTumorSusModel
						.getParagraphtime()));
				objects.add(StringUtils.dateToChineseTen(rdsTumorSusModel
						.getConfirm_date()));
				objects.add(rdsTumorSusModel.getDiscountPrice());

				objects.add(rdsTumorSusModel.getRemarks());
				objects.add(StringUtils.dateToChineseTen(rdsTumorSusModel
						.getExpresstime()));
				objects.add(rdsTumorSusModel.getExpresstype());
				objects.add(rdsTumorSusModel.getExpressnum());
				objects.add(rdsTumorSusModel.getAreaname());
				objects.add(rdsTumorSusModel.getOwnpersonname());
				objects.add(rdsTumorSusModel.getAgentname());
				objects.add(rdsTumorSusModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "肿瘤易感基因检测");
		} else {
			Object[] titles = { "编号", "日期", "姓名", "性别", "年龄", "检测项目", "送检人",
					"电话", "快递日期", "快递类型", "快递单号", "归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraTumorSusModel rdsTumorSusModel = (RdsBaceraTumorSusModel) list
						.get(i);
				objects.add(rdsTumorSusModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsTumorSusModel
						.getDate()));
				objects.add(rdsTumorSusModel.getName());
				objects.add(rdsTumorSusModel.getGender());
				objects.add(rdsTumorSusModel.getAge());
				objects.add(rdsTumorSusModel.getTestitems());
				objects.add(rdsTumorSusModel.getCheckper());
				objects.add(rdsTumorSusModel.getPhonenum());
				objects.add(StringUtils.dateToChineseTen(rdsTumorSusModel
						.getExpresstime()));
				objects.add(rdsTumorSusModel.getExpresstype());
				objects.add(rdsTumorSusModel.getExpressnum());
				objects.add(rdsTumorSusModel.getAreaname());
				objects.add(rdsTumorSusModel.getOwnpersonname());
				objects.add(rdsTumorSusModel.getAgentname());
				objects.add(rdsTumorSusModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "肿瘤易感基因检测");
		}

	}

}
