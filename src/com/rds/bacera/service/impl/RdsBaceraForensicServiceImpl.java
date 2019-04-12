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

import com.rds.bacera.mapper.RdsBaceraForensicMapper;
import com.rds.bacera.model.RdsBaceraForensicModel;
import com.rds.bacera.service.RdsBaceraForensicService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;

@Service("rdsBaceraForensicService")
public class RdsBaceraForensicServiceImpl implements RdsBaceraForensicService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	@Setter
	@Autowired
	private RdsBaceraForensicMapper rdsForensicMapper;

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
		result.put("total", rdsForensicMapper.queryAllCount(params));
		result.put("data", rdsForensicMapper.queryAllPage(params));
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
			return rdsForensicMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsForensicMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsForensicMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@Override
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map) {
		return rdsForensicMapper.queryNumExit(map);
	}

	/**
	 * 导出骨龄信息
	 */
	@Override
	public void exportForensicInfo(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "法医病理";
		List<Object> list = rdsForensicMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			Object[] titles = { "编号", "姓名", "委托人", "受案时间", "应收款项", "所付款项",
					"到款时间","确认时间", "优惠价格",  "财务备注", "快递日期", "快递类型",
					"快递单号","快递备注", "委托事项", "备注和要求", "归属地", "归属人", "代理商" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraForensicModel forensic = (RdsBaceraForensicModel) list.get(i);
				objects.add(forensic.getNum());
				objects.add(forensic.getName());
				objects.add(forensic.getClient());
				objects.add(StringUtils.dateToChineseTen(forensic.getDate()));
				// 应收款项
				objects.add(("".equals(forensic.getReceivables()) || null == forensic
						.getReceivables()) ? 0 : Float.parseFloat(forensic
						.getReceivables()));
				// 所付款项
				objects.add(("".equals(forensic.getPayment()) || null == forensic
						.getPayment()) ? 0 : Float.parseFloat(forensic
						.getPayment()));
				objects.add(StringUtils.dateToChineseTen(forensic
						.getParagraphtime()));
				objects.add(StringUtils.dateToChineseTen(forensic
						.getConfirm_date()));
				objects.add(forensic.getDiscountPrice());
				objects.add(forensic.getRemarks());
				objects.add(StringUtils.dateToChineseTen(forensic
						.getExpresstime()));
				objects.add(forensic.getExpresstype());
				objects.add(forensic.getExpressnum());
				objects.add(forensic.getExpressremark());
				objects.add(forensic.getCommitment());
				objects.add(forensic.getRemark());
				objects.add(forensic.getAreaname());
				objects.add(forensic.getOwnpersonname());
				objects.add(forensic.getAgentname());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "法医病理");
		} else {
			Object[] titles = { "编号", "姓名", "委托人", "受案时间", "快递日期", "快递类型",
					"快递单号","快递备注", "委托事项", "备注和要求", "归属地", "归属人", "代理商" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraForensicModel forensic = (RdsBaceraForensicModel) list.get(i);
				objects.add(forensic.getNum());
				objects.add(forensic.getName());
				objects.add(forensic.getClient());
				objects.add(StringUtils.dateToChineseTen(forensic.getDate()));
				objects.add(StringUtils.dateToChineseTen(forensic
						.getExpresstime()));
				objects.add(forensic.getExpresstype());
				objects.add(forensic.getExpressnum());
				objects.add(forensic.getExpressremark());
				objects.add(forensic.getCommitment());
				objects.add(forensic.getRemark());
				objects.add(forensic.getAreaname());
				objects.add(forensic.getOwnpersonname());
				objects.add(forensic.getAgentname());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "法医病理");
		}

	}
}
