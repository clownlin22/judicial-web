package com.rds.crm.service;

import java.util.List;
import java.util.Map;

import com.rds.crm.model.RdsCrmSampleModel;

public interface RdsCrmRegesterService {

	Map<String, Object> getOrderList(Map<String, Object> params);

	boolean isPhoneIn(String phone);

	Map<String, Object> getDetectionClass();

	Map<String, Object> getDetectionOrg(String id);

	Map<String, Object> saveOrderInfo(Map<String, Object> params);

	Map<String, Object> updateOrderInfo(Map<String, Object> params);

	Map<String, Object> getSampleCombo();

	List<RdsCrmSampleModel> getSampleName(Map<String, Object> params);

	Map<String, Object> saveFeeInfo(Map<String, Object> params);

	Map<String, Object> getCallBack(Map<String, Object> params);

	Map<String, Object> saveCallBack(Map<String, Object> params);

	Map<String, Object> getOrderFeeList(Map<String, Object> params);

	Map<String, Object> getOrderListQuery(Map<String, Object> params);

}
