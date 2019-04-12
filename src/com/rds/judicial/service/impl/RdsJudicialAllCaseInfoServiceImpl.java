package com.rds.judicial.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.code.date.DateUtils;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.judicial.mapper.RdsJudicialAllCaseInfoMapper;
import com.rds.judicial.model.RdsJudicialAllCaseInfoModel;
import com.rds.judicial.model.RdsJudicialCaseConfirmTime;
import com.rds.judicial.model.RdsJudicialExperimentModel;
import com.rds.judicial.model.RdsJudicialExportCaseInfoModel;
import com.rds.judicial.model.RdsJudicialParamsModel;
import com.rds.judicial.model.RdsJudicialResponse;
import com.rds.judicial.model.RdsJudicialSampleInfoModel;
import com.rds.judicial.service.RdsJudicialAllCaseInfoService;

@Service("RdsJudicialAllCaseInfoService")
public class RdsJudicialAllCaseInfoServiceImpl implements
		RdsJudicialAllCaseInfoService {
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");
	public static final Map<String, String> mapPlace = new HashMap<>();

	static {
		mapPlace.put("A1", "1");
		mapPlace.put("A2", "9");
		mapPlace.put("A3", "17");
		mapPlace.put("A4", "25");
		mapPlace.put("A5", "33");
		mapPlace.put("A6", "41");
		mapPlace.put("A7", "49");
		mapPlace.put("A8", "57");
		mapPlace.put("A9", "65");
		mapPlace.put("A10", "73");
		mapPlace.put("A11", "81");
		mapPlace.put("A12", "89");
		mapPlace.put("A13", "2");
		mapPlace.put("A14", "10");
		mapPlace.put("A15", "18");
		mapPlace.put("A16", "26");
		mapPlace.put("A17", "34");
		mapPlace.put("A18", "42");
		mapPlace.put("A19", "50");
		mapPlace.put("A20", "58");
		mapPlace.put("A21", "66");
		mapPlace.put("A22", "74");
		mapPlace.put("A23", "82");
		mapPlace.put("A24", "90");
		mapPlace.put("A25", "3");
		mapPlace.put("A26", "11");
		mapPlace.put("A27", "19");
		mapPlace.put("A28", "27");
		mapPlace.put("A29", "35");
		mapPlace.put("A30", "43");
		mapPlace.put("A31", "51");
		mapPlace.put("A32", "59");
		mapPlace.put("A33", "67");
		mapPlace.put("A34", "75");
		mapPlace.put("A35", "83");
		mapPlace.put("A36", "91");
		mapPlace.put("A37", "4");
		mapPlace.put("A38", "12");
		mapPlace.put("A39", "20");
		mapPlace.put("A40", "28");
		mapPlace.put("A41", "36");
		mapPlace.put("A42", "44");
		mapPlace.put("A43", "52");
		mapPlace.put("A44", "60");
		mapPlace.put("A45", "68");
		mapPlace.put("A46", "76");
		mapPlace.put("A47", "84");
		mapPlace.put("A48", "92");
		mapPlace.put("A49", "5");
		mapPlace.put("A50", "13");
		mapPlace.put("A51", "21");
		mapPlace.put("A52", "29");
		mapPlace.put("A53", "37");
		mapPlace.put("A54", "45");
		mapPlace.put("A55", "53");
		mapPlace.put("A56", "61");
		mapPlace.put("A57", "69");
		mapPlace.put("A58", "77");
		mapPlace.put("A59", "85");
		mapPlace.put("A60", "93");
		mapPlace.put("A61", "6");
		mapPlace.put("A62", "14");
		mapPlace.put("A63", "22");
		mapPlace.put("A64", "30");
		mapPlace.put("A65", "38");
		mapPlace.put("A66", "46");
		mapPlace.put("A67", "54");
		mapPlace.put("A68", "62");
		mapPlace.put("A69", "70");
		mapPlace.put("A70", "78");
		mapPlace.put("A71", "86");
		mapPlace.put("A72", "94");
		mapPlace.put("A73", "7");
		mapPlace.put("A74", "15");
		mapPlace.put("A75", "23");
		mapPlace.put("A76", "31");
		mapPlace.put("A77", "39");
		mapPlace.put("A78", "47");
		mapPlace.put("A79", "55");
		mapPlace.put("A80", "63");
		mapPlace.put("A81", "71");
		mapPlace.put("A82", "79");
		mapPlace.put("A83", "87");
		mapPlace.put("A84", "95");
		mapPlace.put("A85", "8");
		mapPlace.put("A86", "16");
		mapPlace.put("A87", "24");
		mapPlace.put("A88", "32");
		mapPlace.put("A89", "40");
		mapPlace.put("A90", "48");
		mapPlace.put("A91", "56");
		mapPlace.put("A92", "64");
		mapPlace.put("A93", "72");
		mapPlace.put("A94", "80");
		mapPlace.put("A95", "88");
		mapPlace.put("A96", "96");
	}
	@Autowired
	private RdsJudicialAllCaseInfoMapper aciMapper;

	@Override
	public Map<String, Object> getAllCaseInfo(Map<String, Object> params) {
		List<RdsJudicialAllCaseInfoModel> acilist = aciMapper
				.getAllCaseInfo(params);
		int count = aciMapper.getAllCaseInfoCount(params);
		params.clear();
		params.put("data", acilist);
		params.put("total", count);
		return params;
	}

	@Override
	public void exportSampleInfo(RdsJudicialParamsModel params,
			HttpServletResponse response) {
		String filename = "样本列表";
		// 根据查询条件查询案例信息
		List<RdsJudicialExportCaseInfoModel> caseInfoModels = aciMapper
				.queryAllCaseInfo(params);
		// List<RdsJudicialSampleInfoModel> parentSampleInfoslist =
		// aciMapper.queryParentSampleInfoList(caseInfoModels);
		// List<RdsJudicialSampleInfoModel> childSampleInfoslist =
		// aciMapper.queryChildSampleInfoList(caseInfoModels);
		// excel表格列头
		Object[] titles = { "案例编号", "委托日期", "登记日期", "父母亲", "身份证", "孩子", "出生日期",
				"应收款项", "所付款项", "汇款时间", "确认时间", "是否检验报告", "所属分公司", "员工名字",
				"代理", "财务类型", "账户类型", "财务备注", "财务是否确认", "备注和要求", "电话", "资料&照片",
				"样本数", "报告日期&快递&收件人", "送样本时间", "未出原因", "开票时间", "开票金额", "发票号码",
				"合作方" };
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		// 循环案例列表拼装表格一行
		for (int i = 0; i < caseInfoModels.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			// // 根据案例查询父母样本信息
			// List<RdsJudicialSampleInfoModel> parentSampleInfos = new
			// LinkedList<RdsJudicialSampleInfoModel>();
			// for (RdsJudicialSampleInfoModel m : parentSampleInfoslist){
			// if(m.getCase_id().equals(caseInfoModels.get(i).getCase_id())){
			// parentSampleInfos.add(m);
			// }
			// }
			//
			// // 根据案例查询孩子样本信息
			// List<RdsJudicialSampleInfoModel> childSampleInfos = new
			// LinkedList<RdsJudicialSampleInfoModel>();
			// for(RdsJudicialSampleInfoModel m : childSampleInfoslist){
			// if (m.getCase_id().equals(caseInfoModels.get(i).getCase_id())){
			// childSampleInfos.add(m);
			// }
			// }
			// // 表格中父母样本信息列
			// String sample_info_parent = "";
			// // 身份证列
			// String sample_info_id_number = "";
			// // 孩子信息列
			// String sample_info_child = "";
			// // 孩子生日列
			// String sample_info_child_birth = "";
			// // 拼装列信息
			// // 父母顺序排序，根据样本编号后面的 F （父亲） M（母亲）排，F在前M在后
			// if (parentSampleInfos.size() > 1) {
			// // 比较器
			// Collections.sort(parentSampleInfos,
			// new Comparator<RdsJudicialSampleInfoModel>() {
			// @Override
			// public int compare(RdsJudicialSampleInfoModel o1,
			// RdsJudicialSampleInfoModel o2) {
			// if (o1.getSample_code()
			// .substring(
			// o1.getSample_code().length() - 1)
			// .charAt(0) < o2
			// .getSample_code()
			// .substring(
			// o2.getSample_code().length() - 1)
			// .charAt(0))
			// return -1;
			// else
			// return 1;
			// }
			// });
			// }
			//
			// for (RdsJudicialSampleInfoModel sampleInfoModel :
			// parentSampleInfos) {
			//
			// if (StringUtils
			// .isNotEmpty(sampleInfoModel.getSample_callname())) {
			// sample_info_parent += sampleInfoModel.getSample_callname();
			// }
			// if (StringUtils
			// .isNotEmpty(sampleInfoModel.getSample_username())) {
			// sample_info_parent += "-"
			// + sampleInfoModel.getSample_username();
			// }
			// if (StringUtils.isNotEmpty(sampleInfoModel.getSample_type())) {
			// sample_info_parent += "-"
			// + sampleInfoModel.getSample_typename();
			// }
			// if (StringUtils.isNotEmpty(sampleInfoModel.getId_number())) {
			// sample_info_id_number += sampleInfoModel.getId_number();
			// sample_info_id_number += ";";
			// }
			// if (StringUtils.isNotEmpty(sampleInfoModel.getBirth_date())) {
			// sample_info_child_birth += DateUtils
			// .DateString2DateString(sampleInfoModel
			// .getBirth_date());
			// sample_info_child_birth += ";";
			// }
			// sample_info_parent += ";";
			// }
			//
			//
			// // 孩子顺序排序，根据样本编号后面的ABC排序
			// if(childSampleInfos.size()>1){
			// Collections.sort(childSampleInfos, new
			// Comparator<RdsJudicialSampleInfoModel>() {
			//
			// @Override
			// public int compare(RdsJudicialSampleInfoModel o1,
			// RdsJudicialSampleInfoModel o2) {
			// if (o1.getSample_code()
			// .substring(
			// o1.getSample_code().length() - 1)
			// .charAt(0) < o2
			// .getSample_code()
			// .substring(
			// o2.getSample_code().length() - 1)
			// .charAt(0))
			// return -1;
			// else
			// return 1;
			// }
			// });
			// }
			// // 拼装孩子列
			// for (RdsJudicialSampleInfoModel sampleInfoModel :
			// childSampleInfos) {
			// if (StringUtils
			// .isNotEmpty(sampleInfoModel.getSample_callname())) {
			// sample_info_child += sampleInfoModel.getSample_callname();
			// }
			// if (StringUtils
			// .isNotEmpty(sampleInfoModel.getSample_username())) {
			// sample_info_child += "-"
			// + sampleInfoModel.getSample_username();
			// }
			// if (StringUtils.isNotEmpty(sampleInfoModel.getSample_type())) {
			// sample_info_child += "-"
			// + sampleInfoModel.getSample_typename();
			// }
			// if (StringUtils.isNotEmpty(sampleInfoModel.getBirth_date())) {
			// sample_info_child_birth += DateUtils
			// .DateString2DateString(sampleInfoModel
			// .getBirth_date());
			// sample_info_child_birth += ";";
			// }
			// if (StringUtils.isNotEmpty(sampleInfoModel.getId_number())) {
			// sample_info_id_number += sampleInfoModel.getId_number();
			// sample_info_id_number += ";";
			// }
			// sample_info_child += ";";
			// }
			// 案例编号
			objects.add(caseInfoModels.get(i).getCase_code());
			// 委托日期
			if (null == caseInfoModels.get(i).getConsignment_time())
				objects.add("");
			else
				objects.add(DateUtils.DateString2DateString(caseInfoModels.get(
						i).getConsignment_time()));
			// 登记日期
			if (null == caseInfoModels.get(i).getUpdate_date())
				objects.add("");
			else
				objects.add(DateUtils.DateString2DateString(caseInfoModels.get(
						i).getUpdate_date()));
			// 父母
			objects.add(caseInfoModels.get(i).getFandm());
			// 身份证
			objects.add(caseInfoModels.get(i).getId_card());
			// 孩子
			objects.add(caseInfoModels.get(i).getChild());
			// 生日
			objects.add(caseInfoModels.get(i).getBirth_date());
			// 应收款项
			objects.add(("".equals(caseInfoModels.get(i).getReal_sum()) || null == caseInfoModels
					.get(i).getReal_sum()) ? 0 : Float
					.parseFloat(caseInfoModels.get(i).getReal_sum()));

			// 所付款项
			objects.add(("".equals(caseInfoModels.get(i).getReturn_sum()) || null == caseInfoModels
					.get(i).getReturn_sum()) ? 0 : Float
					.parseFloat(caseInfoModels.get(i).getReturn_sum()));
			// 汇款时间
			objects.add(caseInfoModels.get(i).getRemittanceDate());
			// 确认时间
			objects.add(caseInfoModels.get(i).getConfirm_date());
			// 是否检验报告
			objects.add("zyjymodel".equals(caseInfoModels.get(i)
					.getReport_model()) ? "检验" : "");
			// 所属分公司
			String address = StringUtils.isEmpty(caseInfoModels.get(i)
					.getReceiver_area()) ? "" : caseInfoModels.get(i)
					.getReceiver_area() + "-";
			String name = StringUtils.isEmpty(caseInfoModels.get(i)
					.getCase_receiver()) ? "" : caseInfoModels.get(i)
					.getCase_receiver();
			objects.add(address + name);
			if (StringUtils.isNotEmpty(caseInfoModels.get(i).getAgent())) {
				name = caseInfoModels.get(i).getAgent();
			}
			// 员工名字
			objects.add(name);
			// 代理名字
			if (StringUtils.isNotEmpty(caseInfoModels.get(i).getAgent()))
				name = caseInfoModels.get(i).getCase_receiver();
			else
				name = "";
			objects.add(name);
			String type = caseInfoModels.get(i).getType();
			// 财务类型
			objects.add("0".equals(type) ? "正常"
					: ("1".equals(type) ? "为先出报告后付款" : ("2".equals(type) ? "免单"
							: ("3".equals(type) ? "优惠"
									: ("4".equals(type) ? "月结" : ("5"
											.equals(type) ? "二次采样" : "补样"))))));
			// 账户类型
			objects.add(caseInfoModels.get(i).getAccount());
			// 财务备注
			objects.add(caseInfoModels.get(i).getFinanceRemark());
			// 案例财务确认
			objects.add(("0".equals(caseInfoModels.get(i).getFinanceStatus()) ? "是"
					: "否"));
			// 备注和要求
			objects.add(caseInfoModels.get(i).getRemark());
			// 电话
			objects.add(caseInfoModels.get(i).getPhone());
			// 资料&照片
			objects.add("");
			// 样本数
			objects.add(caseInfoModels.get(i).getSample_count());
			// 报告日期&快递
			objects.add(caseInfoModels.get(i).getMail_info());
			// 送样本时间
			objects.add("");
			// 未出原因
			objects.add("");
			// 开票时间
			objects.add("");
			// 开票金额
			objects.add("");
			// 发票号码
			objects.add("");
			objects.add(caseInfoModels.get(i).getParnter_name());
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "亲子鉴定-宿迁子渊"
				+ DateUtils.Date2String(new Date()));
	}

	@Override
	public void exportCaseInfoNoFinance(RdsJudicialParamsModel params,
			HttpServletResponse response) {
		String filename = "样本列表";
		// 根据查询条件查询案例信息
		List<RdsJudicialExportCaseInfoModel> caseInfoModels = aciMapper
				.queryAllCaseInfo(params);
		// List<RdsJudicialSampleInfoModel> parentSampleInfoslist =
		// aciMapper.queryParentSampleInfoList(caseInfoModels);
		// List<RdsJudicialSampleInfoModel> childSampleInfoslist =
		// aciMapper.queryChildSampleInfoList(caseInfoModels);
		// excel表格列头
		Object[] titles = { "案例编号", "受理日期", "登记日期", "父母亲", "身份证", "孩子", "出生日期",
				"应收款项", "所付款项", "到款时间", "是否检验报告", "所属分公司", "员工名字", "代理",
				"备注和要求", "地址", "电话", "资料&照片", "样本数", "报告日期&快递", "送样本时间",
				"未出原因", "开票时间", "开票金额", "发票号码" };
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		// 循环案例列表拼装表格一行
		for (int i = 0; i < caseInfoModels.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			// 根据案例查询父母样本信息
			// List<RdsJudicialSampleInfoModel> parentSampleInfos = new
			// LinkedList<RdsJudicialSampleInfoModel>();
			// for (RdsJudicialSampleInfoModel m : parentSampleInfoslist){
			// if(m.getCase_id().equals(caseInfoModels.get(i).getCase_id())){
			// parentSampleInfos.add(m);
			// }
			// }
			// // 根据案例查询孩子样本信息
			// List<RdsJudicialSampleInfoModel> childSampleInfos = new
			// LinkedList<RdsJudicialSampleInfoModel>();
			// for(RdsJudicialSampleInfoModel m : childSampleInfoslist){
			// if (m.getCase_id().equals(caseInfoModels.get(i).getCase_id())){
			// childSampleInfos.add(m);
			// }
			// }
			// // 表格中父母样本信息列
			// String sample_info_parent = "";
			// // 身份证列
			// String sample_info_id_number = "";
			// // 孩子信息列
			// String sample_info_child = "";
			// // 孩子生日列
			// String sample_info_child_birth = "";
			// // 拼装列信息
			// // 父母顺序排序，根据样本编号后面的 F （父亲） M（母亲）排，F在前M在后
			// if (parentSampleInfos.size() > 1) {
			// // 比较器
			// Collections.sort(parentSampleInfos,
			// new Comparator<RdsJudicialSampleInfoModel>() {
			// @Override
			// public int compare(RdsJudicialSampleInfoModel o1,
			// RdsJudicialSampleInfoModel o2) {
			// if (o1.getSample_code()
			// .substring(
			// o1.getSample_code().length() - 1)
			// .charAt(0) < o2
			// .getSample_code()
			// .substring(
			// o2.getSample_code().length() - 1)
			// .charAt(0))
			// return -1;
			// else
			// return 1;
			// }
			// });
			// }
			//
			// for (RdsJudicialSampleInfoModel sampleInfoModel :
			// parentSampleInfos) {
			//
			// if (StringUtils
			// .isNotEmpty(sampleInfoModel.getSample_callname())) {
			// sample_info_parent += sampleInfoModel.getSample_callname();
			// }
			// if (StringUtils
			// .isNotEmpty(sampleInfoModel.getSample_username())) {
			// sample_info_parent += "-"
			// + sampleInfoModel.getSample_username();
			// }
			// if (StringUtils.isNotEmpty(sampleInfoModel.getSample_type())) {
			// sample_info_parent += "-"
			// + sampleInfoModel.getSample_typename();
			// }
			// if (StringUtils.isNotEmpty(sampleInfoModel.getId_number())) {
			// sample_info_id_number += sampleInfoModel.getId_number();
			// sample_info_id_number += ";";
			// }
			// if (StringUtils.isNotEmpty(sampleInfoModel.getBirth_date())) {
			// sample_info_child_birth += DateUtils
			// .DateString2DateString(sampleInfoModel
			// .getBirth_date());
			// sample_info_child_birth += ";";
			// }
			// sample_info_parent += ";";
			// }
			//
			//
			// // 孩子顺序排序，根据样本编号后面的ABC排序
			// if(childSampleInfos.size()>1){
			// Collections.sort(childSampleInfos, new
			// Comparator<RdsJudicialSampleInfoModel>() {
			//
			// @Override
			// public int compare(RdsJudicialSampleInfoModel o1,
			// RdsJudicialSampleInfoModel o2) {
			// if (o1.getSample_code()
			// .substring(
			// o1.getSample_code().length() - 1)
			// .charAt(0) < o2
			// .getSample_code()
			// .substring(
			// o2.getSample_code().length() - 1)
			// .charAt(0))
			// return -1;
			// else
			// return 1;
			// }
			// });
			// }
			// // 拼装孩子列
			// for (RdsJudicialSampleInfoModel sampleInfoModel :
			// childSampleInfos) {
			// if (StringUtils
			// .isNotEmpty(sampleInfoModel.getSample_callname())) {
			// sample_info_child += sampleInfoModel.getSample_callname();
			// }
			// if (StringUtils
			// .isNotEmpty(sampleInfoModel.getSample_username())) {
			// sample_info_child += "-"
			// + sampleInfoModel.getSample_username();
			// }
			// if (StringUtils.isNotEmpty(sampleInfoModel.getSample_type())) {
			// sample_info_child += "-"
			// + sampleInfoModel.getSample_typename();
			// }
			// if (StringUtils.isNotEmpty(sampleInfoModel.getBirth_date())) {
			// sample_info_child_birth += DateUtils
			// .DateString2DateString(sampleInfoModel
			// .getBirth_date());
			// sample_info_child_birth += ";";
			// }
			// if (StringUtils.isNotEmpty(sampleInfoModel.getId_number())) {
			// sample_info_id_number += sampleInfoModel.getId_number();
			// sample_info_id_number += ";";
			// }
			// sample_info_child += ";";
			// }
			// 案例编号
			objects.add(caseInfoModels.get(i).getCase_code());
			// 受理日期
			if (null == caseInfoModels.get(i).getAccept_time())
				objects.add("");
			else
				objects.add(DateUtils.DateString2DateString(caseInfoModels.get(
						i).getAccept_time()));
			// 登记日期
			if (null == caseInfoModels.get(i).getUpdate_date())
				objects.add("");
			else
				objects.add(DateUtils.DateString2DateString(caseInfoModels.get(
						i).getUpdate_date()));
			// 父母
			objects.add(caseInfoModels.get(i).getFandm());
			// 身份证
			objects.add(caseInfoModels.get(i).getId_card());
			// 孩子
			objects.add(caseInfoModels.get(i).getChild());
			// 生日
			objects.add(caseInfoModels.get(i).getBirth_date());
			objects.add("");
			objects.add("");
			objects.add("");
			// 是否检验报告
			objects.add("zyjymodel".equals(caseInfoModels.get(i)
					.getReport_model()) ? "检验" : "");
			// 所属分公司
			String address = StringUtils.isEmpty(caseInfoModels.get(i)
					.getReceiver_area()) ? "" : caseInfoModels.get(i)
					.getReceiver_area() + "-";
			String name = StringUtils.isEmpty(caseInfoModels.get(i)
					.getCase_receiver()) ? "" : caseInfoModels.get(i)
					.getCase_receiver();
			objects.add(address + name);
			if (StringUtils.isNotEmpty(caseInfoModels.get(i).getAgent())) {
				name = caseInfoModels.get(i).getAgent();
			}
			// 员工名字
			objects.add(name);
			// 代理名字
			if (StringUtils.isNotEmpty(caseInfoModels.get(i).getAgent()))
				name = caseInfoModels.get(i).getCase_receiver();
			else
				name = "";
			objects.add(name);
			// 备注和要求
			objects.add(caseInfoModels.get(i).getRemark());
			// 地址
			objects.add(caseInfoModels.get(i).getCase_area());
			// 电话
			objects.add(caseInfoModels.get(i).getPhone());
			// 资料&照片
			objects.add("");
			// 样本数
			objects.add(caseInfoModels.get(i).getSample_count());
			// 报告日期&快递
			objects.add(caseInfoModels.get(i).getMail_info());
			// 送样本时间
			objects.add("");
			// 未出原因
			objects.add("");
			// 开票时间
			objects.add("");
			// 开票金额
			objects.add("");
			// 发票号码
			objects.add("");
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "亲子鉴定-宿迁子渊"
				+ DateUtils.Date2String(new Date()));
	}

	@Override
	public List<RdsJudicialExperimentModel> queryPlaceBySamplecode(
			Map<String, Object> params) {
		List<RdsJudicialExperimentModel> list = aciMapper
				.queryPlaceBySamplecode(params);
		for (RdsJudicialExperimentModel rdsJudicialExperimentModel : list) {
			if (!rdsJudicialExperimentModel.getPlaces().startsWith("A")) {
				String place = "A" + rdsJudicialExperimentModel.getPlaces();
				rdsJudicialExperimentModel.setPlaces(mapPlace.get(place));
			} else
				rdsJudicialExperimentModel.setPlaces(mapPlace
						.get(rdsJudicialExperimentModel.getPlaces()));
		}
		return list;
	}

	@Override
	public void exportCaseInfo(Map<String, Object> params,
			HttpServletResponse response) {

		String filename = "案例列表";
		// 根据查询条件查询案例信息
		List<RdsJudicialExportCaseInfoModel> caseInfoModels = aciMapper
				.queryExportCaseInfo(params);
		if (FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			Object[] titles = { "案例编号", "物鉴字号", "受理日期", "登记日期", "父母亲", "身份证",
					"孩子", "出生日期", "应收款项", "所付款项", "汇款时间", "确认时间", "是否检验报告",
					"所属分公司", "员工名字", "代理", "采样员", "财务类型", "账户类型", "财务备注",
					"财务是否确认", "备注和要求", "电话", "资料&照片", "样本数", "报告日期&快递&收件人",
					"送样本时间", "未出原因", "开票时间", "开票金额", "发票号码", "合作方" };
			// 表格实体
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			// 循环案例列表拼装表格一行
			for (int i = 0; i < caseInfoModels.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				// 案例编号
				objects.add(caseInfoModels.get(i).getCase_code());
				objects.add(caseInfoModels.get(i).getSerial_number());
				// 受理日期
				if (null == caseInfoModels.get(i).getAccept_time())
					objects.add("");
				else
					objects.add(DateUtils.DateString2DateString(caseInfoModels
							.get(i).getAccept_time()));
				// 登记日期
				if (null == caseInfoModels.get(i).getUpdate_date())
					objects.add("");
				else
					objects.add(DateUtils.DateString2DateString(caseInfoModels
							.get(i).getUpdate_date()));
				// 父母
				objects.add(caseInfoModels.get(i).getFandm());
				// 身份证
				objects.add(caseInfoModels.get(i).getId_card());
				// 孩子
				objects.add(caseInfoModels.get(i).getChild());
				// 生日
				objects.add(caseInfoModels.get(i).getBirth_date());
				// 应收款项
				objects.add(("".equals(caseInfoModels.get(i).getReal_sum()) || null == caseInfoModels
						.get(i).getReal_sum()) ? 0 : caseInfoModels.get(i)
						.getReal_sum());

				// 所付款项
				objects.add(("".equals(caseInfoModels.get(i).getReturn_sum()) || null == caseInfoModels
						.get(i).getReturn_sum()) ? 0 : caseInfoModels.get(i)
						.getReturn_sum());
				// 汇款时间
				objects.add(caseInfoModels.get(i).getRemittanceDate());
				// 汇款时间
				objects.add(caseInfoModels.get(i).getConfirm_date());
				// 是否检验报告
				objects.add("zyjymodel".equals(caseInfoModels.get(i)
						.getReport_model()) ? "检验" : "");
				// 所属分公司
				String address = StringUtils.isEmpty(caseInfoModels.get(i)
						.getReceiver_area()) ? "" : caseInfoModels.get(i)
						.getReceiver_area() + "-";
				String name = StringUtils.isEmpty(caseInfoModels.get(i)
						.getCase_receiver()) ? "" : caseInfoModels.get(i)
						.getCase_receiver();
				objects.add(address + name);
				if (StringUtils.isNotEmpty(caseInfoModels.get(i).getAgent())) {
					name = caseInfoModels.get(i).getAgent();
				}
				// 员工名字
				objects.add(name);
				// 代理名字
				if (StringUtils.isNotEmpty(caseInfoModels.get(i).getAgent()))
					name = caseInfoModels.get(i).getCase_receiver();
				else
					name = "";
				objects.add(name);
				// 采样员名字
				objects.add(caseInfoModels.get(i).getSample_in_per());
				String type = caseInfoModels.get(i).getType();
				// 财务类型
				objects.add("0".equals(type) ? "正常"
						: ("1".equals(type) ? "为先出报告后付款"
								: ("2".equals(type) ? "免单"
										: ("3".equals(type) ? "优惠" : ("4"
												.equals(type) ? "月结" : ("5"
												.equals(type) ? "二次采样" : "补样"))))));
				// 账户类型
				objects.add(caseInfoModels.get(i).getAccount());
				// 财务备注
				objects.add(caseInfoModels.get(i).getFinanceRemark());
				// 案例财务确认
				objects.add(("0".equals(caseInfoModels.get(i)
						.getFinanceStatus()) ? "是" : "否"));
				// 备注和要求
				objects.add(caseInfoModels.get(i).getRemark());
				// 电话
				objects.add(caseInfoModels.get(i).getPhone());
				// 资料&照片
				objects.add("");
				// 样本数
				objects.add("dna".equals(caseInfoModels.get(i).getCase_type()) ? caseInfoModels
						.get(i).getSample_count() : 0);
				// 报告日期&快递
				objects.add(caseInfoModels.get(i).getMail_info());
				// 送样本时间
				objects.add("");
				// 未出原因
				objects.add("");
				// 开票时间
				objects.add("");
				// 开票金额
				objects.add("");
				// 发票号码
				objects.add("");
				objects.add(caseInfoModels.get(i).getParnter_name());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "亲子鉴定"
					+ DateUtils.Date2String(new Date()));
		} else {
			Object[] titles = { "案例编号", "物鉴字号", "受理日期", "登记日期", "父母亲", "身份证",
					"孩子", "出生日期", "是否检验报告", "所属分公司", "员工名字", "代理", "采样员",
					"备注和要求", "电话", "资料&照片", "样本数", "报告日期&快递&收件人", "送样本时间",
					"未出原因", "开票时间", "开票金额", "发票号码", "合作方" };
			// 表格实体
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			// 循环案例列表拼装表格一行
			for (int i = 0; i < caseInfoModels.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				// 案例编号
				objects.add(caseInfoModels.get(i).getCase_code());
				objects.add(caseInfoModels.get(i).getSerial_number());
				// 受理日期
				if (null == caseInfoModels.get(i).getAccept_time())
					objects.add("");
				else
					objects.add(DateUtils.DateString2DateString(caseInfoModels
							.get(i).getAccept_time()));
				// 登记日期
				if (null == caseInfoModels.get(i).getUpdate_date())
					objects.add("");
				else
					objects.add(DateUtils.DateString2DateString(caseInfoModels
							.get(i).getUpdate_date()));
				// 父母
				objects.add(caseInfoModels.get(i).getFandm());
				// 身份证
				objects.add(caseInfoModels.get(i).getId_card());
				// 孩子
				objects.add(caseInfoModels.get(i).getChild());
				// 生日
				objects.add(caseInfoModels.get(i).getBirth_date());
				// 是否检验报告
				objects.add("zyjymodel".equals(caseInfoModels.get(i)
						.getReport_model()) ? "检验" : "");
				// 所属分公司
				String address = StringUtils.isEmpty(caseInfoModels.get(i)
						.getReceiver_area()) ? "" : caseInfoModels.get(i)
						.getReceiver_area() + "-";
				String name = StringUtils.isEmpty(caseInfoModels.get(i)
						.getCase_receiver()) ? "" : caseInfoModels.get(i)
						.getCase_receiver();
				objects.add(address + name);
				if (StringUtils.isNotEmpty(caseInfoModels.get(i).getAgent())) {
					name = caseInfoModels.get(i).getAgent();
				}
				// 员工名字
				objects.add(name);
				// 代理名字
				if (StringUtils.isNotEmpty(caseInfoModels.get(i).getAgent()))
					name = caseInfoModels.get(i).getCase_receiver();
				else
					name = "";
				objects.add(name);
				// 采样员名字
				objects.add(caseInfoModels.get(i).getSample_in_per());
				// 备注和要求
				objects.add(caseInfoModels.get(i).getRemark());
				// 电话
				objects.add(caseInfoModels.get(i).getPhone());
				// 资料&照片
				objects.add("");
				// 样本数
				objects.add("dna".equals(caseInfoModels.get(i).getCase_type()) ? caseInfoModels
						.get(i).getSample_count() : 0);
				// 报告日期&快递
				objects.add(caseInfoModels.get(i).getMail_info());
				// 送样本时间
				objects.add("");
				// 未出原因
				objects.add("");
				// 开票时间
				objects.add("");
				// 开票金额
				objects.add("");
				// 发票号码
				objects.add("");
				objects.add(caseInfoModels.get(i).getParnter_name());
				bodys.add(objects);
			}

			ExportUtils.export(response, filename, titles, bodys, "亲子鉴定"
					+ DateUtils.Date2String(new Date()));
		}

	}

	@Override
	public Map<String, Object> queryExportCaseInfo(Map<String, Object> params) {
		List<RdsJudicialExportCaseInfoModel> acilist = aciMapper
				.queryExportCaseInfo(params);
		int count = aciMapper.queryCountExportCaseInfo(params);
		params.clear();
		params.put("data", acilist);
		params.put("total", count);
		return params;
	}

	@Override
	public void exportPartnerAllCaseInfo(Map<String, Object> params,
			HttpServletResponse response) {
		String filename = "案例列表";
		// 根据查询条件查询案例信息
		List<RdsJudicialExportCaseInfoModel> caseInfoModels = aciMapper
				.queryPartnerAllCaseInfo(params);
		Object[] titles = { "案例编号", "物鉴字号", "日期", "父母亲", "身份证", "孩子", "出生日期",
				"所属分公司", "员工名字", "备注和要求", "电话", "资料&照片", "样本数", "报告日期&快递&收件人",
				"送样本时间", "未出原因", "合作方" };
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		// 循环案例列表拼装表格一行
		for (int i = 0; i < caseInfoModels.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			// 案例编号
			objects.add(caseInfoModels.get(i).getCase_code());
			// 物鉴字号
			objects.add(caseInfoModels.get(i).getSerial_number());
			// 受理日期
			objects.add(DateUtils.DateString2DateString(caseInfoModels.get(i)
					.getAccept_time()));
			// 父母
			objects.add(caseInfoModels.get(i).getFandm());
			// 身份证
			objects.add(caseInfoModels.get(i).getId_card());
			// 孩子
			objects.add(caseInfoModels.get(i).getChild());
			// 生日
			objects.add(caseInfoModels.get(i).getBirth_date());
			// 所属分公司
			String address = StringUtils.isEmpty(caseInfoModels.get(i)
					.getReceiver_area()) ? "" : caseInfoModels.get(i)
					.getReceiver_area() + "-";
			String name = StringUtils.isEmpty(caseInfoModels.get(i)
					.getCase_receiver()) ? "" : caseInfoModels.get(i)
					.getCase_receiver();
			objects.add(address + name);
			if (StringUtils.isNotEmpty(caseInfoModels.get(i).getAgent())) {
				name = caseInfoModels.get(i).getAgent();
			}
			// 员工名字
			objects.add(name);
			// 备注和要求
			objects.add(caseInfoModels.get(i).getRemark());
			// 电话
			objects.add(caseInfoModels.get(i).getPhone());
			// 资料&照片
			objects.add("");
			// 样本数
			objects.add(caseInfoModels.get(i).getSample_count());
			// 报告日期&快递
			objects.add(caseInfoModels.get(i).getMail_info());
			// 送样本时间
			objects.add("");
			// 未出原因
			objects.add("");
			objects.add(caseInfoModels.get(i).getParnter_name());
			bodys.add(objects);
		}

		ExportUtils.export(response, filename, titles, bodys, "亲子鉴定"
				+ DateUtils.Date2String(new Date()));

	}

	@Override
	public RdsJudicialResponse getSampleInfo(Map<String, Object> params) {
		RdsJudicialResponse resJudicialResponse = new RdsJudicialResponse();
		List<RdsJudicialSampleInfoModel> sampleInfoModels = aciMapper
				.getExceptionSampleInfo(params);
		resJudicialResponse.setItems(sampleInfoModels);
		return resJudicialResponse;
	}

	@Override
	public void exportMessageCaseInfo(Map<String, Object> params,
			HttpServletResponse response) {
		String filename = "案例列表";
		// 根据查询条件查询案例信息
		List<RdsJudicialExportCaseInfoModel> caseInfoModels = aciMapper
				.queryMessageCase(params);
		Object[] titles = { "案例编号", "日期", "委托人", "电话", "归属人", "归属地", "不通过信息" };
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		// 循环案例列表拼装表格一行
		for (int i = 0; i < caseInfoModels.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			// 案例编号
			objects.add(caseInfoModels.get(i).getCase_code());
			// 受理日期
			objects.add(DateUtils.DateString2DateString(caseInfoModels.get(i)
					.getAccept_time()));
			objects.add(caseInfoModels.get(i).getClient());
			objects.add(caseInfoModels.get(i).getPhone());
			objects.add(caseInfoModels.get(i).getCase_receiver());
			objects.add(caseInfoModels.get(i).getReceiver_area());
			objects.add(caseInfoModels.get(i).getRemark());
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "亲子鉴定"
				+ DateUtils.Date2String(new Date()));

	}

	@Override
	public Map<String, Object> queryFMChild(Map<String, Object> params) {
		List<Map<String, String>> list = aciMapper.queryFMChild(params);
		int count = aciMapper.queryFMChildCount(params);
		params.clear();
		params.put("data", list);
		params.put("total", count);
		return params;
	}

	@Override
	public int queryFMChildCount(Map<String, Object> params) {
		return aciMapper.queryFMChildCount(params);
	}

	@Override
	public void exportFMChild(Map<String, Object> params,
			HttpServletResponse response) {
		String filename = "案例列表";
		// 根据查询条件查询案例信息
		List<Map<String, String>> list = aciMapper.queryFMChild(params);
		Object[] titles = { "案例编号", "受理日期", "委托日期", "父母亲", "父母亲身份证", "父母亲出身日期",
				"孩子", "孩子身份证", "孩子出生日期" };
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		// 循环案例列表拼装表格一行
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			String accept_time = list.get(i).get("accept_time");
			String consignment_time = list.get(i).get("consignment_time");
			// 案例编号
			objects.add(list.get(i).get("case_code").toString());
			// 受理日期
			objects.add(DateUtils.DateString2DateString(accept_time));
			objects.add(DateUtils.DateString2DateString(consignment_time));
			objects.add(list.get(i).get("fm") == null ? "" : list.get(i)
					.get("fm").toString());
			objects.add(list.get(i).get("fmid") == null ? "" : list.get(i)
					.get("fmid").toString());
			objects.add(list.get(i).get("fmbirth") == null ? "" : list.get(i)
					.get("fmbirth").toString());
			objects.add(list.get(i).get("child") == null ? "" : list.get(i)
					.get("child").toString());
			objects.add(list.get(i).get("childid") == null ? "" : list.get(i)
					.get("childid").toString());
			objects.add(list.get(i).get("childbirth") == null ? "" : list
					.get(i).get("childbirth").toString());
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "案例列表"
				+ DateUtils.Date2String(new Date()));
	}

	@Override
	public Map<String, Object> queryExperimentInfo(Map<String, Object> params) {
		List<Map<String, String>> list = aciMapper.queryExperimentInfo(params);
		int count = aciMapper.queryExperimentCount(params);
		params.clear();
		params.put("data", list);
		params.put("total", count);
		return params;
	}

	@Override
	public int queryExperimentCount(Map<String, Object> params) {
		return aciMapper.queryExperimentCount(params);
	}

	@Override
	public void exportExperimentInfo(Map<String, Object> params,
			HttpServletResponse response) {
		String filename = "案例列表";
		// 根据查询条件查询案例信息
		List<Map<String, String>> list = aciMapper.queryExperimentInfo(params);
		Object[] titles = { "案例编号", "受理日期", "样本确认日期", "实验日期", "报告确认日期",
				"是否存在加位点", "是否存在否定案例", "合作方" };
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		// 循环案例列表拼装表格一行
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			String accept_time = list.get(i).get("accept_time");
			// 案例编号
			objects.add(list.get(i).get("case_code").toString());
			// 受理日期
			objects.add(DateUtils.DateString2DateString(accept_time));
			objects.add(list.get(i).get("sampleConfirm"));
			objects.add(list.get(i).get("experiment"));
			objects.add(list.get(i).get("reportConfirm"));
			objects.add(list.get(i).get("reagent_name_ext") == null ? "" : list
					.get(i).get("reagent_name_ext").toString());
			objects.add(list.get(i).get("result") == null ? "" : list.get(i)
					.get("result").toString());
			objects.add(list.get(i).get("parnter_name") == null ? "" : list
					.get(i).get("parnter_name").toString());
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "案例列表"
				+ DateUtils.Date2String(new Date()));
	}

	@Override
	public Map<String, Object> queryConfirmTimePage(Map<String, Object> params) {
		List<RdsJudicialCaseConfirmTime> list = aciMapper
				.queryCaseConfirmTime(params);
		int count = aciMapper.queryCaseConfirmTimeCount(params);
		params.put("data", list);
		params.put("total", count);
		return params;
	}

	@Override
	public void exportConfirmTimeInfo(Map<String, Object> params,
			HttpServletResponse response) {
		String filename = "案例列表";
		// 根据查询条件查询案例信息
		List<RdsJudicialCaseConfirmTime> list = aciMapper.queryCaseConfirmTime(params);
		Object[] titles = { "案例编号", "归属人", "历史登记人", "当前登记人", "登记时长（提交审核时间-登记开始时间）",
				"审核人员", "审核时长（审核通过时间-提交审核时间）", "样本交接人员","样本交接时长（交接单生成时间-审核通过时间）",
				"样本确认人员","样本确认时长（交接单确认时间-交接单生成时间）","电子报告生成时长（报告书生成时间-样本确认时间）",
				"报告书制作时长（报告交接单生成时间-报告书生成时间）","报告书交接时长（交接单确认时间-交接单生成时间）","邮寄人员",
				"邮寄时长（邮寄时间-报告书确认时间）","报告周期（邮寄时间-审核通过时间）","财务确认耗时(财务确认时间-报告书生成时间)","案例异常信息","备注"};
		// 表格实体
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		// 循环案例列表拼装表格一行
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			// 案例编号
			objects.add(list.get(i).getCase_code());
			// 归属人
			objects.add(list.get(i).getUsername());
			objects.add(list.get(i).getRegisterPer());
			objects.add(list.get(i).getRegisterUsername());
			objects.add(list.get(i).getRegisterTime());
			objects.add(list.get(i).getVerifyPer());
			objects.add(list.get(i).getVerifyTime());
			objects.add(list.get(i).getSampleRecivePer());
			objects.add(list.get(i).getSampleTime());
			objects.add(list.get(i).getSampleConfirmPer());
			objects.add(list.get(i).getSampleConfirmTime());
			objects.add(list.get(i).getReportCreateTime());
			objects.add(list.get(i).getReportMakeTime());
			objects.add(list.get(i).getReportCheckTime());
			objects.add(list.get(i).getEmailPer());
			objects.add(list.get(i).getEmailTime());
			objects.add(list.get(i).getReportCircleTime());
			objects.add(list.get(i).getFinanceTime());
			objects.add(list.get(i).getException_desc());
			objects.add(list.get(i).getRemark());
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "案例列表"
				+ DateUtils.Date2String(new Date()));
	}
}
