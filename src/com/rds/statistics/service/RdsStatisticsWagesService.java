package com.rds.statistics.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * @author yxb
 * @className
 * @description
 * @date 2017/8/15
 */
public interface RdsStatisticsWagesService {
	List<Object> queryAllPage(Map<String, Object> params);

	int queryAllCount(Map<String, Object> params);

	boolean insertWages(Map<String, Object> params);

	List<Object> queryAll(Map<String, Object> params);
	
	boolean deleteWages(Map<String, Object> params);
	
	Map<String,Object> uploadWages(String wages_month,String attachmentPath,String userid,String attachment_id) throws Exception;

	List<Object> queryWagesAttachment(Map<String, Object> params);
	
	boolean insertWagesAttachment(Map<String, Object> params);

	boolean updateWagesAttachment(Map<String, Object> params);
	
    void exportWagesInfo(Map<String,Object> params,HttpServletResponse response)  throws Exception;
	
}
