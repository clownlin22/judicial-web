package com.rds.children.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rds.children.mapper.RdsChildrenAgentiaMapper;
import com.rds.children.model.RdsChildrenAgentiaLocusModel;
import com.rds.children.model.RdsChildrenAgentiaModel;
import com.rds.children.service.RdsChildrenAgentiaService;
import com.rds.code.utils.uuid.UUIDUtil;
/**
 * 儿童基因试剂管理
 * @author 少明
 *
 */
@Service("RdsChildrenAgentiaService")
public class RdsChildrenAgentiaServiceImpl implements RdsChildrenAgentiaService {

	@Autowired
	private RdsChildrenAgentiaMapper aMapper;
	
	/**
	 * 获取试剂信息
	 */
	@Override
	public Map<String, Object> getAgentiaInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		List<RdsChildrenAgentiaModel> list = aMapper.getAgentiaInfo(params);
		int count = aMapper.getAgentiaInfoCount(params);
		params.clear();
		params.put("data", list);
		params.put("count", count);
		return params;
	}
	/**
	 * 新增试剂
	 */
	@Override
	@Transactional
	public Map<String, Object> save(Map<String, Object> params) {
		RdsChildrenAgentiaModel amodel = new RdsChildrenAgentiaModel();
		String agentia_id = UUIDUtil.getUUID();
		amodel.setAgentia_id(agentia_id);
		amodel.setAgentia_name((String)params.get("agentia_name"));
		amodel.setRemark((String)params.get("remark"));
		aMapper.insertAgentia(amodel);
		@SuppressWarnings("unchecked")
		List<String> locus_name = (List<String>)params.get("locus_name");
		@SuppressWarnings("unchecked")
		List<String> orders = (List<String>)params.get("order");
		RdsChildrenAgentiaLocusModel lmodel = null;
		List<RdsChildrenAgentiaLocusModel> llist = new ArrayList<>();
		for (int i = 0;i<orders.size();i++){
			lmodel = new RdsChildrenAgentiaLocusModel();
			lmodel.setAgentia_id(agentia_id);
			lmodel.setLocus_name(locus_name.get(i));
			lmodel.setOrder(Integer.parseInt(orders.get(i)));
			llist.add(lmodel);

		}
		aMapper.insertLocus(llist);
		params.put("success", true);
		params.put("message", "新增成功!");
		return params;
	}
	
	@Override
	public Map<String, Object> getAgentiaCombo() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", aMapper.getAgentiaCombo());
		return map;
	}
	
	/**
	 * 获取位点信息
	 */
	@Override
	public Map<String, Object> getLocusInfo(Map<String, Object> params) {
		List<RdsChildrenAgentiaLocusModel> list = aMapper.getLocusInfo(params);
		params.put("data", list);
		params.put("count", 0);
		return params;
	}
	
	@Transactional
	@Override
	public Map<String, Object> delete(Map<String, Object> params) {
		// TODO Auto-generated method stub
		aMapper.delete(params);
		params.put("success", true);
		params.put("message", "作废成功！");
		return params;
	}

}
