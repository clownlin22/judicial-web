package com.rds.children.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.children.mapper.RdsChildGeneCaseMapper;
import com.rds.children.model.RdsChildrenCaseInfoModel;
import com.rds.children.model.RdsChildrenCaseLocusModel;
import com.rds.children.model.RdsChildrenCustodyInfoModel;
import com.rds.children.model.RdsChildrenQueryModel;
import com.rds.children.service.RdsChildrenGeneQueryService;
@Service("RdsChildrenGeneQueryService")
public class RdsChildrenGeneQueryServiceImpl implements
		RdsChildrenGeneQueryService {

	@Autowired
	private RdsChildGeneCaseMapper caseMapper;
	
	@Override
	public Map<String, Object> getGenInfo(RdsChildrenQueryModel queryModel) {
		Map<String, Object> map = new HashMap<String, Object>();
		//获取儿童信息
		List<RdsChildrenCaseInfoModel> childlist = caseMapper.getChild(queryModel);
		if (childlist.size() < 1) {
			map.put("msg", "未查到对应宝宝基因信息，请确认查询条件无误！");
			map.put("success", false);
			return map;
		}
		//获取监护人信息
		List<RdsChildrenCustodyInfoModel> custodylist = caseMapper.getCustody(childlist.get(0).getCase_id());
		//获取基因列表
		List<RdsChildrenCaseLocusModel> locuslist = caseMapper.getLocus(childlist.get(0).getCase_id());
		for(int i=0;i<locuslist.size();i++){
			locuslist.get(i).setLocus_value1(locuslist.get(i).getLocus_value().split(",")[0]);
			locuslist.get(i).setLocus_value2(locuslist.get(i).getLocus_value().split(",")[1]);
		}
		map.put("childinfo", childlist.get(0));
		map.put("custodyinfo", custodylist);
		map.put("locusinfo", locuslist);
		map.put("success", true);
		map.put("msg", "success");
		return map;
	}

//	@Override
//	public Map<String, Object> queryChildGene(String id_number,
//			String child_name, String birth_date){
//		RdsChildrenQueryModel queryModel = new RdsChildrenQueryModel(id_number,child_name,birth_date);
//		return getGenInfo(queryModel);
//	}
}
