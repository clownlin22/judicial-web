package com.rds.judicial.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialBillMapper;
import com.rds.judicial.mapper.RdsJudicialPhoneMapper;
import com.rds.judicial.mapper.RdsJudicialRegisterMapper;
import com.rds.judicial.service.RdsJudicialBillService;
import com.rds.upc.model.RdsUpcUserModel;

@Service("RdsJudicialBillService")
public class RdsJudicialBillServiceImpl implements RdsJudicialBillService {

	@Setter
	@Autowired
	private RdsJudicialBillMapper billMapper;

	@Setter
	@Autowired
	private RdsJudicialRegisterMapper registerMapper;
	
	@Setter
	@Autowired
	private RdsJudicialPhoneMapper pMapper;
	
	private Map<String, Object> setMsg(boolean success, String message) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", success);
		map.put("message", message);
		return map;
	}
	@Override
	public Map<String, Object> queryAllBill(Map<String, Object> params,HttpServletRequest request)
			throws Exception {
		RdsUpcUserModel user =	(RdsUpcUserModel) request.getSession().getAttribute("user");
		if(user == null){
			return setMsg(false,"请重新登录");
		}
		//如果用户类型是总经理就显示全部否则只显示当前用户开票信息
//		if (!user.getUsertype().equals("1")){
//			params.put("bill_per",user.getUsercode());
//		}else
//			params.put("bill_per", "");
		List<Object> list = billMapper.queryAllPage(params);
		int total = billMapper.queryAllCount(params);
		Map<String, Object> map = new HashMap<String, Object>();
		
			map.put("data", list);
			map.put("total", total);
			map.put("result", true);
			

		return map;
	}

	@Override
	@Transactional
	public Map<String, Object> save(Map<String, Object> params,
			HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String case_code = pMapper.getCaseID((String) params
				.get("case_code"));
		if (case_code == null || "".equals(case_code)) {
			map.put("result", false);
			map.put("message", "新增开票失败，案例编号有误");
			return map;
		}
		params.put("case_id",
				pMapper.getCaseID((String) params.get("case_code")));

		params.put("bill_id", UUIDUtil.getUUID());
		params.put("bill_code", params.get("bill_codes"));
		params.put("bill_charge", params.get("bill_charges"));
		params.put("remark", params.get("remarks"));
		// params.put("date",
		// new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		params.put("bill_per", ((RdsUpcUserModel) request.getSession()
				.getAttribute("user")).getUserid());
		params.put("is_bill", 1);

		if (registerMapper.updateIsBill(params) > 0
				&& billMapper.insert(params) > 0) {
			map.put("result", true);
			map.put("message", "新增开票成功");
		} else {
			map.put("result", false);
			map.put("message", "新增开票失败，请联系管理员");
		}
		return map;
	}

	@Override
	@Transactional
	public Map<String, Object> delete(Map<String, Object> params,HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		RdsUpcUserModel user =	(RdsUpcUserModel) request.getSession().getAttribute("user");
		if(user == null){
			return setMsg(false,"请重新登录");
		}
		//组长可以删除任何人的开票数据否则只能删除自己的开票数据
		if (!user.getUsertype().equals("1") || user.getUsercode().equals(billMapper.queryByBillId(params).getBill_per())){
			map.put("result", false);
			map.put("message", "不可删除非该用户开据票据记录");
			return map;
		}
		
		//可能存在一个案例开两次发票的状况
		boolean flag = false;
		if (billMapper.queryByCaseId(params).size() > 1) {
			if (billMapper.delete(params) > 0)
				flag = true;
		} else {
			params.put("is_bill", 0);
			if (billMapper.delete(params) > 0
					&& registerMapper.updateIsBill(params) > 0)
				flag = true;
		}
		map.put("result", flag);
		if (flag)
			map.put("message", "删除成功");
		else
			map.put("message", "删除失败,请联系管理员");
		return map;
	}
}
