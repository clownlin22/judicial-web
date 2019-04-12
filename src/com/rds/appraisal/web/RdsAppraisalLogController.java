package com.rds.appraisal.web;

import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.appraisal.service.RdsAppraisalLogService;
import com.rds.upc.web.common.RdsUpcConstantUtil;

/**
 * 操作日志controller
 * 
 * @author yxb 2015-07-22
 */
@Controller
@RequestMapping("appraisal/log")
public class RdsAppraisalLogController extends RdsAppraisalAbstractController {

	@Setter
	@Autowired
	private RdsAppraisalLogService rdsAppraisalLogService;

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
		try {
			rdsAppraisalLogService.insert(params);
			return this.setModel(true, RdsUpcConstantUtil.SAVE_SUCCESS);
		} catch (Exception e) {
			return this.setModel(true, false, e.getMessage());
		}

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
		int result = 0;
		try {
			result = rdsAppraisalLogService.delete(params);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
		if (result > 0)
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		else
			return this.setModel(true, RdsUpcConstantUtil.DELETE_FAILED);
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
		return rdsAppraisalLogService.queryModel(params);
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
		return rdsAppraisalLogService.queryAll(params);
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
		return rdsAppraisalLogService.queryAllPage(this.pageSet(params));
	}
}
