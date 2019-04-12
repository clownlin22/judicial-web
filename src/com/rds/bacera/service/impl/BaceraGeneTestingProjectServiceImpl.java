package com.rds.bacera.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;
import com.rds.code.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.bacera.mapper.BaceraGeneTestingProjectMapper;
import com.rds.bacera.mapper.RdsBaceraBoneAgeMapper;
import com.rds.bacera.model.BaceraGeneTestingProjectModel;
import com.rds.bacera.service.BaceraGeneTestingProjectService;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.syspath.ConfigPath;
@Service("BaceraGeneTestingProjectService")
public class BaceraGeneTestingProjectServiceImpl implements BaceraGeneTestingProjectService {
    
	@Setter
	@Autowired
	private BaceraGeneTestingProjectMapper GeneMapper;
	@Autowired
	private RdsBaceraBoneAgeMapper rdsBaceraBoneAgeMapper;
	
    private static final String FILE_PATH = ConfigPath.getWebInfPath()
            + "spring" + File.separatorChar + "properties" + File.separatorChar
            + "config.properties";

    private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
            FILE_PATH, "finance_permit");
	@Override
	public Object queryAll(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryModel(Object params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryAllPage(Object params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", GeneMapper.queryAllCount(params));
		result.put("data", GeneMapper.queryAllPage(params));
		return result;
	}

	@Override
	public int queryAllCount(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateJunior(Object params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

@Override
public void exportGeneInfo(Map<String, Object> params,
		HttpServletResponse response) throws Exception {
	String filename = "基因检测";
	List<Object> list = GeneMapper.queryAll(params);
	if (FINANCE_PERMIT.contains(params.get("roleType").toString())
			|| FINANCE_PERMIT.contains(params.get("userCode").toString())) {
		Object[] titles = { "案例编号","样本编号","姓名",
				"性别", "生日","年龄","手机号码","医院","医生","归属人全称","检测套餐名称", "登记时间","参考价格","标准价格", "所付款项", "汇款时间", "优惠价格","到款时间","确认时间", "财务备注", "快递日期",
				"快递类型", "快递单号", "快递备注", "代理商", "检测项名称","备注"};
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {
			List<Object> objects = new ArrayList<Object>();
			BaceraGeneTestingProjectModel rdsGeneModel = (BaceraGeneTestingProjectModel) list
					.get(i);	
			objects.add(rdsGeneModel.getTest_number());
			objects.add(rdsGeneModel.getSample_number());
			objects.add(rdsGeneModel.getConsumer_name());
			objects.add("M".equals(rdsGeneModel.getConsumer_sex())?"男":"女");
			objects.add(StringUtils.dateToChineseTen(rdsGeneModel
					.getConsumer_birthday()));
			objects.add(rdsGeneModel.getAge());
			objects.add(rdsGeneModel.getConsumer_phone());
			objects.add(rdsGeneModel.getHospital());
			objects.add(rdsGeneModel.getDoctor());
			objects.add(rdsGeneModel.getCharge_standard_name());
			objects.add(rdsGeneModel.getTest_package_name());
			objects.add(StringUtils.dateToTen(rdsGeneModel
					.getAdd_time()));
			//参考价格
			objects.add((double)rdsGeneModel.getPrice()/100);
			// 标准价格
			objects.add(("".equals(rdsGeneModel.getReceivables()) || null == rdsGeneModel
					.getReceivables()) ? 0 : Float
					.parseFloat(rdsGeneModel.getReceivables()));
			// 实收款项
			objects.add(("".equals(rdsGeneModel.getPayment()) || null == rdsGeneModel
					.getPayment()) ? 0 : Float
					.parseFloat(rdsGeneModel.getPayment()));
			objects.add(StringUtils.dateToChineseTen(rdsGeneModel.getRemittanceDate()));
			objects.add(StringUtils.dateToChineseTen(rdsGeneModel.getConfirm_date()));
			objects.add(rdsGeneModel.getDiscountPrice());
			objects.add(rdsGeneModel.getParagraphtime());
			objects.add(rdsGeneModel.getRemarks());
			//快递日期
			objects.add("".equals(rdsGeneModel.getExpresstime())
					|| null == rdsGeneModel.getExpresstime() ? null
					: StringUtils.dateToChineseTen(rdsGeneModel
							.getExpresstime()));
			objects.add(rdsGeneModel.getExpresstype());
			objects.add(rdsGeneModel.getExpressnum());
			objects.add(rdsGeneModel.getExpressremark());
			objects.add(rdsGeneModel.getAgency_name());
			objects.add(rdsGeneModel.getTest_item_names());
			objects.add(rdsGeneModel.getRemark());
			bodys.add(objects);
		}
		ExportUtils.export(response, filename, titles, bodys, "生信基因检测");
	} else {
		Object[] titles = {"案例编号","样本编号", "姓名",
				"性别", "生日","年龄","手机号码","医院","医生","检测套餐名称", "登记时间","归属人全称","快递日期",
				"快递类型", "快递单号", "快递备注",  "代理商" ,"检测项名称"};
		List<List<Object>> bodys = new ArrayList<List<Object>>();
		for (int i = 0; i < list.size(); i++) {

			List<Object> objects = new ArrayList<Object>();
			BaceraGeneTestingProjectModel rdsGeneModel = (BaceraGeneTestingProjectModel) list
					.get(i);
			objects.add(rdsGeneModel.getTest_number());
			objects.add(rdsGeneModel.getSample_number());
			objects.add(rdsGeneModel.getConsumer_name());
			objects.add("M".equals(rdsGeneModel.getConsumer_sex())?"男":"女");
			objects.add(StringUtils.dateToChineseTen(rdsGeneModel
					.getConsumer_birthday()));
			objects.add(rdsGeneModel.getAge());
			objects.add(rdsGeneModel.getConsumer_phone());
			objects.add(rdsGeneModel.getHospital());
			objects.add(rdsGeneModel.getDoctor());
            objects.add(rdsGeneModel.getTest_package_name());
			objects.add(StringUtils.dateToTen(rdsGeneModel
					.getAdd_time()));
			objects.add(rdsGeneModel.getCharge_standard_name());
			//快递日期
			objects.add("".equals(rdsGeneModel.getExpresstime())
					|| null == rdsGeneModel.getExpresstime() ? null
					: StringUtils.dateToChineseTen(rdsGeneModel
							.getExpresstime()));
			objects.add(rdsGeneModel.getExpresstype());
			objects.add(rdsGeneModel.getExpressnum());
			objects.add(rdsGeneModel.getExpressremark());
			objects.add(rdsGeneModel.getAgency_name());
			objects.add(rdsGeneModel.getTest_item_names());
			objects.add(rdsGeneModel.getRemark());
			bodys.add(objects);
		
		}
		ExportUtils.export(response, filename, titles, bodys, "生信基因检测");
	}

}

//	}
	}
