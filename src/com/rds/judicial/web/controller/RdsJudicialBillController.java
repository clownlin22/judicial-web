package com.rds.judicial.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rds.judicial.service.RdsJudicialBillService;
import com.rds.judicial.web.common.PageUtil;
import com.rds.upc.model.RdsUpcUserModel;

/**
 * @description 发票管理
 * @author 傅少明
 * 2015年5月1日
 *
 */
@Controller
@RequestMapping("judicial/bill")
public class RdsJudicialBillController {

	@Setter
	@Autowired
	private RdsJudicialBillService billService;
	/**
	 * 查询所有已开发票
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("queryAllBill")
	@ResponseBody
	public Map<String , Object> queryALLBill(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception{
		return billService.queryAllBill(PageUtil.pageSet(params),request);
	}
	/**
	 * 开发票
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@ResponseBody
	public Map<String, Object> save(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception{
		return billService.save(params,request);
	}
	/**
	 * 删除发票记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("delete")
	@ResponseBody
	public Map<String, Object> delete(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception{
		return billService.delete(params,request);
	}
	@RequestMapping("queryUsertype")
	@ResponseBody
	public Map<String, Object> queryUsertype(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		RdsUpcUserModel user =	(RdsUpcUserModel) request.getSession().getAttribute("user");
		if(user == null){
			map.put("result", false);
			return map;
		}
		map.put("usertype", user.getUsertype());
		map.put("result", true);
		return map;
	}
}
