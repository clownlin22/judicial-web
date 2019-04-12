package com.rds.crm.mapper;

import java.util.List;
import java.util.Map;

import com.rds.crm.model.RdsCrmCallBackModel;
import com.rds.crm.model.RdsCrmFeeModel;
import com.rds.crm.model.RdsCrmOrderModel;
import com.rds.crm.model.RdsCrmSampleModel;

public interface RdsCrmRegesterMapper {

	List<RdsCrmOrderModel> getOrderList(Map<String, Object> params);

	int getOrderCount(Map<String, Object> params);

	int getOrderCountByPhone(String phone);

	List<Map<String, Object>> getDetectionClass();

	List<Map<String, Object>> getDetectionOrg(String id);

	int insertOrder(RdsCrmOrderModel oModel);

	int updateOrder(Map<String, Object> params);

	List<Map<String, Object>> getSampleCombo();

	int insertSample(List<RdsCrmSampleModel> sList);

	List<RdsCrmSampleModel> getSampleName(String orderId);

	int deleteSampleByOrderId(String orderId);

	int insertFee(RdsCrmFeeModel crmFeeModel);

	List<RdsCrmCallBackModel> getCallBack(Map<String, Object> params);

	int getCallBackCount(Map<String, Object> params);

	int saveCallBack(RdsCrmCallBackModel cModel);

	List<RdsCrmFeeModel> getOrderFeeList(Map<String, Object> params);

	int getOrderFeeListCount(Map<String, Object> params);

	List<RdsCrmOrderModel> getOrderListQuery(Map<String, Object> params);

	int getOrderQueryCount(Map<String, Object> params);

}
