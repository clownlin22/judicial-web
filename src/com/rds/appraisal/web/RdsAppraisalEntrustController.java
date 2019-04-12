package com.rds.appraisal.web;

import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.appraisal.model.RdsAppraisalEntrustModel;
import com.rds.appraisal.service.RdsAppraisalEntrustService;
import com.rds.appraisal.service.RdsAppraisalInfoService;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.upc.web.common.RdsUpcConstantUtil;

/**
 * 委托人controller
 * 
 * @author yxb 2015-07-22
 */
@Controller
@RequestMapping("appraisal/entrust")
public class RdsAppraisalEntrustController extends RdsAppraisalAbstractController {

	@Setter
	@Autowired
	private RdsAppraisalEntrustService rdsAppraisalEntrustService;
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
		try {
			// 根据名称查询是否存在记录
			RdsAppraisalEntrustModel list = (RdsAppraisalEntrustModel) rdsAppraisalEntrustService
					.queryModel(params);
			// 新增
			if (params.get("id") == null
					|| "".equals(params.get("id"))) {
				// 判断是否存在记录
				if (list == null) {
					// 自动生成类型id
					params.put("id", UUIDUtil.getUUID());
					// 插入返回结果值
					int result = rdsAppraisalEntrustService.insert(params);
					if (result > 0) {
						// 返回json控制
						return this.setModel(true,
								RdsUpcConstantUtil.INSERT_SUCCESS);
					} else {
						return this.setModel(false,
								RdsUpcConstantUtil.INSERT_FAILED);
					}
				} else {
					return this.setModel(false, RdsUpcConstantUtil.ENTRUST_REPERT);
				}

			} else {
				// 判断记录是否存在
				if (list != null) {
					// 存在记录比较id是否相同，相同说明需要跟新，不相同说明重复记录
					if (list.getId().equals(params.get("id"))) {
						int result = rdsAppraisalEntrustService.update(params);
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
								RdsUpcConstantUtil.ENTRUST_REPERT);
					}
				} else {
					//更新名称记录不存在，可以更新
					int result = rdsAppraisalEntrustService.update(params);
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
			result = rdsAppraisalEntrustService.delete(params);
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
		return rdsAppraisalEntrustService.queryModel(params);
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
		return rdsAppraisalEntrustService.queryAll(params);
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
		return rdsAppraisalEntrustService.queryAllPage(this.pageSet(params));
	}
	
	/**
	 * 保存/更新委托人
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("saveMechanism")
	@ResponseBody
	public Object insertMechanism(@RequestBody Map<String, Object> params) {
		try {
			// 根据名称查询是否存在记录
			RdsAppraisalEntrustModel list = (RdsAppraisalEntrustModel) rdsAppraisalEntrustService
					.queryModelMechanism(params);
			// 新增
			if (params.get("id") == null
					|| "".equals(params.get("id"))) {
				if (list == null) {
					// 判断是否存在记录
					// 自动生成类型id
					params.put("id", UUIDUtil.getUUID());
					// 插入返回结果值
					int result = rdsAppraisalInfoService.insertMechanism(params);
					if (result > 0) {
						// 返回json控制
						return this.setModel(true,
								RdsUpcConstantUtil.INSERT_SUCCESS);
					} else {
						return this.setModel(false,
								RdsUpcConstantUtil.INSERT_FAILED);
					}
				} else {
					return this.setModel(false, RdsUpcConstantUtil.MECHANISM_REPERT);
				}

			} else {
				// 判断记录是否存在
				if (list != null) {
					// 存在记录比较id是否相同，相同说明需要跟新，不相同说明重复记录
					if (list.getId().equals(params.get("id"))) {
						int result = rdsAppraisalEntrustService.updateMechanism(params);
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
								RdsUpcConstantUtil.MECHANISM_REPERT);
					}
				} else {
					//更新名称记录不存在，可以更新
					int result = rdsAppraisalEntrustService.updateMechanism(params);
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
	 * 删除委托人
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("deleteMechanism")
	@ResponseBody
	public Object deleteMechanism(@RequestBody Map<String, Object> params) {
		int result = 0;
		try {
			result = rdsAppraisalEntrustService.deleteMechanism(params);
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
	@RequestMapping("queryModelMechanism")
	@ResponseBody
	public Object queryModelMechanism(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsAppraisalEntrustService.queryModelMechanism(params);
	}

	/**
	 * 获取所有委托人
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryAllMechanism")
	@ResponseBody
	public Object queryAllMechanism(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsAppraisalEntrustService.queryAllMechanism(params);
	}

	/**
	 * 分页获取鉴定单位记录
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryAllPageMechanism")
	@ResponseBody
	public Object queryAllPageMechanism(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsAppraisalEntrustService.queryAllPageMechanism(this.pageSet(params));
	}
}
