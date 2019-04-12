package com.rds.appraisal.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.appraisal.model.RdsAppraisalAttachmentModel;
import com.rds.appraisal.model.RdsAppraisalInfoModel;
import com.rds.appraisal.service.RdsAppraisalInfoService;
import com.rds.appraisal.service.RdsAppraisalPrinterService;

/**
 * 委托人controller
 * 
 * @author yxb 2015-07-22
 */
@Controller
@RequestMapping("appraisal/printer")
public class RdsAppraisalPrinterController extends RdsAppraisalAbstractController {

	@Setter
	@Autowired
	private RdsAppraisalPrinterService rdsAppraisalPrinterService;
	@Setter
	@Autowired
	private RdsAppraisalInfoService rdsAppraisalInfoService;

	/**
	 * 保存/更新委托人
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@ResponseBody
	public Object insert(@RequestBody Map<String, Object> params) {
		return null;

	}

	/**
	 * 删除委托人
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		return null;
	}

	/**
	 * 查询单个委托人
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryModel")
	@ResponseBody
	public Object queryModel(@RequestBody Map<String, Object> params)
			throws Exception {
		return null;
	}

	/**
	 * 获取所有委托人
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryAll")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params)
			throws Exception {
		return null;
	}

	/**
	 * 分页获取委托人记录
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryAllPage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params)
			throws Exception {
		return null;
	}
	
	/**
	 * 分页获取委托人记录
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getPrinterInfo")
	public String getPrinterInfo(HttpServletRequest request,HttpServletResponse response,String case_id)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("case_id", case_id);
		List<String> list = rdsAppraisalPrinterService.getPrinterInfo(params);
		request.setAttribute("list", list);
		List<RdsAppraisalAttachmentModel> lists =rdsAppraisalInfoService.queryAttachmentList(params);
//		RdsAppraisalInfoModel infoModel = (RdsAppraisalInfoModel)rdsAppraisalInfoService.queryModel(params);
		request.setAttribute("attachments", lists);
		request.setAttribute("attachmentsSize", lists.size());
//		request.setAttribute("imgSize",  lists.size());
//		request.setAttribute("infoModel",  infoModel);
		RdsAppraisalInfoModel infoModel = (RdsAppraisalInfoModel) rdsAppraisalInfoService.queryModel(params);
		request.setAttribute("judgename", infoModel.getJudgename());
		request.setAttribute("case_date", infoModel.getAccept_date().substring(0,4));
		return "dna/print_appraisal";
	}
	
}
