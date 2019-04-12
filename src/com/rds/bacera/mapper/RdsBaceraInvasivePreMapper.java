package com.rds.bacera.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rds.bacera.model.RdsBaceraInvasivePhotoModel;
import com.rds.children.model.RdsChildrenCasePhotoModel;
import com.rds.judicial.model.RdsJudicialCaseAttachmentModel;
import com.rds.upc.model.RdsUpcUserModel;

@Component("RdsBaceraInvasivePreMapper")
public interface RdsBaceraInvasivePreMapper extends RdsBaceraBaseMapper {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
	int exsitCaseCode(Map<String, Object> params);
	
	Object exsitOne(Object params);
	/**
	 * 分页查询无创产前应收金额
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Object> queryInvasivePreFee(Object params) throws Exception;
	
	/**
	 * 查询无创产前应收金额总数
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int queryInvasivePreFeeCount(Object params) throws Exception;
	
	/**
	 * 保存无创产前财务信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveInvasivePreFee(Object params) throws Exception;
	/**
	 * 保存并提交审核
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveVerify(Object params) throws Exception;
	
	/**
	 * 更新无创产前财务信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateInvasivePreFee(Object params) throws Exception;
	/**
	 * 提交审核
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateVerify(Map<String, Object>params )throws Exception;
	
	/**
	 * 审核通过
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int verify(Map<String, Object>params )throws Exception;
	/**
	 * 审核不通过
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int noVerify(Map<String, Object>params )throws Exception;
	
	/**
	 * 删除无创产前财务配置
	 */
	public int deleteInvasivePreFee(Object params) throws Exception;
	
	/**
	 * 根据area_id查询配置金额
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Object> queryInvasivePreFeeByRec(Object params) throws Exception;
	
	int updateVerifyState(String id);
	
    int downVerifyState (Map<String,Object> params);
    
    int mailAttachment(Map<String,Object> params);
    
    int onMailOver(Map<String,Object> params);
    
    int onFile(Map<String,Object> params);
    
    int updateEmailFlag(Map<String, Object> params);
    
    int insertHeadPhoto(RdsChildrenCasePhotoModel pmodel);
    
    public List<Object> queryAllPageS(Object params) throws Exception;
	public int insertHeadPhoto(RdsBaceraInvasivePhotoModel pmodel);
	
	public List<RdsBaceraInvasivePhotoModel> getAttachMent(
			Map<String, Object> params);
	
	public int deletePhoto(Map<String, Object> params) throws Exception;
	
	int getPhoto(Map<String, Object> params) throws Exception;
}
