package com.rds.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.service.RdsJudicialCaseDailyService;
import com.rds.judicial.service.RdsJudicialFinanceService;
import com.rds.statistics.mapper.RdsAllCaseInfoMapper;
import com.rds.statistics.mapper.RdsExperimentalDataMapper;
import com.rds.statistics.mapper.RdsFinanceConfigMapper;
import com.rds.statistics.service.RdsFinanceConfigService;
import com.rds.statistics.service.RdsSampleStatisticsService;

@Component
public class RdsJudicialFinanceDailyTask {

	@Setter
	@Autowired
	private RdsJudicialFinanceService fService;

	@Autowired
	private RdsJudicialCaseDailyService dService;
	
	@Autowired
	private RdsAllCaseInfoMapper rdsAllCaseInfoMapper;
	
	@Autowired
	private RdsExperimentalDataMapper rdsExperimentalDataMapper;
	
	@Autowired
	private RdsSampleStatisticsService rdsSampleStatisticsService;
	
	@Autowired
	private RdsFinanceConfigService rdsFinanceConfigService;
	
	@Autowired
	private RdsFinanceConfigMapper rdsFinanceConfigMapper;

	// 每天早上2点执行
	// 日报生成
//	@Scheduled(cron = "0 50 23 * * ? ")
	// @Scheduled(cron="10 55 * * * ? ")
//	 @Scheduled(cron="0/60 * *  * * ? ")
	/**
	 * 亲子鉴定日报生成
	 */
	@Scheduled(cron = "0 0 2 * * ? ")
	public void generateFinanceDaily() {
		// System.out.println(123);
		fService.generateFinanceDaily();
	}
	
	/**
	 * 无创产前日报生成
	 */
//	@Scheduled(cron="0/60 * *  * * ? ")
	@Scheduled(cron = "0 30 2 * * ? ")
	public void inversiveFinanceDaily() {
		// System.out.println(123);
		fService.inversiveFinanceDaily();
	}
	
	/**
	 * 儿童基因库日报生成
	 */
	@Scheduled(cron = "0 0 4 * * ? ")
//	 @Scheduled(cron="0/60 * *  * * ? ")
	public void childrenFinanceDaily() {
		// System.out.println(123);
		fService.childrenFinanceDaily();;
	}

	// 每天早上1点执行，月报生成，请鉴定数据生成
	// @Scheduled(cron="10 3 * * * ? ")
	// @Scheduled(cron="0 0 1 5 * ? ")
	@Scheduled(cron = "0 0 1 * * ? ")
//	 @Scheduled(cron="0/60 * *  * * ? ")
	public void generateFinanceMonthly() {
		// System.out.println(123);
		fService.generateFinanceMonthly();
		rdsAllCaseInfoMapper.callCollect();
	}
	
	/**
	 * 亲子鉴定导出数据生成
	 */
//	@Scheduled(cron="0/60 * *  * * ? ")
	@Scheduled(cron = "0 0 3 * * ? ")
	public void caseExportInof() {
		rdsAllCaseInfoMapper.callStat_finance();
		rdsAllCaseInfoMapper.callExportCaseInof();
	}

	@Scheduled(cron = "0 0 1  * * ? ")
	public void generateCaseDaily() {
		dService.generateCaseDaily();
	}
	
	@Scheduled(cron = "0 0 4 * * ? ")
	public void generateExperimentalData() {
		rdsExperimentalDataMapper.search_data();
	}
	
	/**
	 * 样本统计数据生成
	 */
//  @Scheduled(cron="0 0 1 5 * ? ")
	@Scheduled(cron = "0 30 1 1 * ? ")
//	@Scheduled(cron="0/60 * *  * * ? ")
	public void sample_statistisc() {
		rdsSampleStatisticsService.sampleStatistisc();
	}
	
	/**
	 * 阿米巴数据生成
	 */
	@Scheduled(cron = "0 30 0 * * ? ")
//	@Scheduled(cron="0/60 * *  * * ? ")
	public void callCaseDetailInofy() {
		Map<String, Object> map = new HashMap<String, Object>();
		Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, -1);    //得到前一天
        String  yestedayDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		rdsAllCaseInfoMapper.callCaseDetailInfo(yestedayDate);
		//内部结算价计算
		map.put("confirm_date", yestedayDate);
		try{
			rdsFinanceConfigService.financeCaseDetail(map);
		}catch(Exception e){
			map.put("id", UUIDUtil.getUUID());
			map.put("logs", yestedayDate+":该日期阿米巴生成有误！！！");
			rdsFinanceConfigMapper.insertFinanceCreateLog(map);
			e.printStackTrace();
		}
	}
}
