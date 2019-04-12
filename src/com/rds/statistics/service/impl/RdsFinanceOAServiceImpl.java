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
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.statistics.mapper.RdsFinanceOAMapper;
import com.rds.statistics.model.RdsFinanceCaseDetailStatisticsModel;
import com.rds.statistics.model.RdsFinanceOAModel;
import com.rds.statistics.model.RdsFinanceOATypeModel;
import com.rds.statistics.model.RdsStatisticsTypeModel;
import com.rds.statistics.model.RdsStatisticsTypeModel2;
import com.rds.statistics.service.RdsFinanceOAService;

@Service
public class RdsFinanceOAServiceImpl implements RdsFinanceOAService {

	@Setter
	@Autowired
	private RdsFinanceOAMapper rdsFinanceOAMapper;

	@Override
	public List<RdsFinanceOAModel> queryAllPage(Map<String, Object> params) {
		return rdsFinanceOAMapper.queryAllPage(params);
	}

	@Override
	public int queryAllCount(Map<String, Object> params) {
		return rdsFinanceOAMapper.queryAllCount(params);
	}
	
	@Override
	public boolean updateOAInfo(Map<String, Object> params) {
		List<RdsFinanceOAModel> list1 = rdsFinanceOAMapper.queryExist1(params);
		List<RdsFinanceOAModel> list2 = rdsFinanceOAMapper.queryExist2(params);
		if(list1.size() > 0){
			return rdsFinanceOAMapper.updateOAInfo1(params);
		}else if(list2.size() > 0){
			return rdsFinanceOAMapper.updateOAInfo2(params);
		}
		return false;
	}

