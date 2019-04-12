package com.rds.appraisal.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rds.appraisal.model.RdsAppraisalAttachmentModel;
import com.rds.appraisal.model.RdsAppraisalStandardModel;

@Component("RdsAppraisalInfoMapper")
public interface RdsAppraisalInfoMapper extends RdsAppraisalBaseMapper{
	//模版查询
	public List<Object> queryTemplate(Object params) throws Exception;
	//模版插入
	public int insertTemplate(Object params) throws Exception;
	//被鉴定人信息插入
	public int insertInfo(Object params) throws Exception;
	//检案摘要插入
	public int insertAbstract(Object params) throws Exception;
	//检验过程插入
	public int insertProcess(Object params) throws Exception;
	//分析说明插入
	public int insertAnalysis(Object params) throws Exception;
	//鉴定意见插入
	public int insertAdvice(Object params) throws Exception;
	//鉴定人插入
	public int insertMechanism(Object params) throws Exception;
	//附件插入
	public int insertAttachment(RdsAppraisalAttachmentModel params) throws Exception;
	//插入鉴定类型
	public int insertRelation(Object params) throws Exception;
	//查询附件
	public List<RdsAppraisalAttachmentModel> queryAttachment(Object params) throws Exception;
	//查询类型
	public List<Object> queryCaseType(Object params) throws Exception;
	//查询标准
	public List<RdsAppraisalStandardModel> queryStandard(Object params) throws Exception;
	//删除案例对应的类型
	public int deleteType(Object params) throws Exception;
	//更新案例登记信息
	public int updateBaseInfo(Object params) throws Exception;
	//修改被鉴定人信息
	public int updateIdentifyInfo(Object params) throws Exception;
	//更新登记信息状态
	public int updateExamineBaseInfo(Object params) throws Exception;
	//更新检案摘要
	public int updateAbstract(Object params) throws Exception;
	//更新鉴定意见
	public int updateAdvice(Object params) throws Exception;
	//更新分析说明
	public int updateAnalysis(Object params) throws Exception;
	//更新检验过程
	public int updateProcess(Object params) throws Exception;
	//删除附件
	public int deleteAttachment(Object params) throws Exception;
	//查询基本信息总数
	public int queryBaseCount() throws Exception;
	//查询历史记录
	public Object queryHistoryInfo(Object params) throws Exception;
	public int insertCaseFee(Map<String, Object> params);
	
	public List<Map<String,Object>> queryExportInfo(Map<String, Object> params);
}
