package com.rds.appraisal.service;

import java.util.List;

/**
 * 案例打印接口
 * 
 * @author yxb
 *
 */
public interface RdsAppraisalPrinterService extends RdsAppraisalBaseService {
	
	/**
	 * 获取打印信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<String> getPrinterInfo(Object params) throws Exception;
	
}
