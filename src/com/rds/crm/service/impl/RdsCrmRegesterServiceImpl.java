package com.rds.crm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.text.SimpleDateFormat;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.crm.mapper.RdsCrmRegesterMapper;
import com.rds.crm.model.RdsCrmCallBackModel;
import com.rds.crm.model.RdsCrmFeeModel;
import com.rds.crm.model.RdsCrmOrderModel;
import com.rds.crm.model.RdsCrmSampleModel;
import com.rds.crm.service.RdsCrmRegesterService;

@Service("RdsCrmRegesterService")
@Transactional
public class RdsCrmRegesterServiceImpl implements RdsCrmRegesterService {

	@Autowired
	private RdsCrmRegesterMapper rMapper;

	@Override
	public Map<String, Object> getOrderList(Map<String, Object> params) {
		List<RdsCrmOrderModel> olist = rMapper.getOrderList(params);
		int count = rMapper.getOrderCount(params);
		return setResp(olist, count);
	}

	private Map<String, Object> setResp(Object data, int count) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", data);
		map.put("count", count);
		return map;
	}

	private Map<String, Object> setResp(boolean success, String msg) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("msg", msg);
		return map;
	}

	@Override
	public boolean isPhoneIn(String phone) {
		int re = rMapper.getOrderCountByPhone(phone);
		if (re > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Map<String, Object> getDetectionClass() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", rMapper.getDetectionClass());
		return map;
	}

	@Override
	public Map<String, Object> getDetectionOrg(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", rMapper.getDetectionOrg(id));
		return map;
	}

	@Override
	public Map<String, Object> saveOrderInfo(Map<String, Object> params) {
		RdsCrmOrderModel oModel = new RdsCrmOrderModel();
		String orderId = UUIDUtil.getUUID();
		oModel.setOrderId(orderId);
		oModel.setOrderInPer((String) params.get("user"));
		oModel.setAddress((String) params.get("address"));
		oModel.setClient((String) params.get("client"));
		oModel.setConsultCount(Integer.parseInt((String) params
				.get("consultCount")));
		oModel.setDetectionClass((String) params.get("detectionClass"));
		oModel.setDetectionOrg((String) params.get("detectionOrg"));
		oModel.setIsExtendOrder(Integer.parseInt((String) params
				.get("isExtendOrder")));
		oModel.setIsPostpaid(Integer.parseInt((String) params.get("isPostpaid")));
		oModel.setOrderInDate((String) params.get("orderInDate"));
		oModel.setOrderType(Integer.parseInt((String) params.get("orderType")));
		oModel.setPhone((String) params.get("phone"));
		oModel.setRemark((String) params.get("remark"));
		oModel.setStandFee(Double.parseDouble((String) params.get("standFee")));
		oModel.setStatus(0);
		oModel.setIsArchive(0);

		List<RdsCrmSampleModel> sList = new ArrayList<RdsCrmSampleModel>();
		RdsCrmSampleModel sModel;
		for (String sampleName : getValues(params.get("sampleName"))) {
			sModel = new RdsCrmSampleModel(UUIDUtil.getUUID());
			sModel.setSampleName(sampleName);
			sModel.setOrderId(orderId);
			sList.add(sModel);
		}
		if (rMapper.insertOrder(oModel) > 0 && rMapper.insertSample(sList) > 0) {
			return setResp(true, "OK");
		}
		return setResp(false, "保存失败！");
	}

	@Override
	public Map<String, Object> updateOrderInfo(Map<String, Object> params) {
		String orderId = (String) params.get("orderId");

		int dere = rMapper.deleteSampleByOrderId(orderId);
		List<RdsCrmSampleModel> sList = new ArrayList<RdsCrmSampleModel>();
		RdsCrmSampleModel sModel;
		List<String> sampleNameList = getValues(params.get("sampleName"));
		for (String sampleName : sampleNameList) {
			sModel = new RdsCrmSampleModel(UUIDUtil.getUUID());
			sModel.setSampleName(sampleName);
			sModel.setOrderId(orderId);
			sList.add(sModel);
		}
		rMapper.insertSample(sList);
		int re = rMapper.updateOrder(params);
		if (re > 0) {
			return setResp(true, "OK");
		}
		return setResp(false, "更新失败！");
	}

	@Override
	public Map<String, Object> getSampleCombo() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", rMapper.getSampleCombo());
		return map;
	}

	@Override
	public List<RdsCrmSampleModel> getSampleName(Map<String, Object> params) {
		String orderId = (String) params.get("orderId");
		List<RdsCrmSampleModel> sList = rMapper.getSampleName(orderId);
		return sList;
	}

	private static List<String> getValues(Object object) {
		List<String> values = new ArrayList<String>();
		if (object != null) {
			String str = object.toString();
			String[] objects = str.split(",");
			if (objects.length > 1) {
				str = str.substring(1, str.length() - 1);
				String[] objs = str.split(",");
				for (String s : objs) {
					values.add(s.trim());
				}
			} else {
				values.add(str.trim());
			}
		}
		return values;
	}

	@Override
	public Map<String, Object> saveFeeInfo(Map<String, Object> params) {

		RdsCrmFeeModel crmFeeModel = new RdsCrmFeeModel();
		crmFeeModel.setFeeId(UUIDUtil.getUUID());
		crmFeeModel.setOrderId(params.get("orderId").toString());
		double realFee = Double.parseDouble(params.get("realFee").toString()
				.trim());
		if (params.get("isRefund").toString().equals("1")) {
			realFee = 0 - realFee;
		}
		crmFeeModel.setRealFee(realFee);
		crmFeeModel.setFeeTime(new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()));
		if (rMapper.insertFee(crmFeeModel) > 0) {
			return setResp(true, "ok");
		}
		return setResp(false, "出错了，请联系管理员！");
	}

	@Override
	public Map<String, Object> getCallBack(Map<String, Object> params) {
		List<RdsCrmCallBackModel> list = rMapper.getCallBack(params);
		int count = rMapper.getCallBackCount(params);
		return setResp(list, count);
	}

	@Override
	public Map<String, Object> saveCallBack(Map<String, Object> params) {
		RdsCrmCallBackModel cModel = new RdsCrmCallBackModel();
		cModel.setOrderId(params.get("orderId").toString());
		cModel.setCbContent(params.get("cbContent").toString());
		cModel.setCbId(UUIDUtil.getUUID());
		cModel.setCbInPer(params.get("user").toString());
		cModel.setCbTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		if (rMapper.saveCallBack(cModel) > 0) {
			return setResp(true, "ok");
		}
		return setResp(false, "保存失败");
	}

	@Override
	public Map<String, Object> getOrderFeeList(Map<String, Object> params) {
		List<RdsCrmFeeModel> flist = rMapper.getOrderFeeList(params);
		int count = rMapper.getOrderFeeListCount(params);
		return setResp(flist, count);
	}

	@Override
	public Map<String, Object> getOrderListQuery(Map<String, Object> params) {
		List<RdsCrmOrderModel> olist = rMapper.getOrderListQuery(params);
		int count = rMapper.getOrderQueryCount(params);
		return setResp(olist, count);
	}
}
