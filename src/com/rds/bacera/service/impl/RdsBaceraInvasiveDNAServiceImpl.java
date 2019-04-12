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

import com.rds.bacera.mapper.RdsBaceraInvasiveDNAMapper;
import com.rds.bacera.model.RdsBaceraBoneAgeFeeModel;
import com.rds.bacera.model.RdsBaceraInvasiveDNAModel;
import com.rds.bacera.service.RdsBaceraInvasiveDNAService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;

@Service("rdsBaceraInvasiveDNAService")
public class RdsBaceraInvasiveDNAServiceImpl implements RdsBaceraInvasiveDNAService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");
	
	@Setter
	@Autowired
	private RdsBaceraInvasiveDNAMapper rdsInvasiveDNAMapper;

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
		result.put("total", rdsInvasiveDNAMapper.queryAllCount(params));
		result.put("data", rdsInvasiveDNAMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return 0;
	}

	@Override
	public int update(Object params) throws Exception {
		try {
			return rdsInvasiveDNAMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsInvasiveDNAMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsInvasiveDNAMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int queryNumExit(Map map) {
		return rdsInvasiveDNAMapper.queryNumExit(map);
	}

	/**
	 * 导出无创dna信息
	 */
	@Override
	public void exportInvasiveInfo(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "无创DNA检测";
		List<Object> list = rdsInvasiveDNAMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			Object[] titles = { "案例编号", "受理日期", "父亲姓名", "母亲姓名", "父本类型","母本孕周", "委托日期",
					"归属地", "归属人", "代理商", "应收款项", "所付款项", "到款时间","确认时间", "优惠价格",
					"财务备注", "快递日期", "快递类型", "快递单号", "快递备注", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraInvasiveDNAModel rdsInvasiveDNAModel = (RdsBaceraInvasiveDNAModel) list
						.get(i);
				objects.add(rdsInvasiveDNAModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsInvasiveDNAModel
						.getDate()));
				objects.add(rdsInvasiveDNAModel.getFatherName());
				objects.add(rdsInvasiveDNAModel.getMotherName());
				objects.add(rdsInvasiveDNAModel.getFatherType());
				objects.add(rdsInvasiveDNAModel.getGestational());
				objects.add(StringUtils.dateToChineseTen(rdsInvasiveDNAModel
						.getConsigningDate()));
				objects.add(rdsInvasiveDNAModel.getAreaname());
				objects.add(rdsInvasiveDNAModel.getOwnpersonname());
				objects.add(rdsInvasiveDNAModel.getAgentname());
				// 应收款项
				objects.add(("".equals(rdsInvasiveDNAModel.getReceivables()) || null == rdsInvasiveDNAModel
						.getReceivables()) ? 0 : Float
						.parseFloat(rdsInvasiveDNAModel.getReceivables()));
				// 所付款项
				objects.add(("".equals(rdsInvasiveDNAModel.getPayment()) || null == rdsInvasiveDNAModel
						.getPayment()) ? 0 : Float
						.parseFloat(rdsInvasiveDNAModel.getPayment()));
				objects.add(StringUtils.dateToChineseTen(rdsInvasiveDNAModel
						.getParagraphtime()));
				objects.add(StringUtils.dateToChineseTen(rdsInvasiveDNAModel
						.getConfirm_date()));
				objects.add(rdsInvasiveDNAModel.getDiscountPrice());

				objects.add(rdsInvasiveDNAModel.getRemarks());
				objects.add(StringUtils.dateToChineseTen(rdsInvasiveDNAModel
						.getExpresstime()));
				objects.add(rdsInvasiveDNAModel.getExpresstype());
				objects.add(rdsInvasiveDNAModel.getExpressnum());
				objects.add(rdsInvasiveDNAModel.getExpressremark());
				objects.add(rdsInvasiveDNAModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "无创亲子鉴定");
		} else {
			Object[] titles = { "编号", "受理日期", "父亲姓名", "母亲姓名", "父本类型","母本孕周", "委托日期",
					"归属地", "归属人", "代理商", "快递日期", "快递类型", "快递单号", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraInvasiveDNAModel rdsInvasiveDNAModel = (RdsBaceraInvasiveDNAModel) list
						.get(i);
				objects.add(rdsInvasiveDNAModel.getNum());
				objects.add(StringUtils.dateToChineseTen(rdsInvasiveDNAModel
						.getDate()));
				objects.add(rdsInvasiveDNAModel.getFatherName());
				objects.add(rdsInvasiveDNAModel.getMotherName());
				objects.add(rdsInvasiveDNAModel.getFatherType());
				objects.add(rdsInvasiveDNAModel.getGestational());
				objects.add(StringUtils.dateToChineseTen(rdsInvasiveDNAModel
						.getConsigningDate()));
				objects.add(rdsInvasiveDNAModel.getAreaname());
				objects.add(rdsInvasiveDNAModel.getOwnpersonname());
				objects.add(rdsInvasiveDNAModel.getAgentname());
				objects.add(StringUtils.dateToChineseTen(rdsInvasiveDNAModel
						.getExpresstime()));
				objects.add(rdsInvasiveDNAModel.getExpresstype());
				objects.add(rdsInvasiveDNAModel.getExpressnum());
				objects.add(rdsInvasiveDNAModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "无创亲子鉴定");
		}

	}

	@Override
	public Object queryInvasiveDNAFee(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total",
				rdsInvasiveDNAMapper.queryInvasiveNDAFeeCount(params));
		result.put("data", rdsInvasiveDNAMapper.queryInvasiveNDAFee(params));
		return result;
	}

	@Override
	public int saveInvasiveDNAFee(Object params) throws Exception {
		return rdsInvasiveDNAMapper.saveInvasiveNDAFee(params);
	}

	@Override
	public int updateInvasiveDNAFee(Object params) throws Exception {
		return rdsInvasiveDNAMapper.updateInvasiveNDAFee(params);
	}

	@Override
	public int queryInvasiveDNAFeeCount(Object params) throws Exception {
		return rdsInvasiveDNAMapper.queryInvasiveNDAFeeCount(params);
	}

	@Override
	public int deleteInvasiveDNAFee(Object params) throws Exception {
		return rdsInvasiveDNAMapper.deleteInvasiveNDAFee(params);
	}

	@Override
	public RdsBaceraBoneAgeFeeModel queryInvasiveDNAFeeByRec(Object params)
			throws Exception {
		List<Object> list = rdsInvasiveDNAMapper
				.queryInvasiveNDAFeeByRec(params);
		if (list.size() > 0)
			return (RdsBaceraBoneAgeFeeModel) list.get(0);
		else
			return new RdsBaceraBoneAgeFeeModel();
	}

}
