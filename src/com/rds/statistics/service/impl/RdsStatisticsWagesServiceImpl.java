package com.rds.statistics.service.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
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

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.statistics.mapper.RdsStatisticsWagesMapper;
import com.rds.statistics.model.RdsFinanceWagesModel;
import com.rds.statistics.service.RdsStatisticsWagesService;

@Service
public class RdsStatisticsWagesServiceImpl implements RdsStatisticsWagesService {

	@Autowired
	private RdsStatisticsWagesMapper rdsStatisticsWagesMapper;

	@Override
	public List<Object> queryAllPage(Map<String, Object> params) {
		return rdsStatisticsWagesMapper.queryAllPage(params);
	}

	@Override
	public int queryAllCount(Map<String, Object> params) {
		return rdsStatisticsWagesMapper.queryAllCount(params);
	}

	@Override
	public boolean insertWages(Map<String, Object> params) {
		return rdsStatisticsWagesMapper.insertWages(params);
	}

	@Override
	public List<Object> queryAll(Map<String, Object> params) {
		return rdsStatisticsWagesMapper.queryAll(params);
	}

	@Override
	public boolean deleteWages(Map<String, Object> params) {
		return rdsStatisticsWagesMapper.deleteWages(params);
	}

	@Override
	public Map<String, Object> uploadWages(String wages_month,
			String attachmentPath, String userid, String attachment_id)
			throws Exception {
		String msg = "操作成功";
		List<Map<String, Object>> lists = new ArrayList<>();
		jxl.Workbook readwb = null;
		try {
			InputStream instream = new FileInputStream(attachmentPath);
			readwb = Workbook.getWorkbook(instream);
			// Sheet的下标是从0开始

			// 获取第一张Sheet表

			Sheet readsheet = readwb.getSheet(0);

			// 获取Sheet表中所包含的总列数

			int rsColumns = readsheet.getColumns();

			// 获取Sheet表中所包含的总行数

			int rsRows = readsheet.getRows();

			// 获取指定单元格的对象引用

			for (int i = 0; i < rsRows; i++)

			{
				String id = UUIDUtil.getUUID();
				String user_dept_level1 = "";
				String user_dept_level2 = "";
				String user_dept_level3 = "";
				String wages = "";
				String wages_name = "";
				Map<String, Object> map = new HashMap<>();
				map.put("id", id);
				map.put("create_per", userid);
				map.put("wages_month", wages_month);
				map.put("attachment_id", attachment_id);
				for (int j = 0; j < rsColumns; j++) {
					Cell cell = readsheet.getCell(j, i);
					if (j == 0) {
						wages_name = cell.getContents().trim();
						map.put("wages_name", wages_name.trim());
					}
					if (j == 1) {
						user_dept_level1 = cell.getContents().trim();
						map.put("user_dept_level1", user_dept_level1.trim());
					}
					if (j == 2) {
						user_dept_level2 = cell.getContents().trim();
						map.put("user_dept_level2", user_dept_level2.trim());
					}
					if (j == 3) {
						user_dept_level3 = cell.getContents().trim();
						map.put("user_dept_level3", user_dept_level3.trim());
					}
					if (j == 4) {
						wages = cell.getContents().trim();
						map.put("wages", wages.trim());
					}

				}
				lists.add(map);
			}
			Map<String, Object> params = new HashMap<>();
			params.put("list", lists);
			// 插入人员成本表
			if (!rdsStatisticsWagesMapper.insertWages(params)) {
				return setMsg(true, false, msg + "上传失败,请联系管理员");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return setMsg(true, false, msg + "上传失败,请联系管理员");
		}
		return setMsg(true, true, msg);
	}

	private Map<String, Object> setMsg(boolean success, boolean result,
			String message) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	@Override
	public List<Object> queryWagesAttachment(Map<String, Object> params) {
		return rdsStatisticsWagesMapper.queryWagesAttachment(params);
	}

	@Override
	public boolean insertWagesAttachment(Map<String, Object> params) {
		return rdsStatisticsWagesMapper.insertWagesAttachment(params);
	}

	@Override
	public boolean updateWagesAttachment(Map<String, Object> params) {
		return rdsStatisticsWagesMapper.updateWagesAttachment(params);
	}

	@Override
	public void exportWagesInfo(Map<String, Object> params,
			HttpServletResponse httpResponse) throws Exception {
		List<Object> objs = rdsStatisticsWagesMapper.queryAllPage(params);
		// excel表格列头
		Object[] titles = { "人工成本归属划分核心部分", "", "", "", "", "", "", "发生月份",
				"人工成本", "", "", "", "", "", "创建人", "创建日期", "备注"};

		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		List<Object> firstobject = new ArrayList<Object>();
		firstobject.add("成本归属事业部");
		firstobject.add("成本归属二级部门");
		firstobject.add("成本归属三级部门");
		firstobject.add("成本归属四级部门");
		firstobject.add("成本归属五级部门");
		firstobject.add("成本归属人");
		firstobject.add("归属人工号");
		firstobject.add("");
		firstobject.add("合计");
		firstobject.add("社保");
		firstobject.add("公积金");
		firstobject.add("月中部分（底薪）");
		firstobject.add("月底部分（绩效等）");
		firstobject.add("其他");
		firstobject.add("");
		firstobject.add("");
		firstobject.add("");
		bodys.add(firstobject);
		// 循环案例列表拼装表格一行
		for (int i = 0; i < objs.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsFinanceWagesModel rdsFinanceWagesModel = (RdsFinanceWagesModel)objs
					.get(i);
			objects.add(rdsFinanceWagesModel.getUser_dept_level1());
			objects.add(rdsFinanceWagesModel.getUser_dept_level2());
			objects.add(rdsFinanceWagesModel.getUser_dept_level3());
			objects.add(rdsFinanceWagesModel.getUser_dept_level4());
			objects.add(rdsFinanceWagesModel.getUser_dept_level5());
			objects.add(rdsFinanceWagesModel.getWages_name());
			objects.add(rdsFinanceWagesModel.getWorkcode());
			objects.add(rdsFinanceWagesModel.getWages_month());
			objects.add(rdsFinanceWagesModel.getWagesTemp());
			objects.add(rdsFinanceWagesModel.getWages_socialTemp());
			objects.add(rdsFinanceWagesModel.getWages_accumulationTemp());
			objects.add(rdsFinanceWagesModel.getWages_middleTemp());
			objects.add(rdsFinanceWagesModel.getWages_endTemp());
			objects.add(rdsFinanceWagesModel.getWages_otherTemp());
			objects.add(rdsFinanceWagesModel.getCreate_pername());
			objects.add(rdsFinanceWagesModel.getCreate_date());
			objects.add(rdsFinanceWagesModel.getRemark());
			bodys.add(objects);
		}

		try {
			OutputStream os = httpResponse.getOutputStream();// 取得输出流
			httpResponse.reset();// 清空输出流
			String fname = new String("人工成本明细".getBytes("gb2312"), "iso8859-1");
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

			wsheet.mergeCells(7, 0, 7, 1);
			WritableCellFormat writableCellFormat1 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat1.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat1
					.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 7; i <= 7; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat1);
			}
			for (int i = 7; i <= 7; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat1);
			}
			
