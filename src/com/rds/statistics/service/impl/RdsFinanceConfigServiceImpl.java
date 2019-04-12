package com.rds.statistics.service.impl;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.JScriptInvoke;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.statistics.mapper.RdsFinanceConfigMapper;
import com.rds.statistics.model.RdsFinanceAmoebaStatisticsModel;
import com.rds.statistics.model.RdsFinanceAptitudModel;
import com.rds.statistics.model.RdsFinanceCaseDetailModel;
import com.rds.statistics.model.RdsFinanceCaseDetailStatisticsModel;
import com.rds.statistics.model.RdsFinanceConfigModel;
import com.rds.statistics.model.RdsFinanceProgramModel;
import com.rds.statistics.model.RdsStatisticsDepartmentModel;
import com.rds.statistics.service.RdsFinanceConfigService;
import com.rds.upc.model.RdsUpcUserModel;

@Service
public class RdsFinanceConfigServiceImpl implements RdsFinanceConfigService {
	@Autowired
	private RdsFinanceConfigMapper rdsFinanceConfigMapper;

	public List<RdsFinanceConfigModel> queryAll(Map<String, Object> params) {
		return rdsFinanceConfigMapper.queryAll(params);
	}

	public int queryAllCount(Map<String, Object> params) {
		return rdsFinanceConfigMapper.queryAllCount(params);
	}

