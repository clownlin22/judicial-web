package com.rds.bacera.service.impl;

/**
 * @author yuanxiaobo
 */
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.bacera.mapper.RdsBaceraIdentifyMapper;
import com.rds.bacera.model.RdsBaceraIdentifyInfoModel;
import com.rds.bacera.service.RdsBaceraIdentifyService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.JScriptInvoke;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.finance.mapper.RdsFinanceChargeStandardMapper;
import com.rds.finance.model.RdsFinanceChargeStandardModel;
import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleInfoModel;

@Service("RdsBaceraIdentifyService")
@Transactional
public class RdsBaceraIdentifyServiceImpl implements RdsBaceraIdentifyService {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");

	@Setter
	@Autowired
	private RdsJudicialRegisterMapper RdsJudicialRegisterMapper;

	@Setter
	@Autowired
	private RdsBaceraIdentifyMapper rdsIdentifyMapper;

	@Setter
	@Autowired
	private RdsFinanceChargeStandardMapper rdsFinanceChargeStandardMapper;

	/**
	 * 根据条件获取案例的基本信息
	 */
	@Override
	public Object getCaseInfo(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", rdsIdentifyMapper.countCaseInfo(params));
		result.put("data", rdsIdentifyMapper.queryCaseInfo(params));
		return result;
	}

	/**
	 * 删除案例的信息
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public boolean deleteCaseInfo(Map<String, Object> params) {

		int result = rdsIdentifyMapper.deleteCaseInfo(params);
		if (result > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 删除样本的信息
	 */
	public int deleteSampleInfo(Map<String, Object> params) {
		return rdsIdentifyMapper.deleteSampleInfo(params);
	}

	/**
	 * 获取案例样本的信息
	 */
	@Override
	public RdsJudicialResponse getSampleInfo(Map<String, Object> params) {
		RdsJudicialResponse resJudicialResponse = new RdsJudicialResponse();
		List<RdsJudicialSampleInfoModel> sampleInfoModels = rdsIdentifyMapper
				.getSampleInfo(params.get("case_id"));
		resJudicialResponse.setItems(sampleInfoModels);
		return resJudicialResponse;
	}