	@Override
	public void exportFinanceOAAll(Map<String, Object> params,
			HttpServletResponse httpResponse) throws Exception {
		List<RdsFinanceOAModel> objs = rdsFinanceOAMapper.queryAllPage(params);
		// excel表格列头
		Object[] titles = { "流程编号", "流程申请人", "支出成本承担主体划分核心部分", "", "", "","", "", "", "费用科目",
				"金额", "财务操作细节部分", "", "", "流程申请日期", "说明","成本承担旧事业部","提单人","提单人工号", "核对状态" };
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		List<Object> firstobject = new ArrayList<Object>();
		firstobject.add("");
		firstobject.add("");
		firstobject.add("成本承担事业部");
		firstobject.add("成本承担二级部门");
		firstobject.add("成本承担三级级部门");
		firstobject.add("成本承担四级部门");
		firstobject.add("成本承担五级部门");
		firstobject.add("成本承担人");
		firstobject.add("承担人工号");
		firstobject.add("");
		firstobject.add("");
		firstobject.add("财务出纳操作日期");
		firstobject.add("财务出纳操作情况");
		firstobject.add("财务出纳意见");
		firstobject.add("");
		firstobject.add("");
		firstobject.add("");
		firstobject.add("");
		firstobject.add("");
		firstobject.add("");
		bodys.add(firstobject);
		// 循环案例列表拼装表格一行
		for (int i = 0; i < objs.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsFinanceOAModel rdsFinanceOAModel = objs
					.get(i);
			objects.add(rdsFinanceOAModel.getDjbh());
			objects.add(rdsFinanceOAModel.getSqrxm());
			objects.add(rdsFinanceOAModel.getUser_dept_level1());
			objects.add(rdsFinanceOAModel.getUser_dept_level2());
			objects.add(rdsFinanceOAModel.getUser_dept_level3());
			objects.add(rdsFinanceOAModel.getUser_dept_level4());
			objects.add(rdsFinanceOAModel.getUser_dept_level5());
			objects.add(rdsFinanceOAModel.getFycdrxm());
			objects.add(rdsFinanceOAModel.getFycdrworkcode());
			objects.add(rdsFinanceOAModel.getKmmc());
			objects.add(rdsFinanceOAModel.getMx1bxje());
			objects.add(rdsFinanceOAModel.getOperatedate());
			objects.add(rdsFinanceOAModel.getIsremark());
			objects.add(rdsFinanceOAModel.getCwcnyj());
			objects.add(rdsFinanceOAModel.getSqrq());
			objects.add(rdsFinanceOAModel.getBxsm());
			objects.add(rdsFinanceOAModel.getZtejmc());
			objects.add(rdsFinanceOAModel.getTdrxm());
			objects.add(rdsFinanceOAModel.getTdrworkcode());
			objects.add("");
			bodys.add(objects);
		}

		try {
			OutputStream os = httpResponse.getOutputStream();// 取得输出流
			httpResponse.reset();// 清空输出流
			String fname = new String("泛薇部门成本".getBytes("gb2312"), "iso8859-1");
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
			//一列
			wsheet.mergeCells(0, 0, 0, 1);
			WritableCellFormat writableCellFormat1 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat1.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 0; i <= 0; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat1);
			}
			for (int i = 0; i <= 0; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat1);
			}
			//二列
			wsheet.mergeCells(1, 0, 1, 1);
			WritableCellFormat writableCellFormat2 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat2.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 1; i <= 1; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat2);
			}
			for (int i = 1; i <= 1; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat2);
			}
			
			wsheet.mergeCells(2, 0, 8, 0);
			WritableCellFormat writableCellFormat3 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat3.setAlignment(jxl.format.Alignment.CENTRE);
			for (int i = 2; i < 8; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat3);
			}
			wsheet.getWritableCell(6, 0).setCellFormat(writableCellFormat3);
			for (int i = 2; i <= 8; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat3);
			}

			wsheet.mergeCells(9, 0, 9, 1);
			WritableCellFormat writableCellFormat4 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat4.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat4.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 9; i <= 9; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat4);
			}
			for (int i = 9; i <= 9; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat4);
			}

			wsheet.mergeCells(10, 0, 10, 1);
			WritableCellFormat writableCellFormat5 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat5.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat5.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 10; i < 10; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat5);
			}
			for (int i = 10; i <= 10; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat5);
			}

			wsheet.mergeCells(11, 0, 13, 0);
			WritableCellFormat writableCellFormat6 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat6.setAlignment(jxl.format.Alignment.CENTRE);
			for (int i = 11; i < 13; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat6);
			}
			for (int i = 11; i <= 13; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat6);
			}

			wsheet.mergeCells(14, 0, 14, 1);
			WritableCellFormat writableCellFormat7 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat7.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat7.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 14; i <= 14; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat7);
			}
			for (int i = 14; i <= 14; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat7);
			}
			
			wsheet.mergeCells(15, 0, 15, 1);
			WritableCellFormat writableCellFormat8 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat8.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat8.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 15; i <= 15; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat8);
			}
			for (int i = 15; i <= 15; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat8);
			}
			
			wsheet.mergeCells(16, 0, 16, 1);
			WritableCellFormat writableCellFormat9 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat9.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat9.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 16; i <= 16; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat8);
			}
			for (int i = 16; i <= 16; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat8);
			}
			
			wsheet.mergeCells(17, 0, 17, 1);
			WritableCellFormat writableCellFormat10 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat9.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat9.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 17; i <= 17; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat10);
			}
			for (int i = 17; i <= 17; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat10);
			}
			// 主体内容生成结束
			wbook.write(); // 写入文件
			wbook.close();
			os.close(); // 关闭流
		} catch (Exception e) {
			e.printStackTrace();
		}
		// String filename = "泛薇部门成本";
		// List<RdsFinanceOAModel> list =
		// rdsFinanceOAMapper.queryAllPage(params);
		// Object[] titles = { "请求流程编号", "承担部门名称", "申请人姓名", "提单人姓名", "费用类型",
		// "金额",
		// "处理日期", "申请日期", "说明" };
		// List<List<Object>> bodys = new ArrayList<List<Object>>();
		// for (int i = 0; i < list.size(); i++) {
		// List<Object> objects = new ArrayList<Object>();
		// RdsFinanceOAModel rdsFinanceOAModel = list.get(i);
		// objects.add(rdsFinanceOAModel.getDjbh());
		// objects.add(rdsFinanceOAModel.getZtsybmc());
		// objects.add(rdsFinanceOAModel.getSqrxm());
		// objects.add(rdsFinanceOAModel.getTdrxm());
		// objects.add(rdsFinanceOAModel.getKmmc());
		// objects.add(rdsFinanceOAModel.getMx1bxje());
		// objects.add(rdsFinanceOAModel.getOperatedate());
		// objects.add(rdsFinanceOAModel.getSqrq());
		// objects.add(rdsFinanceOAModel.getBxsm());
		// bodys.add(objects);
		// }
		// ExportUtils.export(response, filename, titles, bodys, "泛薇部门成本");
	}

	@Override
	public boolean updateOAdept(Map<String, Object> params) {
		List<RdsFinanceOAModel> list1 = rdsFinanceOAMapper.queryExist1(params);
		List<RdsFinanceOAModel> list2 = rdsFinanceOAMapper.queryExist2(params);
		// 泛薇成本分为两张表，分别查询
		if (list1.size() > 0) {
			{
				// 插入日志
				params.put("id", UUIDUtil.getUUID());
				params.put("operateLog", params.get("username") + "修改:"
						+ list1.get(0).getDjbh() + "；原事业部："
						+ list1.get(0).getZtsybmc() + ";原费用类型："
						+ list1.get(0).getKmmc());
				params.put("requestid", list1.get(0).getDjbh());
				params.put("case_id", list1.get(0).getCase_id());
				params.put("kmmc", params.get("kmmc"));
				if (rdsFinanceOAMapper.insertOALog(params))
					return rdsFinanceOAMapper.updateOAdept1(params);
				else
					return false;
			}
		} else if (list2.size() > 0) {
			params.put("id", UUIDUtil.getUUID());
			params.put("operateLog", params.get("username") + "修改:"
					+ list2.get(0).getDjbh() + "；原事业部："
					+ list2.get(0).getZtsybmc());
			params.put("requestid", list2.get(0).getDjbh());
			params.put("case_id", list2.get(0).getCase_id());
			if (rdsFinanceOAMapper.insertOALog(params))
				return rdsFinanceOAMapper.updateOAdept2(params);
			else
				return false;
		} else
			return false;
	}

	@Override
	public List<RdsFinanceOATypeModel> queryOAtypePage(
			Map<String, Object> params) {
		return rdsFinanceOAMapper.queryOAtypePage(params);
	}

	@Override
	public int queryOAtypeCount(Map<String, Object> params) {
		return rdsFinanceOAMapper.queryOAtypeCount(params);
	}

	@Override
	public boolean insertOAtype(Map<String, Object> params) {
		return rdsFinanceOAMapper.insertOAtype(params);
	}

	@Override
	public boolean updateOAtype(Map<String, Object> params) {
		return rdsFinanceOAMapper.updateOAtype(params);
	}

	@Override
	public boolean deleteOAtype(Map<String, Object> params) {
		return rdsFinanceOAMapper.deleteOAtype(params);
	}

	@Override
	public List<RdsStatisticsTypeModel2> queryUserDeptToModel() {
		return rdsFinanceOAMapper.queryUserDeptToModel();
	}

	@Override
	public List<RdsStatisticsTypeModel2> queryUserDeptToModel2(Map<String, Object> params) {
		return rdsFinanceOAMapper.queryUserDeptToModel2(params);
	}

	@Override
	public String queryBuMen(String deptid) {
		return rdsFinanceOAMapper.queryBuMen(deptid);
	}

}
