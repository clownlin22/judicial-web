package com.rds.bacera.web.controller;

import java.util.HashMap;
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

import com.rds.bacera.service.BaceraGeneTestingProjectService;
import com.rds.bacera.service.RdsBaceraBoneAgeService;
import com.rds.upc.model.RdsUpcUserModel;
import com.rds.upc.web.common.RdsUpcConstantUtil;

@Controller
@RequestMapping("bacera/Gene")
public class BaceraGeneTestingProjectController extends RdsBaceraAbstractController {
    @Setter
	@Autowired
	private BaceraGeneTestingProjectService baceraGeneTestingProjectService;
    
	@Setter
	@Autowired
	private RdsBaceraBoneAgeService rdsBoneAgeService;
	
	@RequestMapping("queryallpage")
	@ResponseBody
	public Object queryAllPage(@RequestBody Map<String, Object> params)
			throws Exception {
		return baceraGeneTestingProjectService.queryAllPage(params);
	}
	@RequestMapping("saveGeneExpress")
	@ResponseBody
	public Object saveGeneExpress(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		Object id = params.get("id");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		int temp = 0;
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("input_person", user.getUserid());
		try {
			temp = rdsBoneAgeService.queryExpressExit(map);
			if (temp == 0) {
				return insertExpress(params, request);
			} else if(temp > 0) {
				return updateExpress(params, request);
			}else
			{
				return this.setModel(false, RdsUpcConstantUtil.EXCEPTION);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, RdsUpcConstantUtil.EXCEPTION
					+ "\n" + e.getMessage());
		}
		
	}
	
	/**
	 * 插入快递信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	private Object insertExpress(Map<String, Object> params,
			HttpServletRequest request) {
		try {
			int result = rdsBoneAgeService.insertExpress(params);
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
	
	/**
	 * 更新快递信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	private Object updateExpress(Map<String, Object> params,
			HttpServletRequest request) {
		try {
			int result = rdsBoneAgeService.updateExpress(params);
			if (result > 0) {
				return this.setModel(true, "操作成功");
			} else {
				return this.setModel(false, "操作失败，请重试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}
	/**
	 * 财务
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping("saveGeneFinance")
	@ResponseBody
	public Object saveGeneFinance(@RequestBody Map<String, Object> params,
			HttpServletRequest request) {
		Object id = params.get("id");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		int temp = 0;
		RdsUpcUserModel user = (RdsUpcUserModel) request.getSession()
				.getAttribute("user");
		params.put("input_person", user.getUserid());
		try {
			temp = rdsBoneAgeService.queryFinanceExit(map);
			if (temp == 0) {
				return insertFinance(params, request);
			} else if(temp > 0) {
				return updateFinance(params, request);
			}else
			{
				return this.setModel(false, RdsUpcConstantUtil.EXCEPTION);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, RdsUpcConstantUtil.EXCEPTION
					+ "\n" + e.getMessage());
		}
	}

	/**
	 * 插入财务信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	private Object insertFinance(Map<String, Object> params,
			HttpServletRequest request) {
		try {
			int result = rdsBoneAgeService.insertFinance(params);
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

	/**
	 * 更新财务信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	private Object updateFinance(Map<String, Object> params,
			HttpServletRequest request) {
		try {
			int result = rdsBoneAgeService.updateFinance(params);
			if (result > 0) {
				return this.setModel(true, "操作成功");
			} else {
				return this.setModel(false, "操作失败，请重试");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return this.setModel(true, false, e.getMessage());
		}
	}
	/**
	 * 导出基因检测信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("exportGeneInfo")
	public void exportGeneInfo(HttpServletResponse response, HttpServletRequest request , HttpSession session) {
		try {
			
			Map<String, Object> params = new HashMap<String, Object>();
			String consumer_name = request.getParameter("consumer_name");
			params.put("consumer_name", consumer_name);
			String sample_number = request.getParameter("sample_number");
			params.put("sample_number", sample_number);
			String gene_starttime = request.getParameter("gene_starttime");
			params.put("gene_starttime", gene_starttime);
			params.put("gene_endtime", request.getParameter("gene_endtime"));
			params.put("test_number",request.getParameter("test_number"));
			String test_package_name = request.getParameter("test_package_name");
			params.put("test_package_name", test_package_name);
			params.put("agency_name",request.getParameter("agency_name"));
			params.put("test_item_names",request.getParameter("test_item_names"));
			params.put("charge_standard_name",request.getParameter("charge_standard_name"));
			params.put("consumer_sex",request.getParameter("consumer_sex"));
			params.put("consumer_phone",request.getParameter("consumer_phone"));
			RdsUpcUserModel user = (RdsUpcUserModel) session.getAttribute("user");
			params.put("roleType", user.getRoletype());
			params.put("userCode", user.getUsercode());
			baceraGeneTestingProjectService.exportGeneInfo(params, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
