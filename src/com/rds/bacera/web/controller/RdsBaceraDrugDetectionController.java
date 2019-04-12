package com.rds.bacera.web.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.bacera.service.RdsBaceraBoneAgeService;
import com.rds.bacera.service.RdsBaceraDrugDetectionService;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("bacera/drugDetection")
public class RdsBaceraDrugDetectionController extends RdsBaceraAbstractController {

	@Setter
	@Autowired
	private RdsBaceraDrugDetectionService rdsBaceraDrugDetectionService;
	
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
	public Object queryAllPage(@RequestBody Map<String, Object> params)
			throws Exception {
		return rdsBaceraDrugDetectionService.queryAllPage(params);
	}

	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsBaceraDrugDetectionService.delete(params);
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
			String apro=params.get("program").toString();
			String []program=(apro.substring(1, apro.length()-1)).split(",");;
			String appraiser="";
			for (String i : program) {
				appraiser+=i+",";
			}
			appraiser = appraiser.substring(0, appraiser.length() - 1);
			params.put("program",appraiser);
			params.put("id", RdsUpcUUIDUtil.getUUID());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("num", params.get("num").toString());
			int numResult = rdsBaceraDrugDetectionService.queryNumExit(map);
			if (numResult > 0) {
				return this.setModel(false, "编号已存在，请重新输入");
			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
			params.put("case_type", "毒品检测"+params.get("program"));
			rdsBoneAgeService.insertFinance(params);
			int result = rdsBaceraDrugDetectionService.insert(params);
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
			String apro=params.get("program").toString();
			String []program=(apro.substring(1, apro.length()-1)).split(",");;
			String appraiser="";
			for (String i : program) {
				appraiser+=i+",";
			}
			appraiser = appraiser.substring(0, appraiser.length() - 1);
			params.put("program",appraiser);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("num", params.get("num").toString());
			map.put("id", params.get("id").toString());
			int numResult = rdsBaceraDrugDetectionService.queryNumExit(map);
			if (numResult <= 0) {
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("num", params.get("num").toString().trim());
				if (rdsBaceraDrugDetectionService.queryNumExit(map1) > 0)
					return this.setModel(false, "编号已存在，请重新修改");
			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
			rdsBoneAgeService.updateFinance(params);
			int result = rdsBaceraDrugDetectionService.update(params);
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
	 * 导出毒品检测信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("exportDrugDetection")
	public void exportDrugDetection(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			//编号
			String num = request.getParameter("num");
			params.put("num", num);
			//送检人
			String name = request.getParameter("name");
			params.put("name", name);
			//委托单位
			String entrusted_unit = request.getParameter("entrusted_unit");
//			String samplename = new String(request.getParameter("samplename").getBytes("ISO-8859-1"),"UTF-8");
			params.put("entrusted_unit", entrusted_unit);
			//样品类型
			String sample_type = request.getParameter("sample_type");
			params.put("sample_type", sample_type);
			//项目
			String program = "null".equals(request.getParameter("program"))?"":request.getParameter("program").toString();
			params.put("program", program);
			String inspection = request.getParameter("inspection");
			params.put("inspection", inspection);
			//归属人
			String ownperson = request.getParameter("ownperson");
			params.put("ownperson", ownperson);
			String partner = request.getParameter("partner");
			params.put("partner", partner);
			//起始时间
			params.put("drugDetection_starttime", request.getParameter("drugDetection_starttime"));
			params.put("drugDetection_endtime", request.getParameter("drugDetection_endtime"));
			//是否发报告
			params.put("reportif", request.getParameter("reportif"));
			//是否作废
			params.put("cancelif","0".equals(request.getParameter("cancelif"))?"":request.getParameter("cancelif"));
			//代理商
			String agent = request.getParameter("agent");
			params.put("agent", agent);
			//所属地
			params.put("areacode", "null".equals(request.getParameter("areacode"))?"":request.getParameter("areacode"));
			RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
			params.put("roleType", user.getRoletype());
			params.put("userCode", user.getUsercode());
			rdsBaceraDrugDetectionService.exportDrugDetection(params, response);;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("queryProgram")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> queryProgram(){
		return rdsBaceraDrugDetectionService.queryProgram();
	}
}
