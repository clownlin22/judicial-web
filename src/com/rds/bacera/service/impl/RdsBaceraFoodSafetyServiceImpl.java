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

import com.rds.bacera.mapper.RdsBaceraFoodSafetyMapper;
import com.rds.bacera.model.RdsBaceraFoodSafetyModel;
import com.rds.bacera.service.RdsBaceraFoodSafetyService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;

@Service("rdsBaceraFoodSafetyServiceImpl")
public class RdsBaceraFoodSafetyServiceImpl implements RdsBaceraFoodSafetyService {
	
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";

	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");
	
	@Setter
	@Autowired
	private RdsBaceraFoodSafetyMapper rdsFoodSafetyMapper;

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
		result.put("total", rdsFoodSafetyMapper.queryAllCount(params));
		result.put("data", rdsFoodSafetyMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Object params) throws Exception {
		try {
			return rdsFoodSafetyMapper.update(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int insert(Object params) throws Exception {
		try {
			return rdsFoodSafetyMapper.insert(params);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return rdsFoodSafetyMapper.delete(params);
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int queryNumExit(Map map) {
		return rdsFoodSafetyMapper.queryNumExit(map);
	}

	@Override
	public void exportFoodSafe(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "食品安全检测";
		List<Object> list = rdsFoodSafetyMapper.queryAllPage(params);
		if (FINANCE_PERMIT.contains(params.get("roleType").toString())
				|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
			//导出excel头列表
			Object[] titles = { "编号", "样本名称", "生产日期","数量","样本数","服务商","测试项目","检测方法","项目类型",
					"应收款项","所付款项", "到款时间", "确认时间","优惠价格", "财务备注","快递日期","快递类型","快递单号",
					"快递备注","归属地","归属人","代理商","是否发报告","备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for(int i = 0 ; i < list.size() ; i ++)
			{
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraFoodSafetyModel rdsFoodSafetyModel = (RdsBaceraFoodSafetyModel)list.get(i);
				//按照顺序对应表头
				objects.add(rdsFoodSafetyModel.getNum());
				objects.add(rdsFoodSafetyModel.getSamplename());
				objects.add(StringUtils.dateToChineseTen(rdsFoodSafetyModel.getDate()));
				objects.add(rdsFoodSafetyModel.getQuantity());
				objects.add(rdsFoodSafetyModel.getSampleCount());
				objects.add(rdsFoodSafetyModel.getServiceprovider());
				objects.add(rdsFoodSafetyModel.getTestitems());
				objects.add(rdsFoodSafetyModel.getTestmethod());
				objects.add(rdsFoodSafetyModel.getProgram_type());
				//应收款项
				objects.add(("".equals(rdsFoodSafetyModel.getReceivables()) || null == rdsFoodSafetyModel
						.getReceivables()) ? 0 : Float.parseFloat(rdsFoodSafetyModel.getReceivables()));
				//所付款项
				objects.add(("".equals(rdsFoodSafetyModel.getPayment()) || null == rdsFoodSafetyModel
						.getPayment()) ? 0 : Float.parseFloat(rdsFoodSafetyModel.getPayment()));
				objects.add(StringUtils.dateToChineseTen(rdsFoodSafetyModel.getParagraphtime()));
				objects.add(StringUtils.dateToChineseTen(rdsFoodSafetyModel.getConfirm_date()));
				objects.add(rdsFoodSafetyModel.getDiscountPrice());
				objects.add(rdsFoodSafetyModel.getRemarks());
				objects.add(StringUtils.dateToChineseTen(rdsFoodSafetyModel.getExpresstime()));
				objects.add(rdsFoodSafetyModel.getExpresstype());
				objects.add(rdsFoodSafetyModel.getExpressnum());
				objects.add(rdsFoodSafetyModel.getExpressremark());
				
				objects.add(rdsFoodSafetyModel.getAreaname());
				objects.add(rdsFoodSafetyModel.getOwnpersonname());
				objects.add(rdsFoodSafetyModel.getAgentname());
				objects.add("1".equals(rdsFoodSafetyModel.getReportif())?"是":"否");
				objects.add(rdsFoodSafetyModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "食品安全检测");
		}else
		{
			//导出excel头列表
			Object[] titles = { "编号", "样本名称", "生产日期","数量","样本数","服务商","测试项目","检测方法","项目类型",
					"快递日期","快递类型","快递单号","快递备注","归属地","归属人","代理商","是否发报告","备注和要求" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for(int i = 0 ; i < list.size() ; i ++)
			{
				List<Object> objects = new ArrayList<Object>();
				RdsBaceraFoodSafetyModel rdsFoodSafetyModel = (RdsBaceraFoodSafetyModel)list.get(i);
				//按照顺序对应表头
				objects.add(rdsFoodSafetyModel.getNum());
				objects.add(rdsFoodSafetyModel.getSamplename());
				objects.add(StringUtils.dateToChineseTen(rdsFoodSafetyModel.getDate()));
				objects.add(rdsFoodSafetyModel.getQuantity());
				objects.add(rdsFoodSafetyModel.getSampleCount());
				objects.add(rdsFoodSafetyModel.getServiceprovider());
				objects.add(rdsFoodSafetyModel.getTestitems());
				objects.add(rdsFoodSafetyModel.getTestmethod());
				objects.add(rdsFoodSafetyModel.getProgram_type());
				objects.add(StringUtils.dateToChineseTen(rdsFoodSafetyModel.getExpresstime()));
				objects.add(rdsFoodSafetyModel.getExpresstype());
				objects.add(rdsFoodSafetyModel.getExpressnum());
				objects.add(rdsFoodSafetyModel.getExpressremark());
				objects.add(rdsFoodSafetyModel.getAreaname());
				objects.add(rdsFoodSafetyModel.getOwnpersonname());
				objects.add(rdsFoodSafetyModel.getAgentname());
				objects.add("1".equals(rdsFoodSafetyModel.getReportif())?"是":"否");
				objects.add(rdsFoodSafetyModel.getRemark());
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "食品安全检测");
		}
	
	}

}
