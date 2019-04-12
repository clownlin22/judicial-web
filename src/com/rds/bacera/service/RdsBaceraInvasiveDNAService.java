package com.rds.bacera.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.rds.bacera.model.RdsBaceraBoneAgeFeeModel;


/**
 * 无创亲子鉴定-service
 * @author Administrator
 *
 */
public interface RdsBaceraInvasiveDNAService extends RdsBaceraBaseService {
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map);
	void exportInvasiveInfo(Map<String, Object> params,HttpServletResponse response) throws Exception;
	
	/**
	 * 无创dna财务公式分页
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryInvasiveDNAFee(Object params) throws Exception;
	
	/**
	 * 保存无创dna财务信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveInvasiveDNAFee(Object params) throws Exception;
	
	/**
	 * 更新无创dna财务信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateInvasiveDNAFee(Object params) throws Exception;
	
	public int queryInvasiveDNAFeeCount(Object params) throws Exception;
	
	public int deleteInvasiveDNAFee(Object params) throws Exception;
	
	public RdsBaceraBoneAgeFeeModel queryInvasiveDNAFeeByRec(Object params) throws Exception;
}
