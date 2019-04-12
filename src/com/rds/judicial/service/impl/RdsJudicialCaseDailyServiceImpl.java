package com.rds.judicial.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;
import com.rds.code.utils.uuid.UUIDUtil;
import com.rds.judicial.mapper.RdsJudicialCaseDailyMapper;
import com.rds.judicial.model.RdsJudicialCaseAllDateModel;
import com.rds.judicial.model.RdsJudicialCaseCountStatisticsModel;
import com.rds.judicial.model.RdsJudicialCaseDailyModel;
import com.rds.judicial.model.RdsJudicialCaseExceptionCountModel;
import com.rds.judicial.model.RdsJudicialCaseExceptionSumStatisticModel;
import com.rds.judicial.model.RdsJudicialKeyValueModel;
import com.rds.judicial.model.RdsJudicialPerformanceDesModel;
import com.rds.judicial.service.RdsJudicialCaseDailyService;
import com.rds.upc.model.RdsUpcUserModel;

@Service("RdsJudicialCaseDailyService")
public class RdsJudicialCaseDailyServiceImpl implements
		RdsJudicialCaseDailyService {

	@Autowired
	private RdsJudicialCaseDailyMapper dMapper;

	@Override
	public void generateCaseDaily() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("days", 1);
		params.put("key", "e2843cf08cb9433ebb0772bb615b0f7d");
		dMapper.insertCaseDaily(getCaseStatisticSum(params));
	}

	@Override
	public Map<String, Object> getInfo() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", dMapper.getInfo());
		return map;
	}

	@Override
	public Map<String, Object> getAllinfo(Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", dMapper.getAllInfo(params));
		map.put("count", dMapper.getAllInfoCount(params));
		return map;
	}

	@Override
	public Map<String, Object> getDaysStatistics(Map<String, Object> params) {
		RdsJudicialCaseExceptionSumStatisticModel sModel = null;
		List<RdsJudicialCaseExceptionSumStatisticModel> sList = new LinkedList<RdsJudicialCaseExceptionSumStatisticModel>();
		for (int i = 2; i < 11; i++) {
			sModel = new RdsJudicialCaseExceptionSumStatisticModel();
			sModel.setDays(i);
			sList.add(sModel);
		}

		// 已超期未快递的案例数和
		List<RdsJudicialCaseCountStatisticsModel> csnomailList = dMapper
				.getNo_mailByDays(params);
		int tempCount = 0;
		if (csnomailList.size() > 0) {
			for (int i = 0; i < sList.size(); i++) {
				for (RdsJudicialCaseCountStatisticsModel csTempModel : csnomailList) {
					if (csTempModel.getDays() > sList.get(i).getDays()) {
						tempCount += csTempModel.getCount();
					}
				}
				sList.get(i).setNo_mail(tempCount);
				sList.get(i).setNo_mail_all(tempCount);
				tempCount = 0;
			}
		}

		// 已超期数据未上传的案例数
		List<RdsJudicialCaseCountStatisticsModel> csList = dMapper
				.getNo_dataByDays(params);
		if (csList.size() > 0) {
			for (int i = 0; i < sList.size(); i++) {
				for (RdsJudicialCaseCountStatisticsModel csTempModel : csList) {
					if (csTempModel.getDays() > sList.get(i).getDays()) {
						tempCount += csTempModel.getCount();
					}
				}
				sList.get(i).setNo_data(tempCount);
				tempCount = 0;
			}
		}
//
//		// 已超期照片未上传的案例数
//		 csList = dMapper.getNo_photoByDays(params);
//		 if (csList.size() > 0) {
//		 for (int i = 0; i < sList.size(); i++) {
//		 for (RdsJudicialCaseCountStatisticsModel csTempModel : csList) {
//		 if (csTempModel.getDays() > sList.get(i).getDays()) {
//		 tempCount += csTempModel.getCount();
//		 }
//		 }
//		 sList.get(i).setNo_photo(tempCount);
//		 tempCount = 0;
//		 }
//		 }

		// 已超期未回款的案例数
		csList = dMapper.getNo_feeByDays(params);
		if (csList.size() > 0) {
			for (int i = 0; i < sList.size(); i++) {
				for (RdsJudicialCaseCountStatisticsModel csTempModel : csList) {
					if (csTempModel.getDays() > sList.get(i).getDays()) {
						tempCount += csTempModel.getCount();
					}
				}
				sList.get(i).setNo_fee(tempCount);
				tempCount = 0;
			}
		}

		// 已超期异常的案例数
		csList = dMapper.getCase_exceptionByDays(params);
		if (csList.size() > 0) {
			for (int i = 0; i < sList.size(); i++) {
				for (RdsJudicialCaseCountStatisticsModel csTempModel : csList) {
					if (csTempModel.getDays() > sList.get(i).getDays()) {
						tempCount += csTempModel.getCount();
					}
				}
				sList.get(i).setCase_exception(tempCount);
				tempCount = 0;
			}
		}

//		 //已超期数据上传照片未上传未回款的案例数
//		 csList = dMapper.getData_nophoto_nofeeByDays(params);
//		 if (csList.size() > 0) {
//		 for (int i = 0; i < sList.size(); i++) {
//		 for (RdsJudicialCaseCountStatisticsModel csTempModel : csList) {
//		 if (csTempModel.getDays() > sList.get(i).getDays()) {
//		 tempCount += csTempModel.getCount();
//		 }
//		 }
//		 sList.get(i).setData_nophoto_nofee(tempCount);
//		 tempCount = 0;
//		 }
//		 }
//		
//		 //已超期数据上传照片上传未回款的案例数
//		 csList = dMapper.getData_photo_nufee(params);
//		 if (csList.size() > 0) {
//		 for (int i = 0; i < sList.size(); i++) {
//		 for (RdsJudicialCaseCountStatisticsModel csTempModel : csList) {
//		 if (csTempModel.getDays() > sList.get(i).getDays()) {
//		 tempCount += csTempModel.getCount();
//		 }
//		 }
//		 sList.get(i).setData_photo_nofee(tempCount);
//		 tempCount = 0;
//		 }
//		 }
//		 //已超期数据上传照片未上传已回款的案例数
//		 csList = dMapper.getData_nophoto_feeByDays(params);
//		 if (csList.size() > 0) {
//		 for (int i = 0; i < sList.size(); i++) {
//		 for (RdsJudicialCaseCountStatisticsModel csTempModel : csList) {
//		 if (csTempModel.getDays() > sList.get(i).getDays()) {
//		 tempCount += csTempModel.getCount();
//		 }
//		 }
//		 sList.get(i).setData_nophoto_fee(tempCount);
//		 tempCount = 0;
//		 }
//		 }
//		 //已超期数据未上传照片已上传未回款的案例数
//		 csList = dMapper.getNodata_photo_nofeeByDays(params);
//		 if (csList.size() > 0) {
//		 for (int i = 0; i < sList.size(); i++) {
//		 for (RdsJudicialCaseCountStatisticsModel csTempModel : csList) {
//		 if (csTempModel.getDays() > sList.get(i).getDays()) {
//		 tempCount += csTempModel.getCount();
//		 }
//		 }
//		 sList.get(i).setNodata_photo_nofee(tempCount);
//		 tempCount = 0;
//		 }
//		 }
//		 //已超期数据未上传照片已上传已回款的案例数
//		 csList = dMapper.getNodata_photo_feeByDays(params);
//		 if (csList.size() > 0) {
//		 for (int i = 0; i < sList.size(); i++) {
//		 for (RdsJudicialCaseCountStatisticsModel csTempModel : csList) {
//		 if (csTempModel.getDays() > sList.get(i).getDays()) {
//		 tempCount += csTempModel.getCount();
//		 }
//		 }
//		 sList.get(i).setNodata_photo_fee(tempCount);
//		 tempCount = 0;
//		 }
//		 }
//		 //已超期数据未上传照片未上传已回款的案例数
//		 csList = dMapper.getNodata_nophoto_feeByDays(params);
//		 if (csList.size() > 0) {
//		 for (int i = 0; i < sList.size(); i++) {
//		 for (RdsJudicialCaseCountStatisticsModel csTempModel : csList) {
//		 if (csTempModel.getDays() > sList.get(i).getDays()) {
//		 tempCount += csTempModel.getCount();
//		 }
//		 }
//		 sList.get(i).setNodata_nophoto_fee(tempCount);
//		 tempCount = 0;
//		 }
//		 }
//		 //已超期数据未上传照片未上传未回款的案例数
//		 csList = dMapper.getNodata_nophoto_nofeeByDays(params);
//		 if (csList.size() > 0) {
//		 for (int i = 0; i < sList.size(); i++) {
//		 for (RdsJudicialCaseCountStatisticsModel csTempModel : csList) {
//		 if (csTempModel.getDays() > sList.get(i).getDays()) {
//		 tempCount += csTempModel.getCount();
//		 }
//		 }
//		 sList.get(i).setNodata_nophoto_nofee(tempCount);
//		 tempCount = 0;
//		 }
//		 }

		// 已超期其他异常案例数
		csList = dMapper.getOther_exceptionByDays(params);
		if (csList.size() > 0) {
			for (int i = 0; i < sList.size(); i++) {
				for (RdsJudicialCaseCountStatisticsModel csTempModel : csList) {
					if (csTempModel.getDays() > sList.get(i).getDays()) {
						tempCount += csTempModel.getCount();
					}
				}
				sList.get(i).setOther_exception(tempCount);
				tempCount = 0;
			}
		}

		if (csnomailList.size() > 0) {

			for (RdsJudicialCaseCountStatisticsModel csTempModel : csnomailList) {
				tempCount += csTempModel.getCount();
			}
			RdsJudicialCaseExceptionSumStatisticModel ssModel = new RdsJudicialCaseExceptionSumStatisticModel();
			ssModel.setNo_mail_all(tempCount);
			ssModel.setDays(0);
			sList.add(0, ssModel);
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", sList);
		return result;
	}

	@Override
	public Map<String, Object> getNowinfo(Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<RdsJudicialCaseDailyModel> clist = new ArrayList<>();
		params.put("days", 0);
		clist.add(getCaseStatisticSum(params));
		map.put("data", clist);
		return map;
	}

	/**
	 *
	 * @param days
	 *            截至日期往前 推 days 天的数据
	 * @return
	 */
	private RdsJudicialCaseDailyModel getCaseStatisticSum(Map<String, Object> params) {
		RdsJudicialCaseDailyModel dModel = new RdsJudicialCaseDailyModel();
		dModel.setDaily_id(UUIDUtil.getUUID());
		// 本日样本登记案例
		dModel.setCase_sum(dMapper.getCase_sum(params));
		// 本日受理案例
		dModel.setCase_accept(dMapper.getCase_accept(params));
		
		if(params.get("key").toString().equals("e2843cf08cb9433ebb0772bb615b0f7d")){
			// 本日样本接收数量
			dModel.setSample_receive(dMapper.getSample_receive(params));
			// 本日样本交接数量
			dModel.setSample_relay(dMapper.getSample_relay(params));
			// 本日送审案例数量
			dModel.setCase_confirm(dMapper.getCase_confirm(params));
			// 本日样本审核案例数
			dModel.setSample_verify(dMapper.getSample_verify(params));
			// 本日确认回款案例数
			dModel.setCase_comfirm_fee_sum(dMapper.getCase_comfirm_fee_sum(params));
			// 本日异常案例数
			dModel.setCase_exception_sum(dMapper.getCase_exception_sum(params));
		}
	
		
		// 本日数据上传案例
		dModel.setCase_data_sum(dMapper.getCase_data_sum(params));
		// 本日上传昨日登记案例数
		dModel.setData_today_register_yes(dMapper
				.getDate_today_register_yes(params));
		// 本日打印案例
		dModel.setCase_print_sum(dMapper.getCase_print_sum(params));
		// 本日打印昨日上传案例数
		dModel.setPrint_today_data_yes(dMapper.getPrint_today_data_yes(params));
		// 本日上传打印案例
		dModel.setPrint_today_data_tod(dMapper.getPrint_today_data_tod(params));
	
		// 本日确认昨日登记案例数
		dModel.setFee_today_register_yes(dMapper
				.getFee_today_register_yes(params));
		// 本日确认本日登记案例数
		dModel.setFee_today_register_tod(dMapper
				.getFee_today_register_tod(params));
		// 本日快递案例
		dModel.setCase_mail_sum(dMapper.getCase_mail_sum(params));
		// 本日快递昨日上传案例数
		dModel.setMail_today_data_yes(dMapper.getMail_today_data_yes(params));
		// 本日快递本日上传案例数
		dModel.setMail_today_data_tod(dMapper.getMail_today_data_tod(params));
		
		// 日期
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, -(Integer.parseInt(params.get("days").toString())));// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果

		dModel.setTask_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(date));
		dModel.setRemark("");
		return dModel;
	}

	@Override
	public Map<String, Object> getPerformance(Map<String, Object> params) {
		params.put("case_date", params.get("performance_date").toString()
				.substring(0, 7));
		List<RdsJudicialCaseAllDateModel> cdlist = dMapper
				.getCaseAllDate1(params);
		List<RdsJudicialCaseExceptionCountModel> cecList = dMapper
				.getCaseExceptionCount(params);
		RdsJudicialPerformanceDesModel pdModel = new RdsJudicialPerformanceDesModel(
				"本月登记案例数", dMapper.getCaseAcceptThisMonth(params));
		RdsJudicialPerformanceDesModel pdModel1 = new RdsJudicialPerformanceDesModel(
				"按时完成案例数(无论有无异常)", 0);
		RdsJudicialPerformanceDesModel pdModel2 = new RdsJudicialPerformanceDesModel(
				"正常送审无异常超期1天完成案例数", 0);
		RdsJudicialPerformanceDesModel pdModel3 = new RdsJudicialPerformanceDesModel(
				"正常送审无异常超期一天以上案例", 0);

		// RdsJudicialPerformanceDesModel pdModel6 = new
		// RdsJudicialPerformanceDesModel(
		// "异常解除案例数(按时)", 0);
		// RdsJudicialPerformanceDesModel pdModel7 = new
		// RdsJudicialPerformanceDesModel(
		// "异常解除案例数(超期一天)", 0);
		// RdsJudicialPerformanceDesModel pdModel8 = new
		// RdsJudicialPerformanceDesModel(
		// "异常解除案例数(超期一天以上)", 0);
		
		for (RdsJudicialCaseAllDateModel dModel : cdlist) {
			if (dateCompare(dModel.getMail_time(), dModel.getCompare_date(), 4) == 1) {
				pdModel3.setCount(pdModel3.getCount() + 1);
			} else {
				if (dateInWeek(dModel.getCompare_date()) == 1) {
					if (dateInWeek(dModel.getMail_time()) < 3) {
						pdModel1.setCount(pdModel1.getCount() + 1);
						
					} else if (dateInWeek(dModel.getMail_time()) == 3) {
						pdModel2.setCount(pdModel2.getCount() + 1);
						
					} else {
						pdModel3.setCount(pdModel3.getCount() + 1);
						
					}
				} else if (dateInWeek(dModel.getCompare_date()) == 2) {
					if (dateInWeek(dModel.getMail_time()) < 4) {
						pdModel1.setCount(pdModel1.getCount() + 1);
						
					} else if (dateInWeek(dModel.getMail_time()) == 4) {
						pdModel2.setCount(pdModel2.getCount() + 1);
						
					} else {
						pdModel3.setCount(pdModel3.getCount() + 1);
						
					}
				} else if (dateInWeek(dModel.getCompare_date()) == 3) {
					if (dateInWeek(dModel.getMail_time()) < 5) {
						pdModel1.setCount(pdModel1.getCount() + 1);
						
					} else if (dateInWeek(dModel.getMail_time()) >= 5) {
						pdModel2.setCount(pdModel2.getCount() + 1);
						
					}
				} else if (dateInWeek(dModel.getCompare_date()) == 4) {
					if (dateInWeek(dModel.getMail_time()) >= 4) {
						pdModel1.setCount(pdModel1.getCount() + 1);
						
					} else {
						pdModel2.setCount(pdModel2.getCount() + 1);
						
					}
				} else if (dateInWeek(dModel.getCompare_date()) == 5) {
					int temp = dateInWeek(dModel.getMail_time());
					if (temp >= 5 || temp == 1) {
						pdModel1.setCount(pdModel1.getCount() + 1);
						
					} else {
						pdModel2.setCount(pdModel2.getCount() + 1);
						
					}
				} else if (dateInWeek(dModel.getCompare_date()) == 6) {
					int temp = dateInWeek(dModel.getMail_time());
					if (temp > 5 || temp == 1) {
						pdModel1.setCount(pdModel1.getCount() + 1);
						
					} else if (temp == 2) {
						pdModel2.setCount(pdModel2.getCount() + 1);
						
					} else {
						pdModel3.setCount(pdModel3.getCount() + 1);
						
					}
				} else if (dateInWeek(dModel.getCompare_date()) == 7) {
					int temp = dateInWeek(dModel.getMail_time());
					if (temp < 2 || temp == 7) {
						pdModel1.setCount(pdModel1.getCount() + 1);
						
					} else if (temp == 2) {
						pdModel2.setCount(pdModel2.getCount() + 1);
						
					} else {
						pdModel3.setCount(pdModel3.getCount() + 1);
						
					}
				}
			}
		}
		
		List<RdsJudicialPerformanceDesModel> pdList = new LinkedList<RdsJudicialPerformanceDesModel>();
		pdList.add(pdModel);
		pdList.add(pdModel1);
		pdList.add(pdModel2);
		pdList.add(pdModel3);
		// pdList.add(pdModel6);
		// pdList.add(pdModel7);
		// pdList.add(pdModel8);
		params.clear();
		params.put("date", pdList);
		return params;
	}

	/**
	 * 两个日期相比差days 天 1大于days天 0等于days天 -1小于days天
	 * 
	 * @param firstdate
	 * @param seconddate
	 * @param days
	 * @return
	 */
	private int dateCompare(Date firstdate, Date seconddate, int days) {
		Calendar firstCalendar = Calendar.getInstance();
		firstCalendar.setTime(firstdate);

		Calendar secondCalendar = Calendar.getInstance();
		secondCalendar.setTime(seconddate);

		long fdl = firstCalendar.getTimeInMillis();
		long sdl = secondCalendar.getTimeInMillis();

		int cdays = (int) (fdl - sdl) / 86400000;// (3600*24*1000)
		if (cdays > days) {
			return 1;
		} else if (cdays == days) {
			return 0;
		} else {
			return -1;
		}
	}

	private int dateInWeek(Date date) {
		Calendar firstCalendar = Calendar.getInstance();
		firstCalendar.setTime(date);
		if (firstCalendar.get(Calendar.DAY_OF_WEEK) != 1) {
			return firstCalendar.get(Calendar.DAY_OF_WEEK) - 1;
		} else {
			return 7;
		}
	}

	@Override
	public List<RdsJudicialKeyValueModel> getCompany(HttpSession session) {
		Map<String, Object> params = new HashMap<String, Object>();
		RdsUpcUserModel user = null;
		if (session.getAttribute("user") != null) {
			user = (RdsUpcUserModel) session
					.getAttribute("user");
			params.put("userid", user.getUserid());
			params.put("company", user.getCompanyid());
		}
		return dMapper.getCompany(params);
	}
}
