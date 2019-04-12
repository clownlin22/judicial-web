package com.rds.bacera.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.rds.bacera.model.RdsBaceraBoneAgeFeeModel;
import com.rds.bacera.model.RdsBaceraInvasivePhotoModel;
import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;
import com.rds.upc.model.RdsUpcUserModel;

/**
 * 无创产前-service
 * 
 * @author yxb
 *
 */
public interface RdsBaceraInvasivePreService extends RdsBaceraBaseService {
	public int queryNumExit(@SuppressWarnings("rawtypes") Map map);

	void exportInvasiveInfo(Map<String, Object> params,
			HttpServletResponse response) throws Exception;

	public boolean exsitCaseCode(Map<String, Object> params);

	public Object exsitOne(Object params) throws Exception;

	public Map<String, Object> saveCaseInfo(Map<String, Object> params,
			RdsUpcUserModel user) throws Exception;

	/**
	 * 无创亲子财务公式分页
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object queryInvasivePreFee(Object params) throws Exception;

	/**
	 * 保存财务信息
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveInvasivePreFee(Object params) throws Exception;

	/**
	 * 新增 保存并提交审核
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveVerify(Object params) throws Exception;

	/**
	 * 更新财务信息
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateInvasivePreFee(Object params) throws Exception;

	public int updateVerify(Map<String, Object> params) throws Exception;

	public int queryInvasivePreFeeCount(Object params) throws Exception;

	public int deleteInvasivePreFee(Object params) throws Exception;

	public int updateVerifyAll(Map<String, Object> params) throws Exception;

	public int update(Map<String, Object> params) throws Exception;

	public RdsBaceraBoneAgeFeeModel queryInvasivePreFeeByRec(Object params)
			throws Exception;

	int delete(Map<String, Object> params) throws Exception;

	public int yesInvasivePreVerify(Map<String, Object> params);

	public int noInvasivePreVerify(Map<String, Object> params);
	
	public int updateEmailFlag(Map<String, Object> params);
	
	List<RdsBaceraInvasivePhotoModel> getAttachMent(
			Map<String, Object> params);
	void getImg(HttpServletResponse response, String filename);
	
	Map<String, Object> turnImg(Map<String, Object> params);
	
	Boolean upload(String id, MultipartFile files,String userid)
			throws Exception;
	
	boolean deleteFile(Map<String, Object> params);
	
	List<RdsBaceraInvasivePhotoModel> queryAllFile(Map<String, Object> params);
}