			wsheet.mergeCells(8, 0, 13, 0);
			WritableCellFormat writableCellFormat2 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat2.setAlignment(jxl.format.Alignment.CENTRE);
			for (int i = 8; i < 13; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat2);
			}
			for (int i = 8; i <= 13; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat2);
			}

			wsheet.mergeCells(14, 0, 14, 1);
			WritableCellFormat writableCellFormat3 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat3.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 14; i <= 14; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat3);
			}
			for (int i = 14; i <= 14; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat3);
			}
			
			wsheet.mergeCells(15, 0, 15, 1);
			WritableCellFormat writableCellFormat4 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat4.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat4.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 15; i <= 15; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat4);
			}
			for (int i = 15; i <= 15; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat4);
			}
			
			wsheet.mergeCells(16, 0, 16, 1);
			WritableCellFormat writableCellFormat5 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat5.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat5.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			for (int i = 16; i <= 16; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat5);
			}
			for (int i = 16; i <= 164; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat5);
			}
			// 主体内容生成结束
			wbook.write(); // 写入文件
			wbook.close();
			os.close(); // 关闭流
		} catch (Exception e) {
			e.printStackTrace();
		}
		// String filename = "苏博人员成本";
		// List<Object> list = rdsStatisticsWagesMapper.queryAllPage(params);
		// Object[] titles = { "人员姓名", "月份", "一级部门", "二级部门", "三级部门", "总成本",
		// "公司社保", "公积金", "月中小计","月底小计","创建时间","备注" };
		// List<List<Object>> bodys = new ArrayList<List<Object>>();
		// for (int i = 0; i < list.size(); i++) {
		// List<Object> objects = new ArrayList<Object>();
		// RdsFinanceWagesModel rdsFinanceWagesModel =
		// (RdsFinanceWagesModel)list.get(i);
		// objects.add(rdsFinanceWagesModel.getWages_name());
		// objects.add(rdsFinanceWagesModel.getWages_month());
		// objects.add(rdsFinanceWagesModel.getUser_dept_level1());
		// objects.add(rdsFinanceWagesModel.getUser_dept_level2());
		// objects.add(rdsFinanceWagesModel.getUser_dept_level3());
		// objects.add(rdsFinanceWagesModel.getWagesTemp());
		// objects.add(rdsFinanceWagesModel.getWages_accumulationTemp());
		// objects.add(rdsFinanceWagesModel.getWages_socialTemp());
		// objects.add(rdsFinanceWagesModel.getWages_middleTemp());
		// objects.add(rdsFinanceWagesModel.getWages_endTemp());
		// objects.add(rdsFinanceWagesModel.getCreate_date());
		// objects.add(rdsFinanceWagesModel.getRemark());
		// bodys.add(objects);
		// }
		// ExportUtils.export(response, filename, titles, bodys, "苏博人员成本");
	}
}