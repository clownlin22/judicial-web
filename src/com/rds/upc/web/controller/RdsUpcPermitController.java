package com.rds.upc.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.upc.model.RdsUpcPermitNodeModel;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.service.RdsUpcPermitService;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;
/**
 * 
 * @ClassName: RdsUpcPermitController
 * @Description: 菜单管理Controller
 * @author yxb
 * @date 2015年4月27日 
 *
 */
@Controller
@RequestMapping("upc/permit")
public class RdsUpcPermitController extends RdsUpcAbstractController{
	
	@Setter
	@Autowired
	private RdsUpcPermitService rdsUpcPermitService;
	
	/**
	 * 根据用户的角色信息赋予用户相应权限
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("queryUserPermitModel")
	@ResponseBody
	public Object queryPermitModelByParentCode(@RequestBody Map<String, Object> params,HttpServletRequest request){
		RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
		Object node = params.get("node")==null?"":params.get("node").toString();
		Object parentscode = params.get("parentcode"); 
		String roleType = user.getRoletype();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("permittype", roleType);
		if(node != "" && !node.equals("root"))
		{
			map.put("parentcode", node);
		}else
		{
			map.put("parentcode", parentscode);
		}
		List<Object> lists = rdsUpcPermitService.queryModule(map);
		List<Object> result = new ArrayList<Object>();
		
		RdsUpcPermitNodeModel model = new RdsUpcPermitNodeModel();
		if(parentscode.toString().equals("")){
		model.setId("0");
		model.setText("业务功能");
		model.setLeaf(false);
		result.add(model);
		}else{
			model = new RdsUpcPermitNodeModel();
			for(int i = 0 ; i <lists.size() ; i ++)
			{
				model = (RdsUpcPermitNodeModel)lists.get(i);
				result.add(model);
			}

		}
		return result;
	}


	/**
	 * 根据父类代码查询所有的子菜单
	 * @param params
	 * @return
	 */
	@RequestMapping("queryPermitModelByParentCode1")
	@ResponseBody
	public Object queryPermitModelByParentCode1(@RequestBody Map<String, Object> params){
		Object result = rdsUpcPermitService.queryPermitModelByParentCode(params);
		return result;
	}
	
	/**
	 * 查询所有菜单信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryall")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params) throws Exception{
		Object result = rdsUpcPermitService.queryAll(params);
		return result;
	}
	
	/**
	 * 保存菜单信息
	 * @param params
	 * @return
	 */
	@RequestMapping("savepermitmodule")
	@ResponseBody
	public Object savePermitModule(@RequestBody Map<String, Object> params){
		try {
			boolean result = rdsUpcPermitService.savePermitModule(params);
			return this.setModel(result, result?RdsUpcConstantUtil.SAVE_SUCCESS:RdsUpcConstantUtil.SAVE_FAILED);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, RdsUpcConstantUtil.EXCEPTION
					+ "\n" + e.getMessage());
		}
	}
	
	/**
	 * 删除菜单
	 * @param params
	 * @return
	 */
	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params){
		try {
			rdsUpcPermitService.delete(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
		
	}
	
	/**
	 * 保存更新菜单信息
	 * @param params
	 * @return
	 */
	@RequestMapping("save")
	@ResponseBody
	public Object save(@RequestBody Map<String, Object> params){
		Object permitcode = params.get("permitcode");
		if(permitcode==null||permitcode.equals("")){
			return insert(params);
		}else {
			return update(params);
		}
	}
	
	private Object insert(Map<String, Object> params){
		try {
			params.put("permitcode", RdsUpcUUIDUtil.getUUID());
			int result = rdsUpcPermitService.insert(params);
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
			int result = rdsUpcPermitService.update(params);
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
}
