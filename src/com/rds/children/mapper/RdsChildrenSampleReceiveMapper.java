package com.rds.children.mapper;

import java.util.List;
import java.util.Map;

import com.rds.children.model.RdsChildrenSampleModel;
import com.rds.children.model.RdsChildrenSampleReceiveModel;

public interface RdsChildrenSampleReceiveMapper {
	
	/**
	 * 查询接样信息
	 * @param param
	 * @return
	 */
	List<RdsChildrenSampleReceiveModel> getSampleReceiveInfo(Map<String, Object> param);
	
	/**
	 * 计数接样信息
	 * @param param
	 * @return
	 */
	int getCountSampleReceiveInfo(Map<String, Object> param);
	
	/**
	 * 分页查询交接单信息
	 * @param param
	 * @return
	 */
	List<RdsChildrenSampleReceiveModel> getTransferInfo(Map<String, Object> param);
	
	/**
	 * 计数交接单信息
	 * @param param
	 * @return
	 */
	int getTransferInfoCount(Map<String, Object> param);
	
	/**
	 * 根绝receiveId查询交接样本
	 * @param param
	 * @return
	 */
	List<RdsChildrenSampleReceiveModel> getRelaySampleInfo(Map<String, Object> param);
	
	/**
	 * 保存交接单信息
	 * @param param
	 * @return
	 */
	boolean saveTransferNum(Map<String, Object> param);
	
	/**
	 * 保存交接单样本信息
	 * @param param
	 * @return
	 */
	boolean saveTransferSample(Map<String, Object> param);
	
	/**
	 * 更新交接单状态
	 * @param param
	 * @return
	 */
	boolean updateTransferNum(Map<String, Object> param);
	
	/**
	 * 更新交接单样本确认状态
	 * @param param
	 * @return
	 */
	boolean updateTransferSample(Map<String, Object> param);
	
	/**
	 * 校验样本审核状态
	 * @param param
	 * @return
	 */
	List<String> queryVerifybySampleCode(Map<String, Object> param);
	
	/**
	 * 根据样本编号查询案例编号
	 * @param param
	 * @return
	 */
	List<String> queryCaseCodebySampleCode(Map<String, Object> param);
	
	/**
	 * 更新案例状态
	 * @param param
	 * @return
	 */
	boolean updateCaseState(Map<String, Object> param);
	
	/**
	 * 查询样本是否接收过
	 * @param param
	 * @return
	 */
	List<String> queryTransferbySampleCode(Map<String, Object> param);
	
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
	 * 保存确认信息
	 * @param param
	 * @return
	 */
	boolean saveReceiveConfirm(Map<String, Object> param);
	
	/**
	 * 根据接收id查询接收案例编号
	 * @param param
	 * @return
	 */
	List<String> queryCaseCodebyReceive(Map<String, Object> param);
}
