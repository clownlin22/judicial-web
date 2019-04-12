package com.rds.appraisal.web;

import java.util.HashMap;
import java.util.Map;

import com.rds.upc.model.RdsUpcMessageModel;

/**
 * 临床基础工具抽象
 * 
 * @author yxb 2015-07-22
 */
public abstract class RdsAppraisalAbstractController {

	/**
	 * 设置回调参数（带success参数）
	 * 
	 * @param success
	 * @param result
	 * @param message
	 * @return RdsUpcMessageModel
	 */
	protected RdsUpcMessageModel setModel(boolean success, boolean result,
			String message) {
		RdsUpcMessageModel model = new RdsUpcMessageModel();
		model.setSuccess(success);
		model.setResult(result);
		model.setMessage(message);
		return model;
	}

	/**
	 * 设置回调参数
	 * 
	 * @param result
	 * @param message
	 * @return RdsUpcMessageModel
	 */
	protected RdsUpcMessageModel setModel(boolean result, String message) {
		RdsUpcMessageModel model = new RdsUpcMessageModel();
		model.setResult(result);
		model.setMessage(message);
		return model;
	}

	/**
	 * 分页控制
	 * 
	 * @param map
	 *            （参数 limit：记录条数，start：开始记录）
	 * @return Map
	 * @throws Exception
	 */
	protected Map<String, Object> pageSet(Map<String, Object> map)
			throws Exception {
		if (map == null) {
			throw new Exception("分页参数为空。");
		}
		Integer start = (Integer) map.get("start");
		Integer limit = (Integer) map.get("limit");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("start", start);
		result.put("end", limit);
		map.putAll(result);
		return map;
	}

}
