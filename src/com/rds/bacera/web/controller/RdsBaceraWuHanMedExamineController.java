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
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.bacera.service.RdsBaceraBoneAgeService;
import com.rds.bacera.service.RdsBaceraWuHanMedExamineService;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("bacera/wuHanMedExamine")
public class RdsBaceraWuHanMedExamineController extends RdsBaceraAbstractController {

	@Setter
	@Autowired
	private RdsBaceraWuHanMedExamineService rdsBaceraWuHanMedExamineService;
	
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
		return rdsBaceraWuHanMedExamineService.queryAllPage(params);
	}

	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsBaceraWuHanMedExamineService.delete(params);
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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("num", params.get("num").toString());
			int numResult = rdsBaceraWuHanMedExamineService.queryNumExit(map);
			if (numResult > 0) {
				return this.setModel(false, "编号已存在，请重新输入");
			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
			params.put("case_type", "医学检验"+params.get("program"));
			rdsBoneAgeService.insertFinance(params);
			int result = rdsBaceraWuHanMedExamineService.insert(params);
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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("num", params.get("num").toString());
			map.put("id", params.get("id").toString());
			int numResult = rdsBaceraWuHanMedExamineService.queryNumExit(map);
			if (numResult <= 0) {
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("num", params.get("num").toString().trim());
				if (rdsBaceraWuHanMedExamineService.queryNumExit(map1) > 0)
					return this.setModel(false, "编号已存在，请重新修改");
			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
			rdsBoneAgeService.updateFinance(params);
			int result = rdsBaceraWuHanMedExamineService.update(params);
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
	 * 导出骨龄信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("exportMedExamine")
	public void exportMedExamine(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			//编号
			String num = request.getParameter("num");
//			String num = new String(request.getParameter("num").getBytes("ISO-8859-1"),"UTF-8");
			params.put("num", num);
			//样品名称
			String name = request.getParameter("ename");
//			String samplename = new String(request.getParameter("samplename").getBytes("ISO-8859-1"),"UTF-8");
			params.put("name", name);
			//数量
			String program = request.getParameter("program");
//			String quantity = new String(request.getParameter("quantity").getBytes("ISO-8859-1"),"UTF-8");
			params.put("program", program);
			//归属人
			String ownperson = request.getParameter("ownperson");
			params.put("ownperson", ownperson);
			//起始时间
			params.put("medExamine_starttime", request.getParameter("medExamine_starttime"));
			params.put("medExamine_endtime", request.getParameter("medExamine_endtime"));
			params.put("expressnum", request.getParameter("expressnum")); 
			params.put("express_starttime", request.getParameter("express_starttime")); 
			params.put("express_endtime", request.getParameter("express_endtime")); 
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
			rdsBaceraWuHanMedExamineService.exportMedExamine(params, response);;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("queryProgram")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> queryProgram(){
		return rdsBaceraWuHanMedExamineService.queryProgram();
	}
}
