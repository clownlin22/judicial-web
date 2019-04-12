package com.rds.bacera.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.bacera.mapper.RdsBaceraDocAppraisalMapper;
import com.rds.bacera.model.RdsBaceraMedExamineModel;
import com.rds.bacera.service.RdsBaceraDocAppraisalService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.model.RdsJudicialKeyValueModel;

@Service("rdsBaceraDocAppraisalServiceImpl")
public class RdsBaceraDocAppraisalServiceImpl implements
		RdsBaceraDocAppraisalService {

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	@Setter
	@Autowired
	private RdsBaceraDocAppraisalMapper rdsBaceraDocAppraisalMapper;

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
		result.put("total", rdsBaceraDocAppraisalMapper.queryAllCount(params));
		result.put("data", rdsBaceraDocAppraisalMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int update(Object params) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", UUIDUtil.getUUID());
			map.put("program_name",
					(((Map<String, Object>) params).get("program").toString()
							.trim()));
			if (rdsBaceraDocAppraisalMapper.queryProgramCount(map) == 0) {
				rdsBaceraDocAppraisalMapper.insertProgram(map);
			}
			return rdsBaceraDocAppraisalMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int insert(Object params) throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", UUIDUtil.getUUID());
			map.put("program_name",
					(((Map<String, Object>) params).get("program").toString()
							.trim()));
			if (rdsBaceraDocAppraisalMapper.queryProgramCount(map) == 0) {
				rdsBaceraDocAppraisalMapper.insertProgram(map);
			}
			return rdsBaceraDocAppraisalMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		return rdsBaceraDocAppraisalMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int queryNumExit(Map map) {
		return rdsBaceraDocAppraisalMapper.queryNumExit(map);
	}

	@Override
	public void exportDocAppraisal(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "文书鉴定项";
		List<Object> list = rdsBaceraDocAppraisalMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			// 导出excel头列表
			Object[] titles = { "案例编号", "日期", "姓名", "项目", "应收款项", "所付款项",
					"到款时间", "确认时间", "优惠价格", "财务备注","快递日期", "快递类型", "快递单号",
					"快递备注", "归属地", "归属人", "代理商", "是否发报告", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraMedExamineModel rdsBaceraMedExamineModel = (RdsBaceraMedExamineModel) list
						.get(i);
				// 按照顺序对应表头
				objects.add(rdsBaceraMedExamineModel.getNum());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedExamineModel.getDate()));
				objects.add(rdsBaceraMedExamineModel.getName());
				objects.add(rdsBaceraMedExamineModel.getProgram());
				// 应收款项
				objects.add(("".equals(rdsBaceraMedExamineModel
						.getReceivables()) || null == rdsBaceraMedExamineModel
						.getReceivables()) ? 0 : Float
						.parseFloat(rdsBaceraMedExamineModel.getReceivables()));
				// 所付款项
				objects.add(("".equals(rdsBaceraMedExamineModel.getPayment()) || null == rdsBaceraMedExamineModel
						.getPayment()) ? 0 : Float
						.parseFloat(rdsBaceraMedExamineModel.getPayment()));
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedExamineModel
								.getParagraphtime()));
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedExamineModel
								.getConfirm_date()));
				objects.add(rdsBaceraMedExamineModel.getDiscountPrice());
				objects.add(rdsBaceraMedExamineModel.getRemarks());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedExamineModel
								.getExpresstime()));
				objects.add(rdsBaceraMedExamineModel.getExpresstype());
				objects.add(rdsBaceraMedExamineModel.getExpressnum());
				objects.add(rdsBaceraMedExamineModel.getExpressremark());

				objects.add(rdsBaceraMedExamineModel.getAreaname());
				objects.add(rdsBaceraMedExamineModel.getOwnpersonname());
				objects.add(rdsBaceraMedExamineModel.getAgentname());
				objects.add("1".equals(rdsBaceraMedExamineModel.getReportif()) ? "是"
						: "否");
				objects.add(rdsBaceraMedExamineModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "文书鉴定项");
		} else {
			// 导出excel头列表
			Object[] titles = { "案例编号", "日期", "姓名", "项目","应收款项", "快递日期", "快递类型",
					"快递单号", "快递备注", "归属地", "归属人", "代理商", "是否发报告", "备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraMedExamineModel rdsBaceraMedExamineModel = (RdsBaceraMedExamineModel) list
						.get(i);
				// 按照顺序对应表头
				objects.add(rdsBaceraMedExamineModel.getNum());
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedExamineModel.getDate()));
				objects.add(rdsBaceraMedExamineModel.getName());
				objects.add(rdsBaceraMedExamineModel.getProgram());
				// 应收款项
				objects.add(("".equals(rdsBaceraMedExamineModel
						.getReceivables()) || null == rdsBaceraMedExamineModel
						.getReceivables()) ? 0 : Float
						.parseFloat(rdsBaceraMedExamineModel.getReceivables()));
				objects.add(StringUtils
						.dateToChineseTen(rdsBaceraMedExamineModel
								.getExpresstime()));
				objects.add(rdsBaceraMedExamineModel.getExpresstype());
				objects.add(rdsBaceraMedExamineModel.getExpressnum());
				objects.add(rdsBaceraMedExamineModel.getExpressremark());

				objects.add(rdsBaceraMedExamineModel.getAreaname());
				objects.add(rdsBaceraMedExamineModel.getOwnpersonname());
				objects.add(rdsBaceraMedExamineModel.getAgentname());
				objects.add("1".equals(rdsBaceraMedExamineModel.getReportif()) ? "是"
						: "否");
				objects.add(rdsBaceraMedExamineModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "文书鉴定项");
		}

	}

	@Override
	public List<RdsJudicialKeyValueModel> queryProgram() {
		return rdsBaceraDocAppraisalMapper.queryProgram();
	}

	@Override
	public boolean insertFinanceTax(Map<String, Object> params) {
		String ids = params.get("ids").toString();
		String payments = params.get("payment").toString();
		String[] id = ids.split(",");
		String[] payment = payments.split(",");
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			for (int i = 0; i < id.length; i++) {
				//删除原来的
				rdsBaceraDocAppraisalMapper.deleteFinanceTax(id[i]);
				
				Double paymentSum = Double.parseDouble(payment[i]);
				// 增值税
				Double addTax = paymentSum - paymentSum / 1.03;
				BigDecimal addTaxTemp = new BigDecimal(addTax);
				addTax = addTaxTemp.setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
				// 附加税
				Double additionalTax = addTax * 0.115;
				BigDecimal additionalTaxTemp = new BigDecimal(additionalTax);
				additionalTax = additionalTaxTemp.setScale(2,
						BigDecimal.ROUND_HALF_UP).doubleValue();
				// 税后总额
				Double paymentAfterTax = paymentSum - addTax - additionalTax;
				BigDecimal paymentAfterTaxTemp = new BigDecimal(paymentAfterTax);
				paymentAfterTax = paymentAfterTaxTemp.setScale(2,
						BigDecimal.ROUND_HALF_UP).doubleValue();
				// 税后公司回款
				Double paymentSumAfterTax = paymentAfterTax * 0.7;
				BigDecimal paymentSumAfterTaxTemp = new BigDecimal(
						paymentSumAfterTax);
				paymentSumAfterTax = paymentSumAfterTaxTemp.setScale(2,
						BigDecimal.ROUND_HALF_UP).doubleValue();
				Map<String, Object> docAppraisal = new HashMap<>();
				docAppraisal.put("id", id[i]);
				docAppraisal.put("added_tax", addTax);
				docAppraisal.put("additional_tax", additionalTax);
				docAppraisal.put("total_aftertax", paymentAfterTax);
				docAppraisal.put("returnsum_aftertax", paymentSumAfterTax);
				docAppraisal.put("create_per", params.get("userid"));
				list.add(docAppraisal);
			}
			map.put("list", list);
			rdsBaceraDocAppraisalMapper.insertFinanceTax(map);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean updateFinanceTax(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return false;
	}

}