	@Override
	public void finance_config(Map<String, Object> params) {
		List<RdsFinanceConfigModel> list = rdsFinanceConfigMapper
				.queryAll(params);
		for (RdsFinanceConfigModel rdsFinanceConfigModel : list) {
			// 管理费公式
			String finance_manage = rdsFinanceConfigModel.getFinance_manage();
			// 后端结算公式
			String back_settlement = rdsFinanceConfigModel.getBack_settlement();

			params.put("case_subtype",
					rdsFinanceConfigModel.getFinance_program_type());
			// 根据项目计算应收总额，案例数量，样本数量等
			List<RdsFinanceProgramModel> programList = rdsFinanceConfigMapper
					.queryProgramFinance(params);
			Double program_real_sum = programList.get(0).getProgram_real_sum();
			// 应收大于0
			if (program_real_sum != null) {
				int program_case_num = programList.get(0).getProgram_case_num();
				int program_sample_num = programList.get(0).getSample_count();
				Double aptitude_price = 0.0;
				// 资质合作方自资费计算
				if ("亲子鉴定(资质)".equals(rdsFinanceConfigModel
						.getFinance_program_type())) {
					// 遍历所有用资质的合作方
					List<RdsFinanceAptitudModel> aptitudList = rdsFinanceConfigMapper
							.queryAptitude(params);
					for (RdsFinanceAptitudModel rdsFinanceAptitudModel : aptitudList) {
						// 正孚和信诺不算在资质费里面
						if (!"广东正孚法医毒物司法鉴定所".equals(rdsFinanceAptitudModel
								.getPartnername())
								&& !"北京信诺司法鉴定所".equals(rdsFinanceAptitudModel
										.getPartnername())) {
							Double aptitude_price_temp = 0.0;
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("case_subtype", "亲子鉴定(资质)");
							map.put("confirm_date", params.get("confirm_date"));
							map.put("partner",
									rdsFinanceAptitudModel.getPartnername());
							// 查询出资质合作方案例
							List<RdsFinanceProgramModel> programList1 = rdsFinanceConfigMapper
									.queryProgramFinance(map);
							if (0 == rdsFinanceAptitudModel.getAptitude_case()) {
								aptitude_price_temp += rdsFinanceAptitudModel
										.getAptitude_sample()
										* programList1.get(0).getSample_count();
								aptitude_price += rdsFinanceAptitudModel
										.getAptitude_sample()
										* programList1.get(0).getSample_count();
							} else {
								aptitude_price_temp += rdsFinanceAptitudModel
										.getAptitude_case()
										* programList1.get(0)
												.getProgram_case_num();
								aptitude_price += rdsFinanceAptitudModel
										.getAptitude_case()
										* programList1.get(0)
												.getProgram_case_num();
							}
							// 插入月度资质费
							map.put("aptitude_id", UUIDUtil.getUUID());
							map.put("aptitude_month",
									params.get("confirm_date"));
							map.put("aptitude_partner",
									rdsFinanceAptitudModel.getPartnername());
							map.put("aptitude_fee", aptitude_price_temp);
							if (aptitude_price_temp > 0)
								rdsFinanceConfigMapper.insertAptitudeFee(map);
						} else {
							// 信诺和正孚算作委外成本
							Double aptitude_price_temp = 0.0;
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("case_subtype", "亲子鉴定(资质)");
							map.put("confirm_date", params.get("confirm_date"));
							map.put("partner",
									rdsFinanceAptitudModel.getPartnername());
							// 查询出资质合作方案例
							List<RdsFinanceProgramModel> programList1 = rdsFinanceConfigMapper
									.queryProgramFinance(map);
							if (0 == rdsFinanceAptitudModel.getAptitude_case()) {
								aptitude_price_temp += rdsFinanceAptitudModel
										.getAptitude_sample()
										* programList1.get(0).getSample_count();
							} else {
								aptitude_price_temp += rdsFinanceAptitudModel
										.getAptitude_case()
										* programList1.get(0)
												.getProgram_case_num();
							}
							if ("广东正孚法医毒物司法鉴定所".equals(rdsFinanceAptitudModel
									.getPartnername())) {
								// 特殊样本
								aptitude_price_temp += programList1.get(0)
										.getSpecial_count1() * 50;
								aptitude_price_temp += programList1.get(0)
										.getSpecial_count2() * 400;
							}
							// 插入委外成本
							map.put("outsourcing_id", UUIDUtil.getUUID());
							map.put("outsourcing_month",
									params.get("confirm_date"));
							map.put("outsourcing_partner",
									rdsFinanceAptitudModel.getPartnername());
							map.put("user_dept_level1", rdsFinanceConfigModel
									.getFinance_manage_dept());
							map.put("outsourcing_fee", aptitude_price_temp);
							// 委外成本
							if (aptitude_price_temp > 0)
								rdsFinanceConfigMapper.insertOutsourcinFee(map);

						}

					}

				}

				// 内部结算管理费
				try {
					Double finance_manage_fee = 0.0;
					if (null == finance_manage || "".equals(finance_manage))
						finance_manage_fee = 0.0;
					else
						finance_manage_fee = JScriptInvoke.getProgramFinance(
								finance_manage, program_real_sum,
								program_case_num, program_sample_num,
								aptitude_price);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("price_id", UUIDUtil.getUUID());
					map.put("program_name",
							rdsFinanceConfigModel.getFinance_program_type());
					map.put("program_case_num", programList.get(0)
							.getProgram_case_num());
					map.put("program_month", params.get("confirm_date"));
					map.put("user_dept_level1",
							rdsFinanceConfigModel.getFinance_manage_dept());
					map.put("finance_dept", finance_manage_fee);
					if (finance_manage_fee > 0)
						rdsFinanceConfigMapper.insertProgramPrice(map);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 内部结算后端费用
				try {
					Double back_settlement_fee = 0.0;
					if (null == back_settlement || "".equals(back_settlement))
						back_settlement_fee = 0.0;
					else
						back_settlement_fee = JScriptInvoke.getProgramFinance(
								back_settlement, program_real_sum,
								program_sample_num, program_case_num,
								aptitude_price);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("price_id", UUIDUtil.getUUID());
					map.put("program_name",
							rdsFinanceConfigModel.getFinance_program_type());
					map.put("program_case_num", programList.get(0)
							.getProgram_case_num());
					map.put("program_month", params.get("confirm_date"));
					map.put("user_dept_level1",
							rdsFinanceConfigModel.getBack_settlement_dept());
					map.put("finance_dept", back_settlement_fee);
					// 特殊样本
					if ("亲子鉴定(医学)".equals(rdsFinanceConfigModel
							.getFinance_program_type())) {
						back_settlement_fee += programList.get(0)
								.getSpecial_count1() * 200;
						back_settlement_fee += programList.get(0)
								.getSpecial_count2() * 200;
					}
					// 特殊样本
					if ("亲子鉴定(子渊)".equals(rdsFinanceConfigModel
							.getFinance_program_type())) {
						back_settlement_fee += programList.get(0)
								.getSpecial_count1() * 200;
						back_settlement_fee += programList.get(0)
								.getSpecial_count2() * 400;
					}
					if (back_settlement_fee > 0)
						rdsFinanceConfigMapper.insertProgramPrice(map);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

		// 所有事业部
		List<String> listDept = rdsFinanceConfigMapper.queryUserDept();
		// List<String> listDept = new ArrayList<>();listDept.add("转化医学事业部");
		// 遍历事业部
		for (String string : listDept) {
			params.put("user_dept_level1", string);
			// 每个事业部下面遍历每一个项目
			for (RdsFinanceConfigModel rdsFinanceConfigModel : list) {
				Double finance_cost = 0.0;
				// 管理费公式
				String finance_manage = rdsFinanceConfigModel
						.getFinance_manage();
				// 后端结算公式
				String back_settlement = rdsFinanceConfigModel
						.getBack_settlement();

				params.put("case_subtype",
						rdsFinanceConfigModel.getFinance_program_type());
				// 根据项目计算应收总额，案例数量，样本数量等
				List<RdsFinanceProgramModel> programList = rdsFinanceConfigMapper
						.queryProgramFinance(params);
				Double program_real_sum = programList.get(0)
						.getProgram_real_sum();
				// 应收大于0
				if (program_real_sum != null) {
					int program_case_num = programList.get(0)
							.getProgram_case_num();
					int program_sample_num = programList.get(0)
							.getSample_count();
					Double aptitude_price = 0.0;
					// 资质合作方自资费计算
					if ("亲子鉴定(资质)".equals(rdsFinanceConfigModel
							.getFinance_program_type())) {
						// 遍历所有用资质的合作方
						List<RdsFinanceAptitudModel> aptitudList = rdsFinanceConfigMapper
								.queryAptitude(params);
						for (RdsFinanceAptitudModel rdsFinanceAptitudModel : aptitudList) {
							Double aptitude_price_temp = 0.0;
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("case_subtype", "亲子鉴定(资质)");
							map.put("confirm_date", params.get("confirm_date"));
							map.put("partner",
									rdsFinanceAptitudModel.getPartnername());
							map.put("user_dept_level1", string);
							// 查询出资质合作方案例
							List<RdsFinanceProgramModel> programList1 = rdsFinanceConfigMapper
									.queryProgramFinance(map);
							if (0 == rdsFinanceAptitudModel.getAptitude_case()) {
								aptitude_price_temp += rdsFinanceAptitudModel
										.getAptitude_sample()
										* programList1.get(0).getSample_count();
								aptitude_price += rdsFinanceAptitudModel
										.getAptitude_sample()
										* programList1.get(0).getSample_count();
							} else {
								aptitude_price_temp += rdsFinanceAptitudModel
										.getAptitude_case()
										* programList1.get(0)
												.getProgram_case_num();
								aptitude_price += rdsFinanceAptitudModel
										.getAptitude_case()
										* programList1.get(0)
												.getProgram_case_num();
							}
							// 插入事业部资质费
							map.put("aptitude_id", UUIDUtil.getUUID());
							map.put("aptitude_month",
									params.get("confirm_date"));
							map.put("aptitude_partner",
									rdsFinanceAptitudModel.getPartnername());
							map.put("aptitude_fee", aptitude_price_temp);
							// 正孚和信诺算委外成本
							if (!"广东正孚法医毒物司法鉴定所".equals(rdsFinanceAptitudModel
									.getPartnername())
									&& !"北京信诺司法鉴定所"
											.equals(rdsFinanceAptitudModel
													.getPartnername())) {
								if (aptitude_price_temp > 0)
									rdsFinanceConfigMapper
											.insertChannelAptitudeFee(map);
							}
						}

					}

					// 内部结算管理费
					try {
						Double finance_manage_fee = 0.0;
						if (null == finance_manage || "".equals(finance_manage))
							finance_manage_fee = 0.0;
						else
							finance_manage_fee = JScriptInvoke
									.getProgramFinance(finance_manage,
											program_real_sum, program_case_num,
											program_sample_num, aptitude_price);
						finance_cost += finance_manage_fee;
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 后端结算费用
					try {
						Double back_settlement_fee = 0.0;
						if (null == back_settlement
								|| "".equals(back_settlement))
							back_settlement_fee = 0.0;
						else
							back_settlement_fee = JScriptInvoke
									.getProgramFinance(back_settlement,
											program_real_sum, program_case_num,
											program_sample_num, aptitude_price);
						// 特殊样本
						if ("亲子鉴定(医学)".equals(rdsFinanceConfigModel
								.getFinance_program_type())) {
							back_settlement_fee += programList.get(0)
									.getSpecial_count1() * 200;
							back_settlement_fee += programList.get(0)
									.getSpecial_count2() * 200;
						}
						if ("亲子鉴定(子渊)".equals(rdsFinanceConfigModel
								.getFinance_program_type())) {
							back_settlement_fee += programList.get(0)
									.getSpecial_count1() * 200;
							back_settlement_fee += programList.get(0)
									.getSpecial_count2() * 400;
						}
						finance_cost += back_settlement_fee;
					} catch (Exception e) {
						e.printStackTrace();
					}

					// 部门结算成本
					try {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("cost_id", UUIDUtil.getUUID());
						map.put("program_name",
								rdsFinanceConfigModel.getFinance_program_type());
						map.put("program_case_num", programList.get(0)
								.getProgram_case_num());
						map.put("program_month", params.get("confirm_date"));
						map.put("user_dept_level1", string);
						map.put("finance_cost", finance_cost);
						if (finance_cost > 0)
							rdsFinanceConfigMapper.insertCostPrice(map);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<List<String>> queryAmoeba(Map<String, Object> params) {
		List<List<String>> list = new ArrayList<>();
		List<String> listDeptTemp = new ArrayList<String>();
		// 部门列表
		if (params.get("listDeptTemp") != null)
			listDeptTemp = (List<String>) params.get("listDeptTemp");
		else
			listDeptTemp = rdsFinanceConfigMapper.queryUserDept();
		List<String> listDept = new ArrayList<>();
		for (String string : listDeptTemp) {
			listDept.add("<a href='#' onClick=jumpToNext('" + string + "')>"
					+ string + "</a>");
		}
		// 部门服务收入
		List<String> listDeptServiceSum = new ArrayList<>();
		listDeptServiceSum.add("<th>服务收入</th>");
		// 部门销售收入
		List<String> listDeptSellSum = new ArrayList<>();
		listDeptSellSum.add("<th>销售收入</th>");
		// 部门内部结算收入
		List<String> listDeptInsideSum = new ArrayList<>();
		listDeptInsideSum.add("<th>内部结算收入</th>");
		// 部门收入小计（含税）
		List<String> listDeptInTaxSum = new ArrayList<>();
		listDeptInTaxSum.add("<th>收入小计(含税)</th>");
		// 部门收入小计（税）
		List<String> listDeptTaxSum = new ArrayList<>();
		listDeptTaxSum.add("<th>税</th>");
		// 部门收入小计（不含税）
		List<String> listDeptOutTaxSum = new ArrayList<>();
		listDeptOutTaxSum.add("<th>收入小计（不含税）</th>");
		// 空格
		List<String> listDeptNull = new ArrayList<>();
		listDeptNull.add("<th></th>");
		// 人工成本
		List<String> listDeptWagesSum = new ArrayList<>();
		listDeptWagesSum.add("<th>人工成本</th>");
		// 材料成本
		List<String> listDeptMaterialCostSum = new ArrayList<>();
		listDeptMaterialCostSum.add("<th>材料成本</th>");
		// 委外检测成本
		List<String> listDeptExternalCostSum = new ArrayList<>();
		listDeptExternalCostSum.add("<th>委外检测成本</th>");
		// 内部结算成本
		List<String> listDeptCostInsideSum = new ArrayList<>();
		listDeptCostInsideSum.add("<th>内部结算成本</th>");
		// 销售管理结算成本
		List<String> listDeptSaleManageSum = new ArrayList<>();
		listDeptSaleManageSum.add("<th>销售管理费用</th>");
		// 资质费用
		List<String> listDeptAptitudeSum = new ArrayList<>();
		listDeptAptitudeSum.add("<th>资质费用</th>");
		// 其他费用（含折旧及摊销）
		List<String> listDeptOtherSum = new ArrayList<>();
		listDeptOtherSum.add("<th>其他费用（含折旧及摊销）</th>");
		// 对外投资
		List<String> listDeptInvestmentSum = new ArrayList<>();
		listDeptInvestmentSum.add("<th>对外投资</th>");
		// 仪器设备
		List<String> listDeptInstrumentSum = new ArrayList<>();
		listDeptInstrumentSum.add("<th>房屋、装修、仪器及设备采购成本</th>");
		// 成本小计
		List<String> listSum = new ArrayList<>();
		listSum.add("<th>成本小计</th>");
		// 空格
		List<String> listDeptNull1 = new ArrayList<>();
		listDeptNull1.add("<th></th>");
		// 利润小计
		List<String> listProfit = new ArrayList<>();
		listProfit.add("<th>利润小计</th>");
		// 税
		List<String> listTax = new ArrayList<>();
		listTax.add("<th>税</th>");
		// 净利润小计
		List<String> listNetProfit = new ArrayList<>();
		listNetProfit.add("<th>净利润</th>");
		// 内部结算按照月份计算
		Map<String, Object> map = new HashMap<>();
		map.put("confirm_date_start", params.get("confirm_date_start")
				.toString().substring(0, 7));
		map.put("confirm_date_end", params.get("confirm_date_end").toString()
				.substring(0, 7));
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置

		Double listDeptServiceSumAllCount = 0.0;
		Double listDeptSellSumAllCount = 0.0;
		Double listDeptInsideSumAllCount = 0.0;
		Double ListDeptInTaxAllSum = 0.0;
		Double ListDeptTaxAllSum = 0.0;
		Double ListDeptOutTaxAllSum = 0.0;
		Double ListDeptWagesAllSum = 0.0;
		Double listDeptMaterialCostAllSum = 0.0;
		Double listDeptExternalAllSum = 0.0;
		Double listDeptCostInsideAllSum = 0.0;
		Double listDeptSaleManageAllSum = 0.0;
		Double listDeptAptitudeAllSum = 0.0;
		Double listDeptOtherAllSum = 0.0;
		Double listDeptInvestmentAllSum = 0.0;
		Double listDeptInstrumentAllSum = 0.0;
		Double listSumAllCount = 0.0;
		Double listProfitAllCount = 0.0;
		Double listTaxAllCount = 0.0;
		Double listNetProfitAllCount = 0.0;
		for (String string : listDeptTemp) {
			Double listDeptServiceSumCount = 0.0;
			Double listDeptSellSumCount = 0.0;
			Double listDeptInsideSumCount = 0.0;
			Double ListDeptInTaxSum = 0.0;
			Double ListDeptTaxSum = 0.0;
			Double ListDeptOutTaxSum = 0.0;
			Double ListDeptWagesSum = 0.0;
			Double ListDeptMaterialSum = 0.0;
			Double listDeptExternalSumCount = 0.0;
			Double listDeptCostInsideSumCount = 0.0;
			Double listDeptSaleManageSumCount = 0.0;
			Double listDeptAptitudeSumCount = 0.0;
			Double listDeptOtherSumCount = 0.0;
			Double listDeptInvestmentSumCount = 0.0;
			Double listDeptInstrumentSumCount = 0.0;
			Double listSumCount = 0.0;
			Double listProfitCount = 0.0;
			Double listTaxCount = 0.0;
			Double listNetProfitCount = 0.0;
			params.put("deptname", string);
			map.put("deptname", string);
			// 服务收入
			List<Map<String, Object>> tempServiceSum = rdsFinanceConfigMapper
					.queryDepServiceSum(params);
			if (tempServiceSum.size() == 0) {
				listDeptServiceSumCount = 0.0;
				listDeptServiceSum.add("<td>0</td>");
			} else {
				listDeptServiceSumCount = (Double) (tempServiceSum.get(0) == null ? 0.0
						: tempServiceSum.get(0).get("deptServiceSum"));
				listDeptServiceSum.add("<td><a href='#' onClick=alert('暂未开通')>"
						+ decimalFormat.format(listDeptServiceSumCount)
						+ "</a></td>");
			}
			// 销售收入
			List<Map<String, Object>> tempSellSum = rdsFinanceConfigMapper
					.queryDepSellSum(params);
			if (tempSellSum.size() == 0) {
				listDeptSellSumCount = 0.0;
				listDeptSellSum.add("<td>0</td>");
			} else {
				listDeptSellSumCount = (Double) (tempSellSum.get(0) == null ? 0.0
						: tempSellSum.get(0).get("deptSellSum"));
				listDeptSellSum.add("<td>"
						+ decimalFormat.format(listDeptSellSumCount) + "</td>");
			}
			// 内部结算收入
			List<Map<String, Object>> tempInsideSum = rdsFinanceConfigMapper
					.queryDeptInsideSum(params);
			if (tempInsideSum.size() == 0) {
				listDeptInsideSumCount = 0.0;
				listDeptInsideSum.add("<td>0</td>");
			} else {
				listDeptInsideSumCount = (Double) (tempInsideSum.get(0) == null ? 0.0
						: tempInsideSum.get(0).get("deptInsideSum"));
				listDeptInsideSum.add("<td>"
						+ decimalFormat.format(listDeptInsideSumCount)
						+ "</td>");
			}
			ListDeptInTaxSum = listDeptServiceSumCount + listDeptSellSumCount;
			// 收入小计（含税）
			listDeptInTaxSum.add("<td>"
					+ decimalFormat.format(ListDeptInTaxSum) + "</td>");
			String dateFirstIntVal = params.get("confirm_date_start")
					.toString();
			String dateLastIntVal = "2018-05-01";
			if (compare_date(dateFirstIntVal, dateLastIntVal)) {
				ListDeptTaxSum = listDeptServiceSumCount
						+ listDeptSellSumCount
						- (listDeptServiceSumCount / 1.06 + listDeptSellSumCount / 1.16);
				ListDeptOutTaxSum = listDeptServiceSumCount / 1.06
						+ listDeptSellSumCount / 1.16;
			} else {
				ListDeptTaxSum = listDeptServiceSumCount
						+ listDeptSellSumCount
						- (listDeptServiceSumCount / 1.06 + listDeptSellSumCount / 1.17);
				ListDeptOutTaxSum = listDeptServiceSumCount / 1.06
						+ listDeptSellSumCount / 1.17;
			}
			// 收入（税）
			listDeptTaxSum.add("<td>" + decimalFormat.format(ListDeptTaxSum)
					+ "</td>");

			// 收入小计（不含税）,服务收入6个税点，销售收入17个税点
			listDeptOutTaxSum.add("<td>"
					+ decimalFormat.format(ListDeptOutTaxSum
							+ listDeptInsideSumCount) + "</td>");
			listDeptNull.add("<td>-</td>");
			listDeptNull1.add("<td>-</td>");
			// 人工成本
			List<Map<String, Object>> tempWagesSum = rdsFinanceConfigMapper
					.queryDeptWagesSum(map);
			if (tempWagesSum.size() == 0) {
				listDeptWagesSum.add("<td>0</td>");
			} else {
				ListDeptWagesSum = (Double) (tempWagesSum.get(0) == null ? 0.0
						: tempWagesSum.get(0).get("wagesSum"));
				listDeptWagesSum.add("<td>"
						+ decimalFormat.format(ListDeptWagesSum) + "</td>");
			}

			// 材料成本
			params.put("amoeba_program", "耗材");
			List<Map<String, Object>> tempMaterialCostSum = rdsFinanceConfigMapper
					.queryDeptMaterialCostSum(params);
			if (tempMaterialCostSum.size() == 0) {
				listDeptMaterialCostSum.add("<td>0</td>");
			} else {
				ListDeptMaterialSum = (Double) (tempMaterialCostSum.get(0) == null ? 0.0
						: tempMaterialCostSum.get(0).get("materialSum"));
				listDeptMaterialCostSum.add("<td>"
						+ decimalFormat.format(ListDeptMaterialSum) + "</td>");
			}

			// 委外检测成本
			params.put("amoeba_program", "委外检测成本");
			List<Map<String, Object>> tempExternalCostSum = rdsFinanceConfigMapper
					.queryDeptExternalCostSum(params);
			if (tempExternalCostSum.size() == 0) {
				listDeptExternalCostSum.add("<td>0</td>");
			} else {
				listDeptExternalSumCount = (Double) (tempExternalCostSum.get(0) == null ? 0.0
						: tempExternalCostSum.get(0).get("deptExternalCostSum"));
				listDeptExternalCostSum.add("<td>"
						+ decimalFormat.format(listDeptExternalSumCount)
						+ "</td>");
			}

			// 内部结算成本
			List<Map<String, Object>> tempCostInsideSum = rdsFinanceConfigMapper
					.queryCostInsideSum(params);
			if (tempCostInsideSum.size() == 0) {
				listDeptCostInsideSum.add("<td>0</td>");
			} else {
				listDeptCostInsideSumCount = (Double) (tempCostInsideSum.get(0) == null ? 0.0
						: tempCostInsideSum.get(0).get("costInsideSum"));
				listDeptCostInsideSum.add("<td>"
						+ decimalFormat.format(listDeptCostInsideSumCount)
						+ "</td>");
			}

			// 销售管理费用
			List<Map<String, Object>> tempSaleManageSum = rdsFinanceConfigMapper
					.querySaleManageSum(params);
			if (tempSaleManageSum.size() == 0) {
				listDeptSaleManageSum.add("<td>0</td>");
			} else {
				listDeptSaleManageSumCount = (Double) (tempSaleManageSum.get(0) == null ? 0.0
						: tempSaleManageSum.get(0).get("deptSaleManageSum"));
				listDeptSaleManageSum.add("<td>"
						+ decimalFormat.format(listDeptSaleManageSumCount)
						+ "</td>");
			}

			// 资质费用
			List<Map<String, Object>> tempDeptAptitudeSum = rdsFinanceConfigMapper
					.queryDeptAptitudeCostSum(params);
			if (tempDeptAptitudeSum.size() == 0) {
				listDeptAptitudeSum.add("<td>0</td>");
			} else {
				listDeptAptitudeSumCount = (Double) (tempDeptAptitudeSum.get(0) == null ? 0.0
						: tempDeptAptitudeSum.get(0).get("deptAptitudeCostSum"));
				listDeptAptitudeSum.add("<td>"
						+ decimalFormat.format(listDeptAptitudeSumCount)
						+ "</td>");
			}

			// 其他费用（含折旧及摊销）
			map.put("amoeba_program", "其他费用（含折旧及摊销）");
			List<Map<String, Object>> tempOtherCostSum = rdsFinanceConfigMapper
					.queryDeptDepreciationCostSum(map);
			if (tempOtherCostSum.size() == 0) {
				listDeptOtherSum.add("<td>0</td>");
			} else {
				listDeptOtherSumCount = (Double) (tempOtherCostSum.get(0) == null ? 0.0
						: tempOtherCostSum.get(0).get("depreciationSum"));
				listDeptOtherSum
						.add("<td>"
								+ decimalFormat.format(listDeptOtherSumCount)
								+ "</td>");
			}
			// 对外投资
			map.put("amoeba_program", "对外投资");
			List<Map<String, Object>> tempInvestmentCostSum = rdsFinanceConfigMapper
					.queryDeptDepreciationCostSum(map);
			if (tempInvestmentCostSum.size() == 0) {
				listDeptInvestmentSum.add("<td>0</td>");
			} else {
				listDeptInvestmentSumCount = (Double) (tempInvestmentCostSum
						.get(0) == null ? 0.0 : tempInvestmentCostSum.get(0)
						.get("depreciationSum"));
				listDeptInvestmentSum.add("<td>"
						+ decimalFormat.format(listDeptInvestmentSumCount)
						+ "</td>");
			}

			// 仪器设备
			List<Map<String, Object>> tempInstrumentCostSum = rdsFinanceConfigMapper
					.queryDeptInstrumentCostSum(params);
			if (tempInstrumentCostSum.size() == 0) {
				listDeptInstrumentSum.add("<td>0</td>");
			} else {
				listDeptInstrumentSumCount = (Double) (tempInstrumentCostSum
						.get(0) == null ? 0.0 : tempInstrumentCostSum.get(0)
						.get("instrumentSum"));
				listDeptInstrumentSum.add("<td>"
						+ decimalFormat.format(listDeptInstrumentSumCount)
						+ "</td>");
			}

			listDeptServiceSumAllCount += listDeptServiceSumCount;
			listDeptSellSumAllCount += listDeptSellSumCount;
			listDeptInsideSumAllCount += listDeptInsideSumCount;
			ListDeptInTaxAllSum += ListDeptInTaxSum;
			ListDeptTaxAllSum += ListDeptTaxSum;
			ListDeptOutTaxAllSum += ListDeptOutTaxSum;
			ListDeptWagesAllSum += ListDeptWagesSum;
			listDeptMaterialCostAllSum += ListDeptMaterialSum;
			listDeptExternalAllSum += listDeptExternalSumCount;
			listDeptCostInsideAllSum += listDeptCostInsideSumCount;
			listDeptSaleManageAllSum += listDeptSaleManageSumCount;
			listDeptAptitudeAllSum += listDeptAptitudeSumCount;
			listDeptOtherAllSum += listDeptOtherSumCount;
			listDeptInvestmentAllSum += listDeptInvestmentSumCount;
			listDeptInstrumentAllSum += listDeptInstrumentSumCount;

			// 成本小计
			listSumCount = ListDeptWagesSum + ListDeptMaterialSum
					+ listDeptExternalSumCount + listDeptCostInsideSumCount
					+ listDeptSaleManageSumCount + listDeptAptitudeSumCount
					+ listDeptOtherSumCount;
			listSum.add("<td>" + decimalFormat.format(listSumCount) + "</td>");
			listProfitCount = ListDeptOutTaxSum + listDeptInsideSumCount
					- listSumCount;
			listProfit.add("<td>" + decimalFormat.format(listProfitCount)
					+ "</td>");
			listTaxCount = listProfitCount * 0.2;
			listTax.add("<td>" + decimalFormat.format(listTaxCount) + "</td>");
			listNetProfitCount = listProfitCount - listTaxCount;
			listNetProfit.add("<td>" + decimalFormat.format(listNetProfitCount)
					+ "</td>");

			listSumAllCount += listSumCount;
			listProfitAllCount += listProfitCount;
			listTaxAllCount += listTaxCount;
			listNetProfitAllCount += listNetProfitCount;

		}

		listDept.add("合计");
		list.add(listDept);
		// 服务收入
		listDeptServiceSum.add("<td>"
				+ decimalFormat.format(listDeptServiceSumAllCount) + "</td>");
		list.add(listDeptServiceSum);
		// 销售收入
		listDeptSellSum.add("<td>"
				+ decimalFormat.format(listDeptSellSumAllCount) + "</td>");
		list.add(listDeptSellSum);
		// 内部结算收入
		listDeptInsideSum.add("<td>"
				+ decimalFormat.format(listDeptInsideSumAllCount) + "</td>");
		list.add(listDeptInsideSum);
		// 收入小计（含税）
		listDeptInTaxSum.add("<td>" + decimalFormat.format(ListDeptInTaxAllSum)
				+ "</td>");
		list.add(listDeptInTaxSum);
		// 收入小计（税）
		listDeptTaxSum.add("<td>" + decimalFormat.format(ListDeptTaxAllSum)
				+ "</td>");
		list.add(listDeptTaxSum);
		// 收入小计（不含税）
		listDeptOutTaxSum.add("<td>"
				+ decimalFormat.format(ListDeptOutTaxAllSum
						+ listDeptInsideSumAllCount) + "</td>");
		list.add(listDeptOutTaxSum);
		// 空格
		listDeptNull.add("<td>-</td>");
		list.add(listDeptNull);
		// 人工成本
		listDeptWagesSum.add("<td>" + decimalFormat.format(ListDeptWagesAllSum)
				+ "</td>");
		list.add(listDeptWagesSum);
		// 材料成本
		listDeptMaterialCostSum.add("<td>"
				+ decimalFormat.format(listDeptMaterialCostAllSum) + "</td>");
		list.add(listDeptMaterialCostSum);
		// 委外成本
		listDeptExternalCostSum.add("<td>"
				+ decimalFormat.format(listDeptExternalAllSum) + "</td>");
		list.add(listDeptExternalCostSum);
		// 内部结算成本
		listDeptCostInsideSum.add("<td>"
				+ decimalFormat.format(listDeptCostInsideAllSum) + "</td>");
		list.add(listDeptCostInsideSum);
		// 销售管理成本
		listDeptSaleManageSum.add("<td>"
				+ decimalFormat.format(listDeptSaleManageAllSum) + "</td>");
		list.add(listDeptSaleManageSum);
		// 资质费用
		listDeptAptitudeSum.add("<td>"
				+ decimalFormat.format(listDeptAptitudeAllSum) + "</td>");
		list.add(listDeptAptitudeSum);
		// 其他费用（含折旧及摊销）
		listDeptOtherSum.add("<td>" + decimalFormat.format(listDeptOtherAllSum)
				+ "</td>");
		list.add(listDeptOtherSum);
		// 对外投资
		listDeptInvestmentSum.add("<td>"
				+ decimalFormat.format(listDeptInvestmentAllSum) + "</td>");
		list.add(listDeptInvestmentSum);
		// 仪器设备
		listDeptInstrumentSum.add("<td>"
				+ decimalFormat.format(listDeptInstrumentAllSum) + "</td>");
		list.add(listDeptInstrumentSum);
		// 成本小计
		listSum.add("<td>" + decimalFormat.format(listSumAllCount) + "</td>");
		list.add(listSum);
		// 空格
		listDeptNull1.add("<td>-</td>");
		list.add(listDeptNull1);
		// 利润小计
		listProfit.add("<td>" + decimalFormat.format(listProfitAllCount)
				+ "</td>");
		list.add(listProfit);
		// 税
		listTax.add("<td>" + decimalFormat.format(listTaxAllCount) + "</td>");
		list.add(listTax);
		// 净利润小计
		listNetProfit.add("<td>" + decimalFormat.format(listNetProfitAllCount)
				+ "</td>");
		list.add(listNetProfit);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<List<String>> queryAmoebaSecond(Map<String, Object> params) {
		List<List<String>> list = new ArrayList<>();
		List<String> listDeptTemp = new ArrayList<String>();
		// 部门列表
		if (params.get("listDeptTemp") != null)
			listDeptTemp = (List<String>) params.get("listDeptTemp");
		else
			listDeptTemp = rdsFinanceConfigMapper.queryUserDeptSecond(params);
		List<String> listDept = new ArrayList<>();
		for (String string : listDeptTemp) {
			listDept.add(string);
		}
		// 部门服务收入
		List<String> listDeptServiceSum = new ArrayList<>();
		listDeptServiceSum.add("<th>服务收入</th>");
		// 部门销售收入
		List<String> listDeptSellSum = new ArrayList<>();
		listDeptSellSum.add("<th>销售收入</th>");
		// 部门内部结算收入
		List<String> listDeptInsideSum = new ArrayList<>();
		listDeptInsideSum.add("<th>内部结算收入</th>");
		// 部门收入小计（含税）
		List<String> listDeptInTaxSum = new ArrayList<>();
		listDeptInTaxSum.add("<th>收入小计(含税)</th>");
		// 部门收入小计（税）
		List<String> listDeptTaxSum = new ArrayList<>();
		listDeptTaxSum.add("<th>税</th>");
		// 部门收入小计（不含税）
		List<String> listDeptOutTaxSum = new ArrayList<>();
		listDeptOutTaxSum.add("<th>收入小计（不含税）</th>");
		// 空格
		List<String> listDeptNull = new ArrayList<>();
		listDeptNull.add("<th></th>");
		// 人工成本
		List<String> listDeptWagesSum = new ArrayList<>();
		listDeptWagesSum.add("<th>人工成本</th>");
		// 材料成本
		List<String> listDeptMaterialCostSum = new ArrayList<>();
		listDeptMaterialCostSum.add("<th>材料成本</th>");
		// 委外检测成本
		List<String> listDeptExternalCostSum = new ArrayList<>();
		listDeptExternalCostSum.add("<th>委外检测成本</th>");
		// 内部结算成本
		List<String> listDeptCostInsideSum = new ArrayList<>();
		listDeptCostInsideSum.add("<th>内部结算成本</th>");
		// 销售管理结算成本
		List<String> listDeptSaleManageSum = new ArrayList<>();
		listDeptSaleManageSum.add("<th>销售管理费用</th>");
		// 资质费用
		List<String> listDeptAptitudeSum = new ArrayList<>();
		listDeptAptitudeSum.add("<th>资质费用</th>");
		// 其他费用（含折旧及摊销）
		List<String> listDeptOtherSum = new ArrayList<>();
		listDeptOtherSum.add("<th>其他费用（含折旧及摊销）</th>");
		// 对外投资
		List<String> listDeptInvestmentSum = new ArrayList<>();
		listDeptInvestmentSum.add("<th>对外投资</th>");
		// 仪器设备
		List<String> listDeptInstrumentSum = new ArrayList<>();
		listDeptInstrumentSum.add("<th>房屋、装修、仪器及设备采购成本</th>");
		// 成本小计
		List<String> listSum = new ArrayList<>();
		listSum.add("<th>成本小计</th>");
		// 空格
		List<String> listDeptNull1 = new ArrayList<>();
		listDeptNull1.add("<th></th>");
		// 利润小计
		List<String> listProfit = new ArrayList<>();
		listProfit.add("<th>利润小计</th>");
		// 税
		List<String> listTax = new ArrayList<>();
		listTax.add("<th>税</th>");
		// 净利润小计
		List<String> listNetProfit = new ArrayList<>();
		listNetProfit.add("<th>净利润</th>");
		// 内部结算按照月份计算
		Map<String, Object> map = new HashMap<>();
		map.put("confirm_date_start", params.get("confirm_date_start")
				.toString().substring(0, 7));
		map.put("confirm_date_end", params.get("confirm_date_end").toString()
				.substring(0, 7));
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置

		Double listDeptServiceSumAllCount = 0.0;
		Double listDeptSellSumAllCount = 0.0;
		Double listDeptInsideSumAllCount = 0.0;
		Double ListDeptInTaxAllSum = 0.0;
		Double ListDeptTaxAllSum = 0.0;
		Double ListDeptOutTaxAllSum = 0.0;
		Double ListDeptWagesAllSum = 0.0;
		Double listDeptMaterialCostAllSum = 0.0;
		Double listDeptExternalAllSum = 0.0;
		Double listDeptCostInsideAllSum = 0.0;
		Double listDeptSaleManageAllSum = 0.0;
		Double listDeptAptitudeAllSum = 0.0;
		Double listDeptOtherAllSum = 0.0;
		Double listDeptInvestmentAllSum = 0.0;
		Double listDeptInstrumentAllSum = 0.0;
		Double listSumAllCount = 0.0;
		Double listProfitAllCount = 0.0;
		Double listTaxAllCount = 0.0;
		Double listNetProfitAllCount = 0.0;
		for (String string : listDeptTemp) {
			Double listDeptServiceSumCount = 0.0;
			Double listDeptSellSumCount = 0.0;
			Double listDeptInsideSumCount = 0.0;
			Double ListDeptInTaxSum = 0.0;
			Double ListDeptTaxSum = 0.0;
			Double ListDeptOutTaxSum = 0.0;
			Double ListDeptWagesSum = 0.0;
			Double ListDeptMaterialSum = 0.0;
			Double listDeptExternalSumCount = 0.0;
			Double listDeptCostInsideSumCount = 0.0;
			Double listDeptSaleManageSumCount = 0.0;
			Double listDeptAptitudeSumCount = 0.0;
			Double listDeptOtherSumCount = 0.0;
			Double listDeptInvestmentSumCount = 0.0;
			Double listDeptInstrumentSumCount = 0.0;
			Double listSumCount = 0.0;
			Double listProfitCount = 0.0;
			Double listTaxCount = 0.0;
			Double listNetProfitCount = 0.0;
			params.put("deptname1", string);
			map.put("deptname1", string);
			// 服务收入
			List<Map<String, Object>> tempServiceSum = rdsFinanceConfigMapper
					.queryDepServiceSumSecond(params);
			if (tempServiceSum.size() == 0) {
				listDeptServiceSumCount = 0.0;
				listDeptServiceSum.add("<td>0</td>");
			} else {
				listDeptServiceSumCount = (Double) (tempServiceSum.get(0) == null ? 0.0
						: tempServiceSum.get(0).get("deptServiceSum"));
				listDeptServiceSum.add("<td><a href='#' onClick=alert('暂未开通')>"
						+ decimalFormat.format(listDeptServiceSumCount)
						+ "</a></td>");
			}
			// 销售收入
			List<Map<String, Object>> tempSellSum = rdsFinanceConfigMapper
					.queryDepSellSumSecond(params);
			if (tempSellSum.size() == 0) {
				listDeptSellSumCount = 0.0;
				listDeptSellSum.add("<td>0</td>");
			} else {
				listDeptSellSumCount = (Double) (tempSellSum.get(0) == null ? 0.0
						: tempSellSum.get(0).get("deptSellSum"));
				listDeptSellSum.add("<td>"
						+ decimalFormat.format(listDeptSellSumCount) + "</td>");
			}
			// 内部结算收入
			if (string.equals(params.get("deptname"))) {
				List<Map<String, Object>> tempInsideSum = rdsFinanceConfigMapper
						.queryDeptInsideSum(params);
				if (tempInsideSum.size() == 0) {
					listDeptInsideSumCount = 0.0;
					listDeptInsideSum.add("<td>0</td>");
				} else {
					listDeptInsideSumCount = (Double) (tempInsideSum.get(0) == null ? 0.0
							: tempInsideSum.get(0).get("deptInsideSum"));
					listDeptInsideSum.add("<td>"
							+ decimalFormat.format(listDeptInsideSumCount)
							+ "</td>");
				}
			} else
				listDeptInsideSum.add("<td>0</td>");
			ListDeptInTaxSum = listDeptServiceSumCount + listDeptSellSumCount;
			// 收入小计（含税）
			listDeptInTaxSum.add("<td>"
					+ decimalFormat.format(ListDeptInTaxSum) + "</td>");

			// ListDeptTaxSum =
			// listDeptServiceSumCount+listDeptSellSumCount-(listDeptServiceSumCount/1.06+listDeptSellSumCount/1.17);

			String dateFirstIntVal = params.get("confirm_date_start")
					.toString();
			String dateLastIntVal = "2018-05-01";
			if (compare_date(dateFirstIntVal, dateLastIntVal)) {
				ListDeptTaxSum = listDeptServiceSumCount
						+ listDeptSellSumCount
						- (listDeptServiceSumCount / 1.06 + listDeptSellSumCount / 1.16);
				ListDeptOutTaxSum = listDeptServiceSumCount / 1.06
						+ listDeptSellSumCount / 1.16;
			} else {
				ListDeptTaxSum = listDeptServiceSumCount
						+ listDeptSellSumCount
						- (listDeptServiceSumCount / 1.06 + listDeptSellSumCount / 1.17);
				ListDeptOutTaxSum = listDeptServiceSumCount / 1.06
						+ listDeptSellSumCount / 1.17;
			}

			// 收入（税）
			listDeptTaxSum.add("<td>" + decimalFormat.format(ListDeptTaxSum)
					+ "</td>");
			// ListDeptOutTaxSum =
			// listDeptServiceSumCount/1.06+listDeptSellSumCount/1.17;

			// 收入小计（不含税）,服务收入6个税点，销售收入17个税点
			listDeptOutTaxSum.add("<td>"
					+ decimalFormat.format(ListDeptOutTaxSum
							+ listDeptInsideSumCount) + "</td>");
			listDeptNull.add("<td>-</td>");
			listDeptNull1.add("<td>-</td>");
			// 人工成本
			List<Map<String, Object>> tempWagesSum = rdsFinanceConfigMapper
					.queryDeptWagesSumSecond(params);
			if (tempWagesSum.size() == 0) {
				listDeptWagesSum.add("<td>0</td>");
			} else {
				ListDeptWagesSum = (Double) (tempWagesSum.get(0) == null ? 0.0
						: tempWagesSum.get(0).get("wagesSum"));
				listDeptWagesSum.add("<td>"
						+ decimalFormat.format(ListDeptWagesSum) + "</td>");
			}

			// 材料成本
			if (string.equals(params.get("deptname"))) {
				params.put("amoeba_program", "耗材");
				List<Map<String, Object>> tempMaterialCostSum = rdsFinanceConfigMapper
						.queryDeptMaterialCostSum(params);
				if (tempMaterialCostSum.size() == 0) {
					listDeptMaterialCostSum.add("<td>0</td>");
				} else {
					ListDeptMaterialSum = (Double) (tempMaterialCostSum.get(0) == null ? 0.0
							: tempMaterialCostSum.get(0).get("materialSum"));
					listDeptMaterialCostSum.add("<td>"
							+ decimalFormat.format(ListDeptMaterialSum)
							+ "</td>");
				}
			} else
				listDeptMaterialCostSum.add("<td>0</td>");

			// 委外检测成本
			if (string.equals(params.get("deptname"))) {
				params.put("amoeba_program", "委外检测成本");
				List<Map<String, Object>> tempExternalCostSum = rdsFinanceConfigMapper
						.queryDeptExternalCostSum(params);
				if (tempExternalCostSum.size() == 0) {
					listDeptExternalCostSum.add("<td>0</td>");
				} else {
					listDeptExternalSumCount = (Double) (tempExternalCostSum
							.get(0) == null ? 0.0 : tempExternalCostSum.get(0)
							.get("deptExternalCostSum"));
					listDeptExternalCostSum.add("<td>"
							+ decimalFormat.format(listDeptExternalSumCount)
							+ "</td>");
				}
			} else
				listDeptExternalCostSum.add("<td>0</td>");

			// 内部结算成本
			List<Map<String, Object>> tempCostInsideSum = rdsFinanceConfigMapper
					.queryCostInsideSumSecond(params);
			if (tempCostInsideSum.size() == 0) {
				listDeptCostInsideSum.add("<td>0</td>");
			} else {
				listDeptCostInsideSumCount = (Double) (tempCostInsideSum.get(0) == null ? 0.0
						: tempCostInsideSum.get(0).get("costInsideSum"));
				listDeptCostInsideSum.add("<td>"
						+ decimalFormat.format(listDeptCostInsideSumCount)
						+ "</td>");
			}

			// 销售管理费用
			if (string.equals(params.get("deptname"))) {
				List<Map<String, Object>> tempSaleManageSum = rdsFinanceConfigMapper
						.querySaleManageSum(params);
				if (tempSaleManageSum.size() == 0) {
					listDeptSaleManageSum.add("<td>0</td>");
				} else {
					listDeptSaleManageSumCount = (Double) (tempSaleManageSum
							.get(0) == null ? 0.0 : tempSaleManageSum.get(0)
							.get("deptSaleManageSum"));
					listDeptSaleManageSum.add("<td>"
							+ decimalFormat.format(listDeptSaleManageSumCount)
							+ "</td>");
				}
			} else
				listDeptSaleManageSum.add("<td>0</td>");

			// 资质费用
			List<Map<String, Object>> tempDeptAptitudeSum = rdsFinanceConfigMapper
					.queryDeptAptitudeCostSumSecond(params);
			if (tempDeptAptitudeSum.size() == 0) {
				listDeptAptitudeSum.add("<td>0</td>");
			} else {
				listDeptAptitudeSumCount = (Double) (tempDeptAptitudeSum.get(0) == null ? 0.0
						: tempDeptAptitudeSum.get(0).get("deptAptitudeCostSum"));
				listDeptAptitudeSum.add("<td>"
						+ decimalFormat.format(listDeptAptitudeSumCount)
						+ "</td>");
			}

			// 其他费用（含折旧及摊销）
			if (string.equals(params.get("deptname"))) {
				map.put("amoeba_program", "其他费用（含折旧及摊销）");
				map.put("deptname", string);
				List<Map<String, Object>> tempOtherCostSum = rdsFinanceConfigMapper
						.queryDeptDepreciationCostSum(map);
				if (tempOtherCostSum.size() == 0) {
					listDeptOtherSum.add("<td>0</td>");
				} else {
					listDeptOtherSumCount = (Double) (tempOtherCostSum.get(0) == null ? 0.0
							: tempOtherCostSum.get(0).get("depreciationSum"));
					listDeptOtherSum.add("<td>"
							+ decimalFormat.format(listDeptOtherSumCount)
							+ "</td>");
				}
			} else
				listDeptOtherSum.add("<td>0</td>");
			// 对外投资
			if (string.equals(params.get("deptname"))) {
				map.put("amoeba_program", "对外投资");
				map.put("deptname", string);
				List<Map<String, Object>> tempInvestmentCostSum = rdsFinanceConfigMapper
						.queryDeptDepreciationCostSum(map);
				if (tempInvestmentCostSum.size() == 0) {
					listDeptInvestmentSum.add("<td>0</td>");
				} else {
					listDeptInvestmentSumCount = (Double) (tempInvestmentCostSum
							.get(0) == null ? 0.0 : tempInvestmentCostSum
							.get(0).get("depreciationSum"));
					listDeptInvestmentSum.add("<td>"
							+ decimalFormat.format(listDeptInvestmentSumCount)
							+ "</td>");
				}
			} else
				listDeptInvestmentSum.add("<td>0</td>");

			// 仪器设备
			if (string.equals(params.get("deptname"))) {
				List<Map<String, Object>> tempInstrumentCostSum = rdsFinanceConfigMapper
						.queryDeptInstrumentCostSum(params);
				if (tempInstrumentCostSum.size() == 0) {
					listDeptInstrumentSum.add("<td>0</td>");
				} else {
					listDeptInstrumentSumCount = (Double) (tempInstrumentCostSum
							.get(0) == null ? 0.0 : tempInstrumentCostSum
							.get(0).get("instrumentSum"));
					listDeptInstrumentSum.add("<td>"
							+ decimalFormat.format(listDeptInstrumentSumCount)
							+ "</td>");
				}
			} else
				listDeptInstrumentSum.add("<td>0</td>");

			listDeptServiceSumAllCount += listDeptServiceSumCount;
			listDeptSellSumAllCount += listDeptSellSumCount;
			listDeptInsideSumAllCount += listDeptInsideSumCount;
			ListDeptInTaxAllSum += ListDeptInTaxSum;
			ListDeptTaxAllSum += ListDeptTaxSum;
			ListDeptOutTaxAllSum += ListDeptOutTaxSum;
			ListDeptWagesAllSum += ListDeptWagesSum;
			listDeptMaterialCostAllSum += ListDeptMaterialSum;
			listDeptExternalAllSum += listDeptExternalSumCount;
			listDeptCostInsideAllSum += listDeptCostInsideSumCount;
			listDeptSaleManageAllSum += listDeptSaleManageSumCount;
			listDeptAptitudeAllSum += listDeptAptitudeSumCount;
			listDeptOtherAllSum += listDeptOtherSumCount;
			listDeptInvestmentAllSum += listDeptInvestmentSumCount;
			listDeptInstrumentAllSum += listDeptInstrumentSumCount;

			// 成本小计
			listSumCount = ListDeptWagesSum + ListDeptMaterialSum
					+ listDeptExternalSumCount + listDeptCostInsideSumCount
					+ listDeptSaleManageSumCount + listDeptAptitudeSumCount
					+ listDeptOtherSumCount;
			listSum.add("<td>" + decimalFormat.format(listSumCount) + "</td>");
			listProfitCount = ListDeptOutTaxSum + listDeptInsideSumCount
					- listSumCount;
			listProfit.add("<td>" + decimalFormat.format(listProfitCount)
					+ "</td>");
			listTaxCount = listProfitCount * 0.2;
			listTax.add("<td>" + decimalFormat.format(listTaxCount) + "</td>");
			listNetProfitCount = listProfitCount - listTaxCount;
			listNetProfit.add("<td>" + decimalFormat.format(listNetProfitCount)
					+ "</td>");

			listSumAllCount += listSumCount;
			listProfitAllCount += listProfitCount;
			listTaxAllCount += listTaxCount;
			listNetProfitAllCount += listNetProfitCount;

		}

		listDept.add("合计");
		list.add(listDept);
		// 服务收入
		listDeptServiceSum.add("<td>"
				+ decimalFormat.format(listDeptServiceSumAllCount) + "</td>");
		list.add(listDeptServiceSum);
		// 销售收入
		listDeptSellSum.add("<td>"
				+ decimalFormat.format(listDeptSellSumAllCount) + "</td>");
		list.add(listDeptSellSum);
		// 内部结算收入
		listDeptInsideSum.add("<td>"
				+ decimalFormat.format(listDeptInsideSumAllCount) + "</td>");
		list.add(listDeptInsideSum);
		// 收入小计（含税）
		listDeptInTaxSum.add("<td>" + decimalFormat.format(ListDeptInTaxAllSum)
				+ "</td>");
		list.add(listDeptInTaxSum);
		// 收入小计（税）
		listDeptTaxSum.add("<td>" + decimalFormat.format(ListDeptTaxAllSum)
				+ "</td>");
		list.add(listDeptTaxSum);
		// 收入小计（不含税）
		listDeptOutTaxSum.add("<td>"
				+ decimalFormat.format(ListDeptOutTaxAllSum
						+ listDeptInsideSumAllCount) + "</td>");
		list.add(listDeptOutTaxSum);
		// 空格
		listDeptNull.add("<td>-</td>");
		list.add(listDeptNull);
		// 人工成本
		listDeptWagesSum.add("<td>" + decimalFormat.format(ListDeptWagesAllSum)
				+ "</td>");
		list.add(listDeptWagesSum);
		// 材料成本
		listDeptMaterialCostSum.add("<td>"
				+ decimalFormat.format(listDeptMaterialCostAllSum) + "</td>");
		list.add(listDeptMaterialCostSum);
		// 委外成本
		listDeptExternalCostSum.add("<td>"
				+ decimalFormat.format(listDeptExternalAllSum) + "</td>");
		list.add(listDeptExternalCostSum);
		// 内部结算成本
		listDeptCostInsideSum.add("<td>"
				+ decimalFormat.format(listDeptCostInsideAllSum) + "</td>");
		list.add(listDeptCostInsideSum);
		// 销售管理成本
		listDeptSaleManageSum.add("<td>"
				+ decimalFormat.format(listDeptSaleManageAllSum) + "</td>");
		list.add(listDeptSaleManageSum);
		// 资质费用
		listDeptAptitudeSum.add("<td>"
				+ decimalFormat.format(listDeptAptitudeAllSum) + "</td>");
		list.add(listDeptAptitudeSum);
		// 其他费用（含折旧及摊销）
		listDeptOtherSum.add("<td>" + decimalFormat.format(listDeptOtherAllSum)
				+ "</td>");
		list.add(listDeptOtherSum);
		// 对外投资
		listDeptInvestmentSum.add("<td>"
				+ decimalFormat.format(listDeptInvestmentAllSum) + "</td>");
		list.add(listDeptInvestmentSum);
		// 仪器设备
		listDeptInstrumentSum.add("<td>"
				+ decimalFormat.format(listDeptInstrumentAllSum) + "</td>");
		list.add(listDeptInstrumentSum);
		// 成本小计
		listSum.add("<td>" + decimalFormat.format(listSumAllCount) + "</td>");
		list.add(listSum);
		// 空格
		listDeptNull1.add("<td>-</td>");
		list.add(listDeptNull1);
		// 利润小计
		listProfit.add("<td>" + decimalFormat.format(listProfitAllCount)
				+ "</td>");
		list.add(listProfit);
		// 税
		listTax.add("<td>" + decimalFormat.format(listTaxAllCount) + "</td>");
		list.add(listTax);
		// 净利润小计
		listNetProfit.add("<td>" + decimalFormat.format(listNetProfitAllCount)
				+ "</td>");
		list.add(listNetProfit);
		return list;
	}

	@Override
	public List<RdsFinanceAptitudModel> queryAptitude(Map<String, Object> params) {
		return rdsFinanceConfigMapper.queryAptitude(params);
	}

	@Override
	public int queryCountAptitude(Map<String, Object> params) {
		return rdsFinanceConfigMapper.queryCountAptitude(params);
	}

	@Override
	public boolean saveAptitudeConfig(Map<String, Object> params) {
		String config_id = params.get("config_id") == null ? "" : params.get(
				"config_id").toString();
		RdsUpcUserModel user = (RdsUpcUserModel) params.get("user");
		params.put("update_per", user.getUserid());
		if ("".equals(config_id)) {
			params.put("config_id", UUIDUtil.getUUID());
			// 新建
			return rdsFinanceConfigMapper.insertAptitudeConfig(params);
		} else {
			// 更新
			return rdsFinanceConfigMapper.updateApitudeConfig(params);
		}
	}

	@Override
	public boolean deleteAptitudeConfig(Map<String, Object> param) {
		return rdsFinanceConfigMapper.updateApitudeConfigFlag(param);
	}

	@Override
	public boolean saveFinanceConfig(Map<String, Object> params) {
		String config_id = params.get("config_id") == null ? "" : params.get(
				"config_id").toString();
		RdsUpcUserModel user = (RdsUpcUserModel) params.get("user");
		params.put("update_per", user.getUserid());
		if ("".equals(config_id)) {
			params.put("config_id", UUIDUtil.getUUID());
			// 新建
			return rdsFinanceConfigMapper.insertFinanceConfig(params);
		} else {
			// 更新
			return rdsFinanceConfigMapper.updateFinanceConfig(params);
		}

	}

	@Override
	public List<String> queryUserDept() {
		return rdsFinanceConfigMapper.queryUserDept();
	}

	@Override
	public void financeCaseDetail(Map<String, Object> params) {
		try {
			// 销售收入案例，即一次性收入，合同收入
			rdsFinanceConfigMapper.insertContractStatistic(params);
			// 试剂盒内部结算
			rdsFinanceConfigMapper.insertKitSetStatistic(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 服务收入案例
		List<RdsFinanceCaseDetailModel> lists = rdsFinanceConfigMapper
				.queryFinanceCaseDetail(params);
		List<Map<String, Object>> listAll = new ArrayList<>();
		for (RdsFinanceCaseDetailModel rdsFinanceCaseDetailModel : lists) {
			String case_subtype = rdsFinanceCaseDetailModel.getCase_subtype();
			if (null != case_subtype && !"".equals(case_subtype)) {
				Map<String, Object> mapResult = new HashMap<>();
				mapResult
						.put("case_id", rdsFinanceCaseDetailModel.getCase_id());
				mapResult.put("case_code",
						rdsFinanceCaseDetailModel.getCase_code());
				mapResult.put("accept_time",
						rdsFinanceCaseDetailModel.getAccept_time());
				mapResult.put("confirm_date",
						rdsFinanceCaseDetailModel.getConfirm_date());
				mapResult.put("paragraphtime",
						rdsFinanceCaseDetailModel.getParagraphtime());
				mapResult.put("update_date",
						rdsFinanceCaseDetailModel.getUpdate_date());
				mapResult.put("stand_sum",
						rdsFinanceCaseDetailModel.getStand_sum());
				mapResult.put("real_sum",
						rdsFinanceCaseDetailModel.getReal_sum());
				mapResult.put("return_sum",
						rdsFinanceCaseDetailModel.getReturn_sum());
				mapResult.put("client", rdsFinanceCaseDetailModel.getClient());
				mapResult.put("webchart",
						rdsFinanceCaseDetailModel.getWebchart());
				mapResult.put("type", rdsFinanceCaseDetailModel.getType());
				mapResult.put("remark", rdsFinanceCaseDetailModel.getRemark());
				mapResult.put("finance_remark",
						rdsFinanceCaseDetailModel.getFinance_remark());
				mapResult.put("deductible",
						rdsFinanceCaseDetailModel.getDeductible());
				// 渠道事业部分公司特殊处理，根据归属地区分
				if ("渠道一部".equals(rdsFinanceCaseDetailModel
						.getUser_dept_level3())) {
					String case_area = rdsFinanceCaseDetailModel.getCase_area() == null ? ""
							: rdsFinanceCaseDetailModel.getCase_area();
					String user_dept_level4 = rdsFinanceCaseDetailModel
							.getUser_dept_level4() == null ? ""
							: rdsFinanceCaseDetailModel.getUser_dept_level4();
					try {
						// 判断地区和分公司关系
						if (!(user_dept_level4.substring(0, 2))
								.equals(case_area.substring(0, 2))) {
							if ("内蒙".equals(case_area.substring(0, 2))) {
								mapResult.put("user_dept_level4", "内蒙古分公司");
							} else if ("黑龙".equals(case_area.substring(0, 2))) {
								mapResult.put("user_dept_level4", "黑龙江分公司");
							} else if ("无地".equals(case_area.substring(0, 2))) {
								mapResult.put("user_dept_level4",
										rdsFinanceCaseDetailModel
												.getUser_dept_level4());
							} else {
								String user_dept_level4_temp = case_area
										.substring(0, 2) + "分公司";
								Map<String, Object> temp = new HashMap<>();
								temp.put("deptname", user_dept_level4_temp);
								// 判断如果该分公司存在的话
								if (rdsFinanceConfigMapper.deptExitsCount(temp) > 0)
									mapResult.put("user_dept_level4",
											case_area.substring(0, 2) + "分公司");
								else
									mapResult.put("user_dept_level4",
											rdsFinanceCaseDetailModel
													.getUser_dept_level4());
							}
						} else {
							mapResult.put("user_dept_level4",
									rdsFinanceCaseDetailModel
											.getUser_dept_level4());
						}
					} catch (Exception e) {
						mapResult
								.put("user_dept_level4",
										rdsFinanceCaseDetailModel
												.getUser_dept_level4());
					}

				} else {
					mapResult.put("user_dept_level4",
							rdsFinanceCaseDetailModel.getUser_dept_level4());
				}
				mapResult.put("user_dept_level1",
						rdsFinanceCaseDetailModel.getUser_dept_level1());
				mapResult.put("user_dept_level2",
						rdsFinanceCaseDetailModel.getUser_dept_level2());
				mapResult.put("user_dept_level3",
						rdsFinanceCaseDetailModel.getUser_dept_level3());
				mapResult.put("user_dept_level5",
						rdsFinanceCaseDetailModel.getUser_dept_level5());
				mapResult.put("case_user",
						rdsFinanceCaseDetailModel.getCase_user());
				mapResult.put("case_area",
						rdsFinanceCaseDetailModel.getCase_area());
				mapResult
						.put("partner", rdsFinanceCaseDetailModel.getPartner());
				mapResult.put("case_type",
						rdsFinanceCaseDetailModel.getCase_type());
				mapResult.put("case_subtype",
						rdsFinanceCaseDetailModel.getCase_subtype());
				// 查看改案例的所有项目
				String[] caseType = case_subtype.split(";");
				// 计算资质费
				Double aptitudeCost = 0.0;
				// 委外成本
				Double externalCost = 0.0;
				// 内部结算费用
				Double insideCost = 0.0;
				// 计算实验费
				Double experimentCost = 0.0;
				// 管理费
				Double manageCost = 0.0;
				// 遍历项目计算成本
				for (String string : caseType) {
					Map<String, Object> map = new HashMap<>();
					map.put("finance_program_type", string.trim());
					List<RdsFinanceConfigModel> list = rdsFinanceConfigMapper
							.queryAll(map);
					if (list.size() > 0) {
						RdsFinanceConfigModel configModel = list.get(0);
						// 管理费公式
						String finance_manage = configModel.getFinance_manage();
						// 后端结算公式
						String back_settlement = configModel
								.getBack_settlement();
						// 内部结算部门
						mapResult.put("insideCostUnit",
								configModel.getBack_settlement_dept());
						mapResult.put("manageCostUnit",
								configModel.getFinance_manage_dept());

						if ("亲子鉴定(资质)".equals(configModel
								.getFinance_program_type())) {
							map.put("partnername",
									rdsFinanceCaseDetailModel.getPartner());
							// 计算资质费
							// Double aptitudeCost = 0.0;
							// 查询出资质费收费标准
							List<RdsFinanceAptitudModel> aptitudList = rdsFinanceConfigMapper
									.queryAptitude(map);
							RdsFinanceAptitudModel rdsFinanceAptitudModel = aptitudList
									.get(0);
							// 正孚和信诺算到委外成本
							if ("广东正孚法医毒物司法鉴定所"
									.equals(rdsFinanceCaseDetailModel
											.getPartner())
									|| "北京信诺司法鉴定所"
											.equals(rdsFinanceCaseDetailModel
													.getPartner())) {
								// Double externalCost = 0.0;
								if (0 == rdsFinanceAptitudModel
										.getAptitude_case()) {
									externalCost += rdsFinanceAptitudModel
											.getAptitude_sample()
											* rdsFinanceCaseDetailModel
													.getSample_count();
								} else {
									externalCost += rdsFinanceAptitudModel
											.getAptitude_case();
								}
								// 口腔试子及毛发加收50元/样本 其他特殊样本加收400元/样本
								if ("广东正孚法医毒物司法鉴定所"
										.equals(rdsFinanceCaseDetailModel
												.getPartner())) {
									// 特殊样本
									externalCost += rdsFinanceCaseDetailModel
											.getSpecial_count1() * 50;
									externalCost += rdsFinanceCaseDetailModel
											.getSpecial_count2() * 400;
								}
								// mapResult.put("externalCost", externalCost);
								// mapResult.put("id", UUIDUtil.getUUID());
								// //插入该案例结算结果
								// rdsFinanceConfigMapper.insertFinanceCaseDetail(mapResult);
							} else {
								if (0 == rdsFinanceAptitudModel
										.getAptitude_case()) {
									aptitudeCost += rdsFinanceAptitudModel
											.getAptitude_sample()
											* rdsFinanceCaseDetailModel
													.getSample_count();
								} else {
									aptitudeCost += rdsFinanceAptitudModel
											.getAptitude_case();
								}
								// 千麦资质费在每个样本基础上加300
								if ("浙江杭州千麦司法鉴定中心"
										.equals(rdsFinanceCaseDetailModel
												.getPartner())) {
									// 特殊样本
									aptitudeCost += 300;
								}
								// 管理费
								// Double manageCost = 0.0;
								if (null != finance_manage
										|| !"".equals(finance_manage)) {
									try {
										manageCost += JScriptInvoke
												.getProgramFinance(
														finance_manage,
														rdsFinanceCaseDetailModel
																.getReturn_sum(),
														1,
														rdsFinanceCaseDetailModel
																.getSample_count(),
														aptitudeCost);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								// 内部结算费用
								// Double insideCost = 0.0;
								if (null != back_settlement
										|| !"".equals(back_settlement)) {
									try {
										insideCost += JScriptInvoke
												.getProgramFinance(
														back_settlement,
														rdsFinanceCaseDetailModel
																.getReturn_sum(),
														1,
														rdsFinanceCaseDetailModel
																.getSample_count(),
														aptitudeCost);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

								// mapResult.put("id", UUIDUtil.getUUID());
								// mapResult.put("insideCost", insideCost);
								// mapResult.put("insideCostUnit",
								// configModel.getBack_settlement_dept());
								// mapResult.put("manageCost", manageCost);
								// mapResult.put("manageCostUnit",
								// configModel.getFinance_manage_dept());
								// mapResult.put("aptitudeCost", aptitudeCost);
								// //插入该案例结算结果
								// rdsFinanceConfigMapper.insertFinanceCaseDetail(mapResult);
							}
						} else if ("亲子鉴定(实验)".equals(configModel
								.getFinance_program_type())) {
							map.put("partnername",
									rdsFinanceCaseDetailModel.getPartner());
							// 计算实验费
							// Double experimentCost = 0.0;
							// 查询出资质费收费标准
							List<RdsFinanceAptitudModel> aptitudList = rdsFinanceConfigMapper
									.queryAptitude(map);
							RdsFinanceAptitudModel rdsFinanceAptitudModel = aptitudList
									.get(0);

							if (0 == rdsFinanceAptitudModel
									.getExperiment_case()) {
								experimentCost += rdsFinanceAptitudModel
										.getExperiment_sample()
										* rdsFinanceCaseDetailModel
												.getSample_count();
							} else {
								experimentCost += rdsFinanceAptitudModel
										.getExperiment_case();
							}
							// 管理费
							// Double manageCost = 0.0;
							if (null != finance_manage
									|| !"".equals(finance_manage)) {
								try {
									manageCost += JScriptInvoke
											.getProgramFinance(finance_manage,
													rdsFinanceCaseDetailModel
															.getReturn_sum(),
													1,
													rdsFinanceCaseDetailModel
															.getSample_count(),
													experimentCost);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							// 内部结算费用
							// Double insideCost = 0.0;
							if (null != back_settlement
									|| !"".equals(back_settlement)) {
								try {
									insideCost += JScriptInvoke
											.getProgramFinance(back_settlement,
													rdsFinanceCaseDetailModel
															.getReturn_sum(),
													1,
													rdsFinanceCaseDetailModel
															.getSample_count(),
													experimentCost);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							// mapResult.put("id", UUIDUtil.getUUID());
							// mapResult.put("insideCost", insideCost);
							// mapResult.put("insideCostUnit",
							// configModel.getBack_settlement_dept());
							// mapResult.put("manageCost", manageCost);
							// mapResult.put("manageCostUnit",
							// configModel.getFinance_manage_dept());
							// mapResult.put("experimentCost", experimentCost);
							// //插入该案例结算结果
							// rdsFinanceConfigMapper.insertFinanceCaseDetail(mapResult);

						} else {
							// 管理费
							// Double manageCost = 0.0;
							if (null != finance_manage
									|| !"".equals(finance_manage)) {
								try {
									manageCost += JScriptInvoke
											.getProgramFinance(finance_manage,
													rdsFinanceCaseDetailModel
															.getReturn_sum(),
													1,
													rdsFinanceCaseDetailModel
															.getSample_count(),
													0.0);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							// 内部结算费用
							// Double insideCost = 0.0;
							if (null != back_settlement
									|| !"".equals(back_settlement)) {
								try {
									insideCost += JScriptInvoke
											.getProgramFinance(back_settlement,
													rdsFinanceCaseDetailModel
															.getReturn_sum(),
													1,
													rdsFinanceCaseDetailModel
															.getSample_count(),
													0.0);
									// 特殊样本
									if ("亲子鉴定(医学)".equals(configModel
											.getFinance_program_type())) {
										insideCost += rdsFinanceCaseDetailModel
												.getSpecial_count1() * 200;
										insideCost += rdsFinanceCaseDetailModel
												.getSpecial_count2() * 200;
									}
									// 特殊样本
									if ("亲子鉴定(子渊)".equals(configModel
											.getFinance_program_type())) {
										insideCost += rdsFinanceCaseDetailModel
												.getSpecial_count1() * 200;
										insideCost += rdsFinanceCaseDetailModel
												.getSpecial_count2() * 400;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							// 美吉按3500/案例计入委外
							if ("无创亲子".equals(configModel
									.getFinance_program_type())) {
								if (rdsFinanceCaseDetailModel.getCase_code()
										.startsWith("W")) {
									externalCost += 3500.0;
									insideCost = 0.0;
									manageCost = 0.0;
								}
								// mapResult.put("externalCost", 3500.0);
							}
							// 计入委外
							if ("神经肌肉系统遗传病基因检测".equals(configModel
									.getFinance_program_type())) {
								externalCost += 1000.0;
								insideCost = 0.0;
								manageCost = 0.0;
							}
							if (configModel.getBack_settlement_dept().equals(
									rdsFinanceCaseDetailModel
											.getUser_dept_level2())) {
								insideCost = 0.0;
							}
							if (configModel.getFinance_manage_dept().equals(
									rdsFinanceCaseDetailModel
											.getUser_dept_level1())) {
								manageCost = 0.0;
							}
							// mapResult.put("id", UUIDUtil.getUUID());
							// mapResult.put("insideCost", insideCost);
							// mapResult.put("insideCostUnit",
							// configModel.getBack_settlement_dept());
							// mapResult.put("manageCost", manageCost);
							// mapResult.put("manageCostUnit",
							// configModel.getFinance_manage_dept());
							// //插入该案例结算结果
							// rdsFinanceConfigMapper.insertFinanceCaseDetail(mapResult);
						}

					}

				}
				Map<String, Object> maps = new HashMap<>();
				maps.put("case_id", rdsFinanceCaseDetailModel.getCase_id());
				if (rdsFinanceConfigMapper.queryCaseDetailAllCount(maps) > 0) {
					insideCost = 0.0;
					manageCost = 0.0;
					externalCost = 0.0;
					experimentCost = 0.0;
					aptitudeCost = 0.0;
				}
				mapResult.put("id", UUIDUtil.getUUID());
				mapResult.put("insideCost", insideCost);
				mapResult.put("externalCost", externalCost);
				mapResult.put("aptitudeCost", aptitudeCost);
				mapResult.put("experimentCost", experimentCost);
				// mapResult.put("insideCostUnit",
				// configModel.getBack_settlement_dept());
				mapResult.put("manageCost", manageCost);
				// mapResult.put("manageCostUnit",
				// configModel.getFinance_manage_dept());
				listAll.add(mapResult);
			} else {
				Map<String, Object> mapResult = new HashMap<>();
				mapResult
						.put("case_id", rdsFinanceCaseDetailModel.getCase_id());
				mapResult.put("case_code",
						rdsFinanceCaseDetailModel.getCase_code());
				mapResult.put("confirm_date",
						rdsFinanceCaseDetailModel.getConfirm_date());
				mapResult.put("paragraphtime",
						rdsFinanceCaseDetailModel.getParagraphtime());
				mapResult.put("update_date",
						rdsFinanceCaseDetailModel.getUpdate_date());
				mapResult.put("stand_sum",
						rdsFinanceCaseDetailModel.getStand_sum());
				mapResult.put("real_sum",
						rdsFinanceCaseDetailModel.getReal_sum());
				mapResult.put("return_sum",
						rdsFinanceCaseDetailModel.getReturn_sum());
				mapResult.put("client", rdsFinanceCaseDetailModel.getClient());
				mapResult.put("webchart",
						rdsFinanceCaseDetailModel.getWebchart());
				mapResult.put("type", rdsFinanceCaseDetailModel.getType());
				mapResult.put("remark", rdsFinanceCaseDetailModel.getRemark());
				mapResult.put("finance_remark",
						rdsFinanceCaseDetailModel.getFinance_remark());
				mapResult.put("accept_time",
						rdsFinanceCaseDetailModel.getAccept_time());
				mapResult.put("user_dept_level1",
						rdsFinanceCaseDetailModel.getUser_dept_level1());
				mapResult.put("user_dept_level2",
						rdsFinanceCaseDetailModel.getUser_dept_level2());
				mapResult.put("user_dept_level3",
						rdsFinanceCaseDetailModel.getUser_dept_level3());
				mapResult.put("user_dept_level4",
						rdsFinanceCaseDetailModel.getUser_dept_level4());
				mapResult.put("user_dept_level5",
						rdsFinanceCaseDetailModel.getUser_dept_level5());
				mapResult.put("case_user",
						rdsFinanceCaseDetailModel.getCase_user());
				mapResult.put("case_area",
						rdsFinanceCaseDetailModel.getCase_area());
				mapResult
						.put("partner", rdsFinanceCaseDetailModel.getPartner());
				mapResult.put("case_type",
						rdsFinanceCaseDetailModel.getCase_type());
				mapResult.put("case_subtype",
						rdsFinanceCaseDetailModel.getCase_subtype());
				mapResult.put("id", UUIDUtil.getUUID());
				mapResult.put("insideCost", 0.0);
				mapResult.put("externalCost", 0.0);
				mapResult.put("aptitudeCost", 0.0);
				mapResult.put("experimentCost", 0.0);
				mapResult.put("insideCostUnit", "");
				mapResult.put("manageCost", 0.0);
				mapResult.put("manageCostUnit", "");
				listAll.add(mapResult);
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("list", listAll);
		if (listAll.size() > 0)
			// 插入该案例结算结果
			rdsFinanceConfigMapper.insertFinanceCaseDetail(map);
	}

	@Override
	public List<RdsFinanceCaseDetailStatisticsModel> queryCaseDetailAll(
			Map<String, Object> params) {
		return rdsFinanceConfigMapper.queryCaseDetailAll(params);
	}

	@Override
	public int queryCaseDetailAllCount(Map<String, Object> params) {
		return rdsFinanceConfigMapper.queryCaseDetailAllCount(params);
	}

	@Override
	public List<RdsFinanceCaseDetailStatisticsModel> queryCaseDetailAllSum(
			Map<String, Object> params) {
		return rdsFinanceConfigMapper.queryCaseDetailAllSum(params);
	}

	@Override
	public List<Map<String, String>> queryUserDepartment(
			Map<String, Object> params) {
		return rdsFinanceConfigMapper.queryUserDepartment(params);
	}

	@Override
	public void exportCaseDetailAll(Map<String, Object> params,
			HttpServletResponse httpResponse) throws Exception {
		List<RdsFinanceCaseDetailStatisticsModel> objs = rdsFinanceConfigMapper
				.queryCaseDetailAll(params);
		// excel表格列头
		Object[] titles = { "案例回款归属划分核心部分", "", "", "", "", "", "", "收入细节部分",
				"", "", "", "", "", "内部结算细节部分", "", "", "", "", "", "", "其他参数",
				"", "", "", "", "", "", "核对状态", "是否抵扣" };

		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		List<Object> firstobject = new ArrayList<Object>();
		firstobject.add("案例归属事业部");
		firstobject.add("案例归属二级部门");
		firstobject.add("案例归属三级部门");
		firstobject.add("案例归属四级部门");
		firstobject.add("案例归属五级部门");
		firstobject.add("案例归属人");
		firstobject.add("归属人工号");
		firstobject.add("案例受理日期");
		firstobject.add("收入类型");
		firstobject.add("应收回款金额");
		firstobject.add("财务确认到账日期");
		firstobject.add("实际回款金额");
		firstobject.add("财务备注");
		firstobject.add("内部结算部门");
		firstobject.add("内部结算费用");
		firstobject.add("管理费部门");
		firstobject.add("管理费");
		firstobject.add("委外成本");
		firstobject.add("资质费");
		firstobject.add("实验费");
		firstobject.add("案例编号");
		firstobject.add("委托人");
		firstobject.add("案例归属地");
		firstobject.add("案例备注");
		firstobject.add("项目");
		firstobject.add("子项目");
		firstobject.add("合作方");
		bodys.add(firstobject);
		// 循环案例列表拼装表格一行
		for (int i = 0; i < objs.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsFinanceCaseDetailStatisticsModel rdsAllCaseInfoModel = objs
					.get(i);
			objects.add(rdsAllCaseInfoModel.getUser_dept_level1());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level2());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level3());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level4());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level5());
			objects.add(rdsAllCaseInfoModel.getCase_user());
			objects.add(rdsAllCaseInfoModel.getWebchart());
			objects.add(rdsAllCaseInfoModel.getAccept_time());
			objects.add(rdsAllCaseInfoModel.getType());
			objects.add(rdsAllCaseInfoModel.getReal_sum());
			objects.add(rdsAllCaseInfoModel.getConfirm_date());
			objects.add(rdsAllCaseInfoModel.getReturn_sum());
			objects.add(rdsAllCaseInfoModel.getFinance_remark());
			objects.add(rdsAllCaseInfoModel.getInsideCostUnit());
			objects.add(rdsAllCaseInfoModel.getInsideCost());
			objects.add(rdsAllCaseInfoModel.getManageCostUnit());
			objects.add(rdsAllCaseInfoModel.getManageCost());
			objects.add(rdsAllCaseInfoModel.getExternalCost());
			objects.add(rdsAllCaseInfoModel.getAptitudeCost());
			objects.add(rdsAllCaseInfoModel.getExperimentCost());
			objects.add(rdsAllCaseInfoModel.getCase_code());
			objects.add(rdsAllCaseInfoModel.getClient());
			objects.add(rdsAllCaseInfoModel.getCase_area());
			objects.add(rdsAllCaseInfoModel.getRemark());
			objects.add(rdsAllCaseInfoModel.getCase_type());
			objects.add(rdsAllCaseInfoModel.getCase_subtype());
			objects.add(rdsAllCaseInfoModel.getPartner());
			objects.add("");
			objects.add("1".equals(rdsAllCaseInfoModel.getDeductible()) ? "是"
					: "否");
			bodys.add(objects);
		}

		try {
			OutputStream os = httpResponse.getOutputStream();// 取得输出流
			httpResponse.reset();// 清空输出流
			String fname = new String("案例明细表".getBytes("gb2312"), "iso8859-1");
			httpResponse.setHeader("Content-disposition",
					"attachment; filename=" + fname + ".xls");// 设定输出文件头
			httpResponse.setContentType("application/vnd.ms-excel");
			WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
			WritableSheet wsheet = wbook.createSheet("sheet1", 0); // sheet名称

			for (int i = 0; i < titles.length; i++) {
				Label label = new Label(i, 0, String.valueOf(titles[i]));
				wsheet.addCell(label);
			}
			for (int i = 0; i < bodys.size(); i++) {
				for (int j = 0; j < bodys.get(i).size(); j++) {
					if (bodys.get(i).get(j) != null) {
						if (bodys.get(i).get(j) instanceof String) {
							Label label = new Label(j, i + 1,
									String.valueOf(bodys.get(i).get(j)));
							wsheet.addCell(label);
						} else {
							Number number = new Number(j, i + 1,
									Float.valueOf(String.valueOf(bodys.get(i)
											.get(j))));
							wsheet.addCell(number);
						}
					} else {
						Label label = new Label(j, i + 1, "");
						wsheet.addCell(label);
					}

				}
			}
			wsheet.mergeCells(0, 0, 6, 0);
			WritableCellFormat writableCellFormat = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
			for (int i = 0; i < 6; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat);
			}
			wsheet.getWritableCell(6, 0).setCellFormat(writableCellFormat);
			for (int i = 0; i <= 6; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat);
			}

			wsheet.mergeCells(7, 0, 12, 0);
			WritableCellFormat writableCellFormat1 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat1.setAlignment(jxl.format.Alignment.CENTRE);
			for (int i = 7; i < 11; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat1);
			}
			for (int i = 7; i <= 12; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat1);
			}

			wsheet.mergeCells(13, 0, 19, 0);
			WritableCellFormat writableCellFormat2 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat2.setAlignment(jxl.format.Alignment.CENTRE);
			for (int i = 13; i < 18; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat2);
			}
			for (int i = 13; i <= 19; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat2);
			}

			wsheet.mergeCells(20, 0, 26, 0);
			WritableCellFormat writableCellFormat3 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat3.setAlignment(jxl.format.Alignment.CENTRE);
			for (int i = 20; i < 25; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat3);
			}
			for (int i = 20; i <= 26; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat3);
			}

			wsheet.mergeCells(27, 0, 27, 1);
			WritableCellFormat writableCellFormat4 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat4.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat4
					.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 27; i <= 27; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat4);
			}
			for (int i = 27; i <= 27; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat4);
			}

			wsheet.mergeCells(28, 0, 28, 1);
			WritableCellFormat writableCellFormat5 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat5.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat5
					.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 28; i <= 28; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat5);
			}
			for (int i = 28; i <= 28; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat5);
			}
			// 主体内容生成结束
			wbook.write(); // 写入文件
			wbook.close();
			os.close(); // 关闭流
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<RdsFinanceAmoebaStatisticsModel> queryAmoebaInfoPage(
			Map<String, Object> params) {
		return rdsFinanceConfigMapper.queryAmoebaInfoPage(params);
	}

	@Override
	public int queryCountAmoebaInfo(Map<String, Object> params) {
		return rdsFinanceConfigMapper.queryCountAmoebaInfo(params);
	}

	@Override
	public boolean insertAmoebaInfo(Map<String, Object> params)
			throws Exception {
		return rdsFinanceConfigMapper.insertAmoebaInfo(params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exportAmoeba(Map<String, Object> params,
			HttpServletResponse response) {
		List<List<String>> list = new ArrayList<>();
		List<String> listDeptTemp = new ArrayList<String>();
		// 部门列表
		if (params.get("listDeptTemp") != null)
			listDeptTemp = (List<String>) params.get("listDeptTemp");
		else
			listDeptTemp = rdsFinanceConfigMapper.queryUserDept();
		List<String> listDept = new ArrayList<>();
		listDept.add("");
		for (String string : listDeptTemp) {
			listDept.add(string);
		}
		// 部门服务收入
		List<String> listDeptServiceSum = new ArrayList<>();
		listDeptServiceSum.add("服务收入");
		// 部门销售收入
		List<String> listDeptSellSum = new ArrayList<>();
		listDeptSellSum.add("销售收入");
		// 部门内部结算收入
		List<String> listDeptInsideSum = new ArrayList<>();
		listDeptInsideSum.add("内部结算收入");
		// 部门收入小计（含税）
		List<String> listDeptInTaxSum = new ArrayList<>();
		listDeptInTaxSum.add("收入小计(含税)");
		// 部门收入小计（税）
		List<String> listDeptTaxSum = new ArrayList<>();
		listDeptTaxSum.add("税");
		// 部门收入小计（不含税）
		List<String> listDeptOutTaxSum = new ArrayList<>();
		listDeptOutTaxSum.add("收入小计（不含税）");
		// 空格
		List<String> listDeptNull = new ArrayList<>();
		listDeptNull.add("");
		// 人工成本
		List<String> listDeptWagesSum = new ArrayList<>();
		listDeptWagesSum.add("人工成本");
		// 材料成本
		List<String> listDeptMaterialCostSum = new ArrayList<>();
		listDeptMaterialCostSum.add("材料成本");
		// 委外检测成本
		List<String> listDeptExternalCostSum = new ArrayList<>();
		listDeptExternalCostSum.add("委外检测成本");
		// 内部结算成本
		List<String> listDeptCostInsideSum = new ArrayList<>();
		listDeptCostInsideSum.add("内部结算成本");
		// 销售管理结算成本
		List<String> listDeptSaleManageSum = new ArrayList<>();
		listDeptSaleManageSum.add("销售管理费用");
		// 资质费用
		List<String> listDeptAptitudeSum = new ArrayList<>();
		listDeptAptitudeSum.add("资质费用");
		// 其他费用（含折旧及摊销）
		List<String> listDeptOtherSum = new ArrayList<>();
		listDeptOtherSum.add("其他费用（含折旧及摊销）");
		// 对外投资
		List<String> listDeptInvestmentSum = new ArrayList<>();
		listDeptInvestmentSum.add("对外投资");
		// 仪器设备
		List<String> listDeptInstrumentSum = new ArrayList<>();
		listDeptInstrumentSum.add("房屋、装修、仪器及设备采购成本");
		// 成本小计
		List<String> listSum = new ArrayList<>();
		listSum.add("成本小计");
		// 空格
		List<String> listDeptNull1 = new ArrayList<>();
		listDeptNull1.add("");
		// 利润小计
		List<String> listProfit = new ArrayList<>();
		listProfit.add("利润小计");
		// 税
		List<String> listTax = new ArrayList<>();
		listTax.add("税");
		// 净利润小计
		List<String> listNetProfit = new ArrayList<>();
		listNetProfit.add("净利润");
		// 内部结算按照月份计算
		Map<String, Object> map = new HashMap<>();
		map.put("confirm_date_start", params.get("confirm_date_start")
				.toString().substring(0, 7));
		map.put("confirm_date_end", params.get("confirm_date_end").toString()
				.substring(0, 7));
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置

		Double listDeptServiceSumAllCount = 0.0;
		Double listDeptSellSumAllCount = 0.0;
		Double listDeptInsideSumAllCount = 0.0;
		Double ListDeptInTaxAllSum = 0.0;
		Double ListDeptTaxAllSum = 0.0;
		Double ListDeptOutTaxAllSum = 0.0;
		Double ListDeptWagesAllSum = 0.0;
		Double listDeptMaterialCostAllSum = 0.0;
		Double listDeptExternalAllSum = 0.0;
		Double listDeptCostInsideAllSum = 0.0;
		Double listDeptSaleManageAllSum = 0.0;
		Double listDeptAptitudeAllSum = 0.0;
		Double listDeptOtherAllSum = 0.0;
		Double listDeptInvestmentAllSum = 0.0;
		Double listDeptInstrumentAllSum = 0.0;
		Double listSumAllCount = 0.0;
		Double listProfitAllCount = 0.0;
		Double listTaxAllCount = 0.0;
		Double listNetProfitAllCount = 0.0;
		for (String string : listDeptTemp) {
			Double listDeptServiceSumCount = 0.0;
			Double listDeptSellSumCount = 0.0;
			Double listDeptInsideSumCount = 0.0;
			Double ListDeptInTaxSum = 0.0;
			Double ListDeptTaxSum = 0.0;
			Double ListDeptOutTaxSum = 0.0;
			Double ListDeptWagesSum = 0.0;
			Double ListDeptMaterialSum = 0.0;
			Double listDeptExternalSumCount = 0.0;
			Double listDeptCostInsideSumCount = 0.0;
			Double listDeptSaleManageSumCount = 0.0;
			Double listDeptAptitudeSumCount = 0.0;
			Double listDeptOtherSumCount = 0.0;
			Double listDeptInvestmentSumCount = 0.0;
			Double listDeptInstrumentSumCount = 0.0;
			Double listSumCount = 0.0;
			Double listProfitCount = 0.0;
			Double listTaxCount = 0.0;
			Double listNetProfitCount = 0.0;
			params.put("deptname", string);
			map.put("deptname", string);
			// 服务收入
			List<Map<String, Object>> tempServiceSum = rdsFinanceConfigMapper
					.queryDepServiceSum(params);
			if (tempServiceSum.size() == 0) {
				listDeptServiceSumCount = 0.0;
				listDeptServiceSum.add("0");
			} else {
				listDeptServiceSumCount = (Double) (tempServiceSum.get(0) == null ? 0.0
						: tempServiceSum.get(0).get("deptServiceSum"));
				listDeptServiceSum.add(decimalFormat
						.format(listDeptServiceSumCount));
			}
			// 销售收入
			List<Map<String, Object>> tempSellSum = rdsFinanceConfigMapper
					.queryDepSellSum(params);
			if (tempSellSum.size() == 0) {
				listDeptSellSumCount = 0.0;
				listDeptSellSum.add("0");
			} else {
				listDeptSellSumCount = (Double) (tempSellSum.get(0) == null ? 0.0
						: tempSellSum.get(0).get("deptSellSum"));
				listDeptSellSum.add(decimalFormat.format(listDeptSellSumCount));
			}
			// 内部结算收入
			List<Map<String, Object>> tempInsideSum = rdsFinanceConfigMapper
					.queryDeptInsideSum(params);
			if (tempInsideSum.size() == 0) {
				listDeptInsideSumCount = 0.0;
				listDeptInsideSum.add("0");
			} else {
				listDeptInsideSumCount = (Double) (tempInsideSum.get(0) == null ? 0.0
						: tempInsideSum.get(0).get("deptInsideSum"));
				listDeptInsideSum.add(decimalFormat
						.format(listDeptInsideSumCount));
			}
			ListDeptInTaxSum = listDeptServiceSumCount + listDeptSellSumCount;
			// 收入小计（含税）
			listDeptInTaxSum.add(decimalFormat.format(ListDeptInTaxSum));
			// ListDeptTaxSum =
			// listDeptServiceSumCount+listDeptSellSumCount-(listDeptServiceSumCount/1.06+listDeptSellSumCount/1.17);

			String dateFirstIntVal = params.get("confirm_date_start")
					.toString();
			String dateLastIntVal = "2018-05-01";
			if (compare_date(dateFirstIntVal, dateLastIntVal)) {
				ListDeptTaxSum = listDeptServiceSumCount
						+ listDeptSellSumCount
						- (listDeptServiceSumCount / 1.06 + listDeptSellSumCount / 1.16);
				ListDeptOutTaxSum = listDeptServiceSumCount / 1.06
						+ listDeptSellSumCount / 1.16;
			} else {
				ListDeptTaxSum = listDeptServiceSumCount
						+ listDeptSellSumCount
						- (listDeptServiceSumCount / 1.06 + listDeptSellSumCount / 1.17);
				ListDeptOutTaxSum = listDeptServiceSumCount / 1.06
						+ listDeptSellSumCount / 1.17;
			}

			// 收入（税）
			listDeptTaxSum.add(decimalFormat.format(ListDeptTaxSum));
			// ListDeptOutTaxSum =
			// listDeptServiceSumCount/1.06+listDeptSellSumCount/1.17;

			// 收入小计（不含税）,服务收入6个税点，销售收入17个税点
			listDeptOutTaxSum.add(decimalFormat.format(ListDeptOutTaxSum
					+ listDeptInsideSumCount));
			listDeptNull.add("-");
			listDeptNull1.add("-");
			// 人工成本
			List<Map<String, Object>> tempWagesSum = rdsFinanceConfigMapper
					.queryDeptWagesSum(map);
			if (tempWagesSum.size() == 0) {
				listDeptWagesSum.add("0");
			} else {
				ListDeptWagesSum = (Double) (tempWagesSum.get(0) == null ? 0.0
						: tempWagesSum.get(0).get("wagesSum"));
				listDeptWagesSum.add(decimalFormat.format(ListDeptWagesSum));
			}

			// 材料成本
			params.put("amoeba_program", "耗材");
			List<Map<String, Object>> tempMaterialCostSum = rdsFinanceConfigMapper
					.queryDeptMaterialCostSum(params);
			if (tempMaterialCostSum.size() == 0) {
				listDeptMaterialCostSum.add("0");
			} else {
				ListDeptMaterialSum = (Double) (tempMaterialCostSum.get(0) == null ? 0.0
						: tempMaterialCostSum.get(0).get("materialSum"));
				listDeptMaterialCostSum.add(decimalFormat
						.format(ListDeptMaterialSum));
			}

			// 委外检测成本
			params.put("amoeba_program", "委外检测成本");
			List<Map<String, Object>> tempExternalCostSum = rdsFinanceConfigMapper
					.queryDeptExternalCostSum(params);
			if (tempExternalCostSum.size() == 0) {
				listDeptExternalCostSum.add("0");
			} else {
				listDeptExternalSumCount = (Double) (tempExternalCostSum.get(0) == null ? 0.0
						: tempExternalCostSum.get(0).get("deptExternalCostSum"));
				listDeptExternalCostSum.add(decimalFormat
						.format(listDeptExternalSumCount));
			}

			// 内部结算成本
			List<Map<String, Object>> tempCostInsideSum = rdsFinanceConfigMapper
					.queryCostInsideSum(params);
			if (tempCostInsideSum.size() == 0) {
				listDeptCostInsideSum.add("0");
			} else {
				listDeptCostInsideSumCount = (Double) (tempCostInsideSum.get(0) == null ? 0.0
						: tempCostInsideSum.get(0).get("costInsideSum"));
				listDeptCostInsideSum.add(decimalFormat
						.format(listDeptCostInsideSumCount));
			}

			// 销售管理费用
			List<Map<String, Object>> tempSaleManageSum = rdsFinanceConfigMapper
					.querySaleManageSum(params);
			if (tempSaleManageSum.size() == 0) {
				listDeptSaleManageSum.add("0");
			} else {
				listDeptSaleManageSumCount = (Double) (tempSaleManageSum.get(0) == null ? 0.0
						: tempSaleManageSum.get(0).get("deptSaleManageSum"));
				listDeptSaleManageSum.add(decimalFormat
						.format(listDeptSaleManageSumCount));
			}

			// 资质费用
			List<Map<String, Object>> tempDeptAptitudeSum = rdsFinanceConfigMapper
					.queryDeptAptitudeCostSum(params);
			if (tempDeptAptitudeSum.size() == 0) {
				listDeptAptitudeSum.add("0");
			} else {
				listDeptAptitudeSumCount = (Double) (tempDeptAptitudeSum.get(0) == null ? 0.0
						: tempDeptAptitudeSum.get(0).get("deptAptitudeCostSum"));
				listDeptAptitudeSum.add(decimalFormat
						.format(listDeptAptitudeSumCount));
			}

			// 其他费用（含折旧及摊销）
			map.put("amoeba_program", "其他费用（含折旧及摊销）");
			List<Map<String, Object>> tempOtherCostSum = rdsFinanceConfigMapper
					.queryDeptDepreciationCostSum(map);
			if (tempOtherCostSum.size() == 0) {
				listDeptOtherSum.add("0");
			} else {
				listDeptOtherSumCount = (Double) (tempOtherCostSum.get(0) == null ? 0.0
						: tempOtherCostSum.get(0).get("depreciationSum"));
				listDeptOtherSum.add(decimalFormat
						.format(listDeptOtherSumCount));
			}
			// 对外投资
			map.put("amoeba_program", "对外投资");
			List<Map<String, Object>> tempInvestmentCostSum = rdsFinanceConfigMapper
					.queryDeptDepreciationCostSum(map);
			if (tempInvestmentCostSum.size() == 0) {
				listDeptInvestmentSum.add("0");
			} else {
				listDeptInvestmentSumCount = (Double) (tempInvestmentCostSum
						.get(0) == null ? 0.0 : tempInvestmentCostSum.get(0)
						.get("depreciationSum"));
				listDeptInvestmentSum.add(decimalFormat
						.format(listDeptInvestmentSumCount));
			}

			// 仪器设备
			List<Map<String, Object>> tempInstrumentCostSum = rdsFinanceConfigMapper
					.queryDeptInstrumentCostSum(params);
			if (tempInstrumentCostSum.size() == 0) {
				listDeptInstrumentSum.add("0");
			} else {
				listDeptInstrumentSumCount = (Double) (tempInstrumentCostSum
						.get(0) == null ? 0.0 : tempInstrumentCostSum.get(0)
						.get("instrumentSum"));
				listDeptInstrumentSum.add(decimalFormat
						.format(listDeptInstrumentSumCount));
			}

			listDeptServiceSumAllCount += listDeptServiceSumCount;
			listDeptSellSumAllCount += listDeptSellSumCount;
			listDeptInsideSumAllCount += listDeptInsideSumCount;
			ListDeptInTaxAllSum += ListDeptInTaxSum;
			ListDeptTaxAllSum += ListDeptTaxSum;
			ListDeptOutTaxAllSum += ListDeptOutTaxSum;
			ListDeptWagesAllSum += ListDeptWagesSum;
			listDeptMaterialCostAllSum += ListDeptMaterialSum;
			listDeptExternalAllSum += listDeptExternalSumCount;
			listDeptCostInsideAllSum += listDeptCostInsideSumCount;
			listDeptSaleManageAllSum += listDeptSaleManageSumCount;
			listDeptAptitudeAllSum += listDeptAptitudeSumCount;
			listDeptOtherAllSum += listDeptOtherSumCount;
			listDeptInvestmentAllSum += listDeptInvestmentSumCount;
			listDeptInstrumentAllSum += listDeptInstrumentSumCount;

			// 成本小计
			listSumCount = ListDeptWagesSum + ListDeptMaterialSum
					+ listDeptExternalSumCount + listDeptCostInsideSumCount
					+ listDeptSaleManageSumCount + listDeptAptitudeSumCount
					+ listDeptOtherSumCount;
			listSum.add(decimalFormat.format(listSumCount));
			listProfitCount = ListDeptOutTaxSum + listDeptInsideSumCount
					- listSumCount;
			listProfit.add(decimalFormat.format(listProfitCount));
			listTaxCount = listProfitCount * 0.2;
			listTax.add(decimalFormat.format(listTaxCount));
			listNetProfitCount = listProfitCount - listTaxCount;
			listNetProfit.add(decimalFormat.format(listNetProfitCount));

			listSumAllCount += listSumCount;
			listProfitAllCount += listProfitCount;
			listTaxAllCount += listTaxCount;
			listNetProfitAllCount += listNetProfitCount;

		}

		listDept.add("合计");
		// list.add(listDept);
		// 服务收入
		listDeptServiceSum
				.add(decimalFormat.format(listDeptServiceSumAllCount));
		list.add(listDeptServiceSum);
		// 销售收入
		listDeptSellSum.add(decimalFormat.format(listDeptSellSumAllCount));
		list.add(listDeptSellSum);
		// 内部结算收入
		listDeptInsideSum.add(decimalFormat.format(listDeptInsideSumAllCount));
		list.add(listDeptInsideSum);
		// 收入小计（含税）
		listDeptInTaxSum.add(decimalFormat.format(ListDeptInTaxAllSum));
		list.add(listDeptInTaxSum);
		// 收入小计（税）
		listDeptTaxSum.add(decimalFormat.format(ListDeptTaxAllSum));
		list.add(listDeptTaxSum);
		// 收入小计（不含税）
		listDeptOutTaxSum.add(decimalFormat.format(ListDeptOutTaxAllSum
				+ listDeptInsideSumAllCount));
		list.add(listDeptOutTaxSum);
		// 空格
		listDeptNull.add("-");
		list.add(listDeptNull);
		// 人工成本
		listDeptWagesSum.add(decimalFormat.format(ListDeptWagesAllSum));
		list.add(listDeptWagesSum);
		// 材料成本
		listDeptMaterialCostSum.add(decimalFormat
				.format(listDeptMaterialCostAllSum));
		list.add(listDeptMaterialCostSum);
		// 委外成本
		listDeptExternalCostSum.add(decimalFormat
				.format(listDeptExternalAllSum));
		list.add(listDeptExternalCostSum);
		// 内部结算成本
		listDeptCostInsideSum.add(decimalFormat
				.format(listDeptCostInsideAllSum));
		list.add(listDeptCostInsideSum);
		// 销售管理成本
		listDeptSaleManageSum.add(decimalFormat
				.format(listDeptSaleManageAllSum));
		list.add(listDeptSaleManageSum);
		// 资质费用
		listDeptAptitudeSum.add(decimalFormat.format(listDeptAptitudeAllSum));
		list.add(listDeptAptitudeSum);
		// 其他费用（含折旧及摊销）
		listDeptOtherSum.add(decimalFormat.format(listDeptOtherAllSum));
		list.add(listDeptOtherSum);
		// 对外投资
		listDeptInvestmentSum.add(decimalFormat
				.format(listDeptInvestmentAllSum));
		list.add(listDeptInvestmentSum);
		// 仪器设备
		listDeptInstrumentSum.add(decimalFormat
				.format(listDeptInstrumentAllSum));
		list.add(listDeptInstrumentSum);
		// 成本小计
		listSum.add(decimalFormat.format(listSumAllCount));
		list.add(listSum);
		// 空格
		listDeptNull1.add("-");
		list.add(listDeptNull1);
		// 利润小计
		listProfit.add(decimalFormat.format(listProfitAllCount));
		list.add(listProfit);
		// 税
		listTax.add(decimalFormat.format(listTaxAllCount));
		list.add(listTax);
		// 净利润小计
		listNetProfit.add(decimalFormat.format(listNetProfitAllCount));
		list.add(listNetProfit);

		String filename = "阿米巴";

		// 导出excel头列表
		Object[] titles = new Object[listDept.size()];
		for (int i = 0; i < listDept.size(); i++) {
			titles[i] = listDept.get(i);
		}
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			List<String> model = list.get(i);
			for (int j = 0; j < model.size(); j++)
				objects.add(model.get(j));
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "阿米巴");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exportAmoebaSecond(Map<String, Object> params,
			HttpServletResponse response) {
		List<List<String>> list = new ArrayList<>();
		List<String> listDeptTemp = new ArrayList<String>();
		// 部门列表
		if (params.get("listDeptTemp") != null)
			listDeptTemp = (List<String>) params.get("listDeptTemp");
		else
			listDeptTemp = rdsFinanceConfigMapper.queryUserDeptSecond(params);
		List<String> listDept = new ArrayList<>();
		listDept.add("");
		for (String string : listDeptTemp) {
			listDept.add(string);
		}
		// 部门服务收入
		List<String> listDeptServiceSum = new ArrayList<>();
		listDeptServiceSum.add("服务收入");
		// 部门销售收入
		List<String> listDeptSellSum = new ArrayList<>();
		listDeptSellSum.add("销售收入");
		// 部门内部结算收入
		// List<String> listDeptInsideSum = new ArrayList<>();
		// listDeptInsideSum.add("内部结算收入");
		// 部门收入小计（含税）
		List<String> listDeptInTaxSum = new ArrayList<>();
		listDeptInTaxSum.add("收入小计(含税)");
		// 部门收入小计（税）
		List<String> listDeptTaxSum = new ArrayList<>();
		listDeptTaxSum.add("税");
		// 部门收入小计（不含税）
		List<String> listDeptOutTaxSum = new ArrayList<>();
		listDeptOutTaxSum.add("收入小计（不含税）");
		// 委外检测成本
		List<String> listDeptExternalCostSum = new ArrayList<>();
		listDeptExternalCostSum.add("委外检测成本");
		// 内部结算成本
		List<String> listDeptCostInsideSum = new ArrayList<>();
		listDeptCostInsideSum.add("内部结算成本");
		// 内部结算按照月份计算
		Map<String, Object> map = new HashMap<>();
		map.put("confirm_date_start", params.get("confirm_date_start")
				.toString().substring(0, 7));
		map.put("confirm_date_end", params.get("confirm_date_end").toString()
				.substring(0, 7));
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置

		Double listDeptServiceSumAllCount = 0.0;
		Double listDeptSellSumAllCount = 0.0;
		Double listDeptInsideSumAllCount = 0.0;
		Double ListDeptInTaxAllSum = 0.0;
		Double ListDeptTaxAllSum = 0.0;
		Double ListDeptOutTaxAllSum = 0.0;
		Double listDeptExternalAllSum = 0.0;
		Double listDeptCostInsideAllSum = 0.0;
		for (String string : listDeptTemp) {
			Double listDeptServiceSumCount = 0.0;
			Double listDeptSellSumCount = 0.0;
			Double listDeptInsideSumCount = 0.0;
			Double ListDeptInTaxSum = 0.0;
			Double ListDeptTaxSum = 0.0;
			Double ListDeptOutTaxSum = 0.0;
			Double listDeptExternalSumCount = 0.0;
			Double listDeptCostInsideSumCount = 0.0;
			params.put("deptname", string);
			map.put("deptname", string);
			// 服务收入
			List<Map<String, Object>> tempServiceSum = rdsFinanceConfigMapper
					.queryDepServiceSumSecond(params);
			if (tempServiceSum.size() == 0) {
				listDeptServiceSumCount = 0.0;
				listDeptServiceSum.add("0");
			} else {
				listDeptServiceSumCount = (Double) (tempServiceSum.get(0) == null ? 0.0
						: tempServiceSum.get(0).get("deptServiceSum"));
				listDeptServiceSum.add(decimalFormat
						.format(listDeptServiceSumCount));
			}
			// 销售收入
			List<Map<String, Object>> tempSellSum = rdsFinanceConfigMapper
					.queryDepSellSumSecond(params);
			if (tempSellSum.size() == 0) {
				listDeptSellSumCount = 0.0;
				listDeptSellSum.add("0");
			} else {
				listDeptSellSumCount = (Double) (tempSellSum.get(0) == null ? 0.0
						: tempSellSum.get(0).get("deptSellSum"));
				listDeptSellSum.add(decimalFormat.format(listDeptSellSumCount));
			}

			ListDeptInTaxSum = listDeptServiceSumCount + listDeptSellSumCount;
			// 收入小计（含税）
			listDeptInTaxSum.add(decimalFormat.format(ListDeptInTaxSum));
			// ListDeptTaxSum =
			// listDeptServiceSumCount+listDeptSellSumCount-(listDeptServiceSumCount/1.06+listDeptSellSumCount/1.17);
			String dateFirstIntVal = params.get("confirm_date_start")
					.toString();
			String dateLastIntVal = "2018-05-01";
			if (compare_date(dateFirstIntVal, dateLastIntVal)) {
				ListDeptTaxSum = listDeptServiceSumCount
						+ listDeptSellSumCount
						- (listDeptServiceSumCount / 1.06 + listDeptSellSumCount / 1.16);
				ListDeptOutTaxSum = listDeptServiceSumCount / 1.06
						+ listDeptSellSumCount / 1.16;
			} else {
				ListDeptTaxSum = listDeptServiceSumCount
						+ listDeptSellSumCount
						- (listDeptServiceSumCount / 1.06 + listDeptSellSumCount / 1.17);
				ListDeptOutTaxSum = listDeptServiceSumCount / 1.06
						+ listDeptSellSumCount / 1.17;
			}

			// 收入（税）
			listDeptTaxSum.add(decimalFormat.format(ListDeptTaxSum));
			// ListDeptOutTaxSum =
			// listDeptServiceSumCount/1.06+listDeptSellSumCount/1.17;

			// 收入小计（不含税）,服务收入6个税点，销售收入17个税点
			listDeptOutTaxSum.add(decimalFormat.format(ListDeptOutTaxSum));

			// 委外检测成本
			List<Map<String, Object>> tempExternalCostSum = rdsFinanceConfigMapper
					.queryDeptExternalCostSumSecond(params);
			if (tempSellSum.size() == 0) {
				listDeptExternalCostSum.add("0");
			} else {
				listDeptExternalSumCount = (Double) (tempExternalCostSum.get(0) == null ? 0.0
						: tempExternalCostSum.get(0).get("deptExternalCostSum"));
				listDeptExternalCostSum.add(decimalFormat
						.format(listDeptExternalSumCount));
			}

			// 内部结算成本
			List<Map<String, Object>> tempCostInsideSum = rdsFinanceConfigMapper
					.queryCostInsideSumSecond(params);
			if (tempSellSum.size() == 0) {
				listDeptCostInsideSum.add("0");
			} else {
				listDeptCostInsideSumCount = (Double) (tempCostInsideSum.get(0) == null ? 0.0
						: tempCostInsideSum.get(0).get("costInsideSum"));
				listDeptCostInsideSum.add(decimalFormat
						.format(listDeptCostInsideSumCount));
			}
			listDeptServiceSumAllCount += listDeptServiceSumCount;
			listDeptSellSumAllCount += listDeptSellSumCount;
			listDeptInsideSumAllCount += listDeptInsideSumCount;
			ListDeptInTaxAllSum += ListDeptInTaxSum;
			ListDeptTaxAllSum += ListDeptTaxSum;
			ListDeptOutTaxAllSum += ListDeptOutTaxSum;
			listDeptExternalAllSum += listDeptExternalSumCount;
			listDeptCostInsideAllSum += listDeptCostInsideSumCount;
		}

		listDept.add("合计");
		// list.add(listDept);
		// 服务收入
		listDeptServiceSum
				.add(decimalFormat.format(listDeptServiceSumAllCount));
		list.add(listDeptServiceSum);
		// 销售收入
		listDeptSellSum.add(decimalFormat.format(listDeptSellSumAllCount));
		list.add(listDeptSellSum);
		// 内部结算收入
		// listDeptInsideSum.add(decimalFormat.format(listDeptInsideSumAllCount));
		// list.add(listDeptInsideSum);
		// 收入小计（含税）
		listDeptInTaxSum.add(decimalFormat.format(ListDeptInTaxAllSum));
		list.add(listDeptInTaxSum);
		// 收入小计（税）
		listDeptTaxSum.add(decimalFormat.format(ListDeptTaxAllSum));
		list.add(listDeptTaxSum);
		// 收入小计（不含税）
		listDeptOutTaxSum.add(decimalFormat.format(ListDeptOutTaxAllSum));
		list.add(listDeptOutTaxSum);
		// 委外成本
		listDeptExternalCostSum.add(decimalFormat
				.format(listDeptExternalAllSum));
		list.add(listDeptExternalCostSum);
		// 内部结算成本
		listDeptCostInsideSum.add(decimalFormat
				.format(listDeptCostInsideAllSum));
		list.add(listDeptCostInsideSum);

		String filename = "阿米巴";

		// 导出excel头列表
		Object[] titles = new Object[listDept.size()];
		for (int i = 0; i < listDept.size(); i++) {
			titles[i] = listDept.get(i);
		}
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			List<String> model = list.get(i);
			for (int j = 0; j < model.size(); j++)
				objects.add(model.get(j));
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "阿米巴");
	}

	@Override
	public List<RdsStatisticsDepartmentModel> queryDepartmentAll(
			Map<String, Object> params) {
		// 查询出当前所有部门
		List<RdsStatisticsDepartmentModel> list = rdsFinanceConfigMapper
				.queryDepartmentAll(params);
		// 内部结算按照月份计算
		Map<String, Object> map = new HashMap<>();
		map.put("confirm_date_start",
				"".equals(params.get("confirm_date_start")) ? "" : params
						.get("confirm_date_start").toString().substring(0, 7));
		map.put("confirm_date_end",
				"".equals(params.get("confirm_date_end")) ? "" : params
						.get("confirm_date_end").toString().substring(0, 7));
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置
		Double listDeptServiceSumAllCount = 0.0;
		Double listDeptSellSumAllCount = 0.0;
		Double listDeptInsideSumAllCount = 0.0;
		Double ListDeptInTaxAllSum = 0.0;
		Double ListDeptTaxAllSum = 0.0;
		Double ListDeptOutTaxAllSum = 0.0;
		Double ListDeptWagesAllSum = 0.0;
		Double listDeptMaterialCostAllSum = 0.0;
		Double listDeptExternalAllSum = 0.0;
		Double listDeptCostInsideAllSum = 0.0;
		Double listDeptSaleManageAllSum = 0.0;
		Double listDeptAptitudeAllSum = 0.0;
		Double listDeptOtherAllSum = 0.0;
		Double listDeptInvestmentAllSum = 0.0;
		Double listDeptInstrumentAllSum = 0.0;
		Double listSumAllCount = 0.0;
		Double listProfitAllCount = 0.0;
		Double listTaxAllCount = 0.0;
		Double listNetProfitAllCount = 0.0;
		String parentcode = params.get("parentcode").toString();
		for (RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel : list) {
			Double listDeptServiceSumCount = 0.0;
			Double listDeptSellSumCount = 0.0;
			Double listDeptInsideSumCount = 0.0;
			Double ListDeptInTaxSum = 0.0;
			Double ListDeptTaxSum = 0.0;
			Double ListDeptOutTaxSum = 0.0;
			Double ListDeptWagesSum = 0.0;
			Double ListDeptMaterialSum = 0.0;
			Double listDeptExternalSumCount = 0.0;
			Double listDeptCostInsideSumCount = 0.0;
			Double listDeptSaleManageSumCount = 0.0;
			Double listDeptAptitudeSumCount = 0.0;
			Double listDeptOtherSumCount = 0.0;
			Double listDeptInvestmentSumCount = 0.0;
			Double listDeptInstrumentSumCount = 0.0;
			Double listSumCount = 0.0;
			Double listProfitCount = 0.0;
			Double listTaxCount = 0.0;
			Double listNetProfitCount = 0.0;

			if (!"0".equals(parentcode)) {
				map.put("deptid", parentcode);
				Map<String, String> maps = rdsFinanceConfigMapper
						.queryUserDepartment(map).get(0);
				// 二级部门为空，当前根路径为一级部门
				if (null == maps.get("user_dept_level2")) {
					map.put("deptname", maps.get("user_dept_level1"));
					params.put("deptname", maps.get("user_dept_level1"));
					map.put("deptname1",
							rdsStatisticsDepartmentModel.getDeptname());
					params.put("deptname1",
							rdsStatisticsDepartmentModel.getDeptname());
				} else if (null == maps.get("user_dept_level3")
						&& null != maps.get("user_dept_level2")) {
					map.put("deptname", maps.get("user_dept_level1"));
					params.put("deptname", maps.get("user_dept_level1"));
					map.put("deptname1", maps.get("user_dept_level2"));
					params.put("deptname1", maps.get("user_dept_level2"));
					map.put("deptname2",
							rdsStatisticsDepartmentModel.getDeptname());
					params.put("deptname2",
							rdsStatisticsDepartmentModel.getDeptname());
				} else if (null == maps.get("user_dept_level4")
						&& null != maps.get("user_dept_level3")) {
					map.put("deptname", maps.get("user_dept_level1"));
					params.put("deptname", maps.get("user_dept_level1"));
					map.put("deptname1", maps.get("user_dept_level2"));
					params.put("deptname1", maps.get("user_dept_level2"));
					map.put("deptname2", maps.get("user_dept_level3"));
					params.put("deptname2", maps.get("user_dept_level3"));
					map.put("deptname3",
							rdsStatisticsDepartmentModel.getDeptname());
					params.put("deptname3",
							rdsStatisticsDepartmentModel.getDeptname());
				} else if (null == maps.get("user_dept_level5")
						&& null != maps.get("user_dept_level4")) {
					map.put("deptname", maps.get("user_dept_level1"));
					params.put("deptname", maps.get("user_dept_level1"));
					map.put("deptname1", maps.get("user_dept_level2"));
					params.put("deptname1", maps.get("user_dept_level2"));
					map.put("deptname2", maps.get("user_dept_level3"));
					params.put("deptname2", maps.get("user_dept_level3"));
					map.put("deptname3", maps.get("user_dept_level4"));
					params.put("deptname3", maps.get("user_dept_level4"));
					map.put("deptname4",
							rdsStatisticsDepartmentModel.getDeptname());
					params.put("deptname4",
							rdsStatisticsDepartmentModel.getDeptname());
				}
			} else {
				map.put("deptname", rdsStatisticsDepartmentModel.getDeptname());
				params.put("deptname",
						rdsStatisticsDepartmentModel.getDeptname());
			}
			// 服务收入
			List<Map<String, Object>> tempServiceSum = rdsFinanceConfigMapper
					.queryDepServiceSum(params);
			if (tempServiceSum.size() == 0) {
				listDeptServiceSumCount = 0.0;
				rdsStatisticsDepartmentModel.setServiceSum("-");
			} else {
				listDeptServiceSumCount = (Double) (tempServiceSum.get(0) == null ? 0.0
						: tempServiceSum.get(0).get("deptServiceSum"));
				rdsStatisticsDepartmentModel
						.setServiceSum(listDeptServiceSumCount != 0 ? decimalFormat
								.format(listDeptServiceSumCount) : "-");
			}
			// 销售收入
			List<Map<String, Object>> tempSellSum = rdsFinanceConfigMapper
					.queryDepSellSum(params);
			if (tempSellSum.size() == 0) {
				listDeptSellSumCount = 0.0;
				rdsStatisticsDepartmentModel.setSellSum("-");
			} else {
				listDeptSellSumCount = (Double) (tempSellSum.get(0) == null ? 0.0
						: tempSellSum.get(0).get("deptSellSum"));
				rdsStatisticsDepartmentModel
						.setSellSum(listDeptSellSumCount != 0 ? decimalFormat
								.format(listDeptSellSumCount) : "-");
			}
			// 内部结算收入
			// 事业部查看特殊处理
			if ("2".equals(params.get("level"))) {
				Map<String, Object> maptemp = new HashMap<>();
				maptemp.put("deptname", params.get("deptname1"));
				maptemp.put("confirm_date_start",
						params.get("confirm_date_start"));
				maptemp.put("confirm_date_end", params.get("confirm_date_end"));
				List<Map<String, Object>> tempInsideSum = rdsFinanceConfigMapper
						.queryDeptInsideSum(maptemp);
				if (tempInsideSum.size() == 0) {
					listDeptInsideSumCount = 0.0;
					rdsStatisticsDepartmentModel.setInsideSum("-");
				} else {
					listDeptInsideSumCount = (Double) (tempInsideSum.get(0) == null ? 0.0
							: tempInsideSum.get(0).get("deptInsideSum"));
					rdsStatisticsDepartmentModel
							.setInsideSum(listDeptInsideSumCount != 0 ? decimalFormat
									.format(listDeptInsideSumCount) : "-");
				}
			} else {
				List<Map<String, Object>> tempInsideSum = rdsFinanceConfigMapper
						.queryDeptInsideSum(params);
				if (tempInsideSum.size() == 0) {
					listDeptInsideSumCount = 0.0;
					rdsStatisticsDepartmentModel.setInsideSum("-");
				} else {
					// 内部结算只到事业部
					if (null == params.get("deptname1")) {
						listDeptInsideSumCount = (Double) (tempInsideSum.get(0) == null ? 0.0
								: tempInsideSum.get(0).get("deptInsideSum"));
						rdsStatisticsDepartmentModel
								.setInsideSum(listDeptInsideSumCount != 0 ? decimalFormat
										.format(listDeptInsideSumCount) : "-");
					} else {
						listDeptInsideSumCount = 0.0;
						rdsStatisticsDepartmentModel.setInsideSum("-");
					}
				}
			}
			ListDeptInTaxSum = listDeptServiceSumCount + listDeptSellSumCount;
			// 收入小计（含税）
			rdsStatisticsDepartmentModel
					.setDeptInTaxSum(ListDeptInTaxSum != 0 ? decimalFormat
							.format(ListDeptInTaxSum) : "-");
			String dateFirstIntVal = params.get("confirm_date_start")
					.toString();
			String dateLastIntVal = "2018-05-01";
			if (compare_date(dateFirstIntVal, dateLastIntVal)) {
				ListDeptTaxSum = listDeptServiceSumCount
						+ listDeptSellSumCount
						- (listDeptServiceSumCount / 1.06 + listDeptSellSumCount / 1.16);
				ListDeptOutTaxSum = listDeptServiceSumCount / 1.06
						+ listDeptSellSumCount / 1.16;
			} else {
				ListDeptTaxSum = listDeptServiceSumCount
						+ listDeptSellSumCount
						- (listDeptServiceSumCount / 1.06 + listDeptSellSumCount / 1.17);
				ListDeptOutTaxSum = listDeptServiceSumCount / 1.06
						+ listDeptSellSumCount / 1.17;
			}
			// ListDeptTaxSum =
			// listDeptServiceSumCount+listDeptSellSumCount-(listDeptServiceSumCount/1.06+listDeptSellSumCount/1.17);
			// 收入（税）
			rdsStatisticsDepartmentModel
					.setTaxSum(ListDeptTaxSum != 0 ? decimalFormat
							.format(ListDeptTaxSum) : "-");

			// ListDeptOutTaxSum =
			// listDeptServiceSumCount/1.06+listDeptSellSumCount/1.17;
			// 收入小计（不含税）,服务收入6个税点，销售收入17个税点
			rdsStatisticsDepartmentModel
					.setDeptOutTaxSum((ListDeptOutTaxSum + listDeptInsideSumCount) != 0 ? decimalFormat
							.format(ListDeptOutTaxSum + listDeptInsideSumCount)
							: "-");

			// 人工成本
			List<Map<String, Object>> tempWagesSum = rdsFinanceConfigMapper
					.queryDeptWagesSum(map);
			if (tempWagesSum.size() == 0) {
				rdsStatisticsDepartmentModel.setDeptWagesSum("-");
			} else {
				ListDeptWagesSum = (Double) (tempWagesSum.get(0) == null ? 0.0
						: tempWagesSum.get(0).get("wagesSum"));
				rdsStatisticsDepartmentModel
						.setDeptWagesSum(ListDeptWagesSum != 0 ? decimalFormat
								.format(ListDeptWagesSum) : "-");
			}

			// 材料成本
			params.put("amoeba_program", "耗材");
			List<Map<String, Object>> tempMaterialCostSum = rdsFinanceConfigMapper
					.queryDeptMaterialCostSum(params);
			if (tempMaterialCostSum.size() == 0) {
				rdsStatisticsDepartmentModel.setDeptMaterialCostSum("-");
			} else {
				ListDeptMaterialSum = (Double) (tempMaterialCostSum.get(0) == null ? 0.0
						: tempMaterialCostSum.get(0).get("materialSum"));
				rdsStatisticsDepartmentModel
						.setDeptMaterialCostSum(ListDeptMaterialSum != 0 ? decimalFormat
								.format(ListDeptMaterialSum) : "-");
			}

			// 委外检测成本
			params.put("amoeba_program", "委外检测成本");
			List<Map<String, Object>> tempExternalCostSum = rdsFinanceConfigMapper
					.queryDeptExternalCostSum(params);
			if (tempExternalCostSum.size() == 0) {
				rdsStatisticsDepartmentModel.setDeptExternalCostSum("-");
			} else {
				listDeptExternalSumCount = (Double) (tempExternalCostSum.get(0) == null ? 0.0
						: tempExternalCostSum.get(0).get("deptExternalCostSum"));
				rdsStatisticsDepartmentModel
						.setDeptExternalCostSum(listDeptExternalSumCount != 0 ? decimalFormat
								.format(listDeptExternalSumCount) : "-");
			}

			// 内部结算成本
			List<Map<String, Object>> tempCostInsideSum = rdsFinanceConfigMapper
					.queryCostInsideSum(params);
			if (tempCostInsideSum.size() == 0) {
				rdsStatisticsDepartmentModel.setDeptCostInsideSum("-");
			} else {
				listDeptCostInsideSumCount = (Double) (tempCostInsideSum.get(0) == null ? 0.0
						: tempCostInsideSum.get(0).get("costInsideSum"));
				rdsStatisticsDepartmentModel
						.setDeptCostInsideSum(listDeptCostInsideSumCount != 0 ? decimalFormat
								.format(listDeptCostInsideSumCount) : "-");
			}

			// 销售管理费用
			List<Map<String, Object>> tempSaleManageSum = rdsFinanceConfigMapper
					.querySaleManageSum(params);
			if (tempSaleManageSum.size() == 0) {
				rdsStatisticsDepartmentModel.setDeptSaleManageSum("-");
			} else {
				listDeptSaleManageSumCount = (Double) (tempSaleManageSum.get(0) == null ? 0.0
						: tempSaleManageSum.get(0).get("deptSaleManageSum"));
				rdsStatisticsDepartmentModel
						.setDeptSaleManageSum(listDeptSaleManageSumCount != 0 ? decimalFormat
								.format(listDeptSaleManageSumCount) : "-");
			}

			// 资质费用
			List<Map<String, Object>> tempDeptAptitudeSum = rdsFinanceConfigMapper
					.queryDeptAptitudeCostSum(params);
			if (tempDeptAptitudeSum.size() == 0) {
				rdsStatisticsDepartmentModel.setDeptAptitudeSum("-");
			} else {
				listDeptAptitudeSumCount = (Double) (tempDeptAptitudeSum.get(0) == null ? 0.0
						: tempDeptAptitudeSum.get(0).get("deptAptitudeCostSum"));
				rdsStatisticsDepartmentModel
						.setDeptAptitudeSum(listDeptAptitudeSumCount != 0 ? decimalFormat
								.format(listDeptAptitudeSumCount) : "-");
			}

			// 其他费用（含折旧及摊销）
			map.put("amoeba_program", "其他费用（含折旧及摊销）");
			List<Map<String, Object>> tempOtherCostSum = rdsFinanceConfigMapper
					.queryDeptDepreciationCostSum(map);
			if (tempOtherCostSum.size() == 0) {
				rdsStatisticsDepartmentModel.setDeptOtherSum("-");
			} else {
				listDeptOtherSumCount = (Double) (tempOtherCostSum.get(0) == null ? 0.0
						: tempOtherCostSum.get(0).get("depreciationSum"));
				rdsStatisticsDepartmentModel
						.setDeptOtherSum(listDeptOtherSumCount != 0 ? decimalFormat
								.format(listDeptOtherSumCount) : "-");
			}

			// 对外投资
			map.put("amoeba_program", "对外投资");
			List<Map<String, Object>> tempInvestmentCostSum = rdsFinanceConfigMapper
					.queryDeptDepreciationCostSum(map);
			if (tempInvestmentCostSum.size() == 0) {
				rdsStatisticsDepartmentModel.setDeptInvestmentSum("-");
			} else {
				listDeptInvestmentSumCount = (Double) (tempInvestmentCostSum
						.get(0) == null ? 0.0 : tempInvestmentCostSum.get(0)
						.get("depreciationSum"));
				rdsStatisticsDepartmentModel
						.setDeptInvestmentSum(listDeptInvestmentSumCount != 0 ? decimalFormat
								.format(listDeptInvestmentSumCount) : "-");
			}

			// 仪器设备
			List<Map<String, Object>> tempInstrumentCostSum = rdsFinanceConfigMapper
					.queryDeptInstrumentCostSum(params);
			if (tempInstrumentCostSum.size() == 0) {
				rdsStatisticsDepartmentModel.setDeptInstrumentSum("-");
			} else {
				listDeptInstrumentSumCount = (Double) (tempInstrumentCostSum
						.get(0) == null ? 0.0 : tempInstrumentCostSum.get(0)
						.get("instrumentSum"));
				rdsStatisticsDepartmentModel
						.setDeptInstrumentSum(listDeptInstrumentSumCount != 0 ? decimalFormat
								.format(listDeptInstrumentSumCount) : "-");
			}

			listDeptServiceSumAllCount += listDeptServiceSumCount;
			listDeptSellSumAllCount += listDeptSellSumCount;
			listDeptInsideSumAllCount += listDeptInsideSumCount;
			ListDeptInTaxAllSum += ListDeptInTaxSum;
			ListDeptTaxAllSum += ListDeptTaxSum;
			ListDeptOutTaxAllSum += ListDeptOutTaxSum;
			ListDeptWagesAllSum += ListDeptWagesSum;
			listDeptMaterialCostAllSum += ListDeptMaterialSum;
			listDeptExternalAllSum += listDeptExternalSumCount;
			listDeptCostInsideAllSum += listDeptCostInsideSumCount;
			listDeptSaleManageAllSum += listDeptSaleManageSumCount;
			listDeptAptitudeAllSum += listDeptAptitudeSumCount;
			listDeptOtherAllSum += listDeptOtherSumCount;
			listDeptInvestmentAllSum += listDeptInvestmentSumCount;
			listDeptInstrumentAllSum += listDeptInstrumentSumCount;

			// 成本小计
			listSumCount = ListDeptWagesSum + ListDeptMaterialSum
					+ listDeptExternalSumCount + listDeptCostInsideSumCount
					+ listDeptSaleManageSumCount + listDeptAptitudeSumCount
					+ listDeptOtherSumCount;
			rdsStatisticsDepartmentModel
					.setDeptCostSum(listSumCount != 0 ? decimalFormat
							.format(listSumCount) : "-");
			// 利润小计
			listProfitCount = ListDeptOutTaxSum + listDeptInsideSumCount
					- listSumCount;
			rdsStatisticsDepartmentModel
					.setDeptProfit(listProfitCount != 0 ? decimalFormat
							.format(listProfitCount) : "-");
			// 税
			listTaxCount = listProfitCount * 0.2;
			rdsStatisticsDepartmentModel
					.setDeptTax(listTaxCount != 0 ? decimalFormat
							.format(listTaxCount) : "-");
			// 净利润小计
			listNetProfitCount = listProfitCount - listTaxCount;
			rdsStatisticsDepartmentModel
					.setDeptNetProfit(listNetProfitCount != 0 ? decimalFormat
							.format(listNetProfitCount) : "-");

			listSumAllCount += listSumCount;
			listProfitAllCount += listProfitCount;
			listTaxAllCount += listTaxCount;
			listNetProfitAllCount += listNetProfitCount;
		}
		if ("0".equals(parentcode) || null != params.get("allFlag")) {
			RdsStatisticsDepartmentModel model = new RdsStatisticsDepartmentModel();
			// 合计处理
			if (!"0".equals(parentcode)) {
				model.setParentname(params.get("deptname").toString());
			} else {
				model.setParentname("");
			}
			model.setDeptname("合计");
			// 服务收入
			model.setServiceSum(listDeptServiceSumAllCount != 0 ? decimalFormat
					.format(listDeptServiceSumAllCount) : "-");
			// 销售收入
			model.setSellSum(listDeptSellSumAllCount != 0 ? decimalFormat
					.format(listDeptSellSumAllCount) : "-");
			// 内部结算收入
			model.setInsideSum(listDeptInsideSumAllCount != 0 ? decimalFormat
					.format(listDeptInsideSumAllCount) : "-");
			// 收入小计（含税）
			model.setDeptInTaxSum(ListDeptInTaxAllSum != 0 ? decimalFormat
					.format(ListDeptInTaxAllSum) : "-");
			// 收入小计（税）
			model.setTaxSum(ListDeptTaxAllSum != 0 ? decimalFormat
					.format(ListDeptTaxAllSum) : "-");
			// 收入小计（不含税）
			model.setDeptOutTaxSum((ListDeptOutTaxAllSum + listDeptInsideSumAllCount) != 0 ? decimalFormat
					.format(ListDeptOutTaxAllSum + listDeptInsideSumAllCount)
					: "-");
			// 人工成本
			model.setDeptWagesSum(ListDeptWagesAllSum != 0 ? decimalFormat
					.format(ListDeptWagesAllSum) : "-");
			// 材料成本
			model.setDeptMaterialCostSum(listDeptMaterialCostAllSum != 0 ? decimalFormat
					.format(listDeptMaterialCostAllSum) : "-");
			// 委外成本
			model.setDeptExternalCostSum(listDeptExternalAllSum != 0 ? decimalFormat
					.format(listDeptExternalAllSum) : "-");
			// 内部结算成本
			model.setDeptCostInsideSum(listDeptCostInsideAllSum != 0 ? decimalFormat
					.format(listDeptCostInsideAllSum) : "-");
			// 销售管理成本
			model.setDeptSaleManageSum(listDeptSaleManageAllSum != 0 ? decimalFormat
					.format(listDeptSaleManageAllSum) : "-");
			// 资质费用
			model.setDeptAptitudeSum(listDeptAptitudeAllSum != 0 ? decimalFormat
					.format(listDeptAptitudeAllSum) : "-");
			// 其他费用（含折旧及摊销）
			model.setDeptOtherSum(listDeptOtherAllSum != 0 ? decimalFormat
					.format(listDeptOtherAllSum) : "-");
			// 对外投资
			model.setDeptInvestmentSum(listDeptInvestmentAllSum != 0 ? decimalFormat
					.format(listDeptInvestmentAllSum) : "-");
			// 仪器设备
			model.setDeptInstrumentSum(listDeptInstrumentAllSum != 0 ? decimalFormat
					.format(listDeptInstrumentAllSum) : "-");
			// 成本小计
			model.setDeptCostSum(listSumAllCount != 0 ? decimalFormat
					.format(listSumAllCount) : "-");
			// 利润小计
			model.setDeptProfit(listProfitAllCount != 0 ? decimalFormat
					.format(listProfitAllCount) : "-");
			// 税
			model.setDeptTax(listTaxAllCount != 0 ? decimalFormat
					.format(listTaxAllCount) : "-");
			// 净利润小计
			model.setDeptNetProfit(listNetProfitAllCount != 0 ? decimalFormat
					.format(listNetProfitAllCount) : "-");
			model.setLeaf(true);
			list.add(model);
		}

		return list;
	}

	@Override
	public void exportAmoebaTree(Map<String, Object> params,
			HttpServletResponse httpResponse) throws Exception {
		// 查询出当前所有部门
		List<RdsStatisticsDepartmentModel> listAll = new ArrayList<>();
		// 查询出当前所有部门
		List<RdsStatisticsDepartmentModel> list = rdsFinanceConfigMapper
				.queryDepartmentAll(params);
		// 内部结算按照月份计算
		Map<String, Object> map = new HashMap<>();
		map.put("confirm_date_start",
				"".equals(params.get("confirm_date_start")) ? "" : params
						.get("confirm_date_start").toString().substring(0, 7));
		map.put("confirm_date_end",
				"".equals(params.get("confirm_date_end")) ? "" : params
						.get("confirm_date_end").toString().substring(0, 7));
		String parentcode = params.get("parentcode").toString();
		// 一级部门
		for (RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel : list) {
			// 存入一级部门数据
			listAll.add(amoebaDepart(params, map, parentcode,
					rdsStatisticsDepartmentModel, ""));

			Map<String, Object> params1 = new HashMap<>();
			params1.put("confirm_date_start", params.get("confirm_date_start"));
			params1.put("confirm_date_end", params.get("confirm_date_end"));
			params1.put("level", params.get("level"));

			Map<String, Object> map1 = new HashMap<>();
			map1.put("confirm_date_start", map.get("confirm_date_start"));
			map1.put("confirm_date_end", map.get("confirm_date_end"));
			// 判断是否有二级部门
			if (!rdsStatisticsDepartmentModel.isLeaf()) {
				String parentcode1 = rdsStatisticsDepartmentModel.getDeptid();
				// 当前部门id
				params1.put("parentcode", parentcode1);
				// 查询出当前所有部门
				List<RdsStatisticsDepartmentModel> list1 = rdsFinanceConfigMapper
						.queryDepartmentAll(params1);
				// 遍历二级部门
				for (RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel2 : list1) {
					// 存入二级部门数据
					rdsStatisticsDepartmentModel2
							.setDeptname(rdsStatisticsDepartmentModel2
									.getDeptname());
					listAll.add(amoebaDepart(params1, map1, parentcode1,
							rdsStatisticsDepartmentModel2, "（二级）"));
					Map<String, Object> params2 = new HashMap<>();
					params2.put("confirm_date_start",
							params.get("confirm_date_start"));
					params2.put("confirm_date_end",
							params.get("confirm_date_end"));
					params2.put("level", params.get("level"));

					Map<String, Object> map2 = new HashMap<>();
					map2.put("confirm_date_start",
							map.get("confirm_date_start"));
					map2.put("confirm_date_end", map.get("confirm_date_end"));
					// 判断是否存在三级部门
					if (!rdsStatisticsDepartmentModel2.isLeaf()) {
						String parentcode2 = rdsStatisticsDepartmentModel2
								.getDeptid();
						params2.put("parentcode", parentcode2);
						// 查询出当前所有部门
						List<RdsStatisticsDepartmentModel> list2 = rdsFinanceConfigMapper
								.queryDepartmentAll(params2);
						// 遍历三级部门
						for (RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel3 : list2) {
							rdsStatisticsDepartmentModel3
									.setDeptname(rdsStatisticsDepartmentModel3
											.getDeptname());
							listAll.add(amoebaDepart(params2, map2,
									parentcode2, rdsStatisticsDepartmentModel3,
									"（三级）"));

							Map<String, Object> params3 = new HashMap<>();
							params3.put("confirm_date_start",
									params.get("confirm_date_start"));
							params3.put("confirm_date_end",
									params.get("confirm_date_end"));
							params3.put("level", params.get("level"));

							Map<String, Object> map3 = new HashMap<>();
							map3.put("confirm_date_start",
									map.get("confirm_date_start"));
							map3.put("confirm_date_end",
									map.get("confirm_date_end"));
							// 判断是否存在四级部门
							if (!rdsStatisticsDepartmentModel3.isLeaf()) {
								String parentcode3 = rdsStatisticsDepartmentModel2
										.getDeptid();
								params3.put("parentcode", parentcode3);
								// 查询出当前所有部门
								List<RdsStatisticsDepartmentModel> list3 = rdsFinanceConfigMapper
										.queryDepartmentAll(params3);
								// //遍历四级部门
								for (RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel4 : list3) {
									rdsStatisticsDepartmentModel4
											.setDeptname(rdsStatisticsDepartmentModel4
													.getDeptname());
									listAll.add(amoebaDepart(params3, map3,
											parentcode3,
											rdsStatisticsDepartmentModel4,
											"（四级）"));

									Map<String, Object> params4 = new HashMap<>();
									params4.put("confirm_date_start",
											params.get("confirm_date_start"));
									params4.put("confirm_date_end",
											params.get("confirm_date_end"));
									params4.put("level", params.get("level"));

									Map<String, Object> map4 = new HashMap<>();
									map4.put("confirm_date_start",
											map.get("confirm_date_start"));
									map4.put("confirm_date_end",
											map.get("confirm_date_end"));
									// 判断是否存在五级部门
									if (!rdsStatisticsDepartmentModel4.isLeaf()) {
										String parentcode4 = rdsStatisticsDepartmentModel2
												.getDeptid();
										params4.put("parentcode", parentcode4);
										// 查询出当前所有部门
										List<RdsStatisticsDepartmentModel> list4 = rdsFinanceConfigMapper
												.queryDepartmentAll(params4);
										// 最多只有五级部门，存入五级部门数据
										for (RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel5 : list4) {
											rdsStatisticsDepartmentModel5
													.setDeptname(rdsStatisticsDepartmentModel5
															.getDeptname());
											listAll.add(amoebaDepart(
													params4,
													map4,
													parentcode4,
													rdsStatisticsDepartmentModel5,
													"（五级）"));
										}
									}

								}
							}

						}
					}
					// else{
					// listAll.add(amoebaDepart(params, map, parentcode,
					// rdsStatisticsDepartmentModel2));
					// }
				}
			}
			// else{
			// listAll.add(amoebaDepart(params, map, parentcode,
			// rdsStatisticsDepartmentModel));
			// }
		}
		Object[] titles = { "部门名称", "收入", "", "", "", "", "", "成本", "", "", "",
				"", "", "", "", "", "", "利润小计", "税", "净利润" };
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		List<Object> firstobject = new ArrayList<Object>();
		firstobject.add("");
		firstobject.add("收入合计（不含税）");
		firstobject.add("服务收入");
		firstobject.add("销售收入");
		firstobject.add("内部结算收入");
		firstobject.add("收入小计（含税）");
		firstobject.add("税");
		firstobject.add("成本合计");
		firstobject.add("人工成本");
		firstobject.add("材料成本");
		firstobject.add("委外检测成本");
		firstobject.add("内部结算成本");
		firstobject.add("销售管理费用");
		firstobject.add("资质费用");
		firstobject.add("其他费用（含折旧及摊销）");
		firstobject.add("对外投资");
		firstobject.add("房屋、装修、仪器及设备采购成本");
		bodys.add(firstobject);
		for (RdsStatisticsDepartmentModel model : listAll) {
			List<Object> objects = new ArrayList<Object>();
			objects.add(model.getDeptname());
			objects.add(model.getDeptOutTaxSum());
			objects.add(model.getServiceSum());
			objects.add(model.getSellSum());
			objects.add(model.getInsideSum());
			objects.add(model.getDeptInTaxSum());
			objects.add(model.getTaxSum());
			objects.add(model.getDeptCostSum());
			objects.add(model.getDeptWagesSum());
			objects.add(model.getDeptMaterialCostSum());
			objects.add(model.getDeptExternalCostSum());
			objects.add(model.getDeptCostInsideSum());
			objects.add(model.getDeptSaleManageSum());
			objects.add(model.getDeptAptitudeSum());
			objects.add(model.getDeptOtherSum());
			objects.add(model.getDeptInvestmentSum());
			objects.add(model.getDeptInstrumentSum());
			objects.add(model.getDeptProfit());
			objects.add(model.getDeptTax());
			objects.add(model.getDeptNetProfit());
			bodys.add(objects);
		}
		try {
			OutputStream os = httpResponse.getOutputStream();// 取得输出流
			httpResponse.reset();// 清空输出流
			String fname = new String("独立核算".getBytes("gb2312"), "iso8859-1");
			httpResponse.setHeader("Content-disposition",
					"attachment; filename=" + fname + ".xls");// 设定输出文件头
			httpResponse.setContentType("application/vnd.ms-excel");
			WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
			WritableSheet wsheet = wbook.createSheet("sheet1", 0); // sheet名称

			for (int i = 0; i < titles.length; i++) {
				Label label = new Label(i, 0, String.valueOf(titles[i]));
				wsheet.addCell(label);
			}
			for (int i = 0; i < bodys.size(); i++) {
				for (int j = 0; j < bodys.get(i).size(); j++) {
					if (bodys.get(i).get(j) != null) {
						if (bodys.get(i).get(j) instanceof String) {
							Label label = new Label(j, i + 1,
									String.valueOf(bodys.get(i).get(j)));
							wsheet.addCell(label);
						} else {
							Number number = new Number(j, i + 1,
									Float.valueOf(String.valueOf(bodys.get(i)
											.get(j))));
							wsheet.addCell(number);
						}
					} else {
						Label label = new Label(j, i + 1, "");
						wsheet.addCell(label);
					}

				}
			}
			wsheet.mergeCells(1, 0, 6, 0);
			wsheet.mergeCells(7, 0, 16, 0);
			WritableCellFormat writableCellFormat = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
			for (int i = 0; i < 1; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat);
			}
			for (int i = 17; i < 20; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat);
			}
			// wsheet.getWritableCell(11,0).setCellFormat(writableCellFormat);
			for (int i = 1; i <= 6; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat);
			}
			for (int i = 7; i <= 16; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat);
			}
			// 主体内容生成结束
			wbook.write(); // 写入文件
			wbook.close();
			os.close(); // 关闭流
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private RdsStatisticsDepartmentModel amoebaDepart(
			Map<String, Object> params, Map<String, Object> map,
			String parentcode,
			RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel,
			String level) {
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置
		Double listDeptServiceSumCount = 0.0;
		Double listDeptSellSumCount = 0.0;
		Double listDeptInsideSumCount = 0.0;
		Double ListDeptInTaxSum = 0.0;
		Double ListDeptTaxSum = 0.0;
		Double ListDeptOutTaxSum = 0.0;
		Double ListDeptWagesSum = 0.0;
		Double ListDeptMaterialSum = 0.0;
		Double listDeptExternalSumCount = 0.0;
		Double listDeptCostInsideSumCount = 0.0;
		Double listDeptSaleManageSumCount = 0.0;
		Double listDeptAptitudeSumCount = 0.0;
		Double listDeptOtherSumCount = 0.0;
		Double listDeptInvestmentSumCount = 0.0;
		Double listDeptInstrumentSumCount = 0.0;
		Double listSumCount = 0.0;
		Double listProfitCount = 0.0;
		Double listTaxCount = 0.0;
		Double listNetProfitCount = 0.0;

		if (!"0".equals(parentcode)) {
			map.put("deptid", parentcode);
			Map<String, String> maps = rdsFinanceConfigMapper
					.queryUserDepartment(map).get(0);
			// 二级部门为空，当前根路径为一级部门
			if (null == maps.get("user_dept_level2")) {
				map.put("deptname", maps.get("user_dept_level1"));
				params.put("deptname", maps.get("user_dept_level1"));
				map.put("deptname1", rdsStatisticsDepartmentModel.getDeptname());
				params.put("deptname1",
						rdsStatisticsDepartmentModel.getDeptname());
			} else if (null == maps.get("user_dept_level3")
					&& null != maps.get("user_dept_level2")) {
				map.put("deptname", maps.get("user_dept_level1"));
				params.put("deptname", maps.get("user_dept_level1"));
				map.put("deptname1", maps.get("user_dept_level2"));
				params.put("deptname1", maps.get("user_dept_level2"));
				map.put("deptname2", rdsStatisticsDepartmentModel.getDeptname());
				params.put("deptname2",
						rdsStatisticsDepartmentModel.getDeptname());
			} else if (null == maps.get("user_dept_level4")
					&& null != maps.get("user_dept_level3")) {
				map.put("deptname", maps.get("user_dept_level1"));
				params.put("deptname", maps.get("user_dept_level1"));
				map.put("deptname1", maps.get("user_dept_level2"));
				params.put("deptname1", maps.get("user_dept_level2"));
				map.put("deptname2", maps.get("user_dept_level3"));
				params.put("deptname2", maps.get("user_dept_level3"));
				map.put("deptname3", rdsStatisticsDepartmentModel.getDeptname());
				params.put("deptname3",
						rdsStatisticsDepartmentModel.getDeptname());
			} else if (null == maps.get("user_dept_level5")
					&& null != maps.get("user_dept_level4")) {
				map.put("deptname", maps.get("user_dept_level1"));
				params.put("deptname", maps.get("user_dept_level1"));
				map.put("deptname1", maps.get("user_dept_level2"));
				params.put("deptname1", maps.get("user_dept_level2"));
				map.put("deptname2", maps.get("user_dept_level3"));
				params.put("deptname2", maps.get("user_dept_level3"));
				map.put("deptname3", maps.get("user_dept_level4"));
				params.put("deptname3", maps.get("user_dept_level4"));
				map.put("deptname4", rdsStatisticsDepartmentModel.getDeptname());
				params.put("deptname4",
						rdsStatisticsDepartmentModel.getDeptname());
			}
		} else {
			map.put("deptname", rdsStatisticsDepartmentModel.getDeptname());
			params.put("deptname", rdsStatisticsDepartmentModel.getDeptname());
		}
		// 服务收入
		List<Map<String, Object>> tempServiceSum = rdsFinanceConfigMapper
				.queryDepServiceSum(params);
		if (tempServiceSum.size() == 0) {
			listDeptServiceSumCount = 0.0;
			rdsStatisticsDepartmentModel.setServiceSum("-");
		} else {
			listDeptServiceSumCount = (Double) (tempServiceSum.get(0) == null ? 0.0
					: tempServiceSum.get(0).get("deptServiceSum"));
			rdsStatisticsDepartmentModel
					.setServiceSum(listDeptServiceSumCount != 0 ? decimalFormat
							.format(listDeptServiceSumCount) : "-");
		}
		// 销售收入
		List<Map<String, Object>> tempSellSum = rdsFinanceConfigMapper
				.queryDepSellSum(params);
		if (tempSellSum.size() == 0) {
			listDeptSellSumCount = 0.0;
			rdsStatisticsDepartmentModel.setSellSum("-");
		} else {
			listDeptSellSumCount = (Double) (tempSellSum.get(0) == null ? 0.0
					: tempSellSum.get(0).get("deptSellSum"));
			rdsStatisticsDepartmentModel
					.setSellSum(listDeptSellSumCount != 0 ? decimalFormat
							.format(listDeptSellSumCount) : "-");
		}
		// 内部结算收入
		// 事业部查看特殊处理
		if ("2".equals(params.get("level"))) {
			Map<String, Object> maptemp = new HashMap<>();
			maptemp.put("deptname", params.get("deptname1"));
			maptemp.put("confirm_date_start", params.get("confirm_date_start"));
			maptemp.put("confirm_date_end", params.get("confirm_date_end"));
			List<Map<String, Object>> tempInsideSum = rdsFinanceConfigMapper
					.queryDeptInsideSum(maptemp);
			if (tempInsideSum.size() == 0) {
				listDeptInsideSumCount = 0.0;
				rdsStatisticsDepartmentModel.setInsideSum("-");
			} else {
				listDeptInsideSumCount = (Double) (tempInsideSum.get(0) == null ? 0.0
						: tempInsideSum.get(0).get("deptInsideSum"));
				rdsStatisticsDepartmentModel
						.setInsideSum(listDeptInsideSumCount != 0 ? decimalFormat
								.format(listDeptInsideSumCount) : "-");
			}
		} else {
			List<Map<String, Object>> tempInsideSum = rdsFinanceConfigMapper
					.queryDeptInsideSum(params);
			if (tempInsideSum.size() == 0) {
				listDeptInsideSumCount = 0.0;
				rdsStatisticsDepartmentModel.setInsideSum("-");
			} else {
				// 内部结算只到事业部
				if (null == params.get("deptname1")) {
					listDeptInsideSumCount = (Double) (tempInsideSum.get(0) == null ? 0.0
							: tempInsideSum.get(0).get("deptInsideSum"));
					rdsStatisticsDepartmentModel
							.setInsideSum(listDeptInsideSumCount != 0 ? decimalFormat
									.format(listDeptInsideSumCount) : "-");
				} else {
					listDeptInsideSumCount = 0.0;
					rdsStatisticsDepartmentModel.setInsideSum("-");
				}
			}
		}
		ListDeptInTaxSum = listDeptServiceSumCount + listDeptSellSumCount;
		// 收入小计（含税）
		rdsStatisticsDepartmentModel
				.setDeptInTaxSum(ListDeptInTaxSum != 0 ? decimalFormat
						.format(ListDeptInTaxSum) : "-");
		// ListDeptTaxSum = listDeptServiceSumCount
		// + listDeptSellSumCount
		// - (listDeptServiceSumCount / 1.06 + listDeptSellSumCount / 1.17);
		String dateFirstIntVal = params.get("confirm_date_start").toString();
		String dateLastIntVal = "2018-05-01";
		if (compare_date(dateFirstIntVal, dateLastIntVal)) {
			ListDeptTaxSum = listDeptServiceSumCount
					+ listDeptSellSumCount
					- (listDeptServiceSumCount / 1.06 + listDeptSellSumCount / 1.16);
			ListDeptOutTaxSum = listDeptServiceSumCount / 1.06
					+ listDeptSellSumCount / 1.16;
		} else {
			ListDeptTaxSum = listDeptServiceSumCount
					+ listDeptSellSumCount
					- (listDeptServiceSumCount / 1.06 + listDeptSellSumCount / 1.17);
			ListDeptOutTaxSum = listDeptServiceSumCount / 1.06
					+ listDeptSellSumCount / 1.17;
		}

		// 收入（税）
		rdsStatisticsDepartmentModel
				.setTaxSum(ListDeptTaxSum != 0 ? decimalFormat
						.format(ListDeptTaxSum) : "-");

		// ListDeptOutTaxSum = listDeptServiceSumCount / 1.06
		// + listDeptSellSumCount / 1.17;
		// 收入小计（不含税）,服务收入6个税点，销售收入17个税点
		rdsStatisticsDepartmentModel
				.setDeptOutTaxSum((ListDeptOutTaxSum + listDeptInsideSumCount) != 0 ? decimalFormat
						.format(ListDeptOutTaxSum + listDeptInsideSumCount)
						: "-");

		// 人工成本
		List<Map<String, Object>> tempWagesSum = rdsFinanceConfigMapper
				.queryDeptWagesSum(map);
		if (tempWagesSum.size() == 0) {
			rdsStatisticsDepartmentModel.setDeptWagesSum("-");
		} else {
			ListDeptWagesSum = (Double) (tempWagesSum.get(0) == null ? 0.0
					: tempWagesSum.get(0).get("wagesSum"));
			rdsStatisticsDepartmentModel
					.setDeptWagesSum(ListDeptWagesSum != 0 ? decimalFormat
							.format(ListDeptWagesSum) : "-");
		}

		// 材料成本
		params.put("amoeba_program", "耗材");
		List<Map<String, Object>> tempMaterialCostSum = rdsFinanceConfigMapper
				.queryDeptMaterialCostSum(params);
		if (tempMaterialCostSum.size() == 0) {
			rdsStatisticsDepartmentModel.setDeptMaterialCostSum("-");
		} else {
			ListDeptMaterialSum = (Double) (tempMaterialCostSum.get(0) == null ? 0.0
					: tempMaterialCostSum.get(0).get("materialSum"));
			rdsStatisticsDepartmentModel
					.setDeptMaterialCostSum(ListDeptMaterialSum != 0 ? decimalFormat
							.format(ListDeptMaterialSum) : "-");
		}

		// 委外检测成本
		params.put("amoeba_program", "委外检测成本");
		List<Map<String, Object>> tempExternalCostSum = rdsFinanceConfigMapper
				.queryDeptExternalCostSum(params);
		if (tempExternalCostSum.size() == 0) {
			rdsStatisticsDepartmentModel.setDeptExternalCostSum("-");
		} else {
			listDeptExternalSumCount = (Double) (tempExternalCostSum.get(0) == null ? 0.0
					: tempExternalCostSum.get(0).get("deptExternalCostSum"));
			rdsStatisticsDepartmentModel
					.setDeptExternalCostSum(listDeptExternalSumCount != 0 ? decimalFormat
							.format(listDeptExternalSumCount) : "-");
		}

		// 内部结算成本
		List<Map<String, Object>> tempCostInsideSum = rdsFinanceConfigMapper
				.queryCostInsideSum(params);
		if (tempCostInsideSum.size() == 0) {
			rdsStatisticsDepartmentModel.setDeptCostInsideSum("-");
		} else {
			listDeptCostInsideSumCount = (Double) (tempCostInsideSum.get(0) == null ? 0.0
					: tempCostInsideSum.get(0).get("costInsideSum"));
			rdsStatisticsDepartmentModel
					.setDeptCostInsideSum(listDeptCostInsideSumCount != 0 ? decimalFormat
							.format(listDeptCostInsideSumCount) : "-");
		}

		// 销售管理费用
		List<Map<String, Object>> tempSaleManageSum = rdsFinanceConfigMapper
				.querySaleManageSum(params);
		if (tempSaleManageSum.size() == 0) {
			rdsStatisticsDepartmentModel.setDeptSaleManageSum("-");
		} else {
			listDeptSaleManageSumCount = (Double) (tempSaleManageSum.get(0) == null ? 0.0
					: tempSaleManageSum.get(0).get("deptSaleManageSum"));
			rdsStatisticsDepartmentModel
					.setDeptSaleManageSum(listDeptSaleManageSumCount != 0 ? decimalFormat
							.format(listDeptSaleManageSumCount) : "-");
		}

		// 资质费用
		List<Map<String, Object>> tempDeptAptitudeSum = rdsFinanceConfigMapper
				.queryDeptAptitudeCostSum(params);
		if (tempDeptAptitudeSum.size() == 0) {
			rdsStatisticsDepartmentModel.setDeptAptitudeSum("-");
		} else {
			listDeptAptitudeSumCount = (Double) (tempDeptAptitudeSum.get(0) == null ? 0.0
					: tempDeptAptitudeSum.get(0).get("deptAptitudeCostSum"));
			rdsStatisticsDepartmentModel
					.setDeptAptitudeSum(listDeptAptitudeSumCount != 0 ? decimalFormat
							.format(listDeptAptitudeSumCount) : "-");
		}

		// 其他费用（含折旧及摊销）
		map.put("amoeba_program", "其他费用（含折旧及摊销）");
		List<Map<String, Object>> tempOtherCostSum = rdsFinanceConfigMapper
				.queryDeptDepreciationCostSum(map);
		if (tempOtherCostSum.size() == 0) {
			rdsStatisticsDepartmentModel.setDeptOtherSum("-");
		} else {
			listDeptOtherSumCount = (Double) (tempOtherCostSum.get(0) == null ? 0.0
					: tempOtherCostSum.get(0).get("depreciationSum"));
			rdsStatisticsDepartmentModel
					.setDeptOtherSum(listDeptOtherSumCount != 0 ? decimalFormat
							.format(listDeptOtherSumCount) : "-");
		}

		// 对外投资
		map.put("amoeba_program", "对外投资");
		List<Map<String, Object>> tempInvestmentCostSum = rdsFinanceConfigMapper
				.queryDeptDepreciationCostSum(map);
		if (tempInvestmentCostSum.size() == 0) {
			rdsStatisticsDepartmentModel.setDeptInvestmentSum("-");
		} else {
			listDeptInvestmentSumCount = (Double) (tempInvestmentCostSum.get(0) == null ? 0.0
					: tempInvestmentCostSum.get(0).get("depreciationSum"));
			rdsStatisticsDepartmentModel
					.setDeptInvestmentSum(listDeptInvestmentSumCount != 0 ? decimalFormat
							.format(listDeptInvestmentSumCount) : "-");
		}

		// 仪器设备
		List<Map<String, Object>> tempInstrumentCostSum = rdsFinanceConfigMapper
				.queryDeptInstrumentCostSum(params);
		if (tempInstrumentCostSum.size() == 0) {
			rdsStatisticsDepartmentModel.setDeptInstrumentSum("-");
		} else {
			listDeptInstrumentSumCount = (Double) (tempInstrumentCostSum.get(0) == null ? 0.0
					: tempInstrumentCostSum.get(0).get("instrumentSum"));
			rdsStatisticsDepartmentModel
					.setDeptInstrumentSum(listDeptInstrumentSumCount != 0 ? decimalFormat
							.format(listDeptInstrumentSumCount) : "-");
		}

		// 成本小计
		listSumCount = ListDeptWagesSum + ListDeptMaterialSum
				+ listDeptExternalSumCount + listDeptCostInsideSumCount
				+ listDeptSaleManageSumCount + listDeptAptitudeSumCount
				+ listDeptOtherSumCount;
		rdsStatisticsDepartmentModel
				.setDeptCostSum(listSumCount != 0 ? decimalFormat
						.format(listSumCount) : "-");
		// 利润小计
		listProfitCount = ListDeptOutTaxSum + listDeptInsideSumCount
				- listSumCount;
		rdsStatisticsDepartmentModel
				.setDeptProfit(listProfitCount != 0 ? decimalFormat
						.format(listProfitCount) : "-");
		// 税
		listTaxCount = listProfitCount * 0.2;
		rdsStatisticsDepartmentModel
				.setDeptTax(listTaxCount != 0 ? decimalFormat
						.format(listTaxCount) : "-");
		// 净利润小计
		listNetProfitCount = listProfitCount - listTaxCount;
		rdsStatisticsDepartmentModel
				.setDeptNetProfit(listNetProfitCount != 0 ? decimalFormat
						.format(listNetProfitCount) : "-");
		rdsStatisticsDepartmentModel.setDeptname(level
				+ rdsStatisticsDepartmentModel.getDeptname());
		return rdsStatisticsDepartmentModel;
	}

	public static boolean compare_date(String DATE1, String DATE2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() >= dt2.getTime()) {
				return true;
			} else if (dt1.getTime() < dt2.getTime()) {
				return false;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}
}
