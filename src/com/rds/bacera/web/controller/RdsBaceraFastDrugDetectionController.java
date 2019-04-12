package com.rds.bacera.web.controller;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.bacera.service.RdsBaceraBoneAgeService;
import com.rds.bacera.service.RdsBaceraFastDrugDetectionService;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("bacera/fastDetection")
public class RdsBaceraFastDrugDetectionController extends RdsBaceraAbstractController {
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");
	@Setter
	@Autowired
	private RdsBaceraFastDrugDetectionService rdsBacerafastDrugDetectionService;
	
	@Setter
	@Autowired
	private RdsBaceraBoneAgeService rdsBoneAgeService;

	@RequestMapping("queryall")
	@ResponseBody
	public Object queryAll(@RequestBody Map<String, Object> params)
			throws Exception {
		return 0;
	}

	@RequestMapping("queryallpage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params,HttpServletRequest request)
			throws Exception {
		RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
		if (FINANCE_PERMIT.contains(user.getRoletype())
				|| FINANCE_PERMIT.contains(user.getUsercode())){
		    return rdsBacerafastDrugDetectionService.queryAllPage(params);
		}else{
			params.put("input", user.getUserid());
			return rdsBacerafastDrugDetectionService.queryAllPage(params);
		}
		
	}

	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsBacerafastDrugDetectionService.delete(params);
			return this.setModel(true, RdsUpcConstantUtil.DELETE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}

	@RequestMapping("save")
	@ResponseBody
	public Object save(@RequestBody Map<String, Object> params,HttpServletRequest request) {
		Object id = params.get("id");
		if (id == null || id.equals("")) {
			return insert(params,request);
		} else {
			return update(params,request);
		}
	}

	private Object insert(Map<String, Object> params,HttpServletRequest request) {
		try {
			params.put("id", RdsUpcUUIDUtil.getUUID());
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("input", user.getUserid());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("num", params.get("num").toString());
			int numResult = rdsBacerafastDrugDetectionService.queryNumExit(map);
			if (numResult > 0) {
				return this.setModel(false, "编号已存在，请重新输入");
			}
			int result = rdsBacerafastDrugDetectionService.insert(params);
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

	private Object update(Map<String, Object> params,HttpServletRequest request) {
		try {

			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("input", user.getUserid());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("num", params.get("num").toString());
			map.put("id", params.get("id").toString());
			int numResult = rdsBacerafastDrugDetectionService.queryNumExit(map);
			if (numResult <= 0) {
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("num", params.get("num").toString().trim());
				if (rdsBacerafastDrugDetectionService.queryNumExit(map1) > 0)
					return this.setModel(false, "编号已存在，请重新修改");
			}
			int result = rdsBacerafastDrugDetectionService.update(params);
			if (result > 0) {
				return this.setModel(true, "操作成功");
			} else {
				return this.setModel(false, "操作成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}
	
	/**
	 * 导出快检信息
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping("exportDrugDetection")
	public void exportDrugDetection(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			//负责人
			String person = request.getParameter("person");
			params.put("person", person);
			String num = request.getParameter("num");
			params.put("num", num);
			//地址
			String address = request.getParameter("address");
			params.put("address", address);
			//登记日期
			String fastDetection_starttime = request.getParameter("fastDetection_starttime");
			params.put("fastDetection_starttime", fastDetection_starttime);
			String fastDetection_endtime = request.getParameter("fastDetection_endtime");
			params.put("fastDetection_endtime", fastDetection_endtime);
			//试剂类型
			String reagent_type = request.getParameter("reagent_type");
			params.put("reagent_type", reagent_type);
			//试剂数量
			String reagent_count = request.getParameter("reagent_count");
			params.put("reagent_count", reagent_count);
			//试用类型
			String trial_type = request.getParameter("trial_type");
			trial_type=new String(trial_type.getBytes("ISO-8859-1"),"UTF-8");
            params.put("trial_type", trial_type);
			
			//备注
			String remark = request.getParameter("remark");
			params.put("remark", remark);
			RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
			params.put("roleType", user.getRoletype());
			params.put("userCode", user.getUsercode());
			params.put("userId", user.getUserid());
			rdsBacerafastDrugDetectionService.exportFastDrugDetection(params, response);;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("queryProgram")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> queryProgram(){
		return rdsBacerafastDrugDetectionService.queryProgram();
	}
}
