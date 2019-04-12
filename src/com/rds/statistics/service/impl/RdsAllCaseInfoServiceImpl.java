package com.rds.statistics.service.impl;

import java.io.OutputStream;
import java.util.ArrayList;
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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.rds.statistics.mapper.RdsAllCaseInfoMapper;
import com.rds.statistics.model.RdsAllCaseInfoModel;
import com.rds.statistics.model.RdsAllCaseInfoModelOld;
import com.rds.statistics.service.RdsAllCaseInfoService;

/**
 * @author lys
 * @className
 * @description
 * @date 2016/5/3
 */
@Service
public class RdsAllCaseInfoServiceImpl implements RdsAllCaseInfoService {
	@Autowired
	private RdsAllCaseInfoMapper rdsAllCaseInfoMapper;

	public List<Object> queryAll(Map<String, Object> params) {
		return rdsAllCaseInfoMapper.queryAll(params);
	}

	public int queryAllCount(Map<String, Object> params) {
		return rdsAllCaseInfoMapper.queryAllCount(params);
	}

	Logger logger = Logger.getLogger(RdsAllCaseInfoServiceImpl.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void export(@RequestParam String case_code,
			@RequestParam String ptype, @RequestParam String receiver,
			@RequestParam String deptname, @RequestParam String agent,
			@RequestParam String start_time, @RequestParam String end_time,
			@RequestParam String client, HttpServletResponse httpResponse) {
		Map params = new HashMap();
		params.put("case_code", case_code);
		params.put("ptype", ptype);
		params.put("receiver", receiver);
		params.put("deptname", deptname);
		params.put("agent", agent);
		params.put("start_time", start_time);
		params.put("end_time", end_time);
		params.put("client", client);
		List<Object> objs = queryAll(params);
		// excel表格列头
		Object[] titles = { "案例发生月份", "案例受理日期", "项目类别", "编号", "委托人", "应收款项",
				"实收款项", "回款日期", "是否确认", "样本数量", "财务备注", "案例备注", "报告种类", "一级部门",
				"二级部门","三级级部门","四级部门","五级级部门", "员工名字", "代理商名称", "合作方名称", "渠道信息" };
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		List<Object> firstobject = new ArrayList<Object>();
		for (int i = 0; i < 19; i++) {
			firstobject.add("");
		}
		firstobject.add("省");
		firstobject.add("地市");
		firstobject.add("区县");
		firstobject.add("是否抵扣");
		bodys.add(firstobject);
		// 循环案例列表拼装表格一行
		for (int i = 0; i < objs.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsAllCaseInfoModel rdsAllCaseInfoModel = (RdsAllCaseInfoModel) objs
					.get(i);
			try {
				objects.add(rdsAllCaseInfoModel.getAccept_time()
						.substring(0, 4)
						+ rdsAllCaseInfoModel.getAccept_time().substring(5, 7));
			} catch (Exception e) {
				objects.add(rdsAllCaseInfoModel.getAccept_time());
			}
			objects.add(rdsAllCaseInfoModel.getAccept_time());
			objects.add(rdsAllCaseInfoModel.getPtype());
			objects.add(rdsAllCaseInfoModel.getCase_code());
			objects.add(rdsAllCaseInfoModel.getClient());
			objects.add(rdsAllCaseInfoModel.getReal_sum());
			objects.add(rdsAllCaseInfoModel.getReturn_sum());
			objects.add(rdsAllCaseInfoModel.getRemittanceDate());
			objects.add("0".equals(rdsAllCaseInfoModel.getStatus()) ? "是" : "否");
			objects.add(rdsAllCaseInfoModel.getSample_count());
			objects.add(rdsAllCaseInfoModel.getFinance_remark());
			objects.add(rdsAllCaseInfoModel.getRemark());
			objects.add(rdsAllCaseInfoModel.getRtype());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level1());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level2());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level3());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level4());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level5());
			objects.add(rdsAllCaseInfoModel.getReceiver());
			objects.add(rdsAllCaseInfoModel.getAgent());
			objects.add(rdsAllCaseInfoModel.getPartner());
			objects.add(rdsAllCaseInfoModel.getProvince());
			objects.add(rdsAllCaseInfoModel.getCity());
			objects.add(rdsAllCaseInfoModel.getCounty());
			objects.add("1".equals(rdsAllCaseInfoModel.getDeductible())?"是":"否");
			bodys.add(objects);
		}

		try {
			OutputStream os = httpResponse.getOutputStream();// 取得输出流
			httpResponse.reset();// 清空输出流
			String fname = new String("经营分析基表".getBytes("gb2312"), "iso8859-1");
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
			wsheet.mergeCells(21, 0, 23, 0);
			WritableCellFormat writableCellFormat = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
			for (int i = 0; i < 22; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat);
			}
			wsheet.getWritableCell(11, 0).setCellFormat(writableCellFormat);
			for (int i = 21; i <= 23; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat);
			}
			
			wsheet.mergeCells(24, 0, 24, 0);
			WritableCellFormat writableCellFormat5 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat5.setAlignment(jxl.format.Alignment.CENTRE);
			for (int i = 24; i <= 24; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat5);
			}
			for (int i = 24; i <= 24; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat5);
			}
			// 主体内容生成结束
			wbook.write(); // 写入文件
			wbook.close();
			os.close(); // 关闭流
		} catch (Exception e) {
			logger.debug(e.getStackTrace());
		}
		// ExportUtils.export(httpResponse, "sheet1", titles, bodys, "经营分析基表"
		// + DateUtils.Date2String(new Date()));
	}

	@Override
	public List<Object> queryAllOld(Map<String, Object> params) {
		return rdsAllCaseInfoMapper.queryAllOld(params);
	}

	@Override
	public int queryAllCountOld(Map<String, Object> params) {
		return rdsAllCaseInfoMapper.queryAllCountOld(params);
	}

	@Override
	public void exportOld(String case_code, String ptype, String receiver,
			String deptname, String agent, String start_time, String end_time,
			String client, HttpServletResponse httpResponse) {
		Map<String,Object> params = new HashMap<>();
		params.put("case_code", case_code);
		params.put("ptype", ptype);
		params.put("receiver", receiver);
		params.put("deptname", deptname);
		params.put("agent", agent);
		params.put("start_time", start_time);
		params.put("end_time", end_time);
		params.put("client", client);
		List<Object> objs = queryAllOld(params);
		// excel表格列头
		Object[] titles = { "案例发生月份", "案例受理日期", "项目类别", "编号", "委托人", "应收款项",
				"实收款项", "回款日期", "是否确认", "样本数量", "财务备注", "案例备注", "报告种类", "一级部门",
				"二级部门","三级级部门", "员工名字", "代理商名称", "合作方名称", "渠道信息" };
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		List<Object> firstobject = new ArrayList<Object>();
		for (int i = 0; i < 19; i++) {
			firstobject.add("");
		}
		firstobject.add("省");
		firstobject.add("地市");
		firstobject.add("区县");
		bodys.add(firstobject);
		// 循环案例列表拼装表格一行
		for (int i = 0; i < objs.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			RdsAllCaseInfoModelOld rdsAllCaseInfoModel = (RdsAllCaseInfoModelOld) objs
					.get(i);
			try {
				objects.add(rdsAllCaseInfoModel.getAccept_time()
						.substring(0, 4)
						+ rdsAllCaseInfoModel.getAccept_time().substring(5, 7));
			} catch (Exception e) {
				objects.add(rdsAllCaseInfoModel.getAccept_time());
			}
			objects.add(rdsAllCaseInfoModel.getAccept_time());
			objects.add(rdsAllCaseInfoModel.getPtype());
			objects.add(rdsAllCaseInfoModel.getCase_code());
			objects.add(rdsAllCaseInfoModel.getClient());
			objects.add(rdsAllCaseInfoModel.getReal_sum());
			objects.add(rdsAllCaseInfoModel.getReturn_sum());
			objects.add("");
			objects.add("");
			objects.add("");
			objects.add("");
			objects.add("");
			objects.add(rdsAllCaseInfoModel.getRtype());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level1());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level2());
			objects.add(rdsAllCaseInfoModel.getUser_dept_level3());
			objects.add(rdsAllCaseInfoModel.getReceiver());
			objects.add(rdsAllCaseInfoModel.getAgent());
			objects.add(rdsAllCaseInfoModel.getPartner());
			objects.add(rdsAllCaseInfoModel.getProvince());
			objects.add(rdsAllCaseInfoModel.getCity());
			objects.add(rdsAllCaseInfoModel.getCounty());
			bodys.add(objects);
		}

		try {
			OutputStream os = httpResponse.getOutputStream();// 取得输出流
			httpResponse.reset();// 清空输出流
			String fname = new String("经营分析基表".getBytes("gb2312"), "iso8859-1");
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
			wsheet.mergeCells(19, 0, 21, 0);
			WritableCellFormat writableCellFormat = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
			for (int i = 0; i < 20; i++) {
				wsheet.mergeCells(i, 0, i, 1);
				wsheet.getWritableCell(i, 0).setCellFormat(writableCellFormat);
			}
			wsheet.getWritableCell(11, 0).setCellFormat(writableCellFormat);
			for (int i = 19; i <= 21; i++) {
				wsheet.getWritableCell(i, 1).setCellFormat(writableCellFormat);
			}
			// 主体内容生成结束
			wbook.write(); // 写入文件
			wbook.close();
			os.close(); // 关闭流
		} catch (Exception e) {
			logger.debug(e.getStackTrace());
		}
	}
}
