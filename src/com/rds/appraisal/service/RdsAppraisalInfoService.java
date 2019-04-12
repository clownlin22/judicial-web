package com.rds.appraisal.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.rds.appraisal.model.RdsAppraisalAttachmentModel;
import com.rds.appraisal.model.RdsAppraisalStandardModel;

/**
 * 鉴定报告目录接口
 * 
 * @author yxb
 *
 */
public interface RdsAppraisalInfoService extends RdsAppraisalBaseService {
	
	/**
	 * 模版查询
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryTemplate(Object params) throws Exception;
	
	/**
	 * 插入模版
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertTemplate(Object params) throws Exception;
	
	/**
	 * 被鉴定人信息插入
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertInfo(Object params) throws Exception;
	
	/**
	 * 检案摘要插入
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertAbstract(Object params) throws Exception;
	
	/**
	 * 检验过程插入
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertProcess(Object params) throws Exception;
	
	/**
	 * 分析说明插入
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertAnalysis(Object params) throws Exception;
	
	/**
	 * 鉴定意见插入
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertAdvice(Object params) throws Exception;
 
	/**
	 * 附件上传
	 * 
	 * @param case_code
	 * @param files
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> upload(String case_id, String attachment_type,
			String attachment_name, String attachment_order,
			MultipartFile[] files) throws Exception;

	/**
	 * 根据case_id查询附件信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String queryAttachment(Object params) throws Exception;
	
	/**
	 * 插入鉴定类型
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertRelation(Object params) throws Exception;
	
	/**
	 * 根据条件查询鉴定类型
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Object> queryCaseType(Object params) throws Exception;
	
	/**
	 * 根据条件查询鉴定标准
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<RdsAppraisalStandardModel> queryStandard(Object params) throws Exception;
	
	/**
	 * 鉴定登记
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertBaseInfo(Object params) throws Exception;
	
	/**
	 * 删除案例对应的类型
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int deleteType(Object params) throws Exception;
	
	/**
	 * 更新案例登记信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateBaseInfo(Object params) throws Exception;
	
	/**
	 * 修改被鉴定人信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateIdentifyInfo(Object params) throws Exception;
	
	/**
	 * 更新登记信息状态
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateExamineBaseInfo(Object params) throws Exception;
	
	/**
	 * 查询历史记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryHistoryInfo(Object params) throws Exception;

	/**
	 * 更新检案摘要
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateAbstract(Object params) throws Exception;
	
	/**
	 * 更新鉴定意见
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateAdvice(Object params) throws Exception;
	
	/**
	 * 更新分析说明
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateAnalysis(Object params) throws Exception;
	
	/**
	 * 更新检验过程
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateProcess(Object params) throws Exception;
	
	/**
	 * 附件删除
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int deleteAttachment(Object params) throws Exception;
	

	/**
	 * 获取附件图片
	 * @param response
	 * @param filename
	 */
	public void getImg(HttpServletResponse response, String filename);
	
	/**
	 * 获取附件列表
	 * @param params
	 * @return
	 */
	public List<RdsAppraisalAttachmentModel> queryAttachmentList(Object params);
	
	public int queryBaseCount() throws Exception;
	/**
	 * 鉴定人插入
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int insertMechanism(Object params) throws Exception;

	public int insertCaseFee(Map<String, Object> params);
	
	/**
	 * 临床案例导出
	 * @param start_time
	 * @param end_time
	 */
	void exportInfo(String start_time, String end_time, HttpServletResponse response)  throws Exception ;

 }
