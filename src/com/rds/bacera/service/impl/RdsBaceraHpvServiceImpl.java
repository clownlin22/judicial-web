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

import com.rds.bacera.mapper.RdsBaceraHpvMapper;
import com.rds.bacera.model.RdsBaceraHpvModel;
import com.rds.bacera.service.RdsBaceraHpvService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;

@Service("rdsBaceraHpvService")
public class RdsBaceraHpvServiceImpl implements RdsBaceraHpvService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	@Setter
	@Autowired
	private RdsBaceraHpvMapper rdsBaceraHpvMapper;

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
		result.put("total", rdsBaceraHpvMapper.queryAllCount(params));
		result.put("data", rdsBaceraHpvMapper.queryAllPage(params));
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
			return rdsBaceraHpvMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsBaceraHpvMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsBaceraHpvMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@Override
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map) {
		return rdsBaceraHpvMapper.queryNumExit(map);
	}

	/*
	 * export tumorSus
	 * 
	 * @see com.rds.upc.service.RdsTumorSusService#exportTumorSus(java.util.Map,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void exportHpv(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "HPV";
		List<Object> list = rdsBaceraHpvMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			Object[] titles = { "编号", "日期", "姓名", "性别", "年龄","项目类型", "应收款项", "所付款项",
					"到款时间","确认时间", "优惠价格", "财务备注", "快递日期", "快递类型", "快递单号", "归属地",
					"归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraHpvModel rdsHpvModel = (RdsBaceraHpvModel) list.get(i);
				objects.add(rdsHpvModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsHpvModel.getDate()));
				objects.add(rdsHpvModel.getName());
				objects.add(rdsHpvModel.getGender());
				objects.add(rdsHpvModel.getAge());
				objects.add(rdsHpvModel.getProgram());
				// 应收款项
				objects.add(("".equals(rdsHpvModel.getReceivables()) || null == rdsHpvModel
						.getReceivables()) ? 0 : Float.parseFloat(rdsHpvModel
						.getReceivables()));
				// 所付款项
				objects.add(("".equals(rdsHpvModel.getPayment()) || null == rdsHpvModel
						.getPayment()) ? 0 : Float.parseFloat(rdsHpvModel
						.getPayment()));
				objects.add(StringUtils.dateToChineseTen(rdsHpvModel
						.getParagraphtime()));
				objects.add(StringUtils.dateToChineseTen(rdsHpvModel
						.getConfirm_date()));
				objects.add(rdsHpvModel.getDiscountPrice());

				objects.add(rdsHpvModel.getRemarks());
				objects.add(StringUtils.dateToChineseTen(rdsHpvModel
						.getExpresstime()));
				objects.add(rdsHpvModel.getExpresstype());
				objects.add(rdsHpvModel.getExpressnum());
				objects.add(rdsHpvModel.getAreaname());
				objects.add(rdsHpvModel.getOwnpersonname());
				objects.add(rdsHpvModel.getAgentname());
				objects.add(rdsHpvModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "HPV");
		} else {
			Object[] titles = { "编号", "日期", "姓名", "性别", "年龄", "检测项目", "送检人",
					"电话", "快递日期", "快递类型", "快递单号", "归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraHpvModel rdsHpvModel = (RdsBaceraHpvModel) list.get(i);
				objects.add(rdsHpvModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsHpvModel.getDate()));
				objects.add(rdsHpvModel.getName());
				objects.add(rdsHpvModel.getGender());
				objects.add(rdsHpvModel.getAge());
				objects.add(StringUtils.dateToChineseTen(rdsHpvModel
						.getExpresstime()));
				objects.add(rdsHpvModel.getExpresstype());
				objects.add(rdsHpvModel.getExpressnum());
				objects.add(rdsHpvModel.getAreaname());
				objects.add(rdsHpvModel.getOwnpersonname());
				objects.add(rdsHpvModel.getAgentname());
				objects.add(rdsHpvModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "HPV");
		}

	}

}
