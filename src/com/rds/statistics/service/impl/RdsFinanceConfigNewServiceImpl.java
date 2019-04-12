package com.rds.statistics.service.impl;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.JScriptInvoke;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.statistics.mapper.RdsFinanceConfigNewMapper;
import com.rds.statistics.model.RdsFinanceAmoebaStatisticsModel;
import com.rds.statistics.model.RdsFinanceAptitudModel;
import com.rds.statistics.model.RdsFinanceCaseDetailModel;
import com.rds.statistics.model.RdsFinanceCaseDetailStatisticsModel;
import com.rds.statistics.model.RdsFinanceConfigModel;
import com.rds.statistics.model.RdsFinanceProgramModel;
import com.rds.statistics.model.RdsStatisticsDepartmentModel;
import com.rds.statistics.service.RdsFinanceConfigNewService;
import com.rds.upc.model.RdsUpcUserModel;
import com.sun.xml.internal.bind.v2.TODO;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Service
public class RdsFinanceConfigNewServiceImpl implements RdsFinanceConfigNewService{
	@Autowired
	private RdsFinanceConfigNewMapper rdsFinanceConfigNewMapper;

	public List<RdsFinanceConfigModel> queryAll(Map<String, Object> params) {
		return rdsFinanceConfigNewMapper.queryAll(params);
	}

	public int queryAllCount(Map<String, Object> params) {
		return rdsFinanceConfigNewMapper.queryAllCount(params);
	}

	@Override
	public void finance_config(Map<String, Object> params) {
		List<RdsFinanceConfigModel> list = rdsFinanceConfigNewMapper
				.queryAll(params);
		for (RdsFinanceConfigModel rdsFinanceConfigModel : list) {
			// 管理费公式
			String finance_manage = rdsFinanceConfigModel.getFinance_manage();
			// 后端结算公式
			String back_settlement = rdsFinanceConfigModel.getBack_settlement();

			params.put("case_subtype",
					rdsFinanceConfigModel.getFinance_program_type());
			// 根据项目计算应收总额，案例数量，样本数量等
			List<RdsFinanceProgramModel> programList = rdsFinanceConfigNewMapper
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
					List<RdsFinanceAptitudModel> aptitudList = rdsFinanceConfigNewMapper
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
							List<RdsFinanceProgramModel> programList1 = rdsFinanceConfigNewMapper
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
								rdsFinanceConfigNewMapper.insertAptitudeFee(map);
						} else {
							// 信诺和正孚算作委外成本
							Double aptitude_price_temp = 0.0;
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("case_subtype", "亲子鉴定(资质)");
							map.put("confirm_date", params.get("confirm_date"));
							map.put("partner",
									rdsFinanceAptitudModel.getPartnername());
							// 查询出资质合作方案例
							List<RdsFinanceProgramModel> programList1 = rdsFinanceConfigNewMapper
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
								rdsFinanceConfigNewMapper.insertOutsourcinFee(map);

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
						rdsFinanceConfigNewMapper.insertProgramPrice(map);
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
						rdsFinanceConfigNewMapper.insertProgramPrice(map);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

		// 所有事业部
		List<String> listDept = rdsFinanceConfigNewMapper.queryUserDept();
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
				List<RdsFinanceProgramModel> programList = rdsFinanceConfigNewMapper
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
						List<RdsFinanceAptitudModel> aptitudList = rdsFinanceConfigNewMapper
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
							List<RdsFinanceProgramModel> programList1 = rdsFinanceConfigNewMapper
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
									rdsFinanceConfigNewMapper
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
							rdsFinanceConfigNewMapper.insertCostPrice(map);
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
		Map<String, Object> map = new HashMap<>();
		List<List<String>> list = new ArrayList<>();
		List<String> listDeptTemp = new ArrayList<String>();
		List<String> listDeptTemp2 = (List<String>) params.get("listDeptTemp");
		List<String> listDeptTemp3 = (List<String>) params.get("listDeptTemp2");
		List<String> shangji = (List<String>) params.get("shangji");
		List<String> listDept = new ArrayList<>();

		if (params.get("listDeptTemp") != null){
			RdsStatisticsDepartmentModel BuMen=null;
			for (String string : listDeptTemp3) {
				BuMen=rdsFinanceConfigNewMapper.queryBuMen(string);
				List<RdsStatisticsDepartmentModel> listBuMen=rdsFinanceConfigNewMapper.queryXiaJiBuMen(string);
				if(CollectionUtils.isNotEmpty(listBuMen)){
					listDept.add("<a href='#' onClick=jumpToNext('" + string + "')>" + 
							BuMen.getDeptname() + "</a>");
				}else{
					listDept.add(BuMen.getDeptname());
				}
			}
			listDeptTemp.add(BuMen.getDeptname());
		}else{
			List<RdsStatisticsDepartmentModel> listBuMen=rdsFinanceConfigNewMapper.queryXiaJiBuMen("0");
			for (RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel : listBuMen) {
				listDept.add("<a href='#' onClick=jumpToNext('" + rdsStatisticsDepartmentModel.getDeptid() + "')>" + 
						rdsStatisticsDepartmentModel.getDeptname() + "</a>");
				listDeptTemp.add(rdsStatisticsDepartmentModel.getDeptname());
			}
		}


		//部门服务收入
		List<String> listDeptServiceSum = new ArrayList<>();
		listDeptServiceSum.add("<th>服务收入</th>");
		//部门销售收入
		List<String> listDeptSellSum = new ArrayList<>();
		listDeptSellSum.add("<th>销售收入</th>");
		//合作方（实收）收入
		List<String> listDeptHeZou = new ArrayList<>();
		listDeptHeZou.add("<th>合作方（实收）收入</th>");	 
		//部门内部结算收入
		List<String> listDeptInsideSum = new ArrayList<>();
		listDeptInsideSum.add("<th>内部结算收入</th>");
		//部门收入小计（含税）
		List<String> listDeptInTaxSum = new ArrayList<>();
		listDeptInTaxSum.add("<th>收入小计(含税)</th>");
		//部门收入小计（税）
		List<String> listDeptTaxSum = new ArrayList<>();
		listDeptTaxSum.add("<th>税</th>");
		//部门收入小计（不含税）
		List<String> listDeptOutTaxSum = new ArrayList<>();
		listDeptOutTaxSum.add("<th><h2>收入小计（不含税）</h2></th>");
		//空格
		List<String> listDeptNull = new ArrayList<>();
		listDeptNull.add("<th></th>");
		//人工成本
		List<String> listDeptWagesSum = new ArrayList<>();
		listDeptWagesSum.add("<th><h3>人工成本</h3></th>");
		//材料成本
		List<String> listDeptMaterialCostSum = new ArrayList<>();
		listDeptMaterialCostSum.add("<th><h3>材料成本</h3></th>");
		//采购材料付款
		List<String> listDeptCaiGouSum = new ArrayList<>();
		listDeptCaiGouSum.add("<th>采购材料付款</th>");
		//耗材
		List<String> listDeptHaoCaiSum = new ArrayList<>();
		listDeptHaoCaiSum.add("<th>耗材</th>");
		//委外费用
		List<String> listDeptWeiWaiCostSum = new ArrayList<>();
		listDeptWeiWaiCostSum.add("<th><h3>委外费用</h3></th>");
		//委外检测成本
		List<String> listDeptExternalCostSum = new ArrayList<>();
		listDeptExternalCostSum.add("<th>委外检测成本</th>");
		//代理费
		List<String> listDeptDaiLiCostSum = new ArrayList<>();
		listDeptDaiLiCostSum.add("<th>代理费</th>");
		//资质费
		List<String> listDeptZiZhiCostSum = new ArrayList<>();
		listDeptZiZhiCostSum.add("<th>资质费</th>");

		//销售管理费用
		List<String> listDeptXiaoShouCostSum = new ArrayList<>();
		listDeptXiaoShouCostSum.add("<th><h3>销售管理费用</h3></th>");
		//办公费
		List<String> listDeptBanGongCostSum = new ArrayList<>();
		listDeptBanGongCostSum.add("<th>办公费</th>");
		//备用金
		List<String> listDeptBeiYonCostSum = new ArrayList<>();
		listDeptBeiYonCostSum.add("<th>备用金</th>");
		//差旅费
		List<String> listDeptChailvCostSum = new ArrayList<>();
		listDeptChailvCostSum.add("<th>差旅费</th>");
		//福利费
		List<String> listDeptFuLiCostSum = new ArrayList<>();
		listDeptFuLiCostSum.add("<th>福利费</th>");
		//广告宣传
		List<String> listDeptGuangGaoCostSum = new ArrayList<>();
		listDeptGuangGaoCostSum.add("<th>广告宣传</th>");
		//业务招待费
		List<String> listDeptYeWuCostSum = new ArrayList<>();
		listDeptYeWuCostSum.add("<th>业务招待费</th>");
		//租赁费
		List<String> listDeptZuPingCostSum = new ArrayList<>();
		listDeptZuPingCostSum.add("<th>租赁费</th>");
		//其他费用
		List<String> listDeptQiTaFeiYonCostSum = new ArrayList<>();
		listDeptQiTaFeiYonCostSum.add("<th>其他费用</th>");
		//其他付款
		List<String> listDeptQiTaFuKuanCostSum = new ArrayList<>();
		listDeptQiTaFuKuanCostSum.add("<th>其他付款</th>");
		//其它采购
		List<String> listDeptQiTaCaiGouCostSum = new ArrayList<>();
		listDeptQiTaCaiGouCostSum.add("<th>其它采购</th>");

		//对外投资
		List<String> listDeptInvestmentSum = new ArrayList<>();
		listDeptInvestmentSum.add("<th><h3>对外投资</h3></th>");

		//其他支出
		List<String> listDeptQiTaZhiChuCostSum = new ArrayList<>();
		listDeptQiTaZhiChuCostSum.add("<th><h3>其他支出</h3></th>");
		//采购仪器设备付款
		List<String> listDeptCaiGouYiQiCostSum = new ArrayList<>();
		listDeptCaiGouYiQiCostSum.add("<th>采购仪器设备付款</th>");
		//工程
		List<String> listDeptGongChenCostSum = new ArrayList<>();
		listDeptGongChenCostSum.add("<th>工程</th>");
		//仪器设备
		List<String> listDeptYiQiSheBeiCostSum = new ArrayList<>();
		listDeptYiQiSheBeiCostSum.add("<th>仪器设备</th>");
		//装修付款
		List<String> listDeptZhuangXiuFuKuanCostSum = new ArrayList<>();
		listDeptZhuangXiuFuKuanCostSum.add("<th>装修付款</th>");
		//支出费用小计
		List<String> listDeptZhiChuFeiYonCostSum = new ArrayList<>();
		listDeptZhiChuFeiYonCostSum.add("<th><h2>支出费用小计</h2></th>");

		//现金流量
		List<String> listDeptXianJinCostSum = new ArrayList<>();
		listDeptXianJinCostSum.add("<th>现金流量</th>");
		//所得税
		List<String> listDeptSuoDeiCostSum = new ArrayList<>();
		listDeptSuoDeiCostSum.add("<th>所得税</th>");
		//现金流量净额
		List<String> listDeptJingECostSum = new ArrayList<>();
		listDeptJingECostSum.add("<th>现金流量净额</th>");

		//合作方（应收）结算收入
		List<String> listDeptHeZouFangCostSum = new ArrayList<>();
		listDeptHeZouFangCostSum.add("<th>合作方（应收）结算收入</th>");

		//运营利润额
		List<String> listDeptYunYinCostSum = new ArrayList<>();
		listDeptYunYinCostSum.add("<th>运营利润额</th>");
		//所得税
		List<String> listDeptSuoDeiShuiCostSum = new ArrayList<>();
		listDeptSuoDeiShuiCostSum.add("<th>所得税</th>");
		//运营净利润额
		List<String> listDeptYunYinJingLiRunCostSum = new ArrayList<>();
		listDeptYunYinJingLiRunCostSum.add("<th>运营净利润额</th>");
		//未实付运营费用
		List<String> listDeptWeiShiFuYunYinCostSum = new ArrayList<>();
		listDeptWeiShiFuYunYinCostSum.add("<th>未实付运营费用</th>");
		//虚拟运营净利润合计
		List<String> listDeptXuNiYunYinCostSum = new ArrayList<>();
		listDeptXuNiYunYinCostSum.add("<th><h2>虚拟运营净利润合计</h2></th>");

		//内部结算成本
		List<String> listDeptCostInsideSum = new ArrayList<>();
		listDeptCostInsideSum.add("<th>内部结算成本</th>");
		//销售管理结算成本
		List<String> listDeptSaleManageSum = new ArrayList<>();
		listDeptSaleManageSum.add("<th>销售管理费用</th>");
		//资质费用
		List<String> listDeptAptitudeSum = new ArrayList<>();
		listDeptAptitudeSum.add("<th>资质费用</th>");
		//其他费用（含折旧及摊销）
		List<String> listDeptOtherSum = new ArrayList<>();
		listDeptOtherSum.add("<th>其他费用（含折旧及摊销）</th>");
		//仪器设备
		List<String> listDeptInstrumentSum = new ArrayList<>();
		listDeptInstrumentSum.add("<th>房屋、装修、仪器及设备采购成本</th>");
		//成本小计
		List<String> listSum = new ArrayList<>();
		listSum.add("<th>成本小计</th>");
		//空格
		List<String> listDeptNull1 = new ArrayList<>();
		listDeptNull1.add("<th></th>");
		//利润小计
		List<String> listProfit = new ArrayList<>();
		listProfit.add("<th>利润小计</th>");
		//税
		List<String> listTax = new ArrayList<>();
		listTax.add("<th>税</th>");
		//净利润小计
		List<String> listNetProfit = new ArrayList<>();
		listNetProfit.add("<th>净利润</th>");
		// 内部结算按照月份计算
		map.put("confirm_date_start", params.get("confirm_date_start").toString()
				.substring(0, 7));
		map.put("confirm_date_end", params.get("confirm_date_end").toString()
				.substring(0, 7));
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置

		Double listDeptShouRuBuSumAllCount = 0.0;
		Double listDeptServiceSumAllCount = 0.0;
		Double listDeptSellSumAllCount = 0.0;
		Double listDeptHeZouAllCount = 0.0;
		Double listDeptInsideSumAllCount = 0.0;
		Double ListDeptInTaxAllSum = 0.0;
		Double ListDeptTaxAllSum = 0.0;
		Double ListDeptOutTaxAllSum = 0.0;
		Double ListDeptWagesAllSum = 0.0;
		Double listDeptMaterialCostAllSum=0.0;
		Double listDeptCaiGouAllSum=0.0;
		Double listDeptHaoCaiAllSum=0.0;
		Double listDeptWeiWaiAllSum =0.0;
		Double listDeptExternalAllSum =0.0;
		Double listDeptDaiLiAllSum =0.0;
		Double listDeptZiZhiAllSum =0.0;

		Double listDeptXiaoShouAllSum =0.0;
		Double listDeptBanGongAllSum =0.0;
		Double listDeptBeiYonAllSum =0.0;
		Double listDeptChailvAllSum =0.0;
		Double listDeptFuLiAllSum =0.0;
		Double listDeptGuangGaoAllSum =0.0;
		Double listDeptYeWuAllSum =0.0;
		Double listDeptZuPingAllSum =0.0;
		Double listDeptQiTaFeiYonAllSum =0.0;
		Double listDeptQiTaFuKuanAllSum =0.0;
		Double listDeptQiTaCaiGouAllSum =0.0;

		Double listDeptQiTaZhiChuAllSum =0.0;
		Double listDeptCaiGouYiQiAllSum =0.0;
		Double listDeptGongChenAllSum =0.0;
		Double listDeptYiQiSheBeiAllSum =0.0;
		Double listDeptZhuangXiuFuKuanAllSum =0.0;
		Double listDeptZhiChuFeiYonAllSum =0.0;

		Double listDeptXianJinAllSum=0.0;
		Double listDeptSuoDeiAllSum=0.0;
		Double listDeptJingEAllSum=0.0;

		Double listDeptHeZouFangAllSum=0.0;

		Double listDeptYunYinAllSum=0.0;
		Double listDeptSuoDeiShuiAllSum=0.0;
		Double listDeptYunYinJingLiRunAllSum=0.0;
		Double listDeptWeiShiFuYunYinAllSum=0.0;
		Double listDeptXuNiYunYinAllSum=0.0;

		Double listDeptCostInsideAllSum=0.0;
		Double listDeptSaleManageAllSum=0.0;
		Double listDeptAptitudeAllSum=0.0;
		Double listDeptOtherAllSum=0.0;
		Double listDeptInvestmentAllSum=0.0;
		Double listDeptInstrumentAllSum=0.0;
		Double listSumAllCount=0.0;
		Double listProfitAllCount=0.0;
		Double listTaxAllCount = 0.0;
		Double listNetProfitAllCount=0.0;
		for (String string : listDeptTemp) {
			Double listDeptShouRuBuSum = 0.0;
			Double listDeptServiceSumCount = 0.0;
			Double listDeptSellSumCount = 0.0;
			Double listDeptHeZouCount = 0.0;
			Double listDeptInsideSumCount = 0.0;
			Double ListDeptInTaxSum = 0.0;
			Double ListDeptTaxSum = 0.0;
			Double ListDeptOutTaxSum = 0.0;
			Double ListDeptWagesSum = 0.0;
			Double ListDeptMaterialSum = 0.0;
			Double ListDeptCaiGouSum = 0.0;
			Double ListDeptHaoCaiSum = 0.0;
			Double listDeptWeiWaiSumCount=0.0;
			Double listDeptExternalSumCount=0.0;
			Double listDeptDaiLiSumCount=0.0;
			Double listDeptZiZhiSumCount=0.0;

			Double listDeptXiaoShouSumCount=0.0;
			Double listDeptBanGongSumCount=0.0;
			Double listDeptBeiYonSumCount=0.0;
			Double listDeptChailvSumCount=0.0;
			Double listDeptFuLiSumCount=0.0;
			Double listDeptGuangGaoSumCount=0.0;
			Double listDeptYeWuSumCount=0.0;
			Double listDeptZuPingSumCount=0.0;
			Double listDeptQiTaFeiYonSumCount=0.0;
			Double listDeptQiTaFuKuanSumCount=0.0;
			Double listDeptQiTaCaiGouSumCount=0.0;

			Double listDeptQiTaZhiChuSumCount=0.0;
			Double listDeptCaiGouYiQiSumCount=0.0;
			Double listDeptGongChenSumCount=0.0;
			Double listDeptYiQiSheBeiSumCount=0.0;
			Double listDeptZhuangXiuFuKuanSumCount=0.0;
			Double listDeptZhiChuFeiYonSumCount=0.0;

			Double listDeptXianJinSumCount=0.0;
			Double listDeptSuoDeiSumCount=0.0;
			Double listDeptJingESumCount=0.0;

			Double listDeptHeZouFangSumCount=0.0;

			Double listDeptYunYinSumCount=0.0;
			Double listDeptSuoDeiShuiSumCount=0.0;
			Double listDeptYunYinJingLiRunSumCount=0.0;
			Double listDeptWeiShiFuYunYinSumCount=0.0;
			Double listDeptXuNiYunYinSumCount=0.0;

			Double listDeptCostInsideSumCount=0.0;
			Double listDeptSaleManageSumCount=0.0;
			Double listDeptAptitudeSumCount=0.0;
			Double listDeptOtherSumCount=0.0;
			Double listDeptInvestmentSumCount=0.0;
			Double listDeptInstrumentSumCount=0.0;
			Double listSumCount=0.0;
			Double listProfitCount=0.0;
			Double listTaxCount = 0.0;
			Double listNetProfitCount=0.0;
			if(listDeptTemp2==null){
				params.put("deptname", string);
				map.put("deptname", string);
			}else if(shangji.size()==1){
				params.put("deptname", string);
				map.put("deptname", string);
			}else if(shangji.size()==2){
				params.put("deptname", shangji.get(0));
				map.put("deptname", shangji.get(0));
				params.put("deptname1", string);
				map.put("deptname1", string);
			}else if(shangji.size()==3){
				params.put("deptname", shangji.get(0));
				map.put("deptname", shangji.get(0));
				params.put("deptname1", shangji.get(1));
				map.put("deptname1", shangji.get(1));
				params.put("deptname2", string);
				map.put("deptname2", string);
			}
			
			//服务收入  type=1
			List<Map<String, Object>> tempServiceSum =null;
			if(map.size()==4  && "精准医学事业部".equals(string)){
				tempServiceSum = rdsFinanceConfigNewMapper
						.queryDepServiceSum2(params);
			}else{
				tempServiceSum = rdsFinanceConfigNewMapper
						.queryDepServiceSum(params);
			} 
			if(tempServiceSum.size()==0)
			{
				listDeptServiceSumCount=0.0;
				listDeptServiceSum.add("<td>0</td>");
			}
			else{
				listDeptServiceSumCount = (Double)(tempServiceSum.get(0)==null?0.0:tempServiceSum.get(0).get("deptServiceSum"));
				listDeptServiceSum.add("<td><a href='#' onClick=alert('暂未开通')>"+decimalFormat.format(listDeptServiceSumCount)+"</a></td>");
			}
			//销售收入 type=2
			List<Map<String, Object>> tempSellSum = rdsFinanceConfigNewMapper
					.queryDepSellSum(params);
			if(tempSellSum.size()==0){
				listDeptSellSumCount=0.0;
				listDeptSellSum.add("<td>0</td>");
			}
			else{
				listDeptSellSumCount = (Double)(tempSellSum.get(0)==null?0.0:tempSellSum.get(0).get("deptSellSum"));
				listDeptSellSum.add("<td>"+decimalFormat.format(listDeptSellSumCount)+"</td>");
			}
			//合作方（实收）收入
			List<Map<String, Object>> tempHeZou = rdsFinanceConfigNewMapper
					.queryDepHeZou(params);
			if(tempHeZou.size()==0){
				listDeptHeZouCount=0.0;
				listDeptHeZou.add("<td>0</td>");
			}
			else{
				listDeptHeZouCount = (Double)(tempHeZou.get(0)==null?0.0:tempHeZou.get(0).get("deptHeZou"));
				listDeptHeZou.add("<td>"+decimalFormat.format(listDeptHeZouCount)+"</td>");
			}
		
			//收入小计（含税）
			ListDeptInTaxSum = listDeptHeZouCount+listDeptSellSumCount+ listDeptServiceSumCount;
			listDeptInTaxSum.add("<td>"+decimalFormat.format(ListDeptInTaxSum)+"</td>");
			//收入（税）
			ListDeptTaxSum =(listDeptServiceSumCount/1.06*0.06)+(listDeptHeZouCount/1.06*0.06)+(listDeptSellSumCount/1.16*0.16) ;
			listDeptTaxSum.add("<td>"+decimalFormat.format(ListDeptTaxSum)+"</td>");

			//收入小计（不含税）
			listDeptShouRuBuSum=ListDeptInTaxSum-ListDeptTaxSum;
			listDeptOutTaxSum.add("<td>"+decimalFormat.format(listDeptShouRuBuSum)+"</td>");

			listDeptNull.add("<td>-</td>");
			listDeptNull1.add("<td>-</td>");
			//人工成本  wags
			List<Map<String, Object>> tempWagesSum=null;
			if(map.size()==4  && "精准医学事业部".equals(string)){
				 tempWagesSum = rdsFinanceConfigNewMapper
							.queryDeptWagesSum2(map);
			}else{
				 tempWagesSum = rdsFinanceConfigNewMapper
							.queryDeptWagesSum(map);
			} 
			if(tempWagesSum.size()==0){			
				listDeptWagesSum.add("<td>0</td>");
			}
			else{
				ListDeptWagesSum= (Double)(tempWagesSum.get(0)==null?0.0:tempWagesSum.get(0).get("wagesSum"));
				listDeptWagesSum.add("<td>"+decimalFormat.format(ListDeptWagesSum)+"</td>");
			}

			//采购材料付款
			params.put("amoeba_program", "采购材料付款");
			List<Map<String, Object>> tempCaiGouSum = rdsFinanceConfigNewMapper
					.queryDeptMaterialCostSum(params);
			if(tempCaiGouSum.size()==0){			
				listDeptCaiGouSum.add("<td>0</td>");
			}
			else{
				ListDeptCaiGouSum= (Double)(tempCaiGouSum.get(0)==null?0.0:tempCaiGouSum.get(0).get("materialSum"));
				listDeptCaiGouSum.add("<td>"+decimalFormat.format(ListDeptCaiGouSum)+"</td>");
			}

			//耗材
			params.put("amoeba_program", "耗材");
			List<Map<String, Object>> tempHaoCaiSum = rdsFinanceConfigNewMapper
					.queryDeptMaterialCostSum(params);
			if(tempHaoCaiSum.size()==0){			
				listDeptHaoCaiSum.add("<td>0</td>");
			}
			else{
				ListDeptHaoCaiSum= (Double)(tempHaoCaiSum.get(0)==null?0.0:tempHaoCaiSum.get(0).get("materialSum"));
				listDeptHaoCaiSum.add("<td>"+decimalFormat.format(ListDeptHaoCaiSum)+"</td>");
			}

			//材料成本  
			ListDeptMaterialSum=ListDeptCaiGouSum+ListDeptHaoCaiSum;
			listDeptMaterialCostSum.add("<td>"+decimalFormat.format(ListDeptMaterialSum)+"</td>");


			//委外检测成本
			params.put("amoeba_program", "委外检测成本");
			listDeptExternalSumCount = findByNameOne(params, listDeptExternalCostSum, decimalFormat,listDeptExternalSumCount);
			//代理费
			params.put("amoeba_program", "代理费");
			listDeptDaiLiSumCount = findByNameOne(params, listDeptDaiLiCostSum, decimalFormat, listDeptDaiLiSumCount);
			//资质费
			params.put("amoeba_program", "资质费");
			listDeptZiZhiSumCount = findByNameOne(params, listDeptZiZhiCostSum, decimalFormat, listDeptZiZhiSumCount);

			//办公费
			params.put("amoeba_program", "办公费");
			listDeptBanGongSumCount = findByNameOne(params, listDeptBanGongCostSum, decimalFormat,listDeptBanGongSumCount);
			//备用金
			listDeptBeiYonSumCount = findByName(params, listDeptBeiYonCostSum, decimalFormat, listDeptBeiYonSumCount);
			//差旅费
			params.put("amoeba_program", "差旅费");
			listDeptChailvSumCount = findByNameOne(params, listDeptChailvCostSum, decimalFormat,listDeptChailvSumCount);
			//福利费
			params.put("amoeba_program", "福利费");
			listDeptFuLiSumCount = findByNameOne(params, listDeptFuLiCostSum, decimalFormat,listDeptFuLiSumCount);
			//广告宣传
			params.put("amoeba_program", "广告宣传");
			listDeptGuangGaoSumCount = findByNameOne(params, listDeptGuangGaoCostSum, decimalFormat,listDeptGuangGaoSumCount);
			//业务招待费
			params.put("amoeba_program", "业务招待费");
			listDeptYeWuSumCount = findByNameOne(params, listDeptYeWuCostSum, decimalFormat,listDeptYeWuSumCount);
			//租赁费
			params.put("amoeba_program", "租赁费");
			listDeptZuPingSumCount = findByNameOne(params, listDeptZuPingCostSum, decimalFormat,listDeptZuPingSumCount);
			//其他费用
			params.put("amoeba_program", "其他费用");
			listDeptQiTaFeiYonSumCount = findByNameOne(params, listDeptQiTaFeiYonCostSum, decimalFormat,listDeptQiTaFeiYonSumCount);
			//其他付款
			params.put("amoeba_program", "其他付款");
			listDeptQiTaFuKuanSumCount = findByNameOne(params, listDeptQiTaFuKuanCostSum, decimalFormat,listDeptQiTaFuKuanSumCount);
			//其它采购
			params.put("amoeba_program", "其它采购");
			listDeptQiTaCaiGouSumCount = findByNameOne(params, listDeptQiTaCaiGouCostSum, decimalFormat,listDeptQiTaCaiGouSumCount);
			//销售管理费用
			listDeptXiaoShouSumCount=listDeptBanGongSumCount+listDeptBeiYonSumCount
					+listDeptChailvSumCount+listDeptFuLiSumCount+listDeptGuangGaoSumCount+listDeptYeWuSumCount+listDeptZuPingSumCount
					+listDeptQiTaFeiYonSumCount+listDeptQiTaFuKuanSumCount+listDeptQiTaCaiGouSumCount;
			listDeptXiaoShouCostSum.add("<td>"+decimalFormat.format(listDeptXiaoShouSumCount)+"</td>");

			//采购仪器设备付款
			params.put("amoeba_program", "采购仪器设备付款");
			listDeptCaiGouYiQiSumCount = findByNameOne(params, listDeptCaiGouYiQiCostSum, decimalFormat,listDeptCaiGouYiQiSumCount);
			//工程
			params.put("amoeba_program", "工程");
			listDeptGongChenSumCount = findByNameOne(params, listDeptGongChenCostSum, decimalFormat,listDeptGongChenSumCount);
			//仪器设备
			params.put("amoeba_program", "仪器设备");
			listDeptYiQiSheBeiSumCount = findByNameOne(params, listDeptYiQiSheBeiCostSum, decimalFormat,listDeptYiQiSheBeiSumCount);
			//装修付款
			params.put("amoeba_program", "装修付款");
			listDeptZhuangXiuFuKuanSumCount = findByNameOne(params, listDeptZhuangXiuFuKuanCostSum, decimalFormat,listDeptZhuangXiuFuKuanSumCount);
			//对外投资
			map.put("amoeba_program", "对外投资");
			List<Map<String, Object>> tempInvestmentCostSum = rdsFinanceConfigNewMapper
					.queryDeptDepreciationCostSum(map);
			if(tempInvestmentCostSum.size()==0){			
				listDeptInvestmentSum.add("<td>0</td>");
			}
			else{
				listDeptInvestmentSumCount= (Double)(tempInvestmentCostSum.get(0)==null?0.0:tempInvestmentCostSum.get(0).get("depreciationSum"));
				listDeptInvestmentSum.add("<td>"+decimalFormat.format(listDeptInvestmentSumCount)+"</td>");
			}
			//其他支出
			listDeptQiTaZhiChuSumCount= listDeptCaiGouYiQiSumCount+listDeptGongChenSumCount
					+listDeptYiQiSheBeiSumCount+listDeptZhuangXiuFuKuanSumCount;
			listDeptQiTaZhiChuCostSum.add("<td>"+decimalFormat.format(listDeptQiTaZhiChuSumCount)+"</td>");


			//合作方（应收）结算收入
			params.put("amoeba_program", "合作方收入");
			List<Map<String, Object>> tempHeZouFangSum = rdsFinanceConfigNewMapper
					.queryDeptHeZouFangCostSum(params);
			if(tempHeZouFangSum.size()==0){			
				listDeptHeZouFangCostSum.add("<td>0</td>");
			}
			else{
				listDeptHeZouFangSumCount= (Double)(tempHeZouFangSum.get(0)==null?0.0:tempHeZouFangSum.get(0).get("HeZouFangSum"));
				listDeptHeZouFangCostSum.add("<td>"+decimalFormat.format(listDeptHeZouFangSumCount)+"</td>");
			}

			//内部结算收入
			//TODO
			String deptnames=null;
			List<Map<String, Object>> tempInsideSum =null;
			if(listDeptTemp2==null || shangji.size()==1){
				deptnames=(String) params.get("deptname");
				if("司法鉴定事业部".equals(deptnames)){
					tempInsideSum = rdsFinanceConfigNewMapper
							.queryDeptInsideSum1(params);
				}else if("精准医学事业部".equals(deptnames)){
					 tempInsideSum = rdsFinanceConfigNewMapper
							.queryDeptInsideSum2(params);
				}else if("公司总部".equals(deptnames)){
					 tempInsideSum = rdsFinanceConfigNewMapper
								.queryDeptInsideSum3(params);
				} 
			}else if(shangji.size()==2){
				deptnames=(String) params.get("deptname1");
				if("技术与服务管理部".equals(string)){
					tempInsideSum = rdsFinanceConfigNewMapper
							.queryDeptInsideSum1(params);
				}else if("精准医学事业部".equals(string)){
					 tempInsideSum = rdsFinanceConfigNewMapper
							.queryDeptInsideSum2(params);
				} 
			}
			if(tempInsideSum==null){
				listDeptInsideSumCount=0.0;				
				listDeptInsideSum.add("<td>0</td>");
			}
			else{
				listDeptInsideSumCount = (Double)(tempInsideSum.get(0)==null?0.0:tempInsideSum.get(0).get("deptInsideSum"));
				listDeptInsideSum.add("<td>"+decimalFormat.format(listDeptInsideSumCount)+"</td>");
			}
			
			//其他费用（含折旧及摊销）
			map.put("amoeba_program", "其他费用（含折旧及摊销）");
			List<Map<String, Object>> tempOtherCostSum = rdsFinanceConfigNewMapper
					.queryDeptDepreciationCostSum(map);
			if(tempOtherCostSum.size()==0){			
				listDeptOtherSum.add("<td>0</td>");
			}
			else{
				listDeptOtherSumCount= (Double)(tempOtherCostSum.get(0)==null?0.0:tempOtherCostSum.get(0).get("depreciationSum"));
				listDeptOtherSum.add("<td>"+decimalFormat.format(listDeptOtherSumCount)+"</td>");
			}
			//资质费用
			List<Map<String, Object>> tempDeptAptitudeSum=null;
			if("司法鉴定事业部".equals(deptnames)){
				tempDeptAptitudeSum = rdsFinanceConfigNewMapper
						.queryDeptAptitudeCostSum(params);
			} 
			if(tempDeptAptitudeSum==null){			
				listDeptAptitudeSum.add("<td>0</td>");
			}
			else{
				listDeptAptitudeSumCount = (Double)(tempDeptAptitudeSum.get(0)==null?0.0:tempDeptAptitudeSum.get(0).get("deptAptitudeCostSum"));
				listDeptAptitudeSum.add("<td>"+decimalFormat.format(listDeptAptitudeSumCount)+"</td>");
			}
			//内部结算成本
			List<Map<String, Object>> tempCostInsideSum=null;
			if(map.size()==5  && "精准医学事业部".equals(string)){
				 tempCostInsideSum = rdsFinanceConfigNewMapper
							.queryCostInsideSum2(params);
			}else{
				 tempCostInsideSum = rdsFinanceConfigNewMapper
							.queryCostInsideSum(params);
			} 
			if(tempCostInsideSum.size()==0){			
				listDeptCostInsideSum.add("<td>0</td>");
			}
			else{
				listDeptCostInsideSumCount = (Double)(tempCostInsideSum.get(0)==null?0.0:tempCostInsideSum.get(0).get("costInsideSum"));
				listDeptCostInsideSum.add("<td>"+decimalFormat.format(listDeptCostInsideSumCount)+"</td>");
			}

			//委外费用 
			listDeptWeiWaiSumCount=listDeptExternalSumCount+listDeptDaiLiSumCount+listDeptZiZhiSumCount;
			listDeptWeiWaiCostSum.add("<td>"+decimalFormat.format(listDeptWeiWaiSumCount)+"</td>");
			
			//销售管理费用
			List<Map<String, Object>> tempSaleManageSum = rdsFinanceConfigNewMapper
					.querySaleManageSum(params);
			if(tempSaleManageSum.size()==0){			
				listDeptSaleManageSum.add("<td>0</td>");
			}
			else{
				listDeptSaleManageSumCount = (Double)(tempSaleManageSum.get(0)==null?0.0:tempSaleManageSum.get(0).get("deptSaleManageSum"));
				listDeptSaleManageSum.add("<td>"+decimalFormat.format(listDeptSaleManageSumCount)+"</td>");
			}

			//仪器设备
			List<Map<String, Object>> tempInstrumentCostSum = rdsFinanceConfigNewMapper
					.queryDeptInstrumentCostSum(params);
			if(tempInstrumentCostSum.size()==0){			
				listDeptInstrumentSum.add("<td>0</td>");
			}
			else{
				listDeptInstrumentSumCount= (Double)(tempInstrumentCostSum.get(0)==null?0.0:tempInstrumentCostSum.get(0).get("instrumentSum"));
				listDeptInstrumentSum.add("<td>"+decimalFormat.format(listDeptInstrumentSumCount)+"</td>");
			}
			
			//支出费用小计
			listDeptZhiChuFeiYonSumCount=ListDeptWagesSum+ListDeptMaterialSum
					+listDeptWeiWaiSumCount+listDeptXiaoShouSumCount+listDeptQiTaZhiChuSumCount;
			listDeptZhiChuFeiYonCostSum.add("<td>"+decimalFormat.format(listDeptZhiChuFeiYonSumCount)+"</td>");
			
			
			//现金流量
			listDeptXianJinSumCount=ListDeptInTaxSum-ListDeptTaxSum - listDeptZhiChuFeiYonSumCount;
			listDeptXianJinCostSum.add("<td>"+decimalFormat.format(listDeptXianJinSumCount)+"</td>");
			//所得税
			listDeptSuoDeiSumCount=listDeptXianJinSumCount * 0.2 ;
			listDeptSuoDeiCostSum.add("<td>"+decimalFormat.format(listDeptSuoDeiSumCount)+"</td>");
			//现金流量净额
			listDeptJingESumCount= listDeptXianJinSumCount - listDeptSuoDeiSumCount;
			listDeptJingECostSum.add("<td>"+decimalFormat.format(listDeptJingESumCount)+"</td>");
			
			//运营利润额
			listDeptYunYinSumCount= listDeptXianJinSumCount+listDeptInsideSumCount-listDeptCostInsideSumCount
					+listDeptInvestmentSumCount+listDeptQiTaZhiChuSumCount-listDeptOtherSumCount;
			listDeptYunYinCostSum.add("<td>"+decimalFormat.format(listDeptYunYinSumCount)+"</td>");
			//所得税
			listDeptSuoDeiShuiSumCount=listDeptYunYinSumCount*0.2;
			listDeptSuoDeiShuiCostSum.add("<td>"+decimalFormat.format(listDeptSuoDeiShuiSumCount)+"</td>");
			//运营净利润额
			listDeptYunYinJingLiRunSumCount=listDeptYunYinSumCount - listDeptSuoDeiShuiSumCount;
			listDeptYunYinJingLiRunCostSum.add("<td>"+decimalFormat.format(listDeptYunYinJingLiRunSumCount)+"</td>");
			//未实付运营费用
			Double a=listDeptAptitudeSumCount == 0.0?0.0:listDeptAptitudeSumCount-listDeptZiZhiSumCount;
			listDeptWeiShiFuYunYinSumCount=a - listDeptHeZouFangSumCount;
			listDeptWeiShiFuYunYinCostSum.add("<td>"+decimalFormat.format(listDeptWeiShiFuYunYinSumCount)+"</td>");
			//虚拟运营净利润合计
			listDeptXuNiYunYinSumCount=listDeptYunYinJingLiRunSumCount - listDeptWeiShiFuYunYinSumCount;
			listDeptXuNiYunYinCostSum.add("<td>"+decimalFormat.format(listDeptXuNiYunYinSumCount)+"</td>");


			listDeptShouRuBuSumAllCount+=listDeptShouRuBuSum;
			listDeptServiceSumAllCount+=listDeptServiceSumCount;
			listDeptSellSumAllCount+=listDeptSellSumCount;
			listDeptHeZouAllCount+=listDeptHeZouCount;
			listDeptInsideSumAllCount+=listDeptInsideSumCount;
			ListDeptInTaxAllSum+=ListDeptInTaxSum;
			ListDeptTaxAllSum+=ListDeptTaxSum;
			ListDeptOutTaxAllSum+=ListDeptOutTaxSum;
			ListDeptWagesAllSum+=ListDeptWagesSum;
			listDeptWeiWaiAllSum+=listDeptWeiWaiSumCount;
			listDeptMaterialCostAllSum+=ListDeptMaterialSum;
			listDeptCaiGouAllSum+=ListDeptCaiGouSum;
			listDeptHaoCaiAllSum+=ListDeptHaoCaiSum;
			listDeptExternalAllSum+=listDeptExternalSumCount;
			listDeptDaiLiAllSum+=listDeptDaiLiSumCount;
			listDeptZiZhiAllSum+=listDeptZiZhiSumCount;

			listDeptXiaoShouAllSum+=listDeptXiaoShouSumCount;
			listDeptBanGongAllSum+=listDeptBanGongSumCount;
			listDeptBeiYonAllSum+=listDeptBeiYonSumCount;
			listDeptChailvAllSum+=listDeptChailvSumCount;
			listDeptFuLiAllSum+=listDeptFuLiSumCount;
			listDeptGuangGaoAllSum+=listDeptGuangGaoSumCount;
			listDeptYeWuAllSum+=listDeptYeWuSumCount;
			listDeptZuPingAllSum+=listDeptZuPingSumCount;
			listDeptQiTaFeiYonAllSum+=listDeptQiTaFeiYonSumCount;
			listDeptQiTaFuKuanAllSum+=listDeptQiTaFuKuanSumCount;
			listDeptQiTaCaiGouAllSum+=listDeptQiTaCaiGouSumCount;

			listDeptXianJinAllSum+=listDeptXianJinSumCount;
			listDeptSuoDeiAllSum+=listDeptSuoDeiSumCount;
			listDeptJingEAllSum+=listDeptJingESumCount;

			listDeptHeZouFangAllSum+=listDeptHeZouFangSumCount;

			listDeptYunYinAllSum+=listDeptYunYinSumCount;
			listDeptSuoDeiShuiAllSum+=listDeptSuoDeiShuiSumCount;
			listDeptYunYinJingLiRunAllSum+=listDeptYunYinJingLiRunSumCount;
			listDeptWeiShiFuYunYinAllSum+=listDeptWeiShiFuYunYinSumCount;
			listDeptXuNiYunYinAllSum+=listDeptXuNiYunYinSumCount;

			listDeptQiTaZhiChuAllSum+=listDeptQiTaZhiChuSumCount;
			listDeptCaiGouYiQiAllSum+=listDeptCaiGouYiQiSumCount;
			listDeptGongChenAllSum+=listDeptGongChenSumCount;
			listDeptYiQiSheBeiAllSum+=listDeptYiQiSheBeiSumCount;
			listDeptZhuangXiuFuKuanAllSum+=listDeptZhuangXiuFuKuanSumCount;
			listDeptZhiChuFeiYonAllSum+=listDeptZhiChuFeiYonSumCount;

			listDeptCostInsideAllSum+=listDeptCostInsideSumCount;
			listDeptSaleManageAllSum+=listDeptSaleManageSumCount;
			listDeptAptitudeAllSum+=listDeptAptitudeSumCount;
			listDeptOtherAllSum+=listDeptOtherSumCount;
			listDeptInvestmentAllSum+=listDeptInvestmentSumCount;
			listDeptInstrumentAllSum+=listDeptInstrumentSumCount;

			//成本小计
			listSumCount=ListDeptWagesSum+ListDeptMaterialSum+listDeptExternalSumCount+listDeptCostInsideSumCount+listDeptSaleManageSumCount+listDeptAptitudeSumCount+listDeptOtherSumCount;
			listSum.add("<td>"+decimalFormat.format(listSumCount)+"</td>");
			listProfitCount = ListDeptOutTaxSum+listDeptInsideSumCount-listSumCount;
			listProfit.add("<td>"+decimalFormat.format(listProfitCount)+"</td>");
			listTaxCount = listProfitCount*0.2;
			listTax.add("<td>"+decimalFormat.format(listTaxCount)+"</td>");
			listNetProfitCount=listProfitCount-listTaxCount;
			listNetProfit.add("<td>"+decimalFormat.format(listNetProfitCount)+"</td>");

			listSumAllCount+=listSumCount;
			listProfitAllCount+=listProfitCount;
			listTaxAllCount+=listTaxCount;
			listNetProfitAllCount+=listNetProfitCount;

		}

		listDept.add("合计");
		list.add(listDept);
		//服务收入
		listDeptServiceSum.add("<td>"+decimalFormat.format(listDeptServiceSumAllCount)+"</td>");
		list.add(listDeptServiceSum);
		//销售收入
		listDeptSellSum.add("<td>"+decimalFormat.format(listDeptSellSumAllCount)+"</td>");
		list.add(listDeptSellSum);
		//合作方（实收）收入
		listDeptHeZou.add("<td>"+decimalFormat.format(listDeptHeZouAllCount)+"</td>");
		list.add(listDeptHeZou);

		//收入小计（含税）
		listDeptInTaxSum.add("<td>"+decimalFormat.format(ListDeptInTaxAllSum)+"</td>");
		list.add(listDeptInTaxSum);
		//收入小计（税）
		listDeptTaxSum.add("<td>"+decimalFormat.format(ListDeptTaxAllSum)+"</td>");
		list.add(listDeptTaxSum);
		//收入小计（不含税）
		listDeptOutTaxSum.add("<td>"+decimalFormat.format(listDeptShouRuBuSumAllCount)+"</td>");
		list.add(listDeptOutTaxSum);
		//空格
		listDeptNull1.add("<td>-</td>");
		list.add(listDeptNull1);
		//人工成本
		listDeptWagesSum.add("<td>"+decimalFormat.format(ListDeptWagesAllSum)+"</td>");
		list.add(listDeptWagesSum);
		//材料成本
		listDeptMaterialCostSum.add("<td>"+decimalFormat.format(listDeptMaterialCostAllSum)+"</td>");
		list.add(listDeptMaterialCostSum);
		//采购材料付款
		listDeptCaiGouSum.add("<td>"+decimalFormat.format(listDeptCaiGouAllSum)+"</td>");
		list.add(listDeptCaiGouSum);
		//耗材
		listDeptHaoCaiSum.add("<td>"+decimalFormat.format(listDeptHaoCaiAllSum)+"</td>");
		list.add(listDeptHaoCaiSum);
		//委外费用
		listDeptWeiWaiCostSum.add("<td>"+decimalFormat.format(listDeptWeiWaiAllSum)+"</td>");
		list.add(listDeptWeiWaiCostSum);
		//委外检测成本
		listDeptExternalCostSum.add("<td>"+decimalFormat.format(listDeptExternalAllSum)+"</td>");
		list.add(listDeptExternalCostSum);
		//代理费
		listDeptDaiLiCostSum.add("<td>"+decimalFormat.format(listDeptDaiLiAllSum)+"</td>");
		list.add(listDeptDaiLiCostSum);
		//资质费
		listDeptZiZhiCostSum.add("<td>"+decimalFormat.format(listDeptZiZhiAllSum)+"</td>");
		list.add(listDeptZiZhiCostSum);
		//销售管理费用
		listDeptXiaoShouCostSum.add("<td>"+decimalFormat.format(listDeptXiaoShouAllSum)+"</td>");
		list.add(listDeptXiaoShouCostSum);
		//办公费
		listDeptBanGongCostSum.add("<td>"+decimalFormat.format(listDeptBanGongAllSum)+"</td>");
		list.add(listDeptBanGongCostSum);
		//备用金
		listDeptBeiYonCostSum.add("<td>"+decimalFormat.format(listDeptBeiYonAllSum)+"</td>");
		list.add(listDeptBeiYonCostSum);
		//差旅费
		listDeptChailvCostSum.add("<td>"+decimalFormat.format(listDeptChailvAllSum)+"</td>");
		list.add(listDeptChailvCostSum);
		//福利费
		listDeptFuLiCostSum.add("<td>"+decimalFormat.format(listDeptFuLiAllSum)+"</td>");
		list.add(listDeptFuLiCostSum);
		//广告宣传
		listDeptGuangGaoCostSum.add("<td>"+decimalFormat.format(listDeptGuangGaoAllSum)+"</td>");
		list.add(listDeptGuangGaoCostSum);
		//业务招待费
		listDeptYeWuCostSum.add("<td>"+decimalFormat.format(listDeptYeWuAllSum)+"</td>");
		list.add(listDeptYeWuCostSum);
		//租赁费
		listDeptZuPingCostSum.add("<td>"+decimalFormat.format(listDeptZuPingAllSum)+"</td>");
		list.add(listDeptZuPingCostSum);
		//其他费用
		listDeptQiTaFeiYonCostSum.add("<td>"+decimalFormat.format(listDeptQiTaFeiYonAllSum)+"</td>");
		list.add(listDeptQiTaFeiYonCostSum);
		//其他付款
		listDeptQiTaFuKuanCostSum.add("<td>"+decimalFormat.format(listDeptQiTaFuKuanAllSum)+"</td>");
		list.add(listDeptQiTaFuKuanCostSum);
		//其它采购
		listDeptQiTaCaiGouCostSum.add("<td>"+decimalFormat.format(listDeptQiTaCaiGouAllSum)+"</td>");
		list.add(listDeptQiTaCaiGouCostSum);

		//对外投资
		listDeptInvestmentSum.add("<td>"+decimalFormat.format(listDeptInvestmentAllSum)+"</td>");
		list.add(listDeptInvestmentSum);

		//其他支出
		listDeptQiTaZhiChuCostSum.add("<td>"+decimalFormat.format(listDeptQiTaZhiChuAllSum)+"</td>");
		list.add(listDeptQiTaZhiChuCostSum);
		//采购仪器设备付款
		listDeptCaiGouYiQiCostSum.add("<td>"+decimalFormat.format(listDeptCaiGouYiQiAllSum)+"</td>");
		list.add(listDeptCaiGouYiQiCostSum);
		//工程
		listDeptGongChenCostSum.add("<td>"+decimalFormat.format(listDeptGongChenAllSum)+"</td>");
		list.add(listDeptGongChenCostSum);
		//仪器设备
		listDeptYiQiSheBeiCostSum.add("<td>"+decimalFormat.format(listDeptYiQiSheBeiAllSum)+"</td>");
		list.add(listDeptYiQiSheBeiCostSum);
		//装修付款
		listDeptZhuangXiuFuKuanCostSum.add("<td>"+decimalFormat.format(listDeptZhuangXiuFuKuanAllSum)+"</td>");
		list.add(listDeptZhuangXiuFuKuanCostSum);
		//支出费用小计
		listDeptZhiChuFeiYonCostSum.add("<td>"+decimalFormat.format(listDeptZhiChuFeiYonAllSum)+"</td>");
		list.add(listDeptZhiChuFeiYonCostSum);
		//空格
		list.add(listDeptNull1);

		//现金流量
		listDeptXianJinCostSum.add("<td>"+decimalFormat.format(listDeptXianJinAllSum)+"</td>");
		list.add(listDeptXianJinCostSum);
		//所得税
		listDeptSuoDeiCostSum.add("<td>"+decimalFormat.format(listDeptSuoDeiAllSum)+"</td>");
		list.add(listDeptSuoDeiCostSum);
		//现金流量净额
		listDeptJingECostSum.add("<td>"+decimalFormat.format(listDeptJingEAllSum)+"</td>");
		list.add(listDeptJingECostSum);
		//空格
		list.add(listDeptNull1);

		//合作方（应收）结算收入
		listDeptHeZouFangCostSum.add("<td>"+decimalFormat.format(listDeptHeZouFangAllSum)+"</td>");
		list.add(listDeptHeZouFangCostSum);
		//内部结算收入
		listDeptInsideSum.add("<td>"+decimalFormat.format(listDeptInsideSumAllCount)+"</td>");
		list.add(listDeptInsideSum);
		//其他费用（含折旧及摊销）
		listDeptOtherSum.add("<td>"+decimalFormat.format(listDeptOtherAllSum)+"</td>");
		list.add(listDeptOtherSum);
		//合作方案例资质费用
		listDeptAptitudeSum.add("<td>"+decimalFormat.format(listDeptAptitudeAllSum)+"</td>");
		list.add(listDeptAptitudeSum);
		//内部结算成本
		listDeptCostInsideSum.add("<td>"+decimalFormat.format(listDeptCostInsideAllSum)+"</td>");
		list.add(listDeptCostInsideSum);
		//空格
		list.add(listDeptNull1);

		//运营利润额
		listDeptYunYinCostSum.add("<td>"+decimalFormat.format(listDeptYunYinAllSum)+"</td>");
		list.add(listDeptYunYinCostSum);
		//所得税
		listDeptSuoDeiShuiCostSum.add("<td>"+decimalFormat.format(listDeptSuoDeiShuiAllSum)+"</td>");
		list.add(listDeptSuoDeiShuiCostSum);
		//运营净利润额
		listDeptYunYinJingLiRunCostSum.add("<td>"+decimalFormat.format(listDeptYunYinJingLiRunAllSum)+"</td>");
		list.add(listDeptYunYinJingLiRunCostSum);
		//未实付运营费用
		listDeptWeiShiFuYunYinCostSum.add("<td>"+decimalFormat.format(listDeptWeiShiFuYunYinAllSum)+"</td>");
		list.add(listDeptWeiShiFuYunYinCostSum);
		//虚拟运营净利润合计
		listDeptXuNiYunYinCostSum.add("<td>"+decimalFormat.format(listDeptXuNiYunYinAllSum)+"</td>");
		list.add(listDeptXuNiYunYinCostSum);

		return list;
	}

	private Double findByNameOne(Map<String, Object> params, List<String> listDeptBanGongCostSum,
			DecimalFormat decimalFormat, Double listDeptBanGongSumCount) {
		List<Map<String, Object>> tempBanGongCostSum = rdsFinanceConfigNewMapper
				.queryDeptExternalCostSum(params);
		if(tempBanGongCostSum.size()==0){			
			listDeptBanGongCostSum.add("<td>0</td>");
		}
		else{
			listDeptBanGongSumCount= (Double)(tempBanGongCostSum.get(0)==null?0.0:tempBanGongCostSum.get(0).get("deptExternalCostSum"));
			listDeptBanGongCostSum.add("<td>"+decimalFormat.format(listDeptBanGongSumCount)+"</td>");
		}
		return listDeptBanGongSumCount;
	}
	private Double findByNameOneE(Map<String, Object> params, List<String> listDeptBanGongCostSum,
			DecimalFormat decimalFormat, Double listDeptBanGongSumCount) {
		List<Map<String, Object>> tempBanGongCostSum = rdsFinanceConfigNewMapper
				.queryDeptExternalCostSum(params);
		if(tempBanGongCostSum.size()==0){			
			listDeptBanGongCostSum.add("0");
		}
		else{
			listDeptBanGongSumCount= (Double)(tempBanGongCostSum.get(0)==null?0.0:tempBanGongCostSum.get(0).get("deptExternalCostSum"));
			listDeptBanGongCostSum.add(decimalFormat.format(listDeptBanGongSumCount));
		}
		return listDeptBanGongSumCount;
	}

	private Double findByName(Map<String, Object> params, List<String> listDeptBeiYonCostSum,
			DecimalFormat decimalFormat, Double listDeptBeiYonSumCount) {
		List<Map<String, Object>> tempBeiYonCostSum = rdsFinanceConfigNewMapper
				.queryDeptBeiYonCostSum(params);
		if(tempBeiYonCostSum.size()==0){			
			listDeptBeiYonCostSum.add("<td>0</td>");
		}
		else{
			listDeptBeiYonSumCount= (Double)(tempBeiYonCostSum.get(0)==null?0.0:tempBeiYonCostSum.get(0).get("deptExternalCostSum"));
			listDeptBeiYonCostSum.add("<td>"+decimalFormat.format(listDeptBeiYonSumCount)+"</td>");
		}
		return listDeptBeiYonSumCount;
	}
	private Double findByNameE(Map<String, Object> params, List<String> listDeptBeiYonCostSum,
			DecimalFormat decimalFormat, Double listDeptBeiYonSumCount) {
		List<Map<String, Object>> tempBeiYonCostSum = rdsFinanceConfigNewMapper
				.queryDeptBeiYonCostSum(params);
		if(tempBeiYonCostSum.size()==0){			
			listDeptBeiYonCostSum.add("0");
		}
		else{
			listDeptBeiYonSumCount= (Double)(tempBeiYonCostSum.get(0)==null?0.0:tempBeiYonCostSum.get(0).get("deptExternalCostSum"));
			listDeptBeiYonCostSum.add(decimalFormat.format(listDeptBeiYonSumCount));
		}
		return listDeptBeiYonSumCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<List<String>> queryAmoebaThree(Map<String, Object> params) {
		List<List<String>> list = new ArrayList<>();
		List<String> listDept = new ArrayList<>();
		List<String> listDeptTemp = new ArrayList<String>();
		List<String> listDeptTemp2 = (List<String>) params.get("listDeptTemp");
		String listDeptTempId = (String) params.get("deptnameId");
		String deptname=null;
		String deptname1=null;

		List<RdsStatisticsDepartmentModel> listBuMen=rdsFinanceConfigNewMapper.queryXiaJiBuMen(listDeptTempId);
		for (RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel : listBuMen) {
			listDept.add(rdsStatisticsDepartmentModel.getDeptname());
			listDeptTemp.add(rdsStatisticsDepartmentModel.getDeptname());
		}

		if(params.get("listDeptTemp")!=null){
			RdsStatisticsDepartmentModel buMen = rdsFinanceConfigNewMapper.queryBuMen(listDeptTempId);
			RdsStatisticsDepartmentModel buMen2 = rdsFinanceConfigNewMapper.queryBuMen(listDeptTempId);
			deptname1 = buMen.getDeptname();
			deptname= buMen2.getDeptname();
		}else{
			RdsStatisticsDepartmentModel buMen = rdsFinanceConfigNewMapper.queryBuMen(listDeptTempId);
			RdsStatisticsDepartmentModel buMen2 = rdsFinanceConfigNewMapper.queryBuMen2(listDeptTempId);
			deptname1 = buMen.getDeptname();
			deptname= buMen2.getDeptname();
		}


		//部门服务收入
		List<String> listDeptServiceSum = new ArrayList<>();
		listDeptServiceSum.add("<th>服务收入</th>");
		//部门销售收入
		List<String> listDeptSellSum = new ArrayList<>();
		listDeptSellSum.add("<th>销售收入</th>");
		//合作方（实收）收入
		List<String> listDeptHeZou = new ArrayList<>();
		listDeptHeZou.add("<th>合作方（实收）收入</th>");	 
		//部门内部结算收入
		List<String> listDeptInsideSum = new ArrayList<>();
		listDeptInsideSum.add("<th>内部结算收入</th>");
		//部门收入小计（含税）
		List<String> listDeptInTaxSum = new ArrayList<>();
		listDeptInTaxSum.add("<th>收入小计(含税)</th>");
		//部门收入小计（税）
		List<String> listDeptTaxSum = new ArrayList<>();
		listDeptTaxSum.add("<th>税</th>");
		//部门收入小计（不含税）
		List<String> listDeptOutTaxSum = new ArrayList<>();
		listDeptOutTaxSum.add("<th><h2>收入小计（不含税）</h2></th>");
		//空格
		List<String> listDeptNull = new ArrayList<>();
		listDeptNull.add("<th></th>");
		//人工成本
		List<String> listDeptWagesSum = new ArrayList<>();
		listDeptWagesSum.add("<th><h3>人工成本</h3></th>");
		//材料成本
		List<String> listDeptMaterialCostSum = new ArrayList<>();
		listDeptMaterialCostSum.add("<th><h3>材料成本</h3></th>");
		//采购材料付款
		List<String> listDeptCaiGouSum = new ArrayList<>();
		listDeptCaiGouSum.add("<th>采购材料付款</th>");
		//耗材
		List<String> listDeptHaoCaiSum = new ArrayList<>();
		listDeptHaoCaiSum.add("<th>耗材</th>");
		//委外费用
		List<String> listDeptWeiWaiCostSum = new ArrayList<>();
		listDeptWeiWaiCostSum.add("<th><h3>委外费用</h3></th>");
		//委外检测成本
		List<String> listDeptExternalCostSum = new ArrayList<>();
		listDeptExternalCostSum.add("<th>委外检测成本</th>");
		//代理费
		List<String> listDeptDaiLiCostSum = new ArrayList<>();
		listDeptDaiLiCostSum.add("<th>代理费</th>");
		//资质费
		List<String> listDeptZiZhiCostSum = new ArrayList<>();
		listDeptZiZhiCostSum.add("<th>资质费</th>");

		//销售管理费用
		List<String> listDeptXiaoShouCostSum = new ArrayList<>();
		listDeptXiaoShouCostSum.add("<th><h3>销售管理费用</h3></th>");
		//办公费
		List<String> listDeptBanGongCostSum = new ArrayList<>();
		listDeptBanGongCostSum.add("<th>办公费</th>");
		//备用金
		List<String> listDeptBeiYonCostSum = new ArrayList<>();
		listDeptBeiYonCostSum.add("<th>备用金</th>");
		//差旅费
		List<String> listDeptChailvCostSum = new ArrayList<>();
		listDeptChailvCostSum.add("<th>差旅费</th>");
		//福利费
		List<String> listDeptFuLiCostSum = new ArrayList<>();
		listDeptFuLiCostSum.add("<th>福利费</th>");
		//广告宣传
		List<String> listDeptGuangGaoCostSum = new ArrayList<>();
		listDeptGuangGaoCostSum.add("<th>广告宣传</th>");
		//业务招待费
		List<String> listDeptYeWuCostSum = new ArrayList<>();
		listDeptYeWuCostSum.add("<th>业务招待费</th>");
		//租赁费
		List<String> listDeptZuPingCostSum = new ArrayList<>();
		listDeptZuPingCostSum.add("<th>租赁费</th>");
		//其他费用
		List<String> listDeptQiTaFeiYonCostSum = new ArrayList<>();
		listDeptQiTaFeiYonCostSum.add("<th>其他费用</th>");
		//其他付款
		List<String> listDeptQiTaFuKuanCostSum = new ArrayList<>();
		listDeptQiTaFuKuanCostSum.add("<th>其他付款</th>");
		//其它采购
		List<String> listDeptQiTaCaiGouCostSum = new ArrayList<>();
		listDeptQiTaCaiGouCostSum.add("<th>其它采购</th>");

		//对外投资
		List<String> listDeptInvestmentSum = new ArrayList<>();
		listDeptInvestmentSum.add("<th><h3>对外投资</h3></th>");

		//其他支出
		List<String> listDeptQiTaZhiChuCostSum = new ArrayList<>();
		listDeptQiTaZhiChuCostSum.add("<th><h3>其他支出</h3></th>");
		//采购仪器设备付款
		List<String> listDeptCaiGouYiQiCostSum = new ArrayList<>();
		listDeptCaiGouYiQiCostSum.add("<th>采购仪器设备付款</th>");
		//工程
		List<String> listDeptGongChenCostSum = new ArrayList<>();
		listDeptGongChenCostSum.add("<th>工程</th>");
		//仪器设备
		List<String> listDeptYiQiSheBeiCostSum = new ArrayList<>();
		listDeptYiQiSheBeiCostSum.add("<th>仪器设备</th>");
		//装修付款
		List<String> listDeptZhuangXiuFuKuanCostSum = new ArrayList<>();
		listDeptZhuangXiuFuKuanCostSum.add("<th>装修付款</th>");
		//支出费用小计
		List<String> listDeptZhiChuFeiYonCostSum = new ArrayList<>();
		listDeptZhiChuFeiYonCostSum.add("<th><h2>支出费用小计</h2></th>");

		//现金流量
		List<String> listDeptXianJinCostSum = new ArrayList<>();
		listDeptXianJinCostSum.add("<th>现金流量</th>");
		//所得税
		List<String> listDeptSuoDeiCostSum = new ArrayList<>();
		listDeptSuoDeiCostSum.add("<th>所得税</th>");
		//现金流量净额
		List<String> listDeptJingECostSum = new ArrayList<>();
		listDeptJingECostSum.add("<th>现金流量净额</th>");

		//合作方（应收）结算收入
		List<String> listDeptHeZouFangCostSum = new ArrayList<>();
		listDeptHeZouFangCostSum.add("<th>合作方（应收）结算收入</th>");

		//运营利润额
		List<String> listDeptYunYinCostSum = new ArrayList<>();
		listDeptYunYinCostSum.add("<th>运营利润额</th>");
		//所得税
		List<String> listDeptSuoDeiShuiCostSum = new ArrayList<>();
		listDeptSuoDeiShuiCostSum.add("<th>所得税</th>");
		//运营净利润额
		List<String> listDeptYunYinJingLiRunCostSum = new ArrayList<>();
		listDeptYunYinJingLiRunCostSum.add("<th>运营净利润额</th>");
		//未实付运营费用
		List<String> listDeptWeiShiFuYunYinCostSum = new ArrayList<>();
		listDeptWeiShiFuYunYinCostSum.add("<th>未实付运营费用</th>");
		//虚拟运营净利润合计
		List<String> listDeptXuNiYunYinCostSum = new ArrayList<>();
		listDeptXuNiYunYinCostSum.add("<th><h2>虚拟运营净利润合计</h2></th>");

		//内部结算成本
		List<String> listDeptCostInsideSum = new ArrayList<>();
		listDeptCostInsideSum.add("<th>内部结算成本</th>");
		//销售管理结算成本
		List<String> listDeptSaleManageSum = new ArrayList<>();
		listDeptSaleManageSum.add("<th>销售管理费用</th>");
		//资质费用
		List<String> listDeptAptitudeSum = new ArrayList<>();
		listDeptAptitudeSum.add("<th>资质费用</th>");
		//其他费用（含折旧及摊销）
		List<String> listDeptOtherSum = new ArrayList<>();
		listDeptOtherSum.add("<th>其他费用（含折旧及摊销）</th>");
		//仪器设备
		List<String> listDeptInstrumentSum = new ArrayList<>();
		listDeptInstrumentSum.add("<th>房屋、装修、仪器及设备采购成本</th>");
		//成本小计
		List<String> listSum = new ArrayList<>();
		listSum.add("<th>成本小计</th>");
		//空格
		List<String> listDeptNull1 = new ArrayList<>();
		listDeptNull1.add("<th></th>");
		//利润小计
		List<String> listProfit = new ArrayList<>();
		listProfit.add("<th>利润小计</th>");
		//税
		List<String> listTax = new ArrayList<>();
		listTax.add("<th>税</th>");
		//净利润小计
		List<String> listNetProfit = new ArrayList<>();
		listNetProfit.add("<th>净利润</th>");
		// 内部结算按照月份计算
		Map<String, Object> map = new HashMap<>();
		map.put("confirm_date_start", params.get("confirm_date_start").toString()
				.substring(0, 7));
		map.put("confirm_date_end", params.get("confirm_date_end").toString()
				.substring(0, 7));
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置

		Double listDeptShouRuBuSumAllCount = 0.0;
		Double listDeptServiceSumAllCount = 0.0;
		Double listDeptSellSumAllCount = 0.0;
		Double listDeptHeZouAllCount = 0.0;
		Double listDeptInsideSumAllCount = 0.0;
		Double ListDeptInTaxAllSum = 0.0;
		Double ListDeptTaxAllSum = 0.0;
		Double ListDeptOutTaxAllSum = 0.0;
		Double ListDeptWagesAllSum = 0.0;
		Double listDeptMaterialCostAllSum=0.0;
		Double listDeptCaiGouAllSum=0.0;
		Double listDeptHaoCaiAllSum=0.0;
		Double listDeptWeiWaiAllSum =0.0;
		Double listDeptExternalAllSum =0.0;
		Double listDeptDaiLiAllSum =0.0;
		Double listDeptZiZhiAllSum =0.0;

		Double listDeptXiaoShouAllSum =0.0;
		Double listDeptBanGongAllSum =0.0;
		Double listDeptBeiYonAllSum =0.0;
		Double listDeptChailvAllSum =0.0;
		Double listDeptFuLiAllSum =0.0;
		Double listDeptGuangGaoAllSum =0.0;
		Double listDeptYeWuAllSum =0.0;
		Double listDeptZuPingAllSum =0.0;
		Double listDeptQiTaFeiYonAllSum =0.0;
		Double listDeptQiTaFuKuanAllSum =0.0;
		Double listDeptQiTaCaiGouAllSum =0.0;

		Double listDeptQiTaZhiChuAllSum =0.0;
		Double listDeptCaiGouYiQiAllSum =0.0;
		Double listDeptGongChenAllSum =0.0;
		Double listDeptYiQiSheBeiAllSum =0.0;
		Double listDeptZhuangXiuFuKuanAllSum =0.0;
		Double listDeptZhiChuFeiYonAllSum =0.0;

		Double listDeptXianJinAllSum=0.0;
		Double listDeptSuoDeiAllSum=0.0;
		Double listDeptJingEAllSum=0.0;

		Double listDeptHeZouFangAllSum=0.0;

		Double listDeptYunYinAllSum=0.0;
		Double listDeptSuoDeiShuiAllSum=0.0;
		Double listDeptYunYinJingLiRunAllSum=0.0;
		Double listDeptWeiShiFuYunYinAllSum=0.0;
		Double listDeptXuNiYunYinAllSum=0.0;

		Double listDeptCostInsideAllSum=0.0;
		Double listDeptSaleManageAllSum=0.0;
		Double listDeptAptitudeAllSum=0.0;
		Double listDeptOtherAllSum=0.0;
		Double listDeptInvestmentAllSum=0.0;
		Double listDeptInstrumentAllSum=0.0;
		Double listSumAllCount=0.0;
		Double listProfitAllCount=0.0;
		Double listTaxAllCount = 0.0;
		Double listNetProfitAllCount=0.0;
		for (String string : listDeptTemp) {
			Double listDeptShouRuBuSum  = 0.0;
			Double listDeptServiceSumCount = 0.0;
			Double listDeptSellSumCount = 0.0;
			Double listDeptHeZouCount = 0.0;
			Double listDeptInsideSumCount = 0.0;
			Double ListDeptInTaxSum = 0.0;
			Double ListDeptTaxSum = 0.0;
			Double ListDeptOutTaxSum = 0.0;
			Double ListDeptWagesSum = 0.0;
			Double ListDeptMaterialSum = 0.0;
			Double ListDeptCaiGouSum = 0.0;
			Double ListDeptHaoCaiSum = 0.0;
			Double listDeptWeiWaiSumCount=0.0;
			Double listDeptExternalSumCount=0.0;
			Double listDeptDaiLiSumCount=0.0;
			Double listDeptZiZhiSumCount=0.0;

			Double listDeptXiaoShouSumCount=0.0;
			Double listDeptBanGongSumCount=0.0;
			Double listDeptBeiYonSumCount=0.0;
			Double listDeptChailvSumCount=0.0;
			Double listDeptFuLiSumCount=0.0;
			Double listDeptGuangGaoSumCount=0.0;
			Double listDeptYeWuSumCount=0.0;
			Double listDeptZuPingSumCount=0.0;
			Double listDeptQiTaFeiYonSumCount=0.0;
			Double listDeptQiTaFuKuanSumCount=0.0;
			Double listDeptQiTaCaiGouSumCount=0.0;

			Double listDeptQiTaZhiChuSumCount=0.0;
			Double listDeptCaiGouYiQiSumCount=0.0;
			Double listDeptGongChenSumCount=0.0;
			Double listDeptYiQiSheBeiSumCount=0.0;
			Double listDeptZhuangXiuFuKuanSumCount=0.0;
			Double listDeptZhiChuFeiYonSumCount=0.0;

			Double listDeptXianJinSumCount=0.0;
			Double listDeptSuoDeiSumCount=0.0;
			Double listDeptJingESumCount=0.0;

			Double listDeptHeZouFangSumCount=0.0;

			Double listDeptYunYinSumCount=0.0;
			Double listDeptSuoDeiShuiSumCount=0.0;
			Double listDeptYunYinJingLiRunSumCount=0.0;
			Double listDeptWeiShiFuYunYinSumCount=0.0;
			Double listDeptXuNiYunYinSumCount=0.0;

			Double listDeptCostInsideSumCount=0.0;
			Double listDeptSaleManageSumCount=0.0;
			Double listDeptAptitudeSumCount=0.0;
			Double listDeptOtherSumCount=0.0;
			Double listDeptInvestmentSumCount=0.0;
			Double listDeptInstrumentSumCount=0.0;
			Double listSumCount=0.0;
			Double listProfitCount=0.0;
			Double listTaxCount = 0.0;
			Double listNetProfitCount=0.0;


			if(listDeptTemp2==null){
				params.put("deptname", deptname);
				map.put("deptname", deptname);
				params.put("deptname1", deptname1);
				map.put("deptname1", deptname1);
				params.put("deptname2", string);
				map.put("deptname2", string);
			}else if(listDeptTemp2.size()==1){
				params.put("deptname", listDeptTemp2.get(0));
				map.put("deptname", listDeptTemp2.get(0));
				params.put("deptname1", deptname1);
				map.put("deptname1", deptname1);
				params.put("deptname2", string);
				map.put("deptname2", string);
			}else if(listDeptTemp2.size()==2){
				params.put("deptname", listDeptTemp2.get(0));
				map.put("deptname", listDeptTemp2.get(0));
				params.put("deptname1", listDeptTemp2.get(1));
				map.put("deptname1", listDeptTemp2.get(1));
				params.put("deptname2", deptname);
				map.put("deptname2", deptname);
				params.put("deptname3", string);
				map.put("deptname3", string);
			}else if(listDeptTemp2.size()==3){
				params.put("deptname", listDeptTemp2.get(0));
				map.put("deptname", listDeptTemp2.get(0));
				params.put("deptname1", listDeptTemp2.get(1));
				map.put("deptname1", listDeptTemp2.get(1));
				params.put("deptname2", listDeptTemp2.get(2));
				map.put("deptname2", listDeptTemp2.get(2));
				params.put("deptname3", listDeptTemp2.get(3));
				map.put("deptname3", listDeptTemp2.get(3));
				params.put("deptname4", string);
				map.put("deptname4", string);
			}

			//服务收入  type=1
			List<Map<String, Object>> tempServiceSum = rdsFinanceConfigNewMapper
					.queryDepServiceSum(params);
			if(tempServiceSum.size()==0)
			{
				listDeptServiceSumCount=0.0;
				listDeptServiceSum.add("<td>0</td>");
			}
			else{
				listDeptServiceSumCount = (Double)(tempServiceSum.get(0)==null?0.0:tempServiceSum.get(0).get("deptServiceSum"));
				listDeptServiceSum.add("<td><a href='#' onClick=alert('暂未开通')>"+decimalFormat.format(listDeptServiceSumCount)+"</a></td>");
			}
			//销售收入 type=2
			List<Map<String, Object>> tempSellSum = rdsFinanceConfigNewMapper
					.queryDepSellSum(params);
			if(tempSellSum.size()==0){
				listDeptSellSumCount=0.0;
				listDeptSellSum.add("<td>0</td>");
			}
			else{
				listDeptSellSumCount = (Double)(tempSellSum.get(0)==null?0.0:tempSellSum.get(0).get("deptSellSum"));
				listDeptSellSum.add("<td>"+decimalFormat.format(listDeptSellSumCount)+"</td>");
			}
			//合作方（实收）收入
			List<Map<String, Object>> tempHeZou = rdsFinanceConfigNewMapper
					.queryDepHeZou(params);
			if(tempHeZou.size()==0){
				listDeptHeZouCount=0.0;
				listDeptHeZou.add("<td>0</td>");
			}
			else{
				listDeptHeZouCount = (Double)(tempHeZou.get(0)==null?0.0:tempHeZou.get(0).get("deptHeZou"));
				listDeptHeZou.add("<td>"+decimalFormat.format(listDeptHeZouCount)+"</td>");
			}
		
			//收入小计（含税）
			ListDeptInTaxSum = listDeptHeZouCount+listDeptSellSumCount+ listDeptServiceSumCount;
			listDeptInTaxSum.add("<td>"+decimalFormat.format(ListDeptInTaxSum)+"</td>");
			//收入（税）
			ListDeptTaxSum =(listDeptServiceSumCount/1.06*0.06)+(listDeptHeZouCount/1.06*0.06)+(listDeptSellSumCount/1.16*0.16) ;
			listDeptTaxSum.add("<td>"+decimalFormat.format(ListDeptTaxSum)+"</td>");

			//收入小计（不含税）
			listDeptShouRuBuSum=ListDeptInTaxSum-ListDeptTaxSum;
			listDeptOutTaxSum.add("<td>"+decimalFormat.format(listDeptShouRuBuSum)+"</td>");

			listDeptNull.add("<td>-</td>");
			listDeptNull1.add("<td>-</td>");
			//人工成本  wags
			List<Map<String, Object>> tempWagesSum = rdsFinanceConfigNewMapper
					.queryDeptWagesSum(map);
			if(tempWagesSum.size()==0){			
				listDeptWagesSum.add("<td>0</td>");
			}
			else{
				ListDeptWagesSum= (Double)(tempWagesSum.get(0)==null?0.0:tempWagesSum.get(0).get("wagesSum"));
				listDeptWagesSum.add("<td>"+decimalFormat.format(ListDeptWagesSum)+"</td>");
			}

			//采购材料付款
			params.put("amoeba_program", "采购材料付款");
			List<Map<String, Object>> tempCaiGouSum = rdsFinanceConfigNewMapper
					.queryDeptMaterialCostSum(params);
			if(tempCaiGouSum.size()==0){			
				listDeptCaiGouSum.add("<td>0</td>");
			}
			else{
				ListDeptCaiGouSum= (Double)(tempCaiGouSum.get(0)==null?0.0:tempCaiGouSum.get(0).get("materialSum"));
				listDeptCaiGouSum.add("<td>"+decimalFormat.format(ListDeptCaiGouSum)+"</td>");
			}

			//耗材
			params.put("amoeba_program", "耗材");
			List<Map<String, Object>> tempHaoCaiSum = rdsFinanceConfigNewMapper
					.queryDeptMaterialCostSum(params);
			if(tempHaoCaiSum.size()==0){			
				listDeptHaoCaiSum.add("<td>0</td>");
			}
			else{
				ListDeptHaoCaiSum= (Double)(tempHaoCaiSum.get(0)==null?0.0:tempHaoCaiSum.get(0).get("materialSum"));
				listDeptHaoCaiSum.add("<td>"+decimalFormat.format(ListDeptHaoCaiSum)+"</td>");
			}

			//材料成本  
			ListDeptMaterialSum=ListDeptCaiGouSum+ListDeptHaoCaiSum;
			listDeptMaterialCostSum.add("<td>"+decimalFormat.format(ListDeptMaterialSum)+"</td>");


			//委外检测成本
			params.put("amoeba_program", "委外检测成本");
			listDeptExternalSumCount = findByNameOne(params, listDeptExternalCostSum, decimalFormat,listDeptExternalSumCount);
			//代理费
			params.put("amoeba_program", "代理费");
			listDeptDaiLiSumCount = findByNameOne(params, listDeptDaiLiCostSum, decimalFormat, listDeptDaiLiSumCount);
			//资质费
			params.put("amoeba_program", "资质费");
			listDeptZiZhiSumCount = findByNameOne(params, listDeptZiZhiCostSum, decimalFormat, listDeptZiZhiSumCount);

			//办公费
			params.put("amoeba_program", "办公费");
			listDeptBanGongSumCount = findByNameOne(params, listDeptBanGongCostSum, decimalFormat,listDeptBanGongSumCount);
			//备用金
			listDeptBeiYonSumCount = findByName(params, listDeptBeiYonCostSum, decimalFormat, listDeptBeiYonSumCount);
			//差旅费
			params.put("amoeba_program", "差旅费");
			listDeptChailvSumCount = findByNameOne(params, listDeptChailvCostSum, decimalFormat,listDeptChailvSumCount);
			//福利费
			params.put("amoeba_program", "福利费");
			listDeptFuLiSumCount = findByNameOne(params, listDeptFuLiCostSum, decimalFormat,listDeptFuLiSumCount);
			//广告宣传
			params.put("amoeba_program", "广告宣传");
			listDeptGuangGaoSumCount = findByNameOne(params, listDeptGuangGaoCostSum, decimalFormat,listDeptGuangGaoSumCount);
			//业务招待费
			params.put("amoeba_program", "业务招待费");
			listDeptYeWuSumCount = findByNameOne(params, listDeptYeWuCostSum, decimalFormat,listDeptYeWuSumCount);
			//租赁费
			params.put("amoeba_program", "租赁费");
			listDeptZuPingSumCount = findByNameOne(params, listDeptZuPingCostSum, decimalFormat,listDeptZuPingSumCount);
			//其他费用
			params.put("amoeba_program", "其他费用");
			listDeptQiTaFeiYonSumCount = findByNameOne(params, listDeptQiTaFeiYonCostSum, decimalFormat,listDeptQiTaFeiYonSumCount);
			//其他付款
			params.put("amoeba_program", "其他付款");
			listDeptQiTaFuKuanSumCount = findByNameOne(params, listDeptQiTaFuKuanCostSum, decimalFormat,listDeptQiTaFuKuanSumCount);
			//其它采购
			params.put("amoeba_program", "其它采购");
			listDeptQiTaCaiGouSumCount = findByNameOne(params, listDeptQiTaCaiGouCostSum, decimalFormat,listDeptQiTaCaiGouSumCount);
			//销售管理费用
			listDeptXiaoShouSumCount=listDeptBanGongSumCount+listDeptBeiYonSumCount
					+listDeptChailvSumCount+listDeptFuLiSumCount+listDeptGuangGaoSumCount+listDeptYeWuSumCount+listDeptZuPingSumCount
					+listDeptQiTaFeiYonSumCount+listDeptQiTaFuKuanSumCount+listDeptQiTaCaiGouSumCount;
			listDeptXiaoShouCostSum.add("<td>"+decimalFormat.format(listDeptXiaoShouSumCount)+"</td>");

			//采购仪器设备付款
			params.put("amoeba_program", "采购仪器设备付款");
			listDeptCaiGouYiQiSumCount = findByNameOne(params, listDeptCaiGouYiQiCostSum, decimalFormat,listDeptCaiGouYiQiSumCount);
			//工程
			params.put("amoeba_program", "工程");
			listDeptGongChenSumCount = findByNameOne(params, listDeptGongChenCostSum, decimalFormat,listDeptGongChenSumCount);
			//仪器设备
			params.put("amoeba_program", "仪器设备");
			listDeptYiQiSheBeiSumCount = findByNameOne(params, listDeptYiQiSheBeiCostSum, decimalFormat,listDeptYiQiSheBeiSumCount);
			//装修付款
			params.put("amoeba_program", "装修付款");
			listDeptZhuangXiuFuKuanSumCount = findByNameOne(params, listDeptZhuangXiuFuKuanCostSum, decimalFormat,listDeptZhuangXiuFuKuanSumCount);
			//对外投资
			map.put("amoeba_program", "对外投资");
			List<Map<String, Object>> tempInvestmentCostSum = rdsFinanceConfigNewMapper
					.queryDeptDepreciationCostSum(map);
			if(tempInvestmentCostSum.size()==0){			
				listDeptInvestmentSum.add("<td>0</td>");
			}
			else{
				listDeptInvestmentSumCount= (Double)(tempInvestmentCostSum.get(0)==null?0.0:tempInvestmentCostSum.get(0).get("depreciationSum"));
				listDeptInvestmentSum.add("<td>"+decimalFormat.format(listDeptInvestmentSumCount)+"</td>");
			}
			//其他支出
			listDeptQiTaZhiChuSumCount= listDeptCaiGouYiQiSumCount+listDeptGongChenSumCount
					+listDeptYiQiSheBeiSumCount+listDeptZhuangXiuFuKuanSumCount;
			listDeptQiTaZhiChuCostSum.add("<td>"+decimalFormat.format(listDeptQiTaZhiChuSumCount)+"</td>");


			//合作方（应收）结算收入
			params.put("amoeba_program", "合作方收入");
			List<Map<String, Object>> tempHeZouFangSum = rdsFinanceConfigNewMapper
					.queryDeptHeZouFangCostSum(params);
			if(tempHeZouFangSum.size()==0){			
				listDeptHeZouFangCostSum.add("<td>0</td>");
			}
			else{
				listDeptHeZouFangSumCount= (Double)(tempHeZouFangSum.get(0)==null?0.0:tempHeZouFangSum.get(0).get("HeZouFangSum"));
				listDeptHeZouFangCostSum.add("<td>"+decimalFormat.format(listDeptHeZouFangSumCount)+"</td>");
			}

			//内部结算收入
			listDeptInsideSum.add("<td>0</td>");
//			List<Map<String, Object>> tempInsideSum = rdsFinanceConfigNewMapper
//					.queryDeptInsideSum(params);
//			if(tempInsideSum.size()==0){
//				listDeptInsideSumCount=0.0;				
//				listDeptInsideSum.add("<td>0</td>");
//			}
//			else{
//				listDeptInsideSumCount = (Double)(tempInsideSum.get(0)==null?0.0:tempInsideSum.get(0).get("deptInsideSum"));
//				listDeptInsideSum.add("<td>"+decimalFormat.format(listDeptInsideSumCount)+"</td>");
//			}
			
			//其他费用（含折旧及摊销）
			map.put("amoeba_program", "其他费用（含折旧及摊销）");
			List<Map<String, Object>> tempOtherCostSum = rdsFinanceConfigNewMapper
					.queryDeptDepreciationCostSum(map);
			if(tempOtherCostSum.size()==0){			
				listDeptOtherSum.add("<td>0</td>");
			}
			else{
				listDeptOtherSumCount= (Double)(tempOtherCostSum.get(0)==null?0.0:tempOtherCostSum.get(0).get("depreciationSum"));
				listDeptOtherSum.add("<td>"+decimalFormat.format(listDeptOtherSumCount)+"</td>");
			}
			//资质费用
			listDeptAptitudeSum.add("<td>0</td>");
//			List<Map<String, Object>> tempDeptAptitudeSum = rdsFinanceConfigNewMapper
//					.queryDeptAptitudeCostSum(params);
//			if(tempDeptAptitudeSum.size()==0){			
//				listDeptAptitudeSum.add("<td>0</td>");
//			}
//			else{
//				listDeptAptitudeSumCount = (Double)(tempDeptAptitudeSum.get(0)==null?0.0:tempDeptAptitudeSum.get(0).get("deptAptitudeCostSum"));
//				listDeptAptitudeSum.add("<td>"+decimalFormat.format(listDeptAptitudeSumCount)+"</td>");
//			}
			//内部结算成本
			List<Map<String, Object>> tempCostInsideSum = rdsFinanceConfigNewMapper
					.queryCostInsideSum(params);
			if(tempCostInsideSum.size()==0){			
				listDeptCostInsideSum.add("<td>0</td>");
			}
			else{
				listDeptCostInsideSumCount = (Double)(tempCostInsideSum.get(0)==null?0.0:tempCostInsideSum.get(0).get("costInsideSum"));
				listDeptCostInsideSum.add("<td>"+decimalFormat.format(listDeptCostInsideSumCount)+"</td>");
			}

			//委外费用 
			listDeptWeiWaiSumCount=listDeptExternalSumCount+listDeptDaiLiSumCount+listDeptZiZhiSumCount;
			listDeptWeiWaiCostSum.add("<td>"+decimalFormat.format(listDeptWeiWaiSumCount)+"</td>");
			
			//销售管理费用
			List<Map<String, Object>> tempSaleManageSum = rdsFinanceConfigNewMapper
					.querySaleManageSum(params);
			if(tempSaleManageSum.size()==0){			
				listDeptSaleManageSum.add("<td>0</td>");
			}
			else{
				listDeptSaleManageSumCount = (Double)(tempSaleManageSum.get(0)==null?0.0:tempSaleManageSum.get(0).get("deptSaleManageSum"));
				listDeptSaleManageSum.add("<td>"+decimalFormat.format(listDeptSaleManageSumCount)+"</td>");
			}

			//仪器设备
			List<Map<String, Object>> tempInstrumentCostSum = rdsFinanceConfigNewMapper
					.queryDeptInstrumentCostSum(params);
			if(tempInstrumentCostSum.size()==0){			
				listDeptInstrumentSum.add("<td>0</td>");
			}
			else{
				listDeptInstrumentSumCount= (Double)(tempInstrumentCostSum.get(0)==null?0.0:tempInstrumentCostSum.get(0).get("instrumentSum"));
				listDeptInstrumentSum.add("<td>"+decimalFormat.format(listDeptInstrumentSumCount)+"</td>");
			}
			
			//支出费用小计
			listDeptZhiChuFeiYonSumCount=ListDeptWagesSum+ListDeptMaterialSum
					+listDeptWeiWaiSumCount+listDeptXiaoShouSumCount+listDeptQiTaZhiChuSumCount;
			listDeptZhiChuFeiYonCostSum.add("<td>"+decimalFormat.format(listDeptZhiChuFeiYonSumCount)+"</td>");
			
			
			//现金流量
			listDeptXianJinSumCount=ListDeptInTaxSum-ListDeptTaxSum - listDeptZhiChuFeiYonSumCount;
			listDeptXianJinCostSum.add("<td>"+decimalFormat.format(listDeptXianJinSumCount)+"</td>");
			//所得税
			listDeptSuoDeiSumCount=listDeptXianJinSumCount * 0.2 ;
			listDeptSuoDeiCostSum.add("<td>"+decimalFormat.format(listDeptSuoDeiSumCount)+"</td>");
			//现金流量净额
			listDeptJingESumCount= listDeptXianJinSumCount - listDeptSuoDeiSumCount;
			listDeptJingECostSum.add("<td>"+decimalFormat.format(listDeptJingESumCount)+"</td>");
			
			//运营利润额
			listDeptYunYinSumCount= listDeptXianJinSumCount+listDeptInsideSumCount-listDeptCostInsideSumCount
					+listDeptInvestmentSumCount+listDeptQiTaZhiChuSumCount-listDeptOtherSumCount;
			listDeptYunYinCostSum.add("<td>"+decimalFormat.format(listDeptYunYinSumCount)+"</td>");
			//所得税
			listDeptSuoDeiShuiSumCount=listDeptYunYinSumCount*0.2;
			listDeptSuoDeiShuiCostSum.add("<td>"+decimalFormat.format(listDeptSuoDeiShuiSumCount)+"</td>");
			//运营净利润额
			listDeptYunYinJingLiRunSumCount=listDeptYunYinSumCount - listDeptSuoDeiShuiSumCount;
			listDeptYunYinJingLiRunCostSum.add("<td>"+decimalFormat.format(listDeptYunYinJingLiRunSumCount)+"</td>");
			//未实付运营费用
			Double a=listDeptAptitudeSumCount == 0.0?0.0:listDeptAptitudeSumCount-listDeptZiZhiSumCount;
			listDeptWeiShiFuYunYinSumCount=a - listDeptHeZouFangSumCount;
			listDeptWeiShiFuYunYinCostSum.add("<td>"+decimalFormat.format(listDeptWeiShiFuYunYinSumCount)+"</td>");
			//虚拟运营净利润合计
			listDeptXuNiYunYinSumCount=listDeptYunYinJingLiRunSumCount - listDeptWeiShiFuYunYinSumCount;
			listDeptXuNiYunYinCostSum.add("<td>"+decimalFormat.format(listDeptXuNiYunYinSumCount)+"</td>");


			listDeptShouRuBuSumAllCount+=listDeptShouRuBuSum;
			listDeptServiceSumAllCount+=listDeptServiceSumCount;
			listDeptSellSumAllCount+=listDeptSellSumCount;
			listDeptHeZouAllCount+=listDeptHeZouCount;
			listDeptInsideSumAllCount+=listDeptInsideSumCount;
			ListDeptInTaxAllSum+=ListDeptInTaxSum;
			ListDeptTaxAllSum+=ListDeptTaxSum;
			ListDeptOutTaxAllSum+=ListDeptOutTaxSum;
			ListDeptWagesAllSum+=ListDeptWagesSum;
			listDeptWeiWaiAllSum+=listDeptWeiWaiSumCount;
			listDeptMaterialCostAllSum+=ListDeptMaterialSum;
			listDeptCaiGouAllSum+=ListDeptCaiGouSum;
			listDeptHaoCaiAllSum+=ListDeptHaoCaiSum;
			listDeptExternalAllSum+=listDeptExternalSumCount;
			listDeptDaiLiAllSum+=listDeptDaiLiSumCount;
			listDeptZiZhiAllSum+=listDeptZiZhiSumCount;

			listDeptXiaoShouAllSum+=listDeptXiaoShouSumCount;
			listDeptBanGongAllSum+=listDeptBanGongSumCount;
			listDeptBeiYonAllSum+=listDeptBeiYonSumCount;
			listDeptChailvAllSum+=listDeptChailvSumCount;
			listDeptFuLiAllSum+=listDeptFuLiSumCount;
			listDeptGuangGaoAllSum+=listDeptGuangGaoSumCount;
			listDeptYeWuAllSum+=listDeptYeWuSumCount;
			listDeptZuPingAllSum+=listDeptZuPingSumCount;
			listDeptQiTaFeiYonAllSum+=listDeptQiTaFeiYonSumCount;
			listDeptQiTaFuKuanAllSum+=listDeptQiTaFuKuanSumCount;
			listDeptQiTaCaiGouAllSum+=listDeptQiTaCaiGouSumCount;

			listDeptXianJinAllSum+=listDeptXianJinSumCount;
			listDeptSuoDeiAllSum+=listDeptSuoDeiSumCount;
			listDeptJingEAllSum+=listDeptJingESumCount;

			listDeptHeZouFangAllSum+=listDeptHeZouFangSumCount;

			listDeptYunYinAllSum+=listDeptYunYinSumCount;
			listDeptSuoDeiShuiAllSum+=listDeptSuoDeiShuiSumCount;
			listDeptYunYinJingLiRunAllSum+=listDeptYunYinJingLiRunSumCount;
			listDeptWeiShiFuYunYinAllSum+=listDeptWeiShiFuYunYinSumCount;
			listDeptXuNiYunYinAllSum+=listDeptXuNiYunYinSumCount;

			listDeptQiTaZhiChuAllSum+=listDeptQiTaZhiChuSumCount;
			listDeptCaiGouYiQiAllSum+=listDeptCaiGouYiQiSumCount;
			listDeptGongChenAllSum+=listDeptGongChenSumCount;
			listDeptYiQiSheBeiAllSum+=listDeptYiQiSheBeiSumCount;
			listDeptZhuangXiuFuKuanAllSum+=listDeptZhuangXiuFuKuanSumCount;
			listDeptZhiChuFeiYonAllSum+=listDeptZhiChuFeiYonSumCount;

			listDeptCostInsideAllSum+=listDeptCostInsideSumCount;
			listDeptSaleManageAllSum+=listDeptSaleManageSumCount;
			listDeptAptitudeAllSum+=listDeptAptitudeSumCount;
			listDeptOtherAllSum+=listDeptOtherSumCount;
			listDeptInvestmentAllSum+=listDeptInvestmentSumCount;
			listDeptInstrumentAllSum+=listDeptInstrumentSumCount;

			//成本小计
			listSumCount=ListDeptWagesSum+ListDeptMaterialSum+listDeptExternalSumCount+listDeptCostInsideSumCount+listDeptSaleManageSumCount+listDeptAptitudeSumCount+listDeptOtherSumCount;
			listSum.add("<td>"+decimalFormat.format(listSumCount)+"</td>");
			listProfitCount = ListDeptOutTaxSum+listDeptInsideSumCount-listSumCount;
			listProfit.add("<td>"+decimalFormat.format(listProfitCount)+"</td>");
			listTaxCount = listProfitCount*0.2;
			listTax.add("<td>"+decimalFormat.format(listTaxCount)+"</td>");
			listNetProfitCount=listProfitCount-listTaxCount;
			listNetProfit.add("<td>"+decimalFormat.format(listNetProfitCount)+"</td>");

			listSumAllCount+=listSumCount;
			listProfitAllCount+=listProfitCount;
			listTaxAllCount+=listTaxCount;
			listNetProfitAllCount+=listNetProfitCount;

		}

		listDept.add("合计");
		list.add(listDept);
		//服务收入
		listDeptServiceSum.add("<td>"+decimalFormat.format(listDeptServiceSumAllCount)+"</td>");
		list.add(listDeptServiceSum);
		//销售收入
		listDeptSellSum.add("<td>"+decimalFormat.format(listDeptSellSumAllCount)+"</td>");
		list.add(listDeptSellSum);
		//合作方（实收）收入
		listDeptHeZou.add("<td>"+decimalFormat.format(listDeptHeZouAllCount)+"</td>");
		list.add(listDeptHeZou);

		//收入小计（含税）
		listDeptInTaxSum.add("<td>"+decimalFormat.format(ListDeptInTaxAllSum)+"</td>");
		list.add(listDeptInTaxSum);
		//收入小计（税）
		listDeptTaxSum.add("<td>"+decimalFormat.format(ListDeptTaxAllSum)+"</td>");
		list.add(listDeptTaxSum);
		//收入小计（不含税）
		listDeptOutTaxSum.add("<td>"+decimalFormat.format(listDeptShouRuBuSumAllCount)+"</td>");
		list.add(listDeptOutTaxSum);
		//空格
		listDeptNull.add("<td>-</td>");
		list.add(listDeptNull);
		//人工成本
		listDeptWagesSum.add("<td>"+decimalFormat.format(ListDeptWagesAllSum)+"</td>");
		list.add(listDeptWagesSum);
		//材料成本
		listDeptMaterialCostSum.add("<td>"+decimalFormat.format(listDeptMaterialCostAllSum)+"</td>");
		list.add(listDeptMaterialCostSum);
		//采购材料付款
		listDeptCaiGouSum.add("<td>"+decimalFormat.format(listDeptCaiGouAllSum)+"</td>");
		list.add(listDeptCaiGouSum);
		//耗材
		listDeptHaoCaiSum.add("<td>"+decimalFormat.format(listDeptHaoCaiAllSum)+"</td>");
		list.add(listDeptHaoCaiSum);
		//委外费用
		listDeptWeiWaiCostSum.add("<td>"+decimalFormat.format(listDeptWeiWaiAllSum)+"</td>");
		list.add(listDeptWeiWaiCostSum);
		//委外检测成本
		listDeptExternalCostSum.add("<td>"+decimalFormat.format(listDeptExternalAllSum)+"</td>");
		list.add(listDeptExternalCostSum);
		//代理费
		listDeptDaiLiCostSum.add("<td>"+decimalFormat.format(listDeptDaiLiAllSum)+"</td>");
		list.add(listDeptDaiLiCostSum);
		//资质费
		listDeptZiZhiCostSum.add("<td>"+decimalFormat.format(listDeptZiZhiAllSum)+"</td>");
		list.add(listDeptZiZhiCostSum);
		//销售管理费用
		listDeptXiaoShouCostSum.add("<td>"+decimalFormat.format(listDeptXiaoShouAllSum)+"</td>");
		list.add(listDeptXiaoShouCostSum);
		//办公费
		listDeptBanGongCostSum.add("<td>"+decimalFormat.format(listDeptBanGongAllSum)+"</td>");
		list.add(listDeptBanGongCostSum);
		//备用金
		listDeptBeiYonCostSum.add("<td>"+decimalFormat.format(listDeptBeiYonAllSum)+"</td>");
		list.add(listDeptBeiYonCostSum);
		//差旅费
		listDeptChailvCostSum.add("<td>"+decimalFormat.format(listDeptChailvAllSum)+"</td>");
		list.add(listDeptChailvCostSum);
		//福利费
		listDeptFuLiCostSum.add("<td>"+decimalFormat.format(listDeptFuLiAllSum)+"</td>");
		list.add(listDeptFuLiCostSum);
		//广告宣传
		listDeptGuangGaoCostSum.add("<td>"+decimalFormat.format(listDeptGuangGaoAllSum)+"</td>");
		list.add(listDeptGuangGaoCostSum);
		//业务招待费
		listDeptYeWuCostSum.add("<td>"+decimalFormat.format(listDeptYeWuAllSum)+"</td>");
		list.add(listDeptYeWuCostSum);
		//租赁费
		listDeptZuPingCostSum.add("<td>"+decimalFormat.format(listDeptZuPingAllSum)+"</td>");
		list.add(listDeptZuPingCostSum);
		//其他费用
		listDeptQiTaFeiYonCostSum.add("<td>"+decimalFormat.format(listDeptQiTaFeiYonAllSum)+"</td>");
		list.add(listDeptQiTaFeiYonCostSum);
		//其他付款
		listDeptQiTaFuKuanCostSum.add("<td>"+decimalFormat.format(listDeptQiTaFuKuanAllSum)+"</td>");
		list.add(listDeptQiTaFuKuanCostSum);
		//其它采购
		listDeptQiTaCaiGouCostSum.add("<td>"+decimalFormat.format(listDeptQiTaCaiGouAllSum)+"</td>");
		list.add(listDeptQiTaCaiGouCostSum);

		//对外投资
		listDeptInvestmentSum.add("<td>"+decimalFormat.format(listDeptInvestmentAllSum)+"</td>");
		list.add(listDeptInvestmentSum);

		//其他支出
		listDeptQiTaZhiChuCostSum.add("<td>"+decimalFormat.format(listDeptQiTaZhiChuAllSum)+"</td>");
		list.add(listDeptQiTaZhiChuCostSum);
		//采购仪器设备付款
		listDeptCaiGouYiQiCostSum.add("<td>"+decimalFormat.format(listDeptCaiGouYiQiAllSum)+"</td>");
		list.add(listDeptCaiGouYiQiCostSum);
		//工程
		listDeptGongChenCostSum.add("<td>"+decimalFormat.format(listDeptGongChenAllSum)+"</td>");
		list.add(listDeptGongChenCostSum);
		//仪器设备
		listDeptYiQiSheBeiCostSum.add("<td>"+decimalFormat.format(listDeptYiQiSheBeiAllSum)+"</td>");
		list.add(listDeptYiQiSheBeiCostSum);
		//装修付款
		listDeptZhuangXiuFuKuanCostSum.add("<td>"+decimalFormat.format(listDeptZhuangXiuFuKuanAllSum)+"</td>");
		list.add(listDeptZhuangXiuFuKuanCostSum);
		//支出费用小计
		listDeptZhiChuFeiYonCostSum.add("<td>"+decimalFormat.format(listDeptZhiChuFeiYonAllSum)+"</td>");
		list.add(listDeptZhiChuFeiYonCostSum);
		//空格
		list.add(listDeptNull);

		//现金流量
		listDeptXianJinCostSum.add("<td>"+decimalFormat.format(listDeptXianJinAllSum)+"</td>");
		list.add(listDeptXianJinCostSum);
		//所得税
		listDeptSuoDeiCostSum.add("<td>"+decimalFormat.format(listDeptSuoDeiAllSum)+"</td>");
		list.add(listDeptSuoDeiCostSum);
		//现金流量净额
		listDeptJingECostSum.add("<td>"+decimalFormat.format(listDeptJingEAllSum)+"</td>");
		list.add(listDeptJingECostSum);
		//空格
		list.add(listDeptNull);

		//合作方（应收）结算收入
		listDeptHeZouFangCostSum.add("<td>"+decimalFormat.format(listDeptHeZouFangAllSum)+"</td>");
		list.add(listDeptHeZouFangCostSum);
		//内部结算收入
		listDeptInsideSum.add("<td>"+decimalFormat.format(listDeptInsideSumAllCount)+"</td>");
		list.add(listDeptInsideSum);
		//其他费用（含折旧及摊销）
		listDeptOtherSum.add("<td>"+decimalFormat.format(listDeptOtherAllSum)+"</td>");
		list.add(listDeptOtherSum);
		//合作方案例资质费用
		listDeptAptitudeSum.add("<td>"+decimalFormat.format(listDeptAptitudeAllSum)+"</td>");
		list.add(listDeptAptitudeSum);
		//内部结算成本
		listDeptCostInsideSum.add("<td>"+decimalFormat.format(listDeptCostInsideAllSum)+"</td>");
		list.add(listDeptCostInsideSum);
		//空格
		list.add(listDeptNull);

		//运营利润额
		listDeptYunYinCostSum.add("<td>"+decimalFormat.format(listDeptYunYinAllSum)+"</td>");
		list.add(listDeptYunYinCostSum);
		//所得税
		listDeptSuoDeiShuiCostSum.add("<td>"+decimalFormat.format(listDeptSuoDeiShuiAllSum)+"</td>");
		list.add(listDeptSuoDeiShuiCostSum);
		//运营净利润额
		listDeptYunYinJingLiRunCostSum.add("<td>"+decimalFormat.format(listDeptYunYinJingLiRunAllSum)+"</td>");
		list.add(listDeptYunYinJingLiRunCostSum);
		//未实付运营费用
		listDeptWeiShiFuYunYinCostSum.add("<td>"+decimalFormat.format(listDeptWeiShiFuYunYinAllSum)+"</td>");
		list.add(listDeptWeiShiFuYunYinCostSum);
		//虚拟运营净利润合计
		listDeptXuNiYunYinCostSum.add("<td>"+decimalFormat.format(listDeptXuNiYunYinAllSum)+"</td>");
		list.add(listDeptXuNiYunYinCostSum);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<List<String>> queryAmoebaSecond(Map<String, Object> params) {
		List<List<String>> list = new ArrayList<>();
		List<String> listDept = new ArrayList<>();
		List<String> listDeptTemp = new ArrayList<String>();
		List<String> listDeptTemp2 = (List<String>) params.get("listDeptTemp");
		String listDeptTempId = (String) params.get("deptnameId");
		String deptname=null;

		List<RdsStatisticsDepartmentModel> listBuMen=rdsFinanceConfigNewMapper.queryXiaJiBuMen(listDeptTempId);
		for (RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel : listBuMen) {
			if(!rdsStatisticsDepartmentModel.isLeaf()){
				listDept.add("<a href='#' onClick=jumpToNext('" + rdsStatisticsDepartmentModel.getDeptid() + "')>" + 
						rdsStatisticsDepartmentModel.getDeptname() + "</a>");
			}else{
				listDept.add(rdsStatisticsDepartmentModel.getDeptname());
			}
			listDeptTemp.add(rdsStatisticsDepartmentModel.getDeptname());
		}
		RdsStatisticsDepartmentModel buMen=null;
		if(params.get("listDeptTemp")!=null){

		}else{
			 buMen = rdsFinanceConfigNewMapper.queryBuMen(listDeptTempId);
			deptname = buMen.getDeptname();
		}




		//部门服务收入
		List<String> listDeptServiceSum = new ArrayList<>();
		listDeptServiceSum.add("<th>服务收入</th>");
		//部门销售收入
		List<String> listDeptSellSum = new ArrayList<>();
		listDeptSellSum.add("<th>销售收入</th>");
		//合作方（实收）收入
		List<String> listDeptHeZou = new ArrayList<>();
		listDeptHeZou.add("<th>合作方（实收）收入</th>");	 
		//部门内部结算收入
		List<String> listDeptInsideSum = new ArrayList<>();
		listDeptInsideSum.add("<th>内部结算收入</th>");
		//部门收入小计（含税）
		List<String> listDeptInTaxSum = new ArrayList<>();
		listDeptInTaxSum.add("<th>收入小计(含税)</th>");
		//部门收入小计（税）
		List<String> listDeptTaxSum = new ArrayList<>();
		listDeptTaxSum.add("<th>税</th>");
		//部门收入小计（不含税）
		List<String> listDeptOutTaxSum = new ArrayList<>();
		listDeptOutTaxSum.add("<th><h2>收入小计（不含税）</h2></th>");
		//空格
		List<String> listDeptNull = new ArrayList<>();
		listDeptNull.add("<th></th>");
		//人工成本
		List<String> listDeptWagesSum = new ArrayList<>();
		listDeptWagesSum.add("<th><h3>人工成本</h3></th>");
		//材料成本
		List<String> listDeptMaterialCostSum = new ArrayList<>();
		listDeptMaterialCostSum.add("<th><h3>材料成本</h3></th>");
		//采购材料付款
		List<String> listDeptCaiGouSum = new ArrayList<>();
		listDeptCaiGouSum.add("<th>采购材料付款</th>");
		//耗材
		List<String> listDeptHaoCaiSum = new ArrayList<>();
		listDeptHaoCaiSum.add("<th>耗材</th>");
		//委外费用
		List<String> listDeptWeiWaiCostSum = new ArrayList<>();
		listDeptWeiWaiCostSum.add("<th><h3>委外费用</h3></th>");
		//委外检测成本
		List<String> listDeptExternalCostSum = new ArrayList<>();
		listDeptExternalCostSum.add("<th>委外检测成本</th>");
		//代理费
		List<String> listDeptDaiLiCostSum = new ArrayList<>();
		listDeptDaiLiCostSum.add("<th>代理费</th>");
		//资质费
		List<String> listDeptZiZhiCostSum = new ArrayList<>();
		listDeptZiZhiCostSum.add("<th>资质费</th>");

		//销售管理费用
		List<String> listDeptXiaoShouCostSum = new ArrayList<>();
		listDeptXiaoShouCostSum.add("<th><h3>销售管理费用</h3></th>");
		//办公费
		List<String> listDeptBanGongCostSum = new ArrayList<>();
		listDeptBanGongCostSum.add("<th>办公费</th>");
		//备用金
		List<String> listDeptBeiYonCostSum = new ArrayList<>();
		listDeptBeiYonCostSum.add("<th>备用金</th>");
		//差旅费
		List<String> listDeptChailvCostSum = new ArrayList<>();
		listDeptChailvCostSum.add("<th>差旅费</th>");
		//福利费
		List<String> listDeptFuLiCostSum = new ArrayList<>();
		listDeptFuLiCostSum.add("<th>福利费</th>");
		//广告宣传
		List<String> listDeptGuangGaoCostSum = new ArrayList<>();
		listDeptGuangGaoCostSum.add("<th>广告宣传</th>");
		//业务招待费
		List<String> listDeptYeWuCostSum = new ArrayList<>();
		listDeptYeWuCostSum.add("<th>业务招待费</th>");
		//租赁费
		List<String> listDeptZuPingCostSum = new ArrayList<>();
		listDeptZuPingCostSum.add("<th>租赁费</th>");
		//其他费用
		List<String> listDeptQiTaFeiYonCostSum = new ArrayList<>();
		listDeptQiTaFeiYonCostSum.add("<th>其他费用</th>");
		//其他付款
		List<String> listDeptQiTaFuKuanCostSum = new ArrayList<>();
		listDeptQiTaFuKuanCostSum.add("<th>其他付款</th>");
		//其它采购
		List<String> listDeptQiTaCaiGouCostSum = new ArrayList<>();
		listDeptQiTaCaiGouCostSum.add("<th>其它采购</th>");

		//对外投资
		List<String> listDeptInvestmentSum = new ArrayList<>();
		listDeptInvestmentSum.add("<th><h3>对外投资</h3></th>");

		//其他支出
		List<String> listDeptQiTaZhiChuCostSum = new ArrayList<>();
		listDeptQiTaZhiChuCostSum.add("<th><h3>其他支出</h3></th>");
		//采购仪器设备付款
		List<String> listDeptCaiGouYiQiCostSum = new ArrayList<>();
		listDeptCaiGouYiQiCostSum.add("<th>采购仪器设备付款</th>");
		//工程
		List<String> listDeptGongChenCostSum = new ArrayList<>();
		listDeptGongChenCostSum.add("<th>工程</th>");
		//仪器设备
		List<String> listDeptYiQiSheBeiCostSum = new ArrayList<>();
		listDeptYiQiSheBeiCostSum.add("<th>仪器设备</th>");
		//装修付款
		List<String> listDeptZhuangXiuFuKuanCostSum = new ArrayList<>();
		listDeptZhuangXiuFuKuanCostSum.add("<th>装修付款</th>");
		//支出费用小计
		List<String> listDeptZhiChuFeiYonCostSum = new ArrayList<>();
		listDeptZhiChuFeiYonCostSum.add("<th><h2>支出费用小计</h2></th>");

		//现金流量
		List<String> listDeptXianJinCostSum = new ArrayList<>();
		listDeptXianJinCostSum.add("<th>现金流量</th>");
		//所得税
		List<String> listDeptSuoDeiCostSum = new ArrayList<>();
		listDeptSuoDeiCostSum.add("<th>所得税</th>");
		//现金流量净额
		List<String> listDeptJingECostSum = new ArrayList<>();
		listDeptJingECostSum.add("<th>现金流量净额</th>");

		//合作方（应收）结算收入
		List<String> listDeptHeZouFangCostSum = new ArrayList<>();
		listDeptHeZouFangCostSum.add("<th>合作方（应收）结算收入</th>");

		//运营利润额
		List<String> listDeptYunYinCostSum = new ArrayList<>();
		listDeptYunYinCostSum.add("<th>运营利润额</th>");
		//所得税
		List<String> listDeptSuoDeiShuiCostSum = new ArrayList<>();
		listDeptSuoDeiShuiCostSum.add("<th>所得税</th>");
		//运营净利润额
		List<String> listDeptYunYinJingLiRunCostSum = new ArrayList<>();
		listDeptYunYinJingLiRunCostSum.add("<th>运营净利润额</th>");
		//未实付运营费用
		List<String> listDeptWeiShiFuYunYinCostSum = new ArrayList<>();
		listDeptWeiShiFuYunYinCostSum.add("<th>未实付运营费用</th>");
		//虚拟运营净利润合计
		List<String> listDeptXuNiYunYinCostSum = new ArrayList<>();
		listDeptXuNiYunYinCostSum.add("<th><h2>虚拟运营净利润合计</h2></th>");

		//内部结算成本
		List<String> listDeptCostInsideSum = new ArrayList<>();
		listDeptCostInsideSum.add("<th>内部结算成本</th>");
		//销售管理结算成本
		List<String> listDeptSaleManageSum = new ArrayList<>();
		listDeptSaleManageSum.add("<th>销售管理费用</th>");
		//资质费用
		List<String> listDeptAptitudeSum = new ArrayList<>();
		listDeptAptitudeSum.add("<th>资质费用</th>");
		//其他费用（含折旧及摊销）
		List<String> listDeptOtherSum = new ArrayList<>();
		listDeptOtherSum.add("<th>其他费用（含折旧及摊销）</th>");
		//仪器设备
		List<String> listDeptInstrumentSum = new ArrayList<>();
		listDeptInstrumentSum.add("<th>房屋、装修、仪器及设备采购成本</th>");
		//成本小计
		List<String> listSum = new ArrayList<>();
		listSum.add("<th>成本小计</th>");
		//空格
		List<String> listDeptNull1 = new ArrayList<>();
		listDeptNull1.add("<th></th>");
		//利润小计
		List<String> listProfit = new ArrayList<>();
		listProfit.add("<th>利润小计</th>");
		//税
		List<String> listTax = new ArrayList<>();
		listTax.add("<th>税</th>");
		//净利润小计
		List<String> listNetProfit = new ArrayList<>();
		listNetProfit.add("<th>净利润</th>");
		// 内部结算按照月份计算
		Map<String, Object> map = new HashMap<>();
		map.put("confirm_date_start", params.get("confirm_date_start").toString()
				.substring(0, 7));
		map.put("confirm_date_end", params.get("confirm_date_end").toString()
				.substring(0, 7));
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置

		Double listDeptShouRuBuSumAllCount = 0.0;
		Double listDeptServiceSumAllCount = 0.0;
		Double listDeptSellSumAllCount = 0.0;
		Double listDeptHeZouAllCount = 0.0;
		Double listDeptInsideSumAllCount = 0.0;
		Double ListDeptInTaxAllSum = 0.0;
		Double ListDeptTaxAllSum = 0.0;
		Double ListDeptOutTaxAllSum = 0.0;
		Double ListDeptWagesAllSum = 0.0;
		Double listDeptMaterialCostAllSum=0.0;
		Double listDeptCaiGouAllSum=0.0;
		Double listDeptHaoCaiAllSum=0.0;
		Double listDeptWeiWaiAllSum =0.0;
		Double listDeptExternalAllSum =0.0;
		Double listDeptDaiLiAllSum =0.0;
		Double listDeptZiZhiAllSum =0.0;

		Double listDeptXiaoShouAllSum =0.0;
		Double listDeptBanGongAllSum =0.0;
		Double listDeptBeiYonAllSum =0.0;
		Double listDeptChailvAllSum =0.0;
		Double listDeptFuLiAllSum =0.0;
		Double listDeptGuangGaoAllSum =0.0;
		Double listDeptYeWuAllSum =0.0;
		Double listDeptZuPingAllSum =0.0;
		Double listDeptQiTaFeiYonAllSum =0.0;
		Double listDeptQiTaFuKuanAllSum =0.0;
		Double listDeptQiTaCaiGouAllSum =0.0;

		Double listDeptQiTaZhiChuAllSum =0.0;
		Double listDeptCaiGouYiQiAllSum =0.0;
		Double listDeptGongChenAllSum =0.0;
		Double listDeptYiQiSheBeiAllSum =0.0;
		Double listDeptZhuangXiuFuKuanAllSum =0.0;
		Double listDeptZhiChuFeiYonAllSum =0.0;

		Double listDeptXianJinAllSum=0.0;
		Double listDeptSuoDeiAllSum=0.0;
		Double listDeptJingEAllSum=0.0;

		Double listDeptHeZouFangAllSum=0.0;

		Double listDeptYunYinAllSum=0.0;
		Double listDeptSuoDeiShuiAllSum=0.0;
		Double listDeptYunYinJingLiRunAllSum=0.0;
		Double listDeptWeiShiFuYunYinAllSum=0.0;
		Double listDeptXuNiYunYinAllSum=0.0;

		Double listDeptCostInsideAllSum=0.0;
		Double listDeptSaleManageAllSum=0.0;
		Double listDeptAptitudeAllSum=0.0;
		Double listDeptOtherAllSum=0.0;
		Double listDeptInvestmentAllSum=0.0;
		Double listDeptInstrumentAllSum=0.0;
		Double listSumAllCount=0.0;
		Double listProfitAllCount=0.0;
		Double listTaxAllCount = 0.0;
		Double listNetProfitAllCount=0.0;
		for (String string : listDeptTemp) {
			Double listDeptShouRuBuSum = 0.0;
			Double listDeptServiceSumCount = 0.0;
			Double listDeptSellSumCount = 0.0;
			Double listDeptHeZouCount = 0.0;
			Double listDeptInsideSumCount = 0.0;
			Double ListDeptInTaxSum = 0.0;
			Double ListDeptTaxSum = 0.0;
			Double ListDeptOutTaxSum = 0.0;
			Double ListDeptWagesSum = 0.0;
			Double ListDeptMaterialSum = 0.0;
			Double ListDeptCaiGouSum = 0.0;
			Double ListDeptHaoCaiSum = 0.0;
			Double listDeptWeiWaiSumCount=0.0;
			Double listDeptExternalSumCount=0.0;
			Double listDeptDaiLiSumCount=0.0;
			Double listDeptZiZhiSumCount=0.0;

			Double listDeptXiaoShouSumCount=0.0;
			Double listDeptBanGongSumCount=0.0;
			Double listDeptBeiYonSumCount=0.0;
			Double listDeptChailvSumCount=0.0;
			Double listDeptFuLiSumCount=0.0;
			Double listDeptGuangGaoSumCount=0.0;
			Double listDeptYeWuSumCount=0.0;
			Double listDeptZuPingSumCount=0.0;
			Double listDeptQiTaFeiYonSumCount=0.0;
			Double listDeptQiTaFuKuanSumCount=0.0;
			Double listDeptQiTaCaiGouSumCount=0.0;

			Double listDeptQiTaZhiChuSumCount=0.0;
			Double listDeptCaiGouYiQiSumCount=0.0;
			Double listDeptGongChenSumCount=0.0;
			Double listDeptYiQiSheBeiSumCount=0.0;
			Double listDeptZhuangXiuFuKuanSumCount=0.0;
			Double listDeptZhiChuFeiYonSumCount=0.0;

			Double listDeptXianJinSumCount=0.0;
			Double listDeptSuoDeiSumCount=0.0;
			Double listDeptJingESumCount=0.0;

			Double listDeptHeZouFangSumCount=0.0;

			Double listDeptYunYinSumCount=0.0;
			Double listDeptSuoDeiShuiSumCount=0.0;
			Double listDeptYunYinJingLiRunSumCount=0.0;
			Double listDeptWeiShiFuYunYinSumCount=0.0;
			Double listDeptXuNiYunYinSumCount=0.0;

			Double listDeptCostInsideSumCount=0.0;
			Double listDeptSaleManageSumCount=0.0;
			Double listDeptAptitudeSumCount=0.0;
			Double listDeptOtherSumCount=0.0;
			Double listDeptInvestmentSumCount=0.0;
			Double listDeptInstrumentSumCount=0.0;
			Double listSumCount=0.0;
			Double listProfitCount=0.0;
			Double listTaxCount = 0.0;
			Double listNetProfitCount=0.0;

			if(listDeptTemp2==null){
				params.put("deptname", deptname);
				map.put("deptname", deptname);
				params.put("deptname1", string);
				map.put("deptname1", string);
			}else if(listDeptTemp2.size()==1){
				params.put("deptname", listDeptTemp2.get(0));
				map.put("deptname", listDeptTemp2.get(0));
				params.put("deptname1", string);
				map.put("deptname1", string);
			}else if(listDeptTemp2.size()==2){
				params.put("deptname", listDeptTemp2.get(0));
				map.put("deptname", listDeptTemp2.get(0));
				params.put("deptname1", listDeptTemp2.get(1));
				map.put("deptname1", listDeptTemp2.get(1));
				params.put("deptname2", string);
				map.put("deptname2", string);
			}else if(listDeptTemp2.size()==3){
				params.put("deptname", listDeptTemp2.get(0));
				map.put("deptname", listDeptTemp2.get(0));
				params.put("deptname1", listDeptTemp2.get(1));
				map.put("deptname1", listDeptTemp2.get(1));
				params.put("deptname2", listDeptTemp2.get(2));
				map.put("deptname2", listDeptTemp2.get(2));
				params.put("deptname3", string);
				map.put("deptname3", string);
			}

			//服务收入  type=1
			List<Map<String, Object>> tempServiceSum=null;
			if((listDeptTemp2==null || listDeptTemp2.size()==1) && "精准医学事业部".equals(string)){
				tempServiceSum = rdsFinanceConfigNewMapper
						.queryDepServiceSum2(params);
			}else{
				tempServiceSum = rdsFinanceConfigNewMapper
						.queryDepServiceSum(params);
			}
			if(tempServiceSum.size()==0)
			{
				listDeptServiceSumCount=0.0;
				listDeptServiceSum.add("<td>0</td>");
			}
			else{
				listDeptServiceSumCount = (Double)(tempServiceSum.get(0)==null?0.0:tempServiceSum.get(0).get("deptServiceSum"));
				listDeptServiceSum.add("<td><a href='#' onClick=alert('暂未开通')>"+decimalFormat.format(listDeptServiceSumCount)+"</a></td>");
			}
			//销售收入 type=2
			List<Map<String, Object>> tempSellSum = rdsFinanceConfigNewMapper
					.queryDepSellSum(params);
			if(tempSellSum.size()==0){
				listDeptSellSumCount=0.0;
				listDeptSellSum.add("<td>0</td>");
			}
			else{
				listDeptSellSumCount = (Double)(tempSellSum.get(0)==null?0.0:tempSellSum.get(0).get("deptSellSum"));
				listDeptSellSum.add("<td>"+decimalFormat.format(listDeptSellSumCount)+"</td>");
			}
			//合作方（实收）收入
			List<Map<String, Object>> tempHeZou = rdsFinanceConfigNewMapper
					.queryDepHeZou(params);
			if(tempHeZou.size()==0){
				listDeptHeZouCount=0.0;
				listDeptHeZou.add("<td>0</td>");
			}
			else{
				listDeptHeZouCount = (Double)(tempHeZou.get(0)==null?0.0:tempHeZou.get(0).get("deptHeZou"));
				listDeptHeZou.add("<td>"+decimalFormat.format(listDeptHeZouCount)+"</td>");
			}
		
			//收入小计（含税）
			ListDeptInTaxSum = listDeptHeZouCount+listDeptSellSumCount+ listDeptServiceSumCount;
			listDeptInTaxSum.add("<td>"+decimalFormat.format(ListDeptInTaxSum)+"</td>");
			//收入（税）
			ListDeptTaxSum =(listDeptServiceSumCount/1.06*0.06)+(listDeptHeZouCount/1.06*0.06)+(listDeptSellSumCount/1.16*0.16) ;
			listDeptTaxSum.add("<td>"+decimalFormat.format(ListDeptTaxSum)+"</td>");

			//收入小计（不含税）
			listDeptShouRuBuSum=ListDeptInTaxSum-ListDeptTaxSum;
			listDeptOutTaxSum.add("<td>"+decimalFormat.format(listDeptShouRuBuSum)+"</td>");

			listDeptNull.add("<td>-</td>");
			listDeptNull1.add("<td>-</td>");
			
			//人工成本  wags
			List<Map<String, Object>> tempWagesSum=null;
			if((listDeptTemp2==null || listDeptTemp2.size()==1) && "精准医学事业部".equals(string)){
				 tempWagesSum = rdsFinanceConfigNewMapper
						.queryDeptWagesSum2(map);
			}else{
				 tempWagesSum = rdsFinanceConfigNewMapper
						.queryDeptWagesSum(map);
			}
			if(tempWagesSum.size()==0){			
				listDeptWagesSum.add("<td>0</td>");
			}
			else{
				ListDeptWagesSum= (Double)(tempWagesSum.get(0)==null?0.0:tempWagesSum.get(0).get("wagesSum"));
				listDeptWagesSum.add("<td>"+decimalFormat.format(ListDeptWagesSum)+"</td>");
			}

			//采购材料付款
			params.put("amoeba_program", "采购材料付款");
			List<Map<String, Object>> tempCaiGouSum = rdsFinanceConfigNewMapper
					.queryDeptMaterialCostSum(params);
			if(tempCaiGouSum.size()==0){			
				listDeptCaiGouSum.add("<td>0</td>");
			}
			else{
				ListDeptCaiGouSum= (Double)(tempCaiGouSum.get(0)==null?0.0:tempCaiGouSum.get(0).get("materialSum"));
				listDeptCaiGouSum.add("<td>"+decimalFormat.format(ListDeptCaiGouSum)+"</td>");
			}

			//耗材
			params.put("amoeba_program", "耗材");
			List<Map<String, Object>> tempHaoCaiSum = rdsFinanceConfigNewMapper
					.queryDeptMaterialCostSum(params);
			if(tempHaoCaiSum.size()==0){			
				listDeptHaoCaiSum.add("<td>0</td>");
			}
			else{
				ListDeptHaoCaiSum= (Double)(tempHaoCaiSum.get(0)==null?0.0:tempHaoCaiSum.get(0).get("materialSum"));
				listDeptHaoCaiSum.add("<td>"+decimalFormat.format(ListDeptHaoCaiSum)+"</td>");
			}

			//材料成本  
			ListDeptMaterialSum=ListDeptCaiGouSum+ListDeptHaoCaiSum;
			listDeptMaterialCostSum.add("<td>"+decimalFormat.format(ListDeptMaterialSum)+"</td>");


			//委外检测成本
			params.put("amoeba_program", "委外检测成本");
			listDeptExternalSumCount = findByNameOne(params, listDeptExternalCostSum, decimalFormat,listDeptExternalSumCount);
			//代理费
			params.put("amoeba_program", "代理费");
			listDeptDaiLiSumCount = findByNameOne(params, listDeptDaiLiCostSum, decimalFormat, listDeptDaiLiSumCount);
			//资质费
			params.put("amoeba_program", "资质费");
			listDeptZiZhiSumCount = findByNameOne(params, listDeptZiZhiCostSum, decimalFormat, listDeptZiZhiSumCount);

			//办公费
			params.put("amoeba_program", "办公费");
			listDeptBanGongSumCount = findByNameOne(params, listDeptBanGongCostSum, decimalFormat,listDeptBanGongSumCount);
			//备用金
			listDeptBeiYonSumCount = findByName(params, listDeptBeiYonCostSum, decimalFormat, listDeptBeiYonSumCount);
			//差旅费
			params.put("amoeba_program", "差旅费");
			listDeptChailvSumCount = findByNameOne(params, listDeptChailvCostSum, decimalFormat,listDeptChailvSumCount);
			//福利费
			params.put("amoeba_program", "福利费");
			listDeptFuLiSumCount = findByNameOne(params, listDeptFuLiCostSum, decimalFormat,listDeptFuLiSumCount);
			//广告宣传
			params.put("amoeba_program", "广告宣传");
			listDeptGuangGaoSumCount = findByNameOne(params, listDeptGuangGaoCostSum, decimalFormat,listDeptGuangGaoSumCount);
			//业务招待费
			params.put("amoeba_program", "业务招待费");
			listDeptYeWuSumCount = findByNameOne(params, listDeptYeWuCostSum, decimalFormat,listDeptYeWuSumCount);
			//租赁费
			params.put("amoeba_program", "租赁费");
			listDeptZuPingSumCount = findByNameOne(params, listDeptZuPingCostSum, decimalFormat,listDeptZuPingSumCount);
			//其他费用
			params.put("amoeba_program", "其他费用");
			listDeptQiTaFeiYonSumCount = findByNameOne(params, listDeptQiTaFeiYonCostSum, decimalFormat,listDeptQiTaFeiYonSumCount);
			//其他付款
			params.put("amoeba_program", "其他付款");
			listDeptQiTaFuKuanSumCount = findByNameOne(params, listDeptQiTaFuKuanCostSum, decimalFormat,listDeptQiTaFuKuanSumCount);
			//其它采购
			params.put("amoeba_program", "其它采购");
			listDeptQiTaCaiGouSumCount = findByNameOne(params, listDeptQiTaCaiGouCostSum, decimalFormat,listDeptQiTaCaiGouSumCount);
			//销售管理费用
			listDeptXiaoShouSumCount=listDeptBanGongSumCount+listDeptBeiYonSumCount
					+listDeptChailvSumCount+listDeptFuLiSumCount+listDeptGuangGaoSumCount+listDeptYeWuSumCount+listDeptZuPingSumCount
					+listDeptQiTaFeiYonSumCount+listDeptQiTaFuKuanSumCount+listDeptQiTaCaiGouSumCount;
			listDeptXiaoShouCostSum.add("<td>"+decimalFormat.format(listDeptXiaoShouSumCount)+"</td>");

			//采购仪器设备付款
			params.put("amoeba_program", "采购仪器设备付款");
			listDeptCaiGouYiQiSumCount = findByNameOne(params, listDeptCaiGouYiQiCostSum, decimalFormat,listDeptCaiGouYiQiSumCount);
			//工程
			params.put("amoeba_program", "工程");
			listDeptGongChenSumCount = findByNameOne(params, listDeptGongChenCostSum, decimalFormat,listDeptGongChenSumCount);
			//仪器设备
			params.put("amoeba_program", "仪器设备");
			listDeptYiQiSheBeiSumCount = findByNameOne(params, listDeptYiQiSheBeiCostSum, decimalFormat,listDeptYiQiSheBeiSumCount);
			//装修付款
			params.put("amoeba_program", "装修付款");
			listDeptZhuangXiuFuKuanSumCount = findByNameOne(params, listDeptZhuangXiuFuKuanCostSum, decimalFormat,listDeptZhuangXiuFuKuanSumCount);
			//对外投资
			map.put("amoeba_program", "对外投资");
			List<Map<String, Object>> tempInvestmentCostSum = rdsFinanceConfigNewMapper
					.queryDeptDepreciationCostSum(map);
			if(tempInvestmentCostSum.size()==0){			
				listDeptInvestmentSum.add("<td>0</td>");
			}
			else{
				listDeptInvestmentSumCount= (Double)(tempInvestmentCostSum.get(0)==null?0.0:tempInvestmentCostSum.get(0).get("depreciationSum"));
				listDeptInvestmentSum.add("<td>"+decimalFormat.format(listDeptInvestmentSumCount)+"</td>");
			}
			//其他支出
			listDeptQiTaZhiChuSumCount= listDeptCaiGouYiQiSumCount+listDeptGongChenSumCount
					+listDeptYiQiSheBeiSumCount+listDeptZhuangXiuFuKuanSumCount;
			listDeptQiTaZhiChuCostSum.add("<td>"+decimalFormat.format(listDeptQiTaZhiChuSumCount)+"</td>");


			//合作方（应收）结算收入
			params.put("amoeba_program", "合作方收入");
			List<Map<String, Object>> tempHeZouFangSum = rdsFinanceConfigNewMapper
					.queryDeptHeZouFangCostSum(params);
			if(tempHeZouFangSum.size()==0){			
				listDeptHeZouFangCostSum.add("<td>0</td>");
			}
			else{
				listDeptHeZouFangSumCount= (Double)(tempHeZouFangSum.get(0)==null?0.0:tempHeZouFangSum.get(0).get("HeZouFangSum"));
				listDeptHeZouFangCostSum.add("<td>"+decimalFormat.format(listDeptHeZouFangSumCount)+"</td>");
			}

			//内部结算收入
//			TODO
			List<Map<String, Object>> tempInsideSum =null;
			if("技术与服务管理部".equals(string)){
				tempInsideSum = rdsFinanceConfigNewMapper
						.queryDeptInsideSum1(params);
			}else if("精准医学事业部".equals(string)){
				 tempInsideSum = rdsFinanceConfigNewMapper
						.queryDeptInsideSum2(params);
			} 
			if(tempInsideSum==null){
				listDeptInsideSumCount=0.0;				
				listDeptInsideSum.add("<td>0</td>");
			}
			else{
				listDeptInsideSumCount = (Double)(tempInsideSum.get(0)==null?0.0:tempInsideSum.get(0).get("deptInsideSum"));
				listDeptInsideSum.add("<td>"+decimalFormat.format(listDeptInsideSumCount)+"</td>");
			}
			
			//其他费用（含折旧及摊销）
			map.put("amoeba_program", "其他费用（含折旧及摊销）");
			List<Map<String, Object>> tempOtherCostSum = rdsFinanceConfigNewMapper
					.queryDeptDepreciationCostSum(map);
			if(tempOtherCostSum.size()==0){			
				listDeptOtherSum.add("<td>0</td>");
			}
			else{
				listDeptOtherSumCount= (Double)(tempOtherCostSum.get(0)==null?0.0:tempOtherCostSum.get(0).get("depreciationSum"));
				listDeptOtherSum.add("<td>"+decimalFormat.format(listDeptOtherSumCount)+"</td>");
			}
			//资质费用
			List<Map<String, Object>> tempDeptAptitudeSum=null;
			if("技术与服务管理部".equals(string)){
				tempDeptAptitudeSum = rdsFinanceConfigNewMapper
						.queryDeptAptitudeCostSum(params);
			} 
			if(tempDeptAptitudeSum==null){			
				listDeptAptitudeSum.add("<td>0</td>");
			}
			else{
				listDeptAptitudeSumCount = (Double)(tempDeptAptitudeSum.get(0)==null?0.0:tempDeptAptitudeSum.get(0).get("deptAptitudeCostSum"));
				listDeptAptitudeSum.add("<td>"+decimalFormat.format(listDeptAptitudeSumCount)+"</td>");
			}
			//内部结算成本
			List<Map<String, Object>> tempCostInsideSum=null;
			if((listDeptTemp2==null || listDeptTemp2.size()==1) && "精准医学事业部".equals(string)){
				tempCostInsideSum = rdsFinanceConfigNewMapper
						.queryCostInsideSum2(params);
			}else{
				tempCostInsideSum = rdsFinanceConfigNewMapper
						.queryCostInsideSum(params);
			}
			if(tempCostInsideSum.size()==0){			
				listDeptCostInsideSum.add("<td>0</td>");
			}
			else{
				listDeptCostInsideSumCount = (Double)(tempCostInsideSum.get(0)==null?0.0:tempCostInsideSum.get(0).get("costInsideSum"));
				listDeptCostInsideSum.add("<td>"+decimalFormat.format(listDeptCostInsideSumCount)+"</td>");
			}

			//委外费用 
			listDeptWeiWaiSumCount=listDeptExternalSumCount+listDeptDaiLiSumCount+listDeptZiZhiSumCount;
			listDeptWeiWaiCostSum.add("<td>"+decimalFormat.format(listDeptWeiWaiSumCount)+"</td>");
			
			//销售管理费用
			List<Map<String, Object>> tempSaleManageSum = rdsFinanceConfigNewMapper
					.querySaleManageSum(params);
			if(tempSaleManageSum.size()==0){			
				listDeptSaleManageSum.add("<td>0</td>");
			}
			else{
				listDeptSaleManageSumCount = (Double)(tempSaleManageSum.get(0)==null?0.0:tempSaleManageSum.get(0).get("deptSaleManageSum"));
				listDeptSaleManageSum.add("<td>"+decimalFormat.format(listDeptSaleManageSumCount)+"</td>");
			}

			//仪器设备
			List<Map<String, Object>> tempInstrumentCostSum = rdsFinanceConfigNewMapper
					.queryDeptInstrumentCostSum(params);
			if(tempInstrumentCostSum.size()==0){			
				listDeptInstrumentSum.add("<td>0</td>");
			}
			else{
				listDeptInstrumentSumCount= (Double)(tempInstrumentCostSum.get(0)==null?0.0:tempInstrumentCostSum.get(0).get("instrumentSum"));
				listDeptInstrumentSum.add("<td>"+decimalFormat.format(listDeptInstrumentSumCount)+"</td>");
			}
			
			//支出费用小计
			listDeptZhiChuFeiYonSumCount=ListDeptWagesSum+ListDeptMaterialSum
					+listDeptWeiWaiSumCount+listDeptXiaoShouSumCount+listDeptQiTaZhiChuSumCount;
			listDeptZhiChuFeiYonCostSum.add("<td>"+decimalFormat.format(listDeptZhiChuFeiYonSumCount)+"</td>");
			
			
			//现金流量
			listDeptXianJinSumCount=ListDeptInTaxSum-ListDeptTaxSum - listDeptZhiChuFeiYonSumCount;
			listDeptXianJinCostSum.add("<td>"+decimalFormat.format(listDeptXianJinSumCount)+"</td>");
			//所得税
			listDeptSuoDeiSumCount=listDeptXianJinSumCount * 0.2 ;
			listDeptSuoDeiCostSum.add("<td>"+decimalFormat.format(listDeptSuoDeiSumCount)+"</td>");
			//现金流量净额
			listDeptJingESumCount= listDeptXianJinSumCount - listDeptSuoDeiSumCount;
			listDeptJingECostSum.add("<td>"+decimalFormat.format(listDeptJingESumCount)+"</td>");
			
			//运营利润额
			listDeptYunYinSumCount= listDeptXianJinSumCount+listDeptInsideSumCount-listDeptCostInsideSumCount
					+listDeptInvestmentSumCount+listDeptQiTaZhiChuSumCount-listDeptOtherSumCount;
			listDeptYunYinCostSum.add("<td>"+decimalFormat.format(listDeptYunYinSumCount)+"</td>");
			//所得税
			listDeptSuoDeiShuiSumCount=listDeptYunYinSumCount*0.2;
			listDeptSuoDeiShuiCostSum.add("<td>"+decimalFormat.format(listDeptSuoDeiShuiSumCount)+"</td>");
			//运营净利润额
			listDeptYunYinJingLiRunSumCount=listDeptYunYinSumCount - listDeptSuoDeiShuiSumCount;
			listDeptYunYinJingLiRunCostSum.add("<td>"+decimalFormat.format(listDeptYunYinJingLiRunSumCount)+"</td>");
			//未实付运营费用
			Double a=listDeptAptitudeSumCount == 0.0?0.0:listDeptAptitudeSumCount-listDeptZiZhiSumCount;
			listDeptWeiShiFuYunYinSumCount=a - listDeptHeZouFangSumCount;
			listDeptWeiShiFuYunYinCostSum.add("<td>"+decimalFormat.format(listDeptWeiShiFuYunYinSumCount)+"</td>");
			//虚拟运营净利润合计
			listDeptXuNiYunYinSumCount=listDeptYunYinJingLiRunSumCount - listDeptWeiShiFuYunYinSumCount;
			listDeptXuNiYunYinCostSum.add("<td>"+decimalFormat.format(listDeptXuNiYunYinSumCount)+"</td>");


			listDeptShouRuBuSumAllCount+=listDeptShouRuBuSum;
			listDeptServiceSumAllCount+=listDeptServiceSumCount;
			listDeptSellSumAllCount+=listDeptSellSumCount;
			listDeptHeZouAllCount+=listDeptHeZouCount;
			listDeptInsideSumAllCount+=listDeptInsideSumCount;
			ListDeptInTaxAllSum+=ListDeptInTaxSum;
			ListDeptTaxAllSum+=ListDeptTaxSum;
			ListDeptOutTaxAllSum+=ListDeptOutTaxSum;
			ListDeptWagesAllSum+=ListDeptWagesSum;
			listDeptWeiWaiAllSum+=listDeptWeiWaiSumCount;
			listDeptMaterialCostAllSum+=ListDeptMaterialSum;
			listDeptCaiGouAllSum+=ListDeptCaiGouSum;
			listDeptHaoCaiAllSum+=ListDeptHaoCaiSum;
			listDeptExternalAllSum+=listDeptExternalSumCount;
			listDeptDaiLiAllSum+=listDeptDaiLiSumCount;
			listDeptZiZhiAllSum+=listDeptZiZhiSumCount;

			listDeptXiaoShouAllSum+=listDeptXiaoShouSumCount;
			listDeptBanGongAllSum+=listDeptBanGongSumCount;
			listDeptBeiYonAllSum+=listDeptBeiYonSumCount;
			listDeptChailvAllSum+=listDeptChailvSumCount;
			listDeptFuLiAllSum+=listDeptFuLiSumCount;
			listDeptGuangGaoAllSum+=listDeptGuangGaoSumCount;
			listDeptYeWuAllSum+=listDeptYeWuSumCount;
			listDeptZuPingAllSum+=listDeptZuPingSumCount;
			listDeptQiTaFeiYonAllSum+=listDeptQiTaFeiYonSumCount;
			listDeptQiTaFuKuanAllSum+=listDeptQiTaFuKuanSumCount;
			listDeptQiTaCaiGouAllSum+=listDeptQiTaCaiGouSumCount;

			listDeptXianJinAllSum+=listDeptXianJinSumCount;
			listDeptSuoDeiAllSum+=listDeptSuoDeiSumCount;
			listDeptJingEAllSum+=listDeptJingESumCount;

			listDeptHeZouFangAllSum+=listDeptHeZouFangSumCount;

			listDeptYunYinAllSum+=listDeptYunYinSumCount;
			listDeptSuoDeiShuiAllSum+=listDeptSuoDeiShuiSumCount;
			listDeptYunYinJingLiRunAllSum+=listDeptYunYinJingLiRunSumCount;
			listDeptWeiShiFuYunYinAllSum+=listDeptWeiShiFuYunYinSumCount;
			listDeptXuNiYunYinAllSum+=listDeptXuNiYunYinSumCount;

			listDeptQiTaZhiChuAllSum+=listDeptQiTaZhiChuSumCount;
			listDeptCaiGouYiQiAllSum+=listDeptCaiGouYiQiSumCount;
			listDeptGongChenAllSum+=listDeptGongChenSumCount;
			listDeptYiQiSheBeiAllSum+=listDeptYiQiSheBeiSumCount;
			listDeptZhuangXiuFuKuanAllSum+=listDeptZhuangXiuFuKuanSumCount;
			listDeptZhiChuFeiYonAllSum+=listDeptZhiChuFeiYonSumCount;

			listDeptCostInsideAllSum+=listDeptCostInsideSumCount;
			listDeptSaleManageAllSum+=listDeptSaleManageSumCount;
			listDeptAptitudeAllSum+=listDeptAptitudeSumCount;
			listDeptOtherAllSum+=listDeptOtherSumCount;
			listDeptInvestmentAllSum+=listDeptInvestmentSumCount;
			listDeptInstrumentAllSum+=listDeptInstrumentSumCount;

			//成本小计
			listSumCount=ListDeptWagesSum+ListDeptMaterialSum+listDeptExternalSumCount+listDeptCostInsideSumCount+listDeptSaleManageSumCount+listDeptAptitudeSumCount+listDeptOtherSumCount;
			listSum.add("<td>"+decimalFormat.format(listSumCount)+"</td>");
			listProfitCount = ListDeptOutTaxSum+listDeptInsideSumCount-listSumCount;
			listProfit.add("<td>"+decimalFormat.format(listProfitCount)+"</td>");
			listTaxCount = listProfitCount*0.2;
			listTax.add("<td>"+decimalFormat.format(listTaxCount)+"</td>");
			listNetProfitCount=listProfitCount-listTaxCount;
			listNetProfit.add("<td>"+decimalFormat.format(listNetProfitCount)+"</td>");

			listSumAllCount+=listSumCount;
			listProfitAllCount+=listProfitCount;
			listTaxAllCount+=listTaxCount;
			listNetProfitAllCount+=listNetProfitCount;

		}

		listDept.add("合计");
		list.add(listDept);
		//服务收入
		listDeptServiceSum.add("<td>"+decimalFormat.format(listDeptServiceSumAllCount)+"</td>");
		list.add(listDeptServiceSum);
		//销售收入
		listDeptSellSum.add("<td>"+decimalFormat.format(listDeptSellSumAllCount)+"</td>");
		list.add(listDeptSellSum);
		//合作方（实收）收入
		listDeptHeZou.add("<td>"+decimalFormat.format(listDeptHeZouAllCount)+"</td>");
		list.add(listDeptHeZou);

		//收入小计（含税）
		listDeptInTaxSum.add("<td>"+decimalFormat.format(ListDeptInTaxAllSum)+"</td>");
		list.add(listDeptInTaxSum);
		//收入小计（税）
		listDeptTaxSum.add("<td>"+decimalFormat.format(ListDeptTaxAllSum)+"</td>");
		list.add(listDeptTaxSum);
		//收入小计（不含税）
		listDeptOutTaxSum.add("<td>"+decimalFormat.format(listDeptShouRuBuSumAllCount)+"</td>");
		list.add(listDeptOutTaxSum);
		//空格
		listDeptNull.add("<td>-</td>");
		list.add(listDeptNull);
		//人工成本
		listDeptWagesSum.add("<td>"+decimalFormat.format(ListDeptWagesAllSum)+"</td>");
		list.add(listDeptWagesSum);
		//材料成本
		listDeptMaterialCostSum.add("<td>"+decimalFormat.format(listDeptMaterialCostAllSum)+"</td>");
		list.add(listDeptMaterialCostSum);
		//采购材料付款
		listDeptCaiGouSum.add("<td>"+decimalFormat.format(listDeptCaiGouAllSum)+"</td>");
		list.add(listDeptCaiGouSum);
		//耗材
		listDeptHaoCaiSum.add("<td>"+decimalFormat.format(listDeptHaoCaiAllSum)+"</td>");
		list.add(listDeptHaoCaiSum);
		//委外费用
		listDeptWeiWaiCostSum.add("<td>"+decimalFormat.format(listDeptWeiWaiAllSum)+"</td>");
		list.add(listDeptWeiWaiCostSum);
		//委外检测成本
		listDeptExternalCostSum.add("<td>"+decimalFormat.format(listDeptExternalAllSum)+"</td>");
		list.add(listDeptExternalCostSum);
		//代理费
		listDeptDaiLiCostSum.add("<td>"+decimalFormat.format(listDeptDaiLiAllSum)+"</td>");
		list.add(listDeptDaiLiCostSum);
		//资质费
		listDeptZiZhiCostSum.add("<td>"+decimalFormat.format(listDeptZiZhiAllSum)+"</td>");
		list.add(listDeptZiZhiCostSum);
		//销售管理费用
		listDeptXiaoShouCostSum.add("<td>"+decimalFormat.format(listDeptXiaoShouAllSum)+"</td>");
		list.add(listDeptXiaoShouCostSum);
		//办公费
		listDeptBanGongCostSum.add("<td>"+decimalFormat.format(listDeptBanGongAllSum)+"</td>");
		list.add(listDeptBanGongCostSum);
		//备用金
		listDeptBeiYonCostSum.add("<td>"+decimalFormat.format(listDeptBeiYonAllSum)+"</td>");
		list.add(listDeptBeiYonCostSum);
		//差旅费
		listDeptChailvCostSum.add("<td>"+decimalFormat.format(listDeptChailvAllSum)+"</td>");
		list.add(listDeptChailvCostSum);
		//福利费
		listDeptFuLiCostSum.add("<td>"+decimalFormat.format(listDeptFuLiAllSum)+"</td>");
		list.add(listDeptFuLiCostSum);
		//广告宣传
		listDeptGuangGaoCostSum.add("<td>"+decimalFormat.format(listDeptGuangGaoAllSum)+"</td>");
		list.add(listDeptGuangGaoCostSum);
		//业务招待费
		listDeptYeWuCostSum.add("<td>"+decimalFormat.format(listDeptYeWuAllSum)+"</td>");
		list.add(listDeptYeWuCostSum);
		//租赁费
		listDeptZuPingCostSum.add("<td>"+decimalFormat.format(listDeptZuPingAllSum)+"</td>");
		list.add(listDeptZuPingCostSum);
		//其他费用
		listDeptQiTaFeiYonCostSum.add("<td>"+decimalFormat.format(listDeptQiTaFeiYonAllSum)+"</td>");
		list.add(listDeptQiTaFeiYonCostSum);
		//其他付款
		listDeptQiTaFuKuanCostSum.add("<td>"+decimalFormat.format(listDeptQiTaFuKuanAllSum)+"</td>");
		list.add(listDeptQiTaFuKuanCostSum);
		//其它采购
		listDeptQiTaCaiGouCostSum.add("<td>"+decimalFormat.format(listDeptQiTaCaiGouAllSum)+"</td>");
		list.add(listDeptQiTaCaiGouCostSum);

		//对外投资
		listDeptInvestmentSum.add("<td>"+decimalFormat.format(listDeptInvestmentAllSum)+"</td>");
		list.add(listDeptInvestmentSum);

		//其他支出
		listDeptQiTaZhiChuCostSum.add("<td>"+decimalFormat.format(listDeptQiTaZhiChuAllSum)+"</td>");
		list.add(listDeptQiTaZhiChuCostSum);
		//采购仪器设备付款
		listDeptCaiGouYiQiCostSum.add("<td>"+decimalFormat.format(listDeptCaiGouYiQiAllSum)+"</td>");
		list.add(listDeptCaiGouYiQiCostSum);
		//工程
		listDeptGongChenCostSum.add("<td>"+decimalFormat.format(listDeptGongChenAllSum)+"</td>");
		list.add(listDeptGongChenCostSum);
		//仪器设备
		listDeptYiQiSheBeiCostSum.add("<td>"+decimalFormat.format(listDeptYiQiSheBeiAllSum)+"</td>");
		list.add(listDeptYiQiSheBeiCostSum);
		//装修付款
		listDeptZhuangXiuFuKuanCostSum.add("<td>"+decimalFormat.format(listDeptZhuangXiuFuKuanAllSum)+"</td>");
		list.add(listDeptZhuangXiuFuKuanCostSum);
		//支出费用小计
		listDeptZhiChuFeiYonCostSum.add("<td>"+decimalFormat.format(listDeptZhiChuFeiYonAllSum)+"</td>");
		list.add(listDeptZhiChuFeiYonCostSum);
		//空格
		list.add(listDeptNull);

		//现金流量
		listDeptXianJinCostSum.add("<td>"+decimalFormat.format(listDeptXianJinAllSum)+"</td>");
		list.add(listDeptXianJinCostSum);
		//所得税
		listDeptSuoDeiCostSum.add("<td>"+decimalFormat.format(listDeptSuoDeiAllSum)+"</td>");
		list.add(listDeptSuoDeiCostSum);
		//现金流量净额
		listDeptJingECostSum.add("<td>"+decimalFormat.format(listDeptJingEAllSum)+"</td>");
		list.add(listDeptJingECostSum);
		//空格
		list.add(listDeptNull);

		//合作方（应收）结算收入
		listDeptHeZouFangCostSum.add("<td>"+decimalFormat.format(listDeptHeZouFangAllSum)+"</td>");
		list.add(listDeptHeZouFangCostSum);
		//内部结算收入
		listDeptInsideSum.add("<td>"+decimalFormat.format(listDeptInsideSumAllCount)+"</td>");
		list.add(listDeptInsideSum);
		//其他费用（含折旧及摊销）
		listDeptOtherSum.add("<td>"+decimalFormat.format(listDeptOtherAllSum)+"</td>");
		list.add(listDeptOtherSum);
		//合作方案例资质费用
		listDeptAptitudeSum.add("<td>"+decimalFormat.format(listDeptAptitudeAllSum)+"</td>");
		list.add(listDeptAptitudeSum);
		//内部结算成本
		listDeptCostInsideSum.add("<td>"+decimalFormat.format(listDeptCostInsideAllSum)+"</td>");
		list.add(listDeptCostInsideSum);
		//空格
		list.add(listDeptNull);

		//运营利润额
		listDeptYunYinCostSum.add("<td>"+decimalFormat.format(listDeptYunYinAllSum)+"</td>");
		list.add(listDeptYunYinCostSum);
		//所得税
		listDeptSuoDeiShuiCostSum.add("<td>"+decimalFormat.format(listDeptSuoDeiShuiAllSum)+"</td>");
		list.add(listDeptSuoDeiShuiCostSum);
		//运营净利润额
		listDeptYunYinJingLiRunCostSum.add("<td>"+decimalFormat.format(listDeptYunYinJingLiRunAllSum)+"</td>");
		list.add(listDeptYunYinJingLiRunCostSum);
		//未实付运营费用
		listDeptWeiShiFuYunYinCostSum.add("<td>"+decimalFormat.format(listDeptWeiShiFuYunYinAllSum)+"</td>");
		list.add(listDeptWeiShiFuYunYinCostSum);
		//虚拟运营净利润合计
		listDeptXuNiYunYinCostSum.add("<td>"+decimalFormat.format(listDeptXuNiYunYinAllSum)+"</td>");
		list.add(listDeptXuNiYunYinCostSum);

		//		//销售管理成本
		//		listDeptSaleManageSum.add("<td>"+decimalFormat.format(listDeptSaleManageAllSum)+"</td>");
		//		list.add(listDeptSaleManageSum);
		//		//仪器设备
		//		listDeptInstrumentSum.add("<td>"+decimalFormat.format(listDeptInstrumentAllSum)+"</td>");
		//		list.add(listDeptInstrumentSum);
		//		//成本小计
		//		listSum.add("<td>"+decimalFormat.format(listSumAllCount)+"</td>");
		//		list.add(listSum);
		//		//空格
		//		listDeptNull1.add("<td>-</td>");
		//		list.add(listDeptNull1);
		//		//利润小计
		//		listProfit.add("<td>"+decimalFormat.format(listProfitAllCount)+"</td>");
		//		list.add(listProfit);
		//		//税
		//		listTax.add("<td>"+decimalFormat.format(listTaxAllCount)+"</td>");
		//		list.add(listTax);
		//		//净利润小计
		//		listNetProfit.add("<td>"+decimalFormat.format(listNetProfitAllCount)+"</td>");
		//		list.add(listNetProfit);
		return list;
	}

	@Override
	public List<RdsFinanceAptitudModel> queryAptitude(Map<String, Object> params) {
		return rdsFinanceConfigNewMapper.queryAptitude(params);
	}

	@Override
	public int queryCountAptitude(Map<String, Object> params) {
		return rdsFinanceConfigNewMapper.queryCountAptitude(params);
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
			return rdsFinanceConfigNewMapper.insertAptitudeConfig(params);
		} else {
			// 更新
			return rdsFinanceConfigNewMapper.updateApitudeConfig(params);
		}
	}

	@Override
	public boolean deleteAptitudeConfig(Map<String, Object> param) {
		return rdsFinanceConfigNewMapper.updateApitudeConfigFlag(param);
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
			return rdsFinanceConfigNewMapper.insertFinanceConfig(params);
		} else {
			// 更新
			return rdsFinanceConfigNewMapper.updateFinanceConfig(params);
		}

	}

	@Override
	public List<String> queryUserDept() {
		return rdsFinanceConfigNewMapper.queryUserDept();
	}

	@Override
	public void financeCaseDetail(Map<String, Object> params) {
		try{
			//销售收入案例，即一次性收入，合同收入
			rdsFinanceConfigNewMapper.insertContractStatistic(params);
			//试剂盒内部结算
			rdsFinanceConfigNewMapper.insertKitSetStatistic(params);
		}catch(Exception e){
			e.printStackTrace();
		}
		//服务收入案例
		List<RdsFinanceCaseDetailModel> lists = rdsFinanceConfigNewMapper
				.queryFinanceCaseDetail(params);
		List<Map<String,Object>> listAll = new ArrayList<>();
		for (RdsFinanceCaseDetailModel rdsFinanceCaseDetailModel : lists) {
			String case_subtype = rdsFinanceCaseDetailModel.getCase_subtype();
			if (null != case_subtype && !"".equals(case_subtype)) {
				Map<String, Object> mapResult = new HashMap<>();
				mapResult
				.put("case_id", rdsFinanceCaseDetailModel.getCase_id());
				mapResult.put("case_code", rdsFinanceCaseDetailModel.getCase_code());
				mapResult.put("confirm_date",
						rdsFinanceCaseDetailModel.getConfirm_date());
				mapResult.put("return_sum",
						rdsFinanceCaseDetailModel.getReturn_sum());
				//渠道事业部分公司特殊处理，根据归属地区分
				if("渠道事业部".equals(rdsFinanceCaseDetailModel.getUser_dept_level1())){
					String case_area = rdsFinanceCaseDetailModel.getCase_area() == null ? ""
							: rdsFinanceCaseDetailModel.getCase_area();
					String user_dept_level2 = rdsFinanceCaseDetailModel
							.getUser_dept_level2() == null ? ""
									: rdsFinanceCaseDetailModel.getUser_dept_level2();
					if(!(user_dept_level2.substring(0, 2)).equals(case_area.substring(0, 2))){
						if("内蒙".equals(case_area.substring(0, 2))){
							mapResult.put("user_dept_level2","内蒙古分公司");
						}else if("黑龙".equals(case_area.substring(0, 2))){
							mapResult.put("user_dept_level2","黑龙江分公司");
						}else if("无地".equals(case_area.substring(0, 2))){
							mapResult.put("user_dept_level2",
									rdsFinanceCaseDetailModel.getUser_dept_level2());
						}else{
							mapResult.put("user_dept_level2",case_area.substring(0, 2)+"分公司");
						}
					}else{
						mapResult.put("user_dept_level2",
								rdsFinanceCaseDetailModel.getUser_dept_level2());
					}
				}else
				{
					mapResult.put("user_dept_level2",
							rdsFinanceCaseDetailModel.getUser_dept_level2());
				}
				mapResult.put("user_dept_level1",
						rdsFinanceCaseDetailModel.getUser_dept_level1());
				mapResult.put("user_dept_level3",
						rdsFinanceCaseDetailModel.getUser_dept_level3());
				mapResult.put("case_user", rdsFinanceCaseDetailModel.getCase_user());
				mapResult.put("case_area", rdsFinanceCaseDetailModel.getCase_area());
				mapResult.put("partner", rdsFinanceCaseDetailModel.getPartner());
				mapResult.put("case_type", rdsFinanceCaseDetailModel.getCase_type());
				mapResult.put("case_subtype", rdsFinanceCaseDetailModel.getCase_subtype());
				// 查看改案例的所有项目
				String[] caseType = case_subtype.split(";");
				// 计算资质费
				Double aptitudeCost = 0.0;
				//委外成本
				Double externalCost = 0.0;
				//内部结算费用
				Double insideCost = 0.0;
				// 计算实验费
				Double experimentCost = 0.0;
				//管理费
				Double manageCost = 0.0;
				// 遍历项目计算成本
				for (String string : caseType) {
					Map<String, Object> map = new HashMap<>();
					map.put("finance_program_type", string.trim());
					List<RdsFinanceConfigModel> list = rdsFinanceConfigNewMapper
							.queryAll(map);
					if (list.size() > 0) {
						RdsFinanceConfigModel configModel = list.get(0);
						// 管理费公式
						String finance_manage = configModel.getFinance_manage();
						// 后端结算公式
						String back_settlement = configModel.getBack_settlement();
						//内部结算部门
						mapResult.put("insideCostUnit", configModel.getBack_settlement_dept());
						mapResult.put("manageCostUnit", configModel.getFinance_manage_dept());

						if ("亲子鉴定(资质)".equals(configModel
								.getFinance_program_type())) {
							map.put("partnername",
									rdsFinanceCaseDetailModel.getPartner());
							// 计算资质费
							//							Double aptitudeCost = 0.0;
							// 查询出资质费收费标准
							List<RdsFinanceAptitudModel> aptitudList = rdsFinanceConfigNewMapper
									.queryAptitude(map);
							RdsFinanceAptitudModel rdsFinanceAptitudModel = aptitudList
									.get(0);
							// 正孚和信诺算到委外成本
							if ("广东正孚法医毒物司法鉴定所".equals(rdsFinanceCaseDetailModel.getPartner())
									|| "北京信诺司法鉴定所".equals(rdsFinanceCaseDetailModel.getPartner())) {
								//								Double externalCost = 0.0;
								if (0 == rdsFinanceAptitudModel.getAptitude_case()) {
									externalCost += rdsFinanceAptitudModel.getAptitude_sample()
											* rdsFinanceCaseDetailModel.getSample_count();
								} else {
									externalCost += rdsFinanceAptitudModel.getAptitude_case();
								}
								//口腔试子及毛发加收50元/样本  其他特殊样本加收400元/样本
								if ("广东正孚法医毒物司法鉴定所".equals(rdsFinanceCaseDetailModel.getPartner())) {
									// 特殊样本
									externalCost += rdsFinanceCaseDetailModel.getSpecial_count1() * 50;
									externalCost += rdsFinanceCaseDetailModel.getSpecial_count2() * 400;
								}
								//								mapResult.put("externalCost", externalCost);
								//								mapResult.put("id", UUIDUtil.getUUID());
								//								//插入该案例结算结果
								//								rdsFinanceConfigNewMapper.insertFinanceCaseDetail(mapResult);
							} else {
								if (0 == rdsFinanceAptitudModel.getAptitude_case()) {
									aptitudeCost += rdsFinanceAptitudModel.getAptitude_sample()
											* rdsFinanceCaseDetailModel.getSample_count();
								} else {
									aptitudeCost += rdsFinanceAptitudModel.getAptitude_case();
								}
								//千麦资质费在每个样本基础上加300
								if ("浙江杭州千麦司法鉴定中心".equals(rdsFinanceCaseDetailModel.getPartner())) {
									// 特殊样本
									aptitudeCost +=300;
								}
								//管理费
								//								Double manageCost = 0.0;
								if (null != finance_manage || !"".equals(finance_manage)){
									try {
										manageCost += JScriptInvoke.getProgramFinance(
												finance_manage, rdsFinanceCaseDetailModel.getReturn_sum(),
												1, rdsFinanceCaseDetailModel.getSample_count(),
												aptitudeCost);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								//内部结算费用
								//								Double insideCost = 0.0;
								if (null != back_settlement || !"".equals(back_settlement)){
									try {
										insideCost += JScriptInvoke.getProgramFinance(
												back_settlement, rdsFinanceCaseDetailModel.getReturn_sum(),
												1, rdsFinanceCaseDetailModel.getSample_count(),
												aptitudeCost);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

								//								mapResult.put("id", UUIDUtil.getUUID());
								//								mapResult.put("insideCost", insideCost);
								//								mapResult.put("insideCostUnit", configModel.getBack_settlement_dept());
								//								mapResult.put("manageCost", manageCost);
								//								mapResult.put("manageCostUnit", configModel.getFinance_manage_dept());
								//								mapResult.put("aptitudeCost", aptitudeCost);
								//								//插入该案例结算结果
								//								rdsFinanceConfigNewMapper.insertFinanceCaseDetail(mapResult);
							}
						}else if("亲子鉴定(实验)".equals(configModel
								.getFinance_program_type())){
							map.put("partnername",
									rdsFinanceCaseDetailModel.getPartner());
							// 计算实验费
							//							Double experimentCost = 0.0;
							// 查询出资质费收费标准
							List<RdsFinanceAptitudModel> aptitudList = rdsFinanceConfigNewMapper
									.queryAptitude(map);
							RdsFinanceAptitudModel rdsFinanceAptitudModel = aptitudList
									.get(0);

							if (0 == rdsFinanceAptitudModel.getExperiment_case()) {
								experimentCost += rdsFinanceAptitudModel.getExperiment_sample()
										* rdsFinanceCaseDetailModel.getSample_count();
							} else {
								experimentCost += rdsFinanceAptitudModel.getExperiment_case();
							}
							//管理费
							//							Double manageCost = 0.0;
							if (null != finance_manage || !"".equals(finance_manage)){
								try {
									manageCost += JScriptInvoke.getProgramFinance(
											finance_manage, rdsFinanceCaseDetailModel.getReturn_sum(),
											1, rdsFinanceCaseDetailModel.getSample_count(),
											experimentCost);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							//内部结算费用
							//							Double insideCost = 0.0;
							if (null != back_settlement || !"".equals(back_settlement)){
								try {
									insideCost += JScriptInvoke.getProgramFinance(
											back_settlement, rdsFinanceCaseDetailModel.getReturn_sum(),
											1, rdsFinanceCaseDetailModel.getSample_count(),
											experimentCost);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							//							mapResult.put("id", UUIDUtil.getUUID());
							//							mapResult.put("insideCost", insideCost);
							//							mapResult.put("insideCostUnit", configModel.getBack_settlement_dept());
							//							mapResult.put("manageCost", manageCost);
							//							mapResult.put("manageCostUnit", configModel.getFinance_manage_dept());
							//							mapResult.put("experimentCost", experimentCost);
							//							//插入该案例结算结果
							//							rdsFinanceConfigNewMapper.insertFinanceCaseDetail(mapResult);

						}else
						{
							//管理费
							//							Double manageCost = 0.0;
							if (null != finance_manage || !"".equals(finance_manage)){
								try {
									manageCost += JScriptInvoke.getProgramFinance(
											finance_manage, rdsFinanceCaseDetailModel.getReturn_sum(),1, rdsFinanceCaseDetailModel.getSample_count(),0.0);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							//内部结算费用
							//							Double insideCost = 0.0;
							if (null != back_settlement || !"".equals(back_settlement)){
								try {
									insideCost += JScriptInvoke.getProgramFinance(
											back_settlement, rdsFinanceCaseDetailModel.getReturn_sum(),1, rdsFinanceCaseDetailModel.getSample_count(),0.0);
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
							//美吉按3500/案例计入委外
							if("无创亲子".equals(configModel
									.getFinance_program_type())){
								if(rdsFinanceCaseDetailModel.getCase_code().startsWith("W")){
									externalCost+=3500.0;
									insideCost=0.0;
									manageCost=0.0;
								}
								//									mapResult.put("externalCost", 3500.0);
							}
							if(rdsFinanceCaseDetailModel.getUser_dept_level1().equals(configModel.getBack_settlement_dept())){
								insideCost=0.0;
							}
							if(rdsFinanceCaseDetailModel.getUser_dept_level1().equals(configModel.getFinance_manage_dept())){
								manageCost=0.0;
							}
							//							mapResult.put("id", UUIDUtil.getUUID());
							//							mapResult.put("insideCost", insideCost);
							//							mapResult.put("insideCostUnit", configModel.getBack_settlement_dept());
							//							mapResult.put("manageCost", manageCost);
							//							mapResult.put("manageCostUnit", configModel.getFinance_manage_dept());
							//							//插入该案例结算结果
							//							rdsFinanceConfigNewMapper.insertFinanceCaseDetail(mapResult);
						}

					}

				}
				Map<String,Object> maps = new HashMap<>();
				maps.put("case_id", rdsFinanceCaseDetailModel.getCase_id());
				if(rdsFinanceConfigNewMapper.queryCaseDetailAllCount(maps)>0){
					insideCost=0.0;
					manageCost=0.0;
					externalCost=0.0;
					experimentCost=0.0;
					aptitudeCost=0.0;
				}
				mapResult.put("id", UUIDUtil.getUUID());
				mapResult.put("insideCost", insideCost);
				mapResult.put("externalCost", externalCost);
				mapResult.put("aptitudeCost", aptitudeCost);
				mapResult.put("experimentCost", experimentCost);
				//				mapResult.put("insideCostUnit", configModel.getBack_settlement_dept());
				mapResult.put("manageCost", manageCost);
				//				mapResult.put("manageCostUnit", configModel.getFinance_manage_dept());
				listAll.add(mapResult);
			}else{
				Map<String, Object> mapResult = new HashMap<>();
				mapResult
				.put("case_id", rdsFinanceCaseDetailModel.getCase_id());
				mapResult.put("case_code", rdsFinanceCaseDetailModel.getCase_code());
				mapResult.put("confirm_date",
						rdsFinanceCaseDetailModel.getConfirm_date());
				mapResult.put("return_sum",
						rdsFinanceCaseDetailModel.getReturn_sum());
				mapResult.put("user_dept_level1",
						rdsFinanceCaseDetailModel.getUser_dept_level1());
				mapResult.put("user_dept_level2",
						rdsFinanceCaseDetailModel.getUser_dept_level2());
				mapResult.put("user_dept_level3",
						rdsFinanceCaseDetailModel.getUser_dept_level3());
				mapResult.put("case_user", rdsFinanceCaseDetailModel.getCase_user());
				mapResult.put("case_area", rdsFinanceCaseDetailModel.getCase_area());
				mapResult.put("partner", rdsFinanceCaseDetailModel.getPartner());
				mapResult.put("case_type", rdsFinanceCaseDetailModel.getCase_type());
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
		Map<String,Object> map = new HashMap<>();
		map.put("list", listAll);
		if(listAll.size()>0)
			//插入该案例结算结果
			rdsFinanceConfigNewMapper.insertFinanceCaseDetail(map);
	}

	@Override
	public List<RdsFinanceCaseDetailStatisticsModel> queryCaseDetailAll(
			Map<String, Object> params) {
		return rdsFinanceConfigNewMapper.queryCaseDetailAll(params);
	}

	@Override
	public int queryCaseDetailAllCount(Map<String, Object> params) {
		return rdsFinanceConfigNewMapper.queryCaseDetailAllCount(params);
	}

	@Override
	public List<RdsFinanceCaseDetailStatisticsModel> queryCaseDetailAllSum(
			Map<String, Object> params) {
		return rdsFinanceConfigNewMapper.queryCaseDetailAllSum(params);
	}

	@Override
	public List<Map<String, String>> queryUserDepartment(
			Map<String, Object> params) {
		return rdsFinanceConfigNewMapper.queryUserDepartment(params);
	}

	@Override
	public void exportCaseDetailAll(Map<String, Object> params,HttpServletResponse response) throws Exception{
		String filename = "个人案例详细信息表";
		List<RdsFinanceCaseDetailStatisticsModel> list = rdsFinanceConfigNewMapper.queryCaseDetailAll(params);
		Object[] titles = { "案例编号","归属人", "归属地", "财务确认时间", "回款金额", "项目", "类型",
				"一级部门", "二级部门", "三级部门", "内部结算部门", "内部结算费用", "管理费部门", "管理费",
				"委外成本", "资质费", "实验费", "合作方" };
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsFinanceCaseDetailStatisticsModel statisticModel = list.get(i);
			objects.add(statisticModel.getCase_code());
			objects.add(statisticModel.getCase_user());
			objects.add(statisticModel.getCase_area());
			objects.add(StringUtils.dateToChineseTen(statisticModel.getConfirm_date()));
			objects.add(statisticModel.getReturn_sum());
			objects.add(statisticModel.getCase_type());
			objects.add("1".equals(statisticModel.getType()) ?"服务收入":"销售收入");
			objects.add(statisticModel.getUser_dept_level1());
			objects.add(statisticModel.getUser_dept_level2());
			objects.add(statisticModel.getUser_dept_level3());
			objects.add(statisticModel.getInsideCostUnit());
			objects.add(statisticModel.getInsideCost());
			objects.add(statisticModel.getManageCostUnit());
			objects.add(statisticModel.getManageCost());
			objects.add(statisticModel.getExternalCost());
			objects.add(statisticModel.getAptitudeCost());
			objects.add(statisticModel.getExperimentCost());
			objects.add(statisticModel.getPartner());
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "个人案例详细信息表");

	}

	@Override
	public List<RdsFinanceAmoebaStatisticsModel> queryAmoebaInfoPage(
			Map<String, Object> params) {
		return rdsFinanceConfigNewMapper.queryAmoebaInfoPage(params);
	}

	@Override
	public int queryCountAmoebaInfo(Map<String, Object> params) {
		return rdsFinanceConfigNewMapper.queryCountAmoebaInfo(params);
	}

	@Override
	public boolean insertAmoebaInfo(Map<String, Object> params)
			throws Exception {
		return rdsFinanceConfigNewMapper.insertAmoebaInfo(params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exportAmoeba(Map<String, Object> params,HttpServletResponse response) {
		
		Map<String, Object> map = new HashMap<>();
		List<List<String>> list = new ArrayList<>();
		List<String> listDeptTemp = new ArrayList<String>();
		List<String> listDeptTemp2 = (List<String>) params.get("listDeptTemp");
		List<String> listDeptTemp3 = (List<String>) params.get("listDeptTemp2");
		List<String> shangji = (List<String>) params.get("shangji");
		List<String> listDept = new ArrayList<>();
		listDept.add("");
		if (params.get("listDeptTemp") != null){
			RdsStatisticsDepartmentModel BuMen=null;
			for (String string : listDeptTemp3) {
				BuMen=rdsFinanceConfigNewMapper.queryBuMen(string);
				listDept.add(BuMen.getDeptname());
				listDeptTemp.add(BuMen.getDeptname());
			}
		}else{
			List<RdsStatisticsDepartmentModel> listBuMen=rdsFinanceConfigNewMapper.queryXiaJiBuMen("0");
			for (RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel : listBuMen) {
				listDept.add(rdsStatisticsDepartmentModel.getDeptname());
				listDeptTemp.add(rdsStatisticsDepartmentModel.getDeptname());
			}
		}
 

		//部门服务收入
		List<String> listDeptServiceSum = new ArrayList<>();
		listDeptServiceSum.add("服务收入");
		//部门销售收入
		List<String> listDeptSellSum = new ArrayList<>();
		listDeptSellSum.add("销售收入");
		//合作方（实收）收入
		List<String> listDeptHeZou = new ArrayList<>();
		listDeptHeZou.add("合作方（实收）收入");	 
		//部门内部结算收入
		List<String> listDeptInsideSum = new ArrayList<>();
		listDeptInsideSum.add("内部结算收入");
		//部门收入小计（含税）
		List<String> listDeptInTaxSum = new ArrayList<>();
		listDeptInTaxSum.add("收入小计(含税)");
		//部门收入小计（税）
		List<String> listDeptTaxSum = new ArrayList<>();
		listDeptTaxSum.add("税");
		//部门收入小计（不含税）
		List<String> listDeptOutTaxSum = new ArrayList<>();
		listDeptOutTaxSum.add("收入小计（不含税）");
		//空格
		List<String> listDeptNull = new ArrayList<>();
		listDeptNull.add("");
		//人工成本
		List<String> listDeptWagesSum = new ArrayList<>();
		listDeptWagesSum.add("人工成本");
		//材料成本
		List<String> listDeptMaterialCostSum = new ArrayList<>();
		listDeptMaterialCostSum.add("材料成本");
		//采购材料付款
		List<String> listDeptCaiGouSum = new ArrayList<>();
		listDeptCaiGouSum.add("采购材料付款");
		//耗材
		List<String> listDeptHaoCaiSum = new ArrayList<>();
		listDeptHaoCaiSum.add("耗材");
		//委外费用
		List<String> listDeptWeiWaiCostSum = new ArrayList<>();
		listDeptWeiWaiCostSum.add("委外费用");
		//委外检测成本
		List<String> listDeptExternalCostSum = new ArrayList<>();
		listDeptExternalCostSum.add("委外检测成本");
		//代理费
		List<String> listDeptDaiLiCostSum = new ArrayList<>();
		listDeptDaiLiCostSum.add("代理费");
		//资质费
		List<String> listDeptZiZhiCostSum = new ArrayList<>();
		listDeptZiZhiCostSum.add("资质费");

		//销售管理费用
		List<String> listDeptXiaoShouCostSum = new ArrayList<>();
		listDeptXiaoShouCostSum.add("销售管理费用");
		//办公费
		List<String> listDeptBanGongCostSum = new ArrayList<>();
		listDeptBanGongCostSum.add("办公费");
		//备用金
		List<String> listDeptBeiYonCostSum = new ArrayList<>();
		listDeptBeiYonCostSum.add("备用金");
		//差旅费
		List<String> listDeptChailvCostSum = new ArrayList<>();
		listDeptChailvCostSum.add("差旅费");
		//福利费
		List<String> listDeptFuLiCostSum = new ArrayList<>();
		listDeptFuLiCostSum.add("福利费");
		//广告宣传
		List<String> listDeptGuangGaoCostSum = new ArrayList<>();
		listDeptGuangGaoCostSum.add("广告宣传");
		//业务招待费
		List<String> listDeptYeWuCostSum = new ArrayList<>();
		listDeptYeWuCostSum.add("业务招待费");
		//租赁费
		List<String> listDeptZuPingCostSum = new ArrayList<>();
		listDeptZuPingCostSum.add("租赁费");
		//其他费用
		List<String> listDeptQiTaFeiYonCostSum = new ArrayList<>();
		listDeptQiTaFeiYonCostSum.add("其他费用");
		//其他付款
		List<String> listDeptQiTaFuKuanCostSum = new ArrayList<>();
		listDeptQiTaFuKuanCostSum.add("其他付款");
		//其它采购
		List<String> listDeptQiTaCaiGouCostSum = new ArrayList<>();
		listDeptQiTaCaiGouCostSum.add("其它采购");

		//对外投资
		List<String> listDeptInvestmentSum = new ArrayList<>();
		listDeptInvestmentSum.add("对外投资");

		//其他支出
		List<String> listDeptQiTaZhiChuCostSum = new ArrayList<>();
		listDeptQiTaZhiChuCostSum.add("其他支出");
		//采购仪器设备付款
		List<String> listDeptCaiGouYiQiCostSum = new ArrayList<>();
		listDeptCaiGouYiQiCostSum.add("采购仪器设备付款");
		//工程
		List<String> listDeptGongChenCostSum = new ArrayList<>();
		listDeptGongChenCostSum.add("工程");
		//仪器设备
		List<String> listDeptYiQiSheBeiCostSum = new ArrayList<>();
		listDeptYiQiSheBeiCostSum.add("仪器设备");
		//装修付款
		List<String> listDeptZhuangXiuFuKuanCostSum = new ArrayList<>();
		listDeptZhuangXiuFuKuanCostSum.add("装修付款");
		//支出费用小计
		List<String> listDeptZhiChuFeiYonCostSum = new ArrayList<>();
		listDeptZhiChuFeiYonCostSum.add("支出费用小计");

		//现金流量
		List<String> listDeptXianJinCostSum = new ArrayList<>();
		listDeptXianJinCostSum.add("现金流量");
		//所得税
		List<String> listDeptSuoDeiCostSum = new ArrayList<>();
		listDeptSuoDeiCostSum.add("所得税");
		//现金流量净额
		List<String> listDeptJingECostSum = new ArrayList<>();
		listDeptJingECostSum.add("现金流量净额");

		//合作方（应收）结算收入
		List<String> listDeptHeZouFangCostSum = new ArrayList<>();
		listDeptHeZouFangCostSum.add("合作方（应收）结算收入");

		//运营利润额
		List<String> listDeptYunYinCostSum = new ArrayList<>();
		listDeptYunYinCostSum.add("运营利润额");
		//所得税
		List<String> listDeptSuoDeiShuiCostSum = new ArrayList<>();
		listDeptSuoDeiShuiCostSum.add("所得税");
		//运营净利润额
		List<String> listDeptYunYinJingLiRunCostSum = new ArrayList<>();
		listDeptYunYinJingLiRunCostSum.add("运营净利润额");
		//未实付运营费用
		List<String> listDeptWeiShiFuYunYinCostSum = new ArrayList<>();
		listDeptWeiShiFuYunYinCostSum.add("未实付运营费用");
		//虚拟运营净利润合计
		List<String> listDeptXuNiYunYinCostSum = new ArrayList<>();
		listDeptXuNiYunYinCostSum.add("虚拟运营净利润合计");

		//内部结算成本
		List<String> listDeptCostInsideSum = new ArrayList<>();
		listDeptCostInsideSum.add("内部结算成本");
		//销售管理结算成本
		List<String> listDeptSaleManageSum = new ArrayList<>();
		listDeptSaleManageSum.add("销售管理费用");
		//资质费用
		List<String> listDeptAptitudeSum = new ArrayList<>();
		listDeptAptitudeSum.add("资质费用");
		//其他费用（含折旧及摊销）
		List<String> listDeptOtherSum = new ArrayList<>();
		listDeptOtherSum.add("其他费用（含折旧及摊销）");
		//仪器设备
		List<String> listDeptInstrumentSum = new ArrayList<>();
		listDeptInstrumentSum.add("房屋、装修、仪器及设备采购成本");
		//成本小计
		List<String> listSum = new ArrayList<>();
		listSum.add("成本小计");
		//空格
		List<String> listDeptNull1 = new ArrayList<>();
		listDeptNull1.add("");
		//利润小计
		List<String> listProfit = new ArrayList<>();
		listProfit.add("利润小计");
		//税
		List<String> listTax = new ArrayList<>();
		listTax.add("税");
		//净利润小计///////////////////
		List<String> listNetProfit = new ArrayList<>();
		listNetProfit.add("净利润");
		// 内部结算按照月份计算
		map.put("confirm_date_start", params.get("confirm_date_start").toString()
				.substring(0, 7));
		map.put("confirm_date_end", params.get("confirm_date_end").toString()
				.substring(0, 7));
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置

		Double listDeptShouRuBuSumAllCount = 0.0;
		Double listDeptServiceSumAllCount = 0.0;
		Double listDeptSellSumAllCount = 0.0;
		Double listDeptHeZouAllCount = 0.0;
		Double listDeptInsideSumAllCount = 0.0;
		Double ListDeptInTaxAllSum = 0.0;
		Double ListDeptTaxAllSum = 0.0;
		Double ListDeptOutTaxAllSum = 0.0;
		Double ListDeptWagesAllSum = 0.0;
		Double listDeptMaterialCostAllSum=0.0;
		Double listDeptCaiGouAllSum=0.0;
		Double listDeptHaoCaiAllSum=0.0;
		Double listDeptWeiWaiAllSum =0.0;
		Double listDeptExternalAllSum =0.0;
		Double listDeptDaiLiAllSum =0.0;
		Double listDeptZiZhiAllSum =0.0;

		Double listDeptXiaoShouAllSum =0.0;
		Double listDeptBanGongAllSum =0.0;
		Double listDeptBeiYonAllSum =0.0;
		Double listDeptChailvAllSum =0.0;
		Double listDeptFuLiAllSum =0.0;
		Double listDeptGuangGaoAllSum =0.0;
		Double listDeptYeWuAllSum =0.0;
		Double listDeptZuPingAllSum =0.0;
		Double listDeptQiTaFeiYonAllSum =0.0;
		Double listDeptQiTaFuKuanAllSum =0.0;
		Double listDeptQiTaCaiGouAllSum =0.0;

		Double listDeptQiTaZhiChuAllSum =0.0;
		Double listDeptCaiGouYiQiAllSum =0.0;
		Double listDeptGongChenAllSum =0.0;
		Double listDeptYiQiSheBeiAllSum =0.0;
		Double listDeptZhuangXiuFuKuanAllSum =0.0;
		Double listDeptZhiChuFeiYonAllSum =0.0;

		Double listDeptXianJinAllSum=0.0;
		Double listDeptSuoDeiAllSum=0.0;
		Double listDeptJingEAllSum=0.0;

		Double listDeptHeZouFangAllSum=0.0;

		Double listDeptYunYinAllSum=0.0;
		Double listDeptSuoDeiShuiAllSum=0.0;
		Double listDeptYunYinJingLiRunAllSum=0.0;
		Double listDeptWeiShiFuYunYinAllSum=0.0;
		Double listDeptXuNiYunYinAllSum=0.0;

		Double listDeptCostInsideAllSum=0.0;
		Double listDeptSaleManageAllSum=0.0;
		Double listDeptAptitudeAllSum=0.0;
		Double listDeptOtherAllSum=0.0;
		Double listDeptInvestmentAllSum=0.0;
		Double listDeptInstrumentAllSum=0.0;
		Double listSumAllCount=0.0;
		Double listProfitAllCount=0.0;
		Double listTaxAllCount = 0.0;
		Double listNetProfitAllCount=0.0;
		for (String string : listDeptTemp) {
			Double listDeptShouRuBuSum  = 0.0;
			Double listDeptServiceSumCount = 0.0;
			Double listDeptSellSumCount = 0.0;
			Double listDeptHeZouCount = 0.0;
			Double listDeptInsideSumCount = 0.0;
			Double ListDeptInTaxSum = 0.0;
			Double ListDeptTaxSum = 0.0;
			Double ListDeptOutTaxSum = 0.0;
			Double ListDeptWagesSum = 0.0;
			Double ListDeptMaterialSum = 0.0;
			Double ListDeptCaiGouSum = 0.0;
			Double ListDeptHaoCaiSum = 0.0;
			Double listDeptWeiWaiSumCount=0.0;
			Double listDeptExternalSumCount=0.0;
			Double listDeptDaiLiSumCount=0.0;
			Double listDeptZiZhiSumCount=0.0;

			Double listDeptXiaoShouSumCount=0.0;
			Double listDeptBanGongSumCount=0.0;
			Double listDeptBeiYonSumCount=0.0;
			Double listDeptChailvSumCount=0.0;
			Double listDeptFuLiSumCount=0.0;
			Double listDeptGuangGaoSumCount=0.0;
			Double listDeptYeWuSumCount=0.0;
			Double listDeptZuPingSumCount=0.0;
			Double listDeptQiTaFeiYonSumCount=0.0;
			Double listDeptQiTaFuKuanSumCount=0.0;
			Double listDeptQiTaCaiGouSumCount=0.0;

			Double listDeptQiTaZhiChuSumCount=0.0;
			Double listDeptCaiGouYiQiSumCount=0.0;
			Double listDeptGongChenSumCount=0.0;
			Double listDeptYiQiSheBeiSumCount=0.0;
			Double listDeptZhuangXiuFuKuanSumCount=0.0;
			Double listDeptZhiChuFeiYonSumCount=0.0;

			Double listDeptXianJinSumCount=0.0;
			Double listDeptSuoDeiSumCount=0.0;
			Double listDeptJingESumCount=0.0;

			Double listDeptHeZouFangSumCount=0.0;

			Double listDeptYunYinSumCount=0.0;
			Double listDeptSuoDeiShuiSumCount=0.0;
			Double listDeptYunYinJingLiRunSumCount=0.0;
			Double listDeptWeiShiFuYunYinSumCount=0.0;
			Double listDeptXuNiYunYinSumCount=0.0;

			Double listDeptCostInsideSumCount=0.0;
			Double listDeptSaleManageSumCount=0.0;
			Double listDeptAptitudeSumCount=0.0;
			Double listDeptOtherSumCount=0.0;
			Double listDeptInvestmentSumCount=0.0;
			Double listDeptInstrumentSumCount=0.0;
			Double listSumCount=0.0;
			Double listProfitCount=0.0;
			Double listTaxCount = 0.0;
			Double listNetProfitCount=0.0;
			if(listDeptTemp2==null){
				params.put("deptname", string);
				map.put("deptname", string);
			}else if(shangji.size()==1){
				params.put("deptname", string);
				map.put("deptname", string);
			}else if(shangji.size()==2){
				params.put("deptname", shangji.get(0));
				map.put("deptname", shangji.get(0));
				params.put("deptname1", string);
				map.put("deptname1", string);
			}else if(shangji.size()==3){
				params.put("deptname", shangji.get(0));
				map.put("deptname", shangji.get(0));
				params.put("deptname1", shangji.get(1));
				map.put("deptname1", shangji.get(1));
				params.put("deptname2", string);
				map.put("deptname2", string);
			}
			
			//服务收入  type=1
			List<Map<String, Object>> tempServiceSum =null;
			if(map.size()==4  && "精准医学事业部".equals(string)){
				tempServiceSum = rdsFinanceConfigNewMapper
						.queryDepServiceSum2(params);
			}else{
				tempServiceSum = rdsFinanceConfigNewMapper
						.queryDepServiceSum(params);
			} 
			if(tempServiceSum.size()==0)
			{
				listDeptServiceSumCount=0.0;
				listDeptServiceSum.add("0");
			}
			else{
				listDeptServiceSumCount = (Double)(tempServiceSum.get(0)==null?0.0:tempServiceSum.get(0).get("deptServiceSum"));
				listDeptServiceSum.add(decimalFormat.format(listDeptServiceSumCount));
			}
			//销售收入 type=2
			List<Map<String, Object>> tempSellSum = rdsFinanceConfigNewMapper
					.queryDepSellSum(params);
			if(tempSellSum.size()==0){
				listDeptSellSumCount=0.0;
				listDeptSellSum.add("0");
			}
			else{
				listDeptSellSumCount = (Double)(tempSellSum.get(0)==null?0.0:tempSellSum.get(0).get("deptSellSum"));
				listDeptSellSum.add(decimalFormat.format(listDeptSellSumCount));
			}
			//合作方（实收）收入
			List<Map<String, Object>> tempHeZou = rdsFinanceConfigNewMapper
					.queryDepHeZou(params);
			if(tempHeZou.size()==0){
				listDeptHeZouCount=0.0;
				listDeptHeZou.add("0");
			}
			else{
				listDeptHeZouCount = (Double)(tempHeZou.get(0)==null?0.0:tempHeZou.get(0).get("deptHeZou"));
				listDeptHeZou.add(decimalFormat.format(listDeptHeZouCount));
			}
		
			//收入小计（含税）
			ListDeptInTaxSum = listDeptHeZouCount+listDeptSellSumCount+ listDeptServiceSumCount;
			listDeptInTaxSum.add(decimalFormat.format(ListDeptInTaxSum));
			//收入（税）
			ListDeptTaxSum =(listDeptServiceSumCount/1.06*0.06)+(listDeptHeZouCount/1.06*0.06)+(listDeptSellSumCount/1.16*0.16) ;
			listDeptTaxSum.add(decimalFormat.format(ListDeptTaxSum));

			//收入小计（不含税）
			listDeptShouRuBuSum=ListDeptInTaxSum-ListDeptTaxSum;
			listDeptOutTaxSum.add(decimalFormat.format(listDeptShouRuBuSum));

			listDeptNull.add("-");
			listDeptNull1.add("-");
			//人工成本  wags
			List<Map<String, Object>> tempWagesSum=null;
			if(map.size()==4  && "精准医学事业部".equals(string)){
				 tempWagesSum = rdsFinanceConfigNewMapper
							.queryDeptWagesSum2(map);
			}else{
				 tempWagesSum = rdsFinanceConfigNewMapper
							.queryDeptWagesSum(map);
			} 
			if(tempWagesSum.size()==0){			
				listDeptWagesSum.add("0");
			}
			else{
				ListDeptWagesSum= (Double)(tempWagesSum.get(0)==null?0.0:tempWagesSum.get(0).get("wagesSum"));
				listDeptWagesSum.add(decimalFormat.format(ListDeptWagesSum));
			}

			//采购材料付款
			params.put("amoeba_program", "采购材料付款");
			List<Map<String, Object>> tempCaiGouSum = rdsFinanceConfigNewMapper
					.queryDeptMaterialCostSum(params);
			if(tempCaiGouSum.size()==0){			
				listDeptCaiGouSum.add("0");
			}
			else{
				ListDeptCaiGouSum= (Double)(tempCaiGouSum.get(0)==null?0.0:tempCaiGouSum.get(0).get("materialSum"));
				listDeptCaiGouSum.add(decimalFormat.format(ListDeptCaiGouSum));
			}

			//耗材
			params.put("amoeba_program", "耗材");
			List<Map<String, Object>> tempHaoCaiSum = rdsFinanceConfigNewMapper
					.queryDeptMaterialCostSum(params);
			if(tempHaoCaiSum.size()==0){			
				listDeptHaoCaiSum.add("0");
			}
			else{
				ListDeptHaoCaiSum= (Double)(tempHaoCaiSum.get(0)==null?0.0:tempHaoCaiSum.get(0).get("materialSum"));
				listDeptHaoCaiSum.add(decimalFormat.format(ListDeptHaoCaiSum));
			}

			//材料成本  
			ListDeptMaterialSum=ListDeptCaiGouSum+ListDeptHaoCaiSum;
			listDeptMaterialCostSum.add(decimalFormat.format(ListDeptMaterialSum));


			//委外检测成本
			params.put("amoeba_program", "委外检测成本");
			listDeptExternalSumCount = findByNameOneE(params, listDeptExternalCostSum, decimalFormat,listDeptExternalSumCount);
			//代理费
			params.put("amoeba_program", "代理费");
			listDeptDaiLiSumCount = findByNameOneE(params, listDeptDaiLiCostSum, decimalFormat, listDeptDaiLiSumCount);
			//资质费
			params.put("amoeba_program", "资质费");
			listDeptZiZhiSumCount = findByNameOneE(params, listDeptZiZhiCostSum, decimalFormat, listDeptZiZhiSumCount);

			//办公费
			params.put("amoeba_program", "办公费");
			listDeptBanGongSumCount = findByNameOneE(params, listDeptBanGongCostSum, decimalFormat,listDeptBanGongSumCount);
			//备用金
			listDeptBeiYonSumCount = findByNameE(params, listDeptBeiYonCostSum, decimalFormat, listDeptBeiYonSumCount);
			//差旅费
			params.put("amoeba_program", "差旅费");
			listDeptChailvSumCount = findByNameOneE(params, listDeptChailvCostSum, decimalFormat,listDeptChailvSumCount);
			//福利费
			params.put("amoeba_program", "福利费");
			listDeptFuLiSumCount = findByNameOneE(params, listDeptFuLiCostSum, decimalFormat,listDeptFuLiSumCount);
			//广告宣传
			params.put("amoeba_program", "广告宣传");
			listDeptGuangGaoSumCount = findByNameOneE(params, listDeptGuangGaoCostSum, decimalFormat,listDeptGuangGaoSumCount);
			//业务招待费
			params.put("amoeba_program", "业务招待费");
			listDeptYeWuSumCount = findByNameOneE(params, listDeptYeWuCostSum, decimalFormat,listDeptYeWuSumCount);
			//租赁费
			params.put("amoeba_program", "租赁费");
			listDeptZuPingSumCount = findByNameOneE(params, listDeptZuPingCostSum, decimalFormat,listDeptZuPingSumCount);
			//其他费用
			params.put("amoeba_program", "其他费用");
			listDeptQiTaFeiYonSumCount = findByNameOneE(params, listDeptQiTaFeiYonCostSum, decimalFormat,listDeptQiTaFeiYonSumCount);
			//其他付款
			params.put("amoeba_program", "其他付款");
			listDeptQiTaFuKuanSumCount = findByNameOneE(params, listDeptQiTaFuKuanCostSum, decimalFormat,listDeptQiTaFuKuanSumCount);
			//其它采购
			params.put("amoeba_program", "其它采购");
			listDeptQiTaCaiGouSumCount = findByNameOneE(params, listDeptQiTaCaiGouCostSum, decimalFormat,listDeptQiTaCaiGouSumCount);
			//销售管理费用
			listDeptXiaoShouSumCount=listDeptBanGongSumCount+listDeptBeiYonSumCount
					+listDeptChailvSumCount+listDeptFuLiSumCount+listDeptGuangGaoSumCount+listDeptYeWuSumCount+listDeptZuPingSumCount
					+listDeptQiTaFeiYonSumCount+listDeptQiTaFuKuanSumCount+listDeptQiTaCaiGouSumCount;
			listDeptXiaoShouCostSum.add(decimalFormat.format(listDeptXiaoShouSumCount));

			//采购仪器设备付款
			params.put("amoeba_program", "采购仪器设备付款");
			listDeptCaiGouYiQiSumCount = findByNameOneE(params, listDeptCaiGouYiQiCostSum, decimalFormat,listDeptCaiGouYiQiSumCount);
			//工程
			params.put("amoeba_program", "工程");
			listDeptGongChenSumCount = findByNameOneE(params, listDeptGongChenCostSum, decimalFormat,listDeptGongChenSumCount);
			//仪器设备
			params.put("amoeba_program", "仪器设备");
			listDeptYiQiSheBeiSumCount = findByNameOneE(params, listDeptYiQiSheBeiCostSum, decimalFormat,listDeptYiQiSheBeiSumCount);
			//装修付款
			params.put("amoeba_program", "装修付款");
			listDeptZhuangXiuFuKuanSumCount = findByNameOneE(params, listDeptZhuangXiuFuKuanCostSum, decimalFormat,listDeptZhuangXiuFuKuanSumCount);
			//对外投资
			map.put("amoeba_program", "对外投资");
			List<Map<String, Object>> tempInvestmentCostSum = rdsFinanceConfigNewMapper
					.queryDeptDepreciationCostSum(map);
			if(tempInvestmentCostSum.size()==0){			
				listDeptInvestmentSum.add("0");
			}
			else{
				listDeptInvestmentSumCount= (Double)(tempInvestmentCostSum.get(0)==null?0.0:tempInvestmentCostSum.get(0).get("depreciationSum"));
				listDeptInvestmentSum.add(decimalFormat.format(listDeptInvestmentSumCount));
			}
			//其他支出
			listDeptQiTaZhiChuSumCount= listDeptCaiGouYiQiSumCount+listDeptGongChenSumCount
					+listDeptYiQiSheBeiSumCount+listDeptZhuangXiuFuKuanSumCount;
			listDeptQiTaZhiChuCostSum.add(decimalFormat.format(listDeptQiTaZhiChuSumCount));


			//合作方（应收）结算收入
			params.put("amoeba_program", "合作方收入");
			List<Map<String, Object>> tempHeZouFangSum = rdsFinanceConfigNewMapper
					.queryDeptHeZouFangCostSum(params);
			if(tempHeZouFangSum.size()==0){			
				listDeptHeZouFangCostSum.add("0");
			}
			else{
				listDeptHeZouFangSumCount= (Double)(tempHeZouFangSum.get(0)==null?0.0:tempHeZouFangSum.get(0).get("HeZouFangSum"));
				listDeptHeZouFangCostSum.add(decimalFormat.format(listDeptHeZouFangSumCount));
			}

			//内部结算收入
			String deptnames=null;
			List<Map<String, Object>> tempInsideSum =null;
			if(listDeptTemp2==null || shangji.size()==1){
				deptnames=(String) params.get("deptname");
				if("司法鉴定事业部".equals(deptnames)){
					tempInsideSum = rdsFinanceConfigNewMapper
							.queryDeptInsideSum1(params);
				}else if("精准医学事业部".equals(deptnames)){
					 tempInsideSum = rdsFinanceConfigNewMapper
							.queryDeptInsideSum2(params);
				}else if("公司总部".equals(deptnames)){
					 tempInsideSum = rdsFinanceConfigNewMapper
								.queryDeptInsideSum3(params);
				} 
			}else if(shangji.size()==2){
				deptnames=(String) params.get("deptname1");
				if("技术与服务管理部".equals(string)){
					tempInsideSum = rdsFinanceConfigNewMapper
							.queryDeptInsideSum1(params);
				}else if("精准医学事业部".equals(string)){
					 tempInsideSum = rdsFinanceConfigNewMapper
							.queryDeptInsideSum2(params);
				} 
			}
			if(tempInsideSum==null){
				listDeptInsideSumCount=0.0;				
				listDeptInsideSum.add("0");
			}
			else{
				listDeptInsideSumCount = (Double)(tempInsideSum.get(0)==null?0.0:tempInsideSum.get(0).get("deptInsideSum"));
				listDeptInsideSum.add(decimalFormat.format(listDeptInsideSumCount));
			}
			
			//其他费用（含折旧及摊销）
			map.put("amoeba_program", "其他费用（含折旧及摊销）");
			List<Map<String, Object>> tempOtherCostSum = rdsFinanceConfigNewMapper
					.queryDeptDepreciationCostSum(map);
			if(tempOtherCostSum.size()==0){			
				listDeptOtherSum.add("0");
			}
			else{
				listDeptOtherSumCount= (Double)(tempOtherCostSum.get(0)==null?0.0:tempOtherCostSum.get(0).get("depreciationSum"));
				listDeptOtherSum.add(decimalFormat.format(listDeptOtherSumCount));
			}
			//资质费用
			List<Map<String, Object>> tempDeptAptitudeSum=null;
			if("司法鉴定事业部".equals(deptnames)){
				tempDeptAptitudeSum = rdsFinanceConfigNewMapper
						.queryDeptAptitudeCostSum(params);
			} 
			if(tempDeptAptitudeSum==null){		
				listDeptAptitudeSum.add("0");
			}
			else{
				listDeptAptitudeSumCount = (Double)(tempDeptAptitudeSum.get(0)==null?0.0:tempDeptAptitudeSum.get(0).get("deptAptitudeCostSum"));
				listDeptAptitudeSum.add(decimalFormat.format(listDeptAptitudeSumCount));
			}
			//内部结算成本
			List<Map<String, Object>> tempCostInsideSum=null;
			if(map.size()==5  && "精准医学事业部".equals(string)){
				 tempCostInsideSum = rdsFinanceConfigNewMapper
							.queryCostInsideSum2(params);
			}else{
				 tempCostInsideSum = rdsFinanceConfigNewMapper
							.queryCostInsideSum(params);
			} 
			if(tempCostInsideSum.size()==0){			
				listDeptCostInsideSum.add("0");
			}
			else{
				listDeptCostInsideSumCount = (Double)(tempCostInsideSum.get(0)==null?0.0:tempCostInsideSum.get(0).get("costInsideSum"));
				listDeptCostInsideSum.add(decimalFormat.format(listDeptCostInsideSumCount));
			}

			//委外费用 
			listDeptWeiWaiSumCount=listDeptExternalSumCount+listDeptDaiLiSumCount+listDeptZiZhiSumCount;
			listDeptWeiWaiCostSum.add(decimalFormat.format(listDeptWeiWaiSumCount));
			
			//销售管理费用
			List<Map<String, Object>> tempSaleManageSum = rdsFinanceConfigNewMapper
					.querySaleManageSum(params);
			if(tempSaleManageSum.size()==0){			
				listDeptSaleManageSum.add("0");
			}
			else{
				listDeptSaleManageSumCount = (Double)(tempSaleManageSum.get(0)==null?0.0:tempSaleManageSum.get(0).get("deptSaleManageSum"));
				listDeptSaleManageSum.add(decimalFormat.format(listDeptSaleManageSumCount));
			}

			//仪器设备
			List<Map<String, Object>> tempInstrumentCostSum = rdsFinanceConfigNewMapper
					.queryDeptInstrumentCostSum(params);
			if(tempInstrumentCostSum.size()==0){			
				listDeptInstrumentSum.add("0");
			}
			else{
				listDeptInstrumentSumCount= (Double)(tempInstrumentCostSum.get(0)==null?0.0:tempInstrumentCostSum.get(0).get("instrumentSum"));
				listDeptInstrumentSum.add(decimalFormat.format(listDeptInstrumentSumCount));
			}
			
			//支出费用小计
			listDeptZhiChuFeiYonSumCount=ListDeptWagesSum+ListDeptMaterialSum
					+listDeptWeiWaiSumCount+listDeptXiaoShouSumCount+listDeptQiTaZhiChuSumCount;
			listDeptZhiChuFeiYonCostSum.add(decimalFormat.format(listDeptZhiChuFeiYonSumCount));
			
			
			//现金流量
			listDeptXianJinSumCount=ListDeptInTaxSum-ListDeptTaxSum - listDeptZhiChuFeiYonSumCount;
			listDeptXianJinCostSum.add(decimalFormat.format(listDeptXianJinSumCount));
			//所得税
			listDeptSuoDeiSumCount=listDeptXianJinSumCount * 0.2 ;
			listDeptSuoDeiCostSum.add(decimalFormat.format(listDeptSuoDeiSumCount));
			//现金流量净额
			listDeptJingESumCount= listDeptXianJinSumCount - listDeptSuoDeiSumCount;
			listDeptJingECostSum.add(decimalFormat.format(listDeptJingESumCount));
			
			//运营利润额
			listDeptYunYinSumCount= listDeptXianJinSumCount+listDeptInsideSumCount-listDeptCostInsideSumCount
					+listDeptInvestmentSumCount+listDeptQiTaZhiChuSumCount-listDeptOtherSumCount;
			listDeptYunYinCostSum.add(decimalFormat.format(listDeptYunYinSumCount));
			//所得税
			listDeptSuoDeiShuiSumCount=listDeptYunYinSumCount*0.2;
			listDeptSuoDeiShuiCostSum.add(decimalFormat.format(listDeptSuoDeiShuiSumCount));
			//运营净利润额
			listDeptYunYinJingLiRunSumCount=listDeptYunYinSumCount - listDeptSuoDeiShuiSumCount;
			listDeptYunYinJingLiRunCostSum.add(decimalFormat.format(listDeptYunYinJingLiRunSumCount));
			//未实付运营费用
			Double a=listDeptAptitudeSumCount == 0.0?0.0:listDeptAptitudeSumCount-listDeptZiZhiSumCount;
			listDeptWeiShiFuYunYinSumCount=a - listDeptHeZouFangSumCount;
			listDeptWeiShiFuYunYinCostSum.add(decimalFormat.format(listDeptWeiShiFuYunYinSumCount));
			//虚拟运营净利润合计
			listDeptXuNiYunYinSumCount=listDeptYunYinJingLiRunSumCount - listDeptWeiShiFuYunYinSumCount;
			listDeptXuNiYunYinCostSum.add(decimalFormat.format(listDeptXuNiYunYinSumCount));
			
			listDeptShouRuBuSumAllCount+=listDeptShouRuBuSum;
			listDeptServiceSumAllCount+=listDeptServiceSumCount;
			listDeptSellSumAllCount+=listDeptSellSumCount;
			listDeptHeZouAllCount+=listDeptHeZouCount;
			listDeptInsideSumAllCount+=listDeptInsideSumCount;
			ListDeptInTaxAllSum+=ListDeptInTaxSum;
			ListDeptTaxAllSum+=ListDeptTaxSum;
			ListDeptOutTaxAllSum+=ListDeptOutTaxSum;
			ListDeptWagesAllSum+=ListDeptWagesSum;
			listDeptWeiWaiAllSum+=listDeptWeiWaiSumCount;
			listDeptMaterialCostAllSum+=ListDeptMaterialSum;
			listDeptCaiGouAllSum+=ListDeptCaiGouSum;
			listDeptHaoCaiAllSum+=ListDeptHaoCaiSum;
			listDeptExternalAllSum+=listDeptExternalSumCount;
			listDeptDaiLiAllSum+=listDeptDaiLiSumCount;
			listDeptZiZhiAllSum+=listDeptZiZhiSumCount;

			listDeptXiaoShouAllSum+=listDeptXiaoShouSumCount;
			listDeptBanGongAllSum+=listDeptBanGongSumCount;
			listDeptBeiYonAllSum+=listDeptBeiYonSumCount;
			listDeptChailvAllSum+=listDeptChailvSumCount;
			listDeptFuLiAllSum+=listDeptFuLiSumCount;
			listDeptGuangGaoAllSum+=listDeptGuangGaoSumCount;
			listDeptYeWuAllSum+=listDeptYeWuSumCount;
			listDeptZuPingAllSum+=listDeptZuPingSumCount;
			listDeptQiTaFeiYonAllSum+=listDeptQiTaFeiYonSumCount;
			listDeptQiTaFuKuanAllSum+=listDeptQiTaFuKuanSumCount;
			listDeptQiTaCaiGouAllSum+=listDeptQiTaCaiGouSumCount;

			listDeptXianJinAllSum+=listDeptXianJinSumCount;
			listDeptSuoDeiAllSum+=listDeptSuoDeiSumCount;
			listDeptJingEAllSum+=listDeptJingESumCount;

			listDeptHeZouFangAllSum+=listDeptHeZouFangSumCount;

			listDeptYunYinAllSum+=listDeptYunYinSumCount;
			listDeptSuoDeiShuiAllSum+=listDeptSuoDeiShuiSumCount;
			listDeptYunYinJingLiRunAllSum+=listDeptYunYinJingLiRunSumCount;
			listDeptWeiShiFuYunYinAllSum+=listDeptWeiShiFuYunYinSumCount;
			listDeptXuNiYunYinAllSum+=listDeptXuNiYunYinSumCount;

			listDeptQiTaZhiChuAllSum+=listDeptQiTaZhiChuSumCount;
			listDeptCaiGouYiQiAllSum+=listDeptCaiGouYiQiSumCount;
			listDeptGongChenAllSum+=listDeptGongChenSumCount;
			listDeptYiQiSheBeiAllSum+=listDeptYiQiSheBeiSumCount;
			listDeptZhuangXiuFuKuanAllSum+=listDeptZhuangXiuFuKuanSumCount;
			listDeptZhiChuFeiYonAllSum+=listDeptZhiChuFeiYonSumCount;

			listDeptCostInsideAllSum+=listDeptCostInsideSumCount;
			listDeptSaleManageAllSum+=listDeptSaleManageSumCount;
			listDeptAptitudeAllSum+=listDeptAptitudeSumCount;
			listDeptOtherAllSum+=listDeptOtherSumCount;
			listDeptInvestmentAllSum+=listDeptInvestmentSumCount;
			listDeptInstrumentAllSum+=listDeptInstrumentSumCount;

			//成本小计
			listSumCount=ListDeptWagesSum+ListDeptMaterialSum+listDeptExternalSumCount+listDeptCostInsideSumCount+listDeptSaleManageSumCount+listDeptAptitudeSumCount+listDeptOtherSumCount;
			listSum.add(decimalFormat.format(listSumCount));
			listProfitCount = ListDeptOutTaxSum+listDeptInsideSumCount-listSumCount;
			listProfit.add(decimalFormat.format(listProfitCount));
			listTaxCount = listProfitCount*0.2;
			listTax.add(decimalFormat.format(listTaxCount));
			listNetProfitCount=listProfitCount-listTaxCount;
			listNetProfit.add(decimalFormat.format(listNetProfitCount));

			listSumAllCount+=listSumCount;
			listProfitAllCount+=listProfitCount;
			listTaxAllCount+=listTaxCount;
			listNetProfitAllCount+=listNetProfitCount;

		}

		listDept.add("合计");
		//服务收入
		listDeptServiceSum.add(decimalFormat.format(listDeptServiceSumAllCount));
		list.add(listDeptServiceSum);
		//销售收入
		listDeptSellSum.add(decimalFormat.format(listDeptSellSumAllCount));
		list.add(listDeptSellSum);
		//合作方（实收）收入
		listDeptHeZou.add(decimalFormat.format(listDeptHeZouAllCount));
		list.add(listDeptHeZou);

		//收入小计（含税）
		listDeptInTaxSum.add(decimalFormat.format(ListDeptInTaxAllSum));
		list.add(listDeptInTaxSum);
		//收入小计（税）
		listDeptTaxSum.add(decimalFormat.format(ListDeptTaxAllSum));
		list.add(listDeptTaxSum);
		//收入小计（不含税）
		listDeptOutTaxSum.add(decimalFormat.format(listDeptShouRuBuSumAllCount));
		list.add(listDeptOutTaxSum);
		//空格
		listDeptNull.add("-"); 
		list.add(listDeptNull);
		//人工成本
		listDeptWagesSum.add(decimalFormat.format(ListDeptWagesAllSum));
		list.add(listDeptWagesSum);
		//材料成本
		listDeptMaterialCostSum.add(decimalFormat.format(listDeptMaterialCostAllSum));
		list.add(listDeptMaterialCostSum);
		//采购材料付款
		listDeptCaiGouSum.add(decimalFormat.format(listDeptCaiGouAllSum));
		list.add(listDeptCaiGouSum);
		//耗材
		listDeptHaoCaiSum.add(decimalFormat.format(listDeptHaoCaiAllSum));
		list.add(listDeptHaoCaiSum);
		//委外费用
		listDeptWeiWaiCostSum.add(decimalFormat.format(listDeptWeiWaiAllSum));
		list.add(listDeptWeiWaiCostSum);
		//委外检测成本
		listDeptExternalCostSum.add(decimalFormat.format(listDeptExternalAllSum));
		list.add(listDeptExternalCostSum);
		//代理费
		listDeptDaiLiCostSum.add(decimalFormat.format(listDeptDaiLiAllSum));
		list.add(listDeptDaiLiCostSum);
		//资质费
		listDeptZiZhiCostSum.add(decimalFormat.format(listDeptZiZhiAllSum));
		list.add(listDeptZiZhiCostSum);
		//销售管理费用
		listDeptXiaoShouCostSum.add(decimalFormat.format(listDeptXiaoShouAllSum));
		list.add(listDeptXiaoShouCostSum);
		//办公费
		listDeptBanGongCostSum.add(decimalFormat.format(listDeptBanGongAllSum));
		list.add(listDeptBanGongCostSum);
		//备用金
		listDeptBeiYonCostSum.add(decimalFormat.format(listDeptBeiYonAllSum));
		list.add(listDeptBeiYonCostSum);
		//差旅费
		listDeptChailvCostSum.add(decimalFormat.format(listDeptChailvAllSum));
		list.add(listDeptChailvCostSum);
		//福利费
		listDeptFuLiCostSum.add(decimalFormat.format(listDeptFuLiAllSum));
		list.add(listDeptFuLiCostSum);
		//广告宣传
		listDeptGuangGaoCostSum.add(decimalFormat.format(listDeptGuangGaoAllSum));
		list.add(listDeptGuangGaoCostSum);
		//业务招待费
		listDeptYeWuCostSum.add(decimalFormat.format(listDeptYeWuAllSum));
		list.add(listDeptYeWuCostSum);
		//租赁费
		listDeptZuPingCostSum.add(decimalFormat.format(listDeptZuPingAllSum));
		list.add(listDeptZuPingCostSum);
		//其他费用
		listDeptQiTaFeiYonCostSum.add(decimalFormat.format(listDeptQiTaFeiYonAllSum));
		list.add(listDeptQiTaFeiYonCostSum);
		//其他付款
		listDeptQiTaFuKuanCostSum.add(decimalFormat.format(listDeptQiTaFuKuanAllSum));
		list.add(listDeptQiTaFuKuanCostSum);
		//其它采购
		listDeptQiTaCaiGouCostSum.add(decimalFormat.format(listDeptQiTaCaiGouAllSum));
		list.add(listDeptQiTaCaiGouCostSum);

		//对外投资
		listDeptInvestmentSum.add(decimalFormat.format(listDeptInvestmentAllSum));
		list.add(listDeptInvestmentSum);

		//其他支出
		listDeptQiTaZhiChuCostSum.add(decimalFormat.format(listDeptQiTaZhiChuAllSum));
		list.add(listDeptQiTaZhiChuCostSum);
		//采购仪器设备付款
		listDeptCaiGouYiQiCostSum.add(decimalFormat.format(listDeptCaiGouYiQiAllSum));
		list.add(listDeptCaiGouYiQiCostSum);
		//工程
		listDeptGongChenCostSum.add(decimalFormat.format(listDeptGongChenAllSum));
		list.add(listDeptGongChenCostSum);
		//仪器设备
		listDeptYiQiSheBeiCostSum.add(decimalFormat.format(listDeptYiQiSheBeiAllSum));
		list.add(listDeptYiQiSheBeiCostSum);
		//装修付款
		listDeptZhuangXiuFuKuanCostSum.add(decimalFormat.format(listDeptZhuangXiuFuKuanAllSum));
		list.add(listDeptZhuangXiuFuKuanCostSum);
		//支出费用小计
		listDeptZhiChuFeiYonCostSum.add(decimalFormat.format(listDeptZhiChuFeiYonAllSum));
		list.add(listDeptZhiChuFeiYonCostSum);
		//空格
		list.add(listDeptNull);

		//现金流量
		listDeptXianJinCostSum.add(decimalFormat.format(listDeptXianJinAllSum));
		list.add(listDeptXianJinCostSum);
		//所得税
		listDeptSuoDeiCostSum.add(decimalFormat.format(listDeptSuoDeiAllSum));
		list.add(listDeptSuoDeiCostSum);
		//现金流量净额
		listDeptJingECostSum.add(decimalFormat.format(listDeptJingEAllSum));
		list.add(listDeptJingECostSum);
		//空格
		list.add(listDeptNull);

		//合作方（应收）结算收入
		listDeptHeZouFangCostSum.add(decimalFormat.format(listDeptHeZouFangAllSum));
		list.add(listDeptHeZouFangCostSum);
		//内部结算收入
		listDeptInsideSum.add(decimalFormat.format(listDeptInsideSumAllCount));
		list.add(listDeptInsideSum);
		//其他费用（含折旧及摊销）
		listDeptOtherSum.add(decimalFormat.format(listDeptOtherAllSum));
		list.add(listDeptOtherSum);
		//合作方案例资质费用
		listDeptAptitudeSum.add(decimalFormat.format(listDeptAptitudeAllSum));
		list.add(listDeptAptitudeSum);
		//内部结算成本
		listDeptCostInsideSum.add(decimalFormat.format(listDeptCostInsideAllSum));
		list.add(listDeptCostInsideSum);
		//空格
		list.add(listDeptNull);

		//运营利润额
		listDeptYunYinCostSum.add(decimalFormat.format(listDeptYunYinAllSum));
		list.add(listDeptYunYinCostSum);
		//所得税
		listDeptSuoDeiShuiCostSum.add(decimalFormat.format(listDeptSuoDeiShuiAllSum));
		list.add(listDeptSuoDeiShuiCostSum);
		//运营净利润额
		listDeptYunYinJingLiRunCostSum.add(decimalFormat.format(listDeptYunYinJingLiRunAllSum));
		list.add(listDeptYunYinJingLiRunCostSum);
		//未实付运营费用
		listDeptWeiShiFuYunYinCostSum.add(decimalFormat.format(listDeptWeiShiFuYunYinAllSum));
		list.add(listDeptWeiShiFuYunYinCostSum);
		//虚拟运营净利润合计//////////
		listDeptXuNiYunYinCostSum.add(decimalFormat.format(listDeptXuNiYunYinAllSum));
		list.add(listDeptXuNiYunYinCostSum);

		String filename = "阿米巴";

		// 导出excel头列表
		Object[] titles = new Object[listDept.size()];
		for(int i = 0 ; i < listDept.size() ; i ++){
			titles[i]=listDept.get(i);
		}
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			List<String> model= list.get(i);
			for(int j = 0 ; j < model.size() ; j ++)
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
		List<String> listDept = new ArrayList<>();
		List<String> listDeptTemp = new ArrayList<String>();
		List<String> listDeptTemp2 = (List<String>) params.get("listDeptTemp");
		String listDeptTempId = (String) params.get("deptnameId");
		String deptname=null;
		listDept.add("");

		List<RdsStatisticsDepartmentModel> listBuMen=rdsFinanceConfigNewMapper.queryXiaJiBuMen(listDeptTempId);
		for (RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel : listBuMen) {
			if(!rdsStatisticsDepartmentModel.isLeaf()){
				listDept.add(rdsStatisticsDepartmentModel.getDeptname());
			}else{
				listDept.add(rdsStatisticsDepartmentModel.getDeptname());
			}
			listDeptTemp.add(rdsStatisticsDepartmentModel.getDeptname());
		}

		if(params.get("listDeptTemp")!=null){

		}else{
			RdsStatisticsDepartmentModel buMen = rdsFinanceConfigNewMapper.queryBuMen(listDeptTempId);
			deptname = buMen.getDeptname();
		}

		//部门服务收入
		List<String> listDeptServiceSum = new ArrayList<>();
		listDeptServiceSum.add("服务收入");
		//部门销售收入
		List<String> listDeptSellSum = new ArrayList<>();
		listDeptSellSum.add("销售收入");
		//合作方（实收）收入
		List<String> listDeptHeZou = new ArrayList<>();
		listDeptHeZou.add("合作方（实收）收入");	 
		//部门内部结算收入
		List<String> listDeptInsideSum = new ArrayList<>();
		listDeptInsideSum.add("内部结算收入");
		//部门收入小计（含税）
		List<String> listDeptInTaxSum = new ArrayList<>();
		listDeptInTaxSum.add("收入小计(含税)");
		//部门收入小计（税）
		List<String> listDeptTaxSum = new ArrayList<>();
		listDeptTaxSum.add("税");
		//部门收入小计（不含税）
		List<String> listDeptOutTaxSum = new ArrayList<>();
		listDeptOutTaxSum.add("收入小计（不含税）");
		//空格
		List<String> listDeptNull = new ArrayList<>();
		listDeptNull.add("");
		//人工成本
		List<String> listDeptWagesSum = new ArrayList<>();
		listDeptWagesSum.add("人工成本");
		//材料成本
		List<String> listDeptMaterialCostSum = new ArrayList<>();
		listDeptMaterialCostSum.add("材料成本");
		//采购材料付款
		List<String> listDeptCaiGouSum = new ArrayList<>();
		listDeptCaiGouSum.add("采购材料付款");
		//耗材
		List<String> listDeptHaoCaiSum = new ArrayList<>();
		listDeptHaoCaiSum.add("耗材");
		//委外费用
		List<String> listDeptWeiWaiCostSum = new ArrayList<>();
		listDeptWeiWaiCostSum.add("委外费用");
		//委外检测成本
		List<String> listDeptExternalCostSum = new ArrayList<>();
		listDeptExternalCostSum.add("委外检测成本");
		//代理费
		List<String> listDeptDaiLiCostSum = new ArrayList<>();
		listDeptDaiLiCostSum.add("代理费");
		//资质费
		List<String> listDeptZiZhiCostSum = new ArrayList<>();
		listDeptZiZhiCostSum.add("资质费");

		//销售管理费用
		List<String> listDeptXiaoShouCostSum = new ArrayList<>();
		listDeptXiaoShouCostSum.add("销售管理费用");
		//办公费
		List<String> listDeptBanGongCostSum = new ArrayList<>();
		listDeptBanGongCostSum.add("办公费");
		//备用金
		List<String> listDeptBeiYonCostSum = new ArrayList<>();
		listDeptBeiYonCostSum.add("备用金");
		//差旅费
		List<String> listDeptChailvCostSum = new ArrayList<>();
		listDeptChailvCostSum.add("差旅费");
		//福利费
		List<String> listDeptFuLiCostSum = new ArrayList<>();
		listDeptFuLiCostSum.add("福利费");
		//广告宣传
		List<String> listDeptGuangGaoCostSum = new ArrayList<>();
		listDeptGuangGaoCostSum.add("广告宣传");
		//业务招待费
		List<String> listDeptYeWuCostSum = new ArrayList<>();
		listDeptYeWuCostSum.add("业务招待费");
		//租赁费
		List<String> listDeptZuPingCostSum = new ArrayList<>();
		listDeptZuPingCostSum.add("租赁费");
		//其他费用
		List<String> listDeptQiTaFeiYonCostSum = new ArrayList<>();
		listDeptQiTaFeiYonCostSum.add("其他费用");
		//其他付款
		List<String> listDeptQiTaFuKuanCostSum = new ArrayList<>();
		listDeptQiTaFuKuanCostSum.add("其他付款");
		//其它采购
		List<String> listDeptQiTaCaiGouCostSum = new ArrayList<>();
		listDeptQiTaCaiGouCostSum.add("其它采购");

		//对外投资
		List<String> listDeptInvestmentSum = new ArrayList<>();
		listDeptInvestmentSum.add("对外投资");

		//其他支出
		List<String> listDeptQiTaZhiChuCostSum = new ArrayList<>();
		listDeptQiTaZhiChuCostSum.add("其他支出");
		//采购仪器设备付款
		List<String> listDeptCaiGouYiQiCostSum = new ArrayList<>();
		listDeptCaiGouYiQiCostSum.add("采购仪器设备付款");
		//工程
		List<String> listDeptGongChenCostSum = new ArrayList<>();
		listDeptGongChenCostSum.add("工程");
		//仪器设备
		List<String> listDeptYiQiSheBeiCostSum = new ArrayList<>();
		listDeptYiQiSheBeiCostSum.add("仪器设备");
		//装修付款
		List<String> listDeptZhuangXiuFuKuanCostSum = new ArrayList<>();
		listDeptZhuangXiuFuKuanCostSum.add("装修付款");
		//支出费用小计
		List<String> listDeptZhiChuFeiYonCostSum = new ArrayList<>();
		listDeptZhiChuFeiYonCostSum.add("支出费用小计");

		//现金流量
		List<String> listDeptXianJinCostSum = new ArrayList<>();
		listDeptXianJinCostSum.add("现金流量");
		//所得税
		List<String> listDeptSuoDeiCostSum = new ArrayList<>();
		listDeptSuoDeiCostSum.add("所得税");
		//现金流量净额
		List<String> listDeptJingECostSum = new ArrayList<>();
		listDeptJingECostSum.add("现金流量净额");

		//合作方（应收）结算收入
		List<String> listDeptHeZouFangCostSum = new ArrayList<>();
		listDeptHeZouFangCostSum.add("合作方（应收）结算收入");

		//运营利润额
		List<String> listDeptYunYinCostSum = new ArrayList<>();
		listDeptYunYinCostSum.add("运营利润额");
		//所得税
		List<String> listDeptSuoDeiShuiCostSum = new ArrayList<>();
		listDeptSuoDeiShuiCostSum.add("所得税");
		//运营净利润额
		List<String> listDeptYunYinJingLiRunCostSum = new ArrayList<>();
		listDeptYunYinJingLiRunCostSum.add("运营净利润额");
		//未实付运营费用
		List<String> listDeptWeiShiFuYunYinCostSum = new ArrayList<>();
		listDeptWeiShiFuYunYinCostSum.add("未实付运营费用");
		//虚拟运营净利润合计
		List<String> listDeptXuNiYunYinCostSum = new ArrayList<>();
		listDeptXuNiYunYinCostSum.add("虚拟运营净利润合计");

		//内部结算成本
		List<String> listDeptCostInsideSum = new ArrayList<>();
		listDeptCostInsideSum.add("内部结算成本");
		//销售管理结算成本
		List<String> listDeptSaleManageSum = new ArrayList<>();
		listDeptSaleManageSum.add("销售管理费用");
		//资质费用
		List<String> listDeptAptitudeSum = new ArrayList<>();
		listDeptAptitudeSum.add("资质费用");
		//其他费用（含折旧及摊销）
		List<String> listDeptOtherSum = new ArrayList<>();
		listDeptOtherSum.add("其他费用（含折旧及摊销）");
		//仪器设备
		List<String> listDeptInstrumentSum = new ArrayList<>();
		listDeptInstrumentSum.add("房屋、装修、仪器及设备采购成本");
		//成本小计
		List<String> listSum = new ArrayList<>();
		listSum.add("成本小计");
		//空格
		List<String> listDeptNull1 = new ArrayList<>();
		listDeptNull1.add("");
		//利润小计
		List<String> listProfit = new ArrayList<>();
		listProfit.add("利润小计");
		//税
		List<String> listTax = new ArrayList<>();
		listTax.add("税");
		//净利润小计///////////////////
		List<String> listNetProfit = new ArrayList<>();
		listNetProfit.add("净利润");
		// 内部结算按照月份计算
		Map<String, Object> map = new HashMap<>();
		map.put("confirm_date_start", params.get("confirm_date_start").toString()
				.substring(0, 7));
		map.put("confirm_date_end", params.get("confirm_date_end").toString()
				.substring(0, 7));
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置

		Double listDeptShouRuBuSumAllCount = 0.0;
		Double listDeptServiceSumAllCount = 0.0;
		Double listDeptSellSumAllCount = 0.0;
		Double listDeptHeZouAllCount = 0.0;
		Double listDeptInsideSumAllCount = 0.0;
		Double ListDeptInTaxAllSum = 0.0;
		Double ListDeptTaxAllSum = 0.0;
		Double ListDeptOutTaxAllSum = 0.0;
		Double ListDeptWagesAllSum = 0.0;
		Double listDeptMaterialCostAllSum=0.0;
		Double listDeptCaiGouAllSum=0.0;
		Double listDeptHaoCaiAllSum=0.0;
		Double listDeptWeiWaiAllSum =0.0;
		Double listDeptExternalAllSum =0.0;
		Double listDeptDaiLiAllSum =0.0;
		Double listDeptZiZhiAllSum =0.0;

		Double listDeptXiaoShouAllSum =0.0;
		Double listDeptBanGongAllSum =0.0;
		Double listDeptBeiYonAllSum =0.0;
		Double listDeptChailvAllSum =0.0;
		Double listDeptFuLiAllSum =0.0;
		Double listDeptGuangGaoAllSum =0.0;
		Double listDeptYeWuAllSum =0.0;
		Double listDeptZuPingAllSum =0.0;
		Double listDeptQiTaFeiYonAllSum =0.0;
		Double listDeptQiTaFuKuanAllSum =0.0;
		Double listDeptQiTaCaiGouAllSum =0.0;

		Double listDeptQiTaZhiChuAllSum =0.0;
		Double listDeptCaiGouYiQiAllSum =0.0;
		Double listDeptGongChenAllSum =0.0;
		Double listDeptYiQiSheBeiAllSum =0.0;
		Double listDeptZhuangXiuFuKuanAllSum =0.0;
		Double listDeptZhiChuFeiYonAllSum =0.0;

		Double listDeptXianJinAllSum=0.0;
		Double listDeptSuoDeiAllSum=0.0;
		Double listDeptJingEAllSum=0.0;

		Double listDeptHeZouFangAllSum=0.0;

		Double listDeptYunYinAllSum=0.0;
		Double listDeptSuoDeiShuiAllSum=0.0;
		Double listDeptYunYinJingLiRunAllSum=0.0;
		Double listDeptWeiShiFuYunYinAllSum=0.0;
		Double listDeptXuNiYunYinAllSum=0.0;

		Double listDeptCostInsideAllSum=0.0;
		Double listDeptSaleManageAllSum=0.0;
		Double listDeptAptitudeAllSum=0.0;
		Double listDeptOtherAllSum=0.0;
		Double listDeptInvestmentAllSum=0.0;
		Double listDeptInstrumentAllSum=0.0;
		Double listSumAllCount=0.0;
		Double listProfitAllCount=0.0;
		Double listTaxAllCount = 0.0;
		Double listNetProfitAllCount=0.0;
		for (String string : listDeptTemp) {
			Double listDeptShouRuBuSum  = 0.0;
			Double listDeptServiceSumCount = 0.0;
			Double listDeptSellSumCount = 0.0;
			Double listDeptHeZouCount = 0.0;
			Double listDeptInsideSumCount = 0.0;
			Double ListDeptInTaxSum = 0.0;
			Double ListDeptTaxSum = 0.0;
			Double ListDeptOutTaxSum = 0.0;
			Double ListDeptWagesSum = 0.0;
			Double ListDeptMaterialSum = 0.0;
			Double ListDeptCaiGouSum = 0.0;
			Double ListDeptHaoCaiSum = 0.0;
			Double listDeptWeiWaiSumCount=0.0;
			Double listDeptExternalSumCount=0.0;
			Double listDeptDaiLiSumCount=0.0;
			Double listDeptZiZhiSumCount=0.0;

			Double listDeptXiaoShouSumCount=0.0;
			Double listDeptBanGongSumCount=0.0;
			Double listDeptBeiYonSumCount=0.0;
			Double listDeptChailvSumCount=0.0;
			Double listDeptFuLiSumCount=0.0;
			Double listDeptGuangGaoSumCount=0.0;
			Double listDeptYeWuSumCount=0.0;
			Double listDeptZuPingSumCount=0.0;
			Double listDeptQiTaFeiYonSumCount=0.0;
			Double listDeptQiTaFuKuanSumCount=0.0;
			Double listDeptQiTaCaiGouSumCount=0.0;

			Double listDeptQiTaZhiChuSumCount=0.0;
			Double listDeptCaiGouYiQiSumCount=0.0;
			Double listDeptGongChenSumCount=0.0;
			Double listDeptYiQiSheBeiSumCount=0.0;
			Double listDeptZhuangXiuFuKuanSumCount=0.0;
			Double listDeptZhiChuFeiYonSumCount=0.0;

			Double listDeptXianJinSumCount=0.0;
			Double listDeptSuoDeiSumCount=0.0;
			Double listDeptJingESumCount=0.0;

			Double listDeptHeZouFangSumCount=0.0;

			Double listDeptYunYinSumCount=0.0;
			Double listDeptSuoDeiShuiSumCount=0.0;
			Double listDeptYunYinJingLiRunSumCount=0.0;
			Double listDeptWeiShiFuYunYinSumCount=0.0;
			Double listDeptXuNiYunYinSumCount=0.0;

			Double listDeptCostInsideSumCount=0.0;
			Double listDeptSaleManageSumCount=0.0;
			Double listDeptAptitudeSumCount=0.0;
			Double listDeptOtherSumCount=0.0;
			Double listDeptInvestmentSumCount=0.0;
			Double listDeptInstrumentSumCount=0.0;
			Double listSumCount=0.0;
			Double listProfitCount=0.0;
			Double listTaxCount = 0.0;
			Double listNetProfitCount=0.0;

			if(listDeptTemp2==null){
				params.put("deptname", deptname);
				map.put("deptname", deptname);
				params.put("deptname1", string);
				map.put("deptname1", string);
			}else if(listDeptTemp2.size()==1){
				params.put("deptname", listDeptTemp2.get(0));
				map.put("deptname", listDeptTemp2.get(0));
				params.put("deptname1", string);
				map.put("deptname1", string);
			}else if(listDeptTemp2.size()==2){
				params.put("deptname", listDeptTemp2.get(0));
				map.put("deptname", listDeptTemp2.get(0));
				params.put("deptname1", listDeptTemp2.get(1));
				map.put("deptname1", listDeptTemp2.get(1));
				params.put("deptname2", string);
				map.put("deptname2", string);
			}else if(listDeptTemp2.size()==3){
				params.put("deptname", listDeptTemp2.get(0));
				map.put("deptname", listDeptTemp2.get(0));
				params.put("deptname1", listDeptTemp2.get(1));
				map.put("deptname1", listDeptTemp2.get(1));
				params.put("deptname2", listDeptTemp2.get(2));
				map.put("deptname2", listDeptTemp2.get(2));
				params.put("deptname3", string);
				map.put("deptname3", string);
			}

			//服务收入  type=1
			List<Map<String, Object>> tempServiceSum=null;
			if((listDeptTemp2==null || listDeptTemp2.size()==1) && "精准医学事业部".equals(string)){
				tempServiceSum = rdsFinanceConfigNewMapper
						.queryDepServiceSum2(params);
			}else{
				tempServiceSum = rdsFinanceConfigNewMapper
						.queryDepServiceSum(params);
			}
			if(tempServiceSum.size()==0)
			{
				listDeptServiceSumCount=0.0;
				listDeptServiceSum.add("0");
			}
			else{
				listDeptServiceSumCount = (Double)(tempServiceSum.get(0)==null?0.0:tempServiceSum.get(0).get("deptServiceSum"));
				listDeptServiceSum.add(decimalFormat.format(listDeptServiceSumCount));
			}
			//销售收入 type=2
			List<Map<String, Object>> tempSellSum = rdsFinanceConfigNewMapper
					.queryDepSellSum(params);
			if(tempSellSum.size()==0){
				listDeptSellSumCount=0.0;
				listDeptSellSum.add("0");
			}
			else{
				listDeptSellSumCount = (Double)(tempSellSum.get(0)==null?0.0:tempSellSum.get(0).get("deptSellSum"));
				listDeptSellSum.add(decimalFormat.format(listDeptSellSumCount));
			}
			//合作方（实收）收入
			List<Map<String, Object>> tempHeZou = rdsFinanceConfigNewMapper
					.queryDepHeZou(params);
			if(tempHeZou.size()==0){
				listDeptHeZouCount=0.0;
				listDeptHeZou.add("0");
			}
			else{
				listDeptHeZouCount = (Double)(tempHeZou.get(0)==null?0.0:tempHeZou.get(0).get("deptHeZou"));
				listDeptHeZou.add(decimalFormat.format(listDeptHeZouCount));
			}
		
			//收入小计（含税）
			ListDeptInTaxSum = listDeptHeZouCount+listDeptSellSumCount+ listDeptServiceSumCount;
			listDeptInTaxSum.add(decimalFormat.format(ListDeptInTaxSum));
			//收入（税）
			ListDeptTaxSum =(listDeptServiceSumCount/1.06*0.06)+(listDeptHeZouCount/1.06*0.06)+(listDeptSellSumCount/1.16*0.16) ;
			listDeptTaxSum.add(decimalFormat.format(ListDeptTaxSum));

			//收入小计（不含税）
			listDeptShouRuBuSum=ListDeptInTaxSum-ListDeptTaxSum;
			listDeptOutTaxSum.add(decimalFormat.format(listDeptShouRuBuSum));

			listDeptNull.add("-");
			listDeptNull1.add("-");
			//人工成本  wags
			List<Map<String, Object>> tempWagesSum=null;
			if((listDeptTemp2==null || listDeptTemp2.size()==1) && "精准医学事业部".equals(string)){
				 tempWagesSum = rdsFinanceConfigNewMapper
						.queryDeptWagesSum2(map);
			}else{
				 tempWagesSum = rdsFinanceConfigNewMapper
						.queryDeptWagesSum(map);
			}
			if(tempWagesSum.size()==0){			
				listDeptWagesSum.add("0");
			}
			else{
				ListDeptWagesSum= (Double)(tempWagesSum.get(0)==null?0.0:tempWagesSum.get(0).get("wagesSum"));
				listDeptWagesSum.add(decimalFormat.format(ListDeptWagesSum));
			}

			//采购材料付款
			params.put("amoeba_program", "采购材料付款");
			List<Map<String, Object>> tempCaiGouSum = rdsFinanceConfigNewMapper
					.queryDeptMaterialCostSum(params);
			if(tempCaiGouSum.size()==0){			
				listDeptCaiGouSum.add("0");
			}
			else{
				ListDeptCaiGouSum= (Double)(tempCaiGouSum.get(0)==null?0.0:tempCaiGouSum.get(0).get("materialSum"));
				listDeptCaiGouSum.add(decimalFormat.format(ListDeptCaiGouSum));
			}

			//耗材
			params.put("amoeba_program", "耗材");
			List<Map<String, Object>> tempHaoCaiSum = rdsFinanceConfigNewMapper
					.queryDeptMaterialCostSum(params);
			if(tempHaoCaiSum.size()==0){			
				listDeptHaoCaiSum.add("0");
			}
			else{
				ListDeptHaoCaiSum= (Double)(tempHaoCaiSum.get(0)==null?0.0:tempHaoCaiSum.get(0).get("materialSum"));
				listDeptHaoCaiSum.add(decimalFormat.format(ListDeptHaoCaiSum));
			}

			//材料成本  
			ListDeptMaterialSum=ListDeptCaiGouSum+ListDeptHaoCaiSum;
			listDeptMaterialCostSum.add(decimalFormat.format(ListDeptMaterialSum));


			//委外检测成本
			params.put("amoeba_program", "委外检测成本");
			listDeptExternalSumCount = findByNameOneE(params, listDeptExternalCostSum, decimalFormat,listDeptExternalSumCount);
			//代理费
			params.put("amoeba_program", "代理费");
			listDeptDaiLiSumCount = findByNameOneE(params, listDeptDaiLiCostSum, decimalFormat, listDeptDaiLiSumCount);
			//资质费
			params.put("amoeba_program", "资质费");
			listDeptZiZhiSumCount = findByNameOneE(params, listDeptZiZhiCostSum, decimalFormat, listDeptZiZhiSumCount);

			//办公费
			params.put("amoeba_program", "办公费");
			listDeptBanGongSumCount = findByNameOneE(params, listDeptBanGongCostSum, decimalFormat,listDeptBanGongSumCount);
			//备用金
			listDeptBeiYonSumCount = findByNameE(params, listDeptBeiYonCostSum, decimalFormat, listDeptBeiYonSumCount);
			//差旅费
			params.put("amoeba_program", "差旅费");
			listDeptChailvSumCount = findByNameOneE(params, listDeptChailvCostSum, decimalFormat,listDeptChailvSumCount);
			//福利费
			params.put("amoeba_program", "福利费");
			listDeptFuLiSumCount = findByNameOneE(params, listDeptFuLiCostSum, decimalFormat,listDeptFuLiSumCount);
			//广告宣传
			params.put("amoeba_program", "广告宣传");
			listDeptGuangGaoSumCount = findByNameOneE(params, listDeptGuangGaoCostSum, decimalFormat,listDeptGuangGaoSumCount);
			//业务招待费
			params.put("amoeba_program", "业务招待费");
			listDeptYeWuSumCount = findByNameOneE(params, listDeptYeWuCostSum, decimalFormat,listDeptYeWuSumCount);
			//租赁费
			params.put("amoeba_program", "租赁费");
			listDeptZuPingSumCount = findByNameOneE(params, listDeptZuPingCostSum, decimalFormat,listDeptZuPingSumCount);
			//其他费用
			params.put("amoeba_program", "其他费用");
			listDeptQiTaFeiYonSumCount = findByNameOneE(params, listDeptQiTaFeiYonCostSum, decimalFormat,listDeptQiTaFeiYonSumCount);
			//其他付款
			params.put("amoeba_program", "其他付款");
			listDeptQiTaFuKuanSumCount = findByNameOneE(params, listDeptQiTaFuKuanCostSum, decimalFormat,listDeptQiTaFuKuanSumCount);
			//其它采购
			params.put("amoeba_program", "其它采购");
			listDeptQiTaCaiGouSumCount = findByNameOneE(params, listDeptQiTaCaiGouCostSum, decimalFormat,listDeptQiTaCaiGouSumCount);
			//销售管理费用
			listDeptXiaoShouSumCount=listDeptBanGongSumCount+listDeptBeiYonSumCount
					+listDeptChailvSumCount+listDeptFuLiSumCount+listDeptGuangGaoSumCount+listDeptYeWuSumCount+listDeptZuPingSumCount
					+listDeptQiTaFeiYonSumCount+listDeptQiTaFuKuanSumCount+listDeptQiTaCaiGouSumCount;
			listDeptXiaoShouCostSum.add(decimalFormat.format(listDeptXiaoShouSumCount));

			//采购仪器设备付款
			params.put("amoeba_program", "采购仪器设备付款");
			listDeptCaiGouYiQiSumCount = findByNameOneE(params, listDeptCaiGouYiQiCostSum, decimalFormat,listDeptCaiGouYiQiSumCount);
			//工程
			params.put("amoeba_program", "工程");
			listDeptGongChenSumCount = findByNameOneE(params, listDeptGongChenCostSum, decimalFormat,listDeptGongChenSumCount);
			//仪器设备
			params.put("amoeba_program", "仪器设备");
			listDeptYiQiSheBeiSumCount = findByNameOneE(params, listDeptYiQiSheBeiCostSum, decimalFormat,listDeptYiQiSheBeiSumCount);
			//装修付款
			params.put("amoeba_program", "装修付款");
			listDeptZhuangXiuFuKuanSumCount = findByNameOneE(params, listDeptZhuangXiuFuKuanCostSum, decimalFormat,listDeptZhuangXiuFuKuanSumCount);
			//对外投资
			map.put("amoeba_program", "对外投资");
			List<Map<String, Object>> tempInvestmentCostSum = rdsFinanceConfigNewMapper
					.queryDeptDepreciationCostSum(map);
			if(tempInvestmentCostSum.size()==0){			
				listDeptInvestmentSum.add("0");
			}
			else{
				listDeptInvestmentSumCount= (Double)(tempInvestmentCostSum.get(0)==null?0.0:tempInvestmentCostSum.get(0).get("depreciationSum"));
				listDeptInvestmentSum.add(decimalFormat.format(listDeptInvestmentSumCount));
			}
			//其他支出
			listDeptQiTaZhiChuSumCount= listDeptCaiGouYiQiSumCount+listDeptGongChenSumCount
					+listDeptYiQiSheBeiSumCount+listDeptZhuangXiuFuKuanSumCount;
			listDeptQiTaZhiChuCostSum.add(decimalFormat.format(listDeptQiTaZhiChuSumCount));


			//合作方（应收）结算收入
			params.put("amoeba_program", "合作方收入");
			List<Map<String, Object>> tempHeZouFangSum = rdsFinanceConfigNewMapper
					.queryDeptHeZouFangCostSum(params);
			if(tempHeZouFangSum.size()==0){			
				listDeptHeZouFangCostSum.add("0");
			}
			else{
				listDeptHeZouFangSumCount= (Double)(tempHeZouFangSum.get(0)==null?0.0:tempHeZouFangSum.get(0).get("HeZouFangSum"));
				listDeptHeZouFangCostSum.add(decimalFormat.format(listDeptHeZouFangSumCount));
			}

			//内部结算收入
			List<Map<String, Object>> tempInsideSum =null;
			if("技术与服务管理部".equals(string)){
				tempInsideSum = rdsFinanceConfigNewMapper
						.queryDeptInsideSum1(params);
			}else if("精准医学事业部".equals(string)){
				 tempInsideSum = rdsFinanceConfigNewMapper
						.queryDeptInsideSum2(params);
			} 
			if(tempInsideSum==null){
				listDeptInsideSumCount=0.0;				
				listDeptInsideSum.add("0");
			}
			else{
				listDeptInsideSumCount = (Double)(tempInsideSum.get(0)==null?0.0:tempInsideSum.get(0).get("deptInsideSum"));
				listDeptInsideSum.add(decimalFormat.format(listDeptInsideSumCount));
			}
			
			//其他费用（含折旧及摊销）
			map.put("amoeba_program", "其他费用（含折旧及摊销）");
			List<Map<String, Object>> tempOtherCostSum = rdsFinanceConfigNewMapper
					.queryDeptDepreciationCostSum(map);
			if(tempOtherCostSum.size()==0){			
				listDeptOtherSum.add("0");
			}
			else{
				listDeptOtherSumCount= (Double)(tempOtherCostSum.get(0)==null?0.0:tempOtherCostSum.get(0).get("depreciationSum"));
				listDeptOtherSum.add(decimalFormat.format(listDeptOtherSumCount));
			}
			//资质费用
			List<Map<String, Object>> tempDeptAptitudeSum=null;
			if("技术与服务管理部".equals(string)){
				tempDeptAptitudeSum = rdsFinanceConfigNewMapper
						.queryDeptAptitudeCostSum(params);
			} 
			if(tempDeptAptitudeSum==null){		
				listDeptAptitudeSum.add("0");
			}
			else{
				listDeptAptitudeSumCount = (Double)(tempDeptAptitudeSum.get(0)==null?0.0:tempDeptAptitudeSum.get(0).get("deptAptitudeCostSum"));
				listDeptAptitudeSum.add(decimalFormat.format(listDeptAptitudeSumCount));
			}
			//内部结算成本
			List<Map<String, Object>> tempCostInsideSum=null;
			if((listDeptTemp2==null || listDeptTemp2.size()==1) && "精准医学事业部".equals(string)){
				tempCostInsideSum = rdsFinanceConfigNewMapper
						.queryCostInsideSum2(params);
			}else{
				tempCostInsideSum = rdsFinanceConfigNewMapper
						.queryCostInsideSum(params);
			}
			if(tempCostInsideSum.size()==0){			
				listDeptCostInsideSum.add("0");
			}
			else{
				listDeptCostInsideSumCount = (Double)(tempCostInsideSum.get(0)==null?0.0:tempCostInsideSum.get(0).get("costInsideSum"));
				listDeptCostInsideSum.add(decimalFormat.format(listDeptCostInsideSumCount));
			}

			//委外费用 
			listDeptWeiWaiSumCount=listDeptExternalSumCount+listDeptDaiLiSumCount+listDeptZiZhiSumCount;
			listDeptWeiWaiCostSum.add(decimalFormat.format(listDeptWeiWaiSumCount));
			
			//销售管理费用
			List<Map<String, Object>> tempSaleManageSum = rdsFinanceConfigNewMapper
					.querySaleManageSum(params);
			if(tempSaleManageSum.size()==0){			
				listDeptSaleManageSum.add("0");
			}
			else{
				listDeptSaleManageSumCount = (Double)(tempSaleManageSum.get(0)==null?0.0:tempSaleManageSum.get(0).get("deptSaleManageSum"));
				listDeptSaleManageSum.add(decimalFormat.format(listDeptSaleManageSumCount));
			}

			//仪器设备
			List<Map<String, Object>> tempInstrumentCostSum = rdsFinanceConfigNewMapper
					.queryDeptInstrumentCostSum(params);
			if(tempInstrumentCostSum.size()==0){			
				listDeptInstrumentSum.add("0");
			}
			else{
				listDeptInstrumentSumCount= (Double)(tempInstrumentCostSum.get(0)==null?0.0:tempInstrumentCostSum.get(0).get("instrumentSum"));
				listDeptInstrumentSum.add(decimalFormat.format(listDeptInstrumentSumCount));
			}
			
			//支出费用小计
			listDeptZhiChuFeiYonSumCount=ListDeptWagesSum+ListDeptMaterialSum
					+listDeptWeiWaiSumCount+listDeptXiaoShouSumCount+listDeptQiTaZhiChuSumCount;
			listDeptZhiChuFeiYonCostSum.add(decimalFormat.format(listDeptZhiChuFeiYonSumCount));
			
			
			//现金流量
			listDeptXianJinSumCount=ListDeptInTaxSum-ListDeptTaxSum - listDeptZhiChuFeiYonSumCount;
			listDeptXianJinCostSum.add(decimalFormat.format(listDeptXianJinSumCount));
			//所得税
			listDeptSuoDeiSumCount=listDeptXianJinSumCount * 0.2 ;
			listDeptSuoDeiCostSum.add(decimalFormat.format(listDeptSuoDeiSumCount));
			//现金流量净额
			listDeptJingESumCount= listDeptXianJinSumCount - listDeptSuoDeiSumCount;
			listDeptJingECostSum.add(decimalFormat.format(listDeptJingESumCount));
			
			//运营利润额
			listDeptYunYinSumCount= listDeptXianJinSumCount+listDeptInsideSumCount-listDeptCostInsideSumCount
					+listDeptInvestmentSumCount+listDeptQiTaZhiChuSumCount-listDeptOtherSumCount;
			listDeptYunYinCostSum.add(decimalFormat.format(listDeptYunYinSumCount));
			//所得税
			listDeptSuoDeiShuiSumCount=listDeptYunYinSumCount*0.2;
			listDeptSuoDeiShuiCostSum.add(decimalFormat.format(listDeptSuoDeiShuiSumCount));
			//运营净利润额
			listDeptYunYinJingLiRunSumCount=listDeptYunYinSumCount - listDeptSuoDeiShuiSumCount;
			listDeptYunYinJingLiRunCostSum.add(decimalFormat.format(listDeptYunYinJingLiRunSumCount));
			//未实付运营费用
			Double a=listDeptAptitudeSumCount == 0.0?0.0:listDeptAptitudeSumCount-listDeptZiZhiSumCount;
			listDeptWeiShiFuYunYinSumCount=a - listDeptHeZouFangSumCount;
			listDeptWeiShiFuYunYinCostSum.add(decimalFormat.format(listDeptWeiShiFuYunYinSumCount));
			//虚拟运营净利润合计
			listDeptXuNiYunYinSumCount=listDeptYunYinJingLiRunSumCount - listDeptWeiShiFuYunYinSumCount;
			listDeptXuNiYunYinCostSum.add(decimalFormat.format(listDeptXuNiYunYinSumCount));

			listDeptShouRuBuSumAllCount+=listDeptShouRuBuSum;
			listDeptServiceSumAllCount+=listDeptServiceSumCount;
			listDeptSellSumAllCount+=listDeptSellSumCount;
			listDeptHeZouAllCount+=listDeptHeZouCount;
			listDeptInsideSumAllCount+=listDeptInsideSumCount;
			ListDeptInTaxAllSum+=ListDeptInTaxSum;
			ListDeptTaxAllSum+=ListDeptTaxSum;
			ListDeptOutTaxAllSum+=ListDeptOutTaxSum;
			ListDeptWagesAllSum+=ListDeptWagesSum;
			listDeptWeiWaiAllSum+=listDeptWeiWaiSumCount;
			listDeptMaterialCostAllSum+=ListDeptMaterialSum;
			listDeptCaiGouAllSum+=ListDeptCaiGouSum;
			listDeptHaoCaiAllSum+=ListDeptHaoCaiSum;
			listDeptExternalAllSum+=listDeptExternalSumCount;
			listDeptDaiLiAllSum+=listDeptDaiLiSumCount;
			listDeptZiZhiAllSum+=listDeptZiZhiSumCount;

			listDeptXiaoShouAllSum+=listDeptXiaoShouSumCount;
			listDeptBanGongAllSum+=listDeptBanGongSumCount;
			listDeptBeiYonAllSum+=listDeptBeiYonSumCount;
			listDeptChailvAllSum+=listDeptChailvSumCount;
			listDeptFuLiAllSum+=listDeptFuLiSumCount;
			listDeptGuangGaoAllSum+=listDeptGuangGaoSumCount;
			listDeptYeWuAllSum+=listDeptYeWuSumCount;
			listDeptZuPingAllSum+=listDeptZuPingSumCount;
			listDeptQiTaFeiYonAllSum+=listDeptQiTaFeiYonSumCount;
			listDeptQiTaFuKuanAllSum+=listDeptQiTaFuKuanSumCount;
			listDeptQiTaCaiGouAllSum+=listDeptQiTaCaiGouSumCount;

			listDeptXianJinAllSum+=listDeptXianJinSumCount;
			listDeptSuoDeiAllSum+=listDeptSuoDeiSumCount;
			listDeptJingEAllSum+=listDeptJingESumCount;

			listDeptHeZouFangAllSum+=listDeptHeZouFangSumCount;

			listDeptYunYinAllSum+=listDeptYunYinSumCount;
			listDeptSuoDeiShuiAllSum+=listDeptSuoDeiShuiSumCount;
			listDeptYunYinJingLiRunAllSum+=listDeptYunYinJingLiRunSumCount;
			listDeptWeiShiFuYunYinAllSum+=listDeptWeiShiFuYunYinSumCount;
			listDeptXuNiYunYinAllSum+=listDeptXuNiYunYinSumCount;

			listDeptQiTaZhiChuAllSum+=listDeptQiTaZhiChuSumCount;
			listDeptCaiGouYiQiAllSum+=listDeptCaiGouYiQiSumCount;
			listDeptGongChenAllSum+=listDeptGongChenSumCount;
			listDeptYiQiSheBeiAllSum+=listDeptYiQiSheBeiSumCount;
			listDeptZhuangXiuFuKuanAllSum+=listDeptZhuangXiuFuKuanSumCount;
			listDeptZhiChuFeiYonAllSum+=listDeptZhiChuFeiYonSumCount;

			listDeptCostInsideAllSum+=listDeptCostInsideSumCount;
			listDeptSaleManageAllSum+=listDeptSaleManageSumCount;
			listDeptAptitudeAllSum+=listDeptAptitudeSumCount;
			listDeptOtherAllSum+=listDeptOtherSumCount;
			listDeptInvestmentAllSum+=listDeptInvestmentSumCount;
			listDeptInstrumentAllSum+=listDeptInstrumentSumCount;

			//成本小计
			listSumCount=ListDeptWagesSum+ListDeptMaterialSum+listDeptExternalSumCount+listDeptCostInsideSumCount+listDeptSaleManageSumCount+listDeptAptitudeSumCount+listDeptOtherSumCount;
			listSum.add(decimalFormat.format(listSumCount));
			listProfitCount = ListDeptOutTaxSum+listDeptInsideSumCount-listSumCount;
			listProfit.add(decimalFormat.format(listProfitCount));
			listTaxCount = listProfitCount*0.2;
			listTax.add(decimalFormat.format(listTaxCount));
			listNetProfitCount=listProfitCount-listTaxCount;
			listNetProfit.add(decimalFormat.format(listNetProfitCount));

			listSumAllCount+=listSumCount;
			listProfitAllCount+=listProfitCount;
			listTaxAllCount+=listTaxCount;
			listNetProfitAllCount+=listNetProfitCount;

		}

		listDept.add("合计");
		//服务收入
		listDeptServiceSum.add(decimalFormat.format(listDeptServiceSumAllCount));
		list.add(listDeptServiceSum);
		//销售收入
		listDeptSellSum.add(decimalFormat.format(listDeptSellSumAllCount));
		list.add(listDeptSellSum);
		//合作方（实收）收入
		listDeptHeZou.add(decimalFormat.format(listDeptHeZouAllCount));
		list.add(listDeptHeZou);

		//收入小计（含税）
		listDeptInTaxSum.add(decimalFormat.format(ListDeptInTaxAllSum));
		list.add(listDeptInTaxSum);
		//收入小计（税）
		listDeptTaxSum.add(decimalFormat.format(ListDeptTaxAllSum));
		list.add(listDeptTaxSum);
		//收入小计（不含税）
		listDeptOutTaxSum.add(decimalFormat.format(listDeptShouRuBuSumAllCount));
		list.add(listDeptOutTaxSum);
		//空格
		listDeptNull.add("-");
		list.add(listDeptNull);
		//人工成本
		listDeptWagesSum.add(decimalFormat.format(ListDeptWagesAllSum));
		list.add(listDeptWagesSum);
		//材料成本
		listDeptMaterialCostSum.add(decimalFormat.format(listDeptMaterialCostAllSum));
		list.add(listDeptMaterialCostSum);
		//采购材料付款
		listDeptCaiGouSum.add(decimalFormat.format(listDeptCaiGouAllSum));
		list.add(listDeptCaiGouSum);
		//耗材
		listDeptHaoCaiSum.add(decimalFormat.format(listDeptHaoCaiAllSum));
		list.add(listDeptHaoCaiSum);
		//委外费用
		listDeptWeiWaiCostSum.add(decimalFormat.format(listDeptWeiWaiAllSum));
		list.add(listDeptWeiWaiCostSum);
		//委外检测成本
		listDeptExternalCostSum.add(decimalFormat.format(listDeptExternalAllSum));
		list.add(listDeptExternalCostSum);
		//代理费
		listDeptDaiLiCostSum.add(decimalFormat.format(listDeptDaiLiAllSum));
		list.add(listDeptDaiLiCostSum);
		//资质费
		listDeptZiZhiCostSum.add(decimalFormat.format(listDeptZiZhiAllSum));
		list.add(listDeptZiZhiCostSum);
		//销售管理费用
		listDeptXiaoShouCostSum.add(decimalFormat.format(listDeptXiaoShouAllSum));
		list.add(listDeptXiaoShouCostSum);
		//办公费
		listDeptBanGongCostSum.add(decimalFormat.format(listDeptBanGongAllSum));
		list.add(listDeptBanGongCostSum);
		//备用金
		listDeptBeiYonCostSum.add(decimalFormat.format(listDeptBeiYonAllSum));
		list.add(listDeptBeiYonCostSum);
		//差旅费
		listDeptChailvCostSum.add(decimalFormat.format(listDeptChailvAllSum));
		list.add(listDeptChailvCostSum);
		//福利费
		listDeptFuLiCostSum.add(decimalFormat.format(listDeptFuLiAllSum));
		list.add(listDeptFuLiCostSum);
		//广告宣传
		listDeptGuangGaoCostSum.add(decimalFormat.format(listDeptGuangGaoAllSum));
		list.add(listDeptGuangGaoCostSum);
		//业务招待费
		listDeptYeWuCostSum.add(decimalFormat.format(listDeptYeWuAllSum));
		list.add(listDeptYeWuCostSum);
		//租赁费
		listDeptZuPingCostSum.add(decimalFormat.format(listDeptZuPingAllSum));
		list.add(listDeptZuPingCostSum);
		//其他费用
		listDeptQiTaFeiYonCostSum.add(decimalFormat.format(listDeptQiTaFeiYonAllSum));
		list.add(listDeptQiTaFeiYonCostSum);
		//其他付款
		listDeptQiTaFuKuanCostSum.add(decimalFormat.format(listDeptQiTaFuKuanAllSum));
		list.add(listDeptQiTaFuKuanCostSum);
		//其它采购
		listDeptQiTaCaiGouCostSum.add(decimalFormat.format(listDeptQiTaCaiGouAllSum));
		list.add(listDeptQiTaCaiGouCostSum);

		//对外投资
		listDeptInvestmentSum.add(decimalFormat.format(listDeptInvestmentAllSum));
		list.add(listDeptInvestmentSum);

		//其他支出
		listDeptQiTaZhiChuCostSum.add(decimalFormat.format(listDeptQiTaZhiChuAllSum));
		list.add(listDeptQiTaZhiChuCostSum);
		//采购仪器设备付款
		listDeptCaiGouYiQiCostSum.add(decimalFormat.format(listDeptCaiGouYiQiAllSum));
		list.add(listDeptCaiGouYiQiCostSum);
		//工程
		listDeptGongChenCostSum.add(decimalFormat.format(listDeptGongChenAllSum));
		list.add(listDeptGongChenCostSum);
		//仪器设备
		listDeptYiQiSheBeiCostSum.add(decimalFormat.format(listDeptYiQiSheBeiAllSum));
		list.add(listDeptYiQiSheBeiCostSum);
		//装修付款
		listDeptZhuangXiuFuKuanCostSum.add(decimalFormat.format(listDeptZhuangXiuFuKuanAllSum));
		list.add(listDeptZhuangXiuFuKuanCostSum);
		//支出费用小计
		listDeptZhiChuFeiYonCostSum.add(decimalFormat.format(listDeptZhiChuFeiYonAllSum));
		list.add(listDeptZhiChuFeiYonCostSum);
		//空格
		list.add(listDeptNull);

		//现金流量
		listDeptXianJinCostSum.add(decimalFormat.format(listDeptXianJinAllSum));
		list.add(listDeptXianJinCostSum);
		//所得税
		listDeptSuoDeiCostSum.add(decimalFormat.format(listDeptSuoDeiAllSum));
		list.add(listDeptSuoDeiCostSum);
		//现金流量净额
		listDeptJingECostSum.add(decimalFormat.format(listDeptJingEAllSum));
		list.add(listDeptJingECostSum);
		//空格
		list.add(listDeptNull);

		//合作方（应收）结算收入
		listDeptHeZouFangCostSum.add(decimalFormat.format(listDeptHeZouFangAllSum));
		list.add(listDeptHeZouFangCostSum);
		//内部结算收入
		listDeptInsideSum.add(decimalFormat.format(listDeptInsideSumAllCount));
		list.add(listDeptInsideSum);
		//其他费用（含折旧及摊销）
		listDeptOtherSum.add(decimalFormat.format(listDeptOtherAllSum));
		list.add(listDeptOtherSum);
		//合作方案例资质费用
		listDeptAptitudeSum.add(decimalFormat.format(listDeptAptitudeAllSum));
		list.add(listDeptAptitudeSum);
		//内部结算成本
		listDeptCostInsideSum.add(decimalFormat.format(listDeptCostInsideAllSum));
		list.add(listDeptCostInsideSum);
		//空格
		list.add(listDeptNull);

		//运营利润额
		listDeptYunYinCostSum.add(decimalFormat.format(listDeptYunYinAllSum));
		list.add(listDeptYunYinCostSum);
		//所得税
		listDeptSuoDeiShuiCostSum.add(decimalFormat.format(listDeptSuoDeiShuiAllSum));
		list.add(listDeptSuoDeiShuiCostSum);
		//运营净利润额
		listDeptYunYinJingLiRunCostSum.add(decimalFormat.format(listDeptYunYinJingLiRunAllSum));
		list.add(listDeptYunYinJingLiRunCostSum);
		//未实付运营费用
		listDeptWeiShiFuYunYinCostSum.add(decimalFormat.format(listDeptWeiShiFuYunYinAllSum));
		list.add(listDeptWeiShiFuYunYinCostSum);
		//虚拟运营净利润合计//////////
		listDeptXuNiYunYinCostSum.add(decimalFormat.format(listDeptXuNiYunYinAllSum));
		list.add(listDeptXuNiYunYinCostSum);

		String filename = "阿米巴";

		// 导出excel头列表
		Object[] titles = new Object[listDept.size()];
		for(int i = 0 ; i < listDept.size() ; i ++){
			titles[i]=listDept.get(i);
		}
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			List<String> model= list.get(i);
			for(int j = 0 ; j < model.size() ; j ++)
				objects.add(model.get(j));
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "阿米巴");
	}
	@SuppressWarnings("unchecked")
	@Override
	public void exportAmoebaThree(Map<String, Object> params,
			HttpServletResponse response) {
		List<List<String>> list = new ArrayList<>();
		List<String> listDept = new ArrayList<>();
		List<String> listDeptTemp = new ArrayList<String>();
		List<String> listDeptTemp2 = (List<String>) params.get("listDeptTemp");
		String listDeptTempId = (String) params.get("deptnameId");
		String deptname=null;
		String deptname1=null;
		listDept.add("");

		List<RdsStatisticsDepartmentModel> listBuMen=rdsFinanceConfigNewMapper.queryXiaJiBuMen(listDeptTempId);
		for (RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel : listBuMen) {
			if(!rdsStatisticsDepartmentModel.isLeaf()){
				listDept.add(rdsStatisticsDepartmentModel.getDeptname() );
			}else{
				listDept.add(rdsStatisticsDepartmentModel.getDeptname());
			}
			listDeptTemp.add(rdsStatisticsDepartmentModel.getDeptname());
		}

		if(params.get("listDeptTemp")!=null){
			RdsStatisticsDepartmentModel buMen = rdsFinanceConfigNewMapper.queryBuMen(listDeptTempId);
			RdsStatisticsDepartmentModel buMen2 = rdsFinanceConfigNewMapper.queryBuMen(listDeptTempId);
			deptname1 = buMen.getDeptname();
			deptname= buMen2.getDeptname();
		}else{
			RdsStatisticsDepartmentModel buMen = rdsFinanceConfigNewMapper.queryBuMen(listDeptTempId);
			RdsStatisticsDepartmentModel buMen2 = rdsFinanceConfigNewMapper.queryBuMen2(listDeptTempId);
			deptname1 = buMen.getDeptname();
			deptname= buMen2.getDeptname();
		}

		//部门服务收入
		List<String> listDeptServiceSum = new ArrayList<>();
		listDeptServiceSum.add("服务收入");
		//部门销售收入
		List<String> listDeptSellSum = new ArrayList<>();
		listDeptSellSum.add("销售收入");
		//合作方（实收）收入
		List<String> listDeptHeZou = new ArrayList<>();
		listDeptHeZou.add("合作方（实收）收入");	 
		//部门内部结算收入
		List<String> listDeptInsideSum = new ArrayList<>();
		listDeptInsideSum.add("内部结算收入");
		//部门收入小计（含税）
		List<String> listDeptInTaxSum = new ArrayList<>();
		listDeptInTaxSum.add("收入小计(含税)");
		//部门收入小计（税）
		List<String> listDeptTaxSum = new ArrayList<>();
		listDeptTaxSum.add("税");
		//部门收入小计（不含税）
		List<String> listDeptOutTaxSum = new ArrayList<>();
		listDeptOutTaxSum.add("收入小计（不含税）");
		//空格
		List<String> listDeptNull = new ArrayList<>();
		listDeptNull.add("");
		//人工成本
		List<String> listDeptWagesSum = new ArrayList<>();
		listDeptWagesSum.add("人工成本");
		//材料成本
		List<String> listDeptMaterialCostSum = new ArrayList<>();
		listDeptMaterialCostSum.add("材料成本");
		//采购材料付款
		List<String> listDeptCaiGouSum = new ArrayList<>();
		listDeptCaiGouSum.add("采购材料付款");
		//耗材
		List<String> listDeptHaoCaiSum = new ArrayList<>();
		listDeptHaoCaiSum.add("耗材");
		//委外费用
		List<String> listDeptWeiWaiCostSum = new ArrayList<>();
		listDeptWeiWaiCostSum.add("委外费用");
		//委外检测成本
		List<String> listDeptExternalCostSum = new ArrayList<>();
		listDeptExternalCostSum.add("委外检测成本");
		//代理费
		List<String> listDeptDaiLiCostSum = new ArrayList<>();
		listDeptDaiLiCostSum.add("代理费");
		//资质费
		List<String> listDeptZiZhiCostSum = new ArrayList<>();
		listDeptZiZhiCostSum.add("资质费");

		//销售管理费用
		List<String> listDeptXiaoShouCostSum = new ArrayList<>();
		listDeptXiaoShouCostSum.add("销售管理费用");
		//办公费
		List<String> listDeptBanGongCostSum = new ArrayList<>();
		listDeptBanGongCostSum.add("办公费");
		//备用金
		List<String> listDeptBeiYonCostSum = new ArrayList<>();
		listDeptBeiYonCostSum.add("备用金");
		//差旅费
		List<String> listDeptChailvCostSum = new ArrayList<>();
		listDeptChailvCostSum.add("差旅费");
		//福利费
		List<String> listDeptFuLiCostSum = new ArrayList<>();
		listDeptFuLiCostSum.add("福利费");
		//广告宣传
		List<String> listDeptGuangGaoCostSum = new ArrayList<>();
		listDeptGuangGaoCostSum.add("广告宣传");
		//业务招待费
		List<String> listDeptYeWuCostSum = new ArrayList<>();
		listDeptYeWuCostSum.add("业务招待费");
		//租赁费
		List<String> listDeptZuPingCostSum = new ArrayList<>();
		listDeptZuPingCostSum.add("租赁费");
		//其他费用
		List<String> listDeptQiTaFeiYonCostSum = new ArrayList<>();
		listDeptQiTaFeiYonCostSum.add("其他费用");
		//其他付款
		List<String> listDeptQiTaFuKuanCostSum = new ArrayList<>();
		listDeptQiTaFuKuanCostSum.add("其他付款");
		//其它采购
		List<String> listDeptQiTaCaiGouCostSum = new ArrayList<>();
		listDeptQiTaCaiGouCostSum.add("其它采购");

		//对外投资
		List<String> listDeptInvestmentSum = new ArrayList<>();
		listDeptInvestmentSum.add("对外投资");

		//其他支出
		List<String> listDeptQiTaZhiChuCostSum = new ArrayList<>();
		listDeptQiTaZhiChuCostSum.add("其他支出");
		//采购仪器设备付款
		List<String> listDeptCaiGouYiQiCostSum = new ArrayList<>();
		listDeptCaiGouYiQiCostSum.add("采购仪器设备付款");
		//工程
		List<String> listDeptGongChenCostSum = new ArrayList<>();
		listDeptGongChenCostSum.add("工程");
		//仪器设备
		List<String> listDeptYiQiSheBeiCostSum = new ArrayList<>();
		listDeptYiQiSheBeiCostSum.add("仪器设备");
		//装修付款
		List<String> listDeptZhuangXiuFuKuanCostSum = new ArrayList<>();
		listDeptZhuangXiuFuKuanCostSum.add("装修付款");
		//支出费用小计
		List<String> listDeptZhiChuFeiYonCostSum = new ArrayList<>();
		listDeptZhiChuFeiYonCostSum.add("支出费用小计");

		//现金流量
		List<String> listDeptXianJinCostSum = new ArrayList<>();
		listDeptXianJinCostSum.add("现金流量");
		//所得税
		List<String> listDeptSuoDeiCostSum = new ArrayList<>();
		listDeptSuoDeiCostSum.add("所得税");
		//现金流量净额
		List<String> listDeptJingECostSum = new ArrayList<>();
		listDeptJingECostSum.add("现金流量净额");

		//合作方（应收）结算收入
		List<String> listDeptHeZouFangCostSum = new ArrayList<>();
		listDeptHeZouFangCostSum.add("合作方（应收）结算收入");

		//运营利润额
		List<String> listDeptYunYinCostSum = new ArrayList<>();
		listDeptYunYinCostSum.add("运营利润额");
		//所得税
		List<String> listDeptSuoDeiShuiCostSum = new ArrayList<>();
		listDeptSuoDeiShuiCostSum.add("所得税");
		//运营净利润额
		List<String> listDeptYunYinJingLiRunCostSum = new ArrayList<>();
		listDeptYunYinJingLiRunCostSum.add("运营净利润额");
		//未实付运营费用
		List<String> listDeptWeiShiFuYunYinCostSum = new ArrayList<>();
		listDeptWeiShiFuYunYinCostSum.add("未实付运营费用");
		//虚拟运营净利润合计
		List<String> listDeptXuNiYunYinCostSum = new ArrayList<>();
		listDeptXuNiYunYinCostSum.add("虚拟运营净利润合计");

		//内部结算成本
		List<String> listDeptCostInsideSum = new ArrayList<>();
		listDeptCostInsideSum.add("内部结算成本");
		//销售管理结算成本
		List<String> listDeptSaleManageSum = new ArrayList<>();
		listDeptSaleManageSum.add("销售管理费用");
		//资质费用
		List<String> listDeptAptitudeSum = new ArrayList<>();
		listDeptAptitudeSum.add("资质费用");
		//其他费用（含折旧及摊销）
		List<String> listDeptOtherSum = new ArrayList<>();
		listDeptOtherSum.add("其他费用（含折旧及摊销）");
		//仪器设备
		List<String> listDeptInstrumentSum = new ArrayList<>();
		listDeptInstrumentSum.add("房屋、装修、仪器及设备采购成本");
		//成本小计
		List<String> listSum = new ArrayList<>();
		listSum.add("成本小计");
		//空格
		List<String> listDeptNull1 = new ArrayList<>();
		listDeptNull1.add("");
		//利润小计
		List<String> listProfit = new ArrayList<>();
		listProfit.add("利润小计");
		//税
		List<String> listTax = new ArrayList<>();
		listTax.add("税");
		//净利润小计///////////////////
		List<String> listNetProfit = new ArrayList<>();
		listNetProfit.add("净利润");
		// 内部结算按照月份计算
		Map<String, Object> map = new HashMap<>();
		map.put("confirm_date_start", params.get("confirm_date_start").toString()
				.substring(0, 7));
		map.put("confirm_date_end", params.get("confirm_date_end").toString()
				.substring(0, 7));
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置

		Double listDeptShouRuBuSumAllCount = 0.0;
		Double listDeptServiceSumAllCount = 0.0;
		Double listDeptSellSumAllCount = 0.0;
		Double listDeptHeZouAllCount = 0.0;
		Double listDeptInsideSumAllCount = 0.0;
		Double ListDeptInTaxAllSum = 0.0;
		Double ListDeptTaxAllSum = 0.0;
		Double ListDeptOutTaxAllSum = 0.0;
		Double ListDeptWagesAllSum = 0.0;
		Double listDeptMaterialCostAllSum=0.0;
		Double listDeptCaiGouAllSum=0.0;
		Double listDeptHaoCaiAllSum=0.0;
		Double listDeptWeiWaiAllSum =0.0;
		Double listDeptExternalAllSum =0.0;
		Double listDeptDaiLiAllSum =0.0;
		Double listDeptZiZhiAllSum =0.0;

		Double listDeptXiaoShouAllSum =0.0;
		Double listDeptBanGongAllSum =0.0;
		Double listDeptBeiYonAllSum =0.0;
		Double listDeptChailvAllSum =0.0;
		Double listDeptFuLiAllSum =0.0;
		Double listDeptGuangGaoAllSum =0.0;
		Double listDeptYeWuAllSum =0.0;
		Double listDeptZuPingAllSum =0.0;
		Double listDeptQiTaFeiYonAllSum =0.0;
		Double listDeptQiTaFuKuanAllSum =0.0;
		Double listDeptQiTaCaiGouAllSum =0.0;

		Double listDeptQiTaZhiChuAllSum =0.0;
		Double listDeptCaiGouYiQiAllSum =0.0;
		Double listDeptGongChenAllSum =0.0;
		Double listDeptYiQiSheBeiAllSum =0.0;
		Double listDeptZhuangXiuFuKuanAllSum =0.0;
		Double listDeptZhiChuFeiYonAllSum =0.0;

		Double listDeptXianJinAllSum=0.0;
		Double listDeptSuoDeiAllSum=0.0;
		Double listDeptJingEAllSum=0.0;

		Double listDeptHeZouFangAllSum=0.0;

		Double listDeptYunYinAllSum=0.0;
		Double listDeptSuoDeiShuiAllSum=0.0;
		Double listDeptYunYinJingLiRunAllSum=0.0;
		Double listDeptWeiShiFuYunYinAllSum=0.0;
		Double listDeptXuNiYunYinAllSum=0.0;

		Double listDeptCostInsideAllSum=0.0;
		Double listDeptSaleManageAllSum=0.0;
		Double listDeptAptitudeAllSum=0.0;
		Double listDeptOtherAllSum=0.0;
		Double listDeptInvestmentAllSum=0.0;
		Double listDeptInstrumentAllSum=0.0;
		Double listSumAllCount=0.0;
		Double listProfitAllCount=0.0;
		Double listTaxAllCount = 0.0;
		Double listNetProfitAllCount=0.0;
		for (String string : listDeptTemp) {
			Double listDeptShouRuBuSum  = 0.0;
			Double listDeptServiceSumCount = 0.0;
			Double listDeptSellSumCount = 0.0;
			Double listDeptHeZouCount = 0.0;
			Double listDeptInsideSumCount = 0.0;
			Double ListDeptInTaxSum = 0.0;
			Double ListDeptTaxSum = 0.0;
			Double ListDeptOutTaxSum = 0.0;
			Double ListDeptWagesSum = 0.0;
			Double ListDeptMaterialSum = 0.0;
			Double ListDeptCaiGouSum = 0.0;
			Double ListDeptHaoCaiSum = 0.0;
			Double listDeptWeiWaiSumCount=0.0;
			Double listDeptExternalSumCount=0.0;
			Double listDeptDaiLiSumCount=0.0;
			Double listDeptZiZhiSumCount=0.0;

			Double listDeptXiaoShouSumCount=0.0;
			Double listDeptBanGongSumCount=0.0;
			Double listDeptBeiYonSumCount=0.0;
			Double listDeptChailvSumCount=0.0;
			Double listDeptFuLiSumCount=0.0;
			Double listDeptGuangGaoSumCount=0.0;
			Double listDeptYeWuSumCount=0.0;
			Double listDeptZuPingSumCount=0.0;
			Double listDeptQiTaFeiYonSumCount=0.0;
			Double listDeptQiTaFuKuanSumCount=0.0;
			Double listDeptQiTaCaiGouSumCount=0.0;

			Double listDeptQiTaZhiChuSumCount=0.0;
			Double listDeptCaiGouYiQiSumCount=0.0;
			Double listDeptGongChenSumCount=0.0;
			Double listDeptYiQiSheBeiSumCount=0.0;
			Double listDeptZhuangXiuFuKuanSumCount=0.0;
			Double listDeptZhiChuFeiYonSumCount=0.0;

			Double listDeptXianJinSumCount=0.0;
			Double listDeptSuoDeiSumCount=0.0;
			Double listDeptJingESumCount=0.0;

			Double listDeptHeZouFangSumCount=0.0;

			Double listDeptYunYinSumCount=0.0;
			Double listDeptSuoDeiShuiSumCount=0.0;
			Double listDeptYunYinJingLiRunSumCount=0.0;
			Double listDeptWeiShiFuYunYinSumCount=0.0;
			Double listDeptXuNiYunYinSumCount=0.0;

			Double listDeptCostInsideSumCount=0.0;
			Double listDeptSaleManageSumCount=0.0;
			Double listDeptAptitudeSumCount=0.0;
			Double listDeptOtherSumCount=0.0;
			Double listDeptInvestmentSumCount=0.0;
			Double listDeptInstrumentSumCount=0.0;
			Double listSumCount=0.0;
			Double listProfitCount=0.0;
			Double listTaxCount = 0.0;
			Double listNetProfitCount=0.0;
			if(listDeptTemp2==null){
				params.put("deptname", deptname);
				map.put("deptname", deptname);
				params.put("deptname1", deptname1);
				map.put("deptname1", deptname1);
				params.put("deptname2", string);
				map.put("deptname2", string);
			}else if(listDeptTemp2.size()==1){
				params.put("deptname", listDeptTemp2.get(0));
				map.put("deptname", listDeptTemp2.get(0));
				params.put("deptname1", deptname1);
				map.put("deptname1", deptname1);
				params.put("deptname2", string);
				map.put("deptname2", string);
			}else if(listDeptTemp2.size()==2){
				params.put("deptname", listDeptTemp2.get(0));
				map.put("deptname", listDeptTemp2.get(0));
				params.put("deptname1", listDeptTemp2.get(1));
				map.put("deptname1", listDeptTemp2.get(1));
				params.put("deptname2", deptname);
				map.put("deptname2", deptname);
				params.put("deptname3", string);
				map.put("deptname3", string);
			}else if(listDeptTemp2.size()==3){
				params.put("deptname", listDeptTemp2.get(0));
				map.put("deptname", listDeptTemp2.get(0));
				params.put("deptname1", listDeptTemp2.get(1));
				map.put("deptname1", listDeptTemp2.get(1));
				params.put("deptname2", listDeptTemp2.get(2));
				map.put("deptname2", listDeptTemp2.get(2));
				params.put("deptname3", listDeptTemp2.get(3));
				map.put("deptname3", listDeptTemp2.get(3));
				params.put("deptname4", string);
				map.put("deptname4", string);
			}

			//服务收入  type=1
			List<Map<String, Object>> tempServiceSum = rdsFinanceConfigNewMapper
					.queryDepServiceSum(params);
			if(tempServiceSum.size()==0)
			{
				listDeptServiceSumCount=0.0;
				listDeptServiceSum.add("0");
			}
			else{
				listDeptServiceSumCount = (Double)(tempServiceSum.get(0)==null?0.0:tempServiceSum.get(0).get("deptServiceSum"));
				listDeptServiceSum.add(decimalFormat.format(listDeptServiceSumCount));
			}
			//销售收入 type=2
			List<Map<String, Object>> tempSellSum = rdsFinanceConfigNewMapper
					.queryDepSellSum(params);
			if(tempSellSum.size()==0){
				listDeptSellSumCount=0.0;
				listDeptSellSum.add("0");
			}
			else{
				listDeptSellSumCount = (Double)(tempSellSum.get(0)==null?0.0:tempSellSum.get(0).get("deptSellSum"));
				listDeptSellSum.add(decimalFormat.format(listDeptSellSumCount));
			}
			//合作方（实收）收入
			List<Map<String, Object>> tempHeZou = rdsFinanceConfigNewMapper
					.queryDepHeZou(params);
			if(tempHeZou.size()==0){
				listDeptHeZouCount=0.0;
				listDeptHeZou.add("0");
			}
			else{
				listDeptHeZouCount = (Double)(tempHeZou.get(0)==null?0.0:tempHeZou.get(0).get("deptHeZou"));
				listDeptHeZou.add(decimalFormat.format(listDeptHeZouCount));
			}
		
			//收入小计（含税）
			ListDeptInTaxSum = listDeptHeZouCount+listDeptSellSumCount+ listDeptServiceSumCount;
			listDeptInTaxSum.add(decimalFormat.format(ListDeptInTaxSum));
			//收入（税）
			ListDeptTaxSum =(listDeptServiceSumCount/1.06*0.06)+(listDeptHeZouCount/1.06*0.06)+(listDeptSellSumCount/1.16*0.16) ;
			listDeptTaxSum.add(decimalFormat.format(ListDeptTaxSum));

			//收入小计（不含税）
			listDeptShouRuBuSum=ListDeptInTaxSum-ListDeptTaxSum;
			listDeptOutTaxSum.add(decimalFormat.format(listDeptShouRuBuSum));

			listDeptNull.add("-");
			listDeptNull1.add("-");
			//人工成本  wags
			List<Map<String, Object>> tempWagesSum = rdsFinanceConfigNewMapper
					.queryDeptWagesSum(map);
			if(tempWagesSum.size()==0){			
				listDeptWagesSum.add("0");
			}
			else{
				ListDeptWagesSum= (Double)(tempWagesSum.get(0)==null?0.0:tempWagesSum.get(0).get("wagesSum"));
				listDeptWagesSum.add(decimalFormat.format(ListDeptWagesSum));
			}

			//采购材料付款
			params.put("amoeba_program", "采购材料付款");
			List<Map<String, Object>> tempCaiGouSum = rdsFinanceConfigNewMapper
					.queryDeptMaterialCostSum(params);
			if(tempCaiGouSum.size()==0){			
				listDeptCaiGouSum.add("0");
			}
			else{
				ListDeptCaiGouSum= (Double)(tempCaiGouSum.get(0)==null?0.0:tempCaiGouSum.get(0).get("materialSum"));
				listDeptCaiGouSum.add(decimalFormat.format(ListDeptCaiGouSum));
			}

			//耗材
			params.put("amoeba_program", "耗材");
			List<Map<String, Object>> tempHaoCaiSum = rdsFinanceConfigNewMapper
					.queryDeptMaterialCostSum(params);
			if(tempHaoCaiSum.size()==0){			
				listDeptHaoCaiSum.add("0");
			}
			else{
				ListDeptHaoCaiSum= (Double)(tempHaoCaiSum.get(0)==null?0.0:tempHaoCaiSum.get(0).get("materialSum"));
				listDeptHaoCaiSum.add(decimalFormat.format(ListDeptHaoCaiSum));
			}

			//材料成本  
			ListDeptMaterialSum=ListDeptCaiGouSum+ListDeptHaoCaiSum;
			listDeptMaterialCostSum.add(decimalFormat.format(ListDeptMaterialSum));


			//委外检测成本
			params.put("amoeba_program", "委外检测成本");
			listDeptExternalSumCount = findByNameOneE(params, listDeptExternalCostSum, decimalFormat,listDeptExternalSumCount);
			//代理费
			params.put("amoeba_program", "代理费");
			listDeptDaiLiSumCount = findByNameOneE(params, listDeptDaiLiCostSum, decimalFormat, listDeptDaiLiSumCount);
			//资质费
			params.put("amoeba_program", "资质费");
			listDeptZiZhiSumCount = findByNameOneE(params, listDeptZiZhiCostSum, decimalFormat, listDeptZiZhiSumCount);

			//办公费
			params.put("amoeba_program", "办公费");
			listDeptBanGongSumCount = findByNameOneE(params, listDeptBanGongCostSum, decimalFormat,listDeptBanGongSumCount);
			//备用金
			listDeptBeiYonSumCount = findByNameE(params, listDeptBeiYonCostSum, decimalFormat, listDeptBeiYonSumCount);
			//差旅费
			params.put("amoeba_program", "差旅费");
			listDeptChailvSumCount = findByNameOneE(params, listDeptChailvCostSum, decimalFormat,listDeptChailvSumCount);
			//福利费
			params.put("amoeba_program", "福利费");
			listDeptFuLiSumCount = findByNameOneE(params, listDeptFuLiCostSum, decimalFormat,listDeptFuLiSumCount);
			//广告宣传
			params.put("amoeba_program", "广告宣传");
			listDeptGuangGaoSumCount = findByNameOneE(params, listDeptGuangGaoCostSum, decimalFormat,listDeptGuangGaoSumCount);
			//业务招待费
			params.put("amoeba_program", "业务招待费");
			listDeptYeWuSumCount = findByNameOneE(params, listDeptYeWuCostSum, decimalFormat,listDeptYeWuSumCount);
			//租赁费
			params.put("amoeba_program", "租赁费");
			listDeptZuPingSumCount = findByNameOneE(params, listDeptZuPingCostSum, decimalFormat,listDeptZuPingSumCount);
			//其他费用
			params.put("amoeba_program", "其他费用");
			listDeptQiTaFeiYonSumCount = findByNameOneE(params, listDeptQiTaFeiYonCostSum, decimalFormat,listDeptQiTaFeiYonSumCount);
			//其他付款
			params.put("amoeba_program", "其他付款");
			listDeptQiTaFuKuanSumCount = findByNameOneE(params, listDeptQiTaFuKuanCostSum, decimalFormat,listDeptQiTaFuKuanSumCount);
			//其它采购
			params.put("amoeba_program", "其它采购");
			listDeptQiTaCaiGouSumCount = findByNameOneE(params, listDeptQiTaCaiGouCostSum, decimalFormat,listDeptQiTaCaiGouSumCount);
			//销售管理费用
			listDeptXiaoShouSumCount=listDeptBanGongSumCount+listDeptBeiYonSumCount
					+listDeptChailvSumCount+listDeptFuLiSumCount+listDeptGuangGaoSumCount+listDeptYeWuSumCount+listDeptZuPingSumCount
					+listDeptQiTaFeiYonSumCount+listDeptQiTaFuKuanSumCount+listDeptQiTaCaiGouSumCount;
			listDeptXiaoShouCostSum.add(decimalFormat.format(listDeptXiaoShouSumCount));

			//采购仪器设备付款
			params.put("amoeba_program", "采购仪器设备付款");
			listDeptCaiGouYiQiSumCount = findByNameOneE(params, listDeptCaiGouYiQiCostSum, decimalFormat,listDeptCaiGouYiQiSumCount);
			//工程
			params.put("amoeba_program", "工程");
			listDeptGongChenSumCount = findByNameOneE(params, listDeptGongChenCostSum, decimalFormat,listDeptGongChenSumCount);
			//仪器设备
			params.put("amoeba_program", "仪器设备");
			listDeptYiQiSheBeiSumCount = findByNameOneE(params, listDeptYiQiSheBeiCostSum, decimalFormat,listDeptYiQiSheBeiSumCount);
			//装修付款
			params.put("amoeba_program", "装修付款");
			listDeptZhuangXiuFuKuanSumCount = findByNameOneE(params, listDeptZhuangXiuFuKuanCostSum, decimalFormat,listDeptZhuangXiuFuKuanSumCount);
			//对外投资
			map.put("amoeba_program", "对外投资");
			List<Map<String, Object>> tempInvestmentCostSum = rdsFinanceConfigNewMapper
					.queryDeptDepreciationCostSum(map);
			if(tempInvestmentCostSum.size()==0){			
				listDeptInvestmentSum.add("0");
			}
			else{
				listDeptInvestmentSumCount= (Double)(tempInvestmentCostSum.get(0)==null?0.0:tempInvestmentCostSum.get(0).get("depreciationSum"));
				listDeptInvestmentSum.add(decimalFormat.format(listDeptInvestmentSumCount));
			}
			//其他支出
			listDeptQiTaZhiChuSumCount= listDeptCaiGouYiQiSumCount+listDeptGongChenSumCount
					+listDeptYiQiSheBeiSumCount+listDeptZhuangXiuFuKuanSumCount;
			listDeptQiTaZhiChuCostSum.add(decimalFormat.format(listDeptQiTaZhiChuSumCount));


			//合作方（应收）结算收入
			params.put("amoeba_program", "合作方收入");
			List<Map<String, Object>> tempHeZouFangSum = rdsFinanceConfigNewMapper
					.queryDeptHeZouFangCostSum(params);
			if(tempHeZouFangSum.size()==0){			
				listDeptHeZouFangCostSum.add("0");
			}
			else{
				listDeptHeZouFangSumCount= (Double)(tempHeZouFangSum.get(0)==null?0.0:tempHeZouFangSum.get(0).get("HeZouFangSum"));
				listDeptHeZouFangCostSum.add(decimalFormat.format(listDeptHeZouFangSumCount));
			}

			//内部结算收入
			List<Map<String, Object>> tempInsideSum = rdsFinanceConfigNewMapper
					.queryDeptInsideSum(params);
			if(tempInsideSum.size()==0){
				listDeptInsideSumCount=0.0;				
				listDeptInsideSum.add("0");
			}
			else{
				listDeptInsideSumCount = (Double)(tempInsideSum.get(0)==null?0.0:tempInsideSum.get(0).get("deptInsideSum"));
				listDeptInsideSum.add(decimalFormat.format(listDeptInsideSumCount));
			}
			
			//其他费用（含折旧及摊销）
			map.put("amoeba_program", "其他费用（含折旧及摊销）");
			List<Map<String, Object>> tempOtherCostSum = rdsFinanceConfigNewMapper
					.queryDeptDepreciationCostSum(map);
			if(tempOtherCostSum.size()==0){			
				listDeptOtherSum.add("0");
			}
			else{
				listDeptOtherSumCount= (Double)(tempOtherCostSum.get(0)==null?0.0:tempOtherCostSum.get(0).get("depreciationSum"));
				listDeptOtherSum.add(decimalFormat.format(listDeptOtherSumCount));
			}
			//资质费用
			List<Map<String, Object>> tempDeptAptitudeSum = rdsFinanceConfigNewMapper
					.queryDeptAptitudeCostSum(params);
			if(tempDeptAptitudeSum.size()==0){			
				listDeptAptitudeSum.add("0");
			}
			else{
				listDeptAptitudeSumCount = (Double)(tempDeptAptitudeSum.get(0)==null?0.0:tempDeptAptitudeSum.get(0).get("deptAptitudeCostSum"));
				listDeptAptitudeSum.add(decimalFormat.format(listDeptAptitudeSumCount));
			}
			//内部结算成本
			List<Map<String, Object>> tempCostInsideSum = rdsFinanceConfigNewMapper
					.queryCostInsideSum(params);
			if(tempCostInsideSum.size()==0){			
				listDeptCostInsideSum.add("0");
			}
			else{
				listDeptCostInsideSumCount = (Double)(tempCostInsideSum.get(0)==null?0.0:tempCostInsideSum.get(0).get("costInsideSum"));
				listDeptCostInsideSum.add(decimalFormat.format(listDeptCostInsideSumCount));
			}

			//委外费用 
			listDeptWeiWaiSumCount=listDeptExternalSumCount+listDeptDaiLiSumCount+listDeptZiZhiSumCount;
			listDeptWeiWaiCostSum.add(decimalFormat.format(listDeptWeiWaiSumCount));
			
			//销售管理费用
			List<Map<String, Object>> tempSaleManageSum = rdsFinanceConfigNewMapper
					.querySaleManageSum(params);
			if(tempSaleManageSum.size()==0){			
				listDeptSaleManageSum.add("0");
			}
			else{
				listDeptSaleManageSumCount = (Double)(tempSaleManageSum.get(0)==null?0.0:tempSaleManageSum.get(0).get("deptSaleManageSum"));
				listDeptSaleManageSum.add(decimalFormat.format(listDeptSaleManageSumCount));
			}

			//仪器设备
			List<Map<String, Object>> tempInstrumentCostSum = rdsFinanceConfigNewMapper
					.queryDeptInstrumentCostSum(params);
			if(tempInstrumentCostSum.size()==0){			
				listDeptInstrumentSum.add("0");
			}
			else{
				listDeptInstrumentSumCount= (Double)(tempInstrumentCostSum.get(0)==null?0.0:tempInstrumentCostSum.get(0).get("instrumentSum"));
				listDeptInstrumentSum.add(decimalFormat.format(listDeptInstrumentSumCount));
			}
			
			//支出费用小计
			listDeptZhiChuFeiYonSumCount=ListDeptWagesSum+ListDeptMaterialSum
					+listDeptWeiWaiSumCount+listDeptXiaoShouSumCount+listDeptQiTaZhiChuSumCount;
			listDeptZhiChuFeiYonCostSum.add(decimalFormat.format(listDeptZhiChuFeiYonSumCount));
			
			
			//现金流量
			listDeptXianJinSumCount=ListDeptInTaxSum-ListDeptTaxSum - listDeptZhiChuFeiYonSumCount;
			listDeptXianJinCostSum.add(decimalFormat.format(listDeptXianJinSumCount));
			//所得税
			listDeptSuoDeiSumCount=listDeptXianJinSumCount * 0.2 ;
			listDeptSuoDeiCostSum.add(decimalFormat.format(listDeptSuoDeiSumCount));
			//现金流量净额
			listDeptJingESumCount= listDeptXianJinSumCount - listDeptSuoDeiSumCount;
			listDeptJingECostSum.add(decimalFormat.format(listDeptJingESumCount));
			
			//运营利润额
			listDeptYunYinSumCount= listDeptXianJinSumCount+listDeptInsideSumCount-listDeptCostInsideSumCount
					+listDeptInvestmentSumCount+listDeptQiTaZhiChuSumCount-listDeptOtherSumCount;
			listDeptYunYinCostSum.add(decimalFormat.format(listDeptYunYinSumCount));
			//所得税
			listDeptSuoDeiShuiSumCount=listDeptYunYinSumCount*0.2;
			listDeptSuoDeiShuiCostSum.add(decimalFormat.format(listDeptSuoDeiShuiSumCount));
			//运营净利润额
			listDeptYunYinJingLiRunSumCount=listDeptYunYinSumCount - listDeptSuoDeiShuiSumCount;
			listDeptYunYinJingLiRunCostSum.add(decimalFormat.format(listDeptYunYinJingLiRunSumCount));
			//未实付运营费用
			Double a=listDeptAptitudeSumCount == 0.0?0.0:listDeptAptitudeSumCount-listDeptZiZhiSumCount;
			listDeptWeiShiFuYunYinSumCount=a - listDeptHeZouFangSumCount;
			listDeptWeiShiFuYunYinCostSum.add(decimalFormat.format(listDeptWeiShiFuYunYinSumCount));
			//虚拟运营净利润合计
			listDeptXuNiYunYinSumCount=listDeptYunYinJingLiRunSumCount - listDeptWeiShiFuYunYinSumCount;
			listDeptXuNiYunYinCostSum.add(decimalFormat.format(listDeptXuNiYunYinSumCount));

			listDeptShouRuBuSumAllCount+=listDeptShouRuBuSum;
			listDeptServiceSumAllCount+=listDeptServiceSumCount;
			listDeptSellSumAllCount+=listDeptSellSumCount;
			listDeptHeZouAllCount+=listDeptHeZouCount;
			listDeptInsideSumAllCount+=listDeptInsideSumCount;
			ListDeptInTaxAllSum+=ListDeptInTaxSum;
			ListDeptTaxAllSum+=ListDeptTaxSum;
			ListDeptOutTaxAllSum+=ListDeptOutTaxSum;
			ListDeptWagesAllSum+=ListDeptWagesSum;
			listDeptWeiWaiAllSum+=listDeptWeiWaiSumCount;
			listDeptMaterialCostAllSum+=ListDeptMaterialSum;
			listDeptCaiGouAllSum+=ListDeptCaiGouSum;
			listDeptHaoCaiAllSum+=ListDeptHaoCaiSum;
			listDeptExternalAllSum+=listDeptExternalSumCount;
			listDeptDaiLiAllSum+=listDeptDaiLiSumCount;
			listDeptZiZhiAllSum+=listDeptZiZhiSumCount;

			listDeptXiaoShouAllSum+=listDeptXiaoShouSumCount;
			listDeptBanGongAllSum+=listDeptBanGongSumCount;
			listDeptBeiYonAllSum+=listDeptBeiYonSumCount;
			listDeptChailvAllSum+=listDeptChailvSumCount;
			listDeptFuLiAllSum+=listDeptFuLiSumCount;
			listDeptGuangGaoAllSum+=listDeptGuangGaoSumCount;
			listDeptYeWuAllSum+=listDeptYeWuSumCount;
			listDeptZuPingAllSum+=listDeptZuPingSumCount;
			listDeptQiTaFeiYonAllSum+=listDeptQiTaFeiYonSumCount;
			listDeptQiTaFuKuanAllSum+=listDeptQiTaFuKuanSumCount;
			listDeptQiTaCaiGouAllSum+=listDeptQiTaCaiGouSumCount;

			listDeptXianJinAllSum+=listDeptXianJinSumCount;
			listDeptSuoDeiAllSum+=listDeptSuoDeiSumCount;
			listDeptJingEAllSum+=listDeptJingESumCount;

			listDeptHeZouFangAllSum+=listDeptHeZouFangSumCount;

			listDeptYunYinAllSum+=listDeptYunYinSumCount;
			listDeptSuoDeiShuiAllSum+=listDeptSuoDeiShuiSumCount;
			listDeptYunYinJingLiRunAllSum+=listDeptYunYinJingLiRunSumCount;
			listDeptWeiShiFuYunYinAllSum+=listDeptWeiShiFuYunYinSumCount;
			listDeptXuNiYunYinAllSum+=listDeptXuNiYunYinSumCount;

			listDeptQiTaZhiChuAllSum+=listDeptQiTaZhiChuSumCount;
			listDeptCaiGouYiQiAllSum+=listDeptCaiGouYiQiSumCount;
			listDeptGongChenAllSum+=listDeptGongChenSumCount;
			listDeptYiQiSheBeiAllSum+=listDeptYiQiSheBeiSumCount;
			listDeptZhuangXiuFuKuanAllSum+=listDeptZhuangXiuFuKuanSumCount;
			listDeptZhiChuFeiYonAllSum+=listDeptZhiChuFeiYonSumCount;

			listDeptCostInsideAllSum+=listDeptCostInsideSumCount;
			listDeptSaleManageAllSum+=listDeptSaleManageSumCount;
			listDeptAptitudeAllSum+=listDeptAptitudeSumCount;
			listDeptOtherAllSum+=listDeptOtherSumCount;
			listDeptInvestmentAllSum+=listDeptInvestmentSumCount;
			listDeptInstrumentAllSum+=listDeptInstrumentSumCount;

			//成本小计
			listSumCount=ListDeptWagesSum+ListDeptMaterialSum+listDeptExternalSumCount+listDeptCostInsideSumCount+listDeptSaleManageSumCount+listDeptAptitudeSumCount+listDeptOtherSumCount;
			listSum.add(decimalFormat.format(listSumCount));
			listProfitCount = ListDeptOutTaxSum+listDeptInsideSumCount-listSumCount;
			listProfit.add(decimalFormat.format(listProfitCount));
			listTaxCount = listProfitCount*0.2;
			listTax.add(decimalFormat.format(listTaxCount));
			listNetProfitCount=listProfitCount-listTaxCount;
			listNetProfit.add(decimalFormat.format(listNetProfitCount));

			listSumAllCount+=listSumCount;
			listProfitAllCount+=listProfitCount;
			listTaxAllCount+=listTaxCount;
			listNetProfitAllCount+=listNetProfitCount;

		}

		listDept.add("合计");
		//服务收入
		listDeptServiceSum.add(decimalFormat.format(listDeptServiceSumAllCount));
		list.add(listDeptServiceSum);
		//销售收入
		listDeptSellSum.add(decimalFormat.format(listDeptSellSumAllCount));
		list.add(listDeptSellSum);
		//合作方（实收）收入
		listDeptHeZou.add(decimalFormat.format(listDeptHeZouAllCount));
		list.add(listDeptHeZou);

		//收入小计（含税）
		listDeptInTaxSum.add(decimalFormat.format(ListDeptInTaxAllSum));
		list.add(listDeptInTaxSum);
		//收入小计（税）
		listDeptTaxSum.add(decimalFormat.format(ListDeptTaxAllSum));
		list.add(listDeptTaxSum);
		//收入小计（不含税）
		listDeptOutTaxSum.add(decimalFormat.format(listDeptShouRuBuSumAllCount));
		list.add(listDeptOutTaxSum);
		//空格
		listDeptNull.add("-");
		list.add(listDeptNull);
		//人工成本
		listDeptWagesSum.add(decimalFormat.format(ListDeptWagesAllSum));
		list.add(listDeptWagesSum);
		//材料成本
		listDeptMaterialCostSum.add(decimalFormat.format(listDeptMaterialCostAllSum));
		list.add(listDeptMaterialCostSum);
		//采购材料付款
		listDeptCaiGouSum.add(decimalFormat.format(listDeptCaiGouAllSum));
		list.add(listDeptCaiGouSum);
		//耗材
		listDeptHaoCaiSum.add(decimalFormat.format(listDeptHaoCaiAllSum));
		list.add(listDeptHaoCaiSum);
		//委外费用
		listDeptWeiWaiCostSum.add(decimalFormat.format(listDeptWeiWaiAllSum));
		list.add(listDeptWeiWaiCostSum);
		//委外检测成本
		listDeptExternalCostSum.add(decimalFormat.format(listDeptExternalAllSum));
		list.add(listDeptExternalCostSum);
		//代理费
		listDeptDaiLiCostSum.add(decimalFormat.format(listDeptDaiLiAllSum));
		list.add(listDeptDaiLiCostSum);
		//资质费
		listDeptZiZhiCostSum.add(decimalFormat.format(listDeptZiZhiAllSum));
		list.add(listDeptZiZhiCostSum);
		//销售管理费用
		listDeptXiaoShouCostSum.add(decimalFormat.format(listDeptXiaoShouAllSum));
		list.add(listDeptXiaoShouCostSum);
		//办公费
		listDeptBanGongCostSum.add(decimalFormat.format(listDeptBanGongAllSum));
		list.add(listDeptBanGongCostSum);
		//备用金
		listDeptBeiYonCostSum.add(decimalFormat.format(listDeptBeiYonAllSum));
		list.add(listDeptBeiYonCostSum);
		//差旅费
		listDeptChailvCostSum.add(decimalFormat.format(listDeptChailvAllSum));
		list.add(listDeptChailvCostSum);
		//福利费
		listDeptFuLiCostSum.add(decimalFormat.format(listDeptFuLiAllSum));
		list.add(listDeptFuLiCostSum);
		//广告宣传
		listDeptGuangGaoCostSum.add(decimalFormat.format(listDeptGuangGaoAllSum));
		list.add(listDeptGuangGaoCostSum);
		//业务招待费
		listDeptYeWuCostSum.add(decimalFormat.format(listDeptYeWuAllSum));
		list.add(listDeptYeWuCostSum);
		//租赁费
		listDeptZuPingCostSum.add(decimalFormat.format(listDeptZuPingAllSum));
		list.add(listDeptZuPingCostSum);
		//其他费用
		listDeptQiTaFeiYonCostSum.add(decimalFormat.format(listDeptQiTaFeiYonAllSum));
		list.add(listDeptQiTaFeiYonCostSum);
		//其他付款
		listDeptQiTaFuKuanCostSum.add(decimalFormat.format(listDeptQiTaFuKuanAllSum));
		list.add(listDeptQiTaFuKuanCostSum);
		//其它采购
		listDeptQiTaCaiGouCostSum.add(decimalFormat.format(listDeptQiTaCaiGouAllSum));
		list.add(listDeptQiTaCaiGouCostSum);

		//对外投资
		listDeptInvestmentSum.add(decimalFormat.format(listDeptInvestmentAllSum));
		list.add(listDeptInvestmentSum);

		//其他支出
		listDeptQiTaZhiChuCostSum.add(decimalFormat.format(listDeptQiTaZhiChuAllSum));
		list.add(listDeptQiTaZhiChuCostSum);
		//采购仪器设备付款
		listDeptCaiGouYiQiCostSum.add(decimalFormat.format(listDeptCaiGouYiQiAllSum));
		list.add(listDeptCaiGouYiQiCostSum);
		//工程
		listDeptGongChenCostSum.add(decimalFormat.format(listDeptGongChenAllSum));
		list.add(listDeptGongChenCostSum);
		//仪器设备
		listDeptYiQiSheBeiCostSum.add(decimalFormat.format(listDeptYiQiSheBeiAllSum));
		list.add(listDeptYiQiSheBeiCostSum);
		//装修付款
		listDeptZhuangXiuFuKuanCostSum.add(decimalFormat.format(listDeptZhuangXiuFuKuanAllSum));
		list.add(listDeptZhuangXiuFuKuanCostSum);
		//支出费用小计
		listDeptZhiChuFeiYonCostSum.add(decimalFormat.format(listDeptZhiChuFeiYonAllSum));
		list.add(listDeptZhiChuFeiYonCostSum);
		//空格
		list.add(listDeptNull);

		//现金流量
		listDeptXianJinCostSum.add(decimalFormat.format(listDeptXianJinAllSum));
		list.add(listDeptXianJinCostSum);
		//所得税
		listDeptSuoDeiCostSum.add(decimalFormat.format(listDeptSuoDeiAllSum));
		list.add(listDeptSuoDeiCostSum);
		//现金流量净额
		listDeptJingECostSum.add(decimalFormat.format(listDeptJingEAllSum));
		list.add(listDeptJingECostSum);
		//空格
		list.add(listDeptNull);

		//合作方（应收）结算收入
		listDeptHeZouFangCostSum.add(decimalFormat.format(listDeptHeZouFangAllSum));
		list.add(listDeptHeZouFangCostSum);
		//内部结算收入
		listDeptInsideSum.add(decimalFormat.format(listDeptInsideSumAllCount));
		list.add(listDeptInsideSum);
		//其他费用（含折旧及摊销）
		listDeptOtherSum.add(decimalFormat.format(listDeptOtherAllSum));
		list.add(listDeptOtherSum);
		//合作方案例资质费用
		listDeptAptitudeSum.add(decimalFormat.format(listDeptAptitudeAllSum));
		list.add(listDeptAptitudeSum);
		//内部结算成本
		listDeptCostInsideSum.add(decimalFormat.format(listDeptCostInsideAllSum));
		list.add(listDeptCostInsideSum);
		//空格
		list.add(listDeptNull);

		//运营利润额
		listDeptYunYinCostSum.add(decimalFormat.format(listDeptYunYinAllSum));
		list.add(listDeptYunYinCostSum);
		//所得税
		listDeptSuoDeiShuiCostSum.add(decimalFormat.format(listDeptSuoDeiShuiAllSum));
		list.add(listDeptSuoDeiShuiCostSum);
		//运营净利润额
		listDeptYunYinJingLiRunCostSum.add(decimalFormat.format(listDeptYunYinJingLiRunAllSum));
		list.add(listDeptYunYinJingLiRunCostSum);
		//未实付运营费用
		listDeptWeiShiFuYunYinCostSum.add(decimalFormat.format(listDeptWeiShiFuYunYinAllSum));
		list.add(listDeptWeiShiFuYunYinCostSum);
		//虚拟运营净利润合计//////////
		listDeptXuNiYunYinCostSum.add(decimalFormat.format(listDeptXuNiYunYinAllSum));
		list.add(listDeptXuNiYunYinCostSum);

		String filename = "阿米巴";

		// 导出excel头列表
		Object[] titles = new Object[listDept.size()];
		for(int i = 0 ; i < listDept.size() ; i ++){
			titles[i]=listDept.get(i);
		}
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			List<String> model= list.get(i);
			for(int j = 0 ; j < model.size() ; j ++)
				objects.add(model.get(j));
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "阿米巴");
	}

	@Override
	public List<RdsStatisticsDepartmentModel> queryDepartmentAll(
			Map<String, Object> params) {
		//查询出当前所有部门
		List<RdsStatisticsDepartmentModel> list = rdsFinanceConfigNewMapper.queryDepartmentAll(params);
		// 内部结算按照月份计算
		Map<String, Object> map = new HashMap<>();
		map.put("confirm_date_start", "".equals(params.get("confirm_date_start"))?"":params.get("confirm_date_start").toString()
				.substring(0, 7));
		map.put("confirm_date_end", "".equals(params.get("confirm_date_end"))?"":params.get("confirm_date_end").toString()
				.substring(0, 7));
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置
		Double listDeptServiceSumAllCount = 0.0;
		Double listDeptSellSumAllCount = 0.0;
		Double listDeptInsideSumAllCount = 0.0;
		Double ListDeptInTaxAllSum = 0.0;
		Double ListDeptTaxAllSum = 0.0;
		Double ListDeptOutTaxAllSum = 0.0;
		Double ListDeptWagesAllSum = 0.0;
		Double listDeptMaterialCostAllSum=0.0;
		Double listDeptExternalAllSum =0.0;
		Double listDeptCostInsideAllSum=0.0;
		Double listDeptSaleManageAllSum=0.0;
		Double listDeptAptitudeAllSum=0.0;
		Double listDeptOtherAllSum=0.0;
		Double listDeptInvestmentAllSum=0.0;
		Double listDeptInstrumentAllSum=0.0;
		Double listSumAllCount=0.0;
		Double listProfitAllCount=0.0;
		Double listTaxAllCount = 0.0;
		Double listNetProfitAllCount=0.0;
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
			Double listDeptExternalSumCount=0.0;
			Double listDeptCostInsideSumCount=0.0;
			Double listDeptSaleManageSumCount=0.0;
			Double listDeptAptitudeSumCount=0.0;
			Double listDeptOtherSumCount=0.0;
			Double listDeptInvestmentSumCount=0.0;
			Double listDeptInstrumentSumCount=0.0;
			Double listSumCount=0.0;
			Double listProfitCount=0.0;
			Double listTaxCount = 0.0;
			Double listNetProfitCount=0.0;

			if(!"0".equals(parentcode)){
				map.put("deptid", parentcode);
				Map<String,String> maps = rdsFinanceConfigNewMapper.queryUserDepartment(map).get(0);
				//二级部门为空，当前根路径为一级部门
				if(null == maps.get("user_dept_level2")){
					map.put("deptname", maps.get("user_dept_level1"));
					params.put("deptname", maps.get("user_dept_level1"));
					map.put("deptname1", rdsStatisticsDepartmentModel.getDeptname());
					params.put("deptname1", rdsStatisticsDepartmentModel.getDeptname());
				}else if(null == maps.get("user_dept_level3") && null != maps.get("user_dept_level2")){
					map.put("deptname", maps.get("user_dept_level1"));
					params.put("deptname", maps.get("user_dept_level1"));
					map.put("deptname1", maps.get("user_dept_level2"));
					params.put("deptname1", maps.get("user_dept_level2"));
					map.put("deptname2", rdsStatisticsDepartmentModel.getDeptname());
					params.put("deptname2", rdsStatisticsDepartmentModel.getDeptname());
				}
			}else{
				map.put("deptname", rdsStatisticsDepartmentModel.getDeptname());
				params.put("deptname", rdsStatisticsDepartmentModel.getDeptname());
			}
			//服务收入
			List<Map<String, Object>> tempServiceSum = rdsFinanceConfigNewMapper
					.queryDepServiceSum(params);
			if(tempServiceSum.size()==0){
				listDeptServiceSumCount=0.0;				
				rdsStatisticsDepartmentModel.setServiceSum("-");
			}
			else{
				listDeptServiceSumCount = (Double)(tempServiceSum.get(0)==null?0.0:tempServiceSum.get(0).get("deptServiceSum"));
				rdsStatisticsDepartmentModel.setServiceSum(listDeptServiceSumCount!=0?decimalFormat.format(listDeptServiceSumCount):"-");
			}
			//销售收入
			List<Map<String, Object>> tempSellSum = rdsFinanceConfigNewMapper
					.queryDepSellSum(params);
			if(tempSellSum.size()==0){
				listDeptSellSumCount=0.0;
				rdsStatisticsDepartmentModel.setSellSum("-");
			}
			else{
				listDeptSellSumCount = (Double)(tempSellSum.get(0)==null?0.0:tempSellSum.get(0).get("deptSellSum"));
				rdsStatisticsDepartmentModel.setSellSum(listDeptSellSumCount!=0?decimalFormat.format(listDeptSellSumCount):"-");
			}
			//内部结算收入
			//事业部查看特殊处理
			if("2".equals(params.get("level"))){
				Map<String,Object> maptemp = new HashMap<>();
				maptemp.put("deptname", params.get("deptname1"));
				maptemp.put("confirm_date_start",params.get("confirm_date_start"));
				maptemp.put("confirm_date_end",params.get("confirm_date_end"));
				List<Map<String, Object>> tempInsideSum = rdsFinanceConfigNewMapper
						.queryDeptInsideSum(maptemp);
				if(tempInsideSum.size()==0){
					listDeptInsideSumCount=0.0;		
					rdsStatisticsDepartmentModel.setInsideSum("-");
				}
				else{
					listDeptInsideSumCount = (Double)(tempInsideSum.get(0)==null?0.0:tempInsideSum.get(0).get("deptInsideSum"));
					rdsStatisticsDepartmentModel.setInsideSum(listDeptInsideSumCount!=0?decimalFormat.format(listDeptInsideSumCount):"-");
				}
			}else
			{
				List<Map<String, Object>> tempInsideSum = rdsFinanceConfigNewMapper
						.queryDeptInsideSum(params);
				if(tempInsideSum.size()==0){
					listDeptInsideSumCount=0.0;		
					rdsStatisticsDepartmentModel.setInsideSum("-");
				}
				else{
					//内部结算只到事业部
					if(null == params.get("deptname1")){
						listDeptInsideSumCount = (Double)(tempInsideSum.get(0)==null?0.0:tempInsideSum.get(0).get("deptInsideSum"));
						rdsStatisticsDepartmentModel.setInsideSum(listDeptInsideSumCount!=0?decimalFormat.format(listDeptInsideSumCount):"-");
					}else{
						listDeptInsideSumCount=0.0;		
						rdsStatisticsDepartmentModel.setInsideSum("-");
					}
				}
			}
			ListDeptInTaxSum = listDeptServiceSumCount+listDeptSellSumCount;
			//收入小计（含税）
			rdsStatisticsDepartmentModel.setDeptInTaxSum(ListDeptInTaxSum!=0?decimalFormat.format(ListDeptInTaxSum):"-");
			ListDeptTaxSum = listDeptServiceSumCount+listDeptSellSumCount-(listDeptServiceSumCount/1.06+listDeptSellSumCount/1.17);
			//收入（税）
			rdsStatisticsDepartmentModel.setTaxSum(ListDeptTaxSum!=0?decimalFormat.format(ListDeptTaxSum):"-");

			ListDeptOutTaxSum = listDeptServiceSumCount/1.06+listDeptSellSumCount/1.17;
			//收入小计（不含税）,服务收入6个税点，销售收入17个税点
			rdsStatisticsDepartmentModel.setDeptOutTaxSum((ListDeptOutTaxSum+listDeptInsideSumCount)!=0?decimalFormat.format(ListDeptOutTaxSum+listDeptInsideSumCount):"-");

			//人工成本
			List<Map<String, Object>> tempWagesSum = rdsFinanceConfigNewMapper
					.queryDeptWagesSum(map);
			if(tempWagesSum.size()==0){			
				rdsStatisticsDepartmentModel.setDeptWagesSum("-");
			}
			else{
				ListDeptWagesSum= (Double)(tempWagesSum.get(0)==null?0.0:tempWagesSum.get(0).get("wagesSum"));
				rdsStatisticsDepartmentModel.setDeptWagesSum(ListDeptWagesSum!=0?decimalFormat.format(ListDeptWagesSum):"-");
			}

			//材料成本
			params.put("amoeba_program", "耗材");
			List<Map<String, Object>> tempMaterialCostSum = rdsFinanceConfigNewMapper
					.queryDeptMaterialCostSum(params);
			if(tempMaterialCostSum.size()==0){			
				rdsStatisticsDepartmentModel.setDeptMaterialCostSum("-");
			}
			else{
				ListDeptMaterialSum= (Double)(tempMaterialCostSum.get(0)==null?0.0:tempMaterialCostSum.get(0).get("materialSum"));
				rdsStatisticsDepartmentModel.setDeptMaterialCostSum(ListDeptMaterialSum!=0?decimalFormat.format(ListDeptMaterialSum):"-");
			}

			//委外检测成本
			params.put("amoeba_program", "委外检测成本");
			List<Map<String, Object>> tempExternalCostSum = rdsFinanceConfigNewMapper
					.queryDeptExternalCostSum(params);
			if(tempExternalCostSum.size()==0){		
				rdsStatisticsDepartmentModel.setDeptExternalCostSum("-");
			}
			else{
				listDeptExternalSumCount= (Double)(tempExternalCostSum.get(0)==null?0.0:tempExternalCostSum.get(0).get("deptExternalCostSum"));
				rdsStatisticsDepartmentModel.setDeptExternalCostSum(listDeptExternalSumCount!=0?decimalFormat.format(listDeptExternalSumCount):"-");
			}

			//内部结算成本
			List<Map<String, Object>> tempCostInsideSum = rdsFinanceConfigNewMapper
					.queryCostInsideSum(params);
			if(tempCostInsideSum.size()==0){	
				rdsStatisticsDepartmentModel.setDeptCostInsideSum("-");
			}
			else{
				listDeptCostInsideSumCount = (Double)(tempCostInsideSum.get(0)==null?0.0:tempCostInsideSum.get(0).get("costInsideSum"));
				rdsStatisticsDepartmentModel.setDeptCostInsideSum(listDeptCostInsideSumCount!=0?decimalFormat.format(listDeptCostInsideSumCount):"-");
			}

			//销售管理费用
			List<Map<String, Object>> tempSaleManageSum = rdsFinanceConfigNewMapper
					.querySaleManageSum(params);
			if(tempSaleManageSum.size()==0){		
				rdsStatisticsDepartmentModel.setDeptSaleManageSum("-");
			}
			else{
				listDeptSaleManageSumCount = (Double)(tempSaleManageSum.get(0)==null?0.0:tempSaleManageSum.get(0).get("deptSaleManageSum"));
				rdsStatisticsDepartmentModel.setDeptSaleManageSum(listDeptSaleManageSumCount!=0?decimalFormat.format(listDeptSaleManageSumCount):"-");
			}

			//资质费用
			List<Map<String, Object>> tempDeptAptitudeSum = rdsFinanceConfigNewMapper
					.queryDeptAptitudeCostSum(params);
			if(tempDeptAptitudeSum.size()==0){	
				rdsStatisticsDepartmentModel.setDeptAptitudeSum("-");
			}
			else{
				listDeptAptitudeSumCount = (Double)(tempDeptAptitudeSum.get(0)==null?0.0:tempDeptAptitudeSum.get(0).get("deptAptitudeCostSum"));
				rdsStatisticsDepartmentModel.setDeptAptitudeSum(listDeptAptitudeSumCount!=0?decimalFormat.format(listDeptAptitudeSumCount):"-");
			}

			//其他费用（含折旧及摊销）
			map.put("amoeba_program", "其他费用（含折旧及摊销）");
			List<Map<String, Object>> tempOtherCostSum = rdsFinanceConfigNewMapper
					.queryDeptDepreciationCostSum(map);
			if(tempOtherCostSum.size()==0){		
				rdsStatisticsDepartmentModel.setDeptOtherSum("-");
			}
			else{
				listDeptOtherSumCount= (Double)(tempOtherCostSum.get(0)==null?0.0:tempOtherCostSum.get(0).get("depreciationSum"));
				rdsStatisticsDepartmentModel.setDeptOtherSum(listDeptOtherSumCount!=0?decimalFormat.format(listDeptOtherSumCount):"-");
			}

			//对外投资
			map.put("amoeba_program", "对外投资");
			List<Map<String, Object>> tempInvestmentCostSum = rdsFinanceConfigNewMapper
					.queryDeptDepreciationCostSum(map);
			if(tempInvestmentCostSum.size()==0){		
				rdsStatisticsDepartmentModel.setDeptInvestmentSum("-");
			}
			else{
				listDeptInvestmentSumCount= (Double)(tempInvestmentCostSum.get(0)==null?0.0:tempInvestmentCostSum.get(0).get("depreciationSum"));
				rdsStatisticsDepartmentModel.setDeptInvestmentSum(listDeptInvestmentSumCount!=0?decimalFormat.format(listDeptInvestmentSumCount):"-");
			}

			//仪器设备
			List<Map<String, Object>> tempInstrumentCostSum = rdsFinanceConfigNewMapper
					.queryDeptInstrumentCostSum(params);
			if(tempInstrumentCostSum.size()==0){			
				rdsStatisticsDepartmentModel.setDeptInstrumentSum("-");
			}
			else{
				listDeptInstrumentSumCount= (Double)(tempInstrumentCostSum.get(0)==null?0.0:tempInstrumentCostSum.get(0).get("instrumentSum"));
				rdsStatisticsDepartmentModel.setDeptInstrumentSum(listDeptInstrumentSumCount!=0?decimalFormat.format(listDeptInstrumentSumCount):"-");
			}

			listDeptServiceSumAllCount+=listDeptServiceSumCount;
			listDeptSellSumAllCount+=listDeptSellSumCount;
			listDeptInsideSumAllCount+=listDeptInsideSumCount;
			ListDeptInTaxAllSum+=ListDeptInTaxSum;
			ListDeptTaxAllSum+=ListDeptTaxSum;
			ListDeptOutTaxAllSum+=ListDeptOutTaxSum;
			ListDeptWagesAllSum+=ListDeptWagesSum;
			listDeptMaterialCostAllSum+=ListDeptMaterialSum;
			listDeptExternalAllSum+=listDeptExternalSumCount;
			listDeptCostInsideAllSum+=listDeptCostInsideSumCount;
			listDeptSaleManageAllSum+=listDeptSaleManageSumCount;
			listDeptAptitudeAllSum+=listDeptAptitudeSumCount;
			listDeptOtherAllSum+=listDeptOtherSumCount;
			listDeptInvestmentAllSum+=listDeptInvestmentSumCount;
			listDeptInstrumentAllSum+=listDeptInstrumentSumCount;

			//成本小计
			listSumCount=ListDeptWagesSum+ListDeptMaterialSum+listDeptExternalSumCount+listDeptCostInsideSumCount+listDeptSaleManageSumCount+listDeptAptitudeSumCount+listDeptOtherSumCount;
			rdsStatisticsDepartmentModel.setDeptCostSum(listSumCount!=0?decimalFormat.format(listSumCount):"-");
			//利润小计
			listProfitCount = ListDeptOutTaxSum+listDeptInsideSumCount-listSumCount;
			rdsStatisticsDepartmentModel.setDeptProfit(listProfitCount!=0?decimalFormat.format(listProfitCount):"-");
			//税
			listTaxCount = listProfitCount*0.2;
			rdsStatisticsDepartmentModel.setDeptTax(listTaxCount!=0?decimalFormat.format(listTaxCount):"-");
			//净利润小计
			listNetProfitCount=listProfitCount-listTaxCount;
			rdsStatisticsDepartmentModel.setDeptNetProfit(listNetProfitCount!=0?decimalFormat.format(listNetProfitCount):"-");

			listSumAllCount+=listSumCount;
			listProfitAllCount+=listProfitCount;
			listTaxAllCount+=listTaxCount;
			listNetProfitAllCount+=listNetProfitCount;
		}
		if("0".equals(parentcode) || null != params.get("allFlag")){
			RdsStatisticsDepartmentModel model = new RdsStatisticsDepartmentModel();
			//合计处理
			if(!"0".equals(parentcode)){
				model.setParentname(params.get("deptname").toString());
			}else
			{
				model.setParentname("");
			}
			model.setDeptname("合计");
			//服务收入
			model.setServiceSum(listDeptServiceSumAllCount!=0?decimalFormat.format(listDeptServiceSumAllCount):"-");
			//销售收入
			model.setSellSum(listDeptSellSumAllCount!=0?decimalFormat.format(listDeptSellSumAllCount):"-");
			//内部结算收入
			model.setInsideSum(listDeptInsideSumAllCount!=0?decimalFormat.format(listDeptInsideSumAllCount):"-");
			//收入小计（含税）
			model.setDeptInTaxSum(ListDeptInTaxAllSum!=0?decimalFormat.format(ListDeptInTaxAllSum):"-");
			//收入小计（税）
			model.setTaxSum(ListDeptTaxAllSum!=0?decimalFormat.format(ListDeptTaxAllSum):"-");
			//收入小计（不含税）
			model.setDeptOutTaxSum((ListDeptOutTaxAllSum+listDeptInsideSumAllCount)!=0?decimalFormat.format(ListDeptOutTaxAllSum+listDeptInsideSumAllCount):"-");
			//人工成本
			model.setDeptWagesSum(ListDeptWagesAllSum!=0?decimalFormat.format(ListDeptWagesAllSum):"-");
			//材料成本
			model.setDeptMaterialCostSum(listDeptMaterialCostAllSum!=0?decimalFormat.format(listDeptMaterialCostAllSum):"-");
			//委外成本
			model.setDeptExternalCostSum(listDeptExternalAllSum!=0?decimalFormat.format(listDeptExternalAllSum):"-");
			//内部结算成本
			model.setDeptCostInsideSum(listDeptCostInsideAllSum!=0?decimalFormat.format(listDeptCostInsideAllSum):"-");
			//销售管理成本
			model.setDeptSaleManageSum(listDeptSaleManageAllSum!=0?decimalFormat.format(listDeptSaleManageAllSum):"-");
			//资质费用
			model.setDeptAptitudeSum(listDeptAptitudeAllSum!=0?decimalFormat.format(listDeptAptitudeAllSum):"-");
			//其他费用（含折旧及摊销）
			model.setDeptOtherSum(listDeptOtherAllSum!=0?decimalFormat.format(listDeptOtherAllSum):"-");
			//对外投资
			model.setDeptInvestmentSum(listDeptInvestmentAllSum!=0?decimalFormat.format(listDeptInvestmentAllSum):"-");
			//仪器设备
			model.setDeptInstrumentSum(listDeptInstrumentAllSum!=0?decimalFormat.format(listDeptInstrumentAllSum):"-");
			//成本小计
			model.setDeptCostSum(listSumAllCount!=0?decimalFormat.format(listSumAllCount):"-");
			//利润小计
			model.setDeptProfit(listProfitAllCount!=0?decimalFormat.format(listProfitAllCount):"-");
			//税
			model.setDeptTax(listTaxAllCount!=0?decimalFormat.format(listTaxAllCount):"-");
			//净利润小计
			model.setDeptNetProfit(listNetProfitAllCount!=0?decimalFormat.format(listNetProfitAllCount):"-");
			model.setLeaf(true);
			list.add(model);
		}

		return list;
	}

	@Override
	public void exportAmoebaTree(Map<String, Object> params,
			HttpServletResponse httpResponse) throws Exception {
		//查询出当前所有部门
		List<RdsStatisticsDepartmentModel> listAll = new ArrayList<>();
		//查询出当前所有部门
		List<RdsStatisticsDepartmentModel> list = rdsFinanceConfigNewMapper.queryDepartmentAll(params);
		// 内部结算按照月份计算
		Map<String, Object> map = new HashMap<>();
		map.put("confirm_date_start", "".equals(params.get("confirm_date_start"))?"":params.get("confirm_date_start").toString()
				.substring(0, 7));
		map.put("confirm_date_end", "".equals(params.get("confirm_date_end"))?"":params.get("confirm_date_end").toString()
				.substring(0, 7));
		String parentcode = params.get("parentcode").toString();
		//一级部门
		for (RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel : list) {
			//存入一级部门数据
			listAll.add(amoebaDepart(params, map, parentcode, rdsStatisticsDepartmentModel,""));

			Map<String, Object> params1 = new HashMap<>();
			params1.put("confirm_date_start", params.get("confirm_date_start"));
			params1.put("confirm_date_end", params.get("confirm_date_end"));
			params1.put("level", params.get("level"));

			Map<String, Object> map1 = new HashMap<>();
			map1.put("confirm_date_start", map.get("confirm_date_start"));
			map1.put("confirm_date_end", map.get("confirm_date_end"));
			//判断是否有二级部门
			if(!rdsStatisticsDepartmentModel.isLeaf()){
				String parentcode1=rdsStatisticsDepartmentModel.getDeptid();
				//当前部门id
				params1.put("parentcode", parentcode1);
				//查询出当前所有部门
				List<RdsStatisticsDepartmentModel> list1 = rdsFinanceConfigNewMapper.queryDepartmentAll(params1);
				//遍历二级部门
				for (RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel2 : list1) {
					//存入二级部门数据
					rdsStatisticsDepartmentModel2.setDeptname(rdsStatisticsDepartmentModel2.getDeptname());
					listAll.add(amoebaDepart(params1, map1, parentcode1, rdsStatisticsDepartmentModel2,"（二级）"));
					Map<String, Object> params2 = new HashMap<>();
					params2.put("confirm_date_start", params.get("confirm_date_start"));
					params2.put("confirm_date_end", params.get("confirm_date_end"));
					params2.put("level", params.get("level"));

					Map<String, Object> map2 = new HashMap<>();
					map2.put("confirm_date_start", map.get("confirm_date_start"));
					map2.put("confirm_date_end", map.get("confirm_date_end"));
					//判断是否存在三级部门
					if(!rdsStatisticsDepartmentModel2.isLeaf()){
						String parentcode2=rdsStatisticsDepartmentModel2.getDeptid();
						params2.put("parentcode", parentcode2);
						//查询出当前所有部门
						List<RdsStatisticsDepartmentModel> list2 = rdsFinanceConfigNewMapper.queryDepartmentAll(params2);
						//最多只有三级部门，存入三级部门数据
						for (RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel3 : list2) {
							rdsStatisticsDepartmentModel3.setDeptname(rdsStatisticsDepartmentModel3.getDeptname());
							listAll.add(amoebaDepart(params2, map2, parentcode2, rdsStatisticsDepartmentModel3,"（三级）"));
						}
					}
					//					else{
					//						listAll.add(amoebaDepart(params, map, parentcode, rdsStatisticsDepartmentModel2));
					//					}
				}
			}
			//			else{
			//				listAll.add(amoebaDepart(params, map, parentcode, rdsStatisticsDepartmentModel));
			//			}
		}
		Object[] titles = { "部门名称","收入","","","","","", "成本","","","","","","","","","", "利润小计", "税", "净利润"};
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
			String fname = new String("独立核算".getBytes("gb2312"),"iso8859-1");
			httpResponse.setHeader("Content-disposition",
					"attachment; filename="+ fname +".xls");// 设定输出文件头
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
			wsheet.mergeCells(1,0,6,0);
			wsheet.mergeCells(7,0,16,0);
			WritableCellFormat writableCellFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
					Colour.BLACK));
			writableCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
			for(int i=0;i<1;i++){
				wsheet.mergeCells(i,0,i,1);
				wsheet.getWritableCell(i,0).setCellFormat(writableCellFormat);
			}
			for(int i=17;i<20;i++){
				wsheet.mergeCells(i,0,i,1);
				wsheet.getWritableCell(i,0).setCellFormat(writableCellFormat);
			}
			//            wsheet.getWritableCell(11,0).setCellFormat(writableCellFormat);
			for(int i=1;i<=6;i++){
				wsheet.getWritableCell(i,1).setCellFormat(writableCellFormat);
			}
			for(int i=7;i<=16;i++){
				wsheet.getWritableCell(i,1).setCellFormat(writableCellFormat);
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
			RdsStatisticsDepartmentModel rdsStatisticsDepartmentModel,String level) {
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
			Map<String, String> maps = rdsFinanceConfigNewMapper
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
			}
		} else {
			map.put("deptname", rdsStatisticsDepartmentModel.getDeptname());
			params.put("deptname", rdsStatisticsDepartmentModel.getDeptname());
		}
		// 服务收入
		List<Map<String, Object>> tempServiceSum = rdsFinanceConfigNewMapper
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
		List<Map<String, Object>> tempSellSum = rdsFinanceConfigNewMapper
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
			List<Map<String, Object>> tempInsideSum = rdsFinanceConfigNewMapper
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
			List<Map<String, Object>> tempInsideSum = rdsFinanceConfigNewMapper
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
		ListDeptTaxSum = listDeptServiceSumCount
				+ listDeptSellSumCount
				- (listDeptServiceSumCount / 1.06 + listDeptSellSumCount / 1.17);
		// 收入（税）
		rdsStatisticsDepartmentModel
		.setTaxSum(ListDeptTaxSum != 0 ? decimalFormat
				.format(ListDeptTaxSum) : "-");

		ListDeptOutTaxSum = listDeptServiceSumCount / 1.06
				+ listDeptSellSumCount / 1.17;
		// 收入小计（不含税）,服务收入6个税点，销售收入17个税点
		rdsStatisticsDepartmentModel
		.setDeptOutTaxSum((ListDeptOutTaxSum + listDeptInsideSumCount) != 0 ? decimalFormat
				.format(ListDeptOutTaxSum + listDeptInsideSumCount)
				: "-");

		// 人工成本
		List<Map<String, Object>> tempWagesSum = rdsFinanceConfigNewMapper
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
		List<Map<String, Object>> tempMaterialCostSum = rdsFinanceConfigNewMapper
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
		List<Map<String, Object>> tempExternalCostSum = rdsFinanceConfigNewMapper
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
		List<Map<String, Object>> tempCostInsideSum = rdsFinanceConfigNewMapper
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
		List<Map<String, Object>> tempSaleManageSum = rdsFinanceConfigNewMapper
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
		List<Map<String, Object>> tempDeptAptitudeSum = rdsFinanceConfigNewMapper
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
		List<Map<String, Object>> tempOtherCostSum = rdsFinanceConfigNewMapper
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
		List<Map<String, Object>> tempInvestmentCostSum = rdsFinanceConfigNewMapper
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
		List<Map<String, Object>> tempInstrumentCostSum = rdsFinanceConfigNewMapper
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
		rdsStatisticsDepartmentModel.setDeptname(level+rdsStatisticsDepartmentModel.getDeptname());
		return rdsStatisticsDepartmentModel;
	}

	@Override
	public List<Map<String, String>> queryUserDepartment2(Map<String, Object> params) {
		return rdsFinanceConfigNewMapper.queryUserDepartment2(params);
	}
}
