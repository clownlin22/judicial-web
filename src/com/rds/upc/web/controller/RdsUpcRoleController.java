package com.rds.upc.web.controller;

import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.upc.service.RdsUpcRoleService;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

/**
 * 
 * @ClassName: RdsUpcRoleController
 * @Description: 角色管理Controller
 * @author yxb
 * @date 2015年4月25日 
 *
 */
@Controller
@RequestMapping("upc/role")
public class RdsUpcRoleController extends RdsUpcAbstractController {
	
	@Setter
	@Autowired
	private RdsUpcRoleService rdsUpcRoleService;
	
	@RequestMapping("queryall")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params) throws Exception{
		return rdsUpcRoleService.queryAll(params);
	}
	
	/**
	 * 分页查询角色信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryallpage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params) throws Exception{
		params = this.pageSet(params);
		return rdsUpcRoleService.queryAllPage(params);
	}
	
	/**
	 * 删除角色信息
	 * @param params
	 * @return
	 */
	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params){
		try {
			rdsUpcRoleService.delete(params);
			rdsUpcRoleService.deletePermit(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
		
	}
	
	/**
	 * 保存更新角色信息
	 * @param params
	 * @return
	 */
	@RequestMapping("save")
	@ResponseBody
	public Object save(@RequestBody Map<String, Object> params){
		Object roleid = params.get("roleid");
		if(roleid==null || roleid.equals("")){
			return insert(params);
		}else {
			return update(params);
		}
	}
	
	/**
	 * 保存角色权限
	 * @param params
	 * @return
	 */
	@RequestMapping("saveRole")
	@ResponseBody
	public Object saveRole(@RequestBody Map<String, Object> params){
		String roletypes = params.get("roletypes")==null?"":params.get("roletypes").toString();
		String modulecodes = params.get("modulecodes")==null?"": params.get("modulecodes").toString();
		String roletype[] = roletypes.split(",");
		String modulecode[] = modulecodes.split(",");
		int result=0;
		for(int i = 0 ; i < roletype.length ; i ++)
		{
			Map<String,Object> maptemp = new HashMap<String, Object>();
			maptemp.put("roletype", roletype[i]);
			rdsUpcRoleService.deletePermit(maptemp);
			for(int j = 0 ; j < modulecode.length; j ++)
			{
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("roletype", roletype[i]);
				map.put("modulecode", modulecode[j]);
				result=rdsUpcRoleService.insertPermit(map);
			}
		}
		
		if (result > 0) {
			return this.setModel(true, RdsUpcConstantUtil.INSERT_SUCCESS);
		} else {
			return this.setModel(false, RdsUpcConstantUtil.INSERT_FAILED);
		}
	}
	
	private Object insert(Map<String, Object> params){
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("roletype", params.get("roletype"));
			int resultType = rdsUpcRoleService.queryAllCount(map);
			if(resultType > 0)
			{
				return this.setModel(false, RdsUpcConstantUtil.ROLE_MSG);
			}
			params.put("roleid", RdsUpcUUIDUtil.getUUID());
			int result = rdsUpcRoleService.insert(params);
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
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("roletype", params.get("roletype"));
			map.put("roleid", params.get("roleid"));
			int resultType = rdsUpcRoleService.queryAllCount(map);
			if(resultType <= 0)
			{
				Map<String,Object> map1 = new HashMap<String, Object>();
				map1.put("roletype", params.get("roletype"));
				if( rdsUpcRoleService.queryAllCount(map1) > 0)
					return this.setModel(false, RdsUpcConstantUtil.ROLE_MSG);
			}
			int result = rdsUpcRoleService.update(params);
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
