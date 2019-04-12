package com.rds.statistics.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rds.bacera.model.RdsBaceraInvasivePreModel;
import com.rds.code.utils.ExportUtils;
import com.rds.code.utils.PropertiesUtils;
import com.rds.code.utils.StringUtils;
import com.rds.code.utils.syspath.ConfigPath;
import com.rds.statistics.mapper.RdsRemittanceConsumptionTimeMapper;
import com.rds.statistics.model.RdsRemittanceConsumptionTimeModel;
import com.rds.statistics.service.RdsRemittanceConsumptionTimeService;
@Service("RdsRemittanceConsumptionTimeService")
public class RdsRemittanceConsumptionTimeServiceImpl implements RdsRemittanceConsumptionTimeService {
    
	private static final String FILE_PATH = ConfigPath.getWebInfPath()
			+ "spring" + File.separatorChar + "properties" + File.separatorChar
			+ "config.properties";
	private static final String FINANCE_PERMIT = PropertiesUtils.readValue(
			FILE_PATH, "finance_permit");
	@Setter
	@Autowired
	private  RdsRemittanceConsumptionTimeMapper rdsRCTM;
	
	@Override
	public Map<String, Object> queryConsumptionTimeAll(Map<String, Object> params) {
		List<RdsRemittanceConsumptionTimeModel> list = rdsRCTM.queryConsumptionTimeAll(params);
		int count = rdsRCTM.queryConsumptionTimeAllCount(params);
		Map<String, Object> map=new HashMap<String, Object>();

		map.put("data", list);
		map.put("total", count);
		return map;
	}
	@Override
	public void export(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String filename = "财务登记至确认耗时";
		List<Object> list = rdsRCTM.queryConsumptionTime(params);

			Object[] titles = { "案例编号", "汇款单号", "登记人", "登记时间", "确认人", "确认时间",
					"登记至确认耗时",  "受理时间", "案例归属人","确认状态", "案例状态" };
			List<List<Object>> bodys = new ArrayList<List<Object>>();
			for (int i = 0; i < list.size(); i++) {
				List<Object> objects = new ArrayList<Object>();
				RdsRemittanceConsumptionTimeModel rdsRCTM = (RdsRemittanceConsumptionTimeModel) list
						.get(i);
				objects.add(rdsRCTM.getCase_code());
				objects.add(rdsRCTM.getRemittance_num());
				objects.add(rdsRCTM.getCreate_per());
				objects.add(rdsRCTM.getCreate_date());
				objects.add(rdsRCTM.getConfirm_per());
				objects.add(rdsRCTM.getConfirm_date());
				int time_spent=rdsRCTM.getTime_spent();
       		     String result="";
						int days = time_spent /86400;
						time_spent = time_spent - days *86400;
						int hours = time_spent /3600;
						time_spent = time_spent - hours *3600;
						int min = time_spent /60;
						time_spent = time_spent - min *60;
						int second = time_spent;

						result += days == 0 ? "" : (days + "天");
						result += hours == 0 ? "" : (hours + "小时");
						result += min == 0 ? "" : (min + "分钟");
						result += second + "秒";
					  if("0秒".equals(result)){
						  result=null;
					  }
					
				objects.add(result);
				objects.add(rdsRCTM.getDate());
				objects.add(rdsRCTM.getCase_receiver());
				int confirm_state =rdsRCTM.getConfirm_state();
				String confirm="";
				if(-1==confirm_state){
					confirm="未确认汇款";
				}
				else if(1==confirm_state){
					confirm="确认汇款通过";
				}
				else if(2==confirm_state){
					confirm="确认汇款不通过";
				}
				else{
					confirm="";
				}
				objects.add(confirm);
				int type=rdsRCTM.getCase_state();
				String Type="";
				if(0==type){Type="正常";}
				else if(1==type){Type="为先出报告后付款";}
				else if(2==type){Type="为免单";}
				else if(3==type){Type="优惠";}
				else if(4==type){Type="月结申请";}
				else if(5==type){Type="二次采样";}
				else if(6==type){Type="补样";}
				else {Type="";}
				objects.add(Type);
				bodys.add(objects);
			}
			ExportUtils.export(response, filename, titles, bodys, "财务登记至确认耗时");
		} 

	
	


}
