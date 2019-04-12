package com.rds.upc.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.service.RdsUpcPartnerConfigService;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("upc/partnerConfig")
public class RdsUpcPartnerConfigController extends RdsUpcAbstractController {

	@Setter
	@Autowired
	private RdsUpcPartnerConfigService rdsUpcPartnerConfigService;

	@RequestMapping("queryall")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsUpcPartnerConfigService.queryAll(params);
	}

	@RequestMapping("queryallpage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params)
			throws Exception {
		params = this.pageSet(params);
		return rdsUpcPartnerConfigService.queryAllPage(params);
	}

	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsUpcPartnerConfigService.delete(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}

	}

	@RequestMapping("save")
	@ResponseBody
	public Object save(@RequestBody Map<String, Object> params,HttpServletRequest request) {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("create_per", user.getUserid());
		Object id = params.get("id");
		if (id == null || id.equals("")) {
			return insert(params);
		} else {
			return update(params);
		}
	}

	/**
	 * 保存区域合作商关系
	 * @param params
	 * @return
	 */
	private Object insert(Map<String, Object> params) {
		try {
			//判断该区域是否已配置合作商
			if (rdsUpcPartnerConfigService.queryQartnerExist(params) > 0)
				return this.setModel(false,"该区域已配置合作商");
			else {
				params.put("id", RdsUpcUUIDUtil.getUUID());
				int result = rdsUpcPartnerConfigService.insert(params);
				if (result > 0) {
					return this.setModel(true,
							RdsUpcConstantUtil.INSERT_SUCCESS);
				} else {
					return this.setModel(false,
							RdsUpcConstantUtil.INSERT_FAILED);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, RdsUpcConstantUtil.EXCEPTION
					+ "\n" + e.getMessage());
		}
	}

	private Object update(Map<String, Object> params) {
		try {
			int result = 0;
			//判断该记录的地区是否没改变
			if(rdsUpcPartnerConfigService.queryQartnerExist(params) > 0)
				result=	rdsUpcPartnerConfigService.update(params);
			else
			{
				//判断改变后的区域代码是否已配置合作方
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("areacode", params.get("areacode"));
				if(rdsUpcPartnerConfigService.queryQartnerExist(map) > 0)
					return this.setModel(false,"该区域已配置合作商");
				else
					result=	rdsUpcPartnerConfigService.update(params);
			}
			if (result > 0) {
				return this.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
			} else {
				return this.setModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}
	
	@RequestMapping("queryPartnerName")
	@ResponseBody
	public Object queryPartnerName(@RequestBody Map<String, Object> params) {
		String report_model = params.get("report_model")==null?"":params.get("report_model").toString();
		String partnername="";
		try {
			partnername=rdsUpcPartnerConfigService.getCaseTask(report_model);
			return this.setModel(true, partnername);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(false, partnername);
		}
	}
	
	@RequestMapping("saveReportModel")
	@ResponseBody
	public Object saveReportModel(@RequestBody Map<String, Object> params,HttpServletRequest request) {
		try {
			RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
					.getAttribute("user");
			//删除配置模版
			Map<String, Object> mapDelete = new HashMap<>();
			mapDelete.put("userid", params.get("userid"));
			rdsUpcPartnerConfigService.deletePartnerModel(mapDelete);
			//保存新增模版
			String[] code = params.get("code").toString().split(",");
			for (String string : code) {
				Map<String, Object> map = new HashMap<>();
				map.put("create_per", user.getUserid());
				map.put("id",UUIDUtil.getUUID());
				map.put("code", string);
				map.put("userid", params.get("userid"));
				rdsUpcPartnerConfigService.insertPartnerModel(map);
			}
			return this.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(false, "保存失败，请联系管理员！");
		}

	}
}
