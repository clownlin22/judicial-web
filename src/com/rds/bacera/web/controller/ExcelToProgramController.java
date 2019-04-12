package com.rds.bacera.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.bacera.mapper.ExcelToProgramMapper;
import com.rds.bacera.mapper.RdsBaceraIdentifyMapper;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.finance.mapper.RdsCaseFinanceMapper;
import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.model.RdsJudicialCaseInfoModel;
import com.rds.judicial.model.RdsJudicialSampleInfoModel;

@Controller
@RequestMapping("bacera/excelToProgram")
public class ExcelToProgramController {

	@Setter
	@Autowired
	private ExcelToProgramMapper mapper;

	@Setter
	@Autowired
	private RdsJudicialRegisterMapper rdsJudicialRegisterMapper;

	@Setter
	@Autowired
	private RdsBaceraIdentifyMapper rdsBaceraIdentifyMapper;

	@Autowired
	private RdsCaseFinanceMapper caseFeeMapper;

	@RequestMapping("getFinancePersion")
	@ResponseBody
	public void getFinancePersion(HttpServletRequest request) {
		jxl.Workbook readwb = null;
		// RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
		// .getAttribute("user");
		try {
			InputStream instream = new FileInputStream("D:/201810.xls");
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
				String user_dept_level1 = "";
				String user_dept_level2 = "";
				String user_dept_level3 = "";
				String user_dept_level4 = "";
				String user_dept_level5 = "";
				String wages_name = "";
				String wages_month = "2018-10";
				Double wages = 0.0;
				Double wages_social = 0.0;
				Double wages_accumulation = 0.0;
				Double wages_middle = 0.0;
				Double wages_end = 0.0;
				Double wages_other = 0.0;
				Map<String, Object> map = new HashMap<>();
				for (int j = 0; j < rsColumns; j++) {
					Cell cell = readsheet.getCell(j, i);
					if (j == 0) {
						user_dept_level1 = cell.getContents().trim();
						map.put("user_dept_level1", user_dept_level1);
					}
					if (j == 1) {
						user_dept_level2 = cell.getContents().trim();
						map.put("user_dept_level2", user_dept_level2);
					}
					if (j == 2) {
						user_dept_level3 = cell.getContents().trim();
						map.put("user_dept_level3", user_dept_level3);
					}
					if (j == 3) {
						wages_name = cell.getContents().trim();
						map.put("wages_name", wages_name);
					}
					if (j == 6) {
						wages_social += Double.parseDouble("".equals(cell
								.getContents().trim().toString()) ? "0" : cell
								.getContents().trim().toString());
						map.put("wages_social", wages_social);
					}
					if (j == 7) {
						wages_accumulation += Double.parseDouble("".equals(cell
								.getContents().trim().toString()) ? "0" : cell
								.getContents().trim().toString());
						map.put("wages_accumulation", wages_accumulation);
					}
					if (j == 8) {
						wages_middle += Double.parseDouble("".equals(cell
								.getContents().trim().toString()) ? "0" : cell
								.getContents().trim().toString());
						map.put("wages_middle", wages_middle);
					}
					if (j == 9) {
						wages_end += Double.parseDouble("".equals(cell
								.getContents().trim().toString()) ? "0" : cell
								.getContents().trim().toString());
						map.put("wages_end", wages_end);
					}
					if (j == 10) {
						wages_other += Double.parseDouble("".equals(cell
								.getContents().trim().toString()) ? "0" : cell
								.getContents().trim().toString());

						map.put("wages_other", wages_other);
					}
					if (j == 5) {
						wages += Double.parseDouble("".equals(cell
								.getContents().trim().toString()) ? "0" : cell
								.getContents().trim().toString());

						map.put("wages", wages);
					}
					map.put("wages_month", wages_month);
				}
				mapper.insertTables3(map);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("getCaseByHand")
	@ResponseBody
	public void getCaseByHand(HttpServletRequest request) {
		jxl.Workbook readwb = null;
		// RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
		// .getAttribute("user");
		try {
			InputStream instream = new FileInputStream("D:/201804.xls");
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
				String case_code = "";
				String fandm = "";
				String sample_name = "";
				String idNumber = "";
				String birthday = "";
				String case_id = UUIDUtil.getUUID();
				String child = "";
				int sample_count = 0;
				String case_userid = "";
				String accept_time = "";
				String consignment_time = "";
				Double stand_sum = 0.0;
				String idNumbers = "";
				String birthdays = "";
				String client = "";
				for (int j = 0; j < rsColumns; j++) {
					Cell cell = readsheet.getCell(j, i);
					if (j == 0) {
						case_code = cell.getContents().trim();
					}
					if (j == 1) {
						accept_time = cell.getContents().trim();
						consignment_time = cell.getContents().trim();
					}
					if (j == 2) {
						client = cell.getContents().trim();
					}
					if (j == 3) {
						sample_count++;
						String temp = cell.getContents().trim();
						if (temp.contains("父亲") && !temp.contains("出生日期")) {
							sample_name = temp.split("：")[1].split("（")[0]
									.trim();
							try {
								idNumber = temp.split("：")[1].split("）")[1]
										.trim();
							} catch (Exception e) {
								idNumber = "";
							}
							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
									UUIDUtil.getUUID(), case_code + "F",
									"cy01", "father", sample_name, idNumber,
									birthday.trim(), case_id);
							rdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
							fandm += "父亲-" + sample_name + "-血痕;";
						} else if (temp.contains("父亲") && temp.contains("出生日期")) {
							idNumber = "";
							sample_name = temp.split("（")[1].split("：")[1]
									.trim();
							try {
								birthday = temp.split("）")[1].split("：")[1]
										.trim();
							} catch (Exception e) {
								birthday = "";
							}
							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
									UUIDUtil.getUUID(), case_code + "F",
									"cy01", "father", sample_name, idNumber,
									birthday.trim(), case_id);
							rdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
							fandm += "父亲-" + sample_name + "-血痕;";
						} else if (temp.contains("母亲")
								&& !temp.contains("出生日期")) {
							idNumber = "";
							sample_name = temp.split("：")[1].split("（")[0]
									.trim();
							try {
								idNumber = temp.split("：")[1].split("）")[1]
										.trim();
							} catch (Exception e) {
								idNumber = "";
							}

							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
									UUIDUtil.getUUID(), case_code + "M",
									"cy01", "mother", sample_name, idNumber,
									birthday.trim(), case_id);
							rdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
							fandm += "母亲-" + sample_name + "-血痕;";
						} else if (temp.contains("母亲") && temp.contains("出生日期")) {
							idNumber = "";
							sample_name = temp.split("（")[1].split("：")[1]
									.trim();
							try {
								birthday = temp.split("）")[1].split("：")[1]
										.trim();
							} catch (Exception e) {
								birthday = "";
							}
							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
									UUIDUtil.getUUID(), case_code + "M",
									"cy01", "mother", sample_name, idNumber,
									birthday.trim(), case_id);
							rdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
							fandm += "" + "-" + sample_name + "-血痕;";
						} else {
							idNumber = "";
							sample_name = temp.split("：")[1].split("（")[0]
									.trim();
							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
									UUIDUtil.getUUID(), case_code + "A",
									"cy01", "child", sample_name, idNumber,
									birthday.trim(), case_id);
							rdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
							child += "孩子-" + sample_name + "-血痕;";
						}
						idNumbers += idNumber + ";";
						birthdays += birthday + ";";
					}
					if (j == 4) {
						sample_count++;
						String temp = cell.getContents().trim();
						if (temp.contains("母亲") && !temp.contains("出生日期")) {
							idNumber = "";
							sample_name = temp.split("（")[0].split("：")[1]
									.trim();
							try {
								idNumber = temp.split("）")[1].trim();
							} catch (Exception e) {
								idNumber = "";
							}
							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
									UUIDUtil.getUUID(), case_code + "M",
									"cy01", "mother", sample_name, idNumber,
									birthday.trim(), case_id);
							rdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
							fandm += "母亲-" + sample_name + "-血痕;";
						} else if (temp.contains("母亲") && temp.contains("出生日期")) {
							idNumber = "";
							sample_name = temp.split("（")[0].split("：")[1]
									.trim();
							try {
								birthday = temp.split("）")[1].split("：")[1]
										.trim();
							} catch (Exception e) {
								birthday = "";
							}
							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
									UUIDUtil.getUUID(), case_code + "M",
									"cy01", "mother", sample_name, idNumber,
									birthday.trim(), case_id);
							rdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
							fandm += "母亲-" + sample_name + "-血痕;";
						} else if (temp.contains("儿子") && temp.contains("出生证号")
								&& temp.contains("出生日期")) {
							idNumber = "";
							sample_name = temp.split("（")[0].split("：")[1]
									.trim();
							try {
								birthday = temp.split("）")[1].split("出生证号")[0]
										.split("：")[1].trim();
							} catch (Exception e) {
								birthday = "";
							}
							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
									UUIDUtil.getUUID(), case_code + "S",
									"cy01", "son", sample_name, idNumber,
									birthday.trim(), case_id);
							rdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
							child += "儿子-" + sample_name + "-血痕;";
						} else if (temp.contains("儿子")
								&& !temp.contains("出生证号")
								&& temp.contains("出生日期")) {
							idNumber = "";
							sample_name = temp.split("（")[0].split("：")[1]
									.trim();
							try {
								birthday = temp.split("）")[1].split("：")[1]
										.trim();
							} catch (Exception e) {
								birthday = "";
							}
							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
									UUIDUtil.getUUID(), case_code + "S",
									"cy01", "son", sample_name, idNumber,
									birthday.trim(), case_id);
							rdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
							child += "儿子-" + sample_name + "-血痕;";
						} else if (temp.contains("儿子")
								&& !temp.contains("出生证号")
								&& !temp.contains("出生日期")) {
							idNumber = "";
							sample_name = temp.split("（")[0].split("：")[1]
									.trim();

							try {
								idNumber = temp.split("）")[1].trim();
							} catch (Exception e) {
								idNumber = "";
							}
							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
									UUIDUtil.getUUID(), case_code + "S",
									"cy01", "son", sample_name, idNumber,
									birthday.trim(), case_id);
							rdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
							child += "儿子-" + sample_name + "-血痕;";
						} else if (temp.contains("女儿") && temp.contains("出生证号")
								&& temp.contains("出生日期")) {
							idNumber = "";
							sample_name = temp.split("（")[0].split("：")[1]
									.trim();

							try {
								birthday = temp.split("）")[1].split("出生证号")[0]
										.split("：")[1].trim();
							} catch (Exception e) {
								birthday = "";
							}
							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
									UUIDUtil.getUUID(), case_code + "D",
									"cy01", "daughter", sample_name, idNumber,
									birthday.trim(), case_id);
							rdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
							child += "女儿-" + sample_name + "-血痕;";
						} else if (temp.contains("女儿")
								&& !temp.contains("出生证号")
								&& temp.contains("出生日期")) {
							idNumber = "";
							sample_name = temp.split("（")[0].split("：")[1]
									.trim();

							try {
								birthday = temp.split("）")[1].split("：")[1]
										.trim();
							} catch (Exception e) {
								birthday = "";
							}
							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
									UUIDUtil.getUUID(), case_code + "D",
									"cy01", "daughter", sample_name, idNumber,
									birthday.trim(), case_id);
							rdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
							child += "女儿-" + sample_name + "-血痕;";
						} else if (temp.contains("女儿")
								&& !temp.contains("出生证号")
								&& !temp.contains("出生日期")) {
							idNumber = "";
							sample_name = temp.split("（")[0].split("：")[1]
									.trim();
							try {
								idNumber = temp.split("）")[1].trim();
							} catch (Exception e) {
								idNumber = "";
							}
							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
									UUIDUtil.getUUID(), case_code + "D",
									"cy01", "daughter", sample_name, idNumber,
									birthday.trim(), case_id);
							rdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
							child += "女儿-" + sample_name + "-血痕;";
						} else {
							idNumber = "";
							sample_name = temp.split("：")[1].split("（")[0]
									.trim();
							RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
									UUIDUtil.getUUID(), case_code + "A",
									"cy01", "child", sample_name, idNumber,
									birthday.trim(), case_id);
							rdsJudicialRegisterMapper
									.insertSampleInfo(sampleInfoModel);
							child += "孩子-" + sample_name + "-血痕;";
						}
						idNumbers += idNumber + ";";
						birthdays += birthday + ";";
					}
					if (j == 5) {
						String temp = cell.getContents().trim();
						if (!"".equals(temp)) {
							sample_count++;
							if (temp.contains("儿子") && temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();

								try {
									birthday = temp.split("）")[1].split("出生证号")[0]
											.split("：")[1].trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "S",
										"cy01", "son", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "儿子-" + sample_name + "-血痕;";
							} else if (temp.contains("儿子")
									&& !temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("：")[1]
											.trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "S",
										"cy01", "son", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "儿子-" + sample_name + "-血痕;";
							} else if (temp.contains("儿子")
									&& !temp.contains("出生证号")
									&& !temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									idNumber = temp.split("）")[1].trim();
								} catch (Exception e) {
									idNumber = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "S",
										"cy01", "son", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "儿子-" + sample_name + "-血痕;";
							} else if (temp.contains("女儿")
									&& temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("出生证号")[0]
											.split("：")[1].trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "D",
										"cy01", "daughter", sample_name,
										idNumber, birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "女儿-" + sample_name + "-血痕;";
							} else if (temp.contains("女儿")
									&& !temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("：")[1]
											.trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "D",
										"cy01", "daughter", sample_name,
										idNumber, birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "女儿-" + sample_name + "-血痕;";
							} else if (temp.contains("女儿")
									&& !temp.contains("出生证号")
									&& !temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									idNumber = temp.split("）")[1].trim();
								} catch (Exception e) {
									idNumber = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "D",
										"cy01", "daughter", sample_name,
										idNumber, birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "女儿-" + sample_name + "-血痕;";
							} else {
								idNumber = "";
								sample_name = temp.split("：")[1].split("（")[0]
										.trim();
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "A",
										"cy01", "child", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "孩子-" + sample_name + "-血痕;";
							}
							idNumbers += idNumber + ";";
							birthdays += birthday + ";";
						}

					}
					if (j == 6) {
						String temp = cell.getContents().trim();
						if (!"".equals(temp)) {
							sample_count++;
							if (temp.contains("儿子") && temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("出生证号")[0]
											.split("：")[1].trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "S",
										"cy01", "son", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "儿子-" + sample_name + "-血痕;";
							} else if (temp.contains("儿子")
									&& !temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("：")[1]
											.trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "S",
										"cy01", "son", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "儿子-" + sample_name + "-血痕;";
							} else if (temp.contains("儿子")
									&& !temp.contains("出生证号")
									&& !temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									idNumber = temp.split("）")[1].trim();
								} catch (Exception e) {
									idNumber = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "S",
										"cy01", "son", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "儿子-" + sample_name + "-血痕;";
							} else if (temp.contains("女儿")
									&& temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("出生证号")[0]
											.split("：")[1].trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "D",
										"cy01", "daughter", sample_name,
										idNumber, birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "女儿-" + sample_name + "-血痕;";
							} else if (temp.contains("女儿")
									&& !temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("：")[1]
											.trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "D",
										"cy01", "daughter", sample_name,
										idNumber, birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "女儿-" + sample_name + "-血痕;";
							} else if (temp.contains("女儿")
									&& !temp.contains("出生证号")
									&& !temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									idNumber = temp.split("）")[1].trim();
								} catch (Exception e) {
									idNumber = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "D",
										"cy01", "daughter", sample_name,
										idNumber, birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "女儿-" + sample_name + "-血痕;";
							} else {
								idNumber = "";
								sample_name = temp.split("：")[1].split("（")[0]
										.trim();
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "A",
										"cy01", "child", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "孩子-" + sample_name + "-血痕;";
							}
							idNumbers += idNumber + ";";
							birthdays += birthday + ";";
						}

					}
					if (j == 7) {
						String temp = cell.getContents().trim();
						if (!"".equals(temp)) {
							sample_count++;
							if (temp.contains("儿子") && temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("出生证号")[0]
											.split("：")[1].trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "S",
										"cy01", "son", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "儿子-" + sample_name + "-血痕;";
							} else if (temp.contains("儿子")
									&& !temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("：")[1]
											.trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "S",
										"cy01", "son", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "儿子-" + sample_name + "-血痕;";
							} else if (temp.contains("儿子")
									&& !temp.contains("出生证号")
									&& !temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									idNumber = temp.split("）")[1].trim();
								} catch (Exception e) {
									idNumber = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "S",
										"cy01", "son", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "儿子-" + sample_name + "-血痕;";
							} else if (temp.contains("女儿")
									&& temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("出生证号")[0]
											.split("：")[1].trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "D",
										"cy01", "daughter", sample_name,
										idNumber, birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "女儿-" + sample_name + "-血痕;";
							} else if (temp.contains("女儿")
									&& !temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("：")[1]
											.trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "D",
										"cy01", "daughter", sample_name,
										idNumber, birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "女儿-" + sample_name + "-血痕;";
							} else if (temp.contains("女儿")
									&& !temp.contains("出生证号")
									&& !temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									idNumber = temp.split("）")[1].trim();
								} catch (Exception e) {
									idNumber = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "D",
										"cy01", "daughter", sample_name,
										idNumber, birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "女儿-" + sample_name + "-血痕;";
							} else {
								idNumber = "";
								sample_name = temp.split("：")[1].split("（")[0]
										.trim();
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "A",
										"cy01", "child", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "孩子-" + sample_name + "-血痕;";
							}
							idNumbers += idNumber + ";";
							birthdays += birthday + ";";
						}

					}
					if (j == 8) {
						String temp = cell.getContents().trim();
						if (!"".equals(temp)) {
							sample_count++;
							if (temp.contains("儿子") && temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("出生证号")[0]
											.split("：")[1].trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "S",
										"cy01", "son", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "儿子" + sample_name + "-血痕;";
							} else if (temp.contains("儿子")
									&& !temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("：")[1]
											.trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "S",
										"cy01", "son", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "儿子" + sample_name + "-血痕;";
							} else if (temp.contains("儿子")
									&& !temp.contains("出生证号")
									&& !temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									idNumber = temp.split("）")[1].trim();
								} catch (Exception e) {
									idNumber = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "S",
										"cy01", "son", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "儿子" + sample_name + "-血痕;";
							} else if (temp.contains("女儿")
									&& temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("出生证号")[0]
											.split("：")[1].trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "D",
										"cy01", "daughter", sample_name,
										idNumber, birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "女儿-" + sample_name + "-血痕;";
							} else if (temp.contains("女儿")
									&& !temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("：")[1]
											.trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "D",
										"cy01", "daughter", sample_name,
										idNumber, birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "女儿-" + sample_name + "-血痕;";
							} else if (temp.contains("女儿")
									&& !temp.contains("出生证号")
									&& !temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									idNumber = temp.split("）")[1].trim();
								} catch (Exception e) {
									idNumber = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "D",
										"cy01", "daughter", sample_name,
										idNumber, birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "女儿-" + sample_name + "-血痕;";
							} else {
								idNumber = "";
								sample_name = temp.split("：")[1].split("（")[0]
										.trim();
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "A",
										"cy01", "child", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "孩子-" + sample_name + "-血痕;";
							}
							idNumbers += idNumber + ";";
							birthdays += birthday + ";";
						}
					}
					if (j == 9) {
						String temp = cell.getContents().trim();
						if (!"".equals(temp)) {
							sample_count++;
							if (temp.contains("儿子") && temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("出生证号")[0]
											.split("：")[1].trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "S",
										"cy01", "son", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "儿子" + sample_name + "-血痕;";
							} else if (temp.contains("儿子")
									&& !temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("：")[1]
											.trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "S",
										"cy01", "son", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "儿子" + sample_name + "-血痕;";
							} else if (temp.contains("儿子")
									&& !temp.contains("出生证号")
									&& !temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									idNumber = temp.split("）")[1].trim();
								} catch (Exception e) {
									idNumber = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "S",
										"cy01", "son", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "儿子" + sample_name + "-血痕;";
							} else if (temp.contains("女儿")
									&& temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("出生证号")[0]
											.split("：")[1].trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "D",
										"cy01", "daughter", sample_name,
										idNumber, birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "女儿" + sample_name + "-血痕;";
							} else if (temp.contains("女儿")
									&& !temp.contains("出生证号")
									&& temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									birthday = temp.split("）")[1].split("：")[1]
											.trim();
								} catch (Exception e) {
									birthday = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "D",
										"cy01", "daughter", sample_name,
										idNumber, birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "女儿" + sample_name + "-血痕;";
							} else if (temp.contains("女儿")
									&& !temp.contains("出生证号")
									&& !temp.contains("出生日期")) {
								idNumber = "";
								sample_name = temp.split("（")[0].split("：")[1]
										.trim();
								try {
									idNumber = temp.split("）")[1].trim();
								} catch (Exception e) {
									idNumber = "";
								}
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "D",
										"cy01", "daughter", sample_name,
										idNumber, birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "女儿" + sample_name + "-血痕;";
							} else {
								idNumber = "";
								sample_name = temp.split("：")[1].split("（")[0]
										.trim();
								RdsJudicialSampleInfoModel sampleInfoModel = new RdsJudicialSampleInfoModel(
										UUIDUtil.getUUID(), case_code + "A",
										"cy01", "child", sample_name, idNumber,
										birthday.trim(), case_id);
								rdsJudicialRegisterMapper
										.insertSampleInfo(sampleInfoModel);
								child += "孩子-" + sample_name + "-血痕;";
							}
							idNumbers += idNumber + ";";
							birthdays += birthday + ";";
						}
					}
				}

				RdsJudicialCaseInfoModel caseInfoModel = new RdsJudicialCaseInfoModel();
				caseInfoModel.setCase_id(case_id);
				caseInfoModel.setCase_code(case_code);
				caseInfoModel
						.setCase_userid("6513189401264e26bf62e63dc69063d0");
				caseInfoModel
						.setCase_in_per("17ce64a679ed46e090492e4c45cc8520");
				caseInfoModel.setReceiver_area("四川省");
				caseInfoModel.setCase_areacode("510000");
				caseInfoModel.setSample_in_per("中信");
				caseInfoModel.setAccept_time(accept_time);
				caseInfoModel.setConsignment_time(consignment_time);
				caseInfoModel.setSample_relation(0);
				caseInfoModel.setClient(client);
				caseInfoModel.setSample_in_time(accept_time);
				caseInfoModel.setRemark("管理员导入");
				caseInfoModel.setParnter_name("四川中信司法鉴定所");
				rdsJudicialRegisterMapper.insertCaseInfo(caseInfoModel);

				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put(
						"fandm",
						fandm.endsWith(";") ? fandm.subSequence(0,
								fandm.length() - 1) : fandm);
				map1.put(
						"child",
						child.endsWith(";") ? child.subSequence(0,
								child.length() - 1) : child);
				map1.put("id_card", idNumbers);
				map1.put("case_id", case_id);
				map1.put("sample_count", sample_count);
				map1.put("birth_date", birthdays);
				rdsJudicialRegisterMapper.addCaseSample(map1);

				Map<String, Object> params = new HashMap<>();
				params.put("id", UUIDUtil.getUUID());
				params.put("case_id", case_id);
				params.put("stand_sum", 300.0);
				params.put("real_sum", 300.0);
				params.put("return_sum", 0);
				params.put("type", 0);
				params.put("update_user", "6513189401264e26bf62e63dc69063d0");
				params.put("update_date", accept_time);
				params.put("finance_type", "亲子鉴定-司法");
				caseFeeMapper.addCaseFee(params);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("insertYXB")
	@ResponseBody
	public Object delete(@RequestParam String username,
			@RequestParam String concat, @RequestParam String adress,
			@RequestParam String code, @RequestParam String type,
			HttpServletRequest request) {
		Map<String, Object> params = new HashMap<>();
		params.put("username", username);
		params.put("concat", concat);
		params.put("adress", adress);
		params.put("code", code);
		params.put("type", type);
		Map<String, Object> result = new HashMap<>();
		try {
			List<Map<String, Object>> list = mapper.selectYXBcodeTemp(params);
			if (list.size() <= 0) {
				result.put("result", false);
				result.put("message", "该校验码有误！");
				return result;
			} else {
				Map<String, Object> map = (Map<String, Object>) list.get(0);
				if ("2".equals(map.get("use").toString())) {
					result.put("result", false);
					result.put("message", "该校验码已被使用！");
					return result;
				} else {
					if (mapper.insertYXBtemp(params) > 0) {
						result.put("result", true);
						result.put("message", "提交成功，坐等收货吧！");
						return result;
					}
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("message", "提交失败，请联系管理员！");
			return result;
		}

	}

	@RequestMapping("export")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response) {
		try {
			String fname = "test";
			String filename = "test";
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			fname = new String(fname.getBytes("gb2312"), "iso8859-1");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fname + ".xls");// 设定输出文件头
			response.setContentType("application/vnd.ms-excel");
			WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
			WritableSheet wsheet = wbook.createSheet(filename, 0); // sheet名称
			wsheet.setRowView(2, 360);
			wsheet.setRowView(3, 360);
			wsheet.setColumnView(1, 9);
			wsheet.setColumnView(2, 15);
			wsheet.setColumnView(3, 13);
			wsheet.setColumnView(4, 13);
			wsheet.setColumnView(5, 13);
			wsheet.setColumnView(6, 13);
			wsheet.setColumnView(7, 13);
			wsheet.setColumnView(8, 13);
			WritableCellFormat writableCellFormat = new WritableCellFormat(
					new WritableFont(WritableFont.createFont("微软雅黑"), 12,
							WritableFont.BOLD, false,
							UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat
					.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			writableCellFormat.setBorder(jxl.format.Border.RIGHT,
					jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
			Label label = new Label(0, 0, "苏博医学阿米巴损益表1.0");
			wsheet.addCell(label);
			wsheet.mergeCells(0, 0, 8, 0);
			wsheet.getWritableCell(0, 0).setCellFormat(writableCellFormat);

			WritableCellFormat writableCellFormat1 = new WritableCellFormat(
					new WritableFont(WritableFont.createFont("微软雅黑"), 11,
							WritableFont.BOLD, false,
							UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat1.setAlignment(jxl.format.Alignment.RIGHT);
			writableCellFormat1
					.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			writableCellFormat1.setBorder(jxl.format.Border.RIGHT,
					jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
			writableCellFormat1.setBorder(jxl.format.Border.BOTTOM,
					jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
			Label label1 = new Label(0, 1, "单元：元");
			wsheet.addCell(label1);
			wsheet.mergeCells(0, 1, 8, 1);
			wsheet.getWritableCell(0, 1).setCellFormat(writableCellFormat1);

			WritableCellFormat writableCellFormat2 = new WritableCellFormat(
					new WritableFont(WritableFont.createFont("微软雅黑"), 12,
							WritableFont.BOLD, false,
							UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat2.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat2
					.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			writableCellFormat2.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
			Label label2 = new Label(0, 2, "项   目");
			wsheet.addCell(label2);
			wsheet.mergeCells(0, 2, 2, 3);
			wsheet.getWritableCell(0, 2).setCellFormat(writableCellFormat2);

			WritableCellFormat writableCellFormat3 = new WritableCellFormat(
					new WritableFont(WritableFont.createFont("微软雅黑"), 11,
							WritableFont.BOLD, false,
							UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			writableCellFormat3.setAlignment(jxl.format.Alignment.CENTRE);
			writableCellFormat3
					.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			writableCellFormat3.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
			Label label3 = new Label(3, 2, "计划");
			wsheet.addCell(label3);
			wsheet.mergeCells(3, 2, 4, 2);
			wsheet.getWritableCell(3, 2).setCellFormat(writableCellFormat3);

			Label label4 = new Label(3, 3, "计划");
			wsheet.addCell(label4);
			wsheet.getWritableCell(3, 3).setCellFormat(writableCellFormat3);

			Label label5 = new Label(4, 3, "占销售净额");
			wsheet.addCell(label5);
			wsheet.getWritableCell(4, 3).setCellFormat(writableCellFormat3);

			Label label6 = new Label(5, 2, "实际");
			wsheet.addCell(label6);
			wsheet.mergeCells(5, 2, 6, 2);
			wsheet.getWritableCell(5, 2).setCellFormat(writableCellFormat3);

			Label label7 = new Label(5, 3, "实际");
			wsheet.addCell(label7);
			wsheet.getWritableCell(5, 3).setCellFormat(writableCellFormat3);

			Label label8 = new Label(6, 3, "占销售净额");
			wsheet.addCell(label8);
			wsheet.getWritableCell(6, 3).setCellFormat(writableCellFormat3);

			Label label9 = new Label(7, 2, "差异");
			wsheet.addCell(label9);
			wsheet.mergeCells(7, 2, 8, 2);
			wsheet.getWritableCell(7, 2).setCellFormat(writableCellFormat3);

			Label label10 = new Label(7, 3, "金额");
			wsheet.addCell(label10);
			wsheet.getWritableCell(7, 3).setCellFormat(writableCellFormat3);

			Label label11 = new Label(8, 3, "比率");
			wsheet.addCell(label11);
			wsheet.getWritableCell(8, 3).setCellFormat(writableCellFormat3);
			// 主体内容生成结束
			wbook.write(); // 写入文件
			wbook.close();
			os.close(); // 关闭流
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("copyFile")
	@ResponseBody
	public void copyFile(HttpServletRequest request) throws Exception {
		List<Map<String, Object>> list = mapper.selectCaseId();
		
		for (Map<String, Object> map : list) {
			// 源文件夹
			String url1 = "F:\\new\\tomcat\\webapps\\judicial-web\\judicial\\"+map.get("case_id")+"\\";
			// 目标文件夹
			String url2 = "E:\\245-999\\"+map.get("case_code")+"\\";
			System.out.println(url1);
			System.out.println(url2);
			// 创建目标文件夹
			(new File(url2)).mkdirs();
			// 获取源文件夹当前下的文件或目录
			File[] file = (new File(url1)).listFiles();
			if(null != file){
				for (int i = 0; i < file.length; i++) {
					if (file[i].isFile()) {
						// 复制文件
						copyFile(file[i], new File(url2 + file[i].getName()));
					}
					if (file[i].isDirectory()) {
						// 复制目录
						String sourceDir = url1 + File.separator + file[i].getName();
						String targetDir = url2 + File.separator + file[i].getName();
						copyDirectiory(sourceDir, targetDir);
					}
				}
			}
		}
	
	}

	// 复制文件
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();

		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}

	// 复制文件夹
	public static void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}
}
