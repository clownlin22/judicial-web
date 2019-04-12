package com.rds.statistics.service.impl;

import java.io.OutputStream;
import java.util.ArrayList;
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
import com.rds.statistics.mapper.RdsStatisticsPersonAmboeaMapper;
import com.rds.statistics.model.RdsAmboeaPersonModel;
import com.rds.statistics.model.RdsFinanceCaseDetailStatisticsModel;
import com.rds.statistics.model.RdsFinancePersonAmboeaModel;
import com.rds.statistics.service.RdsStatisticsPersonAmboeaService;

@Service
public class RdsStatisticsPersonAmboeaServiceImpl implements
		RdsStatisticsPersonAmboeaService {

	@Autowired
	private RdsStatisticsPersonAmboeaMapper rdsStatisticsPersonAmboeaMapper;

	@Override
	public List<Object> queryAllPage(Map<String, Object> params) {
		return rdsStatisticsPersonAmboeaMapper.queryAllPage(params);
	}

	@Override
	public int queryAllCount(Map<String, Object> params) {
		return rdsStatisticsPersonAmboeaMapper.queryAllCount(params);
	}

	@Override
	public void exportPersonAmboeaInfo(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "苏博个人阿米巴";
		List<Object> list = rdsStatisticsPersonAmboeaMapper
				.queryAllPage(params);
		Object[] titles = { "人员姓名", "员工编号", "岗位", "电话", "收入", "差旅费", "人工成本",
				"一级部门", "二级部门", "三级部门", "四级部门", "五级部门" };
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsFinancePersonAmboeaModel rdsFinanceWagesModel = (RdsFinancePersonAmboeaModel) list
					.get(i);
			objects.add(rdsFinanceWagesModel.getUsername());
			objects.add(rdsFinanceWagesModel.getWebchart());
			objects.add(rdsFinanceWagesModel.getUsertype());
			objects.add(rdsFinanceWagesModel.getTelphone());
			objects.add(rdsFinanceWagesModel.getReturn_sum());
			objects.add(rdsFinanceWagesModel.getMx1bxje());
			objects.add(rdsFinanceWagesModel.getWages());
			objects.add(rdsFinanceWagesModel.getDept1());
			objects.add(rdsFinanceWagesModel.getDept2());
			objects.add(rdsFinanceWagesModel.getDept3());
			objects.add(rdsFinanceWagesModel.getDept4());
			objects.add(rdsFinanceWagesModel.getDept5());
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "苏博个人阿米巴");
	}

	@Override
	public List<RdsFinanceCaseDetailStatisticsModel> queryCaseAll(
			Map<String, Object> params) {
		return rdsStatisticsPersonAmboeaMapper.queryCaseAll(params);
	}

	@Override
	public int queryCaseAllCount(Map<String, Object> params) {
		return rdsStatisticsPersonAmboeaMapper.queryCaseAllCount(params);
	}

	@Override
	public void exportCaseAll(Map<String, Object> params,
			HttpServletResponse httpResponse) throws Exception {
		List<RdsFinanceCaseDetailStatisticsModel> objs = rdsStatisticsPersonAmboeaMapper
				.queryCaseAll(params);
		// excel表格列头
		Object[] titles = { "案例回款归属划分核心部分", "", "", "", "", "", "", "收入细节部分",
				"", "", "", "", "", "内部结算细节部分", "", "", "", "", "", "", "其他参数",
				"", "", "", "", "", "" };
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
		firstobject.add("核对状态");
		firstobject.add("是否抵扣");
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

			wsheet.mergeCells(27, 0, 27, 0);
			WritableCellFormat writableCellFormat4 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat4.setAlignment(jxl.format.Alignment.CENTRE);
			for (int i = 27; i <= 27; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat4);
			}
			for (int i = 27; i <= 27; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat4);
			}

			wsheet.mergeCells(28, 0, 28, 0);
			WritableCellFormat writableCellFormat5 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat5.setAlignment(jxl.format.Alignment.CENTRE);
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
		// Object[] titles = { "案例编号", "归属人", "归属地", "委托人","受理时间","财务确认时间",
		// "应收金额","回款金额", "项目","子项目", "类型",
		// "一级部门", "二级部门", "三级部门", "内部结算部门", "内部结算费用", "管理费部门", "管理费",
		// "委外成本", "资质费", "实验费", "合作方" ,"备注","财务备注"};
		// List<List<Object>> bodys = new ArrayList<List<Object>>();
		// for (int i = 0; i < list.size(); i++) {
		// List<Object> objects = new ArrayList<Object>();
		// RdsFinanceCaseDetailStatisticsModel statisticModel = list.get(i);
		// objects.add(statisticModel.getCase_code());
		// objects.add(statisticModel.getCase_user());
		// objects.add(statisticModel.getCase_area());
		// objects.add(statisticModel.getClient());
		// objects.add(statisticModel.getAccept_time());
		// objects.add(StringUtils.dateToChineseTen(statisticModel
		// .getConfirm_date()));
		// objects.add(statisticModel.getReal_sum());
		// objects.add(statisticModel.getReturn_sum());
		// objects.add(statisticModel.getCase_type());
		// objects.add(statisticModel.getCase_subtype());
		// objects.add("1".equals(statisticModel.getType()) ? "服务收入" : "销售收入");
		// objects.add(statisticModel.getUser_dept_level1());
		// objects.add(statisticModel.getUser_dept_level2());
		// objects.add(statisticModel.getUser_dept_level3());
		// objects.add(statisticModel.getInsideCostUnit());
		// objects.add(statisticModel.getInsideCost());
		// objects.add(statisticModel.getManageCostUnit());
		// objects.add(statisticModel.getManageCost());
		// objects.add(statisticModel.getExternalCost());
		// objects.add(statisticModel.getAptitudeCost());
		// objects.add(statisticModel.getExperimentCost());
		// objects.add(statisticModel.getPartner());
		// objects.add(statisticModel.getRemark());
		// objects.add(statisticModel.getFinance_remark());
		// bodys.add(objects);
		// }
		// ExportUtils.export(response, filename, titles, bodys, "案例信息表");

	}

	@Override
	public List<RdsAmboeaPersonModel> queryAmboeaPerson(
			Map<String, Object> params) {
		return rdsStatisticsPersonAmboeaMapper.queryAmboeaPerson(params);
	}

	@Override
	public int queryAmboeaPersonCount(Map<String, Object> params) {
		return rdsStatisticsPersonAmboeaMapper.queryAmboeaPersonCount(params);
	}

	@Override
	public void exportAmboeaPerson(Map<String, Object> params,
			HttpServletResponse httpResponse) throws Exception {
		List<RdsAmboeaPersonModel> objs = rdsStatisticsPersonAmboeaMapper
				.queryAmboeaPerson(params);
		// excel表格列头
		Object[] titles = { "阿米巴数据表（前端个人）", "", "", "", "", "", "", "", "收入",
				"", "", "", "", "", "支出", "", "", "","","","","","","","","","",""
				,"","","","","","","","","","","","案例内部结算（成本）","运营情况","","",""};
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		List<Object> firstobject = new ArrayList<Object>();
		firstobject.add("姓名");
		firstobject.add("工号");
		firstobject.add("事业部");
		firstobject.add("二级部门");
		firstobject.add("三级部门");
		firstobject.add("四级部门");
		firstobject.add("五级部门");
		firstobject.add("任职岗位");
		firstobject.add("服务收入");
		firstobject.add("销售收入");
		firstobject.add("合作方收入");
		firstobject.add("收入小计（含税）");
		firstobject.add("税额合计");
		firstobject.add("收入小计（不含税）");
		firstobject.add("人工成本");
		firstobject.add("材料成本");
		firstobject.add("1");
		firstobject.add("2");
		firstobject.add("委外费用");
		firstobject.add("1");
		firstobject.add("2");
		firstobject.add("销售管理费用");
		firstobject.add("1");
		firstobject.add("2");
		firstobject.add("3");
		firstobject.add("4");
		firstobject.add("5");
		firstobject.add("6");
		firstobject.add("7");
		firstobject.add("8");
		firstobject.add("9");
		firstobject.add("10");
		firstobject.add("其他支出");
		firstobject.add("1");
		firstobject.add("2");
		firstobject.add("3");
		firstobject.add("4");
		firstobject.add("5");
		firstobject.add("支出费用小计");
		firstobject.add("");
		firstobject.add("运营利润额");
		firstobject.add("所得税");
		firstobject.add("虚拟运营净利润合计");
		bodys.add(firstobject);
		List<Object> firstobject1 = new ArrayList<Object>();
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("采购材料付款");
		firstobject1.add("耗材");
		firstobject1.add("");
		firstobject1.add("委外检测成本");
		firstobject1.add("代理费");
		firstobject1.add("");
		firstobject1.add("办公费");
		firstobject1.add("备用金");
		firstobject1.add("差旅费");
		firstobject1.add("福利费");
		firstobject1.add("广告宣传");
		firstobject1.add("业务招待费");
		firstobject1.add("租赁费");
		firstobject1.add("其他费用");
		firstobject1.add("其他付款");
		firstobject1.add("其他采购");
		firstobject1.add("");
		firstobject1.add("采购仪器设备付款");
		firstobject1.add("工程");
		firstobject1.add("仪器设备");
		firstobject1.add("对外投资");
		firstobject1.add("装修付款");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		firstobject1.add("");
		bodys.add(firstobject1);
		// 循环案例列表拼装表格一行
		for (int i = 0; i < objs.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsAmboeaPersonModel rdsAllCaseInfoModel = objs.get(i);
			objects.add(rdsAllCaseInfoModel.getUsername());
			objects.add(rdsAllCaseInfoModel.getWebchart());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level1());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level2());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level3());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level4());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level5());
			objects.add(rdsAllCaseInfoModel.getUsertype());
			objects.add(rdsAllCaseInfoModel.getServiceIncome());
			objects.add(rdsAllCaseInfoModel.getSellIncome());
			objects.add(rdsAllCaseInfoModel.getPartnerIncome());
			objects.add(rdsAllCaseInfoModel.getTotalIncome());
			objects.add(rdsAllCaseInfoModel.getTotalTax());
			objects.add(rdsAllCaseInfoModel.getTotalIncomeOutTax());
			objects.add(rdsAllCaseInfoModel.getLaborCost());
			objects.add(rdsAllCaseInfoModel.getMaterials());
			objects.add(rdsAllCaseInfoModel.getPurchasingMaterials());
			objects.add(rdsAllCaseInfoModel.getConsumables());
			objects.add(rdsAllCaseInfoModel.getExternam());
			objects.add(rdsAllCaseInfoModel.getExternalInspection());
			objects.add(rdsAllCaseInfoModel.getAgent());
			objects.add(rdsAllCaseInfoModel.getManage());
			objects.add(rdsAllCaseInfoModel.getOffice());
			objects.add(rdsAllCaseInfoModel.getSpareGold());
			objects.add(rdsAllCaseInfoModel.getTravelExpenses());
			objects.add(rdsAllCaseInfoModel.getWelfareFunds());
			objects.add(rdsAllCaseInfoModel.getAdvertisement());
			objects.add(rdsAllCaseInfoModel.getBusiness());
			objects.add(rdsAllCaseInfoModel.getLease());
			objects.add(rdsAllCaseInfoModel.getOther());
			objects.add(rdsAllCaseInfoModel.getOtherPay());
			objects.add(rdsAllCaseInfoModel.getOtherProcurement());
			objects.add(rdsAllCaseInfoModel.getOtherPayAll());
			objects.add(rdsAllCaseInfoModel.getInstrumentBuy());
			objects.add(rdsAllCaseInfoModel.getEngine());
			objects.add(rdsAllCaseInfoModel.getInstrumentEquipment());
			objects.add(rdsAllCaseInfoModel.getOutboundInvestment());
			objects.add(rdsAllCaseInfoModel.getRenovation());
			objects.add(rdsAllCaseInfoModel.getPaySum());
			objects.add(rdsAllCaseInfoModel.getInternalSettlement());
			objects.add(rdsAllCaseInfoModel.getOperatingProfit());
			objects.add(rdsAllCaseInfoModel.getTaxIncome());
			objects.add(rdsAllCaseInfoModel.getVirtualProfit());
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

			//添加背景色
			WritableCellFormat writableCellFormat3 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat3.setBackground(jxl.format.Colour.RED);
			WritableCellFormat writableCellFormat4 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat4.setBackground(jxl.format.Colour.YELLOW);
			WritableCellFormat writableCellFormat5 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat5.setBackground(jxl.format.Colour.ORANGE);
			
			WritableCellFormat writableCellFormat6 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat6.setBackground(jxl.format.Colour.GREEN);
			for(int i = 0 ; i < objs.size();i++){
				RdsAmboeaPersonModel rdsAllCaseInfoModel = objs.get(i);
				if(rdsAllCaseInfoModel.getVirtualProfit().contains("-"))
					wsheet.getWritableCell(42, i+3).setCellFormat(writableCellFormat3);
				else
					wsheet.getWritableCell(42, i+3).setCellFormat(writableCellFormat6);
				
				wsheet.getWritableCell(41, i+3).setCellFormat(writableCellFormat4);
				wsheet.getWritableCell(40, i+3).setCellFormat(writableCellFormat4);
			}
			
			wsheet.mergeCells(0, 0, 7, 0);
			for(int i = 0 ; i <=15;i++ )
				wsheet.mergeCells(i, 1, i, 2);

			wsheet.mergeCells(18, 1, 18, 2);
			wsheet.mergeCells(21, 1, 21, 2);
			wsheet.mergeCells(32, 1, 32, 2);
			wsheet.mergeCells(38, 1, 38, 2);
			wsheet.mergeCells(39, 0, 39, 2);
			wsheet.mergeCells(40, 1, 40, 2);
			wsheet.mergeCells(41, 1, 41, 2);
			wsheet.mergeCells(42, 1, 42, 2);
			
			WritableCellFormat writableCellFormat = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 0; i < 7; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat);
			}
			wsheet.getWritableCell(7, 0).setCellFormat(writableCellFormat);
			for (int i = 0; i <= 7; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat);
			}

			wsheet.mergeCells(8, 0, 13, 0);
			WritableCellFormat writableCellFormat1 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat1.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 8; i < 13; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat1);
			}
			for (int i = 8; i <= 13; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat1);
			}

			wsheet.mergeCells(14, 0, 38, 0);
			WritableCellFormat writableCellFormat2 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat2.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 14; i < 38; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat2);
			}
			for (int i = 14; i <= 38; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat2);
			}
			
			wsheet.mergeCells(40, 0, 42, 0);
			for (int i = 40; i < 42; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat2);
			}
			for (int i = 40; i <= 42; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat2);
			}
			wsheet.getWritableCell(18, 1).setCellFormat(writableCellFormat2);
			wsheet.getWritableCell(21, 1).setCellFormat(writableCellFormat2);
			wsheet.getWritableCell(39, 0).setCellFormat(writableCellFormat2);
			

			wsheet.getWritableCell(40, 0).setCellFormat(writableCellFormat5);
			wsheet.getWritableCell(42, 1).setCellFormat(writableCellFormat5);
			
			// 主体内容生成结束
			wbook.write(); // 写入文件
			wbook.close();
			os.close(); // 关闭流
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}