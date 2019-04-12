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
import com.rds.bacera.service.RdsBaceraMedTranslationalService;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;
import com.rds.upc.web.common.RdsUpcUUIDUtil;

@Controller
@RequestMapping("bacera/medTranslational")
public class RdsBaceraMedTranslationalController extends RdsBaceraAbstractController {

	@Setter
	@Autowired
	private RdsBaceraMedTranslationalService rdsBaceraMedTranslationalService;
	
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
		return rdsBaceraMedTranslationalService.queryAllPage(params);
	}

	@RequestMapping("delete")
	@ResponseBody
	public Object delete(@RequestBody Map<String, Object> params) {
		try {
			rdsBaceraMedTranslationalService.delete(params);
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
			int numResult = rdsBaceraMedTranslationalService.queryNumExit(map);
			if (numResult > 0) {
				return this.setModel(false, "编号已存在，请重新输入");
			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
			params.put("case_type", "转化医学检验"+params.get("program"));
			rdsBoneAgeService.insertFinance(params);
			int result = rdsBaceraMedTranslationalService.insert(params);
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
			int numResult = rdsBaceraMedTranslationalService.queryNumExit(map);
			if (numResult <= 0) {
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("num", params.get("num").toString().trim());
				if (rdsBaceraMedTranslationalService.queryNumExit(map1) > 0)
					return this.setModel(false, "编号已存在，请重新修改");
			}
			RdsUpcUserModel user = (RdsUpcUserModel)request.getSession().getAttribute("user");
			params.put("inputperson", user.getUserid());
			rdsBoneAgeService.updateFinance(params);
			int result = rdsBaceraMedTranslationalService.update(params);
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
	@RequestMapping("exportMedTranslational")
	public void exportMedTranslational(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			//编号
			String num = request.getParameter("num");
//			String num = new String(request.getParameter("num").getBytes("ISO-8859-1"),"UTF-8");
			params.put("num", num);
			//样品名称
			String name = request.getParameter("name");
//			String samplename = new String(request.getParameter("samplename").getBytes("ISO-8859-1"),"UTF-8");
			params.put("name", name);
			//数量
			String program = "null".equals(request.getParameter("program"))?"":request.getParameter("program").toString();
//			String quantity = new String(request.getParameter("quantity").getBytes("ISO-8859-1"),"UTF-8");
			params.put("program", program);
			//归属人
			String ownperson = request.getParameter("ownperson");
			params.put("ownperson", ownperson);
			//起始时间
			params.put("medTranslational_starttime", request.getParameter("medTranslational_starttime"));
			params.put("medTranslational_endtime", request.getParameter("medTranslationale_endtime"));
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
			rdsBaceraMedTranslationalService.exportMedTranslational(params, response);;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("queryProgram")
	@ResponseBody
	public List<RdsJudicialKeyValueModel> queryProgram(){
		return rdsBaceraMedTranslationalService.queryProgram();
	}
}
