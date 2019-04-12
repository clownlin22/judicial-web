package com.rds.bacera.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("RdsBaceraInvasiveDNAMapper")
public interface RdsBaceraInvasiveDNAMapper extends RdsBaceraBaseMapper {
	@SuppressWarnings("rawtypes")
	public int queryNumExit(Map map);
	/**
	 * 分页查询无创DNA应收金额
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Object> queryInvasiveNDAFee(Object params) throws Exception;
	
	/**
	 * 查询无创DNA应收金额总数
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int queryInvasiveNDAFeeCount(Object params) throws Exception;
	
	/**
	 * 保存无创DNA财务信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int saveInvasiveNDAFee(Object params) throws Exception;
	/**
	 * 更新无创DNA财务信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateInvasiveNDAFee(Object params) throws Exception;
	
	/**
	 * 删除无创DNA财务配置
	 */
	public int deleteInvasiveNDAFee(Object params) throws Exception;
	
	/**
	 * 根据area_id查询配置金额
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Object> queryInvasiveNDAFeeByRec(Object params) throws Exception;
}
