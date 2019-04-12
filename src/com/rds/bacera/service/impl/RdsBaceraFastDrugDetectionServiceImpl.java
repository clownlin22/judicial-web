package com.rds.bacera.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.bacera.mapper.RdsBaceraDrugDetectionMapper;
import com.rds.bacera.mapper.RdsBaceraFastDrugDetectionMapper;
import com.rds.bacera.model.RdsBaceraDrugDetectionModel;
import com.rds.bacera.model.RdsBaceraFastDrugDetectionModel;
import com.rds.bacera.service.RdsBaceraFastDrugDetectionService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
@Service("rdsBaceraFastDrugDetectionServiceImpl")
public class RdsBaceraFastDrugDetectionServiceImpl  implements
RdsBaceraFastDrugDetectionService {
	
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");


@Setter
@Autowired
private RdsBaceraFastDrugDetectionMapper rdsBaceraFastDrugDetectionMapper;

@Override
public Object queryAll(Object params) throws Exception {
return 0;
}

@Override
public Object queryModel(Object params) throws Exception {
return null;
}

@Override
public Object queryAllPage(Object params) throws Exception {
Map<String, Object> result = new HashMap<String, Object>();
result.put("total", rdsBaceraFastDrugDetectionMapper.queryAllCount(params));
result.put("data", rdsBaceraFastDrugDetectionMapper.queryAllPage(params));
return result;
}

@Override
public int queryAllCount(Object params) throws Exception {
return 0;
}

@SuppressWarnings("unchecked")
@Override
public int update(Object params) throws Exception {
try {

	return rdsBaceraFastDrugDetectionMapper.update(params);
} catch (Exception e) {
	throw new Exception(e);
}
}

@Override
public int insert(Object params) throws Exception {
try {
	
	return rdsBaceraFastDrugDetectionMapper.insert(params);
} catch (Exception e) {
	throw new Exception(e);
}
}

@Override
public int delete(Object params) throws Exception {
return rdsBaceraFastDrugDetectionMapper.delete(params);
}

@Override
public int updateJunior(Object params) throws Exception {
return 0;
}

@SuppressWarnings("rawtypes")
@Override
public int queryNumExit(Map map) {
return rdsBaceraFastDrugDetectionMapper.queryNumExit(map);
}

@Override
public void exportFastDrugDetection( Map<String, Object> params,
	HttpServletResponse response) throws Exception {
String filename = "毒品快检仪";
if (FINANCE_PERMIT.contains(params.get("roleType").toString())
		|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
List<Object> list = rdsBaceraFastDrugDetectionMapper.queryAllPage(params);

	// 导出excel头列表
	Object[] titles = { "案例编号","负责人", "发货地址", "发货日期","试剂/仪器种类", "试剂/仪器数量", "试用类型","备注" };
	List<List<Object>> bodys = new ArrayList<List<Object>>();
	for (int i = 0; i < list.size(); i++) {
		List<Object> objects = new ArrayList<Object>();
		RdsBaceraFastDrugDetectionModel rdsBaceraDrugDetectionModel = (RdsBaceraFastDrugDetectionModel) list
				.get(i);
		// 按照顺序对应表头
		objects.add(rdsBaceraDrugDetectionModel.getNum());
		objects.add(rdsBaceraDrugDetectionModel.getPerson());
		objects.add(rdsBaceraDrugDetectionModel.getAddress());
		objects.add(rdsBaceraDrugDetectionModel.getDate());
		objects.add(rdsBaceraDrugDetectionModel.getReagent_type());
		objects.add(rdsBaceraDrugDetectionModel.getReagent_count());
		String trial_type=rdsBaceraDrugDetectionModel.getTrial_type().toString();
		if("1".equals(trial_type)){
			trial_type="采购";
		}
		if("2".equals(trial_type)){
			trial_type="试用";
		}
		if("3".equals(trial_type)){
			trial_type="销售";
		}
		objects.add(trial_type);
		objects.add(rdsBaceraDrugDetectionModel.getRemark());
		
		bodys.add(objects);
	}
	ExportUtils.export(response, filename, titles, bodys, "毒品快检仪");
}else{
	String input = params.get("userId").toString();
	params.put("input", input);
	List<Object> list = rdsBaceraFastDrugDetectionMapper.queryAllPage(params);

	// 导出excel头列表
	Object[] titles = { "案例编号","负责人", "发货地址", "发货日期","试剂/仪器种类", "试剂/仪器数量", "试用类型","备注" };
	List<List<Object>> bodys = new ArrayList<List<Object>>();
	for (int i = 0; i < list.size(); i++) {
		List<Object> objects = new ArrayList<Object>();
		RdsBaceraFastDrugDetectionModel rdsBaceraDrugDetectionModel = (RdsBaceraFastDrugDetectionModel) list
				.get(i);
		// 按照顺序对应表头
		objects.add(rdsBaceraDrugDetectionModel.getNum());
		objects.add(rdsBaceraDrugDetectionModel.getPerson());
		objects.add(rdsBaceraDrugDetectionModel.getAddress());
		objects.add(rdsBaceraDrugDetectionModel.getDate());
		objects.add(rdsBaceraDrugDetectionModel.getReagent_type());
		objects.add(rdsBaceraDrugDetectionModel.getReagent_count());
		String trial_type=rdsBaceraDrugDetectionModel.getTrial_type().toString();
		if("1".equals(trial_type)){
			trial_type="采购";
		}
		if("2".equals(trial_type)){
			trial_type="试用";
		}
		if("3".equals(trial_type)){
			trial_type="销售";
		}
		objects.add(trial_type);
		objects.add(rdsBaceraDrugDetectionModel.getRemark());
		
		bodys.add(objects);
	}
	ExportUtils.export(response, filename, titles, bodys, "毒品快检仪");
}

}

@Override
public List<RdsJudicialKeyValueModel> queryProgram() {
return rdsBaceraFastDrugDetectionMapper.queryProgram();
}



}
