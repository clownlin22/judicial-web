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
import com.rds.bacera.model.RdsBaceraBoneAgeFeeModel;
import com.rds.bacera.model.RdsBaceraBoneAgeModel;
import com.rds.bacera.model.RdsBaceraFeeModel;
import com.rds.bacera.service.RdsBaceraBoneAgeService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;

@Service("rdsBaceraBoneAgeService")
public class RdsBaceraBoneAgeServiceImpl implements RdsBaceraBoneAgeService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	@Setter
	@Autowired
	private RdsBaceraBoneAgeMapper rdsBaceraBoneAgeMapper;

	@Override
	public Object queryAll(Object params) throws Exception {
		return 0;
		// return rdsUpcBlackListMapper.queryAll(params);
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsBaceraBoneAgeMapper.queryAllCount(params));
		result.put("data", rdsBaceraBoneAgeMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		return rdsBaceraBoneAgeMapper.queryAllCount(params);
	}

	@Override
	public int update(Object params) {
		try {
			return rdsBaceraBoneAgeMapper.update(params);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int insert(Object params) {
		try {
			return rdsBaceraBoneAgeMapper.insert(params);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsBaceraBoneAgeMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@Override
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map) {
		return rdsBaceraBoneAgeMapper.queryNumExit(map);
	}

	@Override
	public int insertFinance(Object params) throws Exception {
		return rdsBaceraBoneAgeMapper.insertFinance(params);
	}

	@Override
	public int updateFinance(Object params) throws Exception {
		return rdsBaceraBoneAgeMapper.updateFinance(params);
	}

	@Override
	public int queryFinanceExit(@SuppressWarnings("rawtypes") Map map) {
		return rdsBaceraBoneAgeMapper.queryFinanceExit(map);
	}

	@Override
	public int queryExpressExit(@SuppressWarnings("rawtypes") Map map) {
		return rdsBaceraBoneAgeMapper.queryExpressExit(map);
	}

	@Override
	public int insertExpress(Object params) throws Exception {
		return rdsBaceraBoneAgeMapper.insertExpress(params);
	}

	@Override
	public int updateExpress(Object params) throws Exception {
		return rdsBaceraBoneAgeMapper.updateExpress(params);
	}

	@Override
	public Object queryAgentByRece(Object params) throws Exception {
		return rdsBaceraBoneAgeMapper.queryAgentByRece(params);
	}

	@Override
	public int insertOAnum(Object params) throws Exception {
		return rdsBaceraBoneAgeMapper.insertOAnum(params);
	}

	/**
	 * 导出骨龄信息
	 */
	@Override
	public void exportBoneInfo(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "骨龄鉴定信息表";
		List<Object> list = rdsBaceraBoneAgeMapper.queryAllPage(params);
		// 特殊权限可以导出看到财务信息
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			Object[] titles = { "编号", "日期", "姓名", "应收款项", "所付款项", "到款时间",
					"确认时间", "财务备注", "优惠价格", "快递日期", "快递类型", "快递单号", "快递备注",
					"归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraBoneAgeModel boneAge = (RdsBaceraBoneAgeModel) list
						.get(i);
				objects.add(boneAge.getNum());
				objects.add(StringUtils.dateToChineseTen(boneAge.getDate()));
				objects.add(boneAge.getName());
				// 应收款项
				objects.add(("".equals(boneAge.getReceivables()) || null == boneAge
						.getReceivables()) ? 0 : Float.parseFloat(boneAge
						.getReceivables()));
				// 所付款项
				objects.add(("".equals(boneAge.getPayment()) || null == boneAge
						.getPayment()) ? 0 : Float.parseFloat(boneAge
						.getPayment()));
				objects.add(StringUtils.dateToChineseTen(boneAge
						.getParagraphtime()));
				objects.add(StringUtils.dateToChineseTen(boneAge
						.getConfirm_date()));
				objects.add(boneAge.getRemarks());
				objects.add(boneAge.getDiscountPrice());
				objects.add(StringUtils.dateToChineseTen(boneAge
						.getExpresstime()));
				objects.add(boneAge.getExpresstype());
				objects.add(boneAge.getExpressnum());
				objects.add(boneAge.getExpressremark());
				objects.add(boneAge.getAreaname());
				objects.add(boneAge.getOwnpersonname());
				objects.add(boneAge.getAgentname());
				objects.add(boneAge.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "骨龄鉴定信息表");
		} else {
			Object[] titles = { "编号", "日期", "姓名", "快递日期", "快递类型", "快递单号",
					"快递备注", "归属地", "归属人", "代理商", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraBoneAgeModel boneAge = (RdsBaceraBoneAgeModel) list
						.get(i);
				objects.add(boneAge.getNum());
				objects.add(StringUtils.dateToChineseTen(boneAge.getDate()));
				objects.add(boneAge.getName());
				objects.add(StringUtils.dateToChineseTen(boneAge
						.getExpresstime()));
				objects.add(boneAge.getExpresstype());
				objects.add(boneAge.getExpressnum());
				objects.add(boneAge.getExpressremark());
				objects.add(boneAge.getAreaname());
				objects.add(boneAge.getOwnpersonname());
				objects.add(boneAge.getAgentname());
				objects.add(boneAge.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "骨龄鉴定信息表");
		}

	}

	@Override
	public Object queryBoneFee(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsBaceraBoneAgeMapper.queryBoneFeeCount(params));
		result.put("data", rdsBaceraBoneAgeMapper.queryBoneFee(params));
		return result;
	}

	@Override
	public int saveBoneFee(Object params) throws Exception {
		return rdsBaceraBoneAgeMapper.saveBoneFee(params);
	}

	@Override
	public int updateBoneFee(Object params) throws Exception {
		return rdsBaceraBoneAgeMapper.updateBoneFee(params);
	}

	@Override
	public int queryBoneFeeCount(Object params) throws Exception {
		return rdsBaceraBoneAgeMapper.queryBoneFeeCount(params);
	}

	@Override
	public int deleteBoneFee(Object params) throws Exception {
		return rdsBaceraBoneAgeMapper.deleteBoneFee(params);
	}

	@Override
	public RdsBaceraBoneAgeFeeModel queryBoneFeeByRec(Object params)
			throws Exception {
		List<Object> list = rdsBaceraBoneAgeMapper.queryBoneFeeByRec(params);
		if (list.size() > 0)
			return (RdsBaceraBoneAgeFeeModel) list.get(0);
		else
			return new RdsBaceraBoneAgeFeeModel();
	}

	@Override
	public boolean caseFeeConfirm(Map<String, Object> params) {
		return rdsBaceraBoneAgeMapper.caseFeeConfirm(params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean insertFinanceList(Map<String, Object> params)
			throws Exception {
		List<RdsBaceraFeeModel> list = new ArrayList<>();
		List<String> insertList = (List<String>) params.get("insertList");
		List<String> receivablesInsertList = (List<String>) params
				.get("receivablesInsertList");
		List<String> numInsertList = (List<String>) params.get("numInsertList");
		for (int i = 0; i < insertList.size(); i++) {
			RdsBaceraFeeModel feeModel = new RdsBaceraFeeModel();
			feeModel.setId(insertList.get(i));
			feeModel.setNum(numInsertList.get(i));
			feeModel.setReceivables(receivablesInsertList.get(i));
			feeModel.setPaid(receivablesInsertList.get(i));
			feeModel.setPayment(receivablesInsertList.get(i));
			feeModel.setAccount_type(params.get("account_type").toString());
			feeModel.setCase_type(params.get("case_type").toString());
			feeModel.setDiscountPrice("0");
			feeModel.setParagraphtime(params.get("paragraphtime").toString());
			feeModel.setRemarks(params.get("remarks").toString());
			feeModel.setRemittanceDate(params.get("remittanceDate").toString());
			feeModel.setRemittanceName(params.get("remittanceName").toString());
			list.add(feeModel);
		}
		if (rdsBaceraBoneAgeMapper.insertFinanceList(list))
			return true;
		else
			return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean updateFinanceList(Map<String, Object> params)
			throws Exception {
		List<String> list = (List<String>) params.get("updateList");
		params.put("ids", list);
		if (rdsBaceraBoneAgeMapper.updateFinanceList(params))
			return true;
		else
			return false;
	}

	@Override
	public boolean confirmFinanceList(Map<String, Object> params)
			throws Exception {
		if (rdsBaceraBoneAgeMapper.confirmFinanceList(params))
			return true;
		else
			return false;
	}
}
