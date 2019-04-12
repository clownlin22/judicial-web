package com.rds.appraisal.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.appraisal.model.RdsAppraisalTypeModel;
import com.rds.appraisal.service.RdsAppraisalTypeService;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.upc.web.common.RdsUpcConstantUtil;

/**
 * 鉴定类型controller
 * 
 * @author yxb 2015-07-22
 */
@Controller
@RequestMapping("appraisal/type")
public class RdsAppraisalTypeController extends RdsAppraisalAbstractController {

	@Setter
	@Autowired
	private RdsAppraisalTypeService rdsAppraisalTypeService;

	/**
	 * 保存/更新鉴定类型
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@ResponseBody
	public Object insert(@RequestBody Map<String, Object> params) {
		try {
			// 根据类型名称查询是否存在记录
			RdsAppraisalTypeModel list = (RdsAppraisalTypeModel) rdsAppraisalTypeService
					.queryModel(params);
			// 新增
			if (params.get("type_id") == null
					|| "".equals(params.get("type_id"))) {
				// 判断是否存在记录
				if (list == null) {
					// 自动生成类型id
					params.put("type_id", UUIDUtil.getUUID());
					// 插入返回结果值
					int result = rdsAppraisalTypeService.insert(params);
					if (result > 0) {
						// 返回json控制
						return this.setModel(true,
								RdsUpcConstantUtil.INSERT_SUCCESS);
					} else {
						return this.setModel(false,
								RdsUpcConstantUtil.INSERT_FAILED);
					}
				} else {
					return this.setModel(false, RdsUpcConstantUtil.TYPE_REPERT);
				}

			} else {
				// 判断记录是否存在
				if (list != null) {
					// 存在记录比较type_id是否相同，相同说明需要跟新，不相同说明重复记录
					if (list.getType_id().equals(params.get("type_id"))) {
						int result = rdsAppraisalTypeService.update(params);
						if (result > 0) {
							// 返回json控制
							return this.setModel(true,
									RdsUpcConstantUtil.UPDATE_SUCCESS);
						} else {
							return this.setModel(false,
									RdsUpcConstantUtil.UPDATE_FAILED);
						}
					} else {
						return this.setModel(false,
								RdsUpcConstantUtil.TYPE_REPERT);
					}
				} else {
					// 跟新类型名称记录不存在，可以更新
					int result = rdsAppraisalTypeService.update(params);
					if (result > 0) {
						// 返回json控制
						return this.setModel(true,
								RdsUpcConstantUtil.UPDATE_SUCCESS);
					} else {
						return this.setModel(false,
								RdsUpcConstantUtil.UPDATE_FAILED);
					}
				}

			}

		} catch (Exception e) {
			return this.setModel(true, false, e.getMessage());
		}

	}

	/**
	 * 删除鉴定类型
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
			result = rdsAppraisalTypeService.delete(params);
			if (result > 0)
				return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
			else
				return this.setModel(true, RdsUpcConstantUtil.DELETE_FAILED);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}

	/**
	 * 查询单个鉴定类型
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryModel")
	@ResponseBody
	public Object queryModel(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsAppraisalTypeService.queryModel(params);
	}

	/**
	 * 获取所有鉴定类型
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryAll")
	@ResponseBody
	public Object queryAll(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HashMap<Object, Object> params = new HashMap<Object, Object>();
		params.put("case_id", request.getParameter("case_id"));
		return rdsAppraisalTypeService.queryAll(params);
	}

	/**
	 * 分页获取鉴定类型记录
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryAllPage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsAppraisalTypeService.queryAllPage(this.pageSet(params));
	}

	/**
	 * 分页获取鉴定标准记录
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryStandardAllPage")
	@ResponseBody
	public Object queryStandardAllPage(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsAppraisalTypeService.queryStandardAllPage(this
				.pageSet(params));
	}

	/**
	 * 保存鉴定标准
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("saveStandard")
	@ResponseBody
	public Object saveStandard(@RequestBody Map<String, Object> params) {
		int result = 0;
		try {
			//判断standard_id是否存在，区分新增还是修改
			if (null == params.get("standard_id")
					|| "".equals(params.get("standard_id"))) {
				params.put("standard_id", UUIDUtil.getUUID());
				result = rdsAppraisalTypeService.insertStandard(params);
				if (result > 0)
					return this.setModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
				else
					return this.setModel(true, RdsUpcConstantUtil.INSERT_FAILED);
			} else
			{
				result = rdsAppraisalTypeService.updateStandard(params);
				if (result > 0)
					return this.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
				else
					return this.setModel(true, RdsUpcConstantUtil.UPDATE_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}

	/**
	 * 删除鉴定标准
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("deleteStandard")
	@ResponseBody
	public Object deleteStandard(@RequestBody Map<String, Object> params) {
		int result = 0;
		try {
			result = rdsAppraisalTypeService.deleteStandard(params);
			if (result > 0)
				return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
			else
				return this.setModel(true, RdsUpcConstantUtil.DELETE_FAILED);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}

}
