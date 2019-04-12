package com.rds.alcohol.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.text.SimpleDateFormat;
import com.rds.alcohol.code.regressionLine.DataPoint;
import com.rds.alcohol.code.regressionLine.RegressionLine;
import com.rds.alcohol.mapper.RdsAlcoholExperimentMapper;
import com.rds.alcohol.mapper.RdsAlcoholRegisterMapper;
import com.rds.alcohol.model.RdsAlcoholExperimentDataModel;
import com.rds.alcohol.model.RdsAlcoholExperimentModel;
import com.rds.alcohol.model.RdsAlcoholRegressionModel;
import com.rds.alcohol.model.RdsAlcoholRegressionPointModel;
import com.rds.alcohol.model.RdsAlcoholResponse;
import com.rds.alcohol.service.RdsAlcoholExperimentService;
import com.rds.code.utils.uuid.UUIDUtil;

/**
 * @description 酒精实验
 * @author 傅少明 2015年6月8日
 *
 */
@Service
public class RdsAlcoholExperimentServiceImpl implements
		RdsAlcoholExperimentService {

	@Setter
	@Autowired
	private RdsAlcoholExperimentMapper experMapper;

	@Setter
	@Autowired
	private RdsAlcoholRegisterMapper registerMapper;

	public RdsAlcoholResponse queryCaseInfo(Map<String, Object> params) {
		List<Map<String, Object>> list = experMapper.queryCaseForExper(params);
		int count = experMapper.queryCaseForExperCount(params);
		RdsAlcoholResponse resp = new RdsAlcoholResponse();
		resp.setItems(list);
		resp.setCount(count);
		return resp;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public Map<String, Object> addRegression(Map<String, Object> params) {
		Map<String, Object> msg = new HashMap<String, Object>();
		List<String> concentration = (List<String>) params.get("concentration");
		List<String> alcohol = (List<String>) params.get("alcohol");
		List<String> butanol = (List<String>) params.get("butanol");
		Double reg_R2 = Double.parseDouble((String) params.get("reg_R2"));

		List<RdsAlcoholRegressionPointModel> list = new ArrayList<RdsAlcoholRegressionPointModel>();
		RdsAlcoholRegressionPointModel pointModel;
		RdsAlcoholRegressionModel regModel = new RdsAlcoholRegressionModel();
		String reg_id = UUIDUtil.getUUID();
		RegressionLine line = new RegressionLine();
		for (int i = 0; i < concentration.size(); i++) {
			pointModel = new RdsAlcoholRegressionPointModel(reg_id,
					Double.parseDouble(concentration.get(i)),
					Double.parseDouble(alcohol.get(i)),
					Double.parseDouble(butanol.get(i)));
			list.add(pointModel);
			if (Double.parseDouble(butanol.get(i))<=0){
				msg.put("success", false);
				msg.put("result", "乙醇或叔丁醇数据填写不合法，请重新录入数据");
				return msg;
			}
			line.addDataPoint(new DataPoint(Double.parseDouble(concentration
					.get(i)), Double.parseDouble(alcohol.get(i))
					/ Double.parseDouble(butanol.get(i))));
		}
		double Reg_A = line.getA1();
		double Reg_B = line.getA0();
		double lineR = line.getR();
		if (Reg_A == Double.NaN || Reg_A == 0 || lineR == 0) {
			msg.put("success", false);
			msg.put("result", "乙醇或叔丁醇数据填写不合法，请重新录入数据");
			return msg;
		}
		regModel.setReg_id(reg_id);
		regModel.setReg_A(Reg_A);
		regModel.setReg_B(Reg_B);
		regModel.setReg_R2(lineR);
		regModel.setReg_code(new Long(new Date().getTime()).toString());
		regModel.setReg_time(new Date());
		regModel.setReg_qualify(lineR >= reg_R2 ? 0 : 1);

		if (experMapper.setRegressionData(list) >= 1
				&& experMapper.setRegression(regModel) >= 1) {
			msg.put("success", true);
			if (lineR >= reg_R2) {
				msg.put("result", "校验曲线生成成功,</br>R=" + lineR + "</br>Y="
						+ Reg_A + "X" + (Reg_B < 0 ? Reg_B : ("+" + Reg_B)));
				return msg;
			} else {
				msg.put("result", "校验曲线不合格,</br>R=" + lineR + "<" + reg_R2
						+ ",</br>请重新生成校验曲线");
				return msg;
			}
		} else {
			msg.put("success", false);
			msg.put("result", "校验曲线生成失败！");
			return msg;
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public Map<String, Object> addExperiment(Map<String, Object> params) {
		Map<String, Object> msg = new HashMap<String, Object>();
		List<String> alcohol = (List<String>) params.get("alcohol");
		List<String> butanol = (List<String>) params.get("butanol");

		RdsAlcoholRegressionModel regModel = experMapper.getRegModel(null);
		RdsAlcoholExperimentModel experModel = new RdsAlcoholExperimentModel();
		experModel.setExper_time(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
				.format(new Date()));
		experModel.setCase_id((String) params.get("case_id"));
		experModel.setExper_id(UUIDUtil.getUUID());
		experModel.setExper_code(Long.toString(new Date().getTime()));
		experModel.setReg_id(regModel.getReg_id());
		experModel.setRemark((String) params.get("remark"));
		List<RdsAlcoholExperimentDataModel> list = new ArrayList<RdsAlcoholExperimentDataModel>();
		RdsAlcoholExperimentDataModel experData;
		for (int i = 0; i < alcohol.size(); i++) {
			experData = new RdsAlcoholExperimentDataModel();
			experData.setExper_id(experModel.getExper_id());
			experData.setId(UUIDUtil.getUUID());
			experData.setAlcohol(Double.parseDouble(alcohol.get(i)));
			experData.setButanol(Double.parseDouble(butanol.get(i)));
			experData.setResult((experData.getAlcohol()
					/ experData.getButanol() - regModel.getReg_B())
					/ regModel.getReg_A());
			list.add(experData);
		}
		double relativeValue = Math.abs((list.get(0).getResult() - list.get(1)
				.getResult())
				/ ((list.get(0).getResult() + list.get(1).getResult()) / 2));
		if (relativeValue > 0.15) {
			experModel.setExper_isdelete(1);
			msg.put("result", "计算成功!</br>测定结果相对差为" + relativeValue
					+ "测定结果无效！请重新测定！");
		} else {
			experModel.setExper_isdelete(0);
			msg.put("result", "计算成功!</br>测定结果相对差为" + relativeValue + "测定结果有效！");
			params.put("state", 3);
			experMapper.updateCaseState(params);
		}
		if (experMapper.addExperiment(experModel) > 0
				&& experMapper.addExperimentData(list) > 1) {
			msg.put("success", true);
			return msg;
		} else {
			msg.put("success", false);
			msg.put("result", "计算失败，请联系管理员");
			return msg;
		}
	}

	@Override
	public Map<String, Object> isRegPastDue(Map<String, Object> params) {
		if (experMapper.getExperCount(params) > 0) {
			params.put("result", "该案例已经成功实验！无需再次实验。");
			params.put("success", 2);
			return params;
		}
		Date now = new Date();
		params.put("reg_date",
				new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(now));
		RdsAlcoholRegressionModel reg = experMapper.getRegModel(params);
		params.clear();
		if (reg != null) {

			if (now.getTime() - reg.getReg_time().getTime() > 3600000) {
				params.put("success", 1);
				params.put("result", "校验曲线生成时间超过 1 小时，是否需要重新生成曲线？");
				return params;
			} else {
				params.put("success", 0);
				params.put("result", "OK");
				return params;
			}
		} else {
			params.put("success", 2);
			params.put("result", "今天还没有生成校验曲线，请先生成校验曲线！");
			return params;
		}
	}

	@Override
	@Transactional
	public List<Map<String, Object>> queryExperDetail(Map<String, Object> params) {
		return experMapper.getExperDetail(params);
	}

	@Override
	public Map<String, Object> deleteExper(Map<String, Object> params) {
		if (experMapper.getExperCount(params) == 0) {
			params.clear();
			params.put("success", false);
			params.put("result", "此案例没有有效实验数据，无需作废实验！");
			return params;
		}
		if (experMapper.deleteExper(params) > 0) {
			params.put("state", 2);
			experMapper.updateCaseState(params);
			params.clear();
			params.put("success", true);
			params.put("result", "实验作废成功！可以重新实验");
			return params;
		}
		{
			params.clear();
			params.put("success", false);
			params.put("result", "实验作废失败！");
			return params;
		}
	}
}
