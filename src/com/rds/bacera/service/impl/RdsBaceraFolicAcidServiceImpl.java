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

import com.rds.bacera.mapper.RdsBaceraFolicAcidMapper;
import com.rds.bacera.model.RdsBaceraFolicAcidModel;
import com.rds.bacera.service.RdsBaceraFolicAcidService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;

@Service("rdsBaceraFolicAcidService")
public class RdsBaceraFolicAcidServiceImpl implements RdsBaceraFolicAcidService {
	
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");
	
	@Setter
	@Autowired
	private RdsBaceraFolicAcidMapper rdsFolicAcidMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return 0;
//		return rdsUpcBlackListMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsFolicAcidMapper.queryAllCount(params));
		result.put("data", rdsFolicAcidMapper.queryAllPage(params));
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
			return rdsFolicAcidMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsFolicAcidMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsFolicAcidMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int queryNumExit(Map map) {
		return rdsFolicAcidMapper.queryNumExit(map);
	}

	/**
	 * 导出肿瘤个体
	 */
	@Override
	public void exportFolicAcid(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "叶酸代谢能力检测";
		List<Object> list = rdsFolicAcidMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			Object[] titles = { "编号", "日期", "姓名", "年龄","性别", "病理诊断", "检测项目", "送检人","送检医院",
					"电话", "应收款项", "所付款项", "到款时间", "确认时间",  "优惠价格","财务备注", "快递日期", "快递类型",
					"快递单号","快递备注", "归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraFolicAcidModel rdsFolicAcidModel = (RdsBaceraFolicAcidModel) list
						.get(i);
				objects.add(rdsFolicAcidModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsFolicAcidModel
						.getDate()));
				objects.add(rdsFolicAcidModel.getName());
				objects.add(rdsFolicAcidModel.getAge());
				objects.add(rdsFolicAcidModel.getSex());
				objects.add(rdsFolicAcidModel.getDiagnosis());
				objects.add(rdsFolicAcidModel.getTestitems());
				objects.add(rdsFolicAcidModel.getCheckper());
				objects.add(rdsFolicAcidModel.getHospital());
				objects.add(rdsFolicAcidModel.getPhonenum());
				// 应收款项
				objects.add(("".equals(rdsFolicAcidModel.getReceivables()) || null == rdsFolicAcidModel
						.getReceivables()) ? 0 : Float.parseFloat(rdsFolicAcidModel.getReceivables()));
				// 所付款项
				objects.add(("".equals(rdsFolicAcidModel.getPayment()) || null == rdsFolicAcidModel
						.getPayment()) ? 0 : Float.parseFloat(rdsFolicAcidModel
						.getPayment()));
				objects.add(StringUtils.dateToChineseTen(rdsFolicAcidModel
						.getParagraphtime()));
				objects.add(StringUtils.dateToChineseTen(rdsFolicAcidModel
						.getConfirm_date()));
				objects.add(rdsFolicAcidModel.getDiscountPrice());
				objects.add(rdsFolicAcidModel.getRemarks());
				objects.add(StringUtils.dateToChineseTen(rdsFolicAcidModel
						.getExpresstime()));
				objects.add(rdsFolicAcidModel.getExpresstype());
				objects.add(rdsFolicAcidModel.getExpressnum());
				objects.add(rdsFolicAcidModel.getExpressremark());
				objects.add(rdsFolicAcidModel.getAreaname());
				objects.add(rdsFolicAcidModel.getOwnpersonname());
				objects.add(rdsFolicAcidModel.getAgentname());
				objects.add(rdsFolicAcidModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "叶酸代谢能力检测");
		} else {
			Object[] titles = { "编号", "日期", "姓名", "年龄", "病理诊断", "检测项目", "送检人","送检医院",
					"电话", "快递日期", "快递类型", "快递单号","快递备注", "归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraFolicAcidModel rdsFolicAcidModel = (RdsBaceraFolicAcidModel) list
						.get(i);
				objects.add(rdsFolicAcidModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsFolicAcidModel
						.getDate()));
				objects.add(rdsFolicAcidModel.getName());
				objects.add(rdsFolicAcidModel.getAge());
				objects.add(rdsFolicAcidModel.getDiagnosis());
				objects.add(rdsFolicAcidModel.getTestitems());
				objects.add(rdsFolicAcidModel.getCheckper());
				objects.add(rdsFolicAcidModel.getHospital());
				objects.add(rdsFolicAcidModel.getPhonenum());
				objects.add(StringUtils.dateToChineseTen(rdsFolicAcidModel
						.getExpresstime()));
				objects.add(rdsFolicAcidModel.getExpresstype());
				objects.add(rdsFolicAcidModel.getExpressnum());
				objects.add(rdsFolicAcidModel.getExpressremark());
				objects.add(rdsFolicAcidModel.getAreaname());
				objects.add(rdsFolicAcidModel.getOwnpersonname());
				objects.add(rdsFolicAcidModel.getAgentname());
				objects.add(rdsFolicAcidModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "叶酸代谢能力检测");
		}

	}

}
