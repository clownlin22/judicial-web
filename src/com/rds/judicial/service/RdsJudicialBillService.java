package com.rds.judicial.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface RdsJudicialBillService {

	public Map<String, Object> queryAllBill(Map<String, Object> params,HttpServletRequest request) throws Exception;

	public Map<String, Object> save(Map<String, Object> params,HttpServletRequest request) throws Exception;

	public Map<String, Object> delete(Map<String, Object> params,HttpServletRequest request) throws Exception;

}
