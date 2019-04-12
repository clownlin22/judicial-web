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

import com.rds.bacera.mapper.RdsBaceraChildPCRMapper;
import com.rds.bacera.model.RdsBaceraChildPCRModel;
import com.rds.bacera.service.RdsBaceraChildPCRService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;

@Service("rdsBaceraChildPCRService")
public class RdsBaceraChildPCRServiceImpl implements RdsBaceraChildPCRService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");
	
	@Setter
	@Autowired
	private RdsBaceraChildPCRMapper rdsChildPCRMapper;

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
		result.put("total", rdsChildPCRMapper.queryAllCount(params));
		result.put("data", rdsChildPCRMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return rdsChildPCRMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		try {
			return rdsChildPCRMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsChildPCRMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsChildPCRMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map) {
		return rdsChildPCRMapper.queryNumExit(map);
	}

	/*
	 * export ChildPCR
	 * 
	 * @see com.rds.upc.service.RdsTumorSusService#exportTumorSus(java.util.Map,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void exportChildPCR(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "儿童天赋-健康基因检测 ";
		List<Object> list = rdsChildPCRMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
		Object[] titles = { "编号","案例条形码", "日期", "姓名", "性别", "年龄","母亲/父亲姓名","电话", "检测项目", "送检人","送检单位",
					"报告形式","接单平台","应收款项", "所付款项", "到款时间","确认时间", "优惠价格", "财务备注", "快递日期", "快递类型",
					"快递单号", "快递备注","归属地", "归属人", "代理商", "备注和要求","案例类型" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraChildPCRModel rdsChildPCRModel = (RdsBaceraChildPCRModel) list
						.get(i);
				objects.add(rdsChildPCRModel.getNum());
				objects.add(rdsChildPCRModel.getCodenum());
				objects.add(StringUtils.dateToChineseTen(rdsChildPCRModel
						.getDate()));
				objects.add(rdsChildPCRModel.getName());
				objects.add(rdsChildPCRModel.getGender());
				objects.add(rdsChildPCRModel.getAge());
				objects.add(rdsChildPCRModel.getFmname());
				objects.add(rdsChildPCRModel.getPhonenum());
				objects.add(rdsChildPCRModel.getTestitems());
				objects.add(rdsChildPCRModel.getCheckper());
				objects.add(rdsChildPCRModel.getInspectionUnits());
				objects.add(rdsChildPCRModel.getReportType());
				objects.add(rdsChildPCRModel.getOrderPlatform());
				//应收款项
				objects.add(("".equals(rdsChildPCRModel.getReceivables()) || null == rdsChildPCRModel
						.getReceivables()) ? 0 : Float.parseFloat(rdsChildPCRModel.getReceivables()));
				//所付款项
				objects.add(("".equals(rdsChildPCRModel.getPayment()) || null == rdsChildPCRModel
						.getPayment()) ? 0 : Float.parseFloat(rdsChildPCRModel.getPayment()));
				objects.add(StringUtils.dateToChineseTen(rdsChildPCRModel
						.getParagraphtime()));
				objects.add(StringUtils.dateToChineseTen(rdsChildPCRModel
						.getConfirm_date()));
				objects.add(rdsChildPCRModel.getDiscountPrice());

				objects.add(rdsChildPCRModel.getRemarks());
				objects.add(StringUtils.dateToChineseTen(rdsChildPCRModel
						.getExpresstime()));
				objects.add(rdsChildPCRModel.getExpresstype());
				objects.add(rdsChildPCRModel.getExpressnum());
				objects.add(rdsChildPCRModel.getExpressremark());
				objects.add(rdsChildPCRModel.getAreaname());
				objects.add(rdsChildPCRModel.getOwnpersonname());
				objects.add(rdsChildPCRModel.getAgentname());
				objects.add(rdsChildPCRModel.getRemark());
				objects.add("1".equals(rdsChildPCRModel.getCase_type()) ? "儿童健康基因检测" : "儿童天赋基因检测");
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		} else {
			Object[] titles = { "编号","案例条形码", "日期", "姓名", "性别", "年龄","母亲/父亲姓名","电话", "检测项目", "送检人","送检单位",
					"报告形式","接单平台","快递日期", "快递类型", "快递单号","快递备注", "归属地", "归属人", "代理商", "备注和要求","案例类型" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraChildPCRModel rdsChildPCRModel = (RdsBaceraChildPCRModel) list
						.get(i);
				objects.add(rdsChildPCRModel.getNum());
				objects.add(rdsChildPCRModel.getCodenum());
				objects.add(StringUtils.dateToChineseTen(rdsChildPCRModel
						.getDate()));
				objects.add(rdsChildPCRModel.getName());
				objects.add(rdsChildPCRModel.getGender());
				objects.add(rdsChildPCRModel.getAge());
				objects.add(rdsChildPCRModel.getFmname());
				objects.add(rdsChildPCRModel.getPhonenum());
				objects.add(rdsChildPCRModel.getTestitems());
				objects.add(rdsChildPCRModel.getCheckper());
				objects.add(rdsChildPCRModel.getInspectionUnits());
				objects.add(rdsChildPCRModel.getReportType());
				objects.add(rdsChildPCRModel.getOrderPlatform());
				objects.add(StringUtils.dateToChineseTen(rdsChildPCRModel
						.getExpresstime()));
				objects.add(rdsChildPCRModel.getExpresstype());
				objects.add(rdsChildPCRModel.getExpressnum());
				objects.add(rdsChildPCRModel.getExpressremark());
				objects.add(rdsChildPCRModel.getAreaname());
				objects.add(rdsChildPCRModel.getOwnpersonname());
				objects.add(rdsChildPCRModel.getAgentname());
				objects.add(rdsChildPCRModel.getRemark());
				objects.add("1".equals(rdsChildPCRModel.getCase_type()) ? "儿童健康基因检测" : "儿童天赋基因检测");
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		}

	}

}