	/**
	 * 保存案例信息
	 */
	@Override
	public boolean saveCaseInfo(Map<String, Object> params) {
		// 查看案例编号是否存在
		if (exsitCaseCode(params)) {
			// 样本类型
			List<String> sample_types = getValues(params.get("sample_type") == null ? ""
					: params.get("sample_type"));
			// 样本姓名
			List<String> sample_usernames = getValues(params
					.get("sample_username"));
			// 样本身份证号
			List<String> id_numbers = getValues(params.get("id_number") == null ? ""
					: params.get("id_number"));
			// 样本称谓
			List<String> sample_calls = getValues(params.get("sample_call"));
			// 样本出生日期
			List<String> birth_dates = getValues(params.get("birth_date") == null ? ""
					: params.get("birth_date"));
			// 样本条形码
			List<String> sample_code = getValues(params.get("sample_code") == null ? ""
					: params.get("sample_code"));
			// 保存实体
			RdsBaceraIdentifyInfoModel caseInfoModel = new RdsBaceraIdentifyInfoModel(params
					.get("case_id").toString(), params.get("case_code").toString()
					.trim(), params.get("case_areacode").toString(), params.get(
					"receiver_id").toString(), params.get("phone").toString()
					.trim(), params.get("accept_time").toString(), params.get(
					"case_in_per").toString(), params.get("remark").toString()
					.trim(), params.get("client").toString().trim(), params
					.get("type").toString().trim(), params.get("entrustment_time")
					.toString().trim(), params.get("entrustment_matter").toString()
					.trim(), params.get("case_type").toString(), params.get(
					"typeid").toString());
			if (sample_usernames.size() > 0) {
				caseInfoModel.setSample_in_time(DATE_FORMAT.format(new Date()));
			}
			int case_result = addCaseInfo(params,caseInfoModel);

			/** 保存财务报表 start **/
//			try
//			{
//				params.put("financeflag", true);
//				addCaseFinanceInfo(params);
//			}catch(Exception e)
//			{
//				e.printStackTrace();
//				return false;
//			}
			/** 保存财务报表 end **/

			/** 我是分割线，上面是插入案例信息，下面是样本信息 **/
			if (case_result > 0) {
				// 基本信息保存成功后操作
				List<RdsJudicialSampleInfoModel> sampleInfoModels = new ArrayList<RdsJudicialSampleInfoModel>();
				for (int i = 0; i < sample_usernames.size(); i++) {
					if (sample_usernames.get(0).length() > 0) {
						RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
								UUIDUtil.getUUID(), sample_code.get(i).trim(),
								sample_types.get(i), sample_calls.get(i),
								sample_usernames.get(i), id_numbers.get(i),
								birth_dates.get(i), caseInfoModel.getCase_id());
						sampleInfoModels.add(sampleInfoModel);
					}
				}
				// 插入样本信息
				for (RdsJudicialSampleInfoModel model : sampleInfoModels) {
					rdsIdentifyMapper.insertSampleInfo(model);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 修改案例信息
	 */
	@Override
	public boolean updateCaseInfo(Map<String, Object> params) {
		// 获取需要修改的样本信息，需要排序 按照样本条形码
		List<String> sample_types = getValues(params.get("sample_type") == null ? ""
				: params.get("sample_type"));
		List<String> sample_usernames = getValues(params.get("sample_username"));
		List<String> id_numbers = getValues(params.get("id_number") == null ? ""
				: params.get("id_number"));
		List<String> sample_calls = getValues(params.get("sample_call"));
		List<String> birth_dates = getValues(params.get("birth_date") == null ? ""
				: params.get("birth_date"));
		// 样本条形码
		List<String> sample_code = getValues(params.get("sample_code") == null ? ""
				: params.get("sample_code"));
		// 修改保存实体
		RdsBaceraIdentifyInfoModel caseInfoModel = new RdsBaceraIdentifyInfoModel(params
				.get("case_id").toString(), params.get("case_code").toString()
				.trim(), params.get("case_areacode").toString(), params.get(
				"receiver_id").toString(), params.get("phone").toString()
				.trim(), params.get("accept_time").toString(), params.get(
				"case_in_per").toString(), params.get("remark").toString()
				.trim(), params.get("client").toString().trim(), params
				.get("type").toString().trim(), params.get("entrustment_time")
				.toString().trim(), params.get("entrustment_matter").toString()
				.trim(), params.get("case_type").toString(), params.get(
				"typeid").toString());
		// 存在样本时保存样本时间
		if (sample_usernames.size() > 0) {
			String sample_in_date = DATE_FORMAT.format(new Date());
			caseInfoModel.setSample_in_time(sample_in_date);
		}
		// 更新方法
		int case_result = updateCaseInfo(params, caseInfoModel);

		/** 更新财务报表 start **/
//		try
//		{
//			params.put("financeflag", false);
//			addCaseFinanceInfo(params);
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//			return false;
//		}
		/** 更新财务报表 end **/

		if (case_result > 0) {
			// 更新成功，先删除原来的样本
			deleteSampleInfo(params);
			// 获取样本信息
			List<RdsJudicialSampleInfoModel> sampleInfoModels = new ArrayList<RdsJudicialSampleInfoModel>();
			for (int i = 0; i < sample_usernames.size(); i++) {
				if (sample_usernames.get(0).length() > 0) {
					RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
							UUIDUtil.getUUID(), sample_code.get(i).trim(),
							sample_types.get(i), sample_calls.get(i),
							sample_usernames.get(i), id_numbers.get(i),
							birth_dates.get(i), caseInfoModel.getCase_id());
					sampleInfoModels.add(sampleInfoModel);
				}
			}
			// 插入样本信息
			for (RdsJudicialSampleInfoModel model : sampleInfoModels) {
				rdsIdentifyMapper.insertSampleInfo(model);
			}
			return true;
		}
		return false;
	}

	public static List<String> getValues(Object object) {
		List<String> values = new ArrayList<String>();
		if (object != null) {
			String str = object.toString();
			String[] objects = str.split(",");
			if (objects.length > 1) {
				str = str.substring(1, str.length() - 1);
				String[] objs = str.split(",");
				for (String s : objs) {
					values.add(s.trim());
				}
			} else {
				values.add(str.trim());
			}
		}
		return values;
	}

	/**
	 * 判断是否存在
	 */
	@Override
	public boolean exsitCaseCode(Map<String, Object> params) {
		if (params.get("case_code") != null) {
			params.put("case_code", params.get("case_code").toString().trim());
			int result = rdsIdentifyMapper.exsitCaseCode(params);
			if (result == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否存在
	 */
	@Override
	public boolean exsitSampleCode(Map<String, Object> params) {
		int result = rdsIdentifyMapper.exsitSampleCode(params);
		if (result > 0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean verifyId_Number(Map<String, Object> params) {
		Pattern p = Pattern.compile("[0-9]+");
		if (params.get("id_number") != null) {
			String id_number = params.get("id_number").toString();
			int count = rdsIdentifyMapper.exsitBlackNumber(id_number);
			if (count > 0) {
				return false;
			} else {
				Matcher m = p.matcher(id_number.substring(0,
						id_number.length() - 1));
				boolean flg = m.matches();
				if (flg) {
					if (id_number.length() == 18) {
						char c = com.rds.code.utils.StringUtils
								.getValidateCode(id_number.substring(0,
										id_number.length() - 1));
						if (id_number.endsWith(String.valueOf(c))) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	@Override
	public void exportIdentifyInfo(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = ("".equals(params.get("type").toString()) || "null"
				.equals(params.get("type").toString())) ? "亲子鉴定" : params.get(
				"type").toString();

		List<RdsBaceraIdentifyInfoModel> list = rdsIdentifyMapper
				.queryCaseInfo(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			Object[] titles = { "编号", "日期", "父母亲", "身份证", "孩子", "出生日期", "应收款项",
					"所付款项", "到款时间", "是否检验", "所属分公司", "员工", "优惠价格",  "财务备注", "地址", "电话",
					"样本数量", "送样本时间", "快递日期", "快递类型","快递单号", "收件人", "快递备注","案例备注和要求", "案例类型" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraIdentifyInfoModel rdsIdentifyInfoModel = (RdsBaceraIdentifyInfoModel) list
						.get(i);
				objects.add(rdsIdentifyInfoModel.getCase_code());
				objects.add(com.rds.code.utils.StringUtils
						.dateToChineseTen(rdsIdentifyInfoModel.getAccept_time()));
				objects.add(rdsIdentifyInfoModel.getFandm());
				objects.add(rdsIdentifyInfoModel.getId_card());
				objects.add(rdsIdentifyInfoModel.getChild());
				String birth = rdsIdentifyInfoModel.getBirth_date() == null ? ""
						: rdsIdentifyInfoModel.getBirth_date();
//				if (!"".equals(birth_date)) {
//					String[] births = birth_date.split(";");
//					for (String string : births) {
//						birth += com.rds.code.utils.StringUtils
//								.dateToChineseTen(string) + ";";
//					}
//				}
				objects.add(birth);
				// 应收款项
				objects.add(("".equals(rdsIdentifyInfoModel.getReal_sum()) || null == rdsIdentifyInfoModel
						.getReal_sum()) ? 0 : rdsIdentifyInfoModel.getReal_sum());
				// 所付款项
				objects.add(("".equals(rdsIdentifyInfoModel.getReturn_sum()) || null == rdsIdentifyInfoModel
						.getReturn_sum()) ? 0 : rdsIdentifyInfoModel.getReturn_sum());
				// 到款时间
				objects.add(com.rds.code.utils.StringUtils
						.dateToChineseTen(rdsIdentifyInfoModel.getParagraphtime()));
				objects.add("");
				String ownCompany = (null == rdsIdentifyInfoModel
						.getReceiver_area() ? "" : rdsIdentifyInfoModel
						.getReceiver_area())
						+ (("".equals(rdsIdentifyInfoModel.getAgentname()) || null == rdsIdentifyInfoModel
								.getAgentname()) ? "-"
								+ rdsIdentifyInfoModel.getCase_receiver() : "-"
								+ rdsIdentifyInfoModel.getAgentname());
				// 所属分公司
				objects.add(ownCompany.startsWith("-") ? ownCompany.substring(
						1, ownCompany.length()) : ownCompany);
				// 员工
				objects.add(rdsIdentifyInfoModel.getCase_receiver());

				objects.add(rdsIdentifyInfoModel.getDiscountPrice());
				objects.add(rdsIdentifyInfoModel.getFinance_remark());

				objects.add(rdsIdentifyInfoModel.getCase_areacode());
				objects.add(rdsIdentifyInfoModel.getPhone());
				objects.add(rdsIdentifyInfoModel.getSample_count());
				objects.add(com.rds.code.utils.StringUtils
						.dateToChineseTen(rdsIdentifyInfoModel
								.getSample_in_time()));
				objects.add(com.rds.code.utils.StringUtils
						.dateToChineseTen(rdsIdentifyInfoModel.getExpresstime()));
				objects.add(rdsIdentifyInfoModel.getExpresstype());
				objects.add(rdsIdentifyInfoModel.getExpressnum());
				objects.add(rdsIdentifyInfoModel.getRecive());
				objects.add(rdsIdentifyInfoModel.getExpressremark());
				objects.add(rdsIdentifyInfoModel.getRemark());
				objects.add(rdsIdentifyInfoModel.getType());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		} else {
			Object[] titles = { "编号", "日期", "父母亲", "身份证", "孩子", "出生日期", "应收款项",
					"所付款项", "到款时间", "是否检验", "所属分公司", "员工", "地址", "电话", "样本数量",
					"送样本时间", "快递日期", "快递类型", "快递单号", "收件人","快递备注", "备注和要求", "案例类型" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraIdentifyInfoModel rdsIdentifyInfoModel = (RdsBaceraIdentifyInfoModel) list
						.get(i);
				objects.add(rdsIdentifyInfoModel.getCase_code());
				objects.add(com.rds.code.utils.StringUtils
						.dateToChineseTen(rdsIdentifyInfoModel.getAccept_time()));
				objects.add(rdsIdentifyInfoModel.getFandm());
				objects.add(rdsIdentifyInfoModel.getId_card());
				objects.add(rdsIdentifyInfoModel.getChild());
				String birth = rdsIdentifyInfoModel.getBirth_date() == null ? ""
						: rdsIdentifyInfoModel.getBirth_date();
//				if (!"".equals(birth_date)) {
//					String[] births = birth_date.split(";");
//					for (String string : births) {
//						birth += com.rds.code.utils.StringUtils
//								.dateToChineseTen(string) + ";";
//					}
//				}
				objects.add(birth);
				objects.add("");
				objects.add("");
				objects.add("");
				objects.add("");
				String ownCompany = (null == rdsIdentifyInfoModel
						.getReceiver_area() ? "" : rdsIdentifyInfoModel
						.getReceiver_area())
						+ (("".equals(rdsIdentifyInfoModel.getAgentname()) || null == rdsIdentifyInfoModel
								.getAgentname()) ? "-"
								+ rdsIdentifyInfoModel.getCase_receiver() : "-"
								+ rdsIdentifyInfoModel.getAgentname());
				// 所属分公司
				objects.add(ownCompany.startsWith("-") ? ownCompany.substring(
						1, ownCompany.length()) : ownCompany);
				// 员工
				objects.add(rdsIdentifyInfoModel.getCase_receiver());
				// 地址
				objects.add(rdsIdentifyInfoModel.getCase_areacode());
				objects.add(rdsIdentifyInfoModel.getPhone());
				objects.add(rdsIdentifyInfoModel.getSample_count());
				objects.add(com.rds.code.utils.StringUtils
						.dateToChineseTen(rdsIdentifyInfoModel
								.getSample_in_time()));
				objects.add(com.rds.code.utils.StringUtils
						.dateToChineseTen(rdsIdentifyInfoModel.getExpresstime()));
				objects.add(rdsIdentifyInfoModel.getExpresstype());
				objects.add(rdsIdentifyInfoModel.getExpressnum());
				objects.add(rdsIdentifyInfoModel.getRecive());
				objects.add(rdsIdentifyInfoModel.getExpressremark());
				objects.add(rdsIdentifyInfoModel.getRemark());
				objects.add(rdsIdentifyInfoModel.getType());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		}

	}

	@Override
	public void exportIdentifyInfoQM(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = params.get("type").toString();
		List<RdsBaceraIdentifyInfoModel> list = rdsIdentifyMapper
				.queryCaseInfo(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			Object[] titles = { "编号", "委托日期", "委托事项", "受理日期", "孩子", "被鉴定人",
					"人数", "应收款项", "所付款项", "到款时间", "优惠价格",  "财务备注",
					"是否检验", "所属分公司", "员工", "快递日期", "快递类型", "快递单号", "收件人","快递备注",
					"备注和要求", "案例类型" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraIdentifyInfoModel rdsIdentifyInfoModel = (RdsBaceraIdentifyInfoModel) list
						.get(i);
				objects.add(rdsIdentifyInfoModel.getCase_code());
				objects.add(com.rds.code.utils.StringUtils
						.dateToChineseTen(rdsIdentifyInfoModel
								.getEntrustment_time()));
				objects.add(rdsIdentifyInfoModel.getEntrustment_matter());
				objects.add(com.rds.code.utils.StringUtils
						.dateToChineseTen(rdsIdentifyInfoModel.getAccept_time()));
				objects.add(rdsIdentifyInfoModel.getFandm() + ","
						+ rdsIdentifyInfoModel.getChild());
				objects.add(rdsIdentifyInfoModel.getSample_count());
				// 应收款项
				objects.add(("".equals(rdsIdentifyInfoModel.getReal_sum()) || null == rdsIdentifyInfoModel
						.getReal_sum()) ? 0 : rdsIdentifyInfoModel.getReal_sum());
				// 所付款项
				objects.add(("".equals(rdsIdentifyInfoModel.getReturn_sum()) || null == rdsIdentifyInfoModel
						.getReturn_sum()) ? 0 : rdsIdentifyInfoModel.getReturn_sum());
				// 到款时间
				objects.add(com.rds.code.utils.StringUtils
						.dateToChineseTen(rdsIdentifyInfoModel.getParagraphtime()));
				objects.add(rdsIdentifyInfoModel.getDiscountPrice());

				objects.add(rdsIdentifyInfoModel.getFinance_remark());
				objects.add("");
				String ownCompany = (null == rdsIdentifyInfoModel
						.getReceiver_area() ? "" : rdsIdentifyInfoModel
						.getReceiver_area())
						+ (("".equals(rdsIdentifyInfoModel.getAgentname()) || null == rdsIdentifyInfoModel
								.getAgentname()) ? "-"
								+ rdsIdentifyInfoModel.getCase_receiver() : "-"
								+ rdsIdentifyInfoModel.getAgentname());
				// 所属分公司
				objects.add(ownCompany.startsWith("-") ? ownCompany.substring(
						1, ownCompany.length()) : ownCompany);
				// 员工
				objects.add(rdsIdentifyInfoModel.getCase_receiver());
				objects.add(com.rds.code.utils.StringUtils
						.dateToChineseTen(rdsIdentifyInfoModel.getExpresstime()));
				objects.add(rdsIdentifyInfoModel.getExpresstype());
				objects.add(rdsIdentifyInfoModel.getExpressnum());
				objects.add(rdsIdentifyInfoModel.getRecive());
				objects.add(rdsIdentifyInfoModel.getExpressremark());
				objects.add(rdsIdentifyInfoModel.getRemark());
				objects.add(rdsIdentifyInfoModel.getType());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		} else {
			Object[] titles = { "编号", "委托日期", "委托事项", "受理日期", "孩子", "被鉴定人",
					"人数", "应收款项", "所付款项", "到款时间", "是否检验", "所属分公司", "员工",
					"快递日期", "快递类型", "快递单号", "收件人","快递备注", "备注和要求", "类型" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraIdentifyInfoModel rdsIdentifyInfoModel = (RdsBaceraIdentifyInfoModel) list
						.get(i);
				objects.add(rdsIdentifyInfoModel.getCase_code());
				objects.add(com.rds.code.utils.StringUtils
						.dateToChineseTen(rdsIdentifyInfoModel
								.getEntrustment_time()));
				objects.add(rdsIdentifyInfoModel.getEntrustment_matter());
				objects.add(com.rds.code.utils.StringUtils
						.dateToChineseTen(rdsIdentifyInfoModel.getAccept_time()));
				objects.add(rdsIdentifyInfoModel.getFandm() + ","
						+ rdsIdentifyInfoModel.getChild());
				objects.add(rdsIdentifyInfoModel.getSample_count());
				objects.add("");
				objects.add("");
				objects.add("");
				objects.add("");
				String ownCompany = (null == rdsIdentifyInfoModel
						.getReceiver_area() ? "" : rdsIdentifyInfoModel
						.getReceiver_area())
						+ (("".equals(rdsIdentifyInfoModel.getAgentname()) || null == rdsIdentifyInfoModel
								.getAgentname()) ? "-"
								+ rdsIdentifyInfoModel.getCase_receiver() : "-"
								+ rdsIdentifyInfoModel.getAgentname());
				// 所属分公司
				objects.add(ownCompany.startsWith("-") ? ownCompany.substring(
						1, ownCompany.length()) : ownCompany);
				// 员工
				objects.add(rdsIdentifyInfoModel.getCase_receiver());
				objects.add(com.rds.code.utils.StringUtils
						.dateToChineseTen(rdsIdentifyInfoModel.getExpresstime()));
				objects.add(rdsIdentifyInfoModel.getExpresstype());
				objects.add(rdsIdentifyInfoModel.getExpressnum());
				objects.add(rdsIdentifyInfoModel.getRecive());
				objects.add(rdsIdentifyInfoModel.getExpressremark());
				objects.add(rdsIdentifyInfoModel.getRemark());
				objects.add(rdsIdentifyInfoModel.getType());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, filename);
		}

	}

	@Override
	public Map<String, Object> getStandFee(Integer typeid, Integer pernum,
			String receiver_id, Integer case_type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("receiver_id", receiver_id);
		map.put("case_type", case_type);
		try {
			List<RdsFinanceChargeStandardModel> equation = rdsFinanceChargeStandardMapper
					.getEquation(map);
			if (equation.size() > 0) {
				String script = equation.get(0).getEquation()
						.replaceAll("\n", "");
				Double standFee;
				standFee = JScriptInvoke.getStandardFee(script, pernum, typeid);
				map.clear();
				map.put("standFee", standFee == null ? 0 : standFee);
//				map.put("discountRate", equation.get(0).getDiscountrate());
				map.put("success", true);
			} else {
				map.put("standFee", "");
				map.put("discountRate", "");
				map.put("success", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("standFee", "");
			map.put("discountRate", "");
			map.put("success", false);
		}
		return map;
	}

	/**
	 * 案例样财务报表信息插入
	 * 
	 * @param params
	 */
	private int addCaseInfo(Map<String, Object> params,RdsBaceraIdentifyInfoModel caseInfoModel) {
		/** 案例数据封装 start **/
		List<String> sample_types = getValues(params.get("sample_type") == null ? ""
				: params.get("sample_type"));
		// 样本姓名
		List<String> sample_usernames = getValues(params.get("sample_username"));
		// 样本身份证号
		List<String> id_numbers = getValues(params.get("id_number") == null ? ""
				: params.get("id_number"));
		// 样本称谓
		List<String> sample_calls = getValues(params.get("sample_call"));
		// 样本出生日期
		List<String> birth_dates = getValues(params.get("birth_date") == null ? ""
				: params.get("birth_date"));
		// 样本条形码
		List<String> sample_code = getValues(params.get("sample_code") == null ? ""
				: params.get("sample_code"));
		String fandm = "";
		String child = "";
		String id_card = "";
		String birth_date = "";
		int sample_count = 0;
		sample_count = sample_usernames.size();
		// 父母亲，孩子排序
		List<RdsJudicialSampleInfoModel> sampleInfoFM = new ArrayList<RdsJudicialSampleInfoModel>();
		List<RdsJudicialSampleInfoModel> sampleInfoC = new ArrayList<RdsJudicialSampleInfoModel>();
		for (int i = 0; i < sample_count; i++) {
			String sample_call = sample_calls.get(i);
			RdsJudicialKeyValueModel modelCall = RdsJudicialRegisterMapper
					.getSampleCallKey(sample_call);
			RdsJudicialKeyValueModel sample_type = RdsJudicialRegisterMapper
					.getSampleCallKey(sample_types.get(i));
			// 判断哪些在父母亲那一栏
			if ("1".equals(modelCall.getCallstatus())) {
				RdsJudicialSampleInfoModel fm = new RdsJudicialSampleInfoModel();
				fm.setSample_call(modelCall == null ? "" : modelCall.getValue());
				fm.setSample_username(sample_usernames.get(i));
				fm.setSample_type(sample_type == null ? "" : sample_type
						.getValue());
				fm.setSample_code(sample_code.get(i));
				fm.setBirth_date(birth_dates.get(i));
				fm.setId_number(id_numbers.get(i));
				sampleInfoFM.add(fm);
			} else {
				RdsJudicialSampleInfoModel fm = new RdsJudicialSampleInfoModel();
				fm.setSample_call(modelCall == null ? "" : modelCall.getValue());
				fm.setSample_username(sample_usernames.get(i));
				fm.setSample_type(sample_type == null ? "" : sample_type
						.getValue());
				fm.setSample_code(sample_code.get(i));
				fm.setBirth_date(birth_dates.get(i));
				fm.setId_number(id_numbers.get(i));
				sampleInfoC.add(fm);
			}
		}
		// 父母亲样本排序
		if (sampleInfoFM.size() > 1) {
			for (int i = 0; i < sampleInfoFM.size() - 1; i++) {
				for (int j = i + 1; j < sampleInfoFM.size(); j++) {
					if ((sampleInfoFM.get(i).getSample_code().substring(
							sampleInfoFM.get(i).getSample_code().length() - 1,
							sampleInfoFM.get(i).getSample_code().length()))
							.compareTo((sampleInfoFM.get(j).getSample_code()
									.substring(sampleInfoFM.get(j)
											.getSample_code().length() - 1,
											sampleInfoFM.get(j)
													.getSample_code().length()))) > 0) {
						RdsJudicialSampleInfoModel temp = new RdsJudicialSampleInfoModel();
						temp = sampleInfoFM.get(i);
						sampleInfoFM.set(i, sampleInfoFM.get(j));
						sampleInfoFM.set(j, temp);
					}
				}
			}
		}
		// 孩子样本排序
		if (sampleInfoC.size() > 1) {
			for (int i = 0; i < sampleInfoC.size() - 1; i++) {
				for (int j = i + 1; j < sampleInfoC.size(); j++) {
					if ((sampleInfoC.get(i).getSample_code().substring(
							sampleInfoC.get(i).getSample_code().length() - 1,
							sampleInfoC.get(i).getSample_code().length()))
							.compareTo((sampleInfoC.get(j).getSample_code()
									.substring(sampleInfoC.get(j)
											.getSample_code().length() - 1,
											sampleInfoC.get(j).getSample_code()
													.length()))) > 0) {
						RdsJudicialSampleInfoModel temp = new RdsJudicialSampleInfoModel();
						temp = sampleInfoC.get(i);
						sampleInfoC.set(i, sampleInfoC.get(j));
						sampleInfoC.set(j, temp);
					}
				}
			}
		}
		for (RdsJudicialSampleInfoModel model : sampleInfoFM) {
			fandm += model.getSample_call() + "-" + model.getSample_username()
					+ "-" + model.getSample_type() + ";";
			id_card += (null == model.getId_number() || "".equals(model
					.getId_number())) ? "" : (model.getId_number() + ";");
			try {
				birth_date += (null == model.getBirth_date() || "".equals(model
						.getBirth_date())) ? ""
						: (com.rds.code.utils.StringUtils
								.dateToChineseTen(model.getBirth_date()) + ";");
			} catch (Exception e) {
				System.out.println("identifyRegitster----------------string to date exception!!");
				e.printStackTrace();
			}
		}
		for (RdsJudicialSampleInfoModel model : sampleInfoC) {
			child += model.getSample_call() + "-" + model.getSample_username()
					+ "-" + model.getSample_type() + ";";
			id_card += (null == model.getId_number() || "".equals(model
					.getId_number())) ? "" : (model.getId_number() + ";");
			try {
				birth_date += (null == model.getBirth_date() || "".equals(model
						.getBirth_date())) ? ""
						: (com.rds.code.utils.StringUtils
								.dateToChineseTen(model.getBirth_date()) + ";");
			} catch (Exception e) {
				System.out.println("identifyRegitster----------------string to date exception!!");
				e.printStackTrace();
			}
		}
		/** 案例数据封装 end **/
		
		caseInfoModel.setFandm(fandm);
		caseInfoModel.setChild(child);
		caseInfoModel.setId_card(id_card);
		caseInfoModel.setBirth_date(birth_date);
		/** 保存案例信息 start **/
		return rdsIdentifyMapper.insertCaseInfo(caseInfoModel);
		/** 保存案例信息 end **/
		
		/** 保存财务报表 start **/
		
		// 保存财务报表map
//		Map<String, Object> map = new HashMap<String, Object>();
//		// 案例信息
//		map.put("case_id", params.get("case_id"));
//		map.put("case_code", params.get("case_code"));
//		// 案例归属（包括归属地，归属人，代理等信息）
//		map.put("charge_id", params.get("receiver_id"));
//		// 委托人
//		map.put("case_client", params.get("client"));
//		// 样本父母亲姓名
//		map.put("case_sample_fm",
//				fandm != "" ? fandm.substring(0, fandm.length() - 1) : "");
//		// 样本孩子姓名
//		map.put("case_sample_child",
//				child != "" ? child.substring(0, child.length() - 1) : "");
//		// 样本身份证信息
//		map.put("case_sample_idCard",
//				(!id_card.equals("")) ? id_card.substring(0,
//						id_card.length() - 1) : "");
//		// 样本出生日期信息
//		map.put("case_sample_birthDate",
//				(!birth_date.equals("")) ? birth_date.substring(0,
//						birth_date.length() - 1) : "");
//		// 样本数量
//		map.put("case_sample_count", sample_count);
//		// 案例备注
//		map.put("case_remark", params.get("remark"));
//		// 案例受理时间
//		map.put("case_accepttime", params.get("accept_time"));
//		// 案例子类型（求实，中信，唯实等...）
//		map.put("finance_type", params.get("type"));
//		// 案例类型，这里为亲子鉴定
//		map.put("case_type", params.get("case_type"));
//		// 操作人员
//		map.put("update_user", params.get("case_in_per"));
//		// 案例财务信息
//		Map<String, Object> result = this.getStandFee(
//				Integer.parseInt(params.get("typeid").toString()),
//				sample_count, params.get("receiver_id").toString(),
//				Integer.valueOf(params.get("case_type").toString()));
//		if (Boolean.valueOf(result.get("success").toString())) {
//			map.put("stand_sum",result.get("standFee").toString());
//			map.put("real_sum",result.get("standFee").toString());
//		} else {
//			map.put("stand_sum", 0);
//			map.put("real_sum", 0);
//		}
//		// 新增财务信息
//		try {
//			if (Boolean.parseBoolean(params.get("financeflag").toString())) {
//				map.put("finance_id", UUIDUtil.getUUID());
//				rdsFinanceChargeStandardMapper.insertFinanceInfo(map);
//			} else {
//				rdsFinanceChargeStandardMapper.updateFinanceInfo(map);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		/** 保存财务报表 end **/
	}
	
	/**
	 * 案例样财务报表信息插入
	 * 
	 * @param params
	 */
	private int updateCaseInfo(Map<String, Object> params,RdsBaceraIdentifyInfoModel caseInfoModel) {
		/** 案例数据封装 start **/
		List<String> sample_types = getValues(params.get("sample_type") == null ? ""
				: params.get("sample_type"));
		// 样本姓名
		List<String> sample_usernames = getValues(params.get("sample_username"));
		// 样本身份证号
		List<String> id_numbers = getValues(params.get("id_number") == null ? ""
				: params.get("id_number"));
		// 样本称谓
		List<String> sample_calls = getValues(params.get("sample_call"));
		// 样本出生日期
		List<String> birth_dates = getValues(params.get("birth_date") == null ? ""
				: params.get("birth_date"));
		// 样本条形码
		List<String> sample_code = getValues(params.get("sample_code") == null ? ""
				: params.get("sample_code"));
		String fandm = "";
		String child = "";
		String id_card = "";
		String birth_date = "";
		int sample_count = 0;
		sample_count = sample_usernames.size();
		// 父母亲，孩子排序
		List<RdsJudicialSampleInfoModel> sampleInfoFM = new ArrayList<RdsJudicialSampleInfoModel>();
		List<RdsJudicialSampleInfoModel> sampleInfoC = new ArrayList<RdsJudicialSampleInfoModel>();
		for (int i = 0; i < sample_count; i++) {
			String sample_call = sample_calls.get(i);
			RdsJudicialKeyValueModel modelCall = RdsJudicialRegisterMapper
					.getSampleCallKey(sample_call);
			RdsJudicialKeyValueModel sample_type = RdsJudicialRegisterMapper
					.getSampleCallKey(sample_types.get(i));
			// 判断哪些在父母亲那一栏
			if ("1".equals(modelCall.getCallstatus())) {
				RdsJudicialSampleInfoModel fm = new RdsJudicialSampleInfoModel();
				fm.setSample_call(modelCall == null ? "" : modelCall.getValue());
				fm.setSample_username(sample_usernames.get(i));
				fm.setSample_type(sample_type == null ? "" : sample_type
						.getValue());
				fm.setSample_code(sample_code.get(i));
				fm.setBirth_date(birth_dates.get(i));
				fm.setId_number(id_numbers.get(i));
				sampleInfoFM.add(fm);
			} else {
				RdsJudicialSampleInfoModel fm = new RdsJudicialSampleInfoModel();
				fm.setSample_call(modelCall == null ? "" : modelCall.getValue());
				fm.setSample_username(sample_usernames.get(i));
				fm.setSample_type(sample_type == null ? "" : sample_type
						.getValue());
				fm.setSample_code(sample_code.get(i));
				fm.setBirth_date(birth_dates.get(i));
				fm.setId_number(id_numbers.get(i));
				sampleInfoC.add(fm);
			}
		}
		// 父母亲样本排序
		if (sampleInfoFM.size() > 1) {
			for (int i = 0; i < sampleInfoFM.size() - 1; i++) {
				for (int j = i + 1; j < sampleInfoFM.size(); j++) {
					if ((sampleInfoFM.get(i).getSample_code().substring(
							sampleInfoFM.get(i).getSample_code().length() - 1,
							sampleInfoFM.get(i).getSample_code().length()))
							.compareTo((sampleInfoFM.get(j).getSample_code()
									.substring(sampleInfoFM.get(j)
											.getSample_code().length() - 1,
											sampleInfoFM.get(j)
													.getSample_code().length()))) > 0) {
						RdsJudicialSampleInfoModel temp = new RdsJudicialSampleInfoModel();
						temp = sampleInfoFM.get(i);
						sampleInfoFM.set(i, sampleInfoFM.get(j));
						sampleInfoFM.set(j, temp);
					}
				}
			}
		}
		// 孩子样本排序
		if (sampleInfoC.size() > 1) {
			for (int i = 0; i < sampleInfoC.size() - 1; i++) {
				for (int j = i + 1; j < sampleInfoC.size(); j++) {
					if ((sampleInfoC.get(i).getSample_code().substring(
							sampleInfoC.get(i).getSample_code().length() - 1,
							sampleInfoC.get(i).getSample_code().length()))
							.compareTo((sampleInfoC.get(j).getSample_code()
									.substring(sampleInfoC.get(j)
											.getSample_code().length() - 1,
											sampleInfoC.get(j).getSample_code()
													.length()))) > 0) {
						RdsJudicialSampleInfoModel temp = new RdsJudicialSampleInfoModel();
						temp = sampleInfoC.get(i);
						sampleInfoC.set(i, sampleInfoC.get(j));
						sampleInfoC.set(j, temp);
					}
				}
			}
		}
		for (RdsJudicialSampleInfoModel model : sampleInfoFM) {
			fandm += model.getSample_call() + "-" + model.getSample_username()
					+ "-" + model.getSample_type() + ";";
			id_card += (null == model.getId_number() || "".equals(model
					.getId_number())) ? "" : (model.getId_number() + ";");
			try {
				birth_date += (null == model.getBirth_date() || "".equals(model
						.getBirth_date())) ? ""
						: (com.rds.code.utils.StringUtils
								.dateToChineseTen(model.getBirth_date()) + ";");
			} catch (Exception e) {
				System.out.println("identifyRegitster----------------string to date exception!!");
				e.printStackTrace();
			}
		}
		for (RdsJudicialSampleInfoModel model : sampleInfoC) {
			child += model.getSample_call() + "-" + model.getSample_username()
					+ "-" + model.getSample_type() + ";";
			id_card += (null == model.getId_number() || "".equals(model
					.getId_number())) ? "" : (model.getId_number() + ";");
			try {
				birth_date += (null == model.getBirth_date() || "".equals(model
						.getBirth_date())) ? ""
						: (com.rds.code.utils.StringUtils
								.dateToChineseTen(model.getBirth_date()) + ";");
			} catch (Exception e) {
				System.out.println("identifyRegitster----------------string to date exception!!");
				e.printStackTrace();
			}
		}
		/** 案例数据封装 end **/
		
		caseInfoModel.setFandm(fandm);
		caseInfoModel.setChild(child);
		caseInfoModel.setId_card(id_card);
		caseInfoModel.setBirth_date(birth_date);
		/** 保存案例信息 start **/
		return rdsIdentifyMapper.updateCaseInfo(caseInfoModel);
		/** 保存案例信息 end **/
		
		/** 保存财务报表 start **/
		
		// 保存财务报表map
//		Map<String, Object> map = new HashMap<String, Object>();
//		// 案例信息
//		map.put("case_id", params.get("case_id"));
//		map.put("case_code", params.get("case_code"));
//		// 案例归属（包括归属地，归属人，代理等信息）
//		map.put("charge_id", params.get("receiver_id"));
//		// 委托人
//		map.put("case_client", params.get("client"));
//		// 样本父母亲姓名
//		map.put("case_sample_fm",
//				fandm != "" ? fandm.substring(0, fandm.length() - 1) : "");
//		// 样本孩子姓名
//		map.put("case_sample_child",
//				child != "" ? child.substring(0, child.length() - 1) : "");
//		// 样本身份证信息
//		map.put("case_sample_idCard",
//				(!id_card.equals("")) ? id_card.substring(0,
//						id_card.length() - 1) : "");
//		// 样本出生日期信息
//		map.put("case_sample_birthDate",
//				(!birth_date.equals("")) ? birth_date.substring(0,
//						birth_date.length() - 1) : "");
//		// 样本数量
//		map.put("case_sample_count", sample_count);
//		// 案例备注
//		map.put("case_remark", params.get("remark"));
//		// 案例受理时间
//		map.put("case_accepttime", params.get("accept_time"));
//		// 案例子类型（求实，中信，唯实等...）
//		map.put("finance_type", params.get("type"));
//		// 案例类型，这里为亲子鉴定
//		map.put("case_type", params.get("case_type"));
//		// 操作人员
//		map.put("update_user", params.get("case_in_per"));
//		// 案例财务信息
//		Map<String, Object> result = this.getStandFee(
//				Integer.parseInt(params.get("typeid").toString()),
//				sample_count, params.get("receiver_id").toString(),
//				Integer.valueOf(params.get("case_type").toString()));
//		if (Boolean.valueOf(result.get("success").toString())) {
//			map.put("stand_sum",result.get("standFee").toString());
//			map.put("real_sum",result.get("standFee").toString());
//		} else {
//			map.put("stand_sum", 0);
//			map.put("real_sum", 0);
//		}
//		// 新增财务信息
//		try {
//			if (Boolean.parseBoolean(params.get("financeflag").toString())) {
//				map.put("finance_id", UUIDUtil.getUUID());
//				rdsFinanceChargeStandardMapper.insertFinanceInfo(map);
//			} else {
//				rdsFinanceChargeStandardMapper.updateFinanceInfo(map);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		/** 保存财务报表 end **/
	}
}
