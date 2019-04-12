package com.rds.children.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.rds.children.model.RdsChildrenCasePhotoModel;
import com.rds.children.model.RdsChildrenCustodyInfoModel;
import com.rds.children.model.RdsChildrenGatherInfoModel;
import com.rds.children.model.RdsChildrenResponse;

public interface RdsChildrenRegisterService {

	RdsChildrenResponse getCaseInfo(Map<String, Object> params);

	List<RdsChildrenCustodyInfoModel> getCustodyInfo(Map<String, Object> params);

	RdsChildrenGatherInfoModel getGatherInfo(Map<String, Object> params);

	boolean deleteCaseInfo(Map<String, Object> params);

	Map<String, Object> saveCaseInfo(Map<String, Object> params);

	boolean exsitCaseCode(Map<String, Object> params);
	
	boolean exsitSampleCode(Map<String, Object> params);

	Map<String, Object> updateCaseInfo(Map<String, Object> params);

	Map<String, Object> getTariff();

	Map<String, Object> photoUpload(RdsChildrenCasePhotoModel pmodel,
			MultipartFile[] photo) throws IOException;

	void exportInfo(String case_code, String starttime, String endtime,
			String child_name, Integer is_delete, HttpServletResponse response);

	/**
	 * 案例状态更新
	 * 
	 * @param params
	 * @return
	 */
	boolean updateCaseState(Map<String, Object> params);

	/**
	 * 案例照片处理查询
	 * 
	 * @param params
	 * @return
	 */
	List<RdsChildrenCasePhotoModel> queryCasePhoto(Map<String, Object> params);

	/**
	 * 获取图片
	 * 
	 * @param response
	 * @param filename
	 */
	public void getImg(HttpServletResponse response, String filename);

	/**
	 * 案例审核通过
	 * 
	 * @param params
	 * @return
	 */
	public boolean yesVerify(Map<String, Object> params) throws Exception;

	/**
	 * 案例照片下载
	 * 
	 * @param response
	 * @param filename
	 */
	void download(HttpServletResponse response, String case_id,String case_code);
	

	/**
	 * 案例照片下载
	 * 
	 * @param response
	 * @param filename
	 */
	void downloadPhoto(HttpServletResponse response, String photo_id);
	
	/**
	 * 案例word下载
	 * 
	 * @param response
	 * @param filename
	 */
	void downWord(HttpServletResponse response, String case_id,String case_code,String file_name)  throws Exception;
	

	public RdsChildrenResponse queryPageCasePhoto(Map<String, Object> params);
}
