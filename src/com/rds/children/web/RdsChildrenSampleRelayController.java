package com.rds.children.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Setter;

import org.activiti.engine.TaskService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.activiti.mapper.RdsActivitiJudicialMapper;
import com.rds.activiti.service.RdsActivitiJudicialService;
import com.rds.children.mapper.RdsChildrenRegisterMapper;
import com.rds.children.mapper.RdsChildrenSampleReceiveMapper;
import com.rds.children.model.RdsChildrenCaseInfoModel;
import com.rds.children.model.RdsChildrenResponse;
import com.rds.children.model.RdsChildrenSampleModel;
import com.rds.children.model.RdsChildrenSampleReceiveModel;
import com.rds.children.service.RdsChildrenSampleReceiveService;
import com.rds.upc.model.RdsUpcUserModel;

@RequestMapping("children/sampleRelay")
@Controller
public class RdsChildrenSampleRelayController {

	@Autowired
	private RdsChildrenSampleReceiveService rdsChildrenSampleReceiveService;
	
	@Autowired
	private RdsChildrenRegisterMapper rdsChildrenRegisterMapper;
	
	@Setter
	@Autowired
	private RdsActivitiJudicialService rdsActivitiJudicialService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private RdsActivitiJudicialMapper rdsActivitiJudicialMapper;

	@Autowired
	private RdsChildrenSampleReceiveMapper rdsChildrenSampleReceiveMapper;
	/**
	 * 分页查询样本接收信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("getSampleReceiveInfo")
	@ResponseBody
	private RdsChildrenResponse getSampleReceiveInfo(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		String userid = "";
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			userid = user.getUserid();
		}
		if (StringUtils.isEmpty(userid)) {
			return null;
		}
		if (!"admin".equals(user.getUsercode()))
			params.put("transfer_per", userid);
		return rdsChildrenSampleReceiveService.getSampleReceiveInfo(params);
	}

	/**
	 * 分页查询样本接收信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("getTransferInfo")
	@ResponseBody
	private RdsChildrenResponse getTransferInfo(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		String userid = "";
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			userid = user.getUserid();
		}
		if (StringUtils.isEmpty(userid)) {
			return null;
		}
		return rdsChildrenSampleReceiveService.getTransferInfo(params);
	}

	@RequestMapping("getRelaySampleInfo")
	@ResponseBody
	public List<RdsChildrenSampleReceiveModel> getRelaySampleInfo(
			@RequestBody Map<String, Object> params) {
		return rdsChildrenSampleReceiveService.getRelaySampleInfo(params);
	}

	@RequestMapping("getSampleInfo")
	@ResponseBody
	public List<RdsChildrenSampleModel> getSampleInfo(String receive_id) {
		Map<String, Object> map = new HashMap<>();
		map.put("receive_id", receive_id);
		return rdsChildrenSampleReceiveService.getSampleInfo(map);
	}

	/**
	 * 判断样本编码是否存在
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("exsitReceiveSampleCode")
	@ResponseBody
	private Object exsitReceiveSampleCode(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (rdsChildrenSampleReceiveService.existSampleCode(params) > 0)
			result.put("result", true);
		else
			result.put("result", false);
		return result;
	}

	/**
	 * 保存样本编码交接信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("saveReceiveSampleInfo")
	@ResponseBody
	private Object saveReceiveSampleInfo(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		String userid = "";
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			userid = user.getUserid();
		}
		if (StringUtils.isEmpty(userid)) {
			return null;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		params.put("transfer_per", user.getUserid());
		params.put("user", user);
		try {
			// 校验保存交接样本
			result = rdsChildrenSampleReceiveService
					.saveReceiveSampleInfo(params);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("message", "保存失败，请联系管理员查看！");
		}
		return result;
	}

	/**
	 * 保存样本编码交接信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("confirmReceiveSampleInfo")
	@ResponseBody
	private Object confirmReceiveSampleInfo(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		String userid = "";
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			userid = user.getUserid();
		}
		if (StringUtils.isEmpty(userid)) {
			return null;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		params.put("receive_per", user.getUserid());
		params.put("user", user);
		try {
			// 校验保存交接样本
			result = rdsChildrenSampleReceiveService
					.confirmRelaySampleInfo(params);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("message", "保存失败，请联系管理员查看！");
		}
		return result;
	}
	
	/**
	 * 样本交接状态处理
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("sampleCaseCodeRun")
	@ResponseBody
	private Object sampleCaseCodeRun(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		RdsUpcUserModel user = new RdsUpcUserModel();
		String userid = "";
		if (request.getSession().getAttribute("user") != null) {
			user = (RdsUpcUserModel) request.getSession().getAttribute("user");
			userid = user.getUserid();
		}
		if (StringUtils.isEmpty(userid)) {
			return null;
		}
		Map<String, Object> result = new HashMap<String, Object>();
		params.put("user", user);
		try {
			String case_code = params.get("case_code").toString();
			List<RdsChildrenCaseInfoModel> caseInfoModels = rdsChildrenRegisterMapper
					.getCaseInfo(params);
			if(caseInfoModels.size()!=1){
				result.put("result", false);
				result.put("message", "案例编号有误，请联系管理员查看！");
				return result;
			}else{
				//案例不在审核通过状态
				if(caseInfoModels.get(0).getVerify_state()!=3){
					result.put("result", false);
					result.put("message", "该案例状态有误，请联系管理员查看！");
					return result;
				}else{
					Map<String, Object> variables = new HashMap<>();
					String taskId = rdsActivitiJudicialMapper
							.getChildCaseTask(case_code);
					if (taskId == null) {
						result.put("result", false);
						result.put("message", "操作失败，请联系管理员！");
						return result;
					}
					// 加入样本接收流程
					rdsActivitiJudicialService.runByChildCaseCode(case_code,
							variables, user);
					//加入样本审核通过流程
					variables.put("issamplepass", 1);
					rdsActivitiJudicialService.runByChildCaseCode(case_code,
							variables, user);
					
					Map<String, Object> verify_state = new HashMap<>();
					verify_state.put("case_code", case_code);;
					verify_state.put("verify_state", 5);
					// 更新基本信息
					rdsChildrenSampleReceiveMapper.updateCaseState(verify_state);
					result.put("result", true);
					result.put("message", "案例流程操作成功！");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("message", "操作失败，请联系管理员查看！");
		}
		return result;
	}
}
