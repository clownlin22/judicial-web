package com.rds.judicial.mapper;

import java.util.List;
import java.util.Map;

import com.rds.judicial.model.RdsJudicialCaseAllDateModel;
import com.rds.judicial.model.RdsJudicialCaseCountStatisticsModel;
import com.rds.judicial.model.RdsJudicialCaseDailyModel;
import com.rds.judicial.model.RdsJudicialCaseExceptionCountModel;
import com.rds.judicial.model.RdsJudicialKeyValueModel;

public interface RdsJudicialCaseDailyMapper {

	int getCase_sum(Map<String,Object> params);

	int getCase_data_sum(Map<String,Object> params);

	int getCase_datareg_sum(Map<String,Object> params);

	int getCase_print_sum(Map<String,Object> params);

	int getCase_printin2d_sum(Map<String,Object> params);

	int getCase_mail_sum(Map<String,Object> params);

	int getCase_comfirm_fee_sum(Map<String,Object> params);

	int getCase_mailin2d_sum(Map<String,Object> params);

	int getCase_mailfromnow_sum(Map<String,Object> params);

	int getCase_nomail_sum(Map<String,Object> params);

	int getCase_exception2d_nodata_sum(Map<String,Object> params);

	int getCase_exception_nofee_sum(Map<String,Object> params);

	void insertCaseDaily(RdsJudicialCaseDailyModel dModel);

	List<RdsJudicialCaseDailyModel> getInfo();

	List<RdsJudicialCaseDailyModel> getAllInfo(Map<String, Object>params);

	int getAllInfoCount(Map<String, Object> params);

	List<RdsJudicialCaseCountStatisticsModel> getNo_mailByDays(Map<String, Object> params);

	List<RdsJudicialCaseCountStatisticsModel> getNo_dataByDays(Map<String, Object> params);

	List<RdsJudicialCaseCountStatisticsModel> getNo_photoByDays(Map<String, Object> params);

	List<RdsJudicialCaseCountStatisticsModel> getOther_exceptionByDays(Map<String, Object> params);

	int getCase_accept(Map<String,Object> params);

	int getSample_receive(Map<String,Object> params);

	int getSample_relay(Map<String,Object> params);

	int getCase_confirm(Map<String,Object> params);

	int getSample_verify(Map<String,Object> params);

	int getDate_today_register_yes(Map<String,Object> params);

	int getPrint_today_data_yes(Map<String,Object> params);

	int getPrint_today_data_tod(Map<String,Object> params);

	int getFee_today_register_yes(Map<String,Object> params);

	int getFee_today_register_tod(Map<String,Object> params);

	int getMail_today_data_yes(Map<String,Object> params);

	int getMail_today_data_tod(Map<String,Object> params);

	int getCase_exception_sum(Map<String,Object> params);

	List<RdsJudicialCaseCountStatisticsModel> getNo_feeByDays(Map<String, Object> params);

	List<RdsJudicialCaseCountStatisticsModel> getCase_exceptionByDays(Map<String, Object> params);

	List<RdsJudicialCaseCountStatisticsModel> getData_nophoto_nofeeByDays(Map<String, Object> params);

	List<RdsJudicialCaseCountStatisticsModel> getData_photo_nufee(Map<String, Object> params);

	List<RdsJudicialCaseCountStatisticsModel> getData_nophoto_feeByDays(Map<String, Object> params);

	List<RdsJudicialCaseCountStatisticsModel> getNodata_photo_nofeeByDays(Map<String, Object> params);

	List<RdsJudicialCaseCountStatisticsModel> getNodata_photo_feeByDays(Map<String, Object> params);

	List<RdsJudicialCaseCountStatisticsModel> getNodata_nophoto_feeByDays(Map<String, Object> params);

	List<RdsJudicialCaseCountStatisticsModel> getNodata_nophoto_nofeeByDays(Map<String, Object> params);

	List<RdsJudicialCaseAllDateModel> getCaseAllDate(Map<String, Object> params);

	List<RdsJudicialCaseExceptionCountModel> getCaseExceptionCount(
			Map<String, Object> params);

	int getCaseAcceptThisMonth(Map<String, Object> params);
	
	List<RdsJudicialCaseAllDateModel> getCaseAllDate1(Map<String, Object> params);

	List<RdsJudicialKeyValueModel> getCompany(Map<String, Object> params);

}
