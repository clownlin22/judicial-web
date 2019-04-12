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

import com.rds.bacera.mapper.RdsBaceraBoneAgeMapper;
import com.rds.bacera.mapper.RdsBaceraTumorMarkerMapper;
import com.rds.bacera.model.RdsBaceraHpvModel;
import com.rds.bacera.model.RdsBaceraTumorMarkerModel;
import com.rds.bacera.service.RdsBaceraTumorMarkerService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;

@Service("rdsBaceraTumorMarkerService")
public class RdsBaceraTumorMarkerServiceImpl implements RdsBaceraTumorMarkerService{
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");
	
	@Setter
	@Autowired
	private RdsBaceraTumorMarkerMapper rdsBaceraTumorMarkerMapper;
	@Setter
	@Autowired
	private RdsBaceraBoneAgeMapper rdsBaceraBoneAgeMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsBaceraTumorMarkerMapper.queryAllCount(params));
		result.put("data", rdsBaceraTumorMarkerMapper.queryAllPage(params));
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
			return rdsBaceraTumorMarkerMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsBaceraTumorMarkerMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		try{
		rdsBaceraBoneAgeMapper.deleteExpress(params);
		rdsBaceraBoneAgeMapper.deleteFinance(params);
		return rdsBaceraTumorMarkerMapper.delete(params);
		}catch(Exception e){
			throw new Exception(e);
		}
		
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@Override
	public void exportTumor(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "肿瘤标志物";
		List<Object> list = rdsBaceraTumorMarkerMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			Object[] titles = { "编号", "日期", "姓名", "性别", "送检人","检测项目", "应收款项", "所付款项",
					"到款时间","确认时间", "优惠价格", "财务备注", "快递日期", "快递类型", "快递单号", 
					"归属人",  "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraTumorMarkerModel rdsTumorModel = (RdsBaceraTumorMarkerModel) list.get(i);
				objects.add(rdsTumorModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsTumorModel.getDate()));
				objects.add(rdsTumorModel.getName());
				objects.add(rdsTumorModel.getSex());
				objects.add(rdsTumorModel.getInspection());
				objects.add(rdsTumorModel.getProgram());
				// 应收款项
				objects.add(("".equals(rdsTumorModel.getReceivables()) || null == rdsTumorModel
						.getReceivables()) ? 0 : Float.parseFloat(rdsTumorModel
						.getReceivables()));
				// 所付款项
				objects.add(("".equals(rdsTumorModel.getPayment()) || null == rdsTumorModel
						.getPayment()) ? 0 : Float.parseFloat(rdsTumorModel
						.getPayment()));
				objects.add(StringUtils.dateToChineseTen(rdsTumorModel
						.getParagraphtime()));
				objects.add(StringUtils.dateToChineseTen(rdsTumorModel
						.getConfirm_date()));
				objects.add(rdsTumorModel.getDiscountPrice());

				objects.add(rdsTumorModel.getRemarks());
				objects.add(StringUtils.dateToChineseTen(rdsTumorModel
						.getExpresstime()));
				objects.add(rdsTumorModel.getExpresstype());
				objects.add(rdsTumorModel.getExpressnum());
				objects.add(rdsTumorModel.getOwnpersonname());
				objects.add(rdsTumorModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "肿瘤标志物");
		} else {
			Object[] titles = { "编号", "日期", "姓名", "性别", "送检人", "检测项目",
					 "快递日期", "快递类型", "快递单号", "归属人",  "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraTumorMarkerModel rdsTumorModel = (RdsBaceraTumorMarkerModel) list.get(i);
				objects.add(rdsTumorModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsTumorModel.getDate()));
				objects.add(rdsTumorModel.getName());
				objects.add(rdsTumorModel.getSex());
				objects.add(rdsTumorModel.getInspection());
				objects.add(rdsTumorModel.getProgram());
				objects.add(StringUtils.dateToChineseTen(rdsTumorModel
						.getExpresstime()));
				objects.add(rdsTumorModel.getExpresstype());
				objects.add(rdsTumorModel.getExpressnum());
				objects.add(rdsTumorModel.getOwnpersonname());
				objects.add(rdsTumorModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "肿瘤标志物");
		}

	}
	/**
	 * 判断是否存在
	 */
	@Override
	public boolean exsitCaseCode(Map<String, Object> params) {
		if (params.get("num") != null) {
			params.put("num", params.get("num").toString().trim());
			int result = rdsBaceraTumorMarkerMapper.exsitCaseCode(params);
			if (result == 0) {
				return true;
			}
		}
		return false;
	}
}