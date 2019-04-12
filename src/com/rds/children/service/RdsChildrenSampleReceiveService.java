package com.rds.children.service;

import java.util.List;
import java.util.Map;

import com.rds.children.model.RdsChildrenResponse;
import com.rds.children.model.RdsChildrenSampleModel;
import com.rds.children.model.RdsChildrenSampleReceiveModel;


public interface RdsChildrenSampleReceiveService {
	
	/**
	 * 分页查询样本交接信息
	 * @param params
	 * @return
	 */
	RdsChildrenResponse getSampleReceiveInfo(Map<String, Object> params);
	
	/**
	 * 分页查询交接单信息
	 * @param param
	 * @return
	 */
	RdsChildrenResponse getTransferInfo(Map<String, Object> param);
	
	/**
	 * 查询待交接样本
	 * @param param
	 * @return
	 */
	List<RdsChildrenSampleReceiveModel> getRelaySampleInfo(Map<String, Object> param);
	
	/**
	 * 接收样本信息
	 * @param params
	 * @return
	 */
	Map<String,Object> saveReceiveSampleInfo(Map<String, Object> params) throws Exception;
	
	/**
	 * 获取样本条码数
	 * @param params
	 * @return
	 */
	int getCountSampleReceiveInfo(Map<String, Object> params);
	
	/**
	 * 判断样本编号是否存在
	 * @param param
	 * @return
	 */
	int existSampleCode(Map<String, Object> param);
	
	/**
	 * 获取待确认的样本
	 * @param param
	 * @return
	 */
	List<RdsChildrenSampleModel> getSampleInfo(Map<String, Object> param);
	
	/**
	 * 确认样本接收
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> confirmRelaySampleInfo(Map<String, Object> params) throws Exception;
}
