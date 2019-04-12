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

import com.rds.bacera.mapper.RdsBaceraSlimBeautyPCRMapper;
import com.rds.bacera.model.RdsBaceraTumorSusModel;
import com.rds.bacera.service.RdsBaceraSlimBeautyPCRService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;

@Service("rdsBaceraSlimBeautyPCRService")
public class RdsBaceraSlimBeautyPCRServiceImpl implements RdsBaceraSlimBeautyPCRService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	
	@Setter
	@Autowired
	private RdsBaceraSlimBeautyPCRMapper rdsSlimBeautyPCRMapper;

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
		result.put("total", rdsSlimBeautyPCRMapper.queryAllCount(params));
		result.put("data", rdsSlimBeautyPCRMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return rdsSlimBeautyPCRMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) throws Exception {
		try {
			return rdsSlimBeautyPCRMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsSlimBeautyPCRMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsSlimBeautyPCRMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map) {
		return rdsSlimBeautyPCRMapper.queryNumExit(map);
	}

	@Override
	public void exportSlimBeautyPCR(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "1".equals(params.get("case_type")) ? "减肥美容基因检测"
				: ("2".equals(params.get("case_type")) ? "个体特征基因检测" : (("3"
						.equals(params.get("case_type")) ? "重大慢病基因检测"
						: "减肥美容-个体特征-重大慢病")));
		List<Object> list = rdsSlimBeautyPCRMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
		Object[] titles = { "编号", "日期", "姓名", "性别", "年龄","电话",  "检测项目", "送检人","送检单位",
					"报告形式","接单平台","应收款项", "所付款项", "到款时间","确认时间", "优惠价格","财务备注", "快递日期", "快递类型",
					"快递单号","快递备注", "归属地", "归属人", "代理商", "备注和要求","案例类型" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraTumorSusModel rdsSlimBeautyPCRModel = (RdsBaceraTumorSusModel) list
						.get(i);
				objects.add(rdsSlimBeautyPCRModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsSlimBeautyPCRModel
						.getDate()));
				objects.add(rdsSlimBeautyPCRModel.getName());
				objects.add(rdsSlimBeautyPCRModel.getGender());
				objects.add(rdsSlimBeautyPCRModel.getAge());
				objects.add(rdsSlimBeautyPCRModel.getPhonenum());
				objects.add(rdsSlimBeautyPCRModel.getTestitems());
				objects.add(rdsSlimBeautyPCRModel.getCheckper());
				objects.add(rdsSlimBeautyPCRModel.getInspectionUnits());
				objects.add(rdsSlimBeautyPCRModel.getReportType());
				objects.add(rdsSlimBeautyPCRModel.getOrderPlatform());
				//应收款项
				objects.add(("".equals(rdsSlimBeautyPCRModel.getReceivables()) || null == rdsSlimBeautyPCRModel
						.getReceivables()) ? 0 : Float.parseFloat(rdsSlimBeautyPCRModel.getReceivables()));
				//所付款项
				objects.add(("".equals(rdsSlimBeautyPCRModel.getPayment()) || null == rdsSlimBeautyPCRModel
						.getPayment()) ? 0 : Float.parseFloat(rdsSlimBeautyPCRModel.getPayment()));
				objects.add(StringUtils.dateToChineseTen(rdsSlimBeautyPCRModel
						.getParagraphtime()));
				objects.add(StringUtils.dateToChineseTen(rdsSlimBeautyPCRModel
						.getConfirm_date()));
				objects.add(rdsSlimBeautyPCRModel.getDiscountPrice());

				objects.add(rdsSlimBeautyPCRModel.getRemarks());
				objects.add(StringUtils.dateToChineseTen(rdsSlimBeautyPCRModel
						.getExpresstime()));
				objects.add(rdsSlimBeautyPCRModel.getExpresstype());
				objects.add(rdsSlimBeautyPCRModel.getExpressnum());
				objects.add(rdsSlimBeautyPCRModel.getExpressremark());
				objects.add(rdsSlimBeautyPCRModel.getAreaname());
				objects.add(rdsSlimBeautyPCRModel.getOwnpersonname());
				objects.add(rdsSlimBeautyPCRModel.getAgentname());
				objects.add(rdsSlimBeautyPCRModel.getRemark());
				objects.add("1".equals(rdsSlimBeautyPCRModel.getCase_type()) ? "减肥美容基因检测"
						: ("2".equals(rdsSlimBeautyPCRModel.getCase_type()) ? "个体特征基因检测"
								: "重大慢病基因检测"));
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		} else {
			Object[] titles = { "编号", "日期", "姓名", "性别", "年龄","电话", "检测项目", "送检人","送检单位",
					"报告形式","接单平台","快递日期", "快递类型", "快递单号","快递备注", "归属地", "归属人", "代理商", "备注和要求","案例类型" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraTumorSusModel rdsSlimBeautyPCRModel = (RdsBaceraTumorSusModel) list
						.get(i);
				objects.add(rdsSlimBeautyPCRModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsSlimBeautyPCRModel
						.getDate()));
				objects.add(rdsSlimBeautyPCRModel.getName());
				objects.add(rdsSlimBeautyPCRModel.getGender());
				objects.add(rdsSlimBeautyPCRModel.getAge());
				objects.add(rdsSlimBeautyPCRModel.getPhonenum());
				objects.add(rdsSlimBeautyPCRModel.getTestitems());
				objects.add(rdsSlimBeautyPCRModel.getCheckper());
				objects.add(rdsSlimBeautyPCRModel.getInspectionUnits());
				objects.add(rdsSlimBeautyPCRModel.getReportType());
				objects.add(rdsSlimBeautyPCRModel.getOrderPlatform());
				objects.add(StringUtils.dateToChineseTen(rdsSlimBeautyPCRModel
						.getExpresstime()));
				objects.add(rdsSlimBeautyPCRModel.getExpresstype());
				objects.add(rdsSlimBeautyPCRModel.getExpressnum());
				objects.add(rdsSlimBeautyPCRModel.getExpressremark());
				objects.add(rdsSlimBeautyPCRModel.getAreaname());
				objects.add(rdsSlimBeautyPCRModel.getOwnpersonname());
				objects.add(rdsSlimBeautyPCRModel.getAgentname());
				objects.add(rdsSlimBeautyPCRModel.getRemark());
				objects.add("1".equals(rdsSlimBeautyPCRModel.getCase_type()) ? "减肥美容基因检测"
						: ("2".equals(rdsSlimBeautyPCRModel.getCase_type()) ? "个体特征基因检测"
								: "重大慢病基因检测"));
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		}

	}

}
