package com.rds.crm.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.crm.model.RdsCrmSampleModel;
import com.rds.crm.service.RdsCrmRegesterService;
import com.rds.upc.model.RdsUpcUserModel;

/**
 * Crm 登记
 * 
 * @author 少明
 *
 */
@Controller
@RequestMapping("crm/register")
public class RdsCrmRegesterController {

	@Autowired
	private RdsCrmRegesterService rService;

	/**
	 * 获取订单列表
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getOrderList")
	@ResponseBody
	public Map<String, Object> getOrderList(
			@RequestBody Map<String, Object> params, HttpSession session) {

		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			params.put("user", user.getUserid());
			params.put("deptcode", user.getDeptcode());
		} else {
			params.clear();
			params.put("success", false);
			params.put("msg", "请重新登陆！");
			return params;
		}
		Map<String, Object> resp = rService.getOrderList(params);
		return resp;
	}

	/**
	 * 判断手机是否登记过了
	 * 
	 * @param phone
	 * @return
	 */
	@RequestMapping("isphonein")
	@ResponseBody
	public boolean isPhoneIn(@RequestBody Map<String, Object> params) {
		return rService.isPhoneIn((String) params.get("phone"));
	}

	/**
	 * 获取检测类型
	 */
	@RequestMapping("getdectionclass")
	@ResponseBody
	public Map<String, Object> getDetectionClass() {
		return rService.getDetectionClass();
	}

	/**
	 * 获取检测单位
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getdetectionorg")
	@ResponseBody
	public Map<String, Object> getDetectionOrg(
			@RequestBody Map<String, Object> params) {
		return rService.getDetectionOrg((String) params.get("dic_id"));
	}

	/**
	 * 新增订单
	 * 
	 * @param params
	 * @param session
	 * @return
	 */
	@RequestMapping("saveOrderInfo")
	@ResponseBody
	public Map<String, Object> saveOrderInfo(
			@RequestBody Map<String, Object> params, HttpSession session) {
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			params.put("user", user.getUserid());

		} else {
			params.clear();
			params.put("success", false);
			params.put("msg", "请重新登陆！");
			return params;
		}
		return rService.saveOrderInfo(params);
	}

	/**
	 * 更新订单信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("updateOrderInfo")
	@ResponseBody
	public Map<String, Object> updateOrderInfo(
			@RequestBody Map<String, Object> params) {
		return rService.updateOrderInfo(params);
	}

	/**
	 * 获取样本信息
	 * 
	 * @return
	 */
	@RequestMapping("getSampleCombo")
	@ResponseBody
	public Map<String, Object> getSampleCombo() {
		return rService.getSampleCombo();
	}

	/**
	 * 样本
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getSampleName")
	@ResponseBody
	public List<RdsCrmSampleModel> getSampleName(
			@RequestBody Map<String, Object> params) {
		return rService.getSampleName(params);
	}

	/**
	 * 保存财务
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("saveFeeInfo")
	@ResponseBody
	public Map<String, Object> saveFeeInfo(
			@RequestBody Map<String, Object> params) {
		return rService.saveFeeInfo(params);
	}

	/**
	 * 获取回访记录
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("getCallBack")
	@ResponseBody
	public Map<String, Object> getCallBack(
			@RequestBody Map<String, Object> params) {
		return rService.getCallBack(params);
	}

	/**
	 * 保存回访
	 * 
	 * @param params
	 * @param session
	 * @return
	 */
	@RequestMapping("saveCallBack")
	@ResponseBody
	public Map<String, Object> saveCallBack(
			@RequestBody Map<String, Object> params, HttpSession session) {
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			params.put("user", user.getUserid());

		} else {
			params.clear();
			params.put("success", false);
			params.put("msg", "请重新登陆！");
			return params;
		}
		return rService.saveCallBack(params);
	}

	/**
	 * 获取缴费信息
	 */
	@RequestMapping("getOrderFeeList")
	@ResponseBody
	public Map<String, Object> getOrderFeeList(
			@RequestBody Map<String, Object> params) {
		return rService.getOrderFeeList(params);
	}
	
	@RequestMapping("getOrderListQuery")
	@ResponseBody
	public Map<String, Object> getOrderListQuery(@RequestBody Map<String, Object> params, HttpSession session){
		if (session.getAttribute("user") != null) {
			RdsUpcUserModel user = (RdsUpcUserModel) session
					.getAttribute("user");
			params.put("user", user.getDeptcode());
		} else {
			params.clear();
			params.put("success", false);
			params.put("msg", "请重新登陆！");
			return params;
		}
		Map<String, Object> resp = rService.getOrderListQuery(params);
		return resp;
	}
}
