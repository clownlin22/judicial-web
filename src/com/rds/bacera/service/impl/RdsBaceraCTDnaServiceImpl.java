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

import com.rds.bacera.mapper.RdsBaceraCTDnaMapper;
import com.rds.bacera.model.RdsBaceraCTDnaModel;
import com.rds.bacera.service.RdsBaceraCTDnaService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;

@Service("rdsBaceraCTDnaService")
public class RdsBaceraCTDnaServiceImpl implements RdsBaceraCTDnaService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");
	
	@Setter
	@Autowired
	private RdsBaceraCTDnaMapper rdsCTDnaMapper;

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
		result.put("total", rdsCTDnaMapper.queryAllCount(params));
		result.put("data", rdsCTDnaMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return rdsCTDnaMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		try {
			return rdsCTDnaMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsCTDnaMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsCTDnaMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@Override
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map) {
		return rdsCTDnaMapper.queryNumExit(map);
	}

	/*
	 * export ChildPCR
	 * 
	 * @see com.rds.upc.service.RdsTumorSusService#exportTumorSus(java.util.Map,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void exportCTDna(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "ctDna检测";
		List<Object> list = rdsCTDnaMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
		Object[] titles = { "案例编号", "日期", "姓名", "性别", "临床诊断","具体用药史和疗效","应收款项", "所付款项", "到款时间","确认时间", "优惠价格", 
				"财务备注", "快递日期", "快递类型","快递单号", "快递备注","归属地", "归属人", "代理商", "备注和要求"};
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraCTDnaModel rdsCTDnaModel = (RdsBaceraCTDnaModel) list
						.get(i);
				objects.add(rdsCTDnaModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsCTDnaModel
						.getDate()));
				objects.add(rdsCTDnaModel.getName());
				objects.add(rdsCTDnaModel.getSex());
				objects.add(rdsCTDnaModel.getClinical_diagnosis());
				objects.add(rdsCTDnaModel.getHistort_effect());
				//应收款项
				objects.add(("".equals(rdsCTDnaModel.getReceivables()) || null == rdsCTDnaModel
						.getReceivables()) ? 0 : Float.parseFloat(rdsCTDnaModel.getReceivables()));
				//所付款项
				objects.add(("".equals(rdsCTDnaModel.getPayment()) || null == rdsCTDnaModel
						.getPayment()) ? 0 : Float.parseFloat(rdsCTDnaModel.getPayment()));
				objects.add(StringUtils.dateToChineseTen(rdsCTDnaModel
						.getParagraphtime()));
				objects.add(StringUtils.dateToChineseTen(rdsCTDnaModel
						.getConfirm_date()));
				objects.add(rdsCTDnaModel.getDiscountPrice());
				objects.add(rdsCTDnaModel.getFinance_remark());
				
				objects.add(StringUtils.dateToChineseTen(rdsCTDnaModel
						.getExpresstime()));
				objects.add(rdsCTDnaModel.getExpresstype());
				objects.add(rdsCTDnaModel.getExpressnum());
				objects.add(rdsCTDnaModel.getExpressremark());
				
				objects.add(rdsCTDnaModel.getAreaname());
				objects.add(rdsCTDnaModel.getOwnpersonname());
				objects.add(rdsCTDnaModel.getAgentname());
				objects.add(rdsCTDnaModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		} else {
			Object[] titles = { "案例编号", "日期", "姓名", "性别", "临床诊断","具体用药史和疗效"
					,"快递日期", "快递类型", "快递单号","快递备注", "归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraCTDnaModel rdsCTDnaModel = (RdsBaceraCTDnaModel) list
						.get(i);
				objects.add(rdsCTDnaModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsCTDnaModel
						.getDate()));
				objects.add(rdsCTDnaModel.getName());
				objects.add(rdsCTDnaModel.getSex());
				objects.add(rdsCTDnaModel.getClinical_diagnosis());
				objects.add(rdsCTDnaModel.getHistort_effect());
				
				objects.add(StringUtils.dateToChineseTen(rdsCTDnaModel
						.getExpresstime()));
				objects.add(rdsCTDnaModel.getExpresstype());
				objects.add(rdsCTDnaModel.getExpressnum());
				objects.add(rdsCTDnaModel.getExpressremark());
				
				objects.add(rdsCTDnaModel.getAreaname());
				objects.add(rdsCTDnaModel.getOwnpersonname());
				objects.add(rdsCTDnaModel.getAgentname());
				objects.add(rdsCTDnaModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		}

	}

}
