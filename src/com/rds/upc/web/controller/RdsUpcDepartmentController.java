package com.rds.upc.web.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.service.RdsUpcDepartmentService;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("upc/department")
public class RdsUpcDepartmentController extends RdsUpcAbstractController {
	
    private static final String FILE_PATH = ConfigPath.getWebInfPath()
            + "spring" + File.separatorChar + "properties" + File.separatorChar
            + "config.properties";

    private static final String USER_PERMIT = PropertiesUtils.readValue(
            FILE_PATH, "user_permit");
    
	
	@Setter
	@Autowired
	private RdsUpcDepartmentService rdsUpcDepartmentService;
	
	@RequestMapping("queryallpage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params) throws Exception{
//		params = this.pageSet(params);
		return rdsUpcDepartmentService.queryAllPage(params);
	}
	
	@RequestMapping("queryall")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception{
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		// 判断是否加进配置文件配置的人员，查看全部部门权限，否则只能查看本公司部门
		if (!USER_PERMIT.contains(user.getUsercode()))
			params.put("companyid", user.getCompanyid());
		return rdsUpcDepartmentService.queryAll(params);
	}
	
	/**
	 * 人员配置部门时查询树形列表
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryDepartmentList")
	@ResponseBody
	public Object queryDepartmentList(@RequestBody Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		// 判断是否加进配置文件配置的人员，查看全部部门权限，否则只能查看本公司部门
		if (!USER_PERMIT.contains(user.getUsercode()))
			params.put("companyid", user.getCompanyid());
		//修改根节点值
		params.put("node",
				"root".equals(params.get("node")) ? "0" : params.get("node"));
		// 返回部门列表
		return rdsUpcDepartmentService.queryDepartmentList(params);
	}
	
	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params){
		try {
			String ids = params.get("ids").toString();
			rdsUpcDepartmentService.delete(ids);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
		
	}
	
	@RequestMapping("save")
	@ResponseBody
	public Object save(@RequestBody Map<String, Object> params){
		Object deptid = params.get("deptid");
		if(deptid==null||deptid.equals("")){
			return insert(params);
		}else {
			return update(params);
		}
	}
	
	private Object insert(Map<String, Object> params){
		try {
			//判断新增时部门编号是否存在
			String deptcode = params.get("deptcode") ==null ? "":params.get("deptcode").toString();
			params.put("deptid", RdsUpcUUIDUtil.getUUID());
			if( "" == params.get("companyid") || null == params.get("companyid"))
			{
				params.put("companyid", params.get("companyidtemp"));
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deptcode", deptcode.trim());
			if(rdsUpcDepartmentService.queryCountByCode(map)>0)
			{
				return this.setModel(false, RdsUpcConstantUtil.REPERT_CODE);
			}
			int result = rdsUpcDepartmentService.insert(params);
			if (result > 0) {
				return this.setModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
			} else {
				return this.setModel(false, RdsUpcConstantUtil.INSERT_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, RdsUpcConstantUtil.EXCEPTION
					+ "\n" + e.getMessage());
		}
	}
	
	private Object update(Map<String, Object> params){
		try {
			//判断更新时部门编号是否存在
			String deptcode = params.get("deptcode")==null?"":params.get("deptcode").toString();
			String deptid = params.get("deptid")==null?"":params.get("deptid").toString();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deptcode", deptcode.trim());
			map.put("deptid", deptid.trim());
			if(rdsUpcDepartmentService.queryCountByCode(map)<=0)
			{
				map.put("deptid", null);
				if(rdsUpcDepartmentService.queryCountByCode(map)>0)
				{
					return this.setModel(false, RdsUpcConstantUtil.REPERT_CODE);
				}
			}
			if( "" == params.get("companyid") || null == params.get("companyid"))
			{
				params.put("companyid", params.get("companyidtemp"));
			}
			int result = rdsUpcDepartmentService.update(params);
			int i = rdsUpcDepartmentService.updateJunior(params);
			if (result > 0 &&  i > 0) {
				return this.setModel(true, RdsUpcConstantUtil.UPDATE_SUCCESS);
			} else {
				return this.setModel(false, RdsUpcConstantUtil.UPDATE_FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}
	
	@RequestMapping("querytreecombo")
	@ResponseBody
	public Object queryTreeCombo(@RequestBody Map<String, Object> params) throws Exception{
		return rdsUpcDepartmentService.queryTreeCombo(params);
	}
	

}
